package dv.trubnikov.babushka.babookreader.core

sealed class Out<out T> {
    data class Success<T>(val value: T) : Out<T>()
    data class Failure(val error: Throwable?) : Out<Nothing>()
}

inline fun <T> Out<T>.getOr(handler: (Out.Failure) -> T): T {
    return when (this) {
        is Out.Failure -> handler(this)
        is Out.Success -> value
    }
}

inline fun <T> Out<T>.onSuccess(handler: (T) -> Unit) {
    if (this is Out.Success) {
        handler(value)
    }
}
