package com.journaler.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.activity.NoteActivity
import com.journaler.activity.TODOActivity
import com.journaler.fragment.ItemsFragment.companion.TODO_REQUEST
import com.journaler.fragment.ItemsFragment.companion.NOTE_REQUEST
import com.journaler.model.MODE
import java.text.SimpleDateFormat
import java.util.*

class ItemsFragment : BaseFragment() {
    object companion{
        val NOTE_REQUEST = 0
        val TODO_REQUEST = 1
    }

    override val logTag = "Items fragment"
    override fun getLayout(): Int = R.layout.fragment_items

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(getLayout() , container , false)
        setFABOnClickListener(view)
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            TODO_REQUEST -> {
                if (resultCode == Activity.RESULT_OK)
                    Log.i(logTag , "We created new TODO")
                else
                    Log.w(logTag , "We didn't created new TODO")
            }
            NOTE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK)
                    Log.i(logTag , "We created new Note")
                else
                    Log.w(logTag , "We did't created new Note")
            }
        }
    }

    //always need view to find fab, given the fact that we are in fragment class
    private fun setFABOnClickListener(view: View){
        val btn = view.findViewById<FloatingActionButton>(R.id.fab)
        btn?.setOnClickListener {
            val items = arrayOf(
                getString(R.string.notes),
                getString(R.string.todos)
            )
            val builder = AlertDialog.Builder(this@ItemsFragment.context)
                .setTitle(R.string.choose_a_type)
                .setItems(
                    items,
                    {_, which ->
                        when(which){
                            0 -> openCreateNote()
                            1 -> openCreateTODO()
                            else -> Log.v(tag , "Unknown options selected [$which]")
                        }
                    }
                )
            builder.show()
              Log.v(tag, "FAB CLick")
        }
    }

    private fun openCreateNote(){
        val intent = Intent(context , NoteActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRAS_KEY , MODE.CREATE.mode)
        intent.putExtras(data)
        startActivityForResult(intent , NOTE_REQUEST)
    }

    private fun openCreateTODO(){
        val dateTime = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH)
        val timeFormat = SimpleDateFormat("MM:HH", Locale.ENGLISH)

        val intent = Intent(context , TODOActivity::class.java)
        val data  = Bundle()

        data.putInt(MODE.EXTRAS_KEY , MODE.CREATE.mode)
        data.putString(TODOActivity.EXTRA_DATE , dateFormat.format(dateTime))
        data.putString(TODOActivity.EXTRA_TIME , timeFormat.format(dateTime))
        intent.putExtras(data)

        startActivityForResult(intent , TODO_REQUEST)
    }
}