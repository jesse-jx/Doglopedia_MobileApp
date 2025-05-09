package com.example.doglopedia.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import com.example.doglopedia.models.DogBreeds

@Composable
fun DogDetailsScreen(
    selectedBreed: DogBreeds?,
    modifier: Modifier = Modifier
) {
    selectedBreed?.let { breed ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = breed.image?.url ?: "",
                contentDescription = breed.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = breed.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            // You can expand this with more breed info if needed
        }
    } ?: run {
        Text(
            text = "Dog details not available.",
            modifier = modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    }
}