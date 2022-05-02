package com.dbab.compose_places_api_sample.models

data class PlaceItem(
    val id: String?,
    var name: String?,
    val icon: String?,
    val latitude: Double,
    val longitude: Double,
    val distanceFromLocation: Float,
    val address: String
) {


    override fun toString(): String {
        return address
    }
}
