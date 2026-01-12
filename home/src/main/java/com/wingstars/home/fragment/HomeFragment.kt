package com.wingstars.home.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wingstars.base.base.BaseFragment
import com.wingstars.base.net.beans.WSMemberResponse
import com.wingstars.base.net.beans.WSProductResponse
import com.wingstars.base.utils.MMKVManagement
import com.wingstars.home.R
import com.wingstars.home.adapter.*
import com.wingstars.home.databinding.FragmentHomeBinding
import com.wingstars.home.viewmodel.HomeViewModel
import com.wingstars.member.activity.AtmosphereFashionDetailsActivity
import com.wingstars.member.activity.MemberDetailsActivity
import com.wingstars.member.activity.PopularityRankingActivity

class HomeFragment : BaseFragment(), View.OnClickListener,
    SupportFashionAdapter.onSupportFashionListener,
    PopularityAdapter.onPopularityRankingListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    // Các Adapter con
    private lateinit var topBannerAdapter: TopBannerAdapter
    private lateinit var itineraryAdapter: ItineraryBannerAdapter // Thêm adapter cho Today Itinerary
    private lateinit var comingSoonAdapter: ComingSoonAdapter    // Dữ liệu cứng
    private lateinit var productAdapter: ProductAdapter
    private lateinit var popularityAdapter: PopularityAdapter
    private lateinit var highlightsAdapter: YoutubeAdapter       // Thêm adapter cho Youtube/Highlights
    private lateinit var newsAdapter: NewsAdapter

    private lateinit var mainConcatAdapter: ConcatAdapter
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        initView()
        observeData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (!isDataLoaded) {
            loadData()
            isDataLoaded = true
        }
    }

    private fun loadData() {
        viewModel.getRenderedList()
        if (viewModel.wsFashionCategorysData.value == null) {
            viewModel.wsFashionCategorys()
        } else {
            viewModel.wsFashions()
        }
        viewModel.getCalendarData()
        viewModel.getHomeData()
        viewModel.getLatestNewsData()
    }

    private fun initView() {
        // 1. Banner & Bo góc
        val localImages = listOf(R.drawable.placeholder_banner)
        topBannerAdapter = TopBannerAdapter(localImages)
        val roundingOffsetAdapter = SingleViewAdapter(R.layout.item_home_rounding_offset)

        // 2. 今日行程 (Today Itinerary) - Lấy dữ liệu từ API
        itineraryAdapter = ItineraryBannerAdapter(mutableListOf())
        val itinerarySection = SectionWrapperAdapter(
            title = "今日行程",
            innerAdapter = itineraryAdapter,
            onMoreClick = { /* Điều hướng */ }
        )

        // 3. 即將販售 (Coming Soon) - Set dữ liệu cứng
        val comingSoonList = mutableListOf(
            ComingSoonData(R.drawable.placeholder_calendar, "25-26 WS女孩應援毛巾｜天鷹款\n", "2025/09/20 (六) 10:00"),
            ComingSoonData(R.drawable.placeholder_calendar, "Event 2 Title", "2025/10/01")
        )
        comingSoonAdapter = ComingSoonAdapter(comingSoonList)
        val comingSoonSection = SectionWrapperAdapter(
            title = "即將販售",
            innerAdapter = comingSoonAdapter,
            onMoreClick = { /* Điều hướng */ }
        )

        // 4. 熱銷商品 (Hot Products) - Hiển thị Grid 2 cột, 4 sản phẩm
        productAdapter = ProductAdapter(requireActivity(), mutableListOf(), object : ProductAdapter.OnItemListener {
            override fun onItemClick(data: WSProductResponse, position: Int) {
                checkLoginAndAction {
                    openWebUrl(data.permalink)
                }
            }
        })
        val productsSection = SectionWrapperAdapter(
            title = getString(R.string.products_title),
            innerAdapter = productAdapter,
            isGrid = true, // Cần thêm tham số này vào constructor của SectionWrapperAdapter
            onMoreClick = { openWebUrl("https://61.218.209.209/product/") }
        )

        // 5. 人氣排行
        popularityAdapter = PopularityAdapter(requireActivity(), mutableListOf(), this)
        val popularitySection = SectionWrapperAdapter(
            title = getString(R.string.popularity_ranking_title),
            innerAdapter = popularityAdapter,
            onMoreClick = { startActivity(Intent(requireActivity(), PopularityRankingActivity::class.java)) }
        )

        // 6. 活動花絮 (Event Highlights)
        highlightsAdapter = YoutubeAdapter(requireContext())
        val highlightsSection = SectionWrapperAdapter(
            title = "活動花絮",
            innerAdapter = highlightsAdapter,
            onMoreClick = { /* Điều hướng */ }
        )

        // 7. 最新消息
        newsAdapter = NewsAdapter(requireActivity(), mutableListOf(), object : NewsAdapter.OnItemListener {
            override fun onItemClick(data: com.wingstars.base.net.beans.WSPostResponse, position: Int) {
                startActivity(Intent(requireActivity(), com.wingstars.home.activity.LatestNewsActivity::class.java))
            }
        })
        val newsSection = SectionWrapperAdapter(
            title = getString(R.string.news_title),
            innerAdapter = newsAdapter,
            onMoreClick = { startActivity(Intent(requireActivity(), com.wingstars.home.activity.LatestNewsActivity::class.java)) }
        )

        // Thiết lập ConcatAdapter theo thứ tự mockup
        mainConcatAdapter = ConcatAdapter(
            topBannerAdapter,
            roundingOffsetAdapter,
            itinerarySection,
            comingSoonSection,
            productsSection,
            popularitySection,
            highlightsSection,
            newsSection
        )

        binding.rvHomeContent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mainConcatAdapter
        }

        binding.refresh.setOnRefreshListener {
            loadData()
            binding.refresh.isRefreshing = false
        }
    }

    private fun observeData() {
        viewModel.calendarDataList.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                itineraryAdapter.setList(list)
            }
        }

        // Cập nhật Sản phẩm (Lấy 4 cái đầu tiên)
        viewModel.productDataList.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                productAdapter.setList(list.take(4).toMutableList())
            }
        }

        // Cập nhật Rank
        viewModel.wsRankData.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                popularityAdapter.setRankList(list)
            }
        }

        // Cập nhật Youtube Highlights
        viewModel.youtubeVideoList.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                highlightsAdapter.setList(list)
            }
        }

        // Cập nhật Tin tức
        viewModel.newsDataList.observe(viewLifecycleOwner) { list ->
            if (!list.isNullOrEmpty()) {
                newsAdapter.setList(list.take(3))
            }
        }
    }

    private fun openWebUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun checkLoginAndAction(action: () -> Unit) {
        if (MMKVManagement.isLogin()) action()
        else startActivity(Intent(requireActivity(), com.wingstars.login.LoginActivity::class.java))
    }

    override fun onClick(v: View?) {}

    override fun onPopularityRankingClickItem(data: WSMemberResponse) {
        checkLoginAndAction {
            val intent = Intent(requireActivity(), MemberDetailsActivity::class.java)
            intent.putExtra("WSMemberResponse", data)
            startActivity(intent)
        }
    }

    override fun onSupportFashionClickItem(memberId: Int) {
        checkLoginAndAction {
            val intent = Intent(requireActivity(), AtmosphereFashionDetailsActivity::class.java)
            intent.putExtra("memberId", memberId)
            startActivity(intent)
        }
    }
}
