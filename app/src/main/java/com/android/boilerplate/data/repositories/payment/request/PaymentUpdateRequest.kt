package com.android.boilerplate.data.repositories.payment.request

data class PaymentUpdateRequest(
    val payment_id: String,
    val account_number: String,
    val account_name: String,
    val account_balance: String,
    val arrears_month: String,
    val amount_paid: String,
    val collectors_name: String,
    val billing_month: String

)