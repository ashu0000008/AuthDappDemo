package com.example.authdappdemo.tools.rsa

import android.widget.Toast
import com.example.authdappdemo.MyApplication

//测试RSA验签
class RSATest2 {
    companion object{

        private const val rawText = "Hello world!"

        fun test(){
            try{
                val pair = RSATest.generateRSAKeyPair(1024)
                val pubKey = Base64.encode(pair?.public?.encoded)
                val prvKey = Base64.encode(pair?.private?.encoded)

                val singedString = RSA.sign(rawText, prvKey, "utf-8")
                val result = RSA.verify(rawText, singedString, pubKey, "utf-8")
                val tip = when(result){
                    true->"验证成功"
                    else->"验证失败"
                }
                Toast.makeText(MyApplication.mContext, tip, Toast.LENGTH_LONG).show()
            }catch (e:Exception){
            }
        }
    }
}