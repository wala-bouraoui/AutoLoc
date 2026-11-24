package tn.esprit.tpautoloc.service.factory;

import org.springframework.stereotype.Component;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.service.strategy.ITarificationStrategy;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Selectionne dynamiquement la ITarificationStrategy adaptee a une reservation
 * (duree du sejour). Toutes les implementations Spring de ITarificationStrategy
 * sont injectees automatiquement (Map<code, strategy>).
 */
@Component
public class TarificationStrategyFactory {

    private static final long SEUIL_LONGUE_DUREE = 7;
    private static final long DUREE_MAX_WEEKEND = 3;
    private static final Set<DayOfWeek> JOURS_WEEKEND =
            EnumSet.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private final Map<String, ITarificationStrategy> strategies;

    public TarificationStrategyFactory(List<ITarificationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ITarificationStrategy::getCode, Function.identity()));
    }

    public ITarificationStrategy resoudre(Reservation reservation) {
        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());

        if (jours >= SEUIL_LONGUE_DUREE) {
            return strategies.get("LONGUE_DUREE");
        }
        if (estWeekend(reservation)) {
            return strategies.get("WEEKEND");
        }
        return strategies.get("STANDARD");
    }

    private boolean estWeekend(Reservation reservation) {
        long jours = ChronoUnit.DAYS.between(reservation.getDateDebut(), reservation.getDateFin());
        DayOfWeek debut = reservation.getDateDebut().getDayOfWeek();

        return jours <= DUREE_MAX_WEEKEND && JOURS_WEEKEND.contains(debut);
    }
}
