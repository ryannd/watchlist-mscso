package com.ryannd.watchlist_mscso.db

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ryannd.watchlist_mscso.db.model.CustomList

class ListDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "list"

    fun createList(list: CustomList, onComplete: () -> Unit) {
        db.collection(rootCollection).add(list).addOnSuccessListener {
            onComplete()
        }
    }

    fun getList(listId: String, onComplete: (doc: DocumentSnapshot) -> Unit) {
        db.collection(rootCollection).document(listId).get().addOnSuccessListener {
            onComplete(it)
        }
    }
}