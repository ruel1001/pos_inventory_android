package com.android.boilerplate.data.repositories.expenses.request

data class ExpensesCreateRequest(

    val nature_of_expenses: String,
    val amount: String,
)