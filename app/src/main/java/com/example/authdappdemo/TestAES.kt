package com.example.authdappdemo

import android.util.Base64
import android.util.Log
import com.example.authdappdemo.tools.AESCryptTool
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object TestAES {

    private const val password :String = "password"
    private const val message :String = "hello world"

    fun doTest(){
        try {
            val encryptedMsg = AESCryptTool.encrypt(password, message)
            Log.e("TestAES", "encryptedMsg------$encryptedMsg")
            val messageAfterDecrypt = AESCryptTool.decrypt(password, encryptedMsg)
            Log.e("TestAES", "messageAfterDecrypt------$messageAfterDecrypt")

            doTest2()
        } catch (e: GeneralSecurityException) {
            //handle error
            Log.e("", e.toString())
        }
    }

    private fun doTest2(){
        val key = generateKey(password)
        val iv = IvParameterSpec("1234567812345678".toByteArray()).iv
        val cipherText = AESCryptTool.encrypt(key, iv, message.toByteArray(Charsets.UTF_8))
        //NO_WRAP is important as was getting \n at the end
        val encryptedMsg = Base64.encodeToString(cipherText, Base64.NO_WRAP)
        Log.e("TestAES1", "encryptedMsg------$encryptedMsg")
        val messageAfterDecrypt = AESCryptTool.decrypt(key, iv, cipherText)
        val result = String(messageAfterDecrypt, Charsets.UTF_8)
        Log.e("TestAES1", "result------$result")
    }

    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    private fun generateKey(password: String): SecretKeySpec? {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray(Charsets.UTF_8)
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }
}