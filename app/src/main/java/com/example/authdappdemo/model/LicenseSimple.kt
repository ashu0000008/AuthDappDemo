package com.example.authdappdemo.model

import androidx.annotation.Keep
import com.alibaba.fastjson.annotation.JSONCreator
import com.alibaba.fastjson.annotation.JSONField

@Keep
data class LicenseSimple @JSONCreator constructor(
    @JSONField(name = "privateKey") val privateKey: String?,
    @JSONField(name = "licenseId") val licenseId: String?,
    @JSONField(name = "expireAt") val expireAt: String?,
    @JSONField(name = "serviceUrl") val serviceUrl: String?,
    @JSONField(name = "contractAddress") val contractAddress: String?
)