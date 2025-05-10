package com.example.doglopedia.ui.theme.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.doglopedia.models.Dog
import com.example.doglopedia.utils.CONSTANTS
import com.example.doglopedia.viewmodel.SelectedDogUiState

@Composable
fun DogDetailsScreen(
    selectedDogUiState: SelectedDogUiState,
    onMoreImagesClicked: (Dog) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        when (selectedDogUiState) {
            is SelectedDogUiState.Success -> {
                val dog = selectedDogUiState.dog
                val wikiUrl = "https://en.wikipedia.org/wiki/${dog.name.toSnakeCase()}"

                Spacer(modifier = Modifier.height(10.dp))

                // Name and Wikipedia link in the same line
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dog.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl))
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.OpenInNew,
                            contentDescription = "Open Wikipedia",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Image
                Box(modifier = modifier) {
                    AsyncImage(
                        model = CONSTANTS.IMAGE_BASE_URL + dog.refImgId + ".jpg",
                        contentDescription = dog.name,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Dog Info Table
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DogInfoRow(label = "Temperament", value = dog.temperament)
                    DogInfoRow(label = "Origin", value = dog.origin)
                    DogInfoRow(label = "Lifespan", value = dog.lifeSpan)
                    DogInfoRow(label = "Breed Group", value = dog.breedGroup)
                    DogInfoRow(label = "Bred For", value = dog.bredFor)
                    DogInfoRow(label = "Weight", value = "${dog.weight.metric} kg")
                    DogInfoRow(label = "Height", value = "${dog.height.metric} cm")
                    DogInfoRow(label = "Description", value = dog.description)
                }
                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { onMoreImagesClicked(dog) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD9B225)
                        )
                    ) {
                        Text("More Images →")
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))

                TrailerList(
                    videos = listOf(
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                    )
                )
            }

            is SelectedDogUiState.Loading -> {
                Text(
                    text = "loading...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }

            is SelectedDogUiState.Error -> {
                Text(
                    text = "Error!",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun DogInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.width(10.dp))
        if (value.isEmpty())
        {
            Text (
                text = "-",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(2f)
            )
        } else {
            Text (
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(2f)
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    context: Context,
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT // <- Important!
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // no hardcoded height
    )
}

@Composable
fun TrailerList(videos: List<String>) {
    val context = LocalContext.current

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(videos) { videoUrl ->
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .aspectRatio(16f / 9f)
            ) {
                VideoPlayer(
                    context = context,
                    videoUrl = videoUrl,
                    modifier = Modifier
                        .width(250.dp)
                        .aspectRatio(16f / 9f)
                )
            }
        }
    }
}

fun String.toSnakeCase(): String {
    return this.trim().replace("\\s+".toRegex(), "_")
}