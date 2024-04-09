package com.ryannd.watchlist_mscso.db

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ryannd.watchlist_mscso.db.model.User

class UserDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "users"

    fun addNewUser(user: User) {
        db.collection(rootCollection).add(user).addOnSuccessListener {
            Log.d("UserDB", "Added user")
        }
    }

    fun handleLogin(uid: String) {
        db.collection(rootCollection).whereEqualTo("userUid", uid).get().addOnSuccessListener {
            if(it.documents.isEmpty()) {
                val newUser = User(userName = "", userUid = uid)
                addNewUser(newUser)
            }
        }
    }
}