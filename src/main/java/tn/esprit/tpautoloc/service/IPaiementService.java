package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;

public interface IPaiementService {

    PaiementDTO create(PaiementDTO dto);

    PaiementDTO findById(Long id);

    List<PaiementDTO> findAll();

    PaiementDTO update(Long id, PaiementDTO dto);

    void deleteById(Long id);
}
