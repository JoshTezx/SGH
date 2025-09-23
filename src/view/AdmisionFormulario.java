package view;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.swing.*;

import org.jdatepicker.impl.*;

import dao.*;
import model.*;

public class AdmisionFormulario extends JPanel {
    private static final long serialVersionUID = 1L;

    private JComboBox<String> jnombres;
    private JDatePickerImpl fechaIngreso;
    private JDatePickerImpl fechaAlta;
    private JComboBox<Integer> jcamas;
    private JList<String> jtratamientos;
    private DefaultListModel<String> tratamientos;
    private Usuario usuario;
    private Main menu;

    public AdmisionFormulario(Usuario usuario, Main menu) {
        this.usuario = usuario;
        this.setMenu(menu);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        int row = 0;
        
        JLabel titulo = new JLabel("Nueva Admisión", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridy = row++; 
        add(titulo, gbc);
        
        gbc.gridy = row++;
        add(label("Paciente:"), gbc);

        jnombres = new JComboBox<>();
        for (String s : new PacienteDAO().readPacientesAll()) jnombres.addItem(s);
        //styleField(jnombres);
        gbc.gridy = row++;
        add(jnombres, gbc);
        
        gbc.gridy = row++;
        add(label("Fecha de ingreso:"), gbc);

        fechaIngreso = createDate();
        //styleField(fechaIngreso);
        gbc.gridy = row++;
        add(fechaIngreso, gbc);

        gbc.gridy = row++;
        add(label("Fecha de salida:"), gbc);

        fechaAlta = createDate();
        //styleField(fechaAlta);
        gbc.gridy = row++;
        add(fechaAlta, gbc);

        gbc.gridy = row++; 
        add(label("Número de cama:"), gbc);

        jcamas = new JComboBox<>();
        for (int c : new CamaDAO().readCamasDisponiblesNumero()) jcamas.addItem(c);
        //styleField(jcamas);
        gbc.gridy = row++;
        add(jcamas, gbc);

        gbc.gridy = row++; 
        add(label("Tratamientos:"), gbc);

        tratamientos = new DefaultListModel<>();
        for (String t : new TratamientoDAO().readTratamientosAll()) tratamientos.addElement(t);
        jtratamientos = new JList<>(tratamientos);
        jtratamientos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(jtratamientos);
        gbc.gridy = row++;
        add(scroll, gbc);

        JLabel info = new JLabel("Usá Ctrl para seleccionar múltiples tratamientos.");
        info.setFont(new Font("Arial", Font.ITALIC, 11));
        info.setForeground(Color.GRAY);
        gbc.gridy = row++;
        add(info, gbc);

        JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        gbc.gridy = row++;
        add(botones, gbc);

        guardar.addActionListener(e -> guardarAdmision());
        cancelar.addActionListener(e -> menu.cambiarVista("Admisiones"));
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return l;
    }

    /*
    private void styleField(JComponent comp) {
        comp.setPreferredSize(new Dimension(280, 28));
        comp.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }
*/
    
    private void guardarAdmision() {
        if (jnombres.getSelectedItem() == null ||
            fechaIngreso.getModel().getValue() == null ||
            jcamas.getSelectedItem() == null ||
            jtratamientos.getSelectedValuesList().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate ingreso = ((Date) fechaIngreso.getModel().getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate alta = null;
        if (fechaAlta.getModel().getValue() != null) {
            alta = ((Date) fechaAlta.getModel().getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if (alta.isBefore(ingreso)) {
                JOptionPane.showMessageDialog(this, "La fecha de salida no puede ser anterior a la de ingreso.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Admision a = new Admision();
        a.setNombrePaciente((String) jnombres.getSelectedItem());
        a.setFechaIngreso(ingreso);
        a.setFechaAlta(alta);
        a.setNumeroCama((Integer) jcamas.getSelectedItem());
        a.setTratamientos(new ArrayList<>(jtratamientos.getSelectedValuesList()));

        try {
            new AdmisionDAO().create(a);
            menu.cambiarVista("Admisiones");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar admisión:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

	
    public AdmisionFormulario(Admision admi, Usuario usuario, Main menu) {
        this.usuario = usuario;
        this.menu = menu;

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        int row = 0;

        JLabel titulo = new JLabel("Modificar Admisión", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridy = row++;
        add(titulo, gbc);

        gbc.gridy = row++; 
        add(label("Paciente:"), gbc);

        jnombres = new JComboBox<>();
        for (String s : new PacienteDAO().readPacientesAll()) jnombres.addItem(s);
        jnombres.setSelectedItem(admi.getNombrePaciente());
        jnombres.setEnabled(false);
        //styleField(jnombres);
        gbc.gridx = row++;
        add(jnombres, gbc);

        gbc.gridy = row++;
        add(label("Fecha de ingreso:"), gbc);

        fechaIngreso = createDate();
        fechaIngreso.getModel().setDate(
            admi.getFechaIngreso().getYear(),
            admi.getFechaIngreso().getMonthValue() - 1,
            admi.getFechaIngreso().getDayOfMonth()
        );
        fechaIngreso.getModel().setSelected(true);
        //styleField(fechaIngreso);
        gbc.gridy = row++;
        add(fechaIngreso, gbc);

        gbc.gridy = row++; 
        add(label("Fecha de salida:"), gbc);

        fechaAlta = createDate();
        if (admi.getFechaAlta() != null) {
            fechaAlta.getModel().setDate(
                admi.getFechaAlta().getYear(),
                admi.getFechaAlta().getMonthValue() - 1,
                admi.getFechaAlta().getDayOfMonth()
            );
            fechaAlta.getModel().setSelected(true);
        }
        //styleField(fechaAlta);
        gbc.gridy = row++;
        add(fechaAlta, gbc);

        gbc.gridy = row++; 
        add(label("Número de cama:"), gbc);

        jcamas = new JComboBox<>();
        for (int c : new CamaDAO().readCamasDisponiblesNumero()) jcamas.addItem(c);
        jcamas.addItem(admi.getNumeroCama()); 
        jcamas.setSelectedItem(admi.getNumeroCama());
        jcamas.setEnabled(false);
        //styleField(jcamas);
        gbc.gridx = row++;
        add(jcamas, gbc);

        gbc.gridy = row++; 
        add(label("Tratamientos:"), gbc);

        tratamientos = new DefaultListModel<>();
        ArrayList<String> lista = new TratamientoDAO().readTratamientosAll();
        for (String t : lista) tratamientos.addElement(t);
        jtratamientos = new JList<>(tratamientos);
        jtratamientos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        ArrayList<String> actuales = admi.getTratamientos();
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            if (actuales.contains(lista.get(i))) indices.add(i);
        }
        int[] selectedIndices = indices.stream().mapToInt(i -> i).toArray();
        jtratamientos.setSelectedIndices(selectedIndices);

        JScrollPane scroll = new JScrollPane(jtratamientos);
        gbc.gridx = row++;
        add(scroll, gbc);

        JLabel info = new JLabel("Usá Ctrl para seleccionar múltiples tratamientos.");
        info.setFont(new Font("Arial", Font.ITALIC, 11));
        info.setForeground(Color.GRAY);
        gbc.gridy = row++;
        add(info, gbc);

        JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        gbc.gridy = row++;
        add(botones, gbc);
        
        guardar.addActionListener(e -> {
            if (fechaIngreso.getModel().getValue() == null ||
                fechaAlta.getModel().getValue() == null ||
                jtratamientos.getSelectedValuesList().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate ingreso = ((Date) fechaIngreso.getModel().getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate alta = ((Date) fechaAlta.getModel().getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            if (alta.isBefore(ingreso)) {
                JOptionPane.showMessageDialog(this, "La fecha de salida no puede ser anterior a la de ingreso.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Admision nuevo = new Admision();
            nuevo.setNombrePaciente(admi.getNombrePaciente());
            nuevo.setFechaIngreso(ingreso);
            nuevo.setFechaAlta(alta);
            nuevo.setNumeroCama(admi.getNumeroCama());
            nuevo.setTratamientos(new ArrayList<>(jtratamientos.getSelectedValuesList()));

            new AdmisionDAO().update(nuevo, admi);
            menu.cambiarVista("Admisiones");
        });

        cancelar.addActionListener(e -> menu.cambiarVista("Admisiones"));
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
