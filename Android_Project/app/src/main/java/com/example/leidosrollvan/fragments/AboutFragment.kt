package com.example.leidosrollvan.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.leidosrollvan.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var aboutBtn: Button
    private lateinit var aboutText: TextView
    private lateinit var creatorsBtn: Button
    private lateinit var creatorsText: TextView
    private lateinit var licenseBtn: Button
    private lateinit var licenseText: TextView


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
        val view:View = inflater.inflate(R.layout.fragment_about, container, false)

        aboutBtn = view?.findViewById(R.id.aboutbtn)!!
        aboutBtn.setOnClickListener(this)
        creatorsBtn = view?.findViewById(R.id.creatorsbtn)!!
        creatorsBtn.setOnClickListener(this)
        licenseBtn = view?.findViewById(R.id.licensebtn)!!
        licenseBtn.setOnClickListener(this)
        aboutText = view?.findViewById(R.id.aboutText)!!
        creatorsText = view?.findViewById(R.id.creatorsText)!!

        licenseText = view?.findViewById(R.id.licenseText)!!
        licenseText.movementMethod = LinkMovementMethod.getInstance()
        licenseText.setOnClickListener(this)


        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AboutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.aboutbtn -> {
                if(aboutText.visibility==View.VISIBLE){
                    aboutText.visibility=View.GONE
                }
                else{
                    aboutText.visibility=View.VISIBLE
                }
            }
            R.id.licenseText -> {
                var browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.data = Uri.parse("https://github.com/leidosApp/LeidosRollvan/blob/master/LICENSE")
                startActivity(browserIntent)

            }
            R.id.creatorsbtn -> {
                if(creatorsText.visibility==View.VISIBLE){
                    creatorsText.visibility=View.GONE
                }
                else{
                    creatorsText.visibility=View.VISIBLE
                }
            }
            R.id.licensebtn -> {
                if(licenseText.visibility==View.VISIBLE){
                    licenseText.visibility=View.GONE
                }
                else{
                    licenseText.visibility=View.VISIBLE
                }
            }
        }
    }
}