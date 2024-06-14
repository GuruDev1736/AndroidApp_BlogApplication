package com.gurudev.blog_application.Activities.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gurudev.blog_application.Adapter.CommentAdapter
import com.gurudev.blog_application.Constants.Constant
import com.gurudev.blog_application.Constants.WrapContentLinearLayoutManager
import com.gurudev.blog_application.R
import com.gurudev.blog_application.RequestModel.CommentRequestModel
import com.gurudev.blog_application.ResponseModel.Comment
import com.gurudev.blog_application.ResponseModel.CommentResponseModel
import com.gurudev.blog_application.ResponseModel.PostResponseModel
import com.gurudev.blog_application.ResponseModel.getPostByIdResponseModel
import com.gurudev.blog_application.Retrofit.ApiUtilities
import com.gurudev.blog_application.databinding.CommentLayoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_POST_ID = "post_id"

        fun newInstance(postId: Int): CommentFragment {
            val fragment = CommentFragment()
            val args = Bundle()
            args.putInt(ARG_POST_ID, postId)
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var binding : CommentLayoutBinding
    private lateinit var adapter : CommentAdapter
    private lateinit var sharedPreferences : SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CommentLayoutBinding.inflate(layoutInflater)

        binding.recview.layoutManager = WrapContentLinearLayoutManager(context)
        val postId = requireArguments().getInt(ARG_POST_ID)

        ApiUtilities.getInstance().getPostsUsingId(postId).enqueue(object :Callback<getPostByIdResponseModel>{
            override fun onResponse(
                call: Call<getPostByIdResponseModel>,
                response: Response<getPostByIdResponseModel>
            ) {
                if (response.isSuccessful)
                {
                    val data = response.body()
                    if (data!=null)
                    {
                        adapter =  CommentAdapter(context!!,data.comments)
                        binding.recview.adapter = adapter
                    }
                }
                else
                {
                    Constant.error(context!!,"Failed to get comments")
                }
            }

            override fun onFailure(call: Call<getPostByIdResponseModel>, t: Throwable) {
                Constant.error(context!!,"Error : ${t.message}")
            }
        })


        binding.send.setOnClickListener{
            val comment = binding.comment.text.toString()
            val token = getToken()
            val userId = getUserId()
            if (comment.isEmpty())
            {
                binding.comment.error = "Please Enter the comment"
                return@setOnClickListener
            }

            val model = CommentRequestModel(comment)
            ApiUtilities.getInstance().postComment("Bearer $token",userId,postId,model)
                .enqueue(object : Callback<CommentResponseModel> {
                    override fun onResponse(
                        call: Call<CommentResponseModel>,
                        response: Response<CommentResponseModel>
                    ) {
                        if (response.isSuccessful)
                        {
                            val data = response.body()
                            if (data!=null)
                            {
                                Constant.success(context!!,"Comment Posted")
                                dismiss()
                            }
                        }
                        else
                        {
                            Constant.error(context!!,"Failed to post comment")
                        }
                    }

                    override fun onFailure(call: Call<CommentResponseModel>, t: Throwable) {
                        Constant.error(context!!,"Error : ${t.message}")
                    }

                })
        }


        return binding.root
    }

    private fun getToken(): String {
        sharedPreferences = requireContext().getSharedPreferences("JWT",Context.MODE_PRIVATE)
        return sharedPreferences.getString("jwtToken","")!!
    }

    private fun getUserId() : Int{
        sharedPreferences = requireContext().getSharedPreferences("JWT", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("UserId",-1)
    }
}