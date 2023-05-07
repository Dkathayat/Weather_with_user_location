package com.example.devdigitalassignment.core.ui.weatherfragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.example.devdigitalassignment.R
import com.example.devdigitalassignment.core.ui.viewmodel.MainSharedViewModel
import com.example.devdigitalassignment.databinding.FragmentWeatherBinding
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.kathayat.testWeather.MainViewModel
import com.kathayat.testWeather.core.data.Resource
import com.kathayat.testWeather.core.domain.model.currentforecast.CurrentWeather
import com.kathayat.testWeather.core.ui.DailyAdapter
import com.kathayat.testWeather.core.ui.HourlyAdapter
import com.kathayat.testWeather.core.ui.NetworkStatusViewModel
import com.kathayat.testWeather.core.utils.DateFormatter
import com.kathayat.testWeather.core.utils.getLottieSrc
import com.kathayat.testWeather.core.utils.networkstatus.NetworkState
import com.kathayat.testWeather.core.utils.networkstatus.NetworkStatusTracker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class weatherFragment : Fragment() {
    private var mGPS: Boolean = false
    private lateinit var mLocationManager: LocationManager
    private var _binding: FragmentWeatherBinding? = null
    private val binding by lazy { _binding!! }
    private val mainSharedViewModel: MainSharedViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private var hourlyAdapter = HourlyAdapter()
    private var dailyAdapter = DailyAdapter()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double = 0.0
    private var lon: Double = 0.0


    private val viewModel: NetworkStatusViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val networkStatusTracker = NetworkStatusTracker(requireContext())
                    return NetworkStatusViewModel(networkStatusTracker) as T
                }
            },
        )[NetworkStatusViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        mainSharedViewModel.location.observe(viewLifecycleOwner, Observer {
            getCurrentForecast(it.latitude, it.longitude)
            getDailyForecast(it.latitude, it.longitude)
            lat = it.latitude
            lon = it.longitude
            Log.d("mainSharedViewModel",it.toString())

        })



        isLocationPermissionGranted()
        mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (mGPS) {
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Please Turn On the GPS", Toast.LENGTH_LONG).show()
        }
        checkConnection()

        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()



        with(binding.rvHourly) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = hourlyAdapter
        }

        with(binding.rvDaily) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = dailyAdapter
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getDailyForecast(lat, lon)
        getCurrentForecast(lat,lon)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                0
            )
            false
        } else {
            true
        }
    }

    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val request = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 10000
        }
        val permission = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location? = locationResult.lastLocation
                    if (location != null) {

                    }
                }
            }, Looper.getMainLooper())
        }
    }

    private fun getCurrentForecast(lat: Double, lon: Double) {
        mainViewModel.currentForecast(lat, lon).observe(viewLifecycleOwner) { forecast ->
            if (forecast != null) {
                when (forecast) {
                    is Resource.Loading -> {
                        binding.constraint.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.constraint.visibility = View.VISIBLE
                        forecast.data?.let { setCurrentForecast(it) }
                    }

                    is Resource.Error -> {

                        if (forecast.data?.weather?.size != null) {
                            setCurrentForecast(forecast.data)
                        } else {
                            binding.constraint.visibility = View.GONE
                        }
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun getDailyForecast(lat: Double, lon: Double) {
        mainViewModel.forecast(lat, lon).observe(viewLifecycleOwner) { forecast ->

            if (forecast != null) {
                when (forecast) {
                    is Resource.Loading -> {
                        binding.constraint.visibility = View.VISIBLE
                        binding.progress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.constraint.visibility = View.VISIBLE
                        binding.progress.visibility = View.GONE
                        hourlyAdapter.setData(forecast.data?.list)
                        dailyAdapter.setData(forecast.data?.list)
                    }

                    is Resource.Error -> {
                        binding.progress.visibility = View.GONE
                        if (forecast.data?.list?.size != null) {
                            hourlyAdapter.setData(forecast.data.list)
                            dailyAdapter.setData(forecast.data.list)
                            Snackbar.make(
                                binding.root,
                                forecast.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        } else {
                            binding.constraint.visibility = View.GONE
                            Snackbar.make(
                                binding.root,
                                forecast.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }

    private fun setCurrentForecast(forecast: CurrentWeather) {
        binding.apply {

            tvCity.text = forecast.name
            tvTemp.text = (forecast.main?.temp?.toInt().toString() + "°") ?: "-"
            humidityText.text = "Humidity ${forecast.main?.humidity}"
            rainChance.text = "Rain "+(forecast.rain?.r3h ?: "0") as CharSequence?

            val date = forecast.date?.toLong()?.let { DateFormatter.getDayNHour(it) }
            tvDate.text = date

            val img = forecast.weather?.get(0)?.icon
            lavWeather.setAnimation(img?.let { getLottieSrc(it) })
            lavWeather.playAnimation()
            lavWeather.repeatMode = LottieDrawable.RESTART
        }
    }

    private fun checkConnection() {
        viewModel.state.observe(requireActivity()) { state ->
            when (state) {
                NetworkState.Error -> Snackbar.make(
                    binding.root,
                    "Disconnected",
                    Snackbar.LENGTH_LONG
                ).show()
                else -> {}
            }
        }
    }
}