package com.ryannd.watchlist_mscso.auth

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.firebase.ui.auth.AuthUI
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.db.UserDbHelper

class FirebaseAuthenticator(private val registry: ActivityResultRegistry) :
    DefaultLifecycleObserver,
    FirebaseAuth.AuthStateListener {

    private lateinit var signInLauncher: ActivityResultLauncher<Intent>
    private var pendingLogin = false
    val userDbHelper = UserDbHelper()
    init {
        Firebase.auth.addAuthStateListener(this)
    }
    override fun onAuthStateChanged(p0: FirebaseAuth) {
        val firebaseUser = p0.currentUser
        if(firebaseUser == null) {
            login()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        signInLauncher = registry.register("key", owner, FirebaseAuthUIActivityResultContract()) { result ->
            this.onSignInResult(result)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            if(user != null) {
                userDbHelper.handleLogin(user.uid)
            }
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private fun user(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    private fun login() {
        if(user() == null && !pendingLogin) {
            pendingLogin = true
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build()
            )

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
            signInLauncher.launch(signInIntent)
        }
    }
}