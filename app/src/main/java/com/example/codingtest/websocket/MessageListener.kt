package com.example.codingtest.websocket

interface MessageListener {
    fun  onConnectSuccess ()
    fun  onConnectFailed ()
    fun  onClose ()
    fun onMessage(text: String?)
}
