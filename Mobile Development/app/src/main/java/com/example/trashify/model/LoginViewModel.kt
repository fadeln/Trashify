package com.example.trashify.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trashify.data.response.LoginResponse
import com.example.trashify.data.retrofit.ApiConfig
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _auth = MutableLiveData<LoginResponse>()
    val auth: LiveData<LoginResponse> = _auth

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(formLogin: FormLogin){
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email = formLogin.email, password = formLogin.password)
        client.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: retrofit2.Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                Log.e(TAG, "call: ${response.isSuccessful} $response")
                if (response.isSuccessful) {
                    _auth.value = response.body()
                } else {
                    if (response.code() == 401){
                        _auth.value = LoginResponse(error = true, message = "Wrong Password")
                    } else if (response.code() == 400) {

                        _auth.value = LoginResponse(error = true, message = "Wrong Password or Email")
                    } else {
                        _auth.value = LoginResponse(error = true, message = response.message())
                    }
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                if (t.message.toString().startsWith("failed to connect")){
                    _auth.value = LoginResponse(error = true, message = "Failed to connect API")
                }
                _auth.value = LoginResponse(error = true, message = t.message)
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "LOGIN_VIEW_MODEL"

        data class FormLogin(val email: String, val password: String)
    }
}