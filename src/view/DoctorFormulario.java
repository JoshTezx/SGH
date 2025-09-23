package view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import dao.DepartamentoDAO;
import dao.DoctorDAO;
import model.Doctor;
import model.Usuario;

public class DoctorFormulario extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JTextField nombre;
	private JTextField apellido;
	private JComboBox<String> jdepartamentos;
	private ArrayList<String> departamentos = new ArrayList<String>();
	private JTextField matricula;
	private Usuario usuario;
	private Main menu;
	
	public DoctorFormulario(Usuario usuario, Main menu) {	
		this.usuario = usuario;
		this.setMenu(menu);
		
		setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        int row = 0;

        JLabel titulo = new JLabel("Nuevo Admisión", SwingConstants.CENTER);
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
		
		JLabel l2 = new JLabel("Apellido:");
        gbc.gridy = row++; 
		add(l2, gbc);
		
		apellido = new JTextField();
        gbc.gridy = row++; 
		add(apellido, gbc);
		apellido.setColumns(10);
		
		JLabel l3 = new JLabel("Departamento:");
        gbc.gridy = row++; 
		add(l3, gbc);
		
		jdepartamentos = new JComboBox<String>();
        gbc.gridy = row++; 
		
		DepartamentoDAO dDAO = new DepartamentoDAO();
		departamentos = dDAO.readNameDepartamento();
		
		jdepartamentos.addItem("Seleccione una");
		for(String s : departamentos) {
			jdepartamentos.addItem(s);
		}
		add(jdepartamentos, gbc);
		
		JLabel l4 = new JLabel("Matrícula:");
        gbc.gridy = row++; 
		add(l4, gbc);
		
		matricula = new JTextField();
        gbc.gridy = row++; 
		add(matricula, gbc);  
		matricula.setColumns(10);		
		
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
				    apellido.getText().trim().isEmpty() || 
				    matricula.getText().trim().isEmpty() ||
				    jdepartamentos.getSelectedItem() == null ||
				    jdepartamentos.getSelectedItem() == "Seleccione una") {
				    
					JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos antes de guardar.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
					return; 
				}
				
				try {
					DoctorDAO pDAO = new DoctorDAO();
					Doctor d = new Doctor();
							
					d.setNombre(nombre.getText());
					d.setApellido(apellido.getText());
					int matri = Integer.parseInt(matricula.getText());
					d.setMatricula(matri);
					String depto = (String) jdepartamentos.getSelectedItem();
					d.setDepartamento(depto.equals("Sin departamento") ? null : depto);
							
					pDAO.create(d);
					
					menu.cambiarVista("Doctores");
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "La matrícula debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		cancelar.addActionListener(e -> menu.cambiarVista("Doctores"));
	}
	
	public DoctorFormulario(Doctor doctor, Usuario usuario, Main menu) {	
		this.usuario = usuario;
		this.menu = menu;
		
		setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        int row = 0;

        JLabel titulo = new JLabel("Modificar Doctor", SwingConstants.CENTER);
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
		
		JLabel l2 = new JLabel("Apellido:");
        gbc.gridy = row++; 
		add(l2, gbc);
		
		apellido = new JTextField();
        gbc.gridy = row++; 
		add(apellido, gbc);
		apellido.setColumns(10);
		
		JLabel l3 = new JLabel("Departamento:");
        gbc.gridy = row++; 
		add(l3, gbc);
		
		jdepartamentos = new JComboBox<String>();
        gbc.gridy = row++; 
		
		DepartamentoDAO dDAO = new DepartamentoDAO();
		departamentos = dDAO.readNameDepartamento();
		
		jdepartamentos.addItem("Seleccione una");
		for(String s : departamentos) {
			jdepartamentos.addItem(s);
		}
		add(jdepartamentos, gbc);
		
		JLabel l4 = new JLabel("Matrícula:");
        gbc.gridy = row++; 
		add(l4, gbc);
		
		matricula = new JTextField();
        gbc.gridy = row++; 
		add(matricula, gbc);  
		matricula.setColumns(10);		
		this.nombre.setText(doctor.getNombre());
		this.apellido.setText(doctor.getApellido());
		String matric = Integer.toString(doctor.getMatricula());
		this.matricula.setText(matric);
		if (doctor.getDepartamento() == null || doctor.getDepartamento().isEmpty()) {
		    this.jdepartamentos.setSelectedItem("Sin departamento");
		} else {
		    this.jdepartamentos.setSelectedItem(doctor.getDepartamento());
		}

		JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        gbc.gridy = row++;
        add(botones, gbc);
        
		guardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Doctor nuevo = new Doctor();
				nuevo.setNombre(nombre.getText());
				nuevo.setApellido(apellido.getText());
				int matricu = Integer.parseInt(matricula.getText());
				nuevo.setMatricula(matricu);
				String depto = (String) jdepartamentos.getSelectedItem();
				nuevo.setDepartamento(depto.equals("Sin departamento") ? null : depto);
				
				DoctorDAO pDAO = new DoctorDAO();
				pDAO.update(nuevo, doctor);
				
				menu.cambiarVista("Doctores");
			}
		});

		cancelar.addActionListener(e -> menu.cambiarVista("Doctores"));
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
