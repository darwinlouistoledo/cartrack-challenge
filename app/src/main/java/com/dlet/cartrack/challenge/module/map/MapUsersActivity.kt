package com.dlet.cartrack.challenge.module.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dlet.cartrack.challenge.R
import com.dlet.cartrack.challenge.common_android.ext.aac.observe
import com.dlet.cartrack.challenge.common_android.ext.aac.withViewModel
import com.dlet.cartrack.challenge.common_android.ext.component.showToast
import com.dlet.cartrack.challenge.common_android.ext.component.withBinding
import com.dlet.cartrack.challenge.databinding.ActivityUsersMapBinding
import com.dlet.cartrack.challenge.di.factory.ViewModelFactory
import com.dlet.cartrack.challenge.domain.manager.ErrorHandler
import com.dlet.cartrack.challenge.domain.model.User
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MapUsersActivity : AppCompatActivity(),
  OnMapReadyCallback,
  GoogleMap.OnMarkerClickListener {

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  @Inject
  lateinit var errorHandler: ErrorHandler<AppCompatActivity>

  private lateinit var viewModel: MapUsersViewModel

  private lateinit var binding: ActivityUsersMapBinding

  private lateinit var mapFragment: SupportMapFragment

  private var mGoogleMap: GoogleMap? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = withBinding(R.layout.activity_users_map)

    viewModel = withViewModel(this, viewModelFactory) {
      observe(observableState, ::render)
    }

    binding.apply {
      toolbarBack.setOnClickListener {
        finish()
      }
    }

    setupMap()

  }

  private fun render(state: MapUsersViewModel.State) {
    placeMarkers(state.userList)

    state.error?.getContentIfNotHandled()?.let {
      it.printStackTrace()
      errorHandler.handle(this, it)
    }
  }

  private fun setupMap() {
    mapFragment = SupportMapFragment.newInstance(
      GoogleMapOptions()
        .mapType(GoogleMap.MAP_TYPE_NORMAL)
        .rotateGesturesEnabled(true)
        .maxZoomPreference(100f)
        .minZoomPreference(10f)
        .zoomGesturesEnabled(true)
        .zoomControlsEnabled(true)
        .useViewLifecycleInFragment(true)
    )
      .apply {
        retainInstance = true
      }

    supportFragmentManager.beginTransaction()
      .replace(R.id.mapContainer, mapFragment)
      .commit()
    mapFragment.getMapAsync(this)
  }

  override fun onMapReady(googleMap: GoogleMap?) {
    googleMap?.let {
      mGoogleMap = it
      mGoogleMap?.setOnMarkerClickListener(this)

      viewModel.dispatch(MapUsersViewModel.Action.LoadUsers)
    }
  }

  override fun onMarkerClick(marker: Marker?): Boolean {
    marker?.let {
      it.showInfoWindow()
    }
    return true
  }

  private fun placeMarkers(
    list: List<User>
  ) {
    mGoogleMap?.let { map ->
      map.clear()
      list.forEach { u ->
        val latLon = LatLng(
          u.address.geoLocation.latitude.toDouble(),
          u.address.geoLocation.longitude.toDouble()
        )
        val marker = map.addMarker(
          MarkerOptions()
            .position(latLon)
            .title(u.name)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_map_pin))
        )
        marker.tag = u
      }

      val firstUser = list.firstOrNull()
      firstUser?.let { u ->
        val latLon = LatLng(
          u.address.geoLocation.latitude.toDouble(),
          u.address.geoLocation.longitude.toDouble()
        )

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLon, 1f))
      }

    }
  }
}