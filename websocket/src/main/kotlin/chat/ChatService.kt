package chat

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.UUID

@Service
class ChatService(
    private val objectMapper: ObjectMapper,
    private val chatRooms: LinkedHashMap<String, ChatRoom> = linkedMapOf()
) {

    fun findRooms() = ArrayList(chatRooms.values)

    fun findRoom(roomId: String) = chatRooms[roomId]

    fun createRoom(name: String) : ChatRoom {
        return ChatRoom(id = UUID.randomUUID().toString(), name = name).also { chatRooms[it.id] = it }
    }

    fun <T> sendMessage(session: WebSocketSession, message: T) {
        session.sendMessage(TextMessage(objectMapper.writeValueAsString(message)))
    }

}