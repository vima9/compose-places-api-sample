package com.dbab.compose_places_api_sample.models

sealed class AddressUIAction {

    object OnAddressAutoCompleteDone : AddressUIAction()
    object OnAddressAutoCompleteClear : AddressUIAction()
    data class OnAddressChange(val address: String) : AddressUIAction()
    data class OnAddressSelected(val selectedPlaceItem: PlaceItem) : AddressUIAction()
}