package com.github.cookiesjuice.client

import com.github.cookiesjuice.controller.LoginController
import com.github.cookiesjuice.controller.MessageController
import com.github.cookiesjuice.service.impl.SetuServiceImpl
import com.github.cookiesjuice.service.impl.TencentAPIServiceImpl
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.join
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.content

suspend fun main() {
    val loginController = LoginController()

    val messageController = MessageController(
        SetuServiceImpl(), TencentAPIServiceImpl()
    )

    val qid = loginController.id
    val pwd = loginController.password

    val bot = Bot(qid, pwd).alsoLogin()
    bot.subscribeAlways<GroupMessageEvent> { event ->

        //处理at消息
        if (message[At]?.target == qid) {
            //获得消息内容，因为at消息为“@xxx content...”，所以需要过滤掉“@xxx”
            val content = message.content.replace(message[At]!!.display, "")
            val replyMsg = MessageBuilder.buildFromMessage(messageController.handleAtMessage(content))
            if(replyMsg !== null){
                event.reply(replyMsg)
            }
        } else {
            val content = message.content
            val replyMsg = MessageBuilder.buildFromMessage(messageController.handlePlainMessage(content))
            if(replyMsg !== null){
                event.reply(replyMsg)
            }
        }

//        event.message[At]
    }
    bot.join()
}
