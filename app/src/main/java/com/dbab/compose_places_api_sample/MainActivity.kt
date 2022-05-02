package com.dbab.compose_places_api_sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.dbab.compose_places_api_sample.extensions.getMetadataKey
import com.dbab.compose_places_api_sample.ui.theme.ComposeplacesapisampleTheme
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

const val ANDROID_API_KEY_NAME = "com.google.android.geo.API_KEY"


@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    @OptIn(
        ExperimentalComposeUiApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = packageManager.getMetadataKey(packageName, ANDROID_API_KEY_NAME)
        Places.initialize(applicationContext, apiKey)




        setContent {
            ComposeplacesapisampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val placesDemoViewModel = hiltViewModel<PlacesDemoViewModel>()

                    PlacesDemoUI(placesDemoViewModel)
                }
            }
        }




    }
}
