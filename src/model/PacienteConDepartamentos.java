package model;

public class PacienteConDepartamentos extends Paciente {
	private int cantidadDepartamentos;
	
	public PacienteConDepartamentos() { 
		super(); 
	}
	
	public PacienteConDepartamentos(String nombre, String cuil, int cantidadDepartamentos) {
		super(nombre, null, null, cuil);
		this.cantidadDepartamentos = cantidadDepartamentos;
	}

	public int getCantidadDepartamentos() {
		return cantidadDepartamentos;
	}

	public void setCantidadDepartamentos(int cantidadDepartamentos) {
		this.cantidadDepartamentos = cantidadDepartamentos;
	}
	
	
}
