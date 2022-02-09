package com.example.leidosrollvan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.leidosrollvan.R
import com.example.leidosrollvan.fragments.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val accountFragment = AccountFragment()
    private val guestAccountFragment = GuestAccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_home -> replaceFragment(homeFragment)
                R.id.id_search -> replaceFragment(searchFragment)
                R.id.id_map -> startActivity(Intent(this, MapActivity::class.java))
                R.id.id_account -> {
                    if(FirebaseAuth.getInstance().currentUser == null){
                        replaceFragment(guestAccountFragment)
                    }else {
                        replaceFragment(accountFragment)
                    }
                }
            }
            true
        }

    }

    private fun replaceFragment(fragment : Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}