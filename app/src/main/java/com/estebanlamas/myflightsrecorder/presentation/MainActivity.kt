package com.estebanlamas.myflightsrecorder.presentation

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.estebanlamas.myflightsrecorder.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_LOCATION_CODE = 747

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fabRecord.setOnClickListener {
            when {
                isServiceRunning() -> stopRecord()
                hasLocationPermission() -> startRecord()
                else -> requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
            }
        }

        if(isServiceRunning()){
            fabRecord.setImageResource(R.drawable.ic_stop)
        }else{
            fabRecord.setImageResource(R.drawable.ic_record)
        }
    }

    private fun stopRecord() {
        stopService(RecorderService.getIntent(this))
        fabRecord.setImageResource(R.drawable.ic_record)
    }

    private fun hasLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun startRecord() {
        Snackbar.make(fabRecord, "Recording flight...", Snackbar.LENGTH_LONG).show()
        ContextCompat.startForegroundService(this,
            RecorderService.getIntent(this)
        )
        fabRecord.setImageResource(R.drawable.ic_stop)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_LOCATION_CODE &&
            permissions.size == 1 &&
            permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRecord()
        }
    }

    private fun isServiceRunning(): Boolean {
        val serviceClass = RecorderService::class.java
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
