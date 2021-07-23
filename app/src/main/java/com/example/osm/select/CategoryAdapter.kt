package com.example.osm.select

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.osm.databinding.SelectRecyclerItemBinding

class CategoryAdapter(context: Context, private val dataList : ArrayList<CategoryData>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val context = context
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var binding : SelectRecyclerItemBinding

    class ViewHolder(private val binding : SelectRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        val main = binding.contentLayout
        val title = binding.text
        val image = binding.image
        val lockDisplay = binding.lock
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = SelectRecyclerItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataList[position].title
        holder.image.setImageResource(dataList[position].image)
        holder.lockDisplay.isVisible = dataList[position].isLock
        holder.main.setOnClickListener {
            if (!dataList[position].isLock) {
                (context as SelectActivity).goToGame()
            } else {
                Log.d("selectActivity", " is locked! ")
            }
        }
    }

    override fun getItemCount(): Int = dataList.size
}