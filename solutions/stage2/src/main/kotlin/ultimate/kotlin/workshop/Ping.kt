package ultimate.kotlin.workshop

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class Pong(
        val message: String
)

@RestController
@RequestMapping("/ping")
class PingController(
        private val mapper: ObjectMapper
) {

    private val pingResponse = "pong"

    @GetMapping(produces = ["text/plain"])
    fun pingPlain() = pingResponse

    @GetMapping(produces = ["application/json"])
    fun pingJson() = Pong(pingResponse)

}
