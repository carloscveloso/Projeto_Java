package dentalcare.app;

import dentalcare.model.*;
import dentalcare.service.DentalCareService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class SwingApp extends JFrame {
    private final DentalCareService service = new DentalCareService();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private Utilizador utilizadorAtual;

    // Cores do tema - tons mais suaves
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color PRIMARY_DARK = new Color(60, 80, 100);
    private static final Color SUCCESS_COLOR = new Color(60, 140, 80);
    private static final Color DANGER_COLOR = new Color(200, 70, 60);
    private static final Color BG_COLOR = new Color(240, 242, 245);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(50, 60, 70);
    private static final Color TEXT_LIGHT = new Color(140, 150, 160);

    public SwingApp() {
        super("DentalCare - Gestao de Consultas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        getContentPane().setBackground(BG_COLOR);

        inicializarUI();
        mostrarLogin();
    }

    private void inicializarUI() {
        cards.add(criarPainelLogin(), "login");
        cards.add(criarPainelRegistoCliente(), "registerClient");
        cards.add(criarPainelRegistoDono(), "registerOwner");
        cards.add(new JPanel(), "dashboard");
        setContentPane(cards);
    }

    // ==================== PAINEL DE LOGIN ====================
    private JPanel criarPainelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("DentalCare");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(PRIMARY_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sistema de Gestao de Consultas");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField emailField = criarCampoTexto();
        JPasswordField passwordField = criarCampoPassword();

        JButton loginButton = criarBotaoPrimario("Entrar");
        JButton registarClienteButton = criarBotaoSecundario("Criar conta de Cliente");
        JButton registarDonoButton = criarBotaoSecundario("Criar conta de Dono");

        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));
        card.add(new JLabel("Email"));
        card.add(Box.createVerticalStrut(5));
        card.add(emailField);
        card.add(Box.createVerticalStrut(15));
        card.add(new JLabel("Password"));
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(25));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(15));

        JLabel separador = new JLabel("---  ou  ---");
        separador.setAlignmentX(Component.CENTER_ALIGNMENT);
        separador.setForeground(TEXT_LIGHT);
        card.add(separador);
        card.add(Box.createVerticalStrut(15));
        card.add(registarClienteButton);
        card.add(Box.createVerticalStrut(10));
        card.add(registarDonoButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                mostrarErro("Preencha todos os campos.");
                return;
            }

            Utilizador resultado = service.login(email, password);
            if (resultado == null) {
                mostrarErro("Credenciais invalidas.");
                return;
            }
            utilizadorAtual = resultado;
            mostrarDashboard();
        });

        registarClienteButton.addActionListener(e -> mostrarRegistoCliente());
        registarDonoButton.addActionListener(e -> mostrarRegistoDono());

        JScrollPane scrollPane = new JScrollPane(card);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(400, 520));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(scrollPane, gbc);
        return panel;
    }

    // ==================== PAINEIS DE REGISTO ====================
    private JPanel criarPainelRegistoCliente() {
        return criarPainelRegisto("Criar Conta de Cliente", "Cliente", true);
    }

    private JPanel criarPainelRegistoDono() {
        return criarPainelRegisto("Criar Conta de Dono", "Dono de Empresa", false);
    }

    private JPanel criarPainelRegisto(String titulo, String tipo, boolean isCliente) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JPanel header = criarHeader(titulo, true);
        panel.add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        JTextField nomeField = criarCampoTexto();
        JTextField emailField = criarCampoTexto();
        JPasswordField passwordField = criarCampoPassword();
        JPasswordField confirmPasswordField = criarCampoPassword();
        JTextField ccField = criarCampoTexto();
        JTextField fiscalField = criarCampoTexto();
        JTextField telefoneField = criarCampoTexto();
        JTextField moradaField = criarCampoTexto();
        JTextField localidadeField = criarCampoTexto();

        JLabel statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(criarLabelCampo("Nome completo *"));
        formPanel.add(nomeField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(criarLabelCampo("Email *"));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(criarLabelCampo("Password *"));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(criarLabelCampo("Confirmar Password *"));
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(criarLabelCampo("Nº Cartao Cidadao"));
        formPanel.add(ccField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(criarLabelCampo("Nº Fiscal"));
        formPanel.add(fiscalField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(criarLabelCampo("Telefone"));
        formPanel.add(telefoneField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(criarLabelCampo("Morada"));
        formPanel.add(moradaField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(criarLabelCampo("Localidade"));
        formPanel.add(localidadeField);
        formPanel.add(Box.createVerticalStrut(20));

        JButton submitButton = criarBotaoPrimario("Criar " + tipo);
        JButton backButton = criarBotaoSecundario("Voltar ao Login");

        submitButton.addActionListener(e -> {
            if (nomeField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    passwordField.getPassword().length == 0) {
                statusLabel.setForeground(DANGER_COLOR);
                statusLabel.setText("Preencha os campos obrigatorios (*)");
                return;
            }

            if (!new String(passwordField.getPassword())
                    .equals(new String(confirmPasswordField.getPassword()))) {
                statusLabel.setForeground(DANGER_COLOR);
                statusLabel.setText("As passwords nao coincidem");
                return;
            }

            if (!emailField.getText().contains("@")) {
                statusLabel.setForeground(DANGER_COLOR);
                statusLabel.setText("Email invalido");
                return;
            }

            boolean sucesso;
            if (isCliente) {
                Cliente cliente = service.registarCliente(
                        nomeField.getText().trim(),
                        emailField.getText().trim(),
                        new String(passwordField.getPassword()),
                        ccField.getText().trim(),
                        fiscalField.getText().trim(),
                        telefoneField.getText().trim(),
                        moradaField.getText().trim(),
                        localidadeField.getText().trim()
                );
                sucesso = cliente != null;
            } else {
                DonoEmpresa dono = service.registarDonoEmpresa(
                        nomeField.getText().trim(),
                        emailField.getText().trim(),
                        new String(passwordField.getPassword()),
                        ccField.getText().trim(),
                        fiscalField.getText().trim(),
                        telefoneField.getText().trim(),
                        moradaField.getText().trim(),
                        localidadeField.getText().trim()
                );
                sucesso = dono != null;
            }

            if (sucesso) {
                statusLabel.setForeground(SUCCESS_COLOR);
                statusLabel.setText(tipo + " registado com sucesso!");
                nomeField.setText("");
                emailField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                ccField.setText("");
                fiscalField.setText("");
                telefoneField.setText("");
                moradaField.setText("");
                localidadeField.setText("");

                Timer timer = new Timer(1500, evt -> mostrarLogin());
                timer.setRepeats(false);
                timer.start();
            } else {
                statusLabel.setForeground(DANGER_COLOR);
                statusLabel.setText("Email ja existe");
            }
        });

        backButton.addActionListener(e -> mostrarLogin());

        formPanel.add(statusLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(submitButton);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ==================== DASHBOARD ====================
    private JPanel criarPainelDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JPanel header = criarHeader("Painel de Controlo", false);
        JButton logoutButton = new JButton("Sair");
        logoutButton.setBackground(DANGER_COLOR);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.addActionListener(e -> {
            utilizadorAtual = null;
            mostrarLogin();
        });

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRight.setBackground(PRIMARY_DARK);
        headerRight.add(logoutButton);
        header.add(headerRight, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(CARD_COLOR);
        sidePanel.setBorder(new EmptyBorder(20, 15, 20, 15));
        sidePanel.setPreferredSize(new Dimension(220, 0));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(CARD_COLOR);
        outputArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(new LineBorder(new Color(190, 195, 200), 1));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        if (utilizadorAtual != null) {
            adicionarBotoesNavegacao(sidePanel, outputArea);
            outputArea.setText("Bem-vindo, " + utilizadorAtual.getNome() + "\n\nSelecione uma opcao no menu lateral.");
        }

        panel.add(sidePanel, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private void adicionarBotoesNavegacao(JPanel sidePanel, JTextArea outputArea) {
        sidePanel.removeAll();

        if (utilizadorAtual == null) {
            sidePanel.revalidate();
            sidePanel.repaint();
            return;
        }

        JLabel userInfo = new JLabel(utilizadorAtual.getNome());
        userInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userInfo.setForeground(PRIMARY_DARK);
        userInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userType = new JLabel(utilizadorAtual.getClass().getSimpleName());
        userType.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        userType.setForeground(TEXT_LIGHT);
        userType.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidePanel.add(userInfo);
        sidePanel.add(userType);
        sidePanel.add(Box.createVerticalStrut(20));

        JButton btnEmpresas = criarBotaoMenu("Listar Empresas");
        btnEmpresas.addActionListener(e -> {
            List<Empresa> empresas = service.listarEmpresas();
            StringBuilder sb = new StringBuilder("EMPRESAS REGISTADAS\n");
            sb.append("=".repeat(50)).append("\n\n");
            if (empresas.isEmpty()) {
                sb.append("Nenhuma empresa registada.");
            } else {
                for (Empresa emp : empresas) {
                    sb.append("* ").append(emp.getNome())
                            .append("\n   Local: ").append(emp.getLocalidade())
                            .append("\n   Status: ").append(emp.isAtiva() ? "Ativa" : "Inativa")
                            .append("\n\n");
                }
            }
            outputArea.setText(sb.toString());
        });

        sidePanel.add(btnEmpresas);
        sidePanel.add(Box.createVerticalStrut(8));

        if (utilizadorAtual instanceof Cliente cliente) {
            JButton btnMarcar = criarBotaoMenu("Marcar Consulta");
            btnMarcar.addActionListener(e -> mostrarDialogoMarcarConsulta(cliente, outputArea));

            JButton btnMinhas = criarBotaoMenu("Minhas Marcacoes");
            btnMinhas.addActionListener(e -> {
                var marcacoes = service.listarMarcacoesCliente(cliente);
                StringBuilder sb = new StringBuilder("MINHAS MARCACOES\n");
                sb.append("=".repeat(50)).append("\n\n");
                if (marcacoes.isEmpty()) {
                    sb.append("Nenhuma marcacao encontrada.");
                } else {
                    marcacoes.forEach(m -> sb.append(m).append("\n\n"));
                }
                outputArea.setText(sb.toString());
            });

            JButton btnPagar = criarBotaoMenu("Pagar Consulta");
            btnPagar.addActionListener(e -> {
                String id = JOptionPane.showInputDialog(this, "ID da marcacao:");
                if (id != null && !id.trim().isEmpty()) {
                    var marcacao = service.encontrarMarcacaoPorId(id.trim());
                    if (marcacao != null && marcacao.getCliente().equals(cliente)) {
                        service.pagarConsulta(marcacao);
                        outputArea.setText("Consulta paga com sucesso!\n\n" + marcacao);
                    } else {
                        outputArea.setText("Marcacao nao encontrada.");
                    }
                }
            });

            sidePanel.add(btnMarcar);
            sidePanel.add(Box.createVerticalStrut(8));
            sidePanel.add(btnMinhas);
            sidePanel.add(Box.createVerticalStrut(8));
            sidePanel.add(btnPagar);

        } else if (utilizadorAtual instanceof DonoEmpresa dono) {
            JButton btnCriarEmpresa = criarBotaoMenu("Criar Empresa");
            btnCriarEmpresa.addActionListener(e -> mostrarDialogoCriarEmpresa(dono, outputArea));

            JButton btnCriarConsultorio = criarBotaoMenu("Criar Consultorio");
            btnCriarConsultorio.addActionListener(e -> mostrarDialogoCriarConsultorio(outputArea));

            JButton btnCriarFuncionario = criarBotaoMenu("Criar Funcionario");
            btnCriarFuncionario.addActionListener(e -> mostrarDialogoCriarFuncionario(dono, outputArea));

            JButton btnCriarTipo = criarBotaoMenu("Criar Tipo Consulta");
            btnCriarTipo.addActionListener(e -> mostrarDialogoCriarTipoConsulta(outputArea));

            JButton btnMarcacoes = criarBotaoMenu("Ver Marcacoes");
            btnMarcacoes.addActionListener(e -> {
                StringBuilder sb = new StringBuilder("MARCACOES DAS EMPRESAS\n");
                sb.append("=".repeat(50)).append("\n\n");
                for (Empresa emp : dono.getEmpresas()) {
                    sb.append("* ").append(emp.getNome()).append("\n");
                    var marcacoes = service.listarMarcacoesEmpresa(emp);
                    if (marcacoes.isEmpty()) {
                        sb.append("   Nenhuma marcacao\n");
                    } else {
                        marcacoes.forEach(m -> sb.append("   - ").append(m).append("\n"));
                    }
                    sb.append("\n");
                }
                outputArea.setText(sb.toString());
            });

            sidePanel.add(btnCriarEmpresa);
            sidePanel.add(Box.createVerticalStrut(8));
            sidePanel.add(btnCriarConsultorio);
            sidePanel.add(Box.createVerticalStrut(8));
            sidePanel.add(btnCriarFuncionario);
            sidePanel.add(Box.createVerticalStrut(8));
            sidePanel.add(btnCriarTipo);
            sidePanel.add(Box.createVerticalStrut(8));
            sidePanel.add(btnMarcacoes);

        } else if (utilizadorAtual instanceof Funcionario funcionario) {
            JButton btnMinhas = criarBotaoMenu("Minhas Marcacoes");
            btnMinhas.addActionListener(e -> {
                var marcacoes = service.listarMarcacoesFuncionario(funcionario);
                StringBuilder sb = new StringBuilder("MINHAS MARCACOES\n");
                sb.append("=".repeat(50)).append("\n\n");
                if (marcacoes.isEmpty()) {
                    sb.append("Nenhuma marcacao atribuida.");
                } else {
                    marcacoes.forEach(m -> sb.append(m).append("\n\n"));
                }
                outputArea.setText(sb.toString());
            });

            JButton btnConfirmar = criarBotaoMenu("Confirmar Marcacao");
            btnConfirmar.addActionListener(e -> {
                String id = JOptionPane.showInputDialog(this, "ID da marcacao:");
                if (id != null && !id.trim().isEmpty()) {
                    var marcacao = service.encontrarMarcacaoPorId(id.trim());
                    if (marcacao != null && marcacao.getFuncionario() != null &&
                            marcacao.getFuncionario().equals(funcionario)) {
                        service.confirmarMarcacao(marcacao, List.of(), true);
                        outputArea.setText("Marcacao confirmada!\n\n" + marcacao);
                    } else {
                        outputArea.setText("Marcacao nao encontrada.");
                    }
                }
            });

            JButton btnCancelar = criarBotaoMenu("Cancelar Marcacao");
            btnCancelar.addActionListener(e -> {
                String id = JOptionPane.showInputDialog(this, "ID da marcacao:");
                if (id != null && !id.trim().isEmpty()) {
                    var marcacao = service.encontrarMarcacaoPorId(id.trim());
                    if (marcacao != null && marcacao.getFuncionario() != null &&
                            marcacao.getFuncionario().equals(funcionario)) {
                        String motivo = JOptionPane.showInputDialog(this, "Motivo do cancelamento:");
                        service.cancelarMarcacao(marcacao, motivo != null ? motivo : "");
                        outputArea.setText("Marcacao cancelada!\n\n" + marcacao);
                    } else {
                        outputArea.setText("Marcacao nao encontrada.");
                    }
                }
            });

            JButton btnConcluir = criarBotaoMenu("Concluir Marcacao");
            btnConcluir.addActionListener(e -> {
                String id = JOptionPane.showInputDialog(this, "ID da marcacao:");
                if (id != null && !id.trim().isEmpty()) {
                    var marcacao = service.encontrarMarcacaoPorId(id.trim());
                    if (marcacao != null && marcacao.getFuncionario() != null &&
                            marcacao.getFuncionario().equals(funcionario)) {
                        String obs = JOptionPane.showInputDialog(this, "Observacoes:");
                        service.concluirMarcacao(marcacao, obs != null ? obs : "");
                        outputArea.setText("Marcacao concluida!\n\n" + marcacao);
                    } else {
                        outputArea.setText("Marcacao nao encontrada.");
                    }
                }
            });

            sidePanel.add(btnMinhas);
            sidePanel.add(Box.createVerticalStrut(8));
            sidePanel.add(btnConfirmar);
            sidePanel.add(Box.createVerticalStrut(8));
            sidePanel.add(btnCancelar);
            sidePanel.add(Box.createVerticalStrut(8));
            sidePanel.add(btnConcluir);
        }

        sidePanel.revalidate();
        sidePanel.repaint();
    }

    // ==================== DIALOGOS MODAIS ====================
    private void mostrarDialogoMarcarConsulta(Cliente cliente, JTextArea outputArea) {
        JDialog dialog = new JDialog(this, "Marcar Consulta", true);
        dialog.setSize(450, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField empresaField = criarCampoTexto();
        JTextField consultorioField = criarCampoTexto();
        JTextField tipoField = criarCampoTexto();
        JTextField funcionarioField = criarCampoTexto();
        JTextField dataField = criarCampoTexto();

        form.add(criarLabelCampo("Empresa *"));
        form.add(empresaField);
        form.add(Box.createVerticalStrut(10));
        form.add(criarLabelCampo("Consultorio *"));
        form.add(consultorioField);
        form.add(Box.createVerticalStrut(10));
        form.add(criarLabelCampo("Tipo de consulta *"));
        form.add(tipoField);
        form.add(Box.createVerticalStrut(10));
        form.add(criarLabelCampo("Funcionario (opcional)"));
        form.add(funcionarioField);
        form.add(Box.createVerticalStrut(10));
        form.add(criarLabelCampo("Data e Hora * (yyyy-MM-dd HH:mm)"));
        form.add(dataField);
        form.add(Box.createVerticalStrut(20));

        JButton btnMarcar = criarBotaoPrimario("Marcar Consulta");
        btnMarcar.addActionListener(e -> {
            try {
                Empresa empresa = service.encontrarEmpresaPorNome(empresaField.getText().trim());
                if (empresa == null) {
                    mostrarErro("Empresa nao encontrada.");
                    return;
                }

                Consultorio consultorio = service.encontrarConsultorio(empresa, consultorioField.getText().trim());
                if (consultorio == null) {
                    mostrarErro("Consultorio nao encontrado.");
                    return;
                }

                var tipoConsulta = consultorio.buscarConsultaPorNome(tipoField.getText().trim());
                if (tipoConsulta == null) {
                    mostrarErro("Tipo de consulta nao encontrado.");
                    return;
                }

                Funcionario funcionario = null;
                if (!funcionarioField.getText().trim().isEmpty()) {
                    funcionario = consultorio.buscarFuncionarioPorEmail(funcionarioField.getText().trim());
                    if (funcionario == null) {
                        mostrarErro("Funcionario nao encontrado.");
                        return;
                    }
                }

                LocalDateTime dataHora = LocalDateTime.parse(
                        dataField.getText().trim(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                );

                var marcacao = service.marcarConsulta(cliente, empresa, consultorio, tipoConsulta, funcionario, dataHora);
                if (marcacao != null) {
                    outputArea.setText("Consulta marcada com sucesso!\n\n" + marcacao);
                    dialog.dispose();
                }
            } catch (DateTimeParseException ex) {
                mostrarErro("Formato de data invalido. Use: yyyy-MM-dd HH:mm");
            } catch (Exception ex) {
                mostrarErro("Erro ao marcar consulta: " + ex.getMessage());
            }
        });

        form.add(btnMarcar);
        dialog.add(form, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void mostrarDialogoCriarEmpresa(DonoEmpresa dono, JTextArea outputArea) {
        JDialog dialog = criarDialogoFormulario("Criar Empresa",
                new String[]{"Nome", "Morada", "Localidade", "Telefone"}, values -> {
                    Empresa empresa = service.criarEmpresa(dono, values[0], values[1], values[2], values[3]);
                    if (empresa != null) {
                        outputArea.setText("Empresa criada: " + empresa.getNome());
                        return true;
                    }
                    return false;
                });
        dialog.setVisible(true);
    }

    private void mostrarDialogoCriarConsultorio(JTextArea outputArea) {
        JDialog dialog = criarDialogoFormulario("Criar Consultorio",
                new String[]{"Empresa", "Nome", "Morada", "Localidade", "Telefone", "Especialidade"},
                values -> {
                    Empresa empresa = service.encontrarEmpresaPorNome(values[0]);
                    if (empresa == null) {
                        mostrarErro("Empresa nao encontrada.");
                        return false;
                    }
                    Consultorio consultorio = service.adicionarConsultorio(empresa, values[1], values[2], values[3], values[4], values[5]);
                    if (consultorio != null) {
                        outputArea.setText("Consultorio criado: " + consultorio.getNome());
                        return true;
                    }
                    return false;
                });
        dialog.setVisible(true);
    }

    private void mostrarDialogoCriarFuncionario(DonoEmpresa dono, JTextArea outputArea) {
        JDialog dialog = criarDialogoFormulario("Criar Funcionario",
                new String[]{"Empresa", "Consultorio", "Nome", "Email", "Password", "Carteira Prof.", "Especialidade"},
                values -> {
                    Empresa empresa = service.encontrarEmpresaPorNome(values[0]);
                    if (empresa == null) {
                        mostrarErro("Empresa nao encontrada.");
                        return false;
                    }
                    Consultorio consultorio = service.encontrarConsultorio(empresa, values[1]);
                    if (consultorio == null) {
                        mostrarErro("Consultorio nao encontrado.");
                        return false;
                    }
                    try {
                        int carteira = Integer.parseInt(values[5]);
                        Funcionario func = service.criarFuncionario(dono, empresa, consultorio, values[2], values[3], values[4], "", "", "", "", "", carteira, values[6]);
                        if (func != null) {
                            outputArea.setText("Funcionario criado: " + func.getNome());
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        mostrarErro("Carteira profissional deve ser um numero.");
                    }
                    return false;
                });
        dialog.setVisible(true);
    }

    private void mostrarDialogoCriarTipoConsulta(JTextArea outputArea) {
        JDialog dialog = criarDialogoFormulario("Criar Tipo de Consulta",
                new String[]{"Empresa", "Consultorio", "Nome", "Preco Base"},
                values -> {
                    Empresa empresa = service.encontrarEmpresaPorNome(values[0]);
                    if (empresa == null) {
                        mostrarErro("Empresa nao encontrada.");
                        return false;
                    }
                    Consultorio consultorio = service.encontrarConsultorio(empresa, values[1]);
                    if (consultorio == null) {
                        mostrarErro("Consultorio nao encontrado.");
                        return false;
                    }
                    try {
                        double preco = Double.parseDouble(values[3]);
                        var tipo = service.criarTipoConsulta(
                                (DonoEmpresa) utilizadorAtual, empresa, consultorio, values[2], preco);
                        if (tipo != null) {
                            outputArea.setText("Tipo de consulta criado: " + tipo.getNome());
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        mostrarErro("Preco deve ser um numero.");
                    }
                    return false;
                });
        dialog.setVisible(true);
    }

    // ==================== METODOS AUXILIARES ====================
    private JPanel criarHeader(String titulo, boolean comVoltar) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_DARK);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        if (comVoltar) {
            JButton backButton = new JButton("< Voltar");
            backButton.setForeground(Color.WHITE);
            backButton.setContentAreaFilled(false);
            backButton.setBorderPainted(false);
            backButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backButton.addActionListener(e -> mostrarLogin());
            header.add(backButton, BorderLayout.WEST);
        }

        header.add(titleLabel, BorderLayout.CENTER);
        return header;
    }

    private JTextField criarCampoTexto() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(190, 195, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return field;
    }

    private JPasswordField criarCampoPassword() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(190, 195, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return field;
    }

    private JLabel criarLabelCampo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton criarBotaoPrimario(String texto) {
        JButton button = new JButton(texto);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(90, 150, 200));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private JButton criarBotaoSecundario(String texto) {
        JButton button = new JButton(texto);
        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_COLOR);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(PRIMARY_COLOR, 1));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private JButton criarBotaoMenu(String texto) {
        JButton button = new JButton(texto);
        button.setBackground(CARD_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(235, 240, 245));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(CARD_COLOR);
            }
        });

        return button;
    }

    private JDialog criarDialogoFormulario(String titulo, String[] campos, FormSubmitCallback callback) {
        JDialog dialog = new JDialog(this, titulo, true);
        dialog.setSize(420, 200 + campos.length * 50);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 25, 20, 25));
        form.setBackground(CARD_COLOR);

        JTextField[] fields = new JTextField[campos.length];
        for (int i = 0; i < campos.length; i++) {
            form.add(criarLabelCampo(campos[i]));
            fields[i] = criarCampoTexto();
            form.add(fields[i]);
            form.add(Box.createVerticalStrut(10));
        }

        JButton submitButton = criarBotaoPrimario("Confirmar");
        submitButton.addActionListener(e -> {
            String[] values = new String[campos.length];
            for (int i = 0; i < campos.length; i++) {
                values[i] = fields[i].getText().trim();
            }
            if (callback.onSubmit(values)) {
                dialog.dispose();
            }
        });

        form.add(submitButton);
        dialog.add(form, BorderLayout.CENTER);
        return dialog;
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    // ==================== NAVEGACAO ====================
    private void mostrarLogin() {
        cardLayout.show(cards, "login");
        setTitle("DentalCare - Login");
    }

    private void mostrarRegistoCliente() {
        cardLayout.show(cards, "registerClient");
        setTitle("DentalCare - Registo de Cliente");
    }

    private void mostrarRegistoDono() {
        cardLayout.show(cards, "registerOwner");
        setTitle("DentalCare - Registo de Dono");
    }

    private void mostrarDashboard() {
        cards.remove(4);
        cards.add(criarPainelDashboard(), "dashboard");
        cardLayout.show(cards, "dashboard");
        setTitle("DentalCare - Painel de Controlo");
    }

    @FunctionalInterface
    private interface FormSubmitCallback {
        boolean onSubmit(String[] values);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SwingApp().setVisible(true);
        });
    }
}