package com.android.firebasetask.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.firebasetask.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mConstraintLayoutLogin: ConstraintLayout
    private lateinit var mTextViewLoginTitle: TextView
    private lateinit var mTextInputLayoutLoginEmail: TextInputLayout
    private lateinit var mTextInputEditTextLoginEmail: TextInputEditText
    private lateinit var mTextInputLayoutLoginPassword: TextInputLayout
    private lateinit var mTextInputEditTextLoginPassword: TextInputEditText
    private lateinit var mTextViewLoginForgotPassword : TextView
    private lateinit var mButtonLogin: Button
    private lateinit var mTextViewDidntRegisterYetRegisterMe: TextView

    private lateinit var mEmail : String
    private lateinit var mPassword : String

    private var mAuth: FirebaseAuth? = null
    private var mProgressBar: ProgressBar? = null

    public  var TAG: String = "111"

    private lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mConstraintLayoutLogin = findViewById(R.id.constraintLayoutLogin)
        mTextViewLoginTitle = findViewById(R.id.textViewLoginTitle)
        mTextInputLayoutLoginEmail = findViewById(R.id.textInputLayoutLoginEmail)
        mTextInputEditTextLoginEmail = findViewById(R.id.textInputEditTextLoginEmail)
        mTextInputLayoutLoginPassword = findViewById(R.id.textInputLayoutLoginPassword)
        mTextInputEditTextLoginPassword = findViewById(R.id.textInputEditTextLoginPassword)
        mTextViewLoginForgotPassword = findViewById(R.id.textViewLoginForgotPassword)
        mButtonLogin = findViewById(R.id.buttonLogin)
        mTextViewDidntRegisterYetRegisterMe = findViewById(R.id.textViewDidntRegisterYetRegisterMe)

        mAuth = FirebaseAuth.getInstance();

        if (mAuth?.currentUser != null) {
            startActivity(Intent(this@LoginActivity, SuccessActivity::class.java))
            finish()
        }

        mTextViewLoginForgotPassword.setOnClickListener(this)
        mButtonLogin.setOnClickListener(this)
        mTextViewDidntRegisterYetRegisterMe.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.textViewLoginForgotPassword ->
            {
                val mIntent: Intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
                startActivity(mIntent)
            }
            R.id.buttonLogin -> {
                mEmail = mTextInputEditTextLoginEmail.text.toString()
                mPassword = mTextInputEditTextLoginPassword.text.toString()
                isLoginValidated()
            }
            R.id.textViewDidntRegisterYetRegisterMe -> {
                val mIntent: Intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(mIntent)
            }
        }
    }

    fun isLoginValidated() {
        if (mEmail.isEmpty()) {
            mTextInputLayoutLoginEmail.isErrorEnabled = true
            mTextInputLayoutLoginEmail.error = "Can't Empty"
        } else if (mPassword.isEmpty()) {
            mTextInputLayoutLoginPassword.isErrorEnabled = true
            mTextInputLayoutLoginPassword.error = "Can't Empty"
        } else {

            mAuth!!.signInWithEmailAndPassword(
                mEmail,
                mPassword
            )
                .addOnCompleteListener(this,
                    OnCompleteListener<AuthResult?> { task ->
                        if (!task.isSuccessful) {
                            // there was an error
                            if (mPassword.length < 6) {
                                mTextInputLayoutLoginPassword.isErrorEnabled = true
                                mTextInputLayoutLoginPassword.error = getString(R.string.minimum_password)
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    getString(R.string.auth_failed),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            val intent = Intent(this@LoginActivity, SuccessActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })


        }
    }
}