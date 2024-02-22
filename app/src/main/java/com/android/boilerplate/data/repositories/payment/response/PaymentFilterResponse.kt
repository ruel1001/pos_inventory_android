package com.android.boilerplate.data.repositories.payment.response



import androidx.annotation.Keep

@Keep
data class PaymentFilterResponse(
    val `data`: FilterPaymentData?=null,
    val message: String?=null,
    val status: String?=null,
) {
    @Keep
    data class FilterPaymentData(
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
        val plan_subscribed: String?=null,
    )
}