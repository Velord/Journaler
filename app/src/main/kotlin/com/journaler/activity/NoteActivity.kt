package com.journaler.activity

import com.example.velord.masteringandroiddevelopmentwithkotlin.R

class NoteActivity : ItemActivity() {
    override val tag: String = "NoteActivity"
    override fun getLayout(): Int = R.layout.activity_note
}