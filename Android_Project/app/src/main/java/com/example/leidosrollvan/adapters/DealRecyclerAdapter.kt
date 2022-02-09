package com.example.leidosrollvan.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leidosrollvan.dataClasses.Business
import com.example.leidosrollvan.dataClasses.BusinessImage
import com.example.leidosrollvan.R
import com.example.leidosrollvan.dataClasses.Offer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class DealRecyclerAdapter(private val offerList: ArrayList<String>,
                          private val businessIdList: ArrayList<String>,
                          onBusiClickListener: onBusiClickListener
) : RecyclerView.Adapter<DealRecyclerAdapter.ViewHolder>()  {
    private var monBusiClickListener: onBusiClickListener
    init {
        this.monBusiClickListener=onBusiClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.horizdeal,
            parent, false)
        return ViewHolder(itemView,monBusiClickListener)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = offerList[position]
        val currentID = businessIdList[position]
        val mRef = FirebaseDatabase.getInstance().getReference("Businesses")
        mRef.child(currentID).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val busiName = snapshot.getValue(Business::class.java)!!.businessName
                holder.offerText.setText(currentItem.toString())
                holder.businessName.setText(busiName)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        val imRef = FirebaseDatabase.getInstance().getReference("Business Images")
        imRef.child(currentID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val uri = snapshot.getValue(BusinessImage::class.java)!!.mImageUrl
                    Picasso.with(holder.businessImage.context).load(uri).into(holder.businessImage)
                } else {
                    holder.businessImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        }

    override fun getItemCount(): Int {
        return offerList.size
    }


    class ViewHolder(itemView : View, onBusiClickListener: onBusiClickListener) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var businessName : TextView = itemView.findViewById(R.id.offerBusiName)
        var offerText : TextView = itemView.findViewById(R.id.offerText)
        var businessImage : ImageView = itemView.findViewById(R.id.offerImage)
        var onBusiClickListener: onBusiClickListener
        init {
            itemView.setOnClickListener(this)
            this.onBusiClickListener=onBusiClickListener
        }


        override fun onClick(v: View?) {
            onBusiClickListener.onBusiClick(adapterPosition)
        }


    }

    interface onBusiClickListener{
        fun onBusiClick(position: Int)
    }

}