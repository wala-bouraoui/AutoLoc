package tn.esprit.tpautoloc.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.tpautoloc.exception.GlobalExceptionHandler;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.web.controller.ReservationController;
import tn.esprit.tpautoloc.web.dto.ReservationResponseDTO;
import tn.esprit.tpautoloc.web.validator.ReservationRequestValidator;

/**
 * Atelier 7 - Partie 5.6 : le contrôleur rejette (400) ou accepte (201) une réservation.
 * Le service est simulé : seule la couche web et la validation sont exercées.
 */
class ReservationControllerTest {

    private IReservationService service;
    private MockMvc mockMvc;

    @BeforeEach
    void preparer() {
        service = mock(IReservationService.class);
        ReservationController controller =
                new ReservationController(service, new ReservationRequestValidator());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private static String json(String debut, String fin) {
        String d = debut == null ? "null" : "\"" + debut + "\"";
        String f = fin == null ? "null" : "\"" + fin + "\"";
        return "{\"clientId\":1,\"vehiculeId\":2,\"dateDebut\":" + d + ",\"dateFin\":" + f + "}";
    }

    @Test
    void reservationSansDatesEstRejetee() throws Exception {
        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(null, null)))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any());
    }

    @Test
    void reservationDontLaFinPrecedeLeDebutEstRejeteeParLeValidateur() throws Exception {
        LocalDate debut = LocalDate.now().plusDays(5);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(debut.toString(), debut.minusDays(2).toString())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0]").value(
                        "dateFin : La date de fin doit être postérieure à la date de début"));

        verify(service, never()).create(any());
    }

    @Test
    void reservationAvecDatesCoherentesEstCreee() throws Exception {
        LocalDate debut = LocalDate.now().plusDays(1);
        ReservationResponseDTO reponse = ReservationResponseDTO.builder().id(10L).build();
        when(service.create(any())).thenReturn(reponse);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(debut.toString(), debut.plusDays(3).toString())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }
}
