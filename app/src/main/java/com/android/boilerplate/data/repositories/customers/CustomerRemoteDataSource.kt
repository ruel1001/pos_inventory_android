package com.android.boilerplate.data.repositories.customers

import com.android.boilerplate.data.repositories.auth.request.LoginRequest
import com.android.boilerplate.data.repositories.auth.request.RegisterRequest
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse
import com.android.boilerplate.data.repositories.customers.request.CustomerCreateRequest
import com.android.boilerplate.data.repositories.customers.request.SearchNamesRequest
import com.android.boilerplate.data.repositories.customers.request.SearchRequest
import com.android.boilerplate.data.repositories.customers.request.UpdateCustomerRequest
import com.android.boilerplate.data.repositories.customers.response.CustomerEachResponse
import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.customers.response.SearchListNameResponse
import com.android.boilerplate.data.repositories.customers.response.SearchResponse
import com.android.boilerplate.data.repositories.payment.request.Payment_Show_Request
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject

class CustomerRemoteDataSource @Inject constructor(private val customerService: CustomerService) {

    suspend fun doCreate(
        account_name: String,
        address: String,
        date_plan: String,
        amount_of_installation: String,
        due_date_month: String,
        foc: String,
        modem: String,
        connector: String,
        ficamp: String,
        others: String,
        messenger: String,
        contact_number: String,
        account_balance: String,
        arrears: String,
        area: String,
        plan_subscribed: String,
    ): CustomerEachResponse {
        val request = CustomerCreateRequest(
            account_name, address, date_plan,
            amount_of_installation, due_date_month, foc, modem, connector, ficamp, others,
            messenger, contact_number, account_balance, arrears, area,plan_subscribed
        )
        val response = customerService.doCreate(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }

    suspend fun doUpdate(
        account_number: String,
        account_name: String,
        address: String,
        date_plan: String,
        amount_of_installation: String,
        due_date_month: String,
        foc: String,
        modem: String,
        connector: String,
        ficamp: String,
        others: String,
        messenger: String,
        contact_number: String,
        account_balance: String,
        arrears: String,
        area: String,
        plan_subscribed: String,
    ): CustomerEachResponse {
        val request = UpdateCustomerRequest(
            account_number,
            account_name, address, date_plan,
            amount_of_installation, due_date_month, foc, modem, connector, ficamp, others,
            messenger, contact_number, account_balance, arrears, area,plan_subscribed
        )
        val response = customerService.doUpdate(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doDelete(
        account_number: String,

        ): CustomerResponse {
        val request = SearchRequest(
            account_number
        )
        val response = customerService.doDelete(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doSearch(
        account_number: String,
    ): SearchResponse {
        val request = SearchRequest(account_number)
        val response = customerService.doSearchAccount(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun getCustomerNameList(account_name: String): SearchListNameResponse {
        val request = SearchNamesRequest(account_name)
        val response = customerService.doSearchName(request)

        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }
        return response.body() ?: throw NullPointerException("Response data is empty")
    }



}