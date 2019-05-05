package com.estebanlamas.myflightsrecorder.presentation.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.estebanlamas.myflightsrecorder.R
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng

class MapActivity: AppCompatActivity(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null

    companion object {
        private const val CENTER_LAT = 40.4322308
        private const val CENTER_LON = -3.674026
        private const val DEFAULT_ZOOM = 9.0f
        private const val EXTRA_FLIGHT = "com.estebanlamas.myflightsrecorder.presentation.map.extra.flight"

        fun getIntent(flight: Flight, context: Context): Intent {
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra(EXTRA_FLIGHT, flight)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        setupMapFragment()
    }

    private fun setupMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.animateCamera(zoomCammera())
    }

    private fun zoomCammera(): CameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(CENTER_LAT, CENTER_LON), DEFAULT_ZOOM)
}