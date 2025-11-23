package com.wingstars.home.adapter // Hoặc package của bạn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wingstars.home.R // Đảm bảo import R của module :home

class ComingSoonAdapter(private val context: Context, private val dataList: List<Int>) :
    RecyclerView.Adapter<ComingSoonAdapter.ViewHolder>() {

    // ViewHolder giữ các view từ item_classroom.xml
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Bạn hãy đảm bảo ID của các view này khớp với file item_classroom.xml
        val classroomImage: ImageView = view.findViewById(R.id.imgClassroom)
        val classroomTitle: TextView = view.findViewById(R.id.tvClassroomTitle)
        val classroomTime: TextView = view.findViewById(R.id.tvClassroomTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Dùng layout item_classroom.xml
        val view = LayoutInflater.from(context).inflate(R.layout.item_comingsoon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Lấy dữ liệu
        val item = dataList[position]

        // --- Bind dữ liệu thật của bạn ở đây ---
        holder.classroomTitle.text = "Tiêu đề lớp học $item"
        holder.classroomTime.text = "Thời gian: 1$position:00 PM"

        // Gán ảnh placeholder
        holder.classroomImage.setImageResource(R.drawable.placeholder_calendar)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}