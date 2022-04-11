
package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import modelo.Clases.TableCellRendererColor;
import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;

public class VentanaCajaBalance implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public  JInternalFrame frame;
	private JPanel  panelBtn,panelLst;
	public  JLabel		lbl9;
	public JTextField 			textBus;
	private JButton  			buttonNuevo,buttonEditar,buttonEliminar,buttonExportar,buttonSalir;
	JComboBox<String> cbBus,cbBusIni;
    
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	
	public Integer totalitems;
	public static int CONTAR_VENTANA_BALANCE=0;
	
	public static String id="",detalle="";
	
	private String consultar="";
	private JFormattedTextField TextIngresos;
	private JLabel lblIngresos;
	private JFormattedTextField TextEgresos;
	private JLabel lblEgresos;
	private JFormattedTextField TextSaldo;
	private JLabel label_2;
	
	// constructor
	public VentanaCajaBalance() {
		super();
		frameCajaBalance();
		crearPanel();
		crearButtons();
		crearTable();
		crearComboBox();
		crearTextFields();
		crearLabels();
		panelLst.setVisible(true); // PANEL LISTA
		llenarcbBuscar();
		
		creoEmcabezado();
		llenarTableCaja();
		
		CONTAR_VENTANA_BALANCE ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
		
		Menu.Desktop.add(frame);
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
    	frame.setLocation(x, y);
	}
	public void frameCajaBalance() {
		frame = new JInternalFrame();
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				llenarTableCaja();
			}
			//@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				CONTAR_VENTANA_BALANCE=0;
			}
		});
		frame.setTitle("Movimientos de caja chica");
		frame.setFrameIcon(new ImageIcon(VentanaCajaBalance.class.getResource("/modelo/Images/cajaMov.png")));
		frame.setClosable(true);
		frame.setBounds(100, 100, 844, 466);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelBtn = new JPanel();
		panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelBtn.setBounds(10, 375, 808, 49);
		frame.getContentPane().add(panelBtn);
		panelBtn.setLayout(null);
		
		panelLst = new JPanel();
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 5, 808, 371); //10, 333, 659, 268
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	void creoEmcabezado(){
	}
	public void llenarTableCaja () {
		id="";detalle="";
		
		float MONTO_E=0,MONTO_S=0;
		float APERTURA=0,SALDO=0;
		
		String consultaAperura="",consultaPagos="",consultaVenta=""; 
		conexion = new ConexionDB();
        try {
        		// CONSULTO APERUTA DE TURNO
    			int ESTADO=0;
    			//int id=0;
    			//String FECHA_APE="",FECHA_CIE="";
    			Statement statement = conexion.gConnection().createStatement();
    			ResultSet resultSet = statement.executeQuery("Select * from CAJA_APE_CIE order by Id_ApeCie desc limit 0,1");
    			if (resultSet.next()== true) {
    				//id=(Integer.parseInt(resultSet.getString("Id_ApeCie")));
    				ESTADO=Integer.parseInt(resultSet.getString("EstadoApeCie"));
    				//FECHA_APE=resultSet.getString("FechaApe");
    				//FECHA_CIE=resultSet.getString("FechaCie");
    			}
    			// END CONSULTO APERUTA DE TURNO
    			statement.close();
        	
        	totalitems=0;
        	if (ESTADO==1) {
        		/*if (FECHA_APE.trim().equals(FECHA_CIE.trim())||FECHA_APE.trim().equals(Menu.date.trim())) {
		     	   if (textBus.getText().isEmpty()) {
		     		   consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and FechaApe ='" + Menu.date.trim() + "'"; 
		     		   consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta ='" + Menu.date.trim() + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra ='" + Menu.date.trim() + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultar="Select * from CAJA where FechaCaj='" + Menu.date + "'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'";
		     		   
		     		   if(cbBus.getSelectedItem()=="INGRESO") {
		     			   consultar="Select * from CAJA where FechaCaj='" + Menu.date + "'and TipoMov ='"+cbBus.getSelectedItem()+"'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'";
		     		   }
		     		   if(cbBus.getSelectedItem()=="EGRESO") {
		     			   consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and FechaApe='" + " " +  "'";
		     			   consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta='" + " "+ "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     			   consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra='" + " " + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   	   consultar="Select * from CAJA where FechaCaj='" + Menu.date + "'and TipoMov ='"+cbBus.getSelectedItem()+"'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   }
		     	   }else{
		     		   consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and FechaApe='" + Menu.date.trim() +  "'";
		     		   consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta='" + Menu.date + "'and DescripcionCta like'" + textBus.getText() + "%'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra='" + Menu.date + "'and C.NombreCli like'" + textBus.getText() + "%'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultar="Select * from CAJA where FechaCaj='" + Menu.date + "'and DescripcionCaj like'" + textBus.getText() + "%'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   
		     		   if(cbBus.getSelectedItem()=="INGRESO") {
		     			   consultar="Select * from CAJA where FechaCaj='" + Menu.date + "'and TipoMov ='"+cbBus.getSelectedItem()+"'and DescripcionCaj like'" + textBus.getText() + "%'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   }
			     		if(cbBus.getSelectedItem()=="EGRESO") {
			     			consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and FechaApe='" + " " +  "'";
			     			consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta='" + " "+ "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
			     			consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra='" + " " + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
			     		   	consultar="Select * from CAJA where FechaCaj='" + Menu.date + "'and TipoMov ='"+cbBus.getSelectedItem()+"'and DescripcionCaj like'" + textBus.getText() + "%'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
			     		}
		     	   }
        		} 
        		
        		if (!FECHA_APE.trim().equals(Menu.date.trim())) {
 		     	   if (textBus.getText().isEmpty()) {
		     		   consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and FechaApe BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date.trim() + "'"; 
		     		   consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date.trim() + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date.trim() + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'";  
		     		   consultar="Select * from CAJA where FechaCaj='" + Menu.date + "'or FechaCaj='" + VentanaLogin.FechaAyer.trim() + "'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   
		     		   if(cbBus.getSelectedItem()=="INGRESO") {
		     			   consultar="Select * from CAJA where FechaCaj BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and TipoMov ='"+cbBus.getSelectedItem()+"'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   }
		     		   if(cbBus.getSelectedItem()=="EGRESO") {
		     			   consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and FechaApe='" + " " +  "'";
		     			   consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta='" + " "+ "'";
		     			   consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra='" + " " + "'";
		     		   	   consultar="Select * from CAJA where FechaCaj BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and TipoMov ='"+cbBus.getSelectedItem()+"'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   }
		     	   }else{
		     		   consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and FechaApe BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'";
		     		   consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and DescripcionCta like'" + textBus.getText() + "%'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and C.NombreCli like'" + textBus.getText() + "%'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultar="Select * from CAJA where FechaCaj BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and DescripcionCaj like'" + textBus.getText() + "%'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   
		     		   if(cbBus.getSelectedItem()=="INGRESO") {
		     			   consultar="Select * from CAJA where FechaCaj BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and TipoMov ='"+cbBus.getSelectedItem()+"'and DescripcionCaj like'" + textBus.getText() + "%'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   }
			     		if(cbBus.getSelectedItem()=="EGRESO") {
			     			consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and FechaApe='" + " " +  "'";
			     			consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta='" + " "+ "'";
			     			consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra='" + " " + "'";
			     		   	consultar="Select * from CAJA where FechaCaj BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and TipoMov ='"+cbBus.getSelectedItem()+"'and DescripcionCaj like'" + textBus.getText() + "%'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
			     		}
		     	   }
        		}*/
        		
        		
        		consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
        		if (textBus.getText().isEmpty()) {
		     		   consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and IdTurnoP='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.EstadoTra='" + 1 + "'and IdTurnoT='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultar="Select * from CAJA where Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'";
		     		   
		     		   if(cbBus.getSelectedItem()=="INGRESO") {
		     			   consultar="Select * from CAJA where TipoMov ='"+cbBus.getSelectedItem()+"'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'";
		     		   }
		     		   if(cbBus.getSelectedItem()=="EGRESO") {
		     			   //consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + "-1" +  "'";
		     			   consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and IdTurnoP='" + "-1" + "'"; // CONSULTA CERO
		     			   consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and IdTurnoT='" +"-1" + "'"; // CONSULTA CERO
		     		   	   consultar="Select * from CAJA where TipoMov ='"+cbBus.getSelectedItem()+"'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   }
		     	   }else{
		     		   consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and DescripcionCta like'" + textBus.getText() + "%'and IdTurnoP='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.EstadoTra='" + 1 + "'and C.NombreCli like'" + textBus.getText() + "%'and IdTurnoT='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   consultar="Select * from CAJA where DescripcionCaj like'" + textBus.getText() + "%'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   
		     		   if(cbBus.getSelectedItem()=="INGRESO") {
		     			   consultar="Select * from CAJA where TipoMov ='"+cbBus.getSelectedItem()+"'and DescripcionCaj like'" + textBus.getText() + "%'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
		     		   }
			     		if(cbBus.getSelectedItem()=="EGRESO") {
			     			//consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + "-1" +  "'";
			     			consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and IdTurnoP='" + "-1" + "'"; // CONSULTA CERO
			     			consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and IdTurnoT='" + "-1" + "'"; // CONSULTA CERO
			     		   	consultar="Select * from CAJA where TipoMov ='"+cbBus.getSelectedItem()+"'and DescripcionCaj like'" + textBus.getText() + "%'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
			     		}
		     	   }
        	}
		   model= new DefaultTableModel();
		   model.addColumn("Nro mov.");
	       model.addColumn("F.Emisión");
	       model.addColumn("T.M.");
		   model.addColumn("Descripción");
		   model.addColumn("Documento");
		   model.addColumn("Ingresos S/.");
		   model.addColumn("Egresos S/.");
		   model.addColumn("      Saldo S/.");
	      // model.addColumn("User");
		       
		   //tableList.setTableHeader(null);
	 		   String []datoApe= new String[8];
	 		   tableList.setModel(model);
	     	  
	 		 if (cbBusIni.getSelectedIndex()==1) {
 	      	   Statement sttt = conexion.gConnection().createStatement();
 	  		   ResultSet rsss=sttt.executeQuery(consultaAperura);//"Select* from CAJA_APE_CIE where FechaApeCie='" + Menu.date.trim() +  "'");
 	  		   if (rsss.next()==true) {
 	  			 APERTURA=Float.parseFloat(rsss.getString("MontoSolApe"));
 	  			 SALDO=APERTURA;

 	  			datoApe[0]="";
	            datoApe[1]=""+ Menu.formatoFechaString.format(rsss.getDate("FechaApe"));
	            datoApe[2]="";
	            datoApe[3]="         *********** | EFECTIVO INICIAL | ***********";
	            datoApe[4]="";
	            datoApe[5]="";
	            datoApe[6]="";
	            datoApe[7]="S/. "+(Menu.formateadorCurrency.format(APERTURA) +" ");
	            model.addRow(datoApe);
	            tableList.setModel(model);
 	  		   }
 	  		   sttt.close();
	            // ESPACIO
 	  		   	datoApe[0]="";
	            datoApe[1]="";
	            datoApe[2]="";
	            datoApe[3]=" ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::";
	            datoApe[4]="";
	            datoApe[5]="";
	            datoApe[6]="";
	            datoApe[7]="";
	            model.addRow(datoApe);
	            tableList.setModel(model);
	            // END ESPACIO
	 		 }   
		        
	          //************************************************** INGRESOS POR PAGOS
	           Statement sp = conexion.gConnection().createStatement();
	           ResultSet rp=sp.executeQuery(consultaPagos);
		       String []datP= new String[8];
			   	if(!rp.equals("")) {
			   		datP[3]=" INGRESOS X HOSPEDAJE Y CONSUMO ";
			        model.addRow(datP);
			   }
		        while(rp.next()==true) {
			        	datP[1]=" "+Menu.formatoFechaString.format(rp.getDate("FechaDetCta"));
			        	datP[2]=" INGRESO";
			        	datP[3]=" H-"+rp.getString("NroHabCta") + " " +rp.getString("DescripcionCta") + " " +rp.getString("ResDetCta");
			        	datP[4]=" "+Menu.formatid_7.format(rp.getInt("Id_DetCta")) +" "+ rp.getString("MPagoDetCta");
			        	datP[5]=" "+(Menu.formateadorCurrency.format(rp.getFloat("AcuentaDetCta"))+" ");
			        	datP[6]=" 0.00 ";
				        SALDO=SALDO + rp.getFloat("AcuentaDetCta");
				        
		            	MONTO_E= MONTO_E + rp.getFloat("AcuentaDetCta");
		            	datP[7]=" "+(Menu.formateadorCurrency.format(SALDO)+" ");
			        totalitems=totalitems+1;
			        model.addRow(datP);
		        }
		        rp.close();
		        sp.close();
		      
		        datP[0]=" ";
		        datP[1]="  ";
		       	datP[2]="  ";
		       	datP[3]=" ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::";
		       	datP[4]="  ";
	            datP[5]="  ";
	            datP[6]="  ";
	            datP[7]="  " ;
		        model.addRow(datP);
		        tableList.setModel(model);
		       //************************************************** END INGRESOS POR PAGOS
		        
	          //************************************************** INGRESOS POR VENTAS
	           Statement sv = conexion.gConnection().createStatement();
	           ResultSet rv=sv.executeQuery(consultaVenta);
		       String []datv= new String[8];
			   	if(!rv.equals("")) {
			   		datv[3]=" INGRESOS X VENTAS DIRECTA ";
			        model.addRow(datv);
			   }
		        while(rv.next()==true) {
			        	datv[1]=" "+Menu.formatoFechaString.format(rv.getDate("FechaTra"));
			        	datv[2]=" INGRESO";
			        	datv[3]=" "+rv.getString("NombreCli");
			        	datv[4]=" "+rv.getString("Id_DocTra") + " " +rv.getString("NumeroTra");
			        	datv[5]=" "+(Menu.formateadorCurrency.format(rv.getFloat("TotalTra"))+" ");
			        	datv[6]=" 0.00 ";
				        SALDO=SALDO + rv.getFloat("TotalTra");
				        
		            	MONTO_E= MONTO_E + rv.getFloat("TotalTra");
		            	datv[7]=" "+(Menu.formateadorCurrency.format(SALDO)+" ");
			        totalitems=totalitems+1;
			        model.addRow(datv);
		        }
		        sv.close();

		        datv[0]=" ";
		        datv[1]="  ";
		       	datv[2]="  ";
		       	datv[3]=" ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::";
		       	datv[4]="  ";
	            datv[5]="  ";
	            datv[6]="  ";
	            datv[7]="  " ;
		        model.addRow(datv);
		        tableList.setModel(model);
			     //************************************************** END INGRESOS POR VENTAS
			        
		      //************************************************** MOVIMIENTOS DE CAJA CHICA
	     	   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
	 		   
	 		   String []datoM= new String[8];
	           if(!rs.equals(0)) {
	        	   datoM[0]=" ";
	        	   datoM[1]="  ";
	        	   datoM[2]="  ";
	        	   datoM[3]=" MOVIMIENTOS DE CAJA CHICA ";
	        	   datoM[4]="  ";
	        	   datoM[5]="  ";
	        	   datoM[6]="  ";
	        	   datoM[7]="  " ;
		        model.addRow(datoM);
	           }
	           while(rs.next()) {
	        	   datoM[0]=" "+Menu.formatid_7.format(rs.getInt("Id_Caj"));
	        	   datoM[1]=" "+Menu.formatoFechaString.format(rs.getDate("FechaCaj"))+"  "+rs.getString("HoraCaj") ;
	        	   datoM[2]=" "+rs.getString("TipoMov");
	        	   datoM[3]=" "+rs.getString("DescripcionCaj");
	        	   datoM[4]=" "+rs.getString("DocumentoCaj")+" "+rs.getString("NumeroDocCaj");

	            if (rs.getString("TipoMov").equals("INGRESO")) {
	            	datoM[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("MontoCaj"))+" ";
		            datoM[6]=("0.00 ");
		            
		            SALDO=SALDO + rs.getFloat("MontoCaj");
		            datoM[7]=" "+Menu.formateadorCurrency.format(SALDO)+" ";
	            	
	            	MONTO_E= MONTO_E + rs.getFloat("MontoCaj");
	            }
	            if (rs.getString("TipoMov").equals("EGRESO")) {
	            	datoM[5]=("0.00 ");
	            	datoM[6]=" "+Menu.formateadorCurrency.format(rs.getFloat("MontoCaj"))+" ";
		            
		            SALDO=SALDO - rs.getFloat("MontoCaj");
		            datoM[7]=" "+Menu.formateadorCurrency.format(SALDO)+" ";
	            	
	            	MONTO_S= MONTO_S + rs.getFloat("MontoCaj");
	            }
	            totalitems=totalitems+1;
	            model.addRow(datoM);
	            tableList.setModel(model);

		        }
	           st.close();
	           //**************************************************AND MOVIMIENTOS DE CAJA CHICA
	           
	           // LLENO EL ESPACIO
	    	   if (totalitems>0) {
		           String []d= new String[8];
		 		   tableList.setModel(model);
		   
		            d[4]="================";
		            d[5]="========";
		            d[6]="========";
		            d[7]="========";
		            
		            model.addRow(d);
		            tableList.setModel(model);
		            
		           // LLENO RESULTADOS
		           String []dato= new String[8];
		 		   tableList.setModel(model);
		   
		            //dato[1]=" " + Menu.date +" "+ Menu.HORA;
		            dato[4]=" SALDOS DE CAJA ==>";
		            dato[5]=" " +	Menu.formateadorCurrency.format(MONTO_E+APERTURA)+" ";
		            dato[6]=" " +	Menu.formateadorCurrency.format(MONTO_S)+" ";
		            dato[7]=" " +	Menu.formateadorCurrency.format(SALDO )+" ";
		            
		            model.addRow(dato);
		            tableList.setModel(model);
	 		   }
	    	   // MODELO TABLE
	     	   int CONT=19;
	     	   if (totalitems>0) {
	     		   int l=0;
	 	            l=CONT-totalitems;
	 	            if ( CONT>totalitems) {
	 				    String []registros= new String[l];
	 				    for (int n=6; n<l;n++) {
	 					    model.addRow(registros);
	 					    tableList.setModel(model);
	 				    }
	 	            } 
	 		   }else{
	 			    String []registros= new String[CONT];
	 			    for (int n=0; n<CONT;n++) {
	 				    model.addRow(registros);
	 				    tableList.setModel(model);
	 			    }
	 		   }
	     	   // FIN MODELOTABLE
	        
           /*DefaultTableCellRenderer modeloLeft= new DefaultTableCellRenderer();
           modeloLeft.setHorizontalAlignment(SwingConstants.LEFT);
	           
           DefaultTableCellRenderer modeloRight = new DefaultTableCellRenderer();
           modeloRight.setHorizontalAlignment(SwingConstants.RIGHT);
           
           DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
           modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);
           
           tableList.getColumnModel().getColumn(0).setCellRenderer(modeloLeft);
           tableList.getColumnModel().getColumn(1).setCellRenderer(modeloLeft);
           tableList.getColumnModel().getColumn(2).setCellRenderer(modeloLeft);
           tableList.getColumnModel().getColumn(3).setCellRenderer(modeloLeft);
           tableList.getColumnModel().getColumn(4).setCellRenderer(modeloLeft);
           
           tableList.getColumnModel().getColumn(5).setCellRenderer(modeloRight);
           tableList.getColumnModel().getColumn(6).setCellRenderer(modeloRight);
           tableList.getColumnModel().getColumn(7).setCellRenderer(modeloRight);*/
           
           
           tableList.getColumnModel().getColumn(0).setMinWidth(0);
           tableList.getColumnModel().getColumn(0).setMaxWidth(0);
           
     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(70);
     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(5);
     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(310);
     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(130);
     	   tableList.getColumnModel().getColumn(5).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(6).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(7).setPreferredWidth(65);
     	   
     	   TextIngresos.setText("S/. "+Menu.formateadorCurrency.format(MONTO_E+APERTURA));
     	   TextEgresos.setText("S/. "+Menu.formateadorCurrency.format(MONTO_S));
     	   TextSaldo.setText("S/. "+Menu.formateadorCurrency.format(SALDO));
     	   
     	   tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("BALANCE"));//ESTABLESCO COLOR CELDAS
           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems , TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void crearTable(){
		tableList = new JTable();
		tableList.setShowHorizontalLines(false);
		tableList.setFillsViewportHeight(true);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tableList.setBounds(10, 303, 665, 229);
		tableList.addMouseListener(this);
		
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		//scrollTable.setColumnHeaderView(tableList);//ENCABEZADO
		//tableList.setFillsViewportHeight(true);//ENCABEZADO
		scrollTable.setBounds(10, 22, 788, 338);
        panelLst.add(scrollTable);
        tableList.setFillsViewportHeight(true);
    	tableList.setGridColor(new Color(220, 220, 220));
    	
		tableList.setFont(new Font("Tahoma", Font.PLAIN, 11));
    	tableList.setGridColor(new Color(169, 169, 169));
	}
	public void crearLabels() {
		lbl9= new JLabel("Filtrar por:");
		lbl9.setForeground(new Color(0, 0, 0));
		lbl9.setBackground(new Color(128, 128, 128));
		lbl9.setBounds(133, 8, 121, 14);
		panelBtn.add(lbl9);
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(Menu.fontLabel);
		
		lblIngresos = new JLabel("Ingresos:");
		lblIngresos.setForeground(new Color(0, 0, 0));
		lblIngresos.setBackground(new Color(128, 128, 128));
		lblIngresos.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIngresos.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblIngresos.setBounds(575, 8, 68, 14);
		panelBtn.add(lblIngresos);
		
		lblEgresos = new JLabel("Egresos:");
		lblEgresos.setForeground(new Color(0, 0, 0));
		lblEgresos.setBackground(new Color(128, 128, 128));
		lblEgresos.setHorizontalAlignment(SwingConstants.CENTER);
		lblEgresos.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEgresos.setBounds(649, 8, 70, 14);
		panelBtn.add(lblEgresos);
		
		label_2 = new JLabel("Saldo:");
		label_2.setForeground(new Color(0, 0, 0));
		label_2.setBackground(new Color(128, 128, 128));
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_2.setBounds(735, 8, 62, 14);
		panelBtn.add(label_2);
	}
	public void crearTextFields(){
		textBus = new  JTextFieldIcon(new JTextField(),"Search16.png","Search","Search16.png");
		textBus.setBackground(new Color(240, 240, 240));
		textBus.setColumns(10);
		textBus.setFont(Menu.fontText);
		textBus.setForeground(Color.WHITE);
		textBus.setHorizontalAlignment(SwingConstants.LEFT);
		textBus.addActionListener(this);
		textBus.addFocusListener(this);
		textBus.addKeyListener(this);
		textBus.addPropertyChangeListener(this);
		textBus.setBounds(259, 21, 151, 22);
		panelBtn.add(textBus);
		textBus.addFocusListener(this);
		
		TextIngresos = new JFormattedTextField();
		TextIngresos.setBackground(new Color(240, 240, 240));
		TextIngresos.setForeground(new Color(51, 102, 255));
		TextIngresos.setText("25.00");
		TextIngresos.setHorizontalAlignment(SwingConstants.RIGHT);
		TextIngresos.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		TextIngresos.setEditable(false);
		TextIngresos.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 255)));
		TextIngresos.setBounds(568, 22, 75, 21);
		panelBtn.add(TextIngresos);
		
		TextEgresos = new JFormattedTextField();
		TextEgresos.setBackground(new Color(240, 240, 240));
		TextEgresos.setForeground(new Color(204, 51, 0));
		TextEgresos.setText("25.00");
		TextEgresos.setHorizontalAlignment(SwingConstants.CENTER);
		TextEgresos.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		TextEgresos.setEditable(false);
		TextEgresos.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.RED));
		TextEgresos.setBounds(649, 22, 70, 21);
		panelBtn.add(TextEgresos);
		
		TextSaldo = new JFormattedTextField();
		TextSaldo.setBackground(new Color(240, 240, 240));
		TextSaldo.setForeground(new Color(51, 153, 51));
		TextSaldo.setText("0.00");
		TextSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
		TextSaldo.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		TextSaldo.setEditable(false);
		TextSaldo.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 100, 0)));
		TextSaldo.setBounds(723, 22, 75, 21);
		panelBtn.add(TextSaldo);
	}
	public void crearComboBox() {
		cbBusIni = new JComboBox<>();
		cbBusIni.setBounds(10, 21, 136, 21);
		cbBusIni.setFont(Menu.fontText);
		cbBusIni.removeAllItems();
		cbBusIni.addActionListener(this);
		cbBusIni.addFocusListener(this);
		cbBusIni.addKeyListener(this);
        panelBtn.add(cbBusIni);
        
		cbBus = new JComboBox<>();
        cbBus.setBounds(150, 21, 104, 21);
        cbBus.setFont(Menu.fontText);
        cbBus.removeAllItems();
        cbBus.addActionListener(this);
        cbBus.addFocusListener(this);
        cbBus.addKeyListener(this);
        panelBtn.add(cbBus);
	}
	public void llenarcbBuscar() {
        cbBusIni.removeAllItems();
		String [] lista = {"SIN CAJA INICIAL (X)","CON CAJA INICIAL (A)"};
		for (String llenar:lista) {
			cbBusIni.addItem(llenar);
		}
		cbBusIni.setSelectedIndex(1);
		
        cbBus.removeAllItems();
		String [] lista1 = {"%TODOS","INGRESO","EGRESO"};
		for (String llenar:lista1) {
			cbBus.addItem(llenar);
		}
		cbBus.setSelectedIndex(0);
	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setToolTipText("Agregar ítem");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setBounds(412, 20, 36, 23);
		buttonNuevo.setIcon(new ImageIcon(VentanaCajaBalance.class.getResource("/modelo/Images/nuevo.png")));
		panelBtn.add(buttonNuevo);

		buttonEditar= new JButton("");
		buttonEditar.setEnabled(false);
		buttonEditar.setToolTipText("Modificar ítem");
		buttonEditar.addActionListener(this);
		buttonEditar.setBounds(450, 20, 36, 23);
		buttonEditar.setIcon(new ImageIcon(VentanaCajaBalance.class.getResource("/modelo/Images/edit.png")));
		panelBtn.add(buttonEditar);
		
		buttonEliminar= new JButton("");
		buttonEliminar.setEnabled(false);
		buttonEliminar.setToolTipText("Eliminar ítem");
		buttonEliminar.addActionListener(this);
		buttonEliminar.setBounds(488, 20, 36, 23);
		buttonEliminar.setIcon(new ImageIcon(VentanaCajaBalance.class.getResource("/modelo/Images/delete.png")));
		panelBtn.add(buttonEliminar);

		buttonExportar= new JButton("");
		buttonExportar.setEnabled(false);
		buttonExportar.setToolTipText("Exportar a excel");
		buttonExportar.addActionListener(this);
		buttonExportar.setBounds(374, 20, 36, 23);
		buttonExportar.setIcon(new ImageIcon(VentanaCajaBalance.class.getResource("/modelo/Images/Preview.png")));
		panelBtn.add(buttonExportar);
		buttonExportar.setVisible(false);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(526, 20, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaCajaBalance.class.getResource("/modelo/Images/Exit.png")));
		panelBtn.add(buttonSalir);
	}
	public void limpiarTexts() {
		textBus.setText(null);
		textBus.setBackground(Menu.textColorBackgroundInactivo);	
		textBus.setForeground(Menu.textColorForegroundInactivo);
		
		cbBus.removeAllItems();
	}
	void modificarMoviCaja() {
		if (id==null || id.equals("")){
			return;
		}
		int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea modificar el ítem?" + Menu.separador + detalle, Menu.SOFTLE_HOTEL,
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if (respuesta == JOptionPane.YES_OPTION) {
			VentanaEntradaSalidaCajaAgregar.MOD=1;// PERMITE MODIFICAR
			VentanaEntradaSalidaCajaAgregar ven= new VentanaEntradaSalidaCajaAgregar();
			ven.frame.toFront();
			ven.frame.setVisible(true);
			ven.llenarParaModificar();
			VentanaEntradaSalidaCajaAgregar.textArea.requestFocus(true);
			ven.panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modificar Datos ", TitledBorder.LEADING, TitledBorder.TOP, null,(SystemColor.RED)));
		
		}else if (respuesta == JOptionPane.NO_OPTION) {}
	}
	//	METODO ELIMINAR
	public void delete() {
		if (id==null || id.equals("")){
			JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			tableList.requestFocus();
			return;
		}
		int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem?" + Menu.separador + detalle, Menu.SOFTLE_HOTEL,
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (respuesta == JOptionPane.YES_OPTION) {
			try {
				conexion = new ConexionDB();
				Statement statement =  conexion.gConnection().createStatement();
				String query ="Delete from CAJA where Id_Caj ='" + id  + "'";
				statement.execute(query);
				JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				statement.close();
				llenarTableCaja(); 
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else if (respuesta == JOptionPane.NO_OPTION) {}
	}
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonNuevo){// NUEVO
			  VentanaEntradaSalidaCajaAgregar.MOD=0;// NO PERMITE MODIFICAR
			  VentanaEntradaSalidaCajaAgregar ven= new VentanaEntradaSalidaCajaAgregar();
			    ven.frame.toFront();
			    ven.frame.setVisible(true);
			    VentanaEntradaSalidaCajaAgregar.textArea.requestFocus(true);
			    ven.panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos ", TitledBorder.LEADING, TitledBorder.TOP, null,( new Color(106, 90, 205))));

			  }
		  if (evento.getSource() == buttonEditar){// EDITAR
			  if (id==null || id == ""){
				JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				tableList.requestFocus();
				return;
			  }
			  if (id.length() > 0){
				  modificarMoviCaja();
			  	}
			  }	
		  if (evento.getSource() == buttonEliminar){// ELIMINAR
			  delete();
			  }
		  if (evento.getSource() == buttonExportar){// EXPORTAR

			  }	

		  if (evento.getSource() == buttonSalir){// SALIR
			  frame.dispose();
			  }
		  if (evento.getSource() == cbBus||evento.getSource() == cbBusIni){ 
			  llenarTableCaja();
			  }	
		}

		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textBus){textBus.setBackground(new Color(240, 240, 240));}
			if (ev.getSource() == textBus){textBus.setForeground(Color.black);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
				if (evet.getSource() == textBus){
					llenarTableCaja();
					int pos = textBus.getCaretPosition();textBus.setText(textBus.getText().toUpperCase());textBus.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (e==KeyEvent.VK_ENTER || textBus.getText().toLowerCase().length()==30){
							//buscar();
					}
				}
		}
		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			//char e=evet.getKeyChar();
		}
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		//Object source = e.getSource();
	}
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent Mouse_evt) {
		// TODO Auto-generated method stub
		id="";detalle="";
		if (Mouse_evt.getSource().equals(tableList)) {
			try {
				id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
				if (id==null || id.trim().equals("")){
					//tableList.requestFocus();
					buttonEliminar.setEnabled(false);
					buttonEditar.setEnabled(false);
					return;
				}
				id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
				detalle=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim() +" "+ (String) tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim();
					buttonEliminar.setEnabled(true);
					buttonEditar.setEnabled(true);
					if (Mouse_evt.getClickCount() == 2) {
						modificarMoviCaja();
					}
				} catch (Exception e) {}
			}
		}
}
