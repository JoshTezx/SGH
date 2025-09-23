package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.PacienteDAO;
import model.ReportePaciente;
import model.Usuario;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ReporteHistorialClinicoPaciente extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTable table;
	private ArrayList<ReportePaciente> disponibles = new ArrayList<ReportePaciente>();
	private Usuario usuario;
	
	public ReporteHistorialClinicoPaciente(Usuario usuario) {
		this.setUsuario(usuario);
				
		setLayout(new BorderLayout(10, 10));
		
		JLabel titulo = new JLabel("Reporte de historial clínico de pacientes", SwingConstants.CENTER);
		add(titulo, BorderLayout.NORTH);
	
		PacienteDAO pDAO = new PacienteDAO();
		disponibles = pDAO.readPacientesDetalles();
		
		String[] columnas = { "Paciente", "Fecha de alta", "Fecha de baja", "Tratamientos", "Medicamentos" };
		int cont = 0;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Object[][] datos = new Object[disponibles.size()][columnas.length];
		for(ReportePaciente rp : disponibles) {
			datos[cont][0] = rp.getNombre();
			datos[cont][1] = rp.getFechaAlta().format(formatter);
			datos[cont][2] = (rp.getFechaBaja() != null) ? rp.getFechaBaja().format(formatter) : "Sin fecha";
			datos[cont][3] = String.join(", ", rp.getTratamientos());
			datos[cont][4] = String.join(", ", rp.getDrogasSuminstradas());
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
	
	
}
