package dv.trubnikov.babushka.babook.analytics.impl

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dv.trubnikov.babushka.babook.analytics.BuildConfig
import dv.trubnikov.babushka.babook.analytics.api.AnalyticsInitializer
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Inject

class YandexMetricaInitializer @Inject constructor() : AnalyticsInitializer {

    companion object {
        private val SESSION_TIMEOUT = MINUTES.toSeconds(20).toInt()
    }

    override fun init(app: Application) {
        val config = YandexMetricaConfig.newConfigBuilder(BuildConfig.YANDEX_METRIC_API_KEY)
            .withAppVersion(BuildConfig.BUILD_TYPE)
            .withSessionTimeout(SESSION_TIMEOUT)
            .withLocationTracking(false)
            .withCrashReporting(true)
            .withLogs()
            .build()

        YandexMetrica.activate(app, config)
        YandexMetrica.enableActivityAutoTracking(app)
    }
}