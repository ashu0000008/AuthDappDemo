package com.example.authdappdemo

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.authdappdemo.tools.rsa.Base64
import java.security.KeyPairGenerator
import java.security.KeyStore
import javax.crypto.Cipher


class TestKeyStore {
    companion object {
        private const val KEY_ALIAS = "uuid_key"
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"

        @RequiresApi(Build.VERSION_CODES.M)
        fun getUUID(): String {

            val ks: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
            ks.load(null)
            if (!ks.containsAlias(KEY_ALIAS)) {
                genKey()
            }
            val keyEntry = ks.getEntry(KEY_ALIAS, null)
            if (keyEntry !is KeyStore.PrivateKeyEntry) {
                return ""
            }
            //签名每次数据都不一样，不能使用
//            val signature: ByteArray = Signature.getInstance("SHA256withECDSA").run {
//                initSign(keyEntry.privateKey)
//                update(KEY_ALIAS.toByteArray())
//                sign()
//            }

            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            val pubKey = keyEntry.certificate.publicKey
            Log.e("pubkey", Base64.encode(pubKey.encoded))
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            cipher.update(KEY_ALIAS.toByteArray())
            val encodeData = cipher.doFinal()
            val data = Base64.encode(encodeData)
            Log.e("encode:", data)
            return data
        }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun genKey() {
            val kpg: KeyPairGenerator =
                KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER)
            val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).run {
                setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                build()
            }
            kpg.initialize(parameterSpec)
            val kp = kpg.generateKeyPair()
        }

    }

}