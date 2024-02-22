package com.android.boilerplate.data.repositories.customers

import com.android.boilerplate.data.repositories.customers.request.CustomerCreateRequest
import com.android.boilerplate.data.repositories.customers.request.SearchNamesRequest
import com.android.boilerplate.data.repositories.customers.request.SearchRequest
import com.android.boilerplate.data.repositories.customers.request.UpdateCustomerRequest
import com.android.boilerplate.data.repositories.customers.response.CustomerEachResponse
import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.customers.response.SearchListNameResponse
import com.android.boilerplate.data.repositories.customers.response.SearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerService {
    @POST("/api/customers/create")
    suspend fun doCreate(@Body request: CustomerCreateRequest): Response<CustomerEachResponse>

    @POST("/api/customers/get")
    suspend fun doSearchAccount(@Body request: SearchRequest): Response<SearchResponse>

    @POST("/api/customers/update")
    suspend fun doUpdate(@Body request: UpdateCustomerRequest): Response<CustomerEachResponse>

    @POST("/api/customers/delete")
    suspend fun doDelete(@Body request: SearchRequest): Response<CustomerResponse>

    @POST("/api/customers/search_by_name")
    suspend fun doSearchName(@Body request: SearchNamesRequest): Response<SearchListNameResponse>
}