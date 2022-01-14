package com.example.leidosrollvan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.leidosrollvan.fragments.AccountFragment
import com.example.leidosrollvan.fragments.HomeFragment
import com.example.leidosrollvan.fragments.MapFragment
import com.example.leidosrollvan.fragments.SearchFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val mapFragment = MapFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)

        bottom_navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_home -> replaceFragment(homeFragment)
                R.id.id_search -> replaceFragment(searchFragment)
                R.id.id_map -> replaceFragment(mapFragment)
                R.id.id_account -> {
                    if(FirebaseAuth.getInstance().currentUser == null){
                        startActivity(Intent(this, LoginActivity::class.java))
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