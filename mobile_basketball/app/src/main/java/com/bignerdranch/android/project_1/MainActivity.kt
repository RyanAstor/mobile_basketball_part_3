package com.bignerdranch.android.project_1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    GameListFragment.Callbacks, GameFragment.Callbacks
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        startLocationUpdates()

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = GameFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
 //       }
        }
    }

    fun startLocationUpdates() {

        // Create the location request to start receiving updates
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(
            mLocationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    // do work here
                    onLocationChanged(locationResult.lastLocation)
                }
            },
            Looper.myLooper()
        )
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        val lat = location.latitude
        val lon = location.longitude
        Log.d(TAG, "Updated Location: $lat,$lon")
        Log.d(TAG, "onResume() called")
        Log.d(TAG, "$location")
        Log.d(TAG, "$lat, $lon")
        val fragment = GameFragment.newInstance(lat, lon)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onGameSelected(gameId: UUID) {
        val fragment = GameFragment.newInstance(gameId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun displayList(winner: Char) {
        Log.d(TAG, "MainActivity.displayList")
        val fragment = GameListFragment.newInstance(winner)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}