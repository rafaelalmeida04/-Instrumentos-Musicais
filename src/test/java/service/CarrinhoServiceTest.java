package service;

import br.edu.alugahub.exception.RegraNegocioException;
import br.edu.alugahub.model.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {
    @Mock ItemService itens;
    @Mock EstoqueService estoque;
    @InjectMocks CarrinhoService carrinho;

    @Test void acumulaQuantidadesDoMesmoItem() {
        MockHttpSession session = new MockHttpSession();
        Item item = new Item();
        when(itens.buscar(10L)).thenReturn(item);
        when(estoque.disponiveis(item)).thenReturn(4);
        carrinho.adicionar(session, 10L, 1);
        carrinho.adicionar(session, 10L, 2);
        assertEquals(3, carrinho.obter(session).get(10L));
    }

    @Test void naoAdicionaQuantidadeAcimaDoSaldo() {
        MockHttpSession session = new MockHttpSession();
        Item item = new Item();
        when(itens.buscar(10L)).thenReturn(item);
        when(estoque.disponiveis(item)).thenReturn(1);
        assertThrows(RegraNegocioException.class, () -> carrinho.adicionar(session, 10L, 2));
        assertTrue(carrinho.obter(session).isEmpty());
    }
}
