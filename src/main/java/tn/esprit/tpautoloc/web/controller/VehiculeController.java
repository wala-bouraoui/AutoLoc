package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion des véhicules de location")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @GetMapping
    @Operation(summary = "Afficher tous les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules")
    public List<VehiculeDTO> findAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule trouvé")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    public VehiculeDTO findById(
            @Parameter(description = "Identifiant du véhicule")
            @PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un véhicule")
    @ApiResponse(responseCode = "201", description = "Véhicule créé")
    @ApiResponse(responseCode = "400", description = "Requête invalide")
    public VehiculeDTO create(@Valid @RequestBody VehiculeDTO dto) {
        return vehiculeService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un véhicule")
    @ApiResponse(responseCode = "200", description = "Véhicule modifié")
    @ApiResponse(responseCode = "400", description = "Requête invalide")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    public VehiculeDTO update(
            @Parameter(description = "Identifiant du véhicule")
            @PathVariable Long id,
            @Valid @RequestBody VehiculeDTO dto) {
        return vehiculeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un véhicule")
    @ApiResponse(responseCode = "204", description = "Véhicule supprimé")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    public void deleteById(
            @Parameter(description = "Identifiant du véhicule")
            @PathVariable Long id) {
        vehiculeService.deleteById(id);
    }

    @PutMapping("/{vehiculeId}/affecter/{agenceId}")
    @Operation(summary = "Affecter un véhicule à une agence (ou le transférer vers une autre agence)")
    @ApiResponse(responseCode = "200", description = "Véhicule affecté")
    @ApiResponse(responseCode = "404", description = "Véhicule ou agence introuvable")
    public VehiculeDTO affecter(
            @Parameter(description = "Identifiant du véhicule")
            @PathVariable Long vehiculeId,
            @Parameter(description = "Identifiant de l'agence")
            @PathVariable Long agenceId) {
        return vehiculeService.affecterVehiculeAAgence(vehiculeId, agenceId);
    }

    @PutMapping("/{vehiculeId}/desaffecter")
    @Operation(summary = "Désaffecter un véhicule de son agence")
    @ApiResponse(responseCode = "200", description = "Véhicule désaffecté")
    @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    @ApiResponse(responseCode = "409", description = "Le véhicule n'est rattaché à aucune agence")
    public VehiculeDTO desaffecter(
            @Parameter(description = "Identifiant du véhicule")
            @PathVariable Long vehiculeId) {
        return vehiculeService.desaffecterVehiculeDeAgence(vehiculeId);
    }
}