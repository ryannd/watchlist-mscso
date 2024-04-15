package com.ryannd.watchlist_mscso.ui.list

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.toObject
import com.ryannd.watchlist_mscso.db.EntryDbHelper
import com.ryannd.watchlist_mscso.db.MediaDbHelper
import com.ryannd.watchlist_mscso.db.UserDbHelper
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.MediaEntry
import com.ryannd.watchlist_mscso.db.model.User
import com.ryannd.watchlist_mscso.ui.detail.DetailUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListViewModel : ViewModel(), DefaultLifecycleObserver {
    private val _uiState  = MutableStateFlow(ListUiState())
    private val userDbHelper = UserDbHelper()
    private val mediaDbHelper = MediaDbHelper()
    private val entryDbHelper = EntryDbHelper()
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchLists()
    }

    private fun fetchLists() {
        val uuid = Firebase.auth.currentUser?.uid
        if(uuid != null) {
            userDbHelper.getUserData(uuid) {
                val user = it.toObject(User::class.java)
                if(user != null) {
                    if(user.watchlist.completed.isNotEmpty()) {
                        entryDbHelper.getEntries(user.watchlist.completed) { query ->
                            _uiState.update {state ->
                                state.copy(completed = query.documents.mapNotNull {
                                    val entry = it.toObject(MediaEntry::class.java)

                                    if(!_uiState.value.mediaLookup.contains(entry?.mediaUid) && entry?.mediaUid != null) {
                                        mediaDbHelper.getMedia(entry.mediaUid) { doc ->
                                            val media = doc.toObject(Media::class.java)
                                            if(media != null) {
                                                _uiState.value.mediaLookup[entry.mediaUid] = media
                                            }
                                        }
                                    }

                                    entry
                                })
                            }
                        }
                    }

                   if(user.watchlist.planning.isNotEmpty()) {
                       entryDbHelper.getEntries(user.watchlist.planning) { query ->
                           _uiState.update {state ->
                               state.copy(planning = query.documents.mapNotNull {
                                   val entry = it.toObject(MediaEntry::class.java)

                                   if(!_uiState.value.mediaLookup.contains(entry?.mediaUid) && entry?.mediaUid != null) {
                                       mediaDbHelper.getMedia(entry.mediaUid) { doc ->
                                           val media = doc.toObject(Media::class.java)
                                           if(media != null) {
                                               _uiState.value.mediaLookup[entry.mediaUid] = media
                                           }
                                       }
                                   }

                                   entry
                               })
                           }
                       }
                   }

                   if(user.watchlist.watching.isNotEmpty()) {
                       entryDbHelper.getEntries(user.watchlist.watching) { query ->
                           _uiState.update {state ->
                               state.copy(watching = query.documents.mapNotNull {
                                   val entry = it.toObject(MediaEntry::class.java)

                                   if(!_uiState.value.mediaLookup.contains(entry?.mediaUid) && entry?.mediaUid != null) {
                                       mediaDbHelper.getMedia(entry.mediaUid) { doc ->
                                           val media = doc.toObject(Media::class.java)
                                           if(media != null) {
                                               _uiState.value.mediaLookup[entry.mediaUid] = media
                                           }
                                       }
                                   }

                                   entry
                               })
                           }
                       }
                   }
                }
            }
        }
    }
}