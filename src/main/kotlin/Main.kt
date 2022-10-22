import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onContentMessage
import dev.inmo.tgbotapi.extensions.utils.botCommandTextSourceOrNull
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.text
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import dev.inmo.tgbotapi.types.message.content.TextContent

suspend fun main(args: Array<String>) {
    val token = args[0]
    val bot = telegramBot(token)
    var screaming = false

    bot.buildBehaviourWithLongPolling() {
        onCommand("scream") {
            screaming = true
        }
        onCommand("whisper") {
            screaming = false
        }
        onContentMessage(
            initialFilter = CommonMessage<MessageContent>::isNotModeSwitchCommand
        ) {
            val text = it.text

            if (text != null) {
                bot.reply(it, if (screaming) text.uppercase() else text)
            } else {
                bot.reply(it, it)
            }
        }
    }.join()
}

fun CommonMessage<*>.isNotModeSwitchCommand(): Boolean {
    val content = this.content as? TextContent ?: return true

    return content.textSources.none { it.botCommandTextSourceOrNull()?.command in listOf("scream", "whisper") }
}
