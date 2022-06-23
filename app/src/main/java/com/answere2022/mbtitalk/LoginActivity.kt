package com.answere2022.mbtitalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.facebook.CallbackManager
import com.facebook.FacebookButtonBase
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth
private lateinit var callbackManager: CallbackManager

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()

        initLoginButton()
        initSignUpButton()
        initEmaillAndPasswordEditText()
        initFacebookLoginButton()

    }

    private fun initLoginButton() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {


            //로그인버튼이랑 사인업버튼에 둘다 쓸거기떄문에 이렇게 처리
            val email = getInputEmail()
            val password = getInputPassword()


            //파이어베이스에 아이디 및 패스워드 보내기
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    //로그인 성공시 액티비티 종료
                    if (task.isSuccessful) {
                        handleSuccessLogin()
                        Log.e("파이어베이스 업데이트 ", "로그인성공")
                    }
                    //로그인 실패시 해당 메세지 보여주기
                    else {
                        Toast.makeText(this, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT)
                            .show()

                        Log.e("파이어베이스 업데이트 ", "로그인 실패")
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


                        Log.e("회원가입", "회원가입성공")
                    }
                    //회원가입 실패시
                    else {
                        Toast.makeText(this, "이미 가입한 이메일이거나, 회원가입이 실패하였습니다", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("회원가입", "회원가입실패")

                    }

                }


        }
    }


    private fun initEmaillAndPasswordEditText() {
        val idEditText = findViewById<EditText>(R.id.idEditText)
        val pwEditText = findViewById<EditText>(R.id.pwEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signupButton)


        //아이디 및 비번 둘다 비어 있으면 로그인과 회원가입 비활성화

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


    }

    private fun initFacebookLoginButton() {
        val facebookLoginButton = findViewById<LoginButton>(R.id.facebookLoginButton)
        facebookLoginButton.setPermissions("email", "public_profile")
        facebookLoginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {


                override fun onSuccess(result: LoginResult) {
                    //로그인 성공시, Result뒤에 물음표는 로그인 성공시 null값이 안내려올 예정이기에 지워준다
                    Log.d("Facebook", "facebook:onSuccess")
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(this@LoginActivity) { task ->
                            if (task.isSuccessful) {
                                handleSuccessLogin()

                                Log.e("페이스북로그인", "성공")
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "페이스북 로그인이 실패했습니다.",
                                    Toast.LENGTH_SHORT
                                )

                                Log.e("페이스북로그인", "실패")
                            }
                        }


                    //페이스북에 토큰을 넘겨줘서 로그인 시켜주는 방식
                }

                override fun onCancel() {


                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(this@LoginActivity, "페이스북 로그인이 실패했습니다.", Toast.LENGTH_SHORT)
                }
            }


        )
    }


    private fun getInputEmail(): String {
        return findViewById<EditText>(R.id.idEditText).text.toString()

    }

    private fun getInputPassword(): String {
        return findViewById<EditText>(R.id.pwEditText).text.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }


    private fun handleSuccessLogin() {

        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
            return
        }


        //리얼타임베이스에 유저정보 저장하는 부분
        val userId = auth.currentUser?.uid.orEmpty()
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()
        user["userID"] = userId
        currentUserDB.updateChildren(user)

        finish()


    }

}