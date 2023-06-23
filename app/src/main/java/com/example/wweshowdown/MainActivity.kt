package com.example.wweshowdown

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.wweshowdown.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    var callbackManager = CallbackManager.Factory.create()

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this@MainActivity, RecyclerActivity::class.java))
            finish();
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //printHashKey()
        supportActionBar?.hide();

        var email = this.findViewById<TextView>(R.id.email)
        var password = this.findViewById<TextView>(R.id.password)

        var loginbtn = this.findViewById<MaterialButton>(R.id.loginbtn)

        var signUpOpt = this.findViewById<TextView>(R.id.signup)

        progressBar = findViewById(R.id.progressBar)

        auth = FirebaseAuth.getInstance()

        val accessToken = AccessToken.getCurrentAccessToken()

        if(accessToken != null && !accessToken.isExpired){
            startActivity(Intent(this@MainActivity, RecyclerActivity::class.java))
            finish();
        }
        val fbBtn = findViewById<ImageView>(R.id.fbBtn)
        fbBtn.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"));
        }

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                startActivity(Intent(this@MainActivity, RecyclerActivity::class.java))
                finish();
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(error: FacebookException) {
                // App code
            }
        })

        // logica sign in firebase
        loginbtn.setOnClickListener {
            if(TextUtils.isEmpty(email.text.toString())){
                Toast.makeText(this@MainActivity, "Please fill in the email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(password.text.toString())){
                Toast.makeText(this@MainActivity, "Please fill in the password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Authentication successful.",
                            Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, RecyclerActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }

        signUpOpt.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}