package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.MedicamentoDAO;
import model.Medicamento;
import model.Rol;
import model.Usuario;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MedicamentoRegistro extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private ArrayList<Medicamento> medicamentos = new ArrayList<>();
	private Usuario usuario;
	private Main menu;
	
	public MedicamentoRegistro(Usuario usuario, Main menu) {
		this.setUsuario(usuario);
		this.setMenu(menu);
		
		setLayout(new BorderLayout(10, 10));
		
		JLabel titulo = new JLabel("Registro de medicamentos", SwingConstants.CENTER);
		add(titulo, BorderLayout.NORTH);

		String[] columnas = { "Nombre", "Fabricación", "Vencimiento", "Tipo" };
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Object[][] datos = new Object[0][columnas.length];

		MedicamentoDAO mDAO = new MedicamentoDAO();
		try {
			medicamentos = mDAO.readAll();
			datos = new Object[medicamentos.size()][columnas.length];
			
			int cont = 0;
			for (Medicamento m : medicamentos) {
				datos[cont][0] = m.getNombreDroga();
				datos[cont][1] = m.getFabricacion().format(formatter);
				datos[cont][2] = m.getVencimiento().format(formatter);
				datos[cont][3] = m.getTipo();
				cont++;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al cargar medicamentos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		table = new JTable(new DefaultTableModel(datos, columnas) {
			private static final long serialVersionUID = 1L;
			
			@Override 
			public boolean isCellEditable(int row, int column) {
				return false;
			}
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
		
		if (usuario.getRol() == Rol.Administrador || usuario.getRol() == Rol.Doctor) {
			nuevo.addActionListener(e -> menu.agregarVistaDinamica(new MedicamentoFormulario(usuario, menu), "Create Medicamento"));
			botones.add(nuevo);
		}

		if (usuario.getRol() == Rol.Administrador || usuario.getRol() == Rol.Doctor) {
			modificar.addActionListener(e -> {
				int[] filas = table.getSelectedRows();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());

				if (filas.length == 0) {
					JOptionPane.showMessageDialog(frame, "Seleccione un elemento para modificar.");
					return;
				}

				if (filas.length > 1) {
					JOptionPane.showMessageDialog(frame, "No se puede modificar más de un elemento a la vez.");
					return;
				}
				
				int f1 = filas[0];
				Medicamento seleccionada = medicamentos.get(f1);
				
				menu.agregarVistaDinamica(new MedicamentoFormulario(seleccionada, usuario, menu), "Update Medicamento");
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
				Medicamento seleccionada = medicamentos.get(f1);
				
				int confirm = JOptionPane.showConfirmDialog(null,
						"¿Está seguro que desea eliminar este medicamento?",
						"Confirmar eliminación",
						JOptionPane.YES_NO_OPTION);

				if (confirm != JOptionPane.YES_OPTION) return;
				
				String mensaje = "";
				if (mDAO.delete(seleccionada)) {
					mensaje = "Elemento eliminado con exito.";
				} else {
					mensaje = "Ocurrio un error. Intente de nuevo.";
				}
				
				JOptionPane.showMessageDialog(frame, mensaje);

				menu.cambiarVista("Medicamentos");
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
