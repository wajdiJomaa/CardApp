package com.example.cardapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_question_fragment2.*
import kotlinx.android.synthetic.main.fragment_quiz.*
import android.R.layout
import android.content.Intent
import android.view.View.inflate
import kotlinx.android.synthetic.main.builder_input.*






class fragment_quiz() : Fragment() {

    var quizlist = mutableListOf<Quiz>()
    val  quizAdapter = QuizAdapter(quizlist)

//    fun setAdapter(ls:MutableList<Quiz>){
//        quizlist = ls
//        quizAdapter.setCardls(ls)
//    }
    fun add(quiz: Quiz) {
        quizlist.add(quiz)
        quizAdapter.notifyItemInserted(quizlist.size - 1)
    }

    fun delete(callback:(Quiz)->Unit){
        quizAdapter.setOnClickItem { card, i ->
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure you want to delete the question?")
            builder.setCancelable(true)
            builder.setPositiveButton("Yes"){dialog, _ ->
                callback(card)

                quizlist.removeAt(i)
                quizAdapter.notifyItemRemoved(i)
                quizAdapter.notifyItemRangeChanged(i,quizlist.size)

                dialog.dismiss()
            }

            builder.setNegativeButton("No"){dialog ,_ ->
                dialog.dismiss()}

            val alert = builder.create()
            alert.show()
        }
    }


    fun add(callback: (Quiz, Long?) -> Unit, callback2: (String) -> Long?){
        quizAdapter.setOnClickAdd{ quiz, i ->
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setMessage("Enter Card Title")
            builder.setCancelable(true)


            val viewInflated: View = layoutInflater.inflate(R.layout.builder_input,null,false)

            val input = viewInflated.findViewById<EditText>(R.id.edCardTitle)

            builder.setView(viewInflated)


            builder.setPositiveButton("Add"){dialog, _ ->

                val id = callback2(input.text.toString())
                if (id == null){
                    Toast.makeText(context, "No Such Card", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    callback(quiz,id)
                }
            }

            builder.setNegativeButton("Cancel"){dialog ,_ ->
                dialog.dismiss()}

            val alert = builder.create()
            alert.show()
        }
    }

    fun click(){

        quizAdapter.setOnClick { i ->
            val intent = Intent(context,TakeQuiz::class.java)
            intent.putExtra("id",quizlist[i].id)
            startActivity(intent)
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvQuiz.adapter = quizAdapter
        rvQuiz.layoutManager = LinearLayoutManager(context)
        click()
    }
}