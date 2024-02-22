package com.android.boilerplate.data.repositories.maintenance

import com.android.boilerplate.data.repositories.customers.request.CustomerCreateRequest
import com.android.boilerplate.data.repositories.customers.request.SearchRequest
import com.android.boilerplate.data.repositories.customers.request.UpdateCustomerRequest
import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.customers.response.SearchResponse
import com.android.boilerplate.data.repositories.maintenance.request.CreateMaintenanceRequest
import com.android.boilerplate.data.repositories.maintenance.request.Maintenance_Filter_Request
import com.android.boilerplate.data.repositories.maintenance.request.SalesSearchAllRequest
import com.android.boilerplate.data.repositories.maintenance.request.UpdateMaintenanceRequest
import com.android.boilerplate.data.repositories.maintenance.response.MaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.SalesAllResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse.ShowListMaintenanceData
import com.android.boilerplate.data.repositories.materials.request.MaterialSearchAllRequest
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse
import com.android.boilerplate.data.repositories.payment.request.Payment_Show_Request
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MaintenanceService {
    @POST("/api/maintenance/create")
    suspend fun doCreateMaintenance(@Body request: CreateMaintenanceRequest): Response<SearchMaintenanceResponse>

    @POST("/api/maintenance/transaction")
    suspend fun doShowMaintenance(@Body request: Payment_Show_Request): Response<ShowMaintenanceResponse>




    @POST("/api/maintenance/get")
    suspend fun doSearchEachMaintenance(@Body request: Maintenance_Filter_Request): Response<SearchMaintenanceResponse>

    @POST("/api/maintenance/update")
    suspend fun doUpdateMaintenance(@Body request: UpdateMaintenanceRequest): Response<SearchMaintenanceResponse>

    @POST("/api/maintenance/delete")
    suspend fun doDeleteMaintenance(@Body request: Maintenance_Filter_Request): Response<CustomerResponse>


    @POST("/api/sales/getall")
    suspend fun doShowAllSales(@Body request: SalesSearchAllRequest): Response<SalesAllResponse>


}