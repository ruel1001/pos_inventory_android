package com.android.boilerplate.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.boilerplate.data.model.ErrorModel
import com.android.boilerplate.data.repositories.article.ArticleRepository
import com.android.boilerplate.data.repositories.auth.AuthRepository
import com.android.boilerplate.data.repositories.customers.CustomerRepository
import com.android.boilerplate.utils.PopupErrorState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

@HiltViewModel
class CreateCustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _customerSharedFlow = MutableSharedFlow<CreateCustomerViewState>()

    val customerSharedFlow: SharedFlow<CreateCustomerViewState> =
        _customerSharedFlow.asSharedFlow()

    lateinit var imageFile: File


    fun doCreateCustomer(account_name: String, address: String, date_plan: String,
                         amount_of_installation: String
                         , due_date_month: String, foc: String, modem: String, connector: String
                         , ficamp: String, others: String, messenger: String, contact_number: String
                         , account_balance: String, arrears: String,area: String,plan_subscribed: String) {
        viewModelScope.launch {
            customerRepository.doCreateCustomer(account_name, address,date_plan,
                amount_of_installation,due_date_month,foc,modem,connector,ficamp,others,
                messenger,contact_number,account_balance,arrears,area,plan_subscribed)
                .onStart {
                    _customerSharedFlow.emit(CreateCustomerViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _customerSharedFlow.emit(
                        CreateCustomerViewState.SuccessEach(it)
                    )
                }
        }
    }

    fun doUpdateCustomer(account_number: String,
                         account_name: String,
                         address: String,
                         date_plan: String,
                         amount_of_installation: String,
                         due_date_month: String, foc: String, modem: String, connector: String
                         , ficamp: String, others: String, messenger: String, contact_number: String
                         , account_balance: String, arrears: String,area:String,plan_subscribed: String) {
        viewModelScope.launch {
            customerRepository.doUpdateCustomer(account_number,account_name, address,date_plan,
                amount_of_installation,due_date_month,foc,modem,connector,ficamp,others,
                messenger,contact_number,account_balance,arrears,area,plan_subscribed)
                .onStart {
                    _customerSharedFlow.emit(CreateCustomerViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _customerSharedFlow.emit(
                        CreateCustomerViewState.SuccessEach(it)
                    )
                }
        }
    }

    fun doDeleteCustomer(account_number: String
                  ) {
        viewModelScope.launch {
            customerRepository.doRemoveCustomer(account_number)
                .onStart {
                    _customerSharedFlow.emit(CreateCustomerViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _customerSharedFlow.emit(
                        CreateCustomerViewState.SuccessDelete(it.message.orEmpty())
                    )
                }
        }
    }

    fun doSearchCustomer(account_number: String) {
        viewModelScope.launch {
            customerRepository.doSearchCustomer(account_number)
                .onStart {
                    _customerSharedFlow.emit(CreateCustomerViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _customerSharedFlow.emit(
                        CreateCustomerViewState.SuccessSearch(it.data)
                    )
                }
        }
    }

    fun getCustomerNameLIst(account_name: String) {

        viewModelScope.launch {
            customerRepository.getSearchNameList(account_name)
                .onStart {
                    _customerSharedFlow.emit(CreateCustomerViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _customerSharedFlow.emit(
                        CreateCustomerViewState.SuccessListNames(it)
                    )
                }
        }
    }



    private suspend fun onError(exception: Throwable) {
        when (exception) {
            is IOException,
            is TimeoutException,
            -> {
                _customerSharedFlow.emit(
                    CreateCustomerViewState.PopupError(
                        PopupErrorState.NetworkError
                    )
                )
            }
            is HttpException -> {
                val errorBody = exception.response()?.errorBody()
                val gson = Gson()
                val type = object : TypeToken<ErrorModel>() {}.type
                var errorResponse: ErrorModel? = gson.fromJson(errorBody?.charStream(), type)
                if (errorResponse?.has_requirements == true) {
                    _customerSharedFlow.emit(CreateCustomerViewState.InputError(errorResponse.errors))
                } else {
                    _customerSharedFlow.emit(
                        CreateCustomerViewState.PopupError(
                            PopupErrorState.HttpError, errorResponse?.message.orEmpty()
                        )
                    )
                }
            }
            else -> _customerSharedFlow.emit(
                CreateCustomerViewState.PopupError(
                    PopupErrorState.UnknownError
                )
            )
        }
    }

}