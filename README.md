# REMARKS for me
- jeder braucht:
	* internet access!
	* java 8 sdk
	* intellij (free community edition is enough)
	??? gradle preinstalled ???


# TODO
- organize slack room (in kotlin.lang)

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

# Step-by-step

## Stage 1

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


## Stage 2

