package com.gurudev.blog_application.Activities.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gurudev.blog_application.Constants.Constant
import com.gurudev.blog_application.R
import com.gurudev.blog_application.RequestModel.ChangePasswordRequestModel
import com.gurudev.blog_application.ResponseModel.DefaultResponseModel
import com.gurudev.blog_application.Retrofit.ApiUtilities
import com.gurudev.blog_application.databinding.ActivityChangePasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.back.setOnClickListener {
            this@ChangePasswordActivity.onBackPressedDispatcher.onBackPressed()
        }

        binding.update.setOnClickListener{
            val oldPassword = binding.currentPassword.text.toString()
            val newPassword = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()
            val token = getToken()
            val userId = getUserId()

            if (oldPassword.isEmpty())
            {
                Constant.error(this@ChangePasswordActivity,"Please enter your current password")
                return@setOnClickListener
            }
            if (newPassword.isEmpty() || confirmPassword.isEmpty()){
                Constant.error(this@ChangePasswordActivity,"Please enter your new password")
                return@setOnClickListener
            }
            if (newPassword != confirmPassword){
                Constant.error(this@ChangePasswordActivity,"Password does not match")
                return@setOnClickListener
            }

            val model = ChangePasswordRequestModel(confirmPassword , oldPassword)
            ApiUtilities.getInstance().updatePassword("Bearer $token",userId,model)
                .enqueue(object : Callback<DefaultResponseModel> {
                    override fun onResponse(
                        call: Call<DefaultResponseModel>,
                        response: Response<DefaultResponseModel>
                    ) {
                        if (response.isSuccessful)
                        {
                            val data = response.body()
                            if (data!=null)
                            {
                                Constant.success(this@ChangePasswordActivity,data.message)
                                finish()
                            }
                        }
                        else
                        {
                            Constant.error(this@ChangePasswordActivity,"Password Does Not Match")
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponseModel>, t: Throwable) {
                       Constant.error(this@ChangePasswordActivity,t.message.toString())
                    }

                })

        }

    }

    private fun getUserId() : Int{
        sharedPreferences = getSharedPreferences("JWT", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("UserId",-1)
    }

    private fun getToken(): String {
        sharedPreferences = getSharedPreferences("JWT",Context.MODE_PRIVATE)
        return sharedPreferences.getString("jwtToken","")!!
    }
}