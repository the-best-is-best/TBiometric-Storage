package com.example.tbiometricstorage

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.example.tbiometricstorage.ui.theme.TBiometricStorageTheme
import io.tbib.tbiometricstorage.TBiometricStorage
import kotlinx.coroutines.Dispatchers

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

                setContent {
                    val activity = LocalContext.current as FragmentActivity
                    val context = LocalContext.current

                    LaunchedEffect(Dispatchers.Main) {
                        val support = TBiometricStorage.checkIsSupported(context)
                        if (support) {
                            val auth = TBiometricStorage.authenticate(activity, context)
                            if (auth) {
                                TBiometricStorage.storeData(context, "key", "value")
                            } else {
                                Log.d("get v", "auth failed")
                            }
                        } else {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                Log.d("get v", "not active biometric")

                            }else {
                                Log.d("get v", "not support")
                            }
                        }
                    }

                    TBiometricStorageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TBiometricStorageTheme {
        Greeting("Android")
    }
}