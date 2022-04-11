package modelo.Presentacion;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AbrirFoto {
	public  JInternalFrame frame;
	public final JPanel contentPanel = new JPanel();
	public static JFileChooser fileChooser;
	public AbrirFoto() {
		frame = new JInternalFrame();
		frame.setTitle("Abrir");
		frame.setBounds(100, 100, 642, 441);
		frame.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		fileChooser = new JFileChooser();
		fileChooser.setBounds(0, 0, 623, 397);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Formatos de Archivos JPEG-PNG(*.JPG;*.JPEG;*.PNG)", "jpg","jpeg","png");
		fileChooser.setFileFilter(filter);
		File ruta = new File("D:/images");
		fileChooser.setCurrentDirectory(ruta);
		
		contentPanel.add(fileChooser);
	}
}
