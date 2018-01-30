# Stage 4 - Security

ATTENTION: This is NOT how proper authentication is done! Please do not use this as a template for your next project!!!
Seriously! I'm serious about this! This is just for educational purpose. I know you are going to use it in production :'-(

At the moment everything is open to everyone, so we want a __simple__ solution to secure an endpoint.
We do this by injecting a custom `User` type into any secured controller method, like this:

```kotlin
@GetMapping
    fun getAccounts(user: User) = service.readAccounts()
```

Looks too good to be true? Well, you are right.
But this is how the final solution will indeed look like as all the magic is done behind the scenes...

PS: Of course in a real world project we would use that user in order to retrieve only accounts belonging to him and
not simply all accounts ;)

## 4.1 - User concept

### 4.1.1 - Define the data class

We will define a very simple `User` object just identified by its name:

```kotlin
data class User(
        val name: String
)
```

### 4.1.2 - Define the service layer

Now implement a static service (no database connection) to do a fake-lookup:

```kotlin
interface UserService {
    fun findUser(name: String): User?
}

@Service
class UserServiceImpl : UserService {

    private val usersByName = listOf(
            User("admin"),
            User("customer")
    ).associateBy { it.name }

    override fun findUser(name: String) = usersByName[name]
}
```

## 4.2 - User resolver

Now comes the Spring magic...

### 4.2.1 - Implement the resolver

By implementing the `HandlerMethodArgumentResolver` interface we can add some more logic to Spring so it can detect and resolve our `User` class:

```kotlin
class UserResolver(
        private val userService: UserService
) : HandlerMethodArgumentResolver {

    private val headerUsername = "X-ultimate_username"

    override fun supportsParameter(parameter: MethodParameter) =
            parameter.parameterType == User::class.java

    override fun resolveArgument(parameter: MethodParameter, container: ModelAndViewContainer, request: NativeWebRequest, factory: WebDataBinderFactory): Any {
        val headerValue = request.getHeader(headerUsername) ?: throw UnauthorizedException()
        return userService.findUser(headerValue) ?: throw UnauthorizedException()
    }

}

class UnauthorizedException : Exception()
```


### 4.2.2 - Handle custom exceptions

Without defining a user (or defining a wrong user) the default behaviour is just to throw some new kind of exception which will by default translate to an ugly 500 error code.
In order to customize Spring's behaviour when custom exceptions are thrown we need to register a response handler:

```kotlin
@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException) =
            ResponseEntity<String>("You are not authorized to access this page!", HttpStatus.UNAUTHORIZED)
            
}
```


### 4.2.3 - Register resolver to Spring

As a final step you need to register your custom resolver and wire-up the beans:

```kotlin
@Configuration
@EnableWebMvc
class UltimateWebMvcConfig : WebMvcConfigurerAdapter() {

    @Autowired
    private lateinit var userService: UserService

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(UserResolver(userService))
    }
    
}
```


## 4.3 - Oh, don't forget about the tests!

Add two new tests which either test for unknown or invalid username, both expecting a 401 status code:

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
            .header("X-ultimate_username", "wrongUsername")
            .build()

    val response = rest.exchange<String>(request)

    assertThat(response.statusCodeValue).isEqualTo(401)
}
```

Finally adapt the existing tests so they support this new security concept (simply add the fluent `header` method):

```kotlin
    val request = RequestEntity.get(URI.create("/accounts"))
                    // add this line here to the other two tests:
                    .header("X-ultimate_username", "customer")
                    .build()
```

Run all tests again and light up those green signs!

# The end

Nice one, congratulations! You mastered the whole Ultimate Kotlin Workshop :)
Have fun with everything you've learned, and ...

Happy Koding ^^

----
Navigation: [Home](../README.md) - [Extra Stage 5](Stage_5.md)
