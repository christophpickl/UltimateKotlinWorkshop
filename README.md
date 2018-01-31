# Introduction

## About

The __Ultimate Kotlin Workshop__ guides you implementing a basic ReSTful webservice using the [Kotlin](https://kotlinlang.org/) 
programming language.
Kotlin was announced from Google to be an official language support to develop Android apps but gains some momentum on the backend as well.
There is nowadays even support to write client applications just like [TypeScript](https://www.typescriptlang.org/) which translates to JavaScript,
enabling you to write shared code for different platforms.

It will provide a JSON API to manage accounts and maybe transfer money between them.
All of that will be persisted in an in-memory database using the Java Persistence API (JPA) and 
an object-relational mapper called [Hibernate](http://hibernate.org/) under the hood.
The whole application management will be done by the [Spring](https://spring.io/) framework,
especially all the details are covered by Spring Boot, so we can focus on the "real stuff".
If time permits we will also introduce a basic, custom security layer protecting our API.

Its target audience are developers with basic experience in any object oriented programming language 
(preferably Java, C# or Ruby, Python, ...) and general web development (HTTP, ReST, JSON).

## Prerequisites

You will need the following tools prepared on your computer to be able to follow the workshop:

1. [Java 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (or newer)
    * Execute the following command on the command line in order to verify Java works: `$ java -version`
1. [IntelliJ IDE](https://www.jetbrains.com/idea/download/) (the free community edition is sufficient)
    * Please get sure you've installed the most recent Kotlin plugin as this usually leads to some problems afterwards. You can verify this by going to Preferences / Plugins / Browse Repositories / Kotlin and hit the Update button.
    * Also please verify you can actually use the JDK in your IDE (just create a simple Java/Kotlin application and see whether there are configuration errors)
1. [Gradle](https://gradle.org/install/) (tool to build the app)


# Stages

The workshop is split into 4 stages, depending on the given timebox and the progress of the class, it is likely that not all stages will be covered. 
In this case please feel free to go through them on your own.

* [Stage 1](doc/Stage_1.md): Ping-Pong
* [Stage 2](doc/Stage_2.md): Read Accounts
* [Stage 3](doc/Stage_3.md): Persistence
* [Stage 4](doc/Stage_4.md): Security
* [Stage 5](doc/Stage_5.md): Extras

# Prepared Solutions

The [solutions] directory contains a solution for each step implementing all the tasks defined so far.
Whenever you are lost, feel free to cheat and either just have a look at that project or just take over the complete sources from there.

The [solutions/ultimate_solution] implements all stages including the extra 5th stage (full CRUD operations and transaction support).
