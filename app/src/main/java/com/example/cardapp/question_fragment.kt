package com.example.cardapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_question_fragment2.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [question_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class question_fragment (): Fragment() {

    var cardList = mutableListOf<Card>()
    val  cardAdapter = CardAdapter(cardList)


    fun setAdapter(ls:MutableList<Card>){
        cardList = ls
        cardAdapter.setCardls(ls)
    }

    fun add(c:Card){
        cardList.add(c)
        cardAdapter . notifyItemInserted (cardList.size - 1)
    }

    fun delete(callback:(Card)->Unit){
        cardAdapter.setOnClickItem { card, i ->
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure you want to delete the question?")
            builder.setCancelable(true)
            builder.setPositiveButton("Yes"){dialog, _ ->
                callback(card)

                cardList.removeAt(i)
                cardAdapter.notifyItemRemoved(i)
                cardAdapter.notifyItemRangeChanged(i,cardList.size)

                dialog.dismiss()
            }

            builder.setNegativeButton("No"){dialog ,_ ->
                dialog.dismiss()}

            val alert = builder.create()
            alert.show()
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_fragment2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvCards.adapter = cardAdapter
        rvCards.layoutManager = LinearLayoutManager(context)
    }
}