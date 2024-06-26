package com.ryannd.watchlist_mscso.ui.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.ryannd.watchlist_mscso.db.ListDbHelper
import com.ryannd.watchlist_mscso.db.ReviewDbHelper
import com.ryannd.watchlist_mscso.db.UserDbHelper
import com.ryannd.watchlist_mscso.db.model.Review
import com.ryannd.watchlist_mscso.db.model.User
import com.ryannd.watchlist_mscso.ui.detail.DetailViewModel
import com.ryannd.watchlist_mscso.ui.nav.NavBarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    private val id: String,
    private val onComposing: (NavBarState) -> Unit
) : ViewModel(), DefaultLifecycleObserver {
    private val _uiState = MutableStateFlow(ProfileUiState())
    private val userDbHelper = UserDbHelper()
    private val reviewDbHelper = ReviewDbHelper()
    private val listDbHelper = ListDbHelper()
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        fetchUser()
    }
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Firebase.auth.addAuthStateListener(firebaseAuthListener)
    }
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchUser()
    }

    fun logout() {
        Firebase.auth.signOut()
        fetchUser()
    }

    fun follow() {
        if(Firebase.auth.currentUser?.uid != null) {
            _uiState.update {
                it.copy(
                    userName = Firebase.auth.currentUser?.displayName ?: ""
                )
            }
            userDbHelper.getUserData(Firebase.auth.currentUser?.uid!!) {
                val currUser = it.toObject(User::class.java)
                if(currUser != null) {
                    val followList = currUser.followingList.toMutableList()
                    followList.add(id)
                    val updatedCurrUser = currUser.copy(
                        followingList = followList
                    )
                    userDbHelper.updateUser(updatedCurrUser) {
                        val followed = _uiState.value.user
                        if(followed != null) {
                            val followerList = followed.followerList.toMutableList()
                            followerList.add(Firebase.auth.currentUser?.uid.toString())
                            val updatedUser = followed.copy(
                                followerList = followerList
                            )
                            userDbHelper.updateUser(followed.userUid, updatedUser) {
                                fetchUser()
                            }
                        }
                    }
                }


            }
        }
    }

    fun unfollow() {
        if(Firebase.auth.currentUser?.uid != null) {
            userDbHelper.getUserData(Firebase.auth.currentUser?.uid!!) {
                val currUser = it.toObject(User::class.java)
                if(currUser != null) {
                    val followingList = currUser.followingList.toMutableList()
                    val idxOfUser = followingList.indexOf(id)
                    if(idxOfUser != -1) {
                        followingList.removeAt(idxOfUser)
                        val updatedCurrUser = currUser.copy(
                            followingList = followingList
                        )
                        userDbHelper.updateUser(updatedCurrUser) {
                            val followed = _uiState.value.user
                            if(followed != null) {
                                val followerList = followed.followerList.toMutableList()
                                val idxOfCurrUser = followerList.indexOf(Firebase.auth.currentUser?.uid.toString())

                                if(idxOfCurrUser != -1 ) {
                                    followerList.removeAt(idxOfCurrUser)
                                }
                                val updatedUser = followed.copy(
                                    followerList = followerList
                                )
                                userDbHelper.updateUser(followed.userUid, updatedUser) {
                                    fetchUser()
                                }
                            }
                        }
                    }


                }


            }
        }
    }

    fun addProfilePicture(uri: Uri) {
        if(Firebase.auth.currentUser?.uid != null) {
            val id = Firebase.auth.currentUser?.uid
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("images/$id.jpg")
            val uploadTask = imageRef.putFile(uri)

            uploadTask.addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    val updatedUser = uiState.value.user?.copy(
                        profilePic = it.toString()
                    )
                    if(updatedUser != null) {
                        userDbHelper.updateUser(updatedUser) {
                            uiState.value.profilePic = it.toString()
                            fetchUser()
                        }
                    }
                }
            }
        }
    }

    private fun fetchUser() {
        val uuid: String? = if(id == "") {
            Firebase.auth.currentUser?.uid
        } else {
            id
        }

        if(uuid != null) {
            userDbHelper.getUserData(uuid) {
                val user = it.toObject(User::class.java)
                Log.d("ProfileViewModel", user.toString())
                if(user != null) {
                    _uiState.value = ProfileUiState(user = user, profilePic = user.profilePic)

                    if(id == "") {
                        listDbHelper.getUserLists { results ->
                            _uiState.update { state ->
                                state.copy(
                                    lists = results
                                )
                            }
                        }
                    } else {
                        listDbHelper.getUserLists(id) { results ->
                            _uiState.update { state ->
                                state.copy(
                                    lists = results
                                )
                            }

                        }
                    }

                    val reviews = user.reviewLookup.keys.toList()
                    if(reviews.isNotEmpty()) {
                        reviewDbHelper.getReview(user.userUid, reviews) {
                            val review = it.toObject(Review::class.java)
                            if(review != null) {
                                val added = _uiState.value.reviews.toMutableList()
                                if(added.indexOf(review) == -1) {
                                    added.add(review)
                                    _uiState.update {
                                        it.copy(
                                            reviews = added
                                        )
                                    }
                                }

                            }
                        }
                    }

                    if(user.followingList.isNotEmpty()) {
                        userDbHelper.getUserData(user.followingList) {
                            val following = it.toObject(User::class.java)
                            if(following != null) {
                                val added = _uiState.value.following.toMutableList()
                                val idx = added.indexOfFirst {
                                    it.firestoreID == following.firestoreID
                                }
                                if(idx == -1) {
                                    added.add(following)
                                    _uiState.update {
                                        it.copy(
                                            following = added
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if(user.followerList.isNotEmpty()) {
                        userDbHelper.getUserData(user.followerList) {
                            val follower = it.toObject(User::class.java)
                            if(follower != null) {
                                val added = _uiState.value.followers.toMutableList()
                                val idx = added.indexOfFirst {
                                    it.firestoreID == follower.firestoreID
                                }
                                if(idx == -1) {
                                    added.add(follower)
                                    _uiState.update {
                                        it.copy(
                                            followers = added
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if(id != "") {
                        userDbHelper.getUserData {
                            val currUser = it.toObject(User::class.java)
                            if(currUser != null) {
                                val idxOfFollow = currUser.followingList.indexOf(user.userUid)
                                if(idxOfFollow != -1) {
                                    _uiState.update {
                                        it.copy(
                                            isFollowing = true
                                        )
                                    }
                                } else {
                                    _uiState.update {
                                        it.copy(
                                            isFollowing = false
                                        )
                                    }
                                }
                            }
                        }
                        onComposing(NavBarState(title = "${user.userName}'s Profile", showTopBar = true))
                    }
                }
            }
        } else {
            _uiState.value = ProfileUiState(user = null)
        }
    }

}

class ProfileViewModelFactory(private val id: String, private val onComposing: (NavBarState) -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProfileViewModel(id, onComposing) as T
}