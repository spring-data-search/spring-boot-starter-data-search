[![build](https://github.com/commerce-io/spring-boot-starter-data-search/actions/workflows/build.yml/badge.svg)](https://github.com/commerce-io/spring-boot-starter-data-search/actions/workflows/build.yml)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=commerce-io_spring-boot-starter-data-search&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=commerce-io_spring-boot-starter-data-search)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=commerce-io_spring-boot-starter-data-search&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=commerce-io_spring-boot-starter-data-search)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=commerce-io_spring-boot-starter-data-search&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=commerce-io_spring-boot-starter-data-search)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=commerce-io_spring-boot-starter-data-search&metric=coverage)](https://sonarcloud.io/summary/new_code?id=commerce-io_spring-boot-starter-data-search)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=commerce-io_spring-boot-starter-data-search&metric=bugs)](https://sonarcloud.io/summary/new_code?id=commerce-io_spring-boot-starter-data-search)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=commerce-io_spring-boot-starter-data-search&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=commerce-io_spring-boot-starter-data-search)

<p align="center">
  <img src="https://raw.githubusercontent.com/commerce-io/spring-boot-starter-data-search/main/docs/spring-boot-starter-data-search-logo.png">
  <h3 align="center">spring-boot-starter-data-search</h3>
  <p align="center">
    Spring-Data search API augmented with Natural Language support.
  </p>
  <p align="center">
    <label><i>Already supported:</i> </label><br>
    <img src="https://img.shields.io/badge/SpringBoot-_2.1.0_and_higher-%236DB33F.svg?logo=spring-boot&logoColor=white">
    <img src="https://img.shields.io/badge/jdk-_11_and_higher-%236DB33F.svg?logo=java&logoColor=white"><br/>
    <img src="https://img.shields.io/badge/MongoDB-%236DB330.svg?logo=mongodb&logoColor=white">
    <img src="https://img.shields.io/badge/Mysql-%236DB33F.svg?logo=mysql&logoColor=white">
    <img src="https://img.shields.io/badge/MariaDB-%236DB33F.svg?logo=mariadb&logoColor=white">
    <img src="https://img.shields.io/badge/Oracle-%236DB33F.svg?logo=oracle&logoColor=white">
    <img src="https://img.shields.io/badge/PostgreSQL-%236DB33F.svg?logo=postgresql&logoColor=white">
    <img src="https://img.shields.io/badge/DB2-%236DB33F.svg?logo=ibm&logoColor=white">
    <img src="https://img.shields.io/badge/MSSQL-%236DB33F.svg?logo=microsoft&logoColor=white">
  </p>
  <p align="center">
    <label><i>Coming soon:</i> </label><br>
    <img src="https://img.shields.io/badge/SpringBoot-_2.0.x-B1361E.svg?logo=spring-boot&logoColor=white">
    <img src="https://img.shields.io/badge/jdk-_8-B1361E.svg?logo=java&logoColor=white">
  </p>
<p align="center">
    <label><i>Demo:</i> </label>
    <br/>
    <a href="https://github.com/commerce-io/spring-boot-starter-data-search-mongodb-demo" target="_blank" rel="noreferrer noopener">spring-boot-starter-data-search-mongodb-demo</a>
    <br/>
    <a href="https://github.com/commerce-io/spring-boot-starter-data-search-jpa-demo" target="_blank" rel="noreferrer noopener">spring-boot-starter-data-search-jpa-demo</a>
</p>

## Table of contents

- [Unleash your search API](#unleash-your-search-api)
- [How it works?](#how-it-works)
    - [Supported Operators](#supported-operators)
    - [Supported values](#supported-values)
    - [Use cases](#use-cases)
- [Getting Started](#getting-started)
    - [Requirements](#requirements)
    - [Use with mongodb](#use-with-mongodb)
    - [Use with jpa](#use-with-jpa)
    - [Usage](#usage)
- [License](#license)

## Unleash your search API

* If you need a complex/ complete filter to access your data, based on multiple criteria,
* If you don't want to implement a dedicated query for each combination,
* If you want your search API to be up-to-date and not impacted by your data structure changes,

Then this is made for you.

data-search provides a custom repository, to perform advanced search with Natural Language queries.

**Search example**

`birthDate >: '1988-01-01' and (emailAddress : '/.*gmail.com/' or emailAddress: '/.*protonmail.com/') and emailAddressVerified: true and addresses.countryCode: 'FR,CH,CN'`

## How it works?

Data-search uses [ANTLR](https://www.antlr.org/) to build the search grammar, parse the natural language query into a
criteria tree. The search grammar supports logical operators (and, or - case-insensitive), matching operators, and
parenthesis for priorities.

### Supported operators

| Operator | Example | Comment |
| --- | --- | --- |
| equal : | emailAddressVerified : true |  |
| in : | countryCode : 'FR,CH,CN' | |
| not equal !: | emailAddressVerified !: true | |
| not in !: | countryCode !: 'FR,CH,CN' | |
| less than < | birthDate < 1988-01-01 | |
| less than or equal <: | birthDate <: '1988-01-01' | |
| greater than > | birthDate > '1988-01-01' | |
| greater than or equal >: | birthDate >: '1988-01-01' | |
| exists (is not null) | birthDate | |
| doesn't exist (is null) ! | !birthDate | |
| starts with : | firstName: *S | only for jpa, for mongodb, use RegEx instead |
| ends with with : | firstName: S* | only for jpa, for mongodb, use RegEx instead |
| contains : | firstName: \*S* | only for jpa, for mongodb, use RegEx instead |

### Supported values

| Format | Example | Comment |
| --- | --- | --- |
| String | firstName : Stan | |
| Boolean | emailAddressVerified : true | |
| Number | ref >: 100 or coins > 6.76453 | Integer, Double, Long, BigDecimal |
| LocalTime | time >: '18:58:24' and time <: '18:58:24.999' | |
| OffsetTime | time >: '18:58:24Z' and time <: '20:58:24.999+02:00' | |
| LocalDate | birthDate >: '1988-01-01' | |
| LocalDateTime | createdDate >: '2021-08-23T18:58:24' and createdDate <: "2021-10-12T18:58:24.000" | must be put between " or ' and url-encoded |
| OffsetDateTime | createdDate >: "2021-08-23T18:58:24Z" and createdDate <: '2021-10-12T18:58:24.000+02:00' | must be put between " or ' and url-encoded |
| Array | countryCode : FR,CH,CN | Comma separated values (Comma must be escaped (\,) if it's aimed to be used as part of the value)  |
| RegEx | emailAddress : '/.*gmail.com/' | Regular expression, supported only for mongodb ([see documentation](https://docs.mongodb.com/manual/reference/operator/query/regex/)) |

### Use cases

See detailed search use cases [here](./docs/use-cases.md)

## Getting Started

### Requirements

**Java version** 11 or higher (_If java 8 support is needed, please vote
for [this issue](https://github.com/commerce-io/spring-boot-starter-data-search/issues/3)_)

**SpringBoot version** 2.1.0 or higher

### Use with mongodb

#### Demo

https://github.com/commerce-io/spring-boot-starter-data-search-mongodb-demo

#### Installation

##### Maven

```xml

<dependency>
    <groupId>app.commerce-io</groupId>
    <artifactId>spring-boot-starter-data-search-mongodb</artifactId>
    <version>1.0.0</version>
</dependency>
```

##### Gradle

`implementation 'app.commerce-io:spring-boot-starter-data-search-mongodb:1.0.0'`

#### Configuration

In order to use the provided repository, please add the following annotation to the main class or any other
configuration class.

```java

@Configuration
@EnableMongoRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class DemoConfiguration {

}
```

### Use with jpa

#### Demo

https://github.com/commerce-io/spring-boot-starter-data-search-jpa-demo

#### Installation

##### Maven

```xml

<dependency>
    <groupId>app.commerce-io</groupId>
    <artifactId>spring-boot-starter-data-search-jpa</artifactId>
    <version>1.1.0-RC1</version>
</dependency>
```

##### Gradle

`implementation 'app.commerce-io:spring-boot-starter-data-search-jpa:1.1.0-RC1'`

#### Configuration

In order to use the provided repository, please add the following annotation to the main class or any other
configuration class.

```java

@Configuration
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class DemoConfiguration {

}
```

### Usage

Make your repositories extend `SearchRepository`

```java

@Repository
public interface CustomerRepository extends SearchRepository<Customer, String> {
}
```

And use in a controller or from anywhere else

```java

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/customers",
            produces = {"application/json"}
    )
    public ResponseEntity<Page<Customer>> searchCustomers(
            @RequestParam(value = "search", required = false) String search,
            Pageable pageable) {

        Page<Customer> customerPage = customerRepository.findAll(
                search,
                pageable);

        return ok(customerPage);
    }
}
```

## License

This software is released under the Apache license. See `LICENSE` for more information.

[![license](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://github.com/commerce-io/spring-boot-starter-data-search/blob/main/LICENSE.txt)


[license-shield]: https://img.shields.io/badge/License-Apache_2.0-blue.svg

[license-url]: https://github.com/commerce-io/spring-boot-starter-data-search/blob/main/LICENSE.txt
