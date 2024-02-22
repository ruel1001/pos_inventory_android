package com.android.boilerplate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserLocalData(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var user_name : String? = null,
    var username : String? = null,
    val address: String? = null,
    val email: String? = null,
    val token: String? = null
){
    fun getFullName() = "$user_name "
}
