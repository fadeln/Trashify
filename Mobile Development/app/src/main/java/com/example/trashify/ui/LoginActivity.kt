package com.example.trashify.ui
import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.trashify.MainActivity
import com.example.trashify.ViewModelFactory
import com.example.trashify.components.ButtonCustom
import com.example.trashify.databinding.ActivityLoginBinding
import com.example.trashify.helper.requestPermissionLauncher
import com.example.trashify.model.AuthViewModel
import com.example.trashify.model.LoginViewModel
import com.example.trashify.preferences.AuthPreferences
import com.example.trashify.preferences.dataStore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var button: ButtonCustom
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissionLauncher(this, REQUIRED_PERMISSION)

        val pref = AuthPreferences.getInstance(application.dataStore)

        button = binding.buttonCustom
        val registerText = binding.buttonId

        val tokenViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]

        loginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[LoginViewModel::class.java]

        checkIsNotEmpty()

        loginViewModel.auth.observe(this) {
            if (it.error == true) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            } else if (it.error == false) {
                tokenViewModel.saveToken(
                    token = it.loginResult!!.token!!,
                    name = it.loginResult!!.name!!,
                    userId = it.loginResult!!.userId!!
                )
                checkIsNotEmpty()
            } else {
                Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show()
            }

            if (!binding.email.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty()) {
                setButtonEnable(true)
            }
        }

        loginViewModel.isLoading.observe(this) {
            setButtonLoading(it)
        }

        tokenViewModel.getToken().observe(this) {
            if (it.token != "" && it.token?.isNotEmpty() == true) {
                val intentDetail = Intent(this, MainActivity::class.java)
                intentDetail.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intentDetail.putExtra(MainActivity.TOKEN_INTENT_KEY, it)
                startActivity(intentDetail)
            }
        }

        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIsNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkIsNotEmpty()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        button.setOnClickListener {
            if (checkIsNotEmpty()) {
                loginViewModel.login(
                    LoginViewModel.Companion.FormLogin(
                        email = binding.email.text.toString(),
                        password = binding.password.text.toString()
                    )
                )
            } else {
                Toast.makeText(this, "Please enter data corectly", Toast.LENGTH_SHORT).show()
            }
        }

        registerText.setOnClickListener {
            val intentDetail = Intent(this, RegisterActivity::class.java)
            startActivity(intentDetail)
        }
    }

    private fun setButtonEnable(value: Boolean) {
        button.isEnabled = value
    }

    private fun setButtonLoading(value: Boolean) {
        setButtonEnable(false)
        button.setLoading(value)
    }

    private fun checkIsNotEmpty(): Boolean {

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9.-]\$"

        if (!binding.email.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty()) {
            if (binding.password.text!!.length >= 8 && binding.email.text!!.matches(emailRegex.toRegex())) {
                setButtonEnable(true)
                return true
            } else {

                setButtonEnable(false)
            }
        } else {
            setButtonEnable(false)
        }
        return false
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.INTERNET
    }
}