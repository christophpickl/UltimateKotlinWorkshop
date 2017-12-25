package ultimate.kotlin.workshop

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "account")
data class AccountJpa(

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = "account_sequence")
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
