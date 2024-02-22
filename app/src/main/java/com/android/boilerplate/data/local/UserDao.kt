package com.android.boilerplate.data.local

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun login(user : UserLocalData): Long

    @Query("UPDATE users SET token = :token WHERE id =:userId")
    suspend fun updateToken(userId : Int, token: String)

    @Query("SELECT * FROM users WHERE token = :access_token")
    suspend fun getUserInfo(access_token: String): UserLocalData

    @Query("DELETE FROM users WHERE token = :access_token")
    suspend fun logout(access_token: String)
}