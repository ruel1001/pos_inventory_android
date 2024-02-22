package com.android.boilerplate.ui.article.viewmodel

import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.data.model.NewErrorsData
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.customers.response.CustomerEachResponse
import com.android.boilerplate.data.repositories.customers.response.SearchListNameResponse
import com.android.boilerplate.data.repositories.customers.response.SearchResponse.CustomerSingleData
import com.android.boilerplate.utils.PopupErrorState

sealed class CreateCustomerViewState{
    object Loading : CreateCustomerViewState()
    data class Success(val message: String = "") : CreateCustomerViewState()


    data class SuccessDelete(val message: String = "") : CreateCustomerViewState()
    data class SuccessEach(val response: CustomerEachResponse?= CustomerEachResponse()) : CreateCustomerViewState()
    data class SuccessSearch(val response: CustomerSingleData?= CustomerSingleData()) : CreateCustomerViewState()
    data class PopupError(val errorCode: PopupErrorState, val message: String = "") : CreateCustomerViewState()
    data class InputError(val errorData: ErrorsData? = null) : CreateCustomerViewState()
    data class SuccessListNames(val response: SearchListNameResponse?= SearchListNameResponse()) : CreateCustomerViewState()
    data class NewInputError(val newErrorsData: NewErrorsData?= null) : CreateCustomerViewState()
}
