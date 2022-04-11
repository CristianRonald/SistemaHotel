package modelo.Otros;


import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;

public class Fondo extends JDesktopPane {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Image imagen;
 
     public Fondo() {
  
       this.setLayout(null); 
       this.setBounds(0, 0, 1500, 700);
		  try {
		       imagen=ImageIO.read(getClass().getResource("/modelo/Images/fondo2.jpg"));
		  }
		 catch (IOException e) {
		   e.printStackTrace();
		  }
  }
 
 public void paintComponent(Graphics g){
     super.paintComponent(g);
     g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
     setOpaque(false);
 }

}