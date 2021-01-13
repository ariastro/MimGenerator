package com.astronout.testalgostudio.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.astronout.testalgostudio.databinding.ItemMemeBinding
import com.astronout.testalgostudio.ui.main.model.Meme
import java.util.ArrayList

class MainAdapter(private val onClickListener: OnClickListener) : ListAdapter<Meme, MainAdapter.MainViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder.from(parent)

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val meme = getItem(position)
        holder.bind(meme)

        holder.binding.content.setOnClickListener {
            if (meme != null) {
                onClickListener.onClick(meme)
            }
        }

    }

    class MainViewHolder(val binding: ItemMemeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(meme: Meme) {
            binding.itemMeme = meme
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MainViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMemeBinding.inflate(layoutInflater, parent, false)
                return MainViewHolder(binding)
            }
        }
    }

    class OnClickListener(val clickListener: (meme: Meme) -> Unit) {
        fun onClick(meme: Meme) = clickListener(meme) }

    private companion object DiffCallback : DiffUtil.ItemCallback<Meme>() {

        override fun areItemsTheSame(oldItem: Meme, newItem: Meme): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Meme, newItem: Meme): Boolean {
            return oldItem.id == newItem.id
        }

    }

}