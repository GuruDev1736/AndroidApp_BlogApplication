package com.gurudev.blog_application.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gurudev.blog_application.Activities.Auth.LoginActivity
import com.gurudev.blog_application.Activities.Auth.RegisterActivity
import com.gurudev.blog_application.R
import com.gurudev.blog_application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signIn.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        binding.signUp.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}