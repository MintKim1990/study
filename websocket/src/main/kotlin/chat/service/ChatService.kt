package chat.service

import chat.domain.ChatRoom
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketSession
import java.util.*

@Service
class ChatService(
    private val objectMapper: ObjectMapper,
    private val chatRooms: LinkedHashMap<String, ChatRoom> = linkedMapOf()
) {

    fun findRooms() = ArrayList(chatRooms.values)

    fun findRoom(roomId: String) = chatRooms[roomId]

    fun createRoom(name: String) : ChatRoom {
        return ChatRoom(name = name).also { chatRooms[it.id] = it }
    }

    fun <T> sendMessage(session: WebSocketSession, message: T) {

    }

}