package ultimate.kotlin.workshop

import com.nhaarman.mockito_kotlin.whenever
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
import org.springframework.test.context.junit4.SpringRunner
import java.net.URI

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccountTest {

    @Autowired
    private lateinit var rest: TestRestTemplate

    @MockBean
    private lateinit var mockAccountService: AccountService

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

    private fun givenAccount(account: Account) = account.apply {
        whenever(mockAccountService.readAccounts()).thenReturn(listOf(account))
    }
}

inline fun <reified O> TestRestTemplate.exchange(request: RequestEntity<*>) =
        exchange(request, object : ParameterizedTypeReference<O>() {})!!
