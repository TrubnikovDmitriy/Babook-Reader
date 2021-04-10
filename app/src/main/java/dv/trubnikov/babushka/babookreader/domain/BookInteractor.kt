package dv.trubnikov.babushka.babookreader.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookInteractor @Inject constructor(
    private val bookStorage: BookStorage
)
