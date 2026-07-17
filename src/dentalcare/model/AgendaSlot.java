package dentalcare.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AgendaSlot implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime dataHora;
    private String funcionarioEmail;

    public AgendaSlot(LocalDateTime dataHora, String funcionarioEmail) { this.dataHora = dataHora; this.funcionarioEmail = funcionarioEmail; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getFuncionarioEmail() { return funcionarioEmail; }
}
