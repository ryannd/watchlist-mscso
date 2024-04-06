package com.ryannd.watchlist_mscso.ui.search

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryannd.watchlist_mscso.api.SearchResult
import com.ryannd.watchlist_mscso.api.SearchResultRepository
import com.ryannd.watchlist_mscso.api.TmdbApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val tmdbApi = TmdbApi.create()
    private val repository = SearchResultRepository(tmdbApi)
    private val _uiState  = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null
    private fun doSearch(searchTerm: String) {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext + Dispatchers.IO
        ) {
            if(searchTerm != "") {
                val res = repository.getSearch(searchTerm)
                _uiState.value = SearchUiState(searchResults = res)
            } else {
                _uiState.value = SearchUiState(searchResults = listOf())
            }
        }
    }

    fun searchDebounced(searchTerm: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            doSearch(searchTerm)
        }
    }
}