package com.android.boilerplate.ui.sample.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.boilerplate.R
import com.android.boilerplate.data.model.ErrorModel
import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.databinding.ActivityLoginBinding
import com.android.boilerplate.databinding.ActivityRegisterBinding
import com.android.boilerplate.ui.sample.viewmodel.LoginViewModel
import com.android.boilerplate.ui.sample.viewmodel.LoginViewState
import com.android.boilerplate.utils.dialog.CommonDialog
import com.android.boilerplate.utils.dialog.CommonsErrorDialog
import com.android.boilerplate.utils.setOnSingleClickListener
import com.android.boilerplate.utils.showPopupError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var loadingDialog: CommonDialog? = null
    private val viewModel: LoginViewModel by viewModels()
    val roles = listOf("admin", "staff")
    var finalrole=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupClickListener()
        observeLogin()
        setupSpinner()
    }

    private fun setupClickListener() = binding.run{
        loginButton.setOnSingleClickListener {
            viewModel.doRegisterAccount(
                nameEditText.text.toString(),
                emailEditText.text.toString(),
                addressEditText.text.toString(),
                finalrole,
                usernameEditText.text.toString(),
                passEditText.text.toString(),

            )
        }
    }

    private fun setupSpinner() = binding.run {
        ArrayAdapter.createFromResource(
            this@RegisterActivity,
            R.array.spinner_roles,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            roleSpinner.adapter = adapter
        }

        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {

                val selectedItem = parent?.getItemAtPosition(position).toString()
               if (selectedItem.equals("Admin")){
                   finalrole="admin"
               }
                if (selectedItem.equals("Staff")){
                    finalrole="staff"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    private fun observeLogin(){
        lifecycleScope.launch {
            viewModel.loginSharedFlow.collect{
                handleViewState(it)
            }
        }
    }

    private fun handleViewState(viewState: LoginViewState){
        when(viewState){
            is LoginViewState.Loading -> showLoadingDialog(R.string.loading)
            is LoginViewState.Success -> {
                hideLoadingDialog()
                val intent = LoginActivity.getIntent(this@RegisterActivity)
               startActivity(intent)
                Toast.makeText(this, viewState.message, Toast.LENGTH_SHORT).show()
            }
            is LoginViewState.PopupError -> {
                hideLoadingDialog()
                showPopupError(this@RegisterActivity, supportFragmentManager, viewState.errorCode, viewState.message)
            }
            is LoginViewState.InputError -> {
                hideLoadingDialog()
                handleInputError(viewState.errorData?: ErrorsData())
            }
            else -> Unit
        }
    }

    private fun handleInputError(errorsData: ErrorsData){
        if (errorsData.email?.get(0)?.isNotEmpty() == true) binding.emailTextInputLayout.error = errorsData.email?.get(0)
        if (errorsData.password?.get(0)?.isNotEmpty() == true) binding.passTextInputLayout.error = errorsData.password?.get(0)
        if (errorsData.address?.get(0)?.isNotEmpty() == true) binding.addressTextInputLayout.error = errorsData.password?.get(0)
    }

    private fun showLoadingDialog(@StringRes strId: Int) {
        if (loadingDialog == null) {
            loadingDialog = CommonDialog.getLoadingDialogInstance(
                message = getString(strId)
            )
        }
        loadingDialog?.show(supportFragmentManager)
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
        fun getIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}