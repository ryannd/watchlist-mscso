package com.ryannd.watchlist_mscso.ui.detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt

@Composable
fun ReviewDialog(
    stateObj: DetailUiState,
    onAdd: (stateObj: DetailUiState, title: String, text: String, liked: Boolean, onComplete: () -> Unit) -> Unit,
    onDelete: (stateObj: DetailUiState, onComplete: () -> Unit) -> Unit,
    onDismissRequest: () -> Unit,
) {

    var title by remember {
        mutableStateOf(stateObj.userReview?.title ?: "")
    }

    var text by remember {
        mutableStateOf(stateObj.userReview?.text ?: "")
    }

    var liked by remember {
        mutableStateOf(stateObj.userReview?.liked ?: false)
    }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
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
                Text(
                    text = "${if(stateObj.userReview != null) "Edit" else "Add"} Review: ${stateObj.title}",
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.size(20.dp))

                TextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Title") },
                )

                Spacer(modifier = Modifier.size(20.dp))

                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    label = { Text(text = "Description") },
                    singleLine = false,

                )

                Spacer(modifier = Modifier.size(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Do you recommend this title?")
                    Switch(checked = liked, onCheckedChange = { liked = it })
                }

                Spacer(modifier = Modifier.size(20.dp))

                Button(
                    onClick = {
                        onDismissRequest()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text(text = "CANCEL")
                }

                Button(
                    onClick = {
                        onAdd(stateObj, title,  text, liked, onDismissRequest)
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color("#5dbea3".toColorInt()))
                ) {
                    if(stateObj.userReview != null) {
                        Text(text = "EDIT")
                    } else {
                        Text(text = "ADD")
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

                if(stateObj.userReview != null) {
                    Button(
                        onClick = {
                            onDelete(stateObj, onDismissRequest)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color("#dd7973".toColorInt()))
                    ) {
                        Text(text = "DELETE")
                    }
                }


            }
        }
    }
}