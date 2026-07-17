package dentalcare.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import dentalcare.model.*;
import dentalcare.repository.DataRepository;

public class DentalCareService implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Utilizador> utilizadores = new CopyOnWriteArrayList<>();
    private final List<Empresa> empresas = new CopyOnWriteArrayList<>();
    private final List<Marcacao> marcacoes = new CopyOnWriteArrayList<>();
    private final Map<String, Utilizador> utilizadoresPorEmail = new ConcurrentHashMap<>();
    private final Map<String, List<Empresa>> empresasPorLocalidade = new ConcurrentHashMap<>();
    private final Map<String, List<Empresa>> empresasPorDono = new ConcurrentHashMap<>();
    private final Map<String, List<Consultorio>> consultoriosPorEmpresa = new ConcurrentHashMap<>();
    private transient DataRepository repository = new DataRepository();

    public DentalCareService() {
        carregar();
        if (utilizadores.isEmpty() && empresas.isEmpty()) {
            popularDadosIniciais();
        }
    }

    private void popularDadosIniciais() {
        Admin admin = new Admin("Admin DentalCare", "admin@dentalcare.pt", "admin123", "", "", "", "", "");
        adicionarUtilizador(admin);

        DonoEmpresa dono = registarDonoEmpresa("Ana Sousa", "ana@dentalcare.pt", "ana123", "12345678", "123456789", "912345678", "Rua da Estrela", "Lisboa");
        Empresa empresa = criarEmpresa(dono, "Clinica Smile", "Rua da Luz", "Lisboa", "213456789");
        Consultorio consultorio = adicionarConsultorio(empresa, "Consultorio Central", "Rua Nova", "Lisboa", "213456780", "Consulta Geral");
        criarFuncionario(dono, empresa, consultorio, "Dr. Miguel Costa", "miguel@dentalcare.pt", "miguel123", "", "", "", "", "", 12345, "Dentista");
        criarTipoConsulta(dono, empresa, consultorio, "Consulta Geral", 45.00);
        criarTipoConsulta(dono, empresa, consultorio, "Limpeza", 60.00);
        salvar();
    }

    public void salvar() {
        getRepository().salvar(this);
    }

    private void carregar() {
        DentalCareService carregado = getRepository().carregar();
        if (carregado == null) {
            return;
        }
        this.utilizadores.clear();
        this.utilizadores.addAll(carregado.utilizadores);
        this.empresas.clear();
        this.empresas.addAll(carregado.empresas);
        this.marcacoes.clear();
        this.marcacoes.addAll(carregado.marcacoes);
        this.utilizadoresPorEmail.clear();
        this.utilizadoresPorEmail.putAll(carregado.utilizadoresPorEmail);
        this.empresasPorLocalidade.clear();
        this.empresasPorLocalidade.putAll(carregado.empresasPorLocalidade);
        this.empresasPorDono.clear();
        this.empresasPorDono.putAll(carregado.empresasPorDono);
        this.consultoriosPorEmpresa.clear();
        this.consultoriosPorEmpresa.putAll(carregado.consultoriosPorEmpresa);
    }

    private DataRepository getRepository() {
        if (repository == null) {
            repository = new DataRepository();
        }
        return repository;
    }

    private void adicionarUtilizador(Utilizador utilizador) {
        utilizadores.add(utilizador);
        utilizadoresPorEmail.put(utilizador.getEmail(), utilizador);
    }

    public void registarCliente(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Cartao de cidadao: ");
        String cc = scanner.nextLine();
        System.out.print("Numero fiscal: ");
        String fiscal = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Morada: ");
        String morada = scanner.nextLine();
        System.out.print("Localidade: ");
        String localidade = scanner.nextLine();

        Cliente cliente = registarCliente(nome, email, password, cc, fiscal, telefone, morada, localidade);
        if (cliente != null) {
            System.out.println("Cliente registado com sucesso.");
        }
    }

    public void registarDono(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Cartao de cidadao: ");
        String cc = scanner.nextLine();
        System.out.print("Numero fiscal: ");
        String fiscal = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Morada: ");
        String morada = scanner.nextLine();
        System.out.print("Localidade: ");
        String localidade = scanner.nextLine();

        DonoEmpresa dono = registarDonoEmpresa(nome, email, password, cc, fiscal, telefone, morada, localidade);
        if (dono != null) {
            System.out.println("Dono de empresa registado com sucesso.");
        }
    }

    public void login(Scanner scanner) {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Utilizador utilizador = login(email, password);
        if (utilizador == null) {
            System.out.println("Credenciais invalidas.");
            return;
        }

        System.out.println("Bem-vindo, " + utilizador.getNome() + "!");
        if (utilizador instanceof Cliente) {
            menuCliente(scanner, (Cliente) utilizador);
        } else if (utilizador instanceof DonoEmpresa) {
            menuDono(scanner, (DonoEmpresa) utilizador);
        } else if (utilizador instanceof Funcionario) {
            menuFuncionario(scanner, (Funcionario) utilizador);
        } else if (utilizador instanceof Admin) {
            menuAdmin(scanner, (Admin) utilizador);
        }
    }

    private void menuCliente(Scanner scanner, Cliente cliente) {
        while (true) {
            System.out.println("\nCliente");
            System.out.println("1. Ver empresas");
            System.out.println("2. Marcar consulta");
            System.out.println("3. Ver minhas marcacoes");
            System.out.println("4. Pagar consulta");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    listarEmpresas().forEach(e -> System.out.println("- " + e.getNome() + " | " + e.getLocalidade()));
                    break;
                case "2":
                    marcarConsultaCliente(scanner, cliente);
                    break;
                case "3":
                    listarMarcacoesCliente(cliente).forEach(m -> System.out.println(m));
                    break;
                case "4":
                    pagarConsultaCliente(scanner, cliente);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private void menuDono(Scanner scanner, DonoEmpresa dono) {
        while (true) {
            System.out.println("\nDono de empresa");
            System.out.println("1. Criar empresa");
            System.out.println("2. Criar consultorio");
            System.out.println("3. Criar funcionario");
            System.out.println("4. Criar tipo de consulta");
            System.out.println("5. Ver marcacoes da empresa");
            System.out.println("6. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    criarEmpresaMenu(scanner, dono);
                    break;
                case "2":
                    criarConsultorioMenu(scanner, dono);
                    break;
                case "3":
                    criarFuncionarioMenu(scanner, dono);
                    break;
                case "4":
                    criarTipoConsultaMenu(scanner, dono);
                    break;
                case "5":
                    verMarcacoesDono(dono);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private void menuFuncionario(Scanner scanner, Funcionario funcionario) {
        while (true) {
            System.out.println("\nFuncionario");
            System.out.println("1. Ver marcacoes para mim");
            System.out.println("2. Confirmar marcacao");
            System.out.println("3. Cancelar marcacao");
            System.out.println("4. Concluir marcacao");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    listarMarcacoesFuncionario(funcionario).forEach(m -> System.out.println(m));
                    break;
                case "2":
                    confirmarMarcacaoFuncionario(scanner, funcionario);
                    break;
                case "3":
                    cancelarMarcacaoFuncionario(scanner, funcionario);
                    break;
                case "4":
                    concluirMarcacaoFuncionario(scanner, funcionario);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private void menuAdmin(Scanner scanner, Admin admin) {
        while (true) {
            System.out.println("\nAdministrador");
            System.out.println("1. Criar admin");
            System.out.println("2. Ver empresas");
            System.out.println("3. Desativar empresa");
            System.out.println("4. Ver estatisticas");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    criarAdmin(scanner, admin);
                    break;
                case "2":
                    listarEmpresas().forEach(e -> System.out.println(e));
                    break;
                case "3":
                    desativarEmpresa(scanner);
                    break;
                case "4":
                    System.out.println(gerarRelatorioFinanceiro(LocalDate.now().minusMonths(1), LocalDate.now()));
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private void criarEmpresaMenu(Scanner scanner, DonoEmpresa dono) {
        System.out.print("Nome da empresa: ");
        String nome = scanner.nextLine();
        System.out.print("Morada: ");
        String morada = scanner.nextLine();
        System.out.print("Localidade: ");
        String localidade = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        Empresa empresa = criarEmpresa(dono, nome, morada, localidade, telefone);
        if (empresa != null) {
            System.out.println("Empresa criada: " + empresa.getNome());
        }
    }

    private void criarConsultorioMenu(Scanner scanner, DonoEmpresa dono) {
        System.out.print("Nome da empresa: ");
        String nomeEmpresa = scanner.nextLine();
        Empresa empresa = encontrarEmpresaPorNome(nomeEmpresa);
        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }
        System.out.print("Nome do consultorio: ");
        String nome = scanner.nextLine();
        System.out.print("Morada: ");
        String morada = scanner.nextLine();
        System.out.print("Localidade: ");
        String localidade = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Especialidade: ");
        String especialidade = scanner.nextLine();
        Consultorio consultorio = adicionarConsultorio(empresa, nome, morada, localidade, telefone, especialidade);
        if (consultorio != null) {
            System.out.println("Consultorio criado: " + consultorio.getNome());
        }
    }

    private void criarFuncionarioMenu(Scanner scanner, DonoEmpresa dono) {
        System.out.print("Nome da empresa: ");
        String nomeEmpresa = scanner.nextLine();
        Empresa empresa = encontrarEmpresaPorNome(nomeEmpresa);
        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }
        System.out.print("Nome do consultorio: ");
        String nomeConsultorio = scanner.nextLine();
        Consultorio consultorio = encontrarConsultorio(empresa, nomeConsultorio);
        if (consultorio == null) {
            System.out.println("Consultorio nao encontrado.");
            return;
        }
        System.out.print("Nome do funcionario: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Carteira profissional: ");
        int carteira = Integer.parseInt(scanner.nextLine());
        System.out.print("Especialidade: ");
        String especialidade = scanner.nextLine();
        Funcionario funcionario = criarFuncionario(dono, empresa, consultorio, nome, email, password, "", "", "", "", "", carteira, especialidade);
        if (funcionario != null) {
            System.out.println("Funcionario criado: " + funcionario.getNome());
        }
    }

    private void criarTipoConsultaMenu(Scanner scanner, DonoEmpresa dono) {
        System.out.print("Nome da empresa: ");
        String nomeEmpresa = scanner.nextLine();
        Empresa empresa = encontrarEmpresaPorNome(nomeEmpresa);
        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }
        System.out.print("Nome do consultorio: ");
        String nomeConsultorio = scanner.nextLine();
        Consultorio consultorio = encontrarConsultorio(empresa, nomeConsultorio);
        if (consultorio == null) {
            System.out.println("Consultorio nao encontrado.");
            return;
        }
        System.out.print("Nome da consulta: ");
        String nome = scanner.nextLine();
        System.out.print("Preco base: ");
        double preco = Double.parseDouble(scanner.nextLine());
        ConsultaTipo consulta = criarTipoConsulta(dono, empresa, consultorio, nome, preco);
        if (consulta != null) {
            System.out.println("Tipo de consulta criado: " + consulta.getNome());
        }
    }

    private void verMarcacoesDono(DonoEmpresa dono) {
        for (Empresa empresa : dono.getEmpresas()) {
            System.out.println("Empresa: " + empresa.getNome());
            listarMarcacoesEmpresa(empresa).forEach(m -> System.out.println(" - " + m));
        }
    }

    private void marcarConsultaCliente(Scanner scanner, Cliente cliente) {
        System.out.print("Nome da empresa: ");
        String nomeEmpresa = scanner.nextLine();
        Empresa empresa = encontrarEmpresaPorNome(nomeEmpresa);
        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }
        System.out.print("Nome do consultorio: ");
        String nomeConsultorio = scanner.nextLine();
        Consultorio consultorio = encontrarConsultorio(empresa, nomeConsultorio);
        if (consultorio == null) {
            System.out.println("Consultorio nao encontrado.");
            return;
        }
        System.out.print("Tipo de consulta: ");
        String tipo = scanner.nextLine();
        ConsultaTipo consulta = consultorio.buscarConsultaPorNome(tipo);
        if (consulta == null) {
            System.out.println("Tipo de consulta nao encontrado.");
            return;
        }
        System.out.print("Email do funcionario (opcional): ");
        String emailFuncionario = scanner.nextLine();
        Funcionario funcionario = null;
        if (!emailFuncionario.isBlank()) {
            funcionario = consultorio.buscarFuncionarioPorEmail(emailFuncionario);
        }
        System.out.print("Data e hora (yyyy-MM-dd HH:mm): ");
        String dataTexto = scanner.nextLine();
        LocalDateTime dataHora;
        try {
            dataHora = LocalDateTime.parse(dataTexto, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Data invalida.");
            return;
        }
        Marcacao marcacao = marcarConsulta(cliente, empresa, consultorio, consulta, funcionario, dataHora);
        if (marcacao != null) {
            System.out.println("Marcacao criada: " + marcacao);
        }
    }

    private void pagarConsultaCliente(Scanner scanner, Cliente cliente) {
        System.out.print("ID da marcacao: ");
        String id = scanner.nextLine();
        Marcacao marcacao = encontrarMarcacaoPorId(id);
        if (marcacao == null || !marcacao.getCliente().equals(cliente)) {
            System.out.println("Marcacao nao encontrada.");
            return;
        }
        pagarConsulta(marcacao);
        System.out.println("Consulta paga.");
    }

    private void confirmarMarcacaoFuncionario(Scanner scanner, Funcionario funcionario) {
        System.out.print("ID da marcacao: ");
        String id = scanner.nextLine();
        Marcacao marcacao = encontrarMarcacaoPorId(id);
        if (marcacao == null || !marcacao.getFuncionario().equals(funcionario)) {
            System.out.println("Marcacao nao encontrada para este funcionario.");
            return;
        }
        confirmarMarcacao(marcacao, new ArrayList<>(), true);
        System.out.println("Marcacao confirmada.");
    }

    private void cancelarMarcacaoFuncionario(Scanner scanner, Funcionario funcionario) {
        System.out.print("ID da marcacao: ");
        String id = scanner.nextLine();
        Marcacao marcacao = encontrarMarcacaoPorId(id);
        if (marcacao == null || !marcacao.getFuncionario().equals(funcionario)) {
            System.out.println("Marcacao nao encontrada para este funcionario.");
            return;
        }
        System.out.print("Motivo: ");
        String motivo = scanner.nextLine();
        cancelarMarcacao(marcacao, motivo);
        System.out.println("Marcacao cancelada.");
    }

    private void concluirMarcacaoFuncionario(Scanner scanner, Funcionario funcionario) {
        System.out.print("ID da marcacao: ");
        String id = scanner.nextLine();
        Marcacao marcacao = encontrarMarcacaoPorId(id);
        if (marcacao == null || !marcacao.getFuncionario().equals(funcionario)) {
            System.out.println("Marcacao nao encontrada para este funcionario.");
            return;
        }
        System.out.print("Problema/observacao: ");
        String problema = scanner.nextLine();
        concluirMarcacao(marcacao, problema);
        System.out.println("Marcacao concluida.");
    }

    private void criarAdmin(Scanner scanner, Admin admin) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        criarAdmin(admin, nome, email, password, "", "", "", "", "");
        System.out.println("Admin criado.");
    }

    private void desativarEmpresa(Scanner scanner) {
        System.out.print("Nome da empresa: ");
        String nome = scanner.nextLine();
        Empresa empresa = encontrarEmpresaPorNome(nome);
        if (empresa != null) {
            empresa.setAtiva(false);
            salvar();
            System.out.println("Empresa desativada.");
        } else {
            System.out.println("Empresa nao encontrada.");
        }
    }

    public Cliente registarCliente(String nome, String email, String password, String cartao, String fiscal, String telefone, String morada, String localidade) {
        if (utilizadoresPorEmail.containsKey(email)) {
            System.out.println("Email ja existe.");
            return null;
        }
        Cliente cliente = new Cliente(nome, email, password, cartao, fiscal, telefone, morada, localidade);
        adicionarUtilizador(cliente);
        salvar();
        return cliente;
    }

    public DonoEmpresa registarDonoEmpresa(String nome, String email, String password, String cartao, String fiscal, String telefone, String morada, String localidade) {
        if (utilizadoresPorEmail.containsKey(email)) {
            System.out.println("Email ja existe.");
            return null;
        }
        DonoEmpresa dono = new DonoEmpresa(nome, email, password, cartao, fiscal, telefone, morada, localidade);
        adicionarUtilizador(dono);
        salvar();
        return dono;
    }

    public Admin criarAdmin(Utilizador autor, String nome, String email, String password, String cartao, String fiscal, String telefone, String morada, String localidade) {
        if (!(autor instanceof Admin)) {
            return null;
        }
        if (utilizadoresPorEmail.containsKey(email)) {
            return null;
        }
        Admin admin = new Admin(nome, email, password, cartao, fiscal, telefone, morada, localidade);
        adicionarUtilizador(admin);
        salvar();
        return admin;
    }

    public Utilizador login(String email, String password) {
        Utilizador utilizador = utilizadoresPorEmail.get(email);
        if (utilizador != null && utilizador.getPassword().equals(password) && utilizador.isAtivo()) {
            return utilizador;
        }
        return null;
    }

    public Empresa criarEmpresa(DonoEmpresa dono, String nome, String morada, String localidade, String telefone) {
        if (dono == null) {
            return null;
        }
        Empresa empresa = new Empresa(nome, morada, localidade, telefone, dono);
        empresas.add(empresa);
        dono.adicionarEmpresa(empresa);
        empresasPorLocalidade.computeIfAbsent(localidade, k -> new ArrayList<>()).add(empresa);
        empresasPorDono.computeIfAbsent(dono.getEmail(), k -> new ArrayList<>()).add(empresa);
        consultoriosPorEmpresa.put(empresa.getNome(), new ArrayList<>());
        salvar();
        return empresa;
    }

    public Consultorio adicionarConsultorio(Empresa empresa, String nome, String morada, String localidade, String telefone, String especialidade) {
        if (empresa == null || !empresa.isAtiva()) {
            return null;
        }
        Consultorio consultorio = new Consultorio(nome, morada, localidade, telefone, especialidade);
        empresa.adicionarConsultorio(consultorio);
        consultoriosPorEmpresa.computeIfAbsent(empresa.getNome(), k -> new ArrayList<>()).add(consultorio);
        salvar();
        return consultorio;
    }

    public Funcionario criarFuncionario(DonoEmpresa dono, Empresa empresa, Consultorio consultorio, String nome, String email, String password, String cartao, String fiscal, String telefone, String morada, String localidade, int carteiraProfissional, String especialidade) {
        if (dono == null || empresa == null || consultorio == null) {
            return null;
        }
        if (utilizadoresPorEmail.containsKey(email)) {
            return null;
        }
        Funcionario funcionario = new Funcionario(nome, email, password, cartao, fiscal, telefone, morada, localidade, carteiraProfissional, especialidade);
        consultorio.adicionarFuncionario(funcionario);
        adicionarUtilizador(funcionario);
        salvar();
        return funcionario;
    }

    public ConsultaTipo criarTipoConsulta(DonoEmpresa dono, Empresa empresa, Consultorio consultorio, String nome, double precoBase) {
        if (dono == null || empresa == null || consultorio == null) {
            return null;
        }
        ConsultaTipo tipo = new ConsultaTipo(nome, precoBase);
        consultorio.adicionarTipoConsulta(tipo);
        salvar();
        return tipo;
    }

    public Marcacao marcarConsulta(Cliente cliente, Empresa empresa, Consultorio consultorio, ConsultaTipo tipoConsulta, Funcionario funcionario, LocalDateTime dataHora) {
        if (cliente == null || empresa == null || consultorio == null || tipoConsulta == null) {
            return null;
        }
        if (!empresa.isAtiva()) {
            System.out.println("Empresa inativa.");
            return null;
        }
        if (!consultorio.temSlotDisponivel(dataHora, funcionario)) {
            System.out.println("Slot indisponivel.");
            return null;
        }
        Marcacao marcacao = new Marcacao(cliente, empresa, consultorio, tipoConsulta, funcionario, dataHora);
        consultorio.adicionarMarcacao(marcacao);
        marcacoes.add(marcacao);
        consultorio.marcarSlot(dataHora, funcionario);
        salvar();
        return marcacao;
    }

    public void confirmarMarcacao(Marcacao marcacao, List<ServicoComplementar> servicos, boolean paga) {
        if (marcacao == null) {
            return;
        }
        marcacao.setEstado(EstadoMarcacao.CONFIRMADA);
        marcacao.setServicosComplementares(servicos);
        marcacao.setValorTotal(marcacao.getTipoConsulta().getPrecoBase() + marcacao.calcularServicos());
        if (paga) {
            marcacao.setPaga(true);
            marcacao.setEstado(EstadoMarcacao.PAGA);
        }
        salvar();
    }

    public void cancelarMarcacao(Marcacao marcacao, String motivo) {
        if (marcacao == null) {
            return;
        }
        marcacao.setEstado(EstadoMarcacao.CANCELADA);
        marcacao.setMotivoCancelamento(motivo);
        salvar();
    }

    public void concluirMarcacao(Marcacao marcacao, String problema) {
        if (marcacao == null) {
            return;
        }
        marcacao.setEstado(EstadoMarcacao.CONCLUIDA);
        marcacao.setObservacoes(problema);
        salvar();
    }

    public void pagarConsulta(Marcacao marcacao) {
        if (marcacao == null) {
            return;
        }
        marcacao.setPaga(true);
        marcacao.setEstado(EstadoMarcacao.PAGA);
        salvar();
    }

    public List<Empresa> listarEmpresas() {
        return empresas;
    }

    public List<Marcacao> listarMarcacoesCliente(Cliente cliente) {
        return marcacoes.stream().filter(m -> m.getCliente().equals(cliente)).sorted(Comparator.comparing(Marcacao::getDataHora)).toList();
    }

    public List<Marcacao> listarMarcacoesEmpresa(Empresa empresa) {
        return marcacoes.stream().filter(m -> m.getEmpresa().equals(empresa)).sorted(Comparator.comparing(Marcacao::getDataHora)).toList();
    }

    public List<Marcacao> listarMarcacoesFuncionario(Funcionario funcionario) {
        return marcacoes.stream().filter(m -> funcionario != null && funcionario.equals(m.getFuncionario())).sorted(Comparator.comparing(Marcacao::getDataHora)).toList();
    }

    public Empresa encontrarEmpresaPorNome(String nome) {
        return empresas.stream().filter(e -> e.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null);
    }

    public Consultorio encontrarConsultorio(Empresa empresa, String nome) {
        if (empresa == null) {
            return null;
        }
        return empresa.getConsultorios().stream().filter(c -> c.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null);
    }

    public Marcacao encontrarMarcacaoPorId(String id) {
        return marcacoes.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    public String gerarRelatorioFinanceiro(LocalDate inicio, LocalDate fim) {
        double total = 0;
        int count = 0;
        for (Marcacao marcacao : marcacoes) {
            if (marcacao.getEstado() == EstadoMarcacao.PAGA && !marcacao.getDataHora().toLocalDate().isBefore(inicio) && !marcacao.getDataHora().toLocalDate().isAfter(fim)) {
                total += marcacao.getValorTotal();
                count++;
            }
        }
        return String.format("Pagas: %d | Total: %.2f EUR | Comissao DentalCare: %.2f EUR", count, total, total * 0.05);
    }

    public void simularOperacoesConcorretes() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(() -> registarCliente("Cliente A", "clienteA@dentalcare.pt", "123", "", "", "", "", ""));
        executor.submit(() -> registarCliente("Cliente B", "clienteB@dentalcare.pt", "123", "", "", "", "", ""));
        executor.submit(() -> registarDonoEmpresa("Bruno Teixeira", "bruno@dentalcare.pt", "123", "", "", "", "", ""));
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Simulacao concluida.");
    }
}
