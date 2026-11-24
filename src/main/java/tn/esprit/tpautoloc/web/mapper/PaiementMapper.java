package tn.esprit.tpautoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;

@Mapper(componentModel = "spring")
public interface PaiementMapper {

    @Mapping(target = "id", source = "idPaiement")
    @Mapping(target = "contratId", source = "contrat.idContrat")
    PaiementDTO toDto(Paiement paiement);

    /** Le contrat est rattaché par le service à partir de contratId. */
    @Mapping(target = "idPaiement", source = "id")
    @Mapping(target = "contrat", ignore = true)
    Paiement toEntity(PaiementDTO dto);

    @Mapping(target = "idPaiement", ignore = true)
    @Mapping(target = "contrat", ignore = true)
    void updateEntity(PaiementDTO dto, @MappingTarget Paiement paiement);
}
