package com.example.authdappdemo.model

import androidx.annotation.Keep
import com.alibaba.fastjson.annotation.JSONCreator
import com.alibaba.fastjson.annotation.JSONField

@Keep
data class DeviceInfo @JSONCreator constructor(
    @JSONField(name = "brand") val brand: String,
    @JSONField(name = "model") val model: String,
    @JSONField(name = "version") val version: String,
    @JSONField(name = "androidId") val androidId: String
)