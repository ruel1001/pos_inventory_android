package com.android.boilerplate.data.repositories.payment.response


import androidx.annotation.Keep

@Keep
data class PaymentListResponse(
    val `data`: List<PaymentListData>?=null,
    val status: String?=null,
    val total_amount: String?=null,
) {
    @Keep
    data class PaymentListData(
        val account_balance: String?=null,
        val account_name: String?=null,
        val account_number: String?=null,
        val amount_paid: String?=null,
        val arrears_month: String?=null,
        val billing_month: String?=null,
        val collectors_name: String?=null,
        val created_at: String?=null,
        val payment_id: String?=null,
        val updated_at: String?=null,
    )
}