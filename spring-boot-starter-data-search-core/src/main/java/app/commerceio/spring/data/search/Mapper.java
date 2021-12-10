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
import java.util.stream.Stream;

public interface Mapper {

    List<Mapper.Mapping> getMappings();

    String map(String from);

    MappingEntry mappingEntry(String key);

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

    @Getter
    @Builder
    @RequiredArgsConstructor
    class Mapping {
        private final String from;
        private final String to;
        private final Mapper mapper;
        private final ValueMapping valueMapping;

        public MappingEntry mappingEntry() {
            MappingEntry.MappingEntryBuilder mappingEntryBuilder = MappingEntry.builder()
                    .key(to);
            if (valueMapping == null) {
                return mappingEntryBuilder.build();
            }
            return mappingEntryBuilder.valueMapping(valueMapping).build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    class MappingEntry {
        private final String key;
        private final ValueMapping valueMapping;

        public String value(String from) {
            if (valueMapping == null) {
                return from;
            } else {
                return Stream.of(getValues(from))
                        .map(this::cleanValue)
                        .map(valueMapping::map)
                        .collect(Collectors.joining(","));
            }
        }

        private String[] getValues(String value) {
            return value.split("(?<!\\\\),");
        }

        private String cleanValue(String value) {
            return value
                    .replace("\\,", ",")
                    .replace("\\*", "*");
        }
    }

    static FlatMapper.FlatMapperBuilder flatMapper() {
        return new FlatMapper.FlatMapperBuilder();
    }

    static AdvancedMapper.ExtendedMapperBuilder mapper() {
        return new AdvancedMapper.ExtendedMapperBuilder();
    }

}
