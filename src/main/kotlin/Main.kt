import dev.inmo.tgbotapi.extensions.api.answers.answerCallbackQuery
import dev.inmo.tgbotapi.extensions.api.edit.edit
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onContentMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onDataCallbackQuery
import dev.inmo.tgbotapi.extensions.utils.botCommandTextSourceOrNull
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.message
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.text
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardButtons.CallbackDataInlineKeyboardButton
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardButtons.URLInlineKeyboardButton
import dev.inmo.tgbotapi.types.buttons.InlineKeyboardMarkup
import dev.inmo.tgbotapi.types.message.HTMLParseMode
import dev.inmo.tgbotapi.types.message.abstracts.CommonMessage
import dev.inmo.tgbotapi.types.message.abstracts.ContentMessage
import dev.inmo.tgbotapi.types.message.content.MessageContent
import dev.inmo.tgbotapi.types.message.content.TextContent

const val firstMenu = "<b>Menu 1</b>\n\nA beautiful menu with a shiny inline button.";
const val secondMenu = "<b>Menu 2</b>\n\nA better menu with even more shiny inline buttons.";
val next = CallbackDataInlineKeyboardButton("Next", "next")
val back = CallbackDataInlineKeyboardButton("Back", "back")
val url = URLInlineKeyboardButton("Tutorial", "https://madhead.me/posts/from-botfather-to-hello-world")
val firstMenuMarkup = InlineKeyboardMarkup(
    listOf(
        listOf(next)
    )
)
val secondMenuMarkup = InlineKeyboardMarkup(
    listOf(
        listOf(back),
        listOf(url),
    )
)

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
        onCommand("menu") {
            bot.sendMessage(
                chat = it.chat,
                text = firstMenu,
                parseMode = HTMLParseMode,
                replyMarkup = firstMenuMarkup
            )
        }
        onContentMessage(
            initialFilter = CommonMessage<MessageContent>::isNotCommand
        ) {
            val text = it.text

            if (text != null) {
                bot.reply(it, if (screaming) text.uppercase() else text)
            } else {
                bot.reply(it, it)
            }
        }
        onDataCallbackQuery("next") {
            bot.edit(message = it.message as ContentMessage<TextContent>, text = secondMenu, parseMode = HTMLParseMode, replyMarkup = secondMenuMarkup)
        }
        onDataCallbackQuery("back") {
            bot.edit(message = it.message as ContentMessage<TextContent>, text = firstMenu, parseMode = HTMLParseMode, replyMarkup = firstMenuMarkup)
        }
        onDataCallbackQuery {
            bot.answerCallbackQuery(it)
        }
    }.join()
}

fun CommonMessage<*>.isNotCommand(): Boolean {
    val content = this.content as? TextContent ?: return true

    return content.textSources.none { it.botCommandTextSourceOrNull() != null }
}
