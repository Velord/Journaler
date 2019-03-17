package com.journaler.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.DropBoxManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.velord.masteringandroiddevelopmentwithkotlin.R
import com.journaler.database.DbEntry

class EntryAdapter(
    private val ctx: Context,
    private val items: List<DbEntry>
): BaseAdapter() {

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        convertView?.let {
            return convertView
        }
        val inflater = LayoutInflater.from(ctx)
        val view = inflater.inflate(R.layout.adapter_entry, null)
        val label = view.findViewById<TextView>(R.id.title)
        label.text = items[position].title
        return view
    }

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = items[position].id

    override fun getCount(): Int = items.size
}