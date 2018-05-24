package com.harishdevs.dv_t.utils

import com.harishdevs.dv_t.BuildConfig
import com.harishdevs.dv_t.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkComponent private constructor() {
  companion object {
    private var singeInstance: NetworkComponent? = null

    fun getInstance(): NetworkComponent {
      if (singeInstance == null) {
        singeInstance = NetworkComponent()
      }
      return singeInstance!!
    }
  }


  /**
   * Get Api Service
   *
   * Create [ApiService] with OkHttpClient and Retrofit
   *
   * @return [ApiService]
   */
  fun getApiService(): ApiService {
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor().let {
        it.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(it)
      }
    }

    return Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(builder.build())
        .build().create(ApiService::class.java)
  }
}