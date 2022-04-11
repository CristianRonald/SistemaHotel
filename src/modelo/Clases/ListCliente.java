package modelo.Clases;
public class ListCliente {
	private String codigo;
	private String nombres;
	private String documento;
	private String ruc;
	private String nacionalidad;
	private String telefono;
	public ListCliente(String codigo, String nombres, String documento,String ruc, String nacionalidad, String telefono) {
		super();
		this.codigo = codigo;
		this.nombres = nombres;
		this.documento = documento;
		this.ruc = ruc;
		this.nacionalidad = nacionalidad;
		this.telefono = telefono;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
