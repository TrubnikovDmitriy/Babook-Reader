package dv.trubnikov.babushka.babookreader.domain.interactors

import com.kursx.parser.fb2.FictionBook
import dv.trubnikov.babushka.babookreader.core.Out
import dv.trubnikov.babushka.babookreader.core.onSuccess
import dv.trubnikov.babushka.babookreader.domain.BookAnalyticsEvents
import dv.trubnikov.babushka.babookreader.domain.BookInteractor
import dv.trubnikov.babushka.babookreader.domain.models.Book
import java.io.InputStream

class BookInteractorAnalytics(
    private val bookInteractor: BookInteractor,
    private val bookAnalytics: BookAnalyticsEvents,
) : BookInteractor by bookInteractor {

    override suspend fun saveNewBook(inputStream: InputStream): Out<Book> {
        return bookInteractor.saveNewBook(inputStream).also { out ->
            out.onSuccess { bookAnalytics.openNewBook(it.fb2.title) }
        }
    }

    override suspend fun loadLastBook(): Out<Book> {
        return bookInteractor.loadLastBook().also { out ->
            out.onSuccess { bookAnalytics.loadBook(it.fb2.title) }
        }
    }

    override suspend fun saveBookmark(book: FictionBook, page: Int) {
        bookAnalytics.changePage(book.title, page + 1)
        bookInteractor.saveBookmark(book, page)
    }
}
