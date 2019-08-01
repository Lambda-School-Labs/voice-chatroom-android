package com.lambda.voicechatroom

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class JsonResponse(
    val message: String,
    @SerializedName("data")
    val user: User
)

@Serializable
data class Data(
    @SerializedName("User")
    val user: User
)

@Serializable
data class User(
    val accountBalance: String,
    val avatar: String,
    val billingSubscription: String,
    val callStatus: Boolean,
    val createdAt: String,
    var displayName: String,
    var email: String,
    var firstName: String?,
    val id: String,
    val last4: String?,
    var lastName: String?,
    var phoneNumber: String?,
    val stripeId: String?
)