package modelo.Presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import modelo.Clases.AbstractJasperReports;
import modelo.Datos.ConexionDB;
import modelo.Otros.lengthScreem;

public class VentanaControlHotel implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener{
	private static ConexionDB conexion;
	public static JInternalFrame frame;
	private JPanel  panelDto,panelBtn,panelPie,panelToolbar;
	private JButton buttonCerrarCaja,buttonActualizar,buttonSalir;
	
	private JLabel	lblD,lblO,lblR,lblL,lblM,lblT,lbli1,lbli2,lbli3,lbli4,lbli7,lbli8,lbli5,lbli6,lblTCambio;
	private JButton	[] button =  new JButton[155];//133
	private JLabel	[] label  =  new JLabel[155];//133
	public JToolBar	toolBar;
	protected JButton btnAlt,btnAltReserva,btnConsum,btnTranferencia,btnPag,btnOut,btnAnul,btnTarjetaReg,btnTarjetaSer;
	//TAMAÑO DE PANTALL
	lengthScreem l = new lengthScreem();
	private final int lengthScrem = l.getAncho();
	protected static JRadioButton radioDos,radioUno;
	
	private JComboBox<String> 	cbTipoReg,cbTipoHab;
	
	public static String NRO="",HABITACION="",DESCRIPCION="",HUESPED="",ACOMPANANTE="";
	public static int ID_ALQUILER;
	public static int ID_C,ID_DETALLE;
	private JPopupMenu popupMenu;
	private JMenuItem mI,mD,mL,mO,mEli,mTar;
	private String EstadoPoppu="";

	private String HORA="";
	
	protected static int NRO_CUENTA_CREDITO; //CODIGO CUENTA DEL HUESPED
	private String TIPO_HABITACION="";
	private static int ESTADO_ALQUILER;
	
	public VentanaControlHotel() {
		frameControlHotel();
		crearPanel();
		crearLabels();
		crearButtons();
		crearToolbar();
		llenarButtons();
		clock();
		llenarTipoHabOrden();
		
		btnAlt.setEnabled(false);
		btnAltReserva.setEnabled(false);
		btnTranferencia.setEnabled(false);
		btnTarjetaSer.setEnabled(false);
		btnConsum.setEnabled(false);
		btnPag.setEnabled(false);
		btnOut.setEnabled(false);
		
		// AVISA QUE HABITACIONES SE DESOCUPARAN 
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(timerTask, 0,1000);
		Menu.Desktop.add(frame);
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
    	frame.setLocation(x, y);
	}
	/*TimerTask timerTask = new TimerTask(){
         public void run() {
             Color c = Color.BLACK;
             Random rand = new Random();
             int x = rand.nextInt(3) + 1;
             switch (x) {
                 case 1 : c = Color.RED;break;
                 case 2 : c = Color.BLUE;break;
                 case 3 : c = Color.GREEN;break;
             }
			mostrarDesocupar();
             lblD.setForeground(c);
         }
	 };*/
	
	public void frameControlHotel() {
		frame = new JInternalFrame("M�dulo - Control Hotelero");//MODULO DE CONTROL
		frame.setFrameIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/Control-hotel.png")));
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				//System.out.println("metodo activate");
				llenarButtons();
				deshabilitarBtn();

				VentanaLogin.CONSULTO_CAJA_APERTURA();
		        lbli5.setText("TURNO: "+VentanaLogin.DESCRIPCION_TURNO);
		        lblTCambio.setText("TIPO CAMBIO: "+Double.toString(VentanaLogin.TIPO_CAMBIO));
		        if(VentanaLogin.DESCRIPCION_TURNO.equals("CERRADO")) {
		        	int i=0;
		    		int l=button.length;
		    		for(i=1; i<l; i++) {
		    			timerTask.cancel();
		    			button[i].setEnabled(false);
		    			label[i].setEnabled(false);
		    			buttonActualizar.setEnabled(false);
		    			buttonCerrarCaja.setEnabled(false);
		    			panelBtn.setEnabled(false);
		    			panelDto.setEnabled(false);
		    			panelPie.setEnabled(false);
		    		}
		        }
			}
		});
		frame.setClosable(true);
		frame.setBounds(0, 0, lengthScrem,Menu.Desktop.getHeight());
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
	}
	public void clock() {
		Thread clock = new Thread(){
			public void run(){
				try {
					for(;;){
					llenarCountEstados();	
					DecimalFormat format = new DecimalFormat("00");
					Calendar cr = new GregorianCalendar();
					int second = cr.get(Calendar.SECOND);
					int minute = cr.get(Calendar.MINUTE);
					int hour = cr.get(Calendar.HOUR_OF_DAY);
					String ampm = cr.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM";
		            if ( hour >= 13  ) {
		                hour = cr.get(Calendar.HOUR);
		            }
					HORA=format.format(hour) +":"+ format.format(minute) +":"+ format.format(second) +" "+ ampm; 

					//panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),(" | hora de ingreso ===> " + HORA)));
					lbli6.setText("::: | " + Menu.dateLarga + " | ::: | " + HORA +" | :::");
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
		panelBtn = new JPanel();
		panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "MODULO DE CONTROL HOTELERO", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelBtn.setBounds(10, 36, lengthScrem - 20, 42);
		frame.getContentPane().add(panelBtn);
		panelBtn.setLayout(null);
		
		panelDto = new JPanel();
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| ::: Vistas ::: | estado de habitaciones", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDto.setBounds(10, 75, lengthScrem - 20, 468);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		panelPie = new JPanel();
		panelPie.setBackground(SystemColor.controlHighlight);
		panelPie.setBounds(0,  frame.getSize().height -64 , lengthScrem - 20, 24);
		frame.getContentPane().add(panelPie);
		panelPie.setLayout(null);
		
		frame.getContentPane().add(panelPie, BorderLayout.SOUTH);
		panelPie.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		//panelEst.setLayout(new FlowLayout(FlowLayout.RIGHT));
		//panelPie.setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	public void crearToolbar() {
		toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setBounds(10, 5, lengthScrem-20, 32);
		frame.getContentPane().add(toolBar);
		
		// CREO BUTTON TOOLBAR
		btnAlt = new JButton("Check-in");
		btnAlt.setFont(new Font("Agency FB", Font.PLAIN, 18));
		btnAlt.setToolTipText("Generar alojamiento");
		btnAlt.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/document_green.png")));
		btnAlt.addActionListener(this);
		toolBar.add(btnAlt);
		
		btnAltReserva = new JButton("Reservar");
		btnAltReserva.setFont(new Font("Agency FB", Font.PLAIN, 18));
		btnAltReserva.setToolTipText("Gestionar reserva");
		btnAltReserva.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/document_yellow.png")));
		btnAltReserva.addActionListener(this);
		toolBar.add(btnAltReserva);
		
		btnAnul = new JButton("Anular Check-in");//Eliminar
		btnAnul.setFont(new Font("Agency FB", Font.PLAIN, 18));
		btnAnul.setToolTipText("Eliminar Alojamiento");
		btnAnul.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/delete.png")));
		btnAnul.addActionListener(this);
		//btnAnul.setBorderPainted(false);
		toolBar.add(btnAnul);
		
		
		btnTarjetaReg= new JButton("Tarj. Reg.");
		btnTarjetaReg.setEnabled(false);
		btnTarjetaReg.setFont(new Font("Agency FB", Font.PLAIN, 18));
		btnTarjetaReg.setToolTipText("Tarjeta de registro");
		btnTarjetaReg.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/old-versions.png")));
		btnTarjetaReg.addActionListener(this);
		//btnDocu.setBorderPainted(false);
		toolBar.add(btnTarjetaReg);
		
		btnTranferencia = new JButton("Gst.cambio-Hab.");
		btnTranferencia.setFont(new Font("Agency FB", Font.PLAIN, 18));
		btnTranferencia.setToolTipText("<html>Gestionar cambios de habitaci�n<br>Por tipo, nro, tarifa, plan y fecha de salida</html>");
		btnTranferencia.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/revert.png")));
		btnTranferencia.addActionListener(this);
		toolBar.add(btnTranferencia);
		
		btnConsum = new JButton("Consumo...");
		btnConsum.setFont(new Font("Agency FB", Font.PLAIN, 18));
		btnConsum.setToolTipText("Tarjeta de consumo del Huesped");
		btnConsum.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/consumo.png")));
		btnConsum.addActionListener(this);
		toolBar.add(btnConsum);
		
		btnTarjetaSer= new JButton("Tarjeta de servicios");
		btnTarjetaSer.setEnabled(false);
		btnTarjetaSer.setFont(new Font("Agency FB", Font.PLAIN, 18));
		btnTarjetaSer.setToolTipText("Tarjeta de servicios");
		btnTarjetaSer.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/select.png")));
		btnTarjetaSer.addActionListener(this);
		//btnDocu.setBorderPainted(false);
		toolBar.add(btnTarjetaSer);
		
		btnPag = new JButton("Cuenta-huesped");
		btnPag.setFont(new Font("Agency FB", Font.PLAIN, 18));
		btnPag.setToolTipText("Pagos del huesped");
		btnPag.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/Dollar_Green.png")));
		btnPag.addActionListener(this);
		//btnPag.setBorderPainted(false);
		toolBar.add(btnPag);
		
		btnOut = new JButton("Check-Out");
		btnOut.setFont(new Font("Agency FB", Font.PLAIN, 18));
		btnOut.setToolTipText("Generar documento");
		btnOut.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/cout.png")));
		btnOut.addActionListener(this);
		//btnDocu.setBorderPainted(false);
		toolBar.add(btnOut);
		

		panelToolbar = new JPanel();
		toolBar.add(panelToolbar);
		panelToolbar.setLayout(null);
		
		lblTCambio = new JLabel("**********");
		lblTCambio.setSize(182, 20);
		lblTCambio.setLocation(0, 5);
		lblTCambio.setOpaque(true);
		lblTCambio.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTCambio.setForeground(new Color(220, 20, 60));
		lblTCambio.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTCambio.setBackground(SystemColor.menu);
		panelToolbar.add(lblTCambio);
		
		/*radioDos = new JRadioButton("GRUPAL");
		radioDos.setForeground(new Color(0, 0, 205));
		radioDos.setFont(new Font("Tahoma", Font.BOLD, 12));
		radioDos.setSize(89, 19);
		radioDos.setLocation(184, 6);
		radioDos.addActionListener(this);
		buttonGroup.add(radioDos);
		panelToolbar.add(radioDos);
		
		radioUno = new JRadioButton("INDIVIDUAL");
		radioUno.setForeground(new Color(0, 0, 205));
		radioUno.setSelected(true);
		radioUno.setFont(new Font("Tahoma", Font.BOLD, 12));
		radioUno.setSize(98, 19);
		radioUno.setLocation(90, 6);
		radioUno.addActionListener(this);
		buttonGroup.add(radioUno);
		panelToolbar.add(radioUno);*/
	}
	public void crearLabels() {
		int i=0;int x=10;
		int Width=65,Height=14;
		int l=label.length;
		for(i=1; i<l; i++) {
			label[i]= new JLabel("Inhabilitado "+ i);
			label[i].setForeground(Color.WHITE);
			label[i].setOpaque(true);
			label[i].setBackground(new Color(102, 102, 102));
			panelDto.add(label[i]);
			label[i].setHorizontalAlignment(SwingConstants.CENTER);
			label[i].setFont(Menu.fontLabel);
			label[i].setVisible(false);
			label[i].setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			
			if (i==21||i==41||i==61||i==81||i==101||i==121||i==141 ||i==161||i==181||i==201||i==221){
				x=10;
			}
			if (i<=20){
				label[i].setBounds(x, 47, Width, Height);x=x+67;
			}
			if (i>20 && i<=40){
				label[i].setBounds(x, 87, Width, Height);x=x+67;
			}
			if (i>40 && i<=60){
				label[i].setBounds(x, 127, Width, Height);x=x+67;
			}
			if (i>60 && i<=80){
				label[i].setBounds(x, 167, Width, Height);x=x+67;
			}
			if (i>80 && i<=100){
				label[i].setBounds(x, 207, Width, Height);x=x+67;
			}
			if (i>100 && i<=120){
				label[i].setBounds(x, 247, Width, Height);x=x+67;
			}
			if (i>120 && i<=140){
				label[i].setBounds(x, 287, Width, Height);x=x+67;
			}
			if (i>140 && i<=160){
				label[i].setBounds(x, 327, Width, Height);x=x+67;
			}
			if (i>160 && i<=180){
				label[i].setBounds(x, 367, Width, Height);x=x+67;
			}
			if (i>180 && i<=200){
				label[i].setBounds(x, 407, Width, Height);x=x+67;
			}
			if (i>220 && i<=240){
				label[i].setBounds(x, 447, Width, Height);x=x+67;
			}
		}
		panelPie.setLayout(null);
		
		lblD= new JLabel("DISPONIBLE:");
		lblD.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblD.setBackground(SystemColor.controlHighlight);
		lblD.setForeground(new Color(0, 153, 51));
		lblD.setBounds(7, 7, 69, 14);
		lblD.setOpaque(true);
		panelPie.add(lblD);
		lblD.setHorizontalAlignment(SwingConstants.RIGHT);
		//lblD.setFont(Menu.fontLabel);
		
		lbli1= new JLabel("000   ");
		lbli1.setForeground(new Color(0, 153, 51));
		lbli1.setBounds(81, 7, 30, 14);
		lbli1.setOpaque(true);
		lbli1.setBackground(SystemColor.controlHighlight);
		panelPie.add(lbli1);
		lbli1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbli1.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lblO= new JLabel("ALQULADO:");
		lblO.setBackground(SystemColor.controlHighlight);
		lblO.setForeground(new Color(204, 0, 0));
		lblO.setBounds(116, 7, 63, 14);
		lblO.setOpaque(true);
		panelPie.add(lblO);
		lblO.setHorizontalAlignment(SwingConstants.RIGHT);
		lblO.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lbli2= new JLabel("000   ");
		lbli2.setForeground(new Color(255, 0, 0));
		lbli2.setBounds(184, 7, 30, 14);
		lbli2.setOpaque(true);
		lbli2.setBackground(SystemColor.controlHighlight);
		panelPie.add(lbli2);
		lbli2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbli2.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lblR= new JLabel("RESERVADO:");
		lblR.setBackground(SystemColor.controlHighlight);
		lblR.setForeground(new Color(255, 102, 51));
		lblR.setBounds(219, 7, 69, 14);
		lblR.setOpaque(true);
		panelPie.add(lblR);
		lblR.setHorizontalAlignment(SwingConstants.RIGHT);
		lblR.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lbli3= new JLabel("000   ");
		lbli3.setForeground(new Color(255, 102, 51));
		lbli3.setBounds(293, 7, 30, 14);
		lbli3.setOpaque(true);
		lbli3.setBackground(SystemColor.controlHighlight);
		panelPie.add(lbli3);
		lbli3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbli3.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblL= new JLabel("LIMPIEZA:");
		lblL.setBackground(SystemColor.controlHighlight);
		lblL.setForeground(SystemColor.textHighlight);
		lblL.setBounds(328, 7, 57, 14);
		lblL.setOpaque(true);
		panelPie.add(lblL);
		lblL.setHorizontalAlignment(SwingConstants.RIGHT);
		lblL.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lbli4= new JLabel("000   ");
		lbli4.setForeground(SystemColor.textHighlight);
		lbli4.setBounds(390, 7, 30, 14);
		lbli4.setOpaque(true);
		lbli4.setBackground(SystemColor.controlHighlight);
		panelPie.add(lbli4);
		lbli4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbli4.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		
		lblM= new JLabel("MANTENIMIENTO:");
		lblM.setBackground(SystemColor.controlHighlight);
		lblM.setForeground(UIManager.getColor("ToggleButton.foreground"));
		lblM.setBounds(425, 7, 96, 14);
		lblM.setOpaque(true);
		panelPie.add(lblM);
		lblM.setHorizontalAlignment(SwingConstants.RIGHT);
		lblM.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lbli7= new JLabel("000   ");
		lbli7.setForeground(UIManager.getColor("ToggleButton.foreground"));
		lbli7.setBounds(526, 7, 30, 14);
		lbli7.setOpaque(true);
		lbli7.setBackground(SystemColor.controlHighlight);
		panelPie.add(lbli7);
		lbli7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbli7.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lblT= new JLabel("=");
		lblT.setEnabled(false);
		lblT.setBackground(SystemColor.controlHighlight);
		lblT.setForeground(new Color(0, 0, 255));
		lblT.setBounds(561, 7, 14, 14);
		lblT.setOpaque(true);
		panelPie.add(lblT);
		lblT.setHorizontalAlignment(SwingConstants.LEFT);
		lblT.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lbli8= new JLabel("000   ");
		lbli8.setForeground(new Color(0, 0, 205));
		lbli8.setBounds(574, 7, 156, 14);
		lbli8.setOpaque(true);
		lbli8.setBackground(SystemColor.controlHighlight);
		panelPie.add(lbli8);
		lbli8.setHorizontalAlignment(SwingConstants.LEFT);
		lbli8.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lbli5= new JLabel("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		lbli5.setForeground(new Color(165, 42, 42));
		lbli5.setBounds(10, 13, 207, 23);
		lbli5.setOpaque(true);
		lbli5.setBackground(SystemColor.control);
		panelBtn.add(lbli5);
		lbli5.setHorizontalAlignment(SwingConstants.LEFT);
		lbli5.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		lbli6= new JLabel("00:00:00");
		lbli6.setForeground(new Color(165, 42, 42));
		lbli6.setBounds(432, 13, 398, 23);
		lbli6.setOpaque(true);
		lbli6.setBackground(SystemColor.control);
		panelBtn.add(lbli6);
		lbli6.setHorizontalAlignment(SwingConstants.RIGHT);
		lbli6.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		cbTipoHab = new JComboBox<String>();
		cbTipoHab.setToolTipText("Filtrar por tipo");
		cbTipoHab.setFont(Menu.fontText);
		cbTipoHab.addActionListener(this);
		cbTipoHab.setBounds(806, 2, 250, 20);
		cbTipoHab.addFocusListener(this);
		cbTipoHab.addKeyListener(this);
		panelPie.add(cbTipoHab);
		
		cbTipoReg = new JComboBox<String>();
		cbTipoReg.setToolTipText("Filtrar por piso");
		cbTipoReg.setFont(Menu.fontText);
		cbTipoReg.addActionListener(this);
		cbTipoReg.setBounds(763, 2, 80, 20);
		cbTipoReg.addFocusListener(this);
		cbTipoReg.addKeyListener(this);
		panelPie.add(cbTipoReg);
	}
	public void crearButtons() {
		int i=0;int x=10;
		int Width=65,Height=25;
		int l=button.length;
		for(i=1; i<l; i++) {
			button[i]= new JButton("H- "+i);
			button[i].setHorizontalAlignment(SwingConstants.LEFT);
			button[i].addActionListener(this);
			button[i].addMouseListener(this);
			button[i].addFocusListener(this);
			button[i].setBounds(10, 23, 77, 25);
			//button[i].setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/btn-dis.png")));
			button[i].setFont(Menu.fontText);
			panelDto.add(button[i]);
			button[i].setVisible(false);
			
			//button[i].setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			//button[i].setBorder(new LineBorder(new Color(64, 224, 208)));
			
			button[i].setOpaque(false);
			button[i].setBorder(null);
			button[i].setBorderPainted(false);

			if (i==21||i==41||i==61||i==81||i==101||i==121||i==141 ||i==161||i==181||i==201||i==221){
				x=10;
			}
			if (i<=20){
				button[i].setBounds(x, 22, Width, Height);x=x+67;
			}
			if (i>20 && i<=40){
				button[i].setBounds(x, 62, Width, Height);x=x+67;
			}
			if (i>40 && i<=60){
				button[i].setBounds(x, 102, Width, Height);x=x+67;
			}
			if (i>60 && i<=80){
				button[i].setBounds(x, 142, Width, Height);x=x+67;
			}
			if (i>100 && i<=120){
				button[i].setBounds(x, 182, Width, Height);x=x+67;
			}
			if (i>120 && i<=140){
				button[i].setBounds(x, 222, Width, Height);x=x+67;
			}
			if (i>140 && i<=160){
				button[i].setBounds(x, 262, Width, Height);x=x+67;
			}
			if (i>160 && i<=180){
				button[i].setBounds(x, 302, Width, Height);x=x+67;
			}
			if (i>200 && i<=220){
				button[i].setBounds(x, 342, Width, Height);x=x+67;
			}
			if (i>220 && i<=240){
				button[i].setBounds(x, 382, Width, Height);x=x+67;
			}
			if (i>240 && i<=260){
				button[i].setBounds(x, 422, Width, Height);x=x+67;
			}
			
		}

		
		buttonCerrarCaja= new JButton("");
		buttonCerrarCaja.setToolTipText("Cerrar Turno");
		buttonCerrarCaja.addActionListener(this);
		buttonCerrarCaja.setBounds(834, 13, 36, 23);
		buttonCerrarCaja.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/cajacerrar.png")));
		panelBtn.add(buttonCerrarCaja);
		
		buttonActualizar= new JButton("");
		buttonActualizar.setToolTipText("Actualizar");
		buttonActualizar.addActionListener(this);
		buttonActualizar.setBounds(873, 13, 36, 23);
		buttonActualizar.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/Refresh.png")));
		panelBtn.add(buttonActualizar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(910, 13, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/Exit.png")));
		panelBtn.add(buttonSalir);
	}
	void llenarTipoHabOrden(){
		conexion = new ConexionDB();
		cbTipoHab.removeAllItems();
		try {
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery("Select * from TIPO_HABITACION");
			cbTipoHab.addItem("TODOS");
			while (rs.next()==true) {
				cbTipoHab.addItem(rs.getString("Tipo_Hab").trim());
			}
			rs.close();
			st.close();
		} catch (Exception e) {}
		
		cbTipoReg.removeAllItems();
		cbTipoReg.addItem("TODOS");
		
		String [] lista1 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"
				   ,"21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40"
		};
		for (String llenar:lista1) {
			cbTipoReg.addItem(llenar);
		}

		conexion.DesconectarDB();
	}
	public void llenarButtons() {
		HABITACION="";HUESPED="";
		ID_ALQUILER= 0;ID_C = 0;DESCRIPCION="";
		
		// OCULTO TODO LOS BOTONES
		for(int y=1; y<button.length; y++) {button[y].setVisible(false);label[y].setVisible(false);}		
		// END DOCULTO TODO LOS BOTONES
		
		int TItem= 0;
		conexion = new ConexionDB();
		String consult="",estado="";
		try {
			String [] lista = {"DISPONIBLE","OCUPADO","ALQUILADO","RESERVADO","LIMPIEZA","MANTENIMIENTO"};
			for(String llenar:lista) {
				estado=llenar;
				if (cbTipoReg.getSelectedItem()=="TODOS"&&cbTipoHab.getSelectedItem()=="TODOS") {
					consult="Select * from HABITACION where EstadoHab='" + estado + "'";
				}
				if (cbTipoReg.getSelectedItem()!="TODOS"&&cbTipoHab.getSelectedItem()!="TODOS") {
					consult="Select * from HABITACION where EstadoHab='" + estado + "'and PisoHab='" + cbTipoReg.getSelectedItem().toString().trim() + "'and Tipo_Hab='" + cbTipoHab.getSelectedItem().toString().trim() + "'";
				}
				
				if (cbTipoReg.getSelectedItem()=="TODOS"&&cbTipoHab.getSelectedItem()!="TODOS") {
					consult="Select * from HABITACION where EstadoHab='" + estado + "'and Tipo_Hab='" + cbTipoHab.getSelectedItem().toString().trim() + "'";
				}
				if (cbTipoReg.getSelectedItem()!="TODOS"&&cbTipoHab.getSelectedItem()=="TODOS") {
					consult="Select * from HABITACION where EstadoHab='" + estado + "'and PisoHab='" + cbTipoReg.getSelectedItem().toString().trim() + "'";
				}
				
				Statement statement = conexion.gConnection().createStatement();
				ResultSet rs = statement.executeQuery(consult);
				while (rs.next()==true){
					int i=0;
					int b=button.length;
					for(i=0; i<b; i++) {
						if (rs.getInt("OrdenarHab")==i){       // ORDENA POR NUMERO ORDEN MANUAL
							//if (rs.getInt("NumeroHab")==i){ // ORDENA POR NUMERO HABITACION
							button[i].setVisible(true);
							label[i].setVisible(true);
							
							button[i].setText("H-"+rs.getString("NumeroHab"));
							button[i].setToolTipText("<html> --------| "+ rs.getString("Tipo_Hab") +" #"+ rs.getString("Numerohab") +  " PISO #" + rs.getString("pisoHab") +" |-------<br> ");//estado
							label[i].setText(rs.getString("Tipo_Hab"));
							
							button[i].setFont(Menu.fontText);
							
							if (estado.trim()=="DISPONIBLE") {
								button[i].setBackground(new Color(64, 224, 208));//0, 160, 122 
								button[i].setForeground(SystemColor.BLACK);//new Color(255, 255, 255));
								button[i].setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/btn-dis.png")));
								
								label[i].setBackground(new Color(0, 179, 113));
								label[i].setForeground(new Color(255, 255, 255));
							}
							else if (estado.trim()=="ALQUILADO") {
								button[i].setBackground(new Color(204, 102, 102));
								button[i].setForeground(new Color(204, 102, 102));
								button[i].setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/menu-user.png")));
								
								label[i].setBackground(new Color(204, 102, 102));
								label[i].setForeground(new Color(255, 255, 255));
								//MUESTRA MSJ AL PASAR EL MAUSE SOBRE EL LBL
								String con="Select * from DETALLE_A_HABITACION,ALQUILER,CLIENTES where ALQUILER.Id_Cli=CLIENTES.Id_Cli and DETALLE_A_HABITACION.Id_A = ALQUILER.Id_A and ALQUILER.EstadoA <>'" + 0 + "'and DETALLE_A_HABITACION.NumeroH='" + rs.getString("NumeroHab").trim() + "'order by DETALLE_A_HABITACION.Id_A desc limit 0,1";
								Statement s = conexion.gConnection().createStatement();
								ResultSet r = s.executeQuery(con);
								if (r.next()==true) {
									button[i].setToolTipText("<html><h4>REG.: "+
															 Menu.formatid_9.format(r.getInt("Id_A"))+" - "+  Menu.formatoFechaString.format(r.getDate("FechaEmisionA")).trim()+"</h4>"+
															 r.getString("nombrecli").trim()+"<br>"+
															 r.getString("DescripcionH").trim() + " S/."+ r.getFloat("ImporteH") + 
															 "<h4>F.Ingreso:  "+" <br>"+
															 Menu.formatoFechaString.format(r.getDate("FechaIngresoH")) + " | "+ r.getString("HoraIngresoH").trim() +" <br>"+
															 "F.Salida:  "+" <br>"+
															 Menu.formatoFechaString.format(r.getDate("FechaSalidaH")) + " | "+ r.getString("HoraSalidaH").trim() + " <img src=\""+getClass().getResource("/modelo/Images/searchNormal.png")+"\"> </h4></html>");
									
															 

								}
								r.close();
								s.close();
							}
							else if (estado.trim()=="RESERVADO") {
								button[i].setBackground(new Color(255, 160, 122));
								button[i].setForeground(new Color(204, 102, 102));
								button[i].setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/btn-res.png")));
								
								label[i].setBackground(new Color(255, 160, 122));
								label[i].setForeground(new Color(255, 255, 255));
								
								//MUESTRA MSJ AL PASAR EL MAUSE SOBRE EL LBL
								String con="Select * from DETALLE_A_HABITACION,ALQUILER,CLIENTES where ALQUILER.Id_Cli=CLIENTES.Id_Cli and DETALLE_A_HABITACION.Id_A = ALQUILER.Id_A and ALQUILER.EstadoA <>'" + 0 + "'and DETALLE_A_HABITACION.NumeroH='" + rs.getString("NumeroHab").trim() + "'order by DETALLE_A_HABITACION.Id_A desc limit 0,1";
								Statement s = conexion.gConnection().createStatement();
								ResultSet r = s.executeQuery(con);
								if (r.next()==true) {
									button[i].setToolTipText("<html><h4>REG.: "+
											 				 Menu.formatid_9.format(r.getInt("Id_A"))+" - "+  Menu.formatoFechaString.format(r.getDate("FechaEmisionA")).trim()+"</h4>"+
															 r.getString("nombrecli").trim()+"<br>"+
															 r.getString("DescripcionH").trim() + " S/."+ r.getFloat("ImporteH") + 
															 "<h4>F.Llegada:  "+" <br>"+
															 Menu.formatoFechaString.format(r.getDate("FechaIngresoH")) + " | "+ r.getString("HoraIngresoH").trim() +" <br>"+
															 "F.Salida:  "+" <br>"+
															 Menu.formatoFechaString.format(r.getDate("FechaSalidaH")) + " | "+ r.getString("HoraSalidaH").trim() +" </h4></html>");
									
															 

								}
								r.close();
								s.close();
							}
							else if (estado.trim()=="LIMPIEZA") {
								button[i].setBackground(new Color(255, 255, 255));
								button[i].setForeground(new Color(255, 0, 0));
								button[i].setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/btn-lim.png")));
								
								label[i].setBackground(new Color(255, 255, 255));
								label[i].setForeground(new Color(255, 0, 0));
							}
							else if (estado.trim()=="MANTENIMIENTO") {
								button[i].setBackground(new Color(102, 102, 102));
								button[i].setForeground(Color.BLACK);
								button[i].setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/btn-man.png")));
								
								label[i].setBackground(new Color(105, 105, 105));
								label[i].setForeground(new Color(255, 255, 255));
							}
							
							
								// MENU POPPU PA TODAS LS AHBITACIONES ================================================================
								popupMenu = new JPopupMenu();
								addPopup(button[i], popupMenu);
	
								mI = new JMenuItem("Seleccionar habitaci�n");
								mI.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										
									}
								});
								mI.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/check.png")));
								popupMenu.add(mI);
		
								JSeparator separator_1 = new JSeparator();
								popupMenu.add(separator_1);
								
								mL = new JMenuItem("Pasar a limpieza");
								mL.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										EstadoPoppu="LIMPIEZA";
										llenarPoppuEstado();
									}
								});
								mL.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/btn-lim.png")));
								popupMenu.add(mL);
	
								JSeparator separator_2 = new JSeparator();
								popupMenu.add(separator_2);
								
								mD = new JMenuItem("Pasar a disponible");
								mD.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										EstadoPoppu="DISPONIBLE";
										llenarPoppuEstado();
										//llenarButtons();
									}
								});
								mD.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/btn-dis.png")));
								popupMenu.add(mD);
								
								mO = new JMenuItem("Pasar a ocupado");
								mO.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										EstadoPoppu="OCUPADO";
										llenarPoppuEstado();
									}
								});
								mO.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/menu-user.png")));
								popupMenu.add(mO);
								
								JSeparator separator_3 = new JSeparator();
								popupMenu.add(separator_3);
									
									/*mModi = new JMenuItem("Editar Alquiler");
									mModi.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent arg0) {
											
										}
									});
									mModi.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/edit.png")));
									mModi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
									popupMenu.add(mModi);*/
								
								mEli = new JMenuItem("Eliminar alojamiento");
								if (estado.trim()=="RESERVADO") {
									mEli = new JMenuItem("Eliminar reserva");
								}
								mEli.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
									btnAnul.doClick();
									}
								});
								mEli.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/delete.png")));
								popupMenu.add(mEli);
						
								JSeparator separator_4 = new JSeparator();
								popupMenu.add(separator_4);
								
								mTar = new JMenuItem("Vista previa de tarifas");
								mTar.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										VistaTarifa v = new VistaTarifa();
										v.llenarTable(NRO,TIPO_HABITACION+" #"+ NRO );
										VistaTarifa.textNom.setText(NRO);
										VistaTarifa.textNom.selectAll();
										v.frame.setVisible(true);
									}
								});
								mTar.setIcon(new ImageIcon(VentanaControlHotel.class.getResource("/modelo/Images/Dollar_Green.png")));
								popupMenu.add(mTar);
								
								mI.setVisible(true);//SELECCIONAR
								mL.setVisible(false);//LIMPIEZA
								mD.setVisible(false);//DISPONIBLE
								mO.setVisible(false);//OCUPADO
								mEli.setVisible(false);//ELIMINADO
								separator_1.setVisible(true);
								separator_2.setVisible(false);
								separator_3.setVisible(false);
								
							if (estado.equals("DISPONIBLE")) {
								mL.setVisible(true);//LIMPIEZA
							}
							if (estado.equals("LIMPIEZA")) {
								mD.setVisible(true);//DISPONIBLE
							}
							if (estado.equals("ALQUILADO")||estado.equals("RESERVADO")) {
								mEli.setVisible(true);//ELIMINADO
								if (btnAlt.isEnabled()==false) {
									mEli.setVisible(false);//ELIMINADO
									mL.setVisible(true);//LIMPIEZA
									separator_2.setVisible(false);
								}
							}
							if (estado.equals("MANTENIMIENTO")) {
								mL.setVisible(true);//LIMPIEZA
							}
									
						}
					}
					TItem= TItem+ 1;
				}
				lbli8.setText("TOTAL HABITACIONES: "+ TItem+"");
				rs.close();
				statement.close();
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public void llenarCountEstados() {
		int totalItem=0;
		int TItem= 0;
		conexion = new ConexionDB();
		String consult="",estado="";
		try {
			String [] lista = {"DISPONIBLE","OCUPADO","ALQUILADO","RESERVADO","LIMPIEZA","MANTENIMIENTO"};
			for(String llenar:lista) {
				estado=llenar;
				if (cbTipoReg.getSelectedItem()=="TODOS"&&cbTipoHab.getSelectedItem()=="TODOS") {
					consult="Select * from HABITACION where EstadoHab='" + estado + "'";
				}
				if (cbTipoReg.getSelectedItem()!="TODOS"&&cbTipoHab.getSelectedItem()!="TODOS") {
					consult="Select * from HABITACION where EstadoHab='" + estado + "'and PisoHab='" + cbTipoReg.getSelectedItem().toString().trim() + "'and Tipo_Hab='" + cbTipoHab.getSelectedItem().toString().trim() + "'";
				}
				
				if (cbTipoReg.getSelectedItem()=="TODOS"&&cbTipoHab.getSelectedItem()!="TODOS") {
					consult="Select * from HABITACION where EstadoHab='" + estado + "'and Tipo_Hab='" + cbTipoHab.getSelectedItem().toString().trim() + "'";
				}
				if (cbTipoReg.getSelectedItem()!="TODOS"&&cbTipoHab.getSelectedItem()=="TODOS") {
					consult="Select * from HABITACION where EstadoHab='" + estado + "'and PisoHab='" + cbTipoReg.getSelectedItem().toString().trim() + "'";
				}
				Statement statement = conexion.gConnection().createStatement();
				ResultSet rs = statement.executeQuery(consult);
				totalItem=0;
				if (estado.trim()=="DISPONIBLE") {
					lbli1.setText(""+ totalItem+"   ");
				}
				else if (estado.trim()=="ALQUILADO") {
					lbli2.setText(""+ totalItem+"   ");
				}
				else if (estado.trim()=="RESERVADO") {
					lbli3.setText(""+ totalItem+"   ");
				}
				else if (estado.trim()=="LIMPIEZA") {
					lbli4.setText(""+ totalItem +"   ");
				}
				else if (estado.trim()=="MANTENIMIENTO") {
					lbli7.setText(""+ totalItem +"   ");
				}
				while (rs.next()==true){
					totalItem=totalItem+1;
					if (estado.trim()=="DISPONIBLE") {
						lbli1.setText(""+ totalItem+"   ");
					}
					else if (estado.trim()=="ALQUILADO") {
						lbli2.setText(""+ totalItem+"   ");
					}
					else if (estado.trim()=="RESERVADO") {
						lbli3.setText(""+ totalItem+"   ");
					}
					else if (estado.trim()=="LIMPIEZA") {
						lbli4.setText(""+ totalItem +"   ");
					}
					else if (estado.trim()=="MANTENIMIENTO") {
						lbli7.setText(""+ totalItem +"   ");
					}
					TItem++;
				}
				lbli8.setText("TOTAL HABITACIONES: "+ TItem+"");
				rs.close();
				statement.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	void centrar (){
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    
	}
	public void llenarPoppuEstado() {
		conexion = new ConexionDB();
		try {
			// ACTUALIZO EL ESTADO DE LA HABITACION :::::::::::::::::::::::::::::::::::::::::::::
			String s ="UPDATE HABITACION SET NumeroHab = ?,"
	                + "EstadoHab = ?"
	                + "WHERE NumeroHab = '"+ NRO +"'";
					
			PreparedStatement es = conexion.gConnection().prepareStatement(s);
			es.setInt(1, Integer.parseInt(NRO));
			es.setString(2, EstadoPoppu);
			es.executeUpdate();
			es.close();
			JOptionPane.showMessageDialog(null, "la habitaci�n paso a estado "+ EstadoPoppu ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			//llenarButtons();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void eliminarAlojamiento() {
		if (ID_ALQUILER == 0){
			JOptionPane.showMessageDialog(null, "Primero debe seleccionar el �tem...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			btnAnul.requestFocus();
			return;
		}
		String detalle ="";
		if (btnAnul.getText().equals("Anular Check-in")) {
			detalle="Desea deshacer el alojamiento";
		}else {
			detalle="Desea deshacer la reservaci�n";
		}	
		int respuesta = JOptionPane.showConfirmDialog (null, detalle, Menu.SOFTLE_HOTEL,
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (respuesta == JOptionPane.YES_OPTION) {
			conexion = new ConexionDB();
			try {
				// CONSULTO HABI
				String consul="Select * from ALQUILER as A ,DETALLE_A_HABITACION as DH where DH.Id_A=A.Id_A and DH.Id_A='" + ID_ALQUILER  + "'and A.EstadoA <>'" + 0 + "'";
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
					
					Statement ps =  conexion.gConnection().createStatement();
					String query ="Delete from ALQUILER where Id_A ='" + ID_ALQUILER  + "'";
					ps.execute(query);
					ps.close();
				}
				resultSet.close();
				statement.close();
				JOptionPane.showMessageDialog(null, "=======  Alojamiento eliminado =========="+ Menu.separador +
						"habitaciones desocupadas: " + item + Menu.separador +
						"el sistema genero su estado de LIMPIEZA..." ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				llenarButtons();

				//END CONSULTO HAB
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error al anular" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}

		}else if (respuesta == JOptionPane.NO_OPTION) {}
	}
	public void habilitarBtn() {
		btnAlt.setEnabled(true);
		btnOut.setEnabled(true);
		btnConsum.setEnabled(true);
		btnPag.setEnabled(true);
		btnAnul.setVisible(true);
		btnTarjetaReg.setEnabled(true);
	}	
	public void deshabilitarBtn() {
		btnAlt.setEnabled(false);
		btnOut.setEnabled(false);
		btnConsum.setEnabled(false);
		btnPag.setEnabled(false);
		btnAnul.setVisible(false);
		btnTarjetaReg.setEnabled(false);
		btnTranferencia.setEnabled(false);
		btnTarjetaSer.setEnabled(false);
	}
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
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

	@Override
	public void actionPerformed(ActionEvent evet) {
		if (evet.getSource().equals(cbTipoReg)) {
			llenarButtons();
		}
		if (evet.getSource().equals(cbTipoHab)) {
			llenarButtons();
		}
		if (evet.getSource().equals(btnAlt)||evet.getSource().equals(btnAltReserva)||evet.getSource().equals(btnAnul)||evet.getSource().equals(btnTranferencia)||
		    evet.getSource().equals(btnConsum)||evet.getSource().equals(btnOut)||evet.getSource().equals(btnPag)||evet.getSource().equals(btnTarjetaReg)||evet.getSource().equals(btnTarjetaSer)){
			if (VentanaLogin.ID_APETURA_CAJA==0){
				JOptionPane.showMessageDialog(null, "Primero debe aperturar su TURNO...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		// TODO Auto-generated method stub
		if (evet.getSource().equals(buttonActualizar)){
			btnOut.setEnabled(false);btnAlt.setEnabled(false);btnConsum.setEnabled(false);btnPag.setEnabled(false);btnAltReserva.setEnabled(false);btnTarjetaReg.setEnabled(false);
			btnAnul.setEnabled(false);btnTranferencia.setEnabled(false);btnTarjetaSer.setEnabled(false);
			llenarButtons();
		}
		if (evet.getSource().equals(buttonCerrarCaja)){
			 VentanaCierreCaja v = new VentanaCierreCaja();
			 v.frame.setVisible(true);
			 v.textMontoSol.requestFocus();
			 v.textMontoSol.selectAll();
		}
		if (evet.getSource().equals(btnAlt)){
			if (btnAlt.getText()=="Check-in") {
		    	// CONSULTO EL CODIGO DEL ALQUILER ::::::::::::::::::::::::::::::::
				try {
		
					Statement statement = conexion.gConnection().createStatement();
					ResultSet resultSet = statement.executeQuery("Select Id_A from ALQUILER order by Id_A desc limit 0,1");
					if (resultSet.next()== true) {
						int id=(Integer.parseInt(resultSet.getString("Id_A"))+1);
						ID_ALQUILER =id;
					}else{
						ID_ALQUILER =1;
					}
					statement.close();
				} catch (Exception e) {}
				// CONSULTO EL CODIGO DEL ALQUILER :::::::::::::::::::::::::::::::::
				VentanaAlquiler.MOD=0;// PERMITE REGISTRAR
				VentanaAlquiler ven= new VentanaAlquiler(ID_ALQUILER,ID_DETALLE,NRO);
				ven.cbTipoHabi.setSelectedItem(TIPO_HABITACION);
		    	ven.cbNroHab.setSelectedItem(NRO);
		    	ven.llenarAlquiler();
		    	VentanaAlquiler.frame.toFront();VentanaAlquiler.frame.setVisible(true);
			}
			if (btnAlt.getText()=="Check-in editar") {
				VentanaAlquiler.MOD=1;// PERMITE MODIFICAR
				VentanaAlquiler ven= new VentanaAlquiler(ID_ALQUILER,ID_DETALLE,NRO);
		    	//ven.llenarCbNroHab(); //CARGA LA HAB. DISPONIBLES
		    	ven.cbTipoHabi.setSelectedItem(TIPO_HABITACION);
		    	ven.cbNroHab.addItem(NRO);
		    	ven.cbNroHab.setSelectedItem(NRO);
		    	ven.llenarParaModificar();
		    	ven.mostrarImporte();
		    	VentanaAlquiler.frame.toFront();VentanaAlquiler.frame.setVisible(true);
			}
		}
		//========================================================RESERVAR
		if (evet.getSource().equals(btnAltReserva)){
			if (btnAltReserva.getText()=="Reservar") {
				try {
					
					Statement statement = conexion.gConnection().createStatement();
					ResultSet resultSet = statement.executeQuery("Select Id_A from ALQUILER order by Id_A desc limit 0,1");
					if (resultSet.next()== true) {
						int id=(Integer.parseInt(resultSet.getString("Id_A"))+1);
						ID_ALQUILER =id;
					}else{
						ID_ALQUILER =1;
					}
					statement.close();
				} catch (Exception e) {}
				// CONSULTO EL CODIGO DEL ALQUILER :::::::::::::::::::::::::::::::::
				VentanaAReservar.MOD=0;// PERMITE REGISTRAR
				VentanaAReservar ven= new VentanaAReservar(ID_ALQUILER,ID_DETALLE,NRO);
				ven.cbTipoHabi.setSelectedItem(TIPO_HABITACION);
		    	ven.cbNroHab.setSelectedItem(NRO);
		    	ven.llenarReserva();
		    	VentanaAReservar.frame.toFront();VentanaAReservar.frame.setVisible(true);
			}
			if (btnAltReserva.getText()=="Reserva-editar") {
				VentanaAReservar.MOD=1;// PERMITE MODIFICAR
				VentanaAReservar ven= new VentanaAReservar(ID_ALQUILER,ID_DETALLE,NRO);
		    	//ven.llenarCbNroHab(); //CARGA LA HAB. DISPONIBLES
		    	//ven.llenarCbNroHab(); //CARGA LA HAB. DISPONIBLES
		    	ven.cbTipoHabi.setSelectedItem(TIPO_HABITACION);
		    	ven.cbNroHab.addItem(NRO);
		    	ven.cbNroHab.setSelectedItem(NRO);
		    	ven.llenarParaModificar();
		    	ven.mostrarImporte();
		    	VentanaAReservar.frame.toFront();VentanaAReservar.frame.setVisible(true);
			}
		}
		
		//CAMBIO DE HABITACION :::::::::::::::::::::::::::::::::::::::::::::::::::::::
		if (evet.getSource().equals(btnTranferencia)){
				VentanaCambiarHabitacion.MOD=1;// PERMITE MODIFICAR
				VentanaCambiarHabitacion ven= new VentanaCambiarHabitacion(ID_ALQUILER,ID_DETALLE,NRO);
		    	//ven.llenarCbNroHab(); //CARGA LA HAB. DISPONIBLES
		    	ven.cbTipoHabi.setSelectedItem(TIPO_HABITACION);
		    	ven.cbNroHab.addItem(NRO);
		    	ven.cbNroHab.setSelectedItem(NRO);
		    	ven.llenarParaModificar();
		    	ven.mostrarImporte();
		    	VentanaCambiarHabitacion.frame.toFront();VentanaCambiarHabitacion.frame.setVisible(true);
		}
		
		if (evet.getSource().equals(btnConsum)){
			VentanaConsumo c = new VentanaConsumo(ID_ALQUILER,ID_DETALLE,Integer.parseInt(NRO),HABITACION);
  			c.lblT.setText("  " +HUESPED);
  			c.lblH.setText("  " +NRO+ " " +HABITACION);
  			c.lblA.setText("  "  +ACOMPANANTE);
  			VentanaConsumo.frame.toFront();
  			VentanaConsumo.frame.setVisible(true);
		}
		
		if (evet.getSource().equals(btnPag)){
			VentanaCuentaHuesped v = new VentanaCuentaHuesped(ID_ALQUILER);
			v.lblAlq.setText("HABITACION: "+NRO +" | " + HABITACION);
			v.lblAbonado.setText("TITULAR: "+ HUESPED);
			VentanaCuentaHuesped.frame.setVisible(true);
			v.textBus.requestFocus();
			}
		
		if (evet.getSource().equals(btnOut)){
			VentanaGenerarDocumento v  = new VentanaGenerarDocumento(ID_ALQUILER,Integer.parseInt(NRO),HABITACION,HUESPED);
			v.buttonSalir.setEnabled(true);
  			VentanaGenerarDocumento.frame.toFront();
  			VentanaGenerarDocumento.frame.show();
		    v.llenarTable(ID_C);
		    v.llenarParaModificarFacturacion();
		}

		if (evet.getSource().equals(btnAnul)){
			eliminarAlojamiento();
		}
		if (evet.getSource().equals(radioDos)){

		}
		if (evet.getSource().equals(buttonSalir)){
			frame.dispose();
		}
		if (evet.getSource().equals(btnTarjetaReg)){
			try {
				Map<String,Object> parameters = new HashMap<String,Object>();
				SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
				String FechaEntrada = form.format(Menu.fecha);
				parameters.put("PrtFecha",new String (FechaEntrada  +"   " + Menu.HORA.trim()));//12 HORAS HILO
				parameters.put("Prt_ID",new Integer(ID_ALQUILER));
				if (ESTADO_ALQUILER==1||ESTADO_ALQUILER==2) {//
					parameters.put("PrtEstado",new String("ALOJAMIENTO"));
				}
				if (ESTADO_ALQUILER==4) {//
					parameters.put("PrtEstado",new String("RESERVACION"));
				}
				parameters.put("PrtTurno",new String (VentanaLogin.TUR_CORTO+" "+ VentanaLogin.ID_APETURA_CAJA + " / "+ VentanaLogin.NOMBRE_USU_CORTO));
				AbstractJasperReports.createReport( conexion.gConnection(), Menu.URL+"Tarjeta_Registro.jasper",parameters );
				AbstractJasperReports.showViewerModal("Tarjeta de registro",500,700,true,"/modelo/Images/old-versions.png");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (evet.getSource().equals(btnTarjetaSer)){
			VistaConsumo v = new VistaConsumo(ID_ALQUILER,Integer.parseInt(NRO), HABITACION, HUESPED);
		    v.llenarTable(ID_C);
		    v.buttonPrint.doClick();
		    //VistaConsumo.frame.setVisible(true);
		}
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
					
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
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
	public void mousePressed(MouseEvent evet) {
		// TODO Auto-generated method stub
		for (int m=0;m < button.length;m++) {
			if (evet.getSource().equals(button[m])) {
				
				NRO="";DESCRIPCION="";
				NRO =button[m].getText().replace("H-",""); 
				llenarButtons();
				button[m].setBackground(new Color(255,255,0));
				button[m].setForeground(new Color(0,0,0));
				label[m].setBackground(new Color(255,255,0));
				label[m].setForeground(new Color(0,0,0));
				
				button[m].setFont(new Font("Tahoma", Font.BOLD, 11));
				
				
				String est="";
				try {
					HABITACION="";HUESPED="";ACOMPANANTE="";
					ID_ALQUILER= 0;ID_C = 0;DESCRIPCION="";ID_DETALLE=0;
					
					// CONSULTO ELESTADO DE LAS DISPONIBLES PARA ALQUILARLAS
					String consulta="Select * from HABITACION where NumeroHab='" + NRO + "'";
					Statement st = conexion.gConnection().createStatement();
					ResultSet rs = st.executeQuery(consulta);
					while (rs.next()==true){
						est=rs.getString("EstadoHab").trim();
						DESCRIPCION=rs.getString("DescripcionHab").trim();
						TIPO_HABITACION=(rs.getString("Tipo_Hab"));//TIPO HABITACION PARA CHECK IN
					}
					rs.close();
					st.close();
					
					// CONSULTO LOS ALQUILERES	
					//String consul="Select * from HABITACION,DETALLE_A_HABITACION,ALQUILER,CLIENTES where ALQUILER.Id_Cli=CLIENTES.Id_Cli and DETALLE_A_HABITACION.NumeroH = HABITACION.NumeroHab and DETALLE_A_HABITACION.Id_A = ALQUILER.Id_A and DETALLE_A_HABITACION.NumeroH='" + NRO + "'";
					//String consul="Select * from HABITACION,DETALLE_A_HABITACION,ALQUILER,CLIENTES where ALQUILER.Id_Cli=CLIENTES.Id_Cli and DETALLE_A_HABITACION.NumeroH = HABITACION.NumeroHab and DETALLE_A_HABITACION.Id_A = ALQUILER.Id_A and ALQUILER.EstadoA ='" + 1 + "'and DETALLE_A_HABITACION.NumeroH='" + NRO + "'";
					Statement s = conexion.gConnection().createStatement();
					//ResultSet r = s.executeQuery(consul);
					ResultSet r = s.executeQuery("Select * from HABITACION,DETALLE_A_HABITACION,ALQUILER,CLIENTES where ALQUILER.Id_Cli=CLIENTES.Id_Cli and DETALLE_A_HABITACION.NumeroH = HABITACION.NumeroHab and DETALLE_A_HABITACION.Id_A = ALQUILER.Id_A and ALQUILER.EstadoA <>'" + 0 + "'and DETALLE_A_HABITACION.NumeroH='" + NRO + "'order by ALQUILER.Id_A desc limit 0,1");
					while (r.next()==true){
						ID_ALQUILER= r.getInt("Id_A");
						ID_C = r.getInt("Id_Cli");
						ID_DETALLE= r.getInt("Id_D");
						
						HABITACION=(r.getString("DescripcionH"));
						HUESPED=(r.getString("NombreCli"));
						ACOMPANANTE=(r.getString("AcompananteH"));
						
						ESTADO_ALQUILER=(r.getInt("EstadoA"));//IMPRIME LA TARJETA DE REGISTRO
					
					}
					r.close();
					s.close();
					//System.out.println("Habitaci�n " + NRO +"  alquiler " + ID_ALQUILER + " cliente " + ID_C +" " +    HABITACION +" " +  HUESPED);
					
				} catch (Exception e) {}

				if (est.equals("DISPONIBLE")) {
					btnAlt.setEnabled(true);btnAlt.setText("Check-in");
					btnAltReserva.setEnabled(true);btnAltReserva.setVisible(true);btnAltReserva.setText("Reservar");
					btnTranferencia.setEnabled(false);
					
					btnConsum.setEnabled(false);btnConsum.setVisible(true);
					btnPag.setEnabled(false);
					btnAnul.setVisible(false);
					btnOut.setEnabled(false);btnOut.setVisible(true);
					btnTarjetaReg.setEnabled(false);btnTarjetaReg.setVisible(false);
					btnTarjetaSer.setEnabled(false);btnTarjetaSer.setVisible(false);
				}
				if (est.equals("ALQUILADO")) {
					btnAlt.setEnabled(true);btnAlt.setText("Check-in editar");
					btnAltReserva.setVisible(false);
					btnTranferencia.setEnabled(true);
					
					btnConsum.setEnabled(true);btnConsum.setVisible(true);
					btnPag.setEnabled(true);
					btnOut.setEnabled(true);btnOut.setVisible(true);
					btnAnul.setVisible(true);
					btnAnul.setText("Anular Check-in");
					btnTarjetaReg.setEnabled(true);btnTarjetaReg.setVisible(true);
					btnTarjetaSer.setEnabled(true);btnTarjetaSer.setVisible(true);
					try {
						// SI YA SE GENERO LA FACTURA Y NO SEDESOCUPO LA HABITACION , AGREGO UN POPPUP
						Statement s = conexion.gConnection().createStatement();
						ResultSet r = s.executeQuery("Select * from ALQUILER where Id_A='" + ID_ALQUILER + "' and EstadoA='" + 2 + "'");
						btnAnul.setEnabled(true);
						if (r.next()==true){
							btnAlt.setEnabled(false);
							btnTranferencia.setEnabled(false);
							
							btnConsum.setEnabled(false);
							btnAnul.setEnabled(false);
							btnTarjetaReg.setEnabled(false);btnTarjetaReg.setVisible(false);
							btnTarjetaSer.setEnabled(false);btnTarjetaSer.setVisible(false);
						}
						r.close();
						s.close();
					} catch (Exception e) {}		
				}
				if (est.equals("RESERVADO")) {
					btnAlt.setEnabled(true);
					btnAlt.setText("Check-in editar");
					btnAltReserva.setEnabled(true);btnAltReserva.setVisible(true);btnAltReserva.setText("Reserva-editar");
					btnTranferencia.setEnabled(true);
					
					btnConsum.setEnabled(false);btnConsum.setVisible(false);
					btnPag.setEnabled(true);
					btnOut.setEnabled(true);btnOut.setVisible(false);
					btnAnul.setEnabled(true);btnAnul.setVisible(true);
					btnAnul.setText("Anular-Reserva");
					btnTarjetaReg.setEnabled(true);btnTarjetaReg.setVisible(true);
					btnTarjetaSer.setEnabled(true);btnTarjetaSer.setVisible(true);
				}
				if (est.equals("LIMPIEZA")) {
					btnAlt.setEnabled(false);
					btnAltReserva.setEnabled(false);
					btnTranferencia.setEnabled(false);
					
					btnConsum.setEnabled(false);btnConsum.setVisible(true);
					btnPag.setEnabled(false);
					btnOut.setEnabled(false);btnOut.setVisible(true);
					btnAnul.setVisible(false);
					btnTarjetaReg.setEnabled(false);btnTarjetaReg.setVisible(false);
					btnTarjetaSer.setEnabled(false);btnTarjetaSer.setVisible(false);
				}
				if (est.equals("MANTENIMIENTO")) {
					btnAlt.setEnabled(false);
					btnAltReserva.setEnabled(false);
					btnTranferencia.setEnabled(false);
					
					btnConsum.setEnabled(false);btnConsum.setVisible(true);
					btnPag.setEnabled(false);
					btnOut.setEnabled(false);btnOut.setVisible(true);
					btnAnul.setVisible(false);
					btnTarjetaReg.setEnabled(false);btnTarjetaReg.setVisible(false);
					btnTarjetaSer.setEnabled(false);btnTarjetaSer.setVisible(false);
					}
				if (est.equals("ALQUILADO")||est.equals("RESERVADO")) {
					if (ID_ALQUILER==0){
						btnAlt.setEnabled(false);
						btnConsum.setEnabled(false);
						btnPag.setEnabled(false);
						btnOut.setEnabled(false);
						btnAnul.setVisible(false);
						}
					}
				}

			}
		if (btnAlt.getText()=="Check-in") {
			btnAlt.setToolTipText("Gestionar alojamiento");
			}
		if (btnAlt.getText()=="Check-in editar") {
			btnAlt.setToolTipText("Modificar alojamiento");
			}
		
		if (btnAltReserva.getText()=="Reservar") {
			btnAltReserva.setToolTipText("Gestionar reserva");
			}
		if (btnAltReserva.getText()=="Reserva-editar") {
			btnAltReserva.setToolTipText("Modificar reserva");
			}
		
		if (btnAnul.getText()=="Anular Check-in") {
			btnAnul.setToolTipText("Eliminar alojamiento");
			}
		if (btnAnul.getText()=="Anular-Reserva") {
			btnAnul.setToolTipText("Eliminar reserva");
			}
	}

	//void mostrarDesocupar() {
	TimerTask timerTask = new TimerTask(){
        public void run() {
        Random rand = new Random();
       
        Color c =  new Color(204, 102, 102);
        Color l = Color.BLACK; 
        Color foreButton = Color.RED; 
        
        int x = rand.nextInt(2) + 1;
           switch (x) {
            //case 1 : c = new Color(127, 255, 212);
            		//break;//CLARO
            case 1 : c = new Color(0, 255, 255);
            		l = Color.BLACK;
            		break;//CELESTE
            case 2 : c = new Color(204, 102, 102);
            		l = Color.WHITE;
            		break;//ROJO  
           }
		try {
			String consul="Select * from HABITACION where EstadoHab ='" + "ALQUILADO" + "'";
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(consul);
			while (resultSet.next()==true){
				// CONSULTO LOS ALQUILERES	
				String con="Select * from DETALLE_A_HABITACION,ALQUILER where DETALLE_A_HABITACION.Id_A = ALQUILER.Id_A and ALQUILER.EstadoA <>'" + 0 + "'and DETALLE_A_HABITACION.NumeroH='" + resultSet.getString("NumeroHab").trim() + "'order by DETALLE_A_HABITACION.Id_A desc limit 0,1";
				Statement s = conexion.gConnection().createStatement();
				ResultSet r = s.executeQuery(con);
				
				//int HOR_ENTRA=0;
				int HOR_SAL=0;
				
				String FEntrada,FSalida,Factual="",HOR_ACT="";
				//int FE=0,FS=0,FA=0;
				int HA=0;
				if (r.next()==true){
					//HOR_ENTRA= (r.getInt("HoraIngresoH"));
					HOR_SAL= (r.getInt("HoraSalidaH"));
					
					FEntrada= (r.getString("FechaIngresoH"));
					FSalida=(r.getString("FechaSalidaH"));
					//Factual=(Menu.date);

					FEntrada=FEntrada.replaceAll("-", "");
					FSalida=FSalida.replaceAll("-", "");
					Factual=Menu.date.replaceAll("-", "");
					HOR_ACT=HORA.replaceAll("[:|.|AM|PM|am|pm]", "").trim();
					HOR_ACT = HOR_ACT.substring(0,2);//extraer una parte de una cadena
					
					//FE=Integer.parseInt(FEntrada);
					//FS=Integer.parseInt(FSalida);
					//FA=Integer.parseInt(Factual);
					HA=Integer.parseInt(HOR_ACT);
					
					int añoA=Integer.parseInt(Factual.substring(4, 8));
					int añoS=Integer.parseInt(FSalida.substring(4, 8));
					
					int mesA=Integer.parseInt(Factual.substring(2, 4));
					int mesS=Integer.parseInt(FSalida.substring(2, 4));
					
					int diaA=Integer.parseInt(Factual.substring(0, 2));
					int diaS=Integer.parseInt(FSalida.substring(0, 2));
					
					//System.out.println( "entrada:"+añoA +" "+ mesA +" "+diaA +" salisa:"+  añoS +" "+ mesS +" "+diaS +" Hab:"+r.getInt("NumeroH") +" "+ HA +" "+ HOR_SAL);
					if (añoA >= añoS){
						if (mesA >= mesS){
							if (diaA >= diaS){
								if (HA >= HOR_SAL){
									
									for(int i=1; i<button.length; i++) {
										String NRO=button[i].getText().replace("H-", "");
										if (r.getString("NumeroH").equals(NRO.trim())){//if (r.getInt("NumeroH")==i){
											if (ID_ALQUILER>0){
											}else{
									            button[i].setBackground(c);
												button[i].setForeground(foreButton);
												label[i].setBackground(c);
												label[i].setForeground(l);
												
												//button[i].setToolTipText("F.Salida:  "+r.getString("FechaSalidaH") + " | "+ r.getString("HoraSalidaH"));
											}
										}
									}
								}
							
							}
						}	
					}
				}

				r.close();
				s.close();

			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {}
		
        }
	 };
	//}
	@Override
	public void mouseReleased(MouseEvent evet) {
		// TODO Auto-generated method stub

	}
	
}
