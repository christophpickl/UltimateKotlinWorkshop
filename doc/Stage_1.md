# Stage 1 - Ping-Pong

## 1.1 - Project Setup 

### 1.1.1 - Create Skeleton

Go to http://start.spring.io and download a prepared project skeleton:

1. Change from `Maven Project` to `Gradle Project`
1. Change from `Java` to `Kotlin`
1. Keep latest stable Spring Boot version (`1.5.9` in case of January 2018)
1. Enter any group name, e.g.: `ultimate.kotlin`
1. Enter any artifact name, e.g.: `workshop`
1. No dependencies needed, we will add them manually later on
1. Hit `Generate Project` button & download & unzip

![start_spring.png](https://github.com/christophpickl/UltimateKotlinWorkshop/raw/master/doc/img/start_spring.png "start.spring.io sample page")

### 1.1.2 - Import Project

We will first update some of the generated files in order to keep up with most recent versions:

1. Open `workshop/gradle/wrapper/gradle-wrapper.properties`
    * Change from` gradle-3.5.1-bin.zip` to `gradle-4.5-all.zip`
1. Open `workshop/build.gradle`
    * Change `kotlinVersion` from `1.1.61` to `1.2.21` (lookup latest version from [compiler website](https://kotlinlang.org/docs/tutorials/command-line.html))
    * Change the Kotlin standard library dependency from `kotlin-stdlib-jre8` to `kotlin-stdlib-jdk8`
    * Remove line `apply plugin: 'eclipse'` as we are going to use IntelliJ
1. In IntelliJ select "File / New / Project from Existing Sources..."
    * Choose the Spring Project folder (`ultimate`)
    * Select "Import project from external model" and choose Gradle, hit Next
    * Nothing to change on the next screen as defaults should just do, hit Finish
    * Everything should be done for you now automatically 
1. In order to verify the development environment works as expected, run the predefined `WorkshopApplicationTests` and watch the green lights :)

![initial_project.png](https://github.com/christophpickl/UltimateKotlinWorkshop/raw/master/doc/img/initial_project.png "Initial Project in IntelliJ")

## 1.2 - Ping pong

We will now implement a simple `GET /ping` endpoint which returns the static text `pong` to get a first impression on 
how ReST endpoints look like with Spring Boot.

### 1.2.1 - Test first

Add the following dependencies in your `build.gradle` file (You might need to re-import your gradle project and 
want to enable auto-import if asked so):

```groovy
compile("org.springframework.boot:spring-boot-starter-web")
testCompile("org.assertj:assertj-core:3.8.0")
testCompile("com.nhaarman:mockito-kotlin:1.5.0")
```

Create a new Kotlin class `PingTest` next to `WorkshopApplicationTests` in `src/test/kotlin` with the following content:

```kotlin
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PingTest {

    @Autowired
    private lateinit var rest: TestRestTemplate

    @Test
    fun `When GET ping Then pong text returned`() {
        val response = rest.exchange(RequestEntity.get(URI.create("/ping")).build(), String::class.java)

        assertThat(response.body).isEqualTo("pong")
    }

}
```

Run the test. It should fail saying that the connection was refused -that's ok (actually good, as of "testing in the red") 
because we haven't implemented the controller yet ;)


### 1.2.2 - Implement the controller

Create a new file `Ping.kt ` next to the `WorkshopApplication.kt` file and add a new ReST controller:

```kotlin
@RestController
@RequestMapping("/ping")
class PingController {
    @GetMapping
    fun ping() = "pong"
}
```

Now run the test again and watch it turn green :)
You can also invoke the service directly (by running the `WorkshopApplication`'s main method) and
open up http://localhost:8080/ping from your browser (or Postman). 


## 1.3 - Content negotation

We will now add the capability of providing an accept header which will determine the content type of the response.
For this we will support to different types:

1. `text/plain` (we already did this)
1. `application/json` (the most common format as of today, R.I.P. XML)

### 1.3.1 - Test first

First of all adapt the `PingTest` class by adding one more test method like this and watch it turn red:

```kotlin
@Test
fun `When GET ping accepting JSON Then JSON payload is returned`() {
    val response = rest.exchange(
        RequestEntity.get(URI.create("/ping"))
            .header("accept", "application/json") // this line is important
            .build(),
            String::class.java)

    assertThat(response.body).isEqualTo("""{"message":"pong"}""")
}
```

The test fails with the following message (as it will still serve us plain text and don't bother about our provided accept header):

```
org.junit.ComparisonFailure: 
Expected :"{"message":"pong"}"
Actual   :"pong"
```

### 1.3.2 - Update the controller

Define a new, so called "data class" `Pong` storing the message and enhance the `PingController` specifying explicit produce types:

```kotlin
data class Pong(
        val message: String
)

@RestController
@RequestMapping("/ping")
class PingController {
    
    private val pingResponse = "pong"

    @GetMapping(produces = ["text/plain"])
    fun pingPlain() = pingResponse

    @GetMapping(produces = ["application/json"])
    fun pingJson() = Pong(pingResponse)

}
``` 

Now run all test cases for ping which should now have turned both green.

Well done, you finished the very basics of a ReSTful webservice using Kotlin and Spring Boot. Wasn't that hard, was it?! ;)

Now let's head on and implement some very common functionality in the world of services: CRUD

----
Navigation: [Home](../README.md) - [Next Stage 2](Stage_2.md)
