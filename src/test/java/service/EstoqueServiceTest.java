package service;

import br.edu.alugahub.model.Item;
import br.edu.alugahub.model.StatusLocacao;
import br.edu.alugahub.repository.LocacaoItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {
    @Mock LocacaoItemRepository repository;
    @InjectMocks EstoqueService estoque;

    @Test void combinaItensAlugadosEManutencao() {
        Item item = new Item();
        item.setQuantidadeTotal(12);
        item.setQuantidadeManutencao(4);
        when(repository.somarQuantidade(null, StatusLocacao.ATIVA)).thenReturn(3L);
        assertEquals(5, estoque.disponiveis(item));
        assertEquals(3, estoque.criarCard(item).alugados());
    }
}
