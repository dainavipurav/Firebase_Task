package com.android.firebasetask.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.firebasetask.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mConstraintLayoutRegister: ConstraintLayout
    private lateinit var mTextViewRegisterTitle: TextView
    private lateinit var mTextInputLayoutRegisterEmail: TextInputLayout
    private lateinit var mTextInputEditTextRegisterEmail: TextInputEditText
    private lateinit var mTextInputLayoutRegisterPassword: TextInputLayout
    private lateinit var mTextInputEditTextRegisterPassword: TextInputEditText
    private lateinit var mButtonRegister: Button
    private lateinit var mTextViewAlreadyRegisteredLoginMe: TextView

    private lateinit var mContext: Context

    private lateinit var mEmail : String
    private lateinit var mPassword : String

    public  var TAG: String = "111"

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mConstraintLayoutRegister = findViewById(R.id.constraintLayoutRegister)
        mTextViewRegisterTitle = findViewById(R.id.textViewRegisterTitle)
        mTextInputLayoutRegisterEmail = findViewById(R.id.textInputLayoutRegisterEmail)
        mTextInputEditTextRegisterEmail = findViewById(R.id.textInputEditTextRegisterEmail)
        mTextInputLayoutRegisterPassword = findViewById(R.id.textInputLayoutRegisterPassword)
        mTextInputEditTextRegisterPassword = findViewById(R.id.textInputEditTextRegisterPassword)
        mButtonRegister = findViewById(R.id.buttonRegister)
        mTextViewAlreadyRegisteredLoginMe = findViewById(R.id.textViewAlreadyRegisteredLoginMe)

        mButtonRegister.setOnClickListener(this)
        mTextViewAlreadyRegisteredLoginMe.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance();
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.textViewAlreadyRegisteredLoginMe -> {
                val mIntent: Intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(mIntent)
                finish()
            }
            R.id.buttonRegister -> {
                mEmail = mTextInputEditTextRegisterEmail.text.toString()
                mPassword = mTextInputEditTextRegisterPassword.text.toString()
                isRegisterValidated()
            }
        }
    }

    fun isRegisterValidated() {
        if (mEmail.isEmpty()) {
            mTextInputLayoutRegisterEmail.isErrorEnabled = true
            mTextInputLayoutRegisterEmail.error = "Can't Empty"
        } else if (mPassword.isEmpty()!!) {
            mTextInputLayoutRegisterPassword.isErrorEnabled = true
            mTextInputLayoutRegisterPassword.error = "Can't Empty"
        } else {
            mAuth!!.createUserWithEmailAndPassword(
                mEmail,
                mPassword
            ).addOnCompleteListener(
                    this
                ) { task ->
                    if (!task.isSuccessful) {

                        if (mPassword.length < 6) {
                            mTextInputLayoutRegisterPassword.isErrorEnabled = true
                            mTextInputLayoutRegisterPassword.error = getString(R.string.minimum_password)
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                getString(R.string.auth_failed),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        val intent = Intent(this@RegisterActivity, SuccessActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

        }
    }
}