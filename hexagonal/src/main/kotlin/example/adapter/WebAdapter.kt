package example.adapter

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@RestController
@RequestMapping
annotation class WebAdapter(

    // WebAdapter 어노테이션 value 정보를 넘기면 RestController 어노테이션에 value로 값 전달
    @get: AliasFor(annotation = RestController::class)
    val value: String = "",

    // WebAdapter 어노테이션 path 정보를 넘기면 RequestMapping 어노테이션에 path로 값 전달
    @get: AliasFor(annotation = RequestMapping::class, attribute = "path")
    val path: String = ""

)
