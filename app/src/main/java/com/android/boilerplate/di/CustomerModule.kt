package com.android.boilerplate.di

import com.android.boilerplate.BuildConfig
import com.android.boilerplate.data.repositories.AppRetrofitService
import com.android.boilerplate.data.repositories.article.ArticleRemoteDataSource
import com.android.boilerplate.data.repositories.article.ArticleRepository
import com.android.boilerplate.data.repositories.article.ArticleService
import com.android.boilerplate.data.repositories.customers.CustomerRemoteDataSource
import com.android.boilerplate.data.repositories.customers.CustomerRepository
import com.android.boilerplate.data.repositories.customers.CustomerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class CustomerModule {

    @Provides
    fun providesCustomerService(): CustomerService {
        return AppRetrofitService.Builder().build(
            BuildConfig.BASE_URL,
            CustomerService::class.java
        )
    }

    @Provides
    fun providesCustomerRemoteDataSource(authService: CustomerService): CustomerRemoteDataSource {
        return CustomerRemoteDataSource(authService)
    }

    @Provides
    fun providesCustomerRepository(customerRemoteDataSource: CustomerRemoteDataSource): CustomerRepository {
        return CustomerRepository(customerRemoteDataSource)
    }

}