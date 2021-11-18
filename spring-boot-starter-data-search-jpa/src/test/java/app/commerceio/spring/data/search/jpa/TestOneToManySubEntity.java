package app.commerceio.spring.data.search.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_sub_entity")
public class TestOneToManySubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "test_entity_id", referencedColumnName = "id")
    private TestEntity testEntity;

    @OneToMany(mappedBy = "testSubEntity", cascade = CascadeType.ALL)
    private List<TestOneToManySubSubEntity> testSubSubEntities;

    // String
    private String testString;
    private String testStringEmail;
}

