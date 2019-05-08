package com.estebanlamas.myflightsrecorder.presentation.map

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.estebanlamas.myflightsrecorder.R
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
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
    private var marker: Marker? = null

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
//        track.forEachIndexed { index, position ->
//            if(index+1!=track.size) {
//                val options = PolylineOptions().width(5f).color(getAltitudeColor(position.altitude.toInt())).geodesic(true)
//                options.add(
//                    LatLng(position.latitude, position.longitude),
//                    LatLng(track[index+1].latitude, track[index+1].longitude)
//                )
//                googleMap?.addPolyline(options)
//            }
//        }
//        googleMap?.animateCamera(zoomCamera(track[0].latitude, track[0].longitude))

        val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
        track.forEach {
            options.add(LatLng(it.latitude, it.longitude))
        }

        googleMap?.run {
            addPolyline(options)
            animateCamera(zoomCamera(track[0].latitude, track[0].longitude))
        }

        showAltitudeChart(track)
    }

    private fun showAltitudeChart(track: List<PlanePosition>) {
        val chartEntries = arrayListOf<Entry>()

        track.forEach { planePosition ->
            val time = planePosition.date.time - track[0].date.time
            chartEntries.add(Entry(time.toFloat(), planePosition.altitude.toFloat()))
        }

        val lineDataSet = LineDataSet(chartEntries, "Altitud")
        lineDataSet.color = getColor(R.color.colorPrimary)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.cubicIntensity = 0.5f
        lineDataSet.fillColor = getColor(R.color.colorPrimary)
        lineDataSet.fillAlpha = 100
        lineDataSet.setDrawFilled(true)

        val lineData = LineData(lineDataSet)

        val x = chart.xAxis
        x.valueFormatter = MyXAxisValueFormatter()
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawGridLines(false)

        chart.data = lineData
        chart.invalidate()

        chart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener{
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry, h: Highlight?) {
                val d =e.x.toLong()+track[0].date.time
                val position = track.find { d == it.date.time }
                if(position!=null) {
                    marker?.remove()
                    marker = googleMap?.addMarker(create(position))
                }
            }
        })
    }

    fun create(planePosition: PlanePosition): MarkerOptions {
        val latLon = LatLng(planePosition.latitude, planePosition.longitude)
        return MarkerOptions()
            .position(latLon)
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
        val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())
        override fun getFormattedValue(value: Float): String {
            return sdf.format(Date(value.toLong()))
        }
    }
}