package com.android.boilerplate.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.boilerplate.data.model.ErrorModel
import com.android.boilerplate.data.repositories.expenses.ExpensesRepository

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
class CreateExpensesViewModel @Inject constructor(
    private val expensesRepository: ExpensesRepository
) : ViewModel() {

    private val _expensesSharedFlow = MutableSharedFlow<CreateExpensesViewState>()

    val expensesSharedFlow: SharedFlow<CreateExpensesViewState> =
        _expensesSharedFlow.asSharedFlow()

    lateinit var imageFile: File


    fun doCreateExpenses(
        nature_of_expenses: String,
        amount: String,
    ) {
        viewModelScope.launch {
            expensesRepository.doCreateExpenses(
                nature_of_expenses, amount
            )
                .onStart {
                    _expensesSharedFlow.emit(CreateExpensesViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _expensesSharedFlow.emit(
                        CreateExpensesViewState.Success(it.message.orEmpty())
                    )
                }
        }
    }

    fun doUpdateExpenses(
        expenses_id: String,
        nature_of_expenses: String,
        amount: String,
    ) {
        viewModelScope.launch {
            expensesRepository.doUpdateExpenses(expenses_id,
                nature_of_expenses,
                amount
            )
                .onStart {
                    _expensesSharedFlow.emit(CreateExpensesViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _expensesSharedFlow.emit(
                        CreateExpensesViewState.Success(it.message.orEmpty())
                    )
                }
        }
    }

    fun doFilterExpenses(expenses_id: String) {
        viewModelScope.launch {
            expensesRepository.doEachExpenses(expenses_id)
                .onStart {
                    _expensesSharedFlow.emit(CreateExpensesViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _expensesSharedFlow.emit(
                        CreateExpensesViewState.SuccessFilter(it.data)
                    )
                }
        }
    }

    fun doRemoveExpenses(expenses_id: String) {
        viewModelScope.launch {
            expensesRepository.doDeleteExpenses(expenses_id)
                .onStart {
                    _expensesSharedFlow.emit(CreateExpensesViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _expensesSharedFlow.emit(
                        CreateExpensesViewState.Success(it.message.orEmpty())
                    )
                }
        }
    }

    fun getExpensesAllSales(material_name: String) {

        viewModelScope.launch {
            expensesRepository.getExpensesList(material_name)
                .onStart {
                    _expensesSharedFlow.emit(CreateExpensesViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _expensesSharedFlow.emit(
                        CreateExpensesViewState.SuccessExpenseALl(it)
                    )
                }
        }
    }




    private suspend fun onError(exception: Throwable) {
        when (exception) {
            is IOException,
            is TimeoutException,
            -> {
                _expensesSharedFlow.emit(
                    CreateExpensesViewState.PopupError(
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
                    _expensesSharedFlow.emit(CreateExpensesViewState.InputError(errorResponse.errors))
                } else {
                    _expensesSharedFlow.emit(
                        CreateExpensesViewState.PopupError(
                            PopupErrorState.HttpError, errorResponse?.message.orEmpty()
                        )
                    )
                }
            }

            else -> _expensesSharedFlow.emit(
                CreateExpensesViewState.PopupError(
                    PopupErrorState.UnknownError
                )
            )
        }
    }

}