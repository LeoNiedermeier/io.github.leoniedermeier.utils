package io.github.leoniedermeier.utils.date;

import static io.github.leoniedermeier.utils.date.VollendetesLebensjahrUtils.lebensjahreVollendetAm;
import static io.github.leoniedermeier.utils.date.VollendetesLebensjahrUtils.vollendeteLebensjahre;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Period;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class VollendetesLebensjahrUtilsTest {

    @Nested
    class VollendeteLebensjahre {

        @ParameterizedTest // (name = "geburtsdatum={0}, datum={1}, expected={2} ")
        @CsvSource({ //
                "2000-02-21,2003-02-20,2", //
                "2000-02-21,2003-02-21,3", //
                "2000-02-21,2003-02-22,3", //
                // Sonderfälle Geburtstag am 29.02 in einem Schaltjahr
                // Zieldatum keinSchaltjahr
                "2000-02-29,2003-02-28,2", //
                "2000-02-29,2003-03-01,3", //
                // Zieldatum Schaltjahr
                "2000-02-29,2004-02-28,3", //
                "2000-02-29,2004-02-29,4", //
                "2000-02-29,2004-03-01,4", //
        })
        void test(LocalDate geburtsdatum, LocalDate datum, int expected) {

            // Description

            // Setup
            // Execute
            int result = vollendeteLebensjahre(geburtsdatum, datum);
            // Verify
            assertEquals(expected, result);
        }
    }

    @Nested
    class LebensjahreVollendetAm {

        @ParameterizedTest // (name = "geburtsdatum={0}, datum={1}, expected={2} ")
        @CsvSource({ //
                "2000-02-21, 3, 2003-02-20", //
                // Sonderfälle Geburtstag am 29.02 in einem Schaltjahr
                "2000-02-29, 3,  2003-02-28", "2000-02-29, 4,  2004-02-28",

                // 1.3:
                "2000-03-01, 3,  2003-02-28", "2000-03-01, 4,  2004-02-29",

        })
        void test(LocalDate geburtsdatum, int lebensjahre, LocalDate expected) {

            // Description

            // Setup
            // Execute
            LocalDate result = lebensjahreVollendetAm(geburtsdatum, lebensjahre);
            // Verify
            assertEquals(expected, result);
        }
    }
}
