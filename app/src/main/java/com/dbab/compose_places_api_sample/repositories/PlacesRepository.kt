package com.dbab.compose_places_api_sample.repositories

import com.dbab.compose_places_api_sample.utils.Result
import com.dbab.compose_places_api_sample.utils.Result.Success
import com.dbab.compose_places_api_sample.utils.Result.Error
import com.dbab.compose_places_api_sample.models.AddressItem
import com.dbab.compose_places_api_sample.models.PlaceItem
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PlacesRepository @Inject constructor(
private val placesClient: PlacesClient
) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO


    suspend fun getPlacePredictions(query: String): Result<List<PlaceItem>> = withContext(
        ioDispatcher
    ) {

        try {
            val predictedPlaces: ArrayList<PlaceItem> = ArrayList()
            val token = AutocompleteSessionToken.newInstance()
            val predictionsRequest = FindAutocompletePredictionsRequest.builder().apply {
                setQuery(query)
                sessionToken = token
            }.build()


            val autocompletePredictionsTask =
                placesClient.findAutocompletePredictions(predictionsRequest).await()

            val findAutocompletePredictionsResponse =
                autocompletePredictionsTask.autocompletePredictions
            for (prediction in findAutocompletePredictionsResponse) {
                val place = PlaceItem(
                    prediction.placeId,
                    "",
                    "",
                    0.0,
                    0.0,
                    0f,
                    prediction.getFullText(null).toString()
                )
                predictedPlaces.add(place)
            }

            return@withContext Success(
                predictedPlaces
            )

        } catch (ex: Exception) {

            return@withContext Error(ex)
        }
    }


    suspend fun getLocationFromPlace(placeId: String): Result<AddressItem?> =
        withContext(ioDispatcher) {

            try {

                val placeFields = arrayListOf(
                    Place.Field.ID,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS,
                    Place.Field.ADDRESS_COMPONENTS,
                    Place.Field.PHOTO_METADATAS
                )

                val request = FetchPlaceRequest.builder(placeId, placeFields).build()

                val fetchPlaceResponse = placesClient.fetchPlace(request).await()

                val place = fetchPlaceResponse?.place

                if (place == null)
                    return@withContext Success(place)

                val locationItem = getAddressItem(place)
                return@withContext Success(locationItem)

            } catch (ex: Exception) {

                return@withContext Error(ex)
            }
        }

    private fun getAddressItem(place: Place): AddressItem {

        val latLng: LatLng = place.latLng!!
        val addressComponents = place.addressComponents?.asList()

        val photoMetadata = place.photoMetadatas?.get(0)
        photoMetadata?.attributions

        var countryName = ""
        var postalCode = ""
        var city = ""
        var state = ""
        var streetNumber = ""
        var route = ""
        if (addressComponents != null && addressComponents.size > 0) {

            for (addressComponent in addressComponents) {

                if (addressComponent.types.contains("country")) {
                    countryName = addressComponent.name
                } else if (addressComponent.types.contains("postal_code")) {
                    postalCode = addressComponent.name
                } else if (addressComponent.types.contains("locality")) {
                    city = addressComponent.name
                } else if (addressComponent.types.contains("administrative_area_level_1")) {
                    state = addressComponent.name
                } else if (addressComponent.types.contains("street_number")) {
                    streetNumber = addressComponent.name
                } else if (addressComponent.types.contains("route")) {
                    route = addressComponent.name
                }
            }
        }


        return AddressItem(
            "${latLng.latitude},${latLng.longitude}",
            "New Address",
            latLng.latitude,
            latLng.longitude,
            place.address!!,
            "$streetNumber $route",
            city,
            state,
            countryName,
            postalCode,
            "",
            "", Date(),
            false
        )


    }


}