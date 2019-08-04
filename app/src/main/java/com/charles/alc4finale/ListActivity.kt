package com.charles.alc4finale

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import com.firebase.ui.auth.AuthUI
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.ArrayList

class ListActivity : AppCompatActivity() {
    internal var dealModels: ArrayList<TravelDealModel>? = null
    private val mFirebaseDatabase: FirebaseDatabase? = null
    private val mDatabaseReference: DatabaseReference? = null
    private val mChildEventListener: ChildEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.list_activity_menu, menu)
        val insertMenu = menu.findItem(R.id.insert_menu)
        insertMenu.isVisible = FirebaseUtill.isAdmin == true

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.insert_menu -> {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout_menu -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        Log.d("Logout", "User Logged Out!")
                        FirebaseUtill.attachListener()
                    }
                FirebaseUtill.detachListener()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        FirebaseUtill.detachListener()
    }

    @SuppressLint("WrongConstant")
    override fun onResume() {
        super.onResume()
        FirebaseUtill.openFbReference("traveldeals", this)
        val rvDeals = findViewById<RecyclerView>(R.id.rvDeals)

        rvDeals.adapter = DealAdapter()
        rvDeals.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        FirebaseUtill.attachListener()
    }

    fun showMenu() {
        invalidateOptionsMenu()
    }
}
