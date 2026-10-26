package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion du parc de véhicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de véhicule invalides")
    })
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.create(vehicule);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public Vehicule findById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Lister les véhicules")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules récupérée avec succès")
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public Vehicule update(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
            @RequestBody Vehicule vehicule) {
        return vehiculeService.update(id, vehicule);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public void deleteById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteById(id);
    }
}
