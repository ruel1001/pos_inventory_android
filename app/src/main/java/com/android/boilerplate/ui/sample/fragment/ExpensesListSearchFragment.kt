package com.android.boilerplate.ui.sample.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.android.boilerplate.data.repositories.customers.response.SearchListNameData
import com.android.boilerplate.data.repositories.expenses.response.ExpensesAllResponse
import com.android.boilerplate.data.repositories.maintenance.response.SalesAllResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse.ShowListMaintenanceData
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import com.android.boilerplate.databinding.FragmentCreateCustomerBinding
import com.android.boilerplate.databinding.FragmentCustomerMaintenanceBinding
import com.android.boilerplate.databinding.FragmentCustomerSaerchNameBinding
import com.android.boilerplate.databinding.FragmentCustomerTransactionBinding
import com.android.boilerplate.databinding.FragmentExpensesSaerchBinding
import com.android.boilerplate.databinding.FragmentSalesSaerchBinding
import com.android.boilerplate.ui.article.viewmodel.ArticleListViewModel
import com.android.boilerplate.ui.article.viewmodel.ArticleListViewState
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewState
import com.android.boilerplate.ui.article.viewmodel.CreateExpensesViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateExpensesViewState
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewState
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewModel
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewState
import com.android.boilerplate.ui.sample.adapter.ArticleAdapter
import com.android.boilerplate.ui.sample.adapter.CustomerNamesAdapter
import com.android.boilerplate.ui.sample.adapter.ExpensesListAdapter
import com.android.boilerplate.ui.sample.adapter.MaintenanceAdapter
import com.android.boilerplate.ui.sample.adapter.PaymentAdapter
import com.android.boilerplate.ui.sample.adapter.SalesListAdapter
import com.android.boilerplate.utils.EndlessRecyclerScrollListener
import com.android.boilerplate.utils.dialog.CommonDialog
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpensesListSearchFragment : Fragment(), ExpensesListAdapter.ArticleCallback {
    private var _binding: FragmentExpensesSaerchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateExpensesViewModel by viewModels()
    private var loadingDialog: CommonDialog? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var expseAdapter: ExpensesListAdapter? = null
    private var scrollListener: EndlessRecyclerScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentExpensesSaerchBinding.inflate(
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
        viewModel.getExpensesAllSales("")
        binding.searchNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used in this example
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Text is changing
            }

            override fun afterTextChanged(s: Editable?) {
                // After the text has changed
                val newText = s.toString()
                viewModel.getExpensesAllSales(newText)
            }
        })

    }

    private fun setClickListener() = binding.run {

    }

    private fun setupPaymentList() = binding.run {

        expseAdapter = ExpensesListAdapter(requireActivity(), this@ExpensesListSearchFragment)
        linearLayoutManager = LinearLayoutManager(context)
        nameslistRecyclerView.layoutManager = linearLayoutManager
        nameslistRecyclerView.adapter = expseAdapter
        setupScrollListener()
    }

    private fun setupScrollListener() {
        linearLayoutManager?.let {
            scrollListener = object : EndlessRecyclerScrollListener(it) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.getExpensesAllSales(binding.searchNameEditText.text.toString())
                }
            }
        }
        scrollListener?.let {
            it.visibleThreshold = 3
            binding.nameslistRecyclerView.addOnScrollListener(it)
        }
    }

    private fun observeArticleList() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expensesSharedFlow.collect { viewState ->
                    handleViewState(viewState)
                }
            }
        }
    }

    private fun handleViewState(viewState: CreateExpensesViewState) {
        when (viewState) {
            is CreateExpensesViewState.Loading -> Unit
            is CreateExpensesViewState.SuccessExpenseALl -> {
                expseAdapter?.clear()
                expseAdapter?.appendData(viewState.response?.data.orEmpty())
                binding.salesTextView.setText(viewState?.response?.total_amount_expenses)

            }

            is CreateExpensesViewState.PopupError -> {
                expseAdapter?.clear()
            }

            else -> Unit
        }
    }


    companion object {
        private const val INVALID_ID = -1
        fun getIntent(context: Context): Intent {
            return Intent(context, ExpensesListSearchFragment::class.java)
        }

        private const val PERMISSION_WRITE_EXTERNAL = 101
    }






    override fun onItemClicked(data: ExpensesAllResponse.ExpensesAllData) {
        findNavController().navigate(
            ExpensesListSearchFragmentDirections.actionGoUpdateExpenses(
                data.expenses_id.toString(),
                data.nature_of_expenses.toString(),
                data.amount.toString(),
                data.created_at.toString()

            )
        )
    }

}