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

    @Autowired private lateinit var rest: TestRestTemplate
    @MockBean private lateinit var mockService: AccountService

    @Test
    fun `When GET accounts Then return empty list`() {
        val request = RequestEntity.get(URI.create("/accounts")).build()

        val response = rest.exchange(request, object : ParameterizedTypeReference<List<Account>>() {})

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).isEqualTo(emptyList<Account>())
    }

    @Test
    fun `Given single account existing When GET accounts Then return that account`() {
        val account = Account(1, "alias", 42)
        givenAccountsExist(listOf(account))

        val request = RequestEntity.get(URI.create("/accounts")).build()

        val response = rest.exchange(request, object : ParameterizedTypeReference<List<Account>>() {})

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).containsExactly(account)
    }

    private fun givenAccountsExist(accounts: List<Account>) {
        whenever(mockService.readAccounts()).thenReturn(accounts)
    }

}

// inline fun <I, reified O> TestRestTemplate.exchange(request: RequestEntity<I>) =
//         exchange(request, object : ParameterizedTypeReference<O>() {})
