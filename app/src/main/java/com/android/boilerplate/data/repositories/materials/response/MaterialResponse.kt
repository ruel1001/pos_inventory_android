package com.android.boilerplate.data.repositories.materials.response

import androidx.annotation.Keep


@Keep
data class MaterialResponse(
    val message: String?=null,
    val status: String?=null
)