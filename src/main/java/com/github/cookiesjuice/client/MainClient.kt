package com.github.cookiesjuice.client

import com.github.cookiesjuice.controller.LoginController
import com.github.cookiesjuice.controller.MessageController
import com.github.cookiesjuice.service.impl.DeepDanBooruSerivceImpl
import com.github.cookiesjuice.service.impl.SetuServiceImpl
import com.github.cookiesjuice.service.impl.TagLocalizationServiceImpl
import com.github.cookiesjuice.service.impl.TencentAPIServiceImpl
import com.github.cookiesjuice.util.ConfigInfo
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.join
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.content

suspend fun main() {
    val initRet = ConfigInfo.init()
    if (initRet != 0) {
        print("Initialization configuration error! return code = $initRet")
    }

    val loginController = LoginController()

    val messageController = MessageController(
            SetuServiceImpl(),
            TencentAPIServiceImpl(),
            DeepDanBooruSerivceImpl(),
            TagLocalizationServiceImpl()
    )

    val qid = loginController.id
    val pwd = loginController.password

    val bot = Bot(qid, pwd).alsoLogin()
    bot.subscribeAlways<GroupMessageEvent> { event ->
        //处理at消息
        if (message[At]?.target == qid) {
            //消息内容，因为at消息为“@xxx content...”，所以需要过滤掉“@xxx”
            val content = message.content.replace(message[At]!!.display, "")
            var imgPath:String? = null
            if (message[Image] != null) {
                imgPath = message[Image]!!.url()
            }
            val respMsg = messageController.handleAtMessage(content, imgPath)
            if (respMsg != null) {
                val replyMsgChain = MessageBuilder.buildFromGroupMessage(this, respMsg)
                reply(replyMsgChain)
            }
        } else {
            val content = message.content
            val respMsg = messageController.handlePlainMessage(content)
            if (respMsg != null) {
                val replyMsgChain = MessageBuilder.buildFromGroupMessage(this, respMsg)
                reply(replyMsgChain)
            }
        }
    }
    bot.join()
}
