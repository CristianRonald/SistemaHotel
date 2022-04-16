package modelo.Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;


public class ConexionDB {
	
	private static String servidor = "jdbc:mysql://localhost:3306/BD_HOTEL";
	private static String username = "root";
	private static String password = "cris2021";
	private static String driver="com.mysql.jdbc.Driver";
	private static  Connection conexionDB;
	
	public ConexionDB() {
		try {
			Class.forName(driver);//Levanto el driver
			conexionDB=DriverManager.getConnection(servidor, username, password);
			  //JOptionPane.showMessageDialog(null, "conexian realizada con axito","SOFLE_HOTEL",JOptionPane.INFORMATION_MESSAGE); 
		} catch (ClassNotFoundException | SQLException  e) {
			JOptionPane.showMessageDialog(null, "Conexian fallida - BD MySql","SOFLE_HOTEL",JOptionPane.ERROR_MESSAGE); 
			System.exit(0);
		}
	}
	
	public Connection gConnection() {
		return conexionDB;
	}

	public void DesconectarDB() {
		conexionDB = null;
	}
}
