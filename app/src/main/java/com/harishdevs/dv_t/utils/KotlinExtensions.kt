package com.harishdevs.dv_t.utils

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * Single on Background
 *
 * subscribe on [Schedulers.io]
 * observe on [AndroidSchedulers.mainThread]
 *
 */
fun <T> Single<T>.onBackground() = subscribeOn(
    Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

/**
 * Convert text to country code
 */
fun String.toPhoneNoWithCountryCode(countryCode: String = "+91") = "$countryCode$this"

/**
 * Is connected to Internet or not from context
 */
fun Context.isInternetConnected(): Boolean {
  (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let {
    return it.activeNetworkInfo?.isConnected ?: false
  }
}

/**
 * Convert Throwable to Error Body
 */
fun <E : Any> Throwable.toErrorBody(errorClass: Class<E>): E? {
  if (this is HttpException) {
    return try {
      Gson().fromJson<E>(this.response().errorBody()?.string(),
          errorClass)
    } catch (e: Exception) {
      null
    }
  }
  return null
}