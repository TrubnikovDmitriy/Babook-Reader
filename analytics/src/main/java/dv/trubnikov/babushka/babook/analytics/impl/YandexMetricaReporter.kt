package dv.trubnikov.babushka.babook.analytics.impl

import com.yandex.metrica.YandexMetrica
import dv.trubnikov.babushka.babook.analytics.api.AnalyticsReporter
import javax.inject.Inject

class YandexMetricaReporter @Inject constructor() : AnalyticsReporter {

    override fun sendEvent(name: String) {
        YandexMetrica.reportEvent(name)
    }

    override fun sendEvent(name: String, attrs: Map<String, Any?>) {
        YandexMetrica.reportEvent(name, attrs)
    }
}