package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

public interface IVehiculeService {

    VehiculeDTO create(VehiculeDTO dto);

    VehiculeDTO findById(Long id);

    List<VehiculeDTO> findAll();

    VehiculeDTO update(Long id, VehiculeDTO dto);

    void deleteById(Long id);

    /** Rattache le véhicule à l'agence (ou le transfère d'une agence à une autre). */
    VehiculeDTO affecterVehiculeAAgence(Long vehiculeId, Long agenceId);

    /** Détache le véhicule de son agence ; erreur métier s'il n'est rattaché à aucune agence. */
    VehiculeDTO desaffecterVehiculeDeAgence(Long vehiculeId);
}
