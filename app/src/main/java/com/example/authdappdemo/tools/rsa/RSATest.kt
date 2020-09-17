package com.example.authdappdemo.tools.rsa

import android.widget.Toast
import com.example.authdappdemo.MyApplication
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.InvalidKeyException
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException

//测试RSA加密
class RSATest {

    companion object{

        const val RSA = "RSA"
        const val text = "Hello ashu!"

        //公钥加密，私钥解密
        fun test1(){
            try{
                val pair = generateRSAKeyPair(1024)
                val pubKey = Base64.encode(pair?.public?.encoded)
                val prvKey = Base64.encode(pair?.private?.encoded)
                val encodeString = encrypt(pubKey, text, false)
                val decodeString = decrypt(prvKey, encodeString, true)
                Toast.makeText(MyApplication.mContext, decodeString, Toast.LENGTH_LONG).show()
            }catch (e:Exception){
            }
        }
        //私钥加密，公钥解密
        fun test2(){
            try{
                val pair = generateRSAKeyPair(1024)
                val pubKey = Base64.encode(pair?.public?.encoded)
                val prvKey = Base64.encode(pair?.private?.encoded)
                val encodeString = encrypt(prvKey, text, true)
                val decodeString = decrypt(pubKey, encodeString, false)
                Toast.makeText(MyApplication.mContext, decodeString, Toast.LENGTH_LONG).show()
            }catch (e:Exception){
            }
        }

        private fun encrypt(key: String?, plainText: String, isPrv:Boolean): String? {
            try {
                val cipher = Cipher.getInstance(RSA)
                if (isPrv){
                    cipher.init(Cipher.ENCRYPT_MODE, RSAUtils.getPrivateKey(key))
                }else{
                    cipher.init(Cipher.ENCRYPT_MODE, RSAUtils.getPublicKey(key))
                }
                val bytes = plainText.toByteArray()
                val read = ByteArrayInputStream(bytes)
                val write = ByteArrayOutputStream()
                val buf = ByteArray(117)
                var len = 0
                while (read.read(buf).also { len = it } != -1) {
                    var buf1: ByteArray? = null
                    if (buf.size == len) {
                        buf1 = buf
                    } else {
                        buf1 = ByteArray(len)
                        for (i in 0 until len) {
                            buf1[i] = buf[i]
                        }
                    }
                    val bytes1 = cipher.doFinal(buf1)
                    write.write(bytes1)
                }
                return Base64.encode(write.toByteArray())
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        private fun decrypt(key: String?, enStr: String?, isPrv:Boolean): String? {
            try {
                val cipher = Cipher.getInstance(RSA)
                if (isPrv){
                    cipher.init(Cipher.DECRYPT_MODE, RSAUtils.getPrivateKey(key))
                }else{
                    cipher.init(Cipher.DECRYPT_MODE, RSAUtils.getPublicKey(key))
                }
                val bytes = Base64.decode(enStr)
                val read = ByteArrayInputStream(bytes)
                val write = ByteArrayOutputStream()
                val buf = ByteArray(128)
                var len = 0
                while (read.read(buf).also { len = it } != -1) {
                    var buf1: ByteArray? = null
                    if (buf.size == len) {
                        buf1 = buf
                    } else {
                        buf1 = ByteArray(len)
                        for (i in 0 until len) {
                            buf1[i] = buf[i]
                        }
                    }
                    val bytes1 = cipher.doFinal(buf1)
                    write.write(bytes1)
                }
                return String(write.toByteArray())
            } catch (e: InvalidKeyException) {
                e.printStackTrace()
            } catch (e: IllegalBlockSizeException) {
                e.printStackTrace()
            } catch (e: BadPaddingException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return null
        }

        public fun generateRSAKeyPair(keyLength: Int): KeyPair? {
            return try {
                val kpg = KeyPairGenerator.getInstance(RSA)
                kpg.initialize(keyLength)
                kpg.genKeyPair()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                null
            }
        }
    }

}