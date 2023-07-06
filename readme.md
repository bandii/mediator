<p align="right">
    <img src=".github/badges/jacoco.svg" alt="Coverage"/>
    <img src=".github/badges/branches.svg" alt="Branches"/>
</p>

# Motivation
As a newby in the java world it was a surprise for me, I could not find a pure and simple solution for decoupling 
my modules in my projects. Therefore, as a learning project, 
I have started this [mediator](https://en.wikipedia.org/wiki/Mediator_pattern) implementation and shared it with You.

In case you find a bug or have some ideas how to improve the project, feel free opening pull requests, 
but please keep in mind, the goal of this project is to keep it simple and pure, without many third party dependencies.

For examples about how its commands and events work please check or debug the automated tests. I have used fakes everywhere, so it should be comfy :)

# Installation

Maven

```xml

<dependency>
    <groupId>io.github.bandii</groupId>
    <artifactId>mediator</artifactId>
    <version>1.0.3</version>
</dependency>
```

Gradle

```
dependencies {
    compile 'io.github.bandii:mediator:1.0.3'
}
```

# Mediator registration in Spring

```java

@Configuration
class MediatorConfiguration {

    @Bean
    Mediator pipeline(ObjectProvider<ICommandHandler> commandHandlers,
                      ObjectProvider<IEventHandler> notificationHandlers,
                      LoggingMiddleware loggingMiddleware,
                      ErrorHandlingMiddleware errorHandlingMiddleware) {
        var ret = new Mediator();
        
        // Register command handlers
        ret.commandHandler()
           .addHandlers(commandHandlers.stream()
                                       .collect(Collectors.toSet()));

        // Register notification handlers
        ret.eventHandler()
           .addHandlers(notificationHandlers.stream()
                                            .collect(Collectors.toSet()));

        // Order matters!
        ret.addMiddleware(loggingMiddleware);
        ret.addMiddleware(errorHandlingMiddleware);

        return ret;
    }
    
    @Bean
    LoggingMiddleware loggingMiddleware() {
        return new LoggingMiddleware();
    }
    
    @Bean
    ErrorHandlingMiddleware errorHandlingMiddleware() {
        return new ErrorHandlingMiddleware();
    }
}
```

## Notes
If you have any idea how to improve this lib further, do not hesitate opening a ticket :)