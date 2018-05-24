package com.harishdevs.dv_t.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.harishdevs.dv_t.R
import com.harishdevs.dv_t.api.OTPLoginBody
import com.harishdevs.dv_t.api.PINLoginBody
import com.harishdevs.dv_t.api.RequestOTPBody
import com.harishdevs.dv_t.databinding.ActivityLoginBinding
import com.harishdevs.dv_t.model.GenericMessageError
import com.harishdevs.dv_t.model.RequestOTPError
import com.harishdevs.dv_t.utils.onBackground
import com.harishdevs.dv_t.utils.toErrorBody
import com.harishdevs.dv_t.utils.toPhoneNoWithCountryCode

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
  override fun layoutId() = R.layout.activity_login

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)

    binding.isOTPRequested = false
    binding.isOTP = false

    /* Handle submit/login/request click */
    binding.btnSubmit.setOnClickListener {
      when {
        binding.isOTP == false -> loginWithPin()
        binding.isOTPRequested == true -> loginWithOTP()
        else -> requestOTP()
      }
    }

    /* Clear text of otp/pin on switch change */
    binding.switchLoginUsing.setOnCheckedChangeListener { _, _ ->
      binding.editOtpPin.setText("")
    }
  }

  /**
   * Login with PIN
   */
  private fun loginWithPin() {
    if (!isConnected()) {
      showNoInternet()
      return
    }

    if (!validatePhoneNo() || !validatePinOTP()) {
      return
    }

    showProgress()
    val requestBody = PINLoginBody(binding.editPhoneNo.text.toString().toPhoneNoWithCountryCode(),
        binding.editOtpPin.text.toString())

    compositeDisposable.add(
        getService().pinLogin(requestBody)
            .onBackground()
            .subscribe { response, error ->
              hideProgress()
              if (error == null) {
                saveTokenAndNavigateToHome(response.token)
              } else {
                error.toErrorBody(GenericMessageError::class.java).let {
                  showToast(it?.errorMessage ?: "Error Login")
                }
                Log.e("requestOTP:", error.message, error)
              }
            })
  }

  /**
   * Login with OTP
   */
  private fun loginWithOTP() {
    if (!isConnected()) {
      showNoInternet()
      return
    }

    if (!validatePhoneNo() || !validatePinOTP()) {
      return
    }

    showProgress()
    val requestBody = OTPLoginBody(binding.editPhoneNo.text.toString().toPhoneNoWithCountryCode(),
        binding.editOtpPin.text.toString())

    compositeDisposable.add(
        getService().otpLogin(requestBody)
            .onBackground()
            .subscribe { response, error ->
              hideProgress()
              if (error == null) {
                saveTokenAndNavigateToHome(response.token)
              } else {
                error.toErrorBody(GenericMessageError::class.java).let {
                  showToast(it?.errorMessage ?: "Error Login")
                }
                Log.e("requestOTP:", error.message, error)
              }
            })
  }

  /**
   * Request OTP
   *
   * onSuccess set binding.isOTPRequested = true
   */
  private fun requestOTP() {
    if (!isConnected()) {
      showNoInternet()
      return
    }

    if (!validatePhoneNo()) {
      return
    }

    showProgress()
    compositeDisposable.add(
        getService().requestOTP(
            RequestOTPBody(binding.editPhoneNo.text.toString().toPhoneNoWithCountryCode()))
            .onBackground()
            .doFinally { hideProgress() }
            .subscribe { response, error ->
              if (error == null) {
                showSnackbar(response.message)
                binding.isOTPRequested = true
                binding.editOtpPin.requestFocus()
              } else {
                error.toErrorBody(RequestOTPError::class.java).let {
                  showToast(it?.errorBody?.phoneErrors?.first() ?: "Error requesting OTP")
                }
                Log.e("requestOTP:", error.message, error)
              }
            })
  }

  /**
   * Store JWT Token and navigate to Home
   */
  private fun saveTokenAndNavigateToHome(token: String) {
    userRepository.setJWTToken(token)
    startActivity(Intent(this, DetailsActivity::class.java))
    finish()
  }

  /**
   * Validate phone no.
   *
   * 10 digit are required
   */
  private fun validatePhoneNo(): Boolean =
      binding.editPhoneNo.text.toString().let {
        if (it.isEmpty() || it.length != 10) {
          //phone no should be 10 characters
          binding.layoutEditPhoneNo.error = "Enter 10 digit phone no."
          binding.layoutEditPhoneNo.isErrorEnabled = true
          false
        } else {
          binding.layoutEditPhoneNo.error = null
          binding.layoutEditPhoneNo.isErrorEnabled = false
          true
        }
      }

  /**
   * Vlaidate OTP/PIN
   */
  private fun validatePinOTP(): Boolean = binding.editOtpPin.text.toString().let {
    if (it.isEmpty()) {
      binding.layoutEditOtpPin.error = "Cannot be blank"
      binding.layoutEditOtpPin.isErrorEnabled = true
      false
    } else {
      binding.layoutEditOtpPin.error = null
      binding.layoutEditOtpPin.isErrorEnabled = false
      true
    }
  }
}