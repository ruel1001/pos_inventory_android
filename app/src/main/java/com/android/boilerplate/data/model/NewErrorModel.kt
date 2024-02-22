package com.android.boilerplate.data.model

data class NewErrorModel(
    val message: NewErrorsData? = null,
    val status: String?  = null,


)

data class NewErrorsData(
    var email: List<String>? = null,
    var password: List<String>? = null,
    var user_name: List<String>? = null,
    var address: List<String>? = null,
    var username: List<String>? = null,
    var password_confirmation: List<String>? = null,
    var firstname: List<String>? = null,
    var lastname: List<String>? = null,
    var middlename: List<String>? = null,
    var phone_number: List<String>? = null,
    var name: List<String>? = null,
    var account_name: List<String>? = null,
    var amount_of_installation: List<String>? = null,
    var date_plan: List<String>? = null,
    var due_date_month: List<String>? = null,
    var foc: List<String>? = null,
    var modem: List<String>? = null,
    var connector: List<String>? = null,
    var ficamp: List<String>? = null,
    var others: List<String>? = null,
    var messenger: List<String>? = null,
    var contact_number: List<String>? = null,
    var account_balance: List<String>? = null,
    var arrears: List<String>? = null,
    var area: List<String>? = null,
    var desc: List<String>? = null
)
