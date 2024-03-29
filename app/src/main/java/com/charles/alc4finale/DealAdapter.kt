package com.charles.alc4finale

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

import java.util.ArrayList

class DealAdapter : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    internal var dealModels: ArrayList<TravelDealModel>
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseReference: DatabaseReference
    private val mChildListener: ChildEventListener

    init {
        //FirebaseUtill.openFbReference("traveldeals");
        mDatabaseReference = mFirebaseDatabase.reference.child("traveldeals")
        this.dealModels = FirebaseUtill.mDealModels
        mChildListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                /* **** Gets data from the database into the Travel dealModel class ****/
                val td = dataSnapshot.getValue(TravelDealModel::class.java)
                Log.d("Deal: ", td!!.title!!)

                /* ****Sets the id to the push id that was generated by firebase ****/
                td.id = dataSnapshot.key
                dealModels.add(td)

                notifyItemInserted(dealModels.size - 1)
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
        mDatabaseReference.addChildEventListener(mChildListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val context = parent.context
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.deal_row, parent, false)
        return DealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = dealModels[position]
        holder.bind(deal)
    }

    override fun getItemCount(): Int {
        return dealModels.size
    }

    inner class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var tvTitle: TextView = itemView.findViewById(R.id.dealTitle)
        private var tvDescription: TextView = itemView.findViewById(R.id.dealDescription)
        private var tvPrice: TextView = itemView.findViewById(R.id.dealPrice)
        private var tvImage: ImageView = itemView.findViewById(R.id.dealImage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(dealModel: TravelDealModel) {
            tvTitle.text = dealModel.title
            tvDescription.text = dealModel.description
            tvPrice.text = dealModel.price
            Picasso.with(tvImage.context).load(dealModel.imageUrl).resize(100,100).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(tvImage)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            Log.d("Click", position.toString())
            val selectedDeal = dealModels[position]
            val intent = Intent(v.context, AdminActivity::class.java)
            intent.putExtra("Deal", selectedDeal)
            v.context.startActivity(intent)
        }
    }

}
