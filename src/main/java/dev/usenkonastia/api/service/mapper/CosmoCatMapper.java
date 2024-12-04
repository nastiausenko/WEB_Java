package dev.usenkonastia.api.service.mapper;


import dev.usenkonastia.api.domain.CosmoCat;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatDto;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatEntryDto;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatListDto;
import dev.usenkonastia.api.repository.entity.CosmoCatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CosmoCatMapper {

    @Mapping(source = "catName", target = "catName")
    @Mapping(source = "email", target = "email")
    CosmoCatDto toCosmoCatDto(CosmoCat cat);

    @Mapping(target = "catId", ignore = true)
    @Mapping(source = "catName", target = "catName")
    @Mapping(source = "email", target = "email")
    CosmoCat toCosmoCat(CosmoCatDto cosmoCatDto);

    @Mapping(source = "id", target = "catId")
    @Mapping(source = "catName", target = "catName")
    @Mapping(source = "email", target = "email")
    CosmoCat toCosmoCat(CosmoCatEntity cosmoCatEntity);

    default CosmoCatListDto toCosmoCatListDto(List<CosmoCat> cats) {
        return CosmoCatListDto.builder().cosmoCats(toCosmoCatEntryDto(cats)).build();
    }

    List<CosmoCatEntryDto> toCosmoCatEntryDto(List<CosmoCat> cats);

    @Mapping(source = "catId", target = "catId")
    @Mapping(source = "catName", target = "catName")
    @Mapping(source = "email", target = "email")
    CosmoCatEntryDto toCosmoCatEntryDto(CosmoCat cat);

    @Mapping(source = "catId", target = "id")
    @Mapping(source = "catName", target = "catName")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "orders", ignore = true)
    CosmoCatEntity toCosmoCatEntity(CosmoCat cosmoCat);

    default List<CosmoCat> toCosmoCatList(Iterator<CosmoCatEntity> cosmoCatEntityIterator) {
        List<CosmoCat> result = new ArrayList<>();
        cosmoCatEntityIterator.forEachRemaining(
                (cosmoCatEntity) -> {
                    result.add(toCosmoCat(cosmoCatEntity));
                });
        return result;
    }
}
