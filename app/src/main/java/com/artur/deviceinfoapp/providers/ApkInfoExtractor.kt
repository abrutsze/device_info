package com.artur.deviceinfoapp.providers

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.artur.deviceinfoapp.R
import com.artur.deviceinfoapp.data.models.App
import java.util.*


class ApkInfoExtractor(private val context: Context) {

    fun getAllInstalledApps(): List<App> {
        val apps: MutableList<App> = ArrayList()
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        val resolveInfoList = context.packageManager.queryIntentActivities(intent, 0)
        resolveInfoList.forEach {
            val packageName = it.activityInfo.applicationInfo.packageName
            if (!isSystemPackage(it)) {
                apps.add(
                    App(packageName, getAppName(packageName), getAppIconByPackageName(packageName))
                )
            }
        }
        return apps
    }

    private fun isSystemPackage(resolveInfo: ResolveInfo): Boolean {
        return resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun getAppIconByPackageName(apkTempPackageName: String): Drawable? {
        val drawable: Drawable?
        drawable = try {
            context.packageManager.getApplicationIcon(apkTempPackageName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ContextCompat.getDrawable(context, R.mipmap.ic_launcher_round)
        }
        return drawable
    }

    private fun getAppName(apkPackageName: String): String? {
        var name = ""
        val applicationInfo: ApplicationInfo
        val packageManager = context.packageManager
        try {
            applicationInfo = packageManager.getApplicationInfo(apkPackageName, 0)
            applicationInfo.let {
                name = packageManager.getApplicationLabel(applicationInfo) as String
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return name
    }

}