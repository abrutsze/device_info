package com.artur.deviceinfoapp.ui.activites

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.artur.deviceinfoapp.R
import com.artur.deviceinfoapp.data.enums.Pages
import com.artur.deviceinfoapp.ui.base.BaseActivity
import com.artur.deviceinfoapp.ui.viewmodels.DeviceInfoViewModel
import kotlinx.android.synthetic.main.activity_device_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class DeviceInfoActivity : BaseActivity(), LocationListener {

    private val viewModel: DeviceInfoViewModel by viewModel()
    private val TAG = AppsActivity::class.java.simpleName

    // ToDo bad solution to implement the location provider here, but it is the fastest way
    private lateinit var mLocationManager: LocationManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)

        initViews()
        initObservers()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null && location.time > Calendar.getInstance()
                .timeInMillis - 2 * 60 * 1000
        ) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        }
    }

    override fun initViews() {
        vStorageInfo.text = viewModel.getStorageInfo()
        vMemoryInfo.text = viewModel.getMemoryInfo()
        vCpuInfo.text = viewModel.getCpuInfo()
        vNetworkInfo.text = viewModel.getNetworkInfo()
    }

    override fun initObservers() {
        viewModel.pageChange.observe(this, Observer { status ->
            status?.let {
                when (it) {
                    Pages.Apps -> {
                        finish()
                    }
                    else -> {
                        Log.w(TAG, "Something went wrong... received wrong page")
                    }
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.onBackClick()
        return true
    }

    // Required functions
    override fun onProviderDisabled(arg0: String?) {
        vGpsInfo.text = getString(R.string.gps_disabled)
    }
    // ToDo add listener to GPS state change
    override fun onProviderEnabled(arg0: String?) {
    }
    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            vGpsInfo.text =
                String.format(getString(R.string.gps_info), location.latitude.toString(), location.longitude.toString())
            mLocationManager.removeUpdates(this)
        }
    }

    override fun onStatusChanged(arg0: String?, arg1: Int, arg2: Bundle?) {
    }

}