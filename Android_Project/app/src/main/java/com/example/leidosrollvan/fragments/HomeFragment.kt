package com.example.leidosrollvan.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leidosrollvan.*
import com.example.leidosrollvan.R
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var reference: DatabaseReference
    private lateinit var businessRecyclerView : RecyclerView
    private lateinit var businessList: ArrayList<Business>
    private lateinit var businessIdList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        businessIdList = arrayListOf<String>()
        businessList = arrayListOf<Business>()
        businessRecyclerView = view.findViewById(R.id.businessRecyclerView)
        businessRecyclerView.layoutManager = LinearLayoutManager(view.context)
        businessRecyclerView.setHasFixedSize(true)

        getBusinessData()
    }

    private fun getBusinessData() {
        reference = FirebaseDatabase.getInstance().getReference("Businesses")

        reference.addValueEventListener(object : ValueEventListener,
            CustomRecyclerAdapter.onBusiClickListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(businessSnapshot in snapshot.children){
                        val business  = businessSnapshot.getValue(Business::class.java)
                        businessList.add(business!!)
                        businessIdList.add(businessSnapshot.key!!)
                    }


                    businessRecyclerView.adapter = CustomRecyclerAdapter(businessList, businessIdList,this )



                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "error", Toast.LENGTH_LONG).show()
            }

            override fun onBusiClick(position: Int) {
                val b_id = businessIdList.get(position)
                var i  = Intent(activity,businessPage::class.java)
                i.putExtra("b_id", b_id)
                startActivity(i)
            }


        })
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}