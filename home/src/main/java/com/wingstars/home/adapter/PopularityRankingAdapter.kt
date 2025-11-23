package com.wingstars.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wingstars.home.R

class PopularityRankingAdapter(private val context: Context, private val dataList: List<Int>) :
    RecyclerView.Adapter<PopularityRankingAdapter.ViewHolder>() {

    // 1. Tạo một danh sách chứa ID của 5 ảnh xếp hạng theo thứ tự
    private val rankImages = listOf(
        R.drawable.ranking_card_01, // Index 0 (Hạng 1)
        R.drawable.ranking_card_02, // Index 1 (Hạng 2)
        R.drawable.ranking_card_03, // Index 2 (Hạng 3)
        R.drawable.ranking_card_04, // Index 3 (Hạng 4)
        R.drawable.ranking_card_05  // Index 4 (Hạng 5)
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val memberImage: ImageView = view.findViewById(R.id.img_member)
        // val memberName: TextView = view.findViewById(R.id.tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_popularity_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        // --- Logic hiển thị ảnh ranking ---

        // 2. Kiểm tra xem vị trí hiện tại có nằm trong danh sách ảnh ranking không
        if (position < rankImages.size) {
            // Nếu position là 0 -> lấy ảnh rankImages[0] (card_01)
            // Nếu position là 1 -> lấy ảnh rankImages[1] (card_02), v.v.
            holder.memberImage.setImageResource(rankImages[position])
        } else {
            // (Tùy chọn) Xử lý cho người thứ 6 trở đi
            // Ví dụ: Ẩn ảnh hoặc hiện ảnh mặc định
            // holder.memberImage.setImageDrawable(null)
        }

        // --- Các logic khác (tên, avatar thành viên...) ---
        // holder.memberName.text = "Thành viên $item"
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}