package ultimate.kotlin.workshop

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.RequestEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.junit4.SpringRunner
import java.net.URI

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountTest {

    @Autowired
    private lateinit var rest: TestRestTemplate

    @Autowired
    private lateinit var repo: AccountRepository

    @Test
    fun `When GET accounts Then return empty list`() {
        val response = rest.exchange<List<Account>>(RequestEntity.get(URI.create("/accounts")).build())

        assertThat(response.body).isEqualTo(emptyList<Account>())
    }

    @Test
    fun `Given single account When GET accounts Then return that account`() {
        val account = givenAccount(Account(0, "alias", 42))

        val response = rest.exchange<List<Account>>(RequestEntity.get(URI.create("/accounts")).build())

        assertThat(response.body).containsExactly(account)
    }

    private fun givenAccount(account: Account) =
            repo.save(account.toAccountJpa()).toAccount()
}

inline fun <reified O> TestRestTemplate.exchange(request: RequestEntity<*>) =
        exchange(request, object : ParameterizedTypeReference<O>() {})!!
