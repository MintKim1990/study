package chat.service

data class ChatRequest(
    val command: MessageCommand,
    val sender: String,
    var message: String?,
)

enum class MessageCommand {
    JOIN, TALK
}