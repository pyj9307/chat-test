package com.example.chat_test

data class Message(
    var message: String?,
    var sendId: String?
){
    constructor():this("","")
}