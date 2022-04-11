package modelo.Clases;
public class ListVistaServicio {
	private String codigo;
	private String fecha;
	private String nro;
	private String alojamiento;
	private String servicios;
	private String vitrina;
	private String detalle;
	private String turno;
	public ListVistaServicio(String codigo, String fecha, String nro, String alojamiento, String servicios,
			String vitrina, String detalle, String turno) {
		super();
		this.codigo = codigo;
		this.fecha = fecha;
		this.nro = nro;
		this.alojamiento = alojamiento;
		this.servicios = servicios;
		this.vitrina = vitrina;
		this.detalle = detalle;
		this.turno = turno;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getNro() {
		return nro;
	}
	public void setNro(String nro) {
		this.nro = nro;
	}
	public String getAlojamiento() {
		return alojamiento;
	}
	public void setAlojamiento(String alojamiento) {
		this.alojamiento = alojamiento;
	}
	public String getServicios() {
		return servicios;
	}
	public void setServicios(String servicios) {
		this.servicios = servicios;
	}
	public String getVitrina() {
		return vitrina;
	}
	public void setVitrina(String vitrina) {
		this.vitrina = vitrina;
	}
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getTurno() {
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}

}
