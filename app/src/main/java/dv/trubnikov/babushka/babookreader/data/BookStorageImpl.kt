package dv.trubnikov.babushka.babookreader.data

import android.content.Context
import dv.trubnikov.babushka.babookreader.domain.BookStorage
import dv.trubnikov.babushka.babookreader.utils.loge
import dv.trubnikov.babushka.babookreader.utils.logw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class BookStorageImpl(private val context: Context) : BookStorage {

    companion object {
        private const val BOOK_NAME = "current_book.fb2"
    }

    /**
     * 1. Я не нашел корутиновских либ для работы с файлами
     * 2. Это IO-диспатчер, для него блокаться не так критично
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun saveBook(inputStream: InputStream): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                context.openFileOutput(BOOK_NAME, Context.MODE_PRIVATE).use {
                    it.write(inputStream.readBytes())
                }
                true
            } catch (e: IOException) {
                loge(e) { "Не удалось сохранить книжку в интернал сторадж" }
                false
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun loadSavedBook(): File? {
        return withContext(Dispatchers.IO) {
            File(context.filesDir, BOOK_NAME).takeIf { it.isFile }
        }
    }
}