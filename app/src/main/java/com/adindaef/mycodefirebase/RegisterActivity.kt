package com.adindaef.mycodefirebase

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            val nama = RegisNama.text.toString()
            val email = RegisEmail.text.toString()
            val password = RegisPassword.text.toString()
            if (nama.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please Insert", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val progressDialog = ProgressDialog(this, R.style.Theme_MaterialComponents_Light_Dialog)
            progressDialog.isIndeterminate = true
            progressDialog.setMessage("Creating User...")
            progressDialog.show()

            //mDatabase = FirebaseDatabase.getInstance()
            //   mDatabaseReference = mDatabase!!.reference!!.child("Users")
            //   mAuth = FirebaseAuth.getInstance()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    progressDialog.hide()

                    if (it.isSuccessful){

                        val userId = FirebaseAuth.getInstance().currentUser!!.uid
                        //Verify Email
                        verifyEmail()
                        //update user profile information
                        val currentUserDb =  FirebaseDatabase.getInstance().reference.child("Users").child(userId)
                        currentUserDb.child("Name").setValue(nama)

                        updateUserInfoAndUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }

                }
    }
    }

    private fun verifyEmail() {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this@RegisterActivity,
                        "Verification email sent to " + mUser.email,
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ERROR EMAIL", "sendEmailVerification", it.exception)
                    Toast.makeText(this@RegisterActivity,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun updateUserInfoAndUI() {
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }
}
