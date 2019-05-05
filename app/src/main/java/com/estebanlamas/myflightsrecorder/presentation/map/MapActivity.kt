package com.estebanlamas.myflightsrecorder.presentation.map

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.estebanlamas.myflightsrecorder.R
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.koin.android.ext.android.inject

class MapActivity: AppCompatActivity(), OnMapReadyCallback, MapView {

    companion object {
        private const val DEFAULT_ZOOM = 10.0f
        private const val EXTRA_FLIGHT = "com.estebanlamas.myflightsrecorder.presentation.map.extra.flight"

        fun getIntent(flight: Flight, context: Context): Intent {
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra(EXTRA_FLIGHT, flight)
            return intent
        }
    }

    private var googleMap: GoogleMap? = null
    private val presenter: MapPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        setupMapFragment()
        presenter.attacheView(this)
        presenter.requestPlanePositions(getFlightExtra())
    }

    private fun getFlightExtra(): Flight {
        return intent.getSerializableExtra(EXTRA_FLIGHT) as Flight
    }

    private fun setupMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    private fun zoomCamera(lat: Double, lon: Double): CameraUpdate {
        return CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), DEFAULT_ZOOM)
    }

    override fun showTrack(track: List<PlanePosition>) {
        val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
        track.forEach {
            options.add(LatLng(it.latitude, it.longitude))
        }

        googleMap?.run {
            addPolyline(options)
            animateCamera(zoomCamera(track[0].latitude, track[0].longitude))
        }
    }
}