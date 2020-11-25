package com.github.cookiesjuice.cookiesbot.control

import com.github.cookiesjuice.cookiesbot.config.setu.SetuProperties
import com.github.cookiesjuice.cookiesbot.config.setu.UserProperties
import com.github.cookiesjuice.cookiesbot.module.lang.service.TagLocalizationService
import com.github.cookiesjuice.cookiesbot.module.setu.entity.Setu
import com.github.cookiesjuice.cookiesbot.module.setu.service.SetuService
import com.github.cookiesjuice.cookiesbot.module.setu.service.UserService
import com.github.cookiesjuice.cookiesbot.utils.HttpUtils
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.*
import org.springframework.stereotype.Controller
import java.io.File
import java.util.*
import java.util.regex.Pattern

@Controller
class SetuListenControl(
        val setuService: SetuService,
        val userService: UserService,
        val tagLocalizationService: TagLocalizationService,
        val setuProperties: SetuProperties,
        val userProperties: UserProperties,
) {
    /**
     * 上传涩图 | 获取一张指定id的涩图 | 随机获取一张或多张涩图
     */
    private fun Bot.setu(): Boolean {
        var isHandle = false
        subscribeGroupMessages {
            contains("涩图") {
                val matcher = Pattern.compile("([上传]*)(涩图\\s*)([\\d+]*)([\\s*连]*)").matcher(message.content)
                if (matcher.find()) {
                    val user = userService.findOrSave(sender.id)
                    val everyday = userService.getToday(user)
                    val levelSetuMax = setuProperties.levelSetuMaxOfDay
                    val max = levelSetuMax[user.level]
                    var isMaximum = everyday.setuCount >= max

                    val g1 = matcher.group(1) != ""
                    val g3 = matcher.group(3) != ""
                    val g4 = matcher.group(4).trim() != ""

                    if (g1) {//上传涩图
                        val image = message[Image]
                        if (image != null) {
                            val url = image.url()
                            val saveName = UUID.randomUUID().toString()
                            HttpUtils.downloadFile(url, saveName)
                            val tempFile = File(saveName)
                            quoteReply("正在上传中喵~")
                            try {
                                val setu = setuService.upload(user, tempFile)
                                if (setu != null) {
                                    quoteReply("上传成功喵~增加[${setuProperties.uploadExp}]点经验")
                                    var info = "id：${setu.id}\n分析标签(标签名:可信度)：\n\n"
                                    for (evaluate in setu.evaluates) {
                                        info += "${tagLocalizationService.translate(evaluate.tag.name)} : ${evaluate.reliability}\n"
                                    }
                                    quoteReply(info)
                                    userService.addExp(user, setuProperties.uploadExp.toLong())
                                } else {
                                    quoteReply("上传失败惹~涩图被曲奇吃掉惹喵~")
                                }
                            } finally {
                                if (!tempFile.delete()) {
                                    System.gc()
                                    tempFile.delete()
                                }
                            }
                        } else {
                            quoteReply("上传个喵喵?没有看到涩图喵~")
                        }

                    } else if (g3 && g4 && !isMaximum) { //涩图xxx连
                        val num = matcher.group(3).toInt()
                        if (num + everyday.setuCount > max) {
                            isMaximum = true
                        } else {
                            val setuList = setuService.random(num)
                            if (setuList != null && setuList.size > 0) {
                                var reply = messageChainOf()
                                for (setu in setuList) {
                                    if (setu != null) {
                                        val setuFile = setuService.getFile(setu)
                                        if (setuFile != null) {
                                            val image: Image = uploadImage(setuFile)
                                            reply = reply.plus(image)
                                        }
                                    }
                                }
                                userService.addSetuCount(everyday, num)
                                quoteReply(reply)
                            } else {
                                quoteReply("没有找到涩图喵~")
                            }
                        }
                    } else if (!isMaximum) { //涩图
                        //如果没超过此等级每日获取次数
                        val setu: Setu? = if (g3) { //涩图xxx 获取指定涩图
                            val id = matcher.group(3).toLong()
                            setuService.find(id)
                        } else {
                            setuService.random()
                        }

                        var hasSetu = false
                        if (setu != null) {
                            val setuFile = setuService.getFile(setu)
                            if (setuFile != null) {
                                val image: Image = uploadImage(setuFile)
                                val info = "id: [${setu.id}]"
                                quoteReply(messageChainOf(PlainText(info)).plus(image))
                                userService.addSetuCount(everyday)
                                hasSetu = true
                            }
                        }

                        if (!hasSetu) {
                            quoteReply("没有找到涩图喵~")
                        }
                    }

                    if (isMaximum) {
                        val tips = "你的等级为[" + user.level + "]级，每天最多只能获取[" + max + "]张涩图，今日已经获取[" + everyday.setuCount + "]张涩图了哦喵~"
                        quoteReply(tips)
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
    private fun Bot.reply(): Boolean {
        var isHandle = false
        subscribeGroupMessages {
            //如果是引用回复，检查对涩图评分，收藏事件
            has<QuoteReply> {
                if (it.source.fromId == id) {
                    val user = userService.findOrSave(sender.id)
                    val pattern = Pattern.compile("(id: \\[)(\\d+)(])")
                    val matcher = pattern.matcher(it.source.originalMessage[PlainText]!!.contentToString())
                    if (matcher.find()) {
                        val id = matcher.group(2).toLong()
                        val setu = setuService.find(id)
                        if (setu != null) {
                            val centent = message.contentToString()
                            if (centent.contains("喜欢") || centent.contains("收藏")) {
                                if (userService.addFavorite(user, setu)) {
                                    quoteReply("收藏涩图[${setu.id}]成功喵~")
                                } else {
                                    quoteReply("已经收藏过此涩图了喵~")
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
                                                    quoteReply("未更新评分，本次评分与上次评分是一样的喵~")
                                                } else {
                                                    quoteReply("评分已更新喵~ $oldScore -> $score")
                                                }
                                            } else {
                                                quoteReply("评分成功喵~")
                                            }
                                        } else {
                                            quoteReply("分数超出范围(1~100)了喵~")
                                        }
                                    } else {
                                        quoteReply(err)
                                    }
                                } catch (e: NumberFormatException) {
                                    quoteReply(err)
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
    private fun Bot.evaluate(): Boolean {
        var isHandle = false
        subscribeGroupMessages {
            has<Image> {
                if (message.contentToString().contains("分析")) {
                    val saveName = UUID.randomUUID().toString()
                    HttpUtils.downloadFile(it.url(), saveName)
                    val tempFile = File(saveName)
                    quoteReply("正在分析中喵~")
                    val evaluateList = setuService.evaluate(tempFile.path)
                    tempFile.delete()
                    var info = "分析完成喵~分析标签(标签名:可信度)\n"
                    for (evaluate in evaluateList) {
                        info += "${tagLocalizationService.translate(evaluate.tag.name)} : ${evaluate.reliability}\n"
                    }
                    quoteReply(info)
                    isHandle = true
                }
            }
        }
        return isHandle
    }

    /**
     * 发言次数记录并增加经验
     */
    private fun Bot.speak() {
        subscribeGroupMessages {
            always {
                val user = userService.findOrSave(sender.id)
                val everyday = userService.getToday(user)

                //每次发言都会增加经验
                //如果发言加经验的次数没超过配置的最大值
                if (everyday.speakCount < userProperties.speakExpMaxOfDay) {
                    userService.addExp(user, userProperties.speakExp.toLong())
                }

                //增加发言计数
                userService.addSpeakCount(everyday)
            }
        }
    }

    private fun Bot.info() {
        subscribeMessages {
            always {
                val content = message.contentToString()
                val line = "\n\uD83C\uDF6A\uD83C\uDF6A\uD83C\uDF6A\uD83C\uDF6A\uD83C\uDF6A\uD83C\uDF6A\uD83C\uDF6A\uD83C\uDF6A\n"
                if (content.contains("myinfo")) {
                    val user = userService.findOrSave(sender.id)

                    val levelExp = userProperties.levelExp
                    val exp = if (user.level < levelExp.size - 1) "/${levelExp[user.level + 1]}" else ""

                    val info = "id:${user.id}\n" +
                            "等级:${user.level}\n" +
                            "经验:${user.exp}$exp\n" +
                            "涩币:${user.scoin}$line"

                    var favorites = "收藏的涩图(涩图ID):{\n"
                    user.favoriteSetus.forEach { favorites += "${it.id}\n" }
                    favorites += "}$line"

                    var grades = "参与的评分(涩图ID->评分):{\n"
                    user.grades.forEach { grades += "${it.setu.id}->${it.score}\n" }
                    grades += "}$line"

                    val everyday = userService.getToday(user)
                    val max = setuProperties.levelSetuMaxOfDay[user.level]
                    val setuCount = "今日涩图/今日上限:${everyday.setuCount}/$max\n"

                    quoteReply(info + favorites + grades + setuCount)
                } else {
                    val pattern = Pattern.compile("(setuinfo\\s*)(\\d+)")
                    val matcher = pattern.matcher(message.contentToString())
                    if (matcher.find()) {
                        val id = matcher.group(2).toLong()
                        val setu = setuService.find(id)
                        if (setu != null) {
                            val info = "id:${setu.id}\n" +
                                    "作者:${setu.author}\n" +
                                    "上传者:${setu.uploadUser.id}\n" +
                                    "上传时间：${setu.uploadTime}$line"

                            var evaluates = "分析标签(标签名:可信度):{\n"
                            setu.evaluates.forEach { evaluates += "${tagLocalizationService.translate(it.tag.name)} : ${it.reliability}\n" }
                            evaluates += "}$line"

                            var grades = "评分详情(用户ID->分数):{\n"
                            setu.grades.forEach { grades += "${it.user.id}->${it.score}\n" }
                            grades += "}$line"

                            var favorites = "喜爱此涩图的用户(用户ID):{\n"
                            setu.favoriteUsers.forEach { favorites += "${it.id}\n" }
                            favorites += "}"

                            quoteReply(info + evaluates + grades + favorites)
                        } else {
                            quoteReply("没有找到涩图喵~")
                        }
                    }
                }
            }
        }
    }

    private fun Bot.cmdtest() {
        subscribeFriendMessages {
//            contains(botProperties.cmd) {
//                val pattern = Pattern.compile("(${botProperties.cmd})(\\S+)([\\s+\\S]*)") //🍪 cmd arg1 arg2 arg3...
//                val matcher = pattern.matcher(message.contentToString())
//                if (matcher.find()) {
//                    val cmd = matcher.group(2)
//                    val args = matcher.group(3).split("\\s")
//                }
//                val user = userService.findOrSave(sender.id)
//                if (user.adminLevel > 10) {
//
//                }
//            }
            sentBy(2394495949) {
                contains("init setu") {
                    reply("正在初始化涩图库喵~")
                    val setuLibPath = "/home/imgs"
                    val setuPaths = File(setuLibPath).list()
                    if (setuPaths != null) {
                        val user = userService.findOrSave(sender.id)
                        var i = 0
                        for (path in setuPaths) {
                            val setuFile = File("$setuLibPath/$path")
                            if (setuFile.exists()) {
                                setuService.upload(user, setuFile)
                                reply("初始化进度: ${++i}/${setuPaths.size}")
                            }
                        }
                        reply("初始化完成喵~")
                    } else {
                        reply("涩图库读取失败喵~")
                    }
                }
            }
        }
    }

    fun listen(bot: Bot) {
        if (!bot.reply())
            if (!bot.setu())
                if (!bot.evaluate())
                    bot.info()

        bot.speak()

        bot.cmdtest()
    }
}
