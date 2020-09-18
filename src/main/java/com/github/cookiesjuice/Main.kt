package com.github.cookiesjuice

import com.github.cookiesjuice.moudle.SeTuMod
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.join
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content

suspend fun main() {
    val qqId = 1029008034L
    val pwd = "lz20000817*"
    val seTuMod = SeTuMod()

    val bot = Bot(qqId, pwd).alsoLogin()
    bot.subscribeAlways<GroupMessageEvent> { event ->
        if (event.message.content.contains("涩图")) {
            val imgFile = seTuMod.randomSeTuFile()
            if (imgFile != null) {
                event.uploadImage(imgFile).plus("tips：此涩图由[曲奇.qzone.qq.com]提供 :)").send()
            } else {
                event.reply("糟糕！涩图都被曲奇吃掉啦... ╮(╯Д╰)╭")
            }
        }
    }
    bot.join()
}
