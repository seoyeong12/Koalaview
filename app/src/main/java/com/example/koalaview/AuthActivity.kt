package com.example.koalaview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.koalaview.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeVisivility(intent.getStringExtra("data").toString())
        binding.goSignInBtn.setOnClickListener {
            changeVisivility("signin")
        }
        binding.signBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if (task.isSuccessful) {
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask ->
                                if (sendTask.isSuccessful) {
                                    Toast.makeText(
                                        baseContext,
                                        "회원가입 성공!!.. 메일을 확인해주세요",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    changeVisivility("logout")
                                } else {
                                    Toast.makeText(baseContext, "메일발송 실패", Toast.LENGTH_SHORT)
                                        .show()
                                    changeVisivility("logout")
                                }
                            }
                    } else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        changeVisivility("logout")
                    }
                }
        }
        binding.loginBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if (task.isSuccessful) {
                        if (MyApplication.checkAuth()) {
                            MyApplication.email = email
                            finish()
                        } else {
                            Toast.makeText(baseContext, "이메일 인증이 되지않았습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email = null
            changeVisivility("logout")
            finish()
        }
}
    fun changeVisivility(mode:String){
    if(mode.equals("login")){
        binding.run{
            authMainTextView.text=""
            authMainTextView.visibility= View.VISIBLE
            authbg.background.setVisible(false,false)
            logoutBtn.visibility= View.VISIBLE
            goSignInBtn.visibility= View.GONE
            idtv.visibility=View.GONE
            authEmailEditView.visibility= View.GONE
            pwtv.visibility=View.GONE
            authPasswordEditView.visibility= View.GONE
            signBtn.visibility= View.GONE
            loginBtn.visibility= View.GONE
        }
    }
    else if(mode.equals("logout")){
        binding.run{
            authMainTextView.visibility= View.GONE
            logoutBtn.visibility= View.GONE
            goSignInBtn.visibility= View.VISIBLE
            idtv.visibility=View.VISIBLE
            authEmailEditView.visibility= View.VISIBLE
            pwtv.visibility=View.VISIBLE
            authPasswordEditView.visibility= View.VISIBLE
            signBtn.visibility= View.GONE
            loginBtn.visibility= View.VISIBLE
        }
    }
    else if(mode.equals("signin")){
        binding.run{
            authMainTextView.visibility= View.GONE
            logoutBtn.visibility= View.GONE
            goSignInBtn.visibility= View.GONE
            idtv.visibility=View.VISIBLE
            authEmailEditView.visibility= View.VISIBLE
            pwtv.visibility=View.GONE
            authPasswordEditView.visibility= View.VISIBLE
            signBtn.visibility= View.VISIBLE
            loginBtn.visibility= View.GONE
        }
    }
    }
    }