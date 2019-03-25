package com.example.velord.masteringandroiddevelopmentwithkotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import android.support.test.InstrumentationRegistry
import android.util.Log
import com.journaler.database.Content
import com.journaler.model.Note
import com.journaler.service.MainService
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class MainServiceTest {
    private var ctx: Context? = null
    private val tag  = "Main service test"
    private var result:Boolean? = null
    private var serviceIntent: Intent? = null

    private val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.v(tag, "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.v(tag , "Service disconnected")
        }
    }

    @Before
    fun beforeMainServiceTest(){
        Log.v(tag , "Starting")
        ctx = InstrumentationRegistry.getInstrumentation().context
        serviceIntent = Intent(ctx , MainService::class.java)
        ctx?.startService(serviceIntent)
        result = ctx?.bindService(
            serviceIntent,
            serviceConnection,
            android.content.Context.BIND_AUTO_CREATE
        )
    }

    @Test
    fun testMainService(){
        Log.v(tag, "Running")
        assertNotNull(ctx)
        assert(result !=  null)
    }

    @Test
    fun noteInsertTest(){
        val note  = Note(
            "stub ${System.currentTimeMillis()}",
            "stub ${System.currentTimeMillis()}",
            Location("Stub")
        )

        val id = Content.NOTE.insert(note)
        note.id = id

        assert(note.id > 0)
    }

    @After
    fun afterMainServiceTest(){
        Log.v(tag, "Finishing")
        ctx?.unbindService(serviceConnection)
        ctx?.stopService(serviceIntent)
    }
}