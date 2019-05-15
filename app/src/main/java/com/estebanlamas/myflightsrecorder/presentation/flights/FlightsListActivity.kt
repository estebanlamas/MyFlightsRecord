package com.estebanlamas.myflightsrecorder.presentation.flights

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.estebanlamas.myflightsrecorder.R
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.presentation.RecorderService
import com.estebanlamas.myflightsrecorder.presentation.map.MapActivity
import com.estebanlamas.myflightsrecorder.presentation.utils.GpsUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.ext.android.inject


class FlightsListActivity : AppCompatActivity(), FlightsListView {

    companion object {
        private const val REQUEST_LOCATION_CODE = 747
    }

    private val presenter: FlightsListPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fabRecord.setOnClickListener {
            when {
                isServiceRunning() -> stopRecord()
                hasLocationPermission() -> checkGps()
                else -> requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
            }
        }

        if(isServiceRunning()){
            fabRecord.setImageResource(R.drawable.ic_stop)
        }else{
            fabRecord.setImageResource(R.drawable.ic_record)
        }

        presenter.attacheView(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.getFlights()
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
            checkGps()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GpsUtils.LOCATION_REQUEST) {
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

    // private methods

    private fun stopRecord() {
        stopService(RecorderService.getIntent(this))
        fabRecord.setImageResource(R.drawable.ic_record)
    }

    private fun hasLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun checkGps() {
        GpsUtils.turnGPSOn(this, object: GpsUtils.OnGpsListener{
            override fun gpsStatus(isGPSEnable: Boolean) {
                startRecord()
            }
        })
    }

    private fun startRecord() {
        Snackbar.make(fabRecord, getString(R.string.recording_flight), Snackbar.LENGTH_LONG).show()
        ContextCompat.startForegroundService(this, RecorderService.getIntent(this))
        fabRecord.setImageResource(R.drawable.ic_stop)
    }

    // endregion


    // region FlightsListView

    override fun showFlights(flights: List<Flight>) {
        recyclerViewFlights.layoutManager = LinearLayoutManager(this)
        recyclerViewFlights.adapter = FlightAdapter(flights) {
            startActivity(MapActivity.getIntent(it, this@FlightsListActivity))
        }
    }

    // endregion
}
