package projeto.modelo;

public class Piano extends Instrumento {

    private int numeroTeclas;

    public Piano(String nome, String marca, int anoFabricacao, int numeroTeclas) {
        super(nome, marca, anoFabricacao);
        this.numeroTeclas = numeroTeclas;
    }

    public int getNumeroTeclas() {
        return numeroTeclas;
    }

    public void setNumeroTeclas(int numeroTeclas) {
        this.numeroTeclas = numeroTeclas;
    }

    @Override
    public void tocar() {
        System.out.println("O piano " + getNome() + " está tocando uma melodia: dó, ré, mi, fá, sol!");
    }

    @Override
    public void afinar() {
        System.out.println("O afinador está ajustando as cordas das " + numeroTeclas + " teclas do piano " + getNome() + ".");
    }

    @Override
    public String toString() {
        return "Piano [nome=" + getNome() + ", marca=" + getMarca()
                + ", anoFabricacao=" + getAnoFabricacao() + ", numeroTeclas=" + numeroTeclas + "]";
    }
}