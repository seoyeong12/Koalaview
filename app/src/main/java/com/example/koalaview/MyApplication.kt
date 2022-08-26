package com.example.koalaview

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.example.koalaview.MyApplication.Companion.auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MyApplication : MultiDexApplication() {
    companion object{
        lateinit var auth: FirebaseAuth
        var email:String?=null

        fun checkAuth(): Boolean{
            var currentUser = auth.currentUser
            return currentUser?.let{
                email = currentUser.email
                currentUser.isEmailVerified
            }?: let{
                false
            }
        }
        fun getUid():String{
            auth=FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }
        fun getTime():String{
            val currentDateTime= Calendar.getInstance().time
            val dateFormat= SimpleDateFormat("yyyy.MM.dd HH:mm:ss",Locale.KOREA).format(currentDateTime)
            return dateFormat
        }
    }
    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth

    }
}