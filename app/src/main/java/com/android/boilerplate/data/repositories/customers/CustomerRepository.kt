package com.android.boilerplate.data.repositories.customers

import com.android.boilerplate.data.local.UserLocalData
import com.android.boilerplate.data.repositories.auth.AuthLocalDataSource
import com.android.boilerplate.data.repositories.auth.request.LoginRequest
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse
import com.android.boilerplate.data.repositories.auth.response.UserData
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse.LoginxData.UserxData
import com.android.boilerplate.data.repositories.customers.response.CustomerEachResponse
import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.customers.response.SearchListNameResponse
import com.android.boilerplate.data.repositories.customers.response.SearchResponse
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import com.android.boilerplate.security.AuthEncryptedDataManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class CustomerRepository @Inject constructor(
    private val customerRemoteDataSource: CustomerRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun doCreateCustomer(account_name: String, address: String, date_plan: String,
                         amount_of_installation: String
                         , due_date_month: String, foc: String, modem: String, connector: String
                         , ficamp: String, others: String, messenger: String, contact_number: String
                         , account_balance: String, arrears: String,area:String,    plan_subscribed: String,) : Flow<CustomerEachResponse> {
        return flow{
            val response = customerRemoteDataSource.doCreate(account_name, address,date_plan,
                amount_of_installation,due_date_month,foc,modem,connector,ficamp,others,
                messenger,contact_number,account_balance,arrears,area,plan_subscribed)
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun doUpdateCustomer(account_number: String,
                         account_name: String,
                         address: String,
                         date_plan: String,
                         amount_of_installation: String,
                         due_date_month: String, foc: String, modem: String, connector: String
                         , ficamp: String, others: String, messenger: String, contact_number: String
                         , account_balance: String, arrears: String,area:String,    plan_subscribed: String,) : Flow<CustomerEachResponse> {
        return flow{
            val response = customerRemoteDataSource.doUpdate(account_number,account_name, address,date_plan,
                amount_of_installation,due_date_month,foc,modem,connector,ficamp,others,
                messenger,contact_number,account_balance,arrears,area,    plan_subscribed)
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doRemoveCustomer(account_number: String,
         ) : Flow<CustomerResponse> {
        return flow{
            val response = customerRemoteDataSource.doDelete(account_number,)
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doSearchCustomer(account_number: String) : Flow<SearchResponse> {
        return flow{
            val response = customerRemoteDataSource.doSearch(account_number)
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun getSearchNameList(account_name: String): Flow<SearchListNameResponse> {
        return flow {
            val response = customerRemoteDataSource.getCustomerNameList(account_name)
            emit(response)
        }.flowOn(ioDispatcher)
    }


    suspend fun doLogout(){
        //authLocalDataSource.logout(encryptedDataManager.getAccessToken())
    }
}