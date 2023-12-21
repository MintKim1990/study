package chat

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/chat")
class ChatController(
    private val service: ChatService
) {

    @PostMapping
    fun createRoom(@RequestParam name: String) = service.createRoom(name)

    @GetMapping
    fun findRooms() = service.findRooms()

    @RequestMapping("/view")
    fun view() = ModelAndView("view")

}