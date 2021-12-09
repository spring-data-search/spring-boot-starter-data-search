package app.commerceio.spring.data.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperTest {

    @Test
    void flatMapper() {
        Mapper mapper = Mapper.flatMapper()
                .mapping("address.streetNumber", "addressEntity.streetNumber", new NumberToTextValueMapping())
                .mapping("address.street", "addressEntity.streetAddress")
                .mapping("name", "lastName")
                .mapping("address.postalCode.prefix", "addressEntity.zipCode.part1")
                .mapping("address.postalCode.suffix", "addressEntity.zipCode.part2")
                .build();

        Mapper.MappingEntry mappingEntry1 = mapper.map("address.streetNumber", "1");
        assertEquals("addressEntity.streetNumber", mappingEntry1.getKey());
        assertEquals("one", mappingEntry1.getValue());

        Mapper.MappingEntry mappingEntry2 = mapper.map("address.streetNumber", "2");
        assertEquals("addressEntity.streetNumber", mappingEntry2.getKey());
        assertEquals("two", mappingEntry2.getValue());

        Mapper.MappingEntry mappingEntry3 = mapper.map("address.streetNumber", "3");
        assertEquals("addressEntity.streetNumber", mappingEntry3.getKey());
        assertEquals("zero", mappingEntry3.getValue());

        assertEquals("addressEntity.streetAddress", mapper.map("address.street"));
        assertEquals("lastName", mapper.map("name"));
        assertEquals("addressEntity.zipCode.part1", mapper.map("address.postalCode.prefix"));
        assertEquals("addressEntity.zipCode.part2", mapper.map("address.postalCode.suffix"));
    }

    @Test
    void mapper() {
        Mapper postalCodeMapper = Mapper.flatMapper()
                .mapping("prefix", "part1", new NumberToTextValueMapping())
                .mapping("suffix", "part2", new LowerCaseValueMapping())
                .build();

        Mapper addressMapper = Mapper.mapper()
                .mapping("streetNumber", "streetNumber")
                .mapping("street", "streetAddress", new UpperCaseValueMapping())
                .mapping("postalCode", "zipCode", postalCodeMapper)
                .build();

        Mapper mapper = Mapper.mapper()
                .mapping("name", "lastName")
                .mapping("address", "addressEntity", addressMapper)
                .build();

        Mapper.MappingEntry mappingEntry1 = mapper.map("address.postalCode.prefix", "1");
        assertEquals("addressEntity.zipCode.part1", mappingEntry1.getKey());
        assertEquals("one", mappingEntry1.getValue());

        Mapper.MappingEntry mappingEntry2 = mapper.map("address.postalCode.suffix", "A");
        assertEquals("addressEntity.zipCode.part2", mappingEntry2.getKey());
        assertEquals("a", mappingEntry2.getValue());

        Mapper.MappingEntry mappingEntry3 = mapper.map("address.street", "a");
        assertEquals("addressEntity.streetAddress", mappingEntry3.getKey());
        assertEquals("A", mappingEntry3.getValue());

        assertEquals("addressEntity.streetAddress", mapper.map("address.street"));
        assertEquals("lastName", mapper.map("name"));
        assertEquals("addressEntity.zipCode.part1", mapper.map("address.postalCode.prefix"));
        assertEquals("addressEntity.zipCode.part2", mapper.map("address.postalCode.suffix"));
    }
}