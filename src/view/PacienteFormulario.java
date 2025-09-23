package view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import dao.PacienteDAO;
import model.Paciente;
import model.Sexo;
import model.Usuario;

public class PacienteFormulario extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JTextField nombre;
	private JTextField direccion;
	private JRadioButton sexoM;
	private JRadioButton sexoF;
	private JTextField cuil;
	private Usuario usuario;
	private Main menu;
	
	public PacienteFormulario(Usuario usuario, Main menu) {
		this.usuario = usuario;
		this.setMenu(menu);
		
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        int row = 0;

        JLabel titulo = new JLabel("Nuevo Paciente", SwingConstants.CENTER);
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
		
		JLabel l2 = new JLabel("Dirección:");
		gbc.gridy = row++;
		add(l2, gbc);
		
		direccion = new JTextField();
		gbc.gridy = row++;
		add(direccion, gbc);
		direccion.setColumns(10);
		
		JLabel l3 = new JLabel("Sexo:");
		gbc.gridy = row++;
		add(l3, gbc);
		
        ButtonGroup grupoSexo = new ButtonGroup();
        sexoM = new JRadioButton("Masculino");		
		sexoF = new JRadioButton("Femenino");
        JPanel panelSexo = new JPanel();
        
        grupoSexo.add(sexoM);
        grupoSexo.add(sexoF);
        
        panelSexo.add(sexoM);
        panelSexo.add(sexoF);
		gbc.gridy = row++;
		add(panelSexo, gbc);
        
		JLabel l6 = new JLabel("CUIL:");
		gbc.gridy = row++;
		add(l6, gbc);
		
		cuil = new JTextField();
		gbc.gridy = row++;
		add(cuil, gbc);   
		cuil.setColumns(10);	
		
		JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        gbc.gridy = row++;
        add(botones, gbc);
        
		guardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Paciente p = new Paciente();
				PacienteDAO pDAO = new PacienteDAO();
						
				p.setNombre(nombre.getText());
				p.setDireccion(direccion.getText());
				if (sexoM.isSelected()) {
					p.setSexo(Sexo.Masculino);
				}
				if(sexoF.isSelected()) {
					p.setSexo(Sexo.Femenino);
				}
				p.setCuil(cuil.getText());
				
				pDAO.create(p);
				
				menu.cambiarVista("Pacientes");
			}
		});
		
		cancelar.addActionListener(e -> menu.cambiarVista("Pacientes"));
	}
	
	public PacienteFormulario(Paciente pacient, Usuario usuario, Main menu) {
		this.usuario = usuario;
		this.setMenu(menu);
		
		setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        int row = 0;

        JLabel titulo = new JLabel("Modificar Paciente", SwingConstants.CENTER);
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
		
		JLabel l2 = new JLabel("Dirección:");
        gbc.gridy = row++; 
		add(l2, gbc);
		
		direccion = new JTextField();
        gbc.gridy = row++; 
		add(direccion, gbc);
		direccion.setColumns(10);
		
		JLabel l3 = new JLabel("Sexo:");
        gbc.gridy = row++; 
		add(l3, gbc);
		
		ButtonGroup grupoSexo = new ButtonGroup();
        sexoM = new JRadioButton("Masculino");		
		sexoF = new JRadioButton("Femenino");
        JPanel panelSexo = new JPanel();
        
        grupoSexo.add(sexoM);
        grupoSexo.add(sexoF);
        
        panelSexo.add(sexoM);
        panelSexo.add(sexoF);
		gbc.gridy = row++;
		add(panelSexo, gbc);
        
		JLabel l6 = new JLabel("CUIL:");
        gbc.gridy = row++; 
		add(l6, gbc);
		
		cuil = new JTextField();
        gbc.gridy = row++; 
		add(cuil, gbc);   
		cuil.setColumns(10);
		
		this.nombre.setText(pacient.getNombre());
		this.direccion.setText(pacient.getDireccion());
		if(pacient.getSexo() == Sexo.Masculino) {
			sexoM.setSelected(true);
		}
		else if(pacient.getSexo() == Sexo.Femenino) {
			sexoF.setSelected(true);
		}
		this.cuil.setText(pacient.getCuil());
		
		JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        gbc.gridy = row++;
        add(botones, gbc);
		
		guardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Paciente nuevo = new Paciente();
				PacienteDAO pDAO = new PacienteDAO();
						
				nuevo.setNombre(nombre.getText());
				nuevo.setDireccion(direccion.getText());
				if (sexoM.isSelected()) {
					nuevo.setSexo(Sexo.Masculino);
				}
				else if (sexoF.isSelected()) {
					nuevo.setSexo(Sexo.Femenino);
				}
				nuevo.setCuil(cuil.getText());
				
				pDAO.update(nuevo, pacient);
				
				menu.cambiarVista("Pacientes");
			}
		});

		cancelar.addActionListener(e -> menu.cambiarVista("Pacientes"));
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
