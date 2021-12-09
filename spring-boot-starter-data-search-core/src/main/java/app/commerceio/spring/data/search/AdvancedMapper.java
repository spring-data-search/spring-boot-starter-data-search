package app.commerceio.spring.data.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class AdvancedMapper implements Mapper {

    private final List<Mapping> mappings;

    @Override
    public String map(String from) {
        if (mappings == null || mappings.isEmpty()) {
            return from;
        }

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

    @Override
    public MappingEntry map(String key, String value) {
        MappingEntry mappingEntry = MappingEntry.builder()
                .key(key)
                .value(value)
                .build();

        if (mappings == null || mappings.isEmpty()) {
            return mappingEntry;
        }

        String[] keys = StringUtils.split(StringUtils.trimToEmpty(key), ".", 2);
        Optional<Mapping> mapping = mapping(keys[0]);
        MappingEntry firstPart = mapping
                .map(map -> map.mappingEntry(value))
                .orElse(mappingEntry);
        if (keys.length == 1) {
            return firstPart;
        } else {
            var secondPart = mapping
                    .map(Mapping::getMapper)
                    .map(mapper -> mapper.map(keys[1], value))
                    .orElse(MappingEntry.builder()
                            .key(keys[1])
                            .value(value)
                            .build());
            String toKey = StringUtils.joinWith(".", firstPart.getKey(), secondPart.getKey());
            return MappingEntry.builder()
                    .key(toKey)
                    .value(secondPart.getValue())
                    .build();
        }
    }


    public static class ExtendedMapperBuilder {
        private final List<Mapping> fieldMapping;

        ExtendedMapperBuilder() {
            this.fieldMapping = new ArrayList<>();
        }

        public ExtendedMapperBuilder mapping(String from, String to) {
            this.fieldMapping.add(Mapping.builder()
                    .from(from)
                    .to(to)
                    .mapper(null)
                    .build());
            return this;
        }

        public ExtendedMapperBuilder mapping(String from, String to, ValueMapping valueMapping) {
            this.fieldMapping.add(Mapping.builder()
                    .from(from)
                    .to(to)
                    .valueMapping(valueMapping)
                    .mapper(null)
                    .build());
            return this;
        }

        public ExtendedMapperBuilder mapping(String from, String to, Mapper mapper) {
            this.fieldMapping.add(Mapping.builder()
                    .from(from)
                    .to(to)
                    .mapper(mapper)
                    .build());
            return this;
        }

        public AdvancedMapper build() {
            return new AdvancedMapper(fieldMapping);
        }
    }
}
