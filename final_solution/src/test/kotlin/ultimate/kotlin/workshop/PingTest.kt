package ultimate.kotlin.workshop

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod.GET
import org.springframework.http.RequestEntity
import org.springframework.test.context.junit4.SpringRunner
import java.net.URI

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PingTest {

    @Autowired private lateinit var rest: TestRestTemplate

    @Test
    fun `ping pong`() {
        val request = RequestEntity<String>(GET, URI.create("/ping"))

        val response = rest.exchange(request, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).isEqualTo("pong")
    }
}
