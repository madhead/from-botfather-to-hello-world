import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.types.ChatId

suspend fun main(args: Array<String>) {
    val token = args[0]
    val bot = telegramBot(token)

    bot.sendTextMessage(ChatId(254767265), "Hello, World!")
}
