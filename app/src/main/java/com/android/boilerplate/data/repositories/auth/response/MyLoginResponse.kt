package com.android.boilerplate.data.repositories.auth.response



import androidx.annotation.Keep

@Keep
data class MyLoginResponse(
    var `data`: LoginxData? = null,
    var message: String? = null,
    var status: String? = null
) {
    @Keep
    data class LoginxData(
        var token: String? = null,
        var user: UserxData? = null
    ) {
        @Keep
        data class UserxData(
            var created_at: String? = null,
            var email: String? = null,
            var email_verified_at: Any? = null,
            var id: String? = null,
            var user_name: String? = null,
            var username: String? = null,
            var address: String? = null,
            var updated_at: String? = null,
            var uuid: String? = null
        )
    }
}