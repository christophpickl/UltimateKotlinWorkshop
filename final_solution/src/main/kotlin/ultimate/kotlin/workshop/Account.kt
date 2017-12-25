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
//{
//    constructor() : this(1, "alias", 42)
//}

@RestController
@RequestMapping("/accounts")
class AccountController(
        private val service: AccountService
) {

    @GetMapping
    fun getAccounts() = service.readAccounts()

}

interface AccountService {
    fun readAccounts(): List<Account>
}

@Service
class AccountServiceImpl : AccountService {
    private val accounts = mutableListOf<Account>()
    override fun readAccounts() = accounts
}
