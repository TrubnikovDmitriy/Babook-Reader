package dv.trubnikov.babushka.babookreader.domain.models

import com.kursx.parser.fb2.FictionBook

data class Book(
    val fb2: FictionBook,
    val page: Int,
)
