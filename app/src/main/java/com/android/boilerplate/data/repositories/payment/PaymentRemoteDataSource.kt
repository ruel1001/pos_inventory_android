package com.android.boilerplate.data.repositories.payment

import com.android.boilerplate.data.repositories.article.request.ArticleListRequest
import com.android.boilerplate.data.repositories.article.response.ArticleListResponse
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
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject

class PaymentRemoteDataSource @Inject constructor(private val paymentService: PaymentService)  {

    suspend fun doCreatePayment(account_number: String,
                                account_name: String,
                                account_balance: String,
                                arrears_month: String,
                                amount_paid: String,
                                collectors_name: String,
                                billing_month: String,
                        ): PaymentFilterResponse {
        val request = PaymentCreateRequest(account_number, account_name,account_balance,
            arrears_month,amount_paid,collectors_name,billing_month)
        val response = paymentService.doCreatePayment(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun getPaymentList(account_number: String): ShowPaymentResponse {
        val request = Payment_Show_Request(account_number)
        val response = paymentService.doShow(request)

        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }
        return response.body() ?: throw NullPointerException("Response data is empty")
    }

    suspend fun getAllPaymentList(account_name: String): PaymentListResponse {
        val request = PaymentAllRequest(account_name)
        val response = paymentService.doShowAllPayment(request)

        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }
        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doFilterPayment(payment_id: String,

    ): PaymentFilterResponse {
        val request = Payment_filter_Request(payment_id )
        val response = paymentService.doFilter(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doUpdatePayment(payment_id: String,account_number: String,
                                account_name: String,
                                account_balance: String,
                                arrears_month: String,
                                amount_paid: String,
                                collectors_name: String,
                                billing_month: String,
    ): PaymentFilterResponse {
        val request = PaymentUpdateRequest(payment_id,account_number, account_name,account_balance,
            arrears_month,amount_paid,collectors_name,billing_month)
        val response = paymentService.doUpdatePayment(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doRemovePayment(payment_id: String
    ): PaymentResponse {
        val request = Payment_filter_Request(payment_id)
        val response = paymentService.doRemove(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }
}