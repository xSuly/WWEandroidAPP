package com.example.wweshowdown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.wweshowdown.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this@SignUpActivity, RecyclerActivity::class.java))
            finish();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide();
        auth = FirebaseAuth.getInstance()
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)

        progressBar = findViewById(R.id.progressBar)

        var textView: TextView = findViewById(R.id.login)

        val regbtn: MaterialButton = findViewById(R.id.signupbtn)

        regbtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email: String = email.text.toString()
            val password: String = password.text.toString()

            if(TextUtils.isEmpty(email)){
                Toast.makeText(this@SignUpActivity, "Please fill in the email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this@SignUpActivity, "Please fill in the password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "Account created.",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this@SignUpActivity, "Registration failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        textView.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}