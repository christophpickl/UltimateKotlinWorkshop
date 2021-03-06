package ultimate.kotlin.workshop

import org.springframework.stereotype.Service
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
class AccountController(
        private val accountService: AccountService
) {

    @GetMapping
    fun getAccounts() = accountService.readAccounts()

}

interface AccountService {
    fun readAccounts(): List<Account>
}

@Service
class AccountServiceImpl : AccountService {
    private val accounts = mutableListOf<Account>()
    override fun readAccounts() = accounts
}
