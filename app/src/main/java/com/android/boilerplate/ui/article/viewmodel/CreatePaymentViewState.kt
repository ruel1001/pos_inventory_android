package com.android.boilerplate.ui.article.viewmodel

import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.data.repositories.article.response.ArticleListResponse
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentFilterResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentFilterResponse.FilterPaymentData
import com.android.boilerplate.data.repositories.payment.response.PaymentListResponse
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import com.android.boilerplate.utils.PopupErrorState

sealed class CreatePaymentViewState{
    object Loading : CreatePaymentViewState()
    data class Success(val message: String = "") : CreatePaymentViewState()
    data class Successcrud(val response: PaymentFilterResponse?=PaymentFilterResponse()) : CreatePaymentViewState()
    data class SuccessAllPayment(val response: PaymentListResponse?=PaymentListResponse()) : CreatePaymentViewState()
    data class SuccessFilter(val response: FilterPaymentData?=FilterPaymentData()) : CreatePaymentViewState()
    data class SuccessList(val response: ShowPaymentResponse?=ShowPaymentResponse()) : CreatePaymentViewState()
    data class PopupError(val errorCode: PopupErrorState, val message: String = "") : CreatePaymentViewState()
    data class InputError(val errorData: ErrorsData? = null) : CreatePaymentViewState()
}
