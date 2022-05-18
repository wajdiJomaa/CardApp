package com.example.cardapp

import android.graphics.Color
import android.icu.text.CaseMap

data class Card(
     val title : String,
     val color : Int,
     val category : String,
     val type : String,
     val answer : String,
     var id : Long?,
     val question : String
)