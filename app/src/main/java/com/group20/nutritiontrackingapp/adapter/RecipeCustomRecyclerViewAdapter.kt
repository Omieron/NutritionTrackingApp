package com.group20.nutritiontrackingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.group20.nutritiontrackingapp.R
import com.group20.nutritiontrackingapp.RecipeActivity
import com.group20.nutritiontrackingapp.db.Recipe

class RecipeCustomRecyclerViewAdapter(
    private val context: Context,
    private val recyclerItemValues: MutableList<Recipe>
) : RecyclerView.Adapter<RecipeCustomRecyclerViewAdapter.RecyclerViewItemHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerViewItemHolder {
        val inflator = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflator.inflate(R.layout.recipe_item, viewGroup, false)
        return RecyclerViewItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewItemHolder, position: Int) {
        val currentItem = recyclerItemValues[position]

        // Assign
        holder.recipeTitle.text = currentItem.title
        holder.recipeImage.setImageResource(currentItem.imgId)

        // Listeners
        holder.itemView.setOnClickListener {
            val intent = Intent(context, RecipeActivity::class.java).apply {
                putExtra("recipe_id", currentItem.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recyclerItemValues.size

    inner class RecyclerViewItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
    }
}
