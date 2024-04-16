package com.ryannd.watchlist_mscso.db

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.Review

class MediaDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "media"
    private val userDb = UserDbHelper()

    fun getMedia(uuid: String, onComplete: (doc: DocumentSnapshot) -> Unit) {
        db.collection(rootCollection).document(uuid).get().addOnSuccessListener {
            onComplete(it)
        }
    }
    fun addNewMediaToList(newMedia: Media, status: String, currEpisode: Int?, currSeason: Int?, rating: Int, onDismissRequest: () -> Unit) {
        val tmdbId = newMedia.tmdbId
        db.collection(rootCollection).whereEqualTo("tmdbId", tmdbId).get().addOnSuccessListener { querySnapshot ->
            if(querySnapshot.documents.isEmpty()) {
                db.collection(rootCollection).add(newMedia).addOnSuccessListener {document ->
                    document.get().addOnSuccessListener {
                        userDb.addMediaToList(newMedia, it.id, newMedia.type, status, currEpisode, currSeason, rating, onDismissRequest)
                    }
                    Log.d("MediaDB", "Added new media ${newMedia.title}")
                }
            } else {
                for (document in querySnapshot) {
                    val media = document.toObject(Media::class.java)
                    userDb.addMediaToList(media, document.id, newMedia.type, status, currEpisode, currSeason, rating, onDismissRequest)
                    break
                }
            }

        }
    }
}