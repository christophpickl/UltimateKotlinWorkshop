# Stage 1 - Hello World

## 1.1 - Project setup 

* Download a prepared project skeleton:
	- Go to http://start.spring.io/
	- Change from `Maven Project` to `Gradle Project`
	- Change from `Java` to `Kotlin`
	- Keep Spring Boot version `1.5.9` (latest stable release)
	- Enter as Group: `ultimate.kotlin`
	- Enter as Artifact: `workshop`
	- // no dependencies needed, we will add them manually
	- Hit `Generate Project` button & download & unzip
* Prepare the project:
	- Open `workshop/gradle/wrapper/gradle-wrapper.properties`
		* Change from` gradle-3.5.1-bin.zip` to `gradle-4.4.1.zip`
	- Open `workshop/build.gradle`
		* Change `kotlinVersion` from `1.1.61` to `1.2.10`
		* Remove line `apply plugin: 'eclipse'`
* Import project:
	- TODO TODO TODO TODO TODO
	- create new project, empty
	- setup project SDK (select java 8 SDK)
	- import module existing source -> reference starter.spring.io root directory
	    * select gradle
	    * OK
	- TODO TODO TODO TODO TODO
* Test application runs:
    - Locate the `WorkshopApplication` class and run it (it should quit immediately)

## 1.2 - Implement ping feature

## 1.2.1 - Test first

* Add the following dependencies in your `build.gradle` file:

```groovy
compile('org.springframework.boot:spring-boot-starter-web')
testCompile('org.assertj:assertj-core:3.8.0')
testCompile('com.nhaarman:mockito-kotlin:1.5.0')
```

* You might need to re-import your gradle project (enable auto-import if asked so).

* Create a new Kotlin class `PingTest` next to `WorkshopApplicationTests` in `src/test/kotlin`.
* Add the following content to that file:

```kotlin
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PingTest {

    @Autowired private lateinit var rest: TestRestTemplate

    @Test
    fun `When GET ping Then pong text returned`() {
        val request = RequestEntity.get(URI.create("/ping")).build()

        val response = rest.exchange(request, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).isEqualTo("pong")
    }

}
```

* Run the test, it should fail saying that the connection was refused -that's ok ;)

## 1.2.2 - Implement the controller

* Inside of the `WorkshopApplication.kt` file, create a new Kotlin class:

```kotlin
@RestController
@RequestMapping("/ping")
class PingController {
    @GetMapping
    fun ping() = "pong"
}
```

* Now run the test again -it should be green now :)
* You can also invoke the service directly from your browser (or use Postman): http://localhost:8080/ping

## 1.2.3 - Content negotation

* We will now add the capability of providing an accept header, which will determine the content/MIME type of the response:

    1. `text/plain`
    1. `application/json`

* Adapt the `PingTest` by adding one more test like this:

```kotlin
@Test
fun `When GET ping accepting JSON Then JSON payload is returned`() {
    val request = RequestEntity.get(URI.create("/ping"))
            .header("accept", "application/json")
            .build()

    val response = rest.exchange(request, String::class.java)

    assertThat(response.statusCodeValue).isEqualTo(200)
    assertThat(response.body).isEqualTo("""{"message":"pong"}""")
}
```

* When running this new test it should fail, whereas the existing one should still be green.
* Now define a new data class `Pong` storing the message and enhance the `PingController` specifying explicit produce types:

```kotlin
data class Pong(
        val message: String
)

@RestController
@RequestMapping("/ping")
class PingController(
        private val mapper: ObjectMapper
) {
    
    private val pingResponse = "pong"

    @GetMapping(produces = ["text/plain"])
    fun pingPlain() = pingResponse

    @GetMapping(produces = ["application/json"])
    fun pingJson() = Pong(pingResponse)

}
``` 

* In order to keep things tidy, move that class into its own `Ping` file, just like `Account`.
* Now run all test cases for ping which should now have turned both green.

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
