package ultimate.kotlin.workshop

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.RequestEntity
import org.springframework.test.context.junit4.SpringRunner
import java.net.URI

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccountTest {

    @Autowired private lateinit var rest: TestRestTemplate

    @Test
    fun `GET accounts should return empty list`() {
        val request = RequestEntity.get(URI.create("/accounts")).build()

        val response = rest.exchange(request, object : ParameterizedTypeReference<List<Account>>() {})

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).isEqualTo(emptyList<Account>())
    }

}

// inline fun <I, reified O> TestRestTemplate.exchange(request: RequestEntity<I>) =
//         exchange(request, object : ParameterizedTypeReference<O>() {})
