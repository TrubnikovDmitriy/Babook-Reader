package dv.trubnikov.babushka.babookreader

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dv.trubnikov.babushka.babook.analytics.api.AnalyticsInitializer
import javax.inject.Inject

@HiltAndroidApp
class BookApplication : Application() {

    @Inject
    lateinit var analytics: AnalyticsInitializer

    override fun onCreate() {
        super.onCreate()

        analytics.init(this)
    }
}
