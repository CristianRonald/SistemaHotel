
package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Clases.ListVistaServicio;
import modelo.Clases.TableCellRendererColor;
import modelo.Datos.ConexionDB;
import modelo.Negocio.DatasourceVistaServicio;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
	public class VistaConsumo implements ActionListener,FocusListener,KeyListener,PropertyChangeListener{
		private static ConexionDB conexion;

		public static JDialog 		frame;
		private JPanel 				panelLst,panelBt;
		public static JTextField 	textCId,textCNom;
		private JLabel 				lbl2;
		private JLabel 				lbl14,lblA,lbl16,lbl17,lblS,lbl19,lbl21,lbl20,lbl22,lblT;
		public JButton				buttonPrint,buttonSalir;
		private JFormattedTextField textFormatTotalAloj,textFormatTotalSerExt,textFormatTotalVit,textFormaAcuentaAloj,textFormatSaldoAloj,textFormatAcuentaCons,textFormatSaldoCons;
		
		
		private JScrollPane scrollTable;
		public 	static JTable tableList;
		public 	DefaultTableModel model;
			//public static String []datos=new String[5];
		//public static String UNO,DOS,TRES,CUATRO,CINCO;
		public String dateEmision;
		public Integer totalitems;
		
		private int ID_APE_TUR;
		
		public static int MOD;
		int id_auto=0;
		
		private int COD_HOSPEDAJE=0;
		private JSeparator separator_1;
		
		public VistaConsumo(int cOD_HOSPEDAJE, int nRO, String hAB ,String hUESP) {
			super();
			COD_HOSPEDAJE = cOD_HOSPEDAJE;
			frameVistaConsumo();
			crearPanel();
			crearButtons();
			crearTextFields();
			crearLabels();
			crearTable();
		}
		public void frameVistaConsumo() {
			frame = new JDialog();
			frame.setTitle("Vista previa de servicios por alojamiento y consumo");
			
			frame.setResizable(false);
			frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VistaConsumo.class.getResource("/modelo/Images/find.png")));
			frame.setBounds(100, 100, 486, 412);
			frame.getContentPane().setLayout(null);
			frame.setLocationRelativeTo(null);
			frame.setModal(true);
		}
		public void crearPanel() {

			panelLst = new JPanel();
			panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLst.setBounds(5, 5, 473, 258);
			frame.getContentPane().add(panelLst);
			panelLst.setLayout(null);
			
			panelBt = new JPanel();
			panelBt.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Resultado:", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
			panelBt.setBounds(5, 263, 473, 115);
			frame.getContentPane().add(panelBt);
			panelBt.setLayout(null);
		}
									
		public void llenarTable (int COD_CLI) {
	        try {
	        	conexion = new ConexionDB();
	           float DESCT=0;
	     	   totalitems=0;
	     	   String consultar="Select * from DETALLE_A_HABITACION,ALQUILER,CLIENTES where ALQUILER.Id_Cli=CLIENTES.Id_Cli and DETALLE_A_HABITACION.Id_A = ALQUILER.Id_A and ALQUILER.Id_A='" + this.COD_HOSPEDAJE + "'";
			   model= new DefaultTableModel();
			   model.addColumn("Fecha");
			   model.addColumn("Hab.");
			   model.addColumn("Alojam.");
			   model.addColumn("Srv.Extr.");
			   model.addColumn("   Vitrina");
		       model.addColumn("Detalle");
		       model.addColumn("Tur / Ope.");
		       
		       Object []datos= new Object[7];
	 		   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
	 		   tableList.setModel(model);
	 		   
	 		   tableList.getColumnModel().getColumn(0).setPreferredWidth(70);// FECHA
	 		   tableList.getColumnModel().getColumn(1).setPreferredWidth(20);// NRO
	 		   tableList.getColumnModel().getColumn(2).setPreferredWidth(60);// ALJAMIENTO
	 		   tableList.getColumnModel().getColumn(3).setPreferredWidth(65);// SERV.EXTRA
	 		   tableList.getColumnModel().getColumn(4).setPreferredWidth(65);// VITRINA
	 		   tableList.getColumnModel().getColumn(5).setPreferredWidth(170);// DETALLE
	 		   tableList.getColumnModel().getColumn(6).setPreferredWidth(60);// OPERADOR

			
	 		  // ============================================================================== CTAS
	 		  float saldoAlo=0,saldoCon=0; 
	 		  float ctaAlo=0,ctaCon=0;
	          String ct="Select * from CUENTA_HUESPED as C,ALQUILER as A where C.Id_A=A.Id_A and C.Id_A='" + this.COD_HOSPEDAJE+ "'";
	  		  Statement stt = conexion.gConnection().createStatement();
	  		  ResultSet rrr=stt.executeQuery(ct);
	 		  while (rrr.next()) {
	 			  	if (rrr.getString("DescripcionCta").equals("ALOJAMIENTO")) {
		 	    		 saldoAlo=rrr.getFloat("SaldoCta");
		 			  }
		 			  if (rrr.getString("DescripcionCta").equals("CONSUMO")) {
		 	    		 saldoCon=rrr.getFloat("SaldoCta");
		 			 }
	 	             String consultt="Select * from CUENTA_HUESPED_PAGOS as P,CUENTA_HUESPED as C where P.Id_Cta=C.Id_Cta and P.Id_Cta='" + rrr.getInt("Id_Cta") + "'";
	 	    		 Statement ss = conexion.gConnection().createStatement();
	 	    		 ResultSet re=ss.executeQuery(consultt);
	 	             while(re.next()) {
			 			  if (re.getString("DescripcionCta").equals("ALOJAMIENTO")) {
			 				 ctaAlo=ctaAlo + re.getFloat("AcuentaDetCta");
			 	    		 //saldoAlo=re.getFloat("SaldoCta");
			 			  }
			 			  if (re.getString("DescripcionCta").equals("CONSUMO")) {
			 				 ctaCon=ctaCon + re.getFloat("AcuentaDetCta");
			 	    		 //saldoCon=re.getFloat("SaldoCta");
			 			  }
				 		   
	 	             }
	 	             re.close();
	 	             ss.close();
	 		  }
	 		  float montoAlo=0;
	 		  stt.close();rrr.close();
	 		  
	 		  
			  // =============================================================================== ALOJAMIENTO  
	          while(rs.next()) {
		        	ID_APE_TUR= rs.getInt("IdTurnoA");
		            datos[0]=" "+Menu.formatoFechaString.format(rs.getDate("FechaIngresoH"));//Menu.formatid_9.format(rs.getInt("Id_D"));
		            datos[1]=" "+rs.getInt("NumeroH");
		            datos[2]=" "+Menu.formateadorCurrency.format(rs.getFloat("ImporteH")) +" ";
		            datos[3]=" ";
		            datos[4]=" ";
		            datos[5]=" "+rs.getString("DescripcionH");
	
		            //CONSULTO TURNO Y USER
					String TUR="",USU="";
					String TEMP_TUR="",TEMP_NOM="";
					String delimiter = " ";
					String[] temp;
					Statement ss = conexion.gConnection().createStatement();
					ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + ID_APE_TUR +"'");
					if (rr.next()== true) {
						//textUser.setText(rr.getString("turno").trim() +": "+ rr.getString("NombresEmp").trim());
						ID_APE_TUR=rr.getInt("Id_ApeCie");
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
					}
					rr.close();
					ss.close();
					//END CONSULTO TURNO Y USER
		            datos[6]=" "+ TUR+ " / "+USU;
		            
		            totalitems=totalitems+1;
		            model.addRow(datos);
		            tableList.setModel(model);
		            
		            montoAlo =montoAlo+ rs.getFloat("ImporteH");
		            DESCT=rs.getFloat("DsctTA");
		            
					textCId.setText(Menu.formatid_7.format(COD_CLI));
					textCNom.setText(rs.getString("NombreCli"));
	           	
		        }
	          	montoAlo = montoAlo - DESCT;
	          
	          	
	           // =============================================================================== LLENO LOS SERVICIOS
	          float montoSer=0,montoVit=0;
	           String c="Select * from HABITACION,DETALLE_A_HABITACION,DETALLE_A_CONSUMO where DETALLE_A_CONSUMO.Id_D = DETALLE_A_HABITACION.Id_D and  DETALLE_A_HABITACION.NumeroH = HABITACION.NumeroHab and DETALLE_A_HABITACION.Id_A ='" + this.COD_HOSPEDAJE + "'";
	           Object []dato= new Object[7];
	 		   Statement s = conexion.gConnection().createStatement();
	 		   ResultSet r=s.executeQuery(c);
	 		   tableList.setModel(model);
	 		  
	           while(r.next()) {
		        	ID_APE_TUR= r.getInt("IdTurnoC");
		            dato[0]=" "+Menu.formatoFechaString.format(r.getDate("FechaC"));//Menu.formatid_9.format(r.getInt("Id_C"));
		            dato[1]=" "+r.getInt("NumeroH");
		            dato[2]=" ";
		            
		            if (r.getString("TipoConsumoC").equals("SERVICIO")) {
		            	montoSer=montoSer+r.getFloat("TotalC");
		 	            dato[3]=" "+Menu.formateadorCurrency.format(r.getFloat("TotalC"))+" ";
		 	            dato[4]=" ";
		 			  }
		 			  if (r.getString("TipoConsumoC").equals("VITRINA")) {
		 				montoVit=montoVit+r.getFloat("TotalC");
		 			    dato[3]=" ";
		 	            dato[4]=" "+Menu.formateadorCurrency.format(r.getFloat("TotalC"))+" ";
		 			  }
	
		            dato[5]=" "+r.getString("DescripcionC");
		            
		            //CONSULTO TURNO Y USER
					String TUR="",USU="";
					String TEMP_TUR="",TEMP_NOM="";
					String delimiter = " ";
					String[] temp;
					Statement ss = conexion.gConnection().createStatement();
					ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + ID_APE_TUR +"'");
					if (rr.next()== true) {
						//textUser.setText(rr.getString("turno").trim() +": "+ rr.getString("NombresEmp").trim());
						ID_APE_TUR=rr.getInt("Id_ApeCie");
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
					}
					rr.close();
					ss.close();
					//END CONSULTO TURNO Y USER
					dato[6]=" "+ TUR+ " / "+USU;
		            
		            totalitems=totalitems+1;
		            model.addRow(dato);
		            tableList.setModel(model);

		        }
	           	r.close();
	           	s.close();
	           	
	           	textFormatTotalAloj.setText("S/. "+Menu.formateadorCurrency.format(montoAlo));
	           	textFormatTotalSerExt.setText("S/. "+Menu.formateadorCurrency.format(montoSer));
	           	textFormatTotalVit.setText("S/. "+Menu.formateadorCurrency.format(montoVit));
	           	
	           	textFormaAcuentaAloj.setText("S/. "+Menu.formateadorCurrency.format(ctaAlo));
	           	textFormatAcuentaCons.setText("S/. "+Menu.formateadorCurrency.format(ctaCon));
	           	
	           	textFormatSaldoAloj.setText("S/. "+Menu.formateadorCurrency.format(saldoAlo));
	           	textFormatSaldoCons.setText("S/. "+Menu.formateadorCurrency.format(saldoCon));
	           	
	           	float total=0,acuenta=0,saldo=0;
	           	total=montoAlo + montoSer + montoVit;
	           	acuenta= ctaAlo + ctaCon;
	           	saldo=saldoAlo + saldoCon;
	           	
	           	lblT.setText("S/. "+Menu.formateadorCurrency.format(total)+" ");
	           	lblA.setText("S/. "+Menu.formateadorCurrency.format(acuenta)+" ");
	           	lblS.setText("S/. "+Menu.formateadorCurrency.format(saldo)+" ");
	           	
	           	
	         // MODELO TABLE	
	       	   if (totalitems>0) {
	       		int CONT=11;
		     	   if (totalitems>0) {
		     		   int l=0;
		 	            l=CONT-totalitems;
		 	            if ( CONT>totalitems) {
		 				    for (int n=0; n<l;n++) {
		       		   			String []d= new String[7];
			 		 		   	tableList.setModel(model);
					 		   	d[0]="";
					 		   	d[1]="";
					 		   	d[2]="";
					 		   	d[3]="";
					 		   	d[4]="";
					 	        d[5]="";
					            d[6]="";
					            model.addRow(d);
					            tableList.setModel(model);
		 				    }
		 	            } 
		 		   }else{
	 			    for (int n=0; n<CONT;n++) {
       		   			String []d= new String[7];
	 		 		   	tableList.setModel(model);
			 		   	d[0]="";
			 		   	d[1]="";
			 		   	d[2]="";
			 		   	d[3]="";
			 		   	d[4]="";
			 	        d[5]="";
			            d[6]="";
			            model.addRow(d);
			            tableList.setModel(model);
	 			   		}
	 		   		}
	       	   	}
	     	   // FIN MODELOTABLE
	           
	     	   tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("VISTA_CONSUMO"));//ESTABLESCO COLOR CELDAS
	           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total Mov. "+totalitems));
	        conexion.DesconectarDB();   	
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
	        
		}
		
		public void crearLabels() {
			lbl2= new JLabel("Huesped:");
			lbl2.setBounds(0, 24, 58, 14);
			panelLst.add(lbl2);
			lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl2.setFont(Menu.fontLabel);
			
			lbl21= new JLabel("Totales:");
			lbl21.setBounds(10, 35, 56, 14);
			panelBt.add(lbl21);
			lbl21.setHorizontalAlignment(SwingConstants.CENTER);
			lbl21.setFont(Menu.fontLabel);
			
			lbl14= new JLabel("Acuenta:");
			lbl14.setBounds(10, 59, 56, 14);
			panelBt.add(lbl14);
			lbl14.setHorizontalAlignment(SwingConstants.CENTER);
			lbl14.setFont(Menu.fontLabel);
			
			lblA= new JLabel("0.00 ");
			lblA.setForeground(Color.BLUE);
			lblA.setBounds(290, 55, 77, 21);
			panelBt.add(lblA);
			lblA.setHorizontalAlignment(SwingConstants.RIGHT);
			lblA.setFont(new Font("Arial Narrow", Font.BOLD, 13));
			lblA.setBorder(new MatteBorder(0, 0, 1, 1, (Color) new Color(128, 128, 128)));
			
			lbl16= new JLabel("Saldos:");
			lbl16.setBounds(11, 83, 50, 14);
			panelBt.add(lbl16);
			lbl16.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl16.setFont(Menu.fontLabel);
			
			lbl17= new JLabel("Alojamiento:");
			lbl17.setBounds(50, 11, 83, 14);
			panelBt.add(lbl17);
			lbl17.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl17.setFont(Menu.fontLabel);
			
			lblS= new JLabel("0.00 ");
			lblS.setForeground(Color.RED);
			lblS.setBounds(290, 81, 77, 21);
			panelBt.add(lblS);
			lblS.setHorizontalAlignment(SwingConstants.RIGHT);
			lblS.setFont(new Font("Arial Narrow", Font.BOLD, 13));
			lblS.setBorder(new MatteBorder(0, 0, 1, 1, (Color) new Color(128, 128, 128)));
			
			lbl19= new JLabel("Ser.Ext");
			lbl19.setBounds(145, 11, 66, 14);
			panelBt.add(lbl19);
			lbl19.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl19.setFont(Menu.fontLabel);
			
			lbl20= new JLabel("Vitrina");
			lbl20.setBounds(214, 11, 66, 14);
			panelBt.add(lbl20);
			lbl20.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl20.setFont(Menu.fontLabel);
			
			lbl22= new JLabel("TOTAL");
			lbl22.setBounds(284, 11, 83, 14);
			panelBt.add(lbl22);
			lbl22.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl22.setFont(Menu.fontLabel);
			
			lblT= new JLabel("0.00 ");
			lblT.setForeground(Color.BLACK);
			lblT.setBounds(290, 28, 77, 21);
			panelBt.add(lblT);
			lblT.setHorizontalAlignment(SwingConstants.RIGHT);
			lblT.setFont(new Font("Arial Narrow", Font.BOLD, 13));
			lblT.setBorder(new MatteBorder(0, 0, 1, 1, (Color) new Color(128, 128, 128)));
			
			JSeparator separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
			separator.setForeground(new Color(135, 206, 250));
			separator.setBounds(285, 28, 8, 74);
			panelBt.add(separator);
			
			separator_1 = new JSeparator();
			separator_1.setOrientation(SwingConstants.VERTICAL);
			separator_1.setForeground(new Color(135, 206, 250));
			separator_1.setBounds(139, 28, 8, 74);
			panelBt.add(separator_1);
		}
		public void crearTextFields(){
			textCId = new JTextField();
			textCId.setHorizontalAlignment(SwingConstants.CENTER);
			textCId.setEditable(false);
			textCId.setToolTipText("");
			textCId.setColumns(10);
			textCId.setForeground(new Color(75, 0, 130));
			textCId.setBackground(SystemColor.menu);
			textCId.setBounds(60, 22, 58, 15);
			textCId.setBorder(new MatteBorder(0, 1, 1, 0, (Color) new Color(192, 192, 192)));
			panelLst.add(textCId);
			
			textCNom = new JTextField();
			textCNom.setEditable(false);
			textCNom.setToolTipText("");
			textCNom.setColumns(10);
			textCNom.setForeground(new Color(72, 61, 139));
			textCNom.setBackground(SystemColor.menu);
			textCNom.setBounds(120, 22, 342, 15);
			textCNom.addActionListener(this);
			//extCNom.addFocusListener(this);
			//textCNom.addKeyListener(this);
			textCNom.setBorder(new MatteBorder(0, 0, 1, 1, (Color) new Color(192, 192, 192)));
			panelLst.add(textCNom);
			
			
			textFormatTotalAloj = new JFormattedTextField();
			textFormatTotalAloj.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
			textFormatTotalAloj.setEditable(false);
			textFormatTotalAloj.setText("25.00");
			textFormatTotalAloj.setHorizontalAlignment(SwingConstants.RIGHT);
			textFormatTotalAloj.setBounds(67, 28, 66, 21);
			textFormatTotalAloj.setBorder(new MatteBorder(1, 0, 1, 0, (Color) Color.GRAY));
			panelBt.add(textFormatTotalAloj);
			
			textFormatTotalSerExt = new JFormattedTextField();
			textFormatTotalSerExt.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
			textFormatTotalSerExt.setEditable(false);
			textFormatTotalSerExt.setText("0.00");
			textFormatTotalSerExt.setHorizontalAlignment(SwingConstants.RIGHT);
			textFormatTotalSerExt.setBounds(145, 28, 66, 21);
			textFormatTotalSerExt.addActionListener(this);
			textFormatTotalSerExt.addPropertyChangeListener(this);
			textFormatTotalSerExt.addKeyListener(this);
			textFormatTotalSerExt.setBorder(new MatteBorder(1, 0, 1, 0, (Color) Color.GRAY));
			panelBt.add(textFormatTotalSerExt);
			
			textFormatTotalVit = new JFormattedTextField();
			textFormatTotalVit.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
			textFormatTotalVit.setEditable(false);
			textFormatTotalVit.setText("25.00");
			textFormatTotalVit.setHorizontalAlignment(SwingConstants.RIGHT);
			textFormatTotalVit.setBounds(214, 28, 66, 21);
			textFormatTotalVit.setBorder(new MatteBorder(1, 0, 1, 0, (Color) Color.GRAY));
			panelBt.add(textFormatTotalVit);
			
			textFormaAcuentaAloj= new JFormattedTextField();
			textFormaAcuentaAloj.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
			textFormaAcuentaAloj.setEditable(false);
			textFormaAcuentaAloj.setText("25.00");
			textFormaAcuentaAloj.setHorizontalAlignment(SwingConstants.RIGHT);
			textFormaAcuentaAloj.setBounds(67, 55, 66, 21);
			textFormaAcuentaAloj.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 255)));
			panelBt.add(textFormaAcuentaAloj);
			
			textFormatSaldoAloj = new JFormattedTextField();
			textFormatSaldoAloj.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
			textFormatSaldoAloj.setEditable(false);
			textFormatSaldoAloj.setText("0.00");
			textFormatSaldoAloj.setHorizontalAlignment(SwingConstants.RIGHT);
			textFormatSaldoAloj.setBounds(67, 81, 66, 21);
			textFormatSaldoAloj.addActionListener(this);
			textFormatSaldoAloj.addPropertyChangeListener(this);
			textFormatSaldoAloj.addKeyListener(this);
			textFormatSaldoAloj.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 0, 0)));
			panelBt.add(textFormatSaldoAloj);
			
			textFormatAcuentaCons = new JFormattedTextField();
			textFormatAcuentaCons.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
			textFormatAcuentaCons.setEditable(false);
			textFormatAcuentaCons.setText("0.00");
			textFormatAcuentaCons.setHorizontalAlignment(SwingConstants.CENTER);
			textFormatAcuentaCons.setBounds(145, 55, 135, 21);
			textFormatAcuentaCons.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 255)));
			panelBt.add(textFormatAcuentaCons);

			textFormatSaldoCons = new JFormattedTextField();
			textFormatSaldoCons.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
			textFormatSaldoCons.setEditable(false);
			textFormatSaldoCons.setText("0.00");
			textFormatSaldoCons.setHorizontalAlignment(SwingConstants.CENTER);
			textFormatSaldoCons.setBounds(145, 81, 135, 21);
			textFormatSaldoCons.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(255, 0, 0)));
			panelBt.add(textFormatSaldoCons);
		}

		public void crearButtons() {
			buttonPrint = new JButton("");
			buttonPrint.setToolTipText("Print...");
			buttonPrint.addActionListener(this);
			buttonPrint.setBounds(376, 79, 36, 23);
			buttonPrint.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/print.png")));
			panelBt.add(buttonPrint);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Salir");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(415, 79, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/Exit.png")));
			panelBt.add(buttonSalir);
		}
		public void crearTable(){
			tableList = new JTable();
			tableList.setFillsViewportHeight(true);
			tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableList.setShowHorizontalLines(false);
			tableList.setForeground(Color.DARK_GRAY);
			tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			tableList.setBounds(10, 303, 665, 229);
			
			scrollTable = new JScrollPane();
			scrollTable.setViewportView(tableList);
			scrollTable.setBounds(10, 42, 452, 205);
	        panelLst.add(scrollTable);
	    	tableList.setShowVerticalLines(true);
	    	tableList.setGridColor(new Color(240, 230, 140));
	    	tableList.setBackground(new Color(255, 255, 255));
		}
		
		public void actionPerformed(ActionEvent action){
			if (action.getSource().equals(buttonPrint)) {
				try {
					Map<String,Object> parameters = new HashMap<String,Object>();
					parameters.put("PrtSubTitle",new String ("TARJETA DE SERVICIOS POR ALOJAMIENTO Y CONSUMO:  "));
					parameters.put("PrtNomEmpresa",new String (VentanaLogin.RAZON_SOCIAL_HOTEL));
					parameters.put("PrtRucEmpresa",new String (VentanaLogin.RUC_HOTEL));
					parameters.put("PrtDirEmpresa",new String (VentanaLogin.DIRECCION_HOTEL));
					if (!VentanaLogin.URLIMAGE_HOTEL.equals("")) {
						parameters.put("PrtUbiFoto",new String (VentanaLogin.URLIMAGE_HOTEL));
					//}else{
						//parameters.put("PrtUbiFoto",new String (Menu.URL_IMAGE+"login.png"));
					}
					
					String fecha=Menu.formatoFechaString.format(Menu.fecha);
					parameters.put("Prt_ID",new Integer(COD_HOSPEDAJE));
					parameters.put("PrtFecha",new String (fecha.trim()));
					parameters.put("PrtTurno",new String (VentanaLogin.TUR_CORTO.trim() +" / " + VentanaLogin.NOMBRE_USU_CORTO.trim()));
					parameters.put("PrtHuesped",new String (textCNom.getText().trim()));
					
					DatasourceVistaServicio source = new DatasourceVistaServicio();
				    for(int i=0;i<tableList.getRowCount();i++)
				    {
				    	ListVistaServicio c= new ListVistaServicio("o".trim(),tableList.getValueAt(i, 0).toString().trim(),tableList.getValueAt(i, 1).toString().trim(),tableList.getValueAt(i, 2).toString().trim(), tableList.getValueAt(i, 3).toString().trim(), tableList.getValueAt(i, 4).toString().trim(), tableList.getValueAt(i, 5).toString().trim(),tableList.getValueAt(i, 6).toString().trim());
				    	source.addList(c);
				    }
			 		
					parameters.put("PrtAT",new String (textFormatTotalAloj.getText().trim()));
					parameters.put("PrtAC",new String (textFormaAcuentaAloj.getText().trim()));
					parameters.put("PrtAS",new String (textFormatSaldoAloj.getText().trim()));
					
					parameters.put("PrtST",new String (textFormatTotalSerExt.getText().trim()));
					parameters.put("PrtVT",new String (textFormatTotalVit.getText().trim()));
					parameters.put("PrtSVC",new String (textFormatAcuentaCons.getText().trim()));
					parameters.put("PrtSVS",new String (textFormatSaldoCons.getText().trim()));
					
					parameters.put("PrtTT",new String (lblT.getText().trim()));
					parameters.put("PrtTC",new String (lblA.getText().trim()));
					parameters.put("PrtTS",new String (lblS.getText().trim()));
					
			        JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(Menu.URL+"CVistaServicios.jasper");  
			        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, source);  
			        JDialog visor = new JDialog();
			        visor.setModal(true);
			        JasperViewer jrViewer = new JasperViewer(jasperPrint, true); 
			        visor.setSize(700,600); 
			        visor.setLocationRelativeTo(null); 
					visor.setTitle("Tarjeta de servicios");

					visor.getContentPane().add(jrViewer.getContentPane()); 
					visor.setVisible(true);
					
			        //JRExporter exporter = new JRPdfExporter();  
			        //exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint); 
			        //exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File("reporte2PDF.pdf")); 
			        //exporter.exportReport(); 
				} catch (Exception e) {}
			}
			if (action.getSource().equals(buttonSalir)) {
				frame.dispose();
			}
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent e) {
		}
		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void keyReleased(KeyEvent evento) {
			// TODO Auto-generated method stub

		}
		@Override
		public void keyTyped(KeyEvent evet) {
			// TODO Auto-generated method stub
		}
		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
