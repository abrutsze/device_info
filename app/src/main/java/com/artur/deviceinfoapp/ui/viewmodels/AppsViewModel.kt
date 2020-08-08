package com.artur.deviceinfoapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artur.deviceinfoapp.data.enums.Pages
import com.artur.deviceinfoapp.data.models.App
import com.artur.deviceinfoapp.providers.ApkInfoExtractor
import kotlinx.coroutines.*


class AppsViewModel(
    application: Application,
    private val apkInfoExtractor: ApkInfoExtractor
) : AndroidViewModel(application) {

    val pageChange: LiveData<Pages>
        get() = mPageChange
    private var mPageChange = MutableLiveData<Pages>()

    val isLoading: LiveData<Boolean>
        get() = mIsLoading
    private var mIsLoading = MutableLiveData(false)

    val apps: LiveData<List<App>>
        get() = mApps
    private var mApps = MutableLiveData<List<App>>()

    private fun goToMainPage() {
        mPageChange.value = Pages.DeviceInfo
    }

    fun onDeviceInfoClick() {
        goToMainPage()
    }

    fun getAppsList(uiScope: CoroutineScope) {
        mIsLoading.value = true
        uiScope.launch(Dispatchers.IO) {
            val appsList = runBlocking {
                apkInfoExtractor.getAllInstalledApps()
            }
            withContext(Dispatchers.Main) {
                mIsLoading.value = false
                mApps.value = appsList
            }
        }
    }

}