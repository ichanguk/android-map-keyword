package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.PlaceRepository
import campus.tech.kakao.map.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceViewModel(application: Application) : AndroidViewModel(application) {
    private val placeRepository: PlaceRepository = PlaceRepository(application)
    private val _searchResults = MutableLiveData<List<Place>>()

    val searchResults: LiveData<List<Place>> get() = _searchResults

    fun insertPlace(place: Place) {
        placeRepository.insertPlace(place)
    }

    fun clearAllPlaces() {
        placeRepository.clearAllPlaces()
    }

    private fun clearSearchResults() {
        _searchResults.value = emptyList()
    }

    fun searchPlaces(category: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val results = placeRepository.getPlacesByCategory(category)
                _searchResults.postValue(results)
            }
        }
    }

    init {
        clearSearchResults()
    }
}