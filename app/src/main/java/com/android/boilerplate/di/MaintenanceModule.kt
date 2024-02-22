package com.android.boilerplate.di

import com.android.boilerplate.BuildConfig
import com.android.boilerplate.data.repositories.AppRetrofitService
import com.android.boilerplate.data.repositories.article.ArticleRemoteDataSource
import com.android.boilerplate.data.repositories.article.ArticleRepository
import com.android.boilerplate.data.repositories.article.ArticleService
import com.android.boilerplate.data.repositories.customers.CustomerRemoteDataSource
import com.android.boilerplate.data.repositories.customers.CustomerRepository
import com.android.boilerplate.data.repositories.customers.CustomerService
import com.android.boilerplate.data.repositories.maintenance.MaintenanceRemoteDataSource
import com.android.boilerplate.data.repositories.maintenance.MaintenanceRepository
import com.android.boilerplate.data.repositories.maintenance.MaintenanceService
import com.android.boilerplate.data.repositories.payment.PaymentRemoteDataSource
import com.android.boilerplate.data.repositories.payment.PaymentRepository
import com.android.boilerplate.data.repositories.payment.PaymentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class MaintenanceModule {

    @Provides
    fun providesMaintenanceService(): MaintenanceService {
        return AppRetrofitService.Builder().build(
            BuildConfig.BASE_URL,
            MaintenanceService::class.java
        )
    }

    @Provides
    fun providesMaintenanceRemoteDataSource(maintenanceService: MaintenanceService): MaintenanceRemoteDataSource {
        return MaintenanceRemoteDataSource(maintenanceService)
    }

    @Provides
    fun providesMaintenanceRepository(maintenanceRemoteDataSource: MaintenanceRemoteDataSource): MaintenanceRepository {
        return MaintenanceRepository(maintenanceRemoteDataSource)
    }

}