package tn.esprit.tpautoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;

@Mapper(componentModel = "spring")
public interface EmployeMapper {

    @Mapping(target = "id", source = "idEmploye")
    @Mapping(target = "agenceId", source = "agence.idAgence")
    EmployeDTO toDto(Employe employe);

    @Mapping(target = "idEmploye", source = "id")
    @Mapping(target = "agence", ignore = true)
    Employe toEntity(EmployeDTO dto);

    @Mapping(target = "idEmploye", ignore = true)
    @Mapping(target = "agence", ignore = true)
    void updateEntity(EmployeDTO dto, @MappingTarget Employe employe);
}
