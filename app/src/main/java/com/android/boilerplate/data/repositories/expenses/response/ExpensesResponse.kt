package com.android.boilerplate.data.repositories.expenses.response

import androidx.annotation.Keep


@Keep
data class ExpensesResponse(
    val message: String?=null,
    val status: String?=null
)