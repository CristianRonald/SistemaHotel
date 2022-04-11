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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
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
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import modelo.Clases.TableCellRendererColor;
import modelo.Datos.ConexionDB;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
public class VentanaGenerarDocumento implements ActionListener,FocusListener,KeyListener,PropertyChangeListener{
	private static ConexionDB conexion;

	public static JInternalFrame 		frame;
	private JPanel 				panelDto,panelLst,panelBt;
	public static JTextField 	textUser,textCId,textCNom,textCDir,textCRuc;
	protected JTextField			textNumero;
	private JLabel 				lbl1,lbl2,lbl3,lbl5,lbl8,lbl10;
	private JLabel 				lbl11,lbl12,lbl14,lbl15,lbl16,lbl17,lbl18,lbl19,lbl20,lbl21,lblHra;
	public JButton				buttonActualizar,buttonCliente,buttonGrabar,buttonPrint,buttonSalir,buttonAnular,buttonCuenta,buttonCambiarDoc;
	protected JComboBox<String> 	cbDocumento,cbSerie;
	private JFormattedTextField textFormatImporte,textFormatDsct,textFormatSubTotal,textFormatIgv,textFormatAmortizo,textFormatTotal,textFormatDebe;
	
	
	private JScrollPane scrollTable;
	public 	static JTable tableList;
	public 	DefaultTableModel model;
		//public static String []datos=new String[5];
	//public static String UNO,DOS,TRES,CUATRO,CINCO;
	private String ID_DOC="";
	private int ID_SERIE=0;
	private JDateChooser Chooserdate;
	public String dateEmision;
	public Integer totalitems;
	
	static int CONTAR_VENTANA_GENERAR_DOCUMENT=0;
	private int ID_APE_TUR;
	
	public static int MOD;
	int id_auto=0;
	
	private int COD_HOSPEDAJE=0;
	
	private int NRO=0;
	private String HAB="",HUESP="";
	public VentanaGenerarDocumento(int cOD_HOSPEDAJE, int nRO, String hAB ,String hUESP) {
		super();
		COD_HOSPEDAJE = cOD_HOSPEDAJE;
		
		NRO = nRO;
		HAB =hAB;
		HUESP = hUESP;
		
		frameGenerarDocumento();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		crearTable();
		crearOtros();
		clock();
		Chooserdate.setDate(Menu.fecha);
		
		// ACTUALIZO FECHA ACTUAL DE EMISION
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dateEmision = form.format(Chooserdate.getDate());
		llenarComboDocumento();
		
		//llenarTable();
		
		
		CONTAR_VENTANA_GENERAR_DOCUMENT ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
		
		Menu.Desktop.add(frame);
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
    	frame.setLocation(x, y);
    	
    	//if (VentanaControlHotel.ID_ALQUILER==0){
    		//llenarParaModificarFacturacion();
    	//}
	}
	public void frameGenerarDocumento() {
		frame = new JInternalFrame();
		frame.setTitle("Check - Out  |  Facturación de salida");
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				CONTAR_VENTANA_GENERAR_DOCUMENT=0;
			}
			@Override
			public void internalFrameActivated(InternalFrameEvent arg0) {
				//llenarTable();
			}
		});
		frame.setFrameIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/cout.png")));
		frame.setBounds(100, 100, 673, 478);
		frame.getContentPane().setLayout(null);
		//frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setClosable(true);
		//frame.setResizable(true);
		
	}
	public void clock() {
		Thread clock = new Thread(){
			public void run(){
				try {
					for(;;){
					DecimalFormat format = new DecimalFormat("00");
					Calendar cr = new GregorianCalendar();
					int second = cr.get(Calendar.SECOND);
					int minute = cr.get(Calendar.MINUTE);
					int hour = cr.get(Calendar.HOUR_OF_DAY);
					String ampm = cr.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM";
		            if ( hour >= 13  ) {
		                hour = cr.get(Calendar.HOUR);
		            }
					String HORA=format.format(hour) +":"+ format.format(minute) +":"+ format.format(second) +" "+ ampm; 

					//panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),(" | hora de ingreso ===> " + HORA)));
					lblHra.setText(HORA);
					sleep(1000);
					
	               // if ( hour >= 7 && hour == 13 && hour == 20 && hour == 0 && hour < 7 ) {
	                	//System.out.println("es de madrugada");
	                    //return ;
	                //}
				  }
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		clock.start();
	}
	public void crearPanel() {
		panelDto = new JPanel();
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), ":::: | Nueva Facturación | :::: REGISTRO: " + Menu.formatid_9.format(this.COD_HOSPEDAJE), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDto.setBounds(10, 5, 636, 108);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);

		panelLst = new JPanel();
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 111, 636, 271);
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
		
		panelBt = new JPanel();
		panelBt.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Resultado:", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
		panelBt.setBounds(10, 379, 636, 54);
		frame.getContentPane().add(panelBt);
		panelBt.setLayout(null);
	}
								
	public void llenarTable (int COD_CLI) {
		conexion = new ConexionDB();
        try {
        	
           float IMPORTES=0,DESCT=0,ACUENTA=0,TOTAL=0,SALDO=0;
     	   totalitems=0;
     	   String consultar="Select * from DETALLE_A_HABITACION,ALQUILER,CLIENTES where ALQUILER.Id_Cli=CLIENTES.Id_Cli and DETALLE_A_HABITACION.Id_A = ALQUILER.Id_A and ALQUILER.Id_A='" + this.COD_HOSPEDAJE + "'";
		   model= new DefaultTableModel();
		   model.addColumn("Item");
		   model.addColumn("Nro");
		   model.addColumn("Descripción");
		   model.addColumn("Pre. S/.");
		   model.addColumn("Cant.");
	       model.addColumn("Imp. S/.");
	       //model.addColumn("Dsct S/.");
	       //model.addColumn("Tot.");
	       model.addColumn("OK");
	       
	       Object []datos= new Object[11];
 		   tableList.setModel(model);
 		   
 		   tableList.getColumnModel().getColumn(0).setPreferredWidth(60);
 		   tableList.getColumnModel().getColumn(1).setPreferredWidth(15);
 		   tableList.getColumnModel().getColumn(2).setPreferredWidth(300);
 		   tableList.getColumnModel().getColumn(3).setPreferredWidth(45);
 		   tableList.getColumnModel().getColumn(4).setPreferredWidth(30);
 		   tableList.getColumnModel().getColumn(5).setPreferredWidth(50);
 		   tableList.getColumnModel().getColumn(6).setPreferredWidth(5);
 		   //tableList.getColumnModel().getColumn(6).setPreferredWidth(30);
 		   //tableList.getColumnModel().getColumn(7).setPreferredWidth(30);

 			
 		/* JComboBox comboBox = new JComboBox();
 		 comboBox.addItem("TRUE");
 		 comboBox.addItem("FALSE");
 		 TableColumn ComboColumn = tableList.getColumnModel().getColumn(6);
 		 ComboColumn.setCellEditor(new DefaultCellEditor(comboBox));*/
 		  JCheckBox checkBox = new JCheckBox();
 		  
		   Statement st = conexion.gConnection().createStatement();
		   ResultSet rs=st.executeQuery(consultar);
           while(rs.next()) {
        	ID_APE_TUR= rs.getInt("IdTurnoA");
            datos[0]=" "+Menu.formatid_9.format(rs.getInt("Id_D"));
            datos[1]=" "+rs.getInt("NumeroH");
            datos[2]=" "+rs.getString("DescripcionH");
            datos[3]=" "+Menu.formateadorCurrency.format(rs.getFloat("precioH"))+" ";
            datos[4]=" "+rs.getString("DiasH")+" ";
            datos[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("ImporteH")) +" ";
            datos[6]=true;
            totalitems=totalitems+1;
            model.addRow(datos);
            tableList.setModel(model);

            IMPORTES=IMPORTES + rs.getFloat("ImporteH");
			DESCT=rs.getFloat("DsctTA");
			
			textCId.setText(Menu.formatid_7.format(COD_CLI));
			textCNom.setText(rs.getString("NombreCli"));
           	textCDir.setText(rs.getString("DireccionCli"));
           	textCRuc.setText(rs.getString("RucCli").toString());
           	
	        }
           	rs.close();
           	st.close();
           	
           // LLENO LOS SERVICIOS
           String c="Select * from HABITACION,DETALLE_A_HABITACION,ALQUILER,CLIENTES,DETALLE_A_CONSUMO where DETALLE_A_CONSUMO.Id_D = DETALLE_A_HABITACION.Id_D and  ALQUILER.Id_Cli=CLIENTES.Id_Cli and DETALLE_A_HABITACION.NumeroH = HABITACION.NumeroHab and ALQUILER.Id_A = DETALLE_A_HABITACION.Id_A and ALQUILER.Id_A ='" + this.COD_HOSPEDAJE + "'";
           Object []dato= new Object[11];
 		   Statement s = conexion.gConnection().createStatement();
 		   ResultSet r=s.executeQuery(c);
 		   tableList.setModel(model);
 		  
           while(r.next()) {
            dato[0]=" "+Menu.formatid_9.format(r.getInt("Id_C"));
            dato[1]=" "+r.getInt("NumeroH");
            dato[2]=" "+r.getString("DescripcionC");
            dato[3]=" "+Menu.formateadorCurrency.format(r.getFloat("precioC"))+" ";
            dato[4]=" "+r.getString("CantidadC");
            dato[5]=" "+Menu.formateadorCurrency.format(r.getFloat("TotalC"))+" ";
            dato[6]=true;
            totalitems=totalitems+1;
            model.addRow(dato);
            tableList.setModel(model);
            
            IMPORTES=IMPORTES + r.getFloat("ImporteC");
			DESCT=DESCT + r.getFloat("DsctC");

	        }
           	r.close();
           	s.close();
           	textFormatTotal.setText(Float.toString(IMPORTES- DESCT));
           	
           String consult="Select * from CUENTA_HUESPED as C,ALQUILER as A where C.Id_A=A.Id_A and C.Id_A='" + this.COD_HOSPEDAJE+ "'";
  		   Statement stt = conexion.gConnection().createStatement();
  		   ResultSet rrr=stt.executeQuery(consult);
           while(rrr.next()) {
             SALDO= SALDO + rrr.getFloat("SaldoCta");
             String consultt="Select * from CUENTA_HUESPED_PAGOS as P,CUENTA_HUESPED as C where P.Id_Cta=C.Id_Cta and P.Id_Cta='" + rrr.getInt("Id_Cta") + "'";
    		 Statement ss = conexion.gConnection().createStatement();
    		 ResultSet re=ss.executeQuery(consultt);
             while(re.next()) {
               ACUENTA=ACUENTA + re.getFloat("AcuentaDetCta");
             }
             
             //System.out.println(ACUENTA + " " +SALDO);
             re.close();
             ss.close();
           }
           rrr.close();
           stt.close();

			textUser.setText("TURNO: "+ VentanaLogin.TUR_CORTO +"  / OPERADOR: "+ VentanaLogin.NOMBRE_USU_CORTO);
           // MODELO TABLE
     	   int CONT=14;
     	   if (totalitems>0) {
     		   int l=0;
 	            l=CONT-totalitems;
 	            if ( CONT>totalitems) {
 	            	Object []registros= new Object[l];
 				    for (int n=0; n<l;n++) {
 					    model.addRow(registros);
 					    tableList.setModel(model);
 				    }
 	            } 
 		   }else{
 			  Object []registros= new Object[CONT];
 			    for (int n=0; n<CONT;n++) {
 				    model.addRow(registros);
 				    tableList.setModel(model);
 			    }
 		   }
     	   // FIN MODELOTABLE
     	   DefaultCellEditor defaultCellEditor2=new DefaultCellEditor(checkBox);
     	   tableList.getColumnModel().getColumn(6).setCellEditor(defaultCellEditor2);
     	   
           	textFormatImporte.setText(Menu.formateadorCurrency.format(IMPORTES));
            textFormatDsct.setText(Menu.formateadorCurrency.format(DESCT));
            TOTAL=IMPORTES ;
            textFormatSubTotal.setText(Menu.formateadorCurrency.format(TOTAL/Menu.IGV));
            textFormatTotal.setText(Menu.formateadorCurrency.format(TOTAL));
            textFormatIgv.setText(Menu.formateadorCurrency.format(TOTAL-Float.parseFloat(textFormatSubTotal.getText().replaceAll(",", ""))));
            
        	//textFormatDebe.setText(Menu.formateadorCurrency.format(TOTAL));

            //textFormatAmortizo.setText(Menu.formateadorCurrency.format(ACUENTA));
        	//textFormatDebe.setText(Menu.formateadorCurrency.format(TOTAL - ACUENTA));

            
			//textFormatDebe.setText(Menu.formateadorCurrency.format(SALDO));

			buttonPrint.setEnabled(false);
            textFormatAmortizo.setText(Menu.formateadorCurrency.format(ACUENTA));
            textFormatDebe.setText(Menu.formateadorCurrency.format(SALDO));
            

            tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("VENTANA_GENERAR_DOCUMENTO"));//ESTABLESCO COLOR CELDAS
           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
        conexion.DesconectarDB();
	}
	
	public void crearOtros() {
		Chooserdate = new JDateChooser();
		Chooserdate.setDateFormatString("dd-MMM-yyyy");
		Chooserdate.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		Chooserdate.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)Chooserdate.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)Chooserdate.getDateEditor()).setEditable(false);
		Chooserdate.setBounds(418, 78, 100, 23);
		panelDto.add(Chooserdate);
		Chooserdate.addPropertyChangeListener(this);
		//Chooserdate.setDate(Menu.fecha);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(98, 60, 518, 2);
		panelDto.add(separator_1);
	}
	public void crearLabels() {
		lbl1= new JLabel("::::");
		lbl1.setBounds(15, 16, 43, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Huesped:");
		lbl2.setBounds(0, 35, 58, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("::::");
		lbl3.setBounds(17, 76, 43, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl20 = new JLabel("::::");
		lbl20.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl20.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl20.setBounds(17, 86, 43, 14);
		panelDto.add(lbl20);
		
		lbl5= new JLabel("Fecha de emisión:");
		lbl5.setBounds(419, 63, 96, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);

		// ============================================= TERCER NIVEL
		lbl10= new JLabel("Documento:");
		lbl10.setBounds(139, 63, 68, 14);
		panelDto.add(lbl10);
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(Menu.fontLabel);
		
		lbl11= new JLabel("Serie:");
		lbl11.setBounds(212, 63, 65, 14);
		panelDto.add(lbl11);
		lbl11.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl11.setFont(Menu.fontLabel);
		
		lbl12= new JLabel("Número:");
		lbl12.setBounds(282, 63, 132, 14);
		panelDto.add(lbl12);
		lbl12.setHorizontalAlignment(SwingConstants.CENTER);
		lbl12.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Hora:");
		lbl8.setToolTipText("");
		lbl8.setBounds(558, 63, 58, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		
		lbl21= new JLabel("Importe:");
		lbl21.setBounds(106, 11, 56, 14);
		panelBt.add(lbl21);
		lbl21.setHorizontalAlignment(SwingConstants.CENTER);
		lbl21.setFont(Menu.fontLabel);
		
		lbl14= new JLabel("Dscto:");
		lbl14.setBounds(161, 11, 56, 14);
		panelBt.add(lbl14);
		lbl14.setHorizontalAlignment(SwingConstants.CENTER);
		lbl14.setFont(Menu.fontLabel);
		
		lbl15= new JLabel("Acuenta:");
		lbl15.setBounds(393, 11, 53, 14);
		panelBt.add(lbl15);
		lbl15.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl15.setFont(Menu.fontLabel);
		
		lbl16= new JLabel("Sub total:");
		lbl16.setBounds(219, 11, 56, 14);
		panelBt.add(lbl16);
		lbl16.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl16.setFont(Menu.fontLabel);
		
		lbl17= new JLabel("Total:");
		lbl17.setBounds(335, 11, 53, 14);
		panelBt.add(lbl17);
		lbl17.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl17.setFont(Menu.fontLabel);
		
		lbl18= new JLabel("Igv:");
		lbl18.setBounds(274, 11, 56, 14);
		panelBt.add(lbl18);
		lbl18.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl18.setFont(Menu.fontLabel);

		lbl19= new JLabel("Saldo:");
		lbl19.setBounds(450, 11, 54, 14);
		panelBt.add(lbl19);
		lbl19.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl19.setFont(Menu.fontLabel);
		
		lblHra= new JLabel("00:00:00");
		lblHra.setForeground(new Color(165, 42, 42));
		lblHra.setBounds(520, 77, 96, 23);
		lblHra.setOpaque(true);
		lblHra.setBackground(SystemColor.control);
		panelDto.add(lblHra);
		lblHra.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHra.setFont(new Font("Tahoma", Font.BOLD, 12));
	}

	public void crearTextFields(){
		textUser = new JTextField("");
		textUser.setHorizontalAlignment(SwingConstants.RIGHT);
		textUser.setColumns(10);
		textUser.setForeground(new Color(25, 25, 112));
		textUser.setEditable(false);
		textUser.setBackground(new Color(240, 240, 240));
		textUser.setBorder(new MatteBorder(0, 0, 1, 0, (Color) SystemColor.scrollbar));
		textUser.setBounds(98, 13, 518, 16);
		panelDto.add(textUser);
		
		textCId = new JTextField();
		textCId.setHorizontalAlignment(SwingConstants.CENTER);
		textCId.setEditable(false);
		textCId.setToolTipText("");
		textCId.setColumns(10);
		textCId.setForeground(new Color(75, 0, 130));
		textCId.setBackground(SystemColor.menu);
		textCId.setBounds(98, 33, 65, 22);
		textCId.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(205, 92, 92)));
		panelDto.add(textCId);
		
		textCNom = new JTextField();
		textCNom.setEditable(false);
		textCNom.setToolTipText("");
		textCNom.setColumns(10);
		textCNom.setForeground(new Color(72, 61, 139));
		textCNom.setBackground(SystemColor.menu);
		textCNom.setBounds(166, 33, 366, 22);
		textCNom.addActionListener(this);
		//extCNom.addFocusListener(this);
		//textCNom.addKeyListener(this);
		textCNom.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(205, 92, 92)));
		panelDto.add(textCNom);
		
		textCDir = new JTextField();
		textCDir.setEditable(false);
		textCDir.setVisible(false);
		textCDir.setColumns(10);
		textCDir.setForeground(new Color(75, 0, 130));
		textCDir.setBackground(SystemColor.menu);
		textCDir.setBounds(282, 0, 334, 11);
		textCDir.addActionListener(this);
		//extCNom.addFocusListener(this);
		//textCNom.addKeyListener(this);
		textCDir.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(205, 92, 92)));
		panelDto.add(textCDir);
		
		textCRuc = new JTextField("");
		textCRuc.setHorizontalAlignment(SwingConstants.CENTER);
		textCRuc.setToolTipText("R.U.C.");
		textCRuc.setColumns(10);
		textCRuc.setForeground(new Color(75, 0, 130));
		textCRuc.setEditable(false);
		textCRuc.setBackground(SystemColor.menu);
		textCRuc.setBounds(536, 33, 80, 22);
		textCRuc.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(205, 92, 92)));
		panelDto.add(textCRuc);
		
		textNumero= new JTextField("");
		textNumero.setEditable(false);
		textNumero.setHorizontalAlignment(SwingConstants.CENTER);
		textNumero.setToolTipText("");
		textNumero.setColumns(10);
		textNumero.setForeground(new Color(0, 102, 102));
		textNumero.setBackground(new Color(255, 255, 255));
		textNumero.setBounds(282, 79, 132, 22);
		textNumero.addKeyListener(this);
		panelDto.add(textNumero);
		
		
		textFormatImporte = new JFormattedTextField();
		textFormatImporte.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		textFormatImporte.setEditable(false);
		textFormatImporte.setText("25.00");
		textFormatImporte.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatImporte.setBounds(106, 25, 56, 21);
		textFormatImporte.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(64, 64, 64)));
		panelBt.add(textFormatImporte);
		
		textFormatDsct = new JFormattedTextField();
		textFormatDsct.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		textFormatDsct.setEditable(false);
		textFormatDsct.setText("0.00");
		textFormatDsct.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatDsct.setBounds(164, 25, 53, 21);
		textFormatDsct.addActionListener(this);
		textFormatDsct.addPropertyChangeListener(this);
		textFormatDsct.addKeyListener(this);
		textFormatDsct.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(255, 0, 0)));
		panelBt.add(textFormatDsct);
		
		textFormatSubTotal = new JFormattedTextField();
		textFormatSubTotal.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		textFormatSubTotal.setEditable(false);
		textFormatSubTotal.setText("25.00");
		textFormatSubTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatSubTotal.setBounds(219, 25, 56, 21);
		textFormatSubTotal.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(64, 64, 64)));
		panelBt.add(textFormatSubTotal);
		
		textFormatIgv= new JFormattedTextField();
		textFormatIgv.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		textFormatIgv.setEditable(false);
		textFormatIgv.setText("25.00");
		textFormatIgv.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatIgv.setBounds(277, 25, 53, 21);
		textFormatIgv.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(255, 0, 0)));
		panelBt.add(textFormatIgv);
		
		textFormatAmortizo = new JFormattedTextField();
		textFormatAmortizo.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		textFormatAmortizo.setEditable(false);
		textFormatAmortizo.setText("0.00");
		textFormatAmortizo.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatAmortizo.setBounds(390, 25, 56, 21);
		textFormatAmortizo.addActionListener(this);
		textFormatAmortizo.addPropertyChangeListener(this);
		textFormatAmortizo.addKeyListener(this);
		textFormatAmortizo.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(0, 0, 255)));
		panelBt.add(textFormatAmortizo);
		
		textFormatTotal = new JFormattedTextField();
		textFormatTotal.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		textFormatTotal.setEditable(false);
		textFormatTotal.setText("0.00");
		textFormatTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatTotal.setBounds(332, 25, 56, 21);
		textFormatTotal.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(0, 0, 255)));
		panelBt.add(textFormatTotal);

		textFormatDebe = new JFormattedTextField();
		textFormatDebe.setFont(new Font("Arial Narrow", Font.PLAIN, 15));
		textFormatDebe.setEditable(false);
		textFormatDebe.setText("0.00");
		textFormatDebe.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatDebe.setBounds(448, 25, 56, 21);
		textFormatDebe.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(0, 0, 255)));
		panelBt.add(textFormatDebe);
	}
	public void crearComboBox() {
		
		cbDocumento= new JComboBox<>();
		cbDocumento.setBounds(63, 79, 144, 21);
		cbDocumento.setFont(Menu.fontText);
		cbDocumento.addActionListener(this);
		cbDocumento.addFocusListener(this);
		cbDocumento.addKeyListener(this);
		panelDto.add(cbDocumento);
		
		cbSerie = new JComboBox<>();
		cbSerie.setBounds(212, 79, 65, 21);
		cbSerie.setFont(Menu.fontText);
		cbSerie.addActionListener(this);
		cbSerie.addFocusListener(this);
		cbSerie.addKeyListener(this);
		panelDto.add(cbSerie);
	}
	public void crearButtons() {
		buttonActualizar= new JButton("");
		buttonActualizar.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/Refresh.png")));
		buttonActualizar.setToolTipText("Refrescar ventana");
		buttonActualizar.addActionListener(this);
		buttonActualizar.setBounds(82, 23, 25, 23);
		panelBt.add(buttonActualizar);
		
		buttonCliente= new JButton("");
		buttonCliente.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/add_friend.png")));
		buttonCliente.setToolTipText("Alta de clientes");
		buttonCliente.addActionListener(this);
		buttonCliente.setBounds(63, 32, 30, 23);
		panelDto.add(buttonCliente);
		
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("<html> Desocupar habitación(es) <br> y generar documento");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(508, 23, 40, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/flechaD.png")));
		panelBt.add(buttonGrabar);
		
		buttonPrint = new JButton("");
		buttonPrint.setToolTipText("Print...");
		buttonPrint.addActionListener(this);
		buttonPrint.setBounds(551, 23, 36, 23);
		buttonPrint.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/print.png")));
		panelBt.add(buttonPrint);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(590, 23, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/Exit.png")));
		panelBt.add(buttonSalir);
		
		buttonCuenta= new JButton("Cta.");
		buttonCuenta.setToolTipText("Cuenta del huesped ");
		buttonCuenta.addActionListener(this);
		buttonCuenta.setBounds(10, 23, 73, 23);
		buttonCuenta.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/Dollar_Green.png")));
		panelBt.add(buttonCuenta);
		buttonCuenta.setVisible(true);
		
		buttonAnular= new JButton("Out");
		buttonAnular.setToolTipText("Desocupar habitación(es)");
		buttonAnular.addActionListener(this);
		buttonAnular.setBounds(10, 23, 80, 23);
		buttonAnular.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/flechaD.png")));
		panelBt.add(buttonAnular);
		buttonAnular.setVisible(true);
		
		buttonCambiarDoc= new JButton("");
		buttonCambiarDoc.setEnabled(false);
		buttonCambiarDoc.setToolTipText("<html> Cambiar documento <br>......");
		buttonCambiarDoc.addActionListener(this);
		buttonCambiarDoc.setBounds(508, 23, 40, 23);
		buttonCambiarDoc.setIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/convert.png")));
		panelBt.add(buttonCambiarDoc);
		buttonCambiarDoc.setVisible(false);
	}
	public void crearTable(){
		tableList = new JTable();
		tableList.setFillsViewportHeight(true);
		tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableList.setShowHorizontalLines(false);
		tableList.setForeground(new Color(128, 128, 128));
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tableList.setBounds(10, 303, 665, 229);
		
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 16, 615, 248);
        panelLst.add(scrollTable);
    	tableList.setShowVerticalLines(true);
    	tableList.setGridColor(new Color(255, 160, 122));
    	tableList.setBackground(new Color(255, 255, 255));
	}
	
	public void llenarComboDocumento() {
		cbDocumento.removeAllItems();
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from DOCUMENTO");
				for (; resultSet.next();) {
					String [] lista = {resultSet.getString("TipoDoc")};
				    Arrays.sort (lista);
					for (String llenar:lista) {
						ID_DOC =resultSet.getString("Id_Doc");
						cbDocumento.addItem(llenar);
					}
				}
				cbDocumento.setSelectedIndex(-1);
			resultSet.close();	
			statement.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	
	public void llenarComboSeries() {
		textNumero.setText("");
		cbSerie.removeAllItems();
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from SERIES where Id_Doc='" + ID_DOC + "'");
				for (; resultSet.next();) {
					String [] lista = {resultSet.getString("Serie").trim()};
				    Arrays.sort (lista);
					for (String llenar:lista) {
						cbSerie.addItem(Menu.formatid_4.format(Integer.parseInt(llenar.trim())));
					}
				}
			resultSet.close();	
			statement.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	/*void llenoTemp_Facturar() {
		conexion = new ConexionDB();
		String ID_TEM="",descripcion="",precio="",cantidad="",total="";
		int id_auto=0;
		try {
			
		    List<ListCliente> lista = new ArrayList<ListCliente>();
		    for(int i=0;i<tableList.getRowCount();i++)
		    {
		    	ListCliente clientes= new ListCliente(tableList.getValueAt(i, 0).toString().trim(),tableList.getValueAt(i, 1).toString().trim(), tableList.getValueAt(i, 2).toString().trim(), tableList.getValueAt(i, 3).toString().trim(), tableList.getValueAt(i, 4).toString().trim());
		        lista.add(clientes);
		        System.out.println(clientes);
		    }
		    
		   // List<ListCliente> lista = new ArrayList<ListCliente>();
			
			
			// LIMPIAR TABLE TEMPORAL
			Statement statement = conexion.gConnection().createStatement();
			ResultSet rSet = statement.executeQuery("Select* from TEMP_FACTURAR where user_temp ='" + VentanaLogin.COD_USUARIO  + "'"); 
		    while (rSet.next()==true) {
				Statement stat =  conexion.gConnection().createStatement();
				String query ="Delete from TEMP_FACTURAR where auto='" + rSet.getString("auto")  + "'";
				stat.execute(query);
				stat.close();
		    }
			statement.close();
			// EN LIMPIAR TABLE TEMPORAL
		    
			// LLENO TABLE TEMPORAL
		    for(int i=0;i<tableList.getRowCount();i++)
		    {
				// CONSULTO EL CODIGO DE LA TABLE TEMPORAL FACTURAR ::::::::::::::::::::::::::::::::
				try {
					Statement s = conexion.gConnection().createStatement();
					ResultSet rs = s.executeQuery("Select auto from TEMP_FACTURAR order by auto desc limit 0,1");
					if (rs.next()==true) {
						id_auto=Integer.parseInt(rs.getString("auto"))+1;
					}else{
						id_auto=1;
					}
					s.close();
				} catch (Exception e) {}
				// CONSULTO EL CODIGO DE LA TABLE TEMPORAL FACTURAR :::::::::::::::::::::::::::::::::
				
		    	ID_TEM= tableList.getValueAt(i, 0).toString().trim();
		    	descripcion=  tableList.getValueAt(i, 1).toString().trim();
		    	precio= tableList.getValueAt(i, 2).toString().trim();
		    	cantidad=  tableList.getValueAt(i, 3).toString().trim();
		    	total=  tableList.getValueAt(i, 4).toString().trim();

				PreparedStatement ps = conexion.gConnection().prepareStatement("INSERT INTO TEMP_FACTURAR(auto,Id_Temp,Descripcion_Temp,Precio_Temp,Cantidad_Temp,Total_Temp,User_Temp) VALUES (?,?,?,?,?,?,?)");
				ps.setInt(1, id_auto);
				ps.setString(2, ID_TEM);
				ps.setString(3, descripcion +" X S/."+ precio);
				ps.setString(4, precio);
				ps.setString(5, cantidad);
				ps.setString(6, total);
				ps.setString(7, "2");//VentanaLogin.NOM_USUARIO
				ps.execute();
				ps.close();
		    };
		    // LLENO TABLE TEMPORAL
		} catch (Exception e) {}
	}*/
	public void actionPerformed(ActionEvent action){
		if (action.getSource() == buttonActualizar){// ANULAR FACTURACION
			llenarTable(Integer.parseInt(textCId.getText()));
		}
		if (action.getSource() == buttonAnular){// ANULAR FACTURACION
			DesocuparHabitaciones();
		}
		if (action.getSource() == buttonCuenta){// CONSULTO CUENTA HUESPED
			mostrarPagos();
		}
		if (action.getSource() == buttonCliente){// BUSCAR CLIENTE
				VentanaCliente ven4= new VentanaCliente();
				 VentanaCliente.FILTRO_CLI="VGD";
			    ven4.frame.setVisible(true);
			   
		}
		if (action.getSource().equals(cbDocumento)) {
			ID_DOC="";
			conexion = new ConexionDB();
			try {
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * from DOCUMENTO where TipoDoc='" + cbDocumento.getSelectedItem() + "'");
				if ( resultSet.next()==true) {
					ID_DOC =resultSet.getString("Id_Doc");
				}
				resultSet.close();
				statement.close();
				buttonPrint.setEnabled(true);
				if(cbDocumento.getSelectedItem().equals("NINGUNO")) {
					buttonPrint.setEnabled(false);
				}
			} catch (Exception e) {}
			conexion.DesconectarDB();
			llenarComboSeries();
		}
		if (action.getSource().equals(cbSerie)) {
			conexion = new ConexionDB();
			try {
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * from SERIES where Id_Doc='" + ID_DOC.trim() + "'and Serie='" + cbSerie.getSelectedItem()+ "'");
				if (resultSet.next()==true) {
					buttonCambiarDoc.setEnabled(true);
					textNumero.setText(Menu.formatid_9.format(Integer.parseInt(resultSet.getString("NumeroSerie"))+1));
					ID_SERIE=Integer.parseInt(resultSet.getString("Id_S"));
				}
				resultSet.close();
				statement.close();
			} catch (Exception e) {}
			conexion.DesconectarDB();
		}
		if (action.getSource().equals(buttonGrabar)) {
			insertarUpdate();
		}
		if (action.getSource().equals(buttonPrint)) {
			if (this.COD_HOSPEDAJE>0){
				Imprimir(this.COD_HOSPEDAJE);
			}else{
				Imprimir(RptFacturacion.COD_A);
			}
		}
		if (action.getSource().equals(buttonSalir)) {
			frame.dispose();
		}
		if (action.getSource().equals(buttonCambiarDoc)) {
			cambiarDocumento(RptFacturacion.COD_A);
		}
	}
	
	void limpioTemp() {
		conexion= new ConexionDB();
		try {
			Statement st = conexion.gConnection().createStatement();
			ResultSet rSet = st.executeQuery("Select* from TEMP_FACTURAR where user_temp ='" + VentanaLogin.COD_EMP_USER  + "'"); 
		    while (rSet.next()==true) {
				Statement stat =  conexion.gConnection().createStatement();
				String query ="Delete from TEMP_FACTURAR where auto='" + rSet.getString("auto")  + "'";
				stat.execute(query);
				stat.close();
		    }
		    rSet.close();
			st.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	void creoClaveTemp() {
		try {
			Statement s = conexion.gConnection().createStatement();
			ResultSet rs = s.executeQuery("Select auto from TEMP_FACTURAR order by auto desc limit 0,1");
			if (rs.next()==true) {
				id_auto=Integer.parseInt(rs.getString("auto"))+1;
			}else{
				id_auto=1;
			}
			rs.close();
			s.close();
		} catch (Exception e) {}
	}
	 public void Imprimir(int COD){
			if (VentanaLogin.COD_EMP_USER.trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "No se encontro ningun usuario...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				return;
				}
			if (cbDocumento.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null, "No se encontro ningun documento seleccionado...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbDocumento.requestFocus();
				return;
				}
			try {
				limpioTemp();
				
				conexion= new ConexionDB();
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet =statement.executeQuery("Select * from DOCUMENTO as D, SERIES as S, ALQUILER AS A ,FACTURACION as F, DETALLE_A_HABITACION AS DH,CLIENTES AS C,HABITACION AS H where F.Id_A=A.Id_A and A.Id_Cli=C.Id_Cli and DH.Id_A=A.Id_A and DH.NumeroH=H.NumeroHab and S.Id_Doc=D.Id_Doc and F.Id_Doc=D.Id_Doc and A.EstadoA !='" + 0 + "'and F.EstadoFac='" + 1 + "'and A.Id_A='" + COD + "'");
				buttonAnular.setVisible(false);
				
				float 	precio=0,importe=0;
				int 	dias=0,cantidad=0;
				if (resultSet.next()==true){ 
					int ALQUILER= resultSet.getInt("Id_A");

					// ******************************************************************* CONSULTO HABITACIONES ALQUILADAS
					ResultSet r=statement.executeQuery("Select * from DETALLE_A_HABITACION where Id_A='" + ALQUILER + "'");
					precio=0;importe=0;dias=0;cantidad=0;
					while (r.next()==true){ 
						dias= dias + r.getInt("DiasH");
						precio=  precio +r.getFloat("PrecioH");
						importe=  importe + r.getFloat("ImporteH");
						
						// LLENO POR HABITACION ALQUILA
						/*dias=  r.getInt("DiasH");
						precio=  r.getFloat("PrecioH");
						importe=  r.getFloat("ImporteH");
						
						creoClaveTemp();
						PreparedStatement ps = conexion.gConnection().prepareStatement("INSERT INTO TEMP_FACTURAR(auto,Id_Temp,Cantidad_Temp,Descripcion_Temp,Precio_Temp,Total_Temp,User_Temp) VALUES (?,?,?,?,?,?,?)");
						ps.setInt(1, id_auto);
						ps.setInt(2, 0);
						ps.setInt(3, dias);
						ps.setString(4, "ALOJAMIENTO" + " " + r.getString("DescripcionH"));
						ps.setFloat(5, precio);
						ps.setFloat(6, importe);
						ps.setString(7, VentanaLogin.COD_USUARIO);
						ps.execute();
						ps.close();*/
						// END LLENO POR HABITACION ALQUILADA
					}
					r.close();
					// LLENO POR TODO EL ALQUILER
					creoClaveTemp();
					PreparedStatement ps = conexion.gConnection().prepareStatement("INSERT INTO TEMP_FACTURAR(auto,Id_Temp,Cantidad_Temp,Descripcion_Temp,Precio_Temp,Total_Temp,User_Temp) VALUES (?,?,?,?,?,?,?)");
					ps.setInt(1, id_auto);
					ps.setInt(2, 0);
					ps.setInt(3, dias);
					ps.setString(4, "ALOJAMIENTO");
					ps.setFloat(5, precio);
					ps.setFloat(6, importe);
					ps.setString(7, VentanaLogin.COD_EMP_USER);
					ps.execute();
					ps.close();
					// end LLENO POR TODO EL ALQUILER
					
					// *******************************************************************CONSULTO HABITACIONES ALQUILADAS
					
					// *******************************************************************CONSULTO LOS CONSUMOS
					ResultSet rr=statement.executeQuery("Select * from DETALLE_A_HABITACION AS DH, DETALLE_A_CONSUMO AS C  where C.Id_D=DH.Id_D and DH.Id_A='" + ALQUILER + "'");
					precio=0;importe=0;dias=0;cantidad=0;
					while (rr.next()==true){ 
						cantidad= cantidad + 1;
						precio= precio + rr.getFloat("PrecioC");
						importe= importe + rr.getFloat("TotalC");
					}
					rr.close();
					if (cantidad>0){ 
						creoClaveTemp();
						PreparedStatement pss = conexion.gConnection().prepareStatement("INSERT INTO TEMP_FACTURAR(auto,Id_Temp,Cantidad_Temp,Descripcion_Temp,Precio_Temp,Total_Temp,User_Temp) VALUES (?,?,?,?,?,?,?)");
						pss.setInt(1, id_auto);
						pss.setInt(2, 0);
						pss.setInt(3, cantidad);
						pss.setString(4, "CONSUMO");
						pss.setFloat(5, precio);
						pss.setFloat(6, importe);
						pss.setString(7, VentanaLogin.COD_EMP_USER);
						pss.execute();
						pss.close();
					}
					// ********************************************************************CONSULTO LOS CONSUMOS
				}
				resultSet.close();
				statement.close();
				try {
					if (cbDocumento.getSelectedItem()!=null && cbSerie.getSelectedItem()!=null){
						//JasperViewer visor;
						
			    		SimpleDateFormat formm = new SimpleDateFormat("dd/MM/yyyy");
			    		String FechaEntrada = formm.format(Chooserdate.getDate());
						
						//CREAR EL MAPA DE PARAMETROS
						Map<String,Object> parameters = new HashMap<String,Object>();
						parameters.put("PrtId_User",new Integer (VentanaLogin.COD_EMP_USER));
						parameters.put("PrtTurno",new String (VentanaLogin.TUR_CORTO +" "+ VentanaLogin.ID_APETURA_CAJA));
						parameters.put("PrtNombreUser",new String (VentanaLogin.NOMBRE_USU_CORTO));
						
						parameters.put("PrtFecha",new String (FechaEntrada.trim()  +"   " + Menu.HORA.trim()));//12 HORAS HILO
						parameters.put("PrtCliente",new String (textCNom.getText()));
						parameters.put("PrtDireccion",new String (textCDir.getText()));
						parameters.put("PrtDni",new String (textCRuc.getText()));
						
						parameters.put("PrtCorrelativo",new String (cbSerie.getSelectedItem() +"  "+textNumero.getText()));
						
						parameters.put("PrtImporte",new Float (textFormatImporte.getText().replaceAll(",", "")));
						parameters.put("PrtDsct",new Float (textFormatDsct.getText().replaceAll(",", "")));
						parameters.put("PrtSubTotal",new Float (textFormatSubTotal.getText().replaceAll(",", "")));
						parameters.put("PrtIgv",new Float (textFormatIgv.getText().replaceAll(",", "")));
						parameters.put("PrtTotal",new Float (textFormatTotal.getText().replaceAll(",", "")));
						
						JasperPrint jp = JasperFillManager.fillReport(Menu.URL + cbDocumento.getSelectedItem()+".jasper",parameters,conexion.gConnection());
						//::::::::::::::::::::::::::::::: VISTA PREVIA NORMAL
						//JasperViewer.viewReport(jp,false); 
						//::::::::::::::::::::::::::::::: VISTA PREVIA CON TITULO
						//visor = new JasperViewer(jp,false) ;
						//visor.setTitle(cbDocumento.getSelectedItem()+" :");
						//visor.setVisible(true) ; 
						//visor.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
						//:::::::::::::::::::::::::::::::IMPRIME SIN VISTA PREVIA
						JasperPrintManager.printReport(jp, false);
						
					    //EXPORTAR EL INFORME A PDF
					    //JasperExportManager.exportReportToPdfFile(jp,"D:\\PROGRAMACION\\JAVA\\SOFTLE_HOTEL\\src\\modelo\\Reportes\\Ticket.pdf");
					}else{
						JOptionPane.showMessageDialog(null, "Documento no se encuentra registrado...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
						cbDocumento.requestFocus();
					}
				} catch (Exception e) {}
			}catch (Throwable e) {
				e.printStackTrace();
				}
		conexion.DesconectarDB();	
	   }
	 
	 
	 
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
      	if (e.getSource()==(Chooserdate)){
    		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
    		dateEmision = form.format(Chooserdate.getDate());
		}
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent evento) {
		// TODO Auto-generated method stub
		char car=evento.getKeyChar();
		if (evento.getSource() == cbDocumento){// CANCELAR
			llenarComboSeries();
		}
		if (evento.getSource().equals(textNumero)){
			if (textNumero.getText().toLowerCase().isEmpty()|| textNumero.getText().toLowerCase().length()>9){
				textNumero.requestFocus();
				textNumero.selectAll();
				textNumero.setText(null);
				} 
				else if (car==KeyEvent.VK_ENTER || textNumero.getText().toLowerCase().length()==9){
					textNumero.setText(Menu.formatid_9.format(Integer.parseInt(textNumero.getText())));
				}
		}
	}
	@Override
	public void keyTyped(KeyEvent evet) {
		// TODO Auto-generated method stub
		char car=evet.getKeyChar();
		if (evet.getSource() == textNumero){ 
			if ((car<'0'||car>'9'))evet.consume();
		}
	}
	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	void mostrarPagos(){
		llenarTable(Integer.parseInt(textCId.getText()));
		try {
			conexion = new ConexionDB();
			String consul="Select * from CUENTA_HUESPED where Id_A  ='" + this.COD_HOSPEDAJE  +"'and estadoCta ='" + "A" +"'";
		  	Statement st = conexion.gConnection().createStatement();
			ResultSet rs=st.executeQuery(consul);
			if (rs.next()==true) {
				VentanaCuentaHuesped v = new VentanaCuentaHuesped(this.COD_HOSPEDAJE );
				v.lblAlq.setText("HABITACION: "+this.NRO +" | " + this.HAB);
				v.lblAbonado.setText("HUESPED: "+ this.HUESP);
				//v.cbTipo.removeAllItems();
		  		//v.cbTipo.addItem("ALQUILER");
				VentanaCuentaHuesped.frame.setVisible(true);
				v.textBus.requestFocus();
				return;
			}else{
				JOptionPane.showMessageDialog(null, "La cta ya fue cancelada...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			}
			rs.close();
			st.close();
			conexion.DesconectarDB();
		} catch (Exception e) {}	
	}
	//METODO GRABAR MODIFICAR
	public void insertarUpdate() {
		//****************************************** CONSULTO SI TIENE CTA PENDIENTE
		try {
			llenarTable(Integer.parseInt(textCId.getText()));
			conexion = new ConexionDB();
			String consul="Select * from CUENTA_HUESPED where Id_A  ='" + this.COD_HOSPEDAJE  +"'and estadoCta ='" + "A" +"'";
		  	Statement st = conexion.gConnection().createStatement();
			ResultSet rs=st.executeQuery(consul);
			if (rs.next()==true) {
				VentanaCuentaHuesped v = new VentanaCuentaHuesped(this.COD_HOSPEDAJE);
				v.lblAlq.setText("HABITACION: "+this.NRO +" | " + this.HAB);
				v.lblAbonado.setText("HUESPED: "+ this.HUESP);
				//v.cbTipo.removeAllItems();
		  		//v.cbTipo.addItem("ALQUILER");
				VentanaCuentaHuesped.frame.setVisible(true);
				v.textBus.requestFocus();
				return;
			}
			st.close();
			rs.close();
			conexion.DesconectarDB();
		} catch (Exception e) {}
		//**************************************END CONSULTO SI TIENE CTA PENDIENTE
		if (VentanaLogin.COD_EMP_USER.trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "No se encontro ningun usuario...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (this.COD_HOSPEDAJE==0){
			JOptionPane.showMessageDialog(null,"Código: " + this.COD_HOSPEDAJE + "\nno existe ningun alquiler...! \n" +"debe cambiar el estado para poder alquilar",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (textCId.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textCId.requestFocus();
			return;
		}
		if (cbDocumento.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Seleccione el tipo de documento...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbDocumento.requestFocus();
			return;
		}
		if (cbSerie.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Seleccione la serie del documento",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbSerie.requestFocus();
			return;
		}
		if (textNumero.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textNumero.requestFocus();
			return;
		}

		if (dateEmision.trim().equals("")){
			JOptionPane.showMessageDialog(null, "debe ingresar la fecha de emisión...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			Chooserdate.requestFocus();
			return;
		}
		if (Float.parseFloat(textFormatDebe.getText())!=0){
			//insertCaja();
		}
		conexion = new ConexionDB();
		try {
			// CONSULTO EL NUMERO DL DOCUMENTO ::::::::::::::::::::::::::::::::
			int NUMERO=0;
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery("Select * from SERIES where Id_S='" + ID_SERIE + "'order by NumeroSerie desc limit 0,1");
			if (rs.next()== true) {
				NUMERO=(Integer.parseInt(rs.getString("NumeroSerie")));
				NUMERO=NUMERO+1;
			}
			rs.close();
			st.close();
			// GRABO NUMERO
			PreparedStatement p = conexion.gConnection().prepareStatement("UPDATE SERIES SET Id_S = ?,"
					 + "NumeroSerie =?"
	                 + "WHERE Id_S = '"+ID_SERIE+"'" );
			p.setString(1, Integer.toString(ID_SERIE));
			p.setString(2, Integer.toString(NUMERO));
			p.executeUpdate();
			p.close();
			
			 String sql="UPDATE ALQUILER SET Id_Cli = ?,"
	                 //+ "Id_Cli =?,"
	                 //+ "Id_Usu = ?,"
	                 //+ "Id_Doc = ?,"
	                 //+ "SerieDocA = ?,"
	                 //+ "NumeroDocA = ?,"
	                 //+ "FechaEmisionA =?,"
	                 ////+ "SubTA = ?,"
	                 ////+ "DsctTA=?,"
	                 ////+ "IgvTA =?,"
	                 ////+ "TotalA =?,"
	                 + "EstadoA =?"
	                 + "WHERE Id_A = '"+this.COD_HOSPEDAJE+"'"; 
			
			PreparedStatement ps = conexion.gConnection().prepareStatement(sql);

			//ps.setInt(1, VentanaControlHotel.ID_ALQUILER);
			ps.setInt(1, Integer.parseInt(textCId.getText()));
			//ps.setString(3, VentanaLogin.COD_USUARIO.trim());
			//ps.setNString(4, ID_DOC);
			//ps.setString(5, (String)cbSerie.getSelectedItem());
			//ps.setString(6, textNumero.getText());
			//ps.setString(7, textFEmision.getText());
			////ps.setString(7, textFormatImporte.getText());
			////ps.setString(8, textFormatDsct.getText());
			////ps.setString(9, textFormatIgv.getText());
			////ps.setString(10, textFormatTotal.getText());
			ps.setString(2, "2");  // ESTADO FACTURADO
			ps.executeUpdate();
			ps.close();

			// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: GRABO MI FACTURACION
			// CONSULTO EL CODIGO DEL ALQUILER ::::::::::::::::::::::::::::::::
			int FAC=0;
			try {
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select Id_Fac from FACTURACION order by Id_Fac desc limit 0,1");
				if (resultSet.next()== true) {
					FAC=resultSet.getInt("Id_Fac")+1;
				}else{
					FAC=1;
				}
				resultSet.close();
				statement.close();
			} catch (Exception e) {}
			// CONSULTO EL CODIGO DEL ALQUILER :::::::::::::::::::::::::::::::::
			String sqls ="INSERT INTO FACTURACION (Id_Fac,Id_A,Id_Doc,SerieDocFac,NumeroDocFac,FechaEmisionFac,HoraEmisionFac,SubTotFac,DsctFac,IgvFac,TotalFac,EstadoFac,IdTurnoF) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement psf = conexion.gConnection().prepareStatement(sqls);
			psf.setInt(1, FAC);
			psf.setInt(2, this.COD_HOSPEDAJE);
			psf.setNString(3, ID_DOC);
			psf.setString(4, (String)cbSerie.getSelectedItem());
			psf.setString(5, textNumero.getText());
			psf.setString(6, dateEmision.trim());
			psf.setString(7, Menu.HORA);
			psf.setString(8, textFormatImporte.getText().replaceAll(",", ""));
			psf.setString(9, textFormatDsct.getText().replaceAll(",", ""));
			psf.setString(10, textFormatIgv.getText().replaceAll(",", ""));
			psf.setString(11, textFormatTotal.getText().replaceAll(",", ""));
			psf.setString(12, "1");  // ESTADO DE DE ALOJAMIENTO
			psf.setInt(13, VentanaLogin.ID_APETURA_CAJA);  // ID TURNO
			psf.execute();
			psf.close();
			
			buttonGrabar.setEnabled(false);buttonPrint.setEnabled(true);
			JOptionPane.showMessageDialog(null, "El proceso CHECK-OUT se efectuo correctamente" + Menu.separador + 
												"::::::::::::::::::::::::::  FACTURADO  ::::::::::::::::::::::::::"
												,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			

					// CONSULTO EL DETALLE DEL ALQUILER :::::::::::::::::::::::::::::::::::::::::::::
					//String sq ="Select * from DETALLE_A_HABITACION where Id_A='" + VentanaControlHotel.ID_ALQUILER + "'and Id_D='" + VentanaControlHotel.ID_DETALLE + "'";
					String sq="Select * from ALQUILER as A ,DETALLE_A_HABITACION as DH where DH.Id_A=A.Id_A and DH.Id_A='" + this.COD_HOSPEDAJE  + "'and A.EstadoA <>'" + 0 + "'";
					Statement state = conexion.gConnection().createStatement();
					ResultSet resultSet = state.executeQuery(sq);
					String item="";
					while (resultSet.next()==true) {
						// ACTUALIZO DETALLE DE ALQUILER :::::::::::::::::::::::::::::::::::::::::::::
						String det ="UPDATE DETALLE_A_HABITACION SET Id_D = ?,"
								+ "ModoPagoH = ?"
				                //+ "AcuentaH = ?"
				                + "WHERE Id_D = '"+ resultSet.getString("Id_D") +"'";//and NumeroH = '"+ resultSet.getString("NumeroH") +"'";
						
						PreparedStatement Detalle = conexion.gConnection().prepareStatement(det);
						Detalle.setInt(1, Integer.parseInt(resultSet.getString("Id_D")));
						Detalle.setString(2, "X");
						//Detalle.setFloat(3, (resultSet.getFloat("ImporteH")-resultSet.getFloat("DsctH")));
						Detalle.executeUpdate();
						Detalle.close();
					}
					resultSet.close();
					state.close();
					int respuesta = JOptionPane.showConfirmDialog (null, "¿ Desea desocupar la habitación ?", Menu.SOFTLE_HOTEL,
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if (respuesta == JOptionPane.YES_OPTION) {
									String consul="Select * from ALQUILER as A ,DETALLE_A_HABITACION as DH where DH.Id_A=A.Id_A and DH.Id_A='" + this.COD_HOSPEDAJE  + "'and A.EstadoA <>'" + 0 + "'";
									Statement statement = conexion.gConnection().createStatement();
									ResultSet rstSet = statement.executeQuery(consul);
									while (rstSet.next()==true) {
										item=item +" #"+ rstSet.getInt("NumeroH");
										// ACTUALIZO EL ESTADO DE LA HABITACION :::::::::::::::::::::::::::::::::::::::::::::
										String s ="UPDATE HABITACION SET NumeroHab = ?,"
								                + "EstadoHab = ?"
								                + "WHERE NumeroHab = '"+ rstSet.getString("NumeroH") +"'";
												
										PreparedStatement es = conexion.gConnection().prepareStatement(s);
										es.setInt(1, Integer.parseInt(rstSet.getString("NumeroH")));
										es.setString(2, "LIMPIEZA");
										es.executeUpdate();
										es.close();
									}
									rstSet.close(); 
									statement.close();
									JOptionPane.showMessageDialog(null, "=======  Operación correcta  =========="+ Menu.separador + 
																		"Habitaciones desocupadas: " +item + Menu.separador + 
																		"el sistema genero su estado de LIMPIEZA...",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al generar documento" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		conexion.DesconectarDB();
		//frame.dispose();
	}
	
	
	void llenarParaModificarFacturacion() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet =statement.executeQuery("Select * from DOCUMENTO as D, SERIES as S, ALQUILER AS A, FACTURACION as F, DETALLE_A_HABITACION AS DH,CLIENTES AS C,HABITACION AS H where F.Id_A=A.Id_A and A.Id_Cli=C.Id_Cli and DH.Id_A=A.Id_A and DH.NumeroH=H.NumeroHab and S.Id_Doc=D.Id_Doc and F.Id_Doc=D.Id_Doc and A.EstadoA ='" + 2 + "'and F.EstadoFac='"+1+"'and A.Id_A='" + this.COD_HOSPEDAJE + "'");
			buttonAnular.setVisible(false);
			if (resultSet.next()==true){ 
					// NOMBRE CORTO DE USUARIO Y TURNO
				//END CONSULTO TURNO Y USER
				ID_APE_TUR=resultSet.getInt("IdTurnoF");
				
				panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), ":::: | Facturado | :::: REGISTRO:" + Menu.formatid_9.format(this.COD_HOSPEDAJE), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(121, 2, 0)));
				textCId.setText(Menu.formatid_7.format(Integer.parseInt(resultSet.getString("Id_Cli"))));
				textCNom.setText(resultSet.getString("NombreCli"));
				//textFormatImporte.setText(Menu.formateadorCurrency.format(Float.parseFloat(resultSet.getString("ImporteH"))));
				
				cbDocumento.setSelectedItem(resultSet.getString("TipoDoc"));
				cbSerie.setSelectedItem(resultSet.getString("SerieDocFac"));
				textNumero.setText(resultSet.getString("NumeroDocFac"));
				
				textFormatAmortizo.setText(textFormatTotal.getText());
				textFormatDebe.setText("0.00");
				
				buttonGrabar.setVisible(false);buttonPrint.setVisible(false);
				buttonAnular.setVisible(true);buttonAnular.setBounds(507, 23, 80, 23);
				
				dateEmision=resultSet.getString("FechaEmisionFac");
				Date date = Menu.formatoFecha.parse(dateEmision);
				Chooserdate.setDate(date);
				
				buttonCliente.setEnabled(false);cbDocumento.setEnabled(false);cbSerie.setEnabled(false);textNumero.setEditable(false);
				Chooserdate.setEnabled(false);tableList.setEnabled(false);
				tableList.setBackground(new Color(240, 240, 240));//tableList.setForeground(new Color(0,0,0));  
				tableList.setGridColor(new Color(240, 240, 240));
				tableList.setForeground(new Color(128, 128, 128));
				panelBt.setEnabled(false);panelDto.setEnabled(false);panelLst.setEnabled(false);
				buttonActualizar.setEnabled(false);
				if (buttonCambiarDoc.isVisible()==true){
					//CONSULTO TURNO Y USER
					conexion= new ConexionDB();
					Statement ss = conexion.gConnection().createStatement();
					ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + ID_APE_TUR +"'");
					if (rr.next()== true) {
						//textUser.setText(rr.getString("turno").trim() +": "+ rr.getString("NombresEmp").trim());
						ID_APE_TUR=rr.getInt("Id_ApeCie");
						
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
						textUser.setText("TURNO: "+ TUR +"  / OPERADOR: "+ USU);
					}
					rr.close();
					ss.close();
					conexion.DesconectarDB();
					//END CONSULTO TURNO Y USER
					
					buttonGrabar.setVisible(false);
					buttonAnular.setVisible(false);
					buttonCambiarDoc.setVisible(true);
					buttonCambiarDoc.setEnabled(false);
					buttonPrint.setVisible(true);
					buttonCliente.setEnabled(true);cbDocumento.setEnabled(true);cbSerie.setEnabled(true);
					Chooserdate.setEnabled(true);
					frame.setTitle("Facturación: Cambio de Documento");
					frame.setFrameIcon(new ImageIcon(VentanaGenerarDocumento.class.getResource("/modelo/Images/convert.png")));
				}
				
			}
			resultSet.close();
			statement.close();

		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	
	public void DesocuparHabitaciones() {
		if (this.COD_HOSPEDAJE == 0){
			JOptionPane.showMessageDialog(null, "No se encontro el código de alquiler...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea desocupar habitación(es) ?", Menu.SOFTLE_HOTEL,
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (respuesta == JOptionPane.YES_OPTION) {
			conexion = new ConexionDB();
			try {
				String consul="Select * from ALQUILER as A ,DETALLE_A_HABITACION as DH where DH.Id_A=A.Id_A and DH.Id_A='" + this.COD_HOSPEDAJE  + "'and A.EstadoA <>'" + 0 + "'";
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery(consul);
				String item="";
				while (resultSet.next()==true){
					item=item +" #"+ resultSet.getInt("NumeroH");
					// ACTUALIZO EL ESTADO DE LA HABITACION
					String s ="UPDATE HABITACION SET NumeroHab = ?,"
			                + "EstadoHab = ?"
			                + "WHERE NumeroHab = '"+ resultSet.getString("NumeroH").trim()  +"'";
							
					PreparedStatement e = conexion.gConnection().prepareStatement(s);
					e.setInt(1, Integer.parseInt(resultSet.getString("NumeroH").trim()));
					e.setString(2, "LIMPIEZA");
					e.executeUpdate();
					e.close();
				}
				resultSet.close();
				statement.close();
				JOptionPane.showMessageDialog(null, "=======  Operación correcta  =========="+ Menu.separador + 
													"habitaciones desocupadas: " + item + Menu.separador +
													"el sistema genero su estado de LIMPIEZA..." ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error al anular" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
			conexion.DesconectarDB();
		}else if (respuesta == JOptionPane.NO_OPTION) {}
	}
	
	
	
	
	
	
	
	
	
	
	//********************************************************** CAMBIAR DOCUMENTO
	public void cambiarDocumento(int COD_A) {
		conexion = new ConexionDB();
		if (VentanaLogin.COD_EMP_USER.trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "No se encontro ningun usuario...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (COD_A==0){
			JOptionPane.showMessageDialog(null,"Código: " + this.COD_HOSPEDAJE + "\nno existe ningun alquiler...! \n" +"debe cambiar el estado para poder alquilar",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (textCId.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textCId.requestFocus();
			return;
		}
		if (cbDocumento.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Seleccione el tipo de documento...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbDocumento.requestFocus();
			return;
		}
		if (cbSerie.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Seleccione la serie del documento",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbSerie.requestFocus();
			return;
		}
		if (textNumero.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textNumero.requestFocus();
			return;
		}

		if (dateEmision.trim().equals("")){
			JOptionPane.showMessageDialog(null, "debe ingresar la fecha de emisión...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			Chooserdate.requestFocus();
			return;
		}
		try {
			int resp =0;
			if (cbDocumento.getSelectedItem().equals("TICKET")||cbDocumento.getSelectedItem().equals("RECIBO")){
				resp = JOptionPane.showConfirmDialog (null, "¿ Desea ANULAR el documento anterior \ny generar el " + cbDocumento.getSelectedItem() +"  "+cbSerie.getSelectedItem()+"-"+textNumero.getText()+" ?", Menu.SOFTLE_HOTEL,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			}else if (cbDocumento.getSelectedItem().equals("BOLETA")||cbDocumento.getSelectedItem().equals("FACTURA")){
				resp = JOptionPane.showConfirmDialog (null, "¿ Desea ANULAR el documento anterior \ny generar la " + cbDocumento.getSelectedItem() +"  "+cbSerie.getSelectedItem()+"-"+textNumero.getText()+" ?", Menu.SOFTLE_HOTEL,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			}else if (cbDocumento.getSelectedItem().equals("NINGUNO")){
				resp = JOptionPane.showConfirmDialog (null, "¿ Desea ANULAR el documento anterior \ny no generar NINGUN DOCUMENTO ?\n== registro interno #" +cbSerie.getSelectedItem()+"-"+textNumero.getText()+" ==", Menu.SOFTLE_HOTEL,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			}				
			if (resp == JOptionPane.YES_OPTION) {
				// GRABO NUMERO
				PreparedStatement p = conexion.gConnection().prepareStatement("UPDATE SERIES SET NumeroSerie = ?"
		                 + "WHERE Id_S = '"+ID_SERIE+"'" );
				p.setString(1, textNumero.getText());
				p.executeUpdate();
				p.close();
				// ALQUILER
				 String sql="UPDATE ALQUILER SET Id_Cli = ?,"
		                 + "FechaEmisionA =?,"
		                 + "EstadoA =?"
		                 + "WHERE Id_A = '"+COD_A+"'"; 
				
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(textCId.getText()));
				ps.setString(2, dateEmision.trim());
				ps.setString(3, "2");  // ESTADO FACTURADO
				ps.executeUpdate();
				ps.close();
	
				// ====================================================================FACTURACION ANULAR
				String sqll="UPDATE FACTURACION SET Id_A = ?,"
		                 + "EstadoFac =?"
		                 + "WHERE Id_A = '"+COD_A+"'and EstadoFac= '"+1+"'"; 
				
				PreparedStatement psf = conexion.gConnection().prepareStatement(sqll);
				psf.setInt(1, this.COD_HOSPEDAJE);
				psf.setString(2, "0");  // ESTADO ANULADO
				psf.executeUpdate();
				psf.close();
				// ====================================================================END ANULADO 
				
				// ====================================================================GRABO MI FACTURACION
				int FAC=0;
				try {
					Statement statement = conexion.gConnection().createStatement();
					ResultSet resultSet = statement.executeQuery("Select Id_Fac from FACTURACION order by Id_Fac desc limit 0,1");
					if (resultSet.next()== true) {
						FAC=resultSet.getInt("Id_Fac")+1;
					}else{
						FAC=1;
					}
					resultSet.close();
					statement.close();
				} catch (Exception e) {}
				
				String sqls ="INSERT INTO FACTURACION (Id_Fac,Id_A,Id_Doc,SerieDocFac,NumeroDocFac,FechaEmisionFac,HoraEmisionFac,SubTotFac,DsctFac,IgvFac,TotalFac,EstadoFac,IdTurnoF) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement psff = conexion.gConnection().prepareStatement(sqls);
				psff.setInt(1, FAC);
				psff.setInt(2, this.COD_HOSPEDAJE);
				psff.setNString(3, ID_DOC);
				psff.setString(4, (String)cbSerie.getSelectedItem());
				psff.setString(5, textNumero.getText());
				psff.setString(6, dateEmision.trim());
				psff.setString(7, Menu.HORA);
				psff.setString(8, textFormatImporte.getText().replaceAll(",", ""));
				psff.setString(9, textFormatDsct.getText().replaceAll(",", ""));
				psff.setString(10, textFormatIgv.getText().replaceAll(",", ""));
				psff.setString(11, textFormatTotal.getText().replaceAll(",", ""));
				psff.setString(12, "1");  // ESTADO DE DE ALOJAMIENTO
				psff.setInt(13, VentanaLogin.ID_APETURA_CAJA);  // ID TURNO
				psff.execute();
				psff.close();
				// ====================================================================END GRABO MI FACTURACION
				
				if (cbDocumento.getSelectedItem().equals("TICKET")||cbDocumento.getSelectedItem().equals("RECIBO")||cbDocumento.getSelectedItem().equals("BOLETA")||cbDocumento.getSelectedItem().equals("FACTURA")){
					JOptionPane.showMessageDialog(null, "El cambio de documento se efectuo correctamente" + Menu.separador + 
							"::::::::::::::::::::::: "  +cbDocumento.getSelectedItem() +"  "+cbSerie.getSelectedItem()+"-"+textNumero.getText()+  " :::::::::::::::::::::::"
							,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					
				}else if (cbDocumento.getSelectedItem().equals("NINGUNO")){
					JOptionPane.showMessageDialog(null, "El cambio de documento se efectuo correctamente" + Menu.separador + 
							":::::::::::::: "  +" Registro interno #"+cbSerie.getSelectedItem()+"-"+textNumero.getText()+  " :::::::::::::::"
							,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}	
				
			}else if (resp == JOptionPane.NO_OPTION) {}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al cambiar documento" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		//frame.dispose();
	}
}
