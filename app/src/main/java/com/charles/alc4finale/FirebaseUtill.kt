package com.charles.alc4finale

import android.util.Log
import android.widget.Toast

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.ArrayList

private fun FirebaseUtill() = Unit

object FirebaseUtill {
    lateinit var mFirebaseDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    private var firebaseUtill: FirebaseUtill? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null
    lateinit var mDealModels: ArrayList<TravelDealModel>
    private var caller: ListActivity? = null
    var isAdmin: Boolean = false

    private val RC_SIGN_IN = 200

    private fun FirebaseUtill() = FirebaseUtill

    fun openFbReference(ref: String, callerActivity: ListActivity) {
        if (firebaseUtill == null) {
            firebaseUtill = FirebaseUtill()
            mFirebaseDatabase = FirebaseDatabase.getInstance()
            mFirebaseAuth = FirebaseAuth.getInstance()
            caller = callerActivity
            mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                if (firebaseAuth.currentUser == null) {
                    signIn()
                } else {
                    val userId = firebaseAuth.uid
                    checkAdmin(userId)
                }
                Toast.makeText(callerActivity.baseContext, "Welcome back!", Toast.LENGTH_LONG).show()
            }

        }
        mDealModels = ArrayList()
        mDatabaseReference = mFirebaseDatabase.reference.child(ref)
    }

    private fun checkAdmin(uid: String?) {
        isAdmin = false
        val ref = mFirebaseDatabase.reference.child("administrators")
            .child(uid!!)
        val listener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                isAdmin = true
                Log.d("Admin", "You are an administrator")
                caller!!.showMenu()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        ref.addChildEventListener(listener)
    }

    private fun signIn() {
        // Choose authentication providers
        val providers = listOf(AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        caller!!.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    fun attachListener() {
        mFirebaseAuth!!.addAuthStateListener(mAuthStateListener!!)
    }

    fun detachListener() {
        mFirebaseAuth!!.removeAuthStateListener(mAuthStateListener!!)
    }

}
