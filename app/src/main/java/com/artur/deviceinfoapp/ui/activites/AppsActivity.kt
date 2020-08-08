package com.artur.deviceinfoapp.ui.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.artur.deviceinfoapp.R
import com.artur.deviceinfoapp.data.enums.Pages
import com.artur.deviceinfoapp.ui.adapters.AppsAdapter
import com.artur.deviceinfoapp.ui.base.BaseActivity
import com.artur.deviceinfoapp.ui.viewmodels.AppsViewModel
import com.artur.deviceinfoapp.utils.Permissions
import kotlinx.android.synthetic.main.activity_apps.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AppsActivity : BaseActivity() {

    private val viewModel: AppsViewModel by viewModel()
    private val permissions: Permissions by inject()
    private val TAG = AppsActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps)

        initViews()
        initObservers()
        viewModel.getAppsList(uiScope)
    }

    override fun initViews() {
        vAppsList.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(this@AppsActivity)
            // set the custom adapter to the RecyclerView
            adapter = AppsAdapter()
        }
    }

    override fun initObservers() {
        viewModel.isLoading.observe(this, Observer { loadingStatus ->
            if (loadingStatus)
                vProgressBar.visibility = View.VISIBLE
            else
                vProgressBar.visibility = View.GONE
        })
        viewModel.pageChange.observe(this, Observer { status ->
            status?.let {
                when (it) {
                    Pages.DeviceInfo -> {
                        startActivity(Intent(this, DeviceInfoActivity::class.java))
                    }
                    else -> {
                        Log.w(TAG, "Something went wrong... received wrong page")
                    }
                }
            }
        })
        viewModel.apps.observe(this, Observer { apps ->
            vAppsNumber.text = String.format(getString(R.string.apps_number), apps.size)
            (vAppsList.adapter as AppsAdapter).setData(apps)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuDeviceInfo -> {
                if (permissions.gimmePermission(this))
                    viewModel.onDeviceInfoClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // ToDo bad solution, but it is the fastest way
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(this.permissions.gimmePermission(this))
            viewModel.onDeviceInfoClick()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}