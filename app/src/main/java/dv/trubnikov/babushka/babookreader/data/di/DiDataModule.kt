package dv.trubnikov.babushka.babookreader.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dv.trubnikov.babushka.babookreader.data.BookStorageImpl
import dv.trubnikov.babushka.babookreader.data.BookmarkPreferences
import dv.trubnikov.babushka.babookreader.domain.boundaries.BookStorage
import dv.trubnikov.babushka.babookreader.domain.boundaries.BookmarkStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DiDataModule {

    @Binds
    @Singleton
    fun bindBookStorage(impl: BookStorageImpl): BookStorage

    @Binds
    fun bindBookmarkStorage(impl: BookmarkPreferences): BookmarkStorage
}
