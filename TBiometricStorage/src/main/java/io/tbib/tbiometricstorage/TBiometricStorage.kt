package io.tbib.tbiometricstorage

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class TBiometricStorage {
    companion object {
        fun checkIsSupported(context: Context): Boolean {
            val biometricManager = BiometricManager.from(context)

            val canAuthenticate =
                when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                    BiometricManager.BIOMETRIC_SUCCESS -> true
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false // Hardware not available
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false // No biometric hardware
                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false // No biometrics enrolled
                    else -> false // Other errors
                }

            return canAuthenticate
        }

        // Use BiometricPrompt and KeyStore APIs
        suspend fun authenticate(fragment: FragmentActivity, context: Context): Boolean =
            suspendCancellableCoroutine { continuation ->
                val executor = ContextCompat.getMainExecutor(context)
                val biometricPrompt = BiometricPrompt(
                    fragment,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            continuation.resume(true) // Resume with success
                        }

                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            println("error code is $errorCode - message $errString")
                            // Handle error and resume with appropriate value
                        }

                        override fun onAuthenticationFailed() {
                            println("auth failed")
                            // Handle failure and resume with appropriate value
                        }
                    })
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Confirm your identity")
                    .setNegativeButtonText("Cancel")
                    .build()

                biometricPrompt.authenticate(promptInfo)

                continuation.invokeOnCancellation {
                    biometricPrompt.cancelAuthentication() // Cancel if coroutine is cancelled
                }
            }

        // Authentication pending, result will be returned in callback

        fun storeData(context: Context, data: String, fileName: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                _storeData(context, data, fileName)
            } else {
                _storeData(context, data, fileName, "study_body_$fileName")
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun _storeData(context: Context, data: String, fileName: String) {
            val cryptoManager = CryptoManager(context)
            cryptoManager.encryptAndSave(data, fileName)

        }

        private fun _storeData(
            context: Context,
            data: String,
            fileName: String,
            filePassEncrypt: String
        ) {
            val cryptoManager = OldApiCryptoManager(context)
            cryptoManager.encryptAndSave(data, fileName, filePassEncrypt)

        }

         fun retrieveData(context: Context, fileName: String): String? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                _retrieveData(context, fileName)
            } else {
                _retrieveData(context, fileName, "study_body_$fileName")

            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun _retrieveData(context: Context, fileName: String): String? {
            val cryptoManager = CryptoManager(context)
            return cryptoManager.retrieveAndDecrypt(fileName)

        }

        private fun _retrieveData(
            context: Context,
            fileName: String,
            filePassEncrypt: String
        ): String? {
            val cryptoManager = OldApiCryptoManager(context)
            return cryptoManager.retrieveAndDecrypt(fileName, filePassEncrypt)

        }
    }
}