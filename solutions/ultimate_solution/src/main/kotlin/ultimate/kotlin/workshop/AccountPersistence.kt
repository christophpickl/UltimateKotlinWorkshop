package ultimate.kotlin.workshop

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "account")
data class AccountJpa(

        @GenericGenerator(
                name = "accountSequenceGenerator",
                strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                parameters = [
                    Parameter(name = "sequence_name", value = "ACCOUNT_SEQUENCE"),
                    Parameter(name = "initial_value", value = "1000"),
                    Parameter(name = "increment_size", value = "1")
                ]
        )
        @Id
        @GeneratedValue(generator = "accountSequenceGenerator")
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
