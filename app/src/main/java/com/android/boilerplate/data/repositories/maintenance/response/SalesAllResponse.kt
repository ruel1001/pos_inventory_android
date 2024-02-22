package com.android.boilerplate.data.repositories.maintenance.response


import androidx.annotation.Keep

@Keep
data class SalesAllResponse(
    val `data`: List<SalesAllData>?=null,
    val status: String?=null,
    val total_amount_sales: String?=null,
    val total_amount_gain: String?=null,
) {
    @Keep
    data class SalesAllData(
        val amount_gain: String?=null,
        val amount_sales: String?=null,
        val created_at: String?=null,
        val maintenance_id: String?=null,
        val material_id: String?=null,
        val material_name: String?=null,
        val sales_id: String?=null,
        val updated_at: String?=null,

    )
}