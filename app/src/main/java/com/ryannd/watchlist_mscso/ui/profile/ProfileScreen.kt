package com.ryannd.watchlist_mscso.ui.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.R
import com.ryannd.watchlist_mscso.ui.detail.ObserveLifecycle
import com.ryannd.watchlist_mscso.ui.nav.NavBarState
import com.ryannd.watchlist_mscso.ui.nav.navigateTo

@SuppressLint("UnrememberedMutableState")
@Composable
fun ProfileScreen(
    id: String,
    onComposing: (NavBarState) -> Unit,
    navigateTo: (String) -> Unit,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(id, onComposing)),
) {
    val profileUiState = profileViewModel.uiState.collectAsState()
    profileViewModel.ObserveLifecycle(LocalLifecycleOwner.current.lifecycle)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> uri?.let {
            profileViewModel.addProfilePicture(uri)
        } }
    )

    Column (
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard (modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {
            Column (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    if(id == "") {
                        IconButton(onClick = { profileViewModel.logout() }) {
                            Icon(imageVector = Icons.AutoMirrored.Outlined.ExitToApp , contentDescription = "Logout")
                        }
                    } else {
                        if(!profileUiState.value.isFollowing) {
                            IconButton(onClick = { profileViewModel.follow() }) {
                                Icon(imageVector = Icons.Default.FavoriteBorder , contentDescription = "Follow")
                            }
                        } else {
                            IconButton(onClick = { profileViewModel.unfollow() }) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Follow")
                            }
                        }
                    }
                }

                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.clickable {
                        if(id == ""){
                            launcher.launch("image/*")
                        }
                    }
                ) {
                    if(profileUiState.value.profilePic == "") {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape))
                    } else {
                        AsyncImage(
                            model = profileUiState.value.profilePic,
                            contentDescription = "profile picture",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop)
                    }
                   if(id == ""){
                       Icon(imageVector = Icons.Rounded.AddCircle, contentDescription = "edit profile pic", modifier = Modifier.size(30.dp))
                   }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = profileUiState.value.user?.userName ?: "Please login.")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navigateTo("list_screen?id=${id}")
            },
            modifier = Modifier.fillMaxWidth()
        )
        {
            Text(text = "Watchlist")
        }

        Spacer(modifier = Modifier.height(20.dp))
        
        ReviewSection(reviews = profileUiState.value.reviews)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Following")

        UserSection(users = profileUiState.value.following, navigateTo = navigateTo)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Followers")

        UserSection(users = profileUiState.value.followers, navigateTo = navigateTo)

        Spacer(modifier = Modifier.height(20.dp))
        
        if(profileUiState.value.lists.isNotEmpty()){
            Text(text = "Lists")

            ListSection(lists = profileUiState.value.lists, navigateTo = navigateTo)
        }
    }
}