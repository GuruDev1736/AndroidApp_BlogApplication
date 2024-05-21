package com.gurudev.blog_application.Activities.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.gurudev.blog_application.Constants.Constant
import com.gurudev.blog_application.R
import com.gurudev.blog_application.RequestModel.createPostRequestModel
import com.gurudev.blog_application.ResponseModel.CategoryResponseModelItem
import com.gurudev.blog_application.ResponseModel.PostResponseModel
import com.gurudev.blog_application.Retrofit.ApiUtilities
import com.gurudev.blog_application.databinding.FragmentCreatePostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class CreatePostFragment : Fragment() {

    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var sharedPreferences: SharedPreferences
    private  var categortName   = mutableListOf<String> ()
    private  var categortId = mutableListOf<Int>()
    private lateinit var adapter: ArrayAdapter<String>
    private  var selectedId : Int? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePostBinding.inflate(layoutInflater)

        binding.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        adapter = ArrayAdapter(requireContext(),R.layout.spinner_item_layout,categortName)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categories.adapter = adapter

        binding.categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position>=0)
                {
                 selectedId = categortId[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Constant.error(requireContext(),"Nothing Selected")
            }
        }


        binding.post.setOnClickListener{

            val title = binding.topic.text.toString()
            val content = binding.content.text.toString()
            val categoryId = selectedId!!.toInt()
            val token = getToken()
            val userId = getUserId()

            Log.d("TOKEN",token)
            Log.d("Category",categoryId.toString())
            Log.d("UserID",userId.toString())

            if (ValidateInput(title,content,token)){
                CallPostApi(title,content,token,categoryId,userId)
            }
        }
        return binding.root
    }

    private fun CallPostApi(title: String, content: String, token: String, categoryId: Int, userId: Int) {

        val model = createPostRequestModel(content,title)
        ApiUtilities.getInstance().createPost("Bearer $token",userId,categoryId,model)
            .enqueue(object : Callback<PostResponseModel>{
                override fun onResponse(
                    call: Call<PostResponseModel>,
                    response: Response<PostResponseModel>
                ) {
                    Log.d("RESPONSE",response.toString())
                    if (response.isSuccessful)
                    {
                        val data = response.body()
                        if (data!=null)
                        {
                            Constant.success(requireContext(),"Post created Successfully")
                            findNavController().popBackStack()
                        }
                    }
                    else
                    {
                        Constant.error(requireContext(),"Failed to create the post : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PostResponseModel>, t: Throwable) {
                    Constant.error(requireContext(),"Failed to create the post : ${t.message}")
                }

            })

    }


    private fun ValidateInput(title: String, content: String , token : String): Boolean {

        if (title.isEmpty())
        {
            Constant.error(requireContext(),"Please Enter the Title")
            return false
        }

        if (content.isEmpty())
        {
            Constant.error(requireContext(),"Please Enter the Content")
            return false
        }

        if (token == "")
        {
            Constant.error(requireContext(),"Token is Null")
            return false
        }

        return true

    }

    override fun onStart() {
        super.onStart()

     val token  = getToken()
        ApiUtilities.getInstance().getCategory(token)
            .enqueue(object : Callback<List<CategoryResponseModelItem>>{
                override fun onResponse(
                    call: Call<List<CategoryResponseModelItem>>,
                    response: Response<List<CategoryResponseModelItem>>
                ) {
                    if (response.isSuccessful)
                    {
                        val data = response.body()
                        if (data!=null)
                        {
                            categortName.clear()
                            categortId.clear()

                            for (item in data){
                                item.categoryTitle.let { categortName.add(it) }
                                item.categoryId.let { categortId.add(it) }
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<List<CategoryResponseModelItem>>, t: Throwable) {
                    Constant.error(requireContext(),"Error : ${t.message}")
                }

            })
    }

    private fun getToken(): String {
        sharedPreferences = requireContext().getSharedPreferences("JWT",Context.MODE_PRIVATE)
        return sharedPreferences.getString("jwtToken","")!!
    }

    private fun getUserId() : Int{
        sharedPreferences = requireContext().getSharedPreferences("JWT",Context.MODE_PRIVATE)
        return sharedPreferences.getInt("UserId",-1)
    }

}