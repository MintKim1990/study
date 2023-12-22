package chat.controller

import chat.service.RoomService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/redis")
class RedisController(
    private val roomService: RoomService
) {

    @PostMapping("/room")
    fun room(@RequestParam name: String) = roomService.createRoom(name)

    @GetMapping("/room")
    fun findRooms() = roomService.findRooms()


}