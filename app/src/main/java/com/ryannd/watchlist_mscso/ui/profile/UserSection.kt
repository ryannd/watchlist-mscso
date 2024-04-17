package com.ryannd.watchlist_mscso.ui.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ryannd.watchlist_mscso.R
import com.ryannd.watchlist_mscso.db.model.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserSection(
    users: List<User>,
    navigateTo: (String) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = {
        users.size
    })

    Spacer(modifier = Modifier.height(20.dp))

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fixed(85.dp),
                pageSpacing = 15.dp,
                modifier = Modifier.fillMaxHeight()
            ) { page ->
                val curr = users[page]
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .clickable {
                            navigateTo("user?id=${curr.firestoreID}")
                        }
                )
            }
        }
    }
}