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
import com.ryannd.watchlist_mscso.db.model.MediaEntry
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

    fun addMediaToList(mediaUid: String, type: String, status: String, onDismissRequest: () -> Unit) {
        val userUid = Firebase.auth.currentUser?.uid
        if(userUid != null) {
            val newMediaEntry = MediaEntry(
                mediaUid = mediaUid,
                status = status,
                currentEpisode = if(type == "tv") 1 else null,
                currentSeason = if(type == "tv") 1 else null,
                mediaType = type
            )
            val userRef = db.collection(rootCollection).document(userUid)
            entryDbHelper.createEntry(newMediaEntry) {
                userRef.update("watchlist.${status.lowercase(Locale.ROOT)}", FieldValue.arrayUnion(it.id)).addOnSuccessListener {
                    Log.d("UserDB", "Updated watchlist")
                    onDismissRequest()
                }

                userRef.update("listLookup.${mediaUid}", it.id)
            }
        }
    }
}