package com.example.freshmate.data.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmate.R
import com.example.freshmate.data.response.Tips

class TipsAdapter (private val tipsList: List<Tips>) :
    RecyclerView.Adapter<TipsAdapter.TipViewHolder>() {

    class TipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textTitle)
        val description: TextView = itemView.findViewById(R.id.textDescription)
        val caution: TextView = itemView.findViewById(R.id.textCaution)
        val image: ImageView = itemView.findViewById(R.id.imageTip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tips, parent, false)
        return TipViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val tip = tipsList[position]
        holder.title.text = tip.title
        holder.description.text = "Tips: ${tip.description}"
        holder.caution.text = "Hindari: ${tip.caution}"
        holder.image.setImageResource(tip.imageRes) // Set the image
    }

    override fun getItemCount(): Int = tipsList.size
}