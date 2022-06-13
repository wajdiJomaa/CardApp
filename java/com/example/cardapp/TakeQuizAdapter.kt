package com.example.cardapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_true_false.*
import kotlinx.android.synthetic.main.fragment_true_false.view.*
import kotlinx.android.synthetic.main.takequiz_view.view.*

class TakeQuizAdapter (var cards : List<Card>): RecyclerView.Adapter<TakeQuizAdapter.TakeQuizViewHolder>()
{
    private var hold : MutableList<TakeQuizViewHolder>? = mutableListOf()

    inner class TakeQuizViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TakeQuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.takequiz_view,parent,
            false)
        return TakeQuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: TakeQuizViewHolder, position: Int) {
        hold?.add(holder)
        holder.itemView.apply {
            tvQuestionn.text = cards[position].question
            tvshow.setTag(position)
            laytakequizadapter.background.setTint(ContextCompat.getColor(context, R.color.white))

            tvshow.visibility = View.GONE
            if (cards[position].type == "QuestionAnswer")
            {
                rdTrueFalse.visibility = View.GONE
            }
            else
            {
                edAnswerr.visibility = View.GONE
            }
        }


    }
    override fun getItemCount(): Int {
        return cards.size
    }


  fun calculate():String{
      var score = 0
      var answer = ""
//      println(hold)
      for (i in 0..(cards.size - 1)) {

          val h = hold?.get(i)
//          println(h)
          h?.itemView?.apply {
              tvshow.visibility = View.VISIBLE
              val x = tvshow.getTag().toString().toInt()

              if (cards[x].type == "QuestionAnswer")
              {
                  answer = edAnswerr.text.toString()
              }
              else
              {
                  val checked = rdTrueFalse.checkedRadioButtonId
                  when(checked){
                      R.id.TrueR -> answer = "True"
                      R.id.FalseR -> answer = "False"
                  }
              }

              if (cards[x].answer.toLowerCase() == answer.toLowerCase()) {
                  score++
                  tvshow.setTextColor(ContextCompat.getColor(context, R.color.darkGreen))
                  tvshow.text = "Correct Answer!!"
              } else {
                  tvshow.setTextColor(ContextCompat.getColor(context, R.color.darkRed))
                  tvshow.text ="correct answer: ${cards[x].answer.toLowerCase()}"

              }
          }

      }
      return "$score / ${cards.size}"
    }

}
