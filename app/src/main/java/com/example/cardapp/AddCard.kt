package com.example.cardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_add_card.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_question_answer.*
import kotlinx.android.synthetic.main.fragment_true_false.*

class AddCard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        setQuestionAnswer()
        layAddCard.setBackgroundColor(ContextCompat.getColor(this,R.color.DeepBlue))

        btnAdd.setOnClickListener {
            clickBtnQuestion()
        }
        val list = listOf<String>("QuestAns","MultiChoice","TrueFalse","OneChoice")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.spinner_item, list)
        spTypes.adapter = adapter

        spTypes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(adapterView?.getItemAtPosition(position).toString()){
                    "QuestAns" -> setQuestionAnswer()
                    "MultiChoice" -> null
                    "TrueFalse" -> setTrueFalse()
                    "OneChoice" -> null
                }
            }



            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun setQuestionAnswer(){
        val qa =  QuestionAnswer()
        setFr(qa)
        btnAdd.setOnClickListener{
            clickBtnQuestion()
        }
    }

    fun setTrueFalse(){
        val tf =  TrueFalse()
        setFr(tf)
        btnAdd.setOnClickListener { clickBtnTrueFalse()}
    }

    private fun setFr(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

   private fun clickBtnQuestion(){
        val intent = Intent()

        val question = edQuestion.text.toString().trim()
        val answer = edAnswer.text.toString().trim()
        val category = edCategory.text.toString().trim()
        val title = edTitle.text.toString().trim()

       if (question == "" || answer == "" || category == "" || title == "")
       {
           Toast.makeText(this,"Missing Input Fields",Toast.LENGTH_SHORT).show()
           return
       }


       intent.putExtra("type","QuestionAnswer")
       intent.putExtra("question", question)
       intent.putExtra("answer", answer)
       intent.putExtra("title",title)
       intent.putExtra("category",category)

        setResult(RESULT_OK, intent);
        finish();
    }

   private fun clickBtnTrueFalse(){
       val intent = Intent()
       val category = edCategory.text.toString().trim()
       val title = edTitle.text.toString().trim()
       val statement = edStatement.text.toString().trim()


       if (statement == "" || category == "" || title == "")
       {
           Toast.makeText(this,"Missing Input Fields",Toast.LENGTH_SHORT).show()
           return
       }

       intent.putExtra("type", "TrueFalse")
       intent.putExtra("question", statement)
       intent.putExtra("title",title)
       intent.putExtra("category",category)

        val checked = rdTF.checkedRadioButtonId
        when(checked){
            R.id.True -> intent.putExtra("answer", "True")
            R.id.False -> intent.putExtra("answer", "False")
        }
        setResult(RESULT_OK, intent);
        finish();
    }


}