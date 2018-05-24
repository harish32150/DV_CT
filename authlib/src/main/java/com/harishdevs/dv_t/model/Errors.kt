package com.harishdevs.dv_t.model

import com.google.gson.annotations.SerializedName

/**
 * Generic Message Error Body
 */
data class GenericMessageError(@SerializedName("error") val errorMessage: String)

/**
 * Request OTP Error
 */
data class RequestOTPError(@SerializedName("error") val errorBody: PhoneNoError)

/**
 * Phone NO errors
 */
data class PhoneNoError(@SerializedName("phone_number") val phoneErrors: List<String>)