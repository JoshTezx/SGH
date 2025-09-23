package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import dao.MedicamentoDAO;
import dao.TratamientoDAO;
import model.Tratamiento;
import model.Usuario;

public class TratamientoFormulario extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JTextField nombre;
	private JTextField duracion;
	private JTextArea reacciones;
	private JList<String> jmedicamentos;
	private DefaultListModel<String> medicamentosModel;
	private Usuario usuario;
	private Main menu;
	
	public TratamientoFormulario(Usuario usuario, Main menu) {
		this.usuario = usuario;
		this.setMenu(menu);
		
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        int row = 0;

        JLabel titulo = new JLabel("Nuevo Tratamiento", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridy = row++;
        add(titulo, gbc);
		
		JLabel l1 = new JLabel("Nombre:");
        gbc.gridy = row++;
		add(l1, gbc);
		
		nombre = new JTextField();
        gbc.gridy = row++;
		add(nombre, gbc);
		nombre.setColumns(10);
		
		JLabel l2 = new JLabel("Duración:");
        gbc.gridy = row++;
		add(l2, gbc);
		
		duracion = new JTextField();
        gbc.gridy = row++;
		add(duracion, gbc);
		duracion.setColumns(10);
		
		JLabel l3 = new JLabel("Medicamentos:");
        gbc.gridy = row++;
		add(l3, gbc);
		
		medicamentosModel = new DefaultListModel<>();
		MedicamentoDAO mDAO = new MedicamentoDAO();
		for (String s : mDAO.readMedicamentosName()) {
			medicamentosModel.addElement(s);
		}
		
		jmedicamentos = new JList<>(medicamentosModel);
		jmedicamentos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(jmedicamentos);
        gbc.gridy = row++;
		add(scroll, gbc);
		
		JLabel infoSeleccion = new JLabel("Usá Ctrl para seleccionar múltiples medicamentos.");
        gbc.gridy = row++;
		infoSeleccion.setFont(new Font("Arial", Font.ITALIC, 10));
		infoSeleccion.setForeground(Color.GRAY);
		add(infoSeleccion, gbc);
		
		JLabel l4 = new JLabel("Reaciones del paciente:");
        gbc.gridy = row++;
		add(l4, gbc);
		
		reacciones = new JTextArea();
        gbc.gridy = row++;
		add(reacciones, gbc);
		reacciones.setColumns(10);		
		
		JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        gbc.gridy = row++;
        add(botones, gbc);
        
		guardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (nombre.getText().trim().isEmpty() || 
					duracion.getText().trim().isEmpty() || 
					reacciones.getText().trim().isEmpty() || 
					jmedicamentos.getSelectedValuesList().isEmpty()) {

					JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos antes de guardar.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					int dura = Integer.parseInt(duracion.getText());
					
					Tratamiento t = new Tratamiento();
					t.setNombre(nombre.getText());
					t.setDuracion(dura);
					t.setPosiblesReaccionesDelPaciente(reacciones.getText());
					t.setMedicamentos(new ArrayList<>(jmedicamentos.getSelectedValuesList()));

					TratamientoDAO tDAO = new TratamientoDAO();
					tDAO.create(t);

					menu.cambiarVista("Tratamientos");

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "La duración debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error al guardar el tratamiento:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});

		cancelar.addActionListener(e -> menu.cambiarVista("Tratamientos"));
	}
	
	public TratamientoFormulario(Tratamiento tratamiento, Usuario usuario, Main menu) {
		this.usuario = usuario;
		this.setMenu(menu);
				
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        int row = 0;

        JLabel titulo = new JLabel("Nuevo Tratamiento", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridy = row++;
        add(titulo, gbc);
        
		JLabel l1 = new JLabel("Nombre:");
        gbc.gridy = row++;
		add(l1, gbc);
		
		nombre = new JTextField();
        gbc.gridy = row++;
		add(nombre, gbc);
		nombre.setColumns(10);
		
		JLabel l2 = new JLabel("Duración:");
        gbc.gridy = row++;
		add(l2, gbc);
		
		duracion = new JTextField();
        gbc.gridy = row++;
		add(duracion, gbc);
		duracion.setColumns(10);
		
		JLabel l3 = new JLabel("Medicamentos:");
        gbc.gridy = row++;
		add(l3, gbc);
		
		medicamentosModel = new DefaultListModel<>();
		MedicamentoDAO mDAO = new MedicamentoDAO();
		for (String s : mDAO.readMedicamentosName()) {
			medicamentosModel.addElement(s);
		}
		
		jmedicamentos = new JList<>(medicamentosModel);
		jmedicamentos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(jmedicamentos);
        gbc.gridy = row++;
		add(scroll, gbc);
		
		JLabel infoSeleccion = new JLabel("Usá Ctrl para seleccionar múltiples medicamentos.");
        gbc.gridy = row++;
		infoSeleccion.setFont(new Font("Arial", Font.ITALIC, 10));
		infoSeleccion.setForeground(Color.GRAY);
		add(infoSeleccion, gbc);
		
		JLabel l4 = new JLabel("Reaciones del paciente:");
        gbc.gridy = row++;
		add(l4, gbc);
		
		reacciones = new JTextArea();
        gbc.gridy = row++;
		add(reacciones, gbc);
		reacciones.setColumns(10);	
		
		this.nombre.setText(tratamiento.getNombre());
		String dura = Integer.toString(tratamiento.getDuracion());
		this.duracion.setText(dura);
		this.reacciones.setText(tratamiento.getPosiblesReaccionesDelPaciente());

		ArrayList<String> meds = tratamiento.getMedicamentos();
		ArrayList<Integer> indices = new ArrayList<>();
		
		for (int i = 0; i < medicamentosModel.size(); i++) {
			if (meds.contains(medicamentosModel.get(i))) {
				indices.add(i);
			}
		}
		int[] selectedIndices = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
		    selectedIndices[i] = indices.get(i);
		}
		jmedicamentos.setSelectedIndices(selectedIndices);
		
		JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        gbc.gridy = row++;
        add(botones, gbc);
		
		guardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tratamiento nuevo = new Tratamiento();
				nuevo.setNombre(nombre.getText());
				int dura = Integer.parseInt(duracion.getText());
				nuevo.setDuracion(dura);
				nuevo.setPosiblesReaccionesDelPaciente(reacciones.getText());
				
				ArrayList<String> seleccionados = new ArrayList<>(jmedicamentos.getSelectedValuesList());
				nuevo.setMedicamentos(seleccionados);
				
				TratamientoDAO tDAO = new TratamientoDAO();
				tDAO.update(nuevo, tratamiento);
				
				menu.cambiarVista("Tratamientos");
			}
		});

		cancelar.addActionListener(e -> menu.cambiarVista("Tratamientos"));
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
