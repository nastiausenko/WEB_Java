package dev.usenkonastia.api.service.mapper;


import dev.usenkonastia.api.domain.CosmoCat;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatDto;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatEntryDto;
import dev.usenkonastia.api.dto.cosmoCat.CosmoCatListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

    default CosmoCatListDto toCosmoCatListDto(List<CosmoCat> cats) {
        return CosmoCatListDto.builder().cosmoCats(toCosmoCatEntryDto(cats)).build();
    }

    List<CosmoCatEntryDto> toCosmoCatEntryDto(List<CosmoCat> cats);

    @Mapping(source = "catId", target = "catId")
    @Mapping(source = "catName", target = "catName")
    @Mapping(source = "email", target = "email")
    CosmoCatEntryDto toCosmoCatEntryDto(CosmoCat cat);
}
