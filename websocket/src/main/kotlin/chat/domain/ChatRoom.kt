package chat.domain

import chat.service.ChatRequest
import chat.service.ChatService
import chat.service.MessageType.JOIN
import org.springframework.web.reactive.socket.WebSocketSession

class ChatRoom(
    val id: String,
    val name: String,
    private val sessions: HashSet<WebSocketSession> = hashSetOf()
) {

    fun handleAction(session: WebSocketSession, request: ChatRequest, chatService: ChatService) {
        if (request.type == JOIN) {
            sessions.add(session)
            request.message = request.sender + " 님이 입장하셨습니다"
        }

        sessions.parallelStream().forEach { session -> chatService.sendMessage(session, request) }
    }

}