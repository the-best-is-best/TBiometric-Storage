package io.tbib.tbiometricstorage

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal class OldApiCryptoManager(private val context: Context) {
    private val ivSize = 16
    private val keySize = 32

    fun encryptAndSave(text: String, fileName: String, fileSecretEncrypt: String): ByteArray {
        val iv = ByteArray(ivSize).apply { SecureRandom().nextBytes(this) }
        val key = ByteArray(keySize).apply { SecureRandom().nextBytes(this) }
        val file = File(context.filesDir, fileSecretEncrypt)
        val outputStream = FileOutputStream(file)
        outputStream.write(key)
        outputStream.close()

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
        val encryptedBytes = cipher.doFinal(text.toByteArray())
        val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fos.write(iv + encryptedBytes)
        fos.close()
        return encryptedBytes
    }

    fun retrieveAndDecrypt(fileName: String, fileSecretEncrypt: String): String? {
        if (!TBiometricStorage().fileExists(context, fileName)) {
            return null
        }
        val fis = context.openFileInput(fileName)
        val iv = ByteArray(ivSize)
        if (!TBiometricStorage().fileExists(context, fileSecretEncrypt)) {
            return null
        }
        val file = File(context.filesDir, fileName)
        val inputStream = FileInputStream(file)
        val key = inputStream.readBytes()
        inputStream.close()

        fis.read(iv)
        val encryptedBytes = ByteArray(fis.available())
        fis.read(encryptedBytes)
        fis.close()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

}