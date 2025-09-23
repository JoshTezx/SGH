package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import dao.UsuarioDAO;
import model.Usuario;

public class Login extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField usuarioField;
    private JPasswordField contrasenaField;
    private JButton login;
    private JLabel titulo;
    private UsuarioDAO uDAO = new UsuarioDAO();

    public Login() {
        setLayout(new GridBagLayout());
        setBackground(Color.GRAY);
        //setBackground(UIManager.getColor("Panel.background")); 

        JPanel panel = new JPanel(new GridBagLayout());
        //panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/TezxInd.png"));
        
        Image img = icon.getImage().getScaledInstance(160, 100, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(logo, gbc);
        
        // Título
        gbc.gridy++;
        titulo = new JLabel("Iniciar sesión", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panel.add(titulo, gbc);

        // Usuario
        gbc.gridy++;
        JLabel usuarioLabel = new JLabel("Usuario:");
        panel.add(usuarioLabel, gbc);

        gbc.gridy++;
        usuarioField = new JTextField(20);
        usuarioField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(usuarioField, gbc);

        // Contraseña
        gbc.gridy++;
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        panel.add(contrasenaLabel, gbc);

        gbc.gridy++;
        contrasenaField = new JPasswordField(20);
        contrasenaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(contrasenaField, gbc);

        // Botón
        gbc.gridy++;
        login = new JButton("Ingresar");
        login.setFont(new Font("Segoe UI", Font.BOLD, 14));
        login.setBackground(new Color(66, 135, 245));
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false);
        login.setBorderPainted(false);
        login.setPreferredSize(new Dimension(200, 40));
        panel.add(login, gbc);

        add(panel);

        // Acción del botón
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                String password = new String(contrasenaField.getPassword());

                if (usuario.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Usuario u = uDAO.autenticar(usuario, password);
                if (u != null) {
                    JOptionPane.showMessageDialog(null, "Bienvenido, " + u.getUsuario());

                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
                    Main mainPanel = new Main(u, frame);

                    frame.setTitle("Sistema Hospitalario - Usuario: " + u.getUsuario() + " (" + u.getRol() + ")");
                    frame.setContentPane(mainPanel);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    frame.revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
