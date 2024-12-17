package com.group20.nutritiontrackingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.group20.nutritiontrackingapp.R
import com.group20.nutritiontrackingapp.db.AppDatabase
import com.group20.nutritiontrackingapp.db.Meal

class MealCustomRecyclerViewAdapter(
    private val context: Context,
    private var mealItems: MutableList<Meal>,
    private val db: AppDatabase
) : RecyclerView.Adapter<MealCustomRecyclerViewAdapter.MealViewHolder>() {

    private var selectedPosition = -1
    private var selectedMeal: Meal? = null

    fun getSelectedMeal(): Meal? = selectedMeal

    fun setData(items: MutableList<Meal>) {
        mealItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MealViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.meal_item, viewGroup, false)
        return MealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val currentMeal = mealItems[position]

        // Assign values
        holder.apply {
            mealName.text = currentMeal.name
            servingSize.text = "Serving : " + currentMeal.servingSize.toInt().toString()
            calories.text = currentMeal.calories.toString()
            protein.text = String.format("%.1fg", currentMeal.protein)
            carbs.text = String.format("%.1fg", currentMeal.carbs)
            fat.text = String.format("%.1fg", currentMeal.fat)
        }

        // Change background based on selection
        holder.itemView.setBackgroundResource(
            if (position == selectedPosition) R.color.Pastel_Orange
            else R.color.Soft_Eggshell_White
        )

        // When item is clicked, update selection
        holder.itemView.setOnClickListener {
            val newPosition = holder.adapterPosition
            if (newPosition != RecyclerView.NO_POSITION) {  // Check if position is valid
                val previousSelected = selectedPosition
                selectedPosition = newPosition
                selectedMeal = currentMeal

                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)

                Toast.makeText(
                    context,
                    "${currentMeal.name} selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun getItemCount(): Int = mealItems.size

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealName: TextView = itemView.findViewById(R.id.tvMealName)
        val servingSize: TextView = itemView.findViewById(R.id.tvServingSize)
        val calories: TextView = itemView.findViewById(R.id.tvCalories)
        val protein: TextView = itemView.findViewById(R.id.tvProtein)
        val carbs: TextView = itemView.findViewById(R.id.tvCarbs)
        val fat: TextView = itemView.findViewById(R.id.tvFat)
    }
}