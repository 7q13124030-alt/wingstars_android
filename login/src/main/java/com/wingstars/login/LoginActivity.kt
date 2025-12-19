package com.wingstars.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tencent.mmkv.MMKV
import com.wingstars.base.base.BaseActivity
import com.wingstars.base.net.NetBase
import com.wingstars.base.net.beans.CRMSignInRequest
import com.wingstars.login.databinding.ActivityLoginBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoginActivity : BaseActivity(), LoginNavigator {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private var tag = ""
    private var isFromSplash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setTitleFoot(
            view1 = binding.root,
            navigationBarColor = R.color.white,
            statusBarColor = R.color.color_F9DCE8
        )

        window.statusBarColor = ContextCompat.getColor(this, R.color.color_F9DCE8)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        tag = intent?.getStringExtra("tag").toString()

        // NHẬN GIÁ TRỊ TỪ SPLASH HOẶC WELCOME
        isFromSplash = intent?.getBooleanExtra("isFromSplash", false) == true

        viewModel.setNavigator(this)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        // --- Logic Ghi nhớ tài khoản ---
        if (MMKV.defaultMMKV().decodeBool("isRememberAccount")) {
            val account = MMKV.defaultMMKV().decodeString("member_account")
            val psd = MMKV.defaultMMKV().decodeString("member_psd")
            if (!account.isNullOrEmpty() && !psd.isNullOrEmpty()) {
                binding.edtPhone.setText(account)
                binding.edtPsd.setText(psd)
            }
            binding.cbPsd.isChecked = true
        } else {
            binding.cbPsd.isChecked = false
        }

        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // --- Logic nút Đóng (X) ---
        binding.ivClose.setOnClickListener {
            // Nếu đến từ Splash/Welcome -> Bấm đóng nghĩa là vào Main với tư cách Khách (hoặc Login sau)
            if (isFromSplash) {
                navigateToMain()
            }
            finish()
        }

        binding.tvPhoneInputError.visibility = View.INVISIBLE
        binding.tvPsdInputError.visibility = View.INVISIBLE

        setupLiveValidation()

        binding.edtPhone.setOnFocusChangeListener { _, hasFocus ->
            binding.rlPhone.isActivated = hasFocus
        }
        binding.edtPsd.setOnFocusChangeListener { _, hasFocus ->
            binding.rlPsd.isActivated = hasFocus
        }

        binding.cbPsdVisible.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.edtPsd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.edtPsd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.edtPsd.text?.let { binding.edtPsd.setSelection(it.length) }
        }

        // --- Logic nút Login ---
        binding.btnLogin.setOnClickListener {
            val phoneStr = binding.edtPhone.text.toString().trim()
            val psdStr = binding.edtPsd.text.toString().trim()

            if (phoneStr.isEmpty()) {
                showPhoneError(getString(R.string.hint_phone))
                return@setOnClickListener
            }
            if (!isTaiwanPhone(phoneStr)) {
                showDialog(getString(R.string.account), getString(R.string.error_phone_format))
                return@setOnClickListener
            }

            if (psdStr.isEmpty()) {
                showPsdError(getString(R.string.error_psd_empty))
                return@setOnClickListener
            }
            // Gọi API kiểm tra đăng nhập
            viewModel.userCheck(CRMSignInRequest(phoneStr, psdStr), binding.cbPsd.isChecked)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, com.wingstars.register.RegisterActivity::class.java))
        }
        binding.tvForgetPsd.setOnClickListener {
            startActivity(Intent(this, com.wingstars.resetpsd.ResetPsdActivity::class.java))
        }
    }

    // --- Hàm chuyển sang Main Activity dùng chung ---
    private fun navigateToMain() {
        val intent1 = Intent("com.company.wingstars.OPEN_MAIN")
        intent?.getStringExtra("fcmTag")?.let {
            intent1.putExtra("fcmTag", it)
        }

        // Cờ này quan trọng: Xóa Login, Welcome, Splash khỏi Stack để user không back lại được
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        if (intent1.resolveActivity(packageManager) != null) {
            startActivity(intent1)
        }
    }

    // ---------------- Live validation ----------------
    private fun setupLiveValidation() {
        binding.edtPhone.addTextChangedListener(object : SimpleTW() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phone = s?.toString()?.trim().orEmpty()
                when {
                    phone.isEmpty() -> showPhoneError(getString(R.string.hint_phone))
                    !isTaiwanPhone(phone) -> showPhoneError(getString(R.string.error_phone_format))
                    else -> showPhoneNormal()
                }
            }
        })

        binding.edtPsd.addTextChangedListener(object : SimpleTW() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pwd = s?.toString().orEmpty()
                when {
                    pwd.isEmpty() -> showPsdError(getString(R.string.error_psd_empty))
                    else -> showPsdNormal()
                }
            }
        })
    }

    // ---------------- UI error helpers ----------------
    private fun showPhoneError(msg: String) {
        binding.tvPhoneInputError.text = msg
        binding.tvPhoneInputError.visibility = View.VISIBLE
        binding.alertCircle.visibility = View.VISIBLE
    }

    private fun showPhoneNormal() {
        binding.tvPhoneInputError.text = ""
        binding.tvPhoneInputError.visibility = View.INVISIBLE
        binding.alertCircle.visibility = View.INVISIBLE
    }

    private fun showPsdError(msg: String) {
        binding.tvPsdInputError.text = msg
        binding.tvPsdInputError.visibility = View.VISIBLE
    }

    private fun showPsdNormal() {
        binding.tvPsdInputError.text = ""
        binding.tvPsdInputError.visibility = View.INVISIBLE
        binding.cbPsdVisible.visibility = View.VISIBLE
    }

    private fun showDialog(title: String, message: String) {
        if (isFinishing) return
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.confirm)) { d, _ -> d.dismiss() }
            .show()
    }

    // ---------------- Validators ----------------
    private fun isTaiwanPhone(phone: String): Boolean = Regex("^09\\d{8}$").matches(phone)

    private open class SimpleTW : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun setUserName(userName: String){
        MMKV.defaultMMKV().encode("user_name", userName)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
    }

    // --- XỬ LÝ KHI ĐĂNG NHẬP THÀNH CÔNG ---
    override fun loginSuccess() {
        NetBase.refreshEvtTasks(true)
        setUserName(binding.edtPhone.text.toString().trim())

        sendBroadcast(Intent(NetBase.BROADCAST_USER_LOGIN))

        if (tag.isNotEmpty()) {
            val intent = Intent(NetBase.BROADCAST_LOGIN_SUCCESS_INTENT)
            intent.putExtra("intentTag", tag)
            sendBroadcast(intent)
        }
        EventBus.getDefault().post(MessageEvent(EventState.LOG_IN.name, ""))

        // Nếu từ Splash/Welcome -> Vào Main và xóa Stack
        if (isFromSplash) {
            navigateToMain()
        }
        // Nếu Login được mở từ trong App (ví dụ giỏ hàng bắt login) -> Chỉ cần finish để quay lại màn trước
        else {
            finish()
        }
    }
    // 1. Override hàm từ Navigator
    override fun showNotRegisteredDialog() {
        showCustomWarningDialog()
    }

    // 2. Hàm xây dựng Dialog Custom
    private fun showCustomWarningDialog() {
        if (isFinishing) return

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)

        // Inflate cái layout vừa tạo ở Bước 1
        val dialogView = layoutInflater.inflate(R.layout.dialog_warning_custom, null)

        builder.setView(dialogView)
        builder.setCancelable(false) // Không cho bấm ra ngoài để tắt

        val dialog = builder.create()

        // Làm nền dialog trong suốt để bo góc đẹp
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Ánh xạ View và xử lý sự kiện
        val btnConfirm = dialogView.findViewById<android.view.View>(R.id.btnConfirm)

        btnConfirm.setOnClickListener {
            dialog.dismiss()
            // TÙY CHỌN: Chuyển sang màn hình Đăng ký luôn nếu muốn
            startActivity(Intent(this, com.wingstars.register.RegisterActivity::class.java))
        }

        dialog.show()
    }
}