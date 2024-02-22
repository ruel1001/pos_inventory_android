package com.android.boilerplate.data.repositories.maintenance

import com.android.boilerplate.data.local.UserLocalData
import com.android.boilerplate.data.repositories.article.response.ArticleListResponse
import com.android.boilerplate.data.repositories.auth.AuthLocalDataSource
import com.android.boilerplate.data.repositories.auth.request.LoginRequest
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse
import com.android.boilerplate.data.repositories.auth.response.UserData
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse.LoginxData.UserxData
import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.maintenance.response.MaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.SalesAllResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentFilterResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentResponse
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import com.android.boilerplate.security.AuthEncryptedDataManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse.ShowListMaintenanceData
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse

class MaintenanceRepository @Inject constructor(
    private val maintenanceRemoteDataSource: MaintenanceRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun doCreateMaintenance(
        account_number: String,
        account_name: String,
        address: String,
        area: String,
        material_used: String,
        nature_of_repair: String,
        material_id: String,
        material_quantity_used: String,
    ): Flow<SearchMaintenanceResponse> {
        return flow {
            val response = maintenanceRemoteDataSource.doCreateMaintenance(
                account_number, account_name,address,
                area,material_used,nature_of_repair,material_id,material_quantity_used
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun getMaintenanceList(account_number: String): Flow<ShowMaintenanceResponse> {
        return flow {
            val response = maintenanceRemoteDataSource.getMaintenance(account_number)
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun doEachMaintenance(
        maintenance_id: String,
    ): Flow<SearchMaintenanceResponse> {
        return flow {
            val response = maintenanceRemoteDataSource.doFilterEachMaintenance(maintenance_id)
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doUpdateMaintenance(
        maintenance_id: String,
        account_number: String,
        account_name: String,
        address: String,
        area: String,
        material_used: String,
        nature_of_repair: String,
        material_id: String,
        material_quantity_used: String,
    ): Flow<SearchMaintenanceResponse> {
        return flow {
            val response = maintenanceRemoteDataSource.doUpdateMaintenance(maintenance_id,
                account_number, account_name,address,
                area,material_used,nature_of_repair,material_id,material_quantity_used
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun getSalesList(material_name: String): Flow<SalesAllResponse> {
        return flow {
            val response = maintenanceRemoteDataSource.getSalesAll(material_name)
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doDeleteMaintenance(
        payment_id: String,

    ): Flow<CustomerResponse> {
        return flow {
            val response = maintenanceRemoteDataSource.doRemoveMaintenance(payment_id
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }



    suspend fun doLogout() {
        //authLocalDataSource.logout(encryptedDataManager.getAccessToken())
    }
}