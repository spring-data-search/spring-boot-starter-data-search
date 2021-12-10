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

        Mapper.MappingEntry mappingEntry1 = mapper.mappingEntry("address.streetNumber");
        assertEquals("addressEntity.streetNumber", mappingEntry1.getKey());
        assertEquals("one,two,one,zero,two", mappingEntry1.value("1,2,1,3,2"));

        Mapper.MappingEntry mappingEntry2 = mapper.mappingEntry("address.streetNumber");
        assertEquals("addressEntity.streetNumber", mappingEntry2.getKey());
        assertEquals("two", mappingEntry2.value("2"));

        Mapper.MappingEntry mappingEntry3 = mapper.mappingEntry("address.streetNumber");
        assertEquals("addressEntity.streetNumber", mappingEntry3.getKey());
        assertEquals("zero", mappingEntry3.value("3"));

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

        Mapper.MappingEntry mappingEntry1 = mapper.mappingEntry("address.postalCode.prefix");
        assertEquals("addressEntity.zipCode.part1", mappingEntry1.getKey());
        assertEquals("one", mappingEntry1.value("1"));

        Mapper.MappingEntry mappingEntry2 = mapper.mappingEntry("address.postalCode.suffix");
        assertEquals("addressEntity.zipCode.part2", mappingEntry2.getKey());
        assertEquals("a", mappingEntry2.value("A"));

        Mapper.MappingEntry mappingEntry3 = mapper.mappingEntry("address.street");
        assertEquals("addressEntity.streetAddress", mappingEntry3.getKey());
        assertEquals("A", mappingEntry3.value("a"));

        assertEquals("addressEntity.streetAddress", mapper.map("address.street"));
        assertEquals("lastName", mapper.map("name"));
        assertEquals("addressEntity.zipCode.part1", mapper.map("address.postalCode.prefix"));
        assertEquals("addressEntity.zipCode.part2", mapper.map("address.postalCode.suffix"));
    }
}