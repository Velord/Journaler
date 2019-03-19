package com.example.velord.masteringandroiddevelopmentwithkotlin

import android.location.Location
import com.journaler.database.Content
import com.journaler.model.Note
import org.junit.Test

class NoteTest {

    @Test
    fun noteTest(){
        val title = "stub"
        val message = "stun"
        val note  = Note(
            title,
            message,
            Location("Stub")
        )

        val succes = (title == note.title) and (message == note.message)
        assert(succes == true)
    }
}