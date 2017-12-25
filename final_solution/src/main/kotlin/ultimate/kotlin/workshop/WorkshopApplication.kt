package ultimate.kotlin.workshop

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class WorkshopApplication

fun main(args: Array<String>) {
    SpringApplication.run(WorkshopApplication::class.java, *args)
}
