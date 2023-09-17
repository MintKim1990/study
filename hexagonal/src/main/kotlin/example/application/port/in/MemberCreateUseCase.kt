package example.application.port.`in`

import example.application.port.`in`.command.MemberCreateCommand
import example.domain.Member

interface MemberCreateUseCase {
    fun memberCreate(memberCreateCommand: MemberCreateCommand): Member
}