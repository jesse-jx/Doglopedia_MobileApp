package com.example.doglopedia

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doglopedia.ui.theme.paleYellow
import com.example.doglopedia.ui.theme.darkBrown
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.doglopedia.ui.theme.screens.DogDetailsScreen
import com.example.doglopedia.ui.theme.screens.DogGridScreen
import com.example.doglopedia.ui.theme.screens.FavouritesScreen
import com.example.doglopedia.ui.theme.screens.MoreImagesScreen
import com.example.doglopedia.viewmodel.DoglopediaViewModel

enum class HomeScreen(@StringRes val title: Int) {
    Grid(title = R.string.app_name),
    Detail(title = R.string.dog_detail),
    Favourites(title = R.string.favourited_dogs),
    MoreImages(title = R.string.more_images)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoglopediaAppBar(
    currScreen: HomeScreen,
    canNavigateBack:Boolean,
    navigateUp: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    CenterAlignedTopAppBar(
        title = {Text(stringResource(currScreen.title), style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = darkBrown))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = paleYellow
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = darkBrown
                    )
                }
            }
        },
        actions = {

            IconButton(onClick = {
                navController.navigate(HomeScreen.Favourites.name)
            }) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    tint = darkBrown,
                    contentDescription = "Favourites"
                )
            }
        }
    )
}

@Composable
fun DoglopediaApp(navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = HomeScreen.valueOf(
        backStackEntry?.destination?.route ?: HomeScreen.Grid.name
    )
    val doglopediaViewModel: DoglopediaViewModel = viewModel(factory = DoglopediaViewModel.Factory)

    Scaffold(
        topBar = {
            DoglopediaAppBar(currScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeScreen.Grid.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            composable(route = HomeScreen.Grid.name){
                DogGridScreen(
                    dogListUiState = doglopediaViewModel.dogListUiState,
                    onDogClicked = { dog ->
                        doglopediaViewModel.setSelectedDog(dog)
                        navController.navigate(HomeScreen.Detail.name)
                    },
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }
            composable(route = HomeScreen.Detail.name) {
                DogDetailsScreen(
                    selectedDogUiState = doglopediaViewModel.selectedDogUiState,
                    onMoreImagesClicked = { dog ->
                        doglopediaViewModel.getImageList(dog)
                        navController.navigate(HomeScreen.MoreImages.name)
                    },
                    modifier = Modifier
                )
            }
            composable(route = HomeScreen.MoreImages.name) {
                MoreImagesScreen(
                    imageListUiState = doglopediaViewModel.imageListUiState,
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = HomeScreen.Favourites.name) {
                FavouritesScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

