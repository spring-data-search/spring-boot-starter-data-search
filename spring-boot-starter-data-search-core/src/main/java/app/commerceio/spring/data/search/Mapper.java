package app.commerceio.spring.data.search;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class Mapper {

    private final List<Mapping> mappings;
    private final boolean flat;

    Mapper(List<Mapping> mappings, boolean flat) {
        this.mappings = mappings;
        this.flat = flat;
    }

    public String map(String from) {
        if (mappings == null || mappings.size() == 0) {
            return from;
        }

        if (flat) {
            return mapping(from)
                    .map(Mapping::getTo)
                    .orElse(from);
        } else {
            return doMap(from);
        }
    }

    public Pageable map(Pageable from) {
        if (mappings == null || mappings.size() == 0) {
            return from;
        }
        Sort sort = Sort.by(from.getSort().stream()
                .map(order -> new Sort.Order(order.getDirection(), map(order.getProperty())))
                .collect(Collectors.toList()));
        return PageRequest.of(from.getPageNumber(), from.getPageSize(), sort);
    }

    private String doMap(String from) {
        String[] keys = StringUtils.split(StringUtils.trimToEmpty(from), ".", 2);
        Optional<Mapping> mapping = mapping(keys[0]);
        String firstPart = mapping
                .map(Mapping::getTo)
                .orElse(from);
        if (keys.length == 1) {
            return firstPart;
        } else {
            var secondPart = mapping
                    .map(Mapping::getMapper)
                    .map(mapper -> mapper.map(keys[1]))
                    .orElse(keys[1]);
            return StringUtils.joinWith(".", firstPart, secondPart);
        }
    }

    private Optional<Mapping> mapping(String from) {
        return mappings.stream()
                .filter(mapping -> from.equalsIgnoreCase(mapping.getFrom()))
                .findFirst();
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class Mapping {
        private final String from;
        private final String to;
        private final Mapper mapper;
    }

    public static FlatMapperBuilder flatMapper() {
        return new FlatMapperBuilder();
    }

    public static MapperBuilder mapper() {
        return new MapperBuilder();
    }

    public static class FlatMapperBuilder {
        private final List<Mapping> fieldMapping;

        FlatMapperBuilder() {
            this.fieldMapping = new ArrayList<>();
        }

        public FlatMapperBuilder mapping(String from, String to) {
            this.fieldMapping.add(Mapping.builder()
                    .from(from)
                    .to(to)
                    .mapper(null)
                    .build());
            return this;
        }

        public Mapper build() {
            return new Mapper(fieldMapping, true);
        }
    }

    public static class MapperBuilder {
        private final List<Mapping> fieldMapping;

        MapperBuilder() {
            this.fieldMapping = new ArrayList<>();
        }

        public MapperBuilder mapping(String from, String to) {
            this.fieldMapping.add(Mapping.builder()
                    .from(from)
                    .to(to)
                    .mapper(null)
                    .build());
            return this;
        }

        public MapperBuilder mapping(String from, String to, Mapper mapper) {
            this.fieldMapping.add(Mapping.builder()
                    .from(from)
                    .to(to)
                    .mapper(mapper)
                    .build());
            return this;
        }

        public Mapper build() {
            return new Mapper(fieldMapping, false);
        }
    }
}
