package ultimate.kotlin.workshop

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

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
    fun getAccounts(user: User) = accountService.readAccounts()

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

@Entity
@Table(name = "account")
data class AccountJpa(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
        @SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence")
        @Column(updatable = false, nullable = false)
        val id: Long,

        @Column(length = 255)
        val alias: String,

        val balance: Int
)

fun AccountJpa.toAccount() = Account(id, alias, balance)
fun Account.toAccountJpa() = AccountJpa(id, alias, balance)

@Repository
interface AccountRepository : JpaRepository<AccountJpa, Long>
