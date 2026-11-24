package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;

public interface IEmployeService {

    EmployeDTO create(EmployeDTO dto);

    EmployeDTO findById(Long id);

    List<EmployeDTO> findAll();

    EmployeDTO update(Long id, EmployeDTO dto);

    void deleteById(Long id);

    /** Rattache l'employé à l'agence (ou le transfère d'une agence à une autre). */
    EmployeDTO affecterEmployeAAgence(Long employeId, Long agenceId);

    /** Détache l'employé de son agence ; erreur métier s'il n'est rattaché à aucune agence. */
    EmployeDTO desaffecterEmployeDeAgence(Long employeId);
}
