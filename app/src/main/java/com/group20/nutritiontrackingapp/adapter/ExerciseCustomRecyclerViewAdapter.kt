package com.group20.nutritiontrackingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.group20.nutritiontrackingapp.R
import com.group20.nutritiontrackingapp.db.Exercise

class ExerciseCustomRecyclerViewAdapter(
    private val context: Context,
    private val exercises: MutableList<Exercise>,
    private var exercisesFull: List<Exercise> = ArrayList(exercises)

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Interface
    interface ExerciseAdapterInterface {
        fun displayExercise(exercise: Exercise)
        fun displayExercises(exercises: MutableList<Exercise>)
    }

    lateinit var adapterInterface: ExerciseAdapterInterface

    init {
        adapterInterface = context as ExerciseAdapterInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = when (viewType) {
            0 -> R.layout.light_exercise_item
            1 -> R.layout.moderate_exercise_item
            else -> R.layout.intense_exercise_item
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentExercise = exercises[position]
        val exerciseHolder = holder as ExerciseViewHolder

        // Bind data
        exerciseHolder.exerciseName.text = currentExercise.name
        exerciseHolder.exerciseCalories.text = "${currentExercise.caloriesPerHour} cal/hr"

        // Set click listener
        holder.itemView.setOnClickListener {
            adapterInterface.displayExercise(currentExercise)
        }
    }

    override fun getItemCount(): Int = exercises.size

    fun filter(searchText: String) {
        val query = searchText.lowercase().trim()
        exercises.clear()

        if (query.isEmpty()) {
            exercises.addAll(exercisesFull)
        } else {
            exercisesFull.forEach { exercise ->
                if (exercise.name.lowercase().contains(query) ||
                    exercise.category.lowercase().contains(query) ||
                    exercise.description.lowercase().contains(query)
                ) {
                    exercises.add(exercise)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (exercises[position].category.lowercase()) {
            "light" -> 0
            "moderate" -> 1
            "intense" -> 2
            else -> 0
        }
    }

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        val exerciseCalories: TextView = itemView.findViewById(R.id.exerciseCalories)
    }
}