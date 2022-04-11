package modelo.Presentacion;

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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;
import java.awt.Toolkit;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class VentanaAlquiler implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	
	private static ConexionDB conexion;

	public static JDialog frame;
	public JPanel 				panelDto,panelPie;

	public static JTextField 	textUser,textCId,textCNom,textDscCli;
	public JTextField			textPrecioH,textDiasH,textPers,textAcompana;
	
	public static String        Direccion;
	private JLabel 				lbl1,lbl2,lbl3,lbl4,lbl5,lbl6,lbl7,lbl8,lbl9,lblM;
	private JLabel 				lbl11,lbl13,lbl14,lbl15,lbl16,lbl17,lbl18,lbl19,lbl21,lbl23,lbl22,lblE,lblS,lbl24,lbl25,lbl26,lbl27;
	public  JButton				buttonGrabar,buttonSalir,buttonCliente,buttonVerPrecios;
	private JComboBox<String> 	cbTipoTar,cbA,cbN,cbB,cbAR,cbPlan,cbTipoReg;
	public  JComboBox<String>   cbTipoHabi,cbNroHab;
	private JFormattedTextField textFormatImporte,textFormatSubTotal,textFormatDsct,textFormatTotal;
	
	private JDateChooser chooserIngreso,chooserSalida,chooserReg;
	private static String dateEmision;
	private JScrollPane scrollArea,scrollAreaP,scrollObs;
	private JCheckBox chckbxDesc;
	
	public JTextArea textArea,textAreaP,textObservacion;
	public Integer totalitems;
	public static int CONTAR_VENTANA_ALQUILER=0;
	public int ID;
	public int DIAS=0;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup2 = new ButtonGroup();
	private JRadioButton rdbtnEfectivo,rdbtnCredito,radioDos,radioUno;
	
	private String FechaEntrada="",FechaSalida="";
	private int CONT=0;
	
	public static int MOD;
	
	public static float DSCT_CLI=0;
	public static String TIP_DSCT_CLI="";
	private JSpinner  spinnerE,spinnerS;
	SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss a");
	
	private String MPagoH="";
	
	float IMPORTE=0,SUBTOTAL=0,TOTAL=0;
	float temp_sub=0,temp_tot=0;
	
	private int COD_HOSPEDAJE;int COD_DETALLE;String NRO_HABITACION;
	public VentanaAlquiler(int cOD_HOSPEDAJE,int cOD_DETALLE,String nRO_HABITACION) {
		super();
		conexion = new ConexionDB();
		
		COD_HOSPEDAJE = cOD_HOSPEDAJE;
		COD_DETALLE = cOD_DETALLE;
		NRO_HABITACION = nRO_HABITACION;
		frameAlquiler();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		crearOtros();
		horaSpinner();
		
		chooserReg.setDate(Menu.fecha);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dateEmision = form.format(chooserReg.getDate());
		
		chooserIngreso.setDate(Menu.fecha);
		chooserSalida.setDate(null);
		

		FechaEntrada = Menu.formatoDiaMesAño.format(chooserIngreso.getDate());
		llenarCbTipoHabitacion();
		llenarCbPersonas();
		/*if (VentanaControlHotel.radioDos.isSelected()) {
			llenarCbNroHab(); //CARGA LA HAB. DISPONIBLES
		}*/
		
		if (radioDos.isSelected()) {
			llenarCbNroHab(); //CARGA LA HAB. DISPONIBLES
		}
		
		MPagoH="X";//CONTADO
	}
	public void frameAlquiler() {
		frame = new JDialog();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				conexion.DesconectarDB();
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				conexion.DesconectarDB();
			}
		});
		frame.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent arg0) {
				//cbTipoTar.requestFocusInWindow();
			}
			public void windowLostFocus(WindowEvent arg0) {
			}
		});
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaAlquiler.class.getResource("/modelo/Images/document_green.png")));
		frame.setTitle("Entradas o check-in"); 
		frame.setBounds(100, 100, 916, 384);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setModal(true);
	}
	public void crearPanel() {
		panelDto = new JPanel();
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), ":::: | Nuevo | :::: REGISTRO: " + Menu.formatid_9.format(this.COD_HOSPEDAJE), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(121, 2, 0)));
		panelDto.setBounds(10, 11, 890, 215);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		panelPie= new JPanel();
		panelPie.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "" , TitledBorder.LEADING, TitledBorder.TOP, null, new Color(121, 2, 0)));
		panelPie.setBounds(11, 228, 888, 120);
		frame.getContentPane().add(panelPie);
		panelPie.setLayout(null);
	}
	void horaSpinner() {
		 SpinnerDateModel model = new SpinnerDateModel();
		//model.setCalendarField(Calendar.MINUTE);
		spinnerE= new JSpinner();
		spinnerE.setModel(model);
		spinnerE.setEditor(new JSpinner.DateEditor(spinnerE, "hh:mm:ss a"));
		spinnerE.setBounds(688, 42, 91, 20);
		spinnerE.addFocusListener(this);
		spinnerE.addKeyListener(this);
		spinnerE.addPropertyChangeListener(this);
		((JSpinner.DefaultEditor)spinnerE.getEditor()).getTextField().setEditable(false);
		// COLOR
		Component c = spinnerE.getEditor().getComponent(0);
		//c.setBackground(Color.BLUE);
		c.setForeground(new Color(0, 139, 139));
		// END COLOR
		panelDto.add(spinnerE);
		
		
		
		 SpinnerDateModel mod = new SpinnerDateModel();
		//model.setCalendarField(Calendar.MINUTE);
		spinnerS= new JSpinner();
		spinnerS.setModel(mod);
		spinnerS.setEditor(new JSpinner.DateEditor(spinnerS, "hh:mm:ss a"));
		spinnerS.setBounds(782, 42, 91, 20);
		spinnerS.addFocusListener(this);
		spinnerS.addKeyListener(this);
		spinnerS.addPropertyChangeListener(this);
		((JSpinner.DefaultEditor)spinnerS.getEditor()).getTextField().setEditable(false);
		// COLOR
		Component s = spinnerS.getEditor().getComponent(0);
		//s.setBackground(Color.BLUE);
		s.setForeground(new Color(0, 139, 139));
		// END COLOR
		
		panelDto.add(spinnerS);
		try {
			DecimalFormat format = new DecimalFormat("00");
			String ampm = "PM";
            String tim=" "+format.format(01) +":"+ format.format(00) +":"+ format.format(00) +" "+ ampm;
            Time time = new Time(formatTime.parse(tim).getTime());
            spinnerS.setValue(time);
		} catch (ParseException e) {e.printStackTrace();}
	}
	
	public void crearOtros() {
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(135, 206, 235));
		separator.setBounds(83, 73, 790, 8);
		panelDto.add(separator);
		textCId.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(0, 255, 255), new Color(240, 240, 240), new Color(255, 222, 173)));
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(135, 206, 250));
		separator_1.setBounds(83, 124, 790, 8);
		panelDto.add(separator_1);
		
		JSeparator separator2 = new JSeparator();
		separator2.setBackground(Color.RED);
		separator2.setForeground(new Color(135, 206, 235));
		separator2.setBounds(86, 63, 158, 2);
		panelDto.add(separator2);
		
		chooserIngreso= new JDateChooser();
		chooserIngreso.setDateFormatString("dd-MMM-yyyy");
		chooserIngreso.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		//chooserIngreso.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)chooserIngreso.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)chooserIngreso.getDateEditor()).setEditable(false);
		//chooserIngreso.getDateEditor().setEnabled(false);
		//chooserIngreso.getCalendarButton().addActionListener(this);
		chooserIngreso.addPropertyChangeListener(this);
		chooserIngreso.setBounds(598, 92, 92, 23);
		//chooserChooser.setIcon(new ImageIcon(VentanaAlquilarReservar.class.getResource("/modelo/Images/date.png")));
		panelDto.add(chooserIngreso);
		
		chooserSalida = new JDateChooser();
		chooserSalida.setDateFormatString("dd-MMM-yyyy");
		chooserSalida.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		//chooserSalida.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)chooserSalida.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		((JTextField)chooserSalida.getDateEditor().getUiComponent()).setEditable(false);
		//chooserSalida.getDateEditor().setEnabled(false);
		chooserSalida.setBounds(690, 92, 92, 23);
		//dateSalida.getCalendarButton().addActionListener(this);
		chooserSalida.addPropertyChangeListener(this);
		//dateChooser_1.setIcon(new ImageIcon(VentanaAlquilarReservar.class.getResource("/modelo/Images/date.png")));
		panelDto.add(chooserSalida);

		chckbxDesc = new JCheckBox("Desc");
		chckbxDesc.setBounds(618, 72, 19, 15);
		chckbxDesc.addActionListener(this);
		panelPie.add(chckbxDesc);
		
		rdbtnEfectivo = new JRadioButton("Contado");
		rdbtnEfectivo.setSelected(true);
		buttonGroup.add(rdbtnEfectivo);
		rdbtnEfectivo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnEfectivo.setBounds(400, 88, 66, 16);
		rdbtnEfectivo.addActionListener(this);
		rdbtnEfectivo.addKeyListener(this);
		panelPie.add(rdbtnEfectivo);
		
		rdbtnCredito = new JRadioButton("Por Pagar");
		buttonGroup.add(rdbtnCredito);
		rdbtnCredito.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnCredito.setBounds(470, 88, 72, 16);
		rdbtnCredito.addActionListener(this);
		rdbtnCredito.addKeyListener(this);
		panelPie.add(rdbtnCredito);
		
		
		radioDos = new JRadioButton("GRUPAL");
		radioDos.setForeground(new Color(0, 0, 205));
		radioDos.setFont(new Font("Tahoma", Font.BOLD, 12));
		radioDos.setSize(75, 19);
		radioDos.setLocation(174, 43);
		radioDos.addActionListener(this);
		buttonGroup2.add(radioDos);
		panelDto.add(radioDos);
		
		radioUno = new JRadioButton("INDIVIDUAL");
		radioUno.setSelected(true);
		radioUno.setOpaque(true);
		radioUno.setForeground(new Color(0, 0, 205));
		radioUno.setFont(new Font("Tahoma", Font.BOLD, 12));
		radioUno.setSize(98, 19);
		radioUno.setLocation(80, 43);
		radioUno.addActionListener(this);
		buttonGroup2.add(radioUno);
		panelDto.add(radioUno);
		
		chooserReg= new JDateChooser();
		chooserReg.setDateFormatString("dd-MMM-yyyy");
		chooserReg.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		chooserReg.setFont(new Font("SansSerif", Font.PLAIN, 11));
		((JTextField)chooserReg.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		((JTextField)chooserReg.getDateEditor()).setEditable(false);
		//chooserReg.getDateEditor().setEnabled(false);
		//chooserReg.getCalendarButton().addActionListener(this);
		chooserReg.addPropertyChangeListener(this);
		chooserReg.setBounds(285, 88, 92, 21);
		chooserReg.setIcon(new ImageIcon(VentanaAlquiler.class.getResource("/modelo/Images/date.png")));
		panelPie.add(chooserReg);
		
	}
	public void crearLabels() {
		lblM = new JLabel("Modo registro:");
		lblM.setSize(70, 20);
		lblM.setLocation(11, 42);
		lblM.setOpaque(true);
		lblM.setHorizontalAlignment(SwingConstants.LEFT);
		lblM.setForeground(Color.DARK_GRAY);
		lblM.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblM.setBackground(SystemColor.menu);
		panelDto.add(lblM);
		
		lbl1= new JLabel("Nro:");
		lbl1.setForeground(Color.DARK_GRAY);
		lbl1.setBounds(250, 78, 60, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lblE= new JLabel("Hora Entrada:");
		lblE.setForeground(Color.DARK_GRAY);
		lblE.setBounds(688, 29, 91, 14);
		panelDto.add(lblE);
		lblE.setHorizontalAlignment(SwingConstants.RIGHT);
		lblE.setFont(Menu.fontLabel);
		
		lblS= new JLabel("Hora Salida:");
		lblS.setForeground(Color.DARK_GRAY);
		lblS.setBounds(782, 29, 91, 14);
		panelDto.add(lblS);
		lblS.setHorizontalAlignment(SwingConstants.RIGHT);
		lblS.setFont(Menu.fontLabel);
		
		
		lbl2= new JLabel("H. Titular:");
		lbl2.setForeground(Color.DARK_GRAY);
		lbl2.setBounds(283, 25, 55, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Habitaci�n:");
		lbl3.setForeground(Color.DARK_GRAY);
		lbl3.setBounds(10, 96, 68, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl23= new JLabel("Tipo:");
		lbl23.setForeground(Color.DARK_GRAY);
		lbl23.setBounds(180, 78, 68, 14);
		panelDto.add(lbl23);
		lbl23.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl23.setFont(Menu.fontLabel);
		
		lbl6= new JLabel("Fec. Entrada:");
		lbl6.setForeground(Color.DARK_GRAY);
		lbl6.setBounds(594, 78, 81, 14);
		panelDto.add(lbl6);
		lbl6.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl6.setFont(Menu.fontLabel);
		
		lbl7= new JLabel("Fec. Salida:");
		lbl7.setForeground(Color.DARK_GRAY);
		lbl7.setBounds(695, 78, 68, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl14 = new JLabel("Tipo tarifa:");
		lbl14.setForeground(Color.DARK_GRAY);
		lbl14.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl14.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl14.setBounds(392, 78, 60, 14);
		panelDto.add(lbl14);
		
		lbl15 = new JLabel("Precio S/.:");
		lbl15.setForeground(Color.DARK_GRAY);
		lbl15.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl15.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl15.setBounds(524, 78, 60, 14);
		panelDto.add(lbl15);
		
		lbl16 = new JLabel("Dias:");
		lbl16.setForeground(Color.DARK_GRAY);
		lbl16.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl16.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl16.setBounds(773, 78, 38, 14);
		panelDto.add(lbl16);
		
		lbl17 = new JLabel("Descripci�n:");
		lbl17.setForeground(Color.DARK_GRAY);
		lbl17.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl17.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl17.setBounds(10, 128, 68, 14);
		panelDto.add(lbl17);

		lbl19 = new JLabel("Acompa�ante: ");
		lbl19.setForeground(Color.DARK_GRAY);
		lbl19.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl19.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl19.setBounds(4, 187, 81, 14);
		panelDto.add(lbl19);
		
		lbl4= new JLabel("Adultos:");
		lbl4.setForeground(Color.DARK_GRAY);
		lbl4.setBounds(87, 74, 48, 14);
		panelPie.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);
		
		lbl5= new JLabel("ni�os:");
		lbl5.setForeground(Color.DARK_GRAY);
		lbl5.setBounds(145, 74, 36, 14);
		panelPie.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);
		
		lbl9= new JLabel("Bebes:");
		lbl9.setForeground(Color.DARK_GRAY);
		lbl9.setBounds(194, 74, 38, 14);
		panelPie.add(lbl9);
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Pers.:");
		lbl8.setForeground(Color.DARK_GRAY);
		lbl8.setBounds(236, 74, 38, 14);
		panelPie.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		
		lbl11= new JLabel("Importe:");
		lbl11.setForeground(Color.DARK_GRAY);
		lbl11.setBounds(815, 78, 58, 14);
		panelDto.add(lbl11);
		lbl11.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl11.setFont(Menu.fontLabel);
		
		lbl21= new JLabel("Sub total:");
		lbl21.setForeground(Color.DARK_GRAY);
		lbl21.setBounds(553, 74, 64, 14);
		panelPie.add(lbl21);
		lbl21.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl21.setFont(Menu.fontLabel);
		
		lbl18= new JLabel("Dscto:");
		lbl18.setForeground(Color.DARK_GRAY);
		lbl18.setBounds(638, 74, 40, 14);
		panelPie.add(lbl18);
		lbl18.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl18.setFont(Menu.fontLabel);
		
		lbl13= new JLabel("Total:");
		lbl13.setForeground(Color.DARK_GRAY);
		lbl13.setBounds(680, 74, 64, 14);
		panelPie.add(lbl13);
		lbl13.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl13.setFont(Menu.fontLabel);
		
		
		lbl22= new JLabel("Fecha Reg.:");
		lbl22.setForeground(Color.DARK_GRAY);
		lbl22.setBounds(298, 74, 77, 14);
		panelPie.add(lbl22);
		lbl22.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl22.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		lbl24= new JLabel("T. Operaci�n:");
		lbl24.setForeground(Color.DARK_GRAY);
		lbl24.setBounds(644, 189, 91, 14);
		panelDto.add(lbl24);
		lbl24.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl24.setFont(Menu.fontLabel);
		
		lbl25= new JLabel("Pie Reg.:");
		lbl25.setForeground(Color.DARK_GRAY);
		lbl25.setBounds(13, 90, 64, 14);
		panelPie.add(lbl25);
		lbl25.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl25.setFont(Menu.fontLabel);
		
		lbl26 = new JLabel("Observaci\u00F3n: ");
		lbl26.setForeground(Color.DARK_GRAY);
		lbl26.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl26.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl26.setBounds(1, 8, 81, 14);
		panelPie.add(lbl26);
		
		lbl27= new JLabel("Planes:");
		lbl27.setForeground(Color.DARK_GRAY);
		lbl27.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl27.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl27.setBounds(455, 78, 60, 14);
		panelDto.add(lbl27);
	}

	public void crearTextFields(){
		textUser = new JTextField("");
		textUser.setHorizontalAlignment(SwingConstants.RIGHT);
		textUser.setColumns(10);
		textUser.setForeground(new Color(25, 25, 112));
		textUser.setEditable(false);
		textUser.setBackground(new Color(240, 240, 240));
		textUser.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(200, 200, 200)));
		textUser.setBounds(718, 7, 155, 13);
		panelDto.add(textUser);
		
		textCId = new JTextField("");
		textCId.setEditable(false);
		textCId.setColumns(10);
		textCId.setForeground(new Color(0, 0, 128));
		textCId.setBounds(288, 40, 50, 22);
		panelDto.add(textCId);
		textCId.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(0, 255, 255), new Color(240, 240, 240), new Color(227, 227, 227)));
		
		textCNom = new JTextField("");
		textCNom.setEditable(false);
		textCNom.setColumns(10);
		textCNom.setForeground(new Color(0, 0, 128));
		textCNom.setBounds(340, 40, 286, 22);
		textCNom.addActionListener(this);
		panelDto.add(textCNom);
		textCNom.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(0, 255, 255), new Color(240, 240, 240), SystemColor.controlHighlight));
		
		textDscCli = new JTextField("");
		textDscCli.setHorizontalAlignment(SwingConstants.RIGHT);
		textDscCli.setForeground(new Color(0, 0, 128));
		textDscCli.setEditable(false);
		textDscCli.setColumns(10);
		textDscCli.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(192, 192, 192), new Color(0, 255, 255), new Color(240, 240, 240), SystemColor.controlHighlight));
		textDscCli.setBounds(630, 40, 55, 22);
		panelDto.add(textDscCli);
		
		textPrecioH = new JTextField("0.00");
		textPrecioH.setHorizontalAlignment(SwingConstants.CENTER);
		textPrecioH.setToolTipText("Precio de la habitaci�n");
		textPrecioH.setForeground(new Color(0, 102, 102));
		textPrecioH.setColumns(10);
		textPrecioH.setBackground(new Color(255, 255, 255));
		textPrecioH.setBounds(524, 93, 55, 21);
		textPrecioH.addActionListener(this);
		textPrecioH.addKeyListener(this);
		textPrecioH.addFocusListener(this);
		panelDto.add(textPrecioH);
		
		textDiasH = new JTextField();
		textDiasH.setHorizontalAlignment(SwingConstants.CENTER);
		textDiasH.setToolTipText("Dias alojar");
		textDiasH.setForeground(new Color(0, 102, 102));
		textDiasH.setEditable(false);
		textDiasH.setColumns(10);
		textDiasH.setBackground(new Color(255, 255, 255));
		textDiasH.setBounds(783, 93, 30, 21);
		textDiasH.addActionListener(this);
		textDiasH.addKeyListener(this);
		panelDto.add(textDiasH);

		scrollArea= new JScrollPane();
		scrollArea.setBounds(83, 130, 440, 53);
		panelDto.add(scrollArea);
		
		textArea = new JTextArea();
		textArea.setBackground(new Color(240, 240, 240));
		textArea.setEditable(false);
		scrollArea.setViewportView(textArea);
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textArea.setForeground(new Color(128, 128, 128));
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		scrollArea.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(211, 211, 211)));
		
		scrollAreaP= new JScrollPane();
		scrollAreaP.setBounds(524, 130, 349, 53);
		panelDto.add(scrollAreaP);
		
		textAreaP = new JTextArea();
		textAreaP.setBackground(new Color(240, 240, 240));
		textAreaP.setEditable(false);
		scrollAreaP.setViewportView(textAreaP);
		textAreaP.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textAreaP.setForeground(new Color(128, 128, 128));
		textAreaP.setWrapStyleWord(true);
		textAreaP.addFocusListener(this);
		scrollAreaP.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(211, 211, 211)));
		
		textAcompana = new JTextField("");
		textAcompana.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textAcompana.setColumns(10);
		textAcompana.setForeground(new Color(0, 0, 0));
		textAcompana.setBackground(new Color(255, 255, 255));
		textAcompana.setBounds(83, 185, 790, 20);
		textAcompana.addKeyListener(this);
		textAcompana.addActionListener(this);
		textAcompana.addFocusListener(this);
		panelDto.add(textAcompana);
		//textAcompana.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(128, 128, 128), new Color(0, 255, 255), new Color(255, 228, 196), new Color(255, 228, 196)));
		textArea.setEditable(false);
		
		scrollObs= new JScrollPane();
		scrollObs.setBounds(83, 6, 790, 54);
		panelPie.add(scrollObs);
		textObservacion = new JTextArea();
		textObservacion.setBackground(new Color(255, 255, 255));
		scrollObs.setViewportView(textObservacion);
		textObservacion.setFont(new Font("Tahoma", Font.PLAIN, 10));
		textObservacion.setForeground(Menu.textColorForegroundInactivo);
		textObservacion.setWrapStyleWord(true);
		textObservacion.addKeyListener(this);
		textObservacion.addFocusListener(this);
		scrollObs.setBorder(new LineBorder(new Color(72, 209, 204), 1, true));
		//textObservacion.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(128, 128, 128), new Color(0, 255, 255), new Color(255, 228, 196), new Color(255, 228, 196)));
		
		textPers = new JTextField("1");
		textPers.setHorizontalAlignment(SwingConstants.CENTER);
		textPers.setToolTipText("Id");
		textPers.setForeground(new Color(0, 102, 102));
		textPers.setEditable(false);
		textPers.setColumns(10);
		textPers.setBackground(Color.WHITE);
		textPers.setBounds(236, 88, 39, 21);
		panelPie.add(textPers);
		

		textFormatImporte = new JFormattedTextField();
		textFormatImporte.setEditable(false);
		textFormatImporte.setBackground(new Color(255, 255, 255));
		textFormatImporte.setText("25.00");
		textFormatImporte.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatImporte.setBounds(815, 93, 58, 21);
		panelDto.add(textFormatImporte);
		
		textFormatSubTotal = new JFormattedTextField();
		textFormatSubTotal.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFormatSubTotal.setEditable(false);
		textFormatSubTotal.setText("0.00");
		textFormatSubTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatSubTotal.setBounds(553, 87, 64, 21);
		textFormatSubTotal.addActionListener(this);
		textFormatSubTotal.addPropertyChangeListener(this);
		textFormatSubTotal.addKeyListener(this);
		panelPie.add(textFormatSubTotal);
		textFormatSubTotal.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(128, 128, 128), new Color(0, 255, 255), new Color(255, 228, 196), new Color(255, 228, 196)));
		
		textFormatDsct = new JFormattedTextField();
		textFormatDsct.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFormatDsct.setEnabled(false);
		textFormatDsct.setText("0.00");
		textFormatDsct.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatDsct.setBounds(620, 87, 58, 21);
		textFormatDsct.addActionListener(this);
		textFormatDsct.addPropertyChangeListener(this);
		textFormatDsct.addKeyListener(this);
		textFormatDsct.addFocusListener(this);
		panelPie.add(textFormatDsct);
		textFormatDsct.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(128, 128, 128), new Color(0, 255, 255), new Color(255, 228, 196), new Color(255, 228, 196)));
		
		textFormatTotal = new JFormattedTextField();
		textFormatTotal.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textFormatTotal.setEditable(false);
		textFormatTotal.setText("0.00");
		textFormatTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatTotal.setBounds(680, 87, 64, 21);
		panelPie.add(textFormatTotal);
		textFormatTotal.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(128, 128, 128), new Color(0, 255, 255), new Color(255, 228, 196), new Color(255, 228, 196)));
	}
	public void crearComboBox() { 
		cbTipoHabi = new JComboBox<>();
		cbTipoHabi.setBounds(83, 93, 165, 21);
		cbTipoHabi.setFont(Menu.fontText);
		cbTipoHabi.removeAllItems();
		cbTipoHabi.addActionListener(this);
		cbTipoHabi.addPropertyChangeListener(this);
		cbTipoHabi.addFocusListener(this);
		cbTipoHabi.addKeyListener(this);
		panelDto.add(cbTipoHabi);
		
		cbNroHab = new JComboBox<>();
		cbNroHab.setBounds(250, 93, 60, 21);
		cbNroHab.setFont(Menu.fontText);
		cbNroHab.addActionListener(this);
		cbNroHab.addPropertyChangeListener(this);
		cbNroHab.addFocusListener(this);
		cbNroHab.addKeyListener(this);
		panelDto.add(cbNroHab);
		
		cbTipoTar = new JComboBox<>();
		cbTipoTar.setBounds(312, 93, 141, 21);
		cbTipoTar.setFont(Menu.fontText);
		cbTipoTar.removeAllItems();
		cbTipoTar.addActionListener(this);
		cbTipoTar.addPropertyChangeListener(this);
		cbTipoTar.addFocusListener(this);
		cbTipoTar.addKeyListener(this);
		panelDto.add(cbTipoTar);
		
		cbA = new JComboBox<String>();
		cbA.setBackground(new Color(60, 179, 113));
		cbA.setBounds(87, 88, 47, 21);
		cbA.addActionListener(this);
		cbA.addFocusListener(this);
		cbA.addKeyListener(this);
		panelPie.add(cbA);
		cbA.setFont(Menu.fontText);
		
		cbN = new JComboBox<String>();
		cbN.setBounds(137, 88, 47, 21);
		cbN.addActionListener(this);
		cbN.addFocusListener(this);
		cbN.addKeyListener(this);
		panelPie.add(cbN);
		cbN.setFont(Menu.fontText);

		cbB = new JComboBox<String>();
		cbB.setBounds(185, 88, 47, 21);
		cbB.addActionListener(this);
		cbB.addFocusListener(this);
		cbB.addKeyListener(this);
		panelPie.add(cbB);
		cbB.setFont(Menu.fontText);
		
		cbAR = new JComboBox<String>();
		cbAR.setOpaque(true);
		cbAR.setFont(Menu.fontText);
		cbAR.addActionListener(this);
		cbAR.setBounds(737, 185, 136, 21);
		cbAR.addFocusListener(this);
		cbAR.addKeyListener(this);
		panelDto.add(cbAR);
		cbAR.setVisible(false);
		
		cbPlan = new JComboBox<String>();
		cbPlan.setOpaque(true);
		cbPlan.setFont(Menu.fontText);
		cbPlan.setBounds(455, 93, 67, 21);
		cbPlan.addActionListener(this);
		cbPlan.addPropertyChangeListener(this);
		cbPlan.addFocusListener(this);
		cbPlan.addKeyListener(this);
		panelDto.add(cbPlan);
		
		cbTipoReg = new JComboBox<String>();
		cbTipoReg.setFont(Menu.fontText);
		cbTipoReg.addActionListener(this);
		cbTipoReg.setBounds(1, 71, 76, 21);
		cbTipoReg.addFocusListener(this);
		cbTipoReg.addKeyListener(this);
		panelPie.add(cbTipoReg);
		cbTipoReg.setVisible(false);

	}
	public void crearButtons() {
		buttonCliente= new JButton("");
		buttonCliente.setToolTipText("Alta de clientes");
		buttonCliente.addActionListener(this);
		buttonCliente.setBounds(255, 39, 31, 23);
		buttonCliente.setIcon(new ImageIcon(VentanaAlquiler.class.getResource("/modelo/Images/add_friend.png")));
		panelDto.add(buttonCliente);
		
		buttonGrabar= new JButton("Check ");
		buttonGrabar.setHorizontalAlignment(SwingConstants.LEFT);
		buttonGrabar.setToolTipText("Generar alojamiento");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(748, 86, 86, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaAlquiler.class.getResource("/modelo/Images/aceptar.png")));
		panelPie.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(837, 86, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaAlquiler.class.getResource("/modelo/Images/Exit.png")));
		panelPie.add(buttonSalir);
		
		buttonVerPrecios= new JButton("");
		buttonVerPrecios.setToolTipText("Ver precios");
		buttonVerPrecios.addActionListener(this);
		buttonVerPrecios.setBounds(579, 92, 20, 23);
		buttonVerPrecios.setIcon(new ImageIcon(VentanaAlquiler.class.getResource("/modelo/Images/Dollar_Green.png")));
		panelDto.add(buttonVerPrecios);
	}
	void llenarParaModificar() {
		//conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet =statement.executeQuery("Select * from ALQUILER AS A,DETALLE_A_HABITACION AS DH,CLIENTES AS C,HABITACION AS H where A.Id_Cli=C.Id_Cli and DH.Id_A=A.Id_A and DH.NumeroH=H.NumeroHab and A.EstadoA <>'" + 0 + "'and A.Id_A='" + this.COD_HOSPEDAJE + "'and DH.Id_D='" + this.COD_DETALLE + "'");
			if (resultSet.next()==true){ 
				panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), ":::: | Editar | :::: REGISTRO: " + Menu.formatid_9.format(this.COD_HOSPEDAJE), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(121, 2, 0)));
				textCId.setText(Menu.formatid_7.format(Integer.parseInt(resultSet.getString("Id_Cli"))));
				textCNom.setText(resultSet.getString("NombreCli"));
				textDscCli.setText(resultSet.getString("TipoDsctCli")+" "+ resultSet.getString("DsctCli"));
				TIP_DSCT_CLI=resultSet.getString("TipoDsctCli");
				DSCT_CLI=resultSet.getFloat("DsctCli");
				
				// DE DESCRIPCION SACO EL TIPO DE TARIFA
				String caracteristica=resultSet.getString("DescripcionH").trim();
				String delimiterr = resultSet.getString("Tipo_Hab").trim();
				String[] tempp;
				String TIP="";
				tempp = caracteristica.split(delimiterr);
				for(int i =0; i < tempp.length ; i++){
					TIP=tempp[i].trim();
					//System.out.println(tempp[i].trim());
				}
				cbTipoTar.setSelectedItem((String)TIP);
				// EN DE DESCRIPCION SACO EL TIPO DE TARIFA
				
				/*if (resultSet.getString("DescripcionH").equals(resultSet.getString("Tipo_Hab").trim() + " POR DIA")) {
					cbTipoTar.setSelectedItem("POR DIA");
				}
				if (resultSet.getString("DescripcionH").equals(resultSet.getString("Tipo_Hab").trim() + " POR HRS")) {
					cbTipoTar.setSelectedItem("POR HRS");
				}
				if (resultSet.getString("DescripcionH").equals(resultSet.getString("Tipo_Hab").trim() + " POR MES")) {
					cbTipoTar.setSelectedItem("POR MES");
				}
				if (resultSet.getString("DescripcionH").equals(resultSet.getString("Tipo_Hab").trim() + " POR SEMANA")) {
					cbTipoTar.setSelectedItem("POR SEMANA");
				}*/
				
				textPrecioH.setText(Menu.formateadorCurrency.format(Float.parseFloat(resultSet.getString("PrecioH"))));
				textDiasH.setText(resultSet.getString("DiasH"));
				FechaEntrada=(resultSet.getString("FechaIngresoH").toString());
				FechaSalida=(resultSet.getString("FechaSalidaH").toString());
				//textFIngreso.setText(resultSet.getString("FechaIngresoH").toString());
				//textFSalida.setText(resultSet.getString("FechaSalidaH").toString());
				
				dateEmision=resultSet.getString("FechaEmisionA");
				Date dater = Menu.formatoFecha.parse(resultSet.getString("FechaEmisionA"));
				chooserReg.setDate(dater);
				//chooserReg.getDateEditor().setEnabled(false);
				chooserReg.getCalendarButton().setEnabled(false);
				
				Date date = Menu.formatoFecha.parse(FechaEntrada);
				chooserIngreso.setDate(date);
				
				Date dateS = Menu.formatoFecha.parse(FechaSalida);
				chooserSalida.setDate(dateS);
				
				spinnerE.setValue(formatTime.parseObject(resultSet.getString("HoraIngresoH"))); // e.g. input 16:45
				spinnerS.setValue(formatTime.parseObject(resultSet.getString("HoraSalidaH"))); // e.g. input 16:45
				
				cbA.setSelectedItem(resultSet.getString("AdultosA").trim());
				cbN.setSelectedItem(resultSet.getString("Ni�osA").trim());
				cbB.setSelectedItem(resultSet.getString("BebesA").trim());
				textPers.setText(resultSet.getString("PersonasA"));
				cbPlan.setSelectedItem(resultSet.getString("PlanH").trim());
				textObservacion.setText(resultSet.getString("ObservacionA").toString());
				
				if (resultSet.getString("EstadoHab").equals("ALQUILADO")) {
					cbAR.setSelectedItem("ALQUILAR");
				}
				if (resultSet.getString("EstadoHab").equals("RESERVADO")) {
					//cbAR.setSelectedItem("RESERVAR");
					cbAR.setSelectedItem("ALQUILAR");
				}
				cbTipoReg.setSelectedItem(resultSet.getString("TipoRegistroA").trim());
				
				textFormatImporte.setText(Menu.formateadorCurrency.format(Float.parseFloat(resultSet.getString("ImporteH"))));
				
				MPagoH=resultSet.getString("ModoPagoH").trim();
				if (MPagoH.equals("X")) {
					rdbtnEfectivo.setSelected(true);
					}
				if (MPagoH.equals("D")) {
					rdbtnCredito.setSelected(true);
					}
				
		      	if (resultSet.getString("ModoRegistroA").trim().equals(radioUno.getText().trim())){
		      		radioUno.setSelected(true);
		      		}
		      	if (resultSet.getString("ModoRegistroA").trim().equals(radioDos.getText().trim())){
		      		radioDos.setSelected(true);
		      		}
		      		radioUno.setEnabled(false);
		      		radioDos.setEnabled(false);
		      		
				textAcompana.setText(resultSet.getString("AcompananteH"));
				
				IMPORTE=(float)resultSet.getFloat("ImporteH");
				SUBTOTAL=(float)resultSet.getFloat("SubTA");
				TOTAL=(float)resultSet.getFloat("TotalA");
				SUBTOTAL=SUBTOTAL - IMPORTE;
				
				float des= Float.parseFloat(resultSet.getString("DsctTA"));
				
				if (des>0) {
					textFormatDsct.setText(Menu.formateadorCurrency.format(Float.parseFloat(resultSet.getString("DsctTA"))));
					chckbxDesc.setSelected(true);
					textFormatDsct.setEnabled(true);
					//mostrarDscto();
				}
				
		    	//CONSULTO TURNO Y USER
				Statement ss = conexion.gConnection().createStatement();
				ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + resultSet.getInt("idTurnoA") +"'");
				if (rr.next()== true) {
					//textUser.setText(rr.getString("turno").trim() +": "+ rr.getString("NombresEmp").trim());
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
					textUser.setText("TURNO: "+ TUR +"  / OPERADOR: "+ USU);
					// NOMBRE CORTO DE USUARIO Y TURNO
					rr.close();
					ss.close();
					//END CONSULTO TURNO Y USER
				}	
			}
			statement.close();
		} catch (Exception e) {}
	}
	protected void llenarCbTipoHabitacion() {
		//conexion = new ConexionDB();
		cbTipoHabi.removeAllItems();
		try {
			String con = "Select * from TIPO_HABITACION";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			while (rs.next()==true) {
				cbTipoHabi.addItem(rs.getString("Tipo_Hab"));
			}
			cbTipoHabi.setSelectedIndex(-1);
			st.close();
		} catch (Exception e) {}
	}
	protected void llenarCbNroHab() {
		//conexion = new ConexionDB();
		cbNroHab.removeAllItems();
		try {
			String con = "Select * from HABITACION where Tipo_Hab='"+ cbTipoHabi.getSelectedItem()+ "'and EstadoHab='"+ "DISPONIBLE" + "'";//and EstadoHab!='"+ "ALQUILADO" + "'and EstadoHab!='"+ "MANTENIMIENTO" + "'and EstadoHab!='"+ "LIMPIEZA" + "'";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			while (rs.next()==true) {
				cbNroHab.addItem(rs.getString("NumeroHab"));
			}
			cbNroHab.setSelectedIndex(-1);
			st.close();
		} catch (Exception e) {}
	}
	protected void llenarCbPersonas(){
     cbA.removeAllItems();cbN.removeAllItems();cbB.removeAllItems();
        
        cbN.addItem("0");cbB.addItem("0");
		String [] lista1 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"
						   ,"21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40"
						   ,"41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60"
						   ,"61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80"
						   ,"81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99","100"};
		for (String llenar:lista1) {
			cbA.addItem(llenar);
			cbN.addItem(llenar);
			cbB.addItem(llenar);
		}
		
        cbAR.removeAllItems();
		String [] lista2 = {"ALQUILAR","RESERVAR"};
		for (String llenar:lista2) {
			cbAR.addItem(llenar);
		}
		
		// LLENO LOS PLANES
		cbPlan.removeAllItems();
		try {
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery("Select * from PLANES");
			cbPlan.addItem("NO");
			while (rs.next()==true) {
				cbPlan.addItem(rs.getString("InicialesPla").trim());
			}
			cbPlan.setSelectedIndex(0);
			rs.close();
			st.close();
		} catch (Exception e) {}
	}
	protected void llenarAlquiler() {
		cbA.setSelectedIndex(0);
		cbN.setSelectedIndex(0);
		cbB.setSelectedIndex(0);
		cbAR.setSelectedIndex(0);
		
		//conexion = new ConexionDB();
		String consul="";
		consul="Select* from HABITACION,TARIFAS where TARIFAS.NumeroHab=HABITACION.NumeroHab and TARIFAS.NumeroHab='" + cbNroHab.getSelectedItem() + "'order by tipoTar asc";
		cbTipoTar.removeAllItems();
		try {
			Statement statement =conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(consul);
			textArea.setText("");
			while (resultSet.next()==true) {
				cbTipoTar.addItem(resultSet.getString("TipoTar"));
				if (resultSet.getString("CaracteristicasHab").trim().equals("")) {
					textArea.setText("PISO"+resultSet.getString("PisoHab")+": "+resultSet.getString("DescripcionHab"));
				}else{
					textArea.setText("PISO"+resultSet.getString("PisoHab")+": "+resultSet.getString("CaracteristicasHab")+"\n"+resultSet.getString("DescripcionHab"));
				}
			}
			resultSet.close();
			statement.close();
			mostrarTarifa();
		} catch (Exception e) {}
	}
	void CambiarHabitacion () {
		textArea.setText("");
    	if (cbNroHab.getSelectedItem()==null){
    		if (MOD==0){
    			cbPlan.setSelectedIndex(-1);
    		}
    		cbTipoTar.removeAllItems();
			return;
  		}
		String consul="";
		consul="Select* from HABITACION,TARIFAS where TARIFAS.NumeroHab=HABITACION.NumeroHab and TARIFAS.NumeroHab='" + cbNroHab.getSelectedItem() + "'order by tipoTar asc";
		cbTipoTar.removeAllItems();
		try {
			Statement statement =conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(consul);
			textArea.setText("");
			while (resultSet.next()==true) {
				cbTipoTar.addItem(resultSet.getString("TipoTar"));
				if (resultSet.getString("CaracteristicasHab").trim().equals("")) {
					textArea.setText("PISO"+resultSet.getString("PisoHab")+": "+resultSet.getString("DescripcionHab"));
				}else{
					textArea.setText("PISO"+resultSet.getString("PisoHab")+": "+resultSet.getString("CaracteristicasHab")+"\n"+resultSet.getString("DescripcionHab"));
				}
			}
			
			statement.close();
			mostrarTarifa();
		} catch (Exception e) {}
	}
	public void mostrarTarifa() {
		String consul="";
		try {
			if (cbNroHab.getSelectedItem()==null||cbTipoTar.getSelectedItem()==null) {
				cbPlan.setSelectedIndex(-1);
				IMPORTE=0;textPrecioH.setText("0.00");textFormatImporte.setText("0.00");
				textPrecioH.setToolTipText("");
				return;
			}
			consul="Select* from TARIFAS where NumeroHab='" + cbNroHab.getSelectedItem() + "'and TipoTar='" + cbTipoTar.getSelectedItem() + "'";
			Statement s =conexion.gConnection().createStatement();
			ResultSet r = s.executeQuery(consul);
			textPrecioH.setToolTipText("");
			while (r.next()==true) {
				textPrecioH.setText(Menu.formateadorCurrency.format(Float.parseFloat(r.getString("PrecioTar"))));
				
				textPrecioH.setToolTipText("<html> :::::::::::::::::::::::: Precio1 S/.: "+ Menu.formateadorCurrency.format(r.getFloat("PrecioTar"))+" ::::::::::::::::::::::::"+
						"<br> :::::::::::::::::::::::: Precio2 S/.: "+  Menu.formateadorCurrency.format(r.getFloat("Precio2Tar"))+" ::::::::::::::::::::::::");
			}
			r.close();
			s.close();
		} catch (Exception e) {}
	}
	void mostrarVentanaPagos(){
		try {
			/*VentanaCuentaHuesped v = new VentanaCuentaHuesped();
			v.lblAlq.setText("HABITACION: "+VentanaControlHotel.NRO +" | " + VentanaControlHotel.HABITACION);
			v.lblAbonado.setText("TITULAR: "+ VentanaControlHotel.HUESPED);
			v.cbBus.setSelectedItem("DESCRIPCION");
      		v.textBus.setText("CONSUMO");
      		VentanaCuentaHuesped.frame.setVisible(true);
      		VentanaCuentaHuesped.frame.setVisible(false);
      		v.tableList.selectAll();
      		v.mostrarPagosxHabitacion();*/
		  
			String consultar="Select * from CUENTA_HUESPED where Id_A  ='" + this.COD_HOSPEDAJE  +"'and estadoCta ='" + "A" +"'and DescripcionCta like'" +"ALOJAMIENTO"+"%'";
	   	   	Statement st = conexion.gConnection().createStatement();
			ResultSet rs=st.executeQuery(consultar);
			if (rs.next()==true) {
				VentanaCuentaHuesped.id=rs.getString("Id_Cta").trim();
				VentanaCuentaHuesped.descripcion=rs.getString("DescripcionCta").trim();
				
				VentanaCuentaHuespedPagos.MOD=0;// PERMITE REGISTRAR 
				VentanaCuentaHuespedPagos ven= new VentanaCuentaHuespedPagos("TITULAR: "+ textCNom.getText().trim(),this.COD_HOSPEDAJE);
			    
			    if (radioUno.isSelected()) {
				    VentanaCuentaHuespedPagos.cbNH.addItem(this.NRO_HABITACION.trim());
				    VentanaCuentaHuespedPagos.cbNH.addItem((String)cbNroHab.getSelectedItem());
				    VentanaCuentaHuespedPagos.cbNH.setEnabled(false);
			    }
			    if (radioDos.isSelected()) {
			    	VentanaCuentaHuespedPagos.cbNH.setEnabled(true);
				    VentanaCuentaHuespedPagos.cbNH.setSelectedItem("%TODOS");
			    }
			    
			    VentanaCuentaHuespedPagos.textFormatMontoFactura.setText((String)textFormatTotal.getText());
		    	ven.lbl5.setText("MONTO ALOJAMIENTO:");
			    VentanaCuentaHuespedPagos.textFormatAcuenta.requestFocus(true);
			    VentanaCuentaHuespedPagos.textFormatAcuenta.selectAll();
				ven.frame.setTitle("Pagos por alojamiento |**| Nro Reg: "+ Menu.formatid_9.format(COD_HOSPEDAJE) + " - Cta Maestra #: " +  Menu.formatid_7.format(Integer.parseInt(VentanaCuentaHuesped.id)) +" |**|  ");
			    ven.frame.toFront();
			    ven.frame.setVisible(true);	
			}else{
				JOptionPane.showMessageDialog(null, "La cta por alojamiento ya fue cancelada...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			}
			st.close();
			rs.close();
	  	} catch (Exception e) {}
	}
	void soloContado(int COD){
		//conexion = new ConexionDB();
		int i=0;
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_DetCta from CUENTA_HUESPED_PAGOS order by Id_DetCta desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_DetCta"))+1);
				i=id;
			}else {
				i=1;
			}
			resultSet.close();
			statement.close();
			String sql ="INSERT INTO CUENTA_HUESPED_PAGOS (Id_DetCta,Id_Cta,ResDetCta,MPagoDetCta,FechaDetCta,HoraDetCta,AcuentaDetCta,NroHabCta,IdTurnoP) VALUES (?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
			ps.setInt(1, i);
			ps.setInt(2, COD);
			ps.setString(3, textCNom.getText());
			ps.setString(4,"EFECTIVO");
			ps.setNString(5, (dateEmision.trim()));
			ps.setNString(6, Menu.HORA.trim());//12 HORAS HILO
			ps.setFloat(7, Float.parseFloat(textFormatTotal.getText().replaceAll(",", "")));
	 		ps.setString(8, (String)cbNroHab.getSelectedItem());
	 		ps.setInt(9,VentanaLogin.ID_APETURA_CAJA);
			ps.execute();
			ps.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al load" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	void insertCuentaHuesped() {
		int COD_CUENTA_HUESPED=0;
		//conexion = new ConexionDB();
		try {
			Statement stcon = conexion.gConnection().createStatement();
			ResultSet resultS =  stcon.executeQuery("Select * from CUENTA_HUESPED where Id_A ='" + this.COD_HOSPEDAJE + "'and DescripcionCta !='" +"CONSUMO"+ "'");
			JSpinner.DateEditor modele = new JSpinner.DateEditor(spinnerE, "hh:mm:ss a");
			if (resultS.next()==true) { //ACTUALIZO
				try {
					
					// FILTRO LOS IMPORTES
					Statement stPago = conexion.gConnection().createStatement();
					ResultSet resultPago =  stPago.executeQuery("Select * from CUENTA_HUESPED_PAGOS where Id_Cta ='" + resultS.getString("Id_Cta") + "'");
					float IMPORTES=0;
					while (resultPago.next()==true) {
						IMPORTES=IMPORTES+resultPago.getFloat("AcuentaDetCta");
					}
					resultPago.close();
					stPago.close();
					// FILTRO LOS IMPORTES
					
					//:::::::::::::::::::::::::::::::::::: CAMBIA LA HABITACION
					String sq="UPDATE CUENTA_HUESPED_PAGOS SET NroHabCta =?"
			                 + "WHERE Id_Cta ='" + resultS.getString("Id_Cta") + "'and NroHabCta ='" + NRO_HABITACION.trim() + "'";
					PreparedStatement p = conexion.gConnection().prepareStatement(sq);
					p.setString(1, (String)(cbNroHab.getSelectedItem()));
					p.executeUpdate();
					p.close();
					//:::::::::::::::::::::::::::::::::::: END CAMBIA LA HABITACION
					
					COD_CUENTA_HUESPED= Integer.parseInt(resultS.getString("Id_Cta"));
					String sql="UPDATE CUENTA_HUESPED SET Id_A =?,"
			                 //+ "FechaCta =?,"
			                 //+ "HoraCta =?,"
			                 + "DescripcionCta =?,"
			                 + "MontoCta =?,"
			                 + "SaldoCta =?,"
			                 + "EstadoCta = ?"
			                 + "WHERE Id_A='"+this.COD_HOSPEDAJE+"'and DescripcionCta !='" +"CONSUMO"+ "'"; 
			         
							PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
							ps.setInt(1, this.COD_HOSPEDAJE);
							//ps.setString(2, Menu.date.trim());//FechaEntrada.trim()
							//ps.setString(3, modele.getFormat().format(spinnerE.getValue()));
							if (cbAR.getSelectedItem().equals("RESERVAR")) {
								ps.setString(2, "RESERVACION");//(String)cbAR.getSelectedItem() + " ALOJAMIENTO");
							}else{
								ps.setString(2, "ALOJAMIENTO");//(String)cbAR.getSelectedItem() + " ALOJAMIENTO");
							}
							
							if (rdbtnEfectivo.isSelected()){
								if (Float.parseFloat(textFormatTotal.getText())>IMPORTES){// SI EL MONTO ACTUAL ES MAYOR AL ANTERIOR									ps.setFloat(3, TOTAL);
									ps.setFloat(3, TOTAL);
									ps.setFloat(4, TOTAL- IMPORTES);
									ps.setString(5, "A");
								}
								if (Float.parseFloat(textFormatTotal.getText())==IMPORTES){// SI EL MONTO ES IGUAL ANTERIOR
									ps.setFloat(3, TOTAL);
									ps.setFloat(4, 0);
									ps.setString(5, "X");
								}
								if (Float.parseFloat(textFormatTotal.getText())<IMPORTES){// SI EL MONTO ES IGUAL ANTERIOR
									ps.setFloat(3, TOTAL);
									ps.setFloat(4, TOTAL- IMPORTES);
									ps.setString(5, "A");
								}
							}
							if (rdbtnCredito.isSelected()){
								ps.setFloat(3, TOTAL);
								ps.setFloat(4, TOTAL- IMPORTES);
								ps.setString(5, "A");
							}
							ps.executeUpdate();
							ps.close();
							stcon.close();
							
							if (rdbtnEfectivo.isSelected()){
								if (Float.parseFloat(textFormatTotal.getText())>IMPORTES){// SI EL MONTO ACTUAL ES MAYOR AL ANTERIOR									ps.setFloat(3, TOTAL);
									//mostrarVentanaPagos();//MOSTRAR VENTANA PAGOS
								}
							}
							//JOptionPane.showMessageDialog(null, "Cuenta fue actualizada  " + Menu.formatid_9.format(COD_CUENTA_HUESPED) + Menu.separador  ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Error al actualizar cuenta huesped" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						}
			//}else{// GRABO NUEVO
			}
			if (MOD==0) {
				try {
					
						if (rdbtnEfectivo.isSelected()){
							if (radioDos.isSelected()){
								Statement ss = conexion.gConnection().createStatement();
								ResultSet rr =  ss.executeQuery("Select * from CUENTA_HUESPED where Id_A ='" + this.COD_HOSPEDAJE + "'and DescripcionCta !='" +"CONSUMO"+ "'");
								if (rr.next()==true) { 
									Statement s =  conexion.gConnection().createStatement();
									String query ="Delete from CUENTA_HUESPED where Id_Cta='" + rr.getString("Id_Cta")  + "'";
									s.execute(query);
									s.close();
								}
							}
						}
					
						Statement statement = conexion.gConnection().createStatement();
						ResultSet resultSet = statement.executeQuery("Select Id_Cta from CUENTA_HUESPED order by Id_Cta desc limit 0,1");
						if (resultSet.next()== true) {
							COD_CUENTA_HUESPED=(Integer.parseInt(resultSet.getString("Id_Cta"))+1);
						}else {
							COD_CUENTA_HUESPED=1;
						}
						resultS.close();
						statement.close();
	
						PreparedStatement ps = conexion.gConnection().prepareStatement("INSERT INTO CUENTA_HUESPED(Id_Cta,Id_A,FechaCta,HoraCta,DescripcionCta,MontoCta,SaldoCta,EstadoCta,IdTurnoCta) VALUES (?,?,?,?,?,?,?,?,?)");
						ps.setInt(1, COD_CUENTA_HUESPED);
						ps.setInt(2, this.COD_HOSPEDAJE);
						ps.setString(3, dateEmision.trim());//FechaEntrada.trim()
						ps.setString(4, modele.getFormat().format(spinnerE.getValue()));
						if (cbAR.getSelectedItem().equals("RESERVAR")) {
							ps.setString(5, "RESERVACION");//(String)cbAR.getSelectedItem() + " ALOJAMIENTO");
						}else{
							ps.setString(5, "ALOJAMIENTO");//(String)cbAR.getSelectedItem() + " ALOJAMIENTO");
						}
						if (rdbtnEfectivo.isSelected()){
							ps.setFloat(6, TOTAL);
							ps.setFloat(7, 0);
							ps.setString(8, "X");
						}
						if (rdbtnCredito.isSelected()){
							ps.setFloat(6, TOTAL);
							ps.setFloat(7, TOTAL);
							ps.setString(8, "A");
						}
						ps.setInt(9,VentanaLogin.ID_APETURA_CAJA);
						ps.execute();
						ps.close();
						// AGREGO IMPORTE TOTAL
						if (rdbtnEfectivo.isSelected()){
							soloContado(COD_CUENTA_HUESPED);// SOLO CUENDO ES CONTADO GRABO EL IMPORTE
						}
						// END AGREGO IMPORTE TOTAL
						//JOptionPane.showMessageDialog(null, "Se ha creado la cuenta #: " + Menu.formatid_9.format(COD_CUENTA_HUESPED) + Menu.separador  ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al crear cuenta huesped" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch (Exception e) {}
	}
	
	//METODO GRABAR MODIFICAR
	public void insertarUpdate() {
		if (VentanaLogin.ID_APETURA_CAJA==0){
			JOptionPane.showMessageDialog(null, "Primero debe aperturar su TURNO...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (VentanaLogin.COD_EMP_USER.trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "No se encontro ningun usuario...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (this.COD_HOSPEDAJE==0){
			JOptionPane.showMessageDialog(null,"C�digo: " + this.COD_HOSPEDAJE + "\nno existe ningun alquiler...! \n" +"debe cambiar el estado para poder alquilar",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textCId.requestFocus();
			return;
		}
		if (radioUno.isSelected()==false && radioDos.isSelected()==false){
			JOptionPane.showMessageDialog(null, "Porfavor seleccione modo de registro...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (textCId.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Ingrese datos del h�esped...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textCId.requestFocus();
			return;
		}
		if (cbNroHab.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Seleccione el Nro de habitaci�n...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbNroHab.requestFocus();
			return;
		}
		if (cbTipoTar.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Selecione el tipo de tarifa",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbTipoTar.requestFocus();
			return;
		}
		if (cbPlan.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Selecione el tipo de plan",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbPlan.requestFocus();
			return;
		}
		if (textPrecioH.getText().trim().isEmpty()||Float.parseFloat(textPrecioH.getText().trim().replaceAll(",", ""))==0){
			JOptionPane.showMessageDialog(null, "Ingrese la tarifa / precio  habitaci�n...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textPrecioH.requestFocus();
			return;
		}
		if (textDiasH.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar el campo dias...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textDiasH.requestFocus();
			return;
		}
		if (textAcompana.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Ingrese datos del acompa�ante...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textAcompana.requestFocus();
			textAcompana.selectAll();
			return;
		}
		if (cbAR.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Seleccione el tipo de opraci�n...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbAR.requestFocus();
			return;
		}
		if (cbTipoReg.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Seleccione el tipo de resgistro...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbTipoReg.requestFocus();
			return;
		}
		if (chooserIngreso.getDate()==null){
			JOptionPane.showMessageDialog(null, "Falta ingresar la fecha de ingreso...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			chooserIngreso.requestFocus();
			return;
		}
		//if (chooserSalida.getDate()==null){
		if (chooserSalida.getDate()==null){
			JOptionPane.showMessageDialog(null, "debe ingresar la fecha de Salida...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			chooserIngreso.requestFocus();
			return;
		}
		if (textPers.getText().trim()=="0"){
			JOptionPane.showMessageDialog(null, "Seleccione al menos uno...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbA.requestFocus();
			return;
		}
		if (rdbtnEfectivo.isSelected()==false && rdbtnCredito.isSelected()==false){
			JOptionPane.showMessageDialog(null, "Porfavor seleccione modo de pago...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		mostrarTotal();
		//conexion = new ConexionDB();
		if (MOD==0) {// REGISTRAR
				try {
					if (CONT==0) {
						// CONSULTO EL CODIGO DEL ALQUILER ::::::::::::::::::::::::::::::::
						try {
							Statement statement = conexion.gConnection().createStatement();
							ResultSet resultSet = statement.executeQuery("Select Id_A from ALQUILER order by Id_A desc limit 0,1");
							if (resultSet.next()== true) {
								int id=(Integer.parseInt(resultSet.getString("Id_A"))+1);
								ID=id;
							}else{
								ID=1;
							}
							resultSet.close();
							statement.close();
						} catch (Exception e) {}
						// CONSULTO EL CODIGO DEL ALQUILER :::::::::::::::::::::::::::::::::
						
						// GRABO EL ALOJAMIENTO
						String sql ="INSERT INTO ALQUILER (Id_A,Id_Cli,IdTurnoA,FechaEmisionA,AdultosA,Ni�osA,BebesA,PersonasA,SubTA,DsctTA,IgvTA,TotalA,TipoPagoA,EstadoA,ModoRegistroA,TipoRegistroA,ObservacionA) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						
						ps.setInt(1, ID);
						ps.setInt(2, Integer.parseInt(textCId.getText()));
						ps.setInt(3, VentanaLogin.ID_APETURA_CAJA);  // ID TURNO);
						ps.setString(4, dateEmision.trim());
						ps.setString(5, (String)(cbA.getSelectedItem()));
						ps.setString(6, (String)(cbN.getSelectedItem()));
						ps.setString(7, (String)(cbB.getSelectedItem()));
						ps.setString(8, textPers.getText());
						
						ps.setFloat(9, TOTAL + Float.parseFloat(textFormatDsct.getText().replaceAll(",", "")));
						ps.setFloat(10, Float.parseFloat(textFormatDsct.getText().replaceAll(",", "")));
						ps.setFloat(11, Float.parseFloat(Menu.formateadorCurrency.format(TOTAL-TOTAL/Menu.IGV).replaceAll(",", "")));
						ps.setFloat(12, TOTAL);
				      	if (rdbtnEfectivo.isSelected()){
				      		ps.setString(13, "CONTADO");
				      		}
				      	if (rdbtnCredito.isSelected()){
				      		ps.setString(13, "CREDITO");
				      		}

				      	if (cbAR.getSelectedItem().equals("ALQUILAR")){
				    		ps.setString(14, "1");  // ESTADO DE DE ALOJAMIENTO
				      		}
				      	if (cbAR.getSelectedItem().equals("RESERVAR")){
				    		ps.setString(14, "4");  // ESTADO DE DE RESERVACION
				      		}
				      	
				      	if (radioUno.isSelected()){
				      		ps.setString(15, radioUno.getText().trim());
				      		}
				      	if (radioDos.isSelected()){
				      		ps.setString(15, radioDos.getText().trim());
				      		}
				      	
				      	ps.setString(16, (String)cbTipoReg.getSelectedItem());
				      	ps.setString(17, textObservacion.getText().trim());//OBSERVACION
						ps.execute();
						CONT=1;
						ps.close();
					}else{
						// ACTUALIZAR EL ALOJAMIENTO
						 String sql="UPDATE ALQUILER SET Id_Cli = ?,"
				                 + "FechaEmisionA =?,"
				                 + "AdultosA =?,"
				                 + "Ni�osA =?,"
				                 + "BebesA =?,"
				                 + "PersonasA =?,"
				                 + "SubTA = ?,"
				                 + "DsctTA=?,"
				                 + "IgvTA =?,"
				                 + "TotalA =?,"
				                 + "TipoPagoA =?,"
				                 + "EstadoA =?,"
				                 + "ModoRegistroA =?,"
				                 + "TipoRegistroA =?,"
				                 + "ObservacionA =?"
				                 //+ "IdTurnoA = ?"
				                 + "WHERE Id_A = '"+ID+"'"; 
						
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setInt(1, Integer.parseInt(textCId.getText()));
						ps.setString(2, dateEmision.trim());
						
						ps.setString(3, (String)(cbA.getSelectedItem()));
						ps.setString(4, (String)(cbN.getSelectedItem()));
						ps.setString(5, (String)(cbB.getSelectedItem()));
						ps.setString(6, textPers.getText());
						
						ps.setFloat(7, TOTAL + Float.parseFloat(textFormatDsct.getText().replaceAll(",", "")));
						ps.setFloat(8, Float.parseFloat(textFormatDsct.getText().replaceAll(",", "")));
						ps.setFloat(9, Float.parseFloat(Menu.formateadorCurrency.format(TOTAL-TOTAL/Menu.IGV).replaceAll(",", "")));
						ps.setFloat(10, TOTAL);
						
				      	if (rdbtnEfectivo.isSelected()){
				      		ps.setString(11, "CONTADO");
				      		}
				      	if (rdbtnCredito.isSelected()){
				      		ps.setString(11, "CREDITO");
				      		}
				      	
				      	if (cbAR.getSelectedItem().equals("ALQUILAR")){
				    		ps.setString(12, "1");  // ESTADO DE DE ALOJAMIENTO
				      		}
				      	if (cbAR.getSelectedItem().equals("RESERVAR")){
				    		ps.setString(12, "4");  // ESTADO DE DE RESERVACION
				      		}
				      	
				      	if (radioUno.isSelected()){
				      		ps.setString(13, radioUno.getText().trim());
				      		}
				      	if (radioDos.isSelected()){
				      		ps.setString(13, radioDos.getText().trim());
				      		}
				      	
				      	ps.setString(14, (String)cbTipoReg.getSelectedItem());
				      	ps.setString(15, textObservacion.getText().trim());//OBSERVACION
						//ps.setInt(15, VentanaLogin.ID_APETURA_CAJA);  // ID TURNO
						ps.executeUpdate();
						ps.close();
					}
					// GRABO EL DETALLE DEL ALQUILER :::::::::::::::::::::::::::::::::::::::::::::
					String sq ="INSERT INTO DETALLE_A_HABITACION (NumeroH,Id_A,DescripcionH,FechaIngresoH,FechaSalidaH,HoraIngresoH,HoraSalidaH,PrecioH,DiasH,ImporteH,ModoPagoH,AcompananteH,PlanH) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement state = conexion.gConnection().prepareStatement(sq);
					
					state.setInt(1, Integer.parseInt(cbNroHab.getSelectedItem().toString()));
					state.setInt(2, ID);
					state.setString(3, cbTipoHabi.getSelectedItem().toString()  + " " + cbTipoTar.getSelectedItem().toString()); 
					state.setNString(4, FechaEntrada);
					state.setString(5, FechaSalida);
					
					JSpinner.DateEditor modele = new JSpinner.DateEditor(spinnerE, "hh:mm:ss a");
					JSpinner.DateEditor models = new JSpinner.DateEditor(spinnerS, "hh:mm:ss a");
					state.setString(6, modele.getFormat().format(spinnerE.getValue())); //HORAINGRESO);
					state.setString(7, models.getFormat().format(spinnerS.getValue())); //HORASALIDA);
					
					state.setFloat(8, Float.parseFloat(textPrecioH.getText().replaceAll(",", "")));
					state.setInt(9,   Integer.parseInt(textDiasH.getText().trim()));
					state.setFloat(10, IMPORTE);// Float.parseFloat( textFormatImporte.getText().trim()));
					/*state.setFloat(15, Float.parseFloat(textFormatDsct.getText().trim()));
					 * 
			      	if (rdbtnEfectivo.isSelected()){
			      		state.setFloat(16, Float.parseFloat( textFormatTotal.getText().trim()));
			      	}
			      	if (rdbtnCredito.isSelected()){
			      		state.setFloat(16, Float.parseFloat("0"));//textFormatAmortizo.getText().trim()));
			      	}*/
			      	state.setString(11, MPagoH.trim());
					state.setString(12, textAcompana.getText().trim()); 
					state.setString(13, cbPlan.getSelectedItem().toString());
							
					state.execute();
					state.close();
					
						// ACTUALIZO EL ESTADO DE LA HABITACION :::::::::::::::::::::::::::::::::::::::::::::
						String s ="UPDATE HABITACION SET NumeroHab = ?,"
				                + "EstadoHab = ?"
				                + "WHERE NumeroHab = '"+ cbNroHab.getSelectedItem()+"'";
								
						PreparedStatement es = conexion.gConnection().prepareStatement(s);
						es.setInt(1, Integer.parseInt(cbNroHab.getSelectedItem().toString()));
						if (cbAR.getSelectedItem()=="ALQUILAR") {
							es.setString(2, "ALQUILADO");
						}
						if (cbAR.getSelectedItem()=="RESERVAR") {
							es.setString(2, "RESERVADO");
						}
						es.executeUpdate();
						es.close();
					
						insertCuentaHuesped();// CUENTA HUESPED
						JOptionPane.showMessageDialog(null, "   Check-in se efectu� correctamente" + Menu.separador + 
						":::::::::::::::: ESTADO | " + cbAR.getSelectedItem().toString().replace("AR", "ADO") + " ::::::::::::::::" ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
				if (radioDos.isSelected()) {
					llenarCbNroHab();
					cbNroHab.requestFocus();
				}
				if (radioUno.isSelected()) {
					frame.dispose();// SI GRABO SOLO UNA HABI AL GRABAR SALGO
				}
		}
		if (MOD==1) { // MODIFICAR :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				try {
					if (CONT==0) {
						// ACTUALIZAR EL ALOJAMIENTO
						 String sql="UPDATE ALQUILER SET Id_Cli = ?,"
				                 //+ "FechaEmisionA =?,"
				                 + "AdultosA =?,"
				                 + "Ni�osA =?,"
				                 + "BebesA =?,"
				                 + "PersonasA =?,"
				                 + "SubTA = ?,"
				                 + "DsctTA=?,"
				                 + "IgvTA =?,"
				                 + "TotalA =?,"
				                 + "TipoPagoA =?,"
				                 + "EstadoA =?,"
				                 + "ModoRegistroA =?,"
				                 + "TipoRegistroA =?,"
				                 + "ObservacionA =?"
				                 //+ "IdTurnoA = ?"

				                 + "WHERE Id_A = '"+this.COD_HOSPEDAJE+"'"; 
						
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setInt(1, Integer.parseInt(textCId.getText()));
						//ps.setString(3, dateEmision.trim());
						
						ps.setString(2, (String)(cbA.getSelectedItem()));
						ps.setString(3, (String)(cbN.getSelectedItem()));
						ps.setString(4, (String)(cbB.getSelectedItem()));
						ps.setString(5, textPers.getText());
						
						ps.setFloat(6, TOTAL + Float.parseFloat(textFormatDsct.getText().replaceAll(",", "")));
						ps.setFloat(7, Float.parseFloat(textFormatDsct.getText().replaceAll(",", "")));
						ps.setFloat(8, Float.parseFloat(Menu.formateadorCurrency.format(TOTAL-TOTAL/Menu.IGV).replaceAll(",", "")));
						ps.setFloat(9, TOTAL);
				      	if (rdbtnEfectivo.isSelected()){
				      		ps.setString(10, "CONTADO");
				      		}
				      	if (rdbtnCredito.isSelected()){
				      		ps.setString(10, "CREDITO");
				      		}
				      	
				      	if (cbAR.getSelectedItem().equals("ALQUILAR")){
				    		ps.setString(11, "1");  // ESTADO DE DE ALOJAMIENTO
				      		}
				      	if (cbAR.getSelectedItem().equals("RESERVAR")){
				    		ps.setString(11, "4");  // ESTADO DE DE RESERVACION
				      		}
				      	
				      	if (radioUno.isSelected()){
				      		ps.setString(12, radioUno.getText().trim());
				      		}
				      	if (radioDos.isSelected()){
				      		ps.setString(12, radioDos.getText().trim());
				      		}
				      	ps.setString(13, (String)cbTipoReg.getSelectedItem());
				      	ps.setString(14, textObservacion.getText().trim());//OBSERVACION
						//ps.setInt(14, VentanaLogin.ID_APETURA_CAJA);  // ID TURNO
						ps.executeUpdate();
						CONT=1;
						ps.close();
					}
					// ACTUALIZO EL DETALLE DEL ALQUILER :::::::::::::::::::::::::::::::::::::::::::::
					 String sq="UPDATE DETALLE_A_HABITACION SET Id_D = ?,"
			                 + "NumeroH =?,"
			                 + "Id_A = ?,"
			                 + "DescripcionH = ?,"
			                 + "FechaIngresoH = ?,"
			                 + "FechaSalidaH =?,"
			                 + "HoraIngresoH = ?,"
			                 + "HoraSalidaH=?,"
			                 //+ "AdultosH =?,"
			                 //+ "Ni�osH =?,"
			                 //+ "BebesH =?,"
			                 //+ "PersonasH =?,"
			                 + "PrecioH =?,"
			                 + "DiasH =?,"
			                 + "ImporteH =?,"
			                 //+ "DsctH =?,"
			                 //+ "AcuentaH =?,"
			                 + "ModoPagoH =?,"
			                 + "AcompananteH =?,"
			                 + "PlanH =?"
			                 
			                 + "WHERE Id_D = '"+ this.COD_DETALLE +"'"; 
					
					PreparedStatement state = conexion.gConnection().prepareStatement(sq);
					
					state.setInt(1, this.COD_DETALLE);
					state.setInt(2, Integer.parseInt(cbNroHab.getSelectedItem().toString()));
					state.setInt(3, this.COD_HOSPEDAJE);
					state.setString(4, (String)cbTipoHabi.getSelectedItem().toString().trim() + " " + cbTipoTar.getSelectedItem().toString().trim()); 
					state.setNString(5, FechaEntrada);
					state.setString(6, FechaSalida);

					JSpinner.DateEditor modele = new JSpinner.DateEditor(spinnerE, "hh:mm:ss a");
					JSpinner.DateEditor models = new JSpinner.DateEditor(spinnerS, "hh:mm:ss a");
					state.setString(7, modele.getFormat().format(spinnerE.getValue())); //HORAINGRESO);
					state.setString(8, models.getFormat().format(spinnerS.getValue())); //HORASALIDA);
					
					state.setFloat(9, Float.parseFloat(textPrecioH.getText().replaceAll(",", "")));
					state.setInt(10,   Integer.parseInt(textDiasH.getText().trim()));
					state.setFloat(11, IMPORTE);//Float.parseFloat( textFormatImporte.getText().trim()));
					/*state.setFloat(16, Float.parseFloat(textFormatDsct.getText().trim()));
			      	if (rdbtnEfectivo.isSelected()){
			      		state.setFloat(17, Float.parseFloat( textFormatTotal.getText().trim()));
			      	}
			      	if (rdbtnCredito.isSelected()){
			      		state.setFloat(17, Float.parseFloat("0"));
			      	}*/
			      	state.setString(12, MPagoH.trim()); 
					state.setString(13, textAcompana.getText().trim()); 
					state.setString(14, cbPlan.getSelectedItem().toString());
					
					state.executeUpdate();
					state.close();
					// ACTUALIZO EL ESTADO DE LA HABITACION ACTUAL :::::::::::::::::::::::::::::::::::::::::::::
					if (cbNroHab.getSelectedItem()!=NRO_HABITACION.trim()) {
						String s ="UPDATE HABITACION SET NumeroHab = ?,"
				                + "EstadoHab = ?"
				                + "WHERE NumeroHab = '"+ cbNroHab.getSelectedItem() +"'";
								
						PreparedStatement es = conexion.gConnection().prepareStatement(s);
						es.setInt(1, Integer.parseInt(cbNroHab.getSelectedItem().toString()));
						if (cbAR.getSelectedItem()=="ALQUILAR") {
							es.setString(2, "ALQUILADO");
						}
						if (cbAR.getSelectedItem()=="RESERVAR") {
							es.setString(2, "RESERVADO");
						}
						es.executeUpdate();
						es.close();
						
							// ACTUALIZO EL ESTADO DE LA HABITACION ANTERIOR :::::::::::::::::::::::::::::::::::::::
							String c ="UPDATE HABITACION SET NumeroHab = ?,"
					                + "EstadoHab = ?"
					                + "WHERE NumeroHab = '"+ NRO_HABITACION.trim() +"'";
									
							PreparedStatement p = conexion.gConnection().prepareStatement(c);
							p.setInt(1, Integer.parseInt(NRO_HABITACION.trim()));
							p.setString(2, "LIMPIEZA");
							p.executeUpdate();
							p.close();
						
							// ********************************************************** CAMBIO LA HBAITACION DE PAGOS DE HUESPED
							Statement st =  conexion.gConnection().createStatement();
							ResultSet set = st.executeQuery("Select* from CUENTA_HUESPED where Id_A='" + this.COD_HOSPEDAJE + "'and DescripcionCta='" + "CONSUMO" + "'"); 
							int I=0;
							if (set.next()==true) {
								I= set.getInt("Id_Cta");
							}
							
							st.close();
					        String S="UPDATE CUENTA_HUESPED_PAGOS SET Id_Cta = ?,"
					                 + "NroHabCta =?"
					                 + "WHERE Id_Cta = '"+ I + "'and NroHabCta= '"+ NRO_HABITACION.trim() + "'"; 
					         
							PreparedStatement PT = conexion.gConnection().prepareStatement(S);
							PT.setInt(1,I);
							PT.setString(2, (String) cbNroHab.getSelectedItem()); 
							PT.executeUpdate();
							PT.close();
							// ********************************************************** CAMBIO LA HBAITACION DE PAGOS DE HUESPED
							
							JOptionPane.showMessageDialog(null, "La Habitaci�n Nro " + NRO_HABITACION.trim() + Menu.separador + 
																"fue cambiada por la habitaci�n Nro " + cbNroHab.getSelectedItem() ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					
					// ACTUALIZO EL ESTADO DE LA HABITACION SI SE CAMBIA A OTRA :::::::::::::::::::::::::::::::::::::::
					}else if (cbNroHab.getSelectedItem()==NRO_HABITACION.trim()) {
						String s ="UPDATE HABITACION SET NumeroHab = ?,"
				                + "EstadoHab = ?"
				                + "WHERE NumeroHab = '"+ cbNroHab.getSelectedItem()+"'";
								
						PreparedStatement es = conexion.gConnection().prepareStatement(s);
						es.setInt(1, Integer.parseInt(cbNroHab.getSelectedItem().toString()));
						if (cbAR.getSelectedItem()=="ALQUILAR") {
							es.setString(2, "ALQUILADO");
						}
						if (cbAR.getSelectedItem()=="RESERVAR") {
							es.setString(2, "RESERVADO");
						}
						es.executeUpdate();
						es.close();
					}
					
				insertCuentaHuesped();// CUENTA HUESPED	
				JOptionPane.showMessageDialog(null, " La operaci�n se efectu� correctamente" + Menu.separador + 
													":::::::::::::::: ESTADO | " + cbAR.getSelectedItem().toString().replace("AR", "ADO") + " ::::::::::::::::" ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
				frame.dispose();
		}
	}

	public void mostrarDias() {
		try {
			if (textPrecioH.getText().length()<=0 || chooserSalida.getDate()==null) {
				return;
			}
			int diae = chooserIngreso.getCalendar().get(Calendar.DAY_OF_YEAR);
			int dias = chooserSalida.getCalendar().get(Calendar.DAY_OF_YEAR);
			int DIAS = ( dias - diae);
			
			int añoe = chooserIngreso.getCalendar().get(Calendar.YEAR);
			int años = chooserSalida.getCalendar().get(Calendar.YEAR);
			
			this.textDiasH.setText(Integer.toString(DIAS));
			this.textDiasH.setEditable(false);
			if (añoe!=años) {
				 JOptionPane.showMessageDialog(null, "El sistema no puede proceder con el metodo �FECHAS { DIAS }.. " + Menu.separador + "Error...Query  � diferencia entre años ? " + Menu.separador  + "por lo q' debe ingresar los dias de forma manual ", Menu.SOFTLE_HOTEL,JOptionPane.OK_CANCEL_OPTION);
				 this.textDiasH.setEditable(true);
				 this.textDiasH.setText("0");
				 this.textDiasH.requestFocus();
				 this.textDiasH.selectAll();
			}
			mostrarImporte();
			// AGREGO FECHA A VARIABLES
			//SimpleDateFormat form = new SimpleDateFormat("dd-MMM-yyy");
			//FechaEntrada = form.format(chooserIngreso.getDate());
			//FechaSalida = form.format(chooserSalida.getDate());
			
			//textFIngreso.setText(FechaEntrada);
			//textFSalida.setText(FechaSalida);
			
			SimpleDateFormat form1 = new SimpleDateFormat("yyyy-MM-dd");
			FechaEntrada = form1.format(chooserIngreso.getDate());
			FechaSalida = form1.format(chooserSalida.getDate());
			
		} catch (Exception e) {}
	}
	
	public void mostrarImporte() {
		try {
			if (textPrecioH.getText().length()<=0 ) {//|| chooserSalida.getDate()==null) {
				return;
			}
			float dia=(Float.parseFloat(textDiasH.getText()));
			IMPORTE=(Float.parseFloat(textPrecioH.getText().replaceAll(",", ""))*dia);
			if (dia==0) {
				IMPORTE=(Float.parseFloat(textPrecioH.getText().replaceAll(",", "")));
			}
			//	::::::::::: CALCULA SOLO POR DIAS ::::::::::::::
			if (cbTipoTar.getSelectedItem().equals("POR HRS")||cbTipoTar.getSelectedItem().equals("POR MES")||cbTipoTar.getSelectedItem().equals("POR SEMANA")) {
				IMPORTE=(Float.parseFloat(textPrecioH.getText().replaceAll(",", "")));
			}
			textFormatImporte.setText(Menu.formateadorCurrency.format(IMPORTE));
			if (cbPlan.getSelectedItem()!=null){
				mostrarDscto();
			}	
		} catch (Exception e) {}
	}
	public void mostrarDscto() {
		try {
			//if (textPrecioH.getText().trim().isEmpty()||Float.parseFloat(textPrecioH.getText().trim().replaceAll(",", ""))==0){
				
			if (textPrecioH.getText().length()<=0 ) {//|| chooserSalida.getDate()==null) {
				return;
			}
			if (textFormatDsct.getText().isEmpty()) {
				textFormatDsct.setText("0.00");
				textFormatDsct.selectAll();
				textFormatDsct.requestFocus();
			}
			temp_sub=0;temp_tot=0;
			float dsct=Float.valueOf(textFormatDsct.getText().trim()).floatValue();
			temp_sub =SUBTOTAL +IMPORTE;
			temp_tot=(temp_sub-dsct);
			//System.out.println("importe: "+IMPORTE +" sub: " + SUBTOTAL + " total: " + TOTAL + ":::::::::::" + temp_sub + "   " + temp_tot);
			
			textFormatSubTotal.setText(Menu.formateadorCurrency.format(temp_sub));
			textFormatTotal.setText(Menu.formateadorCurrency.format(temp_tot));
		} catch (Exception e) {}
		
	}
	public void  mostrarTotal(){
		SUBTOTAL=temp_sub;
		TOTAL=temp_tot;
	}
	public void mostrarPers() {
		try {
			int a=(Integer.parseInt((String)cbA.getSelectedItem()));
			int n=(Integer.parseInt((String)cbN.getSelectedItem()));
			int b=(Integer.parseInt((String)cbB.getSelectedItem()));
			
			int t=(a + n + b );
			this.textPers.setText(Integer.toString(t));
		} catch (Exception e) {}
	}
	public void llenarTipoRegistro(){
        cbTipoReg.removeAllItems();
        /*if(cbAR.getSelectedItem().equals("ALQUILAR")) {
    		String [] lista2 = {"ALOJAMIENTO"};
    		for (String llenar:lista2) {
    			cbTipoReg.addItem(llenar);
    		}
        }
        if(cbAR.getSelectedItem().equals("RESERVAR")) {
    		String [] lista2 = {"TELEFONO","DIRECTA","WEB","E-MAIL","FAX"};
    		for (String llenar:lista2) {
    			cbTipoReg.addItem(llenar);
    		}
    		////cbTipoReg.setSelectedIndex(-1);
        }*/
		String [] lista2 = {"ALOJAMIENTO","TELEFONO","DIRECTA","WEB","E-MAIL","FAX"};
		for (String llenar:lista2) {
			cbTipoReg.addItem(llenar);
		}
	}
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == buttonCliente){// BUSCAR CLIENTE
			VentanaCliente ventana =new VentanaCliente();
			VentanaCliente.FILTRO_CLI="VA";
      		ventana.frame.setVisible(true);
            ventana.llenarTable("Select * from Clientes order by NombreCli asc");
            ventana.textBus.requestFocus(true);
		}
		if (evento.getSource().equals(cbTipoHabi)) {
			llenarCbNroHab();
		}
		
      	if (evento.getSource().equals(cbNroHab)){
      		if (MOD==1) { // MODIFICAR ALQUILER
	    		CambiarHabitacion();
	    	}
	    	if (MOD==0) { // NUEVO ALQUILER
	    		CambiarHabitacion();
	    	}
			mostrarPlan(); // MUESTRO PLAN
		}
      	if (evento.getSource().equals(cbTipoTar)){
      		mostrarTarifa();
      		mostrarImporte();
    		mostrarPlan();// MUESTRO PLAN
		}
      	if (evento.getSource().equals(cbPlan)){
      		mostrarPlan();
		}
      	if (evento.getSource().equals(textFormatDsct)){
      		mostrarDscto();
      	}
      	if (evento.getSource().equals(rdbtnEfectivo)){
      		MPagoH="X";
      	}
      	if (evento.getSource().equals(rdbtnCredito)){
      		MPagoH="D";
      	}
		if (evento.getSource().equals(cbAR)) {
			llenarTipoRegistro();
		}
		
      	if (evento.getSource().equals(chckbxDesc)){
      		if (chckbxDesc.isSelected()==true) {
      			textFormatDsct.setEnabled(true);
      			textFormatDsct.selectAll();
      			textFormatDsct.requestFocus();
      			// DESCUENTO EL CLIENTE
      			if (TIP_DSCT_CLI.trim().equals("%")) {
      				//float total=(Float.parseFloat(textFormatImporte.getText()));
      				TOTAL=(Float.parseFloat(textFormatSubTotal.getText().replaceAll(",", "")) * DSCT_CLI / 100);		
      				textFormatDsct.setText(Float.toString(TOTAL));
      			}
      			if (TIP_DSCT_CLI.trim().equals("S/.")) {
      				textFormatDsct.setText(Float.toString(DSCT_CLI));
      				mostrarDscto();
      			}
      			// END DESCTO CLIENTE
      		}
      		if (chckbxDesc.isSelected()==false) {
      			textFormatDsct.setEnabled(false);
      			textFormatDsct.setText("0.00");
      		}
      		mostrarDscto();
      		
      	}
      	if (evento.getSource().equals(cbA)||evento.getSource().equals(cbN)||evento.getSource().equals(cbB)){
      		mostrarPers();
      	}
      	if (evento.getSource()==buttonGrabar) {
      		insertarUpdate();
      	}
      	if (evento.getSource()==buttonSalir) {
      		frame.dispose();
      	}
      	if (evento.getSource()==buttonVerPrecios) {
			JOptionPane.showMessageDialog(null,textPrecioH.getToolTipText()+"\n      :::::::::::::::::::: Dolar $: " + Menu.formateadorCurrency.format(Float.parseFloat(textPrecioH.getText())*VentanaLogin.TIPO_CAMBIO)+" :::::::::::::::::::: " ,"Vista de Precios",JOptionPane.PLAIN_MESSAGE);
			textPrecioH.requestFocus();
			return;
      	}
	}
	
	public void mostrarPlan() {
		try {
			Statement s =conexion.gConnection().createStatement();
			ResultSet r = s.executeQuery("Select* from PLANES where InicialesPla='" + cbPlan.getSelectedItem() + "'");
			textAreaP.setText("");
			mostrarTarifa();
			if (r.next()==true) {
				if (cbPlan.getSelectedItem()!="NO") {
					textPrecioH.setText(Menu.formateadorCurrency.format(Float.parseFloat(textPrecioH.getText()) + Float.parseFloat(r.getString("CostoPla"))));
					textAreaP.setText(""+ r.getString("DescripcionPla").trim() +" COSTO: " + Menu.formateadorCurrency.format(r.getFloat("CostoPla"))+"\n"+ r.getString("ObservacionPla").trim() +"");
				}
			}
			r.close();
			s.close();
	    	mostrarImporte(); 
		} catch (Exception e) {}
	}
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
      	if (e.getSource()==(chooserIngreso)||e.getSource()==(chooserSalida)){
      		if (chooserSalida.getDate()!=null){
      			if (cbTipoTar.getSelectedItem()!=null) {
      				mostrarDias();
      			}
      			if (cbPlan.getSelectedItem()!=null) {
      				mostrarPlan();
      				
      			}	
      		}
		}
      	if (e.getSource()==(chooserReg)){
    		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
    		dateEmision = form.format(chooserReg.getDate());
		}
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyReleased(KeyEvent evet) {
		// TODO Auto-generated method stub
		char e=evet.getKeyChar();
		if (evet.getSource() == cbTipoHabi){
			if (e==KeyEvent.VK_ENTER){
				if (cbTipoHabi.getSelectedIndex()!=-1){
					cbNroHab.requestFocus();
				}else{
					cbTipoHabi.requestFocus();
				}
			}
		} 
		if (evet.getSource() == cbNroHab){
			if (e==KeyEvent.VK_ENTER){
				if (cbNroHab.getSelectedIndex()!=-1){
					cbTipoTar.requestFocus();
				}else{
					cbNroHab.requestFocus();
				}
			}
		} 
		if (evet.getSource() == cbTipoTar){
			if (e==KeyEvent.VK_ENTER){
				if (cbTipoTar.getSelectedIndex()!=-1){
					cbPlan.requestFocus();
				}else{
					cbTipoTar.requestFocus();
				}
			}
		} 
		if (evet.getSource() == cbPlan){
			if (e==KeyEvent.VK_ENTER){
				if (cbPlan.getSelectedIndex()!=-1){
					textPrecioH.requestFocus();
					textPrecioH.selectAll();
				}else{
					cbPlan.requestFocus();
				}
			}
		} 
		if (evet.getSource() == cbA){
			if (e==KeyEvent.VK_ENTER){
				if (cbA.getSelectedIndex()!=-1){
					cbN.requestFocus();
				}else{
					cbA.requestFocus();
				}
			}
		} 
		if (evet.getSource() == cbN){
			if (e==KeyEvent.VK_ENTER){
				if (cbN.getSelectedIndex()!=-1){
					cbB.requestFocus();
				}else{
					cbN.requestFocus();
				}
			}
		} 
		if (evet.getSource() == cbB){
			if (e==KeyEvent.VK_ENTER){
				if (cbB.getSelectedIndex()!=-1){
					cbTipoReg.requestFocus();
				}else{
					cbB.requestFocus();
				}
			}
		}
		if (evet.getSource().equals(textDiasH)){
			if (textDiasH.getText().toLowerCase().isEmpty()|| textDiasH.getText().toLowerCase().length()>3){
				textDiasH.requestFocus();
				textDiasH.selectAll();
				textDiasH.setText(null);
				} 
				else if (e==KeyEvent.VK_ENTER || textDiasH.getText().toLowerCase().length()==3){
				}
		}
		if (evet.getSource().equals(textPrecioH)){
			if (textPrecioH.getText().toLowerCase().isEmpty()|| textPrecioH.getText().toLowerCase().length()>6){
				textPrecioH.requestFocus();
				textPrecioH.selectAll();
				textPrecioH.setText(null);
				} 
				else if (e==KeyEvent.VK_ENTER || textPrecioH.getText().toLowerCase().length()==6){
					textAcompana.requestFocus();
					textAcompana.selectAll();
				}
		}
		if (evet.getSource() == textAcompana){
			int pos = textAcompana.getCaretPosition();textAcompana.setText(textAcompana.getText().toUpperCase());textAcompana.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
			if (textAcompana.getText().toLowerCase().isEmpty()|| textAcompana.getText().toLowerCase().length()>=98){
				textAcompana.requestFocus();
				textAcompana.selectAll();
				textAcompana.setText(null);
				} 
				 if (e==KeyEvent.VK_ENTER){
					cbAR.requestFocus();
				}
		}
		if (evet.getSource() == cbAR){
			if (e==KeyEvent.VK_ENTER){
				if (cbAR.getSelectedIndex()!=-1){
					cbA.requestFocus();
				}else{
					cbAR.requestFocus();
				}
			}
		}
		if (evet.getSource() == textObservacion){
			int pos = textObservacion.getCaretPosition();textObservacion.setText(textObservacion.getText().toUpperCase());textObservacion.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
			if (textObservacion.getText().toLowerCase().isEmpty()|| textObservacion.getText().toLowerCase().length()>=290){
				textObservacion.requestFocus();
				textObservacion.selectAll();
				textObservacion.setText(null);
				} 
				 if (e==KeyEvent.VK_ENTER){
				}
		}
		if (evet.getSource() == cbTipoReg){
			if (e==KeyEvent.VK_ENTER){
				if (cbTipoReg.getSelectedIndex()!=-1){
					if (rdbtnEfectivo.isSelected()&&rdbtnCredito.isSelected()) {
						buttonGrabar.doClick();
					}else{
						rdbtnEfectivo.requestFocus();;
					}
				}else{
					cbTipoReg.requestFocus();
				}
			}
		}	
		if (evet.getSource() == rdbtnEfectivo){
			if (e==KeyEvent.VK_ENTER){
				buttonGrabar.requestFocus();
			}
		}
		if (evet.getSource() == rdbtnCredito){
			if (e==KeyEvent.VK_ENTER){
				buttonGrabar.requestFocus();
			}
		}	
		if (evet.getSource().equals(textFormatDsct)){
			if (textFormatDsct.getText().toLowerCase().isEmpty()|| textFormatDsct.getText().toLowerCase().length()>=6){
				textFormatDsct.requestFocus();
				textFormatDsct.selectAll();
				textFormatDsct.setText(null);
				} 
				else if (e==KeyEvent.VK_ENTER){
					buttonGrabar.doClick();
				}
		}
      	if (evet.getSource().equals(textFormatDsct)){
      		mostrarDscto();
      	}
      	if (evet.getSource().equals(textPrecioH) || evet.getSource().equals(textDiasH)){
      		if (textDiasH.getText().length()>0) {
	      		mostrarImporte();
	      		mostrarDscto();
      		}
      	}
	}
	@Override
	public void keyTyped(KeyEvent evet) {
		// PRECIONA EL TECLADO Y ME DA EL EVENTO
		char car=evet.getKeyChar();

		if (evet.getSource() == textAcompana){
			if((car<'a' || car>'z') && (car<'A' || car>'Z')&&(car<' '||car>'.')&&(car<'0'||car>'9')) evet.consume();
		}
		if (evet.getSource() == textPrecioH){ 
			if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
		}
		if (evet.getSource() == textDiasH){ 
			if ((car<'0'||car>'9'))evet.consume();
		}
		if (evet.getSource() == textFormatDsct){ 
			if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
		}
		if (evet.getSource() == textArea){
			if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
		}
	}
	@Override
	public void focusGained(FocusEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getSource() == textAcompana){textAcompana.setForeground(new Color(0, 51, 153));} 
		if (ev.getSource() == textAcompana){textAcompana.selectAll();} 
		if (ev.getSource() == textPrecioH){textPrecioH.selectAll();} 
		if (ev.getSource() == textFormatDsct){textFormatDsct.selectAll();} 
		if (ev.getSource() == textObservacion){textObservacion.selectAll();} 
		
		if (ev.getSource() == textArea){textArea.selectAll();} 
		if (ev.getSource() == textAreaP){textAreaP.selectAll();} 
	}
	@Override
	public void focusLost(FocusEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getSource() == textAcompana){textAcompana.setForeground(new Color(0, 51, 153));} 
	}
}
