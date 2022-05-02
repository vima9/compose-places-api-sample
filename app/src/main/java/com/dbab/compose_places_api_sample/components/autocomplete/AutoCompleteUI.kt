package com.dbab.compose_places_api_sample.components



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


private const val TAG = "AutoCompleteTextView"


@Composable
fun <T> AutoCompleteUI(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    useOutlined: Boolean = false,
    colors: TextFieldColors?=null,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {}
) {

    val view = LocalView.current
    val lazyListState = rememberLazyListState()


    LazyColumn(
        state = lazyListState,
        modifier = modifier.heightIn(max = TextFieldDefaults.MinHeight * 6)
    ) {

        item {
            QuerySearch(
                query = query,
                label = queryLabel,
                useOutlined = useOutlined,
                colors = colors,
                onQueryChanged = onQueryChanged,
                onDoneActionClick = {
                    view.clearFocus()
                    onDoneActionClick()
                },
                onClearClick = {
                    onClearClick()
                }
            )
        }

        if (predictions.count() > 0) {
            items(predictions) { prediction ->
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            view.clearFocus()
                            onItemClick(prediction)
                        }
                ) {
                    itemContent(prediction)
                }
            }
        }
    }
}



@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    useOutlined: Boolean,
    query: String,
    label: String,
    colors: TextFieldColors?,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit
) {



    var showClearButton by remember { mutableStateOf(false) }


    if (useOutlined) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    showClearButton = (focusState.isFocused)
                },
            value = query,
            onValueChange = onQueryChanged,
            label = { Text(text = label) },
            textStyle = MaterialTheme.typography.subtitle1,
            singleLine = true,
            trailingIcon = {
                if (showClearButton) {
                    IconButton(onClick = {

                        onClearClick()
                    }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                    }
                }

            },
            keyboardActions = KeyboardActions(onDone = {

                onDoneActionClick()
            }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            colors = colors ?: TextFieldDefaults.outlinedTextFieldColors()
        )

    } else {

        TextField(
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    showClearButton = (focusState.isFocused)
                },
            value = query,
            onValueChange = onQueryChanged,
            label = { Text(text = label) },
            textStyle = MaterialTheme.typography.subtitle1,
            singleLine = true,
            trailingIcon = {
                if (showClearButton) {
                    IconButton(onClick = { onClearClick() }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                    }
                }

            },
            keyboardActions = KeyboardActions(onDone = {
                onDoneActionClick()
            }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            colors = colors ?: TextFieldDefaults.textFieldColors()
        )
    }
}



