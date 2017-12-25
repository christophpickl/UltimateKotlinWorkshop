# About

The __Ultimate Kotlin Workshop__ guides you implementing a basic ReST webservice using the [Kotlin](https://kotlinlang.org/) and [Spring](https://spring.io/) framework.

## Prerequisites

You will need the following tools prepared on your machine to be able to follow the workshop:

1. [Java 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (9 will also do just fine)
1. [IntelliJ](https://www.jetbrains.com/idea/download/) (Community edition)

# Outline

* short intro me
* short intro kotlin
* stage 1: hello world
	- setup project
	- implement ping-pong service
	- support of different mime types
	- implement greeting service
* stage 2: CRUD operations
	- 
* stage 3: persistence
	- 

# Step-by-step Howto

## Stage 1 - Hello World

* download project skeleton
	- go to http://start.spring.io/
	- change from `Maven Project` to `Gradle Project`
	- change from `Java` to `Kotlin`
	- keep Spring Boot version `1.5.9` (latest stable release)
	- enter as Group: `ultimate.kotlin`
	- enter as Artifact: `workshop`
	- // no dependencies needed, we will add them manually
	- hit `Generate Project` button & download & unzip
* prepare project
	- open `workshop/gradle/wrapper/gradle-wrapper.properties`
		* change from` gradle-3.5.1-bin.zip` to `gradle-4.4.1.zip`
	- open `workshop/build.gradle`
		* change `kotlinVersion` from `1.1.61` to `1.2.10`
		* remove line `apply plugin: 'eclipse'`
* import project
	- TODO TODO TODO TODO TODO

## Stage 2

