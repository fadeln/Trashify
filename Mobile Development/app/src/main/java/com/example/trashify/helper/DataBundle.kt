package com.example.trashify.helper

import java.io.Serializable

data class DetailData(val nama: String, val image: String, val description: String, val time: String):
    Serializable

data class AuthData(var userId: String? = null, var nama: String? = null, var token: String? = null):
    Serializable