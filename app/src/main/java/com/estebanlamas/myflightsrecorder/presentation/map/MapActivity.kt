package com.estebanlamas.myflightsrecorder.presentation.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.estebanlamas.myflightsrecorder.R
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_map.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


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
        track.forEachIndexed { index, position ->
            if(index!=track.size) {
                val options = PolylineOptions().width(5f).color(getAltitudeColor(position.altitude.toInt())).geodesic(true)
                options.add(
                    LatLng(position.latitude, position.longitude),
                    LatLng(track[index].latitude, track[index].longitude)
                )
                googleMap?.addPolyline(options)
            }
        }
        googleMap?.animateCamera(zoomCamera(track[0].latitude, track[0].longitude))
        showAltitudeChart(track)
    }

    private fun showAltitudeChart(track: List<PlanePosition>) {
        val chartEntries = arrayListOf<Entry>()

        for (planePosition in track) {
            val time = planePosition.date.time - track[0].date.time
            chartEntries.add(Entry(time.toFloat(), planePosition.altitude.toFloat()))
        }

        val lineDataSet = LineDataSet(chartEntries, "Altitud")
        lineDataSet.color = getColor(R.color.colorPrimaryDark)
        val lineData = LineData(lineDataSet)

        val x = chart.xAxis
        x.valueFormatter = MyXAxisValueFormatter()
        x.position = XAxis.XAxisPosition.BOTTOM

        chart.data = lineData
        chart.invalidate()
    }

    private fun getAltitudeColor(altitude: Int): Int {
        return when(altitude) {
            in 0..50 -> getColor(R.color.color_altitud_0)
            in 51..100 -> getColor(R.color.color_altitud_1)
            in 101..200 -> getColor(R.color.color_altitud_2)
            in 201..400 -> getColor(R.color.color_altitud_3)
            in 401..600 -> getColor(R.color.color_altitud_4)
            in 601..900 -> getColor(R.color.color_altitud_4)
            else -> getColor(R.color.color_altitud_5)
        }
    }

    class MyXAxisValueFormatter: ValueFormatter() {
        val sdf = SimpleDateFormat("mm:ss")
        override fun getFormattedValue(value: Float): String {
            return sdf.format(Date(value.toLong()))
        }
    }
}