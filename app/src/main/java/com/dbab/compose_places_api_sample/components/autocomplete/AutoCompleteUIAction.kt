package com.dbab.compose_places_api_sample.components.autocomplete

sealed class AutoCompleteUIAction {
    object OnDoneClick : AutoCompleteUIAction()
    object OnClearClick : AutoCompleteUIAction()

    data class OnQueryChange(val query: String) : AutoCompleteUIAction()
    data class OnItemSelected<T>(val selectedItem: T) : AutoCompleteUIAction()
}