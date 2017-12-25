# Introduction

## About

The __Ultimate Kotlin Workshop__ guides you implementing a basic ReST webservice using [Kotlin](https://kotlinlang.org/)
and [Spring](https://spring.io/) framework. It will provide a JSON API to manage accounts and transfer money between them.
All of that will be persisted in an in-memory database using JPA+Hibernate as the ORM (object-relational mapping).
If time permits, we will also introduce a basic, custom security layer protecting our API.

It's target audience are developers with basic background experience in object oriented programming 
(preferably Java, C# or similar) and web development (HTTP, ReST).

## Prerequisites

You will need the following tools prepared on your machine to be able to follow the workshop:

1. [Java 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (9 will also do just fine)
1. [IntelliJ](https://www.jetbrains.com/idea/download/) (Community edition)
1. Gradle bin ???

Optional:

1. [Postman](https://www.getpostman.com/) ReST client to test your API manually

# Step-by-step Howto

## Stage 1 - Hello World

### Step 1.1 - Project setup 

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

### Step 1.2 - Implement ping feature

### Step 1.2.1 - Test first

* Add the following dependencies in your `build.gradle` file:

```groovy
compile('org.springframework.boot:spring-boot-starter-web')
testCompile('org.assertj:assertj-core:3.8.0')
```

* You might need to re-import your gradle project (enable auto-import if asked so)

* Create a new Kotlin class `PingTest` next to `WorkshopApplicationTests` in `src/test/kotlin`
* Add the following content to that file:

```kotlin
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PingTest {

    @Autowired private lateinit var rest: TestRestTemplate

    @Test
    fun `ping pong`() {
        val request = RequestEntity<String>(GET, URI.create("/ping"))

        val response = rest.exchange(request, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).isEqualTo("pong")
    }
}
```

* Run the test, it should fail saying that the connection was refused -that's ok ;)

### Step 1.2.2 - Implement the controller

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

## Stage 2 - CRUD operations

## Stage 3 - Persistence

## Stage 4 - Security


# Further Comments

## Links

* Kotlin and Gradle help: https://kotlinlang.org/docs/reference/using-gradle.html
