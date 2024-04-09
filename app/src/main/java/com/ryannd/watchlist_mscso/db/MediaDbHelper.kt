package com.ryannd.watchlist_mscso.db

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ryannd.watchlist_mscso.db.model.Media

class MediaDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "media"

    private fun limitAndGet(query: Query,
                            resultListener: (List<Media>)->Unit) {
        query
            .limit(100)
            .get()
            .addOnSuccessListener { result ->
                // NB: This is done on a background thread
                resultListener(result.documents.mapNotNull {
                    it.toObject(Media::class.java)
                })
            }
            .addOnFailureListener {
                resultListener(listOf())
            }
    }
}