package com.harishdevs.dv_t.ui

import android.app.ProgressDialog
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.harishdevs.dv_t.R
import com.harishdevs.dv_t.repository.UserRepository
import com.harishdevs.dv_t.utils.NetworkComponent
import com.harishdevs.dv_t.utils.isInternetConnected
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

  /**
   * Get Layout resource id for Data binding
   */
  @LayoutRes
  abstract fun layoutId(): Int

  protected lateinit var binding: B


  protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

  private val progressDialog: ProgressDialog by lazy {
    ProgressDialog(this, R.style.SpinnerDialog)
  }

  protected val userRepository by lazy { UserRepository.getInstance() }
  protected val networkComponent by lazy { NetworkComponent.getInstance() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, layoutId())

    progressDialog.setCancelable(false)
    progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small)
  }

  override fun onDestroy() {
    super.onDestroy()
    if (!compositeDisposable.isDisposed) {
      compositeDisposable.dispose()
    }
  }

  protected fun getService() = networkComponent.getApiService()

  protected fun showProgress() {
    progressDialog.show()
  }

  protected fun hideProgress() {
    progressDialog.hide()
  }

  protected fun showSnackbar(msg: String) {
    Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
  }

  protected fun showNoInternet() {
    showSnackbar(getString(R.string.error_no_internet))
  }

  protected fun showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
  }

  protected fun isConnected() = isInternetConnected()
}
