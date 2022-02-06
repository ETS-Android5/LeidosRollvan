package com.example.leidosrollvan.adapters

import android.content.Context
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


class SearchAdapter(
    mContext: Context,
    mBusiness: ArrayList<Business>,
    businessIdList: ArrayList<String>,
    isChatCheck:Boolean
) :RecyclerView.Adapter<SearchAdapter.ViewHolder?>()
{

    private val mContext:Context
    private val mBusiness: ArrayList<Business>
    private val isChatCheck:Boolean
    private val businessIdList: ArrayList<String>

    init {
        this.mBusiness = mBusiness
        this.mContext = mContext
        this.isChatCheck = isChatCheck
        this.businessIdList = businessIdList
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.recycler_list_item,viewGroup, false)
        return SearchAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val business: Business = mBusiness[position]
        val currentID = businessIdList[position]

        val mRef = FirebaseDatabase.getInstance().getReference("Business Images")
        mRef.child(currentID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val uri = snapshot.getValue(BusinessImage::class.java)!!.mImageUrl
                    holder.businessName.text = business!!.getBusinessName()
                    holder.businessMobile.text = business!!.businessMobile
                    Picasso.with(holder.businessImage.context).load(uri).into(holder.businessImage)
                } else {
                    holder.businessName.text = business!!.getBusinessName()
                    holder.businessMobile.text = business!!.businessMobile
                    holder.businessImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Image retrieval error", error.message)
            }
        })
    }


    override fun getItemCount(): Int {
        return mBusiness.size
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        var businessName: TextView
        var businessMobile: TextView
        val businessImage : ImageView


        init {
            businessName = itemView.findViewById(R.id.nameRecyclerItem)
            businessMobile= itemView.findViewById(R.id.contactRecyclerItem)
            businessImage= itemView.findViewById(R.id.imageRecyclerItem)
        }
    }




}
