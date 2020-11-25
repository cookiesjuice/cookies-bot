package com.github.cookiesjuice.cookiesbot

import com.github.cookiesjuice.cookiesbot.config.BotProperties
import com.github.cookiesjuice.cookiesbot.control.SetuListenControl
import com.github.cookiesjuice.cookiesbot.utils.LogUtils
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@ConfigurationPropertiesScan
@SpringBootApplication
class CookiesBotApplication(val botProperties: BotProperties, val setuControl: SetuListenControl) : CommandLineRunner {
    override fun run(vararg args: String?) = runBlocking {
        val bot = Bot(botProperties.qq, botProperties.password).alsoLogin()

        setuControl.listen(bot)

        bot.join()
    }
}

fun main(args: Array<String>) {
    val now = SimpleDateFormat("yyyy-MM-dd_HH").format(Date())
    if (LogUtils.setOutToFile(File("log/$now.log"))) {
        runApplication<CookiesBotApplication>(*args)
    }
}