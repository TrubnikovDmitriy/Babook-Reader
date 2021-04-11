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
import dv.trubnikov.babushka.babookreader.presentation.BookViewModel.ViewState.*
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
        data class Success(
            val book: FictionBook,
            val bookText: SpannableStringBuilder,
        ) : ViewState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, err ->
        loge(err) { "Непредвиденная ошибка, обработать невозможно, глушим экран стабом" }
        viewStateFlow.value = Error
    }

    val viewStateFlow = MutableStateFlow<ViewState>(Loading)
    val currentPageFlow = MutableStateFlow<Int?>(null)

    init {
        loadBook()
    }

    fun nextPage() {
        val state = viewStateFlow.value as? Success ?: return
        val page = currentPageFlow.value ?: return
        val nextPage = page + 1

        currentPageFlow.value = nextPage
        viewModelScope.launch {
            bookInteractor.saveBookmark(state.book, nextPage)
        }
    }

    fun prevPage() {
        val state = viewStateFlow.value as? Success ?: return
        val page = currentPageFlow.value ?: return
        val prevPage = max(0, page - 1)

        currentPageFlow.value = prevPage
        viewModelScope.launch {
            bookInteractor.saveBookmark(state.book, prevPage)
        }
    }

    private fun loadBook() {
        viewModelScope.launch(errorHandler) {
            val book = bookInteractor.loadLastBook()
            book.handleSuccess {
                buildBookText(it.fb2)
                currentPageFlow.value = it.page
            }
        }
    }

    fun handleUri(intent: Intent, context: Context) {
        val uri = intent.data
        if (uri == null || intent.action != Intent.ACTION_VIEW) {
            if (intent.action == Intent.ACTION_VIEW) {
                viewStateFlow.value = Error
            }
            logd { "Не наш случай: action=[${intent.action}], uri=[$uri]" }
            return
        }

        viewStateFlow.value = Loading

        viewModelScope.launch(errorHandler) {
            val uriInputStream = context.contentResolver.openInputStream(uri)
            if (uriInputStream == null) {
                viewStateFlow.value = Error
                return@launch
            }

            bookInteractor.saveNewBook(uriInputStream).handleSuccess {
                buildBookText(it.fb2)
                currentPageFlow.value = it.page
            }
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
        viewStateFlow.value = Success(book, builder)
    }

    private fun <T> Out<T>.handleSuccess(handler: (T) -> Unit) {
        when (this) {
            is Out.Success -> handler(value)
            is Out.Failure -> viewStateFlow.value = Error
        }
    }
}
