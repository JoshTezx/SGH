package model;

import java.util.ArrayList;

public class Tratamiento {
	private String nombre;
	private int duracion;
	private String posiblesReaccionesDelPaciente;
	private ArrayList<String> medicamentos = new ArrayList<>();
	
	public Tratamiento() {}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String getPosiblesReaccionesDelPaciente() {
		return posiblesReaccionesDelPaciente;
	}

	public void setPosiblesReaccionesDelPaciente(String posiblesReaccionesDelPaciente) {
		this.posiblesReaccionesDelPaciente = posiblesReaccionesDelPaciente;
	}

	public ArrayList<String> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(ArrayList<String> medicamentos) {
		this.medicamentos = medicamentos;
	}

	
}
