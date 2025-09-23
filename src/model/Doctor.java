package model;

public class Doctor {
	private String nombre;
	private String apellido;
	private int matricula;
	private String departamento;

	public Doctor() {}

	public Doctor(String nombre, String apellido, int matricula, String departamento) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.matricula = matricula;
		this.departamento = departamento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getMatricula() {
		return matricula;
	}

	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
}
