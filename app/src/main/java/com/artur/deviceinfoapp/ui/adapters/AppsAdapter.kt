package com.artur.deviceinfoapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artur.deviceinfoapp.R
import com.artur.deviceinfoapp.data.models.App
import kotlinx.android.synthetic.main.item_application.view.*

class AppsAdapter
internal constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<App>? = mutableListOf()

    fun setData(items: List<App>) {
        this.items?.clear()
        this.items?.addAll(items.toMutableList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_application, parent, false)
        return AppsRecyclerHolder(v)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as AppsRecyclerHolder
        items?.get(position)?.let { holder.bind(it) }
    }

    inner class AppsRecyclerHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bind(item: App) {
            itemView.vAppIcon.setImageDrawable(item.icon)
            itemView.vAppName.text = item.name
            itemView.vAppPackageName.text = item.packageName
        }

    }

}