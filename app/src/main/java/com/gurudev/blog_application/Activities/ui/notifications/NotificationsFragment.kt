package com.gurudev.blog_application.Activities.ui.notifications

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gurudev.blog_application.Activities.Auth.LoginActivity
import com.gurudev.blog_application.Constants.Constant
import com.gurudev.blog_application.ResponseModel.ProfileResponse
import com.gurudev.blog_application.Retrofit.ApiUtilities
import com.gurudev.blog_application.databinding.FragmentNotificationsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    private lateinit var sharedPreferences: SharedPreferences
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userId = getUserId()
        ApiUtilities.getInstance().getUserById(userId)
            .enqueue(object : Callback<ProfileResponse>{
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    if (response.isSuccessful)
                    {
                        val data = response.body()
                        if (data!=null)
                        {
                            binding.name.text = data.name
                            binding.email.text = data.email
                        }
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Constant.error(requireContext(),"Error : ${t.message}")
                }

            })

        binding.editProfile.setOnClickListener{
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.changePassword.setOnClickListener{
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }

        binding.logout.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }

        return root
    }

    private fun getUserId() : Int{
        sharedPreferences = requireContext().getSharedPreferences("JWT", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("UserId",-1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}