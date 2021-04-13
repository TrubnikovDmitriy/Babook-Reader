package dv.trubnikov.babushka.babook.analytics.api

interface AnalyticsReporter {
    fun sendEvent(name: String)
    fun sendEvent(name: String, attrs: Map<String, Any?>)
}