package com.android.boilerplate.data.repositories.materials

import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.maintenance.MaintenanceRemoteDataSource
import com.android.boilerplate.data.repositories.maintenance.response.MaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialEachResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialResponse
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class MaterialRepository @Inject constructor(
    private val materialRemoteDataSource: MaterialRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun doCreateMaterial(
        material_name: String,
        quantity: String,
        item: String,
        amount: String,
        sale_amount: String
    ): Flow<MaterialResponse> {
        return flow {
            val response = materialRemoteDataSource.doCreateMaterial(
                material_name, quantity,item,
                amount,sale_amount
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doUpdateMaterial(
        material_id: String,
        material_name: String,
        quantity: String,
        item: String,
        amount: String,
        sale_amount: String

        ): Flow<MaterialResponse> {
        return flow {
            val response = materialRemoteDataSource.doUpdateMaterial(material_id,
                material_name, quantity,item,
                amount,sale_amount
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun getMaterialList(material_name: String): Flow<ShowMaterialListResponse> {
        return flow {
            val response = materialRemoteDataSource.getMaterialAll(material_name)
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doEachMaterial(
        material_id: String,
    ): Flow<MaterialEachResponse> {
        return flow {
            val response = materialRemoteDataSource.doFilterEachMaterial(material_id)
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun doDeleteMaterial(
        material_id: String,

        ): Flow<CustomerResponse> {
        return flow {
            val response = materialRemoteDataSource.doRemoveMaterial(material_id
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }

    suspend fun doLogout() {
        //authLocalDataSource.logout(encryptedDataManager.getAccessToken())
    }
}