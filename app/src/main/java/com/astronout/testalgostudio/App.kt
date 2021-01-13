package com.astronout.testalgostudio

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.astronout.testalgostudio.di.module.appModule
import com.astronout.testalgostudio.di.module.repoModule
import com.astronout.testalgostudio.di.module.viewModelModule
import com.chibatching.kotpref.Kotpref
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
        Kotpref.init(this)
        JodaTimeAndroid.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}