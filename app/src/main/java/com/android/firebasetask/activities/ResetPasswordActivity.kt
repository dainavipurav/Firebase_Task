package com.android.firebasetask.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.firebasetask.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var mConstraintLayoutResetPassword : ConstraintLayout
    private lateinit var mTextInputLayoutResetPasswordEmail : TextInputLayout
    private lateinit var mTextInputEditTextResetPasswrodEmail : TextInputEditText
    private lateinit var mButtonResetPassword : Button

    private lateinit var mEmail : String

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        mAuth = FirebaseAuth.getInstance();

        mConstraintLayoutResetPassword = findViewById(R.id.constraintLayoutResetPassword)
        mTextInputLayoutResetPasswordEmail = findViewById(R.id.textInputLayoutResetPasswordEmail)
        mTextInputEditTextResetPasswrodEmail = findViewById(R.id.textInputEditTextResetPasswrodEmail)
        mButtonResetPassword = findViewById(R.id.buttonResetPassword)

        mButtonResetPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonResetPassword -> {
                mEmail = mTextInputEditTextResetPasswrodEmail.text.toString()
                isResetPasswordEmailValidated()
            }
        }
    }

    fun isResetPasswordEmailValidated(){
        if (mEmail.isEmpty()){
            mTextInputLayoutResetPasswordEmail.isErrorEnabled = true
            mTextInputLayoutResetPasswordEmail.error = "Can't Empty"
        }
        else{
            mAuth?.sendPasswordResetEmail(mEmail)?.addOnCompleteListener { task->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Reset password email is sent!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Failed to send reset email!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}