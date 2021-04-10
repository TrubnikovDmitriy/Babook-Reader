package dv.trubnikov.babushka.babookreader.domain

import com.kursx.parser.fb2.FictionBook
import dv.trubnikov.babushka.babookreader.core.Out
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
    private val bookStorage: BookStorage
) {

    suspend fun saveNewBook(inputStream: InputStream): Out<FictionBook> {
        val bookFile = bookStorage.saveBook(inputStream)
        return createFictionBook(bookFile)
    }

    suspend fun loadLastBook(): Out<FictionBook> {
        val bookFile = bookStorage.loadSavedBook()
        return createFictionBook(bookFile)
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
