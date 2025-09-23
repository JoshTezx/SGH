package model;

public class CamaOcupada extends Cama{
	private String paciente;
	private String cuil;
	
	public CamaOcupada() {}
	
	public int getNumero() {
		return numero;
	}
	
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public String getDepartamento() {
		return departamento;
	}
	
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	public String getPaciente() {
		return paciente;
	}
	
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}
	
	public String getCuil() {
		return cuil;
	}
	
	public void setCuil(String cuil) {
		this.cuil = cuil;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	
}
