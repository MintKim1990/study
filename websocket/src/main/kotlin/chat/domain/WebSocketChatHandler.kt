package chat.domain

import chat.service.ChatRequest
import chat.service.ChatService
import chat.service.MessageType
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.core.io.buffer.NettyDataBuffer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.time.Duration
import java.util.function.Consumer

@Component
class WebSocketChatHandler(
    private val objectMapper: ObjectMapper,
    private val messagePublisher: MessagePublisher,
) : WebSocketHandler {

    private val logger = KotlinLogging.logger {}


    override fun handle(session: WebSocketSession): Mono<Void> {

        logger.info { "최초접근" }

//        val ping = Flux.interval(Duration.ofSeconds(10))
//            .map { session.pingMessage { it.wrap(byteArrayOf()) } }

//        val chatRequest = ChatRequest(type = MessageType.JOIN, roomId = "roomId", sender = "sender", message = "OK")
//        val sendMessage = objectMapper.writeValueAsString(chatRequest)


        val many = messagePublisher.sinks
        many.asFlux()
            .subscribe {
                logger.info { "sink message : $it" }
                session.send(Mono.just(session.textMessage(objectMapper.writeValueAsString(it)))).subscribe()
            }

        return session.receive()
            .doOnNext {
                logger.info { "receive message : ${it.payloadAsText}" }
                many.tryEmitNext(it.payloadAsText)
            }.then()
    }
}


