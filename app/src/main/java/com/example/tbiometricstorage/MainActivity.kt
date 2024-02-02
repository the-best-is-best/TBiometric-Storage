package com.example.tbiometricstorage

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.example.tbiometricstorage.ui.theme.TBiometricStorageTheme
import io.tbib.tbiometricstorage.TBiometricStorage

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


                setContent {
                    val activity = LocalContext.current as FragmentActivity
                    val context = LocalContext.current
                        var message by remember {
                        mutableStateOf("")
                        }

//                    LaunchedEffect(Dispatchers.Main) {
//                        val support = TBiometricStorage.checkIsSupported(context)
//                        if (support) {
//                            val auth = TBiometricStorage.authenticate(activity, context)
//                            if (auth) {
//                                TBiometricStorage.storeData(context, "key", "value")
//                            } else {
//                                Log.d("get v", "auth failed")
//                            }
//                        } else {
//                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                                Log.d("get v", "not active biometric")
//
//                            }else {
//                                Log.d("get v", "not support")
//                            }
//                        }
//                    }




                    TBiometricStorageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                  Column(  modifier = Modifier.fillMaxSize(),
                      verticalArrangement =  Arrangement.Center,
                      horizontalAlignment = Alignment.CenterHorizontally,) {
                      ElevatedButton(onClick = {
                          TBiometricStorage.bioAuthenticate(activity, context, onSuccess = {
                              Log.d("get v", "auth success")
                              message = "auth success"
                          }, onFailed = {
                              Log.d("get v", "auth failed")
                              message = "auth failed"
                          }, onError = { code, errString ->
                              message = when (code){
                                  -1 -> "NOT_AVAILABLE"
                                  -2 -> "TEMPORARILY_UNAVAILABLE"
                                  -3 -> "AVAILABLE_BUT_NOT_ENROLLED"
                                  else -> "NOT_AVAILABLE"
                              }
                          })
                      }) {
                          Text(text = "Authenticate")
                      }
                      Greeting(message)
                  }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

        Text(
            text = "$name!",
             fontSize = 30.sp
        )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TBiometricStorageTheme {
        Greeting("Android")
    }
}