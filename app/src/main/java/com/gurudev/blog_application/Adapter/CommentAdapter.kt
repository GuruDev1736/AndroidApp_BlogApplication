package com.gurudev.blog_application.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.gurudev.blog_application.Activities.ui.home.CommentFragment
import com.gurudev.blog_application.ResponseModel.Comment
import com.gurudev.blog_application.ResponseModel.Content
import com.gurudev.blog_application.databinding.CommentListLayoutBinding
import com.gurudev.blog_application.databinding.PostLayoutBinding
import java.util.Locale


class CommentAdapter(private val context : Context, private val dataList : List<Comment>) : RecyclerView.Adapter<CommentAdapter.onViewHolder>(){

    class onViewHolder(val binding : CommentListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onViewHolder {
        val binding = CommentListLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return onViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: onViewHolder, position: Int) {

        val currentItem = dataList[position]

        holder.binding.apply {
            content.text = currentItem.content
        }

    }

}