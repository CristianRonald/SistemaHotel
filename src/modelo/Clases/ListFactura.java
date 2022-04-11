package modelo.Clases;

public class ListFactura {
	private String empresa;
	private String dirempresa;
	private String rucempresa;
	private String telempresa;
	
	private String codigo;
	private String descripcion;
	private String precio;
	private String valor;
	
	private double subtotal;
	private double dsct;
	private double igv;
	private double total;
	
	public ListFactura(String empresa, String dirempresa, String rucempresa, String telempresa, String codigo,
			String descripcion, String precio, String valor, double subtotal, double dsct, double igv, double total) {
		super();
		this.empresa = empresa;
		this.dirempresa = dirempresa;
		this.rucempresa = rucempresa;
		this.telempresa = telempresa;
		this.codigo = codigo;
		this.setDescripcion(descripcion);
		this.precio = precio;
		this.valor = valor;
		this.subtotal = subtotal;
		this.dsct = dsct;
		this.igv = igv;
		this.total = total;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getDirempresa() {
		return dirempresa;
	}

	public void setDirempresa(String dirempresa) {
		this.dirempresa = dirempresa;
	}

	public String getRucempresa() {
		return rucempresa;
	}

	public void setRucempresa(String rucempresa) {
		this.rucempresa = rucempresa;
	}

	public String getTelempresa() {
		return telempresa;
	}

	public void setTelempresa(String telempresa) {
		this.telempresa = telempresa;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getDsct() {
		return dsct;
	}

	public void setDsct(double dsct) {
		this.dsct = dsct;
	}

	public double getIgv() {
		return igv;
	}

	public void setIgv(double igv) {
		this.igv = igv;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
