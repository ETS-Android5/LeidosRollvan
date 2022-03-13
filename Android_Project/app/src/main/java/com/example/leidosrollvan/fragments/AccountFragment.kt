package com.example.leidosrollvan.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.leidosrollvan.R
import com.example.leidosrollvan.activity.LoginActivity
import com.example.leidosrollvan.activity.MyFavouritesActivity
import com.example.leidosrollvan.activity.MyNotificationsActivity
import com.example.leidosrollvan.dataClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var user : FirebaseUser
    private lateinit var reference : DatabaseReference

    private var helpFragment = HelpFragment()
    private var aboutFragment = AboutFragment()

    private lateinit var userID : String

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
        val view: View = inflater!!.inflate(R.layout.fragment_account, container, false)

        val logoutBtn : Button = view.findViewById(R.id.signOut)
        logoutBtn.setOnClickListener(this)

        val aboutBtn : Button = view.findViewById(R.id.about)
        aboutBtn.setOnClickListener(this)

        val helpBtn : Button = view.findViewById(R.id.help)
        helpBtn.setOnClickListener(this)

        val myNotiBtn : Button = view.findViewById(R.id.notifications)
        myNotiBtn.setOnClickListener(this)

        val faveButton : Button = view.findViewById(R.id.yourFavourites)
        faveButton.setOnClickListener(this)

        user = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users")
        userID = user.uid

        val userName : TextView = view.findViewById(R.id.userName)
        val userEmail : TextView = view.findViewById(R.id.userEmail)

        reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val userProfile : User = dataSnapshot.getValue(
                    User::class.java) as User
                if(userProfile != null){
                    val fullName : String = userProfile.name
                    val email : String = userProfile.email

                    userName.text = fullName
                    userEmail.text = email
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        })
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.signOut -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(Intent(activity, LoginActivity::class.java))
            }
            R.id.help -> {
                replaceFragment(helpFragment)
            }
            R.id.about -> {
                replaceFragment(aboutFragment)
            }
            R.id.notifications -> {
                startActivity(Intent(activity, MyNotificationsActivity::class.java))
            }
            R.id.yourFavourites -> {
                startActivity((Intent(activity, MyFavouritesActivity::class.java)))
            }

        }
    }
}