package chat.domain

import org.springframework.stereotype.Component
import reactor.core.publisher.Sinks

@Component
class MessagePublisher {

    val sinks = Sinks.many().unicast().onBackpressureBuffer<String>()

    fun publish(message: String) = sinks.tryEmitNext(message)

}