package dv.trubnikov.babushka.babookreader.presentation

import android.content.Context
import android.content.Intent
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.core.text.scale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kursx.parser.fb2.FictionBook
import dagger.hilt.android.lifecycle.HiltViewModel
import dv.trubnikov.babushka.babookreader.core.Out
import dv.trubnikov.babushka.babookreader.core.logd
import dv.trubnikov.babushka.babookreader.core.loge
import dv.trubnikov.babushka.babookreader.domain.BookInteractor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookInteractor: BookInteractor
) : ViewModel() {

    sealed class ViewState {
        object Error : ViewState()
        object Loading : ViewState()
        data class Success(val bookText: SpannableStringBuilder) : ViewState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, err ->
        loge(err) { "Непредвиденная ошибка, обработать невозможно, глушим экран стабом" }
        viewStateFlow.value = ViewState.Error
    }

    val viewStateFlow = MutableStateFlow<ViewState>(ViewState.Loading)
    val currentPageFlow = MutableStateFlow<Int?>(null)

    init {
        loadBook()
    }

    fun nextPage() {
        currentPageFlow.value?.let {
            currentPageFlow.value = it + 1
        }
    }

    fun prevPage() {
        currentPageFlow.value?.let {
            currentPageFlow.value = max(0, it - 1)
        }
    }

    private fun loadBook() {
        viewModelScope.launch(errorHandler) {
            val book = bookInteractor.loadLastBook()
            book.handleSuccess { buildBookText(it) }
        }
    }

    fun handleUri(intent: Intent, context: Context) {
        val uri = intent.data
        if (uri == null || intent.action != Intent.ACTION_VIEW) {
            if (intent.action == Intent.ACTION_VIEW) {
                viewStateFlow.value = ViewState.Error
            }
            logd { "Не наш случай: action=[${intent.action}], uri=[$uri]" }
            return
        }

        viewStateFlow.value = ViewState.Loading

        viewModelScope.launch(errorHandler) {
            val uriInputStream = context.contentResolver.openInputStream(uri)
            if (uriInputStream == null) {
                viewStateFlow.value = ViewState.Error
                return@launch
            }

            val book = bookInteractor.saveNewBook(uriInputStream)
            book.handleSuccess { buildBookText(it) }
        }
    }

    private fun buildBookText(book: FictionBook) {
        val builder = SpannableStringBuilder()
        builder.scale(1.5f) {
            bold { appendLine(book.title); appendLine(); }
        }
        book.body?.sections?.forEach { section ->
            builder.bold {
                appendLine(section.getTitleString("", ""))
                appendLine()
            }
            section.elements.forEach { line ->
                builder.appendLine("\t\t${line.text}")
            }
        }
        viewStateFlow.value = ViewState.Success(builder)
        currentPageFlow.value = 0
    }

    private fun <T> Out<T>.handleSuccess(handler: (T) -> Unit) {
        when (this) {
            is Out.Success -> handler(value)
            is Out.Failure -> viewStateFlow.value = ViewState.Error
        }
    }
}
