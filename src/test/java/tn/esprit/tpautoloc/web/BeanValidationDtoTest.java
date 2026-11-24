package tn.esprit.tpautoloc.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tn.esprit.tpautoloc.domain.CategorieVehicule;
import tn.esprit.tpautoloc.domain.ModePaiement;
import tn.esprit.tpautoloc.web.dto.ClientDTO;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

/** Atelier 7 - Partie 5.6 : contraintes Bean Validation des DTO. */
class BeanValidationDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void initialiser() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void fermer() {
        factory.close();
    }

    private static <T> Set<String> champsInvalides(Set<ConstraintViolation<T>> violations) {
        return violations.stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(Collectors.toSet());
    }

    @Test
    void clientAvecEmailInvalideEstRejete() {
        ClientDTO dto = ClientDTO.builder().nom("Ben Ali").prenom("Sami").email("pas-un-email").build();

        Set<ConstraintViolation<ClientDTO>> violations = validator.validate(dto);

        assertEquals(Set.of("email"), champsInvalides(violations));
        assertEquals("L'adresse email est invalide", violations.iterator().next().getMessage());
    }

    @Test
    void clientValideEstAccepte() {
        ClientDTO dto = ClientDTO.builder().nom("Ben Ali").prenom("Sami").email("sami@example.com").build();

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void vehiculeSansImmatriculationEstRejete() {
        VehiculeDTO dto = VehiculeDTO.builder()
                .marque("Toyota").modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("100"))
                .agenceId(1L)
                .build();

        assertEquals(Set.of("immatriculation"), champsInvalides(validator.validate(dto)));
    }

    @Test
    void vehiculeAvecTarifNulOuNegatifEstRejete() {
        VehiculeDTO dto = VehiculeDTO.builder()
                .immatriculation("123-TN-1").marque("Toyota").modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(BigDecimal.ZERO)
                .agenceId(1L)
                .build();
        assertEquals(Set.of("tarifJournalier"), champsInvalides(validator.validate(dto)));

        dto.setTarifJournalier(new BigDecimal("-5"));
        assertEquals(Set.of("tarifJournalier"), champsInvalides(validator.validate(dto)));
    }

    @Test
    void paiementAvecMontantNulOuNegatifEstRejete() {
        PaiementDTO dto = PaiementDTO.builder()
                .montant(BigDecimal.ZERO).modePaiement(ModePaiement.CARTE).contratId(1L).build();
        assertEquals(Set.of("montant"), champsInvalides(validator.validate(dto)));

        dto.setMontant(new BigDecimal("-10"));
        assertEquals(Set.of("montant"), champsInvalides(validator.validate(dto)));
    }

    @Test
    void paiementSansModeNiContratEstRejete() {
        PaiementDTO dto = PaiementDTO.builder().montant(new BigDecimal("50")).build();

        assertEquals(Set.of("modePaiement", "contratId"), champsInvalides(validator.validate(dto)));
    }

    @Test
    void paiementValideEstAccepte() {
        PaiementDTO dto = PaiementDTO.builder()
                .montant(new BigDecimal("50")).modePaiement(ModePaiement.ESPECES).contratId(1L).build();

        assertTrue(validator.validate(dto).isEmpty());
    }
}
