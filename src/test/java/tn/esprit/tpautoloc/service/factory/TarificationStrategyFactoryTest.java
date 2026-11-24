package tn.esprit.tpautoloc.service.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;
import tn.esprit.tpautoloc.service.strategy.TarifLongueDureeStrategy;
import tn.esprit.tpautoloc.service.strategy.TarifStandardStrategy;
import tn.esprit.tpautoloc.service.strategy.TarifWeekendStrategy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TarificationStrategyFactoryTest {

    private TarificationStrategyFactory factory;
    private TarifStandardStrategy standard;
    private TarifWeekendStrategy weekend;
    private TarifLongueDureeStrategy longueDuree;

    @BeforeEach
    void setUp() {
        standard = new TarifStandardStrategy();
        weekend = new TarifWeekendStrategy();
        longueDuree = new TarifLongueDureeStrategy();
        factory = new TarificationStrategyFactory(List.of(standard, weekend, longueDuree));
    }

    private Reservation reservation(LocalDate debut, LocalDate fin) {
        Reservation r = new Reservation();
        r.setDateDebut(debut);
        r.setDateFin(fin);
        return r;
    }

    @Test
    void doitRetournerLongueDureePour7JoursOuPlus() {
        Reservation r = reservation(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 8));

        ITarificationStrategy resultat = factory.resoudre(r);

        assertThat(resultat).isInstanceOf(TarifLongueDureeStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerLongueDureePourExactement7Jours() {
        Reservation r = reservation(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 8));

        assertThat(factory.resoudre(r).getCode()).isEqualTo("LONGUE_DUREE");
    }

    @Test
    void doitRetournerWeekendPourCourtSejourVendredi() {
        Reservation r = reservation(LocalDate.of(2024, 3, 8), LocalDate.of(2024, 3, 10));

        ITarificationStrategy resultat = factory.resoudre(r);

        assertThat(resultat).isInstanceOf(TarifWeekendStrategy.class);
        assertThat(resultat.getCode()).isEqualTo("WEEKEND");
    }

    @Test
    void doitRetournerStandardPourMoyenneDuree() {
        Reservation r = reservation(LocalDate.of(2024, 3, 4), LocalDate.of(2024, 3, 8));

        assertThat(factory.resoudre(r).getCode()).isEqualTo("STANDARD");
    }
}
