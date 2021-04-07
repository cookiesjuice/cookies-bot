package com.github.cookiesjuice.cookiesbot.control

import com.github.cookiesjuice.cookiesbot.config.BotProperties
import com.github.cookiesjuice.cookiesbot.config.setu.SetuProperties
import com.github.cookiesjuice.cookiesbot.config.setu.UserProperties
import com.github.cookiesjuice.cookiesbot.module.cmd.service.CmdService
import com.github.cookiesjuice.cookiesbot.module.setu.entity.Setu
import com.github.cookiesjuice.cookiesbot.module.setu.service.SetuService
import com.github.cookiesjuice.cookiesbot.module.setu.service.UserService
import com.github.cookiesjuice.cookiesbot.module.setu.service.impl.ImageServiceImpl
import com.github.cookiesjuice.cookiesbot.utils.HttpUtils
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.springframework.stereotype.Controller
import java.io.File
import java.util.*
import java.util.regex.Pattern

@Controller
class SetuListenControl(
    val cmdService: CmdService,
    val setuService: SetuService,
    val userService: UserService,
    val imageService: ImageServiceImpl,
    val botProperties: BotProperties,
    val setuProperties: SetuProperties,
    val userProperties: UserProperties,
) {
    /**
     * 上传涩图 | 获取一张指定id的涩图 | 随机获取一张或多张涩图
     */
    private fun setu(): Boolean {
        var isHandle = false
        GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
            if (message.content.contains("涩图")) {
                val matcher = Pattern.compile("(上传)*(涩图[\\s]*)([\\d+]*)(\\s*连)*").matcher(message.content)
                if (matcher.find()) {
                    val user = userService.findOrSave(sender.id)
                    val everyday = userService.getToday(user)
                    val levelSetuMax = setuProperties.levelSetuMaxOfDay
                    val max = levelSetuMax[user.level]
                    var isMaximum = everyday.setuCount >= max

                    val g1 = matcher.group(1) ?: ""
                    val g3 = matcher.group(3) ?: ""
                    val g4 = matcher.group(4) ?: ""
                    val hasG1 = g1 != ""
                    val hasG3 = g3 != ""
                    val hasG4 = g4.trim() != ""

                    if (hasG1) {//上传涩图
                        val image = message[Image]
                        if (image != null) {
                            val url = image.queryUrl()
                            val path = File("temp")
                            if (!path.exists()) {
                                path.mkdir()
                            }
                            val saveName = "$path/${UUID.randomUUID()}"
                            HttpUtils.downloadFile(url, saveName)
                            val tempFile = File(saveName)
                            message.quote().plus("正在上传中喵~").sendTo(subject)
                            try {
                                val setu = setuService.upload(user, tempFile)
                                if (setu != null) {
                                    val msg = PlainText("上传成功喵~增加[${setuProperties.uploadExp}]点经验~\n涩图ID：${setu.id}\n")

                                    val file = imageService.evaluates(setu.evaluates)
                                    if (file != null) {
                                        val img = subject.uploadImage(file.toExternalResource())
                                        message.quote().plus(msg.plus("分析标签：\n").plus(img)).sendTo(subject)
                                        file.delete()
                                    } else {
                                        message.quote().plus(msg.plus("哎呀~分析结果被曲奇吃掉惹喵~")).sendTo(subject)
                                    }

                                    userService.changeExp(user, setuProperties.uploadExp.toLong())
                                } else {
                                    message.quote().plus("上传失败惹~涩图被曲奇吃掉惹喵~").sendTo(subject)
                                }
                            } finally {
                                tempFile.delete()
                            }
                        } else {
                            message.quote().plus("上传个喵喵?没有看到涩图喵~").sendTo(subject)
                        }

                    } else if (hasG3 && hasG4 && !isMaximum) { //涩图xxx连
                        try {
                            val num = g3.toInt()
                            if (num > setuProperties.setuMax) {
                                message.quote().plus("最多只能连续${setuProperties.setuMax}张涩图喵~").sendTo(subject)
                            } else if (num + everyday.setuCount > max) {
                                isMaximum = true
                            } else {
                                val setuList = setuService.random(num)
                                if (setuList != null && setuList.size > 0) {
                                    var reply = messageChainOf()
                                    for (setu in setuList) {
                                        if (setu != null) {
                                            val setuFile = setuService.getFile(setu)
                                            if (setuFile != null) {
                                                val image: Image = subject.uploadImage(setuFile.toExternalResource())
                                                reply = reply.plus(image)
                                            }
                                        }
                                    }
                                    userService.addSetuCount(everyday, num)
                                    message.quote().plus(reply).sendTo(subject)
                                } else {
                                    throw NumberFormatException()
                                }
                            }
                        } catch (_: NumberFormatException) {
                            message.quote().plus("没有找到涩图喵~").sendTo(subject)
                        }
                    } else if (!isMaximum) { //涩图
                        //如果没超过此等级每日获取次数
                        val setu: Setu? = if (hasG3) { //涩图xxx 获取指定涩图
                            try {
                                val id = g3.toLong()
                                setuService.find(id)
                            } catch (_: NumberFormatException) {
                                null
                            }
                        } else {
                            setuService.random()
                        }

                        var hasSetu = false
                        if (setu != null) {
                            val setuFile = setuService.getFile(setu)
                            if (setuFile != null) {
                                val image: Image = subject.uploadImage(setuFile.toExternalResource())
                                val info = "id: [${setu.id}]"
                                message.quote().plus(messageChainOf(PlainText(info)).plus(image)).sendTo(subject)
                                userService.addSetuCount(everyday)
                                hasSetu = true
                            }
                        }

                        if (!hasSetu) {
                            message.quote().plus("没有找到涩图喵~").sendTo(subject)
                        }
                    }

                    if (isMaximum) {
                        val tips = "你的等级为[" + user.level + "]级，每天最多只能获取[" + max + "]张涩图，今日已经获取[" + everyday.setuCount + "]张涩图了哦喵~"
                        message.quote().plus(tips).sendTo(subject)
                    }
                    isHandle = true
                }
            }

        }
        return isHandle
    }

    /**
     * 对涩图评分，或收藏涩图
     */
    private fun reply(): Boolean {
        var isHandle = false
        GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
            //如果是引用回复，检查对涩图评分，收藏事件
            val quote = message[QuoteReply]
            if (quote != null) {
                if (quote.source.fromId == bot.id) {
                    val user = userService.findOrSave(sender.id)
                    val pattern = Pattern.compile("(id: \\[)(\\d+)(])")
                    val matcher = pattern.matcher(quote.source.originalMessage.contentToString())
                    if (matcher.find()) {
                        val id = matcher.group(2).toLong()
                        val setu = setuService.find(id)
                        if (setu != null) {
                            val centent = message.contentToString()
                            if (centent.contains("喜欢") || centent.contains("收藏")) {
                                if (userService.addFavorite(user, setu)) {
                                    message.quote().plus("收藏涩图[${setu.id}]成功喵~").sendTo(subject)
                                } else {
                                    message.quote().plus("已经收藏过此涩图了喵~").sendTo(subject)
                                }
                            } else {
                                val err = "喵?不明白你的意思喵~"
                                try {
                                    val split: Array<String> = centent.split("\\D+".toRegex()).toTypedArray()
                                    var scoreStr: String? = null
                                    for (s in split) {
                                        if (s != "") {
                                            scoreStr = s
                                            break
                                        }
                                    }
                                    if (scoreStr != null) {
                                        val score = scoreStr.toInt()
                                        if (score in 1..100) {
                                            val oldScore = setuService.grade(user, setu, score)
                                            if (oldScore != 0) {
                                                if (oldScore == score) {
                                                    message.quote().plus("未更新评分，本次评分与上次评分是一样的喵~").sendTo(subject)
                                                } else {
                                                    message.quote().plus("评分已更新喵~ $oldScore -> $score").sendTo(subject)
                                                }
                                            } else {
                                                message.quote().plus("评分成功喵~").sendTo(subject)
                                            }
                                        } else {
                                            message.quote().plus("分数超出范围(1~100)了喵~").sendTo(subject)
                                        }
                                    } else {
                                        message.quote().plus(err).sendTo(subject)
                                    }
                                } catch (e: NumberFormatException) {
                                    message.quote().plus(err).sendTo(subject)
                                }
                            }
                        }
                    }
                }
                isHandle = true
            }
        }
        return isHandle
    }

    /**
     * 分析图片标签
     */
    private fun evaluate(): Boolean {
        var isHandle = false
        GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
            val image = message[Image]
            if (image != null) {
                if (message.contentToString().contains("分析")) {
                    val path = File("temp")
                    if (!path.exists()) {
                        path.mkdir()
                    }
                    val saveName = "$path/${UUID.randomUUID()}"
                    HttpUtils.downloadFile(image.queryUrl(), saveName)
                    val tempFile = File(saveName)
                    message.quote().plus("正在分析中喵~").sendTo(subject)
                    val evaluateList = setuService.evaluate(tempFile.path)
                    tempFile.delete()
                    val file = imageService.evaluates(evaluateList)
                    if (file != null) {
                        val img = subject.uploadImage(file.toExternalResource())
                        message.quote().plus("分析完成喵~\n").plus(img).sendTo(subject)
                        file.delete()
                    } else {
                        message.quote().plus("哎呀~分析结果被曲奇吃掉惹喵~").sendTo(subject)
                    }

                    isHandle = true
                }
            }
        }
        return isHandle
    }

    /**
     * 发言次数记录并增加经验
     */
    private fun speak() {
        GlobalEventChannel.subscribeAlways<GroupMessageEvent> {
            val user = userService.findOrSave(sender.id)
            val everyday = userService.getToday(user)

            //每次发言都会增加经验
            //如果发言加经验的次数没超过配置的最大值
            if (everyday.speakCount < userProperties.speakExpMaxOfDay) {
                userService.changeExp(user, userProperties.speakExp.toLong())
            }

            //增加发言计数
            userService.addSpeakCount(everyday)
        }
    }

    private fun info(): Boolean {
        var isHandle = false
        GlobalEventChannel.subscribeAlways<MessageEvent> {
            val content = message.contentToString()
            if (content.contains("myinfo")) {
                val user = userService.findOrSave(sender.id)
                val file = imageService.user(user)
                if (file != null) {
                    val img = subject.uploadImage(file.toExternalResource())
                    message.quote().plus(img).sendTo(subject)
                    file.delete()
                } else {
                    message.quote().plus("哎呀~消息被曲奇吃掉惹喵~").sendTo(subject)
                }
                isHandle = true
            } else {
                val pattern = Pattern.compile("(setuinfo\\s*)(\\d+)")
                val matcher = pattern.matcher(message.contentToString())
                if (matcher.find()) {
                    val setu = try {
                        val id = matcher.group(2).toLong()
                        setuService.find(id)
                    } catch (_: NumberFormatException) {
                        null
                    }
                    if (setu != null) {
                        val file = imageService.setu(setu)
                        if (file != null) {
                            val img = subject.uploadImage(file.toExternalResource())
                            message.quote().plus(img).sendTo(subject)
                            file.delete()
                        } else {
                            message.quote().plus("哎呀~消息被曲奇吃掉惹喵~").sendTo(subject)
                        }
                    } else {
                        message.quote().plus("没有找到涩图喵~").sendTo(subject)
                    }
                    isHandle = true
                }
            }
        }

        return isHandle
    }

    private fun cmd(): Boolean {
        var isHandle = false
        GlobalEventChannel.subscribeAlways<MessageEvent> {
            val pattern = Pattern.compile("^(${botProperties.cmd})(\\S+)([\\s+\\S]*)")
            val matcher = pattern.matcher(message.content)
            if (matcher.find()) {
                val cmd = matcher.group(2)
                val args = arrayListOf<String>()
                matcher.group(3).split("\\s".toRegex()).forEach { s ->
                    if (s != "") {
                        args.add(s)
                    }
                }
                val reply = cmdService.handleCmd(cmd, args.toTypedArray(), sender.id)
                message.quote().plus(reply).sendTo(subject)
                isHandle = true
            }
        }
        return isHandle
    }

    fun listen(bot: Bot) {
        speak()

        if (cmd()) return
        if (info()) return
        if (reply()) return
        if (setu()) return
        if (evaluate()) return
    }
}
