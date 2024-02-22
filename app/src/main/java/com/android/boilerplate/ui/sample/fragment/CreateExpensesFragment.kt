package com.android.boilerplate.ui.sample.fragment

import android.app.AlertDialog
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.android.boilerplate.R
import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.databinding.FragmentCreateExpensesBinding
import com.android.boilerplate.databinding.FragmentCreateMaintenanceBinding
import com.android.boilerplate.databinding.FragmentCreateMaterialsBinding
import com.android.boilerplate.databinding.FragmentPaymentCustomerBinding
import com.android.boilerplate.security.AuthEncryptedDataManager
import com.android.boilerplate.ui.article.viewmodel.CreateExpensesViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateExpensesViewState
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewModel

import com.android.boilerplate.ui.article.viewmodel.CreateMaterialViewModel

import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewModel
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewState
import com.android.boilerplate.utils.dialog.CalendarDialog
import com.android.boilerplate.utils.dialog.CommonDialog
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateExpensesFragment: Fragment()  {
    private var _binding: FragmentCreateExpensesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateExpensesViewModel by viewModels()
    private var loadingDialog: CommonDialog? = null

    private val userInfo = AuthEncryptedDataManager().getProfile()
    private var billing_month = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateExpensesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCreate()
        setClickListener()

        setDataDisplay()
    }
    private fun setDataDisplay() = binding.run {



    }

    private fun setClickListener() = binding.run {

        saveButton.setOnSingleClickListener {
            if (natureOfExpensesNameEditText.text.toString().isEmpty()) {
                natureOfExpensesNameEditText.error = "Field is Required"
            }

            if (amountEditText.text.toString().isEmpty()) {
                amountEditText.error = "Field is Required"
            }


            if(natureOfExpensesNameEditText.text.toString().isNotEmpty()&&amountEditText.text.toString().isNotEmpty()) {
                viewModel.doCreateExpenses(
                    natureOfExpensesNameEditText.text.toString(),
                    amountEditText.text.toString(),
                    )

            }else{
                Toast.makeText(requireActivity(), "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeCreate() {
        lifecycleScope.launch {
            viewModel.expensesSharedFlow.collect {
                handleViewState(it)
            }
        }
    }

    private fun handleViewState(viewState: CreateExpensesViewState) {
        when (viewState) {
            is CreateExpensesViewState.Loading -> showLoadingDialog(R.string.loading)
            is CreateExpensesViewState.Success -> {
                hideLoadingDialog()

                Toast.makeText(requireActivity(), viewState.message, Toast.LENGTH_SHORT).show()
            }

            is CreateExpensesViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(
                    requireActivity(),
                    childFragmentManager,
                    viewState.errorCode,
                    viewState.message
                )
            }

            is CreateExpensesViewState.InputError -> {
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

    override fun onDestroy() {
        super.onDestroy()
        hideLoadingDialog()
    }


    companion object {
        private const val INVALID_ID = -1
        fun getIntent(context: Context): Intent {
            return Intent(context, CreateExpensesFragment::class.java)
        }

        private const val PERMISSION_WRITE_EXTERNAL = 101
    }

}