package com.harishdevs.dv_t.ui

import android.os.Bundle
import com.harishdevs.dv_t.R
import com.harishdevs.dv_t.api.CreatePINBody
import com.harishdevs.dv_t.databinding.ActivityCreatePinBinding
import com.harishdevs.dv_t.model.GenericMessageError
import com.harishdevs.dv_t.utils.Constants.Companion.PIN_LENGTH
import com.harishdevs.dv_t.utils.onBackground
import com.harishdevs.dv_t.utils.toErrorBody

class CreatePinActivity : BaseActivity<ActivityCreatePinBinding>() {
  override fun layoutId() = R.layout.activity_create_pin

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)

    binding.btnSubmit.setOnClickListener {
      createPIN()
    }
  }

  /**
   * Validate and create PIN
   */
  private fun createPIN() {
    if (!isConnected()) {
      showNoInternet()
      return
    }

    if (!validate()) {
      return
    }

    val pin = binding.editPin.text.toString()
    val requestBody = CreatePINBody(pin)

    showProgress()
    compositeDisposable.add(
        getService().createPIN(userRepository.getJWTTokenAsBearer(), requestBody)
            .onBackground()
            .subscribe { response, error ->
              hideProgress()
              if (error == null) {
                showToast(response.message)
                finish()
              } else {
                error.toErrorBody(GenericMessageError::class.java).let {
                  showToast(it?.errorMessage ?: "Error creating PIN")
                }
              }
            })
  }

  /**
   * Validate PIN and Confirm PIN
   */
  private fun validate(): Boolean {
    val pin = binding.editPin.text.toString()
    val cnfPin = binding.editCnfPin.text.toString()

    if (pin.length != PIN_LENGTH) {
      binding.layoutEditPin.isErrorEnabled = true
      binding.layoutEditPin.error = "PIN should be 6 digits"
      return false
    } else {
      binding.layoutEditPin.isErrorEnabled = false
      binding.layoutEditPin.error = null
    }

    if (cnfPin != pin) {
      binding.layoutEditCnfPin.isErrorEnabled = true
      binding.layoutEditCnfPin.error = "Confirm PIN did not match"
      return false
    } else {
      binding.layoutEditCnfPin.isErrorEnabled = false
      binding.layoutEditCnfPin.error = null
    }

    return true
  }
}