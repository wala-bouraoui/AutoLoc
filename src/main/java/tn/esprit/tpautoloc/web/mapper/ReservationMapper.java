package tn.esprit.tpautoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "id", source = "idReservation")
    @Mapping(target = "clientId", source = "client.idClient")
    @Mapping(target = "clientNomComplet", source = "client", qualifiedByName = "nomComplet")
    @Mapping(target = "vehiculeId", source = "vehicule.idVehicule")
    @Mapping(target = "vehiculeImmatriculation", source = "vehicule.immatriculation")
    @Mapping(target = "tarifEstime", ignore = true) // calculé par le service (stratégies de tarification)
    ReservationResponseDTO toDto(Reservation reservation);

    /** Équivalent null-safe de prenom + " " + nom. */
    @Named("nomComplet")
    default String nomComplet(Client client) {
        if (client == null) {
            return null;
        }
        return client.getPrenom() + " " + client.getNom();
    }
}
