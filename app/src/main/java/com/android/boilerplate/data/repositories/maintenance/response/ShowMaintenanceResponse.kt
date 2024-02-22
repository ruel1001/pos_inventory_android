package com.android.boilerplate.data.repositories.maintenance.response

import androidx.annotation.Keep


@Keep
data class ShowMaintenanceResponse(
    val `data`: List<ShowListMaintenanceData>?=null,
    val status: String?=null,
) {
    @Keep
    data class ShowListMaintenanceData(
        val account_name: String?=null,
        val account_number: String?=null,
        val address: String?=null,
        val area: String?=null,
        val created_at: String?=null,
        val maintenance_id: Int?=null,
        val material_used: String?=null,
        val nature_of_repair: String?=null,
        val material_id: String?=null,
        val updated_at: String?=null,
        val material_quantity_used: String?=null,
    )
}