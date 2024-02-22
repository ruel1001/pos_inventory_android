package com.android.boilerplate.data.repositories.expenses.response

import androidx.annotation.Keep


@Keep
data class ExpensesAllResponse(
    val `data`: List<ExpensesAllData>?=null,
    val status: String?=null,
    val total_amount_expenses: String?=null,
) {
    @Keep
    data class ExpensesAllData(
        val amount: String?=null,
        val created_at: String?=null,
        val expenses_id: String?=null,
        val nature_of_expenses: String?=null,
        val updated_at: String?=null,
    )
}