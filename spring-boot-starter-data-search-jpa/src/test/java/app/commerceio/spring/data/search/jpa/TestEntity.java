package app.commerceio.spring.data.search.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_entity")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // String
    private String testString;
    private String testStringEmail;

    // Date & Time
    private LocalDate testLocalDate;
    private LocalTime testLocalTime;
    private OffsetTime testOffsetTime;
    private LocalDateTime testLocalDateTime;
    private OffsetDateTime testOffsetDateTime;

    // Number
    private boolean testBooleanPrimitive;
    private Boolean testBoolean;
    private double testDoublePrimitive;
    private Double testDouble;
    private int testIntegerPrimitive;
    private Integer testInteger;
    private float testFloatPrimitive;
    private Float testFloat;
    private long testLongPrimitive;
    private Long testLong;
    @Column(columnDefinition = "DOUBLE")
    private BigDecimal testBigDecimal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_sub_entity_id", referencedColumnName = "id")
    private TestOneToOneSubEntity testSubEntity;

    @OneToMany(mappedBy = "testEntity", cascade = CascadeType.ALL)
    private List<TestOneToManySubEntity> testSubEntities;
}
