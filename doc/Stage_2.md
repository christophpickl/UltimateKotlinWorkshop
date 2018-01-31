# Stage 2 - Read Accounts

In this stage we will cover implementing a simple list request for accounts.
This represents one of the four CRUD operations, which stands for **C**reate **R**ead **U**pdate **D**elete and 
defines a commonly known set of basic operations on entities.

## 2.1 - Create the domain object

We will need some kind of entity description which we will be dealing with.
Do so by creating a new file `Account.kt` next to `WorkshopApplication.kt` and add the following data class:

```kotlin
data class Account(
        val id: Long,
        val alias: String,
        val balance: Int
)
```


## 2.2 - Test first

We will again write a (failing) test first and implement as long as we satisfy the test's assertions.
Simply copy the already existing `PingTest` and rename it to `AccountTest`.
Replace the existing method with the following (rename the class name, but leave everything else):

```kotlin
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccountTest {

    @Autowired
    private lateinit var rest: TestRestTemplate
    
    @Test
    fun `When GET accounts Then return empty list`() {
        val response = rest.exchange(
            RequestEntity.get(URI.create("/accounts")).build(),
            object : ParameterizedTypeReference<List<Account>>() {}
        )
    
        assertThat(response.body).isEqualTo(emptyList<Account>())
    }

}
```

You might wonder what this fancy `object : ParameterizedTypeReference<List<Account>>() {}` thing is, right?!
Simply put: This is a workaround for Java's type erasure, something nasty which means that type parameters (generics) are
not available during runtime, as the compiler will remove that information... all for the sake of backwards compatibility.
But don't worry, Kotlin got a nice feature (extension functions) to workaround this workaround and use reified generics to make it all clean and shiny again
and just pretend everything is good under the hood :)


## 2.3 - Add a new controller

Copy the existing `PingController` text and paste it into the `Account.kt` file.
Adapt the class name, request mapping path and replace the method accordingly to:

```kotlin
@RestController
@RequestMapping("/accounts")
class AccountController {

    @GetMapping
    fun getAccounts() = emptyList<Account>()

}
```

Running the test now you should see green lights. Well, the implementation doesn't do much, so what about returning some real accounts? 


## 2.4 - Test again

Add a new test method to the `AccountTest` class, which needs to arrange test data first via a non-yet-known `givenAccount` method:

```kotlin
@Test
fun `Given single account When GET accounts Then return that account`() {
    val account = givenAccount(Account(0, "alias", 42))
    
    val response = rest.exchange(
        RequestEntity.get(URI.create("/accounts")).build(),
        object : ParameterizedTypeReference<List<Account>>() {}
    )

    assertThat(response.body).containsExactly(account)
}
```

We will keep this test not compiling for the time being as we proceed on introducing a separate service layer.


## 2.5 - Implement a dummy service layer

Add a new service definition right in the `Account.kt` file.
Best practice is to always separate the the interface from the implementation, even though it just might be a [header interface](https://martinfowler.com/bliki/HeaderInterface.html).

```kotlin
interface AccountService {
    fun readAccounts(): List<Account>
}

@Service
class AccountServiceImpl : AccountService {
    private val accounts = mutableListOf<Account>()
    override fun readAccounts() = accounts
}
```

Inject the service (interface) into the controller, delegating the read operation to the new service:

```kotlin
@RestController
@RequestMapping("/accounts")
class AccountController(
        private val accountService: AccountService
) {

    @GetMapping
    fun getAccounts() = accountService.readAccounts()

}
```

The Spring framework will do all the magic necessary to register and wire up the beans, which is a very handy thing.
But watch out: Those mighty dragons might be too powerfull for you to master at some stage -be warned... 


## 2.6 - Finish the test

It is now possible to mock the service layer in the `AccountTest`, so go ahead and declare that missing method using a `MockBean`
(kind of a marriage between Spring and the [Mockito testing library](http://site.mockito.org)):

```kotlin
class AccountTest {
    
    @MockBean
    private lateinit var mockAccountService: AccountService
    
    // ...
    
    private fun givenAccount(account: Account) = account.apply {
        whenever(mockAccountService.readAccounts()).thenReturn(listOf(account))
    }
}
```

Unfortunately although this SHOULD work, it doesn't as it fails with a `no suitable constructor found` exception :(
Reason is that there is a misconception in the world of Kotlin and the existing libraries which were originally developed for Java.
Fortunately there is a solution to that, by simply adding the following dependency which makes Jackson (the JSON library) aware of Kotlin's paradigm shift:

```groovy
compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.2")
```

PS: Speaking of nasty hacks: Remember the `ParameterizedTypeReference` type and the comment on that above?!
Now it's time to optimize that piece of code to use Kotlin's extension functions, reified generics and inline methods (sounds nasty too, right?!).

Add the following top level method next to the `AccountTest` class and adapt the existing HTTP request call as follows:

```kotlin
inline fun <reified O> TestRestTemplate.exchange(request: RequestEntity<*>) =
        exchange(request, object : ParameterizedTypeReference<O>() {})!!

// OLD
val response = rest.exchange(request, object : ParameterizedTypeReference<List<Account>>() {})

// NEW
val response = rest.exchange<List<Account>>(request)
```

Run all tests again just to verify we didn't break anything.

## 2.7 - Other CRUD operation

We will not cover the other CRUD operations because of time reasons but we can still head on to the next stage where we are going to persist our data to a database.

----
Navigation: [Home](../README.md) - [Next Stage 3](Stage_3.md)
