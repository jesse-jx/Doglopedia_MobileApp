package com.example.doglopedia.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doglopedia.models.DogBreeds
import com.example.doglopedia.models.Image
import kotlinx.coroutines.launch

sealed interface DogListUiState {
    data class Success(val breeds: List<DogBreeds>) : DogListUiState
    object Error : DogListUiState
    object Loading: DogListUiState
}

sealed interface SelectedDogUiState {
    data class Success(val breed: DogBreeds) : SelectedDogUiState
    object Error : SelectedDogUiState
    object Loading: SelectedDogUiState
}

class DoglopediaViewModel : ViewModel() {

    var dogListUiState = mutableStateOf<DogListUiState>(DogListUiState.Loading)
        private set

    init {
        loadDogBreeds()
    }

    private fun loadDogBreeds() {
        viewModelScope.launch {
            try {
                // Normally you'd check for internet connection or use repository
                val breeds = listOf(
                    DogBreeds(1, "Labrador Retriever", Image("https://cdn2.thedogapi.com/images/B1uW7l5VX.jpg")),
                    DogBreeds(2, "German Shepherd", Image("https://cdn2.thedogapi.com/images/SJyBfg5NX.jpg"))
                )
                dogListUiState.value = DogListUiState.Success(breeds)
            } catch (e: Exception) {
                dogListUiState.value = DogListUiState.Error
            }
        }
    }

    fun setSelectedBreed(breed: DogBreeds) {
        _selectedBreed.value = breed
    }

    private val _selectedBreed = mutableStateOf<DogBreeds?>(null)
    val selectedBreed = _selectedBreed
}