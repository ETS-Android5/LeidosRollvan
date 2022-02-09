package com.example.leidosrollvan.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.leidosrollvan.R
import com.example.leidosrollvan.activity.BusinessPageActivity
import com.example.leidosrollvan.activity.CategoryActivity
import com.example.leidosrollvan.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var searchByNameFragment = SearchByNameFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_search, container, false)

        val SearchByName: ImageButton = view.findViewById(R.id.searchByName)
        SearchByName.setOnClickListener(this)

        val Category1: LinearLayout = view.findViewById(R.id.btn1)
        Category1.setOnClickListener(this)
        val Category2: LinearLayout = view.findViewById(R.id.btn2)
        Category2.setOnClickListener(this)
        val Category3: LinearLayout = view.findViewById(R.id.btn3)
        Category3.setOnClickListener(this)
        val Category4: LinearLayout = view.findViewById(R.id.btn4)
        Category4.setOnClickListener(this)
        val Category5: LinearLayout = view.findViewById(R.id.btn5)
        Category5.setOnClickListener(this)
        val Category6: LinearLayout = view.findViewById(R.id.btn6)
        Category6.setOnClickListener(this)

        return view
    }

    private fun replaceFragment(fragment : Fragment){
        if(fragment != null){
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.searchByName -> {
                replaceFragment(searchByNameFragment)
            }
            R.id.btn1-> {
                val categoryName1 = "Vegan"
                startActivity(Intent(activity, CategoryActivity::class.java).putExtra("category", categoryName1))
            }
            R.id.btn2-> {
                val categoryName1 = "Tea and Coffee"
                startActivity(Intent(activity, CategoryActivity::class.java).putExtra("category", categoryName1))
            }
            R.id.btn3-> {
                val categoryName1 = "Healthy"
                startActivity(Intent(activity, CategoryActivity::class.java).putExtra("category", categoryName1))
            }
            R.id.btn4-> {
                val categoryName1 = "Fast Food"
                startActivity(Intent(activity, CategoryActivity::class.java).putExtra("category", categoryName1))
            }
            R.id.btn5-> {
                val categoryName1 = "Desserts"
                startActivity(Intent(activity, CategoryActivity::class.java).putExtra("category", categoryName1))
            }
            R.id.btn6-> {
                val categoryName1 = "Rolls"
                startActivity(Intent(activity, CategoryActivity::class.java).putExtra("category", categoryName1))
            }
        }
    }

}
