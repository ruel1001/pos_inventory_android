package com.android.boilerplate.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.boilerplate.data.model.ErrorModel
import com.android.boilerplate.data.repositories.article.ArticleRepository
import com.android.boilerplate.data.repositories.auth.AuthRepository
import com.android.boilerplate.data.repositories.customers.CustomerRepository
import com.android.boilerplate.data.repositories.payment.PaymentRepository
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
class CreatePaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _paymentSharedFlow = MutableSharedFlow<CreatePaymentViewState>()

    val paymentSharedFlow: SharedFlow<CreatePaymentViewState> =
        _paymentSharedFlow.asSharedFlow()

    lateinit var imageFile: File


    fun doCreatePayment(account_number: String, account_name: String, account_balance: String,
                        arrears_month: String
                         , amount_paid: String, collectors_name: String, billing_month: String) {
        viewModelScope.launch {
            paymentRepository.doCreatepayment(account_number, account_name,account_balance,
                arrears_month,amount_paid,collectors_name,billing_month)
                .onStart {
                    _paymentSharedFlow.emit(CreatePaymentViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _paymentSharedFlow.emit(
                        CreatePaymentViewState.Successcrud(it)
                    )
                }
        }
    }

    fun doUpdatePayment(payment_id: String,account_number: String, account_name: String, account_balance: String,
                        arrears_month: String
                        , amount_paid: String, collectors_name: String, billing_month: String) {
        viewModelScope.launch {
            paymentRepository.doUpdatepayment(payment_id,account_number, account_name,account_balance,
                arrears_month,amount_paid,collectors_name,billing_month)
                .onStart {
                    _paymentSharedFlow.emit(CreatePaymentViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _paymentSharedFlow.emit(
                        CreatePaymentViewState.Successcrud(it)
                    )
                }
        }
    }

    fun doRemovePayment(payment_id: String) {
        viewModelScope.launch {
            paymentRepository.doDeletepayment(payment_id)
                .onStart {
                    _paymentSharedFlow.emit(CreatePaymentViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _paymentSharedFlow.emit(
                        CreatePaymentViewState.Success(it.message.orEmpty())
                    )
                }
        }
    }


    fun doFilterPayment(payment_id: String) {
        viewModelScope.launch {
            paymentRepository.doFilterpayment(payment_id)
                .onStart {
                    _paymentSharedFlow.emit(CreatePaymentViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _paymentSharedFlow.emit(
                        CreatePaymentViewState.SuccessFilter(it.data)
                    )
                }
        }
    }

    fun doAllPaymentlist(account_name: String) {
        viewModelScope.launch {
            paymentRepository.getAllPaymentList(account_name)
                .onStart {
                    _paymentSharedFlow.emit(CreatePaymentViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _paymentSharedFlow.emit(
                        CreatePaymentViewState.SuccessAllPayment(it)
                    )
                }
        }
    }


    fun getPaymentList(account_name: String) {

        viewModelScope.launch {
            paymentRepository.getPaymentList(account_name)
                .onStart {
                    _paymentSharedFlow.emit(CreatePaymentViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _paymentSharedFlow.emit(
                        CreatePaymentViewState.SuccessList(it)
                    )
                }
        }
    }



    private suspend fun onError(exception: Throwable) {
        when (exception) {
            is IOException,
            is TimeoutException,
            -> {
                _paymentSharedFlow.emit(
                    CreatePaymentViewState.PopupError(
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
                    _paymentSharedFlow.emit(CreatePaymentViewState.InputError(errorResponse.errors))
                } else {
                    _paymentSharedFlow.emit(
                        CreatePaymentViewState.PopupError(
                            PopupErrorState.HttpError, errorResponse?.message.orEmpty()
                        )
                    )
                }
            }
            else -> _paymentSharedFlow.emit(
                CreatePaymentViewState.PopupError(
                    PopupErrorState.UnknownError
                )
            )
        }
    }

}