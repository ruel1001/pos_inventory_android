package com.android.boilerplate.data.repositories.expenses

import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.expenses.request.ExpensesAllRequest
import com.android.boilerplate.data.repositories.expenses.request.ExpensesCreateRequest
import com.android.boilerplate.data.repositories.expenses.request.ExpensesFilterEachRequest
import com.android.boilerplate.data.repositories.expenses.request.ExpensesUpdateRequest
import com.android.boilerplate.data.repositories.expenses.response.ExpensesAllResponse
import com.android.boilerplate.data.repositories.expenses.response.ExpensesEachResponse
import com.android.boilerplate.data.repositories.expenses.response.ExpensesResponse
import com.android.boilerplate.data.repositories.maintenance.request.Maintenance_Filter_Request
import com.android.boilerplate.data.repositories.maintenance.request.SalesSearchAllRequest
import com.android.boilerplate.data.repositories.maintenance.response.SalesAllResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.materials.request.MaterialCreateRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialFilterRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialUpdateRequest
import com.android.boilerplate.data.repositories.materials.response.MaterialEachResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ExpensesService {
    @POST("/api/expenses/create")
    suspend fun doCreateExpenses(@Body request: ExpensesCreateRequest): Response<MaterialResponse>

    @POST("/api/expenses/update")
    suspend fun doUpdateExpenses(@Body request: ExpensesUpdateRequest): Response<ExpensesResponse>

    @POST("/api/expenses/get")
    suspend fun doSearchEachExpenses(@Body request: ExpensesFilterEachRequest): Response<ExpensesEachResponse>


    @POST("/api/expenses/delete")
    suspend fun doDeleteExpenses(@Body request: ExpensesFilterEachRequest): Response<CustomerResponse>


    @POST("/api/expenses/getall")
    suspend fun doShowAllExpenses(@Body request: ExpensesAllRequest): Response<ExpensesAllResponse>
}