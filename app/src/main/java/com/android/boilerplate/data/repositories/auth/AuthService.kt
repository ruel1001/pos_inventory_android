package com.android.boilerplate.data.repositories.auth

import com.android.boilerplate.data.repositories.auth.request.LoginRequest
import com.android.boilerplate.data.repositories.auth.request.RegisterRequest
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/auth/login")
    suspend fun doLogin(@Body request: LoginRequest): Response<MyLoginResponse>

    @POST("api/v1/auth/register")
    suspend fun doRegister(@Body request: RegisterRequest): Response<MyLoginResponse>

    @POST("api/auth/refresh-token")
    suspend fun doRefreshToken(): Response<LoginResponse>
}