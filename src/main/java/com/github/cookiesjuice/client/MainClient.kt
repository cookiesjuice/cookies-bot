package com.github.cookiesjuice.client

import com.github.cookiesjuice.controller.LoginController
import com.github.cookiesjuice.service.impl.SetuServiceImpl
import com.github.cookiesjuice.service.impl.TencentAPIMod
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.join
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.content

suspend fun main() {
    val loginController = LoginController()

    val qid = loginController.id
    val pwd = loginController.password
    val seTuMod = SetuServiceImpl()

    val bot = Bot(qid, pwd).alsoLogin()
    bot.subscribeAlways<GroupMessageEvent> { event ->

        //处理at消息
        if (message[At]?.target == qid) {
            //获得消息内容，因为at消息为“@xxx content...”，所以需要过滤掉“@xxx”
            val content = message.content.replace(message[At]!!.display, "")
            val replyMsg = TencentAPIMod.ZhiNengXianLiao(content)

            event.reply(replyMsg)
        } else {
            if (message.content.contains("涩图")) {
                val imgFile = seTuMod.randomSeTuFile()
                if (imgFile != null) {
                    event.uploadImage(imgFile).plus("tips：此涩图由[曲奇.qzone.qq.com]提供 :)").send()
                } else {
                    event.reply("糟糕！涩图都被曲奇吃掉啦... ╮(╯Д╰)╭")
                }
            }
        }

//        event.message[At]
    }
    bot.join()
}
