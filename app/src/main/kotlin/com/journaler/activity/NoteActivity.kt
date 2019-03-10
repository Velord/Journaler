package com.journaler.activity

import android.location.Location
import android.location.LocationListener
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.database.Db
import com.journaler.model.Note
import com.journaler.location.LocationProvider
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : ItemActivity() {
    override val tag: String = "NoteActivity"
    override fun getLayout(): Int = R.layout.activity_note
    private var note: Note? = null
    private var location : Location? = null

    private val textWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            updateNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private val locationListener = object : LocationListener{
        override fun onLocationChanged(locat: Location?) {
            locat?.let {
                LocationProvider.unsubscribe(this)
                this@NoteActivity.location = locat
                val title = getNoteTitle()
                val content = getNoteContent()
                note = Note(title , content, locat)
                val task = object : AsyncTask<Note , Void, Boolean>(){
                    override fun doInBackground(vararg params: Note?): Boolean {
                        if (!params.isEmpty()){
                            val param = params[0]
                            param?.let { return Db.note.insert(param) > 0 }
                        }
                        return false
                    }

                    override fun onPostExecute(result: Boolean?) {
                        result?.let {
                            if (result)
                                Log.i(tag , "Note inserted")
                            else
                                Log.e(tag , "Note not inserted")
                        }
                    }
                }
                task.execute(note)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note_title.addTextChangedListener(textWatcher)
        note_content.addTextChangedListener(textWatcher)
    }

    private fun updateNote(){
        if (note == null){
            if ((!TextUtils.isEmpty(getNoteTitle()))
                and (!TextUtils.isEmpty(getNoteContent()))){
                LocationProvider.subscribe(locationListener)
            }
        }
        else{
            note?.title = getNoteTitle()
            note?.message = getNoteContent()
            val task = object : AsyncTask<Note , Void , Boolean>(){
                override fun doInBackground(vararg params: Note?): Boolean {
                    if (!params.isEmpty()) {
                        val param = params[0]
                        param?.let { return Db.note.update(param) > 0 }
                    }
                    return false
                }

                override fun onPostExecute(result: Boolean?) {
                    result?.let {
                        if (result)
                            Log.i(tag , "Note updated. " +
                                    "title: [${note?.title}] message: [${note?.message}]")
                        else
                            Log.e(tag , "Note not updated")
                    }
                }
            }
            task.execute(note)
        }
    }

    private fun getNoteContent(): String = note_content.text.toString()

    private fun getNoteTitle(): String = note_title.text.toString()
}