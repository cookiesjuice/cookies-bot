package com.github.cookiesjuice

import com.github.cookiesjuice.moudle.SeTuMod
import com.github.cookiesjuice.moudle.TencentAPI
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.join
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import kotlin.math.atan

suspend fun main() {

    val qqid = 1029008034L
    val pwd = "lz20000817*"
    val seTuMod = SeTuMod()

    val bot = Bot(qqid, pwd).alsoLogin()
    bot.subscribeAlways<GroupMessageEvent> { event ->

        //处理at消息
        if (message[At]?.target == qqid) {
            //获得消息内容，因为at消息为“@xxx content...”，所以需要过滤掉“@xxx”
            val content = message.content.replace(message[At]!!.display, "")
            val replyMsg = TencentAPI.ZhiNengXianLiao(content)

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
