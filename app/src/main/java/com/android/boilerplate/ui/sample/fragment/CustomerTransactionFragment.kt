package com.android.boilerplate.ui.sample.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.boilerplate.R
import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import com.android.boilerplate.databinding.FragmentCreateCustomerBinding
import com.android.boilerplate.databinding.FragmentCustomerTransactionBinding
import com.android.boilerplate.ui.article.viewmodel.ArticleListViewModel
import com.android.boilerplate.ui.article.viewmodel.ArticleListViewState
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewState
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewModel
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewState
import com.android.boilerplate.ui.sample.adapter.ArticleAdapter
import com.android.boilerplate.ui.sample.adapter.PaymentAdapter
import com.android.boilerplate.utils.EndlessRecyclerScrollListener
import com.android.boilerplate.utils.dialog.CommonDialog
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerTransactionFragment: Fragment()  ,PaymentAdapter.ArticleCallback{
    private var _binding: FragmentCustomerTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreatePaymentViewModel by viewModels()
    private var loadingDialog: CommonDialog? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var paymentAdapter: PaymentAdapter? = null
    private var scrollListener: EndlessRecyclerScrollListener? = null
    private val args: CustomerTransactionFragmentArgs by this.navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCustomerTransactionBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListener()
        setupPaymentList()
        observeArticleList()

        viewModel.getPaymentList(args.accountNumber)
    }

    private fun setClickListener() = binding.run {

    }

    private fun setupPaymentList() = binding.run {

        paymentAdapter = PaymentAdapter(requireActivity(), this@CustomerTransactionFragment)
        linearLayoutManager = LinearLayoutManager(context)
        payementRecyclerView.layoutManager = linearLayoutManager
        payementRecyclerView.adapter = paymentAdapter
        setupScrollListener()
    }

    private fun setupScrollListener() {
        linearLayoutManager?.let {
            scrollListener = object : EndlessRecyclerScrollListener(it) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.getPaymentList(args.accountNumber)
                }
            }
        }
        scrollListener?.let {
            it.visibleThreshold = 3
            binding.payementRecyclerView.addOnScrollListener(it)
        }
    }

    private fun observeArticleList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.paymentSharedFlow.collect { viewState ->
                    handleViewState(viewState)
                }
            }
        }
    }

    private fun handleViewState(viewState: CreatePaymentViewState) {
        when (viewState) {
            is CreatePaymentViewState.Loading -> Unit
            is CreatePaymentViewState.SuccessList -> {
                paymentAdapter?.clear()
                    paymentAdapter?.appendData(viewState.response?.data.orEmpty())

            }
            is CreatePaymentViewState.PopupError -> {
                showPopupError(requireActivity(), childFragmentManager, viewState.errorCode, viewState.message)
            }
            else -> Unit
        }
    }





    companion object {
        private const val INVALID_ID = -1
        fun getIntent(context: Context): Intent {
            return Intent(context, CustomerTransactionFragment::class.java)
        }

        private const val PERMISSION_WRITE_EXTERNAL = 101
    }

    override fun onItemClicked(data: ShowPaymentResponse.ShowPaymentData) {
        findNavController().navigate(
            MenuFragmentDirections.actionPaymentFilter(data.payment_id.toString(),
                data.account_number.toString(),
                data.account_name.toString(),
                data.account_balance.toString(),
                data.arrears_month.toString(),
                data.amount_paid.toString(),
                data.collectors_name.toString(),
                data.billing_month.toString(),
                data.created_at.toString(),
                data.plan_subscribed.toString()
            )
        )
    }

}