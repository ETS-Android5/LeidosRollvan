package com.example.leidosrollvan.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leidosrollvan.R
import com.example.leidosrollvan.activity.BusinessPageActivity
import com.example.leidosrollvan.activity.CategoryActivity
import com.example.leidosrollvan.adapters.CustomRecyclerAdapter
import com.example.leidosrollvan.adapters.HorizRecyclerAdapter
import com.example.leidosrollvan.adapters.DealRecyclerAdapter
import com.example.leidosrollvan.dataClasses.Business
import com.example.leidosrollvan.dataClasses.Offer
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var reference: DatabaseReference
    private lateinit var businessRecyclerView: RecyclerView
    private lateinit var businessHorizRecyclerView: RecyclerView
    private lateinit var businessHorizRecyclerViewDeals: RecyclerView
    private lateinit var businessList: ArrayList<Business>
    private lateinit var offerList: ArrayList<String>
    private lateinit var offerIdList: ArrayList<String>
    private lateinit var businessIdList: ArrayList<String>

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
        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        val dessertCat: Button = view.findViewById(R.id.dessert)
        dessertCat.setOnClickListener(this)
        val HealthyCat: Button = view.findViewById(R.id.Healthy)
        HealthyCat.setOnClickListener(this)
        val fastFoodCat: Button = view.findViewById(R.id.fastFood)
        fastFoodCat.setOnClickListener(this)
        val VeganCat: Button = view.findViewById(R.id.VeganButton)
        VeganCat.setOnClickListener(this)
        val CoffeeAndTeaCat: Button = view.findViewById(R.id.CoffeeAndTea)
        CoffeeAndTeaCat.setOnClickListener(this)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        businessIdList = arrayListOf<String>()
        businessList = arrayListOf<Business>()
        offerList = arrayListOf<String>()
        offerIdList = arrayListOf<String>()
        businessRecyclerView = view.findViewById(R.id.businessRecyclerView)
        businessRecyclerView.layoutManager = LinearLayoutManager(view.context)
        businessRecyclerView.setHasFixedSize(true)

        businessHorizRecyclerView = view.findViewById(R.id.businessHorizRecyclerView)
        businessHorizRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        businessHorizRecyclerView.setHasFixedSize(true)

        businessHorizRecyclerViewDeals = view.findViewById(R.id.businessHorizRecyclerViewDeals)
        businessHorizRecyclerViewDeals.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        businessHorizRecyclerViewDeals.setHasFixedSize(true)

        getBusinessData()
    }

    private fun getBusinessData() {
        reference = FirebaseDatabase.getInstance().getReference("Businesses")

        reference.addValueEventListener(object : ValueEventListener,
            CustomRecyclerAdapter.OnBusiClickListener, HorizRecyclerAdapter.onBusiClickListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (businessSnapshot in snapshot.children) {
                        val business = businessSnapshot.getValue(Business::class.java)
                        businessList.add(business!!)
                        businessIdList.add(businessSnapshot.key!!)
                    }


                    businessRecyclerView.adapter =
                        CustomRecyclerAdapter(businessList, businessIdList, this)
                    businessHorizRecyclerView.adapter =
                        HorizRecyclerAdapter(businessList, businessIdList, this)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "error", Toast.LENGTH_LONG).show()
            }

            override fun onBusiClick(position: Int) {
                val bID = businessIdList[position]
                var i = Intent(
                    activity,
                    BusinessPageActivity::class.java
                )
                i.putExtra("b_id", bID)
                startActivity(i)
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
            }
        })

        reference = FirebaseDatabase.getInstance().getReference("Business Offer")

        reference.addValueEventListener(object : ValueEventListener,
            CustomRecyclerAdapter.OnBusiClickListener, DealRecyclerAdapter.onBusiClickListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (offerSnapshot in snapshot.children) {
                        val offer = offerSnapshot.getValue(Offer::class.java)
                        val offers = offer?.offers
                        for (item in offers!!) {
                            offerList.add(item!!)
                            offerIdList.add(offerSnapshot.key!!)
                        }

                    }
                    businessHorizRecyclerViewDeals.adapter =
                        DealRecyclerAdapter(offerList, offerIdList, this)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "error", Toast.LENGTH_LONG).show()
            }

            override fun onBusiClick(position: Int) {
                val bID = offerIdList[position]
                var i = Intent(
                    activity,
                    BusinessPageActivity::class.java
                )
                i.putExtra("b_id", bID)
                startActivity(i)
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
            }
        })
    }



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dessert -> {
                val categoryName1 = "Desserts"
                startActivity(
                    Intent(activity, CategoryActivity::class.java).putExtra(
                        "category",
                        categoryName1
                    )
                )
            }
            R.id.Healthy -> {
                val categoryName1 = "Healthy"
                startActivity(
                    Intent(activity, CategoryActivity::class.java).putExtra(
                        "category",
                        categoryName1
                    )
                )
            }
            R.id.fastFood -> {
                val categoryName1 = "Fast Food"
                startActivity(
                    Intent(activity, CategoryActivity::class.java).putExtra(
                        "category",
                        categoryName1))
            }
            R.id.CoffeeAndTea -> {
                val categoryName1 = "Tea and Coffee"
                startActivity(
                    Intent(activity, CategoryActivity::class.java).putExtra(
                        "category",
                        categoryName1
                    )
                )
            }
            R.id.VeganButton -> {
                val categoryName1 = "Vegan"
                startActivity(
                    Intent(activity, CategoryActivity::class.java).putExtra(
                        "category",
                        categoryName1))
            }
        }
    }
}