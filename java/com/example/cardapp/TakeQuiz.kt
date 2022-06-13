package com.example.cardapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add_card.*
import kotlinx.android.synthetic.main.activity_take_quiz.*
import kotlinx.android.synthetic.main.fragment_quiz.*

class TakeQuiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_quiz)
        takequiz.setBackgroundColor(ContextCompat.getColor(this,R.color.DeepBlue))

        val intent = getIntent()
        val id = intent.getLongExtra("id",0)
        val dbHelper = FeedReaderDbHelper(this)
        var cards = dbHelper.getQuestofQuiz(id)
        val adapter = TakeQuizAdapter(cards)
        rvtk.adapter = adapter
        rvtk.layoutManager = LinearLayoutManager(this)

        btnSubmit.setOnClickListener {

            val score2 = adapter.calculate()
            score.text = "Your score is: $score2"
        }

    }
}