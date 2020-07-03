

# Cartrack App Challenge

- **The API URL is returning unknown coordinates. Once you open the map, it will all pointed to the ocean. Also, pagination is not possible base on the API url**

**Given API URL {[https://jsonplaceholder.typicode.com/users](https://jsonplaceholder.typicode.com/users)}**

In developing this application, the primary technologies and libraries are used:
 - **Kotlin** - The primary programming language used.
 - **MVVMi Architecture**  - You can read more about this architecture in this [article](https://medium.com/@thereallukesimpson/clean-architecture-with-mvvmi-architecture-components-rxjava-8c5093337b43)
 - **RxJava/RxKotlin/RxAndroid** - For implementing a reactive approach of the application
 - **Retrofit2 & OkHTTP3** - For HTTP calls and consuming web services
 - **Realm Database** - For local persistence and caching of data.
 - **HILT**  - For dependency injection
 - **Glide** - For fast image loading
 - **Timber** - For logging mechanism
 - **Repository Pattern** - For data management of Source of Truth with a local caching mechanism.
 - **Android Architecture Component** - LiveData, LifeCycle, ViewModel, DataBinding

## Features
 - **Log in**
 - **Display and Selection of Country**
 - **List of Users**
 - **Show users in Map**

## Account for Testing in Login
**Username: juancruz**
**Password: pass1234**

**Username: johndoe**
**Password: pass4321**

