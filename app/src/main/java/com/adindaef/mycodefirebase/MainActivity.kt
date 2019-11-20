package com.adindaef.mycodefirebase

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefManager = SharedPrefManager()
        SharedPrefManager.sp = getSharedPreferences(SharedPrefManager.SP_MAHASISWA_APP, Context.MODE_PRIVATE)
        SharedPrefManager.spEditor = SharedPrefManager.sp.edit()

        if (sharedPrefManager.getSPSudahLogin()!!) {
            startActivity(
                Intent(this@MainActivity, Dashboard::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }

        textforgot.setOnClickListener {
            val intent = Intent (this, ForgotActivity::class.java)
            startActivity(intent)
        }

        textRegis.setOnClickListener {
            val intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please Insert Email and Password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val progressDialog = ProgressDialog(this, R.style.Theme_MaterialComponents_Light_Dialog)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Login...")
            progressDialog.show()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {

                    progressDialog.hide()
                    inputEmail.setText("")
                    inputEmail.requestFocus()
                    inputPassword.setText("")

                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true)
                    if (!it.isSuccessful) {

                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this, "Succesfully Login", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Dashboard::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener{
                    Log.d("Main", "Failed Login: ${it.message}")
                    Toast.makeText(this, "Email/Password incorrect", Toast.LENGTH_SHORT).show()


                }
        }
    }
}
