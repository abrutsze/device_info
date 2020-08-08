package com.artur.deviceinfoapp.ui.viewmodels

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.text.format.Formatter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artur.deviceinfoapp.R
import com.artur.deviceinfoapp.data.enums.Pages
import com.artur.deviceinfoapp.providers.NetworkInfoProvider
import java.io.File
import java.io.IOException


class DeviceInfoViewModel(
    application: Application, private val networkInfoProvider: NetworkInfoProvider
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    val pageChange: LiveData<Pages>
        get() = mPageChange
    private var mPageChange = MutableLiveData<Pages>()

    private fun goToAppsPage() {
        mPageChange.value = Pages.Apps
    }

    fun onBackClick() {
        goToAppsPage()
    }

    fun getStorageInfo(): String {
        val totalSize: Long =
            File(context.applicationContext.filesDir.absoluteFile.toString()).totalSpace
        val totMb = totalSize / (1024 * 1024)

        val availableSize: Double =
            File(context.applicationContext.filesDir.absoluteFile.toString()).freeSpace.toDouble()
        val freeMb = availableSize / (1024 * 1024)

        val freeBytesExternal: Long =
            File(context.getExternalFilesDir(null).toString()).freeSpace
        val free = (freeBytesExternal / (1024 * 1024)).toInt()
        val totalSizeExternal: Long =
            File(context.getExternalFilesDir(null).toString()).totalSpace
        val total = (totalSizeExternal / (1024 * 1024)).toInt()

        return String.format(
            context.resources.getString(R.string.storage_info),
            freeMb,
            totMb,
            free,
            total
        )
    }

    fun getMemoryInfo(): String {
        val memoryInfo = ActivityManager.MemoryInfo()
        (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(
            memoryInfo
        )
        val nativeHeapSize = memoryInfo.totalMem
        val nativeHeapFreeSize = memoryInfo.availMem
        return String.format(
            context.resources.getString(R.string.memory_info),
            Formatter.formatFileSize(context, nativeHeapFreeSize),
            Formatter.formatFileSize(context, nativeHeapSize)
        )
    }

    fun getCpuInfo(): String {
        var info = ""
        val byteArray = ByteArray(1024)

        try {
            val processBuilder = ProcessBuilder(listOf("/system/bin/cat", "/proc/cpuinfo"))
            val process = processBuilder.start()
            val inputStream = process.inputStream
            while (inputStream.read(byteArray) !== -1) {
                info += String(byteArray)
            }
            inputStream.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return info
    }

    fun getNetworkInfo(): String {
        val isWifiEnabled = if (networkInfoProvider.isWifiEnabled())
            context.resources.getString(R.string.enabled)
        else context.resources.getString(R.string.disabled)

        val isMobileDataEnabled = if (networkInfoProvider.isMobileDataEnabled())
            context.resources.getString(R.string.enabled)
        else context.resources.getString(R.string.disabled)

        return String.format(
            context.resources.getString(R.string.network_info), isWifiEnabled, isMobileDataEnabled
        )
    }

}