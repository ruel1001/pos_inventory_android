package com.android.boilerplate.data.repositories.materials

import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.maintenance.request.Maintenance_Filter_Request
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.ShowMaintenanceResponse
import com.android.boilerplate.data.repositories.materials.request.MaterialCreateRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialFilterRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialSearchAllRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialUpdateRequest
import com.android.boilerplate.data.repositories.materials.response.MaterialEachResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialResponse
import com.android.boilerplate.data.repositories.materials.response.ShowMaterialListResponse
import com.android.boilerplate.data.repositories.payment.request.Payment_Show_Request
import com.android.boilerplate.data.repositories.payment.response.PaymentResponse
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject

class MaterialRemoteDataSource @Inject constructor(private val materialsService: MaterialsService) {

    suspend fun doCreateMaterial(
        material_name: String,
        quantity: String,
        item: String,
        amount: String,
        sale_amount: String

        ): MaterialResponse {
        val request = MaterialCreateRequest(
            material_name, quantity, item,
            amount,sale_amount
        )
        val response = materialsService.doCreateMaterial(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }

    suspend fun getMaterialAll(material_name: String): ShowMaterialListResponse {
        val request = MaterialSearchAllRequest(material_name)
        val response = materialsService.doShowAllMaterial(request)

        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }
        return response.body() ?: throw NullPointerException("Response data is empty")
    }

    suspend fun doUpdateMaterial(
        material_id: String,
        material_name: String,
        quantity: String,
        item: String,
        amount: String,
        sale_amount: String
        ): MaterialResponse {
        val request = MaterialUpdateRequest(material_id,
            material_name, quantity, item,
            amount,sale_amount
        )
        val response = materialsService.doUpdateMaterial(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doFilterEachMaterial(material_id: String,

                                        ): MaterialEachResponse {
        val request = MaterialFilterRequest(material_id )
        val response = materialsService.doSearchEachMaterial(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }

    suspend fun doRemoveMaterial(material_id: String
    ): CustomerResponse {
        val request = MaterialFilterRequest(material_id)
        val response = materialsService.doDeleteMaterial(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }

}