# Stage 3 - Persistence

Now it's time to add the persistence layer.
For that we will use JPA (Java's standardized persistence API) and (again) some Spring magic to wire everything up behind the scenes. 

## 3.1 - Adapt Gradle the build

Add a plugin and a dependency to Spring's supreme JPA data library:

```groovy
buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-noarg:${kotlinVersion}")
        // ...
    }
    // ...
}

apply plugin: 'kotlin-jpa'

dependencies {
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    runtime("com.h2database:h2:1.4.196")
    // ...
}
```


## 3.2 - Implement persistence layer

### 3.2.1 - Database object

Create a new data class `AccountJpa` which will be used by Hibernate to map it onto a relational database.

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

As you can see there is not much XML configuration or database schema (SQL DDL) stuff going on here,
as everything is simply inferred by annotations.
Yes, instead of good old "XML based programming", we nowadays do a lot of "annotation based programming" ;)

PS: Use the `javax` annotations rather the Hibernate specific ones where possible.

### 3.2.2 - Define transformations

In order to transform back and forth from the API (ReST) and the database (JPA) representation, we need some (extension) functions
to do the necessary transformation.

```kotlin
fun AccountJpa.toAccount() = Account(id, alias, balance)
fun Account.toAccountJpa() = AccountJpa(id, alias, balance)
```

The big advantage of doing it this way is that neither of them actually know from each other (keeping a desired "low coupling").


### 3.2.3 - Define a repository

Declare a new `JpaRepository` which provides most of the basic operations by default - it's again Spring magic :)
You really don't have to do more in order support all CRUD (and much more) operations on the database layer.

```kotlin
@Repository
interface AccountRepository : JpaRepository<AccountJpa, Long>
```


## 3.3 - Adapt existing test

Change the existing `AccountTest#givenAccount` method by using the repository to setup the test data rather mocking out the service layer.

ATTENTION: As the tests share a common database (global state!) we need to do some cleanup via Spring's `@DirtiesContext` annotation.
(This could be of course improved by simply resetting the database rather destroying the whole Spring application context each time, but we can live with that for the moment.)

```kotlin
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AccountTest {

    @Autowired
    private lateinit var repo: AccountRepository

    // ...
    
    private fun givenAccount(account: Account) =
        repo.save(account.toAccountJpa()).toAccount()

}
```

Running this test should fail for now, which is because we didn't actually tell our service to use the new database of course ;)

## 3.4 - Wire up the repository

Now wire the new `AccountRepository` into the `AccountServiceImpl` and delegate plus transform the returned accounts:

```kotlin
@Service
class AccountServiceImpl(
        private val repo: AccountRepository
) : AccountService {

    override fun readAccounts() = repo.findAll().map { it.toAccount() }

}
```


## 3.5 - Configure Spring datasource

Remove the old `application.properties` located in `src/main/resources` and replace it with a much cooler YAML based file called `application.yml` and add the following contents:

```yaml
spring:
    datasource:
      driver-class-name: org.h2.Driver
      url: jdbc:h2:mem:ultimateDb
```

The above configures Spring's data source to use an in-memory database called [H2](http://www.h2database.com).
Remember we've added a runtime dependency to it just recently?!

Finally run all tests again which should give you enough confidence your work is done and you did a good job :)

Now go ahead and proceed to the final stage you master hacker to whom all bases belong to! 

----
Navigation: [Home](../README.md) - [Next Stage 4](Stage_4.md)
