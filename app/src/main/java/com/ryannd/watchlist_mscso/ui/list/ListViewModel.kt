package com.ryannd.watchlist_mscso.ui.list

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.db.ListDbHelper
import com.ryannd.watchlist_mscso.db.model.CustomList

class ListViewModel : ViewModel() {
    val listDbHelper = ListDbHelper()
    fun newList(name: String, onComplete: () -> Unit) {
        val currUser = Firebase.auth.currentUser
        if(currUser != null) {
            val list = CustomList(name = name, userName = currUser.displayName ?: "", userId = currUser.uid)
            listDbHelper.createList(list) {
                onComplete()
            }
        }
    }
}