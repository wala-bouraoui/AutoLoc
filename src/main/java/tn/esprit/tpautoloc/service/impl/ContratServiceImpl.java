package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.exception.BusinessRuleException;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IContratRepository;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.service.IContratService;
import tn.esprit.tpautoloc.web.dto.ContratDTO;
import tn.esprit.tpautoloc.web.mapper.ContratMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @Override
    public ContratDTO create(ContratDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        Contrat saved = contratRepository.save(contratMapper.toEntity(dto));
        lierReservation(saved, dto.getReservationId());
        return contratMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ContratDTO findById(Long id) {
        return contratMapper.toDto(chargerContrat(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratDTO> findAll() {
        return contratRepository.findAll().stream()
                .map(contratMapper::toDto)
                .toList();
    }

    @Override
    public ContratDTO update(Long id, ContratDTO dto) {
        Contrat existant = chargerContrat(id);
        contratMapper.updateEntity(dto, existant);
        Contrat saved = contratRepository.save(existant);
        lierReservation(saved, dto.getReservationId());
        return contratMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }

    private Contrat chargerContrat(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    /**
     * Réservation est le côté propriétaire de l'association one-to-one :
     * c'est donc elle qu'il faut mettre à jour pour rattacher le contrat.
     */
    private void lierReservation(Contrat contrat, Long reservationId) {
        if (reservationId == null) {
            return;
        }
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", reservationId));
        Contrat dejaLie = reservation.getContrat();
        if (dejaLie != null && !dejaLie.getIdContrat().equals(contrat.getIdContrat())) {
            throw new BusinessRuleException(
                    "La réservation " + reservationId + " possède déjà un contrat");
        }
        reservation.setContrat(contrat);
        reservationRepository.save(reservation);
        contrat.setReservation(reservation);
    }
}
