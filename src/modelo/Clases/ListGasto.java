package modelo.Clases;

public class ListGasto {
	private int id;
	private String tipo;
	private String descripcion;
	private String fechahora;
	private String documento;
	private String monto;
	public ListGasto(int id, String tipo, String descripcion, String fechahora, String documento, String monto) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.fechahora = fechahora;
		this.documento = documento;
		this.monto = monto;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFechahora() {
		return fechahora;
	}
	public void setFechahora(String fechahora) {
		this.fechahora = fechahora;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}

	
}
