package dv.trubnikov.babushka.babookreader.domain.boundaries

interface BookmarkStorage {
    suspend fun getPageFor(bookName: String): Int
    suspend fun setPageFor(bookName: String, page: Int)
}