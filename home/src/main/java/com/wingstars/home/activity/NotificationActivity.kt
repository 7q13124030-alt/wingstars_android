package com.wingstars.home.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wingstars.base.base.BaseActivity
import com.wingstars.home.R
import com.wingstars.home.adapter.NotificationAdapter
import com.wingstars.home.adapter.NotificationData
import com.wingstars.home.databinding.ActivityNotificationBinding
import com.wingstars.home.viewmodel.NotificationViewModel
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.wingstars.base.net.beans.CRMInAppMessageResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationActivity : BaseActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationAdapter
    private var isKeepListVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleFoot(view1 = binding.root,
        navigationBarColor = R.color.color_F3F4F6,
        statusBarColor = R.color.color_F3F4F6,)

        initView()
        loadData()
    }

    override fun initView() {
        binding.btnBack.setOnClickListener { finish() }

        binding.tvMarkAllRead.setOnClickListener {
            isKeepListVisible = true // Đánh dấu là người dùng vừa thao tác, KHÔNG ẩn danh sách
            viewModel.doNotifyAllRead()

            // Cập nhật UI tạm thời mất chấm đỏ
            val currentItems = adapter.snapshot().items
            currentItems.forEach { it?.status = 1 }
            adapter.notifyDataSetChanged()

            binding.tvMarkAllRead.setTextColor(android.graphics.Color.parseColor("#999999"))
            Toast.makeText(this, "已全部標示為已讀", Toast.LENGTH_SHORT).show()
        }

        adapter = NotificationAdapter { data ->
            if (data.status == 0) {
                isKeepListVisible = true // Tương tự, tránh ẩn list nếu đây là tin chưa đọc cuối cùng
                data.status = 1
                adapter.notifyDataSetChanged()
                viewModel.doSingleRead(data.id)
            }
            switchView(data)
        }

        binding.rvNotification.layoutManager = LinearLayoutManager(this)
        binding.rvNotification.adapter = adapter

        adapter.addOnPagesUpdatedListener {
            val allItems = adapter.snapshot().items
            val hasUnread = allItems.any { it?.status == 0 }
            val totalItems = adapter.itemCount

            if (hasUnread) {
                binding.tvMarkAllRead.setTextColor(android.graphics.Color.parseColor("#E2518D"))
            } else {
                binding.tvMarkAllRead.setTextColor(android.graphics.Color.parseColor("#101828"))
            }


            // Không có thông báo nào (totalItems == 0)
            // HOẶC Đã đọc hết (!hasUnread) VÀ Lúc mới vào chưa thao tác gì (!isKeepListVisible)
            if (totalItems == 0 || (!hasUnread && !isKeepListVisible)) {
                binding.rvNotification.visibility = android.view.View.GONE
                binding.layoutEmpty.visibility = android.view.View.VISIBLE
            } else {
                binding.rvNotification.visibility = android.view.View.VISIBLE
                binding.layoutEmpty.visibility = android.view.View.GONE
            }
        }
    }
    private fun loadData() {
        lifecycleScope.launch {
            viewModel.getNotificationList().collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
    private fun switchView(data: CRMInAppMessageResponse) {
        val route = data.targetUrl ?: ""

        // Regex patterns (Giữ nguyên từ dự án cũ)
        val taskPattern     = "^task\\.([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})$".toRegex()
        val couponPattern   = "^coupon\\.([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})$".toRegex()
        val activityPattern = "^activity\\.([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})$".toRegex()

        when {
            route.isEmpty() -> {
                // Không có link -> Mở màn hình chi tiết tin nhắn (nếu có)
                // Hoặc chỉ show nội dung
            }
            route == "home" -> {
                // Về trang chủ (Dùng EventBus hoặc Intent về MainActivity clear top)
                navigateToMain(0)
            }
            route == "point" || route == "task" -> {
                // Mở tab Điểm/Nhiệm vụ
                navigateToMain(2) // Giả sử tab 2 là Point
            }
            route == "ticket" -> {
                navigateToMain(3) // Giả sử tab 3 là Ticket
            }
            // ... Copy các case khác tương tự ...

            // Ví dụ mở link ngoài (Product URL như HomeFragment)
            route.startsWith("http") -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(route))
                    startActivity(intent)
                } catch (e: Exception) { e.printStackTrace() }
            }

            // Xử lý Task/Coupon specific UUID
            route.matches(taskPattern) -> {
                val uuid = taskPattern.find(route)?.groupValues?.get(1)
                // Gọi API lấy chi tiết task rồi hiện Dialog (như trong EventNotifyFragment cũ)
                // viewModel.getTaskInfo(uuid)
            }

            else -> {
                // Mặc định
            }
        }
    }

    private fun navigateToMain(tabIndex: Int) {
        // Code chuyển tab MainActivity (thay cho EventBus nếu muốn đơn giản)
        // val intent = Intent(this, MainActivity::class.java)
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        // intent.putExtra("TAB_INDEX", tabIndex)
        // startActivity(intent)
    }
}