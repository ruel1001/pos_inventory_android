package com.android.boilerplate.data.repositories.maintenance.response



import androidx.annotation.Keep

@Keep
data class MaintenanceResponse(
    val message: String?=null,
    val status: String?=null,
)