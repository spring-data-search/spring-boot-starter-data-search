package io.commerce.spring.data.search.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SearchRepositoryImplTest {

    @Mock
    private MongoOperations mongoOperations;

    @Mock
    private MongoEntityInformation<?, ?> entityInformation;

    @InjectMocks
    private SearchRepositoryImpl<?, ?> searchRepository;

    @Test
    void findAllWithWrongSearch() {
        String search = "<";
        Criteria criteria = new SearchBuilder().parse(search);
        searchRepository.findAll(search, Pageable.unpaged());
    }

    @Test
    void findAllWithEmptySearch() {
        String search = "";
        Criteria criteria = new SearchBuilder().parse(search);
        searchRepository.findAll(search, Pageable.unpaged());
    }

    @Test
    void findAllWithNonEmptySearch() {
        String search = "(name:myName AND age>:33) OR (name:myName2,myName3 AND age:17,22,43) OR (name:/.*m.*/gimscxdtu AND age<20)";
        Criteria criteria = new SearchBuilder().parse(search);
        searchRepository.findAll(search, Pageable.unpaged());
        verify(mongoOperations).count(any(), any(), any());
        verify(mongoOperations).find(any(), any(), any());
    }
}