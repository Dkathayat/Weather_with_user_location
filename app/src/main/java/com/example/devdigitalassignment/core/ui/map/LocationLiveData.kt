package com.example.devdigitalassignment.core.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*

class LocationLiveData(context: Context) : LiveData<LiveLocation>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onInactive() {
        super.onInactive()
        // turn off location updates
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
            location.also {
                setLocationData(it)
            }
        }
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult != null) {
                super.onLocationResult(locationResult)
            }

            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    /**
     * If we've received a location update, this function will be called.
     */
    private fun setLocationData(location: Location) {
        if (location != null) {
            value = LiveLocation(location.latitude,location.longitude)
        }
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            .setMaxUpdateDelayMillis(1000)
            .build()

    }

}