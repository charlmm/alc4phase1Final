package com.charles.alc4finale

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException

class AdminActivity : AppCompatActivity() {
    val ACTIVITY_RESULT_CONST = 1000
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mTxtTitle: EditText? = null
    private var mTxtPrice: EditText? = null
    private var mTxtDescription: EditText? = null
    private var mImageUri: Uri? = null
    private var mImage: Button? = null
    private var setImage: ImageView? = null
    internal var dealModel: TravelDealModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Creates an instance of the database
        mFirebaseDatabase = FirebaseUtill.mFirebaseDatabase

        // Creates a reference of the database and assigns the targeted path
        mDatabaseReference = FirebaseUtill.mDatabaseReference

        mTxtTitle = findViewById(R.id.dealTitle)
        mTxtDescription = findViewById(R.id.dealDescription)
        mTxtPrice = findViewById(R.id.dealPrice)
        mImage = findViewById(R.id.selectImage)
        setImage = findViewById(R.id.dealImage)

        val intent = intent
        var deal = intent.getSerializableExtra("Deal") as TravelDealModel?

        mImage!!.setOnClickListener{ selectImageFromGallery()}

        if (deal == null) {
            deal = TravelDealModel()
        }
        this.dealModel = deal
        mTxtTitle!!.setText(deal.title)
        mTxtDescription!!.setText(deal.description)
        mTxtPrice!!.setText(deal.price)

    }

    private fun selectImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), ACTIVITY_RESULT_CONST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == ACTIVITY_RESULT_CONST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
             mImageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mImageUri) as Bitmap
                setImage!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                Log.d("FETCH_IMAGE", "could not fetch")
                e.printStackTrace()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.save_menu, menu)
        if (FirebaseUtill.isAdmin) {
            menu.findItem(R.id.delete_menu).isVisible = true
            menu.findItem(R.id.save_menu).isVisible = true
            enableEditTexts(true)
        } else {
            menu.findItem(R.id.delete_menu).isVisible = false
            menu.findItem(R.id.save_menu).isVisible = false
            enableEditTexts(false)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> {
                savedDeal()
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show()
                clean()
                backToList()
                return true
            }
            R.id.delete_menu -> {
                deleteDeal()
                Toast.makeText(this, "Deal deleted!", Toast.LENGTH_LONG).show()
                backToList()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun savedDeal() {
        dealModel!!.title = mTxtTitle!!.text.toString()
        dealModel!!.description = mTxtDescription!!.text.toString()
        dealModel!!.price = mTxtPrice!!.text.toString()
        dealModel!!.imageUrl = mImageUri.toString()

        if (dealModel!!.id == null) {
            mDatabaseReference!!.push().setValue(dealModel)
        } else {
            mDatabaseReference!!.child(dealModel!!.id!!).setValue(dealModel)
        }
    }

    private fun deleteDeal() {
        if (dealModel == null) {
            Toast.makeText(this, "Please save the dealModel before deleting", Toast.LENGTH_SHORT).show()
            return
        }
        mDatabaseReference!!.child(dealModel!!.id!!).removeValue()

    }

    private fun backToList() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }

    private fun clean() {
        mTxtTitle!!.setText("")
        mTxtPrice!!.setText("")
        mTxtDescription!!.setText("")
        mTxtTitle!!.requestFocus()
    }

    private fun enableEditTexts(isEnabled: Boolean) {
        mTxtTitle!!.isEnabled = isEnabled
        mTxtDescription!!.isEnabled = isEnabled
        mTxtPrice!!.isEnabled = isEnabled
    }
}
