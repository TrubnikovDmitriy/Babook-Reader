package dv.trubnikov.babushka.babookreader.core

import android.util.Log

const val TAG = "Babook"

inline fun logv(thr: Throwable? = null, message: () -> String) {
    Log.v(TAG, message(), thr)
}

inline fun logd(thr: Throwable? = null, message: () -> String) {
    Log.d(TAG, message(), thr)
}

inline fun logi(thr: Throwable? = null, message: () -> String) {
    Log.i(TAG, message(), thr)
}

inline fun logw(thr: Throwable? = null, message: () -> String) {
    Log.w(TAG, message(), thr)
}

inline fun loge(thr: Throwable? = null, message: () -> String) {
    Log.e(TAG, message(), thr)
}

inline fun wtf(thr: Throwable? = null, message: () -> String) {
    Log.wtf(TAG, message(), thr)
}
