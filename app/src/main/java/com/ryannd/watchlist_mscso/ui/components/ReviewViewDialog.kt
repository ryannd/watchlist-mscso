package com.ryannd.watchlist_mscso.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ryannd.watchlist_mscso.db.model.Review

@Composable
fun ReviewViewDialog(
    review: Review?,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(text = "Review: ${review!!.mediaTitle}")
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = review.title, fontSize = 25.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = review.text)
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if(review.liked) {
                        Text(text = "Recommended by: ${review.userName}")
                        Icon(imageVector = ThumbsUp, contentDescription = "Liked", modifier = Modifier.size(25.dp))
                    } else {
                        Text(text = "Not recommended by: ${review.userName}")
                        Icon(imageVector = ThumbsDown, contentDescription = "Disliked", modifier = Modifier.size(25.dp))
                    }
                }
            }
        }
    }
}