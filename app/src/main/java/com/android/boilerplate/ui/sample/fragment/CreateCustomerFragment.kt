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
import com.android.boilerplate.R
import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.data.repositories.customers.response.CustomerEachResponse
import com.android.boilerplate.databinding.FragmentCreateCustomerBinding
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewModel
import com.android.boilerplate.ui.article.viewmodel.CreateCustomerViewState
import com.android.boilerplate.utils.dialog.CalendarDialog
import com.android.boilerplate.utils.dialog.CommonDialog
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.android.boilerplate.utils.XP380PTPrinter

@AndroidEntryPoint
class CreateCustomerFragment : Fragment() {
    private var _binding: FragmentCreateCustomerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateCustomerViewModel by viewModels()
    private var loadingDialog: CommonDialog? = null
    private var dates = ""

    private lateinit var printer: XP380PTPrinter
    private var nowprint=false
    private var printData: CustomerEachResponse.CustomerEachData? =
        CustomerEachResponse.CustomerEachData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateCustomerBinding.inflate(
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



    private fun setClickListener() = binding.run {
        printButton.setOnSingleClickListener {
            bluprint(printData)


        }
        datePlanEditText.setOnSingleClickListener {
            CalendarDialog.newInstance(dates) { date ->
                datePlanEditText.setText(date)
            }.apply {
                format = "MM/dd/yyy"
            }.show(parentFragmentManager, CalendarDialog.TAG)
        }

        dueDateMonthEditText.setOnSingleClickListener {
            CalendarDialog.newInstance(dates) { date ->
                dueDateMonthEditText.setText(date)
            }.apply {
                format = "MM/dd/yyy"
            }.show(parentFragmentManager, CalendarDialog.TAG)
        }
        saveButton.setOnSingleClickListener {
            if (accountNameEditText.text.toString().isEmpty()) {
                accountNameEditText.error = "Field is Required"
            }

            if (addressEditText.text.toString().isEmpty()) {
                addressEditText.error = "Field is Required"
            }
            if (datePlanEditText.text.toString().isEmpty()) {
                datePlanEditText.error = "Field is Required"
            }
            if (dueDateMonthEditText.text.toString().isEmpty()) {
                dueDateMonthEditText.error = "Field is Required"
            }
            if (focEditText.text.toString().isEmpty()) {
                focEditText.error = "Field is Required"
            }
            if (modemEditText.text.toString().isEmpty()) {
                modemEditText.error = "Field is Required"
            }
            if (connectorEditText.text.toString().isEmpty()) {
                connectorEditText.error = "Field is Required"
            }
            if (ficampEditText.text.toString().isEmpty()) {
                ficampEditText.error = "Field is Required"
            }
            if (othersEditText.text.toString().isEmpty()) {
                othersEditText.error = "Field is Required"
            }
            if (messengerEditText.text.toString().isEmpty()) {
                messengerEditText.error = "Field is Required"
            }
            if (contactNumberEditText.text.toString().isEmpty()) {
                contactNumberEditText.error = "Field is Required and must be 11 digits"
            }
            if (arrearsEditText.text.toString().isEmpty()) {
                arrearsEditText.error = "Field is Required"
            } else {


                viewModel.doCreateCustomer(
                    accountNameEditText.text.toString(),
                    addressEditText.text.toString(),
                    datePlanEditText.text.toString(),
                    amountOfInstallationEditText.text.toString(),
                    dueDateMonthEditText.text.toString(),
                    focEditText.text.toString(),
                    modemEditText.text.toString(),
                    connectorEditText.text.toString(),
                    ficampEditText.text.toString(),
                    othersEditText.text.toString(),
                    messengerEditText.text.toString(),
                    contactNumberEditText.text.toString(),
                    "0",
                    arrearsEditText.text.toString(),
                    areaEditText.text.toString(),
                    planEditText.text.toString()
                )
            }
        }
    }

    private fun observeCreate() {
        lifecycleScope.launch {
            viewModel.customerSharedFlow.collect {
                handleViewState(it)
            }
        }
    }

    private fun handleViewState(viewState: CreateCustomerViewState) {
        when (viewState) {
            is CreateCustomerViewState.Loading -> showLoadingDialog(R.string.login_loading)
            is CreateCustomerViewState.Success -> {
                hideLoadingDialog()

                Toast.makeText(requireActivity(), viewState.message, Toast.LENGTH_SHORT).show()
            }

            is CreateCustomerViewState.SuccessEach -> {
                hideLoadingDialog()
                printData = viewState.response?.data
                nowprint=true
                bluprint(printData)

                Toast.makeText(requireActivity(), viewState.response?.message, Toast.LENGTH_SHORT).show()
            }

            is CreateCustomerViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(
                    requireActivity(),
                    childFragmentManager,
                    viewState.errorCode,
                    viewState.message
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

    private fun bluprint(data: CustomerEachResponse.CustomerEachData?) {
        if (nowprint) {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.apply {
                setTitle("Print")
                setMessage("Do you want to Print")
                setPositiveButton("Yes") { _, _ ->

                    printer.printTextHeader("Customer Information\n")
                    printer.printTextNormalHeader("\nAccount number: " + data?.account_number.toString())
                    printer.printTextNormalHeader("\nAccount name:  " + data?.account_name.toString())
                    printer.printTextNormalHeader("\nAddress:  " + data?.address.toString())
                    printer.printTextNormalHeader("\nDate Plan:  " + data?.date_plan.toString())
                    printer.printTextNormalHeader("\nAmount of Installation:  " + data?.amount_of_installation.toString())
                    printer.printTextNormalHeader("\nDue Date Month:  " + data?.due_date_month.toString())
                    printer.printTextNormalHeader("\nFOC:  " + data?.foc.toString())
                    printer.printTextNormalHeader("\nModem:  " + data?.modem.toString())
                    printer.printTextNormalHeader("\nConnector:  " + data?.connector.toString())
                    printer.printTextNormalHeader("\nFICAMP:  " + data?.ficamp.toString())
                    printer.printTextNormalHeader("\nOthers:  " + data?.others.toString())
                    printer.printTextNormalHeader("\nMessenger:  " + data?.messenger.toString())
                    printer.printTextNormalHeader("\nContact Number:  " + data?.contact_number.toString())
                    printer.printTextNormalHeader("\nAccount Balance:  " + data?.account_balance.toString())
                    printer.printTextNormalHeader("\nArrears:  " + data?.arrears.toString())
                    printer.printTextNormalHeader("\nPlan Subscribed:  " + data?.plan_subscribed.toString())
                    printer.printTextNormalHeader("\nThank you for choosing us\n\n\n\n")

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


    override fun onDestroy() {
        super.onDestroy()
        hideLoadingDialog()
        printer.closePrinterConnection()
    }


    companion object {
        private const val INVALID_ID = -1

        private const val REQUEST_ENABLE_BT = 1
        fun getIntent(context: Context): Intent {
            return Intent(context, CreateCustomerFragment::class.java)
        }

        private const val PERMISSION_WRITE_EXTERNAL = 101
    }

}