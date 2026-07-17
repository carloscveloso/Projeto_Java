package dentalcare.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Consultorio implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String morada;
    private String localidade;
    private String telefone;
    private String especialidade;
    private final List<Funcionario> funcionarios = new ArrayList<>();
    private final List<ConsultaTipo> tiposConsulta = new ArrayList<>();
    private final List<Marcacao> marcacoes = new ArrayList<>();
    private final List<AgendaSlot> agenda = new ArrayList<>();

    public Consultorio(String nome, String morada, String localidade, String telefone, String especialidade) {
        this.nome = nome;
        this.morada = morada;
        this.localidade = localidade;
        this.telefone = telefone;
        this.especialidade = especialidade;
    }

    public String getNome() { return nome; }
    public void adicionarFuncionario(Funcionario funcionario) { funcionarios.add(funcionario); }
    public void adicionarTipoConsulta(ConsultaTipo tipo) { tiposConsulta.add(tipo); }
    public void adicionarMarcacao(Marcacao marcacao) { marcacoes.add(marcacao); }
    public boolean temSlotDisponivel(LocalDateTime dataHora, Funcionario funcionario) {
        return agenda.stream().noneMatch(slot -> slot.getDataHora().equals(dataHora) && (funcionario == null || slot.getFuncionarioEmail().equals(funcionario.getEmail())));
    }
    public void marcarSlot(LocalDateTime dataHora, Funcionario funcionario) { agenda.add(new AgendaSlot(dataHora, funcionario == null ? null : funcionario.getEmail())); }
    public ConsultaTipo buscarConsultaPorNome(String nome) { return tiposConsulta.stream().filter(t -> t.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null); }
    public Funcionario buscarFuncionarioPorEmail(String email) { return funcionarios.stream().filter(f -> f.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null); }
}
