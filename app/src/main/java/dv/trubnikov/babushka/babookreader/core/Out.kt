package dv.trubnikov.babushka.babookreader.core

sealed class Out<out T> {
    data class Success<T>(val value: T) : Out<T>()
    data class Failure(val error: Throwable?) : Out<Nothing>()
}
