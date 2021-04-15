package dv.trubnikov.babushka.babook.analytics.impl

import com.yandex.metrica.YandexMetrica
import dv.trubnikov.babushka.babook.analytics.api.AnalyticsReporter
import javax.inject.Inject

class YandexMetricaReporter @Inject constructor() : AnalyticsReporter {

    companion object {
        private const val CUSTOM_ERROR_TAG = "Custom Error"
    }

    override fun sendEvent(name: String) {
        YandexMetrica.reportEvent(name)
    }

    override fun sendEvent(name: String, attrs: Map<String, Any?>) {
        YandexMetrica.reportEvent(name, attrs)
    }

    override fun sendError(message: String, error: Throwable?) {
        YandexMetrica.reportError(CUSTOM_ERROR_TAG, message, error)
    }
}
