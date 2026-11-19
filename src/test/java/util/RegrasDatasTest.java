package util;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegrasDatasTest {
    @Test void periodoValido() {
        assertDoesNotThrow(() -> RegrasDatas.validarPeriodo(LocalDate.of(2027, 1, 10), LocalDate.of(2027, 1, 15)));
    }
    @Test void rejeitaDevolucaoAnterior() {
        assertThrows(IllegalArgumentException.class, () -> RegrasDatas.validarPeriodo(LocalDate.of(2027, 1, 15), LocalDate.of(2027, 1, 10)));
    }
    @Test void rejeitaDataAusente() {
        assertThrows(IllegalArgumentException.class, () -> RegrasDatas.validarPeriodo(null, LocalDate.of(2027, 1, 10)));
    }
}
