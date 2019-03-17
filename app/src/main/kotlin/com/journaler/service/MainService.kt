package com.journaler.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.journaler.api.BackendServiceHeaderMap
import com.journaler.api.JournalerBackendService
import com.journaler.api.TokenManager
import com.journaler.api.UserLoginRequest
import com.journaler.database.Content
import com.journaler.execution.TaskExecutor
import com.journaler.model.Note
import com.journaler.model.TODO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Authenticates user synchronously,
 * then executes async calls for notes and TODOs fetching.
 * Pay attention on synchronously triggered call via execute()
method.
 * Its asynchronous equivalent is: enqueue().
 */
class MainService : Service() , IDataSynchronization {
    private val tag = "Main service"
    private val binder = getServiceBinder()
    private val executor = TaskExecutor.getInstance(1)

    override fun onCreate() {
        super.onCreate()
        Log.v(tag , "[ON CREATE]")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(tag , "[ON START COMMAND]")
        synchronize()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(tag , "[ON BIND]")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        val result = super.onUnbind(intent)
        Log.v(tag , "[ON UNBIND]")
        return result
    }

    override fun onDestroy() {
        synchronize()
        super.onDestroy()
        Log.v(tag , "ON DESTROY")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(tag , "[ON LOW MEMORY]")
    }

    override fun synchronize() {
        executor.execute{
            Log.i(tag , "Synchronizing data [START]")
            var headers = BackendServiceHeaderMap.obtain()
            val service = JournalerBackendService.obtain()
            val credentials =
                UserLoginRequest("username", "password")

            val tokenResponse = service
                .login(headers ,credentials)
                .execute()

            if (tokenResponse.isSuccessful){
                val token = tokenResponse.body()
                token?.let {
                    TokenManager.currentToken = token
                    headers = BackendServiceHeaderMap.obtain(true)
                    fetchNotes(service, headers)
                    fetchTodos(service, headers)
                }
            }
            Log.i(tag , "Synchronizing data [END]")
        }
    }
    /**
     * Fetches notes asynchronously.
     * Pay attention on enqueue() method
     */
    private fun fetchNotes(
        service: JournalerBackendService,
        headers: Map<String, String>
    ){
        service
            .getNotes(headers)
            .enqueue(
                object : Callback<List<Note>> {
                    override fun onResponse(
                        call: Call<List<Note>>?,
                        response: Response<List<Note>>?
                    ) {
                        response?.let {
                            if (response.isSuccessful) {
                                val notes = response.body()
                                notes?.let {
                                    Content.NOTE.insert(notes)
                                }
                            }
                        }
                    }

                    override fun onFailure(
                        call:
                        Call<List<Note>>?, t: Throwable?
                    ) {
                        Log.e(tag, "We couldn't fetch notes.")
                    }
                }
            )
    }

    private fun fetchTodos(
        service: JournalerBackendService,
        headers: Map<String, String>
    ) {
        service
            .getTODOs(headers)
            .enqueue(
                object : Callback<List<TODO>> {
                    override fun onResponse(
                        call: Call<List<TODO>>?, response:
                        Response<List<TODO>>?
                    ) {
                        response?.let {
                            if (response.isSuccessful) {
                                val todos = response.body()
                                todos?.let {
                                    Content.TODO.insert(todos)
                                }
                            }
                        }
                    }
                    override fun onFailure(call:
                                           Call<List<TODO>>?, t: Throwable?) {
                        Log.e(tag, "We couldn't fetch notes.")
                    }
                }
            )
    }

    private fun getServiceBinder(): MainServiceBinder =
            MainServiceBinder()

    inner class  MainServiceBinder: Binder(){
        fun getService():MainService = this@MainService
    }
}