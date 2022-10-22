import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onContentMessage
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.from
import dev.inmo.tgbotapi.extensions.utils.extensions.raw.text

suspend fun main(args: Array<String>) {
    val token = args[0]
    val (_, job) = telegramBotWithBehaviourAndLongPolling(token) {
        onContentMessage {
            val user = it.from

            println("${user?.firstName ?: "Unknown user"} wrote ${it.text}")
        }
    }

    job.join()
}
