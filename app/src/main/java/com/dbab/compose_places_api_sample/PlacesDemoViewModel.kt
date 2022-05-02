package com.dbab.compose_places_api_sample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbab.compose_places_api_sample.models.AddressItem
import com.dbab.compose_places_api_sample.models.PlaceItem
import com.dbab.compose_places_api_sample.repositories.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.dbab.compose_places_api_sample.utils.Result
import kotlinx.coroutines.flow.asStateFlow


private const val TAG = "PlacesDemoViewModel"

@HiltViewModel
class PlacesDemoViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private var _address: MutableStateFlow<AddressItem> = MutableStateFlow(AddressItem())
    private var _placePredictions: MutableStateFlow<List<PlaceItem>> =
        MutableStateFlow(arrayListOf())

    private var _showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)


    var address = _address.asStateFlow()
    var placePredictions = _placePredictions.asStateFlow()
    var showProgressBar = _showProgressBar.asStateFlow()


    fun getPlacePredictions(query: String) {
        _address.value.streetAddress = query
        viewModelScope.launch {

            when (val placePredictionsResult = placesRepository.getPlacePredictions(query)) {
                is Result.Success -> {
                    val placePredictions = placePredictionsResult.data

                    _placePredictions.value = placePredictions
                }

                is Result.Error -> {
                    Log.e(
                        TAG,
                        "An error occurred when retrieving the predictions for $query",
                        placePredictionsResult.exception
                    )
                }
                else -> {//does not apply here
                }
            }
        }
    }


    suspend fun setLocationPrediction(placeItem: PlaceItem) {

        viewModelScope.launch {

            when (val addressResult = placesRepository.getLocationFromPlace(placeItem.id!!)) {

                is Result.Success -> {
                    val addressFromPlace = addressResult.data
                    if (addressFromPlace != null) {
                        _address.value = addressFromPlace
                    }

                    clearPredictions()
                }

                is Result.Error -> {
                    Log.e(
                        TAG,
                        "An error occurred when retrieving the address from Place  ${placeItem.id}",
                        addressResult.exception
                    )
                }

                else -> {
                }


            }


        }


    }

    fun onLocationAutoCompleteClear() {
        viewModelScope.launch {

            _address.value = AddressItem()
            clearPredictions()
        }
    }


    private fun clearPredictions() {
        _placePredictions.value = mutableListOf()
    }
}