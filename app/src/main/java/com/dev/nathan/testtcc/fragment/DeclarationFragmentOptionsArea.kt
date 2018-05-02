package com.dev.nathan.testtcc.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dev.nathan.testtcc.R


@SuppressLint("ValidFragment")
class DeclarationFragmentOptionsArea(val type : String) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_declaration_itens_area, container, false)
        val title = view.findViewById<TextView>(R.id.declaration_title_to_material)

        val area1 = view.findViewById<CardView>(R.id.declaration_content_cadview_area1)
      //  var text1 = view.findViewById<TextView>(R.id.declaration_text_content_area1)

        val area2 = view.findViewById<CardView>(R.id.declaration_content_cadview_area2)
        //var text2 = view.findViewById<TextView>(R.id.declaration_text_content_area2)





        when (type){

            getString(R.string.about_us_title)-> {
                aboutFunction(title,area1)
            }
            getString(R.string.inspiration_title)-> {
                inspirationFunction(title,area2)
            }

        }


        return view
    }



    private fun inspirationFunction(title:TextView,area: CardView) {

        title.text = getString(R.string.blog_title)
        area.visibility = View.VISIBLE
       // area.backgroundColor = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark)
    }

    private fun aboutFunction(title:TextView,area: CardView) {

        title.text = getString(R.string.info_title)
        area.visibility = View.VISIBLE
       // area.backgroundColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
    }
}