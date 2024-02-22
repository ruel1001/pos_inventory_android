package com.android.boilerplate.data.repositories.materials.response

import androidx.annotation.Keep


@Keep
data class MaterialEachResponse(
    val `data`:  MaterialEachData?=null,
    val status: String?=null
) {
    @Keep
    data class  MaterialEachData(
        val amount: String?=null,
        val created_at: String?=null,
        val item: String?=null,
        val material_id: Int?=null,
        val material_name: String?=null,
        val quantity: String?=null,
        val updated_at: String?=null
    )
}