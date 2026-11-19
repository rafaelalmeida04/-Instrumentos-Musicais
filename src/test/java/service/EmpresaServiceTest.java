package service;

import br.edu.alugahub.dto.CadastroEmpresaForm;
import br.edu.alugahub.exception.RegraNegocioException;
import br.edu.alugahub.model.Empresa;
import br.edu.alugahub.repository.EmpresaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {
    @Mock EmpresaRepository repository;
    @InjectMocks EmpresaService service;

    @Test void cadastraEmpresaComSenhaCriptografada() {
        CadastroEmpresaForm form = form();
        when(repository.findByEmailIgnoreCase("empresa@exemplo.com")).thenReturn(Optional.empty());
        when(repository.save(any(Empresa.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Empresa empresa = service.cadastrar(form);
        assertEquals("empresa@exemplo.com", empresa.getEmail());
        assertNotEquals("123456", empresa.getSenhaHash());
        assertTrue(new BCryptPasswordEncoder().matches("123456", empresa.getSenhaHash()));
    }
    @Test void impedeEmailDuplicado() {
        CadastroEmpresaForm form = form();
        when(repository.findByEmailIgnoreCase("empresa@exemplo.com")).thenReturn(Optional.of(new Empresa()));
        assertThrows(RegraNegocioException.class, () -> service.cadastrar(form));
        verify(repository, never()).save(any());
    }
    private CadastroEmpresaForm form() {
        CadastroEmpresaForm form = new CadastroEmpresaForm();
        form.setNome("Locadora Exemplo");
        form.setEmail("Empresa@Exemplo.com");
        form.setSenha("123456");
        return form;
    }
}
