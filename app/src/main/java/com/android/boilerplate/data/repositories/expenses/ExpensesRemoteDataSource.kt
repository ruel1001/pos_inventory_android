package com.android.boilerplate.data.repositories.expenses

import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.expenses.request.ExpensesAllRequest
import com.android.boilerplate.data.repositories.expenses.request.ExpensesCreateRequest
import com.android.boilerplate.data.repositories.expenses.request.ExpensesFilterEachRequest
import com.android.boilerplate.data.repositories.expenses.request.ExpensesUpdateRequest
import com.android.boilerplate.data.repositories.expenses.response.ExpensesAllResponse
import com.android.boilerplate.data.repositories.expenses.response.ExpensesEachResponse
import com.android.boilerplate.data.repositories.expenses.response.ExpensesResponse
import com.android.boilerplate.data.repositories.maintenance.request.SalesSearchAllRequest
import com.android.boilerplate.data.repositories.maintenance.response.SalesAllResponse
import com.android.boilerplate.data.repositories.materials.request.MaterialCreateRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialFilterRequest
import com.android.boilerplate.data.repositories.materials.request.MaterialUpdateRequest
import com.android.boilerplate.data.repositories.materials.response.MaterialEachResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialResponse
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject

class ExpensesRemoteDataSource @Inject constructor(private val expensesService: ExpensesService) {

    suspend fun doCreateExpenses(
        nature_of_expenses: String,
        amount: String,


        ): MaterialResponse {
        val request = ExpensesCreateRequest(
            nature_of_expenses, amount
        )
        val response = expensesService.doCreateExpenses(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }

    suspend fun doUpdateExpenses(
        expenses_id: String,
        nature_of_expenses: String,
        amount: String,


        ): ExpensesResponse {
        val request = ExpensesUpdateRequest(expenses_id, nature_of_expenses, amount
        )
        val response = expensesService.doUpdateExpenses(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }


    suspend fun doFilterEachExpenses(expenses_id: String,

                                        ): ExpensesEachResponse {
        val request = ExpensesFilterEachRequest(expenses_id )
        val response = expensesService.doSearchEachExpenses(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }

    suspend fun getExpensesAll(nature_of_expenses: String): ExpensesAllResponse {
        val request = ExpensesAllRequest(nature_of_expenses)
        val response = expensesService.doShowAllExpenses(request)

        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }
        return response.body() ?: throw NullPointerException("Response data is empty")
    }

    suspend fun doRemoveMaterial(expenses_id: String
    ): CustomerResponse {
        val request = ExpensesFilterEachRequest(expenses_id)
        val response = expensesService.doDeleteExpenses(request)

        //Check if response code is 200 else it will throw HttpException
        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw HttpException(response)
        }

        //Will automatically throw a NullPointerException when response.body() is Null

        return response.body() ?: throw NullPointerException("Response data is empty")
    }

}