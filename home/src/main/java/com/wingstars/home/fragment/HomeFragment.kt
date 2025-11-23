package com.wingstars.home.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wingstars.base.base.BaseFragment
import com.wingstars.home.activity.TodayItineraryActivity

// Import các Adapter cũ
import com.wingstars.home.adapter.ArticleAdapter
import com.wingstars.home.adapter.StylistVibeAdapter
import com.wingstars.home.adapter.PopularityRankingAdapter
import com.wingstars.home.adapter.NewsAdapter
import com.wingstars.home.adapter.ProductAdapter

// --- THÊM MỚI: Import 2 Adapter mới ---
// --- KẾT THÚC ---

import com.wingstars.home.databinding.FragmentHomeBinding
import com.wingstars.home.viewmodel.HomeViewModel


class HomeFragment : BaseFragment() ,View.OnClickListener{ // Giữ nguyên
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel : HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root
        initView()
        return root
    }

    private fun initView() {
        // 1. Khởi tạo ViewModel
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // 2. Xử lý Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM){
            binding.root.setOnApplyWindowInsetsListener{ v, insets ->
                val statusBarHeight = insets.getInsets(WindowInsets.Type.statusBars()).top
                Log.e("statusBarHeight","statusBarHeight=$statusBarHeight")
                setViewTop(binding.title, statusBarHeight)
                binding.root.setOnApplyWindowInsetsListener(null)
                insets
            }
        } else {
            setViewTop(binding.title, getStatusBarHeight())
        }
        binding.titleProducts.tvSectionTitle.text = "熱銷商品"
        binding.titlePopularRanking.tvSectionTitle.text = "人氣排行"

// 2. Stylist Vibe
        binding.titleStylistVibe.tvSectionTitle.text = "氛圍時尚"
// binding.titleHighlights.tvMore.setOnClickListener { ... }

// 3. Bài viết
        binding.titleHighlights.tvSectionTitle.text = "活動花絮"

// 4. Tin tức
        binding.titleNews.tvSectionTitle.text = "最新消息"
//        binding.todayItinerary.setOnClickListener {
//            val intent = Intent(context, TodayItineraryActivity::class.java)
//
//            context?.startActivity(intent)
//        }

        viewModel.homeDataList.observe(viewLifecycleOwner) { dataList ->
            // Setup rvProducts (Grid)
            val productAdapter = ProductAdapter(requireActivity(), dataList)
            binding.rvProducts.layoutManager = GridLayoutManager(requireActivity(), 2)
            binding.rvProducts.adapter = productAdapter

//            // Setup rvMembers (Horizontal)
//            val memberAdapter = MemberAdapter(requireActivity(), dataList)
//            binding.rvMembers.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
//            binding.rvMembers.adapter = memberAdapter

            // Setup rvHighlights (Grid)
            val highlightAdapter = StylistVibeAdapter(requireActivity(), dataList)
            binding.rvStylistVibe.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvStylistVibe.adapter = highlightAdapter

            // Setup rvArticles (Horizontal)
            val articleAdapter = ArticleAdapter(requireActivity(), dataList)
            binding.rvArticles.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvArticles.adapter = articleAdapter

        }
        viewModel.memberDataList.observe(viewLifecycleOwner){dataList ->
            val memberAdapter = PopularityRankingAdapter(requireActivity(), dataList)
        binding.rvPopularityRanking.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvPopularityRanking.adapter = memberAdapter
        }
        viewModel.newsDataList.observe(viewLifecycleOwner){dataList ->
            val newsAdapter = NewsAdapter(requireActivity(), dataList)
            binding.rvNews.layoutManager = LinearLayoutManager(requireActivity())
            binding.rvNews.adapter = newsAdapter
        }
        viewModel.getHomeData()




        // 7. Setup Click Listener (Mở comment)
        binding.todayItinerary.setOnClickListener(this)
        binding.titleProducts.root.setOnClickListener(this)
        binding.titlePopularRanking.root.setOnClickListener(this)
        binding.titleHighlights.root.setOnClickListener(this)
        binding.titleHighlights.root.setOnClickListener(this)
        binding.titleNews.root.setOnClickListener(this)
    }

    // Hàm này đã đúng, giữ nguyên
    fun setViewTop(view: View, top: Int) {
        val layoutParams = view.layoutParams as LinearLayout.LayoutParams
        layoutParams.topMargin = top
        view.setLayoutParams(layoutParams);
    }

    // --- THÊM MỚI: Hàm onClick bị thiếu ---
    override fun onClick(v: View?) {
        val id = v?.id
        when(id) {
            // Dùng .root.id để so sánh
            binding.todayItinerary.id -> startActivity(Intent(requireActivity(),
                TodayItineraryActivity::class.java
            ))
            binding.titlePopularRanking.root.id-> startActivity(Intent(requireActivity(),
                com.wingstars.member.activity.PopularityRankingActivity::class.java
            ))
//            binding.titleProducts.root.id -> startActivity(Intent(requireActivity(),
//                PopularityRankingActivity::class.java
//            ))
            binding.titleHighlights.root.id -> startActivity(Intent(requireActivity(),
                com.wingstars.member.activity.EventHighlightsActivity::class.java
            ))
//            binding.titleHighlights.root.id -> {
////                Toast.makeText(requireActivity(), "Mở trang Hoa hậu", Toast.SHORT).show()
//            }
//            binding.titleArticles.root.id -> {
////                Toast.makeText(requireActivity(), "Mở trang Bài viết", Toast.SHORT).show()
//            }
//            binding.titleNews.root.id -> {
////                Toast.makeText(requireActivity(), "Mở trang Tin tức", Toast.SHORT).show()
//            }
        }
    }
    // --- KẾT THÚC ---
}