package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;



import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.repository.*;
import tn.esprit.tpautoloc.service.*;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.web.dto.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TpAutolocApplicationTests {

    // ===== Repositories =====

    @Autowired
    private IAgenceRrepository agenceRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IVehiculeRepository vehiculeRepository;

    @Autowired
    private IEmployeRepository employeRepository;

    @Autowired
    private IContratRepository contratRepository;

    @Autowired
    private IEquipementRepository equipementRepository;



    // ===== Services (Atelier 4 - Part 2) =====

    @Autowired
    private IAgenceService agenceService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private IVehiculeService vehiculeService;

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IContratService contratService;

    @Autowired
    private IEquipementService equipementService;


    // =====================================================
    //                 TESTS REPOSITORY
    // =====================================================

    // PARTIE 2 : TEST AGENCE

    @Test
    void testAgenceRepository() {

        System.out.println("========== TEST AGENCE (REPOSITORY) ==========");

        Agence agence = new Agence();
        agence.setNom("AgenceTest");
        agence.setVille("Tunis");
        agence.setAdresse("Centre-ville");
        agence.setTelephone("71111222");

        agence = agenceRepository.save(agence);
        Long id = agence.getIdAgence();

        assertNotNull(id);

        System.out.println("Agence creee : " + id);

        List<Agence> agences = agenceRepository.findAll();
        System.out.println("Liste des agences : " + agences.size());

        assertTrue(agenceRepository.findById(id).isPresent());
        assertTrue(agenceRepository.existsById(id));

        System.out.println("Nombre d'agences : "
                + agenceRepository.count());

        agenceRepository.deleteById(id);

        assertFalse(agenceRepository.existsById(id));

        System.out.println("Agence supprimee.");
    }


    // PARTIE 3 : TEST CLIENT

    @Test
    void testClientRepository() {

        System.out.println("========== TEST CLIENT (REPOSITORY) ==========");

        Client client1 = new Client();
        client1.setNom("Ben Ali");
        client1.setPrenom("Sami");
        client1.setEmail("sami.test@example.com");
        client1.setTelephone("20111222");
        client1.setNumPermis("PERMIS-TEST-001");
        client1.setDateInscription(LocalDate.now());

        Client client2 = new Client();
        client2.setNom("Trabelsi");
        client2.setPrenom("Amira");
        client2.setEmail("amira.test@example.com");
        client2.setTelephone("22111333");
        client2.setNumPermis("PERMIS-TEST-002");
        client2.setDateInscription(LocalDate.now());

        client1 = clientRepository.save(client1);
        client2 = clientRepository.save(client2);

        Long id1 = client1.getIdClient();
        Long id2 = client2.getIdClient();

        assertNotNull(id1);
        assertNotNull(id2);

        System.out.println("Deux clients enregistres.");

        List<Client> clients = clientRepository.findAll();
        System.out.println("Liste des clients : " + clients.size());

        assertTrue(clientRepository.findById(id1).isPresent());
        assertTrue(clientRepository.existsById(id1));
        assertFalse(clientRepository.existsById(-999L));

        System.out.println("Nombre de clients : "
                + clientRepository.count());

        clientRepository.deleteById(id1);
        clientRepository.deleteById(id2);

        System.out.println("Clients supprimes.");
    }


    // PARTIE 4 : TEST VEHICULE

    @Test
    void testVehiculeRepository() {

        System.out.println("========== TEST VEHICULE (REPOSITORY) ==========");

        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("TEST-123");
        vehicule.setMarque("Toyota");
        vehicule.setModele("Yaris");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("120.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        vehicule = vehiculeRepository.save(vehicule);
        Long id = vehicule.getIdVehicule();

        assertNotNull(id);

        System.out.println("Vehicule enregistre : " + id);

        List<Vehicule> vehicules = vehiculeRepository.findAll();
        System.out.println("Liste des vehicules : " + vehicules.size());

        assertTrue(vehiculeRepository.findById(id).isPresent());

        System.out.println("Nombre de vehicules : "
                + vehiculeRepository.count());

        vehicule.setMarque("Renault");
        vehiculeRepository.save(vehicule);

        Vehicule vehiculeModifie =
                vehiculeRepository.findById(id).orElseThrow();

        assertEquals("Renault", vehiculeModifie.getMarque());

        System.out.println("Marque modifiee : "
                + vehiculeModifie.getMarque());

        vehiculeRepository.deleteById(id);

        assertFalse(vehiculeRepository.existsById(id));

        System.out.println("Vehicule supprime.");
    }


    // PARTIE 5 : TEST EMPLOYE

    @Test
    void testEmployeRepository() {

        System.out.println("========== TEST EMPLOYE (REPOSITORY) ==========");

        Agence agence = new Agence();
        agence.setNom("AgenceEmployeTest");
        agence.setVille("Tunis");

        agence = agenceRepository.save(agence);

        Employe employe = new Employe();
        employe.setNom("Mansour");
        employe.setPrenom("Yassine");
        employe.setRole(RoleEmploye.AGENT);
        employe.setAgence(agence);

        employe = employeRepository.save(employe);
        Long id = employe.getIdEmploye();

        assertNotNull(id);
        assertTrue(employeRepository.findById(id).isPresent());
        assertTrue(employeRepository.existsById(id));

        System.out.println("Employe enregistre : " + id);
        System.out.println("Nombre d'employes : "
                + employeRepository.count());

        employeRepository.deleteById(id);
        assertFalse(employeRepository.existsById(id));

        agenceRepository.deleteById(agence.getIdAgence());

        System.out.println("Employe supprime.");
    }


    // PARTIE 5 : TEST CONTRAT

    @Test
    void testContratRepository() {

        System.out.println("========== TEST CONTRAT (REPOSITORY) ==========");

        Contrat contrat = new Contrat();
        contrat.setDateSignature(LocalDate.now());
        contrat.setMontantTotal(new BigDecimal("500.00"));
        contrat.setValide(true);

        contrat = contratRepository.save(contrat);
        Long id = contrat.getIdContrat();

        assertNotNull(id);
        assertTrue(contratRepository.findById(id).isPresent());
        assertTrue(contratRepository.existsById(id));

        System.out.println("Contrat enregistre : " + id);
        System.out.println("Nombre de contrats : "
                + contratRepository.count());

        contratRepository.deleteById(id);

        assertFalse(contratRepository.existsById(id));

        System.out.println("Contrat supprime.");
    }


    // PARTIE 5 : TEST EQUIPEMENT

    @Test
    void testEquipementRepository() {

        System.out.println("========== TEST EQUIPEMENT (REPOSITORY) ==========");

        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS-Test");

        equipement = equipementRepository.save(equipement);
        Long id = equipement.getIdEquipement();

        assertNotNull(id);
        assertTrue(equipementRepository.findById(id).isPresent());
        assertTrue(equipementRepository.existsById(id));

        System.out.println("Equipement enregistre : " + id);
        System.out.println("Nombre d'equipements : "
                + equipementRepository.count());

        equipementRepository.deleteById(id);

        assertFalse(equipementRepository.existsById(id));

        System.out.println("Equipement supprime.");
    }


    // =====================================================
    //                 TESTS SERVICE (DTO)
    // =====================================================

    // =====================================================
    //  SERVICE - PARTIE 2 : AGENCE
    // =====================================================
    @Test
    void testAgenceService() {
        System.out.println("========== TEST AGENCE (SERVICE) ==========");

        int avant = agenceService.findAll().size();

        AgenceDTO dto = AgenceDTO.builder()
                .nom("Agence Centre")
                .ville("Tunis")
                .adresse("Avenue Habib Bourguiba")
                .telephone("71111222")
                .build();

        // create()
        AgenceDTO saved = agenceService.create(dto);
        System.out.println("Agence enregistree : id=" + saved.getId()
                + ", nom=" + saved.getNom() + ", ville=" + saved.getVille());
        assertNotNull(saved.getId());
        assertEquals("Agence Centre", saved.getNom());

        // findAll()
        List<AgenceDTO> agences = agenceService.findAll();
        agences.forEach(a -> System.out.println("  - " + a.getId() + " | " + a.getNom()));
        assertFalse(agences.isEmpty());

        // findById()
        AgenceDTO trouvee = agenceService.findById(saved.getId());
        assertEquals(saved.getId(), trouvee.getId());
        assertEquals(avant + 1, agenceService.findAll().size());

        // update()
        AgenceDTO modif = AgenceDTO.builder()
                .nom("Agence Centre Modifiee")
                .ville("Sfax")
                .adresse(saved.getAdresse())
                .telephone(saved.getTelephone())
                .build();
        AgenceDTO maj = agenceService.update(saved.getId(), modif);
        assertEquals("Agence Centre Modifiee", maj.getNom());
        assertEquals("Sfax", maj.getVille());

        // create() avec un identifiant : refuse
        AgenceDTO avecId = AgenceDTO.builder().id(saved.getId()).nom("X").ville("Y").build();
        assertThrows(IllegalArgumentException.class, () -> agenceService.create(avecId));

        // deleteById() + verification
        Long idSupprime = saved.getId();
        agenceService.deleteById(idSupprime);
        assertThrows(ResourceNotFoundException.class,
                () -> agenceService.findById(idSupprime));
        assertEquals(avant, agenceService.findAll().size());
    }

    // =====================================================
    //  SERVICE - PARTIE 3 : CLIENT
    // =====================================================
    @Test
    void testClientService() {
        System.out.println("========== TEST CLIENT (SERVICE) ==========");

        int avant = clientService.findAll().size();

        ClientDTO c1 = ClientDTO.builder()
                .nom("Ben Ali").prenom("Sami")
                .email("sami.service@example.com")
                .telephone("20111222")
                .numPermis("PERMIS-SVC-001")
                .dateInscription(LocalDate.now())
                .build();
        ClientDTO c2 = ClientDTO.builder()
                .nom("Trabelsi").prenom("Amira")
                .email("amira.service@example.com")
                .telephone("22111333")
                .numPermis("PERMIS-SVC-002")
                .dateInscription(LocalDate.now())
                .build();

        ClientDTO s1 = clientService.create(c1);
        ClientDTO s2 = clientService.create(c2);
        assertNotNull(s1.getId());
        assertNotNull(s2.getId());
        assertNotEquals(s1.getId(), s2.getId());

        assertTrue(clientService.findAll().size() >= 2);

        ClientDTO trouve = clientService.findById(s1.getId());
        assertEquals("Sami", trouve.getPrenom());
        assertEquals(avant + 2, clientService.findAll().size());

        ClientDTO modif = ClientDTO.builder()
                .nom(s1.getNom()).prenom(s1.getPrenom())
                .email("sami.nouveau@example.com")
                .telephone("20999888")
                .build();
        ClientDTO maj = clientService.update(s1.getId(), modif);
        assertEquals("sami.nouveau@example.com", maj.getEmail());

        Long idSupprime = s1.getId();
        clientService.deleteById(idSupprime);
        assertThrows(ResourceNotFoundException.class,
                () -> clientService.findById(idSupprime));

        clientService.deleteById(s2.getId());
        assertEquals(avant, clientService.findAll().size());
    }

    // =====================================================
    //  SERVICE - PARTIE 4 : VEHICULE
    // =====================================================
    @Test
    void testVehiculeService() {
        System.out.println("========== TEST VEHICULE (SERVICE) ==========");

        // Un vehicule doit desormais etre cree avec une agence (agenceId obligatoire)
        AgenceDTO agence = agenceService.create(
                AgenceDTO.builder().nom("Agence Vehicule Test").ville("Tunis").build());

        int avant = vehiculeService.findAll().size();

        VehiculeDTO v = VehiculeDTO.builder()
                .immatriculation("SVC-123-TN")
                .marque("Toyota")
                .modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("120.00"))
                .statut(StatutVehicule.DISPONIBLE)
                .agenceId(agence.getId())
                .build();

        VehiculeDTO saved = vehiculeService.create(v);
        assertNotNull(saved.getId());
        assertEquals(agence.getId(), saved.getAgenceId());

        assertFalse(vehiculeService.findAll().isEmpty());

        VehiculeDTO trouve = vehiculeService.findById(saved.getId());
        assertEquals("SVC-123-TN", trouve.getImmatriculation());
        assertEquals(avant + 1, vehiculeService.findAll().size());

        // agence inexistante
        VehiculeDTO sansAgence = VehiculeDTO.builder()
                .immatriculation("SVC-999-TN").marque("Kia").modele("Rio")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("90.00"))
                .agenceId(-999L)
                .build();
        assertThrows(ResourceNotFoundException.class, () -> vehiculeService.create(sansAgence));

        Long idSupprime = saved.getId();
        vehiculeService.deleteById(idSupprime);
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.findById(idSupprime));
        assertEquals(avant, vehiculeService.findAll().size());

        agenceService.deleteById(agence.getId());
    }

    // =====================================================
    //  SERVICE - PARTIE 5 : EXERCICE AUTONOME (Employe, Contrat, Equipement)
    // =====================================================
    @Test
    void testEmployeService() {
        System.out.println("========== TEST EMPLOYE (SERVICE) ==========");

        AgenceDTO agence = agenceService.create(
                AgenceDTO.builder().nom("Agence Employe Test").ville("Tunis").build());

        EmployeDTO e = EmployeDTO.builder()
                .nom("Mansour").prenom("Yassine")
                .role(RoleEmploye.AGENT)
                .agenceId(agence.getId())
                .build();

        EmployeDTO saved = employeService.create(e);
        assertNotNull(saved.getId());
        assertEquals(agence.getId(), saved.getAgenceId());
        assertFalse(employeService.findAll().isEmpty());

        EmployeDTO trouve = employeService.findById(saved.getId());
        assertEquals("Mansour", trouve.getNom());

        Long idSupprime = saved.getId();
        employeService.deleteById(idSupprime);
        assertThrows(ResourceNotFoundException.class,
                () -> employeService.findById(idSupprime));

        agenceService.deleteById(agence.getId());
    }

    @Test
    void testContratService() {
        System.out.println("========== TEST CONTRAT (SERVICE) ==========");

        ContratDTO c = ContratDTO.builder()
                .dateSignature(LocalDate.now())
                .montantTotal(new BigDecimal("500.00"))
                .valide(true)
                .build();

        ContratDTO saved = contratService.create(c);
        assertNotNull(saved.getId());
        assertFalse(contratService.findAll().isEmpty());

        ContratDTO trouve = contratService.findById(saved.getId());
        assertEquals(0, new BigDecimal("500.00").compareTo(trouve.getMontantTotal()));

        Long idSupprime = saved.getId();
        contratService.deleteById(idSupprime);
        assertThrows(ResourceNotFoundException.class,
                () -> contratService.findById(idSupprime));
    }

    @Test
    void testEquipementService() {
        System.out.println("========== TEST EQUIPEMENT (SERVICE) ==========");

        EquipementDTO saved = equipementService.create(
                EquipementDTO.builder().libelle("GPS-Service-Test").build());
        assertNotNull(saved.getId());
        assertFalse(equipementService.findAll().isEmpty());

        EquipementDTO trouve = equipementService.findById(saved.getId());
        assertEquals("GPS-Service-Test", trouve.getLibelle());

        Long idSupprime = saved.getId();
        equipementService.deleteById(idSupprime);
        assertThrows(ResourceNotFoundException.class,
                () -> equipementService.findById(idSupprime));
    }
}
