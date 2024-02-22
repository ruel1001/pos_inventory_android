package com.android.boilerplate.data.repositories.customers.response



import androidx.annotation.Keep

@Keep
data class CustomerResponse(
    val message: String?=null,
    val status: String?=null
)