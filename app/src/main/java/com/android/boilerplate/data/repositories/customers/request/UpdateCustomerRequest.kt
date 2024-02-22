package com.android.boilerplate.data.repositories.customers.request

data class UpdateCustomerRequest(
    val account_number: String,
    val account_name: String,
    val address: String,
    val date_plan: String,
    val amount_of_installation: String,
    val due_date_month: String,
    val foc: String,
    val modem: String,
    val connector: String,
    val ficamp: String,
    val others: String,
    val messenger: String,
    val contact_number: String,
    val account_balance: String,
    val arrears: String,
    val area:String,
    val plan_subscribed: String
)