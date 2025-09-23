package view;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import javax.swing.*;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import dao.MedicamentoDAO;
import model.Medicamento;
import model.Usuario;

public class MedicamentoFormulario extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JTextField nombre;
	private JDatePickerImpl fabricacion;
	private JDatePickerImpl vencimiento;
	private JTextField tipo;
	private Usuario usuario;
	private Main menu;
	
	public MedicamentoFormulario(Usuario usuario, Main menu) {
		this.usuario = usuario;
		this.setMenu(menu);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        int row = 0;
		
        JLabel titulo = new JLabel("MEDICAMENTO - NUEVO REGISTRO", SwingConstants.CENTER);
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
		
		JLabel l2 = new JLabel("Fabricación:");
		gbc.gridy = row++;
		add(l2, gbc);
		
		fabricacion = createDate();
		gbc.gridy = row++;
		add(fabricacion, gbc);
		
		JLabel l3 = new JLabel("Vencimiento:");
		gbc.gridy = row++;
		add(l3, gbc);
		
		vencimiento = createDate();
		gbc.gridy = row++;
		add(vencimiento, gbc);
		
		JLabel l4 = new JLabel("Tipo:");
		gbc.gridy = row++;
		add(l4, gbc);
		
		tipo = new JTextField();
		gbc.gridy = row++;
		add(tipo, gbc);   
		tipo.setColumns(10);		
		
		JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        gbc.gridy = row++;
        add(botones, gbc);
        
		guardar.addActionListener(e ->{
			try {
				if (nombre.getText().isEmpty() || tipo.getText().isEmpty() || 
					fabricacion.getModel().getValue() == null || vencimiento.getModel().getValue() == null) {
					JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				LocalDate f = ((Date) fabricacion.getModel().getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
				LocalDate v = ((Date) vencimiento.getModel().getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

				if (v.isBefore(f)) {
					JOptionPane.showMessageDialog(null, "La fecha de vencimiento no puede ser anterior a la de fabricación.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (f.isAfter(LocalDate.now())) {
					JOptionPane.showMessageDialog(null, "Fecha inválida", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Medicamento m = new Medicamento();
				m.setNombreDroga(nombre.getText());
				m.setFabricacion(f);
				m.setVencimiento(v);
				m.setTipo(tipo.getText());

				MedicamentoDAO mDAO = new MedicamentoDAO();
				mDAO.create(m);

				menu.cambiarVista("Medicamentos");

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error al guardar el medicamento:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace(); 
			}
		});
		
		cancelar.addActionListener(e -> menu.cambiarVista("Medicamentos"));
	}
	
	public MedicamentoFormulario(Medicamento medic, Usuario usuario, Main menu) {
		this.usuario = usuario;
		this.setMenu(menu);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        int row = 0;

        JLabel titulo = new JLabel("MEDICAMENTO - ACTUALIZAR REGISTRO", SwingConstants.CENTER);
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
		
		JLabel l2 = new JLabel("Fabricación:");
		gbc.gridy = row++;
		add(l2, gbc);
		
		fabricacion = createDate();
		gbc.gridy = row++;
		add(fabricacion, gbc);
		
		JLabel l3 = new JLabel("Vencimiento:");
		gbc.gridy = row++;
		add(l3, gbc);
		
		vencimiento = createDate();
		gbc.gridy = row++;
		add(vencimiento, gbc);
		
		JLabel l4 = new JLabel("Tipo:");
		gbc.gridy = row++;
		add(l4, gbc);
		
		tipo = new JTextField();
		gbc.gridy = row++;
		add(tipo, gbc);   
		tipo.setColumns(10);		
		
		this.nombre.setText(medic.getNombreDroga());
		this.tipo.setText(medic.getTipo());
		
		fabricacion.getModel().setDate(
            medic.getFabricacion().getYear(),
            medic.getFabricacion().getMonthValue() - 1,
            medic.getFabricacion().getDayOfMonth()
        );
        fabricacion.getModel().setSelected(true);

        vencimiento.getModel().setDate(
    		medic.getVencimiento().getYear(),
    		medic.getVencimiento().getMonthValue() - 1,
            medic.getVencimiento().getDayOfMonth()
        );
        vencimiento.getModel().setSelected(true);
		
	        
        JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        gbc.gridy = row++;
        add(botones, gbc);

        guardar.addActionListener(e->{
        	try {
				if (nombre.getText().isEmpty() || tipo.getText().isEmpty() || 
					fabricacion.getModel().getValue() == null || vencimiento.getModel().getValue() == null) {
					JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				LocalDate f = ((Date) fabricacion.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate v = ((Date) vencimiento.getModel().getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				if (v.isBefore(f)) {
					JOptionPane.showMessageDialog(null, "La fecha de vencimiento no puede ser anterior a la de fabricación.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (f.isAfter(LocalDate.now())) {
					JOptionPane.showMessageDialog(null, "Fecha inválida", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Medicamento nuevo = new Medicamento();
				nuevo.setNombreDroga(nombre.getText());
				nuevo.setFabricacion(f);
				nuevo.setVencimiento(v);
				nuevo.setTipo(tipo.getText());

				MedicamentoDAO mDAO = new MedicamentoDAO();
				mDAO.update(nuevo, medic);

				menu.cambiarVista("Medicamentos");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error al modificar el medicamento:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		});
		
		cancelar.addActionListener(e -> menu.cambiarVista("Medicamentos"));
	}

	private JDatePickerImpl createDate() {
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Hoy");
		p.put("text.month", "Mes");
		p.put("text.year", "Año");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		return new JDatePickerImpl(datePanel, new DateComponentFormatter());
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
