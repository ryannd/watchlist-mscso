package com.ryannd.watchlist_mscso.db

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ryannd.watchlist_mscso.db.model.Media

class MediaDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "media"
    private val userDb = UserDbHelper()

    fun addNewMediaToList(newMedia: Media, status: String, onDismissRequest: () -> Unit) {
        val tmdbId = newMedia.tmdbId
        db.collection(rootCollection).whereEqualTo("tmdbId", tmdbId).get().addOnSuccessListener { querySnapshot ->
            if(querySnapshot.documents.isEmpty()) {
                db.collection(rootCollection).add(newMedia).addOnSuccessListener {document ->
                    document.get().addOnSuccessListener {
                        userDb.addMediaToList(it.id, newMedia.type, status, onDismissRequest)
                    }
                    Log.d("MediaDB", "Added new media ${newMedia.title}")
                }
            } else {
                for (document in querySnapshot) {
                    userDb.addMediaToList(document.id, newMedia.type, status, onDismissRequest)
                    break
                }
            }

        }
    }
}