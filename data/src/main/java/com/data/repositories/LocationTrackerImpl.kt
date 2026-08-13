package com.data.repositories

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.data.mappers.toFlagEmoji
import com.domain.LocationResult
import com.domain.LocationTracker
import com.domain.models.CityItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

class LocationTrackerImpl(
    private val locationClient: FusedLocationProviderClient,
    private val context: Context
) : LocationTracker {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationResult {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled) {
            return LocationResult.GpsDisabled
        }

        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocation && !hasCoarseLocation) {
            return LocationResult.NoPermission
        }

        return suspendCancellableCoroutine { cont ->
            locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { freshLocation ->
                if (freshLocation != null) {
                    decodeLocationAndResume(freshLocation.latitude, freshLocation.longitude, cont)
                } else {
                    locationClient.lastLocation.addOnSuccessListener { cachedLocation ->
                        if (cachedLocation != null) {
                            decodeLocationAndResume(
                                cachedLocation.latitude,
                                cachedLocation.longitude,
                                cont
                            )
                        } else {
                            cont.resume(LocationResult.Error)
                        }
                    }.addOnFailureListener {
                        cont.resume(LocationResult.Error)
                    }
                }
            }.addOnFailureListener {
                locationClient.lastLocation.addOnSuccessListener { cachedLocation ->
                    if (cachedLocation != null) {
                        decodeLocationAndResume(
                            cachedLocation.latitude,
                            cachedLocation.longitude,
                            cont
                        )
                    } else {
                        cont.resume(LocationResult.Error)
                    }
                }.addOnFailureListener {
                    cont.resume(LocationResult.Error)
                }
            }
        }
    }

    private fun decodeLocationAndResume(
        lat: Double,
        lon: Double,
        cont: CancellableContinuation<LocationResult>
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(lat, lon, 1, object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        cont.resume(LocationResult.Success(processAddresses(lat, lon, addresses)))
                    }

                    override fun onError(errorMessage: String?) {
                        cont.resume(LocationResult.Error)
                    }
                })
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                cont.resume(LocationResult.Success(processAddresses(lat, lon, addresses)))
            }
        } catch (e: Exception) {
            Log.e("LocationTracker", "Error getting addresses", e)
            cont.resume(LocationResult.Error)
        }
    }

    private fun processAddresses(lat: Double, lon: Double, addresses: List<Address>?): CityItem {
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            val cityName = address.locality ?: address.subAdminArea ?: "My Location"
            val countryName = address.countryName ?: "Unknown"
            val countryCode = address.countryCode ?: ""

            return CityItem(
                id = (lat + lon).toInt(),
                name = cityName,
                country = countryName,
                flagEmoji = countryCode.toFlagEmoji(),
                latitude = lat,
                longitude = lon
            )
        }
        return createFallbackCity(lat, lon)
    }

    private fun createFallbackCity(lat: Double, lon: Double) = CityItem(
        id = 0,
        name = "My Location",
        country = "Unknown",
        flagEmoji = "📍",
        latitude = lat,
        longitude = lon
    )
}