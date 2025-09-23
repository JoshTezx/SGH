package model;

public class Cama {
	protected int numero;
	protected Estado estado;
	protected String departamento;
	
	public Cama() {}
	
	public Cama(int numero, Estado estado, String departamentoNombre) {
		super();
		this.numero = numero;
		this.estado = estado;
		this.departamento = departamentoNombre;
	}
	
	public int getNumero() {
		return numero;
	}
	
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public Estado getEstado() {
		return estado;
	}
	
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public String getDepartamentoNombre() {
		return departamento;
	}
	
	public void setDepartamentoNombre(String departamentoNombre) {
		this.departamento = departamentoNombre;
	}
}
