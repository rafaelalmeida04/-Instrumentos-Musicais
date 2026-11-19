package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegrasEstoqueTest {
    @Test void calculaSaldoDisponivel() {
        assertEquals(6, RegrasEstoque.disponiveis(10, 2, 2));
    }
    @Test void totalReservadoNaoPodeSuperarTotal() {
        assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.disponiveis(4, 2, 3));
    }
    @Test void quantidadesNegativasSaoInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.disponiveis(-1, 0, 0));
    }
    @Test void aceitaReservaDentroDoSaldo() {
        assertDoesNotThrow(() -> RegrasEstoque.validarReserva(3, 3));
    }
    @Test void rejeitaReservaAcimaDoSaldo() {
        assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarReserva(4, 3));
    }
    @Test void rejeitaReservaZero() {
        assertThrows(IllegalArgumentException.class, () -> RegrasEstoque.validarReserva(0, 3));
    }
}
