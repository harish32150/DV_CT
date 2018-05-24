package com.harishdevs.dv_t.api

import com.google.gson.annotations.SerializedName

/**
 * Request OTP Response with message
 */
data class RequestOTPResponse(@SerializedName("success") val message: String)

/**
 * Login with OTP/PIN Response with JWT Token
 */
data class LoginResponse(@SerializedName("jwt") val token: String)

/**
 * User details response
 *
 * Note -- Only Few Params are added for representational purposes
 */
data class UserDetailsResponse(@SerializedName("phone_number") val phoneNo: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("created_on") val createdOn: String,
    @SerializedName("email") val email: String,
    @SerializedName("employee_id") val empId: String)

/**
 * Request OTP Response with message
 */
data class CreatePINResponse(@SerializedName("success") val message: String)
