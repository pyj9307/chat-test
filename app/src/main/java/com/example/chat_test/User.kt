package com.example.chat_test

data class User(
    var name: String,
    var email: String,
    var uId: String
){
    constructor(): this("", "", "")
}