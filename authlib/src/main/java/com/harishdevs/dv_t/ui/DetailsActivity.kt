package com.harishdevs.dv_t.ui

import android.content.Intent
import android.os.Bundle
import com.harishdevs.dv_t.R
import com.harishdevs.dv_t.api.UserDetailsResponse
import com.harishdevs.dv_t.databinding.ActivityHomeBinding
import com.harishdevs.dv_t.databinding.ViewUserDetailItemBinding
import com.harishdevs.dv_t.model.GenericMessageError
import com.harishdevs.dv_t.utils.onBackground
import com.harishdevs.dv_t.utils.toErrorBody

class DetailsActivity : BaseActivity<ActivityHomeBinding>() {
  override fun layoutId() = R.layout.activity_home

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)

    /* Start Create PIN Activity */
    binding.btnCreatePin.setOnClickListener {
      startActivity(Intent(this, CreatePinActivity::class.java))
    }

    /* Fetch User Details and bind to UI */
    fetchUserDetails()
  }

  private fun fetchUserDetails() {
    if (!isConnected()) {
      showNoInternet()
      return
    }

    showProgress()
    compositeDisposable.add(
        getService().userDetails(userRepository.getUserId(), userRepository.getJWTTokenAsBearer())
            .onBackground()
            .doFinally { hideProgress() }
            .subscribe { user, error ->
              if (error == null) {
                bindUserDetails(user)
              } else {
                error.toErrorBody(GenericMessageError::class.java).let {
                  showToast(it?.errorMessage ?: "Error getting User details")
                }
              }
            })
  }

  private fun bindUserDetails(userDetails: UserDetailsResponse) {
    bindUserDetailItem("Firstname", userDetails.firstName)
    bindUserDetailItem("Lastname", userDetails.lastName)
    bindUserDetailItem("Email", userDetails.email)
    bindUserDetailItem("Phone No", userDetails.phoneNo)
    bindUserDetailItem("Emp ID", userDetails.empId)
  }

  /**
   * Create Binding and bind details to UI
   */
  private fun bindUserDetailItem(title: String, value: String) {
    val itemBinding = ViewUserDetailItemBinding.inflate(layoutInflater,
        binding.containerUserDetails, false)
    itemBinding.title = title
    itemBinding.value = value
    binding.containerUserDetails.addView(itemBinding.root)
  }
}