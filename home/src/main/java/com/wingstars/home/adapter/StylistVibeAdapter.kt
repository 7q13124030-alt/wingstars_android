package com.wingstars.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wingstars.home.R

class StylistVibeAdapter(private val context: Context, private val dataList: List<Int>) :
    RecyclerView.Adapter<StylistVibeAdapter.ViewHolder>() {

    // 1. Danh sách ảnh Style (từ 01 đến 06)
    private val styleImages = listOf(
        R.drawable.img_style_01,
        R.drawable.img_style_02,
        R.drawable.img_style_03,
        R.drawable.img_style_04,
        R.drawable.img_style_05,
        R.drawable.img_style_06
    )

    // 2. Danh sách Tiêu đề tương ứng (Bạn hãy sửa lại nội dung text ở đây nhé)
    private val styleTitles = listOf(
        "台鋼雄鷹主場球衣", // Tương ứng img_style_01
        "台鋼雄鷹應援",
        "煞猛拼！好客棒球日",
        "煞猛拼！好客棒球日",
        "鷹TAINAN臺南400紀...",
        "鷹世界冒險法批"
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageStylist: ImageView = view.findViewById(R.id.imageStylist)
        val tittleStylist: TextView = view.findViewById(R.id.tittleStylist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_stylist_vibe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // --- Logic hiển thị ---

        // 3. Sử dụng toán tử chia lấy dư (%) để an toàn
        // Nếu dataList dài hơn 6 item, nó sẽ quay vòng lại từ đầu (item 7 sẽ dùng ảnh 1)
        // Điều này giúp app không bị crash nếu dữ liệu server trả về nhiều hơn số lượng ảnh có sẵn.
        val imageIndex = position % styleImages.size
        val titleIndex = position % styleTitles.size

        holder.imageStylist.setImageResource(styleImages[imageIndex])
        holder.tittleStylist.text = styleTitles[titleIndex]
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}