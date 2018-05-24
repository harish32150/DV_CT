package com.harishdevs.dv_t.api

import com.google.gson.annotations.SerializedName

/**
 * Request OTP API RequestBody
 */
data class RequestOTPBody(@SerializedName("phone_number") private val phoneNo: String)


/**
 * OTP Login Request Body
 */
data class OTPLoginBody(@SerializedName(
    "phone_number") private val phoneNo: String, @SerializedName("otp") private val otp: String)

/**
 * PIN Login Request Body
 */
data class PINLoginBody(@SerializedName(
    "phone_number") private val phoneNo: String, @SerializedName("pin") private val pin: String)

/**
 * Create PIN Request Body
 */
data class CreatePINBody(@SerializedName("pin") private val pin: String)