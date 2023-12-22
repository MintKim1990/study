package chat.service

import chat.domain.ChatRoom
import chat.service.MessageCommand.*
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RoomService(
    private val redisTemplate: ReactiveRedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val reactiveRedisMessageListenerContainer: ReactiveRedisMessageListenerContainer
) {

    private val ROOM_KEY = "ChatRoom"
    private val logger = KotlinLogging.logger {}

    fun createRoom(name: String) {
        val ops = redisTemplate.opsForHash<String, String>()
        ChatRoom(name).let {
            ops.put(ROOM_KEY, it.id, objectMapper.writeValueAsString(it)).subscribe()
        }
    }

    fun findRooms(): Flux<ChatRoom> {
        val ops = redisTemplate.opsForHash<String, String>()
        return ops.values(ROOM_KEY)
            .map { objectMapper.readValue(it, ChatRoom::class.java) }
    }

    fun subscribe(session: WebSocketSession, chatRoomId: String): Flux<WebSocketMessage> {
        return reactiveRedisMessageListenerContainer.receive(ChannelTopic(chatRoomId))
            .map { it.message }
            .doOnNext { logger.info { "listener Message : $it" } }
            .map { session.textMessage(it) }

    }

    fun publish(message: WebSocketMessage, chatRoomId: String) : Mono<Long> {
        val chatRequest = objectMapper.readValue(message.payloadAsText, ChatRequest::class.java)
        when(chatRequest.command) {
            JOIN -> chatRequest.message = chatRequest.sender + "님이 입장하셨습니다."
            TALK -> chatRequest.message = chatRequest.message
            else -> throw IllegalArgumentException("지원하지 않는 기능입니다.")
        }
        return redisTemplate.convertAndSend(chatRoomId, objectMapper.writeValueAsString(chatRequest)).log()
    }

}