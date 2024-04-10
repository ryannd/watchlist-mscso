package com.ryannd.watchlist_mscso.ui.detail

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ryannd.watchlist_mscso.api.TmdbApi
import com.ryannd.watchlist_mscso.ui.nav.NavBarState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class DetailViewModel(private val type: String, private val id: String, private val onComposing: (NavBarState) -> Unit) : ViewModel(), DefaultLifecycleObserver {
    private val tmdbApi = TmdbApi.create()
    private val _uiState  = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getDetails()
    }

    private fun getDetails() {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext + Dispatchers.IO
        ) {
            when(type) {
                "tv" -> {
                    val res = tmdbApi.getShowDetail(id)
                    _uiState.value = DetailUiState(
                        title = res.title,
                        backgroundUrl = res.backgroundUrl,
                        description = res.description,
                        mediaType = "tv",
                        numSeasons = res.numSeasons,
                        posterUrl = res.posterUrl,
                        seasons = res.seasons,
                        tmdbId = res.id.toString()
                    )
                    onComposing(NavBarState(title = res.title, showTopBar = true))
                }
                "movie" -> {
                    val res = tmdbApi.getMovieDetail(id)
                    _uiState.value = DetailUiState(
                        title = res.title,
                        backgroundUrl = res.backgroundUrl,
                        description = res.description,
                        mediaType = "tv",
                        posterUrl = res.posterUrl,
                        runtime = res.runtime,
                        tmdbId = res.id.toString()
                    )
                    onComposing(NavBarState(title = res.title, showTopBar = true))
                }
                else -> {
                    Log.d("Detail Screen", "no type")
                }
            }
        }
    }
}

class DetailViewModelFactory(private val type: String, private val id: String, private val onComposing: (NavBarState) -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(type, id, onComposing) as T
}
