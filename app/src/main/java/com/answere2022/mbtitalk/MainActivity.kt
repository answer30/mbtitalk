package com.answere2022.mbtitalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





    }

    override fun onStart() {
        super.onStart()

        //로그인이 안되있을시 로그인 액티비티로 이동
        if(auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }else{
            startActivity(Intent(this, LikeActivity::class.java))
        }
    }

}