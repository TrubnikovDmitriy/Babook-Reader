package dv.trubnikov.babushka.babookreader.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import dagger.hilt.android.AndroidEntryPoint
import dv.trubnikov.babushka.babookreader.R
import dv.trubnikov.babushka.babookreader.core.logd
import dv.trubnikov.babushka.babookreader.databinding.ActivityBookBinding
import dv.trubnikov.babushka.babookreader.presentation.BookViewModel.ViewState.Error
import dv.trubnikov.babushka.babookreader.presentation.BookViewModel.ViewState.Loading
import dv.trubnikov.babushka.babookreader.presentation.BookViewModel.ViewState.Success
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class BookActivity : AppCompatActivity() {

    private val viewModel by viewModels<BookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStateHandlers(binding)
        setupButtonListeners(binding)

        viewModel.handleUri(intent, this)
    }

    private fun setupStateHandlers(binding: ActivityBookBinding) {
        addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.viewStateFlow
                .onEach { handleState(binding, it) }
                .launchIn(this)
        }
        addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.currentPageFlow
                .filterNotNull()
                .onEach { changePage(binding, it) }
                .launchIn(this)
        }
    }

    private fun setupButtonListeners(binding: ActivityBookBinding) {
        binding.bookStateSuccess.bookLeftButton.setOnClickListener { viewModel.prevPage() }
        binding.bookStateSuccess.bookRightButton.setOnClickListener { viewModel.nextPage() }
    }

    private fun handleState(binding: ActivityBookBinding, state: BookViewModel.ViewState) {
        binding.bookStateSuccess.root.isVisible = state is Success
        binding.bookStateLoading.root.isVisible = state == Loading
        binding.bookStateError.root.isVisible = state == Error

        if (state is Success) {
            binding.bookStateSuccess.bookPageText.text = state.bookText
            binding.bookStateSuccess.bookPageText.post {
                viewModel.currentPageFlow.value?.let { changePage(binding, it) }
            }
        }
    }

    private fun changePage(binding: ActivityBookBinding, currentPage: Int) {
        val totalPageCount = binding.bookStateSuccess.bookPageText.size()
        val pagination = getString(R.string.book_pagination, currentPage + 1, totalPageCount)
        binding.bookStateSuccess.bookPageNumber.text = pagination
        binding.bookStateSuccess.bookPageText.next(currentPage)
    }
}
