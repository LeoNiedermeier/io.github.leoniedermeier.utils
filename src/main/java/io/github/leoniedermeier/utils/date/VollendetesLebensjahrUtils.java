package io.github.leoniedermeier.utils.date;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

public class VollendetesLebensjahrUtils {

    /**
     * Liefert das Datum, an dem ein gegebenes Lebensalter vollendet ist. Siehe
     * <a href=
     * "https://de.wikipedia.org/wiki/Lebensalter">https://de.wikipedia.org/wiki/Lebensalter</a>
     * 
     * @param geburtsdatum Das Geburtsdatum.
     * @param lebensjahre  Anzahl der Lebensjahre.
     * @return Datum, an dem das gebenene Lebensalte um 24:00 vollendet ist.
     */
    public static LocalDate lebensjahreVollendetAm(LocalDate geburtsdatum, int lebensjahre) {
        if (geburtsdatum.getMonth() == Month.FEBRUARY && geburtsdatum.getDayOfMonth() == 29
                && !geburtsdatum.plusYears(lebensjahre).isLeapYear()) {
            // Für die am 29. Februar Geborenen gilt gemäß § 188 Abs. 3 BGB in
            // Nichtschaltjahren die Vollendung des 28. Februar als Ablauftag (siehe Frist),
            // sie stehen daher in Nichtschaltjahren den an einem 1. März Geborenen
            // gleich.[1]
            //
            return geburtsdatum.plusYears(lebensjahre);
        } else {
            return geburtsdatum.plusYears(lebensjahre).minusDays(1);
        }
    }

    /**
     * Vollendete Lebensjahre am Zieldatum um 24:00
     * 
     * Siehe <a href=
     * "https://de.wikipedia.org/wiki/Lebensalter">https://de.wikipedia.org/wiki/Lebensalter</a>
     * 
     * 
     * @param geburtsdatum Das Geburtsdatum.
     * @param datum        Das Datum ( um 00:00), zu dem die Anzahl der vollendeten
     *                     Lebensjahre bestimmt werden sollte.
     * @return Die Anzahl der vollendeten Lebensjahre
     */
    public static int vollendeteLebensjahre(LocalDate geburtsdatum, LocalDate datum) {
        return Period.between(geburtsdatum, datum).getYears();
    }

    private VollendetesLebensjahrUtils() {
        throw new AssertionError("No VollendetesLebensjahrUtils instances for you!");
    }
}
