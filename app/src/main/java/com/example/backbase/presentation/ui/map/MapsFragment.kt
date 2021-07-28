package com.example.backbase.presentation.ui.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.backbase.R
import com.example.backbase.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding

    companion object {
        private const val EXTRAS = "cityLatLong"

        fun newInstance(subBookingTypes: Intent): MapsFragment =
            MapsFragment().apply {
                arguments = bundleOf(EXTRAS to subBookingTypes)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        val intent = (arguments?.get("cityLatLong") as Intent)
        val lat = intent.extras?.get("lat") as Double
        val long = intent.extras?.get("long") as Double

        if (mapFragment != null) {
            goToCity(mapFragment, lat, long)
        }
    }

    private fun goToCity(mapsFragment: SupportMapFragment, lat: Double, long: Double) {
        mapsFragment.getMapAsync { googleMap ->
            val city = LatLng(lat, long)
            googleMap.addMarker(MarkerOptions().position(city).title("Marker in$city"))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(13F))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(city))
        }
    }
}
