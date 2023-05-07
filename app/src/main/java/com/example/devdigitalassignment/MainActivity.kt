package com.example.devdigitalassignment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.devdigitalassignment.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.kathayat.testWeather.MainViewModel
import com.kathayat.testWeather.core.ui.NetworkStatusViewModel
import com.kathayat.testWeather.core.utils.networkstatus.NetworkState
import com.kathayat.testWeather.core.utils.networkstatus.NetworkStatusTracker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mLocationManager: LocationManager
    private val mainViewModel: MainViewModel by viewModels()
    private var mGPS: Boolean = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double = 0.0
    private var lon: Double = 0.0


    private val viewModel: NetworkStatusViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val networkStatusTracker = NetworkStatusTracker(this@MainActivity)
                    return NetworkStatusViewModel(networkStatusTracker) as T
                }
            },
        )[NetworkStatusViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        binding.addIconHome.setOnClickListener {
//            val navHostFragment =
//                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//            val navController = navHostFragment.navController
//            navController.navigate(R.id.mapsFragment)
//        }
        isLocationPermissionGranted()
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (mGPS) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Please Turn On the GPS", Toast.LENGTH_LONG).show()
        }

        checkConnection()
    }

    override fun onResume() {
        super.onResume()
        binding.mapCurrentLocationBtn.visibility = View.GONE
        binding.cardViewHome.visibility = View.GONE
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                0
            )
            false
        } else {
            true
        }
    }

    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val request = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 10000
        }
        val permission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location? = locationResult.lastLocation
                    if (location != null) {
                        lat = location.latitude
                        lon = location.longitude
                        //getCurrentForecast(lat,lon)
                    }
                }
            }, Looper.getMainLooper())
        }
    }

    private fun checkConnection() {
        viewModel.state.observe(this) { state ->
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

    override fun onBackPressed() {
        super.onBackPressed()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.homeFragment)
        binding.cardViewHome.visibility = View.GONE
        binding.mapCurrentLocationBtn.visibility = View.GONE
    }
}