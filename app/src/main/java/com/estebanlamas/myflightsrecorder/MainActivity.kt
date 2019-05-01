package com.estebanlamas.myflightsrecorder

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fabRecord.setOnClickListener { view ->
            if(isServiceRunning(RecorderService::class.java)) {
                stopService(RecorderService.getIntent(this))
                fabRecord.setImageResource(R.drawable.ic_record)
            }else{
                Snackbar.make(view, "Recording flight...", Snackbar.LENGTH_LONG).show()
                ContextCompat.startForegroundService(this, RecorderService.getIntent(this))
                fabRecord.setImageResource(R.drawable.ic_stop)
            }
        }

        if(isServiceRunning(RecorderService::class.java)){
            fabRecord.setImageResource(R.drawable.ic_stop)
        }else{
            fabRecord.setImageResource(R.drawable.ic_record)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
