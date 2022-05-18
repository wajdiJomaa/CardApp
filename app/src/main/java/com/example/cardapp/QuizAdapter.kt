package com.example.cardapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.quiz_view.view.*

class QuizAdapter (var quizs : List<Quiz>):RecyclerView.Adapter<QuizAdapter.QuizViewHolder>()
{

        private var onClickItem: ((Quiz,Int) -> Unit)? = null
        private var onClickAdd: ((Quiz,Int) ->  Unit)? = null
        private var onClick: ((Int) -> Unit)? = null


        fun setOnClickItem(callback:(Quiz,Int)->Unit){
            this.onClickItem = callback
        }

        fun setOnClickAdd(callback:(Quiz,Int)->Unit){
            this.onClickAdd = callback
        }

        fun setOnClick(callback:(Int)->Unit){
            this.onClick = callback
        }

    inner class QuizViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            val btnDelete1 = itemView.btnDelete1
            val btnAddCardToQuiz = itemView.btnAddCardToQuiz
        }

    fun setCardls(ls : MutableList<Quiz>) : MutableList<Quiz>{
        quizs = ls
        return ls
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_view,parent,
            false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.itemView.apply {

            layQuiz.background.setTint(ContextCompat.getColor(context, quizs[position].color))
            tvTitle1.text = quizs[position].title
            tvSubTitle1.text = "${quizs[position].description}"


            btnDelete1.setOnClickListener { onClickItem?.invoke(quizs[position], position) }

            btnAddCardToQuiz.setOnClickListener {
                onClickAdd?.invoke(
                    quizs[position],
                    position
                )
            }
            this.setOnClickListener {
                onClick?.invoke(position)
            }
        }


}
    override fun getItemCount(): Int {
        return quizs.size
    }


}