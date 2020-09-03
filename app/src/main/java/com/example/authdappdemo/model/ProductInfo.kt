package com.example.authdappdemo.model

import androidx.annotation.Keep
import com.alibaba.fastjson.annotation.JSONCreator
import com.alibaba.fastjson.annotation.JSONField

@Keep
data class ProductInfo @JSONCreator constructor(@JSONField(name = "name") val name:String?,
                                                @JSONField(name = "provider") val provider:String?)