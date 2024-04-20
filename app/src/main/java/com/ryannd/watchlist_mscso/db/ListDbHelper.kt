package com.ryannd.watchlist_mscso.db

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.ryannd.watchlist_mscso.db.model.CustomList
import com.ryannd.watchlist_mscso.db.model.Media

class ListDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "list"

    fun deleteList(listId: String, onComplete: () -> Unit) {
        db.collection(rootCollection).document(listId).delete().addOnSuccessListener {
            onComplete()
        }
    }
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

    fun updateList(listId: String, updatedList: CustomList, onComplete: () -> Unit) {
        Log.d("LIST", updatedList.toString())
        db.collection(rootCollection).document(listId).set(updatedList).addOnSuccessListener {
            onComplete()
        }
    }

    fun getAllLists(onComplete: (results: List<CustomList>) -> Unit) {
        db.collection(rootCollection).get().addOnSuccessListener {
            val results = it.documents.mapNotNull {
                it.toObject(CustomList::class.java)
            }
            onComplete(results)
        }
    }

    fun getUserLists(onComplete: (results: List<CustomList>) -> Unit) {
        if(Firebase.auth.currentUser != null) {
            db.collection(rootCollection).whereEqualTo("userId", Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                val results = it.documents.mapNotNull {
                    it.toObject(CustomList::class.java)
                }
                onComplete(results)
            }
        }
    }

    fun getUserLists(uuid: String, onComplete: (results: List<CustomList>) -> Unit) {
        db.collection(rootCollection).whereEqualTo("userId", uuid).get().addOnSuccessListener {
            val results = it.documents.mapNotNull {
                it.toObject(CustomList::class.java)
            }

            onComplete(results)
        }
    }
}