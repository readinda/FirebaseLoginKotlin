package com.adindaef.mycodefirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val mDatabase = FirebaseDatabase.getInstance()
        val mDatabaseReference = mDatabase.reference.child("Users")
        val mAuth = FirebaseAuth.getInstance()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference.child(mUser!!.uid)
        tvEmail!!.text = mUser.email
        tvEmailVerifiied!!.text = mUser.isEmailVerified.toString()
        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvName!!.text = snapshot.child("Name").value as String
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

        btnLogout.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(this, "Sign Out" , Toast.LENGTH_SHORT).show()

            finish()
        }

    }
}
