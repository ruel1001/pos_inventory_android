package com.android.boilerplate.data.repositories.expenses.request

data class ExpensesUpdateRequest(

    val expenses_id: String,
    val nature_of_expenses: String,
    val amount: String,
)