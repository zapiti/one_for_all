package com.dev.nathan.testtcc.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dev.nathan.testtcc.R
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast


@SuppressLint("ValidFragment")
class RelatedFragmentOptionsArea(val type : String) : Fragment() {

    fun RelatedFragmentOptionsArea(){

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_related_itens_area, container, false)
        val title = view.findViewById<TextView>(R.id.title_to_material)

        val area1 = view.findViewById<CardView>(R.id.content_cadview_area1)
      //  var text1 = view.findViewById<TextView>(R.id.text_content_area1)

        var area2 = view.findViewById<CardView>(R.id.content_cadview_area2)
     //   var text2 = view.findViewById<TextView>(R.id.text_content_area2)

        var area3 = view.findViewById<CardView>(R.id.content_cadview_area3)
     //   var text3 = view.findViewById<TextView>(R.id.text_content_area3)

        var area4 = view.findViewById<CardView>(R.id.content_cadview_area4)
     //   var text4 = view.findViewById<TextView>(R.id.text_content_area4)

        var area5 = view.findViewById<CardView>(R.id.content_cadview_area5)
    //    var text5 = view.findViewById<TextView>(R.id.text_content_area5)



        when (type){

            getString(R.string.info_title)-> {
                infoFunction(title,area1)
            }
            getString(R.string.blog_title)-> {
                supportFunction(title,area2)
            }
            getString(R.string.protect_title)-> {
                protectFunction(title,area3)
            }

            getString(R.string.specialist_title)-> {
                specialistFunction(title,area4)
            }

            getString(R.string.prevention_title) -> {
                preventionFunction(title,area5)
            }

        }


        return view
    }

    private fun preventionFunction(title: TextView,area: CardView) {

        title.text = getString(R.string.prevention_title)
        area.visibility = View.VISIBLE
        area.backgroundColor = ContextCompat.getColor(requireContext(), R.color.accent_material_dark)

    }

    private fun specialistFunction(title: TextView,area: CardView) {

        title.text = getString(R.string.specialist_title)
        area.visibility = View.VISIBLE
        area.backgroundColor = ContextCompat.getColor(requireContext(), R.color.background_floating_material_light)
    }

    private fun protectFunction(title: TextView,area: CardView) {

        title.text = getString(R.string.protect_title)
        area.visibility = View.VISIBLE
        area.backgroundColor = ContextCompat.getColor(requireContext(), R.color.abc_hint_foreground_material_dark)
    }

    private fun supportFunction(title: TextView,area: CardView) {

        title.text = getString(R.string.blog_title)
        area.visibility = View.VISIBLE
        area.backgroundColor = ContextCompat.getColor(requireContext(), R.color.abc_btn_colored_borderless_text_material)
    }

    private fun infoFunction(title: TextView,area: CardView) {

        title.text = getString(R.string.info_title)
        area.visibility = View.VISIBLE
     area.backgroundColor = ContextCompat.getColor(requireContext(), R.color.ripple_material_light)
    }
}