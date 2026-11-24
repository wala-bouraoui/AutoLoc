package tn.esprit.tpautoloc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.StatutReservation;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test du pattern Observer : les logs EMAIL et SMS doivent apparaître
 * à la création et à l'annulation d'une réservation.
 */
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class ReservationServiceTest {

    @Autowired
    private IReservationService reservationService;

    private Client nouveauClient() {
        Client client = new Client();
        client.setNom("Ben Ali");
        client.setPrenom("Sami");
        client.setEmail("sami.observer@example.com");
        client.setTelephone("20111222");
        return client;
    }

    @Test
    void creerReservationDoitNotifierEmailEtSms(CapturedOutput output) {
        Client client = nouveauClient();

        Reservation reservation = reservationService.creerReservation(client);

        assertThat(reservation.getStatut()).isEqualTo(StatutReservation.CONFIRMEE);
        assertThat(output.getOut())
                .contains("[EMAIL] Confirmation envoyée au client sami.observer@example.com")
                .contains("[SMS] Confirmation envoyée au 20111222");
    }

    @Test
    void annulerReservationDoitNotifierEmailEtSms(CapturedOutput output) {
        Reservation reservation = reservationService.creerReservation(nouveauClient());

        Reservation annulee = reservationService.annulerReservation(reservation);

        assertThat(annulee.getStatut()).isEqualTo(StatutReservation.ANNULEE);
        assertThat(output.getOut())
                .contains("[EMAIL] Annulation notifiée au client sami.observer@example.com")
                .contains("[SMS] Annulation notifiée au 20111222");
    }
}
