package com.android.boilerplate.ui.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.boilerplate.data.repositories.article.response.ArticleData
import com.android.boilerplate.databinding.FragmentArticleBinding
import com.android.boilerplate.ui.article.activity.CustomerActivity
import com.android.boilerplate.ui.article.viewmodel.ArticleListViewModel
import com.android.boilerplate.ui.article.viewmodel.ArticleListViewState
import com.android.boilerplate.ui.sample.adapter.ArticleAdapter
import com.android.boilerplate.utils.EndlessRecyclerScrollListener
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment: Fragment(), ArticleAdapter.ArticleCallback, SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private var linearLayoutManager: LinearLayoutManager? = null
    private var articleAdapter: ArticleAdapter? = null
    private val viewModel: ArticleListViewModel by viewModels()
    private var scrollListener: EndlessRecyclerScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArticleBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeArticleList()
        setupArticleList()
        setupClickListeners()
       // viewModel.getArticleList(true)
    }

    private fun setupArticleList() = binding.run {
        binding.swipeRefreshLayout.setOnRefreshListener(this@ArticleFragment)
        articleAdapter = ArticleAdapter(requireActivity(), this@ArticleFragment)
        linearLayoutManager = LinearLayoutManager(context)
        articleRecyclerView.layoutManager = linearLayoutManager
        articleRecyclerView.adapter = articleAdapter
        setupScrollListener()
    }

    private fun setupScrollListener() {
        linearLayoutManager?.let {
            scrollListener = object : EndlessRecyclerScrollListener(it) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.getArticleList(false)
                }
            }
        }
        scrollListener?.let {
            it.visibleThreshold = 3
            binding.articleRecyclerView.addOnScrollListener(it)
        }
    }

    private fun observeArticleList() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.articleSharedFlow.collect { viewState ->
                handleViewState(viewState)
            }
        }
    }

    private fun handleViewState(viewState: ArticleListViewState) {
        when (viewState) {
            is ArticleListViewState.Loading -> Unit
            is ArticleListViewState.Success -> {
                binding.swipeRefreshLayout.isRefreshing =false
                if(viewModel.isFirstPage()){
                    articleAdapter?.clear()
                    articleAdapter?.appendData(viewState.articleListResponse?.data.orEmpty())
                }else{
                    articleAdapter?.appendData(viewState.articleListResponse?.data.orEmpty())
                }
            }
            is ArticleListViewState.PopupError -> {
                showPopupError(requireActivity(), childFragmentManager, viewState.errorCode, viewState.message)
            }
        }
    }


    private fun setupClickListeners() = binding.run {
        CustomersLinearLayout.setOnSingleClickListener {
            val intent = CustomerActivity.getIntent(requireActivity())
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(data: ArticleData) {
        Toast.makeText(requireActivity(), "Article ${data.name} is clicked.", Toast.LENGTH_LONG).show()
    }

    override fun onRefresh() {
        viewModel.getArticleList(true)
    }
}