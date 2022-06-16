package com.answere2022.mbtitalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        initLoginButton()
        initSignUpButton()
        initEmaillAndPasswordEditText()


    }

    private fun initLoginButton() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {


            //로그인버튼이랑 사인업버튼에 둘다 쓸거기떄문에 이렇게 처리
            val email = getInputEmail()
            val paassword = getInputPassword()


            //파이어베이스에 아이디 및 패스워드 보내기
            auth.signInWithEmailAndPassword(email, paassword)
                .addOnCompleteListener(this) { task ->
                    //로그인 성공시 액티비티 종료
                    if (task.isSuccessful) {
                        finish()
                    }
                    //로그인 실패시 해당 메세지 보여주기
                    else {
                        Toast.makeText(this, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun initSignUpButton() {
        val singupButton = findViewById<Button>(R.id.signupButton)
        singupButton.setOnClickListener {

            val email = getInputEmail()
            val password = getInputPassword()
            //회원가입
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // 회원가입 성공시
                    if (task.isSuccessful) {
                        Toast.makeText(this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()

                    }
                    //회원가입 실패시
                    else {
                        Toast.makeText(this, "이미 가입한 이메일이거나, 회원가입이 실패하였습니다", Toast.LENGTH_SHORT).show()

                    }

                }


        }
    }


    private fun initEmaillAndPasswordEditText() {
        val idEditText = findViewById<EditText>(R.id.idEditText)
        val pwEditText = findViewById<EditText>(R.id.pwEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signupButton)



        idEditText.addTextChangedListener {
            val enable = idEditText.text.isNotEmpty() && pwEditText.text.isNotEmpty()
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable

        }

        pwEditText.addTextChangedListener {
            val enable = idEditText.text.isNotEmpty() && pwEditText.text.isNotEmpty()
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable

        }



        //아이디 및 비번 둘다 비어 있으면 로그인과 회원가입 비활성화
    }


            private fun getInputEmail(): String {
        return findViewById<EditText>(R.id.idEditText).text.toString()

    }

    private fun getInputPassword(): String {
        return findViewById<EditText>(R.id.pwEditText).text.toString()
    }




}