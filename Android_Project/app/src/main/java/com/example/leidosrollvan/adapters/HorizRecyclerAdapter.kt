package com.example.leidosrollvan.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leidosrollvan.R
import com.example.leidosrollvan.dataClasses.Business
import com.example.leidosrollvan.dataClasses.BusinessImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class HorizRecyclerAdapter(private val businessList: ArrayList<Business>,
                           private val businessIdList: ArrayList<String>,
                           onBusiClickListener: onBusiClickListener
) : RecyclerView.Adapter<HorizRecyclerAdapter.ViewHolder>()  {
    private var monBusiClickListener: onBusiClickListener
    init {
        this.monBusiClickListener=onBusiClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.horiz_bus,
            parent, false)
        return ViewHolder(itemView,monBusiClickListener)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = businessList[position]
        val currentID = businessIdList[position]

        val mRef = FirebaseDatabase.getInstance().getReference("Business Images")
        mRef.child(currentID).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val uri = snapshot.getValue(BusinessImage::class.java)!!.mImageUrl
                    holder.businessName.setText(currentItem.businessName)
                    Picasso.with(holder.businessImage.context).load(uri).into(holder.businessImage)
                }else {
                    holder.businessName.setText(currentItem.businessName)
                    holder.businessImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Image retrieval error", error.message)
            }
        })
    }

    override fun getItemCount(): Int {
        return businessList.size
    }


    class ViewHolder(itemView : View, onBusiClickListener: onBusiClickListener) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
        var businessName : TextView = itemView.findViewById(R.id.businessCard)
        var businessImage : ImageView = itemView.findViewById(R.id.businessCardImage)
        var onBusiClickListener: onBusiClickListener
        init {
            itemView.setOnClickListener(this)
            this.onBusiClickListener =onBusiClickListener
        }


        override fun onClick(v: View?) {
            onBusiClickListener.onBusiClick(adapterPosition)
        }

    }

    interface onBusiClickListener{
        fun onBusiClick(position: Int)
    }

}