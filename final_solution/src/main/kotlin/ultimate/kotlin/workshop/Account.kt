package ultimate.kotlin.workshop

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class Account(
        val id: Long,
        val alias: String,
        val balance: Int
)

@RestController
@RequestMapping("/accounts")
class AccountController {

    @GetMapping
    fun getAccounts() = emptyList<Account>()

}

