package model;

import java.time.LocalDate;

public class ReportePaciente extends Paciente{
	private LocalDate fechaAlta;
	private LocalDate fechaBaja;
	private String tratamientos;
	private String drogasSuministradas;
	
	public ReportePaciente( ) {}

	public String getNombre() {
		return super.nombre;
	}

	public void setNombre(String nombrePaciente) {
		this.nombre = nombrePaciente;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public LocalDate getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getTratamientos() {
		return tratamientos;
	}

	public void setTratamientos(String tratamientos) {
		this.tratamientos = tratamientos;
	}

	public String getDrogasSuminstradas() {
		return drogasSuministradas;
	}

	public void setDrogasSuminstradas(String drogasSuminstradas) {
		this.drogasSuministradas = drogasSuminstradas;
	}
	
	
}
