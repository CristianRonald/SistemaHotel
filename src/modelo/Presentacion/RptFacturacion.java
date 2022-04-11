package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
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
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import modelo.Clases.TableCellRendererColor;
import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;
public class RptFacturacion implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public JInternalFrame 	frame;
	private JPanel  panelDto,panelLst;
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl6;
	private JButton  			buttonSalir;
	protected static JTextField textNom,textDoc;
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	protected JComboBox<String> cbBusIni;
	
	private String consultar="";
	protected static int COD_A=0;
			
	private JDateChooser chooserDesde,chooserHasta;
	private static String FECHA_DESDE="",FECHA_HASTA="";
	
	public RptFacturacion() {
		frameRptFacturacion();
		crearPanel();
		crearButtons();
		crearTable();
		crearTextFields();
		crearLabels();
		crearChooser();
		crearComboBox();
		LimpiarTable();
		llenarTable("","");
		
		chooserDesde.setDate(Menu.fecha);
		chooserHasta.setDate(Menu.fecha);
		
		Menu.Desktop.add(frame);
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
    	frame.setLocation(x, y);
	}
	public void frameRptFacturacion() {
		frame = new JInternalFrame();
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				llenarTable("","");
			}
			//@Override
		});
		frame.setTitle("Historial de facturación por hospedaje");
		frame.setFrameIcon(new ImageIcon(RptFacturacion.class.getResource("/modelo/Images/Histori1.png")));
		frame.setClosable(true);
		frame.setBounds(100, 100, 914, 385);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto = new JPanel();
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDto.setBounds(10, 11, 878, 52);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		panelLst = new JPanel();
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 64, 878, 280);
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
	}
	private void crearChooser () {
		chooserDesde= new JDateChooser();
		chooserDesde.setDateFormatString("dd-MMM-yyyy");
		chooserDesde.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		chooserDesde.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)chooserDesde.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)chooserDesde.getDateEditor()).setEditable(false);
		
		//chooserIngreso.getCalendarButton().addActionListener(this);
		chooserDesde.addPropertyChangeListener(this);
		chooserDesde.setBounds(167, 25, 104, 21);
		panelDto.add(chooserDesde);
		
		chooserHasta = new JDateChooser();
		chooserHasta.setDateFormatString("dd-MMM-yyyy");
		chooserHasta.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		chooserHasta.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)chooserHasta.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)chooserHasta.getDateEditor()).setEditable(false);
		chooserHasta.setBounds(270, 25, 104, 21);
		//dateSalida.getCalendarButton().addActionListener(this);
		chooserHasta.addPropertyChangeListener(this);
		panelDto.add(chooserHasta);
	}
	public void crearLabels(){
		lbl1= new JLabel("Desde:");
		lbl1.setBounds(286, 11, 88, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Hasta:");
		lbl2.setBounds(183, 11, 88, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Huésped:");
		lbl3.setBounds(379, 11, 168, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Documento:");
		lbl4.setBounds(557, 11, 88, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);
		
		lbl5= new JLabel("Módulo:");
		lbl5.setBounds(60, 11, 88, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);
		
		lbl6= new JLabel("");
		lbl6.setForeground(new Color(139, 0, 0));
		lbl6.setBounds(655, 8, 173, 40);
		panelDto.add(lbl6);
		lbl6.setHorizontalAlignment(SwingConstants.LEFT);
		lbl6.setFont(Menu.fontLabel);
	}
	public void crearTextFields() {
		textNom = new JTextFieldIcon(new JTextField(),"searchNormal.png","Search","searchNormal.png");
		textNom.setColumns(10);
		textNom.setFont(Menu.fontText);
		textNom.setForeground(Menu.textColorForegroundInactivo);
		textNom.setHorizontalAlignment(SwingConstants.LEFT);
		textNom.addActionListener(this);
		textNom.addFocusListener(this);
		textNom.addKeyListener(this);
		textNom.setBounds(379, 24, 168, 22);
		panelDto.add(textNom);
		
		textDoc =  new JTextFieldIcon(new JTextField(),"searchNormal.png","Search","searchNormal.png");
		textDoc.setColumns(10);
		textDoc.setFont(Menu.fontText);
		textDoc.setForeground(Menu.textColorForegroundInactivo);
		textDoc.setHorizontalAlignment(SwingConstants.LEFT);
		textDoc.addActionListener(this);
		textDoc.addFocusListener(this);
		textDoc.addKeyListener(this);
		textDoc.setBounds(550, 24, 95, 22);
		panelDto.add(textDoc);
	}
	public void crearButtons() {
		
		buttonSalir= new JButton("");
		buttonSalir.setBounds(832, 24, 36, 23);
		panelDto.add(buttonSalir);
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setIcon(new ImageIcon(RptFacturacion.class.getResource("/modelo/Images/Exit.png")));
	}
	public void crearTable() {
		tableList = new JTable(); 
		tableList.setShowHorizontalLines(false);
		tableList.addMouseListener(this);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 20, 858, 248);
        panelLst.add(scrollTable);

	}
	public void limpiarTexts() {
		textNom.setText(null);
		textNom.setBackground(Menu.textColorBackgroundInactivo);	
		textNom.setForeground(Menu.textColorForegroundInactivo);
		
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		//consultar="SELECT * FROM TURNO order by turno asc";
        llenarTable("","");
	}
	public void crearComboBox() {
		cbBusIni = new JComboBox<>();
		//cbBusIni.setForeground(Color.WHITE);
		//cbBusIni.setBackground(new Color(128, 128, 128));
		cbBusIni.setBounds(10, 25, 153, 21);
		cbBusIni.setFont(Menu.fontText);
		cbBusIni.removeAllItems();
		cbBusIni.addActionListener(this);
		cbBusIni.addFocusListener(this);
		cbBusIni.addKeyListener(this);
        panelDto.add(cbBusIni);
        
        cbBusIni.addItem("%TODOS");
        cbBusIni.addItem("ALOJAMIENTO");
        cbBusIni.addItem("VENTA-COMPRA");
	} 
	public void activarTexts(boolean b) {
		textNom.setEnabled(b);
	}
	public void focusGained(FocusEvent ev) {
		if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundActivo);}
		
		if (ev.getSource() == textDoc){textDoc.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == textDoc){textDoc.setForeground(Menu.textColorForegroundActivo);}
	}
	public void focusLost(FocusEvent ev) {
		if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundInactivo);}
		if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundInactivo);} 
		
		if (ev.getSource() == textDoc){textDoc.setBackground(Menu.textColorBackgroundInactivo);}
		if (ev.getSource() == textDoc){textDoc.setForeground(Menu.textColorForegroundInactivo);} 
	}
	
	protected void llenarTable(String nombrecli,String numerodocu) {
		conexion = new ConexionDB();
		LimpiarTable();
        try {
        	
     	   int totalitems=0;
     	   model= new DefaultTableModel();
		   model.addColumn("Item");
		   if (cbBusIni.getSelectedItem().equals("ALOJAMIENTO")||cbBusIni.getSelectedItem().equals("%TODOS")) {
			   model.addColumn("Reg_Fac");
		   }
		   if (!cbBusIni.getSelectedItem().equals("ALOJAMIENTO")||cbBusIni.getSelectedItem().equals("%TODOS")) {
			   model.addColumn("Reg_Tra");
		   }
		   model.addColumn("Id_cli");
		   if (cbBusIni.getSelectedItem().equals("ALOJAMIENTO")||cbBusIni.getSelectedItem().equals("%TODOS")) {
			   model.addColumn("Huésped");
			   lbl3.setText("Huésped  ");
			   lbl6.setText("<html>Para cambiar el documento o otro<br>debe filtrar un ítem de la lista<br>y pulsar doble click ...</html>");
		   }
		   if (!cbBusIni.getSelectedItem().equals("ALOJAMIENTO")||cbBusIni.getSelectedItem().equals("%TODOS")) {
			   model.addColumn("Cliente / proveedor");
			   lbl3.setText("Cliente / Proveedor  ");
			   lbl6.setText("<html>Para cambiar o anular el documento<br>debe filtrar un ítem de la lista<br>y pulsar doble click ...</html>");
		   }
		   model.addColumn("Documento");
		   model.addColumn("Fecha Emisión");
		   model.addColumn("Tipo Pago");
		   model.addColumn(" Subt total");
		   model.addColumn("     Dsct");
		   model.addColumn("      Igv");
		   model.addColumn("     Total");
		   model.addColumn("Status");
		   model.addColumn("Módulo");
		   model.addColumn("Tur / Ope");
		   
 		   String []datos= new String[14];
 		   tableList.setModel(model);
 		  
 		   if (cbBusIni.getSelectedItem().equals("ALOJAMIENTO")||cbBusIni.getSelectedItem().equals("%TODOS")) {
			   consultar="SELECT * FROM CLIENTES as H, ALQUILER as A, FACTURACION as F where F.Id_A=A.Id_A and A.Id_Cli=H.Id_Cli and F.EstadoFac='"+1+"'and F.FechaEmisionFac >='" + FECHA_DESDE.trim() + "'and F.FechaEmisionFac <='" + FECHA_HASTA.trim() + "'and H.NombreCli like'" + nombrecli + "%'and F.NumeroDocFac like'" + numerodocu + "%' order by F.Id_Fac desc";
	 		   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
	 		  
	    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(5);
	    	   tableList.getColumnModel().getColumn(1).setPreferredWidth(200);
	    	   
	    	   while(rs.next()) {
			    	datos[0]=" "+Menu.formatid_9.format(rs.getInt("Id_A"));
			    	datos[1]=" "+Menu.formatid_9.format(rs.getInt("Id_Fac"));
			    	datos[2]=" "+rs.getString("Id_Cli");
			    	datos[3]=" "+rs.getString("NombreCli");
			    	datos[4]=" "+rs.getString("Id_Doc")+" "+Menu.formatid_4.format(rs.getInt("SerieDocFac")) +"-"+rs.getString("NumeroDocFac");
			    	datos[5]=" "+Menu.formatoFechaString.format(rs.getDate("FechaEmisionFac"))+"  " + rs.getString("HoraEmisionFac");
			    	datos[6]=" "+rs.getString("TipoPagoA");
			    	datos[7]=" "+Menu.formateadorCurrency.format(rs.getFloat("SubTotFac"));
			    	datos[8]=" "+Menu.formateadorCurrency.format(rs.getFloat("DsctFac"));
			    	datos[9]=" "+Menu.formateadorCurrency.format(rs.getFloat("IgvFac"));
			    	datos[10]=" "+Menu.formateadorCurrency.format(rs.getFloat("TotalFac"));
			    	datos[11]=rs.getString("EstadoA");
			    	datos[12]=" ALOJAMIENTO";
			    	
			    	//CONSULTO TURNO Y USER
			    	String strin="";
					Statement ss = conexion.gConnection().createStatement();
					//ResultSet rr = ss.executeQuery("Select * from EMPLEADO  as E, CAJA_APE_CIE as AP where E.Id_Emp=AP.User and AP.Id_ApeCie='" + rs.getInt("IdTurno")+"'");
					ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + rs.getInt("IdTurnoF")+"'");
					if (rr.next()== true) {
						// NOMBRE CORTO DE USUARIO Y TURNO
						String TUR="",USU="";
						String TEMP_TUR="",TEMP_NOM="";
						String delimiter = " ";
						String[] temp;
						
						temp = rr.getString("turno").trim().split(delimiter);
						for(int i =0; i < temp.length ; i++){
							if (temp[i].trim().length()>=(1)) {
								TEMP_TUR=temp[i].trim().substring(0, 1);
								}
								TUR=TUR.trim() + TEMP_TUR.trim();
						}
						
						temp =  rr.getString("NombresEmp").trim().split(delimiter);
						for(int i =0; i < temp.length ; i++){
							if (temp[i].trim().length()>=(1)) {
								TEMP_NOM=temp[i].trim().substring(0, 1);
								}
								USU=USU.trim() + TEMP_NOM.trim();
						}
						strin=TUR +" / "+ USU;
						// NOMBRE CORTO DE USUARIO Y TURNO
					}
					rr.close();
					ss.close();
					//END CONSULTO TURNO Y USER
					datos[13]=" "+strin;
					
		            totalitems=totalitems+1;
		            model.addRow(datos);
		            tableList.setModel(model);
	
		        }
	           rs.close();
 		   }
 		   
 		  if (!cbBusIni.getSelectedItem().equals("ALOJAMIENTO")||cbBusIni.getSelectedItem().equals("%TODOS")) {
	    	   //LLENAR VENTAS FACTURADAS
			   consultar="SELECT * FROM CLIENTES as C, TRANSACCION as T where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.EstadoTra='" + 1 + "'and T.FechaTra >='" + FECHA_DESDE.trim() + "'and T.FechaTra <='" + FECHA_HASTA.trim() + "'and C.NombreCli  like'" + nombrecli + "%'and T.NumeroTra  like'" + numerodocu + "%' order by T.Id_Tra desc";
	 		   Statement stt = conexion.gConnection().createStatement();
	 		   ResultSet rss=stt.executeQuery(consultar);
	    	   while(rss.next()) {
			    	datos[0]=Integer.toString(totalitems);
			    	datos[1]=" "+Menu.formatid_9.format(rss.getInt("Id_Tra"));
			    	datos[2]=" "+rss.getString("Id_Cli");
			    	datos[3]=" "+rss.getString("NombreCli");
			    	datos[4]=" "+rss.getString("Id_DocTra") +" "+Menu.formatid_4.format(rss.getInt("SerieTra")) +"-"+rss.getString("NumeroTra");
			    	datos[5]=" "+Menu.formatoFechaString.format(rss.getDate("FechaTra"))+"  "+ rss.getString("HoraTra");
			    	datos[6]=" "+rss.getString("TipoTra");
			    	datos[7]=" "+Menu.formateadorCurrency.format(rss.getFloat("SubTotalTra"));
			    	datos[8]=" "+Menu.formateadorCurrency.format(rss.getFloat("DsctTra"));
			    	datos[9]=" "+Menu.formateadorCurrency.format(rss.getFloat("IgvTra"));
			    	datos[10]=" "+Menu.formateadorCurrency.format(rss.getFloat("TotalTra"));
			    	datos[11]=rss.getString("EstadoTra");
			    	datos[12]=""+rss.getString("TipoOperacionTra");

			    	//CONSULTO TURNO Y USER
			    	String strin="";
					Statement ss = conexion.gConnection().createStatement();
					//ResultSet rr = ss.executeQuery("Select * from EMPLEADO  as E, CAJA_APE_CIE as AP where AP.Id_ApeCie='" + rs.getString("IdTurno")+"'");
					ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + rss.getInt("IdTurnoT")+"'");
					if (rr.next()== true) {
						// NOMBRE CORTO DE USUARIO Y TURNO
						String TUR="",USU="";
						String TEMP_TUR="",TEMP_NOM="";
						String delimiter = " ";
						String[] temp;
						
						temp = rr.getString("turno").trim().split(delimiter);
						for(int i =0; i < temp.length ; i++){
							if (temp[i].trim().length()>=(1)) {
								TEMP_TUR=temp[i].trim().substring(0, 1);
								}
								TUR=TUR.trim() + TEMP_TUR.trim();
						}
						
						temp =  rr.getString("NombresEmp").trim().split(delimiter);
						for(int i =0; i < temp.length ; i++){
							if (temp[i].trim().length()>=(1)) {
								TEMP_NOM=temp[i].trim().substring(0, 1);
								}
								USU=USU.trim() + TEMP_NOM.trim();
						}
						strin=TUR +" / "+ USU;
						// NOMBRE CORTO DE USUARIO Y TURNO
					}
					rr.close();
					ss.close();
					//END CONSULTO TURNO Y USER
					datos[13]=" "+strin;
					
		            totalitems=totalitems+1;
		            model.addRow(datos);
		            tableList.setModel(model);
	
		        }
	           rss.close();
	    	   //END LLENAR VENTAS FACTURADAS
	    	   
	    	   //LLENAR COMPRAS FACTURADAS
	    	   consultar="SELECT * FROM PROVEEDOR as P, TRANSACCION as T where T.Id_CliPro=P.Id_Prv and T.TipoOperacionTra='" + "COMPRA" + "'and T.EstadoTra='" + 1 + "'and T.FechaTra >='" + FECHA_DESDE.trim() + "'and T.FechaTra <='" + FECHA_HASTA.trim() + "'and P.RazonSocialPrv like'" + nombrecli + "%'and T.NumeroTra  like'" + numerodocu + "%' order by T.Id_Tra desc";
			   Statement stc = conexion.gConnection().createStatement();
	 		   ResultSet rsc=stc.executeQuery(consultar);
	    	   while(rsc.next()) {
			    	datos[0]=Integer.toString(totalitems);
			    	datos[1]=" "+Menu.formatid_9.format(rsc.getInt("Id_Tra"));
			    	datos[2]=" "+rsc.getString("Id_Prv");
			    	datos[3]=" "+rsc.getString("RazonSocialPrv");
			    	datos[4]=" "+ rsc.getString("Id_DocTra") +" "+Menu.formatid_4.format(rsc.getInt("SerieTra")) +"-"+rsc.getString("NumeroTra");
			    	datos[5]=" "+Menu.formatoFechaString.format(rsc.getDate("FechaTra"))+"  "+ rsc.getString("HoraTra");
			    	datos[6]=" "+rsc.getString("TipoTra");
			    	datos[7]=" "+Menu.formateadorCurrency.format(rsc.getFloat("SubTotalTra"));
			    	datos[8]=" "+Menu.formateadorCurrency.format(rsc.getFloat("DsctTra"));
			    	datos[9]=" "+Menu.formateadorCurrency.format(rsc.getFloat("IgvTra"));
			    	datos[10]=" "+Menu.formateadorCurrency.format(rsc.getFloat("TotalTra"));
			    	datos[11]=rsc.getString("EstadoTra");
			    	datos[12]=""+rsc.getString("TipoOperacionTra");
		            totalitems=totalitems+1;
		            
			    	//CONSULTO TURNO Y USER
			    	String strin="";
					Statement ss = conexion.gConnection().createStatement();
					//ResultSet rr = ss.executeQuery("Select * from EMPLEADO  as E, CAJA_APE_CIE as AP where AP.Id_ApeCie='" + rs.getString("IdTurno")+"'");
					ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + rsc.getInt("IdTurnoT")+"'");
					if (rr.next()== true) {
						// NOMBRE CORTO DE USUARIO Y TURNO
						String TUR="",USU="";
						String TEMP_TUR="",TEMP_NOM="";
						String delimiter = " ";
						String[] temp;
						
						temp = rr.getString("turno").trim().split(delimiter);
						for(int i =0; i < temp.length ; i++){
							if (temp[i].trim().length()>=(1)) {
								TEMP_TUR=temp[i].trim().substring(0, 1);
								}
								TUR=TUR.trim() + TEMP_TUR.trim();
						}
						
						temp =  rr.getString("NombresEmp").trim().split(delimiter);
						for(int i =0; i < temp.length ; i++){
							if (temp[i].trim().length()>=(1)) {
								TEMP_NOM=temp[i].trim().substring(0, 1);
								}
								USU=USU.trim() + TEMP_NOM.trim();
						}
						strin=TUR +" / "+ USU;
						// NOMBRE CORTO DE USUARIO Y TURNO
					}
					rr.close();
					ss.close();
					//END CONSULTO TURNO Y USER
					datos[13]=" "+strin;
					
		            model.addRow(datos);
		            tableList.setModel(model);
		        }
	           rsc.close();
	    	   //END LLENAR COMPRAS FACTURADAS
 		  }
    	   // MODELO TABLE
    	   int CONT=14;
    	   if (totalitems>0) {
    		   int c=0;
	            c=CONT-totalitems;
	            if ( CONT>totalitems) {
				    String []registros= new String[c];
				    for (int n=0; n<c;n++) {
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
           tableList.getColumnModel().getColumn(0).setMinWidth(0);
           tableList.getColumnModel().getColumn(0).setMaxWidth(0);
           tableList.getColumnModel().getColumn(2).setMinWidth(0);
           tableList.getColumnModel().getColumn(2).setMaxWidth(0);
           tableList.getColumnModel().getColumn(11).setMinWidth(0);
           tableList.getColumnModel().getColumn(11).setMaxWidth(0);
           //tableList.getColumnModel().getColumn(12).setMinWidth(0);
           //tableList.getColumnModel().getColumn(12).setMaxWidth(0);
           
     	   //tableList.getColumnModel().getColumn(0).setPreferredWidth(20);
     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(0);
     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(220);
     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(110);
     	   tableList.getColumnModel().getColumn(5).setPreferredWidth(80);
     	   tableList.getColumnModel().getColumn(6).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(7).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(8).setPreferredWidth(50);
     	   tableList.getColumnModel().getColumn(9).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(10).setPreferredWidth(60);
     	   tableList.getColumnModel().getColumn(11).setPreferredWidth(0);
     	   tableList.getColumnModel().getColumn(12).setPreferredWidth(100);
     	   tableList.getColumnModel().getColumn(13).setPreferredWidth(100);
     	   tableList.getColumnModel().getColumn(13).setPreferredWidth(60);
     	  
     	   tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("RPT_FACTURACION"));//ESTABLESCO COLOR CELDAS
           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
      	if (e.getSource()==(chooserDesde)||e.getSource()==(chooserDesde)){
      		if (chooserDesde.getDate()!=null){
  				FECHA_DESDE = form.format(chooserDesde.getDate());
  				llenarTable(textNom.getText(), textDoc.getText());
      		}
		}
      	if (e.getSource()==(chooserHasta)||e.getSource()==(chooserHasta)){
      		if (chooserHasta.getDate()!=null){
      			FECHA_HASTA = form.format(chooserHasta.getDate());
      			llenarTable(textNom.getText(),textDoc.getText());
      		}
		}
	}
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		//char e=evet.getKeyChar();
	}
	@Override
	public void keyReleased(KeyEvent evet) {
		// TODO Auto-generated method stub
		try {
			char e=evet.getKeyChar();
			if (evet.getSource().equals(textNom)) {
				int pos = textNom.getCaretPosition();textNom.setText(textNom.getText().toUpperCase());textNom.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				llenarTable(textNom.getText(),textDoc.getText());
				if (textNom.getText().toLowerCase().isEmpty()|| textNom.getText().toLowerCase().length()>=38){
					textNom.requestFocus();
					textNom.selectAll();
					textNom.setText(null);
					} 
					 if (e==KeyEvent.VK_ENTER){
						textDoc.requestFocus();
						textDoc.selectAll();
					}
			}
			if (evet.getSource().equals(textDoc)) {
				int pos = textDoc.getCaretPosition();textDoc.setText(textDoc.getText().toUpperCase());textDoc.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				llenarTable(textNom.getText(),Menu.formatid_9.format(Integer.parseInt(textDoc.getText())));
				if (textDoc.getText().toLowerCase().isEmpty()|| textDoc.getText().toLowerCase().length()>=38){
					textDoc.requestFocus();
					textDoc.selectAll();
					textDoc.setText(null);
					} 
					 if (e==KeyEvent.VK_ENTER){
						 tableList.selectAll();
					}
			}
		} catch (Exception e) {}	
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void actionPerformed(ActionEvent action) {
		// TODO Auto-generated method stub
		if (action.getSource().equals(buttonSalir)) {
			frame.dispose();
		}
		if (action.getSource().equals(cbBusIni)) {
			llenarTable(textNom.getText(),textDoc.getText());
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mousePressed(MouseEvent Mouse_evt) {
		if (Mouse_evt.getSource().equals(tableList)) {
			try {
				
				if (Mouse_evt.getClickCount() == 2) {
					int COD_C=0;
					COD_C=(Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim()));
					String TABBLE=(String)tableList.getValueAt(tableList.getSelectedRow(),12).toString().trim();
					//VentanaControlHotel.ID_ALQUILER=0;
					if (TABBLE.trim().trim().equals("ALOJAMIENTO")){
						COD_A =(Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim()));
						VentanaVitrinaVenta.MOD=0;
						VentanaGenerarDocumento v  = new VentanaGenerarDocumento(COD_A,0,"","");
						v.buttonCambiarDoc.setVisible(true);
		      			VentanaGenerarDocumento.frame.toFront();
		      			VentanaGenerarDocumento.frame.show();
					    v.llenarTable(COD_C);
					    v.llenarParaModificarFacturacion();
					}
					if (TABBLE.trim().trim().equals("VENTA")){
						COD_A =(Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim()));
						VentanaVitrinaVenta.MOD=1;
						VentanaVitrinaVenta v= new VentanaVitrinaVenta();
						v.buttonAnular.setEnabled(true);
						v.llenarModificarVenta(COD_A);
						v.frame.setVisible(true);
						v.buttonGrabar.setToolTipText("Cambiar documento");
					}
					if (TABBLE.trim().trim().equals("COMPRA")){
						COD_A =(Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim()));
						VentanaVitrinaCompra.MOD=1;
						VentanaVitrinaCompra v= new VentanaVitrinaCompra();
						v.buttonAnular.setEnabled(true);
						v.llenarModificarCompra(COD_A);
						v.frame.setVisible(true);
						v.buttonGrabar.setEnabled(false);
					}
				}
			} catch (Exception e) {}
		}
	}
	@Override
	public void mouseReleased(MouseEvent evet) {
		// TODO Auto-generated method stub
	}

}

