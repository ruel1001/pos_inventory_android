package com.android.boilerplate.data.repositories.payment

import com.android.boilerplate.data.local.UserLocalData
import com.android.boilerplate.data.repositories.article.response.ArticleListResponse
import com.android.boilerplate.data.repositories.auth.AuthLocalDataSource
import com.android.boilerplate.data.repositories.auth.request.LoginRequest
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse
import com.android.boilerplate.data.repositories.auth.response.UserData
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse.LoginxData.UserxData
import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentFilterResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentListResponse
import com.android.boilerplate.data.repositories.payment.response.PaymentResponse
import com.android.boilerplate.data.repositories.payment.response.ShowPaymentResponse
import com.android.boilerplate.security.AuthEncryptedDataManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class PaymentRepository @Inject constructor(
    private val paymentRemoteDataSource: PaymentRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun doCreatepayment(
        account_number: String,
        account_name: String,
        account_balance: String,
        arrears_month: String,
        amount_paid: String,
        collectors_name: String,
        billing_month: String,
    ): Flow<PaymentFilterResponse> {
        return flow {
            val response = paymentRemoteDataSource.doCreatePayment(
                account_number, account_name, account_balance,
                arrears_month, amount_paid, collectors_name, billing_month
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun getPaymentList(account_number: String): Flow<ShowPaymentResponse> {
        return flow {
            val response = paymentRemoteDataSource.getPaymentList(account_number)
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun doFilterpayment(
        payment_id: String,
    ): Flow<PaymentFilterResponse> {
        return flow {
            val response = paymentRemoteDataSource.doFilterPayment(payment_id)
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doUpdatepayment(
        payment_id: String,
        account_number: String,
        account_name: String,
        account_balance: String,
        arrears_month: String,
        amount_paid: String,
        collectors_name: String,
        billing_month: String,
    ): Flow<PaymentFilterResponse> {
        return flow {
            val response = paymentRemoteDataSource.doUpdatePayment(payment_id,
                account_number, account_name, account_balance,
                arrears_month, amount_paid, collectors_name, billing_month
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doDeletepayment(
        payment_id: String,

    ): Flow<PaymentResponse> {
        return flow {
            val response = paymentRemoteDataSource.doRemovePayment(payment_id
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun getAllPaymentList(material_name: String): Flow<PaymentListResponse> {
        return flow {
            val response = paymentRemoteDataSource.getAllPaymentList(material_name)
            emit(response)
        }.flowOn(ioDispatcher)
    }



    suspend fun doLogout() {
        //authLocalDataSource.logout(encryptedDataManager.getAccessToken())
    }
}