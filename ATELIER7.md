# Atelier 7 — Affectation, DTO, MapStruct, SonarLint, Validation

## Partie 1 — Affectation / désaffectation (Spring Data JPA)

Côté propriétaire de chaque association : `Vehicule.agence` et `Employe.agence` (ce sont eux qui portent la clé étrangère ; `Agence.vehicules` / `Agence.employes` sont le côté `mappedBy`).

| Opération | Endpoint | Réponse |
|---|---|---|
| Affecter / transférer un véhicule | `PUT /api/vehicules/{vehiculeId}/affecter/{agenceId}` | `VehiculeDTO` |
| Désaffecter un véhicule | `PUT /api/vehicules/{vehiculeId}/desaffecter` | `VehiculeDTO` |
| Affecter / transférer un employé | `PUT /api/employes/{employeId}/affecter/{agenceId}` | `EmployeDTO` |
| Désaffecter un employé | `PUT /api/employes/{employeId}/desaffecter` | `EmployeDTO` |

Règles :
- véhicule/employé ou agence introuvable → `404` (`ResourceNotFoundException`), rien n'est modifié ;
- désaffecter un véhicule/employé qui n'a pas d'agence → `409` (`BusinessRuleException`) ;
- affecter à une autre agence = transfert (la clé étrangère est simplement remplacée) ;
- les méthodes de service sont `@Transactional` (au niveau de la classe).

Tests : `service/AffectationServiceTest` (intégration, base MySQL configurée).

## Partie 2 / 3 — DTO + MapStruct

- `web/dto` : `AgenceDTO`, `ClientDTO`, `ContratDTO`, `PaiementDTO`, `ReservationRequestDTO`, `ReservationResponseDTO`, `VehiculeDTO` (demandés) + `EmployeDTO`, `EquipementDTO`, `MaintenanceDTO` (pour qu'aucun contrôleur n'expose plus d'entité).
- `web/mapper` : un mapper `@Mapper(componentModel = "spring")` par DTO (`toDto`, `toEntity`, `updateEntity`).
  Les identifiants d'entité s'appellent `idAgence`, `idClient`… : le mapping `id <-> idXxx` est explicite.
- Les services reçoivent/retournent des DTO ; les contrôleurs ne manipulent plus aucune entité JPA.
- `ReservationResponseDTO.tarifEstime` est calculé par `ReservationServiceImpl` avec `TarificationStrategyFactory`.
- `creerReservation(Client)` / `annulerReservation(Reservation)` (démo Observer) sont conservés tels quels : ils ne sont pas exposés en REST.

## Partie 4 — Qualité du code (compte rendu)

> Ces corrections ont été faites à la lecture du code ; l'analyse SonarQube for IDE reste à lancer dans IntelliJ pour produire vos captures.

- Attributs aux noms non conformes (`Employes`, `Vehicules`, `Reservations`, `Maintenances`, `Equipements`) renommés en camelCase (getters Lombok inchangés).
- Attributs d'association à visibilité package (`Agence agence;`…) passés en `private`.
- Imports inutilisés supprimés (`Tag` dans `Contrat`, `JsonIgnore` dans `Vehicule`).
- Méthodes `update` vides (`Vehicule`, `Paiement`, `Maintenance`, `Reservation`) implémentées ; commentaires « à compléter » supprimés.
- Duplication supprimée dans les services (`chargerXxx(id)` privés) et dans `GlobalExceptionHandler` (méthode `reponse(...)`).
- Gestion des `null` : nom complet du client dans `ReservationMapper` null-safe (au lieu d'une expression `java(...)` qui lève une NPE si le client est absent).
- Retour `ResponseEntity<Object>` plutôt que `ResponseEntity<?>` dans `ReservationController`.
- Non modifié volontairement : `Vehicule.categorie` / `statut` sont persistés en ordinal (pas de `@Enumerated`). Passer en `EnumType.STRING` change le schéma en base ; à faire avec une migration.

## Partie 5 — Validation

- `spring-boot-starter-validation` + contraintes Bean Validation sur tous les DTO (messages explicites).
- `exception/GlobalExceptionHandler` (déplacé depuis `web/controller`) gère `MethodArgumentNotValidException` → `400` avec `{champ: message}`, ainsi que `ResourceNotFoundException` (404), `IllegalArgumentException` (400) et `BusinessRuleException` (409).
- `web/validator/ReservationRequestValidator` : début ≥ aujourd'hui, fin > début ; appelé explicitement dans `ReservationController` (POST et PUT).
- Tests : `web/BeanValidationDtoTest`, `web/ReservationRequestValidatorTest`, `web/ReservationControllerTest`.

## Points à connaître

- `POST /paiements` a été ajouté (le contrôleur était en lecture seule) car l'atelier demande de tester le rejet d'un paiement invalide.
- `ReservationServiceImpl.create` est une création minimale (client + véhicule + dates, statut `EN_ATTENTE`) afin que le contrôleur soit testable de bout en bout ; à remplacer quand `ReservationFactory` sera introduite.
- `ReservationRequestDTO` : `dateDebut` est `@FutureOrPresent` (cohérent avec la règle du validateur « aujourd'hui ou futur »), `dateFin` est `@Future`.
- `VehiculeDTO.agenceId` est obligatoire (création et modification) comme demandé ; un véhicule désaffecté se réaffecte via l'endpoint `affecter`.
