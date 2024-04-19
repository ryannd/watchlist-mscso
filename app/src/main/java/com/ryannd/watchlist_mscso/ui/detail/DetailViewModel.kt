package com.ryannd.watchlist_mscso.ui.detail

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.api.TmdbApi
import com.ryannd.watchlist_mscso.db.EntryDbHelper
import com.ryannd.watchlist_mscso.db.ListDbHelper
import com.ryannd.watchlist_mscso.db.ReviewDbHelper
import com.ryannd.watchlist_mscso.db.UserDbHelper
import com.ryannd.watchlist_mscso.db.model.CustomList
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.MediaEntry
import com.ryannd.watchlist_mscso.db.model.Review
import com.ryannd.watchlist_mscso.db.model.Season
import com.ryannd.watchlist_mscso.db.model.User
import com.ryannd.watchlist_mscso.ui.nav.NavBarState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class DetailViewModel(private val type: String, private val id: String, private val onComposing: (NavBarState) -> Unit) : ViewModel(), DefaultLifecycleObserver {
    private val tmdbApi = TmdbApi.create()
    private val _uiState  = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
    private val userDb = UserDbHelper()
    private val entryDb = EntryDbHelper()
    private val reviewDb = ReviewDbHelper()
    private val listDb = ListDbHelper()
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getDetails()
        getLists()
        createMedia()
    }

    fun addToCustomList(listIds: List<String>, checked: Map<String, Boolean>, onComplete: () -> Unit) {
        val media = createMedia()
        for(id in listIds) {
            listDb.getList(id) {
                val list = it.toObject(CustomList::class.java)
                if(list != null) {
                    val mutableLookup = list.lookup
                    val mutableContent = list.content.toMutableList()
                    if(list.lookup.contains(media.tmdbId)) {
                        if(checked.contains(id) && checked[id] == false) {
                            mutableLookup.remove(media.tmdbId)
                            mutableContent.removeAll {
                                it.tmdbId == media.tmdbId
                            }
                        }
                    } else {
                        if(checked.contains(id) && checked[id] == true) {
                            mutableLookup[media.tmdbId] = true
                            mutableContent.add(media)
                        }
                    }
                    val newList = list.copy(
                        lookup = mutableLookup,
                        content = mutableContent
                    )
                    listDb.updateList(id, newList) {
                        getLists()
                        onComplete()
                    }
                }
            }
        }
    }

    fun addReview(stateObj: DetailUiState, title: String, text: String, liked: Boolean, onComplete: () -> Unit) {
        val review = Review(
            userUid = Firebase.auth.currentUser?.uid ?: "",
            text = text,
            title = title,
            tmdbId = stateObj.tmdbId,
            liked = liked,
            userName = stateObj.userName,
            mediaTitle = stateObj.title,
            poster = stateObj.posterUrl
        )

        reviewDb.addReview(review, onComplete)
    }

    fun deleteReview(stateObj: DetailUiState, onComplete: () -> Unit) {
        if(stateObj.userReview != null) {
            reviewDb.deleteReview(stateObj.userReview!!, onComplete)
        }
    }

    fun addToList(stateObj: DetailUiState, status: String, currEpisode: String, currSeason: String, rating: Int, onDismissRequest: () -> Unit) {
        val seasons = stateObj.seasons?.map { Season(episodeCount = it.episodeCount, seasonNumber = it.seasonNumber, title = it.title) }
        val newMedia = Media(
            type = stateObj.mediaType,
            background = stateObj.backgroundUrl,
            seasons = if(stateObj.mediaType == "tv") seasons else null,
            description = stateObj.description,
            numSeasons = if(stateObj.mediaType == "tv") stateObj.numSeasons else null,
            title = stateObj.title,
            tmdbId = stateObj.tmdbId,
            poster = stateObj.posterUrl
        )
        val episode = if(currEpisode == "") null else currEpisode.toInt()
        val season = if(currSeason == "") null else currEpisode.toInt()
        userDb.addMediaToList(newMedia, newMedia.type, status, episode, season, rating, onDismissRequest)
    }

    fun editEntry(stateObj: DetailUiState, status: String, currEpisode: String, currSeason: String, rating: Int, onDismissRequest: () -> Unit) {
        val episode = if(currEpisode == "") null else currEpisode.toInt()
        val season = if(currSeason == "") null else currEpisode.toInt()

        val entry = stateObj.userEntry?.copy(
            status = status,
            currentSeason = season,
            currentEpisode = episode,
            rating = rating
        )
        if(entry != null) {
            entryDb.updateEntry(entry.firestoreID, entry) {
                onDismissRequest()
            }
        }
    }

    fun deleteEntry(stateObj: DetailUiState, onDismissRequest: () -> Unit) {
        if(stateObj.userEntry != null) {
            entryDb.deleteEntry(stateObj.userEntry!!.firestoreID, stateObj.userEntry!!.status, stateObj.tmdbId, onDismissRequest)
        }
    }

    fun updateEntry() {
        val uuid = Firebase.auth.currentUser?.uid
        if(uuid != null) {
            userDb.getUserData(uuid) {
                val user = it.toObject(User::class.java)
                if(user != null) {
                    _uiState.update {state ->
                        state.copy(userName = user.userName)
                    }

                    if(user.listLookup.contains(_uiState.value.tmdbId)) {
                        val entryId = user.listLookup[_uiState.value.tmdbId]
                        if(entryId != null) {
                            entryDb.getEntry(entryId) {
                                val entry = it.toObject(MediaEntry::class.java)
                                if(entry != null) {
                                    _uiState.update {
                                        it.copy(userEntry = entry)
                                    }
                                }
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(userEntry = null)
                        }
                    }

                    if(user.reviewLookup.contains(_uiState.value.tmdbId)) {
                        reviewDb.getReview(user.userUid, _uiState.value.tmdbId) {
                            val review = it.toObject(Review::class.java)
                            if(review != null) {
                                _uiState.update {
                                    it.copy(userReview = review)
                                }
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(userReview = null)
                        }
                    }
                }
            }
        }
    }

    fun createMedia() : Media {
        val seasons = _uiState.value.seasons?.map { Season(episodeCount = it.episodeCount, seasonNumber = it.seasonNumber, title = it.title) }
        val newMedia = Media(
            type = _uiState.value.mediaType,
            background = _uiState.value.backgroundUrl,
            seasons = if(_uiState.value.mediaType == "tv") seasons else null,
            description = _uiState.value.description,
            numSeasons = if(_uiState.value.mediaType == "tv") _uiState.value.numSeasons else null,
            title = _uiState.value.title,
            tmdbId = _uiState.value.tmdbId,
            poster = _uiState.value.posterUrl
        )
        return newMedia
    }

    private fun getLists() {
        val uuid = Firebase.auth.currentUser?.uid
        if(uuid != null) {
            listDb.getUserLists {lists ->
                _uiState.update {
                    it.copy(
                        userLists = lists
                    )
                }
            }
        }
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
                        mediaType = type,
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
                        mediaType = type,
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
        }.invokeOnCompletion {
            updateEntry()
        }
    }
}

class DetailViewModelFactory(private val type: String, private val id: String, private val onComposing: (NavBarState) -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(type, id, onComposing) as T
}
