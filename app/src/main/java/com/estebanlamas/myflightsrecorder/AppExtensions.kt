package com.estebanlamas.myflightsrecorder

import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

fun versionIs26OrBigger() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

fun Date.formatDMMMMYYYY(): String {
    val simpleDateFormat = SimpleDateFormat("d MMMM YYYY")
    return simpleDateFormat.format(this)
}