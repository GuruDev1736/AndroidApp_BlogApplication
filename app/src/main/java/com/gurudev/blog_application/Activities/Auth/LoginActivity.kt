package com.gurudev.blog_application.Activities.Auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gurudev.blog_application.Activities.HomeActivity
import com.gurudev.blog_application.Activities.MainActivity
import com.gurudev.blog_application.Constants.Constant
import com.gurudev.blog_application.R
import com.gurudev.blog_application.RequestModel.LoginRequestModel
import com.gurudev.blog_application.ResponseModel.LoginResponseModel
import com.gurudev.blog_application.Retrofit.ApiUtilities
import com.gurudev.blog_application.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var JWTsharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
        JWTsharedPreferences = getSharedPreferences("JWT", Context.MODE_PRIVATE)


        binding.signUp.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        binding.userName.setText(sharedPreferences.getString("EMAIL",null))
        binding.password.setText(sharedPreferences.getString("PASSWORD",null))

        if (sharedPreferences.getBoolean("REMEMBER_ME",false))
        {
            binding.rememberMe.isChecked = true
        }
        else
        {
            binding.rememberMe.isChecked = false
        }


        binding.signIn.setOnClickListener{
            val userName = binding.userName.text.toString()
            val password = binding.password.text.toString()

            if (validation(userName,password))
            {
                val model = LoginRequestModel(userName,password)

                if (binding.rememberMe.isChecked)
                {
                    savePassword(userName, password , true)
                }

                CallApi(model)
            }
        }
    }

    private fun savePassword(userName: String, password: String , rememberMe : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putString("EMAIL", userName)
        editor.putString("PASSWORD", password)
        editor.putBoolean("REMEMBER_ME", rememberMe)
        editor.apply()
    }



    private fun CallApi(model: LoginRequestModel) {

        ApiUtilities.getInstance().login(model)
            .enqueue(object : Callback<LoginResponseModel>{
                override fun onResponse(
                    call: Call<LoginResponseModel>,
                    response: Response<LoginResponseModel>
                ) {
                    if (response.isSuccessful)
                    {
                        val data = response.body()
                        if (data!=null)
                        {
                            Constant.success(this@LoginActivity,"Login Successful")
                            JWTsharedPreferences.edit().putString("jwtToken",data.token).apply()
                            JWTsharedPreferences.edit().putInt("UserId",data.userId).apply()
                            JWTsharedPreferences.edit().putString("UserRole",data.userRole).apply()
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        }
                        else
                        {
                            Constant.error(this@LoginActivity,"Data is Null")
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    Constant.error(this@LoginActivity, "Error : ${t.message}")
                }

            })

    }

    private fun validation(userName: String , password: String): Boolean {
        if (userName.isEmpty()) {
            binding.userName.error = "Please enter your name"
            return false
        }

        if (password.isEmpty()) {
            binding.password.error = "Please enter your password"
            return false
        }

        return true
    }
}