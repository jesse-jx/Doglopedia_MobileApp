package com.example.doglopedia.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.doglopedia.DoglopediaApplication
import com.example.doglopedia.database.DogsRepository
import com.example.doglopedia.models.Dog
import com.example.doglopedia.models.Image
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface DogListUiState {
    data class Success(val dogs: List<Dog>) : DogListUiState
    object Error : DogListUiState
    object Loading: DogListUiState
}

sealed interface SelectedDogUiState {
    data class Success(val dog: Dog) : SelectedDogUiState
    object Error : SelectedDogUiState
    object Loading: SelectedDogUiState
}

sealed interface ImageListUiState {
    data class Success(val images: List<Image>) : ImageListUiState
    object Loading : ImageListUiState
    object Error : ImageListUiState
}

class DoglopediaViewModel(private val dogsRepository: DogsRepository) : ViewModel() {

    var dogListUiState: DogListUiState by mutableStateOf(DogListUiState.Loading)
        private set

    var selectedDogUiState: SelectedDogUiState by mutableStateOf(SelectedDogUiState.Loading)
        private set

    var imageListUiState: ImageListUiState by mutableStateOf(ImageListUiState.Loading)
        private set

    init {
        getDogs()
    }

    fun getDogs() {
        viewModelScope.launch {
            dogListUiState = DogListUiState.Loading
            dogListUiState = try {
                val dogs = dogsRepository.getDogs()
                DogListUiState.Success(dogs)
            } catch (e: IOException) {
                DogListUiState.Error
            } catch (e: HttpException) {
                DogListUiState.Error
            }
        }
    }

    fun setSelectedDog(dog: Dog) {
        viewModelScope.launch {
            selectedDogUiState = SelectedDogUiState.Loading
            selectedDogUiState = try {
                SelectedDogUiState.Success(dog)
            } catch (e: IOException) {
                SelectedDogUiState.Error
            } catch (e: HttpException) {
                SelectedDogUiState.Error
            }
        }
    }

    fun getImageList(dog: Dog) {
        viewModelScope.launch {
            imageListUiState = ImageListUiState.Loading
            imageListUiState = try {
                val moreImages = dogsRepository.getDogImagesById(dog.id)
                ImageListUiState.Success(moreImages)
            } catch (e: IOException) {
                ImageListUiState.Error
            } catch (e: HttpException) {
                ImageListUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DoglopediaApplication)
                val dogsRepository = application.container.dogsRepository
                DoglopediaViewModel(dogsRepository = dogsRepository)
            }
        }
    }
}