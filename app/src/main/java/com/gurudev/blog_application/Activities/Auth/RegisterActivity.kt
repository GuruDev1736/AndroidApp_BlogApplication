package com.gurudev.blog_application.Activities.Auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.gurudev.blog_application.Constants.Constant
import com.gurudev.blog_application.R
import com.gurudev.blog_application.RequestModel.RegisterRequestModel
import com.gurudev.blog_application.ResponseModel.RegisterResponseModel
import com.gurudev.blog_application.Retrofit.ApiUtilities
import com.gurudev.blog_application.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signUp.setOnClickListener {
            val userName = binding.userName.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val about = binding.about.text.toString()

            if (validation(userName, email, password, about)) {
                val model = RegisterRequestModel(userName, email, password, about)
                CallApi(model)
            }
        }
    }

    private fun CallApi(model : RegisterRequestModel) {
        ApiUtilities.getInstance().register(model).enqueue(object : Callback<RegisterResponseModel>{
            override fun onResponse(
                call: Call<RegisterResponseModel>,
                response: Response<RegisterResponseModel>
            ) {
                if (response.isSuccessful)
                {
                    val data = response.body()
                    if (data!=null)
                    {
                        Constant.success(this@RegisterActivity,"User Register Successfully")
                        startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponseModel>, t: Throwable) {
                Constant.error(this@RegisterActivity,"Error : ${t.message}")
            }

        })
    }

    private fun validation(userName: String, email: String, password: String, about: String): Boolean {
        if (userName.isEmpty()) {
            binding.userName.error = "Please enter your name"
            return false
        }

        if (email.isEmpty()) {
            binding.email.error = "Please enter your email"
            return false
        }

        if (password.isEmpty()) {
            binding.password.error = "Please enter your password"
            return false
        }

        if (about.isEmpty()) {
            binding.about.error = "Please enter your about"
            return false
        }

        return true
    }
}
