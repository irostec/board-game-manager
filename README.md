# board-game-manager

Functionally, the idea of this project is to create a cloud native web application that will manage my board game preferences. For many years I've been a fan of board games, and as I've broadened my knowledge about them, I've found that there are just too many of them that, for one reason or another, become interesting to me or to my friends.

Technically, it's an attempt to use, in a single application, some software development techniques that I consider offer many benefits, but that I rarely (if ever) get the chance to use in usual production environments:

* [Clean architecture](https://reflectoring.io/book/), also known as hexagonal architecture, an approach to code structuring that promises easy to test the domain logic, easy to mock out infrastructure and
  technology, crystal-clear separation of domain code and technical code, and even migrating from
  one technology to another seems easy. For this, I tried two of the most popular functional libraries in the Java world: [Vavr](https://github.com/vavr-io/vavr) and [Atlassian Fugue](https://bitbucket.org/atlassian/fugue/src/master/).
* [Railway oriented programming](https://www.youtube.com/watch?v=fYo3LN9Vf_M), a functional approach to the execution of functions sequentially. It offers an alternative to the exception-based error handling that is the norm in Java.

The idea is to integrate them with several technologies that are popular in the Java world, like:

* [Spring Framework](https://spring.io/projects/spring-framework) with [Spring Boot](https://spring.io/projects/spring-boot) 3, the most popular framework for enterprise software development in Java. The main objective here is to see how feasible is its integration with the aforementioned clean architecture and railway oriented programming. [Spring Security](https://spring.io/projects/spring-security) is used to provide authentication and authorization using [JWT web tokens](https://jwt.io/), and [Spring Data JPA](https://spring.io/projects/spring-data-jpa) is used for SQL interactions.
* [LocalStack](https://www.localstack.cloud/), a drop-in replacement for AWS in dev and testing environments.
* [Apache Kafka](https://kafka.apache.org/) with [Apache Zookeeper](https://zookeeper.apache.org/), a distributed event streaming platform, for the user creation notifications, so that every time a user is added to the user management module it's also registered as a potential board game user.
* [PostgreSQL](https://www.postgresql.org/), one of the most popular and battle tested open source SQL databases, as a general datastore for the data related to the board games.
* [Liquibase](https://www.liquibase.com/) to evolve the database schema of the application
* [Splunk](https://www.splunk.com/) for logging.
* [JAXB](https://javaee.github.io/jaxb-v2/), providing API and tools that automate the mapping between XML documents and Java objects.
* [Retrofit](https://square.github.io/retrofit/), a type-safe HTTP client used to communicate with the external APIs that contain the board games' data.

The AWS services of interest are:

* [AWS Systems Manager Parameter Store](https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html), which is integrated with [Spring Cloud AWS](https://awspring.io/) to provide most of the configuration properties for the application.
* [Amazon DynamoDB](https://aws.amazon.com/dynamodb/), a fully managed, serverless, key-value NoSQL that is mainly used to manage the data of the users.
* [AWS CloudFormation](https://aws.amazon.com/cloudformation/) for the initial configuration of the other AWS services.  
* The idea of using [Amazon S3](https://aws.amazon.com/s3/) to store files related to the board games (like manuals, videos, strategy guides, etc.) is still a possibility for a future version.

This project will be abandoned for many reasons, chief among those:

* Eventually, I realized that the separation of the domain and technical code in separate packages make maintenance cumbersome and promotes the multiplication of data classes. Even more important, I suspect that continuous integration testing would be a much more productive approach.
* The annotation-based transactional management provided by Spring does not easily integrate with railway oriented programming, as its commit and rollback mechanism depends on unchecked exceptions.
* JPA's standard method of communication is through unchecked exceptions, and since you can't even control when the database communication occurs (unless you go around flushing every operation, which defeats the purpose of the ORM in the first place), you can't really manage those exceptions. 

As such, the development of this project will be halted, and will probably be retaken with other frameworks and technologies that integrate more seamlessly (e. g., a web framework that is purely programmatic instead of annotation-based, a SQL management tool that is not ORM-based, a testing strategy with heavy use of integration testing, etc.).

# How to run

1. Download and install [Docker](https://www.docker.com/) to be able to run the containers needed for the application.
1. Run ***docker compose up*** to download the required docker images and start the containers
1. Start the web application.
1. Interact with the application using an HTTP client. I use [Postman](https://www.postman.com/), with the environment configuration specified in the ***Board Game Manager.postman_environment.json*** file. The collection included in the ***Board Game Manager.postman_collection.json*** file provides examples of how to use the API.
