package io.tbib.tbiometricstorage

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import javax.crypto.Cipher

@RequiresApi(Build.VERSION_CODES.M)
internal class CryptoManager(private val context: Context) {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    fun encryptAndSave(text: String, fileName: String): ByteArray {
        val keyPair = getKeyPair()
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.public)
        val encryptedBytes = cipher.doFinal(text.toByteArray())
        val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fos.write(encryptedBytes)
        fos.close()
        return encryptedBytes
    }

    fun retrieveAndDecrypt(fileName: String): String? {
        val keyPair = getKeyPair()
        val fileExist = CheckFileExist().fileExists(context, fileName)
        if (!fileExist) {
            return null
        }
        val fis = context.openFileInput(fileName)
        val encryptedBytes = ByteArray(fis.available())
        fis.read(encryptedBytes)
        fis.close()
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    private fun getKeyPair(): KeyPair {
        return if (keyStore.containsAlias("my_key")) {
            val privateKey = keyStore.getKey("my_key", null)
            val publicKey = keyStore.getCertificate("my_key").publicKey
            KeyPair(publicKey, privateKey as java.security.PrivateKey)
        } else {
            val keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore"
            )
            keyPairGenerator.initialize(
                KeyGenParameterSpec.Builder(
                    "my_key", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .build()
            )
            val keyPair = keyPairGenerator.generateKeyPair()
            KeyPair(keyPair.public, keyPair.private)
        }
    }
}
