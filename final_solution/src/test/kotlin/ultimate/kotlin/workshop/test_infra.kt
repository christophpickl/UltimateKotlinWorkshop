package ultimate.kotlin.workshop

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.RequestEntity

inline fun <reified O> TestRestTemplate.exchange(request: RequestEntity<*>) =
        exchange(request, object : ParameterizedTypeReference<O>() {})!!
