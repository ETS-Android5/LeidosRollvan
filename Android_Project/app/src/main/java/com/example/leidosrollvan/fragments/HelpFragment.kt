package com.example.leidosrollvan.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.leidosrollvan.R
import com.example.leidosrollvan.activity.forgotPasswordActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HelpFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var unablesignbtn: Button
    private lateinit var unablesignText: TextView
    private lateinit var forgotbtn: TextView
    private lateinit var favbtn: Button
    private lateinit var favText: TextView
    private lateinit var requestrembtn: Button
    private lateinit var requestremText: TextView
    private lateinit var cantssearchbtn: Button
    private lateinit var cantssearchText: TextView

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
        val view:View = inflater.inflate(R.layout.fragment_help, container, false)
        unablesignbtn = view?.findViewById(R.id.unablesignbtn)!!
        unablesignbtn.setOnClickListener(this)
        forgotbtn = view?.findViewById(R.id.forgotbtn)!!
        forgotbtn.setOnClickListener(this)
        favbtn = view?.findViewById(R.id.favbtn)!!
        favbtn.setOnClickListener(this)
        requestrembtn = view?.findViewById(R.id.requestrembtn)!!
        requestrembtn.setOnClickListener(this)
        cantssearchbtn = view?.findViewById(R.id.cantssearchbtn)!!
        cantssearchbtn.setOnClickListener(this)
        unablesignText = view?.findViewById(R.id.unablesignText)!!
        favText = view?.findViewById(R.id.favText)!!
        requestremText = view?.findViewById(R.id.requestremText)!!
        cantssearchText = view?.findViewById(R.id.cantssearchText)!!

        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HelpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.unablesignbtn -> {
                if(unablesignText.visibility==View.VISIBLE){
                    unablesignText.visibility=View.GONE
                    forgotbtn.visibility=View.GONE
                }
                else{
                    unablesignText.visibility=View.VISIBLE
                    forgotbtn.visibility=View.VISIBLE
                }
            }
            R.id.forgotbtn ->{
                startActivity(Intent(activity, forgotPasswordActivity::class.java))
            }
            R.id.favbtn -> {
                if(favText.visibility==View.VISIBLE){
                    favText.visibility=View.GONE
                }
                else{
                    favText.visibility=View.VISIBLE
                }
            }
            R.id.requestrembtn -> {
                if(requestremText.visibility==View.VISIBLE){
                    requestremText.visibility=View.GONE
                }
                else{
                    requestremText.visibility=View.VISIBLE
                }
            }
            R.id.cantssearchbtn -> {
                if(cantssearchText.visibility==View.VISIBLE){
                    cantssearchText.visibility=View.GONE
                }
                else{
                    cantssearchText.visibility=View.VISIBLE
                }
            }
        }
    }
}