package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.service.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Gestion des opérations de maintenance")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Maintenance créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de maintenance invalides")
    })
    public Maintenance create(@RequestBody Maintenance maintenance) {
        return maintenanceService.create(maintenance);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance trouvée"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public Maintenance findById(@Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        return maintenanceService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Lister les maintenances")
    @ApiResponse(responseCode = "200", description = "Liste des maintenances récupérée avec succès")
    public List<Maintenance> findAll() {
        return maintenanceService.findAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance modifiée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public Maintenance update(
            @Parameter(description = "Identifiant de la maintenance") @PathVariable Long id,
            @RequestBody Maintenance maintenance) {
        return maintenanceService.update(id, maintenance);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Maintenance supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public void deleteById(@Parameter(description = "Identifiant de la maintenance") @PathVariable Long id) {
        maintenanceService.deleteById(id);
    }
}
