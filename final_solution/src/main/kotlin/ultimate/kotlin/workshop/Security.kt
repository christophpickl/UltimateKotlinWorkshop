package ultimate.kotlin.workshop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

const val HEADER_USERNAME = "X-ultimate_username"
const val USERNAME_ADMIN = "admin"
const val USERNAME_CUSTOMER = "customer"

data class User(
        val name: String
)

interface UserService {

    fun findUser(name: String): User?
}

@Service
class UserServiceImpl : UserService {

    private val usersByName = listOf(
            User(USERNAME_ADMIN),
            User(USERNAME_CUSTOMER)
    ).associateBy { it.name }

    override fun findUser(name: String) = usersByName[name]

}

class UserResolver(
        private val userService: UserService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) =
            parameter.parameterType == User::class.java

    override fun resolveArgument(parameter: MethodParameter, container: ModelAndViewContainer, request: NativeWebRequest, factory: WebDataBinderFactory): Any {
        val headerValue = request.getHeader(HEADER_USERNAME) ?: throw UnauthorizedException()
        return userService.findUser(headerValue) ?: throw UnauthorizedException()
    }

}

class UnauthorizedException : Exception()

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException) =
            ResponseEntity<String>("You are not authorized to access this page!", HttpStatus.UNAUTHORIZED)

}

@Configuration
@EnableWebMvc
class UltimateWebMvcConfig : WebMvcConfigurerAdapter() {

    @Autowired private lateinit var userService: UserService

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(UserResolver(userService))
    }
}
