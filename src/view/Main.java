package view;

import javax.swing.*;
import javax.swing.tree.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import model.Usuario;
import model.Rol;

public class Main extends JPanel {
    private static final long serialVersionUID = 1L;
    private Usuario usuarioLogueado;
    private CardLayout cardLayout;
    private JPanel panelContenido;
    private JFrame principal;

    public Main(Usuario usuario, JFrame principal) {
        this.usuarioLogueado = usuario;
        this.setPrincipal(principal);
        setLayout(new BorderLayout());

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Menú");

        DefaultMutableTreeNode registros = new DefaultMutableTreeNode("Registros");
        if (puedeVer(Rol.Administrador, Rol.Administrativo, Rol.Auditor, Rol.Doctor))
            registros.add(new DefaultMutableTreeNode("Admisiones"));
        if (puedeVer(Rol.Administrador, Rol.Administrativo, Rol.Auditor, Rol.Doctor))
            registros.add(new DefaultMutableTreeNode("Pacientes"));
        if (puedeVer(Rol.Administrador, Rol.Auditor))
            registros.add(new DefaultMutableTreeNode("Departamentos"));
        if (puedeVer(Rol.Administrador, Rol.Auditor))
            registros.add(new DefaultMutableTreeNode("Doctores"));
        if (puedeVer(Rol.Administrador, Rol.Doctor, Rol.Auditor))
            registros.add(new DefaultMutableTreeNode("Tratamientos"));
        if (puedeVer(Rol.Administrador, Rol.Doctor, Rol.Auditor))
            registros.add(new DefaultMutableTreeNode("Medicamentos"));

        if (registros.getChildCount() > 0) root.add(registros);

        DefaultMutableTreeNode reportes = new DefaultMutableTreeNode("Reportes");
        if (puedeVer(Rol.Administrador, Rol.Administrativo, Rol.Auditor, Rol.Doctor)) {
            reportes.add(new DefaultMutableTreeNode("Camas disponibles"));
            reportes.add(new DefaultMutableTreeNode("Camas ocupadas con información del paciente"));
        }
        if (puedeVer(Rol.Administrador, Rol.Doctor, Rol.Administrativo, Rol.Auditor)) {
            reportes.add(new DefaultMutableTreeNode("Pacientes crónicos"));
            reportes.add(new DefaultMutableTreeNode("Historial clínico de pacientes"));
        }

        if (reportes.getChildCount() > 0) root.add(reportes);

        JTree menuTree = new JTree(root);
        JScrollPane treeScroll = new JScrollPane(menuTree);
        treeScroll.setPreferredSize(new Dimension(250, 0));


        
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        if (puedeVer(Rol.Administrador, Rol.Administrativo, Rol.Auditor, Rol.Doctor))
            panelContenido.add(new AdmisionRegistro(usuario, this), "Admisiones");
        if (puedeVer(Rol.Administrador, Rol.Administrativo, Rol.Auditor, Rol.Doctor))
            panelContenido.add(new PacienteRegistro(usuario, this), "Pacientes");
        if (puedeVer(Rol.Administrador, Rol.Auditor))
            panelContenido.add(new DepartamentoRegistro(usuario, this), "Departamentos");
        if (puedeVer(Rol.Administrador, Rol.Auditor))
            panelContenido.add(new DoctorRegistro(usuario, this), "Doctores");
        if (puedeVer(Rol.Administrador, Rol.Doctor, Rol.Auditor))
            panelContenido.add(new TratamientoRegistro(usuario, this), "Tratamientos");
        if (puedeVer(Rol.Administrador, Rol.Doctor, Rol.Administrativo, Rol.Farmacéutico, Rol.Auditor))
            panelContenido.add(new MedicamentoRegistro(usuario, this), "Medicamentos");

        if (puedeVer(Rol.Administrador, Rol.Administrativo, Rol.Auditor)) {
            panelContenido.add(new ReporteCamasDisponibles(usuario), "Camas disponibles");
            panelContenido.add(new ReporteCamasOcupadas(usuario), "Camas ocupadas con información del paciente");
        }

        if (puedeVer(Rol.Administrador, Rol.Doctor, Rol.Administrativo, Rol.Auditor)) {
            panelContenido.add(new ReportePacienteCronico(usuario), "Pacientes crónicos");
            panelContenido.add(new ReporteHistorialClinicoPaciente(usuario), "Historial clínico de pacientes");
        }

        // Evento de selección del árbol
        menuTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();
            if (nodo == null || nodo.getChildCount() > 0) return;

            String vista = nodo.getUserObject().toString();
            
            switch (vista) {
            case "Camas disponibles":
        		panelContenido.add(new ReporteCamasDisponibles(usuarioLogueado), "Camas disponibles");
        		break;
        	case "Camas ocupadas con información del paciente":
                panelContenido.add(new ReporteCamasOcupadas(usuarioLogueado), "Camas ocupadas con información del paciente");
        		break;
        	case "Pacientes crónicos":
                panelContenido.add(new ReportePacienteCronico(usuarioLogueado), "Pacientes crónicos");
        		break;
        	case "Historial clínico de pacientes":
                panelContenido.add(new ReporteHistorialClinicoPaciente(usuarioLogueado), "Historial clínico de pacientes");
        		break;
            }
            
            cardLayout.show(panelContenido, vista);
        });

        JButton cerrarSesion = new JButton("Cerrar Sesión");
        cerrarSesion.addActionListener(e -> {
            JFrame m = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
            m.setContentPane(new Login());
            m.setSize(300, 250);
            m.setTitle("Login");
            m.setLocationRelativeTo(null);
            m.revalidate();
            m.repaint();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel cerrarSesionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        String nombreUsuario = usuarioLogueado != null ? usuarioLogueado.getUsuario() : "Usuario";
        String rolUsuario = usuarioLogueado != null ? usuarioLogueado.getRol().name() : "Sin rol";
        JLabel labelUsuario = new JLabel(nombreUsuario + " (" + rolUsuario + ") | ");
        cerrarSesionPanel.add(labelUsuario);
        cerrarSesionPanel.add(cerrarSesion);
        
        topPanel.add(cerrarSesionPanel, BorderLayout.NORTH);

        //JMenuBar menuBar = crearMenuBarDesdePanel((JFrame) SwingUtilities.getWindowAncestor(this));
        
        //principal.setJMenuBar(crearMenuBarDesdePanel(principal));
        
        
        JMenuBar menuBar = crearMenuBarDesdePanel(principal);
        JPanel menuContainer = new JPanel(new BorderLayout());
        menuContainer.add(menuBar, BorderLayout.CENTER);
        
        add(menuContainer, BorderLayout.NORTH);
        
        
        add(treeScroll, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);
        //add(topPanel, BorderLayout.NORTH);
    }

    // Permisos de acceso según rol
    private boolean puedeVer(Rol... rolesPermitidos) {
        if (usuarioLogueado == null) return true;
        for (Rol r : rolesPermitidos)
            if (usuarioLogueado.getRol() == r) return true;
        return false;
    }
    
    public void cambiarVista(String nombreVista) {
    	switch (nombreVista) {
    	case "Admisiones":
    		panelContenido.add(new AdmisionRegistro(usuarioLogueado, this), "Admisiones");
    		break;
    	case "Pacientes":
    		panelContenido.add(new PacienteRegistro(usuarioLogueado, this), "Pacientes");
    		break;
    	case "Departamentos":
    		panelContenido.add(new DepartamentoRegistro(usuarioLogueado, this), "Departamentos");
    		break;
    	case "Doctores":
    		panelContenido.add(new DoctorRegistro(usuarioLogueado, this), "Doctores");
    		break;
    	case "Tratamientos":
    		panelContenido.add(new TratamientoRegistro(usuarioLogueado, this), "Tratamientos");
    		break;
    	case "Medicamentos":
    		panelContenido.add(new MedicamentoRegistro(usuarioLogueado, this), "Medicamentos");
    		break;
    	}
    	
        cardLayout.show(panelContenido, nombreVista);
    }

    public void agregarVistaDinamica(JPanel vista, String nombreVista) {
        panelContenido.add(vista, nombreVista);
        cambiarVista(nombreVista);
    }
    
    public JMenuBar crearMenuBarDesdePanel(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar sesión");
        itemCerrarSesion.addActionListener(e -> {
            frame.setTitle("Login");
            frame.setContentPane(new Login());
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.revalidate();
            frame.repaint();
        });
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(itemCerrarSesion);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        JMenu menuEditar = new JMenu("Editar");
        JMenuItem itemPreferencias = new JMenuItem("Preferencias");
        
        itemPreferencias.addActionListener(e -> preferencias());
        
        menuEditar.add(itemPreferencias);
        
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcercaDe = new JMenuItem("Acerca del sistema...");
        itemAcercaDe.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Sistema Hospitalario\nVersión 1.10\nDesarrollado por Josué Espinoza",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemAcercaDe);

        menuBar.add(menuArchivo);
        menuBar.add(menuEditar);
        menuBar.add(menuAyuda);

        return menuBar;
    }

	public JFrame getPrincipal() {
		return principal;
	}

	public void setPrincipal(JFrame principal) {
		this.principal = principal;
	}

    
    private void preferencias() {
    	JRadioButton claro = new JRadioButton("Claro");
        JRadioButton oscuro = new JRadioButton("Oscuro");
        ButtonGroup estilos = new ButtonGroup();
        estilos.add(claro);
        estilos.add(oscuro);
        
        Object[] datos = { 
        		"Tema: ", 
        		claro,
        		oscuro 
        };
        
        String lookAndFeelActual = UIManager.getLookAndFeel().getClass().getName();

        if (lookAndFeelActual.equals(FlatLightLaf.class.getName())) {
            claro.setSelected(true);
        } else if (lookAndFeelActual.equals(FlatDarkLaf.class.getName())) {
            oscuro.setSelected(true);
        } else {
            // Otro LookAndFeel distinto
        }
        
        int opcion = JOptionPane.showConfirmDialog(this, datos, "Preferencias", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
        	if (claro.isSelected()) {
                claro.setSelected(true);
            	try {
        			UIManager.setLookAndFeel(new FlatLightLaf());
        		} catch (Exception e) {
        			System.err.println("No se pudo aplicar la textura");
        		}
            	for (Window window : Window.getWindows()) {
                    SwingUtilities.updateComponentTreeUI(window);
                }
            }
        	
        	if (oscuro.isSelected()) {
                oscuro.setSelected(true);
            	try {
        			UIManager.setLookAndFeel(new FlatDarkLaf());
        		} catch (Exception e) {
        			System.err.println("No se pudo aplicar la textura");
        		}
            	for (Window window : Window.getWindows()) {
                    SwingUtilities.updateComponentTreeUI(window);
                }
            }
        }
    }
}
