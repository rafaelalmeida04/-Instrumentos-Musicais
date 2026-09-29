package projeto.modelo;

public class Violao extends Instrumento {

    private String tipoCorda;

    public Violao(String nome, String marca, int anoFabricacao, String tipoCorda) {
        super(nome, marca, anoFabricacao);
        this.tipoCorda = tipoCorda;
    }

    public String getTipoCorda() {
        return tipoCorda;
    }

    public void setTipoCorda(String tipoCorda) {
        this.tipoCorda = tipoCorda;
    }

    @Override
    public void tocar() {
        System.out.println("O violão " + getNome() + " está sendo dedilhado: plim, plim, plim!");


    @Override
    public void afinar() {
        System.out.println("Afinando as 6 cordas de " + tipoCorda + " do violão " + getNome() + " (E A D G B E).");
    }

    @Override
    public String toString() {
        return "Violao [nome=" + getNome() + ", marca=" + getMarca()
                + ", anoFabricacao=" + getAnoFabricacao() + ", tipoCorda=" + tipoCorda + "]";
    }
}