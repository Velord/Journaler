package com.journaler.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.database.DbEntry
import com.journaler.database.DbHelper

class EntryAdapter(
    ctx: Context,
    crsr: Cursor
): CursorAdapter(ctx, crsr) {

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.adapter_entry, null)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        view?.let {
            val label = view.findViewById<TextView>(R.id.title)
            label.text = cursor!!.getString(
                cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TITLE)
            )
        }
    }
}