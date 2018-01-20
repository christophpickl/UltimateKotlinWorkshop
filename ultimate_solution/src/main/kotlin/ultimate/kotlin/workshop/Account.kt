package ultimate.kotlin.workshop

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * The data model used for JSON marshalling.
 */
data class Account(
        val id: Long,
        val alias: String,
        val balance: Int
)

/**
 * The service containing any business logic.
 *
 * It should be independent of the any view technology (ReST/HTTP in our case).
 */
@Service class AccountService(
        private val repo: AccountRepository
) {
    fun readAccounts() = repo.findAll().map { it.toAccount() }
}

@RestController
@RequestMapping("/accounts")
class AccountController(
        private val service: AccountService
) {

    @GetMapping
    fun getAccounts(user: User) = service.readAccounts()

}
