package bdd;

import br.edu.alugahub.dto.CadastroEmpresaForm;
import br.edu.alugahub.dto.CheckoutForm;
import br.edu.alugahub.dto.ItemForm;
import br.edu.alugahub.exception.RegraNegocioException;
import br.edu.alugahub.model.Empresa;
import br.edu.alugahub.model.Item;
import br.edu.alugahub.model.Locacao;
import br.edu.alugahub.repository.EmpresaRepository;
import br.edu.alugahub.repository.ItemRepository;
import br.edu.alugahub.repository.LocacaoItemRepository;
import br.edu.alugahub.repository.LocacaoRepository;
import br.edu.alugahub.service.EmpresaService;
import br.edu.alugahub.service.EstoqueService;
import br.edu.alugahub.service.ItemService;
import br.edu.alugahub.service.LocacaoService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

public class PassosLocacao {
    @Autowired EmpresaService empresas;
    @Autowired ItemService itens;
    @Autowired EstoqueService estoque;
    @Autowired LocacaoService locacoes;
    @Autowired EmpresaRepository empresaRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired LocacaoRepository locacaoRepository;
    @Autowired LocacaoItemRepository linhaRepository;

    private Empresa empresa;
    private Item item;
    private Locacao locacao;
    private Exception erro;
    private int estoqueAnterior;

    @Before
    public void limparBancoEntreCenarios() {
        linhaRepository.deleteAll();
        locacaoRepository.deleteAll();
        itemRepository.deleteAll();
        empresaRepository.deleteAll();
    }

    @Given("que existe uma empresa cadastrada")
    public void existeEmpresa() {
        CadastroEmpresaForm form = new CadastroEmpresaForm();
        form.setNome("Locadora Centro"); form.setEmail("contato@centro.com"); form.setSenha("senha123");
        empresa = empresas.cadastrar(form);
    }

    @Then("a empresa pode acessar seu cadastro")
    public void empresaCadastrada() {
        assertEquals("Locadora Centro", empresas.buscar(empresa.getId()).getNome());
    }

    @When("a empresa cadastra um item com {int} unidades e {int} em manutenção")
    public void cadastrarItem(int total, int manutencao) {
        item = itens.salvar(null, empresa.getId(), dadosItem("Furadeira", total, manutencao));
    }

    @Then("o estoque mostra {int} disponíveis")
    public void estoqueEsperado(int esperado) {
        Item atualizado = itens.buscar(item.getId());
        assertEquals(esperado, estoque.disponiveis(atualizado));
    }

    @When("o cliente solicita {int} unidades")
    public void solicitar(int quantidade) {
        locacao = locacoes.finalizar(cliente(), Map.of(item.getId(), quantidade));
    }

    @Then("a locação contém {int} unidade ou unidades")
    public void possuiQuantidade(int esperado) {
        assertEquals(esperado, locacao.getItens().get(0).getQuantidade());
    }

    @When("o cliente tenta solicitar {int} unidades")
    public void solicitarDemais(int quantidade) {
        erro = assertThrows(RegraNegocioException.class,
                () -> locacoes.finalizar(cliente(), Map.of(item.getId(), quantidade)));
    }

    @Then("a solicitação é recusada por falta de estoque")
    public void recusa() {
        assertNotNull(erro);
        assertTrue(erro.getMessage().contains("indisponível"));
    }

    @When("a empresa registra a devolução")
    public void devolver() {
        Long linhaId = locacao.getItens().get(0).getId();
        locacoes.registrarDevolucao(linhaId, empresa.getId());
    }

    @When("a empresa altera a manutenção para {int} unidades")
    public void alterarManutencao(int manutencao) {
        item = itens.salvar(item.getId(), empresa.getId(),
                dadosItem(item.getNome(), item.getQuantidadeTotal(), manutencao));
    }

    @When("o cliente solicita itens de duas empresas")
    public void duasEmpresas() {
        CadastroEmpresaForm f = new CadastroEmpresaForm();
        f.setNome("Locadora Norte"); f.setEmail("norte@teste.com"); f.setSenha("senha123");
        Empresa outra = empresas.cadastrar(f);
        Item outro = itens.salvar(null, outra.getId(), dadosItem("Projetor", 2, 0));
        Map<Long,Integer> carrinho = new LinkedHashMap<>();
        carrinho.put(item.getId(), 1);
        carrinho.put(outro.getId(), 1);
        locacao = locacoes.finalizar(cliente(), carrinho);
    }

    @Then("a locação apresenta {int} itens diferentes")
    public void numeroItens(int quantidade) { assertEquals(quantidade, locacao.getItens().size()); }

    @When("outra empresa tenta alterar o item")
    public void edicaoIndevida() {
        CadastroEmpresaForm f = new CadastroEmpresaForm();
        f.setNome("Concorrente"); f.setEmail("concorrente@teste.com"); f.setSenha("senha123");
        Empresa outra = empresas.cadastrar(f);
        erro = assertThrows(RegraNegocioException.class,
                () -> itens.salvar(item.getId(), outra.getId(), dadosItem("Modificado", 3, 0)));
    }

    @Then("a alteração é negada")
    public void negada() {
        assertNotNull(erro);
        assertEquals("Furadeira", itens.buscar(item.getId()).getNome());
    }

    @When("se tenta cadastrar outra empresa com o mesmo e-mail")
    public void emailRepetido() {
        CadastroEmpresaForm f = new CadastroEmpresaForm();
        f.setNome("Outra locadora"); f.setEmail("CONTATO@CENTRO.COM"); f.setSenha("senha123");
        erro = assertThrows(RegraNegocioException.class, () -> empresas.cadastrar(f));
    }

    @Then("o e-mail duplicado é rejeitado")
    public void rejeitaEmail() { assertNotNull(erro); }

    private ItemForm dadosItem(String nome, int total, int manutencao) {
        ItemForm form = new ItemForm();
        form.setNome(nome); form.setCategoria("Ferramentas");
        form.setDescricao("Equipamento pronto para aluguel");
        form.setObservacoes("Retirar na loja");
        form.setQuantidadeTotal(total); form.setQuantidadeManutencao(manutencao);
        return form;
    }

    private CheckoutForm cliente() {
        CheckoutForm form = new CheckoutForm();
        form.setNome("Ana Silva"); form.setEmail("ana@teste.com"); form.setTelefone("31999998888");
        form.setRetirada(LocalDate.now().plusDays(1));
        form.setDevolucao(LocalDate.now().plusDays(5));
        return form;
    }
}
