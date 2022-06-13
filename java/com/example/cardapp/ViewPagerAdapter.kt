package com.example.cardapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.itemviewpager.view.*

class ViewPagerAdapter(val adapter: List<RecyclerView.Adapter<out RecyclerView.ViewHolder>>)
    : RecyclerView.Adapter<ViewPagerAdapter.ViewpagerPageHolder>(){

    inner class ViewpagerPageHolder(itemview : View) : RecyclerView.ViewHolder(itemview)
    private var parentw : ViewGroup? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewpagerPageHolder {
        parentw = parent
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemviewpager,parent,
            false)
        return ViewpagerPageHolder(view)
    }

    override fun onBindViewHolder(holder: ViewpagerPageHolder, position: Int) {
        holder.itemView.rv.adapter = adapter[position]
        holder.itemView.rv.layoutManager = LinearLayoutManager(parentw?.context)
    }

    override fun getItemCount(): Int {
        return adapter.size
    }
}