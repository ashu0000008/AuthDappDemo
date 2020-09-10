package com.example.authdappdemo.perfermance

import java.math.BigInteger

class Configure2 {
    companion object{
        const val mPrvKey = "09f6c70430d51de75c772bdbe5ca1fe5ea2602197f4074255a3a7618aa0d4794"
        const val mContractAddress = "0x83821433B41F09D58EE5B82E916dC365a9f50390"
        const val mEthNodePrivate = "http://119.45.254.226:8845"
        val mGasPrice = BigInteger("2000000000")
        const val mContractId = "performance"

        const val mAuthAmount :Long = 10000
        const val mDeviceIdPrefix = "2000"
    }
}