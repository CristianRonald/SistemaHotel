package modelo.Presentacion;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import modelo.Clases.AbstractJasperReports;
import modelo.Datos.ConexionDB;
import modelo.Otros.JStatusBar;
import modelo.Otros.lengthScreem;

public class Menu implements ActionListener{
	private static ConexionDB conexion;
	public JFrame   	frame;
	public JMenuBar  	menubar;
	public JMenu 	  	menu1,menu2,menu3,menu4,menu5,menu6,menu7,menu8,menu9,menuRes;
	public static JDesktopPane Desktop;
	public JToolBar		toolBar;
	public static JPanel 		contentPane;
	public JPanel		panelEst;
	private JStatusBar statusBar;
	private JButton btnAbrirTurno,btnAltComp,btnAlVent,btnAltHab,btnTar,btnAltCli,btnHotel,btnCaj,btnHisCta,btnHisVen,btnEmp,btnCerrTurno,btnCerr,btnSec;
	// FALTA PROGRAMAR EL IGV,DOLAR
	public static float IGV=0,DOLARC=0,DOLARV=0;
	
	// FORMATO ID AUTOINCREMENTABLE
	static DecimalFormat formatid_1 = new DecimalFormat("0");
	static DecimalFormat formatid_4 = new DecimalFormat("0000");
	static DecimalFormat formatid_7 = new DecimalFormat("0000000");
	static DecimalFormat formatid_9 = new DecimalFormat("000000000");

	// FORMATO PARA REMPLAZAR , X EL .
	public static DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
	public static DecimalFormat formateadorCurrency;
    
	// FECHA LARGA
	static Date fechaLarga = new Date();
	static DateFormat formatoLargo = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy");
	public static String dateLarga = formatoLargo.format(fechaLarga);

	// FECHA ACTUAL
	static Date fecha = new Date();
	static DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
	public static String date = formatoFecha.format(fecha);

	static DateFormat formatoDiaMesAño = new SimpleDateFormat("dd-MMM-yyyy");
	
	static DateFormat formatoFechaString = new SimpleDateFormat("dd/MM/yyyy");
	
	static SimpleDateFormat formatoTimeString = new SimpleDateFormat("hh:mm:ss a");

	// PRIMER DIA DEL MES
	static Date fechaPrimerDiaMes = new Date();
	
	// HORA ACTUAL 24
	/*static Date hora = new Date();
	public static DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss a");
	public static String hour = formatoHora.format(hora);*/

	// HORA NORMAL 12 HILO 
	public static String HORA="",HORA_24="";
	public static String separador = System.getProperty("line.separator");
	public static String SOFTLE_HOTEL="SGI Hotelero Softle v. 2.01. 2016";
	
	public static Font fontLabel= new Font("Tahoma", Font.PLAIN, 11);
	public static Font fontText= new Font("Tahoma", Font.PLAIN, 11);
	
	public static Color textColorBackgroundActivo = new Color(175, 238, 238);
	public static Color textColorBackgroundInactivo = new Color(255, 255, 255);
	
	public static Color textColorForegroundActivo = new Color(0, 0, 255);
	public static Color textColorForegroundInactivo = new Color(0, 0, 0);
	
	public static Color lblColorForegroundActivo = new Color(220, 20, 60);
	public static Color lblColorForegroundInactivo = new Color(0, 0, 0);
	
	Font fontMenu= new Font("Segoe UI", Font.PLAIN, 12);
	Font fontItem= new Font("Segoe UI", Font.PLAIN, 12);
	Font fontIToolbar= new Font("Arial", Font.PLAIN, 12);
	
	public static String URL="D:\\PROGRAMACION\\JAVA\\SOFTLE_HOTEL\\src\\modelo\\Reportes\\";
	public static String URL_IMAGE="D:\\PROGRAMACION\\JAVA\\SOFTLE_HOTEL\\src\\modelo\\Images\\";
	//TAMAÑO DE PANTALLA
	int alto;
	lengthScreem l = new lengthScreem(); 
	//public static foreColor (255, 255, 255);
	
	/*public static void clock() {
		Thread clock = new Thread(){
			public void run(){
				try {
					for(;;){
					DecimalFormat format = new DecimalFormat("00");
					Calendar cr = new GregorianCalendar();
					int second = cr.get(Calendar.SECOND);
					int minute = cr.get(Calendar.MINUTE);
					int hour = cr.get(Calendar.HOUR);
					String ampm = cr.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM";
					
					HORA=format.format(hour) +":"+ format.format(minute) +":"+ format.format(second) +" "+ ampm; 
					sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		clock.start();
	}*/
	
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
					try {
						
						//************************************* CAMBIO EL SKINS
						//JFrame.setDefaultLookAndFeelDecorated(true);
						
						SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.CremeCoffeeSkin");
						SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceSaturatedTheme");
						Menu menuprincipal = new Menu();
						menuprincipal.frame.setVisible(true);

						//SubstanceLookAndFeel.setCurrentWatermark(new SubstanceImageWatermark("D:\\bs2.png")); //MARCA DE AGUA
						//SubstanceLookAndFeel.setImageWatermarkOpacity(new Float(0.1));//valor 
				         
						/*try {
				            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//SKINS DE W7 W8
				            //com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
				            SwingUtilities.updateComponentTreeUI(menuprincipal.frame);
				            //l.frame.setVisible(true);
				        } catch (UnsupportedLookAndFeelException ex) {}
				          catch (ClassNotFoundException ex) {}
				          catch (InstantiationException ex) {}
				          catch (IllegalAccessException ex) {}*/
				         //*********************************** END CAMBIO EL SKINS	
				         
					/*} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
	}*/
	public Menu() { //constructor
		super();
		conexion = new ConexionDB();
		WindowMenu();
		crearMenu();
		crearTollbar();
		crearDesktop();
		crearStatusBar();
		convet();
		llenarDollarIgv();
		fechaPrimerDiaMen();
		//clock();
	}
	public void WindowMenu(){
		//********************** CREO VENTANA MDI PRINCIPAL
		frame = new JFrame("SGI - SOFTLE-HOTEL V 2.01. 2016 : "+ VentanaLogin.RAZON_SOCIAL_HOTEL +"   R.U.C.: "+ VentanaLogin.RUC_HOTEL +"");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/modelo/Images/control32.png")));
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));	
		frame.setBounds(400, 600, l.getAncho(), l.getAlto());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	public void convet () {
	    simbolo.setDecimalSeparator('.');
	    simbolo.setGroupingSeparator(',');
	    formateadorCurrency = new DecimalFormat("###,##0.00",simbolo);
	}
	public void fechaPrimerDiaMen(){
		DecimalFormat format = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        int Año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH) + 1;
        //int dia = calendar.get(Calendar.DAY_OF_MONTH);
        
        String desde=format.format(Año) + "-"+ format.format(mes) +"-"+ format.format(01);
		DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fechaPrimerDiaMes=formatoFecha.parse(desde);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void llenarDollarIgv(){
		//conexion = new ConexionDB();
		try {
	        String consult="Select * from VARIABLES";
			Statement st = conexion.gConnection().createStatement();
			ResultSet r=st.executeQuery(consult);
	        if(r.next()==true) {
	        	//IGV=r.getFloat("igv");
	        	if (r.getFloat("igv")==10.00) {
	        		IGV= (float)1.10;
	        	}
	        	if (r.getFloat("igv")==11.00) {
	        		IGV= (float)1.11;
	        	}
	        	if (r.getFloat("igv")==12.00) {
	        		IGV= (float)1.12;
	        	}
	        	if (r.getFloat("igv")==13.00) {
	        		IGV= (float)1.13;
	        	}
	        	if (r.getFloat("igv")==14.00) {
	        		IGV= (float)1.14;
	        	}
	        	if (r.getFloat("igv")==15.00) {
	        		IGV= (float)1.15;
	        	}
	        	if (r.getFloat("igv")==16.00) {
	        		IGV= (float)1.16;
	        	}
	        	if (r.getFloat("igv")==17.00) {
	        		IGV= (float)1.17;
	        	}
	        	if (r.getFloat("igv")==18.00) {
	        		IGV= (float)1.18;
	        	}
	        	if (r.getFloat("igv")==19.00) {
	        		IGV= (float)1.19;
	        	}
	        	DOLARC=r.getFloat("DolarC");
	        	DOLARV=r.getFloat("DolarV");
	        }
	        r.close();
			st.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		//conexion.DesconectarDB();
	}
	public void crearDesktop() {
		// El JFrame con el JDesktopPane
		Desktop = new JDesktopPane();
		Desktop.setBackground(new Color(244, 164, 96));//LIGHT_GRAY//119, 136, 153
		frame.getContentPane().add(Desktop, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		alto = Desktop.getSize().height;
		  
		//Fondo fondo = new Fondo();
		//Desktop.add(fondo);
	}
	public void crearStatusBar(){
		statusBar = new JStatusBar();
        JLabel leftLabel = new JLabel(VentanaLogin.RAZON_SOCIAL_HOTEL +"  R.U.C.: "+ VentanaLogin.RUC_HOTEL +"  TEL: "+ VentanaLogin.TELEFONO_HOTEL);
        statusBar.setLeftComponent(leftLabel);
 
		JLabel lbl0 = new JLabel("TURNO: "+ VentanaLogin.DESCRIPCION_TURNO.trim() +" | "+ VentanaLogin.ID_APETURA_CAJA +" | "+ VentanaLogin.COD_EMP_USER.trim());
		lbl0.setHorizontalAlignment(JLabel.CENTER);
		statusBar.addRightComponent(lbl0);
		
		JLabel lbl1 = new JLabel(VentanaLogin.TIP_USUARIO +": "+ VentanaLogin.NOM_USUARIO );
		lbl1.setHorizontalAlignment(JLabel.CENTER);
		statusBar.addRightComponent(lbl1);
        
        final JLabel dateLabel = new JLabel(dateLarga);
        dateLabel.setHorizontalAlignment(JLabel.CENTER);
        statusBar.addRightComponent(dateLabel);
		
        final JLabel timeLabell = new JLabel();
        timeLabell.setHorizontalAlignment(JLabel.CENTER);
        // ************************************ PARA LA HORA
		 Thread clock = new Thread(){
				public void run(){
					try {
						for(;;){
						DecimalFormat format = new DecimalFormat("00");
						Calendar cr = new GregorianCalendar();
						int second = cr.get(Calendar.SECOND);
						int minute = cr.get(Calendar.MINUTE);
						//int hour = cr.get(Calendar.HOUR);
						String ampm = cr.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM";
						
						
						int hour = cr.get(Calendar.HOUR_OF_DAY);
						HORA_24=format.format(hour) +":"+ format.format(minute) +":"+ format.format(second) +" "+ ampm; 
			            if ( hour >= 13  ) {
			                hour = cr.get(Calendar.HOUR);
			            }
	
						HORA=format.format(hour) +":"+ format.format(minute) +":"+ format.format(second) +" "+ ampm; 
						timeLabell.setText(HORA);
						sleep(1000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			clock.start();
			// ************************************ END PARA LA HORA
			statusBar.addRightComponent(timeLabell);
		    contentPane.add(statusBar, BorderLayout.SOUTH);
	}
	public void crearTollbar() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);

		toolBar = new JToolBar();
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		@SuppressWarnings("serial")
		JSeparator separator1 = new JSeparator(){
		    @Override
		    public Dimension getMaximumSize(){
		        return new Dimension(5, 25);
		    }
		};
		separator1.setOrientation(JSeparator.VERTICAL);
		@SuppressWarnings("serial")
		JSeparator separator2 = new JSeparator(){
		    @Override
		    public Dimension getMaximumSize(){
		        return new Dimension(5, 25);
		    }
		};
		separator2.setOrientation(JSeparator.VERTICAL);
		@SuppressWarnings("serial")
		JSeparator separator3 = new JSeparator(){
		    @Override
		    public Dimension getMaximumSize(){
		        return new Dimension(5, 25);
		    }
		};
		separator3.setOrientation(JSeparator.VERTICAL);
		@SuppressWarnings("serial")
		JSeparator separator4 = new JSeparator(){
		    @Override
		    public Dimension getMaximumSize(){
		        return new Dimension(5, 25);
		    }
		};
		separator4.setOrientation(JSeparator.VERTICAL);
		
		@SuppressWarnings("serial")
		JSeparator separator5 = new JSeparator(){
		    @Override
		    public Dimension getMaximumSize(){
		        return new Dimension(5, 25);
		    }
		};
		separator5.setOrientation(JSeparator.VERTICAL);
		
		@SuppressWarnings("serial")
		JSeparator separator6 = new JSeparator(){
		    @Override
		    public Dimension getMaximumSize(){
		        return new Dimension(5, 25);
		    }
		};
		separator6.setOrientation(JSeparator.VERTICAL);
		
		@SuppressWarnings("serial")
		JSeparator separator7 = new JSeparator(){
		    @Override
		    public Dimension getMaximumSize(){
		        return new Dimension(5, 25);
		    }
		};
		separator7.setOrientation(JSeparator.VERTICAL);
		
		@SuppressWarnings("serial")
		JSeparator separator8 = new JSeparator(){
		    @Override
		    public Dimension getMaximumSize(){
		        return new Dimension(5, 25);
		    }
		};
		separator8.setOrientation(JSeparator.VERTICAL);
		
		@SuppressWarnings("serial")
		JSeparator separator9 = new JSeparator(){
		    @Override
		    public Dimension getMaximumSize(){
		        return new Dimension(5, 25);
		    }
		};
		separator9.setOrientation(JSeparator.VERTICAL);
		// CREO BUTTON TOOLBAR
		
		//***************************** LLAMO VENTANA APERTURA TURNO
		btnAbrirTurno = new JButton("");
		btnAbrirTurno.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/clock_play32.png")));
		btnAbrirTurno.setToolTipText("Abrir turno");
		btnAbrirTurno.setBorderPainted(false);
		toolBar.add(btnAbrirTurno);
		btnAbrirTurno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 VentanaAperturaCaja v = new VentanaAperturaCaja();
				 v.frame.setVisible(true);
			}
		});

		toolBar.add(separator1);
        //toolBar.addSeparator(); //SEPARADOR PEQUE�O
        
		//***************************** LLAMO VENTANA VITRINA COMPRAS
		btnAltComp = new JButton("");
		btnAltComp.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/compra32.png")));
		btnAltComp.setToolTipText("Registrar compras");
		btnAltComp.setBorderPainted(false);
		toolBar.add(btnAltComp);
		btnAltComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaVitrinaCompra v = new VentanaVitrinaCompra();
      			v.frame.toFront();
      			v.frame.show();
			}
		});
		
		//***************************** LLAMO VENTANA VITRINA VENTAS
		btnAlVent = new JButton("");
		btnAlVent.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/venta32.png")));
		btnAlVent.setToolTipText("Venta directa");
		btnAlVent.setBorderPainted(false);
		toolBar.add(btnAlVent);
		btnAlVent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaVitrinaVenta v = new VentanaVitrinaVenta();
	      		//if (VentanaVitrinaVenta.CONTAR_VENTANA_VENTA==1) {
	      			v.frame.toFront();
	      			v.frame.show();
	      		//}
			}
		});
		//toolBar.addSeparator(); //SEPARADOR PEQUE�O
		toolBar.add(separator2);
		
		//***************************** LLAMO VENTANA SEARCH HABITACION
		btnAltHab = new JButton("");
		btnAltHab.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Hab32.png")));
		btnAltHab.setToolTipText("Gesti�n de habitaciones");
		btnAltHab.setBorderPainted(false);
		toolBar.add(btnAltHab);
		btnAltHab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaHabitacion v = new VentanaHabitacion();
	            VentanaHabitacion.frame.show();
	            v.textBus.requestFocus(true);
			}
		});
		
		//***************************** LLAMO VENTANA TARIFA
		btnTar = new JButton("");
		btnTar.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Dollar_3D_Green.png")));
		btnTar.setToolTipText("Gesti�n de tarifas");
		btnTar.setBorderPainted(false);
		toolBar.add(btnTar);
		btnTar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaTarifa ventana = new VentanaTarifa();
				ventana.frame.show();
				ventana.cbTipo.requestFocus();
			}
		});
		toolBar.add(separator3);
		
		//***************************** LLAMO VENTANA CLIENTES
		btnAltCli = new JButton("");
		btnAltCli.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/cliente32.png")));
		btnAltCli.setToolTipText("Alta de h\u00FAespedes");
		btnAltCli.setBorderPainted(false);
		toolBar.add(btnAltCli);
		btnAltCli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaCliente ventana = new VentanaCliente();
              	ventana.frame.setVisible(true);
              	VentanaCliente.FILTRO_CLI="VM";
              	ventana.textBus.requestFocus(true);
			}
		});
		toolBar.add(separator4);
		
		//***************************** LLAMO VENTANA CONTROL
		btnHotel= new JButton("");
		btnHotel.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Control-hotel32.png")));
		btnHotel.setToolTipText("Control de habitaciones - Hotel");
		btnHotel.setBorderPainted(false);
		toolBar.add(btnHotel);
		btnHotel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaControlHotel ventana = new VentanaControlHotel();
	            VentanaControlHotel.frame.show();
	            ventana.btnPag.setEnabled(true);
			}
		});
		toolBar.add(separator5);
		
		//***************************** LLAMO VENTANA BALANCE CAJA
		btnCaj = new JButton("");
		btnCaj.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/cajaMov32.png")));
		btnCaj.setToolTipText("Movimientos de caja chica");
		btnCaj.setBorderPainted(false);
		toolBar.add(btnCaj);
		btnCaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaCajaBalance v = new VentanaCajaBalance();
      			v.frame.toFront();
      			v.frame.show();
			}
		});
		toolBar.add(separator6);
		
		//***************************** LLAMO VENTANA HISTORIAL FACTURACION
		btnHisCta= new JButton("");
		btnHisCta.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Sites32.png")));
		btnHisCta.setToolTipText("Historial de ctas");
		btnHisCta.setBorderPainted(false);
		toolBar.add(btnHisCta);
		btnHisCta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RptCta ventana = new RptCta();
              	ventana.frame.setVisible(true);
              	RptCta.textNom.requestFocus(true);
              	RptCta.textNom.selectAll();
			}
		});
		
		//***************************** LLAMO VENTANA HISTORIAL CTAS
		btnHisVen = new JButton("");
		btnHisVen.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Folder32.png")));
		btnHisVen.setToolTipText("Historial de facturaci�n por hospedaje");
		btnHisVen.setBorderPainted(false);
		toolBar.add(btnHisVen);
		btnHisVen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RptFacturacion ventana = new RptFacturacion();
				ventana.cbBusIni.setSelectedItem("ALOJAMIENTO");
				ventana.cbBusIni.setEnabled(false);
              	ventana.frame.setVisible(true);
              	RptFacturacion.textNom.requestFocus(true);
              	RptFacturacion.textNom.selectAll();
			}
		});
		toolBar.add(separator7);
		
		//***************************** LLAMO VENTANA PERSONAL
		btnEmp = new JButton("");
		btnEmp.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/personal32.png")));
		btnEmp.setToolTipText("Alta de empleados");
		btnEmp.setBorderPainted(false);
		toolBar.add(btnEmp);
		btnEmp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaEmpleado v = new VentanaEmpleado();
				VentanaEmpleado.frame.show();
				v.textBus.requestFocus(true);
			}
		});
		toolBar.add(separator8);
		
		//***************************** LLAMO VENTANA CIERRE TURNO
		btnCerrTurno = new JButton("");
		btnCerrTurno.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/clock_stop32.png")));
		btnCerrTurno.setToolTipText("Cerrar turno");
		btnCerrTurno.setBorderPainted(false);
		toolBar.add(btnCerrTurno);
		btnCerrTurno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 VentanaCierreCaja v = new VentanaCierreCaja();
				 v.frame.setVisible(true);
				 v.textMontoSol.requestFocus();
				 v.textMontoSol.selectAll();
			}
		});
		toolBar.add(separator9);
		
		//***************************** LLAMO VENTANA CIERRO SECION LOGIN
		btnSec = new JButton("");
		btnSec.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/lock32.png")));
		btnSec.setToolTipText("Cerrar Seci�n");
		btnSec.setBorderPainted(false);
		toolBar.add(btnSec);
		btnSec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int respuesta = JOptionPane.showConfirmDialog (null, "� Desea cerrar sesi�n ?", Menu.SOFTLE_HOTEL,		
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (respuesta == JOptionPane.YES_OPTION) {
					VentanaLogin ventana = new VentanaLogin() {};
					ventana.opcacityLogin();
					frame.dispose();
				}else if (respuesta == JOptionPane.NO_OPTION) {}
			}
		});
		
		//***************************** SALIR SYSTEM
		btnCerr = new JButton("");
		btnCerr.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/exit-sis32.png")));
		btnCerr.setToolTipText("Cerrar aplicaci�n");
		btnCerr.setBorderPainted(false);
		toolBar.add(btnCerr);
		btnCerr.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			int respuesta = JOptionPane.showConfirmDialog (null, "� Desea cerrar el sistema ?", Menu.SOFTLE_HOTEL,		
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (respuesta == JOptionPane.YES_OPTION) {
				System.exit(0);
			}else if (respuesta == JOptionPane.NO_OPTION) {}
		}
		});
		
		
		// FONTS TOOLBAR
		btnAltCli.setFont(fontIToolbar);
		
		if (VentanaLogin.TIP_USUARIO.equals("RECEPCIONISTA")||VentanaLogin.TIP_USUARIO.equals("CAJERO")) {
			separator2.setVisible(false);
			separator7.setVisible(false);
			btnAltComp.setVisible(false);
			btnAltHab.setVisible(false);
			btnTar.setVisible(false);
			btnEmp.setVisible(false);
		}
		if (VentanaLogin.TIP_USUARIO.equals("INVITADO")) {
			btnAbrirTurno.setVisible(false);
			btnAltComp.setVisible(false);
			btnAlVent.setVisible(false);
			btnAltHab.setVisible(false);
			btnTar.setVisible(false);
			btnAltCli.setVisible(false);
			btnHotel.setVisible(false);
			btnCaj.setVisible(false);
			btnHisCta.setVisible(false);
			btnHisVen.setVisible(false);
			btnEmp.setVisible(false);
			btnCerrTurno.setVisible(false);
		}
	}
	public void crearMenu() {
		menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		menubar.setInheritsPopupMenu(true);
		// apariencia barra menu
		//menubar.setOpaque(false);
		
		// ARCHIVO
		menu1 = new JMenu("Archivo");
		menubar.add(menu1);
		

			JMenuItem itemImp = new JMenuItem("Configurar impresi�n");
			itemImp.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/print.png")));
			JMenuItem itemSec = new JMenuItem("Cerrar seci�n");
			itemSec.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/lock.png")));
			JMenuItem itemSal = new JMenuItem("Salir");
			itemSal.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/exit-sis.png")));

			menu1.add(itemImp);
			menu1.addSeparator();
			menu1.add(itemSec);
			menu1.add(itemSal);
	
		// ALTAS PRINCIPALES	
		menu2 = new JMenu("Altas Principales");
		menubar.add(menu2);
		
			JMenuItem itemAltCli = new JMenuItem("Alta de Hu\u00E9spedes");
			itemAltCli.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/cliente.png")));
			JMenuItem itemAltPro = new JMenuItem("Alta de Proveedor");
			itemAltPro.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/menu-proveedor.png")));
			JMenuItem itemAltPer = new JMenuItem("Alta de Personal");
			itemAltPer.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/personal.png")));
				//JMenuItem subitemAltPers = new JMenuItem("Alta de Personal");
				//JMenuItem subitemAltPro = new JMenuItem("Crear Profesi�n");
				//JMenuItem subitemAltCarg= new JMenuItem("Crear cargo");
				//itemAltPer.add(subitemAltPers);
				//itemAltPer.addSeparator();
				//itemAltPer.add(subitemAltPro);
				//itemAltPer.add(subitemAltCarg);
			
			JMenu itemAltGesHab= new JMenu("Alta y Gesti�n de habitaci�n...");
			itemAltGesHab.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Home.png")));
			
			JMenuItem itemAltHabImb = new JMenuItem("Gesti�n de habitaci�n");
			itemAltHabImb.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Hab.png")));
			JMenuItem itemAltHab= new JMenuItem("Alta de Habitaci�n");
			itemAltHab.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/homet.png")));
			JMenuItem itemAltTip = new JMenuItem("Alta tipo Habitaci�n");
			itemAltTip.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/homeAzul.png")));
			JMenuItem itemAltPlan= new JMenuItem("Gesti�n de Planes");
			itemAltPlan.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Add.png")));
			JMenuItem itemAltTar = new JMenuItem("Gesti�n de Tarifas");
			itemAltTar.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Dollar_Green.png")));
			JMenuItem itemAltTipTar = new JMenuItem("Alta tipo Tarifa");
			itemAltTipTar.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/menu-tarifa.png")));
			JMenuItem itemAltImp = new JMenuItem("Acondicionar habitaci�n ('inventarios')");
			itemAltImp.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/book_picture.png")));
			
			JMenu itemAltInv = new JMenu("Inventarios (Hotel)");
			itemAltInv.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/folder-search.png")));

			JMenuItem subitemConInv= new JMenuItem("Inventarios de hotel");
			subitemConInv.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/folder-search.png")));
			JMenuItem subitemInv= new JMenuItem("Alta de art�culos");
			subitemInv.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/folder-document.png")));
			JMenuItem subitemCat= new JMenuItem("Alta de categorias");
			subitemCat.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Category.png")));
			
			JMenuItem subitemSerSearch= new JMenuItem("Servicios search...");
			subitemSerSearch.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/servicio.png")));
			JMenuItem subitemSer= new JMenuItem("Alta de servicios");
			subitemSer.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/servicios.png")));
			

			menu2.add(itemAltCli);
			menu2.addSeparator();
			menu2.add(itemAltPro);
			if (VentanaLogin.TIP_USUARIO.equals("ADMINISTRADOR")||VentanaLogin.TIP_USUARIO.equals("PROGRAMADOR")) {
				menu2.addSeparator();
			}
			menu2.add(itemAltPer);
			
			if (VentanaLogin.TIP_USUARIO.equals("ADMINISTRADOR")||VentanaLogin.TIP_USUARIO.equals("PROGRAMADOR")) {
				menu2.addSeparator();
			}
			
			menu2.add(itemAltGesHab);
			itemAltGesHab.add(itemAltHabImb);
			itemAltGesHab.addSeparator();
			itemAltGesHab.add(itemAltHab);
			itemAltGesHab.add(itemAltTip);
			itemAltGesHab.addSeparator();
			itemAltGesHab.add(itemAltPlan);
			itemAltGesHab.add(itemAltTar);
			itemAltGesHab.add(itemAltTipTar);
			itemAltGesHab.addSeparator();
			itemAltGesHab.add(itemAltImp);
			
			if (VentanaLogin.TIP_USUARIO.equals("ADMINISTRADOR")||VentanaLogin.TIP_USUARIO.equals("PROGRAMADOR")) {
				menu2.addSeparator();
			}
			
			menu2.add(itemAltInv);
			itemAltInv.add(subitemConInv);
			itemAltInv.addSeparator();
			itemAltInv.add(subitemInv);
			itemAltInv.add(subitemCat);
			
			menu2.addSeparator();
			menu2.add(subitemSerSearch);
			menu2.add(subitemSer);
			
		// CONTROL HOTEL
		menu3 = new JMenu("Control - Hotel");//Control Hotel
		menubar.add(menu3);
		
			JMenuItem itemConHot = new JMenuItem("Mostrador");
			itemConHot.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Control-hotel.png")));
			JMenuItem itemFact = new JMenuItem("Historial de Facturaci�n...");
			itemFact.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Histori1.png")));
			
			menu3.add(itemConHot);
			menu3.addSeparator();
			menu3.add(itemFact);

		// TURNO
		menu4 = new JMenu("Turno");//Tesoreria
		menubar.add(menu4);
			JMenuItem itemEntSal = new JMenuItem("Caja chica");//Entradas y/o Salidas de caja
			
			JMenuItem itemBalCaj = new JMenuItem("Movimientos de Caja chica");
			itemBalCaj.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/cajaMov.png")));
			JMenuItem itemHisCta = new JMenuItem("Historial de cuentas");
			itemHisCta.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Sites16.png")));
			JMenuItem itemApeCaj = new JMenuItem("Abrir  turno");
			itemApeCaj.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/time_add16.png")));
			JMenuItem itemCieCaj = new JMenuItem("Cerrar turno");
			itemCieCaj.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/time_delete16.png")));
			JMenuItem itemMovGas = new JMenuItem("Gastos diversos");
			itemMovGas.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/cajasalida.png")));
			//menu4.add(itemEntSal);
			//menu4.addSeparator();
			
			menu4.add(itemBalCaj);
			menu4.addSeparator();
			menu4.add(itemHisCta);
			menu4.addSeparator();
			menu4.add(itemApeCaj);
			menu4.add(itemCieCaj);
			
		// VITRINA
		menu5 = new JMenu("Vitrina");
		menubar.add(menu5);
			JMenuItem itemInvVit = new JMenuItem("Inventarios de vitrina");
			itemInvVit.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/folder-search.png")));
			JMenuItem itemVen = new JMenuItem("Venta directa");
			itemVen.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/venta16.png")));
			JMenuItem itemCom = new JMenuItem("Nota de ingreso (Compras)");
			itemCom.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/compra16.png")));
			JMenuItem itemRep = new JMenuItem("Historial de facturacion por compra y venta");
			itemRep.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Histori.png")));
			
			menu5.add(itemInvVit);
			menu5.addSeparator();
			menu5.add(itemVen);
			menu5.add(itemCom);
			menu5.addSeparator();
			menu5.add(itemRep);
	
		// RESTAURANT
		menuRes = new JMenu("Bar - Restaurant");
		menubar.add(menuRes);
			/*JMenuItem itemRes1 = new JMenuItem("********");
			itemInvVit.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/folder-search.png")));
			JMenuItem itemRes2 = new JMenuItem("********");
			itemVen.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/venta16.png")));
			JMenuItem itemRes3 = new JMenuItem("********");
			itemCom.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/compra16.png")));
			JMenuItem itemRes4 = new JMenuItem("********");
			itemRep.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Histori.png")));
			
			menuRes.add(itemRes1);
			menuRes.addSeparator();
			menuRes.add(itemRes2);
			menuRes.add(itemRes3);
			menuRes.addSeparator();
			menuRes.add(itemRes4);*/

		// ADMINISTRAR
		menu6 = new JMenu("Configuraci�n");//Administraci�n
		menubar.add(menu6);
			
			JMenuItem itemTipUsu = new JMenuItem("grupos de usuario");
			itemTipUsu.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/user.png")));
			JMenuItem itemCueUsu = new JMenuItem("Crear cuenta de usuario");
			itemCueUsu.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/user.png")));
			JMenuItem itemAltTur= new JMenuItem("Crear turno");
			itemAltTur.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/menu-turno.png")));
			JMenuItem itemEmp = new JMenuItem("Empresa");
			itemEmp.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/empresa.png")));
			JMenuItem itemDoc = new JMenuItem("Documentos");
			itemDoc.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Script.png")));
			JMenuItem itemCor = new JMenuItem("Correlativos");
			itemCor.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Diagram.png")));
			JMenuItem itemIgv = new JMenuItem("Actualizat I.G.V.");
			itemIgv.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/percent.png")));
			JMenuItem itemDol = new JMenuItem("Actualizar Cotizaci�n Dolar");
			itemDol.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/dolarc.png")));
			
			JMenuItem itemBackup = new JMenuItem("Respaldo de BD");
			itemBackup.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/database.png")));
			
			JMenuItem itemRestaurar = new JMenuItem("Restaurar BD");
			itemRestaurar.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/event.png")));
			
			itemTipUsu.setEnabled(false);
			menu6.add(itemTipUsu);
			menu6.add(itemCueUsu);
			menu6.addSeparator();
			menu6.add(itemAltTur);
			if (VentanaLogin.TIP_USUARIO.equals("ADMINISTRADOR")||VentanaLogin.TIP_USUARIO.equals("PROGRAMADOR")) {
				menu6.addSeparator();
			}
			menu6.add(itemEmp);
			menu6.add(itemDoc);
			menu6.add(itemCor);
			if (VentanaLogin.TIP_USUARIO.equals("ADMINISTRADOR")||VentanaLogin.TIP_USUARIO.equals("PROGRAMADOR")) {
				menu6.addSeparator();
			}
			menu6.add(itemIgv);
			menu6.add(itemDol);
			menu6.addSeparator();
			menu6.add(itemBackup);
			menu6.add(itemRestaurar);
			
			
		// REPORTES
		menu7 = new JMenu("Informes y estad�sticas");
		menubar.add(menu7);
			
			JMenu itemRCHECK_IN = new JMenu("Reporte check-in - registro");
			itemRCHECK_IN.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/contact_book.png")));
				JMenuItem itemRA1 = new JMenuItem("Informe por alojamiento (diario)");
				JMenuItem itemRA2 = new JMenuItem("Informe por alojamiento (mensual)");
				JMenuItem itemRA3 = new JMenuItem("Imforme por reservaciones");

			JMenu itemRCHECK_OUT = new JMenu("Reporte check-out - facturaci�n");
			itemRCHECK_OUT.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/contact_book.png")));
				JMenuItem itemRO1 = new JMenuItem("Informe por facturaci�n diaria");
				JMenuItem itemRO2 = new JMenuItem("Imforme por facturaci�n mensual");
				JMenuItem itemRO3 = new JMenuItem("Imforme de documentos emitidos (diario)");
				JMenuItem itemRO4 = new JMenuItem("Imforme de documentos emitidos (mensual)");
				JMenuItem itemRO5 = new JMenuItem("Pre dise�ador de factura");
				JMenuItem itemRO6 = new JMenuItem("Pre dise�ador de boleta");
				JMenuItem itemRO7 = new JMenuItem("Pre dise�ador de ticket");
				
				
			JMenu itemRTesoreria= new JMenu("Reporte de Turnos");
			itemRTesoreria.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/contact_book.png")));
				JMenuItem itemRT1 = new JMenuItem("Informe de caja diaria por turno");
				JMenuItem itemRT2 = new JMenuItem("Imforme de caja mensual por turno");
				JMenuItem itemRT3 = new JMenuItem("Imforme de movimientos de caja chica");
				JMenuItem itemRT4 = new JMenuItem("Imforme de recuento de caja");
				JMenuItem itemRT5 = new JMenuItem("Imforme de apertura de turno");
				JMenuItem itemRT6 = new JMenuItem("Imforme de cierre de turno");
				
			JMenu itemRHabitacion = new JMenu("Reporte de Habitaciones");
			itemRHabitacion.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/contact_book.png")));
				JMenuItem itemRH1 = new JMenuItem("Listado de habitaciones");
				JMenuItem itemRH2 = new JMenuItem("Listado de tipos-habitaci�n");
				JMenuItem itemRH3 = new JMenuItem("Listado de Tarifas");
				JMenuItem itemRH4 = new JMenuItem("Listado de Tipo - Tarifa");
				JMenuItem itemRH5 = new JMenuItem("Listado de habitaciones disponibles");
				JMenuItem itemRH6 = new JMenuItem("Listado de habitaciones ocupadas");
				JMenuItem itemRH7 = new JMenuItem("Listado de habitaciones reservadas");
				JMenuItem itemRH8 = new JMenuItem("Listado de habitaciones en limpieza");
				JMenuItem itemRH9 = new JMenuItem("Listado de habitaciones en mantenimiento");
				JMenuItem itemRH10 = new JMenuItem("Listado de inventarios por habitaciones");
				
			JMenu itemRVitrina = new JMenu("Reporte de Vitrina");
			itemRVitrina.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/contact_book.png")));
				JMenuItem itemRV1 = new JMenuItem("Listado de Inventarios");
				JMenuItem itemRV2 = new JMenuItem("Listado de Ventas diarias");
				JMenuItem itemRV3 = new JMenuItem("Listado de ventas mensuales");
				

			JMenu itemRCliente = new JMenu("Reporte de Hu\u00E9spedes");
			itemRCliente.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/contact_book.png")));
				JMenuItem itemRC0 = new JMenuItem("Listado general de hu\u00E9spedes");
				JMenuItem itemRC1 = new JMenuItem("Listado de hu\u00E9sped - con descuento");
				JMenuItem itemRC2 = new JMenuItem("Listado de hu\u00E9sped- cts");
				JMenuItem itemRC3 = new JMenuItem("Listado de hu\u00E9sped- alojados");
				
			JMenuItem itemREmpleado = new JMenuItem("Listado de empleados");
			itemREmpleado.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/contact_book.png")));
			JMenuItem itemRProveedor = new JMenuItem("Listado de proveedores");
			itemRProveedor.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/contact_book.png")));
		
			menu7.add(itemRCHECK_IN);
			itemRCHECK_IN.add(itemRA1);
			itemRCHECK_IN.add(itemRA2);
			itemRCHECK_IN.addSeparator();
			itemRCHECK_IN.add(itemRA3);
			
			menu7.addSeparator();
			menu7.add(itemRCHECK_OUT);
			itemRCHECK_OUT.add(itemRO1);
			itemRCHECK_OUT.add(itemRO2);
			itemRCHECK_OUT.addSeparator();
			itemRCHECK_OUT.add(itemRO3);
			itemRCHECK_OUT.add(itemRO4);
			itemRCHECK_OUT.addSeparator();
			itemRCHECK_OUT.add(itemRO5);
			itemRCHECK_OUT.add(itemRO6);
			itemRCHECK_OUT.add(itemRO7);
			
			
			menu7.addSeparator();
			menu7.add(itemRTesoreria);
			itemRTesoreria.add(itemRT1);
			itemRTesoreria.add(itemRT2);
			itemRTesoreria.addSeparator();
			itemRTesoreria.add(itemRT3);
			itemRTesoreria.addSeparator();
			itemRTesoreria.add(itemRT4);
			itemRTesoreria.addSeparator();
			itemRTesoreria.add(itemRT5);
			itemRTesoreria.add(itemRT6);
			
			menu7.addSeparator();
			menu7.add(itemRHabitacion);
			itemRHabitacion.add(itemRH1);
			itemRHabitacion.add(itemRH2);
			itemRHabitacion.addSeparator();
			itemRHabitacion.add(itemRH3);
			itemRHabitacion.add(itemRH4);
			itemRHabitacion.addSeparator();
			itemRHabitacion.add(itemRH5);
			itemRHabitacion.add(itemRH6);
			itemRHabitacion.add(itemRH7);
			itemRHabitacion.add(itemRH8);
			itemRHabitacion.add(itemRH9);
			itemRHabitacion.addSeparator();
			itemRHabitacion.add(itemRH10);
			
			menu7.addSeparator();
			menu7.add(itemRVitrina);
			itemRVitrina.add(itemRV1);
			itemRVitrina.add(itemRV2);
			itemRVitrina.add(itemRV3);
			

			
			
			
			menu7.addSeparator();
			menu7.add(itemRCliente);
			itemRCliente.add(itemRC0);
			itemRCliente.addSeparator();
			itemRCliente.add(itemRC1);
			itemRCliente.add(itemRC2);
			itemRCliente.add(itemRC3);
			
			menu7.addSeparator();
			menu7.add(itemREmpleado);
			
			
			menu7.addSeparator();
			menu7.add(itemRProveedor);
			
		
		// HERRAMIENTAS
		menu8 = new JMenu("Herramientas");
		menubar.add(menu8);
		menu8.setVisible(false);
		
		
		// AYUDA
		menu9 = new JMenu("Ayuda");
		menubar.add(menu9);
			JMenuItem itemAyuda = new JMenuItem("Ayuda...");
			itemAyuda.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/consulting.png")));
			JMenuItem itemAcerca = new JMenuItem("Acerca...");
			itemAcerca.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/foco.png")));
			JMenuItem itemActivar= new JMenuItem("Activaci�n de producto");
			itemActivar.setIcon(new ImageIcon(Menu.class.getResource("/modelo/Images/Keyd.png")));
			
			menu9.add(itemAyuda);
			menu9.addSeparator();
			menu9.add(itemAcerca);
			menu9.add(itemActivar);
		
			
			
		// TECLAS ACCESO RAPIDO
		itemAltCli.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,KeyEvent.VK_CONTROL));
		itemSec.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		itemSal.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		itemAltHabImb.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,KeyEvent.CTRL_DOWN_MASK));
		itemAltHab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,KeyEvent.CTRL_DOWN_MASK));
		itemAltTar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,KeyEvent.CTRL_DOWN_MASK));
		itemAltPro.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.CTRL_DOWN_MASK));
		itemAltPer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_DOWN_MASK));
		subitemSerSearch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_DOWN_MASK));
		itemConHot.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		
		itemBalCaj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
		itemApeCaj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_DOWN_MASK));
		itemCieCaj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,KeyEvent.CTRL_DOWN_MASK));
		itemMovGas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,KeyEvent.CTRL_DOWN_MASK));
		
		itemInvVit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,KeyEvent.CTRL_DOWN_MASK));
		itemVen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_DOWN_MASK));
		
		
		
		itemIgv.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,KeyEvent.CTRL_MASK));
		itemDol.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,KeyEvent.CTRL_DOWN_MASK));
		// FONTS MENUS
		menu1.setFont(fontMenu);
		menu2.setFont(fontMenu);
		menu3.setFont(fontMenu);
		menu4.setFont(fontMenu);
		menu5.setFont(fontMenu);
		menu6.setFont(fontMenu);
		menu7.setFont(fontMenu);
		menu8.setFont(fontMenu);
		menu9.setFont(fontMenu);
		// FONTS ITEMS
		itemAltInv.setFont(fontItem);
		subitemConInv.setFont(fontItem);
		subitemInv.setFont(fontItem);
		subitemCat.setFont(fontItem);
		subitemSerSearch.setFont(fontItem);
		subitemSer.setFont(fontItem);
		itemImp.setFont(fontItem);
		itemSec.setFont(fontItem);
		itemSal.setFont(fontItem);
		itemAltCli.setFont(fontItem);
		itemAltPro.setFont(fontItem);
		itemAltPer.setFont(fontItem);
		//subitemAltPers.setFont(fontItem);
		//subitemAltPro.setFont(fontItem);
		itemAltTur.setFont(fontItem);
		//subitemAltCarg.setFont(fontItem);
		
		itemAltGesHab.setFont(fontItem);
		itemAltHabImb.setFont(fontItem);
		itemAltPlan.setFont(fontItem);
		itemAltTar.setFont(fontItem);
		itemAltTipTar.setFont(fontItem);
		itemAltHab.setFont(fontItem);
		itemAltTip.setFont(fontItem);
		itemAltImp.setFont(fontItem);
		
		itemConHot.setFont(fontItem);
		itemFact.setFont(fontItem);
		itemHisCta.setFont(fontItem);
		
		itemApeCaj.setFont(fontItem);
		itemEntSal.setFont(fontItem);
		itemBalCaj.setFont(fontItem); 
		itemCieCaj.setFont(fontItem);
		
		itemMovGas.setFont(fontItem);
		
		itemInvVit.setFont(fontItem);
		itemVen.setFont(fontItem);
		itemCom.setFont(fontItem);
		itemRep.setFont(fontItem);
		
		itemTipUsu.setFont(fontItem);
		itemCueUsu.setFont(fontItem);
		itemEmp.setFont(fontItem);
		itemDoc.setFont(fontItem);
		itemCor.setFont(fontItem);
		itemIgv.setFont(fontItem);
		itemDol.setFont(fontItem);
		itemBackup.setFont(fontItem);
		itemRestaurar.setFont(fontItem);
		
		itemRCHECK_IN.setFont(fontItem);
		itemRA1.setFont(fontItem);
		itemRA2.setFont(fontItem);
		itemRA3.setFont(fontItem);
		itemRCHECK_OUT.setFont(fontItem);
		itemRO1.setFont(fontItem);
		itemRO2.setFont(fontItem);
		itemRO3.setFont(fontItem);
		itemRO4.setFont(fontItem);
		itemRO5.setFont(fontItem);
		itemRO6.setFont(fontItem);
		itemRO7.setFont(fontItem);
		itemRTesoreria.setFont(fontItem);
		itemRT1.setFont(fontItem);
		itemRT2.setFont(fontItem);
		itemRT3.setFont(fontItem);
		itemRT4.setFont(fontItem);
		itemRT5.setFont(fontItem);
		itemRT6.setFont(fontItem);
		itemRHabitacion.setFont(fontItem);
		itemRH1.setFont(fontItem);
		itemRH2.setFont(fontItem);
		itemRH3.setFont(fontItem);
		itemRH4.setFont(fontItem);
		itemRH5.setFont(fontItem);
		itemRH6.setFont(fontItem);
		itemRH7.setFont(fontItem);
		itemRH8.setFont(fontItem);
		itemRH9.setFont(fontItem);
		itemRH10.setFont(fontItem);
		itemRVitrina.setFont(fontItem);
		itemRV1.setFont(fontItem);
		itemRV2.setFont(fontItem);
		itemRV3.setFont(fontItem);
		itemRCliente.setFont(fontItem);
		
		itemRC0.setFont(fontItem);
		itemRC1.setFont(fontItem);
		itemRC2.setFont(fontItem);
		itemRC3.setFont(fontItem);
		itemREmpleado.setFont(fontItem);
		itemRProveedor.setFont(fontItem);
		
		itemAyuda.setFont(fontItem);
		itemAcerca.setFont(fontItem);
		itemActivar.setFont(fontItem);
		
		
		subitemConInv.addActionListener(this); // CONSULTA
		subitemInv.addActionListener(this); // INVENTARIO
		subitemCat.addActionListener(this); // CATEGORIA
		subitemSerSearch.addActionListener(this); // SERVICIO
		subitemSer.addActionListener(this); // SERVICIO AGREGAR
		itemImp.addActionListener(this); // IMPRESION
		itemSec.addActionListener(this); // SECION
		itemSal.addActionListener(this); // SALIR
		
		itemAltCli.addActionListener(this); // CLIENTE
		itemAltPro.addActionListener(this); // PROVEEDOR
		//subitemAltPers.addActionListener(this); // PERSONAL
		//subitemAltPro.addActionListener(this); // HORARIO
		itemAltTur.addActionListener(this); // TURNO
		//subitemAltCarg.addActionListener(this); // CARGO
		
		itemAltHabImb.addActionListener(this); // SEARCH HABITACION
		itemAltPlan.addActionListener(this); // PLANES
		itemAltTar.addActionListener(this); // TARIFAS
		itemAltTipTar.addActionListener(this); // TIPO TARIFA
		itemAltHab.addActionListener(this); // HABITACION
		itemAltTip.addActionListener(this); // TIPO HABITACION
		itemAltImp.addActionListener(this); // IMPLEMENTAR HABITACION

		itemConHot.addActionListener(this); // CONTROL HOTEL
		itemFact.addActionListener(this); // FACTURACION 
		itemHisCta.addActionListener(this); // HISTORIAL CTA 

		itemEntSal.addActionListener(this); // ENTRADAS SALIDAS DE CAJA
		itemBalCaj.addActionListener(this); // BALANCE DE CAJA
		
		itemApeCaj.addActionListener(this); // APERTURA DE CAJA
		itemCieCaj.addActionListener(this); // CIERRE DE CAJA
		itemMovGas.addActionListener(this); // MOVIMIENTOS DE GASTOS
		
		itemInvVit.addActionListener(this); // INVENTARIOS VITRINA
		itemVen.addActionListener(this); // VENTAS
		itemCom.addActionListener(this); // COMPRAS
		itemRep.addActionListener(this); // REPORTES
		
		itemTipUsu.addActionListener(this); // TIPO D EUSUARIO
		itemCueUsu.addActionListener(this); // CUENTA DE USUARIO
		itemEmp.addActionListener(this); // EMPRESA
		itemDoc.addActionListener(this); // DOCUMENTOS
		itemCor.addActionListener(this); // CORRELATIVOS
		itemIgv.addActionListener(this); // IGV
		itemDol.addActionListener(this); // DOLAR
		itemBackup.addActionListener(this); // BACKUP BD
		itemRestaurar.addActionListener(this); // RESTAURAR BD
		
		itemRH1.addActionListener(this);
		itemRH2.addActionListener(this);
		itemRH3.addActionListener(this);
		itemRH4.addActionListener(this);
		itemRH5.addActionListener(this);
		itemRH6.addActionListener(this);
		itemRH7.addActionListener(this);
		itemRH8.addActionListener(this);
		itemRH9.addActionListener(this);
		itemRH10.addActionListener(this);
		
		itemRC0.addActionListener(this);
		itemREmpleado.addActionListener(this);
		itemRProveedor.addActionListener(this);
		
		itemAyuda.addActionListener(this); // AYUDA
		itemAcerca.addActionListener(this); // ACERCA
		itemActivar.addActionListener(this); // ACTIVAR
		
		
		//=====================================================================================================
		//======================================== ARCHIVO ====================================================
		
		//***************************** LLAMO IMPRESORA
		itemImp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int respuesta = JOptionPane.showConfirmDialog (null, "� Desea configurar la impresora ?", Menu.SOFTLE_HOTEL,		
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (respuesta == JOptionPane.YES_OPTION) {
				}else if (respuesta == JOptionPane.NO_OPTION) {}
			}
		});
		
		//***************************** LLAMO VENTANA CIERRO SECION LOGIN
		itemSec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int respuesta = JOptionPane.showConfirmDialog (null, "� Desea cerrar sesi�n ?", Menu.SOFTLE_HOTEL,		
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (respuesta == JOptionPane.YES_OPTION) {
					VentanaLogin ventana = new VentanaLogin() {};
					ventana.opcacityLogin();
					frame.dispose();
				}else if (respuesta == JOptionPane.NO_OPTION) {}
			}
		});
		
		//***************************** SALIR SYSTEM
		itemSal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int respuesta = JOptionPane.showConfirmDialog (null, "� Desea cerrar la aplicaci�n ?", Menu.SOFTLE_HOTEL,		
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (respuesta == JOptionPane.YES_OPTION) {
					System.exit(0);
				}else if (respuesta == JOptionPane.NO_OPTION) {}
			}
		});
		
		
		//=====================================================================================================
		//======================================== PRINCIPALES ================================================
		
		//***************************** LLAMO VENTANA INVENTARIOS
		subitemConInv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaInventarios v = new VentanaInventarios();
	      		//if (VentanaInventarios.CONTAR_VENTANA_INVENTARIOS==1) {
	      			VentanaInventarios.frame.toFront();
	      			VentanaInventarios.frame.show();
	      		//}
	      		VentanaInventarios.frame.setTitle("HOTEL: Control de inventarios");
	      		
			    v.cbFamilia.removeAllItems();
			    v.cbFamilia.addItem("INV_HOTEL");
			    v.textBus.requestFocus(true);
			}
		});
		//***************************** LLAMO VENTANA INVENTARIOS AGREGAR
		subitemInv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaInventariosAgregar v = new VentanaInventariosAgregar();
      			v.frame.toFront();
      			v.frame.show();
	      		VentanaInventariosAgregar.cbFamilia.removeAllItems();
	      		VentanaInventariosAgregar.cbFamilia.addItem("INV_HOTEL");
	      		VentanaInventariosAgregar.cbTipo.requestFocus(true);
			}
		});
		//***************************** LLAMO VENTANA CATEGORIA
		subitemCat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 VentanaCategoria v = new VentanaCategoria();
				 v.frame.show();
				 VentanaCategoria.textDescripcion.requestFocus(true);
				 
				 VentanaCategoria.cbFamilia.removeAllItems();
				 VentanaCategoria.cbFamilia.addItem("INV_HOTEL");
			}
		});
		
		//***************************** LLAMO VENTANA SERVICIOS
		subitemSerSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaServicio v = new VentanaServicio();
				v.frame.show();
				v.textBus.requestFocus(true);
			}
		});
		
		//***************************** LLAMO VENTANA SERVICIOS AGREGAR
		subitemSer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaServiciosAgregar.MOD=0;// NO PERMITE MODIFICAR
				VentanaServiciosAgregar ven= new VentanaServiciosAgregar();
			    ven.frame.toFront();
			    ven.frame.setVisible(true);
			    VentanaServiciosAgregar.textDescripcion.requestFocus(true);
			}
		});
		
		//***************************** LLAMO VENTANA CLIENTES
		itemAltCli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaCliente ventana = new VentanaCliente();
              	ventana.frame.setVisible(true);
              	VentanaCliente.FILTRO_CLI="VM";
              	ventana.textBus.requestFocus(true);
			}
		});
		//***************************** LLAMO VENTANA PROVEEDOR
		itemAltPro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 VentanaProveedor v = new VentanaProveedor();
				 v.frame.show();
			}
		});
		
		//***************************** LLAMO VENTANA PERSONAL
		itemAltPer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaEmpleado v = new VentanaEmpleado();
				VentanaEmpleado.frame.show();
				v.textBus.requestFocus(true);
			}
		});
		
		//***************************** LLAMO VENTANA TURNOS
		itemAltTur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 VentanaTurno v = new VentanaTurno();
				 v.frame.setVisible(true);
				 VentanaTurno.textNom.requestFocus(true);
			}
		});

		
		//=====================================================================================================
		//======================================== GESTION HABITACIONES ========================================
		

		//***************************** LLAMO VENTANA SEARCH INVENTARIO HABITACION
		itemAltHabImb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaHabitacion v = new VentanaHabitacion();
	            VentanaHabitacion.frame.show();
	            v.textBus.requestFocus(true);
			}
		});
		//***************************** LLAMO VENTANA AGREGAR HABITACION
		itemAltHab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//VentanaHabitacionAgregar.MOD=1;// PERMITE MODIFICAR
				VentanaHabitacionAgregar ven= new VentanaHabitacionAgregar();
				ven.frame.toFront();
				ven.frame.setVisible(true);
				ven.llenarcbComboBox();
				ven.activarButton(false);
				ven.textFieldBuscar.setEnabled(false);
				ven.textField1.requestFocus(true);
			}
		});
		//***************************** LLAMO VENTANA TIPO HABITACION
		itemAltTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaTipoHabitacion ventana = new VentanaTipoHabitacion();
	            ventana.frame.show();
			}
		});
		//***************************** LLAMO VENTANA PLANES
		itemAltPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaPlan ventana = new VentanaPlan();
				VentanaPlan.frame.show();
				ventana.textBus.requestFocus();
			}
		});
		//***************************** LLAMO VENTANA TARIFA
		itemAltTar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaTarifa ventana = new VentanaTarifa();
				ventana.frame.show();
				ventana.cbTipo.requestFocus();
			}
		});
		//***************************** LLAMO VENTANA TIPO TARIFA
		itemAltTipTar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 VentanaTarifaTipo v = new VentanaTarifaTipo();
				 v.frame.setVisible(true);
			}
		});
		//***************************** LLAMO VENTANA IMPLEMENTAR HABITACION
		itemAltImp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 VentanaInventarioHabitacion v = new VentanaInventarioHabitacion();
				 v.frame.show();
			}
		});
		//=====================================================================================================
		//======================================== CONTROL HOTEL ==============================================
		
		//***************************** LLAMO VENTANA CONTROL
		itemConHot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaControlHotel ventana = new VentanaControlHotel();
	            VentanaControlHotel.frame.show();
	            ventana.btnAlt.setEnabled(false);
			}
		});
		
		//***************************** LLAMO VENTANA GENERAR DOCUMENTO
		/*itemFact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	    		VentanaGenerarDocumento v  = new VentanaGenerarDocumento();
	    		v.buttonSalir.setEnabled(true);
	      		if (VentanaGenerarDocumento.CONTAR_VENTANA_GENERAR_DOCUMENT==1) {
	      			VentanaGenerarDocumento.frame.toFront();
	      			VentanaGenerarDocumento.frame.show();
	      			}
	    	    if (VentanaGenerarDocumento.CONTAR_VENTANA_GENERAR_DOCUMENT>1) {
	    	    	frame.toBack();
	    	    	VentanaGenerarDocumento.frame.toFront();
	    	    	}
			}
		});*/
		//***************************** LLAMO VENTANA HISTORIAL FACTURACION
		itemFact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RptFacturacion ventana = new RptFacturacion();
				ventana.cbBusIni.setSelectedItem("ALOJAMIENTO");
				ventana.cbBusIni.setEnabled(false);
              	ventana.frame.setVisible(true);
              	RptFacturacion.textNom.requestFocus(true);
              	RptFacturacion.textNom.selectAll();
			}
		});
		
		//***************************** LLAMO VENTANA HISTORIAL CTAS
		itemHisCta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RptCta ventana = new RptCta();
              	ventana.frame.setVisible(true);
              	RptCta.textNom.requestFocus(true);
              	RptCta.textNom.selectAll();
			}
		});
		//=====================================================================================================
		//======================================== TURNO ==================================================

		//***************************** LLAMO VENTANA BALANCE CAJA
		itemBalCaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaCajaBalance v = new VentanaCajaBalance();
	      		//if (VentanaCajaBalance.CONTAR_VENTANA_BALANCE==1) {
	      			v.frame.toFront();
	      			v.frame.show();
	      		//}
			}
		});
		
		//***************************** LLAMO VENTANA APERTURA CAJA
		itemApeCaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 VentanaAperturaCaja v = new VentanaAperturaCaja();
				 v.frame.setVisible(true);
			}
		});
		//***************************** LLAMO VENTANA CIERRE CAJA
		itemCieCaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 VentanaCierreCaja v = new VentanaCierreCaja();
				 v.frame.setVisible(true);
				 v.textMontoSol.requestFocus();
				 v.textMontoSol.selectAll();
			}
		});
		//***************************** LLAMO VENTANA GASTOS
		itemMovGas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaGastos v = new VentanaGastos();
				v.frame.setVisible(true);
      			v.frame.toFront();
      			v.frame.setVisible(true);
			}
		});
		
		//=====================================================================================================
		//======================================== VITRINA ====================================================
		
		//***************************** LLAMO VENTANA VITRINA INVENTARIOS
		itemInvVit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaInventarios v = new VentanaInventarios();
      			VentanaInventarios.frame.setTitle("VITRINA: Control de Inventarios");
      			VentanaInventarios.frame.toFront();
      			VentanaInventarios.frame.show();
			    v.cbFamilia.removeAllItems();
	      		v.cbFamilia.addItem("VITRINA");
			    v.textBus.requestFocus(true);
			}
		});
		
		//***************************** LLAMO VENTANA VITRINA VENTAS
		itemVen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaVitrinaVenta v = new VentanaVitrinaVenta();
      			v.frame.toFront();
      			v.frame.show();
			}
		});
		
		//***************************** LLAMO VENTANA VITRINA COMPRAS
		itemCom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaVitrinaCompra v = new VentanaVitrinaCompra();
      			v.frame.toFront();
      			v.frame.show();
			}
		});
		//***************************** LLAMO VENTANA HISTORIAL COMPRA Y VENTA 
				itemRep.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						RptFacturacion ventana = new RptFacturacion();
						ventana.cbBusIni.setSelectedItem("VENTA-COMPRA");
						ventana.cbBusIni.setEnabled(false);
						ventana.frame.setTitle("Historial de facturaci�n por compra y venta");
		              	ventana.frame.setVisible(true);
		              	RptFacturacion.textNom.requestFocus(true);
		              	RptFacturacion.textNom.selectAll();
					}
				});
		//=====================================================================================================
		//======================================== ADMINISTRACION =============================================

		//***************************** LLAMO VENTANA PERSONAL PARA USUARIOS
		itemCueUsu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaEmpleado v = new VentanaEmpleado();
				VentanaEmpleado.frame.show();
				v.buttonNuevo.setVisible(false);
				v.buttonEditar.setVisible(false);
				v.buttonEliminar.setVisible(false);
				v.buttonBus.setVisible(false);
				v.buttonUsu.setVisible(true);
				v.textBus.setBounds(230, 21, 372, 22);
				v.textBus.requestFocus(true);
			}
		});
		
		//***************************** LLAMO VENTANA EMPRESA
		itemEmp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaEmpresa ventana = new VentanaEmpresa();
				ventana.frame.setModal(true);
				ventana.frame.setVisible(true);
	           
			}
		});
		//***************************** LLAMO VENTANA DOCUMENTOS
		itemDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaDocumento ventana = new VentanaDocumento();
              	ventana.frame.setVisible(true);
              	VentanaDocumento.textCod.requestFocus(true);
			}
		});
		//***************************** LLAMO VENTANA SERIES
		itemCor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaDocumentoSeries ventana = new VentanaDocumentoSeries();
              	ventana.frame.setVisible(true);
              	VentanaDocumentoSeries.cbDocumento.requestFocus(true);
			}
		});
		//***************************** LLAMO VENTANA IGV
		itemIgv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaIgv ventana = new VentanaIgv();
              	ventana.frame.setVisible(true);
              	VentanaIgv.textIgv.requestFocus(true);
			}
		});
		//***************************** LLAMO VENTANA DOLAR
		itemDol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VentanaDolar ventana = new VentanaDolar();
              	ventana.frame.setVisible(true);
              	VentanaDolar.textDolarC.requestFocus(true);
			}
		});
		
		//=====================================================================================================
		//======================================== REPORTES ===================================================
		
		//***************************** REPORTE DE ALOJAMIENTOS DIARIOS
		itemRA1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("ALOJAMIENTO - DIARIO"));
				parameters.put("PrtEstado",new Integer(1));// MAYOR IGUAL
				parameters.put("PrtEstados",new Integer(2));// MENOR IGUAL
				
				parameters.put("PrtDateDesde",date);
				parameters.put("PrtDateHasta",date);
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RAlojamientoGroup.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: alojamiento diaro","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		
		//***************************** REPORTE DE ALOJAMIENTOS MENSUALES
		itemRA2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("ALOJAMIENTO - MENSUAL"));
				parameters.put("PrtEstado",new Integer(1));// MAYOR IGUAL
		    	parameters.put("PrtEstados",new Integer(2));// MENOR IGUAL
				/*Date dates = new Date();
				try {
					dates = formatoFecha.parse(date);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				
				/*JDateChooser chooser= new JDateChooser();
				chooser.setDateFormatString("dd-MMM-yyyy");
				chooser.setBounds(548, 93, 92, 21);
				chooser.setDate(fecha);
				toolBar.add(chooser);*/
				
				
				/*DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cr = new GregorianCalendar();
				int f = cr.get(Calendar.DAY_OF_MONTH);
				String ff=format.format(f) ; 
				System.out.println(ff);*/
				
				
				
				/*Calendar hoy = Calendar.getInstance();
				hoy.add(Calendar.DATE, 3);
				hoy.add(Calendar.MONTH, 2);
				hoy.getActualMaximum(Calendar.DAY_OF_MONTH);
				System.out.println(hoy.getTime());*/
				
				/*GregorianCalendar hoyy = new GregorianCalendar();
				hoyy.set(Calendar.DATE, 1); //Le ponemos el primer dia de este mes
				hoyy.add(Calendar.DATE, -1); //Le restamos un d�a
				hoyy.getActualMaximum(Calendar.DAY_OF_MONTH);
				Date ultimoMesPasado = hoyy.getTime(); //o getDate, no recuerdo
				System.out.println(ultimoMesPasado);*/
				
				//SimpleDateFormat form1 = new SimpleDateFormat("yyyy-MM-dd");
				//String ff = form1.format(fecha);
				
				DecimalFormat format = new DecimalFormat("00");
		        Calendar calendar = Calendar.getInstance();
		        int Año = calendar.get(Calendar.YEAR);
		        int mes = calendar.get(Calendar.MONTH) + 1;
		        int dia = calendar.get(Calendar.DAY_OF_MONTH);
		        dia=1;
		        String desde=format.format(Año) + "-"+ format.format(mes) +"-"+ format.format(dia);
		        dia=31;
		        String hasta=format.format(Año) + "-"+ format.format(mes) +"-"+ format.format(dia);
		        
				parameters.put("PrtDateDesde",desde);
				parameters.put("PrtDateHasta",hasta);
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RAlojamientoGroup.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: alojamiento mensual","/modelo/Images/contact_book.png");
				conexion.DesconectarDB(); 

			}
		});
		//***************************** REPORTE DE RESERVACIONES
		itemRA3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("RESERVACIONES - HASTA LA FECHA"));
				parameters.put("PrtEstado",new Integer(4));// MAYOR IGUAL
				parameters.put("PrtEstados",new Integer(4));// MENOR IGUAL
				
				DecimalFormat format = new DecimalFormat("00");
		        Calendar calendar = Calendar.getInstance();
		        int Año = calendar.get(Calendar.YEAR)-1;
		        int mes = calendar.get(Calendar.MONTH) + 1-1;
		        int dia = calendar.get(Calendar.DAY_OF_MONTH);
		        
		        String desde=format.format(Año) + "-"+ format.format(mes) +"-"+ format.format(dia);
		        
				parameters.put("PrtDateDesde",desde);
				parameters.put("PrtDateHasta",date);
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RAlojamientoGroup.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: reservaciones","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		//=================================================================================================REPORT FACTURACION
		//***************************** REPORTE DE FACTURACION DIARIA
		itemRO1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("FACTURACI�N - DIARIA"));
				parameters.put("PrtEstado",new Integer(0));// MAYOR IGUAL
				parameters.put("PrtEstados",new Integer(1));// MENOR IGUAL
				parameters.put("PrtDateDesde",date);
				parameters.put("PrtDateHasta",date);
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RFacturacionGroup.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: facturaci�n","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE FACTURACION MENSUAL
		itemRO2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("FACTURACI�N - MES"));
				parameters.put("PrtEstado",new Integer(0));// MAYOR IGUAL
				parameters.put("PrtEstados",new Integer(1));// MENOR IGUAL
				
				DecimalFormat format = new DecimalFormat("00");
		        Calendar calendar = Calendar.getInstance();
		        int Año = calendar.get(Calendar.YEAR);
		        int mes = calendar.get(Calendar.MONTH) + 1;
		        int dia = calendar.get(Calendar.DAY_OF_MONTH);
		        dia=1;
		        String desde=format.format(Año) + "-"+ format.format(mes) +"-"+ format.format(dia);
		        dia=31;
		        String hasta=format.format(Año) + "-"+ format.format(mes) +"-"+ format.format(dia);
				parameters.put("PrtDateDesde",desde);
				parameters.put("PrtDate",hasta);
				//Locale locale = new Locale("US", "PEN");
				//parameters.put(JRParameter.REPORT_LOCALE, locale);
				
				//parameters.put("REPORT_LOCALE", new Locale("pen", "PEN"));
				//parameters.put(JRParameter.REPORT_LOCALE, Locale.US);
				
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RFacturacionGroup.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: facturaci�n","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE 
		itemRO3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		//***************************** REPORTE DE 
		itemRO4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		//***************************** REPORTE DE 
		itemRO5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		//***************************** REPORTE DE 
		itemRO6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		//***************************** REPORTE DE 
		itemRO7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		
		//***************************** REPORTE DE HABITACIONES TODOS
		itemRH1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtEstado",new String ("%"));
				parameters.put("PrtSubTitle",new String ("HABITACIONES - TODAS"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RHabitacionGroup.jasper",parameters );
				AbstractJasperReports.showViewer("Listado: habitaciones");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE TIPO HABITACION 
		itemRH2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("TIPOS - HABITACION"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RTipoHabitacion.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: tipos de habitaciones","/modelo/Images/contact_book.png");
				conexion.DesconectarDB(); 
			}
		});
		//***************************** REPORTE DE TARIFAS
		itemRH3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("LISTA DE TARIFAS"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RTarifaGroup.jasper",parameters );
				AbstractJasperReports.showViewer("Listado: tarifas");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE TIPO TARIFA
		itemRH4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("TIPO - TARIFA"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RTipoTarifa.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: tipos de tarifa","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE HABITACIONES DISPONIBLES 
		itemRH5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtEstado",new String ("DISPONIBLE"));
				parameters.put("PrtSubTitle",new String ("HABITACIONES - DISPONIBLES"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RHabitacion.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: habitaciones","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE HBITACIONES ALQUILADAS 
		itemRH6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtEstado",new String ("ALQUILADO"));
				parameters.put("PrtSubTitle",new String ("HABITACIONES - OCUPADAS"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RHabitacion.jasper",parameters );
				AbstractJasperReports.showViewer("Listado: habitaciones");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE HABITACIONES RESERVADAS 
		itemRH7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtEstado",new String ("RESERVADO"));
				parameters.put("PrtSubTitle",new String ("RESERVADAS"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RHabitacion.jasper",parameters );
				AbstractJasperReports.showViewer("Listado: habitaciones");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE HABITACIONES EN LIMPIEZA 
		itemRH8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtEstado",new String ("LIMPIEZA"));
				parameters.put("PrtSubTitle",new String ("HABITACIONES - LIMPIEZA"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RHabitacion.jasper",parameters );
				AbstractJasperReports.showViewer("Listado: habitaciones");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE HABITACIONES EN LIMPIEZA 
		itemRH9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtEstado",new String ("MANTENIMIENTO"));
				parameters.put("PrtSubTitle",new String ("HABITACIONES - MANTENIMIENTO"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RHabitacion.jasper",parameters );
				AbstractJasperReports.showViewer("Listado: habitaciones");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE INVENTARIOS HABITACION 
		itemRH10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("INVENTARIOS - HABITACION"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RInventarioHabitacionGroup.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: inventarios de habitaciones","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		
		//***************************** REPORTE DE CLIENTES
		itemRC0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("H�ESPEDES - TODOS"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RCliente.jasper",null );
				AbstractJasperReports.showViewer("Listado: hu�spedes");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE CLIENTES CON DSCT
		itemRC1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("H�ESPEDES - CON DSCT"));
				parameters.put("PrtDsct",new Integer(1));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RClienteDsct.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: hu�spedes con dsct","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		
		//***************************** REPORTE DE CLIENTES CTS POR PAGAR
		itemRC2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("H�ESPEDES - CTS POR PAGAR"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RClienteCta.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: hu�spedes con cts","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE CLIENTES ALOJADOS
		itemRC3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("H�ESPEDES - ALOJADOS"));
				parameters.put("PrtEstado",new Integer(1));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RClienteAlojados.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: hu�spedes alojados","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		
		//***************************** REPORTE DE INVENTARIOS
		itemRV1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("INVENTARIOS - VITRINA"));
				parameters.put("PrtCategoria",new String("VITRINA"));
				parameters.put("PrtEstado",new String("A"));
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"RInventario.jasper",parameters );
				AbstractJasperReports.showViewerIncustado("Listado: inventarios","/modelo/Images/contact_book.png");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE VENTAS DIARIAS
		itemRC2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


			}
		});
		//***************************** REPORTE DE VENTAS MENSUALES
		itemRC3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


			}
		});
		//***************************** REPORTE DE EMPLEADOS
		itemREmpleado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				// LLAMO AL JASPERT REPORT MEDIANTE LA CLASE ABSTRACTA
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"Empleado.jasper",null );
				AbstractJasperReports.showViewer("Listado: empleados");
				conexion.DesconectarDB();
			}
		});
		//***************************** REPORTE DE PROVEEDORES
		itemRProveedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conexion = new ConexionDB();
				// LLAMO AL JASPERT REPORT MEDIANTE LA CLASE ABSTRACTA
				AbstractJasperReports.createReport( conexion.gConnection(), URL+"Rproveedor.jasper",null);
				AbstractJasperReports.showViewer("Listado: proveedores");
				conexion.DesconectarDB();
			}
		});
		
		if (VentanaLogin.TIP_USUARIO.equals("ADMINISTRADOR")||VentanaLogin.TIP_USUARIO.equals("PROGRAMADOR")) {
			menu4.addSeparator();
			menu4.add(itemMovGas);
		}
		if (VentanaLogin.TIP_USUARIO.equals("RECEPCIONISTA")||VentanaLogin.TIP_USUARIO.equals("CAJERO")) {
			
			//************************************ALTAS PRINCIPALES
			itemAltPer.setVisible(false);//EMPLEADOS
			itemAltGesHab.setVisible(false);//HABITACIONES
			itemAltInv.setVisible(false);//INVENTARIOS HOTEL
			itemCom.setVisible(false);//COMPRAS
			//************************************HERRAMIENTAS
			itemTipUsu.setVisible(false);//TIPO USUARIO
			itemAltTur.setVisible(false);//ALTA DE TURNOS
			itemEmp.setVisible(false);//ALTA DE EMPLEADOS
			itemDoc.setVisible(false);//ALTA DOCUMENTOS
			itemCor.setVisible(false);//CORRELATIVOS
			itemMovGas.setVisible(false);//GASTOS
		}
		if (VentanaLogin.TIP_USUARIO.equals("INVITADO")) {
			btnAltComp.setVisible(false);
			btnAltHab.setVisible(false);
			btnTar.setVisible(false);
			btnEmp.setVisible(false);
			
			menu1.setVisible(false);//ARCHIVO
				itemImp.setVisible(false);//CONFIGURAR IMPRESION
				itemSec.setVisible(false);//CERRAR SECION
				itemSal.setVisible(false);//SALIR
				
			menu2.setVisible(false);//ALTAS PRINCIPALES
			
			itemAltCli.setVisible(false);//CLIENTES
			itemAltPro.setVisible(false);//PROVEEEDOR
			itemAltPer.setVisible(false);//EMPLEADOS
			
			itemAltGesHab.setVisible(false);//HABITACIONES
				itemAltHabImb.setVisible(false);//SEARCH HABITACION
				itemAltHab.setVisible(false);//ALTA HABITACION
				itemAltTip.setVisible(false);//ALTA TIPO HABITACION
				itemAltTar.setVisible(false);//GESTION TARIFAS
				itemAltTipTar.setVisible(false);//ALATA TIPO TARIFA
				itemAltImp.setVisible(false);//IMPLEMENTAR HABITACION
				
			itemAltInv.setVisible(false);//INVENTARIOS HOTEL
				subitemConInv.setVisible(false);//SEARCH ARTICULLOS
				subitemInv.setVisible(false);//ALTA ARTICULOS
				subitemCat.setVisible(false);//ALTA CATEGORIA

			subitemSerSearch.setVisible(false);//SEARCH SERVICIOS
			subitemSer.setVisible(false);//ALTA SERVICIOS

			menu3.setVisible(false);//CONTROL HOTEL
				itemConHot.setVisible(false);//MOSTRADOR
				itemFact.setVisible(false);//HISTAORIAL FACTURACION
				
			menu4.setVisible(false);//TURNO
				itemBalCaj.setVisible(false);//MOVIMIENTOS DE CAJA CHICA
				itemHisCta.setVisible(false);//HISTORIAL DE CTS
				itemMovGas.setVisible(false);//MOVIMIENTOS DE GASTOS
				itemApeCaj.setVisible(false);//ABRIR CAJA
				itemCieCaj.setVisible(false);//CERRAR CAJA
				itemMovGas.setVisible(false);//GASTOS
			menu5.setVisible(false);//VITRINA
				itemInvVit.setVisible(false);//INVENTARIOS DE VITRINA
				itemVen.setVisible(false);//VENTAS
				itemCom.setVisible(false);//COMPRAS
				itemRep.setVisible(false);//HISTORIAL DE COMPRA Y VENTA
			
			menu6.setVisible(false);//HERRAMIENTAS
				itemTipUsu.setVisible(false);//TIPO USUARIO
				itemCueUsu.setVisible(false);//USUARIO
				itemAltTur.setVisible(false);//ALTA DE TURNOS
				itemEmp.setVisible(false);//ALTA DE EMPLEADOS
				itemDoc.setVisible(false);//ALTA DOCUMENTOS
				itemCor.setVisible(false);//CORRELATIVOS
				itemIgv.setVisible(false);//IGV
				itemDol.setVisible(false);//DOLAR
				
			menu7.setVisible(false);//REPORTES
				itemRCHECK_IN.setVisible(false);//TIPO USUARIO
				itemRA1.setVisible(false);//TIPO USUARIO
				itemRA2.setVisible(false);//TIPO USUARIO
				itemRA3.setVisible(false);//TIPO USUARIO
				itemRCHECK_OUT.setVisible(false);//TIPO USUARIO
				itemRO1.setVisible(false);//TIPO USUARIO
				itemRO2.setVisible(false);//TIPO USUARIO
				itemRO3.setVisible(false);//TIPO USUARIO
				itemRO4.setVisible(false);//TIPO USUARIO
				itemRO5.setVisible(false);//TIPO USUARIO
				itemRO6.setVisible(false);//TIPO USUARIO
				itemRO7.setVisible(false);//TIPO USUARIO
				itemRTesoreria.setVisible(false);//TIPO USUARIO
				itemRT1.setVisible(false);//TIPO USUARIO
				itemRT2.setVisible(false);//TIPO USUARIO
				itemRT3.setVisible(false);//TIPO USUARIO
				itemRT4.setVisible(false);//TIPO USUARIO
				itemRT5.setVisible(false);//TIPO USUARIO
				itemRT6.setVisible(false);//TIPO USUARIO
				itemRHabitacion.setVisible(false);//TIPO USUARIO
				itemRH1.setVisible(false);//TIPO USUARIO
				itemRH2.setVisible(false);//TIPO USUARIO
				itemRH3.setVisible(false);//TIPO USUARIO
				itemRH4.setVisible(false);//TIPO USUARIO
				itemRH6.setVisible(false);//TIPO USUARIO
				itemRH7.setVisible(false);//TIPO USUARIO
				itemRH8.setVisible(false);//TIPO USUARIO
				itemRH9.setVisible(false);//TIPO USUARIO
				itemRVitrina.setVisible(false);//TIPO USUARIO
				itemRV1.setVisible(false);//TIPO USUARIO
				itemRV2.setVisible(false);//TIPO USUARIO
				itemRV3.setVisible(false);//TIPO USUARIO
				itemRCliente.setVisible(false);//TIPO USUARIO
				
				itemRC1.setVisible(false);//TIPO USUARIO
				itemRC2.setVisible(false);//TIPO USUARIO
				itemRC3.setVisible(false);//TIPO USUARIO
				itemREmpleado.setVisible(false);//TIPO USUARIO
				itemRProveedor.setVisible(false);//TIPO USUARIO
				
			menu8.setVisible(false);//OTROS
			
			menu9.setVisible(false);//AYUDA
				itemAyuda.setVisible(false);//AYUDA;
				itemAcerca.setVisible(false);//ACERCA
				itemActivar.setVisible(false);//ACTIVAR
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	//public static void main(String args[]) {
		//new Menu();
	//}
}
