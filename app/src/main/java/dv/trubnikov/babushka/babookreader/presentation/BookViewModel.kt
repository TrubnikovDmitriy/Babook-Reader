package dv.trubnikov.babushka.babookreader.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dv.trubnikov.babushka.babookreader.domain.BookInteractor
import dv.trubnikov.babushka.babookreader.core.logd
import dv.trubnikov.babushka.babookreader.core.loge
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookInteractor: BookInteractor
) : ViewModel() {

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
