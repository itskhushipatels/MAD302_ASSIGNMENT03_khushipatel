/**
 * Course: MAD302-01 Android Development
 * Assignment: Assignment 3
 * Student Name: Khushi Patel A00198843
 * Date: 04/24/2026
 * Description: Location screen. Requests ACCESS_FINE_LOCATION permission at runtime,
 *              fetches the device's last known coordinates, and displays them.
 */

package com.example.mad302_assignment03_khushipatel

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mad302_assignment03_khushipatel.databinding.FragmentLocationBinding
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Handles runtime location permission and displays GPS coordinates.
 */
class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    /**
     * Registers the permission launcher. Result is handled inside the callback.
     */
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            getLocation()
        } else {
            binding.tvCoordinates.text = getString(R.string.error_permission_denied)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGetLocation.setOnClickListener {
            checkPermissionAndFetch()
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    /**
     * Checks whether the location permission is already granted.
     * Requests it if not.
     */
    private fun checkPermissionAndFetch() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(requireContext(), permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else {
            locationPermissionLauncher.launch(permission)
        }
    }

    /**
     * Retrieves the last known location using FusedLocationProviderClient.
     * Runs inside a coroutine and updates the UI on the main thread.
     */
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        binding.progressBarLocation.visibility = View.VISIBLE
        binding.tvCoordinates.text = ""
        binding.btnGetLocation.isEnabled = false

        val fusedClient = LocationServices.getFusedLocationProviderClient(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val location = fusedClient.lastLocation.await()
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    binding.tvCoordinates.text =
                        getString(R.string.coordinates_format, lat, lon)
                } else {
                    binding.tvCoordinates.text = getString(R.string.error_location_unavailable)
                }
            } catch (e: Exception) {
                binding.tvCoordinates.text = getString(R.string.error_location_failed)
            } finally {
                binding.progressBarLocation.visibility = View.GONE
                binding.btnGetLocation.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}