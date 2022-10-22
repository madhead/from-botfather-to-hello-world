import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndLongPolling
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

suspend fun main(args: Array<String>) {
    val token = args[0]
    val (_, job) = telegramBotWithBehaviourAndLongPolling(token) {
        this.allUpdatesFlow.onEach { println(it) }.launchIn(GlobalScope)
    }

    job.join()
}
