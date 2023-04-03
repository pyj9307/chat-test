package com.example.chat_test2

data class User(
    var name: String,
    var email: String,
    var uId: String
){
    constructor(): this("", "", "")
}