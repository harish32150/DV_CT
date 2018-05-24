package com.harishdevs.dv_t.repository

import com.auth0.android.jwt.JWT

class UserRepository private constructor() {
  companion object {
    private var singleInstance: UserRepository? = null

    fun getInstance(): UserRepository {
      if (singleInstance == null) {
        singleInstance = UserRepository()
      }
      return singleInstance!!
    }
  }

  private var jwtToken: String? = null


  /**
   * Set JWT Token after success login
   */
  fun setJWTToken(token: String) {
    this.jwtToken = token
  }

  /**
   * Get JWT Token
   */
  fun getJWTToken() = jwtToken

  /**
   * Get JWT Token as Bearer Header
   */
  fun getJWTTokenAsBearer() = "Bearer $jwtToken"

  /**
   * Get User ID from jwt token
   */
  fun getUserId() = jwtToken.let {
    if (it != null) {
      JWT(it).subject ?: ""
    } else {
      "-"
    }
  }
}