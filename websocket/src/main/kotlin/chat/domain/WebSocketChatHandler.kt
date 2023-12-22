package chat.domain

import chat.service.RoomService
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class WebSocketChatHandler(
    private val objectMapper: ObjectMapper,
    private val messagePublisher: MessagePublisher,
    private val roomService: RoomService,
    private val reactiveRedisMessageListenerContainer: ReactiveRedisMessageListenerContainer,
) : WebSocketHandler {

    private val logger = KotlinLogging.logger {}

    companion object {
        const val CHAT_ROOM_ID = "Chat-Room-Id"
    }

    override fun handle(session: WebSocketSession): Mono<Void> {

        logger.info { "최초접근" }



//        val ping = Flux.interval(Duration.ofSeconds(10))
//            .map { session.pingMessage { it.wrap(byteArrayOf()) } }

//        val chatRequest = ChatRequest(type = MessageType.JOIN, roomId = "roomId", sender = "sender", message = "OK")
//        val sendMessage = objectMapper.writeValueAsString(chatRequest)


        val chatRoomId = getChatRoomId(session)

        val sendMessage = session.receive()
            .flatMap { roomService.publish(it, chatRoomId) }

        val receiveMessage = session.send(roomService.subscribe(session, chatRoomId))

        return Flux.zip(sendMessage, receiveMessage).then()
    }

    private fun getChatRoomId(session: WebSocketSession) : String {

        /***
         * 핸드쉐이크 헤더에서 Room 키값을 토큰에서 꺼내오는 방식으로 변경예정,
         * JS에서는 헤더값 추가가 불가능해 QueryParam 방식으로 임시 사용
         *
         * val chatRoomId = session.handshakeInfo.headers.getOrEmpty(CHAT_ROOM_ID)
                                   .stream()
                                   .findFirst() ?: throw IllegalStateException("채팅방 ID가 없습니다.")
         *
         */

        return session.handshakeInfo.uri.query.let {
            it.replace("chatRoomId=", "")
        }
    }
}


