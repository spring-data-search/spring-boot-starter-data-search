### Data structure
```json
{
    "id": "UUID",
    "ref": Long,
    "title": "String",
    "firstName": "String",
    "lastName": "String",
    "emailAddress": "String",
    "emailAddressVerified": Boolean,
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
    "coins": BigDecimal,
    "createdDate": "OffsetDateTime"
}
```
### Use cases

#### equal
* _search customers born the 1988-01-01_

  `birthDate: 1988-01-01`

* _search customers having address in Switzerland_

  `addresses.countryCode: CH`

* _search customers with a verified email address ending with gmail.com or protonmail.com_

  `emailAddressVerified: true and (emailAddress : /.*gmail.com/ or emailAddress: /.*protonmail.com/)`

* _search customers with the first name Adam, case-insensitive_

  `firstName: /^Adam/i`

#### not equal

#### less than

#### less than or equal

#### greater than

#### greater than or equal

#### exists

#### doesn't exist
