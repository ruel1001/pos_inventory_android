package com.android.boilerplate.ui.sample.fragment

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.android.boilerplate.R
import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.data.repositories.customers.response.CustomerEachResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.databinding.FragmentCreateMaintenanceBinding
import com.android.boilerplate.databinding.FragmentPaymentCustomerBinding
import com.android.boilerplate.security.AuthEncryptedDataManager
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateMaintenanceViewState
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewModel
import com.android.boilerplate.ui.article.viewmodel.CreatePaymentViewState
import com.android.boilerplate.ui.sample.dialog.MaterialListDialog
import com.android.boilerplate.utils.XP380PTPrinter
import com.android.boilerplate.utils.dialog.CalendarDialog
import com.android.boilerplate.utils.dialog.CommonDialog
import com.android.boilerplate.utils.dialog.ScannerDialog
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateMaintenanceFragment: Fragment()  {
    private var _binding: FragmentCreateMaintenanceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateMaintenanceViewModel by viewModels()
    private var loadingDialog: CommonDialog? = null

    private val args: CreateMaintenanceFragmentArgs by this.navArgs()
    private val userInfo = AuthEncryptedDataManager().getProfile()
    private var materialID = ""

    private lateinit var printer: XP380PTPrinter
    private var nowprint=false
    private var printData: SearchMaintenanceResponse.MaintenanceSearchData? =
        SearchMaintenanceResponse.MaintenanceSearchData()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateMaintenanceBinding.inflate(
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


        printer = XP380PTPrinter(requireActivity())
        printer.connectPrinter()
        checkBluetooth()
    }

    private fun checkBluetooth() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {

            // Device doesn't support Bluetooth
            Toast.makeText(requireContext(), "Bluetooth is not Connected", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (!bluetoothAdapter.isEnabled) {
                // Bluetooth is not enabled, request to enable it


                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // Bluetooth is enabled
                printer.connectPrinter()
                Toast.makeText(requireContext(), "Bluetooth is enabled", Toast.LENGTH_SHORT).show()
            } else {
                // User declined to enable Bluetooth, handle accordingly
                Toast.makeText(
                    requireContext(),
                    "Bluetooth is required for printing",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun setDataDisplay() = binding.run {
        accountNumberEditText.setText(args?.accountNumber)
        accountNameEditText.setText(args?.accountName)
        addressEditText.setText(args?.address)


    }

    private fun setClickListener() = binding.run {

        printButton.setOnSingleClickListener {
            bluprint(printData)


        }
        materialUsedEditText.setOnSingleClickListener {
            var materialListDialog =
                MaterialListDialog.newInstance(object : MaterialListDialog.ApproveCallBack {

                    override fun onSuccess(itemName: String, itemID: String,itemQuantity: String) {
                       materialUsedEditText.setText(itemName)
                        materialID=itemID

                    }
                })
            materialListDialog.show(childFragmentManager, MaterialListDialog.TAG)
        }

        saveButton.setOnSingleClickListener {
            if (accountNumberEditText.text.toString().isEmpty()) {
                accountNumberEditText.error = "Field is Required"
            }
            if (accountNameEditText.text.toString().isEmpty()) {
                accountNameEditText.error = "Field is Required"
            }

            if (addressEditText.text.toString().isEmpty()) {
                addressEditText.error = "Field is Required"
            }
            if (addressEditText.text.toString().isEmpty()) {
                addressEditText.error = "Field is Required"
            }
            if (areaEditText.text.toString().isEmpty()) {
                areaEditText.error = "Field is Required"
            }
            if (materialUsedEditText.text.toString().isEmpty()) {
                materialUsedEditText.error = "Field is Required"
            }
            if (natureOfRepairEditText.text.toString().isEmpty()) {
                natureOfRepairEditText.error = "Field is Required"
            }

            if(materialUsedEditText.text.toString().isNotEmpty()&&natureOfRepairEditText.text.toString().isNotEmpty()) {
                viewModel.doCreateMaintenance(
                    accountNumberEditText.text.toString(),
                    accountNameEditText.text.toString(),
                    addressEditText.text.toString(),
                    areaEditText.text.toString(),
                    materialUsedEditText.text.toString(),
                    natureOfRepairEditText.text.toString(),
                    materialID,
                    quantityEditText.text.toString()
                    )

            }else{
                Toast.makeText(requireActivity(), "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bluprint(data: SearchMaintenanceResponse.MaintenanceSearchData?) {
        if (nowprint) {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.apply {
                setTitle("Print")
                setMessage("Do you want to Print")
                setPositiveButton("Yes") { _, _ ->

                    printer.printTextHeader("-----Maintenance Information-----\n")
                    printer.printTextNormalHeader("\nAccount number: " + data?.account_number.toString())
                    printer.printTextNormalHeader("\nAccount name:  " + data?.account_name.toString())
                    printer.printTextNormalHeader("\nAddress:  " + data?.address.toString())
                    printer.printTextNormalHeader("\nArea  " + data?.area.toString())
                    printer.printTextNormalHeader("\nMaterial Used:  " + data?.material_used.toString())
                    printer.printTextNormalHeader("\nNature of Repair:  " + data?.nature_of_repair.toString())
                    printer.printTextNormalHeader("\nQuantity Used:  " + data?.material_quantity_used.toString())
                    printer.printTextNormalHeader("\nThank you for choosing us\n\n\n\n" )

                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } else {
            Toast.makeText(requireContext(), "No Data found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeCreate() {
        lifecycleScope.launch {
            viewModel.maintenanceSharedFlow.collect {
                handleViewState(it)
            }
        }
    }

    private fun handleViewState(viewState: CreateMaintenanceViewState) {
        when (viewState) {
            is CreateMaintenanceViewState.Loading -> showLoadingDialog(R.string.loading)
            is CreateMaintenanceViewState.SuccessCrud -> {
                hideLoadingDialog()

                printData = viewState.response?.data
                nowprint=true
                bluprint(printData)

                Toast.makeText(requireActivity(), viewState.response?.message, Toast.LENGTH_SHORT).show()
            }

            is CreateMaintenanceViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(
                    requireActivity(),
                    childFragmentManager,
                    viewState.errorCode,
                    viewState.message
                )
            }

            is CreateMaintenanceViewState.InputError -> {
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
        printer.closePrinterConnection()
    }


    companion object {
        private const val INVALID_ID = -1
        private const val REQUEST_ENABLE_BT = 1
        fun getIntent(context: Context): Intent {
            return Intent(context, CreateMaintenanceFragment::class.java)
        }

        private const val PERMISSION_WRITE_EXTERNAL = 101
    }

}