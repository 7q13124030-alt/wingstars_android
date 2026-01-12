package com.wingstars.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.text
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wingstars.home.R

// home/src/main/java/com/wingstars/home/adapter/SectionWrapperAdapter.kt
class SectionWrapperAdapter(
    private val title: String,
    val innerAdapter: RecyclerView.Adapter<*>,
    private val isGrid: Boolean = false,
    private val onMoreClick: () -> Unit
) : RecyclerView.Adapter<SectionWrapperAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvSectionTitle)
        val btnMore: View = view.findViewById(R.id.ivSectionMore)
        val rvContent: RecyclerView = view.findViewById(R.id.rv_section_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_home_section_container, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = title
        holder.btnMore.setOnClickListener { onMoreClick() }

        holder.rvContent.apply {
            if (isGrid) {
                // Nếu là Grid, hiện 2 cột, không cho cuộn ngang (fix cứng 4 item)
                layoutManager = GridLayoutManager(context, 2)
            } else {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            adapter = innerAdapter
        }
    }

    override fun getItemCount() = 1
}
