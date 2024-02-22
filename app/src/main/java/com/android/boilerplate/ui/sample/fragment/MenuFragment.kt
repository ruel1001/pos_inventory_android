package com.android.boilerplate.ui.sample.fragment

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
import com.android.boilerplate.R
import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.databinding.FragmentMenuBinding
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewState
import com.android.boilerplate.ui.article.viewmodel.CreateExpensesViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateExpensesViewState
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewState
import com.android.boilerplate.ui.article.viewmodel.CreateMaterialViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateMaterialViewState
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewModel
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewState
import com.android.boilerplate.ui.sample.dialog.MaterialDialog
import com.android.boilerplate.ui.sample.activity.MainActivity
import com.android.boilerplate.ui.sample.dialog.CustomerDialog
import com.android.boilerplate.ui.sample.dialog.ExpensesDialog
import com.android.boilerplate.ui.sample.dialog.MaintenanceDialog
import com.android.boilerplate.ui.sample.dialog.PaymentDialog
import com.android.boilerplate.utils.dialog.CommonDialog
import com.android.boilerplate.utils.dialog.ScannerDialog
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val activity by lazy { requireActivity() as MainActivity }
    private val viewModel: CreateCustomerViewModel by viewModels()
    private val paymentviewmodel: CreatePaymentViewModel by viewModels()
    private val maintenanceViewModel: CreateMaintenanceViewModel by viewModels()
    private val materialViewModel: CreateMaterialViewModel by viewModels()
    private val expensesViewModel: CreateExpensesViewModel by viewModels()
    private var loadingDialog: CommonDialog? = null
    private var onUpdateCustomer = false
    private var onCreateCustomer = false
    private var onDeleteCustomer = false
    private var onCreatePayment = false
    private var onUpdatePayment = false


    private var onCreateMaintenance = false
    private var onDeleteMaintenance = false
    private var onCreateMaterial = false
    private var onUpdateMaterial = false
    private var onCreateExpenses = false
    private var onUpdateExpenses = false
    private var onSales = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setNoToolbar()
        setupClickListeners()
        observeCreate()
        observePayment()
        observeMaintenance()
        observeMaterial()
        observeExpenses()
    }


    private fun setupClickListeners() = binding.run {
        CustomersLinearLayout.setOnSingleClickListener {
            CustomerDialog.newInstance(object : CustomerDialog.ApproveCallBack {
                override fun onSuccess(
                    isCreate: Boolean, isUpdate: Boolean, isDelete: Boolean, isSearch: Boolean
                ) {
                    if (isCreate) {
                        findNavController().navigate(
                            MenuFragmentDirections.actionCreateCustomer()
                        )
                    }
                    if (isUpdate) {
                        var scanDialog =
                            ScannerDialog.newInstance(object : ScannerDialog.ScannerListener {
                                override fun onScannerSuccess(qrValue: String) {
                                    viewModel.doSearchCustomer(qrValue.toString())
                                    onUpdateCustomer = true
                                }
                            })
                        scanDialog.show(childFragmentManager, ScannerDialog.TAG)
                    }

                    if (isSearch) {
                        findNavController().navigate(
                            MenuFragmentDirections.actionSearchByNames()
                        )
                    }
                }
            }).show(childFragmentManager, CustomerDialog.TAG)
        }
        paymentLinearLayout.setOnSingleClickListener {
            PaymentDialog.newInstance(object : PaymentDialog.ApproveCallBack {
                override fun onSuccess(
                    isCreate: Boolean, isUpdate: Boolean, isDelete: Boolean, isSearch: Boolean
                ) {
                    if (isCreate) {
                        var scanDialog =
                            ScannerDialog.newInstance(object : ScannerDialog.ScannerListener {
                                override fun onScannerSuccess(qrValue: String) {
                                    viewModel.doSearchCustomer(qrValue.toString())
                                    onCreatePayment = true

                                }
                            })
                        scanDialog.show(childFragmentManager, ScannerDialog.TAG)
                    }
                    if (isUpdate) {
                        var scanDialog =
                            ScannerDialog.newInstance(object : ScannerDialog.ScannerListener {
                                override fun onScannerSuccess(qrValue: String) {
                                    paymentviewmodel.doFilterPayment(qrValue.toString())
                                    onUpdatePayment = true
                                }
                            })
                        scanDialog.show(childFragmentManager, ScannerDialog.TAG)
                    }

                    if (isDelete) {
                        onDeleteCustomer = true
                    }


                    if (isSearch) {
                        findNavController().navigate(
                            MenuFragmentDirections.actionGoPaymentList()
                        )
                    }
                }
            }).show(childFragmentManager, PaymentDialog.TAG)


        }

        maintainLinearLayout.setOnSingleClickListener {
            MaintenanceDialog.newInstance(object : MaintenanceDialog.ApproveCallBack {
                override fun onSuccess(
                    isCreate: Boolean, isUpdate: Boolean, isDelete: Boolean, isSearch: Boolean
                ) {
                    if (isCreate) {
                        var scanDialog =
                            ScannerDialog.newInstance(object : ScannerDialog.ScannerListener {
                                override fun onScannerSuccess(qrValue: String) {
                                    viewModel.doSearchCustomer(qrValue.toString())
                                    onCreateMaintenance = true

                                }
                            })
                        scanDialog.show(childFragmentManager, ScannerDialog.TAG)
                    }
                    if (isUpdate) {
                        var scanDialog =
                            ScannerDialog.newInstance(object : ScannerDialog.ScannerListener {
                                override fun onScannerSuccess(qrValue: String) {
                                    maintenanceViewModel.doFilterMaintenance(qrValue.toString())
                                    onUpdatePayment = true
                                }
                            })
                        scanDialog.show(childFragmentManager, ScannerDialog.TAG)
                    }

                    if (isDelete) {
                        onSales = true
                        findNavController().navigate(
                            MenuFragmentDirections.actionGoSalesList()
                        )

                    }


                    if (isSearch) {

                    }
                }
            }).show(childFragmentManager, MaintenanceDialog.TAG)
        }


        materialPurchaseLinearLayout.setOnSingleClickListener {
            MaterialDialog.newInstance(object : MaterialDialog.ApproveCallBack {
                override fun onSuccess(
                    isCreate: Boolean, isUpdate: Boolean, isDelete: Boolean, isSearch: Boolean
                ) {
                    if (isCreate) {
                        findNavController().navigate(
                            MenuFragmentDirections.actionCreateMaterial()
                        )

                    }
                    if (isUpdate) {
                        var scanDialog =
                            ScannerDialog.newInstance(object : ScannerDialog.ScannerListener {
                                override fun onScannerSuccess(qrValue: String) {
                                    materialViewModel.doFilterMaterial(qrValue.toString())
                                    onUpdateMaterial = true
                                }
                            })
                        scanDialog.show(childFragmentManager, ScannerDialog.TAG)
                    }

                    if (isDelete) {
                        onDeleteCustomer = true
                    }

                    if (isSearch) {
                        findNavController().navigate(
                            MenuFragmentDirections.actionMaterialAll()
                        )

                    }
                }
            }).show(childFragmentManager, MaterialDialog.TAG)


        }




        expensesLinearLayout.setOnSingleClickListener {
            ExpensesDialog.newInstance(object : ExpensesDialog.ApproveCallBack {
                override fun onSuccess(
                    isCreate: Boolean, isUpdate: Boolean, isDelete: Boolean, isSearch: Boolean
                ) {
                    if (isCreate) {
                        findNavController().navigate(
                            MenuFragmentDirections.actionCreateExpenses()
                        )

                    }
                    if (isUpdate) {
                        var scanDialog =
                            ScannerDialog.newInstance(object : ScannerDialog.ScannerListener {
                                override fun onScannerSuccess(qrValue: String) {
                                    expensesViewModel.doFilterExpenses(qrValue.toString())
                                    onUpdateExpenses = true
                                }
                            })
                        scanDialog.show(childFragmentManager, ScannerDialog.TAG)
                    }
                    if (isSearch) {
                        findNavController().navigate(
                            MenuFragmentDirections.actionGoExpensesList()
                        )

                    }



                }
            }).show(childFragmentManager, ExpensesDialog.TAG)


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun observeCreate() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.customerSharedFlow.collect {
                    handleViewState(it)
                }
            }
        }
    }

    private fun handleViewState(viewState: CreateCustomerViewState) {
        when (viewState) {
            is CreateCustomerViewState.Loading -> showLoadingDialog(R.string.loading)
            is CreateCustomerViewState.Success -> {
                hideLoadingDialog()
                Toast.makeText(requireActivity(), viewState.message, Toast.LENGTH_SHORT).show()
            }

            is CreateCustomerViewState.SuccessSearch -> {
                hideLoadingDialog()

                if (onCreatePayment) {
                    findNavController().navigate(
                        MenuFragmentDirections.actionCreatePayment(
                            viewState?.response?.account_number.toString(),
                            viewState?.response?.account_name.toString(),
                            viewState?.response?.address.toString(),
                            viewState?.response?.account_balance.toString(),
                            viewState?.response?.arrears.toString(),
                            viewState?.response?.plan_subscribed.toString()
                        )
                    )
                }

                if (onCreateMaintenance) {
                    findNavController().navigate(
                        MenuFragmentDirections.actionCreateMaintenance(
                            viewState?.response?.account_number.toString(),
                            viewState?.response?.account_name.toString(),
                            viewState?.response?.address.toString(),

                            )
                    )
                }


                if (onUpdateCustomer) {
                    findNavController().navigate(
                        MenuFragmentDirections.actionUpdateCustomer(
                            viewState?.response?.account_number.toString(),

                            )
                    )
                }


            }

            is CreateCustomerViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(
                    requireActivity(), childFragmentManager, viewState.errorCode, viewState.message
                )
            }

            is CreateCustomerViewState.InputError -> {
                hideLoadingDialog()
                handleInputError(viewState.errorData ?: ErrorsData())
            }

            else -> Unit
        }
    }


    private fun handleInputError(errorsData: ErrorsData) {

    }

    private fun showLoadingDialog(@StringRes strId: Int) {
        if (loadingDialog == null) {
            loadingDialog = CommonDialog.getLoadingDialogInstance(
                message = getString(strId)
            )
        }
        loadingDialog?.show(childFragmentManager)
    }


    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }


    private fun observePayment() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                paymentviewmodel.paymentSharedFlow.collect {
                    handlePaymentViewState(it)
                }
            }
        }
    }

    private fun handlePaymentViewState(viewState: CreatePaymentViewState) {
        when (viewState) {
            is CreatePaymentViewState.Loading -> showLoadingDialog(R.string.loading)
            is CreatePaymentViewState.Success -> {
                hideLoadingDialog()
                Toast.makeText(requireActivity(), viewState.message, Toast.LENGTH_SHORT).show()
            }

            is CreatePaymentViewState.SuccessFilter -> {
                hideLoadingDialog()
                if (onUpdatePayment) {
                    findNavController().navigate(
                        MenuFragmentDirections.actionPaymentFilter(
                            viewState.response?.payment_id.toString(),
                            viewState.response?.account_number.toString(),
                            viewState.response?.account_name.toString(),
                            viewState.response?.account_balance.toString(),
                            viewState.response?.arrears_month.toString(),
                            viewState.response?.amount_paid.toString(),
                            viewState.response?.collectors_name.toString(),
                            viewState.response?.billing_month.toString(),
                            viewState.response?.created_at.toString(),
                            viewState.response?.plan_subscribed.toString()
                        )
                    )
                }
            }

            is CreatePaymentViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(
                    requireActivity(), childFragmentManager, viewState.errorCode, viewState.message
                )
            }

            is CreatePaymentViewState.InputError -> {
                hideLoadingDialog()
                handleInputError(viewState.errorData ?: ErrorsData())
            }

            else -> Unit
        }
    }


    private fun observeMaintenance() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                maintenanceViewModel.maintenanceSharedFlow.collect {
                    handleMaintenanceViewState(it)
                }
            }
        }
    }


    private fun handleMaintenanceViewState(viewState: CreateMaintenanceViewState) {
        when (viewState) {
            is CreateMaintenanceViewState.Loading -> showLoadingDialog(R.string.loading)
            is CreateMaintenanceViewState.Success -> {
                hideLoadingDialog()
                Toast.makeText(requireActivity(), viewState.message, Toast.LENGTH_SHORT).show()
            }

            is CreateMaintenanceViewState.SuccessFilter -> {
                hideLoadingDialog()
                if (onUpdatePayment) {
                    findNavController().navigate(
                        MenuFragmentDirections.actionUpdateMaintenance(
                            viewState.response?.maintenance_id.toString(),
                            viewState.response?.account_number.toString(),
                            viewState.response?.account_name.toString(),
                            viewState.response?.address.toString(),
                            viewState.response?.area.toString(),
                            viewState.response?.material_used.toString(),
                            viewState.response?.nature_of_repair.toString(),
                            viewState.response?.material_id.toString(),
                            viewState.response?.material_quantity_used.toString()
                            )
                    )


                }
            }

            is CreateMaintenanceViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(
                    requireActivity(), childFragmentManager, viewState.errorCode, viewState.message
                )
            }

            is CreateMaintenanceViewState.InputError -> {
                hideLoadingDialog()
                handleInputError(viewState.errorData ?: ErrorsData())
            }

            else -> Unit
        }
    }


    private fun observeMaterial() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                materialViewModel.materialSharedFlow.collect {
                    handleMaterialViewState(it)
                }
            }
        }
    }


    private fun handleMaterialViewState(viewState: CreateMaterialViewState) {
        when (viewState) {
            is CreateMaterialViewState.Loading -> showLoadingDialog(R.string.loading)
            is CreateMaterialViewState.Success -> {
                hideLoadingDialog()
                Toast.makeText(requireActivity(), viewState.message, Toast.LENGTH_SHORT).show()
            }

            is CreateMaterialViewState.SuccessFilter -> {
                hideLoadingDialog()
                if (onUpdateMaterial) {
                    findNavController().navigate(
                        MenuFragmentDirections.actionUpdateMaterial(
                            viewState.response?.material_id.toString(),
                            viewState.response?.material_name.toString(),
                            viewState.response?.quantity.toString(),
                            viewState.response?.item.toString(),
                            viewState.response?.amount.toString(),
                            viewState.response?.created_at.toString(),

                            )
                    )


                }
            }

            is CreateMaterialViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(
                    requireActivity(), childFragmentManager, viewState.errorCode, viewState.message
                )
            }

            is CreateMaterialViewState.InputError -> {
                hideLoadingDialog()
                handleInputError(viewState.errorData ?: ErrorsData())
            }

            else -> Unit
        }
    }


    private fun observeExpenses() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                expensesViewModel.expensesSharedFlow.collect {
                    handleExpensesViewState(it)
                }
            }
        }
    }


    private fun handleExpensesViewState(viewState: CreateExpensesViewState) {
        when (viewState) {
            is CreateExpensesViewState.Loading -> showLoadingDialog(R.string.loading)
            is CreateExpensesViewState.Success -> {
                hideLoadingDialog()
                Toast.makeText(requireActivity(), viewState.message, Toast.LENGTH_SHORT).show()
            }

            is CreateExpensesViewState.SuccessFilter -> {
                hideLoadingDialog()
                if (onUpdateExpenses) {
                    findNavController().navigate(
                        MenuFragmentDirections.actionUpdateExpenses(
                            viewState.response?.expenses_id.toString(),
                            viewState.response?.nature_of_expenses.toString(),
                            viewState.response?.amount.toString(),
                            viewState.response?.created_at.toString(),
                            )
                    )


                }
            }

            is CreateExpensesViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(
                    requireActivity(), childFragmentManager, viewState.errorCode, viewState.message
                )
            }

            is CreateExpensesViewState.InputError -> {
                hideLoadingDialog()
                handleInputError(viewState.errorData ?: ErrorsData())
            }

            else -> Unit
        }
    }
}