package service;

import br.edu.alugahub.dto.CheckoutForm;
import br.edu.alugahub.exception.RegraNegocioException;
import br.edu.alugahub.model.Item;
import br.edu.alugahub.model.Locacao;
import br.edu.alugahub.repository.ItemRepository;
import br.edu.alugahub.repository.LocacaoItemRepository;
import br.edu.alugahub.repository.LocacaoRepository;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocacaoServiceTest {
    @Mock LocacaoRepository locacoes;
    @Mock LocacaoItemRepository linhas;
    @Mock ItemRepository itens;
    @Mock EstoqueService estoque;
    @InjectMocks LocacaoService service;

    @Test void reservaDoisItensNoMesmoPedido() {
        Item a = new Item(); a.setNome("Furadeira");
        Item b = new Item(); b.setNome("Projetor");
        when(itens.buscarParaReserva(10L)).thenReturn(Optional.of(a));
        when(itens.buscarParaReserva(11L)).thenReturn(Optional.of(b));
        when(estoque.disponiveis(any(Item.class))).thenReturn(4);
        when(locacoes.save(any(Locacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Map<Long, Integer> carrinho = new LinkedHashMap<>();
        carrinho.put(10L, 2); carrinho.put(11L, 1);
        Locacao pedido = service.finalizar(form(), carrinho);
        assertEquals(2, pedido.getItens().size());
        assertEquals(3, pedido.getItens().stream().mapToInt(l -> l.getQuantidade()).sum());
        verify(locacoes).save(any(Locacao.class));
    }
    @Test void naoReservaEstoqueInsuficiente() {
        Item item = new Item(); item.setNome("Projetor");
        when(itens.buscarParaReserva(10L)).thenReturn(Optional.of(item));
        when(estoque.disponiveis(item)).thenReturn(1);
        assertThrows(RegraNegocioException.class, () -> service.finalizar(form(), Map.of(10L, 2)));
        verify(locacoes, never()).save(any());
    }
    @Test void naoAceitaCarrinhoVazio() {
        assertThrows(RegraNegocioException.class, () -> service.finalizar(form(), Map.of()));
    }
    @Test void naoAceitaPeriodoInvertido() {
        CheckoutForm dados = form();
        dados.setDevolucao(dados.getRetirada().minusDays(1));
        assertThrows(RegraNegocioException.class, () -> service.finalizar(dados, Map.of(10L, 1)));
    }
    private CheckoutForm form() {
        CheckoutForm form = new CheckoutForm();
        form.setNome("Maria Cliente"); form.setEmail("maria@exemplo.com"); form.setTelefone("31999999999");
        form.setRetirada(LocalDate.of(2027, 2, 1)); form.setDevolucao(LocalDate.of(2027, 2, 5));
        return form;
    }
}
