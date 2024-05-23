package com.gurudev.blog_application.Activities.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gurudev.blog_application.Constants.Constant
import com.gurudev.blog_application.R
import com.gurudev.blog_application.RequestModel.updateUserRequestModel
import com.gurudev.blog_application.ResponseModel.ProfileResponse
import com.gurudev.blog_application.Retrofit.ApiUtilities
import com.gurudev.blog_application.databinding.ActivityEditProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.back.setOnClickListener {
            this@EditProfileActivity.onBackPressedDispatcher.onBackPressed()
        }

        val user = getUserId()
        ApiUtilities.getInstance().getUserById(user)
            .enqueue(object : Callback<ProfileResponse>{
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if(response.isSuccessful)
                    {
                        val data = response.body()
                        if (data!=null)
                        {
                            binding.userName.setText(data.name)
                            binding.email.setText(data.email)
                            binding.about.setText(data.about)
                        }
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Constant.error(this@EditProfileActivity,"Error : ${t.message}")
                }

            })


        binding.update.setOnClickListener{
            val name = binding.userName.text.toString()
            val email = binding.email.text.toString()
            val about = binding.about.text.toString()
            val token = getToken()
            val userid = getUserId()
            val model = updateUserRequestModel(about,email,name)

            if (validateInput(name,email,about))
            {
                ApiUtilities.getInstance().updateUser("Bearer $token",userid,model)
                    .enqueue(object : Callback<ProfileResponse>{
                        override fun onResponse(
                            call: Call<ProfileResponse>,
                            response: Response<ProfileResponse>
                        ) {
                            if (response.isSuccessful)
                            {
                                val data = response.body()
                                if (data!= null){
                                    Constant.success(this@EditProfileActivity,"Profile Updated")
                                }
                            }
                            else
                            {
                                Constant.error(this@EditProfileActivity,"Failed to Update the profile")
                            }
                        }

                        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                            Constant.error(this@EditProfileActivity,"Error : ${t.message}")
                        }

                    })
            }
        }


    }

    private fun validateInput(name: String, email: String, about: String): Boolean {

        if (name.isEmpty())
        {
            binding.userName.error = "Name Required"
            return false
        }

        if (email.isEmpty())
        {
            binding.email.error = "Email Required"
            return false
        }

        if (about.isEmpty())
        {
            binding.about.error = "About Required"
            return false
        }

        return true

    }

    private fun getToken(): String {
        sharedPreferences = getSharedPreferences("JWT",Context.MODE_PRIVATE)
        return sharedPreferences.getString("jwtToken","")!!
    }
    private fun getUserId() : Int{
        sharedPreferences = getSharedPreferences("JWT", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("UserId",-1)
    }
}