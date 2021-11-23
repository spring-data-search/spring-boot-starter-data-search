package app.commerceio.spring.data.search.jpa;

import app.commerceio.spring.data.search.Mapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class SearchRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements SearchRepository<T, ID> {

    private final SearchBuilder searchBuilder;

    public SearchRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation,
                                final EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.searchBuilder = new SearchBuilder();
    }

    @Override
    public Page<T> findAll(String search, Pageable pageable) {
        return findAll(search, pageable, Mapper.flatMapper().build());
    }

    @Override
    public Page<T> findAll(String search, Pageable pageable, Mapper mapper) {
        if (StringUtils.isBlank(search)) {
            return findAll(pageable);
        }
        Specification<T> criteria = searchBuilder.parse(search, mapper);
        return findAll(criteria, pageable);
    }
}
