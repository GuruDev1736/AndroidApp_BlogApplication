package com.gurudev.blog_application.Activities.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gurudev.blog_application.Adapter.PostAdapter
import com.gurudev.blog_application.Constants.Constant
import com.gurudev.blog_application.Constants.WrapContentLinearLayoutManager
import com.gurudev.blog_application.R
import com.gurudev.blog_application.ResponseModel.PostResponseModel
import com.gurudev.blog_application.Retrofit.ApiUtilities
import com.gurudev.blog_application.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter : PostAdapter


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.create.setOnClickListener{
            findNavController().navigate(R.id.create_post)
        }
        binding.recView.layoutManager = WrapContentLinearLayoutManager(requireContext())

        ApiUtilities.getInstance().getPosts().enqueue(object : Callback<PostResponseModel>{
            override fun onResponse(call: Call<PostResponseModel>, response: Response<PostResponseModel>) {
                if (response.isSuccessful)
                {
                    val data = response.body()
                    if (data!=null)
                    {
                        adapter = PostAdapter(context!!,data.content)
                        binding.recView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<PostResponseModel>, t: Throwable) {
                Constant.error(requireContext(),"Failed to get the post  : ${t.message}")
            }

        })


        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (::adapter.isInitialized) {
                    adapter.filter(newText.toString())
                }
                return true
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}