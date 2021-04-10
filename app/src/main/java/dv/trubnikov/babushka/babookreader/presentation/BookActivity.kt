package dv.trubnikov.babushka.babookreader.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dv.trubnikov.babushka.babookreader.R

@AndroidEntryPoint
class BookActivity : AppCompatActivity() {

    private val viewModel by viewModels<BookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.handleUri(intent, this)
    }
}
