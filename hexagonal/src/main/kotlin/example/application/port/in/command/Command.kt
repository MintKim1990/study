package example.application.port.`in`.command

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class MemberCreateCommand(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val address: String,
    @field:NotBlank
    val email: String,
    @field:NotNull
    val isCorp: Boolean,
) : CommandValidator<MemberCreateCommand>()

data class MemberFindCommand(
    @field:NotNull
    val id: Long
) : CommandValidator<MemberFindCommand>()