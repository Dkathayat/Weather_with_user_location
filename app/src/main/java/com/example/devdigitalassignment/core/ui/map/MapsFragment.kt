package com.example.devdigitalassignment.core.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.devdigitalassignment.R
import com.example.devdigitalassignment.core.ui.viewmodel.MainSharedViewModel
import com.example.devdigitalassignment.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding by lazy { _binding!! }
    private lateinit var mGoogleMaps: GoogleMap
    private lateinit var liveViewModel: LiveLocationViewModel
    private var defaultLocation = LatLng(28.080371876417185,83.14100984483959)
    private val mainSharedViewModel:MainSharedViewModel by activityViewModels()
    private var location: LatLng?=null


    @SuppressLint("PotentialBehaviorOverride")
    private val callback = OnMapReadyCallback { googleMap ->
        val cardViewHome = requireActivity().findViewById<CardView>(R.id.card_view_home)
        val addressText = requireActivity().findViewById<TextView>(R.id.address_home)

        cardViewHome.isEnabled = false

        mGoogleMaps = googleMap
        getLiveLocationData()
        cardViewHome.visibility = View.VISIBLE
        googleMap.setOnMapClickListener { marker ->
            googleMap.clear()
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                marker.latitude,
                marker.longitude,
                1
            )
            location = marker
            for (i in addresses) {
                try {
                    val address: String =
                        i.getAddressLine(0)
                    val city: String = i.locality
                    if (city.isNullOrBlank()){
                        cardViewHome.visibility = View.GONE
                    } else {
                        cardViewHome.isEnabled = true
                        mainSharedViewModel.setLocationAddress(city)
                    }
                    val state: String = i.adminArea
                    val country: String = i.countryName
                    val postalCode: String = i.postalCode
                    addressText.text = "$address-$city-$state$country$postalCode"
                } catch (e: java.lang.NullPointerException) {
                    Toast.makeText(requireContext(), "In-Valid Location", Toast.LENGTH_SHORT).show()
                    cardViewHome.isEnabled = false
                }
            }
            googleMap.addMarker(MarkerOptions().position(marker))

        }

        cardViewHome.setOnClickListener {
            findNavController().navigate(R.id.action_mapsFragment_to_weatherFragment)
            cardViewHome.visibility = View.GONE
            val locationButton = requireActivity().findViewById<ImageView>(R.id.mapCurrentLocationBtn)
            locationButton.visibility = View.GONE
            location?.let { it1 -> mainSharedViewModel.setLatLang(it1) }
        }



        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                defaultLocation,
                6f
            )
        )
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        liveViewModel = ViewModelProvider(this)[LiveLocationViewModel::class.java]


        val locationButton = requireActivity().findViewById<ImageView>(R.id.mapCurrentLocationBtn)
        locationButton.visibility = View.VISIBLE
        locationButton.setOnClickListener {
            mGoogleMaps.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    defaultLocation,
                    18f
                )
            )
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun getLiveLocationData() {
        liveViewModel.getLocationLiveData().observe(viewLifecycleOwner, Observer { location ->
            if (isLocationEnabled()) {
                if (location != null) {
                    defaultLocation = LatLng(location.lat, location.lon)
                }
            }
        })
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}