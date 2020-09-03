package com.example.authdappdemo.wallet

import java.math.BigInteger

class WalletConfigure {
    companion object{
        const val mMnemonic = "liar rescue hair method cradle pizza unusual whale twin cute into daring"
        val mGasPrice = BigInteger("6000000000")
        val mGasLimit = BigInteger("3000000")
        const val mContractAddress = "0xfFD23A0e058F72932902AcE0d594Ddb4A393bc93"
        const val mContractAddressNew = "0xe0D20b9837B375bB25a3D292DF1A3666D7Ee8604"

        const val mEthNode = "https://rinkeby.infura.io/v3/bebe1ba6263e44279370df5b205e8b9c"
        const val mEthNodePrivate = "http://119.45.254.226:8845"

//        const val mMetaWalletAddress = "0x75C9232a2065F22FC4741c19F6cfbfA56358661B"
        const val mPrvKey = "39f41530f7bd0dfcdf4840eba896d975086cdf1f547a884234ba76b8dc8c92ea"


        //本机唯一标识，可以是钱包地址
        const val mDeviceId = 11111
        const val mContractId = "contract_company_100"
        const val mContractIndex = 2

    }

}