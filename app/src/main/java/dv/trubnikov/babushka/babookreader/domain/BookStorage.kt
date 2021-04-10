package dv.trubnikov.babushka.babookreader.domain

import java.io.File
import java.io.InputStream

interface BookStorage {

    /**
     * Save copy of stream to internal storage
     * (sic. it overwrites previous data)
     *
     * @return file with saved book or null if something went wrong
     */
    suspend fun saveBook(inputStream: InputStream): File?

    /**
     * Return file with last saved book.
     * Return null if there is no saved books.
     */
    suspend fun loadSavedBook(): File?
}