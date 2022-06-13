package com.example.cardapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE Cards(id INTEGER PRIMARY KEY," +
                "type TEXT," +
                "title TEXT," +
                "category TEXT," +
                "question TEXT," +
                "answer TEXT," +
                "color INTEGER )"

    private val SQL_CREATE_ENTRIES2 =
        "CREATE TABLE Quiz(id INTEGER PRIMARY KEY," +
                "title TEXT," +
                "description TEXT," +
                "color INTEGER )"

    private val RelationCardQuiz =
        "CREATE TABLE CQ(id INTEGER PRIMARY KEY," +
                "quiz_id Integer," +
                "card_id Integer," +
                "FOREIGN KEY(quiz_id) REFERENCES Quiz(id)," +
                "FOREIGN KEY(card_id) REFERENCES Cards(id))"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS Cards"
    private val SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS Quiz"
    private val SQL_DELETE_ENTRIES3 = "DROP TABLE IF EXISTS CQ"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES2)
        db.execSQL(RelationCardQuiz)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_DELETE_ENTRIES2)
        db.execSQL(SQL_DELETE_ENTRIES3)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)

    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 6
        const val DATABASE_NAME = "Card.db"
    }

    fun deleteCard(c: Card): Int {
        val db = this.writableDatabase
        db.delete("CQ", "card_id = ?", arrayOf(c.id.toString()))
        val success = db.delete(
            "Cards", "id = ?",
            arrayOf(c.id.toString())
        )
        return success
    }
//        val contentValues = ContentValues()

//        contentValues.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Question,c.title)
//        contentValues.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Color,c.color)
//
//        val success = db.update(FeedReaderContract.FeedEntry.TABLE_NAME,contentValues,
//            "color=" + c.color,null)
//        db.close()
//
//        return success
//}

    fun retrieveData(): MutableList<Card> {
        val db = this.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
//        val projection = arrayOf(null)

        // Filter results WHERE "title" = 'My Title'
//        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_Question} = ?"
//        val selectionArgs = arrayOf("My Title")

        // How you want the results sorted in the resulting Cursor
//        val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_Answer} DESC"

        val cursor = db.query(
            "Cards",   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null             // The sort order
        )

        var cards = mutableListOf<Card>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow("title"))
                val color = getInt(getColumnIndexOrThrow("color"))
                val category = getString(getColumnIndexOrThrow("category"))
                val id = getLong(getColumnIndexOrThrow("id"))
                val type = getString(getColumnIndexOrThrow("type"))
                val answer = getString(getColumnIndexOrThrow("answer"))
                val question = getString(getColumnIndexOrThrow("question"))

                cards.add(Card(title, color, category, type, answer, id, question))
            }
        }
        cursor.close()
        return cards
    }

    fun dbAdd(card: Card): Long? {
        val db = this.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put("question", card.question)
            put("answer", card.answer)
            put("color", card.color)
            put("type", card.type)
            put("category", card.category)
            put("title", card.title)
        }
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert("Cards", null, values)

        return newRowId
    }


    fun dbAddQuiz(q: Quiz): Long? {
        val db = this.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put("description", q.description)
            put("color", q.color)
            put("title", q.title)
        }
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert("Quiz", null, values)

        return newRowId
    }


    fun retrieveDataQuiz(): MutableList<Quiz> {
        val db = this.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
//        val projection = arrayOf(null)

        // Filter results WHERE "title" = 'My Title'
//        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_Question} = ?"
//        val selectionArgs = arrayOf("My Title")

        // How you want the results sorted in the resulting Cursor
//        val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_Answer} DESC"

        val cursor = db.query(
            "Quiz",   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null             // The sort order
        )

        var quizs = mutableListOf<Quiz>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow("title"))
                val color = getInt(getColumnIndexOrThrow("color"))
                val description = getString(getColumnIndexOrThrow("description"))
                val id = getLong(getColumnIndexOrThrow("id"))

                quizs.add(Quiz(title, id, color, description))
            }
        }
        cursor.close()
        return quizs
    }

    fun deleteQuiz(q: Quiz): Int {
        val db = this.writableDatabase
        db.delete("CQ", "quiz_id = ?", arrayOf(q.id.toString()))
        val success = db.delete(
            "Quiz", "id = ?",
            arrayOf(q.id.toString())
        )
        return success
    }

    fun AddQuizCard(q: Quiz, c: Long?): Long? {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("quiz_id", q.id)
            put("card_id", c)
        }
        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert("CQ", null, values)
        println(newRowId)

        return newRowId
    }


    fun getCard(title: String): Long? {
        val db = this.readableDatabase
        val cursor = db.query(
            "Cards",   // The table to query
            arrayOf("id"),             // The array of columns to return (pass null to get all)
            "title = ?",              // The columns for the WHERE clause
            arrayOf(title),          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null             // The sort order
        )
        var id: Long? = null
        with(cursor) {
            while (moveToNext()) {
                id = getLong(getColumnIndexOrThrow("id"))
            }
        }
        cursor.close()
        return id
    }

    fun getQuestofQuiz(quiz: Long): MutableList<Card> {
        val db = this.readableDatabase
        val cards = mutableListOf<Card>()

        val cursor = db.rawQuery(
            "Select * from Cards where id in (select card_id from CQ where " +
                    "quiz_id = ?)", arrayOf(quiz.toString())
        )
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow("title"))
                val color = getInt(getColumnIndexOrThrow("color"))
                val category = getString(getColumnIndexOrThrow("category"))
                val id = getLong(getColumnIndexOrThrow("id"))
                val type = getString(getColumnIndexOrThrow("type"))
                val answer = getString(getColumnIndexOrThrow("answer"))
                val question = getString(getColumnIndexOrThrow("question"))

                cards.add(Card(title, color, category, type, answer, id, question))
            }
        }
        cursor.close()
        return cards
    }

    fun getQuiz(title: String): Long? {
        val db = this.readableDatabase
        val cursor = db.query(
            "Quiz",   // The table to query
            arrayOf("id"),             // The array of columns to return (pass null to get all)
            "title = ?",              // The columns for the WHERE clause
            arrayOf(title),          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null             // The sort order
        )
        var id: Long? = null
        with(cursor) {
            while (moveToNext()) {
                id = getLong(getColumnIndexOrThrow("id"))
            }
        }
        cursor.close()
        return id
    }
}