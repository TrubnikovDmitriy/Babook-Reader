package dv.trubnikov.babushka.babookreader.domain

import java.io.File
import java.io.InputStream

interface BookStorage {

    /**
     * Save copy of stream to internal storage
     * (sic. it overwrites previous data)
     *
     * @return true in success case, false in otherwise
     */
    suspend fun saveBook(inputStream: InputStream): Boolean

    /**
     * Return file with last saved book.
     * Return null if there is no saved books.
     */
    suspend fun loadSavedBook(): File?
}