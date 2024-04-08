package com.ryannd.watchlist_mscso.ui.detail

import com.ryannd.watchlist_mscso.api.data.MovieDetail
import com.ryannd.watchlist_mscso.api.data.ShowDetail

data class DetailUiState(
    var showDetail: ShowDetail? = null,
    var movieDetail: MovieDetail? = null
)