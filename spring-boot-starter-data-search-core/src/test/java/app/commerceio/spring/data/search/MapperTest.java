package app.commerceio.spring.data.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    @Test
    void flatMapper() {
        Mapper mapper = Mapper.flatMapper()
                .mapping("address.street", "addressEntity.streetAddress")
                .mapping("name", "lastName")
                .mapping("address.postalCode.prefix", "addressEntity.zipCode.part1")
                .mapping("address.postalCode.suffix", "addressEntity.zipCode.part2")
                .build();

        assertEquals("addressEntity.streetAddress", mapper.map("address.street"));
        assertEquals("lastName", mapper.map("name"));
        assertEquals("addressEntity.zipCode.part1", mapper.map("address.postalCode.prefix"));
        assertEquals("addressEntity.zipCode.part2", mapper.map("address.postalCode.suffix"));
    }

    @Test
    void mapper() {
        Mapper postalCodeMapper = Mapper.flatMapper()
                .mapping("prefix", "part1")
                .mapping("suffix", "part2")
                .build();

        Mapper addressMapper = Mapper.mapper()
                .mapping("street", "streetAddress")
                .mapping("postalCode", "zipCode", postalCodeMapper)
                .build();

        Mapper mapper = Mapper.mapper()
                .mapping("name", "lastName")
                .mapping("address", "addressEntity", addressMapper)
                .build();

        assertEquals("addressEntity.streetAddress", mapper.map("address.street"));
        assertEquals("lastName", mapper.map("name"));
        assertEquals("addressEntity.zipCode.part1", mapper.map("address.postalCode.prefix"));
        assertEquals("addressEntity.zipCode.part2", mapper.map("address.postalCode.suffix"));
    }
}