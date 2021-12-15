[![build](https://github.com/commerce-io/spring-boot-starter-data-search/actions/workflows/build.yml/badge.svg)](https://github.com/commerce-io/spring-boot-starter-data-search/actions/workflows/build.yml)
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->
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
    Spring Data Search API augmented with Natural Language support.
  </p>
  <p align="center">
    <label><i>Already supported:</i> </label><br>
    <img src="https://img.shields.io/badge/SpringBoot-_2.1.0_to_2.6.x-%236DB33F.svg?logo=spring-boot&logoColor=white">
    <img src="https://img.shields.io/badge/jdk-_11_to_17-%236DB33F.svg?logo=java&logoColor=white"><br/>
    <img src="https://img.shields.io/badge/MongoDB-%236DB330.svg?logo=mongodb&logoColor=white">
    <img src="https://img.shields.io/badge/Mysql-%236DB33F.svg?logo=mysql&logoColor=white">
    <img src="https://img.shields.io/badge/MariaDB-%236DB33F.svg?logo=mariadb&logoColor=white">
    <img src="https://img.shields.io/badge/Oracle-%236DB33F.svg?logo=oracle&logoColor=white">
    <img src="https://img.shields.io/badge/PostgreSQL-%236DB33F.svg?logo=postgresql&logoColor=white">
    <img src="https://img.shields.io/badge/DB2-%236DB33F.svg?logo=ibm&logoColor=white">
    <img src="https://img.shields.io/badge/MSSQL-%236DB33F.svg?logo=microsoft&logoColor=white">
  </p>
  <p align="center">
    Data Search is compatible with 
    <a href="https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/" target="_blank" rel="noreferrer noopener">
      Spring Native
    </a>
    (<a href="https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#support" target="_blank" rel="noreferrer noopener">see the list of supported drivers and limitations</a>)
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

# Table of Contents
- [Data Search](#data-search)
- [Features](#features)
  - [Comparison operators](#comparison-operators)
  - [Supported values](#supported-values)
- [Detailed use case](#detailed-use-case)
- [Getting Started](#getting-started)
  - [Requirements](#requirements)
  - [Demo](#demo)
    - [Mongodb demo](#mongodb-demo)
    - [JPA demo](#jpa-demo)
  - [Installation](#installation)
    - [Data Search Mongodb starter](#data-search-mongodb-starter)
    - [Data Search JPA Starter](#data-search-jpa-starter)
  - [Configuration](#configuration)
    - [Mongodb Repository](#mongodb-repository)
    - [JPA Repository](#jpa-repository)
  - [Usage](#usage)
  - [Field Mapping](#field-mapping)
    - [Flat Mapper](#flat-mapper)
    - [Advanced Mapper](#advanced-mapper)
  - [Value Mapping](#value-mapping)
- [Contributors](#Contributors)
- [License](#license)

# Data Search
Data Search provides an enterprise & production ready API, prowered by Spring Boot and Antlr, to query your data, and perform advanced search with Natural Language.

**Search example**: users born after 1988-01-01, with an gmail or protonmail email address, having completed the email verification, and having an address in one of the following countries: France, Switzerland or China

`birthDate >: '1988-01-01' and (emailAddress : *gmail.com or emailAddress: *protonmail.com) and emailAddressVerified: true and addresses.countryCode: 'FR,CH,CN'`

# Features
- Logical operators (or/ and)
- Parenthesis/ criteria prioritization
- Mongodb and all JPA compatible db engines
- Fields mapping/ DTO to Entities
- Filter by subentity fields/ deep search
- Advanced RegEx for Mongodb and like operator for JPA
- All comparison operators

## Comparison operators
#### Equal :
`emailAddressVerified : true`
#### Not equal !:
`emailAddressVerified !: true`
#### In :
`countryCode : 'FR,CH,CN'`
#### Not in :
`countryCode !: 'FR,CH,CN'`
#### Starts with :
`firstName: S*`
#### Ends with :
`firstName: *S`
#### Contains :
`firstName: *S*`
#### Less than <
`birthDate < '1988-01-01'`
#### Less than or equal <:
`birthDate <: '1988-01-01'`
#### Greater than >
`birthDate > '1988-01-01'`
#### Greater than or equal >:
`birthDate >: '1988-01-01'`
#### Exists (is not null)
`birthDate`
#### Doesn't exist (is null) !
`!birthDate`

## Supported values
#### String
`firstName : Stan`
#### Enum
:new: 1.3.0
`title : MR`
#### Boolean
`emailAddressVerified : true`
#### Number (Integer, Double, Long, BigDecimal)
`ref >: 100 or coins > 6.76453`
#### LocalTime
`time >: '18:58:24' and time <: '18:58:24.999'`
#### OffsetTime
`time >: '18:58:24Z' and time <: '20:58:24.999+02:00'`
#### LocalDate
`birthDate >: '1988-01-01'`
#### LocalDateTime
`createdDate >: '2021-08-23T18:58:24' and createdDate <: '2021-10-12T18:58:24.000'`
#### OffsetDateTime
`createdDate >: "2021-08-23T18:58:24Z" and createdDate <: '2021-10-12T18:58:24.000+02:00'`
#### Array
`countryCode : 'FR,CH,CN'`
#### RegEx
`emailAddress : '/.*gmail.com/'`
Regular expression, supported only for mongodb ([see documentation](https://docs.mongodb.com/manual/reference/operator/query/regex/)) |

# Detailed use case

See detailed search use case [here](./docs/use-cases.md)

# Getting Started

## Requirements

**Java version** 11 or higher (_If java 8 support is needed, please vote
for [this issue](https://github.com/commerce-io/spring-boot-starter-data-search/issues/3)_)

**SpringBoot version** 2.1.0 or higher
## Demo
### Mongodb demo
https://github.com/commerce-io/spring-boot-starter-data-search-mongodb-demo

### JPA demo
https://github.com/commerce-io/spring-boot-starter-data-search-jpa-demo

## Installation
:arrow_right: [Get the latest releases here](https://github.com/commerce-io/spring-boot-starter-data-search/releases) :arrow_left:
#### Data Search Mongodb starter
**Maven**
```xml
<dependency>
    <groupId>app.commerce-io</groupId>
    <artifactId>spring-boot-starter-data-search-mongodb</artifactId>
    <version>1.3.0</version>
</dependency>
```
**Gradle**
`implementation 'app.commerce-io:spring-boot-starter-data-search-mongodb:1.3.0'`

#### Data Search JPA Starter
**Maven**
```xml
<dependency>
    <groupId>app.commerce-io</groupId>
    <artifactId>spring-boot-starter-data-search-jpa</artifactId>
    <version>1.3.0</version>
</dependency>
```
**Gradle**
`implementation 'app.commerce-io:spring-boot-starter-data-search-jpa:1.3.0'`

## Configuration
Data Search provides a custom repository. In order to use the provided repository, add the following annotation to the main class or any other
configuration class.

### Mongodb Repository
```java
@Configuration
@EnableMongoRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class DemoConfiguration {
}
```
### JPA Repository
```java
@Configuration
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class DemoConfiguration {
}
```

## Usage
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

## Field Mapping
Users will tend to filter by the search result fields, and sometimes, your DTO structure differs from your entity/ collection.
Data search provides a mapper, to define your custom mappings rules in two different ways:

### Flat Mapper
The flat mapper is a basic String to String mapping, which could be useful for simple use cases.
```java
String search = "addressName: test";
Mapper addressMapper = Mapper.flatMapper()
            .mapping("addressName", "address.firstName")
            .build();

Page<CustomerEntity> page = customerRepository.findAll(search, pageable, addressMapper);
```
### Advanced Mapper
The advanced mapper is used for complex structure mapping, and enable the reuse of mappers. Advanced mappers can be combined with flat mappers.
```java
String search = "name: test OR address.addressName: test";
Mapper addressMapper = Mapper.flatMapper()
            .mapping("addressName", "firstName")
            .build();

Mapper mapper = Mapper.mapper()
                .mapping("name", "firstName")
                .mapping("address", "addressEntity", addressMapper)
                .build();

Page<CustomerEntity> page = customerRepository.findAll(search, pageable, mapper);
```

## Value Mapping
:new: 1.2.0

In addition to the fields mapping, some values could be converted before returning the search result.
Data Search supports values mapping:

```java
public class LowerCaseValueMapping implements ValueMapping {

  @Override
  public String map(String from) {
    return from == null ? null : from.toLowerCase();
  }
}
```

```java
String search = "addressName: TeSt";
Mapper addressMapper = Mapper.flatMapper()
        .mapping("addressName", "address.firstName", new LowerCaseValueMapping())
        .build();

Page<CustomerEntity> page = customerRepository.findAll(search, pageable, addressMapper);
```

# Contributors
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-2-blue.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

Thanks to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/adlmez"><img src="https://avatars.githubusercontent.com/u/28732941?v=4?s=100" width="100px;" alt=""/><br /><sub><b>@adlmez</b></sub></a><br /><a href="https://github.com/commerce-io/spring-boot-starter-data-search/commits?author=adlmez" title="Code">💻</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!

# License

This software is released under the Apache license. See `LICENSE` for more information.

[![license](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://github.com/commerce-io/spring-boot-starter-data-search/blob/main/LICENSE.txt)

[license-shield]: https://img.shields.io/badge/License-Apache_2.0-blue.svg

[license-url]: https://github.com/commerce-io/spring-boot-starter-data-search/blob/main/LICENSE.txt

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!