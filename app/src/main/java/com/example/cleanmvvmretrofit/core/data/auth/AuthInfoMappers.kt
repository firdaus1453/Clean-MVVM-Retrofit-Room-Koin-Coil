package com.example.cleanmvvmretrofit.core.data.auth

import com.example.cleanmvvmretrofit.core.domain.AuthInfo

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        userId = userId,
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        userId = userId,
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}
