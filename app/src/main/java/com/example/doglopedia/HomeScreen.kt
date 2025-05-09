package com.example.doglopedia

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.doglopedia.viewmodel.DogListUiState
import com.example.doglopedia.viewmodel.DoglopediaViewModel

enum class HomeScreen(@StringRes val title: Int) {
    Grid(title = R.string.app_name),
    Detail(title = R.string.dog_detail),
    Favourites(title = R.string.favourited_dogs)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoglopediaAppBar(
    currScreen: com.example.doglopedia.HomeScreen,
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
fun DoglopediaApp(navController: NavHostController = rememberNavController(),
                  doglopediaViewModel: DoglopediaViewModel = viewModel()
) {
    val context = LocalContext.current
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = com.example.doglopedia.HomeScreen.valueOf(
        backStackEntry?.destination?.route ?: com.example.doglopedia.HomeScreen.Grid.name
    )

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
            startDestination = com.example.doglopedia.HomeScreen.Grid.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            composable(route = com.example.doglopedia.HomeScreen.Grid.name){
                DogGridScreen(
                    dogListUiState = doglopediaViewModel.dogListUiState.value,
                    onBreedClicked = { breed ->
                        doglopediaViewModel.setSelectedBreed(breed)
                        navController.navigate(com.example.doglopedia.HomeScreen.Detail.name)
                    },
                    modifier = Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())
                )
            }
            composable(route = com.example.doglopedia.HomeScreen.Detail.name) {
                DogDetailsScreen(
                    selectedBreed = doglopediaViewModel.selectedBreed.value,
                    modifier = Modifier
                )
            }
            composable(route = com.example.doglopedia.HomeScreen.Favourites.name) {
                FavouritesScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

