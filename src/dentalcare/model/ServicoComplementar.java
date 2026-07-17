package dentalcare.model;

import java.io.Serializable;

public class ServicoComplementar implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private double preco;

    public ServicoComplementar(String nome, double preco) { this.nome = nome; this.preco = preco; }
    public double getPreco() { return preco; }
}
