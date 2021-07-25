package com.example.osm.result

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.osm.databinding.ScoreRecyclerItemBinding

class scoreRecyclerAdapter(context: Context, private val dataList : ArrayList<Int>) : RecyclerView.Adapter<scoreRecyclerAdapter.ViewHolder>() {

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var binding : ScoreRecyclerItemBinding

    class ViewHolder(private val binding : ScoreRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val number = binding.number
        val score = binding.score
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ScoreRecyclerItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.number.text = (position + 1).toString() + "."
        holder.score.text = dataList[position].toString()
    }

    override fun getItemCount(): Int = dataList.size
}