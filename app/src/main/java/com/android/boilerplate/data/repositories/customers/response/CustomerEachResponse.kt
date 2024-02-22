package com.android.boilerplate.data.repositories.customers.response

import androidx.annotation.Keep


@Keep
data class CustomerEachResponse(
    val `data`: CustomerEachData?=null,
    val message: String?=null,
    val status: String?=null,
) {
    @Keep
    data class  CustomerEachData(
        val account_balance: String?=null,
        val account_name: String?=null,
        val account_number: Int?=null,
        val address: String?=null,
        val amount_of_installation: String?=null,
        val area: String?=null,
        val arrears: String?=null,
        val connector: String?=null,
        val contact_number: String?=null,
        val created_at: String?=null,
        val date_plan: String?=null,
        val due_date_month: String?=null,
        val ficamp: String?=null,
        val foc: String?=null,
        val messenger: String?=null,
        val modem: String?=null,
        val others: String?=null,
        val updated_at: String?=null,
        val plan_subscribed: String?=null,
    )
}