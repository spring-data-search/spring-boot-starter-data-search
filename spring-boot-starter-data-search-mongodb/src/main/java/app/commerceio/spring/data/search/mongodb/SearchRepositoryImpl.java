package app.commerceio.spring.data.search.mongodb;

import app.commerceio.spring.data.search.Mapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.List;

public class SearchRepositoryImpl<T, I extends Serializable> extends SimpleMongoRepository<T, I> implements SearchRepository<T, I> {

    private final SearchBuilder searchBuilder;
    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T, I> entityInformation;

    public SearchRepositoryImpl(final MongoEntityInformation<T, I> entityInformation,
                                final MongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);

        this.entityInformation = entityInformation;
        this.mongoOperations = mongoOperations;
        this.searchBuilder = new SearchBuilder();
    }

    @Override
    public Page<T> findAll(final Query query, final Pageable pageable) {
        if (query == null) {
            return findAll(pageable);
        } else {
            long total = mongoOperations.count(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
            List<T> content = mongoOperations.find(query.with(pageable), entityInformation.getJavaType(), entityInformation.getCollectionName());

            return new PageImpl<>(content, pageable, total);
        }
    }

    @Override
    public Page<T> findAll(final Criteria criteria, final Pageable pageable) {
        Query query = criteria != null ? Query.query(criteria) : null;
        return findAll(query, pageable);
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
        Criteria criteria = searchBuilder.parse(search, mapper);
        return findAll(criteria, pageable);
    }
}
