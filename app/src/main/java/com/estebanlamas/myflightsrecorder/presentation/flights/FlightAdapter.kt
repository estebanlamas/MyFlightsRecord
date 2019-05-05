package com.estebanlamas.myflightsrecorder.presentation.flights

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.estebanlamas.myflightsrecorder.R
import com.estebanlamas.myflightsrecorder.domain.model.Flight
import com.estebanlamas.myflightsrecorder.formatDMMMMYYYY
import kotlinx.android.synthetic.main.item_view_flight.view.*
import java.util.*

class FlightAdapter(
    val flights: List<Flight>,
    val onItemClickListener: (Flight) -> Unit): RecyclerView.Adapter<FlightAdapter.FlightViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_flight, parent,false)
        return FlightViewHolder(view)
    }

    override fun getItemCount() = flights.size

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight = flights[position]
        holder.onBind(flight)
        holder.itemView.setOnClickListener { onItemClickListener(flight) }
    }

    class FlightViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBind(flight: Flight) {
            if(flight.name.isNotBlank()) itemView.textName.text = flight.name
            itemView.textDate.text = Date(flight.id).formatDMMMMYYYY()
        }
    }
}