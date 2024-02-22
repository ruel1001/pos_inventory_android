package com.android.boilerplate.data.repositories.maintenance

import com.android.boilerplate.data.repositories.article.request.ArticleListRequest
import com.android.boilerplate.data.repositories.article.response.ArticleListResponse
import com.android.boilerplate.data.repositories.auth.request.LoginRequest
import com.android.boilerplate.data.repositories.auth.request.RegisterRequest
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse
import com.android.boilerplate.data.repositories.customers.request.CustomerCreateRequest
import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.maintenance.request.CreateMaintenanceRequest
import com.android.boilerplate.data.repositories.maintenance.request.Maintenance_Filter_Request
import com.android.boilerplate.data.repositories.maintenance.request.SalesSearchAllRequest
import com.android.boilerplate.data.repositories.maintenance.request.UpdateMaintenanceRequest
import com.android.boilerplate.data.repositories.maintenance.response.MaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.SalesAllResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse
import com.android.boilerplate.data.repositories.payment.request.PaymentCreateRequest
import com.android.boilerplate.data.repositories.payment.request.PaymentUpdateRequest
import com.android.boilerplate.data.repositories.payment.request.Payment_Show_Request
import com.android.boilerplate.data.repositories.payment.request.Payment_filter_Request
import com.android.boilerplate.data.repositories.payment.response.PaymentFilterResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentResponse
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse.ShowListMaintenanceData
import com.android.boilerplate.data.repositories.materials.request.MaterialSearchAllRequest
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse

class MaintenanceRemoteDataSource @Inject constructor(private val maintenanceService: MaintenanceService)  {

    suspend fun doCreateMaintenance(account_number: String,
                                account_name: String,
                                address: String,
                                area: String,
                                material_used: String,
                                nature_of_repair: String,
                                    material_id: String,
                                    material_quantity_used: String,
                        ): SearchMaintenanceResponse {
        val request = CreateMaintenanceRequest(account_number, account_name,address,
            area,material_used,nature_of_repair,material_id,material_quantity_used)
        val response = maintenanceService.doCreateMaintenance(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun getMaintenance(account_number: String): ShowMaintenanceResponse {
        val request = Payment_Show_Request(account_number)
        val response = maintenanceService.doShowMaintenance(request)

        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }
        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doFilterEachMaintenance(maintenance_id: String,

    ): SearchMaintenanceResponse {
        val request = Maintenance_Filter_Request(maintenance_id )
        val response = maintenanceService.doSearchEachMaintenance(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doUpdateMaintenance(maintenance_id:String,
        account_number: String,
                                    account_name: String,
                                    address: String,
                                    area: String,
                                    material_used: String,
                                    nature_of_repair: String,
                                    material_id: String,
                                    material_quantity_used: String,
    ): SearchMaintenanceResponse {
        val request = UpdateMaintenanceRequest(maintenance_id,account_number, account_name,address,
            area,material_used,nature_of_repair,material_id,material_quantity_used)
        val response = maintenanceService.doUpdateMaintenance(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }

    suspend fun getSalesAll(material_name: String): SalesAllResponse {
        val request = SalesSearchAllRequest(material_name)
        val response = maintenanceService.doShowAllSales(request)

        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }
        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doRemoveMaintenance(payment_id: String
    ): CustomerResponse {
        val request = Maintenance_Filter_Request(payment_id)
        val response = maintenanceService.doDeleteMaintenance(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }
}