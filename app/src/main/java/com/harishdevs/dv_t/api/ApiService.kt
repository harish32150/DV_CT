package com.harishdevs.dv_t.api

import io.reactivex.Single
import retrofit2.http.*

interface ApiService {

  /**
   * Request OTP API call
   */
  @POST("request-otp/")
  fun requestOTP(@Body requestBody: RequestOTPBody): Single<RequestOTPResponse>


  /**
   * Login with OTP
   */
  @POST("auth-otp-login/")
  fun otpLogin(@Body requestBody: OTPLoginBody): Single<LoginResponse>

  /**
   * Login with PIN
   */
  @POST("v2/login-with-pin/")
  fun pinLogin(@Body requestBody: PINLoginBody): Single<LoginResponse>

  /**
   * Get User Details
   */
  @GET("v2/users/{id}")
  fun userDetails(@Path("id") userId: String, @Header(
      "Authorization") authHeader: String): Single<UserDetailsResponse>

  /**
   * Create PIN
   *
   * Should be called after login
   */
  @POST("v2/create-pin/")
  fun createPIN(@Header(
      "Authorization") authHeader: String, @Body requestBody: CreatePINBody): Single<CreatePINResponse>
}