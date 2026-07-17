package dentalcare.model;

import java.io.Serializable;

public class ConsultaTipo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private double precoBase;

    public ConsultaTipo(String nome, double precoBase) { this.nome = nome; this.precoBase = precoBase; }
    public String getNome() { return nome; }
    public double getPrecoBase() { return precoBase; }
}
