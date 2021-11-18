### Data structure
```json
{
    "id": "UUID",
    "ref": "Long",
    "title": "String",
    "firstName": "String",
    "lastName": "String",
    "emailAddress": "String",
    "emailAddressVerified": "Boolean",
    "birthDate": "LocalDate",
    "addresses": [
        {
            "streetAddress": "String", 
            "postalCode": "String",
            "city": "String",
            "country": "String",
            "countryCode": "String"
        }
    ],
    "coins": "BigDecimal",
    "createdDate": "OffsetDateTime"
}
```
### Use cases

#### equal
* _search customers born the 1988-01-01_

  `birthDate: 1988-01-01`

* _search customers having address in Switzerland_

  `addresses.countryCode: CH`

* _search customers with a verified email address ending with gmail.com or starting with test_

  * _mongodb_

    `emailAddressVerified: true and (emailAddress : /.*gmail.com/ or emailAddress: /test.*/)`

  * _jpa_

    `emailAddressVerified: true and (emailAddress : *gmail.com or emailAddress: test*)`
  
* _search customers with the first name Adam, case-insensitive_

  * _mongodb_

    `firstName: /^Adam/i`

  * _jpa_

    `No supported`

#### not equal
* _search customers not born the 1988-01-01_

  `birthDate !: 1988-01-01`

* _search customers having address outside Switzerland_

  `addresses.countryCode !: CH`

* _search customers with unverified email address not ending with gmail.com and not starting with test_

  * _mongodb_

    `emailAddressVerified !: true and emailAddress !: /.*gmail.com/ and emailAddress !: /test.*/`

  * _jpa_

    `emailAddressVerified !: true and emailAddress !: *gmail.com and emailAddress !: test*)`

#### less than

#### less than or equal

#### greater than

#### greater than or equal

#### exists

#### doesn't exist
