package chat

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WebSocketChatHandler(
    private val objectMapper: ObjectMapper,
    private val chatService: ChatService
) : TextWebSocketHandler() {

    private val logger = KotlinLogging.logger {}

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info { "payload : $message.payload" }

        val chatRequest = objectMapper.readValue(message.payload, ChatRequest::class.java)
        chatService.findRoom(chatRequest.roomId)?.also {
            it.handleAction(session, chatRequest, chatService)
        } ?: throw IllegalStateException("채팅방이 없습니다.")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info { "session : ${session.id} close" }
    }
}