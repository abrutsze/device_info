package com.artur.deviceinfoapp.providers

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager


class NetworkInfoProvider(private val context: Context) {

    fun isWifiEnabled(): Boolean {
        val wifiMgr = context.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        return if (wifiMgr!!.isWifiEnabled) { // Wi-Fi adapter is ON
            val wifiInfo = wifiMgr.connectionInfo
            wifiInfo.networkId != -1
            // Connected to an access point
        } else {
            false // Wi-Fi adapter is OFF
        }
    }

    fun isMobileDataEnabled(): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return try {
            val cmClass = Class.forName(cm.javaClass.name)
            val method = cmClass.getDeclaredMethod("getMobileDataEnabled")
            method.isAccessible = true // Make the method callable
            // get the setting for "mobile data"
            method.invoke(cm) as Boolean
        } catch (e: Exception) {
            // Some problem accessible private API
            false
        }
    }

}