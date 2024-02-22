package com.android.boilerplate.data.repositories.payment.response

import androidx.annotation.Keep


@Keep
data class PaymentResponse(
    val message: String?=null,
    val status: String?=null
)