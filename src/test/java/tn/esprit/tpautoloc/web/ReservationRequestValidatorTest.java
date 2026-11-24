package tn.esprit.tpautoloc.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import tn.esprit.tpautoloc.web.dto.ReservationRequestDTO;
import tn.esprit.tpautoloc.web.validator.ReservationRequestValidator;

/** Atelier 7 - Partie 5 : validateur personnalisé des dates d'une réservation. */
class ReservationRequestValidatorTest {

    private final ReservationRequestValidator validator = new ReservationRequestValidator();

    private static ValidatorFactory factory;
    private static Validator beanValidator;

    @BeforeAll
    static void initialiser() {
        factory = Validation.buildDefaultValidatorFactory();
        beanValidator = factory.getValidator();
    }

    @AfterAll
    static void fermer() {
        factory.close();
    }

    private ReservationRequestDTO requete(LocalDate debut, LocalDate fin) {
        return ReservationRequestDTO.builder()
                .clientId(1L).vehiculeId(1L).dateDebut(debut).dateFin(fin).build();
    }

    private Errors valider(ReservationRequestDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "reservationRequestDTO");
        validator.validate(dto, errors);
        return errors;
    }

    @Test
    void supporteUniquementReservationRequestDTO() {
        assertTrue(validator.supports(ReservationRequestDTO.class));
        assertFalse(validator.supports(String.class));
    }

    @Test
    void datesCoherentesSontAcceptees() {
        LocalDate demain = LocalDate.now().plusDays(1);
        Errors errors = valider(requete(demain, demain.plusDays(3)));

        assertFalse(errors.hasErrors());
    }

    @Test
    void debutAujourdhuiEstAccepte() {
        Errors errors = valider(requete(LocalDate.now(), LocalDate.now().plusDays(2)));

        assertFalse(errors.hasErrors());
    }

    @Test
    void dateFinEgaleAuDebutEstRejetee() {
        LocalDate debut = LocalDate.now().plusDays(2);
        Errors errors = valider(requete(debut, debut));

        assertTrue(errors.hasFieldErrors("dateFin"));
        assertEquals("reservation.dates.invalides",
                errors.getFieldError("dateFin").getCode());
    }

    @Test
    void dateFinAvantLeDebutEstRejetee() {
        LocalDate debut = LocalDate.now().plusDays(5);
        Errors errors = valider(requete(debut, debut.minusDays(2)));

        assertTrue(errors.hasFieldErrors("dateFin"));
    }

    @Test
    void debutDansLePasseEstRejete() {
        Errors errors = valider(requete(LocalDate.now().minusDays(1), LocalDate.now().plusDays(2)));

        assertTrue(errors.hasFieldErrors("dateDebut"));
        assertEquals("reservation.dateDebut.passee",
                errors.getFieldError("dateDebut").getCode());
    }

    @Test
    void datesAbsentesSontLaisseesABeanValidation() {
        Errors errors = valider(requete(null, null));

        assertFalse(errors.hasErrors());
        // ... et Bean Validation, lui, les rejette
        var violations = beanValidator.validate(requete(null, null));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateDebut")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateFin")));
    }
}
