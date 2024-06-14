package com.gurudev.blog_application.Activities.ui.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gurudev.blog_application.Adapter.PostAdapter
import com.gurudev.blog_application.Constants.Constant
import com.gurudev.blog_application.Constants.WrapContentLinearLayoutManager
import com.gurudev.blog_application.ResponseModel.PostResponseModel
import com.gurudev.blog_application.Retrofit.ApiUtilities
import com.gurudev.blog_application.databinding.FragmentDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private lateinit var sharedPreferences : SharedPreferences

    private lateinit var adapter : PostAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recView.layoutManager = WrapContentLinearLayoutManager(requireContext())

        val userId = getUserId()

        ApiUtilities.getInstance().getPostsById(userId)
            .enqueue(object : Callback<PostResponseModel>{
                override fun onResponse(
                    call: Call<PostResponseModel>,
                    response: Response<PostResponseModel>
                ) {
                    if (response.isSuccessful){
                        val data = response.body()

                        if (data!=null)
                        {
                            adapter = PostAdapter(context!!,data.content)
                            binding.recView.adapter = adapter
                        }
                    }
                    else
                    {
                        Constant.error(requireContext(),"Failed to get the post Data")
                    }
                }

                override fun onFailure(call: Call<PostResponseModel>, t: Throwable) {
                    Constant.error(requireContext(),"Error : ${t.message}")
                }

            })

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