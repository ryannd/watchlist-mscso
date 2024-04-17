package com.ryannd.watchlist_mscso.db

import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.MediaEntry
import com.ryannd.watchlist_mscso.db.model.Review
import com.ryannd.watchlist_mscso.db.model.User
import java.util.Locale

class UserDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "users"
    private val entryDbHelper = EntryDbHelper()

    fun searchUsers(searchTerm: String, onComplete: (doc: QuerySnapshot) -> Unit) {
       if (searchTerm != "") {
           db.collection(rootCollection)
               .whereGreaterThanOrEqualTo("userName", searchTerm)
               .whereLessThan("userName", searchTerm + 'z')
               .get().addOnSuccessListener {
                   Log.d("UserDB", it.documents.toString())
                   onComplete(it)
               }
       }
    }
    fun getUserData(uuid: String, onComplete: (doc: DocumentSnapshot) -> Unit) {
        db.collection(rootCollection).document(uuid).get().addOnSuccessListener {
            onComplete(it)
        }
    }

    fun getUserData(onComplete: (doc: DocumentSnapshot) -> Unit) {
        val userUid = Firebase.auth.currentUser?.uid
        if(userUid != null) {
            db.collection(rootCollection).document(userUid).get().addOnSuccessListener(onComplete)
        }
    }

    fun getUserData(list: List<String>, onComplete: (doc: DocumentSnapshot) -> Unit) {
        for(userId in list) {
            getUserData(userId, onComplete)
        }
    }

    fun addNewUser(user: User) {
        db.collection(rootCollection).document(user.userUid).set(user)
    }

    fun handleLogin(uid: String, isAlertShowing: MutableState<Boolean>) {
        db.collection(rootCollection).document(uid).get().addOnSuccessListener { document ->
            Log.d("UserDB", document.toString())
            if(!document.exists()) {
                isAlertShowing.value = true
            }
        }
    }

    fun addReviewToUser(review: Review, onComplete: () -> Unit) {
        val userUid = Firebase.auth.currentUser?.uid
        if(userUid != null) {
            db.collection(rootCollection).document(userUid).update("reviewLookup.${review.tmdbId}", true).addOnSuccessListener {
                onComplete()
            }
        }
    }

    fun deleteReviewFromUser(review: Review, onComplete: () -> Unit) {
        val userUid = Firebase.auth.currentUser?.uid
        if(userUid != null) {
            db.collection(rootCollection).document(userUid).update("reviewLookup.${review.tmdbId}", FieldValue.delete()).addOnSuccessListener {
                onComplete()
            }
        }
    }

    fun addMediaToList(media: Media, mediaUid: String, type: String, status: String, currEpisode: Int?, currSeason: Int?, rating: Int, onDismissRequest: () -> Unit) {
        val userUid = Firebase.auth.currentUser?.uid
        if(userUid != null) {
            val newMediaEntry = MediaEntry(
                mediaUid = mediaUid,
                status = status,
                currentEpisode = if(type == "tv") currEpisode else null,
                currentSeason = if(type == "tv") currSeason else null,
                rating = rating,
                mediaType = type
            )
            val userRef = db.collection(rootCollection).document(userUid)
            entryDbHelper.createEntry(newMediaEntry) {
                userRef.update("watchlist.${status.lowercase(Locale.ROOT)}", FieldValue.arrayUnion(it.id)).addOnSuccessListener {
                    Log.d("UserDB", "Updated watchlist")
                    onDismissRequest()
                }

                userRef.update("listLookup.${media.tmdbId}", it.id)
            }
        }
    }

    fun updateUser(user: User, onSuccess: () -> Unit) {
        val userUid = Firebase.auth.currentUser?.uid
        if(userUid != null) {
            db.collection(rootCollection).document(userUid).set(user).addOnSuccessListener {
                onSuccess()
            }
        }
    }

    fun updateUser(uuid: String, user: User, onSuccess: () -> Unit) {
        db.collection(rootCollection).document(uuid).set(user).addOnSuccessListener {
            onSuccess()
        }
    }

    fun deleteMediaFromList(entryId: String, status: String, tmdbId: String, onSuccess: () -> Unit) {
        val userUid = Firebase.auth.currentUser?.uid
        if(userUid != null) {
            db.collection(rootCollection).document(userUid).get().addOnSuccessListener {
                val user = it.toObject(User::class.java)?.copy()
                if(user != null) {
                    user.listLookup.remove(tmdbId)
                    val list = user.watchlist[status]?.toMutableList()
                    if(list != null) {
                        list.removeAt(list.indexOf(entryId))
                        user.watchlist[status] = list
                    }
                    updateUser(user, onSuccess)
                }
            }
        }
    }
}