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
        private val service: AccountService
) {

    @GetMapping
    fun getAccounts(user: User) = service.readAccounts()

}

interface AccountService {

    fun readAccounts(): List<Account>

}

@Service
class AccountServiceImpl(
        private val repo: AccountRepository
) : AccountService {

    override fun readAccounts() = repo.findAll().map { it.toAccount() }

}
