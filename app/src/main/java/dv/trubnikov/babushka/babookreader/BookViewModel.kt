package dv.trubnikov.babushka.babookreader

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dv.trubnikov.babushka.babookreader.utils.logd
import dv.trubnikov.babushka.babookreader.utils.loge
import dv.trubnikov.babushka.babookreader.utils.logw
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel() : ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, err ->
        loge(err) { "Непредвиденная ошибка, обработать невозможно, глушим экран стабом" }
    }

    fun handleUri(intent: Intent, context: Context) {
        val uri = intent.data
        if (uri == null || intent.action != Intent.ACTION_VIEW) {
            logd { "Не наш случай: action=[${intent.action}], uri=[$uri]" }
            return
        }

        viewModelScope.launch(Dispatchers.IO + errorHandler) {

        }
    }
}
