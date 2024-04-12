package com.ryannd.watchlist_mscso.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryannd.watchlist_mscso.api.SearchResultRepository
import com.ryannd.watchlist_mscso.api.TmdbApi
import com.ryannd.watchlist_mscso.db.UserDbHelper
import com.ryannd.watchlist_mscso.db.model.MediaEntry
import com.ryannd.watchlist_mscso.db.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val tmdbApi = TmdbApi.create()
    private val repository = SearchResultRepository(tmdbApi)
    private val _uiState  = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null
    private val userDbHelper = UserDbHelper()
    private fun doMediaSearch(searchTerm: String) {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext + Dispatchers.IO
        ) {
            if(searchTerm != "") {
                val res = repository.getSearch(searchTerm)
                _uiState.value = SearchUiState(mediaSearchResults = res)
            } else {
                _uiState.value = SearchUiState(mediaSearchResults = listOf())
            }
        }
    }

    private fun doPeopleSearch(searchTerm: String) {
        userDbHelper.searchUsers(searchTerm) { query ->
            _uiState.update {state ->
                state.copy(peopleSearchResults = query.documents.mapNotNull {
                    it.toObject(User::class.java)
                })
            }
        }
    }

    fun peopleSearchDebounced(searchTerm: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            doPeopleSearch(searchTerm)
        }
    }

    fun mediaSearchDebounced(searchTerm: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            doMediaSearch(searchTerm)
        }
    }
}