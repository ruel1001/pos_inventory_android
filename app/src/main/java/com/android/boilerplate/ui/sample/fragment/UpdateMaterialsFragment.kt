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
import com.android.boilerplate.databinding.FragmentCreateMaintenanceBinding
import com.android.boilerplate.databinding.FragmentCreateMaterialsBinding
import com.android.boilerplate.databinding.FragmentPaymentCustomerBinding
import com.android.boilerplate.databinding.FragmentUpdateMaterialsBinding
import com.android.boilerplate.security.AuthEncryptedDataManager
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateMaterialViewState
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
class UpdateMaterialsFragment : Fragment() {
    private var _binding: FragmentUpdateMaterialsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateMaterialViewModel by viewModels()
    private var loadingDialog: CommonDialog? = null

    private val args: UpdateMaterialsFragmentArgs by this.navArgs()
    private val userInfo = AuthEncryptedDataManager().getProfile()
    private var billing_month = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpdateMaterialsBinding.inflate(
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


        materialNameEditText.setText(args.materialName)
        itemEditText.setText(args.item)
        quantityEditText.setText(args.quantity)
        amountEditText.setText(args.amount)


    }

    private fun setClickListener() = binding.run {
        deleteButton.setOnSingleClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.apply {
                setTitle("Delete Material")
                setMessage("Are you sure you want to delete ")
                setPositiveButton("Delete") { _, _ ->
                    viewModel.doRemoveMaterial(args.materialId)
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }
        saveButton.setOnSingleClickListener {
            if (materialNameEditText.text.toString().isEmpty()) {
                materialNameEditText.error = "Field is Required"
            }
            if (quantityEditText.text.toString().isEmpty()) {
                quantityEditText.error = "Field is Required"
            }

            if (itemEditText.text.toString().isEmpty()) {
                itemEditText.error = "Field is Required"
            }
            if (amountEditText.text.toString().isEmpty()) {
                amountEditText.error = "Field is Required"
            }


            if (materialNameEditText.text.toString()
                    .isNotEmpty() && quantityEditText.text.toString().isNotEmpty()
                && itemEditText.text.toString().isNotEmpty() && amountEditText.text.toString()
                    .isNotEmpty()
            ) {
                viewModel.doUpdateMaterial(
                    args.materialId,
                    materialNameEditText.text.toString(),
                    quantityEditText.text.toString(),
                    itemEditText.text.toString(),
                    amountEditText.text.toString(),
                    purchasedAmountEditText.text.toString()
                )

            } else {
                Toast.makeText(requireActivity(), "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeCreate() {
        lifecycleScope.launch {
            viewModel.materialSharedFlow.collect {
                handleViewState(it)
            }
        }
    }

    private fun handleViewState(viewState: CreateMaterialViewState) {
        when (viewState) {
            is CreateMaterialViewState.Loading -> showLoadingDialog(R.string.loading)
            is CreateMaterialViewState.Success -> {
                hideLoadingDialog()

                Toast.makeText(requireActivity(), viewState.message, Toast.LENGTH_SHORT).show()
            }

            is CreateMaterialViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(
                    requireActivity(),
                    childFragmentManager,
                    viewState.errorCode,
                    viewState.message
                )
            }

            is CreateMaterialViewState.InputError -> {
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
            return Intent(context, UpdateMaterialsFragment::class.java)
        }

        private const val PERMISSION_WRITE_EXTERNAL = 101
    }

}