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
import model.Departamento;
import model.Usuario;

public class DepartamentoFormulario extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JTextField nombre;
	private JTextField ubicacion;
	private JComboBox<String> jdoctores;
	private ArrayList<String> doctores =  new ArrayList<String>();
	private JTextField disponibles;
	private Usuario usuario;
	private Main menu;
	
	public DepartamentoFormulario(Usuario usuario, Main menu) {	
		this.setUsuario(usuario);
		this.setMenu(menu);
		
		setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        int row = 0;
        
        JLabel titulo = new JLabel("Nuevo Departamento", SwingConstants.CENTER);
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
		
		JLabel l2 = new JLabel("Ubicación:");
        gbc.gridy = row++; 
		add(l2, gbc);
		
		ubicacion = new JTextField();
        gbc.gridy = row++; 
		add(ubicacion, gbc);
		ubicacion.setColumns(10);
		
		JLabel l3 = new JLabel("Jefe:");
        gbc.gridy = row++; 
		add(l3, gbc);
		
		jdoctores = new JComboBox<String>();
        gbc.gridy = row++; 
		add(jdoctores, gbc);

		DoctorDAO dDAO = new DoctorDAO();
		doctores = dDAO.readNombreDoctores();
		
		jdoctores.addItem("Sin doctor");
		for(String s : doctores) {
			jdoctores.addItem(s);
		}
		add(jdoctores, gbc);
		
		JLabel l4 = new JLabel("Cantidad de camas:");
        gbc.gridy = row++; 
		add(l4, gbc);
		
		disponibles = new JTextField();
        gbc.gridy = row++; 
		add(disponibles, gbc);  
		disponibles.setColumns(10);
		
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
				    ubicacion.getText().trim().isEmpty() || 
				    disponibles.getText().trim().isEmpty()) {
				    //ocupadas.getText().trim().isEmpty()) {
				    
					JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos antes de guardar.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
					return; 
				}
				
				try {
					int camasDisp = Integer.parseInt(disponibles.getText().trim());
					//int camasOcup = Integer.parseInt(ocupadas.getText().trim());
					
					if (camasDisp < 0) { //|| camasOcup < 0
		                JOptionPane.showMessageDialog(null, "Las cantidades de camas no pueden ser negativas.", "Valor inválido", JOptionPane.WARNING_MESSAGE);
		                return;
		            }
					
					Departamento d = new Departamento();		
					d.setNombre(nombre.getText().trim());
					d.setUbicacion(ubicacion.getText().trim());
					String doc = (String) jdoctores.getSelectedItem();
					d.setNombreMedico(doc.equals("Sin doctor") ? null : doc);
					d.setCamasDisponibles(camasDisp);
					//d.setCamasOcupadas(camasOcup);
					
					DepartamentoDAO dDAO = new DepartamentoDAO();
					dDAO.create(d);
					
					int id = dDAO.idDepartamentoNoExistente(d); //Obtiene el id y realiza dDAO.create
					//dDAO.create(d);
					
					if(id != -1) {
						dDAO.createCamas(id, camasDisp);
					}
					
					menu.cambiarVista("Departamentos");
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Las camas deben ser números válidos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
				} catch (Exception ex) {
		            JOptionPane.showMessageDialog(null, "Error al guardar el departamento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});

		cancelar.addActionListener(e -> menu.cambiarVista("Departamentos"));
	}
	
	public DepartamentoFormulario(Departamento depart, Usuario usuario, Main menu) {
		this.usuario = usuario;
		this.menu = menu;
		
		setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        int row = 0;
        
        JLabel titulo = new JLabel("Nuevo Departamento", SwingConstants.CENTER);
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
		
		JLabel l2 = new JLabel("Ubicación:");
        gbc.gridy = row++; 
		add(l2, gbc);
		
		ubicacion = new JTextField();
        gbc.gridy = row++; 
		add(ubicacion, gbc);
		ubicacion.setColumns(10);
		
		JLabel l3 = new JLabel("Jefe:");
        gbc.gridy = row++; 
		add(l3, gbc);
		
		jdoctores = new JComboBox<String>();
        gbc.gridy = row++; 
		add(jdoctores, gbc);

		DoctorDAO dDAO = new DoctorDAO();
		doctores = dDAO.readNombreDoctores();
		
		jdoctores.addItem("Sin doctor");
		for(String s : doctores) {
			jdoctores.addItem(s);
		}
		add(jdoctores, gbc);
		
		JLabel l4 = new JLabel("Cantidad de camas:");
        gbc.gridy = row++; 
		add(l4, gbc);
		
		disponibles = new JTextField();
        gbc.gridy = row++; 
		add(disponibles, gbc);  
		disponibles.setColumns(10);
		
		this.nombre.setText(depart.getNombre());
		this.ubicacion.setText(depart.getUbicacion());
		if (depart.getNombreMedico() == null || depart.getNombreMedico().isEmpty()) {
		    this.jdoctores.setSelectedItem("Sin doctor");
		} else {
		    this.jdoctores.setSelectedItem(depart.getNombreMedico());
		}

		String disp = Integer.toString(depart.getCamasDisponibles());
		this.disponibles.setText(disp);
		//String ocup = Integer.toString(depart.getCamasOcupadas());
		//this.ocupadas.setText(ocup);
		
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
					ubicacion.getText().trim().isEmpty() ||
			        disponibles.getText().trim().isEmpty()) {
			        //ocupadas.getText().trim().isEmpty()) {

			        JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos antes de guardar.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
			        return;
			    }
				
				try {
					int camasDisp = Integer.parseInt(disponibles.getText().trim());
		            //int camasOcup = Integer.parseInt(ocupadas.getText().trim());

		            if (camasDisp < 0) { //|| camasOcup < 0) {
		                JOptionPane.showMessageDialog(null, "Las cantidades de camas no pueden ser negativas.", "Valor inválido", JOptionPane.WARNING_MESSAGE);
		                return;
		            }
		            
		            int camasOriginales = depart.getCamasDisponibles();
		            if(camasDisp < camasOriginales) {
		            	JOptionPane.showMessageDialog(null, "No se puede reducir la cantidad de camas.", "Actualización no permitida", JOptionPane.WARNING_MESSAGE); return;
		            }
					
		            Departamento nuevo = new Departamento();
		            nuevo.setNombre(nombre.getText().trim());
		            nuevo.setUbicacion(ubicacion.getText().trim());
		            String doc = (String) jdoctores.getSelectedItem();
		            nuevo.setNombreMedico(doc.equals("Sin doctor") ? null : doc);
		            nuevo.setCamasDisponibles(camasDisp);
		            //nuevo.setCamasOcupadas(camasOcup);
					
					DepartamentoDAO dDAO = new DepartamentoDAO();
					dDAO.update(nuevo, depart);
					
					menu.cambiarVista("Departamentos");
					
				} catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(null, "Las camas deben ser números válidos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(null, "Error al guardar el departamento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});
		
		cancelar.addActionListener(e -> menu.cambiarVista("Departamentos"));

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
