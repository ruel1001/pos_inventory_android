package com.android.boilerplate.data.repositories.auth

import com.android.boilerplate.data.local.UserLocalData
import com.android.boilerplate.data.repositories.auth.request.LoginRequest
import com.android.boilerplate.data.repositories.auth.response.LoginResponse
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse
import com.android.boilerplate.data.repositories.auth.response.UserData
import com.android.boilerplate.data.repositories.auth.response.MyLoginResponse.LoginxData.UserxData
import com.android.boilerplate.security.AuthEncryptedDataManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

//class AuthRepository @Inject constructor(
//    private val authRemoteDataSource: AuthRemoteDataSource,
//    private val encryptedDataManager: AuthEncryptedDataManager,
//    private val authLocalDataSource: AuthLocalDataSource,
//    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
//) {
//
//    fun doLogin(email: String, password: String): Flow<LoginResponse> {
//        return flow {
//            val response = authRemoteDataSource.doLogin(email, password)
//            val userInfo = response.data?: UserData()
//            val token = response.token.orEmpty()
//            encryptedDataManager.setAccessToken(token)
//            encryptedDataManager.setUserBasicInfo(userInfo)
//            emit(response)
//        }.flowOn(ioDispatcher)
//    }
//}

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val encryptedDataManager: AuthEncryptedDataManager,
    private val authLocalDataSource: AuthLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun doLogin(email : String, password : String) : Flow<MyLoginResponse> {
        return flow{
            val response = authRemoteDataSource.doLogin(email, password)
            val userInfo = response.data?.user?: UserxData()
            val token = response.data?.token.orEmpty()
            encryptedDataManager.setAccessToken(token)
            encryptedDataManager.setProfile(response.data?.user?.user_name,response.data?.user?.address)
           // authLocalDataSource.login(setUpUserLocalData(userInfo, token))
            emit(response)
        }.flowOn(ioDispatcher)
    }


    fun doRegister( user_name : String, email : String, address : String, user_type : String,username: String,password: String) : Flow<MyLoginResponse> {
        return flow{
            val response = authRemoteDataSource.doRegister(user_name, email,address,user_type,username,password)
            val userInfo = response.data?.user?: UserxData()
            val token = response.data?.token.orEmpty()
            encryptedDataManager.setAccessToken(token)
           // authLocalDataSource.login(setUpUserLocalData(userInfo, token))
            emit(response)
        }.flowOn(ioDispatcher)
    }

    fun doRefreshToken() : Flow<LoginResponse> {
        return flow{
            val response = authRemoteDataSource.doRefreshToken()
            val userInfo = response.data?: UserData()
            val token = response.token.orEmpty()
            encryptedDataManager.setAccessToken(token)
           // authLocalDataSource.updateToken(userInfo.user_id?:0, token)
            emit(response)
        }.flowOn(ioDispatcher)
    }

    private fun setUpUserLocalData(user : UserxData?, tokens: String) : UserLocalData {
        return UserLocalData(
            id=user?.id?.toInt(),
            email = user?.email,
            username = user?.username,
            token = tokens
        )
    }

    fun getUserInfo() : Flow<UserLocalData>{
        return flow{
            val userLocalData = authLocalDataSource.getUserInfo(encryptedDataManager.getAccessToken())
            emit(userLocalData)
        }.flowOn(ioDispatcher)
    }

    suspend fun doLogout(){
        authLocalDataSource.logout(encryptedDataManager.getAccessToken())
    }
}