package tn.esprit.tpautoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.tpautoloc.domain.Equipement;
import tn.esprit.tpautoloc.web.dto.EquipementDTO;

@Mapper(componentModel = "spring")
public interface EquipementMapper {

    @Mapping(target = "id", source = "idEquipement")
    EquipementDTO toDto(Equipement equipement);

    @Mapping(target = "idEquipement", source = "id")
    @Mapping(target = "vehicules", ignore = true)
    Equipement toEntity(EquipementDTO dto);

    @Mapping(target = "idEquipement", ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    void updateEntity(EquipementDTO dto, @MappingTarget Equipement equipement);
}
