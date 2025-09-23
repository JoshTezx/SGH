package model;

import java.time.LocalDate;

public class Medicamento {
	private String nombreDroga;
	private LocalDate fabricacion;
	private LocalDate vencimiento;
	private String tipo;
	
	public Medicamento() {}
	
	public Medicamento(String nombreDroga, LocalDate fabricacion, LocalDate vencimiento, String tipo) {
		super();
		this.nombreDroga = nombreDroga;
		this.fabricacion = fabricacion;
		this.vencimiento = vencimiento;
		this.tipo = tipo;
	}

	public String getNombreDroga() {
		return nombreDroga;
	}

	public void setNombreDroga(String nombreDroga) {
		this.nombreDroga = nombreDroga;
	}

	public LocalDate getFabricacion() {
		return fabricacion;
	}

	public void setFabricacion(LocalDate fabricacion) {
		this.fabricacion = fabricacion;
	}

	public LocalDate getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(LocalDate vencimiento) {
		this.vencimiento = vencimiento;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
