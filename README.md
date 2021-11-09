[![build](https://github.com/commerce-io/spring-boot-starter-data-search/actions/workflows/build.yml/badge.svg)](https://github.com/commerce-io/spring-boot-starter-data-search/actions/workflows/build.yml)
[![versionjava](https://img.shields.io/badge/jdk-11,_17-brightgreen.svg?logo=java)]()
[![license](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://github.com/commerce-io/spring-boot-starter-data-search/blob/main/LICENSE.txt)

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
</p>

## Table of contents

- [Made for complexe search](#made-for-complexe-search)
- [Simplify your search API](#simplify-your-search-api)
- [How it works](#how-it-works)
- [Getting Started](#getting-started)
  - [Use with mongodb](#use-with-mongodb)
    - [Demo](#demo)
    - [Installation](#installation)
      - [Maven](#maven)
      - [Gradle](#gradle)
    - [Usage](#usage)
  - [Use with jpa](#use-with-jpa)
  - [License](#license)

## Made for complex search
If you need a complex/ complete filter to access your data, based on multiple criteria, and you don't want to implement a dedicated query for each combination, then this is made for you.

## Simplify your search API
data-search provides a custom repository, to perform advanced search with Natural Language queries.

**Interface**
```java
Page<T> findAll(String search, Pageable pageable);
```

**Example**
```java
String search = "birthDate >: 1988-01-01 and (emailAddress : '/.*gmail.com/' or emailAddress: '/.*protonmail.com/') and emailAddressVerified: true and addresses.countryCode: FR,CH,CN";
Page<Customer> customers = customerRepository.findAll(search, Pageable.unpaged());
```

## How it works?
data-search uses [ANTLR](https://www.antlr.org/) to build and parse the search grammar.

**Operators**
| Operator | Description |Example|
| --- | --- | --- |
| : | equal | emailAddressVerified : true |
| : | in | countryCode : FR,CH,CN |
| !: | not equal | emailAddressVerified !: true |
| !: | not in | countryCode !: FR,CH,CN |
| < | less than | birthDate< 1988-01-01 |
| <: | less than or equal | birthDate <: 1988-01-01 |
| > | greater than | birthDate > 1988-01-01 |
| >: | greater than or equal | birthDate >: 1988-01-01 |
|  | exists (is not null) | birthDate |
| ! | doesn't exist (is null) | !birthDate |

**Supported values**
| Format | Description |Example|
| --- | --- | --- |
| String | Strings must be between " or ' if the valeu contains one of the operator or logical operators | firstName : Stan |
| Boolean | True or False | emailAddressVerified : true |
| Number | Any type of numeric value | ref >: 100 or coins > 6.76453 |
| Date | Date without time | birthDate >: 1988-01-01 |
| Datetime | Date with time and optional offset (UTC if not precised). Datetime values must be put between " or ' and the + url-encoded | createdDate >: 2021-08-23T18:58:24Z and createdDate <: 2021-10-12T18:58:24.000+02:00 |
| Array | Comma separated values (Comma must be escaped (\,) if it's aimed to be used as part of the value)  | countryCode : FR,CH,CN |
| RegEx | Regular expression, supported only for mongodb ([see documentation](https://docs.mongodb.com/manual/reference/operator/query/regex/)) | emailAddress : '/.*gmail.com/' |


## Getting Started
**Supports Java 11 or higher**

If Java 8 support is needed, please vote for [this issue](https://github.com/commerce-io/spring-boot-starter-data-search/issues/3)


### Use with mongodb

#### Demo
https://github.com/commerce-io/spring-boot-starter-data-search-demo

#### Installation
##### Maven

```xml
<dependency>
    <groupId>app.commerce-io</groupId>
    <artifactId>spring-boot-starter-data-search-mongodb</artifactId>
    <version>0.0.1</version>
</dependency>
```

##### Gradle

`implementation 'app.commerce-io:spring-boot-starter-data-search-mongodb:0.0.1'`

#### Usage

In order to use the provided repository, please add the following annotation to the main class or any other configuration class.

```java
@Configuration
@EnableMongoRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class DemoConfiguration {

}
```

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

### Use with jpa

jpa support will be released in the next version

## License

This software is released under the Apache license. See `LICENSE` for more information.

[license-shield]: https://img.shields.io/badge/License-Apache_2.0-blue.svg
[license-url]: https://github.com/commerce-io/spring-boot-starter-data-search/blob/main/LICENSE.txt