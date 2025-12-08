package com.wingstars.count.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wingstars.count.R
import com.wingstars.count.databinding.ActivityExchangeDetailsBinding
import com.wingstars.count.databinding.DialogOtpCouponsBinding
import com.wingstars.count.databinding.DialogPublicPopupBoxBinding
import com.wingstars.count.viewmodel.CountListItemViewModel
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.listener.OnPageChangeListener
import kotlin.random.Random

class ExchangeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExchangeDetailsBinding
    private var currentOtpCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityExchangeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        loadData()
        initBanner()
    }

    // ===================== DATA =====================
    private fun loadData() {
        val item: CountListItemViewModel? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("EXTRA_GIFT_ITEM", CountListItemViewModel::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("EXTRA_GIFT_ITEM")
            }

        item?.let {
            binding.couponName.text = it.title
            binding.pointCost.text = "${it.count} 點"
            binding.tvCouponTime.text = it.time
            binding.status.text = it.exitem
            binding.maxPerMember.text = it.limit
            binding.activityTime.text = it.total
            binding.finishTime.text = it.location
            binding.tvUsageRules.text = it.usageRules
            binding.tvInformation.text = it.information
            binding.tvPrecautions.text = it.description
        }
    }

    // ===================== VIEW =====================
    private fun initView() {
        binding.imgBack.setOnClickListener { finish() }

        binding.rlRuleHeader.setOnClickListener {
            toggleSection(binding.tvUsageRules, binding.ivArrow)
        }

        binding.rlInformation.setOnClickListener {
            toggleSection(binding.tvInformation, binding.ivArrow1)
        }

        binding.rlPrecautions.setOnClickListener {
            toggleSection(binding.tvPrecautions, binding.ivArrow3)
        }

//        binding.btnExchange.setOnClickListener {
//            showOtpDialog()
//        }
    }

    // ===================== BANNER =====================
    private fun initBanner() {

        val imageList = listOf(
            R.drawable.bg_round_image,
            R.drawable.bg_round_image,
            R.drawable.bg_round_image
        )

        val bannerAdapter = object : BannerImageAdapter<Int>(imageList) {
            override fun onBindView(
                holder: BannerImageHolder,
                data: Int,
                position: Int,
                size: Int
            ) {
                // Load ảnh bằng Glide
                Glide.with(holder.itemView)
                    .load(data)
                    .into(holder.imageView)
            }
        }

        binding.bannerUserGuideImage.setAdapter(bannerAdapter)
        binding.bannerUserGuideImage.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                binding.tvIndicator.text = "${position + 1}/${imageList.size}"
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.tvIndicator.text = "1/${imageList.size}"
        binding.bannerUserGuideImage.start()
    }

    // ===================== OTP =====================
    private fun showOtpDialog() {
        val otpBinding = DialogOtpCouponsBinding.inflate(LayoutInflater.from(this))
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(otpBinding.root)
        bottomSheetDialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
            bottomSheet?.let {
                it.setBackgroundColor(Color.TRANSPARENT)
                val displayMetrics = resources.displayMetrics
                val height = displayMetrics.heightPixels
                val layoutParams = it.layoutParams
                layoutParams.height = (height * 0.7).toInt()
                it.layoutParams = layoutParams
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }

        otpBinding.etInput.visibility = View.VISIBLE
        otpBinding.title.visibility = View.VISIBLE
        otpBinding.content.visibility = View.VISIBLE
        otpBinding.title.text = "驗證碼確認"
        otpBinding.content.text = "請輸入驗證碼確認身分。兌換後不可取消，是否繼續？"
        fun refreshOtp() {
            val newCode = Random.nextInt(100000, 999999).toString()
            currentOtpCode = newCode
            otpBinding.tvOtpCode.text = newCode
        }

        refreshOtp()

        otpBinding.ivCloseDialog.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        otpBinding.etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (input.length == 6) {
                    if (input == currentOtpCode) {
                        bottomSheetDialog.dismiss()
                        showSuccessDialog()
                    } else {
                        Toast.makeText(this@ExchangeDetailsActivity, "驗證碼錯誤！", Toast.LENGTH_SHORT).show()
                        otpBinding.etInput.text.clear()
                        refreshOtp()
                    }
                }
            }
        })
        bottomSheetDialog.show()
    }

    // ===================== SUCCESS =====================
    private fun showSuccessDialog() {
        val successBinding = DialogPublicPopupBoxBinding.inflate(LayoutInflater.from(this))
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(successBinding.root)

        bottomSheetDialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
        }

        successBinding.tvDialogTitle.text = "兌換成功"
        successBinding.tvDialogContent.text = "您的兌換券已放入「兌換歷程」，隨時可查看使用。"
        successBinding.ivDialogImage.setImageResource(R.drawable.ic_takamei)

        successBinding.tvDialogConfirm.setOnClickListener {
            bottomSheetDialog.dismiss()
            val intent = Intent(this, ExchangeHistoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra("EXTRA_TARGET_TAB", 0)
            startActivity(intent)
            finish()
        }

        bottomSheetDialog.show()
    }

    // ===================== TOGGLE =====================
    private fun toggleSection(content: View, arrow: ImageView) {
        if (content.visibility == View.VISIBLE) {
            content.visibility = View.GONE
            arrow.animate().rotation(0f).setDuration(200).start()
        } else {
            content.visibility = View.VISIBLE
            arrow.animate().rotation(180f).setDuration(200).start()
        }
    }
}
