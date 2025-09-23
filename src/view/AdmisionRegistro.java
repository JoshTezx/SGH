package view;

import model.Admision;
import model.Rol;
import model.Usuario;
import dao.AdmisionDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdmisionRegistro extends JPanel{
    private static final long serialVersionUID = 1L;
    private JTable table;
    private ArrayList<Admision> admisiones;
    private Usuario usuario;
    private Main menu;
    
    public AdmisionRegistro(Usuario usuario, Main menu) {
		this.setUsuario(usuario);
    	this.setMenu(menu);

        setLayout(new BorderLayout(10, 10));
        
        JLabel titulo = new JLabel("Registro de admisiones", SwingConstants.CENTER);
		add(titulo, BorderLayout.NORTH);
        
		String[] columnas = { "Paciente", "Ingreso", "Baja", "Cama", "Tratamientos" };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Object[][] datos = new Object[0][columnas.length];
		
		AdmisionDAO aDAO = new AdmisionDAO();
		try {
			admisiones = aDAO.readAll();
			datos = new Object[admisiones.size()][columnas.length];
			
			int cont = 0;
			for (Admision a : admisiones) {
				datos[cont][0] = a.getNombrePaciente();
				datos[cont][1] = a.getFechaIngreso().format(formatter);
				datos[cont][2] = (a.getFechaAlta() != null) ? a.getFechaAlta().format(formatter) : "Sin fecha";
				datos[cont][3] = a.getNumeroCama();
				datos[cont][4] = String.join(", ", a.getTratamientos());
				cont++;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,  "Error al cargar admisiones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
        table = new JTable(new DefaultTableModel(datos, columnas) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setResizingAllowed(false);

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel botones = new JPanel();
        botones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton nuevo = new JButton("Nuevo");
		JButton modificar = new JButton("Modificar");
		JButton eliminar = new JButton("Eliminar");
        
        if (usuario.getRol() == Rol.Administrador || usuario.getRol() == Rol.Administrativo) {
            nuevo.addActionListener(e -> menu.agregarVistaDinamica(new AdmisionFormulario(usuario, menu), "Create Admisión"));
            botones.add(nuevo);
            
            modificar.addActionListener(e -> {
                int[] filas = table.getSelectedRows();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                
                if (filas.length == 0) {
                    JOptionPane.showMessageDialog(frame, "Seleccione un elemento para modificar.");
                    return;
                }
                
                if (filas.length > 1) {
                    JOptionPane.showMessageDialog(frame, "No se puede modificar más de un elemento a la vez.");
                    return;
                }
                
                int f1 = filas[0];
                Admision seleccionada = admisiones.get(f1);
                
                menu.agregarVistaDinamica(new AdmisionFormulario(seleccionada, usuario, menu), "Update Admisión");
            });
            botones.add(modificar);
        }

        if (usuario.getRol() == Rol.Administrador || usuario.getRol() == Rol.Administrativo) {
            eliminar.addActionListener(e -> {
                int[] filas = table.getSelectedRows();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                
                if (filas.length == 0) {
                    JOptionPane.showMessageDialog(frame, "Seleccione un elemento para eliminar.");
                    return;
                }
                
                if (filas.length > 1) {
                    JOptionPane.showMessageDialog(frame, "No se puede eliminar más de un elemento a la vez.");
                    return;
                }
                
                int f1 = filas[0];
                Admision seleccionada = admisiones.get(f1);
                
                int confirm = JOptionPane.showConfirmDialog(null,
						"¿Está seguro que desea eliminar esta admisión?",
						"Confirmar eliminación",
						JOptionPane.YES_NO_OPTION);

				if (confirm != JOptionPane.YES_OPTION) return;
				
				String mensaje = "";
				if(aDAO.delete(seleccionada)) {
					mensaje = "Elemento eliminado con exito";	
				}
				else {
					mensaje = "Ocurrio un error. Intente de nuevo";
				}
				
				JOptionPane.showMessageDialog(frame, mensaje);
				
				menu.cambiarVista("Admisiones");
            });
            botones.add(eliminar);
        }
        
        add(botones, BorderLayout.SOUTH);
        
		ajustarAnchoColumnas(table);
    }

    private void ajustarAnchoColumnas(JTable tabla) {
		for (int col = 0; col < tabla.getColumnCount(); col++) {
			int maxWidth = 75; 
			for (int row = 0; row < tabla.getRowCount(); row++) {
				Component comp = tabla.prepareRenderer(tabla.getCellRenderer(row, col), row, col);
				maxWidth = Math.max(comp.getPreferredSize().width + 10, maxWidth);
			}
			tabla.getColumnModel().getColumn(col).setPreferredWidth(maxWidth);
		}
	}
    
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Main getMenu() {
		return menu;
	}

	public void setMenu(Main menu) {
		this.menu = menu;
	}
}
