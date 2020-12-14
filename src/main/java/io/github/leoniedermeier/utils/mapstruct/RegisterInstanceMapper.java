package io.github.leoniedermeier.utils.mapstruct;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;

public interface RegisterInstanceMapper<DTO extends Identifiable<?>, MODELL> {

    @AfterMapping
    default void registerInstance(DTO dto, @MappingTarget MODELL modell, @Context MappingContext mappingContext) {
        if (dto != null && modell != null) {
            mappingContext.registerInstance(dto.getId(), modell);
        }
    }
}
