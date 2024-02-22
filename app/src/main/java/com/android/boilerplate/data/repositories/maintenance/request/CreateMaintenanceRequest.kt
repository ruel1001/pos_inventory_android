package com.android.boilerplate.data.repositories.maintenance.request

data class CreateMaintenanceRequest(
    val account_number: String,
    val account_name: String,
    val address: String,
    val area: String,
    val material_used: String,
    val nature_of_repair: String,
    val material_id: String,
    val material_quantity_used: String,
)