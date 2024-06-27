package com.example.cuzzapp.response

import com.squareup.moshi.Json

data class PostManResponse (
    @field:Json(name = "url") val url: String?
)