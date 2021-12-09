package app.commerceio.spring.data.search;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Mapper {

    List<Mapper.Mapping> getMappings();

    String map(String from);

    MappingEntry map(String key, String value);

    default Pageable map(Pageable from) {
        if (getMappings() == null || getMappings().isEmpty()) {
            return from;
        }
        Sort sort = Sort.by(from.getSort().stream()
                .map(order -> new Sort.Order(order.getDirection(), map(order.getProperty())))
                .collect(Collectors.toList()));
        return PageRequest.of(from.getPageNumber(), from.getPageSize(), sort);
    }

    default Optional<Mapping> mapping(String from) {
        return getMappings().stream()
                .filter(mapping -> from.equalsIgnoreCase(mapping.getFrom()))
                .findFirst();
    }

    @Builder
    @RequiredArgsConstructor
    class Mapping {
        @Getter
        private final String from;
        @Getter
        private final String to;
        @Getter
        private final Mapper mapper;
        private final ValueMapping valueMapping;

        public String mapValue(String from) {
            if (valueMapping == null) {
                return from;
            }
            return valueMapping.map(from);
        }

        public MappingEntry mappingEntry(String from) {
            MappingEntry.MappingEntryBuilder mappingEntryBuilder = MappingEntry.builder()
                    .key(to);
            if (valueMapping == null) {
                return mappingEntryBuilder.value(from).build();
            }
            return mappingEntryBuilder.value(valueMapping.map(from)).build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    class MappingEntry {
        private final String key;
        private final String value;
    }

    static FlatMapper.FlatMapperBuilder flatMapper() {
        return new FlatMapper.FlatMapperBuilder();
    }

    static AdvancedMapper.ExtendedMapperBuilder mapper() {
        return new AdvancedMapper.ExtendedMapperBuilder();
    }

}
