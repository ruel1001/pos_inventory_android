package com.android.boilerplate.data.repositories.auth.request

data class RegisterRequest(
    val user_name: String,
    val email: String,
    val address: String,
    val user_type: String,
    val username: String,
    val password: String
)