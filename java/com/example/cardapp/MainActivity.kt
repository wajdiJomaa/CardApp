package com.example.cardapp

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.google.android.material.tabs.TabLayoutMediator
import com.opencsv.CSVReader
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    var colors = listOf(R.color.Beige1,R.color.BlueViolet1,R.color.OrangeYellow1,
        R.color.LightRed)

    var quizcolors = listOf(R.color.darkGreen,R.color.BlueViolet2,
        R.color.OrangeYellow2,R.color.Beige2)

    var colorCounter = 0
    var quizcolorCounter = 0



    var quizlist = mutableListOf<Quiz>()
    val  quizAdapter = QuizAdapter(quizlist)

    var cardList = mutableListOf<Card>()
    val  cardAdapter = CardAdapter(cardList)

    val dbHelper = FeedReaderDbHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layMain.setBackgroundColor(ContextCompat.getColor(this,R.color.DeepBlue))

        quizlist = dbHelper.retrieveDataQuiz()
        quizAdapter.setCardls(quizlist)

        quizAdapter.setOnClick { i ->
            val intent = Intent(this,TakeQuiz::class.java)
            intent.putExtra("id",quizlist[i].id)
            startActivity(intent)
        }


        quizAdapter.setOnClickItem { quiz, i ->
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete the question?")
            builder.setCancelable(true)
            builder.setPositiveButton("Yes"){dialog, _ ->
                dbHelper.deleteQuiz(quiz)

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



        quizAdapter.setOnClickAdd{ quiz, i ->
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Enter Card Title")
            builder.setCancelable(true)


            val viewInflated: View = layoutInflater.inflate(R.layout.builder_input,null,false)

            val input = viewInflated.findViewById<EditText>(R.id.edCardTitle)

            builder.setView(viewInflated)


            builder.setPositiveButton("Add"){dialog, _ ->

                val id = dbHelper.getCard(input.text.toString())
                if (id == null){
                    Toast.makeText(this, "No Such Card", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    dbHelper.AddQuizCard(quiz,id)
                }
            }

            builder.setNegativeButton("Cancel"){dialog ,_ ->
                dialog.dismiss()}

            val alert = builder.create()
            alert.show()
        }


        cardList = dbHelper.retrieveData()
        cardAdapter.setCardls(cardList)


        cardAdapter.setOnClickItem { card, i ->
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete the question?")
            builder.setCancelable(true)
            builder.setPositiveButton("Yes"){dialog, _ ->
                dbHelper.deleteCard(card)

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


        val adapter = ViewPagerAdapter(listOf(quizAdapter,cardAdapter))
        viewpager.adapter = adapter

        TabLayoutMediator(tablayout,viewpager){ tab,pos ->
            if (pos == 0) {
                tab.text = "Quizs"
            }
            else{
                tab.text = "Cards"
            }
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addCard){
        val switchActivityIntent = Intent(this, AddCard::class.java)
        startActivityForResult(switchActivityIntent, 1);
        }
        if (item.itemId == R.id.addQuiz)
        {
            val switchActivityIntent = Intent(this, AddQuiz::class.java)
            startActivityForResult(switchActivityIntent, 2)
        }
        if (item.itemId == R.id.exportQuiz)
        {
            exportData()
        }

        if (item.itemId == R.id.importQuiz)
        {
            importFile()
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
//                question_fragment.add(card)
                cardList.add(card)
                cardAdapter . notifyItemInserted (cardList.size - 1)

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
//                quiz_fragment.add(quiz)
                quizlist.add(quiz)
                quizAdapter.notifyItemInserted(quizlist.size - 1)
            }
        }

        if (requestCode == 3){
            if (resultCode == RESULT_OK)
            {
                if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()))
                {
                    println(true)
                }



//                val file = data?.data?.toFile()
                val file = File("file://com.android.externalstorage.documents/document/primary:Android/data/com.example.cardapp/files/math.csv")
                file?.forEachLine {
                    val items = it.split(",")
                    println(items)
                }


            }
        }
    }
    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }


    fun exportData(){

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Enter QUIZ Title")
            builder.setCancelable(true)


            val viewInflated: View = layoutInflater.inflate(R.layout.builder_input,null,false)

            val input = viewInflated.findViewById<EditText>(R.id.edCardTitle)

            builder.setView(viewInflated)


            builder.setPositiveButton("export"){dialog, _ ->
                val HEADER = "title,type,category,question,answer"

                val id = dbHelper.getQuiz(input.text.toString())
                if (id == null)
                {
                    Toast.makeText(this,"invalid Quiz",Toast.LENGTH_SHORT).show()
                }
                else {
                    val arrofCards = dbHelper.getQuestofQuiz(id!!)

                    if (arrofCards.isEmpty()) {
                        Toast.makeText(this, "Empty Quiz", Toast.LENGTH_SHORT).show()

                    } else {

                        var filename = "${input.text}.csv"

                        var path = getExternalFilesDir(null)   //get file directory for this package
                        //(Android/data/.../files | ... is your app package)

                        //create fileOut object
                        var fileOut = File(path, filename)

                        //delete any file object with path and filename that already exists
                        fileOut.delete()

                        //create a new file
                        fileOut.createNewFile()

                        //append the header and a newline
                        fileOut.appendText(HEADER)
                        fileOut.appendText("\n")
                        // trying to append some data into csv file
                        for (c in arrofCards) {
                            fileOut.appendText("${c.title},${c.type},${c.category},${c.question},${c.answer}")
                            fileOut.appendText("\n")
                        }


                        val sendIntent = Intent(Intent.ACTION_SEND)
                        val uri =
                            FileProvider.getUriForFile(this, "com.mydomain.fileprovider", fileOut)
                        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
                        sendIntent.type = "text/csv"

                        startActivity(Intent.createChooser(sendIntent, "SHARE"))
                    }
                }
            }


            builder.setNegativeButton("Cancel"){dialog ,_ ->
            dialog.dismiss()}

            val alert = builder.create()
            alert.show()
    }

    fun importFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 3)
    }


}