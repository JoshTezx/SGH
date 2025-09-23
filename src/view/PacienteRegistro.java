package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.PacienteDAO;
import model.Paciente;
import model.Rol;
import model.Usuario;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PacienteRegistro extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTable table;
	private ArrayList<Paciente> pacientes = new ArrayList<Paciente>();
	private Usuario usuario;
	private Main menu;
	
	public PacienteRegistro(Usuario usuario, Main menu) {
		this.setUsuario(usuario);
		this.setMenu(menu);
		
		setLayout(new BorderLayout(10, 10));
		
		JLabel titulo = new JLabel("Registro de pacientes", SwingConstants.CENTER);
		add(titulo, BorderLayout.NORTH);
		
		String[] columnas = { "Nombre", "Dirección", "Sexo", "CUIL" };
		Object[][] datos = new Object[0][columnas.length];

		PacienteDAO pDAO = new PacienteDAO();
		try {
			pacientes = pDAO.readAll();
			datos = new Object[pacientes.size()][columnas.length];
			
			int cont = 0;
			for(Paciente p : pacientes) {
				datos[cont][0] = p.getNombre();
				datos[cont][1] = p.getDireccion();
				datos[cont][2] = p.getSexo();
				datos[cont][3] = p.getCuil();
				cont++;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al cargar pacientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
			nuevo.addActionListener(e -> menu.agregarVistaDinamica(new PacienteFormulario(usuario, menu), "Create Paciente"));
			botones.add(nuevo);
		}
		
		if (usuario.getRol() == Rol.Administrador || usuario.getRol() == Rol.Administrativo) {
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
				Paciente seleccionada = pacientes.get(f1);
				
				menu.agregarVistaDinamica(new PacienteFormulario(seleccionada, usuario, menu), "Update Paciente"); 
			});
			botones.add(modificar);
			
			eliminar.addActionListener(e -> {
				int[] filas = table.getSelectedRows();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());

				if (filas.length == 0) {
					JOptionPane.showMessageDialog(frame, "Seleccione un elemento para eliminar.");
					return;
				}

				if (filas.length > 1) {
					JOptionPane.showMessageDialog(frame, "No se puede eliminar más de un elemento a la vez.");
					return;
				}
				
				int f1 = filas[0];
				Paciente seleccionada = pacientes.get(f1);
				
				int confirm = JOptionPane.showConfirmDialog(null,
						"¿Está seguro que desea eliminar este paciente?",
						"Confirmar eliminación",
						JOptionPane.YES_NO_OPTION);

				if (confirm != JOptionPane.YES_OPTION) return;
				
				String mensaje = "";
				if(pDAO.delete(seleccionada)) {
					mensaje = "Elemento eliminado con exito";	
				}
				else {
					mensaje = "Ocurrio un error. Intente de nuevo";
				}
				
				JOptionPane.showMessageDialog(frame, mensaje);
				
				menu.cambiarVista("Pacientes");
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
