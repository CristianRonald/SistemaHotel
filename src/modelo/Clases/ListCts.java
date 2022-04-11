package modelo.Clases;

public class ListCts {
	private String id;
	private String descripcion;
	private String fechahora;
	private String monto;
	private String saldo;
	public ListCts(String id, String descripcion, String fechahora, String monto, String saldo) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.fechahora = fechahora;
		this.monto = monto;
		this.saldo = saldo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	
}
