package com.android.boilerplate.ui.article.viewmodel

import com.android.boilerplate.data.model.ErrorsData
import com.android.boilerplate.data.repositories.expenses.response.ExpensesAllResponse
import com.android.boilerplate.data.repositories.expenses.response.ExpensesEachResponse.ExpensesEachData
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialEachResponse.MaterialEachData
import com.android.boilerplate.utils.PopupErrorState

sealed class CreateExpensesViewState{
    object Loading : CreateExpensesViewState()
    data class Success(val message: String = "") : CreateExpensesViewState()
    data class SuccessExpenseALl(val response: ExpensesAllResponse?=ExpensesAllResponse()) : CreateExpensesViewState()
    data class SuccessFilter(val response: ExpensesEachData?=ExpensesEachData()) : CreateExpensesViewState()
    data class SuccessList(val response: ShowMaintenanceResponse= ShowMaintenanceResponse()) : CreateExpensesViewState()
    data class PopupError(val errorCode: PopupErrorState, val message: String = "") : CreateExpensesViewState()
    data class InputError(val errorData: ErrorsData? = null) : CreateExpensesViewState()
}
