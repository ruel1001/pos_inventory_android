package com.android.boilerplate.data.repositories.materials.response



import androidx.annotation.Keep

@Keep
data class ShowMaterialListResponse(
    val `data`: List<MaterialListData>?=null,
    val status: String?=null,
) {
    @Keep
    data class MaterialListData(
        val amount: String?=null,
        val created_at: String?=null,
        val item: String?=null,
        val material_id: String?=null,
        val material_name: String?=null,
        val quantity: String?=null,
        val updated_at: String?=null
    )
}