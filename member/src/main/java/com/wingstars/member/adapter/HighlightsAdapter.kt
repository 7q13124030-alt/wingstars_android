package com.wingstars.member.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wingstars.member.databinding.ItemHighlightsListBinding

class HighlightsAdapter(
    private val context: Context,
    private var dataList: MutableList<Int>?
) : RecyclerView.Adapter<HighlightsAdapter.NormalItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalItemViewHolder {
        val binding =
            ItemHighlightsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NormalItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NormalItemViewHolder, position: Int) {
        holder.binding(position)
    }

    override fun getItemCount(): Int {
        return if (dataList != null) dataList!!.size else 0
    }

    fun setList(list: MutableList<Int>?) {
        dataList = if (dataList == null) {
            ArrayList()
        } else {
            dataList == null
            ArrayList()
        }
        dataList!!.addAll(list!!)
        notifyDataSetChanged()
    }

    fun getData(): MutableList<Int>? {
        if (dataList == null) {
            return null
        }
        return dataList
    }

    inner class NormalItemViewHolder(private val binding: ItemHighlightsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(position: Int) {
            var data = dataList!![position]
        }
    }
}