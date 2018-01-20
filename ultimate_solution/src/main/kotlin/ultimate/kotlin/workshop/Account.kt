package ultimate.kotlin.workshop

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class Account(
        val id: Long,
        val alias: String,
        val balance: Int
)

@Service class AccountService(
        private val repo: AccountRepository
) {
    fun readAccounts() = repo.findAll().map { it.toAccount() }
    fun createAccount(account: Account) = repo.save(account.toAccountJpa().copy(id = 0)).toAccount()
}

@RestController
@RequestMapping("/accounts")
class AccountController(
        private val service: AccountService
) {

    @GetMapping(produces = ["application/json"])
    fun getAccounts(user: User) = service.readAccounts()

    @PostMapping(produces = ["application/json"], consumes = ["application/json"])
    fun postAccount(user: User, @RequestBody account: Account) = service.createAccount(account)

}
