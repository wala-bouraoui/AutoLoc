package tn.esprit.tpautoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpautoloc.service.IEmployeService;
import tn.esprit.tpautoloc.web.dto.EmployeDTO;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(name = "Employés", description = "Gestion des employés des agences")
public class EmployeController {

    private final IEmployeService employeService;

    @GetMapping
    @Operation(summary = "Afficher tous les employés")
    @ApiResponse(responseCode = "200", description = "Liste des employés")
    public List<EmployeDTO> findAll() {
        return employeService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un employé")
    @ApiResponse(responseCode = "200", description = "Employé trouvé")
    @ApiResponse(responseCode = "404", description = "Employé introuvable")
    public EmployeDTO findById(
            @Parameter(description = "Identifiant de l'employé")
            @PathVariable Long id) {
        return employeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un employé")
    @ApiResponse(responseCode = "201", description = "Employé créé")
    @ApiResponse(responseCode = "400", description = "Requête invalide")
    public EmployeDTO create(@Valid @RequestBody EmployeDTO dto) {
        return employeService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un employé")
    @ApiResponse(responseCode = "200", description = "Employé modifié")
    @ApiResponse(responseCode = "400", description = "Requête invalide")
    @ApiResponse(responseCode = "404", description = "Employé introuvable")
    public EmployeDTO update(
            @Parameter(description = "Identifiant de l'employé")
            @PathVariable Long id,
            @Valid @RequestBody EmployeDTO dto) {
        return employeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un employé")
    @ApiResponse(responseCode = "204", description = "Employé supprimé")
    @ApiResponse(responseCode = "404", description = "Employé introuvable")
    public void deleteById(
            @Parameter(description = "Identifiant de l'employé")
            @PathVariable Long id) {
        employeService.deleteById(id);
    }

    @PutMapping("/{employeId}/affecter/{agenceId}")
    @Operation(summary = "Affecter un employé à une agence (ou le transférer vers une autre agence)")
    @ApiResponse(responseCode = "200", description = "Employé affecté")
    @ApiResponse(responseCode = "404", description = "Employé ou agence introuvable")
    public EmployeDTO affecter(
            @Parameter(description = "Identifiant de l'employé")
            @PathVariable Long employeId,
            @Parameter(description = "Identifiant de l'agence")
            @PathVariable Long agenceId) {
        return employeService.affecterEmployeAAgence(employeId, agenceId);
    }

    @PutMapping("/{employeId}/desaffecter")
    @Operation(summary = "Désaffecter un employé de son agence")
    @ApiResponse(responseCode = "200", description = "Employé désaffecté")
    @ApiResponse(responseCode = "404", description = "Employé introuvable")
    @ApiResponse(responseCode = "409", description = "L'employé n'est rattaché à aucune agence")
    public EmployeDTO desaffecter(
            @Parameter(description = "Identifiant de l'employé")
            @PathVariable Long employeId) {
        return employeService.desaffecterEmployeDeAgence(employeId);
    }
}
