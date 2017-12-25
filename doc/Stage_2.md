# Stage 2 - CRUD operations

* CRUD stands for **C**reate **R**ead **U**pdate **D**elete and defines the basic (manipulation) operation on entities.

## 2.1 - Read operation

### 2.1.1 - Create the domain object

* We will need some kind of entity description which with we will be dealing with.
* Do so by creating a new file `Account.kt` next to `WorkshopApplication.kt` and add the following so-called _data class_:

```kotlin
data class Account(
        val id: Long,
        val alias: String,
        val balance: Int
)
```

### 2.1.2 - Test first

* Copy `PingTest` and rename it to `AccountTest`.
* Replace the existing method with the following (rename the class name, but leave everything else):

```kotlin
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccountTest {

    @Autowired private lateinit var rest: TestRestTemplate
    
    @Test
    fun `When GET accounts Then return empty list`() {
        val request = RequestEntity.get(URI.create("/accounts")).build()
    
        val response = rest.exchange(request, object : ParameterizedTypeReference<List<Account>>() {})
    
        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).isEqualTo(emptyList<Account>())
    }

}
```

* Running the test should fail for now.

### 2.1.3 - Add a new controller for accounts

* Copy the `PingController` text and paste it into the `Account.kt` file.
* Adapt the class name, request mapping path and replace the method accordingly to:

```kotlin
@RestController
@RequestMapping("/accounts")
class AccountController {

    @GetMapping
    fun getAccounts() = emptyList<Account>()

}
```

* If you run the test now, it should be green, for this specific case but what about actual returned accounts?!

### 2.1.4 - Test first again

* Add a new test method in `AccountTest` which needs to arrange test data first via a non-yet-known `givenAccountsExist` method:

```kotlin
@Test
fun `Given single account existing When GET accounts Then return that account`() {
    val account = Account(1, "alias", 42)
    givenAccountsExist(listOf(account))

    val request = RequestEntity.get(URI.create("/accounts")).build()

    val response = rest.exchange(request, object : ParameterizedTypeReference<List<Account>>() {})

    assertThat(response.statusCodeValue).isEqualTo(200)
    assertThat(response.body).containsExactly(account)
}
```

* We will keep this test not compiling for the time being as we proceed on introducing a separate service layer.

### 2.1.5 - Service layer

* In the `Account` file add the following service definition:

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

* Wire up the interface into the controller, delegating the read operation to the new service.

### 2.1.6 - Finish the test

* In the `AccountTest` it is now possible to mock the service layer:

```kotlin
// ...
class AccountTest {
    // ...
    @MockBean private lateinit var mockService: AccountService
    // ...
    private fun givenAccountsExist(accounts: List<Account>) {
        whenever(mockService.readAccounts()).thenReturn(accounts)
    }
}
```

* Unfortunately although this SHOULD work, it doesn't as it fails with a `no suitable constructor found` exception.
* Reason is, there is a misconception in the world of Kotlin and the existing libraries which were originally developed for Java.
* Fortunately there is a solution to that, by simply adding the following dependency which makes Jackson (the JSON library) aware of Kotlin's paradigm shift:

```groovy
compile('com.fasterxml.jackson.module:jackson-module-kotlin:2.9.2')
```
    
    
## 2.2 - Create operation

## 2.3 - Update operation

## 2.4 - Delete operation


----
Navigation: [Home](../README.md) - [Next Stage 3](Stage_3.md)
