package com.github.cookiesjuice.client

import com.github.cookiesjuice.response.Message
import com.github.cookiesjuice.response.message.ReceivedImage
import net.mamoe.mirai.message.data.*

suspend fun convertMiraiToMessage(messageChain: MessageChain): Message {
    val message = Message()
    messageChain.forEach {
        when(it) {
            is At -> {
                message.put(com.github.cookiesjuice.response.message.At(it.target))
            }
            is Image -> {
                message.put(ReceivedImage(it.queryUrl()))
            }
            is PlainText -> {
                message.put(com.github.cookiesjuice.response.message.PlainText(it.content))
            }
            else -> {
                // do nothing for now
            }
        }
    }
    return message;
}