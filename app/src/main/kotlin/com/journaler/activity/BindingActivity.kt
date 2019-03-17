package com.journaler.activity

import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.example.velord.masteringandroiddevelopmentwithkotlin.databinding.ActivityBindingBinding
import com.journaler.model.Note


class BindingActivity: BaseActivity() {
    override val tag: String = "Binding activity"
    override fun getLayout(): Int = R.layout.activity_binding
    override fun getActivityTitle(): Int = R.string.app_name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * ActivityBindingBinding is auto generated class
         * which name is derived from activity_binding.xml filename.
         */
        val binding : ActivityBindingBinding =
            DataBindingUtil.setContentView(
                this, getLayout()
            )
        val location = Location("dummy")

        val note = Note("my note", "bla", location)
        binding.note = note
    }
}