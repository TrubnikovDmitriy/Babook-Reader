package dv.trubnikov.babushka.babook.analytics.api

import android.app.Application

interface AnalyticsInitializer {
    fun init(app: Application)
}