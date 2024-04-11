package com.ryannd.watchlist_mscso.db

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.MediaEntry

class EntryDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "entry"

    fun createEntry(mediaEntry: MediaEntry, onComplete: (doc: DocumentReference) -> Unit) {
        db.collection(rootCollection).add(mediaEntry).addOnSuccessListener {
            onComplete(it)
        }
    }

    fun getEntries(entries: List<String>, onComplete: (query: QuerySnapshot) -> Unit) {
        db.collection(rootCollection).whereIn(FieldPath.documentId(), entries).get().addOnSuccessListener {
            onComplete(it)
        }
    }
}