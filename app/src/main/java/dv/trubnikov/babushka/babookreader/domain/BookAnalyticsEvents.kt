package dv.trubnikov.babushka.babookreader.domain

import dv.trubnikov.babushka.babook.analytics.api.AnalyticsReporter
import javax.inject.Inject

class BookAnalyticsEvents @Inject constructor(
    private val analytics: AnalyticsReporter,
) {

    fun openNewBook(book: String) {
        analytics.sendEvent("new_book", mapOf("title" to book))
    }

    fun loadBook(book: String) {
        analytics.sendEvent("load_book", mapOf("title" to book))
    }

    fun changePage(book: String, page: Int) {
        val attrs = mapOf(
            "book" to book,
            "page" to page
        )
        analytics.sendEvent("change_page", attrs)
    }
}
