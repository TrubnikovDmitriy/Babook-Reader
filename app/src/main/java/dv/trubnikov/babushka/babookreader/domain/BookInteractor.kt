package dv.trubnikov.babushka.babookreader.domain

import com.kursx.parser.fb2.FictionBook
import dv.trubnikov.babushka.babookreader.core.Out
import dv.trubnikov.babushka.babookreader.domain.models.Book
import java.io.InputStream

interface BookInteractor {

    suspend fun saveNewBook(inputStream: InputStream): Out<Book>

    suspend fun loadLastBook(): Out<Book>

    suspend fun saveBookmark(book: FictionBook, page: Int)
}
