package dentalcare.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String morada;
    private String localidade;
    private String telefone;
    private DonoEmpresa dono;
    private boolean ativa = true;
    private final List<Consultorio> consultorios = new ArrayList<>();

    public Empresa(String nome, String morada, String localidade, String telefone, DonoEmpresa dono) {
        this.nome = nome;
        this.morada = morada;
        this.localidade = localidade;
        this.telefone = telefone;
        this.dono = dono;
    }

    public String getNome() { return nome; }
    public String getLocalidade() { return localidade; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
    public List<Consultorio> getConsultorios() { return consultorios; }
    public void adicionarConsultorio(Consultorio consultorio) { consultorios.add(consultorio); }
    @Override public String toString() { return nome + " - " + localidade + " - " + (ativa ? "Ativa" : "Inativa"); }
}
