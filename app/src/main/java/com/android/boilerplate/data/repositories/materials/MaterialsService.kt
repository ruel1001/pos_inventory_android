package com.android.boilerplate.data.repositories.materials

import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.maintenance.request.Maintenance_Filter_Request
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse
import com.android.boilerplate.data.repositories.materials.request.MaterialCreateRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialFilterRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialSearchAllRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialUpdateRequest
import com.android.boilerplate.data.repositories.materials.response.MaterialEachResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialResponse
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse
import com.android.boilerplate.data.repositories.payment.request.Payment_Show_Request
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MaterialsService {
    @POST("/api/material/create")
    suspend fun doCreateMaterial(@Body request: MaterialCreateRequest): Response<MaterialResponse>

    @POST("/api/material/update")
    suspend fun doUpdateMaterial(@Body request: MaterialUpdateRequest): Response<MaterialResponse>

    @POST("/api/material/get")
    suspend fun doSearchEachMaterial(@Body request: MaterialFilterRequest): Response<MaterialEachResponse>

    @POST("/api/material/getall")
    suspend fun doShowAllMaterial(@Body request: MaterialSearchAllRequest): Response<ShowMaterialListResponse>



    @POST("/api/material/delete")
    suspend fun doDeleteMaterial(@Body request: MaterialFilterRequest): Response<CustomerResponse>
}