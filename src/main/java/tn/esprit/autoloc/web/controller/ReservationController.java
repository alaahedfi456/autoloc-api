package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.IReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Réservations", description = "Gestion des réservations")
public class ReservationController {

    private final IReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de réservation invalides")
    })
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.create(reservation);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    public Reservation findById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return reservationService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Lister les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations récupérée avec succès")
    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation modifiée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    public Reservation update(
            @Parameter(description = "Identifiant de la réservation") @PathVariable Long id,
            @RequestBody Reservation reservation) {
        return reservationService.update(id, reservation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une réservation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Réservation supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    public void deleteById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        reservationService.deleteById(id);
    }
}
