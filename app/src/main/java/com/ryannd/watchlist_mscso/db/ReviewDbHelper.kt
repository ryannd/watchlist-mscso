package com.ryannd.watchlist_mscso.db

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ryannd.watchlist_mscso.db.model.Review

class ReviewDbHelper {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val rootCollection = "review"
    private val userDb = UserDbHelper()

    fun deleteReview(review: Review, onComplete: () -> Unit) {
        db.collection(rootCollection).document(review.tmdbId).collection("reviews").document(review.userUid).delete().addOnSuccessListener {
            userDb.deleteReviewFromUser(review, onComplete)
        }
    }
    fun addReview(review: Review, onComplete: () -> Unit) {
        db.collection(rootCollection).document(review.tmdbId).collection("reviews").document(review.userUid).set(review).addOnSuccessListener {
            userDb.addReviewToUser(review, onComplete)
        }
    }

    fun getReview(userUid: String, tmdbId: String, onComplete: (doc: DocumentSnapshot) -> Unit) {
        db.collection(rootCollection).document(tmdbId).collection("reviews").document(userUid).get().addOnSuccessListener {
            onComplete(it)
        }
    }
}