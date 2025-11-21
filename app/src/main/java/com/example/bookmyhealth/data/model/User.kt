package com.example.bookmyhealth.data.model

data class User(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var age: String = "",
    var address: String = "",
    var imageUrl: String = "",
    var role: String = "User"
)
