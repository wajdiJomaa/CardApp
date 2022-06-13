package com.example.cardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_add_card.*
import kotlinx.android.synthetic.main.activity_add_quiz.*
import kotlinx.android.synthetic.main.fragment_question_answer.*

class AddQuiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quiz)
        layaddQuiz.setBackgroundColor(ContextCompat.getColor(this,R.color.DeepBlue))


        btnAdd2.setOnClickListener{
            val intent = Intent()

            val title = edQuizTitle.text.toString().trim()
            val descrition = edQuizDescription.text.toString().trim()


            if (title == "" || descrition == "")
            {
                Toast.makeText(this,"Missing Input Fields", Toast.LENGTH_SHORT).show()
            }
            else{

            intent.putExtra("title", title)
            intent.putExtra("description", descrition)

            setResult(RESULT_OK, intent);
            finish();}
        }

    }
}