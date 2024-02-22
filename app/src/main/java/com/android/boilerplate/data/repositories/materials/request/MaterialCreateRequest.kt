package com.android.boilerplate.data.repositories.materials.request

data class MaterialCreateRequest(

    val material_name: String,
    val quantity: String,
    val item: String,
    val amount: String,
    val sale_amount: String
)