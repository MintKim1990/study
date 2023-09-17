package example.adapter.`in`.web

import example.adapter.WebAdapter
import example.adapter.`in`.web.request.MemberCreateRequest
import example.application.port.`in`.MemberCreateUseCase
import example.application.port.`in`.command.MemberCreateCommand
import example.domain.Member
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI

@WebAdapter(path = "/member")
class MemberCreateController(
    private val memberCreateUseCase: MemberCreateUseCase
) {

    @PostMapping("/create")
    fun createMember(@RequestBody request: MemberCreateRequest): ResponseEntity<Member> {
        val memberCreateCommand = MemberCreateCommand(
            name = request.name,
            address = request.address,
            email = request.email,
            isCorp = request.isCorp
        )

        val createMember = memberCreateUseCase.memberCreate(memberCreateCommand)
        return ResponseEntity.created(URI.create("/member/" + createMember.id)).body(createMember)
    }

}