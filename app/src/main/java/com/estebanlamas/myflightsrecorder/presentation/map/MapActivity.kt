package com.estebanlamas.myflightsrecorder.presentation.map

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.estebanlamas.myflightsrecorder.R
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.domain.model.PlanePosition
import com.estebanlamas.myflightsrecorder.presentation.utils.EditableDialog
import com.estebanlamas.myflightsrecorder.presentation.utils.MyXAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import org.koin.android.ext.android.inject


class MapActivity: AppCompatActivity(), OnMapReadyCallback, MapView, OnChartValueSelectedListener {

    companion object {
        private const val DEFAULT_ZOOM = 10.0f
        private const val EXTRA_FLIGHT = "com.estebanlamas.myflightsrecorder.presentation.map.extra.flight"
        private const val ICON_ROTATION = 45

        fun getIntent(flight: Flight, context: Context): Intent {
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra(EXTRA_FLIGHT, flight)
            return intent
        }
    }

    private var googleMap: GoogleMap? = null
    private val presenter: MapPresenter by inject()
    private var marker: Marker? = null
    private lateinit var icon: BitmapDescriptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        setupMapFragment()
        initIcon()
        presenter.attacheView(this)
        presenter.requestPlanePositions(getFlightExtra())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showDeleteFlightDialog()
                true
            }
            R.id.action_edit -> {
                presenter.onClickEdit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initIcon() {
        val vectorDrawable = ContextCompat.getDrawable(this, R.drawable.ic_airplane)
        vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        icon = BitmapDescriptorFactory.fromBitmap(bitmap)
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

    private fun showDeleteFlightDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.confirm_remove_flight))
        builder.setPositiveButton(android.R.string.yes){ _, _ ->
            presenter.removeFlight()
        }
        builder.setNegativeButton(android.R.string.no){ dialog,_ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    // region MapView

    override fun showTrack(track: List<PlanePosition>) {
        // create polyline
        val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
        track.forEach { options.add(LatLng(it.latitude, it.longitude)) }

        // draw track on map and animate it
        googleMap?.run {
            addPolyline(options)
            animateCamera(zoomCamera(track[0].latitude, track[0].longitude))
        }
    }

    override fun showAltitudeChart(track: List<PlanePosition>) {
        val chartEntries = arrayListOf<Entry>()

        track.forEach { planePosition ->
            val time = planePosition.date.time - track[0].date.time
            chartEntries.add(Entry(time.toFloat(), planePosition.altitude.toFloat()))
        }

        val lineDataSet = LineDataSet(chartEntries, getString(R.string.altitude))
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
        chart.setOnChartValueSelectedListener(this)
    }

    override fun showPlanePostion(planePosition: PlanePosition, heading: Float) {
        marker?.remove()
        marker = googleMap?.addMarker(createMarker(planePosition, heading))
    }

    private fun createMarker(planePosition: PlanePosition, heading: Float): MarkerOptions {
        return MarkerOptions()
            .icon(icon)
            .position(LatLng(planePosition.latitude, planePosition.longitude))
            .anchor(0.5f, 0.5f)
            .rotation(heading - ICON_ROTATION)
    }

    override fun showChangeNameDialog(name: String) {
        val editableDialog = EditableDialog(name, presenter)
        editableDialog.show(supportFragmentManager,"")
    }

    override fun setToolbar(nameFlight: String) {
        title = nameFlight
    }

    override fun flightRemoved() {
        finish()
    }

    // endregion

    // region OnChartValueSelectedListener

    override fun onNothingSelected() {
    }

    override fun onValueSelected(entry: Entry, h: Highlight?) {
        presenter.onSelectTime(entry.x.toLong())
    }

    // endregion
}