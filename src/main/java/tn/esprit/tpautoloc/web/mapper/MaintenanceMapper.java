package tn.esprit.tpautoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.tpautoloc.domain.Maintenance;
import tn.esprit.tpautoloc.web.dto.MaintenanceDTO;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {

    @Mapping(target = "id", source = "idMaintenance")
    @Mapping(target = "vehiculeId", source = "vehicule.idVehicule")
    MaintenanceDTO toDto(Maintenance maintenance);

    @Mapping(target = "idMaintenance", source = "id")
    @Mapping(target = "vehicule", ignore = true)
    Maintenance toEntity(MaintenanceDTO dto);

    @Mapping(target = "idMaintenance", ignore = true)
    @Mapping(target = "vehicule", ignore = true)
    void updateEntity(MaintenanceDTO dto, @MappingTarget Maintenance maintenance);
}
