package com.linden.learningkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*

class CardAdapter(val habits: List<Habit>): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        if (habits.isNotEmpty()) {
            val habit = habits.get(position)
            holder.iv.tvTitle.text = habit.title
            holder.iv.tvDescription.text = habit.description
            holder.iv.ivIcon.setImageBitmap(habit.image)
        }
    }

    class CardViewHolder(val iv: View): RecyclerView.ViewHolder(iv)

}