package com.answere2022.mbtitalk

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CardItemAdapter: ListAdapter<CardItem, CardItemAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(private  val view: View): RecyclerView.ViewHolder(view){

            fun bind(cardItem: CardItem){
                view.findViewById<TextView>(R.id.nameTextView).text = cardItem.name

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.id.item_card,parent,false))

        //뷰바인딩에서 하는거 공부하기
        //layout에 있는 아이템카드 왜못가져오지
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<CardItem>(){
            override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {

                return oldItem.userId == newItem.userId
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
                 return oldItem == newItem
            }

        }


    }
}