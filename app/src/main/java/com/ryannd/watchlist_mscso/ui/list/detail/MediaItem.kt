package com.ryannd.watchlist_mscso.ui.list.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.ryannd.watchlist_mscso.db.model.Media
import java.util.Locale

@Composable
fun MediaItem(
    media: Media,
    navigateTo: (String) -> Unit,
    editOn: Boolean,
    onDelete: (media: Media, onComplete: () -> Unit) -> Unit
) {
    ListItem(
        headlineContent = { Text(media.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        leadingContent = {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w200/" + media.poster,
                contentDescription = media.title,
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp),
                error = rememberVectorPainter(image = Icons.Default.Menu),
                fallback = rememberVectorPainter(image = Icons.Default.Menu),
                contentScale = ContentScale.Crop,
            )
        },
        supportingContent = { Text(media.type.uppercase(Locale.ROOT)) },
        trailingContent = {
            if(editOn) {
                IconButton(
                    onClick = {
                        onDelete(media) {

                        }
                    },
                    colors = IconButtonColors(containerColor = Color("#dd7973".toColorInt()), contentColor = Color.White, disabledContainerColor = Color.Gray, disabledContentColor = Color.Black)
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Delete from list")
                }
            } else {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "To Detail")
            }
        },
        modifier = Modifier.clickable {
            if(!editOn) {
                navigateTo("detail_screen/id=${media.tmdbId}&type=${media.type}")
            }
        }
    )
    HorizontalDivider()
}