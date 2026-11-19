package service;

import br.edu.alugahub.dto.ItemForm;
import br.edu.alugahub.exception.RegraNegocioException;
import br.edu.alugahub.model.Empresa;
import br.edu.alugahub.model.Item;
import br.edu.alugahub.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock ItemRepository repository;
    @Mock EmpresaService empresas;
    @Mock EstoqueService estoque;
    @InjectMocks ItemService service;

    @Test void cadastraItemComEstoqueEObservacoes() {
        ItemForm form = form();
        form.setObservacoes("220V");
        Empresa empresa = new Empresa();
        when(empresas.buscar(1L)).thenReturn(empresa);
        when(repository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Item item = service.salvar(null, 1L, form);
        assertEquals(5, item.getQuantidadeTotal());
        assertEquals(1, item.getQuantidadeManutencao());
        assertEquals("220V", item.getObservacoes());
        assertSame(empresa, item.getEmpresa());
    }

    @Test void impedeImagemQueNaoSejaFoto() {
        ItemForm form = form();
        form.setImagem(new MockMultipartFile("imagem", "arquivo.pdf", "application/pdf", new byte[]{1,2}));
        when(empresas.buscar(1L)).thenReturn(new Empresa());
        assertThrows(RegraNegocioException.class, () -> service.salvar(null, 1L, form));
        verify(repository, never()).save(any());
    }

    @Test void impedeManutencaoMaiorQueEstoque() {
        ItemForm form = form();
        form.setQuantidadeManutencao(6);
        when(empresas.buscar(1L)).thenReturn(new Empresa());
        assertThrows(RegraNegocioException.class, () -> service.salvar(null, 1L, form));
    }

    private ItemForm form() {
        ItemForm f = new ItemForm();
        f.setNome("Furadeira"); f.setCategoria("Ferramentas");
        f.setQuantidadeTotal(5); f.setQuantidadeManutencao(1);
        return f;
    }
}
