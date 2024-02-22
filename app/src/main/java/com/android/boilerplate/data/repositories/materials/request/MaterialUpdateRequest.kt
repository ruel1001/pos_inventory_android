package com.android.boilerplate.data.repositories.materials.request

data class MaterialUpdateRequest(
    val material_id: String,
    val material_name: String,
    val quantity: String,
    val item: String,
    val amount: String,
    val sale_amount: String
)