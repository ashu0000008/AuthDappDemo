package com.example.authdappdemo.model

import androidx.annotation.Keep
import com.alibaba.fastjson.annotation.JSONCreator
import com.alibaba.fastjson.annotation.JSONField

@Keep
data class LicenseSimple @JSONCreator constructor(@JSONField(name = "prvKey") val prvKey:String?,
                                                  @JSONField(name = "contractId") val contractId:String?)