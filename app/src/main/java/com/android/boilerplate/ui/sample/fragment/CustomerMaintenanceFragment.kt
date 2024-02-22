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
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse.ShowListMaintenanceData
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import com.android.boilerplate.databinding.FragmentCreateCustomerBinding
import com.android.boilerplate.databinding.FragmentCustomerMaintenanceBinding
import com.android.boilerplate.databinding.FragmentCustomerTransactionBinding
import com.android.boilerplate.ui.article.viewmodel.ArticleListViewModel
import com.android.boilerplate.ui.article.viewmodel.ArticleListViewState
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewState
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewState
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewModel
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewState
import com.android.boilerplate.ui.sample.adapter.ArticleAdapter
import com.android.boilerplate.ui.sample.adapter.MaintenanceAdapter
import com.android.boilerplate.ui.sample.adapter.PaymentAdapter
import com.android.boilerplate.utils.EndlessRecyclerScrollListener
import com.android.boilerplate.utils.dialog.CommonDialog
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerMaintenanceFragment : Fragment(), MaintenanceAdapter.ArticleCallback {
    private var _binding: FragmentCustomerMaintenanceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateMaintenanceViewModel by viewModels()
    private var loadingDialog: CommonDialog? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var maintenanceAdapter: MaintenanceAdapter? = null
    private var scrollListener: EndlessRecyclerScrollListener? = null
    private val args: CustomerTransactionFragmentArgs by this.navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCustomerMaintenanceBinding.inflate(
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

        viewModel.getMaintenance(args.accountNumber)
    }

    private fun setClickListener() = binding.run {

    }

    private fun setupPaymentList() = binding.run {

        maintenanceAdapter = MaintenanceAdapter(requireActivity(), this@CustomerMaintenanceFragment)
        linearLayoutManager = LinearLayoutManager(context)
        payementRecyclerView.layoutManager = linearLayoutManager
        payementRecyclerView.adapter = maintenanceAdapter
        setupScrollListener()
    }

    private fun setupScrollListener() {
        linearLayoutManager?.let {
            scrollListener = object : EndlessRecyclerScrollListener(it) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.getMaintenance(args.accountNumber)
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
                viewModel.maintenanceSharedFlow.collect { viewState ->
                    handleViewState(viewState)
                }
            }
        }
    }

    private fun handleViewState(viewState: CreateMaintenanceViewState) {
        when (viewState) {
            is CreateMaintenanceViewState.Loading -> Unit
            is CreateMaintenanceViewState.SuccessList -> {
                maintenanceAdapter?.clear()
                maintenanceAdapter?.appendData(viewState.response?.data.orEmpty())

            }

            is CreateMaintenanceViewState.PopupError -> {
                showPopupError(
                    requireActivity(),
                    childFragmentManager,
                    viewState.errorCode,
                    viewState.message
                )
            }

            else -> Unit
        }
    }


    companion object {
        private const val INVALID_ID = -1
        fun getIntent(context: Context): Intent {
            return Intent(context, CustomerMaintenanceFragment::class.java)
        }

        private const val PERMISSION_WRITE_EXTERNAL = 101
    }

    override fun onItemClicked(data: ShowListMaintenanceData) {
        findNavController().navigate(
            CustomerMaintenanceFragmentDirections.actionMaintenanceFromListFilter(
                data.maintenance_id.toString(),
                data.account_number.toString(),
                data.account_name.toString(),
                data.address.toString(),
                data.area.toString(),
                data.material_used.toString(),
                data.nature_of_repair.toString(),
                data.material_id.toString(),
                data.material_quantity_used.toString()
            )
        )
    }

}