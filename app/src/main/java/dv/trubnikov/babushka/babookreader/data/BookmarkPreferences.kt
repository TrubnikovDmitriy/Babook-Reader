package dv.trubnikov.babushka.babookreader.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dv.trubnikov.babushka.babookreader.domain.boundaries.BookmarkStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkPreferences @Inject constructor(
    @ApplicationContext context: Context
) : BookmarkStorage {

    companion object {
        private const val PREFS_NAME = "bookmarks"
    }

    private val preferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override suspend fun getPageFor(bookName: String): Int {
        return withContext(Dispatchers.IO) {
            preferences.getInt(bookName, 0)
        }
    }

    override suspend fun setPageFor(bookName: String, page: Int) {
        preferences.edit().putInt(bookName, page).apply()
    }
}
