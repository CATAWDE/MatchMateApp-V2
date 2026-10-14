package com.example.matchmateapp.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.matchmateapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MatchMateViewModel by viewModels()

    private val adapter by lazy {
        MatchMateAdapter { user, status ->
            viewModel.updateStatus(user.id, status)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter
        binding.retryButton.setOnClickListener { adapter.retry() }
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }

        observeMatches()
        observeMessage()
    }

    private fun observeMatches() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.matches.collect { pagingData ->
                        adapter.submitData(pagingData)
                    }
                }

                launch {
                    adapter.loadStateFlow.collect { loadStates ->
                        val refresh = loadStates.refresh

                        binding.progressBar.isVisible = refresh is LoadState.Loading
                        binding.retryButton.isVisible = refresh is LoadState.Error
                        binding.errorText.isVisible = refresh is LoadState.Error
                        binding.emptyText.isVisible =
                            refresh is LoadState.NotLoading && adapter.itemCount == 0

                        binding.swipeRefresh.isRefreshing =
                            loadStates.refresh is LoadState.Loading &&
                                adapter.itemCount > 0
                    }
                }
            }
        }
    }

    private fun observeMessage() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collect { message ->
                    message?.let {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                        viewModel.clearMessage()
                    }
                }
            }
        }
    }
}
