package dv.trubnikov.babushka.babookreader.domain

import com.kursx.parser.fb2.FictionBook
import dv.trubnikov.babushka.babookreader.core.Out
import dv.trubnikov.babushka.babookreader.core.getOr
import dv.trubnikov.babushka.babookreader.domain.boundaries.BookStorage
import dv.trubnikov.babushka.babookreader.domain.boundaries.BookmarkStorage
import dv.trubnikov.babushka.babookreader.domain.models.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton
import javax.xml.parsers.ParserConfigurationException

@Singleton
class BookInteractor @Inject constructor(
    private val bookStorage: BookStorage,
    private val bookmarkStorage: BookmarkStorage,
) {

    suspend fun saveNewBook(inputStream: InputStream): Out<Book> {
        val bookFile = bookStorage.saveBook(inputStream)
        val fictionBook = createFictionBook(bookFile).getOr { return it }
        val page = bookmarkStorage.getPageFor(fictionBook.title)
        return Out.Success(Book(fictionBook, page))
    }

    suspend fun loadLastBook(): Out<Book> {
        val bookFile = bookStorage.loadSavedBook()
        val fictionBook = createFictionBook(bookFile).getOr { return it }
        val page = bookmarkStorage.getPageFor(fictionBook.title)
        return Out.Success(Book(fictionBook, page))
    }

    suspend fun saveBookmark(book: FictionBook, page: Int) {
        bookmarkStorage.setPageFor(book.title, page)
    }

    private suspend fun createFictionBook(bookFile: File?): Out<FictionBook> {
        if (bookFile == null) {
            return Out.Failure(null)
        }
        return withContext(Dispatchers.IO) {
            try {
                val book = FictionBook(bookFile)
                Out.Success(book)
            } catch (e: IOException) {
                Out.Failure(e)
            } catch (e: ParserConfigurationException) {
                Out.Failure(e)
            } catch (e: SAXException) {
                Out.Failure(e)
            }
        }
    }
}
