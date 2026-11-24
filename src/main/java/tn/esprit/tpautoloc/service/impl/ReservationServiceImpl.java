package tn.esprit.tpautoloc.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.StatutReservation;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IClientRepository;
import tn.esprit.tpautoloc.repository.IReservationRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.service.factory.TarificationStrategyFactory;
import tn.esprit.tpautoloc.service.observer.ReservationSubject;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.mapper.ReservationMapper;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationSubject reservationSubject;
    private final ReservationMapper reservationMapper;
    private final TarificationStrategyFactory tarificationStrategyFactory;

    /**
     * Création minimale (client + véhicule + dates, statut EN_ATTENTE).
     * La logique de création complète (disponibilité, contrat...) est prévue
     * ultérieurement via ReservationFactory.
     */
    @Override
    public ReservationResponseDTO create(ReservationRequestDTO dto) {
        Reservation reservation = new Reservation();
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        appliquer(dto, reservation);
        return versReponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        return versReponse(chargerReservation(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::versReponse)
                .toList();
    }

    @Override
    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {
        Reservation existante = chargerReservation(id);
        appliquer(dto, existante);
        return versReponse(reservationRepository.save(existante));
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Réservation", id);
        }
        reservationRepository.deleteById(id);
    }

    /**
     * Crée une réservation et notifie tous les observateurs.
     */
    @Override
    public Reservation creerReservation(Client client) {
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setDateDebut(LocalDate.now(ZoneId.systemDefault()));
        reservation.setStatut(StatutReservation.CONFIRMEE);

        // Déclenche le process métier via le pattern Observer
        reservationSubject.notifierConfirmation(reservation);
        return reservation;
    }

    /**
     * Annule une réservation existante et notifie tous les observateurs.
     */
    @Override
    public Reservation annulerReservation(Reservation reservation) {
        reservation.setStatut(StatutReservation.ANNULEE);
        log.info("Réservation #{} annulée pour le client {}",
                reservation.getIdReservation(), reservation.getClient().getEmail());

        reservationSubject.notifierAnnulation(reservation);
        return reservation;
    }

    private void appliquer(ReservationRequestDTO dto, Reservation reservation) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", dto.getVehiculeId()));

        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
    }

    private Reservation chargerReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", id));
    }

    /** Entité -> DTO, puis calcul du tarif estimé avec la stratégie adaptée à la durée. */
    private ReservationResponseDTO versReponse(Reservation reservation) {
        ReservationResponseDTO reponse = reservationMapper.toDto(reservation);
        Vehicule vehicule = reservation.getVehicule();
        if (vehicule != null && vehicule.getTarifJournalier() != null
                && reservation.getDateDebut() != null && reservation.getDateFin() != null) {
            reponse.setTarifEstime(tarificationStrategyFactory.resoudre(reservation)
                    .calculerTarif(vehicule, reservation));
        }
        return reponse;
    }
}
