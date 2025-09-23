package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.CamaDAO;
import model.Cama;
import model.Usuario;

import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ReporteCamasDisponibles extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTable table;
	private ArrayList<Cama> disponibles = new ArrayList<Cama>();
	private Usuario usuario;
	
	public ReporteCamasDisponibles(Usuario usuario) {
		this.setUsuario(usuario);
		
		setLayout(new BorderLayout(10, 10));
		
		JLabel titulo = new JLabel("Reporte de camas disponibles", SwingConstants.CENTER);
		add(titulo, BorderLayout.NORTH);
		
		CamaDAO cDAO = new CamaDAO();
		disponibles = cDAO.readCamasDisponiblesConDepartamento();
		
		String[] columnas = { "Número de cama", "Departamento" };
		int cont = 0;
		Object[][] datos = new Object[disponibles.size()][columnas.length];
		for(Cama c : disponibles) {
			datos[cont][0] = c.getNumero();
			datos[cont][1] = c.getDepartamentoNombre();
			cont++;
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
		
		add(botones, BorderLayout.SOUTH);
		
		ajustarAnchoColumnas(table);
	}

	private void ajustarAnchoColumnas(JTable tabla) {
		for (int col = 0; col < tabla.getColumnCount(); col++) {
			int maxWidth = 75; // Ancho mínimo
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
	
	
}
