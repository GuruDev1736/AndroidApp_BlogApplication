package com.gurudev.blog_application.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gurudev.blog_application.ResponseModel.Content
import com.gurudev.blog_application.ResponseModel.PostListModel
import com.gurudev.blog_application.databinding.PostLayoutBinding
import java.util.Locale

class PostAdapter(private val dataList : List<Content>) : RecyclerView.Adapter<PostAdapter.onViewHolder>(){

    private var filteredList: List<Content> = dataList
    class onViewHolder(val binding : PostLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onViewHolder {
        val binding = PostLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return onViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: onViewHolder, position: Int) {

        val currentItem = filteredList[position]

        holder.binding.apply {
            title.text = currentItem.title
            content.text = getTruncatedContent(currentItem.content)
            username.text = currentItem.user.name
        }

    }

    fun getTruncatedContent(content : String): String {
        return if (content.length > 40) {
            content.substring(0, 40) + "..."
        } else {
            content
        }
    }

    fun filter(text: String) {
        val searchText = text.lowercase(Locale.getDefault())
        filteredList = if (searchText.isEmpty()) {
            dataList // If the search text is empty, return the original data list
        } else {
            dataList.filter{ item ->
                item.title.lowercase(Locale.getDefault()).contains(searchText) ||
                        item.content.lowercase(Locale.getDefault()).contains(searchText) ||
                        item.user.name.lowercase(Locale.getDefault()).contains(searchText)
            }
        }
        notifyDataSetChanged()
    }

}