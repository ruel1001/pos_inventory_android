package com.android.boilerplate.data.repositories.payment

import com.android.boilerplate.data.repositories.auth.request.LoginRequest
import com.android.boilerplate.data.repositories.auth.request.RegisterRequest
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse
import com.android.boilerplate.data.repositories.customers.request.CustomerCreateRequest
import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.materials.request.MaterialSearchAllRequest
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse
import com.android.boilerplate.data.repositories.payment.request.PaymentAllRequest
import com.android.boilerplate.data.repositories.payment.request.PaymentCreateRequest
import com.android.boilerplate.data.repositories.payment.request.PaymentUpdateRequest
import com.android.boilerplate.data.repositories.payment.request.Payment_Show_Request
import com.android.boilerplate.data.repositories.payment.request.Payment_filter_Request
import com.android.boilerplate.data.repositories.payment.response.PaymentFilterResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentListResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentResponse
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentService {
    @POST("/api/payment/create")
    suspend fun doCreatePayment(@Body request: PaymentCreateRequest): Response<PaymentFilterResponse>

    @POST("/api/payment/transaction")
    suspend fun doShow(@Body request: Payment_Show_Request): Response<ShowPaymentResponse>


    @POST("/api/payment/get")
    suspend fun doFilter(@Body request: Payment_filter_Request): Response<PaymentFilterResponse>

    @POST("/api/payment/update")
    suspend fun doUpdatePayment(@Body request: PaymentUpdateRequest): Response<PaymentFilterResponse>

    @POST("/api/payment/delete")
    suspend fun doRemove(@Body request: Payment_filter_Request): Response<PaymentResponse>

    @POST("/api/payment/getall")
    suspend fun doShowAllPayment(@Body request: PaymentAllRequest): Response<PaymentListResponse>


}