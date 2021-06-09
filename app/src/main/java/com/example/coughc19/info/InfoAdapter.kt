package com.example.coughc19.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coughc19.R
import com.example.coughc19.databinding.ItemInfoBinding

class InfoAdapter(private val infoList: List<InfoEntity>) : RecyclerView.Adapter<InfoAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemInfoBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = infoList[position]

        Glide.with(holder.itemView.context)
            .load(user.imageButton)
            .into(holder.binding.btnArrow)
        holder.binding.questionFaq.text = user.question_faq
        holder.binding.answerFaq.text = user.answer_faq

        val isExpandable : Boolean = user.experdable
        val imageButton : Boolean = user.imageButton
        holder.binding.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.binding.btnArrow.visibility = if (imageButton) View.GONE else View.VISIBLE

        holder.binding.info.setOnClickListener{
            user.experdable = !user.experdable
            user.imageButton = !user. imageButton
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return infoList.size
    }
}