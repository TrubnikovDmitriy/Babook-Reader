package dv.trubnikov.babushka.babookreader.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dv.trubnikov.babushka.babookreader.core.loge
import dv.trubnikov.babushka.babookreader.domain.boundaries.BookStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class BookStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BookStorage {

    companion object {
        private const val BOOK_NAME = "current_book.fb2"
    }

    /**
     * 1. Я не нашел корутиновских либ для работы с файлами
     * 2. Это IO-диспатчер, для него блокаться не так критично
     */
    override suspend fun saveBook(inputStream: InputStream): File? {
        return withContext(Dispatchers.IO) {
            try {
                context.openFileOutput(BOOK_NAME, Context.MODE_PRIVATE).use {
                    it.write(inputStream.readBytes())
                }
                loadSavedBook()
            } catch (e: IOException) {
                loge(e) { "Не удалось сохранить книжку в интернал сторадж" }
                null
            }
        }
    }

    override suspend fun loadSavedBook(): File? {
        return withContext(Dispatchers.IO) {
            File(context.filesDir, BOOK_NAME).takeIf { it.isFile }
        }
    }
}
