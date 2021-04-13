package dv.trubnikov.babushka.babook.analytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dv.trubnikov.babushka.babook.analytics.api.AnalyticsInitializer
import dv.trubnikov.babushka.babook.analytics.api.AnalyticsReporter
import dv.trubnikov.babushka.babook.analytics.impl.YandexMetricaInitializer
import dv.trubnikov.babushka.babook.analytics.impl.YandexMetricaReporter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AnalyticsModule {

    @Binds
    @Singleton
    fun bindAnalyticsInitializer(impl: YandexMetricaInitializer): AnalyticsInitializer

    @Binds
    @Singleton
    fun bindAnalyticsReporter(impl: YandexMetricaReporter): AnalyticsReporter
}
