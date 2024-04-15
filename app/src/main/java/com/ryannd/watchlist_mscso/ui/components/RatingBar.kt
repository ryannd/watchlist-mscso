package com.ryannd.watchlist_mscso.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    stars: Int = 5,
    starsColor: Color = Color.Yellow,
    enabled: Boolean,
    onClick: (rating: Int) -> Unit
) {
    Row(modifier = modifier
        .height(30.dp)) {
        repeat(stars) {
            val normalize = it + 1
            if(normalize <= rating) {
                Icon(imageVector = StarIcon, contentDescription = null, tint = starsColor, modifier = Modifier.clickable { if(enabled) onClick(normalize) })
            } else {
                Icon(imageVector = OutlineStar, contentDescription = null, tint = starsColor, modifier = Modifier.clickable {  if(enabled) onClick(normalize) })
            }
        }

    }
}