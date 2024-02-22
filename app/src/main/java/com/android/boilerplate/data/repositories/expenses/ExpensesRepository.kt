package com.android.boilerplate.data.repositories.expenses

import com.android.boilerplate.data.repositories.customers.response.CustomerResponse
import com.android.boilerplate.data.repositories.expenses.response.ExpensesAllResponse
import com.android.boilerplate.data.repositories.expenses.response.ExpensesEachResponse
import com.android.boilerplate.data.repositories.expenses.response.ExpensesResponse
import com.android.boilerplate.data.repositories.maintenance.MaintenanceRemoteDataSource
import com.android.boilerplate.data.repositories.maintenance.response.MaintenanceResponse
import com.android.boilerplate.data.repositories.maintenance.response.SalesAllResponse
import com.android.boilerplate.data.repositories.maintenance.response.SearchMaintenanceResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialEachResponse
import com.android.boilerplate.data.repositories.materials.response.MaterialResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ExpensesRepository @Inject constructor(
    private val expensesRemoteDataSource: ExpensesRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun doCreateExpenses(
        nature_of_expenses: String,
        amount: String,

    ): Flow<MaterialResponse> {
        return flow {
            val response = expensesRemoteDataSource.doCreateExpenses(
                nature_of_expenses, amount
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doUpdateExpenses(
        expenses_id: String,
        nature_of_expenses: String,
        amount: String,

        ): Flow<ExpensesResponse> {
        return flow {
            val response = expensesRemoteDataSource.doUpdateExpenses(expenses_id,
                nature_of_expenses,
                amount
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun getExpensesList(nature_of_expenses: String): Flow<ExpensesAllResponse> {
        return flow {
            val response = expensesRemoteDataSource.getExpensesAll(nature_of_expenses)
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doEachExpenses(
        expenses_id: String,
    ): Flow<ExpensesEachResponse> {
        return flow {
            val response = expensesRemoteDataSource.doFilterEachExpenses(expenses_id)
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun doDeleteExpenses(
        expenses_id: String,

        ): Flow<CustomerResponse> {
        return flow {
            val response = expensesRemoteDataSource.doRemoveMaterial(expenses_id
            )
            emit(response)
        }.flowOn(ioDispatcher)
    }

    suspend fun doLogout() {
        //authLocalDataSource.logout(encryptedDataManager.getAccessToken())
    }
}