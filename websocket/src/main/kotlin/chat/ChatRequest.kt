package chat

data class ChatRequest(
    val type: MessageType,
    val roomId: String,
    val sender: String,
    var message: String?,
)

enum class MessageType {
    JOIN, TALK
}