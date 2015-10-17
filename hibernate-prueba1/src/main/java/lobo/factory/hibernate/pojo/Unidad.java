package lobo.factory.hibernate.pojo;

public class Unidad {
	private Long idUnidad;
	private String codUnidad;
	private String nombre;
	private String descripcion;
	public Long getIdUnidad() {
		return idUnidad;
	}
	public void setIdUnidad(Long idUnidad) {
		this.idUnidad = idUnidad;
	}
	public String getCodUnidad() {
		return codUnidad;
	}
	public void setCodUnidad(String codUnidad) {
		this.codUnidad = codUnidad;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
