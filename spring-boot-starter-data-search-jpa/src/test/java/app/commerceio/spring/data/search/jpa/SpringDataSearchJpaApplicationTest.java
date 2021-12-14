package app.commerceio.spring.data.search.jpa;

import app.commerceio.spring.data.search.Mapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = SpringDataSearchJpaApplication.class)
class SpringDataSearchJpaApplicationTest {

    @Autowired
    private TestEntityRepository testEntityRepository;

    @Test
    void search_Empty() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // EQ

    @Test
    void search_eqEnum() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testEnum: VALUE1", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqString() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "evita.emmerich@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString: Mr.", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqDoublePrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDoublePrimitive: 0.756", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqDouble() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDouble: 0.756", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }


    @Test
    void search_eqBigDecimal() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBigDecimal: 0.756", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqIntegerPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testIntegerPrimitive: 0", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqInteger() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testInteger: 0", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_eqFloatPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloatPrimitive: 0.756", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_eqFloat() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloat: 0.756", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqLongPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLongPrimitive: 0", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqLong() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLong: 0", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqBooleanPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "cherie.bins@gmail.com",
                "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBooleanPrimitive: true", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqBoolean() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "cherie.bins@gmail.com",
                "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBoolean: true", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqLocalDate() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDate: '1977-06-05'", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqLocalTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalTime: '09:00:00.000'", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqOffsetTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetTime: '09:00:00.000Z'", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqLocalDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDateTime: '1977-06-05T09:00:00.000'", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_eqOffsetDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetDateTime: '1977-06-05T09:00:00.000Z'", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // LIKE

    @Test
    void search_startsWith() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "evita.emmerich@yahoo.com", "cherie.bins@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString: Mr*", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_endsWith() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString: Mr* and testStringEmail: *@hotmail.com", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_contains() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString: Mr* and testStringEmail: *@hotmail*", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // NOT LIKE

    @Test
    void search_dontStartWith() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "ruben.dubuque@yahoo.com", "sherrell.goyette@yahoo.com",
                "karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString !: Mr*", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_dontEndWith() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString !: Mr* and testStringEmail !: *@gmail.com", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_dontContain() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString !: Mr* and testStringEmail !: *@gmail*", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // NE

    @Test
    void search_neEnum() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testEnum !: VALUE2", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neString() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString !: Mr.", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neDoublePrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDoublePrimitive !: 0.756", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neDouble() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDouble !: 0.756", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neBigDecimal() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBigDecimal !: 0.756", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neIntegerPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testIntegerPrimitive !: 0", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neInteger() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testInteger !: 0", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_neFloatPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloatPrimitive !: 0.756", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_neFloat() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloat !: 0.756", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neLongPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLongPrimitive !: 0", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neLong() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLong !: 0", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neBooleanPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com", "luna.feeney@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBooleanPrimitive !: true", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neBoolean() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com", "luna.feeney@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBoolean !: true", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neLocalDate() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDate !: '1977-06-05'", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neLocalTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalTime !: '09:00:00.000'", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neOffsetTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetTime !: '09:00:00.000Z'", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neLocalDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDateTime !: '1977-06-05T09:00:00.000'", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_neOffsetDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetDateTime !: '1977-06-05T09:00:00.000Z'", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // IN

    @Test
    void search_inEnum() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testEnum: 'VALUE1,VALUE2,VALUE3'", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inString() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "evita.emmerich@yahoo.com", "ruben.dubuque@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString: Mr.,Ms.", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inDoublePrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDoublePrimitive: 0.756,4.857", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inDouble() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDouble: 0.756,4.857", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }


    @Test
    void search_inBigDecimal() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBigDecimal: 0.756,4.857", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inIntegerPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com",
                "leon.goyette@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testIntegerPrimitive: 0,4", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inInteger() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com",
                "leon.goyette@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testInteger: 0,4", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_inFloatPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloatPrimitive: 0.756,4.857", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_inFloat() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloat: 0.756,4.857", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inLongPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com",
                "leon.goyette@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLongPrimitive: 0,4", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inLong() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com",
                "leon.goyette@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLong: 0,4", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inBooleanPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBooleanPrimitive: true,false", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inBoolean() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBoolean: true,false", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inLocalDate() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDate: '1977-06-05,1985-08-31'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inLocalTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalTime: '09:00:00.000,10:00:10'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inOffsetTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetTime: '09:00:00.000Z,10:00:10Z'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inLocalDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDateTime: '1977-06-05T09:00:00.000,1985-08-31T10:00:10'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_inOffsetDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetDateTime: '1977-06-05T09:00:00.000Z,1985-08-31T10:00:10Z'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // NIN

    @Test
    void search_ninEnum() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testEnum !: 'VALUE2,VALUE3'", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninString() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "evita.emmerich@yahoo.com", "ruben.dubuque@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString !: Mr.,Ms.", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninDoublePrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDoublePrimitive !: 0.756,4.857", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninDouble() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDouble !: 0.756,4.857", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }


    @Test
    void search_ninBigDecimal() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBigDecimal !: 0.756,4.857", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninIntegerPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com",
                "leon.goyette@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testIntegerPrimitive !: 0,4", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninInteger() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com",
                "leon.goyette@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testInteger !: 0,4", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_ninFloatPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloatPrimitive !: 0.756,4.857", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_ninFloat() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloat !: 0.756,4.857", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninLongPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com",
                "leon.goyette@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLongPrimitive !: 0,4", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninLong() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com",
                "leon.goyette@yahoo.com", "sherrell.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLong !: 0,4", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninBooleanPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBooleanPrimitive !: true,false", Pageable.unpaged());
        assertEquals(0, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninBoolean() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBoolean !: true,false", Pageable.unpaged());
        assertEquals(0, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninLocalDate() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDate !: '1977-06-05,1985-08-31'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninLocalTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalTime !: '09:00:00.000,10:00:10'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninOffsetTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetTime !: '09:00:00.000Z,10:00:10Z'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninLocalDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDateTime !: '1977-06-05T09:00:00.000,1985-08-31T10:00:10'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ninOffsetDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetDateTime !: '1977-06-05T09:00:00.000Z,1985-08-31T10:00:10Z'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .noneMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // GT

    @Test
    void search_gtEnum() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testEnum > VALUE0", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtString() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("cherie.bins@gmail.com", "ruben.dubuque@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString > Mr.", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtDoublePrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDoublePrimitive > 0.756", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtDouble() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDouble > 0.756", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtBigDecimal() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBigDecimal > 0.756", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtIntegerPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testIntegerPrimitive > 0", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtInteger() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testInteger > 0", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_gtFloatPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloatPrimitive > 0.756", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_gtFloat() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloat > 0.756", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtLongPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLongPrimitive > 0", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtLong() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLong > 0", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtBooleanPrimitive() {
        initLargeTestData();


        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBooleanPrimitive > true", Pageable.unpaged());
        assertEquals(0, testEntityPage.getTotalElements());
    }

    @Test
    void search_gtBoolean() {
        initLargeTestData();


        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBoolean > true", Pageable.unpaged());
        assertEquals(0, testEntityPage.getTotalElements());
    }

    @Test
    void search_gtLocalDate() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "cherie.bins@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDate > '1977-06-05'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtLocalTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "ruben.dubuque@yahoo.com", "sherrell.goyette@yahoo.com",
                "leon.goyette@yahoo.com", "evita.emmerich@yahoo.com", "cherie.bins@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalTime > '09:00:00.000'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtOffsetTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "ruben.dubuque@yahoo.com", "sherrell.goyette@yahoo.com",
                "leon.goyette@yahoo.com", "evita.emmerich@yahoo.com", "cherie.bins@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetTime > '09:00:00.000Z'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtLocalDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "cherie.bins@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDateTime > '1977-06-05T09:00:00.000'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_gtOffsetDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "cherie.bins@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetDateTime > '1977-06-05T09:00:00.000Z'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // GE

    @Test
    void search_geEnum() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testEnum >: VALUE1", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geString() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("cherie.bins@gmail.com", "ruben.dubuque@yahoo.com", "evita.emmerich@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString >: Mr.", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geDoublePrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDoublePrimitive >: 0.756", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geDouble() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDouble >: 0.756", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geBigDecimal() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBigDecimal >: 0.756", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geIntegerPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testIntegerPrimitive >: 0", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geInteger() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testInteger >: 0", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_geFloatPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloatPrimitive >: 0.756", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_geFloat() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "luna.feeney@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloat >: 0.756", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geLongPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLongPrimitive >: 0", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geLong() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLong >: 0", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geBooleanPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "cherie.bins@gmail.com",
                "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBooleanPrimitive >: true", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));

    }

    @Test
    void search_geBoolean() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "cherie.bins@gmail.com",
                "evita.emmerich@yahoo.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBoolean >: true", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geLocalDate() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "cherie.bins@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDate >: '1977-06-05'", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geLocalTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "ruben.dubuque@yahoo.com", "sherrell.goyette@yahoo.com",
                "leon.goyette@yahoo.com", "evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalTime >: '09:00:00.000'", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geOffsetTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "ruben.dubuque@yahoo.com", "sherrell.goyette@yahoo.com",
                "leon.goyette@yahoo.com", "evita.emmerich@yahoo.com", "cherie.bins@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetTime >: '09:00:00.000Z'", Pageable.unpaged());
        assertEquals(7, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geLocalDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "cherie.bins@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDateTime >: '1977-06-05T09:00:00.000'", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_geOffsetDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "cherie.bins@gmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetDateTime >: '1977-06-05T09:00:00.000Z'", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // LT

    @Test
    void search_ltEnum() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testEnum < VALUE2", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltString() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "sherrell.goyette@yahoo.com", "karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString < Mr.", Pageable.unpaged());
        assertEquals(4, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltDoublePrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDoublePrimitive < 0.756", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltDouble() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDouble < 0.756", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltBigDecimal() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBigDecimal < 0.756", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltIntegerPrimitive() {
        initLargeTestData();


        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testIntegerPrimitive < 0", Pageable.unpaged());
        assertEquals(0, testEntityPage.getTotalElements());
    }

    @Test
    void search_ltInteger() {
        initLargeTestData();


        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testInteger < 0", Pageable.unpaged());
        assertEquals(0, testEntityPage.getTotalElements());
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_ltFloatPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloatPrimitive < 0.756", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_ltFloat() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloat < 0.756", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltLongPrimitive() {
        initLargeTestData();


        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLongPrimitive < 0", Pageable.unpaged());
        assertEquals(0, testEntityPage.getTotalElements());
    }

    @Test
    void search_ltLong() {
        initLargeTestData();


        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLong < 0", Pageable.unpaged());
        assertEquals(0, testEntityPage.getTotalElements());
    }

    @Test
    void search_ltBooleanPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBooleanPrimitive < true", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));

    }

    @Test
    void search_ltBoolean() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBoolean < true", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltLocalDate() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "evita.emmerich@yahoo.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDate < '1977-06-05'", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltLocalTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalTime < '09:00:00.000'", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltOffsetTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetTime < '09:00:00.000Z'", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltLocalDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "evita.emmerich@yahoo.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDateTime < '1977-06-05T09:00:00.000'", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_ltOffsetDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "evita.emmerich@yahoo.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetDateTime < '1977-06-05T09:00:00.000Z'", Pageable.unpaged());
        assertEquals(5, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // LE

    @Test
    void search_leEnum() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testEnum <: VALUE1", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leString() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "sherrell.goyette@yahoo.com",
                "karisa.stracke@gmail.com", "leon.goyette@yahoo.com", "evita.emmerich@yahoo.com", "lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString <: Mr.", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leDoublePrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDoublePrimitive <: 0.756", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leDouble() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testDouble <: 0.756", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leBigDecimal() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBigDecimal <: 0.756", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leIntegerPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testIntegerPrimitive <: 0", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leInteger() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testInteger <: 0", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_leFloatPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloatPrimitive <: 0.756", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    @Disabled(value = "Float type is not recommended since it's approximate by nature, use BigDecimal instead")
    void search_leFloat() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testFloat <: 0.756", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leLongPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLongPrimitive <: 0", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leLong() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("leon.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLong <: 0", Pageable.unpaged());
        assertEquals(3, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leBooleanPrimitive() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBooleanPrimitive <: true", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));

    }

    @Test
    void search_leBoolean() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "ruben.dubuque@yahoo.com", "luna.feeney@gmail.com",
                "cherie.bins@gmail.com", "evita.emmerich@yahoo.com", "karisa.stracke@gmail.com", "sherrell.goyette@yahoo.com", "leon.goyette@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testBoolean <: true", Pageable.unpaged());
        assertEquals(8, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leLocalDate() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "evita.emmerich@yahoo.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDate <: '1977-06-05'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leLocalTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalTime <: '09:00:00.000'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leOffsetTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetTime <: '09:00:00.000Z'", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leLocalDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "evita.emmerich@yahoo.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testLocalDateTime <: '1977-06-05T09:00:00.000'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_leOffsetDateTime() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("luna.feeney@gmail.com", "evita.emmerich@yahoo.com", "ruben.dubuque@yahoo.com",
                "sherrell.goyette@yahoo.com", "lanette.okuneva@hotmail.com", "karisa.stracke@gmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testOffsetDateTime <: '1977-06-05T09:00:00.000Z'", Pageable.unpaged());
        assertEquals(6, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // NULL

    @Test
    void search_null() {
        initLightTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("!testString", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // NOT NULL

    @Test
    void search_notNull() {
        initLightTestData();

        List<String> expectedTestStringEmail = List.of("ruben.dubuque@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testString", Pageable.unpaged());
        assertEquals(1, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // SUB ENTITY

    @Test
    void search_oneToOneSubEntity() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "evita.emmerich@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testSubEntity.testString: Mr.", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    @Test
    void search_oneToManySubSubEntity() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "evita.emmerich@yahoo.com");

        Page<TestEntity> testEntityPage = testEntityRepository.findAll("testSubEntities.testSubSubEntities.testString: Mr.", Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // COMPLEX SEARCH

    @Test
    void search_complexSearch() {
        initLargeTestData();

        List<String> expectedTestStringEmail = List.of("karisa.stracke@gmail.com", "leon.goyette@yahoo.com");

        String search = "(testString: Miss and (testBoolean: true or testDouble <1)) and testLocalDate >: '1977-06-05'";
        Page<TestEntity> testEntityPage = testEntityRepository.findAll(search, Pageable.unpaged());
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // SEARCH MAPPING
    @Test
    void search_searchMapping() {
        initLargeTestData();

        Mapper subSubEntityMapper = Mapper.flatMapper()
                .mapping("contactTitle", "testString")
                .mapping("contactEmail", "testStringEmail")
                .build();
        Mapper subEntityMapper = Mapper.mapper()
                .mapping("addressTitle", "testString")
                .mapping("addressEmail", "testStringEmail")
                .mapping("contacts", "testSubSubEntities", subSubEntityMapper)
                .build();
        Mapper mapper = Mapper.mapper()
                .mapping("title", "testString")
                .mapping("email", "testStringEmail")
                .mapping("addresses", "testSubEntities", subEntityMapper)
                .build();

        List<String> expectedTestStringEmail = List.of("lanette.okuneva@hotmail.com", "evita.emmerich@yahoo.com");

        String search = "addresses.contacts.contactTitle: Mr.";

        Page<TestEntity> testEntityPage = testEntityRepository.findAll(search, Pageable.unpaged(), mapper);
        assertEquals(2, testEntityPage.getTotalElements());
        assertTrue(testEntityPage.getContent().stream()
                .allMatch(testEntity -> expectedTestStringEmail.contains(testEntity.getTestStringEmail())));
    }

    // INIT DATA

    private List<TestEntity> initLightTestData() {
        TestEntity testEntity1 = getTestEntity(null, "lanette.okuneva@hotmail.com",
                1977, 2, 25, 0, 0, 0, 0,
                true, 0.123, 0, 0L, 0.123F,
                "Mr.", "lanette.okuneva@hotmail.com");
        TestEntity testEntity2 = getTestEntity("Ms.", "ruben.dubuque@yahoo.com",
                1954, 10, 12, 11, 1, 0, 0,
                false, 2.765, 2, 2L, 2.765F,
                "Ms.", "ruben.dubuque@yahoo.com");
        return List.of(testEntity1, testEntity2);
    }

    private List<TestEntity> initLargeTestData() {
        TestEntity testEntity1 = getTestEntity("Mr.", "lanette.okuneva@hotmail.com",
                1977, 2, 25, 0, 0, 0, 0,
                true, 0.123, 0, 0L, 0.123F,
                "Mr.", "lanette.okuneva@hotmail.com");
        TestEntity testEntity2 = getTestEntity("Ms.", "ruben.dubuque@yahoo.com",
                1954, 10, 12, 11, 1, 0, 0,
                false, 2.765, 2, 2L, 2.765F,
                "Ms.", "ruben.dubuque@yahoo.com");
        TestEntity testEntity3 = getTestEntity("Miss", "luna.feeney@gmail.com",
                1940, 11, 11, 10, 0, 45, 0,
                false, 7, 7, 7L, 7F,
                "Miss", "luna.feeney@gmail.com");
        TestEntity testEntity4 = getTestEntity("Mrs.", "cherie.bins@gmail.com",
                1993, 2, 11, 10, 0, 0, 0,
                true, 1.987, 1, 1L, 1.987F,
                "Mrs.", "cherie.bins@gmail.com");
        TestEntity testEntity5 = getTestEntity("Mr.", "evita.emmerich@yahoo.com",
                1942, 1, 21, 12, 0, 0, 0,
                true, 1, 1, 1L, 1F,
                "Mr.", "evita.emmerich@yahoo.com");
        TestEntity testEntity6 = getTestEntity("Miss", "karisa.stracke@gmail.com",
                1977, 6, 5, 9, 0, 0, 0,
                false, 0.756, 0, 0L, 0.756F,
                "Miss", "karisa.stracke@gmail.com");
        TestEntity testEntity7 = getTestEntity("Miss", "sherrell.goyette@yahoo.com",
                1969, 9, 24, 10, 10, 0, 0,
                true, 4.857, 4, 4L, 4.857F,
                "Miss", "sherrell.goyette@yahoo.com");
        TestEntity testEntity8 = getTestEntity("Miss", "leon.goyette@yahoo.com",
                1985, 8, 31, 10, 0, 10, 0,
                true, 0.011, 0, 0L, 0.011F,
                "Miss", "leon.goyette@yahoo.com");
        return List.of(testEntity1, testEntity2, testEntity3, testEntity4, testEntity5, testEntity6, testEntity7, testEntity8);
    }

    private TestEntity getTestEntity(String testString, String testStringEmail, int year, int month, int dayOfMonth,
                                     int hour, int minute, int second, int nanoOfSecond, boolean testBoolean,
                                     double testDouble, int testInteger, long testLong, float testFloat,
                                     String testSubString, String testSubStringEmail) {
        TestEntity testEntity = TestEntity.builder()
                .testEnum(TestEnum.VALUE1)
                .testString(testString)
                .testStringEmail(testStringEmail)
                .testLocalDate(LocalDate.of(year, month, dayOfMonth))
                .testLocalTime(LocalTime.of(hour, minute, second, nanoOfSecond))
                .testOffsetTime(OffsetTime.of(hour, minute, second, nanoOfSecond, ZoneOffset.UTC))
                .testLocalDateTime(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond))
                .testOffsetDateTime(OffsetDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, ZoneOffset.UTC))
                .testBooleanPrimitive(testBoolean)
                .testBoolean(testBoolean)
                .testDoublePrimitive(testDouble)
                .testDouble(testDouble)
                .testIntegerPrimitive(testInteger)
                .testInteger(testInteger)
                .testFloatPrimitive(testFloat)
                .testFloat(testFloat)
                .testLongPrimitive(testLong)
                .testLong(testLong)
                .testBigDecimal(BigDecimal.valueOf(testDouble))
                .build();

        TestOneToOneSubEntity testOneToOneSubEntity = TestOneToOneSubEntity.builder()
                .testEntity(testEntity)
                .testString(testSubString)
                .testStringEmail(testSubStringEmail)
                .build();
        testEntity.setTestSubEntity(testOneToOneSubEntity);

        TestOneToManySubEntity testOneToManySubEntity = TestOneToManySubEntity.builder()
                .testEntity(testEntity)
                .testString(testSubString)
                .testStringEmail(testSubStringEmail)
                .build();

        TestOneToManySubSubEntity testOneToManySubSubEntity = TestOneToManySubSubEntity.builder()
                .testSubEntity(testOneToManySubEntity)
                .testString(testSubString)
                .testStringEmail(testSubStringEmail)
                .build();

        testOneToManySubEntity.setTestSubSubEntities(List.of(testOneToManySubSubEntity));

        testEntity.setTestSubEntities(List.of(testOneToManySubEntity));
        return testEntityRepository.save(testEntity);
    }
}