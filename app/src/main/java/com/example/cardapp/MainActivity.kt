package com.example.cardapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var colors = listOf(R.color.Beige1,R.color.BlueViolet1,R.color.OrangeYellow1,
        R.color.LightRed)

    var quizcolors = listOf(R.color.darkGreen,R.color.BlueViolet2,
        R.color.OrangeYellow2,R.color.Beige2)

    var colorCounter = 0
    var quizcolorCounter = 0





    val dbHelper = FeedReaderDbHelper(this)

    val question_fragment = question_fragment()
    val quiz_fragment = fragment_quiz()
    var fragment_selected = "quiz"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layMain.setBackgroundColor(ContextCompat.getColor(this,R.color.DeepBlue))

        question_fragment.setAdapter(dbHelper.retrieveData())
        quiz_fragment.setAdapter(dbHelper.retrieveDataQuiz())

        setFr(quiz_fragment)

        question_fragment.delete {
            dbHelper.deleteCard(it)
        }

        quiz_fragment.delete {
            dbHelper.deleteQuiz(it)
        }


        quiz_fragment.add (this::addCardQuiz,this::retreiveCard)


        toggle.setOnCheckedChangeListener { _, i ->
//            val checked = findViewById<RadioButton>(i).text

            when(i){
                R.id.question -> {setFr(question_fragment)
                                  fragment_selected = "quest"}
                R.id.quiz -> {setFr(quiz_fragment)
                                fragment_selected = "quiz" }
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (fragment_selected == "quest"){
        val switchActivityIntent = Intent(this, AddCard::class.java)
        startActivityForResult(switchActivityIntent, 1);}
        else{
            add_quiz()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1)
        {
            if (resultCode== RESULT_OK){
                val type = data?.getStringExtra("type").toString()
                val title = data?.getStringExtra("title").toString()
                val category = data?.getStringExtra("category").toString()

                if (colorCounter == colors.size){colorCounter = 0}
                val color = colors[colorCounter++]
                val question = data?.getStringExtra("question").toString()
                val answer = data?.getStringExtra("answer").toString()
                var card = Card(title,color,category,type,answer,0,question)
                var id = dbHelper.dbAdd(card)
                println(id)
                card.id = id
                question_fragment.add(card)
            }
        }
        if (requestCode == 2)
        {
            if (resultCode == RESULT_OK){
                val title = data?.getStringExtra("title").toString()
                val description = data?.getStringExtra("description").toString()
                if (quizcolorCounter == quizcolors.size){quizcolorCounter = 0}
                val color = quizcolors[quizcolorCounter++]
                var quiz = Quiz(title,0,color,description)
                val id = dbHelper.dbAddQuiz(quiz)
                quiz.id = id
                quiz_fragment.add(quiz)
            }
        }
    }




    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    private fun setFr(fragment: Fragment){

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment2, fragment)
            commit()
        }
    }

    fun add_quiz(){ val switchActivityIntent = Intent(this, AddQuiz::class.java)
        startActivityForResult(switchActivityIntent, 2)
    }

    fun addCardQuiz(q:Quiz,c:Long?){
        dbHelper.AddQuizCard(q,c)
    }

    fun retreiveCard(title:String) : Long?
    {
       return dbHelper.getCard(title)
    }
}