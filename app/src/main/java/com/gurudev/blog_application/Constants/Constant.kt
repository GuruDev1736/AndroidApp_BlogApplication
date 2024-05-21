package com.gurudev.blog_application.Constants

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

 class Constant {

     companion object{
         fun success(context : Context, message : String) : Toasty?
         {
             Toasty.success(context, message, Toast.LENGTH_LONG, true).show()
             return null
         }


         fun error(context : Context, message : String) : Toasty?
         {
             Toasty.error(context, message, Toast.LENGTH_LONG, true).show()
             return null
         }

     }
}