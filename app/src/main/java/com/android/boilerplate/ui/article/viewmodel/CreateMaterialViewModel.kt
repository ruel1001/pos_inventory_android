package com.android.boilerplate.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.boilerplate.data.model.ErrorModel
import com.android.boilerplate.data.repositories.article.ArticleRepository
import com.android.boilerplate.data.repositories.auth.AuthRepository
import com.android.boilerplate.data.repositories.customers.CustomerRepository
import com.android.boilerplate.data.repositories.maintenance.MaintenanceRepository
import com.android.boilerplate.data.repositories.materials.MaterialRepository
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
class CreateMaterialViewModel @Inject constructor(
    private val materialRepository: MaterialRepository
) : ViewModel() {

    private val _materialSharedFlow = MutableSharedFlow<CreateMaterialViewState>()

    val materialSharedFlow: SharedFlow<CreateMaterialViewState> =
        _materialSharedFlow.asSharedFlow()

    lateinit var imageFile: File


    fun doCreateMaterial(
        material_name: String,
        quantity: String,
        item: String,
        amount: String,
        sale_amount: String
    ) {
        viewModelScope.launch {
            materialRepository.doCreateMaterial(
                material_name, quantity, item,
                amount,sale_amount
            )
                .onStart {
                    _materialSharedFlow.emit(CreateMaterialViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _materialSharedFlow.emit(
                        CreateMaterialViewState.Success(it.message.orEmpty())
                    )
                }
        }
    }

    fun getShowAllMaterial(material_name: String) {

        viewModelScope.launch {
            materialRepository.getMaterialList(material_name)
                .onStart {
                    _materialSharedFlow.emit(CreateMaterialViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _materialSharedFlow.emit(
                        CreateMaterialViewState.SuccessMaterialList(it)
                    )
                }
        }
    }

    fun doUpdateMaterial(
        material_id: String,
        material_name: String,
        quantity: String,
        item: String,
        amount: String,
        sale_amount: String

    ) {
        viewModelScope.launch {
            materialRepository.doUpdateMaterial(material_id,
                material_name, quantity, item,
                amount,sale_amount
            )
                .onStart {
                    _materialSharedFlow.emit(CreateMaterialViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _materialSharedFlow.emit(
                        CreateMaterialViewState.Success(it.message.orEmpty())
                    )
                }
        }
    }

    fun doFilterMaterial(material_id: String) {
        viewModelScope.launch {
            materialRepository.doEachMaterial(material_id)
                .onStart {
                    _materialSharedFlow.emit(CreateMaterialViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _materialSharedFlow.emit(
                        CreateMaterialViewState.SuccessFilter(it.data)
                    )
                }
        }
    }

    fun doRemoveMaterial(material_id: String) {
        viewModelScope.launch {
            materialRepository.doDeleteMaterial(material_id)
                .onStart {
                    _materialSharedFlow.emit(CreateMaterialViewState.Loading)
                }
                .catch { exception ->
                    onError(exception)

                }
                .collect {
                    _materialSharedFlow.emit(
                        CreateMaterialViewState.Success(it.message.orEmpty())
                    )
                }
        }
    }




    private suspend fun onError(exception: Throwable) {
        when (exception) {
            is IOException,
            is TimeoutException,
            -> {
                _materialSharedFlow.emit(
                    CreateMaterialViewState.PopupError(
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
                    _materialSharedFlow.emit(CreateMaterialViewState.InputError(errorResponse.errors))
                } else {
                    _materialSharedFlow.emit(
                        CreateMaterialViewState.PopupError(
                            PopupErrorState.HttpError, errorResponse?.message.orEmpty()
                        )
                    )
                }
            }

            else -> _materialSharedFlow.emit(
                CreateMaterialViewState.PopupError(
                    PopupErrorState.UnknownError
                )
            )
        }
    }

}