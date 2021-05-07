package dv.trubnikov.babushka.babookreader.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dv.trubnikov.babushka.babookreader.domain.BookAnalyticsEvents
import dv.trubnikov.babushka.babookreader.domain.BookInteractor
import dv.trubnikov.babushka.babookreader.domain.interactors.BookInteractorAnalytics
import dv.trubnikov.babushka.babookreader.domain.interactors.BookInteractorDomain
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiDomainModule {

    @Provides
    @Singleton
    fun bindBookInteractor(
        domain: BookInteractorDomain,
        analytics: BookAnalyticsEvents,
    ): BookInteractor {
        return BookInteractorAnalytics(domain, analytics)
    }
}
