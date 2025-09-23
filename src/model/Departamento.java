package model;

public class Departamento {
	private String nombre;
	private String ubicacion;
	private String nombreMedico;
	private int camasDisponibles;
	private int camasOcupadas;
	
	public Departamento() {}
	
	public Departamento(String nombre, String ubicacion, String nombreMedico, int camasDisponibles, int camasOcupadas) {
		super();
		this.nombre = nombre;
		this.ubicacion = ubicacion;
		this.nombreMedico = nombreMedico;
		this.camasDisponibles = camasDisponibles;
		this.camasOcupadas = camasOcupadas;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getNombreMedico() {
		return nombreMedico;
	}

	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}

	public int getCamasDisponibles() {
		return camasDisponibles;
	}

	public void setCamasDisponibles(int camasDisponibles) {
		this.camasDisponibles = camasDisponibles;
	}

	public int getCamasOcupadas() {
		return camasOcupadas;
	}

	public void setCamasOcupadas(int camasOcupadas) {
		this.camasOcupadas = camasOcupadas;
	}
	
	
}
