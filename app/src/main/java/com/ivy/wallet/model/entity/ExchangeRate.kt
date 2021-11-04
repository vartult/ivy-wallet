package com.ivy.wallet.model.entity

import androidx.room.Entity

@Entity(tableName = "exchange_rates", primaryKeys = ["baseCurrency", "currency"])
data class ExchangeRate(
    val baseCurrency: String,
    val currency: String,
    val rate: Double,
)