package com.estebanlamas.myflightsrecorder.presentation.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class MyXAxisValueFormatter: ValueFormatter() {
    private val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())
    override fun getFormattedValue(value: Float): String {
        return sdf.format(Date(value.toLong()))
    }
}