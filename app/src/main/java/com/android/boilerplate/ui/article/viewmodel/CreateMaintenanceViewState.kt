package com.android.boilerplate.ui.article.viewmodel

import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.data.repositories.article.response.ArticleListResponse
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.maintenance.response.SalesAllResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse.MaintenanceSearchData
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentFilterResponse.FilterPaymentData
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse.ShowPaymentData
import com.android.boilerplate.utils.PopupErrorState

sealed class CreateMaintenanceViewState{
    object Loading : CreateMaintenanceViewState()
    data class Success(val message: String = "") : CreateMaintenanceViewState()
    data class SuccessCrud(val response: SearchMaintenanceResponse?=SearchMaintenanceResponse()) : CreateMaintenanceViewState()
    data class SuccessSaleslList(val response: SalesAllResponse = SalesAllResponse()) : CreateMaintenanceViewState()
    data class SuccessFilter(val response: MaintenanceSearchData?=MaintenanceSearchData()) : CreateMaintenanceViewState()
    data class SuccessList(val response: ShowMaintenanceResponse= ShowMaintenanceResponse()) : CreateMaintenanceViewState()
    data class PopupError(val errorCode: PopupErrorState, val message: String = "") : CreateMaintenanceViewState()
    data class InputError(val errorData: ErrorsData? = null) : CreateMaintenanceViewState()
}
