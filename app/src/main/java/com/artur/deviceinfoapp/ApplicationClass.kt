package com.artur.deviceinfoapp

import android.app.Application
import com.artur.deviceinfoapp.providers.ApkInfoExtractor
import com.artur.deviceinfoapp.providers.NetworkInfoProvider
import com.artur.deviceinfoapp.ui.viewmodels.AppsViewModel
import com.artur.deviceinfoapp.ui.viewmodels.DeviceInfoViewModel
import com.artur.deviceinfoapp.utils.Permissions
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ApplicationClass : Application() {


    private val myModule = module {
        single { ApkInfoExtractor(get()) }
        single { NetworkInfoProvider(get()) }
        single { Permissions() }

        viewModel { AppsViewModel(this@ApplicationClass, get()) }
        viewModel { DeviceInfoViewModel(this@ApplicationClass, get()) }
    }

    override fun onCreate() {
        super.onCreate()

        // start Koin!
        startKoin {
            // declare used Android context
            androidContext(this@ApplicationClass)
            // declare modules
            modules(myModule)
        }
    }
}