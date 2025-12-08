package com.wingstars.count.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable // Cần import cái này để vẽ viền
import android.os.Bundle
import android.util.TypedValue // Cần import để tính dp
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton // Import đúng loại nút trong XML
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.wingstars.count.R
import com.wingstars.count.adapter.UnusedCouponAdapter
import com.wingstars.count.databinding.FragmentNotUsedBinding
import com.wingstars.count.viewmodel.CouponViewModel

class NotUsedFragment : Fragment() {
    private var _binding: FragmentNotUsedBinding? = null
    private val binding get() = _binding!!

    private val unusedCouponAdapter = UnusedCouponAdapter()
    private var currentDataList: List<CouponViewModel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotUsedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupRefreshLayout()
        loadData()
    }

    private fun setupRecyclerView() {
        binding.rvNotUsed.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = unusedCouponAdapter
        }
        unusedCouponAdapter.onBarcodeClick = { position ->
            showBarcodeDialog(position, currentDataList)
        }
    }

    private fun setupRefreshLayout() {
        binding.srlNotUsed.setOnRefreshListener { refreshLayout ->
            loadData()
            refreshLayout.finishRefresh(1000)
        }
    }

    private fun loadData() {
        val mockData = listOf(
            CouponViewModel("1", "2025 WS單曲寫真壓克力鑰匙圈", "兌換期間：2025/10/28 ~ 2025/11/09", R.drawable.ic_count_gift_1),
            CouponViewModel("2", "2025 WS單曲寫真女孩貼紙包", "兌換期間：2025/10/28 ~ 2025/11/09", R.drawable.ic_count_gift_3),
            CouponViewModel("3", "Wing Stars 簽名會（第三梯次）", "兌換期間：2025/10/28 ~ 2025/11/09", R.drawable.bg_round_image),
            CouponViewModel("4", "Wing Stars 簽名會（第二梯次）", "兌換期間：2025/10/28 ~ 2025/11/09", R.drawable.bg_round_image),
        )

        currentDataList = mockData
        updateUI(mockData)
    }

    private fun updateUI(data: List<CouponViewModel>) {
        if (data.isNotEmpty()) {
            unusedCouponAdapter.setData(data)
            binding.rvNotUsed.visibility = View.VISIBLE
            binding.llEmpty.visibility = View.GONE
        } else {
            binding.rvNotUsed.visibility = View.GONE
            binding.llEmpty.visibility = View.VISIBLE
        }
    }

    private fun showBarcodeDialog(startPosition: Int, dataList: List<CouponViewModel>) {
        if (dataList.isEmpty()) return

        val context = requireContext()
        val dialog = Dialog(context, com.google.android.material.R.style.Theme_MaterialComponents_Light_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_exchange_barcode)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            decorView.setPadding(0, 0, 0, 0)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
        }

        val btnClose = dialog.findViewById<ImageView>(R.id.iv_close_dialog)

        val btnNext = dialog.findViewById<AppCompatButton>(R.id.btn_next)
        val btnPrev = dialog.findViewById<AppCompatButton>(R.id.btn_prev)
        val tvName = dialog.findViewById<TextView>(R.id.tv_exchange_name)
        val tvPeriod = dialog.findViewById<TextView>(R.id.tv_exchange_period1)
        val ivImage = dialog.findViewById<ImageView>(R.id.iv_goods_image)
        val ivQrCode = dialog.findViewById<ImageView>(R.id.iv_qr_code)
        val tvQrEnlarge = dialog.findViewById<TextView>(R.id.tv_qr_code)

        var currentDialogPosition = startPosition

        fun zoomQrCode() {
            val zoomDialog = Dialog(context)
            zoomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            zoomDialog.setContentView(R.layout.dialog_zoom_qr)
            zoomDialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                decorView.setPadding(0, 0, 0, 0)
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
            val ivZoomedQr = zoomDialog.findViewById<ImageView>(R.id.iv_zoomed_qr)
            val btnCloseZoom = zoomDialog.findViewById<ImageView>(R.id.iv_close_zoom)

            if (ivQrCode.drawable != null) {
                ivZoomedQr.setImageDrawable(ivQrCode.drawable.constantState?.newDrawable())
            }
            btnCloseZoom.setOnClickListener { zoomDialog.dismiss() }
            zoomDialog.show()
        }

        ivQrCode.setOnClickListener { zoomQrCode() }
        tvQrEnlarge.setOnClickListener { zoomQrCode() }
        fun dpToPx(dp: Float): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
            )
        }

        fun updateDialogUI() {
            val item = dataList[currentDialogPosition]

            tvName.text = item.title
            tvPeriod.text = item.expiryDate
            Glide.with(context).load(item.imageResId).into(ivImage)

            if (currentDialogPosition == 0) {
                btnPrev.isEnabled = false
                btnPrev.setTextColor(ContextCompat.getColor(context, R.color.color_101828))

            } else {
                btnPrev.isEnabled = true
                btnPrev.setTextColor(ContextCompat.getColor(context, R.color.color_E2518D))
            }
            // -----------------------------------------------

            if (currentDialogPosition == dataList.size - 1) {
                btnNext.isEnabled = false
                btnNext.alpha = 0.5f
            } else {
                btnNext.isEnabled = true
                btnNext.alpha = 1.0f
            }
        }

        updateDialogUI()

        btnClose.setOnClickListener { dialog.dismiss() }

        btnNext.setOnClickListener {
            if (currentDialogPosition < dataList.size - 1) {
                currentDialogPosition++
                updateDialogUI()
            }
        }

        btnPrev.setOnClickListener {
            if (currentDialogPosition > 0) {
                currentDialogPosition--
                updateDialogUI()
            }
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}