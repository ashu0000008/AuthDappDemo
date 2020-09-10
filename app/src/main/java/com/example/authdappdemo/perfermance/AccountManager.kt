package com.example.authdappdemo.perfermance

import org.web3j.crypto.Credentials
import java.util.*

object AccountManager {
    private val prvKey = mutableListOf<String>()
    private val wallets = mutableListOf<Credentials>()
    private val random = Random()

    init {
        prvKey.add("09f6c70430d51de75c772bdbe5ca1fe5ea2602197f4074255a3a7618aa0d4794")
        prvKey.add("48299b0801b81f00014948a8ee87822e5e6d6baf72783242ef511cc223db6053")
        prvKey.add("24ff77dba88fcce4f300b6277eff0e20d93156724e539e160d89d8986805ff45")
        prvKey.add("7ed8c56787c1a3161474ccdc11c4210ccead8707c0eee7b482981037d0ba5e61")
        prvKey.add("da0d437e42ba6047d5eb02792d8ef25d607b34ad9302cd3ba04bc45f15f2a1fe")

        for (key in prvKey){
            wallets.add(Credentials.create(key))
        }
    }

    @Synchronized
    fun getWallet() :Credentials{
        val index = random.nextInt() % 5
        return wallets[index]
    }
}