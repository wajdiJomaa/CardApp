package com.example.cardapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_view.view.*

class CardAdapter(var cards: List<Card>
):RecyclerView.Adapter<CardAdapter.CardViewHolder>(){
    private var onClickItem: ((Card,Int) -> Unit)? = null

    inner class CardViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val btnDelete = itemView.btnDelete
    }


    fun setOnClickItem(callback:(Card,Int)->Unit){
        this.onClickItem = callback
    }

    fun setCardls(ls : MutableList<Card>) : MutableList<Card>{
        cards = ls
        return ls
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view,parent,
            false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.itemView.apply {

            layCard.background.setTint(ContextCompat.getColor(context,cards[position].color))
            tvTitle.text = cards[position].title
            tvSubTitle.text = "${cards[position].type} - ${cards[position].category}"

            holder.btnDelete.setOnClickListener {  onClickItem?.invoke(cards[position],position)  }
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}
