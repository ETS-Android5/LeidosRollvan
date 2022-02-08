package com.example.leidosrollvan.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.leidosrollvan.R
import com.example.leidosrollvan.activity.LoginActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GuestAccountFragment : Fragment(), View.OnClickListener{
    // TODO: Rename and change types of parameters
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var helpFragment = HelpFragment()
    private var aboutFragment = AboutFragment()

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
        val view: View = inflater!!.inflate(R.layout.fragment_guest_account, container, false)

        val guestLoginBtn: Button = view.findViewById(R.id.guestLogin)
        guestLoginBtn.setOnClickListener(this)

        val aboutBtn : Button = view.findViewById(R.id.guestAbout)
        aboutBtn.setOnClickListener(this)

        val helpBtn : Button = view.findViewById(R.id.guestHelp)
        helpBtn.setOnClickListener(this)

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
        when(v?.id){
            R.id.guestLogin -> {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
            R.id.guestHelp -> {
                replaceFragment(helpFragment)
            }
            R.id.guestAbout -> {
                replaceFragment(aboutFragment)
            }
        }
    }
}