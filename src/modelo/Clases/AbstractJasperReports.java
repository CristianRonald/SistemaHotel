package modelo.Clases;


import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;

import modelo.Datos.ConexionDB;
import modelo.Presentacion.Menu;
import modelo.Presentacion.VentanaGenerarDocumento;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

public class AbstractJasperReports
{
	@SuppressWarnings("unused")
	private static ConexionDB conexion;
	
	private static JasperReport	report;
	private static JasperPrint	reportFilled;
	private static JasperViewer	viewer;

	
	/*public static void createReport( Connection conn, String path )
	{
		try {
			report = (JasperReport) JRLoader.loadObjectFromFile( path );
			reportFilled = JasperFillManager.fillReport( report, null, conn );
		}
		catch( JRException ex ) {
			ex.printStackTrace();
		}
	}*/

	public static void createReport( Connection conn, String path, Map<String,Object> parameters )
	{
		try {
			report = (JasperReport) JRLoader.loadObjectFromFile( path );
			reportFilled = JasperFillManager.fillReport( report, parameters, conn );
		}
		catch( JRException ex ) {
			ex.printStackTrace();
		}
	}
	
	public static void showViewer(String titulo)
	{
		viewer = new JasperViewer( reportFilled,false );
		viewer.setVisible(true);
		viewer.setTitle(titulo);
	}
	public static void showViewerIncustado(String titulo, String image)
	{
   		JRViewer jrViewer = new  JRViewer (reportFilled);
   		JInternalFrame fram = new JInternalFrame(titulo);
   		fram.setFrameIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource(image)));
		fram.setMaximizable(true);
		fram.setClosable(true);
		fram.setIconifiable(true);
		fram.repaint();
		fram.revalidate();
		fram.setSize(900,700);
		fram.getContentPane().add(jrViewer);
		fram.setVisible(true);
		Menu.Desktop.add(fram);
		try {
			fram.setMaximum(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void showViewerModal(String titulo, int w, int h,  boolean modal,String image)
	{
	    JDialog viewer = new JDialog();
	    JasperViewer jrViewer = new JasperViewer(reportFilled, true); 
	    viewer.setTitle(titulo);
	    viewer.setModal(modal);
	    viewer.setSize(w,h); 
	    viewer.setIconImage(Toolkit.getDefaultToolkit().getImage(AbstractJasperReports.class.getResource(image)));
	    viewer.setLocationRelativeTo(null); 
	    viewer.getContentPane().add(jrViewer.getContentPane()); 
		viewer.setVisible(true) ;
	}

	public static void exportToPDF( String destination )
	{
		try { 
			JasperExportManager.exportReportToPdfFile( reportFilled, destination );
		}
		catch( JRException ex ) {
			ex.printStackTrace();
		}
	}

}
