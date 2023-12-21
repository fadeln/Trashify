package com.example.trashify.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.trashify.databinding.ActivityRegisterBinding
import com.example.trashify.model.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIsNotEmpty()

        registerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RegisterViewModel::class.java]

        registerViewModel.auth.observe(this){
            if (it.error != true){
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Unkown Error", Toast.LENGTH_SHORT).show()
            }

            checkIsNotEmpty()
        }

        registerViewModel.isLoading.observe(this){
            setButtonLoading(it)
        }

        binding.buttonId.setOnClickListener() {
            val intentDetail = Intent(this, LoginActivity::class.java)
            intentDetail.flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentDetail)
        }

        binding.password.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIsNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.buttonCustom.setOnClickListener{
            if (checkIsNotEmpty()){
                registerViewModel.register(
                    RegisterViewModel.Companion.FormRegister(
                        email = binding.email.text.toString(),
                        password = binding.password.text.toString(),
                        name = binding.name.text.toString()
                    )
                )
            }
        }

    }

    private fun setButtonEnable(value: Boolean){
        binding.buttonCustom.isEnabled = value
    }

    private fun setButtonLoading(value: Boolean){
        setButtonEnable(false)
        binding.buttonCustom.setLoading(value)
    }

    private fun checkIsNotEmpty() : Boolean{
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9.-]\$"

        if (!binding.email.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty() && !binding.name.text.isNullOrEmpty()){
            if (binding.password.text!!.length >= 8 && binding.email.text!!.matches(emailRegex.toRegex())){
                setButtonEnable(true)
                return true
            }else{
                setButtonEnable(false)
            }
        }else{
            setButtonEnable(false)
        }
        return false
    }
}