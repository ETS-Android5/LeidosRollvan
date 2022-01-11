package com.example.leidosrollvan.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.leidosrollvan.LoginActivity
import com.example.leidosrollvan.R

class guestAccountFragment : Fragment(), View.OnClickListener {

    private val helpFragment = HelpFragment()
    private val aboutFragment = AboutFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_account_guest, container, false)

        val logInBtn: Button = view.findViewById(R.id.logIn)
        logInBtn.setOnClickListener(this)

        val helpBtn: Button = view.findViewById(R.id.help)
        helpBtn.setOnClickListener(this)

        val aboutBtn: Button = view.findViewById(R.id.about)
        aboutBtn.setOnClickListener(this)



        return view
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.logIn -> {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
            R.id.help -> {
                replaceFragment(helpFragment)
            }
            R.id.about -> {
                replaceFragment(aboutFragment)
            }
        }
    }
    private fun replaceFragment(fragment : Fragment){
        if(fragment != null){
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

}