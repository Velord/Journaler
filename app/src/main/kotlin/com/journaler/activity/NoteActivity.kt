package com.journaler.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationListener
import android.net.ConnectivityManager
import android.os.*
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.database.Db
import com.journaler.execution.TaskExecutor
import com.journaler.model.Note
import com.journaler.location.LocationProvider
import com.journaler.model.MODE
import com.journaler.service.DatabaseService
import kotlinx.android.synthetic.main.activity_note.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class NoteActivity : ItemActivity() {
    override val tag: String = "NoteActivity"
    override fun getLayout(): Int = R.layout.activity_note

    private var note: Note? = null
    private var location : Location? = null
    private var handler: Handler? = null

    private val threadPoolExecutor = ThreadPoolExecutor(
        3,3,1, TimeUnit.SECONDS ,
        LinkedBlockingQueue<Runnable>()
    )
    private val executor = TaskExecutor.getInstance(1)
    private val locationListener = object : LocationListener{
        override fun onLocationChanged(l0: Location?) {
            l0?.let {
                LocationProvider.unsubscribe(this)
                location = l0
                val title = getNoteTitle()
                val content = getNoteContent()
                note = Note(title, content , l0)
                // Switching to intent service.
                val dbIntent = Intent(this@NoteActivity,
                    DatabaseService::class.java)
                dbIntent.putExtra(DatabaseService.EXTRA_ENTRY , note)
                dbIntent.putExtra(DatabaseService.EXTRA_OPERATION,
                    MODE.CREATE.mode)
                startService(dbIntent)
                sendMessage(true)
            }
        }

        override fun onStatusChanged(provider: String?,
                                     status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}
    }
    private val textWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            updateNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                tryAsync(s.toString())
                locationListener
            }
        }
    }
    private val crudOperationListener = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val crudResultValue =
                        intent.getIntExtra(MODE.EXTRAS_KEY, 0)
                sendMessage(crudResultValue == 1 )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHandler()
        note_title.addTextChangedListener(textWatcher)
        note_content.addTextChangedListener(textWatcher)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(crudOperationListener, intentFilter)
    }

    override fun onDestroy() {
        unregisterReceiver(crudOperationListener)
        super.onDestroy()
    }

    private fun sendMessage(result: Boolean){
        Log.v(tag , "Crud operation result [$result]")
        val msg = handler?.obtainMessage()
        if (result)
            msg?.arg1 = 1
        else
            msg?.arg1 = 0
        handler?.sendMessage(msg)
    }

    private fun initHandler(){
        handler = object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message?) {
                msg?.let {
                    var color = R.color.vermilion
                    if (msg.arg1 > 0)
                        color = R.color.green
                    indicator.setBackgroundColor(
                        ContextCompat.getColor(
                            this@NoteActivity,
                            color))
                }
                super.handleMessage(msg)
            }
        }
    }

    private fun updateNote(){
        if (note == null){
            if ((!TextUtils.isEmpty(getNoteTitle()))
                and (!TextUtils.isEmpty(getNoteContent()))){
                LocationProvider.subscribe(locationListener)
            }
        } else{
            note?.title = getNoteTitle()
            note?.message = getNoteContent()
            //switching to intent service
            val dbIntent = Intent(this@NoteActivity ,
                DatabaseService::class.java)
            dbIntent.putExtra(DatabaseService.EXTRA_ENTRY , note)
            dbIntent.putExtra(DatabaseService.EXTRA_OPERATION ,
                MODE.EDIT.mode)
            startService(dbIntent)
            sendMessage(true)
        }
    }

    private fun getNoteContent(): String = note_content.text.toString()

    private fun getNoteTitle(): String = note_title.text.toString()

    private fun tryAsync(identifier: String){
        val tryAsync = TryAsync(identifier)
        tryAsync.executeOnExecutor(threadPoolExecutor)
    }

    inner class TryAsync(val identifier: String): AsyncTask<Unit, Int , Unit>(){
        private val tag  = "TryAsync"

        override fun onPreExecute() {
            Log.i(tag, "onPreExecute [$identifier]")
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Unit?) {
            Log.i(tag, "doInBackground [$identifier] [START]")
            Thread.sleep(5000)
            Log.i(tag , "doInBackground [$identifier] [END]")
            return Unit
        }

        override fun onCancelled() {
            Log.i(tag , "onCancelled [$identifier]")
            super.onCancelled()
        }

        override fun onProgressUpdate(vararg values: Int?) {
            val progress = values.first()
            progress?.let {
                Log.i(tag , "onProgressUpdate [$identifier] [$progress]")
            }
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: Unit?) {
            Log.i(tag , "OnPostExecute")
            super.onPostExecute(result)
        }
    }
}