package com.journaler.activity

import com.example.velord.masteringandroiddevelopmentwithkotlin.R

class TODOActivity: ItemActivity(){
    override val tag: String = "TODOActivity"
    override fun getLayout(): Int = R.layout.activity_todo
}