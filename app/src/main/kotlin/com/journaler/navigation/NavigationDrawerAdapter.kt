package com.journaler.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import com.example.velord.masteringandroiddevelopmentwithkotlin.R

class NavigationDrawerAdapter(
    val ctx : Context,
    val items : List<NavigationDrawerItem>
): BaseAdapter() {

    private val tag = "Nvgtn. drwr. adptr."

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(ctx)
        var view = convertView
        if(view == null)
            view = inflater.inflate(
                R.layout.adapter_navigation_drawer,null
            ) as LinearLayout

        val item  = items[position]
        val title = view.findViewById<Button>(R.id.drawer_item)
        title.text = item.title
        title.setOnClickListener {
            if (item.enabled)
                item.onClick.run()
            else
                Log.w(tag , "Item is disabled: $item")
        }

        return view
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = 0L
}