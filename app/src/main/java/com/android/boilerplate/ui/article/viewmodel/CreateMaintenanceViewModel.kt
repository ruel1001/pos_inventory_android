package com.android.boilerplate.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.boilerplate.data.model.ErrorModel
import com.android.boilerplate.data.repositories.article.ArticleRepository
import com.android.boilerplate.data.repositories.auth.AuthRepository
import com.android.boilerplate.data.repositories.customers.CustomerRepository
import com.android.boilerplate.data.repositories.maintenance.MaintenanceRepository
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
class CreateMaintenanceViewModel @Inject constructor(
    private val maintenanceRepository: MaintenanceRepository
) : ViewModel() {

    private val _maintenanceSharedFlow = MutableSharedFlow<CreateMaintenanceViewState>()

    val maintenanceSharedFlow: SharedFlow<CreateMaintenanceViewState> =
        _maintenanceSharedFlow.asSharedFlow()

    lateinit var imageFile: File


    fun doCreateMaintenance(
        account_number: String,
        account_name: String,
        address: String,
        area: String,
        material_used: String,
        nature_of_repair: String,
        material_id: String,
        material_quantity_used: String,
    ) {
        viewModelScope.launch {
            maintenanceRepository.doCreateMaintenance(
                account_number, account_name, address,
                area, material_used, nature_of_repair,
                material_id,material_quantity_used
            )
                .onStart {
                    _maintenanceSharedFlow.emit(CreateMaintenanceViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _maintenanceSharedFlow.emit(
                        CreateMaintenanceViewState.SuccessCrud(it)
                    )
                }
        }
    }

    fun doUpdateMaintenance(maintenance_id:String,
        account_number: String,
        account_name: String,
        address: String,
        area: String,
        material_used: String,
        nature_of_repair: String,
                            material_id: String,
                            material_quantity_used: String,
    ) {
        viewModelScope.launch {
            maintenanceRepository.doUpdateMaintenance(maintenance_id,
                account_number, account_name, address,
                area, material_used, nature_of_repair,
                        material_id,material_quantity_used
            )
                .onStart {
                    _maintenanceSharedFlow.emit(CreateMaintenanceViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _maintenanceSharedFlow.emit(
                        CreateMaintenanceViewState.SuccessCrud(it)
                    )
                }
        }
    }

    fun getShowAllSales(material_name: String) {

        viewModelScope.launch {
            maintenanceRepository.getSalesList(material_name)
                .onStart {
                    _maintenanceSharedFlow.emit(CreateMaintenanceViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _maintenanceSharedFlow.emit(
                        CreateMaintenanceViewState.SuccessSaleslList(it)
                    )
                }
        }
    }

    fun doRemoveMainteanance(maintenance_id: String) {
        viewModelScope.launch {
            maintenanceRepository.doDeleteMaintenance(maintenance_id)
                .onStart {
                    _maintenanceSharedFlow.emit(CreateMaintenanceViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _maintenanceSharedFlow.emit(
                        CreateMaintenanceViewState.Success(it.message.orEmpty())
                    )
                }
        }
    }


    fun doFilterMaintenance(maintenance_id: String) {
        viewModelScope.launch {
            maintenanceRepository.doEachMaintenance(maintenance_id)
                .onStart {
                    _maintenanceSharedFlow.emit(CreateMaintenanceViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _maintenanceSharedFlow.emit(
                        CreateMaintenanceViewState.SuccessFilter(it.data)
                    )
                }
        }
    }


    fun getMaintenance(account_name: String) {

        viewModelScope.launch {
            maintenanceRepository.getMaintenanceList(account_name)
                .onStart {
                    _maintenanceSharedFlow.emit(CreateMaintenanceViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _maintenanceSharedFlow.emit(
                        CreateMaintenanceViewState.SuccessList(it)
                    )
                }
        }
    }


    private suspend fun onError(exception: Throwable) {
        when (exception) {
            is IOException,
            is TimeoutException,
            -> {
                _maintenanceSharedFlow.emit(
                    CreateMaintenanceViewState.PopupError(
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
                    _maintenanceSharedFlow.emit(CreateMaintenanceViewState.InputError(errorResponse.errors))
                } else {
                    _maintenanceSharedFlow.emit(
                        CreateMaintenanceViewState.PopupError(
                            PopupErrorState.HttpError, errorResponse?.message.orEmpty()
                        )
                    )
                }
            }

            else -> _maintenanceSharedFlow.emit(
                CreateMaintenanceViewState.PopupError(
                    PopupErrorState.UnknownError
                )
            )
        }
    }

}