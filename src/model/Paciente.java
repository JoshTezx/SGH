package model;

public class Paciente {
	protected String nombre;
	protected String direccion;
	protected Sexo sexo; 
	protected String cuil;

	public Paciente() {}

	public Paciente(String nombre, String direccion, Sexo sexo, String cuil) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.sexo = sexo;
		this.cuil = cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public String getCuil() {
		return cuil;
	}

	public void setCuil(String cuil) {
		this.cuil = cuil;
	}
}
