package com.group20.nutritiontrackingapp.chart

import android.view.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.group20.nutritiontrackingapp.R

class DonutChart (context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val data = listOf(40f, 30f, 30f) // Veri yüzdeleri
        val colors = listOf(ContextCompat.getColor(context, R.color.Pastel_Cream), ContextCompat.getColor(context, R.color.Pastel_Green), ContextCompat.getColor(context, R.color.Pastel_Blue)) // Her dilim için renkler

        val total = data.sum()
        var startAngle = 0f

        // Grafik çizim alanını tanımla
        val rect = RectF(150f, 150f, width - 150f, height - 150f)

        // Stroke genişliğini artırarak kalınlığı değiştir
        val strokeWidth = 170f // Donut kalınlığı
        paint.strokeWidth = strokeWidth

        for (i in data.indices) {
            paint.color = colors[i] // Renk seçimi
            val sweepAngle = (data[i] / total) * 360 // Veri yüzdesine göre açı hesaplama
            canvas.drawArc(rect, startAngle, sweepAngle, false, paint) // Yay çizimi
            startAngle += sweepAngle
        }
    }
}