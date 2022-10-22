import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.longPolling
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

suspend fun main(args: Array<String>) {
    val token = args[0]
    val bot = telegramBot(token)

    bot.longPolling {
        messagesFlow
            .onEach {
                bot.reply(it.data, it.data)
            }
            .launchIn(GlobalScope)
    }.join()
}
