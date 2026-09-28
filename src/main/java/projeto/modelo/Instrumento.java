package projeto.modelo;

public class Instrumento {

    private String nome;
    private String marca;
    private int anoFabricacao;

    public Instrumento(String nome, String marca, int anoFabricacao) {
        this.nome = nome;
        this.marca = marca;
        this.anoFabricacao = anoFabricacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    // Método que será sobrescrito pelas subclasses
    public void tocar() {
        System.out.println("O instrumento " + nome + " está emitindo um som genérico.");
    }

    // Método que será sobrescrito pelas subclasses
    public void afinar() {
        System.out.println("Afinando o instrumento " + nome + ".");
    }

    public void exibirInformacoes() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Instrumento [nome=" + nome + ", marca=" + marca + ", anoFabricacao=" + anoFabricacao + "]";
    }
}