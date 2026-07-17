package dentalcare.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Marcacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id = String.valueOf(System.nanoTime());
    private final Cliente cliente;
    private final Empresa empresa;
    private final Consultorio consultorio;
    private final ConsultaTipo tipoConsulta;
    private final Funcionario funcionario;
    private final LocalDateTime dataHora;
    private EstadoMarcacao estado = EstadoMarcacao.PENDENTE;
    private double valorTotal;
    private boolean paga;
    private String observacoes = "";
    private String motivoCancelamento = "";
    private List<ServicoComplementar> servicosComplementares = new ArrayList<>();

    public Marcacao(Cliente cliente, Empresa empresa, Consultorio consultorio, ConsultaTipo tipoConsulta, Funcionario funcionario, LocalDateTime dataHora) {
        this.cliente = cliente; this.empresa = empresa; this.consultorio = consultorio; this.tipoConsulta = tipoConsulta; this.funcionario = funcionario; this.dataHora = dataHora; this.valorTotal = tipoConsulta.getPrecoBase();
    }

    public String getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Empresa getEmpresa() { return empresa; }
    public Consultorio getConsultorio() { return consultorio; }
    public ConsultaTipo getTipoConsulta() { return tipoConsulta; }
    public Funcionario getFuncionario() { return funcionario; }
    public LocalDateTime getDataHora() { return dataHora; }
    public EstadoMarcacao getEstado() { return estado; }
    public void setEstado(EstadoMarcacao estado) { this.estado = estado; }
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    public boolean isPaga() { return paga; }
    public void setPaga(boolean paga) { this.paga = paga; }
    public void setServicosComplementares(List<ServicoComplementar> servicosComplementares) { this.servicosComplementares = servicosComplementares; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public void setMotivoCancelamento(String motivoCancelamento) { this.motivoCancelamento = motivoCancelamento; }
    public double calcularServicos() { return servicosComplementares.stream().mapToDouble(ServicoComplementar::getPreco).sum(); }
    @Override public String toString() { return id + " | " + cliente.getNome() + " | " + empresa.getNome() + " | " + tipoConsulta.getNome() + " | " + estado + " | " + dataHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); }
}
