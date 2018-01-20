
# Extra Stage 5

For those who are interested in doing more, there are two major tasks which still should be implemented in order to make this a useful application.

## CRUD operations

Right now only the READ operation for accounts is supported.
So your task is to also implement CREATE, UPDATE and DELETE of accounts (preferable test driven by reusing the existing test infrastructure).
It's actually basic straight forward, as you simply have to interact with Spring's predefined `JpaRepository` which offers all the basic functionality needed.

## Transfer money

* Add possibility to transfer money from one account to another
* Be aware of authorization and validation and as well as other security concerns...

## Out of scope

The following topics have not been covered in this workshop:

* Continuous Integration
* Releasing
* Containers (Docker)
* Deployin to the cloud
* Versioning of the API
* DB migration (Liquibase, Flybase)
* Security
* Logging (Logback, Log4J)
* Documentation (Swagger)
* ... and much more ...

# Further reading

* Open source project tools:
    * Continuous Integration: http://travis-ci.org
    * Code coverage: https://codecov.io
    * Dependency version checker: https://www.versioneye.com
* Using Gradle with Kotlin:
    * https://kotlinlang.org/docs/reference/using-gradle.html
* ReST client to test your API manually:
    * https://www.getpostman.com

----
Navigation: [Home](../README.md)
