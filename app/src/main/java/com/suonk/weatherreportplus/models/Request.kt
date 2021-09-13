package com.suonk.weatherreportplus.models

data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)