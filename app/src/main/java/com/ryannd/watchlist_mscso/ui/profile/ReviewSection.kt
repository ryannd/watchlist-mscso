package com.ryannd.watchlist_mscso.ui.profile

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.ryannd.watchlist_mscso.db.model.Review
import com.ryannd.watchlist_mscso.ui.components.ReviewViewDialog
import com.ryannd.watchlist_mscso.ui.components.ThumbsDown
import com.ryannd.watchlist_mscso.ui.components.ThumbsUp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReviewSection(reviews: List<Review>) {
    val pagerState = rememberPagerState(pageCount = {
        reviews.size
    })

    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    var dialogReview by remember {
        mutableStateOf<Review?>(null)
    }

    if(isDialogOpen && dialogReview != null) {
        ReviewViewDialog(dialogReview) {
            dialogReview = null
            isDialogOpen = false
        }
    }

    Text(text = "Reviews")

    Spacer(modifier = Modifier.height(20.dp))

    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fixed(150.dp),
                pageSpacing = 15.dp
            ) { page ->
                val curr = reviews[page]
                Card(
                    modifier = Modifier.clickable {
                        dialogReview = curr
                        isDialogOpen = true
                    }
                ) {
                    Box(modifier = Modifier.height(250.dp)) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w200/" + curr.poster,
                            contentDescription = curr.mediaTitle,
                            error = rememberVectorPainter(image = Icons.Default.Menu),
                            fallback = rememberVectorPainter(image = Icons.Default.Menu),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.width(200.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black
                                        ),
                                        startY = 100f
                                    )
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = curr.title, fontWeight = FontWeight.Bold, fontSize = 20.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.width(100.dp))
                                if(curr.liked){
                                    Icon(imageVector = ThumbsUp, contentDescription = "Liked", modifier = Modifier.size(25.dp))
                                } else {
                                    Icon(imageVector = ThumbsDown, contentDescription = "Disliked", modifier = Modifier.size(25.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}