package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;



import tn.esprit.tpautoloc.domain.*;
import tn.esprit.tpautoloc.repository.*;
import tn.esprit.tpautoloc.service.*;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;


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
    //                 TESTS SERVICE
    // =====================================================

    // =====================================================
    //  SERVICE - PARTIE 2 : AGENCE
    // =====================================================
    @Test
    void testAgenceService() {
        System.out.println("========== TEST AGENCE (SERVICE) ==========");

        int avant = agenceService.findAll().size();

        Agence agence = new Agence();
        agence.setNom("Agence Centre");
        agence.setVille("Tunis");
        agence.setAdresse("Avenue Habib Bourguiba");
        agence.setTelephone("71111222");

        // create()
        Agence saved = agenceService.create(agence);
        System.out.println("Agence enregistree : id=" + saved.getIdAgence()
                + ", nom=" + saved.getNom() + ", ville=" + saved.getVille());
        assertNotNull(saved);
        assertNotNull(saved.getIdAgence());
        assertEquals("Agence Centre", saved.getNom());

        // findAll()
        List<Agence> agences = agenceService.findAll();
        System.out.println("Liste des agences (" + agences.size() + ") :");
        agences.forEach(a -> System.out.println("  - " + a.getIdAgence() + " | " + a.getNom()));
        assertFalse(agences.isEmpty());

        // findById()
        Agence trouvee = agenceService.findById(saved.getIdAgence());
        System.out.println("findById -> " + trouvee.getNom() + " (" + trouvee.getVille() + ")");
        assertNotNull(trouvee);
        assertEquals(saved.getIdAgence(), trouvee.getIdAgence());

        // taille de la liste (remplace count())
        System.out.println("Nombre d'agences : " + agenceService.findAll().size());
        assertEquals(avant + 1, agenceService.findAll().size());

        // update()
        Agence modif = new Agence();
        modif.setNom("Agence Centre Modifiee");
        modif.setVille("Sfax");
        modif.setAdresse(saved.getAdresse());
        modif.setTelephone(saved.getTelephone());
        Agence maj = agenceService.update(saved.getIdAgence(), modif);
        System.out.println("Agence modifiee : " + maj.getNom() + " / " + maj.getVille());
        assertEquals("Agence Centre Modifiee", maj.getNom());
        assertEquals("Sfax", maj.getVille());

        // deleteById() + verification
        agenceService.deleteById(saved.getIdAgence());
        Long idSupprime = saved.getIdAgence();
        assertThrows(ResourceNotFoundException.class,
                () -> agenceService.findById(idSupprime));
        assertEquals(avant, agenceService.findAll().size());
        System.out.println("Agence supprimee : elle n'existe plus.");
    }

    // =====================================================
    //  SERVICE - PARTIE 3 : CLIENT
    // =====================================================
    @Test
    void testClientService() {
        System.out.println("========== TEST CLIENT (SERVICE) ==========");

        int avant = clientService.findAll().size();

        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Sami");
        c1.setEmail("sami.service@example.com");
        c1.setTelephone("20111222");
        c1.setNumPermis("PERMIS-SVC-001");
        c1.setDateInscription(LocalDate.now());

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Amira");
        c2.setEmail("amira.service@example.com");
        c2.setTelephone("22111333");
        c2.setNumPermis("PERMIS-SVC-002");
        c2.setDateInscription(LocalDate.now());

        // create() x2
        Client s1 = clientService.create(c1);
        Client s2 = clientService.create(c2);
        System.out.println("Clients enregistres : " + s1.getIdClient() + " et " + s2.getIdClient());
        assertNotNull(s1.getIdClient());
        assertNotNull(s2.getIdClient());
        assertNotEquals(s1.getIdClient(), s2.getIdClient());

        // findAll()
        List<Client> clients = clientService.findAll();
        System.out.println("Liste des clients (" + clients.size() + ") :");
        clients.forEach(c -> System.out.println("  - " + c.getIdClient() + " | "
                + c.getNom() + " " + c.getPrenom() + " | " + c.getEmail()));
        assertTrue(clients.size() >= 2);

        // findById()
        Client trouve = clientService.findById(s1.getIdClient());
        System.out.println("findById -> " + trouve.getNom() + " " + trouve.getPrenom());
        assertNotNull(trouve);
        assertEquals("Sami", trouve.getPrenom());

        // taille de la liste (remplace count())
        System.out.println("Nombre de clients : " + clientService.findAll().size());
        assertEquals(avant + 2, clientService.findAll().size());

        // update()
        Client modif = new Client();
        modif.setNom(s1.getNom());
        modif.setPrenom(s1.getPrenom());
        modif.setEmail("sami.nouveau@example.com");
        modif.setTelephone("20999888");
        Client maj = clientService.update(s1.getIdClient(), modif);
        System.out.println("Client modifie : " + maj.getEmail());
        assertEquals("sami.nouveau@example.com", maj.getEmail());

        // deleteById() + verification
        clientService.deleteById(s1.getIdClient());
        Long idSupprime = s1.getIdClient();
        assertThrows(ResourceNotFoundException.class,
                () -> clientService.findById(idSupprime));
        System.out.println("Client " + s1.getIdClient() + " supprime : il n'existe plus.");

        // nettoyage du second client
        clientService.deleteById(s2.getIdClient());
        assertEquals(avant, clientService.findAll().size());
        System.out.println("Second client supprime (nettoyage).");
    }

    // =====================================================
    //  SERVICE - PARTIE 4 : VEHICULE
    // =====================================================
    @Test
    void testVehiculeService() {
        System.out.println("========== TEST VEHICULE (SERVICE) ==========");

        int avant = vehiculeService.findAll().size();

        Vehicule v = new Vehicule();
        v.setImmatriculation("SVC-123-TN");
        v.setMarque("Toyota");
        v.setModele("Yaris");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("120.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);

        // create()
        Vehicule saved = vehiculeService.create(v);
        System.out.println("Vehicule enregistre : id=" + saved.getIdVehicule()
                + ", " + saved.getMarque() + " " + saved.getModele());
        assertNotNull(saved.getIdVehicule());

        // findAll()
        List<Vehicule> vehicules = vehiculeService.findAll();
        System.out.println("Liste des vehicules (" + vehicules.size() + ") :");
        vehicules.forEach(x -> System.out.println("  - " + x.getIdVehicule() + " | "
                + x.getImmatriculation() + " | " + x.getMarque() + " " + x.getModele()));
        assertFalse(vehicules.isEmpty());

        // findById()
        Vehicule trouve = vehiculeService.findById(saved.getIdVehicule());
        System.out.println("findById -> " + trouve.getImmatriculation());
        assertNotNull(trouve);
        assertEquals("SVC-123-TN", trouve.getImmatriculation());

        // taille de la liste (remplace count())
        System.out.println("Nombre de vehicules : " + vehiculeService.findAll().size());
        assertEquals(avant + 1, vehiculeService.findAll().size());
        // deleteById() + verification
        vehiculeService.deleteById(saved.getIdVehicule());
        Long idSupprime = saved.getIdVehicule();
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.findById(idSupprime));
        assertEquals(avant, vehiculeService.findAll().size());
        System.out.println("Vehicule supprime : il n'existe plus.");

    }

    // =====================================================
    //  SERVICE - PARTIE 5 : EXERCICE AUTONOME (Employe, Contrat, Equipement)
    // =====================================================
    @Test
    void testEmployeService() {
        System.out.println("========== TEST EMPLOYE (SERVICE) ==========");

        Agence agence = new Agence();
        agence.setNom("Agence Employe Test");
        agence.setVille("Tunis");
        agence = agenceService.create(agence);

        Employe e = new Employe();
        e.setNom("Mansour");
        e.setPrenom("Yassine");
        e.setRole(RoleEmploye.AGENT);
        e.setAgence(agence);

        Employe saved = employeService.create(e);
        System.out.println("Employe enregistre : id=" + saved.getIdEmploye());
        assertNotNull(saved.getIdEmploye());

        System.out.println("Liste des employes : " + employeService.findAll().size());
        assertFalse(employeService.findAll().isEmpty());

        Employe trouve = employeService.findById(saved.getIdEmploye());
        assertEquals("Mansour", trouve.getNom());
        assertNotNull(employeService.findById(saved.getIdEmploye()));
        System.out.println("findById OK | count = " + employeService.findAll().size());

        employeService.deleteById(saved.getIdEmploye());
        Long idSupprime = saved.getIdEmploye();
        assertThrows(ResourceNotFoundException.class,
                () -> employeService.findById(idSupprime));
        System.out.println("Employe supprime : findById leve ResourceNotFoundException");

        agenceService.deleteById(agence.getIdAgence());
    }

    @Test
    void testContratService() {
        System.out.println("========== TEST CONTRAT (SERVICE) ==========");

        Contrat c = new Contrat();
        c.setDateSignature(LocalDate.now());
        c.setMontantTotal(new BigDecimal("500.00"));
        c.setValide(true);

        Contrat saved = contratService.create(c);
        System.out.println("Contrat enregistre : id=" + saved.getIdContrat());
        assertNotNull(saved.getIdContrat());

        System.out.println("Liste des contrats : " + contratService.findAll().size());
        assertFalse(contratService.findAll().isEmpty());

        Contrat trouve = contratService.findById(saved.getIdContrat());
        assertEquals(0, new BigDecimal("500.00").compareTo(trouve.getMontantTotal()));
        assertNotNull(contratService.findById(saved.getIdContrat()));
        System.out.println("findById OK | count = " + contratService.findAll().size());

        contratService.deleteById(saved.getIdContrat());
        Long idSupprime = saved.getIdContrat();
        assertThrows(ResourceNotFoundException.class,
                () -> contratService.findById(idSupprime));
        System.out.println("Contrat supprime : findById leve ResourceNotFoundException");
    }

    @Test
    void testEquipementService() {
        System.out.println("========== TEST EQUIPEMENT (SERVICE) ==========");

        Equipement eq = new Equipement();
        eq.setLibelle("GPS-Service-Test");

        Equipement saved = equipementService.create(eq);
        System.out.println("Equipement enregistre : id=" + saved.getIdEquipement());
        assertNotNull(saved.getIdEquipement());

        System.out.println("Liste des equipements : " + equipementService.findAll().size());
        assertFalse(equipementService.findAll().isEmpty());

        Equipement trouve = equipementService.findById(saved.getIdEquipement());
        assertEquals("GPS-Service-Test", trouve.getLibelle());
        assertNotNull(equipementService.findById(saved.getIdEquipement()));
        System.out.println("findById OK | count = " + equipementService.findAll().size());
        equipementService.deleteById(saved.getIdEquipement());
        Long idSupprime = saved.getIdEquipement();
        assertThrows(ResourceNotFoundException.class,
                () -> equipementService.findById(idSupprime));
        System.out.println("Equipement supprime : findById leve ResourceNotFoundException");

    }
}



