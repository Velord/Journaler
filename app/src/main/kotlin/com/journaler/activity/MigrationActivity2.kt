package com.journaler.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.velord.masteringandroiddevelopmentwithkotlin.R

class MigrationActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() = super.onResume()

}