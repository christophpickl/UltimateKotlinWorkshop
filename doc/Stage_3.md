# Stage 3 - Persistence


## 3.1 - Adapt gradle build

* Add a plugin and a dependency to Spring's supreme JPA data library:

```groovy
buildscript {
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion"
    }
}

apply plugin: "kotlin-jpa"

dependencies {
    compile "org.springframework.boot:spring-boot-starter-data-jpa"
    runtime "com.h2database:h2:1.4.196"
}
```


## 3.2 - Implement persistence layer

### 3.2.1 - Define the database object

* Create a new data class `AccountJpa` which will be used by Hibernate to map it onto a relational database.

```kotlin
@Entity
@Table(name = "account")
data class AccountJpa(

        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = "account_sequence")
        @SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence")
        @Column(updatable = false, nullable = false)
        val id: Long,

        @Column(length = 255)
        val alias: String,

        val balance: Int
)

```

### 3.2.2 - Define the transformations

* These extension functions will convert from/to the API (ReST) and the JPA (DB) representation.
* The big advantage of doing it this way is that neither of them actually know from each other (low coupling).

```kotlin
fun AccountJpa.toAccount() = Account(id, alias, balance)
fun Account.toAccountJpa() = AccountJpa(id, alias, balance)
```

### 3.2.3 - Define a repository

* Declare a new `JpaRepository` which provides most of the basic operations by default, it's Spring magic :)

```kotlin
@Repository
interface AccountRepository : JpaRepository<AccountJpa, Long>
```


## 3.3 - Adapt existing test

* Change the existing `AccountTest` by using the repository to setup testdata rather mocking out the service layer.
* ATTENTION: As the tests share a common database (global state!) we need to do some cleanup via Spring's `@DirtiesContext` annotation.

```kotlin
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountTest {

    // remove: @MockBean private lateinit var mockService: AccountService
    @Autowired private lateinit var repo: AccountRepository
    
    private fun givenAccount(account: Account) =
        repo.save(account.toAccountJpa()).toAccount()
}
```

* Running this test should fail now, which is because we didn't actually tell our service to use the new database.

## 3.4 - Wire up the repository

* Now wire the new `AccountRepository` into the `AccountServiceImpl`:

```kotlin
@Service
class AccountServiceImpl(
        private val repo: AccountRepository
) : AccountService {

    override fun readAccounts() = repo.findAll().map { it.toAccount() }

}
```


## 3.5 - Configure Spring datasource

* Remove the old `application.properties` located in `src/main/resources` and replace it 
  with a new YAML based file called `application.yml` and the following content:

```yaml
spring:
    datasource:
      driver-class-name: org.h2.Driver
      url: jdbc:h2:mem:ultimateDb
```

* Which means we configure Spring's data source to an in-memory database called [H2](http://www.h2database.com).

* Finally run all tests again which should give you enough confidence your work is done and you did a good job :)
* Go ahead and proceed to the final stage. 

----
Navigation: [Home](../README.md) - [Next Stage 4](Stage_4.md)
