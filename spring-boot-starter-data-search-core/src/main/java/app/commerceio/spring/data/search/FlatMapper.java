package app.commerceio.spring.data.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class FlatMapper implements Mapper {

    private final List<Mapping> mappings;

    public String map(String from) {
        if (mappings == null || mappings.isEmpty()) {
            return from;
        }
        return mapping(from)
                .map(Mapping::getTo)
                .orElse(from);
    }

    @Override
    public MappingEntry mappingEntry(String key) {
        MappingEntry mappingEntry = MappingEntry.builder()
                .key(key)
                .build();

        if (mappings == null || mappings.isEmpty()) {
            return mappingEntry;
        }
        return mapping(key)
                .map(mapping -> MappingEntry.builder()
                        .key(mapping.getTo())
                        .valueMapping(mapping.mappingEntry().getValueMapping())
                        .build())
                .orElse(mappingEntry);
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

        public FlatMapperBuilder mapping(String from, String to, ValueMapping valueMapping) {
            this.fieldMapping.add(Mapping.builder()
                    .from(from)
                    .to(to)
                    .valueMapping(valueMapping)
                    .mapper(null)
                    .build());
            return this;
        }

        public FlatMapper build() {
            return new FlatMapper(fieldMapping);
        }
    }
}
