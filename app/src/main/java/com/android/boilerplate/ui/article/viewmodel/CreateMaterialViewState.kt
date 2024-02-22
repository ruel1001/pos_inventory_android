package com.android.boilerplate.ui.article.viewmodel

import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.data.repositories.article.response.ArticleListResponse
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse.MaintenanceSearchData
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialEachResponse.MaterialEachData
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentFilterResponse.FilterPaymentData
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse.ShowPaymentData
import com.android.boilerplate.utils.PopupErrorState

sealed class CreateMaterialViewState{
    object Loading : CreateMaterialViewState()
    data class Success(val message: String = "") : CreateMaterialViewState()
    data class SuccessMaterialList(val response: ShowMaterialListResponse = ShowMaterialListResponse()) : CreateMaterialViewState()
    data class SuccessFilter(val response: MaterialEachData?=MaterialEachData()) : CreateMaterialViewState()
    data class SuccessList(val response: ShowMaintenanceResponse= ShowMaintenanceResponse()) : CreateMaterialViewState()
    data class PopupError(val errorCode: PopupErrorState, val message: String = "") : CreateMaterialViewState()
    data class InputError(val errorData: ErrorsData? = null) : CreateMaterialViewState()
}
