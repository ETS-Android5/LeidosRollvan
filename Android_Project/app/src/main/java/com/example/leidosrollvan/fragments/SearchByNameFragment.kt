package com.example.leidosrollvan.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leidosrollvan.R
import com.example.leidosrollvan.activity.BusinessPageActivity
import com.example.leidosrollvan.adapters.CustomRecyclerAdapter
import com.example.leidosrollvan.adapters.SearchAdapter
import com.example.leidosrollvan.dataClasses.Business
import com.example.leidosrollvan.dataClasses.User
import com.google.firebase.database.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchByNameFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var reference: DatabaseReference
    private lateinit var referenceTest: Query
    private lateinit var businessRecyclerView : RecyclerView


    private lateinit var mBusiness: ArrayList<Business>
    private lateinit var businessIdList : ArrayList<String>
    private lateinit var searchEditTextSearch : EditText
    private lateinit var noMatches: TextView

    //private var mBusiness: ArrayList<Business>? = null
    //private var businessIdList: ArrayList<String>? = null
    private lateinit var searchAdapter: RecyclerView
    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            //businessRecyclerView = view?.findViewById(R.id.businessRecyclerView)!!
            //businessRecyclerView.layoutManager = LinearLayoutManager(activity)
            //businessRecyclerView.setHasFixedSize(true)

            //businessList = arrayListOf<Business>()

            //getBusinessData();
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.activity_search_by_name_fragment, container, false)

        searchAdapter = view.findViewById(R.id.recyclerView_search)
        searchAdapter!!.setHasFixedSize(true)
        searchAdapter!!.layoutManager = LinearLayoutManager(context)


        mBusiness = ArrayList()
        businessIdList = ArrayList()
        getBusinessData()

        noMatches = view.findViewById(R.id.textViewNoMatches)

        searchEditTextSearch = view.findViewById(R.id.searchBusinessET)

        searchEditTextSearch!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO:("Not yet implemented")
            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                searchForBusiness(cs.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                //TODO:("Not yet implemented")
            }

        })

        return view
    }

    private fun getBusinessData() {
        val refUser1 = FirebaseDatabase.getInstance().getReference("Businesses")

        refUser1.addValueEventListener(object : ValueEventListener,CustomRecyclerAdapter.OnBusiClickListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mBusiness as ArrayList<User>).clear()
                (businessIdList as ArrayList<User>).clear()
                if(searchEditTextSearch!!.text.toString()==""){
                    for(businessSnapshot in snapshot.children){
                        val business  = businessSnapshot.getValue(Business::class.java)
                        mBusiness.add(business!!)
                        businessIdList.add(businessSnapshot.key!!)
                    }


                    searchAdapter.adapter = CustomRecyclerAdapter(mBusiness,businessIdList,this)
                    //recyclerView!!.adapter = searchAdapter


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onBusiClick(position: Int) {
                val bID = businessIdList[position]
                var i  = Intent(activity,
                    BusinessPageActivity::class.java)
                i.putExtra("b_id", bID)
                startActivity(i)
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
            }
        })
    }

    private fun searchForBusiness(str:String){
        val refUser12 = FirebaseDatabase.getInstance().reference.child("Businesses").orderByChild("businessName")


        refUser12.addValueEventListener(object :ValueEventListener,CustomRecyclerAdapter.OnBusiClickListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mBusiness as ArrayList<User>).clear()
                (businessIdList as ArrayList<User>).clear()
                for(businessSnapshot in snapshot.children){
                    val business  = businessSnapshot.getValue(Business::class.java)
                    val businessName = business?.businessName
                    if(businessName!!.contains(str,ignoreCase = true)) {
                        mBusiness.add(business!!)
                        businessIdList.add(businessSnapshot.key!!)
                        noMatches.apply {
                            text = ""
                        }
                    }

                    if(mBusiness.size==0){
                        noMatches.apply {
                            text = "No Matches"
                        }
                    }




                }
                searchAdapter.adapter = CustomRecyclerAdapter(mBusiness,businessIdList,this)



                //recyclerView!!.adapter = searchAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onBusiClick(position: Int) {
                val bID = businessIdList[position]
                var i  = Intent(activity,
                    BusinessPageActivity::class.java)
                i.putExtra("b_id", bID)
                startActivity(i)
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
            }


        })
    }


}

