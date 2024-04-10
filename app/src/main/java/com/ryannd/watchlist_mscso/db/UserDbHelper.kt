package com.ryannd.watchlist_mscso.db

import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ryannd.watchlist_mscso.db.model.MediaEntry
import com.ryannd.watchlist_mscso.db.model.User
import java.util.Locale

class UserDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "users"

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
            userRef.update("watchlist.${status.lowercase(Locale.ROOT)}", FieldValue.arrayUnion(newMediaEntry)).addOnSuccessListener {
                Log.d("UserDB", "Updated watchlist")
                onDismissRequest()
            }
        }
    }
}