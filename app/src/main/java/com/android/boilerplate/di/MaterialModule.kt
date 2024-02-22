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
import com.android.boilerplate.data.repositories.materials.MaterialRemoteDataSource
import com.android.boilerplate.data.repositories.materials.MaterialRepository
import com.android.boilerplate.data.repositories.materials.MaterialsService
import com.android.boilerplate.data.repositories.payment.PaymentRemoteDataSource
import com.android.boilerplate.data.repositories.payment.PaymentRepository
import com.android.boilerplate.data.repositories.payment.PaymentService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class MaterialModule {

    @Provides
    fun providesMaterialService(): MaterialsService {
        return AppRetrofitService.Builder().build(
            BuildConfig.BASE_URL,
            MaterialsService::class.java
        )
    }

    @Provides
    fun providesMaterialRemoteDataSource(materialsService: MaterialsService): MaterialRemoteDataSource {
        return MaterialRemoteDataSource(materialsService)
    }

    @Provides
    fun providesMaterialRepository(materialRemoteDataSource: MaterialRemoteDataSource): MaterialRepository {
        return MaterialRepository(materialRemoteDataSource)
    }

}