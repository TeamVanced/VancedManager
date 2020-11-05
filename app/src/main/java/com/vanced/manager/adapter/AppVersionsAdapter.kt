package com.vanced.manager.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vanced.manager.model.AppVersionsModel

class AppVersionsAdapter(context: Context, private var values: Array<AppVersionsModel>) : ArrayAdapter<AppVersionsModel?>(context, android.R.layout.simple_spinner_dropdown_item, values) {

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): AppVersionsModel {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = values[position].version
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = values[position].version
        return label
    }

    fun getPrefValue(position: Int): String {
        return values[position].value
    }

}