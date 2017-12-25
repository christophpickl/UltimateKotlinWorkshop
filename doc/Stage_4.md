# Stage 4 - Security

* At the moment everything is open to everyone, so we want a simple solution to secure an endpoint.
* We do this by injecting a custom `User` type into any secured controller method, like this:

```kotlin
@GetMapping
    fun getAccounts(user: User) = service.readAccounts()
```

* All the magic is done behind the scenes...


## 4.1 - Data class and service

* We will define a simple `User` object and implement a static service to manage them:

```kotlin
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
```


## 4.2 - Integrate parameter resolver

* By implementing the `HandlerMethodArgumentResolver` we can add some more logic to Spring so it can detect and resolve our `User` class:

```kotlin
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
```

* So default behaviour is just to throw some new kind of exception, which will by default in a 500 error code.
* In order to customize Spring's behaviour when custom exceptions are thrown, you need to register a response handler:

```kotlin
@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException) =
            ResponseEntity<String>("You are not authorized to access this page!", HttpStatus.UNAUTHORIZED)
}
```


## 4.3 - Register custom resolver

* As a final step you need to inject Spring your custom resolver and wire-up the beans:

```kotlin
@Configuration
@EnableWebMvc
class MyWebMvcConfig : WebMvcConfigurerAdapter() {

    @Autowired private lateinit var userService: UserService

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(UserResolver(userService))
    }
}
```


## 4.4 - The tests

* Add two new tests which either test for unknown or invalid username, both expecting a 401 status code:

```kotlin
@Test
fun `When GET accounts without username Then return 401 Unauthorized`() {
    val request = RequestEntity.get(URI.create("/accounts")).build()

    val response = rest.exchange<String>(request)

    assertThat(response.statusCodeValue).isEqualTo(401)
}

@Test
fun `When GET accounts with wrong username Then return 401 Unauthorized`() {
    val request = RequestEntity.get(URI.create("/accounts"))
            .header(HEADER_USERNAME, "wrongUsername")
            .build()

    val response = rest.exchange<String>(request)

    assertThat(response.statusCodeValue).isEqualTo(401)
}
```

* Finally adapt the existing tests so they support this new security concept:

```kotlin
    val request = RequestEntity.get(URI.create("/accounts"))
                    .header(HEADER_USERNAME, USERNAME_CUSTOMER) // add this line
                    .build()
```

## The end

Nice one, congratulations! You mastered the whole Ultimate Kotlin Workshop :)
Have fun with everything you've learned, and

happy koding

# Extra Stage 5 - Transfer money

* Add possibility to transfer money from one account to another
* Be aware of authorization and validation and other security concerns...

----
Navigation: [Home](../README.md)
