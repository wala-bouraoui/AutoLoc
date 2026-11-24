package tn.esprit.tpautoloc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.CategorieVehicule;
import tn.esprit.tpautoloc.domain.RoleEmploye;
import tn.esprit.tpautoloc.exception.BusinessRuleException;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

/**
 * Atelier 7 - Partie 1 : affectation / désaffectation (véhicule et employé).
 * Test d'intégration sur la base configurée, avec nettoyage en fin de test.
 */
@SpringBootTest
class AffectationServiceTest {

    @Autowired
    private IAgenceService agenceService;
    @Autowired
    private IVehiculeService vehiculeService;
    @Autowired
    private IEmployeService employeService;
    @Autowired
    private IVehiculeRepository vehiculeRepository;

    private AgenceDTO agenceA;
    private AgenceDTO agenceB;
    private VehiculeDTO vehicule;
    private EmployeDTO employe;

    @BeforeEach
    void preparer() {
        agenceA = agenceService.create(AgenceDTO.builder().nom("Agence A").ville("Tunis").build());
        agenceB = agenceService.create(AgenceDTO.builder().nom("Agence B").ville("Sfax").build());

        vehicule = vehiculeService.create(VehiculeDTO.builder()
                .immatriculation("AFF-001-TN").marque("Peugeot").modele("208")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("100.00"))
                .agenceId(agenceA.getId())
                .build());

        employe = employeService.create(EmployeDTO.builder()
                .nom("Gharbi").prenom("Mehdi").role(RoleEmploye.AGENT)
                .agenceId(agenceA.getId())
                .build());
    }

    @AfterEach
    void nettoyer() {
        employeService.deleteById(employe.getId());
        vehiculeService.deleteById(vehicule.getId());
        agenceService.deleteById(agenceA.getId());
        agenceService.deleteById(agenceB.getId());
    }

    // ---------- Véhicule ----------

    @Test
    void affecterVehiculeAUneAgenceExistante() {
        VehiculeDTO resultat = vehiculeService.affecterVehiculeAAgence(
                vehicule.getId(), agenceB.getId());

        assertEquals(agenceB.getId(), resultat.getAgenceId());
        // Vérification de la clé étrangère réellement persistée
        assertEquals(agenceB.getId(), vehiculeService.findById(vehicule.getId()).getAgenceId());
    }

    @Test
    void transfererVehiculeDUneAgenceVersUneAutre() {
        assertEquals(agenceA.getId(), vehiculeService.findById(vehicule.getId()).getAgenceId());

        vehiculeService.affecterVehiculeAAgence(vehicule.getId(), agenceB.getId());

        assertEquals(agenceB.getId(), vehiculeService.findById(vehicule.getId()).getAgenceId());
    }

    @Test
    void affecterVehiculeInexistantEstRefuse() {
        Long agenceId = agenceA.getId();
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.affecterVehiculeAAgence(-999L, agenceId));
    }

    @Test
    void affecterVehiculeAUneAgenceInexistanteLaisseLaBaseInchangee() {
        Long vehiculeId = vehicule.getId();
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.affecterVehiculeAAgence(vehiculeId, -999L));

        // Aucune modification partielle : le véhicule reste dans son agence d'origine
        assertEquals(agenceA.getId(), vehiculeService.findById(vehiculeId).getAgenceId());
    }

    @Test
    void desaffecterVehiculeMetLaCleEtrangereANull() {
        VehiculeDTO resultat = vehiculeService.desaffecterVehiculeDeAgence(vehicule.getId());

        assertNull(resultat.getAgenceId());
        assertNull(vehiculeRepository.findById(vehicule.getId()).orElseThrow().getAgence());
    }

    @Test
    void desaffecterVehiculeDejaLibreEstUneErreurMetier() {
        Long vehiculeId = vehicule.getId();
        vehiculeService.desaffecterVehiculeDeAgence(vehiculeId);

        assertThrows(BusinessRuleException.class,
                () -> vehiculeService.desaffecterVehiculeDeAgence(vehiculeId));
    }

    @Test
    void desaffecterVehiculeInexistantEstRefuse() {
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.desaffecterVehiculeDeAgence(-999L));
    }

    // ---------- Employé ----------

    @Test
    void affecterEtTransfererUnEmploye() {
        EmployeDTO resultat = employeService.affecterEmployeAAgence(
                employe.getId(), agenceB.getId());

        assertNotNull(resultat);
        assertEquals(agenceB.getId(), resultat.getAgenceId());
        assertEquals(agenceB.getId(), employeService.findById(employe.getId()).getAgenceId());
    }

    @Test
    void affecterEmployeAvecIdentifiantsInexistantsEstRefuse() {
        Long employeId = employe.getId();
        Long agenceId = agenceA.getId();

        assertThrows(ResourceNotFoundException.class,
                () -> employeService.affecterEmployeAAgence(-999L, agenceId));
        assertThrows(ResourceNotFoundException.class,
                () -> employeService.affecterEmployeAAgence(employeId, -999L));
        assertEquals(agenceA.getId(), employeService.findById(employeId).getAgenceId());
    }

    @Test
    void desaffecterUnEmploye() {
        Long employeId = employe.getId();

        EmployeDTO resultat = employeService.desaffecterEmployeDeAgence(employeId);

        assertNull(resultat.getAgenceId());
        assertNull(employeService.findById(employeId).getAgenceId());
        assertThrows(BusinessRuleException.class,
                () -> employeService.desaffecterEmployeDeAgence(employeId));
    }
}
