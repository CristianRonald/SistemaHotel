package modelo.Otros;

public class lengthScreem {
	private static int ancho,alto;
	public lengthScreem() {
		this.ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		this.alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	}
	public int  getAncho() {
		return ancho;
	}
	public int getAlto() {
		return alto;
	}

}
