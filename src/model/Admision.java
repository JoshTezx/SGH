package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Admision {
	private String nombrePaciente;
	private LocalDate fechaIngreso;
	private LocalDate fechaAlta;
	private int numeroCama;
	private ArrayList<String> tratamientos;
	
	public Admision() {}
	
	public Admision(String nombrePaciente, LocalDate fechaIngreso, LocalDate fechaAlta, int numeroCama, ArrayList<String> tratamientos) {
		super();
		this.nombrePaciente = nombrePaciente;
		this.fechaIngreso = fechaIngreso;
		this.fechaAlta = fechaAlta;
		this.numeroCama = numeroCama;
		this.tratamientos = tratamientos;
	}

	public String getNombrePaciente() {
		return nombrePaciente;
	}

	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public int getNumeroCama() {
		return numeroCama;
	}

	public void setNumeroCama(int numeroCama) {
		this.numeroCama = numeroCama;
	}

	public ArrayList<String> getTratamientos() {
		return tratamientos;
	}

	public void setTratamientos(ArrayList<String> tratamientos) {
		this.tratamientos = tratamientos;
	}
}
