package tn.esprit.autoloc.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.service.IPaiementService;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class Atelier5ControllerTests {

    private MockMvc mockMvcPaiement;
    private MockMvc mockMvcContrat;

    @Mock
    private IPaiementService paiementService;

    @Mock
    private IContratService contratService;

    @BeforeEach
    void setUp() {
        mockMvcPaiement = MockMvcBuilders
                .standaloneSetup(new PaiementController(paiementService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mockMvcContrat = MockMvcBuilders
                .standaloneSetup(new ContratController(contratService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /paiements/9999 doit renvoyer HTTP 404 avec JSON statut, message, horodatage")
    void testGetPaiementInexistant_Renvoie404() throws Exception {
        when(paiementService.findById(9999L))
                .thenThrow(new ResourceNotFoundException("Paiement", 9999L));

        mockMvcPaiement.perform(get("/paiements/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statut", is(404)))
                .andExpect(jsonPath("$.message", containsString("Paiement introuvable avec l'identifiant 9999")))
                .andExpect(jsonPath("$.horodatage", notNullValue()));
    }

    @Test
    @DisplayName("POST /paiements doit renvoyer HTTP 405 Method Not Allowed")
    void testPostPaiement_Renvoie405() throws Exception {
        mockMvcPaiement.perform(post("/paiements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"montant\": 100.0}"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.statut", is(405)))
                .andExpect(jsonPath("$.horodatage", notNullValue()));
    }

    @Test
    @DisplayName("POST /contrats avec IllegalArgumentException doit renvoyer HTTP 400 Bad Request")
    void testPostContratInvalide_Renvoie400() throws Exception {
        when(contratService.create(any()))
                .thenThrow(new IllegalArgumentException("Le montant total du contrat ne peut pas être négatif"));

        String payload = "{\"dateSignature\":\"2026-11-02\",\"montantTotal\":-50.00,\"valide\":true}";

        mockMvcContrat.perform(post("/contrats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statut", is(400)))
                .andExpect(jsonPath("$.message", containsString("Le montant total du contrat ne peut pas être négatif")))
                .andExpect(jsonPath("$.horodatage", notNullValue()));
    }

    @Test
    @DisplayName("GET /contrats/9999 avec ResourceNotFoundException doit renvoyer HTTP 404")
    void testGetContratInexistant_Renvoie404() throws Exception {
        when(contratService.findById(9999L))
                .thenThrow(new ResourceNotFoundException("Contrat", 9999L));

        mockMvcContrat.perform(get("/contrats/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statut", is(404)))
                .andExpect(jsonPath("$.message", containsString("Contrat introuvable avec l'identifiant 9999")));
    }
}
