package com.android.boilerplate.di

import com.android.boilerplate.BuildConfig
import com.android.boilerplate.data.repositories.AppRetrofitService
import com.android.boilerplate.data.repositories.article.ArticleRemoteDataSource
import com.android.boilerplate.data.repositories.article.ArticleRepository
import com.android.boilerplate.data.repositories.article.ArticleService
import com.android.boilerplate.data.repositories.customers.CustomerRemoteDataSource
import com.android.boilerplate.data.repositories.customers.CustomerRepository
import com.android.boilerplate.data.repositories.customers.CustomerService
import com.android.boilerplate.data.repositories.payment.PaymentRemoteDataSource
import com.android.boilerplate.data.repositories.payment.PaymentRepository
import com.android.boilerplate.data.repositories.payment.PaymentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class PaymentModule {

    @Provides
    fun providesPaymentService(): PaymentService {
        return AppRetrofitService.Builder().build(
            BuildConfig.BASE_URL,
            PaymentService::class.java
        )
    }

    @Provides
    fun providesPaymentRemoteDataSource(paymentService: PaymentService): PaymentRemoteDataSource {
        return PaymentRemoteDataSource(paymentService)
    }

    @Provides
    fun providesPaymentRepository(paymentRemoteDataSource: PaymentRemoteDataSource): PaymentRepository {
        return PaymentRepository(paymentRemoteDataSource)
    }

}