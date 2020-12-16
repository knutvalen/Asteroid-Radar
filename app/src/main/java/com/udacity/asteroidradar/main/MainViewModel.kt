package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Repository
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domainmodels.Asteroid
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = Repository(database)
    val asteroids = repository.asteroids
    val pictureOfTheDay = repository.pictureOfTheDay
    val apiError = repository.apiError

    private val _selectedAsteroid = MutableLiveData<Asteroid>()
    val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid

    init {
        viewModelScope.launch {
            repository.refreshAsteroids()
            repository.refreshPictureOfTheDay()
        }
    }

    fun displayDetails(asteroid: Asteroid) {
        _selectedAsteroid.value = asteroid
    }

    fun displayDetailsComplete() {
        _selectedAsteroid.value = null
    }

    fun displayErrorMessageComplete() {
        repository.resetErrorMessage()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}