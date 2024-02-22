package com.android.boilerplate.data.repositories.expenses.response

import androidx.annotation.Keep


@Keep
data class ExpensesEachResponse(
    val `data`: ExpensesEachData?=null,
    val status: String?=null
) {
    @Keep
    data class ExpensesEachData(
        val amount: String?=null,
        val created_at: String?=null,
        val expenses_id: Int?=null,
        val nature_of_expenses: String?=null,
        val updated_at: String?=null
    )
}