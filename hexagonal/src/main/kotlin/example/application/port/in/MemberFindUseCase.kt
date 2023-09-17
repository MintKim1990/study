package example.application.port.`in`

import example.application.port.`in`.command.MemberFindCommand
import example.domain.Member

interface MemberFindUseCase {
    fun memberFind(memberFindCommand: MemberFindCommand): Member
}