package com.dbab.compose_places_api_sample

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.dbab.compose_places_api_sample.components.AutoCompleteUI
import com.dbab.compose_places_api_sample.models.AddressItem
import com.dbab.compose_places_api_sample.models.AddressUIAction
import com.dbab.compose_places_api_sample.models.PlaceItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlacesDemoUI(placesDemoViewModel: PlacesDemoViewModel) {


    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val address by placesDemoViewModel.address.collectAsState()
    val placesPredictions by placesDemoViewModel.placePredictions.collectAsState()
    val showProgressbar by placesDemoViewModel.showProgressBar.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.padding(8.dp)) {


            Text(
                "Places API Demo with Auto Complete",
                modifier = Modifier.padding(vertical = 24.dp),
                fontSize = 32.sp
            )


            AddressEdit(
                addressItem = address,
                modifier = Modifier,
                placesPredictions
            ) { action ->
                when (action) {
                    is AddressUIAction.OnAddressSelected -> {
                        keyboardController?.hide()
                        scope.launch {

                            placesDemoViewModel.setLocationPrediction(action.selectedPlaceItem)
                        }
                    }

                    is AddressUIAction.OnAddressChange -> {
                        scope.launch {
                            placesDemoViewModel.getPlacePredictions(action.address)
                        }
                    }

                    is AddressUIAction.OnAddressAutoCompleteDone -> {

                         keyboardController?.hide()

                    }

                    is AddressUIAction.OnAddressAutoCompleteClear -> {
                        placesDemoViewModel.onLocationAutoCompleteClear()
                    }
                }

            }
        }

        if (showProgressbar) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }


}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddressEdit(
    addressItem: AddressItem,
    modifier: Modifier,
    addressPlaceItemPredictions: List<PlaceItem>,
    addressUIAction: (AddressUIAction) -> Unit
) {



    Column(
        modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {


        Spacer(modifier = Modifier.height(8.dp))

        AutoCompleteUI(
            modifier = Modifier.fillMaxWidth(),
            query = addressItem.streetAddress,
            queryLabel = "Street Address",
            useOutlined = true,
            onQueryChanged = { updatedAddress ->

                addressItem.streetAddress = updatedAddress
                addressUIAction(AddressUIAction.OnAddressChange(updatedAddress))
            },
            predictions = addressPlaceItemPredictions,
            onClearClick = {
                addressUIAction(AddressUIAction.OnAddressAutoCompleteClear)
            },
            onDoneActionClick = {

                addressUIAction(AddressUIAction.OnAddressAutoCompleteDone)

            },
            onItemClick = { placeItem ->


                addressUIAction(
                    AddressUIAction.OnAddressSelected(
                        placeItem
                    )
                )
            }
        ) {

            //
            Text(it.address, fontSize = 14.sp)

        }


        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {


            Row(modifier = modifier.height(IntrinsicSize.Min)) {


                Column(modifier = Modifier.weight(1f)) {

                    Text("City", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        addressItem.city,
                        modifier = Modifier.weight(1.0f)
                    )
                }



                Column(modifier = Modifier.weight(1f)) {

                    Text("State", fontWeight = FontWeight.Bold)


                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        addressItem.state,
                        modifier = Modifier.weight(1.0f)
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = modifier.height(IntrinsicSize.Min)) {

                Column(modifier = Modifier.weight(1f)) {
                    Text("Postal Code", fontWeight = FontWeight.Bold)

                    Text(
                        addressItem.postalCode,
                        modifier = Modifier.weight(1.0f)
                    )
                }


                Column(modifier = Modifier.weight(1f)) {
                    Text("Country", fontWeight = FontWeight.Bold)

                    Text(
                        addressItem.country,
                        modifier = Modifier.weight(1.0f)
                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = modifier.height(IntrinsicSize.Min)) {


                Column(modifier = Modifier.weight(1f)) {

                    Text("Latitude", fontWeight = FontWeight.Bold)



                    Text(
                        addressItem.latitude.toString(),
                        modifier = Modifier.weight(1.0f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Text("Longitude", fontWeight = FontWeight.Bold)

                    Text(
                        addressItem.longitude.toString(),
                        modifier = Modifier.weight(1.0f)
                    )
                }

            }

        }
    }

}