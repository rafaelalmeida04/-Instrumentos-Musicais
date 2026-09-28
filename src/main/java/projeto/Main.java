package projeto;

import projeto.modelo.Instrumento;

public class Main {

    public static void main(String[] args) {
        Instrumento instrumento = new Instrumento("Instrumento Genérico", "Sem marca", 2024);

        instrumento.exibirInformacoes();
        instrumento.afinar();
        instrumento.tocar();
    }
}