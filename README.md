# Introduction

## About

The __Ultimate Kotlin Workshop__ guides you implementing a basic ReSTful webservice using the [Kotlin](https://kotlinlang.org/) 
programming language.
Kotlin was announced from Google to be an official language support to develop Android apps but gains some momentum on the backend as well.
There is nowadays even support to write client applications just like TypeScript which translates to JavaScript,
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
1. [IntelliJ IDE](https://www.jetbrains.com/idea/download/) (the free community edition is sufficient)
1. [Gradle](https://gradle.org/install/) (tool to build the app)

# Step-by-step Howto

The workshop is split into 4 stages, depending on the given timebox and the progress of the class, it is likely that not all stages will be covered. 
In this case please feel free to go through them on your own.

* [Stage 1](doc/Stage_1.md): Hello World
* [Stage 2](doc/Stage_2.md): CRUD operations
* [Stage 3](doc/Stage_3.md): Persistence
* [Stage 4](doc/Stage_4.md): Security
* [Stage 5](doc/Stage_5.md): Outlook

# Prepared Solutions

The [final_solution] folder contains all the project sources needed to finish this workshop.
Whenever you are lost, feel free to cheat and have a look at that project.

The [ultimate_solution] is based upon the final solution, but contains the implementation for the extra stage 5.
