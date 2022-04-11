package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
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
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;

public class VentanaEmpleadoAgregar implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public  JInternalFrame frame;
	protected JPanel  			panelDto;
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl6,lbl7,lbl8,lblFto,
								lbl10,lbl11,lbl12,lbl13,lbl14,lbl15,lbl17,lbl18;
	protected JTextField 		textCod,textNom,textDni,textRuc,textDir,textTel,textEda,textFecNacio;
	private JButton  			buttonGrabar,buttonSalir,
			 					buttonImg,buttonBus;
	private JFormattedTextField textFormatSueldo;
	JComboBox<String> cbProfesion,cbSexo,cbCivil,cbEstado,cbCargo;
    
	private JScrollPane scrollArea;
	private JTextArea textArea = new JTextArea();
	
	public File fichero;
	private String almacenaFoto;
	private AbrirFoto archivo;
	
	public Integer totalitems;
	
	DecimalFormat formatId = new DecimalFormat("0000000");
	NumberFormat formatDsct;
	
	static int openFrameCountEm = 0;
	public static int MOD=0;
	private JDateChooser dateChooser;
	private String FechaAlta=""; 
	public VentanaEmpleadoAgregar() {
		 //AUMENTADOS EL CONTEO DE LAS VENTANAS.
		openFrameCountEm ++;
		
		frameEmpleadoAgregar();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();

		llenarcbProfesion();
		llenarcbSexo();
      	llenarcbCivil();
      	llenarcbCargo();
      	llenarcbActivida();
		
    	// ACTUALIZO FECHA ACTUAL
      	dateChooser.setDate(Menu.fecha);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		FechaAlta = form.format(dateChooser.getDate());
      
      	
      	Menu.Desktop.add(frame);
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
    	frame.setLocation(x, y);
    	
    	nuevo();
	}
	public void frameEmpleadoAgregar() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaEmpleadoAgregar.class.getResource("/modelo/Images/personal.png")));
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				openFrameCountEm=0;
			}
		});
		frame.setTitle("Alta de empleado");
		frame.setClosable(true);
		frame.setBounds(100, 100, 499, 505);
		frame.getContentPane().setLayout(null);
	}

	public void crearPanel() {
		
		panelDto = new JPanel();
		panelDto.setBounds(10, 11, 463, 453);
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
	}
	
	public void crearLabels() {
		lbl1= new JLabel("Código:");
		lbl1.setBounds(17, 36, 71, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Nombres:");
		lbl2.setBounds(27, 64, 61, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Dni:");
		lbl3.setBounds(27, 113, 61, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Ruc:");
		lbl4.setBounds(182, 115, 56, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);

		lbl5= new JLabel("Dirección:");
		lbl5.setBounds(12, 87, 76, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);
		
		lbl6= new JLabel("Profesión:");
		lbl6.setBounds(11, 165, 76, 14);
		panelDto.add(lbl6);
		lbl6.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl6.setFont(Menu.fontLabel);

		lbl7= new JLabel("Teléfonos:");
		lbl7.setBounds(11, 136, 77, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Sexo:");
		lbl8.setBounds(27, 190, 61, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		lblFto= new JLabel("Sin image");
		lblFto.setBounds(268, 193, 162, 138);
		panelDto.add(lblFto);
		lblFto.setHorizontalAlignment(SwingConstants.CENTER);
		lblFto.setBorder(new LineBorder(new Color(169, 169, 169)));
		
		lbl10= new JLabel("Fecha Nac.:");
		lbl10.setBounds(10, 214, 77, 14);
		panelDto.add(lbl10);
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(Menu.fontLabel);
		
		lbl11= new JLabel("Edad:");
		lbl11.setBounds(20, 239, 67, 14);
		panelDto.add(lbl11);
		lbl11.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl11.setFont(Menu.fontLabel);
		
		lbl12= new JLabel("Estado civil :");
		lbl12.setBounds(10, 264, 77, 14);
		panelDto.add(lbl12);
		lbl12.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl12.setFont(Menu.fontLabel);
		
		lbl13= new JLabel("Sueldo:");
		lbl13.setBounds(26, 313, 61, 14);
		panelDto.add(lbl13);
		lbl13.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl13.setFont(Menu.fontLabel);
		
		lbl14= new JLabel("Estado:");
		lbl14.setBounds(16, 337, 71, 14);
		panelDto.add(lbl14);
		lbl14.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl14.setFont(Menu.fontLabel);
		
		lbl15= new JLabel("Descripción:");
		lbl15.setBounds(17, 362, 71, 14);
		panelDto.add(lbl15);
		lbl15.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl15.setFont(Menu.fontLabel);
		
		lbl17= new JLabel("Cargo:");
		lbl17.setBounds(37, 289, 50, 14);
		panelDto.add(lbl17);
		lbl17.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl17.setFont(Menu.fontLabel);
		
		lbl18 = new JLabel("Fecha alta:");
		lbl18.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl18.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl18.setBounds(192, 319, 68, 14);
		panelDto.add(lbl18);
		
	}
	public void crearTextFields(){
		textCod = new JTextFieldIcon(new JTextField(),"personal.png","","personal.png");
		textCod.setEditable(false);
		textCod.setColumns(10);
		textCod.setFont(Menu.fontText);
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.setHorizontalAlignment(SwingConstants.CENTER);
		textCod.addActionListener(this);
		textCod.addFocusListener(this);
		textCod.addKeyListener(this);
		textCod.setBounds(98, 33, 106, 22);
		panelDto.add(textCod);
		
		textNom = new JTextField();
		textNom.setColumns(10);
		textNom.setFont(Menu.fontText);
		textNom.setForeground(Menu.textColorForegroundInactivo);
		textNom.setHorizontalAlignment(SwingConstants.LEFT);
		textNom.addActionListener(this);
		textNom.addFocusListener(this);
		textNom.addKeyListener(this);
		textNom.setBounds(98, 58, 335, 22);
		panelDto.add(textNom);
		
		textDni = new JTextField();
		textDni.setColumns(10);
		textDni.setFont(Menu.fontText);
		textDni.setForeground(Menu.textColorForegroundInactivo);
		textDni.setHorizontalAlignment(SwingConstants.CENTER);
		textDni.addActionListener(this);
		textDni.addFocusListener(this);
		textDni.addKeyListener(this);
		textDni.setBounds(98, 109, 106, 22);
		
		//textDni.setText(Format instanceof = "00000");

		
		panelDto.add(textDni);
		
		textRuc = new JTextField();
		textRuc.setColumns(10);
		textRuc.setFont(Menu.fontText);
		textRuc.setForeground(Menu.textColorForegroundInactivo);
		textRuc.setHorizontalAlignment(SwingConstants.CENTER);
		textRuc.addActionListener(this);
		textRuc.addFocusListener(this);
		textRuc.addKeyListener(this);
		textRuc.setBounds(243, 109, 190, 22);
		panelDto.add(textRuc);
		
		textDir = new JTextField();
		textDir.setColumns(10);
		textDir.setFont(Menu.fontText);
		textDir.setForeground(Menu.textColorForegroundInactivo);
		textDir.setHorizontalAlignment(SwingConstants.LEFT);
		textDir.addActionListener(this);
		textDir.addFocusListener(this);
		textDir.addKeyListener(this);
		textDir.setBounds(98, 83, 335, 22);
		panelDto.add(textDir);

		
		textTel = new JTextField();
		textTel.setColumns(10);
		textTel.setFont(Menu.fontText);
		textTel.setForeground(Menu.textColorForegroundInactivo);
		textTel.setHorizontalAlignment(SwingConstants.LEFT);
		textTel.addActionListener(this);
		textTel.addFocusListener(this);
		textTel.addKeyListener(this);
		textTel.setBounds(97, 135, 335, 22);
		panelDto.add(textTel);
		
		scrollArea= new JScrollPane();
		scrollArea.setBounds(97, 359, 335, 79);
		panelDto.add(scrollArea);
		textArea = new JTextArea();
		scrollArea.setViewportView(textArea);
		textArea.setFont(Menu.fontText);
		textArea.setForeground(Menu.textColorForegroundInactivo);
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);
		
		textFormatSueldo = new JFormattedTextField(formatDsct);
		textFormatSueldo.setText("0.00");
		textFormatSueldo.setColumns(10);
		textFormatSueldo.setFont(Menu.fontText);
		textFormatSueldo.setForeground(Menu.textColorForegroundInactivo);
		textFormatSueldo.setHorizontalAlignment(SwingConstants.LEFT);
		textFormatSueldo.addActionListener(this);
		textFormatSueldo.addFocusListener(this);
		textFormatSueldo.addKeyListener(this);
		textFormatSueldo.addPropertyChangeListener("values",this);
		textFormatSueldo.setBounds(97, 309, 77, 22);
		panelDto.add(textFormatSueldo);
		
		
		textEda = new JTextField();
		textEda.setColumns(10);
		textEda.setFont(Menu.fontText);
		textEda.setForeground(Menu.textColorForegroundInactivo);
		textEda.setHorizontalAlignment(SwingConstants.CENTER);
		textEda.addActionListener(this);
		textEda.addFocusListener(this);
		textEda.addKeyListener(this);
		textEda.setBounds(97, 235, 61, 22);
		panelDto.add(textEda);

		textFecNacio = new JTextField();
		textFecNacio.setColumns(10);
		textFecNacio.setFont(Menu.fontText);
		textFecNacio.setForeground(Menu.textColorForegroundInactivo);
		textFecNacio.setHorizontalAlignment(SwingConstants.CENTER);
		textFecNacio.addActionListener(this);
		textFecNacio.addFocusListener(this);
		textFecNacio.addKeyListener(this);
		textFecNacio.setBounds(97, 210, 86, 22);
		panelDto.add(textFecNacio);

		
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("dd-MMM-yyyy");
		dateChooser.setBorder(new LineBorder(new Color(51, 153, 255)));
		dateChooser.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)dateChooser.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)dateChooser.getDateEditor()).setEditable(false);
		dateChooser.setBounds(162, 334, 98, 21);
		panelDto.add(dateChooser);
		dateChooser.addPropertyChangeListener(this);
	}
	
	public void crearComboBox() {
        
		cbProfesion = new JComboBox<>();
		cbProfesion.setBounds(97, 161, 334, 21);
		cbProfesion.setFont(Menu.fontText);
		cbProfesion.removeAllItems();
		cbProfesion.addFocusListener(this);
		cbProfesion.addKeyListener(this);
		panelDto.add(cbProfesion);
		
        cbSexo = new JComboBox<>();
        cbSexo.setBounds(97, 187, 50, 21);
        cbSexo.setFont(Menu.fontText);
        cbSexo.removeAllItems();
        cbSexo.addFocusListener(this);
        cbSexo.addKeyListener(this);
        panelDto.add(cbSexo);
        
		cbCivil = new JComboBox<>();
		cbCivil.setBounds(97, 261, 148, 21);
		cbCivil.setFont(Menu.fontText);
		cbCivil.removeAllItems();
		cbCivil.addFocusListener(this);
		cbCivil.addKeyListener(this);
		panelDto.add(cbCivil);
		
		cbEstado= new JComboBox<>();
		cbEstado.setBounds(97, 334, 61, 21);
		cbEstado.setFont(Menu.fontText);
		cbEstado.removeAllItems();
		cbEstado.addFocusListener(this);
		cbEstado.addKeyListener(this);
		panelDto.add(cbEstado);
		
		cbCargo= new JComboBox<>();
		cbCargo.setBounds(97, 286, 127, 21);
		cbCargo.setFont(Menu.fontText);
		cbCargo.removeAllItems();
		cbCargo.addFocusListener(this);
		cbCargo.addKeyListener(this);
		panelDto.add(cbCargo);
	}
	
	 void llenarcbProfesion() {
		cbProfesion.addItem("OTROS");
		String [] lista1 = {"ADMINISTRACION","ADMINISTRACION BANCARIA","ADMINISTRACION DE SERVICIOS","AGRONEGOCIOS INTERNACIONALES",
							"AGRONOMIA","BAR COCTELERIA Y SERVICIOS DE MESA","CARPINTERIA INDUSTRIAL","COMERCIO INTERNACIONAL","COMPUTACION E INFORMATICA","CONFECCIONES TEXTILES","CONTABILIDAD",
							"DERECHO","ECONOMIA","ELECTRONICA","ENFERMERIA","FARMACIA","FINANZAS","GESTION COMERCIAL","HOTELERIA","INDUSTRIAS ALIMENTARIAS","INFORMATICA","INGENIERIA CIVIL",
							"INGENIERIA DE REDES","INGENIERIA DE SISTEMAS","INGENIERIA DEL SOFTWARE","INGENIERIA DE TELECOMUNICACIONES Y TELEMATICA","LABORATORIO CLINICO",
							"OBSTETRICIA","ODONTOLOGÍA","PERIODISMO","PSICOLOGÍA","REDES Y COMUNICACIÓN DE DATOS","SECRETARIADO EJECUTIVO","TRABAJO SOCIAL",
							"TURISMO","VETERINARIA","ZOOTECNIA"};
		Arrays.sort (lista1);
		for (String llenar:lista1) {
			cbProfesion.addItem(llenar);
		}
	}
	 void llenarcbSexo() {
		String [] lista1 = {"M","F"};
		//Arrays.sort (lista1);
		for (String llenar:lista1) {
			cbSexo.addItem(llenar);
		}
	}
	 void llenarcbCivil() {
		String [] lista1 = {"SOLTERO","CASADO","DIVORSIADO","VIUDO"};
		//Arrays.sort (lista1);
		for (String llenar:lista1) {
			cbCivil.addItem(llenar);
		}
	}
	 void llenarcbCargo() {
		String [] lista1 = {"ADMINISTRADOR","RECEPCIONISTA","MANTENIMIENTO","LIMPIEZA","LAVANDERIA","SEGURIDAD","COCINERO(A)","MOZO(A)"};
		//Arrays.sort (lista1);
		for (String llenar:lista1) {
			cbCargo.addItem(llenar);
		}
	}
	 void llenarcbActivida() {
		String [] lista1 = {"A","X"};
		//Arrays.sort (lista1);
		for (String llenar:lista1) {
			cbEstado.addItem(llenar);
		}
	}
	public void crearButtons() {
		
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(358, 33, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaEmpleadoAgregar.class.getResource("/modelo/Images/save.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(395, 33, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaEmpleadoAgregar.class.getResource("/modelo/Images/Exit.png")));
		panelDto.add(buttonSalir);
		
		buttonImg= new JButton("Agregar imagen..");
		buttonImg.setToolTipText("Agregar imagen");
		buttonImg.addActionListener(this);
		buttonImg.setBounds(268, 333, 162, 23);
		buttonImg.setIcon(new ImageIcon(VentanaEmpleadoAgregar.class.getResource("/modelo/Images/blue.png")));
		panelDto.add(buttonImg);
		
		buttonBus= new JButton("");
		buttonBus.setBounds(227, 285, 36, 22);
		buttonBus.setToolTipText("Buscar");
		buttonBus.addActionListener(this);
		buttonBus.setIcon(new ImageIcon(VentanaEmpleadoAgregar.class.getResource("/modelo/Images/search.png")));
		panelDto.add(buttonBus);
		buttonBus.setVisible(false);
		
	}

	public void limpiarTexts() {
		textNom.setText(null);
		textNom.setBackground(Menu.textColorBackgroundInactivo);	
		textNom.setForeground(Menu.textColorForegroundInactivo);

		textDir.setText(null);
		textDir.setBackground(Menu.textColorBackgroundInactivo);	
		textDir.setForeground(Menu.textColorForegroundInactivo);
		
		textDni.setText(null);
		textDni.setBackground(Menu.textColorBackgroundInactivo);	
		textDni.setForeground(Menu.textColorForegroundInactivo);
		
		textRuc.setText(null);
		textRuc.setBackground(Menu.textColorBackgroundInactivo);	
		textRuc.setForeground(Menu.textColorForegroundInactivo);
		
		textTel.setText(null);
		textTel.setBackground(Menu.textColorBackgroundInactivo);	
		textTel.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
		
		
		cbSexo.setSelectedIndex(-1);
		cbCargo.setSelectedIndex(-1);
		cbCivil.setSelectedIndex(-1);
		cbEstado.setSelectedIndex(-1);
		cbProfesion.setSelectedIndex(-1);
		
        almacenaFoto="";
        lblFto.setText("Sin image");
        lblFto.setIcon(null);
       
		
		textFormatSueldo.setText("0.00");
		textFormatSueldo.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatSueldo.setForeground(Menu.textColorForegroundInactivo);
		
		textEda.setText(null);
		textEda.setBackground(Menu.textColorBackgroundInactivo);	
		textEda.setForeground(Menu.textColorForegroundInactivo);
		
		textFecNacio.setText(null);
		textFecNacio.setBackground(Menu.textColorBackgroundInactivo);	
		textFecNacio.setForeground(Menu.textColorForegroundInactivo);
		
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}

	//METODO NUEVO =================
	public void nuevo() {
		conexion = new ConexionDB();
		if (MOD==1) {
			try {
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select* from EMPLEADO where Id_Emp='" + Integer.parseInt(VentanaEmpleado.id) + "'");
				if (resultSet.next()== true) {
					MOD=1;
			        almacenaFoto="";
			        lblFto.setText("Sin image");
			        lblFto.setIcon(null);
					
					int id=(Integer.parseInt(resultSet.getString("Id_Emp")));
					textCod.setText(formatId.format(id));
					textNom.setText(resultSet.getString("NombresEmp"));
					textDir.setText(resultSet.getString("DireccionEmp"));
					textDni.setText(resultSet.getString("DniEmp"));
					textRuc.setText(resultSet.getString("RucEmp"));
					cbProfesion.setSelectedItem(resultSet.getString("ProfesionEmp"));
					textTel.setText(resultSet.getString("TelefonoEmp"));
					cbSexo.setSelectedItem(resultSet.getString("SexoEmp"));
					textFecNacio.setText(resultSet.getString("FechaNacioEmp"));
					textEda.setText(resultSet.getString("EdadEmp"));
					cbCargo.setSelectedItem(resultSet.getString("CargoEmp"));
					cbCivil.setSelectedItem(resultSet.getString("EstadoCivilEmp"));
					textFormatSueldo.setText(resultSet.getString("SueldoEmp"));
					textArea.setText(resultSet.getString("DescripcionEmp"));
					cbEstado.setSelectedItem(resultSet.getString("ActividadEmp"));
					almacenaFoto=resultSet.getString("URLEmp");
					
					FechaAlta=(resultSet.getString("FechaAltaEmp").toString());
					Date date = Menu.formatoFecha.parse(FechaAlta);
					dateChooser.setDate(date);
					
					panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modificar Datos:  alta " + Menu.formatoFechaString.format(resultSet.getDate("FechaAltaEmp")) + "  :::::::  baja " + Menu.formatoFechaString.format(resultSet.getDate("FechaBajaEmp")) +"  "+ Menu.formatoTimeString.format(resultSet.getTime("FechaBajaEmp")), TitledBorder.LEADING, TitledBorder.TOP, null,(SystemColor.RED)));
					// CARGO LA IMAGEN
				    Image i=null;
			        Blob blob = resultSet.getBlob("ImageEmp");
			        i= javax.imageio.ImageIO.read(blob.getBinaryStream());
			        ImageIcon image = new ImageIcon(i);
					Icon icono = new ImageIcon(image.getImage().getScaledInstance(lblFto.getWidth(), lblFto.getHeight(), Image.SCALE_DEFAULT)); 
					lblFto.setText(null);
					lblFto.setIcon(icono);
			 }
			} catch (Exception e) {}
		}
		if (MOD==0) {
			MOD=0;
			try {
				Statement st = conexion.gConnection().createStatement();
				ResultSet rs = st.executeQuery("Select Id_Emp from EMPLEADO order by Id_Emp desc limit 0,1");
				if (rs.next()== true) {
					int id=(Integer.parseInt(rs.getString("Id_Emp"))+1);
					textCod.setText(formatId.format(id));
					textNom.requestFocus(true);
					}else{
						//limpiarTexts();
						textCod.setText(formatId.format(1));
					}
				limpiarTexts();
				st.close();
				} catch (Exception e) {}
		}
	}
	
	//METODO GRABAR =================
	public void insertarUpdate() {
		if (textCod.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textCod.requestFocus();
			return;
		}
		if (textNom.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textNom.requestFocus();
			return;
		}
		if (textDir.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textDir.requestFocus();
			return;
		}
		if (textDni.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textDni.requestFocus();
			return;
		}
		if (textTel.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textTel.requestFocus();
			return;
		}
		if (cbProfesion.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Falta seleccionar su profesión",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbProfesion.requestFocus();
			return;
		}
		if (cbSexo.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbSexo.requestFocus();
			return;
		}
		if (textEda.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textEda.requestFocus();
			return;
		}
		if (cbCivil.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Falta seleccionar su estado civil",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbCivil.requestFocus();
			return;
		}
		if (cbCargo.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Falta seleccionar su cargo o función",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbCargo.requestFocus();
			return;
		}
		if (textFormatSueldo.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textFormatSueldo.requestFocus();
			return;
		}
		if (cbEstado.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbEstado.requestFocus();
			return;
		}
		
		conexion = new ConexionDB();
		try {
			if (MOD==0){ // REGISTRAR :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				String buscoNom= textNom.getText();
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * from EMPLEADO where NombresEmp='"+buscoNom+"'");
				if (resultSet.next()== true) {
					JOptionPane.showMessageDialog(null, "Empleado ya fue registrado",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
					textNom.requestFocus(true);
					textNom.selectAll();
					
					resultSet.close();
					statement.close();
				}else{
					
					try {
	                FileInputStream  archivofoto;
					String sql ="INSERT INTO  EMPLEADO (Id_Emp,Id_Hor,CargoEmp,NombresEmp,DireccionEmp,DniEmp,RucEmp,ProfesionEmp,TelefonoEmp,SexoEmp,FechaNacioEmp,EdadEmp,EstadoCivilEmp,FechaAltaEmp,SueldoEmp,DescripcionEmp,ActividadEmp,ImageEmp,UrlEmp) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText()));
					ps.setInt(2, 0);
					ps.setString(3, (String )cbCargo.getSelectedItem());
					ps.setNString(4,textNom.getText().trim());
					ps.setNString(5,textDir.getText().trim());
					ps.setInt(6, Integer.parseInt(textDni.getText()));
					ps.setString(7, textRuc.getText().trim());
					ps.setString(8, (String)cbProfesion.getSelectedItem());
					ps.setString(9, textTel.getText().trim());
					ps.setString(10, (String)cbSexo.getSelectedItem());
					ps.setString(11, textFecNacio.getText().trim());
					ps.setString(12, textEda.getText().trim());
					ps.setString(13, (String)cbCivil.getSelectedItem());
					ps.setString(14, FechaAlta.trim());
					ps.setFloat(15,  Float.parseFloat(textFormatSueldo.getText()));
					ps.setString(16, textArea.getText().trim());
					ps.setString(17, (String)cbEstado.getSelectedItem());
					
					ps.setString(19, almacenaFoto);
					// VERIFICA SI SE GUARDARA UNA IMAGEN
					if ((almacenaFoto==null)|| (almacenaFoto=="")){
						almacenaFoto="sin image";
						ps.setString(18,almacenaFoto);
					}else {
						archivofoto = new FileInputStream(almacenaFoto);
						ps.setBinaryStream(18,archivofoto);
					}
					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					nuevo();
					limpiarTexts();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
				}
				resultSet.close();
				statement.close();
			}
			if (MOD==1){// MODIFICAR :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				try {
			        FileInputStream  archivofoto;
			         String sql="UPDATE EMPLEADO SET Id_Emp = ?,"
			                 //+ "Id_Hor = ?,"
			                 + "CargoEmp = ?,"
			                 + "NombresEmp = ?,"
			                 + "DireccionEmp =?,"
			                 + "DniEmp = ?,"
			                 + "RucEmp=?,"
			                 + "ProfesionEmp =?,"
			                 + "TelefonoEmp = ?,"
			                 + "SexoEmp = ?,"
							 + "FechaNacioEmp =?,"
			                 + "EdadEmp =?,"
			                 + "EstadoCivilEmp = ?,"
			                 + "SueldoEmp =?,"
			                 + "DescripcionEmp=?,"
			                 + "ActividadEmp =?,"
			                 + "ImageEmp=?,"
			                 + "URLEmp=?,"
			                 + "FechaAltaEmp=?"
			                 
			                 + "WHERE Id_Emp = '"+textCod.getText()+"'"; 
			         
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText()));
					//ps.setInt(2, 0);
					ps.setString(2, (String )cbCargo.getSelectedItem());
					ps.setNString(3,textNom.getText().trim());
					ps.setNString(4,textDir.getText().trim());
					ps.setInt(5, Integer.parseInt(textDni.getText()));
					ps.setString(6, textRuc.getText().trim());
					ps.setString(7, (String)cbProfesion.getSelectedItem());
					ps.setString(8, textTel.getText().trim());
					ps.setString(9, (String)cbSexo.getSelectedItem());
					ps.setString(10, textFecNacio.getText().trim());
					ps.setString(11, textEda.getText().trim());
					ps.setString(12, (String)cbCivil.getSelectedItem());
					ps.setFloat(13,  Float.parseFloat(textFormatSueldo.getText()));
					ps.setString(14, textArea.getText().trim());
					ps.setString(15, (String)cbEstado.getSelectedItem());
					
					ps.setString(17, almacenaFoto);
					// VERIFICA SI SE GUARDARA UNA IMAGEN
					if ((almacenaFoto==null)|| (almacenaFoto=="")){
						almacenaFoto="sin image";
						ps.setString(16,almacenaFoto);
					}else {
						archivofoto = new FileInputStream(almacenaFoto);
						ps.setBinaryStream(16,archivofoto);
					}
					ps.setString(18, FechaAlta.trim());
					ps.executeUpdate();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					frame.dispose();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
				
			}
		} catch (Exception e) {e.printStackTrace();}
		conexion.DesconectarDB();
	}
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textDni){textDni.setBackground(Menu.textColorBackgroundActivo);} 
			if (ev.getSource() == textRuc){textRuc.setBackground(Menu.textColorBackgroundActivo);} 
			if (ev.getSource() == textDir){textDir.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbProfesion){cbProfesion.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textTel){textTel.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbSexo){cbSexo.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFormatSueldo){textFormatSueldo.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textEda){textEda.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFecNacio){textFecNacio.setBackground(Menu.textColorBackgroundActivo);}
			//FORE
			if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textDni){textDni.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textRuc){textRuc.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textDir){textDir.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbProfesion){cbProfesion.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textTel){textTel.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbSexo){cbSexo.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textFormatSueldo){textFormatSueldo.setForeground(Menu.textColorForegroundActivo);}
			
			if (ev.getSource() == textEda){textEda.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textFecNacio){textFecNacio.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textDni){textDni.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textRuc){textRuc.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textDir){textDir.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == cbProfesion){cbProfesion.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textTel){textTel.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == cbSexo){cbSexo.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textFormatSueldo){textFormatSueldo.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textEda){textEda.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textFecNacio){textFecNacio.setBackground(Menu.textColorBackgroundInactivo);}
			//FORE
			if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textDni){textDni.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textRuc){textRuc.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textDir){textDir.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbProfesion){cbProfesion.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textTel){textTel.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbSexo){cbSexo.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textFormatSueldo){textFormatSueldo.setForeground(Menu.textColorForegroundInactivo);}
			
			if (ev.getSource() == textEda){textEda.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textFecNacio){textFecNacio.setForeground(Menu.textColorForegroundInactivo);} 
		}
		
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonGrabar){// GRABAR
					insertarUpdate();
				  }
			  if (evento.getSource() == buttonSalir){// SALIR
				  frame.dispose();
				  }

			  
				
			  if (evento.getSource() == buttonImg){
			  	int resultado;
				archivo = new AbrirFoto();
				archivo.frame.toFront();
				resultado= AbrirFoto.fileChooser.showOpenDialog(null);
				if (JFileChooser.APPROVE_OPTION == resultado){
					fichero= AbrirFoto.fileChooser.getSelectedFile();
					try {
						almacenaFoto="";
						ImageIcon icon = new ImageIcon(fichero.toString());
						Icon icono = new ImageIcon(icon.getImage().getScaledInstance(lblFto.getWidth(), lblFto.getHeight(), Image.SCALE_DEFAULT));        		            
			            almacenaFoto=(String.valueOf(fichero));
			            lblFto.setText(null);
			            lblFto.setIcon(icono);
					} catch (Exception e) {JOptionPane.showMessageDialog(null, "Error al abrir la imagen" + e);}}
				  }
			}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			
				if (evet.getSource() == textNom){
					int pos = textNom.getCaretPosition();textNom.setText(textNom.getText().toUpperCase());textNom.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textNom.getText().toLowerCase().isEmpty()|| textNom.getText().toLowerCase().length()>75){
						textNom.requestFocus();
						textNom.selectAll();
						textNom.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textNom.getText().toLowerCase().length()==75){
							textDir.requestFocus();
							textDir.selectAll();	
						}
				} 
				if (evet.getSource() == textDir){
					int pos = textDir.getCaretPosition();textDir.setText(textDir.getText().toUpperCase());textDir.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textDir.getText().toLowerCase().isEmpty()|| textDir.getText().toLowerCase().length()>95){
						textDir.requestFocus();
						textDir.selectAll();
						textDir.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textDir.getText().toLowerCase().length()==95){
							textDni.requestFocus();
							textDni.selectAll();	
						}
				} 
				if (evet.getSource() == textDni){
					if (textDni.getText().toLowerCase().isEmpty()|| textDni.getText().toLowerCase().length()>8){
						textDni.requestFocus();
						textDni.selectAll();
						textDni.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textDni.getText().toLowerCase().length()==8){
							textRuc.requestFocus();
							textRuc.selectAll();	
						}
				} 
				if (evet.getSource() == textRuc){
					if (textRuc.getText().length()>11){
						textRuc.requestFocus();
						textRuc.selectAll();
						textRuc.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textRuc.getText().toLowerCase().length()==11){
							textTel.requestFocus();
							textTel.selectAll();	
						}
				} 
				if (evet.getSource() == textTel){
					int pos = textTel.getCaretPosition();textTel.setText(textTel.getText().toUpperCase());textTel.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textTel.getText().length()>65){
						textTel.requestFocus();
						textTel.selectAll();
						textTel.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textTel.getText().toLowerCase().length()==65){
							cbProfesion.requestFocus();
						}
				} 
				if (evet.getSource() == cbProfesion){
					if (e==KeyEvent.VK_ENTER){
						if (cbProfesion.getSelectedIndex()!=-1){
							cbSexo.requestFocus();
						}else{
							cbProfesion.requestFocus();
						}
					}	
				}
				if (evet.getSource() == cbSexo){
					if (e==KeyEvent.VK_ENTER){
						if (cbSexo.getSelectedIndex()!=-1){
							textFecNacio.requestFocus();
						}else{
							cbSexo.requestFocus();
						}
					}	
				}
				if (evet.getSource() == textFecNacio){
					if (textFecNacio.getText().toLowerCase().isEmpty()|| textFecNacio.getText().toLowerCase().length()>14){
						textFecNacio.requestFocus();
						textFecNacio.selectAll();
						textFecNacio.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textFecNacio.getText().toLowerCase().length()==14){
							textEda.requestFocus();
							textEda.selectAll();	
						}
				} 
				if (evet.getSource() == textEda){
					if (textEda.getText().toLowerCase().isEmpty()|| textEda.getText().toLowerCase().length()>3){
						textEda.requestFocus();
						textEda.selectAll();
						textEda.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textEda.getText().toLowerCase().length()==3){
							cbCivil.requestFocus();
						}
				}
				if (evet.getSource() == cbCivil){
					if (e==KeyEvent.VK_ENTER){
						if (cbCivil.getSelectedIndex()!=-1){
							cbCargo.requestFocus();
						}else{
							cbCivil.requestFocus();
						}
					}	
				}
				if (evet.getSource() == cbCargo){
					if (e==KeyEvent.VK_ENTER){
						if (cbCargo.getSelectedIndex()!=-1){
							textFormatSueldo.requestFocus();
						}else{
							cbCargo.requestFocus();
						}
					}	
				}
				if (evet.getSource() == textFormatSueldo){
					if (textFormatSueldo.getText().toLowerCase().isEmpty()|| textFormatSueldo.getText().toLowerCase().length()>8){
						textFormatSueldo.requestFocus();
						textFormatSueldo.selectAll();
						textFormatSueldo.setText(null);
						} 
						 if (e==KeyEvent.VK_ENTER){
							 buttonGrabar.doClick();
						}
				}
				if (evet.getSource() == cbEstado){
					if (e==KeyEvent.VK_ENTER){
						if (cbEstado.getSelectedIndex()!=-1){
							buttonGrabar.doClick();
						}else{
							cbEstado.requestFocus();
						}
					}	
				}
				if (evet.getSource() == textArea){
					int pos = textArea.getCaretPosition();textArea.setText(textArea.getText().toUpperCase());textArea.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textArea.getText().toLowerCase().isEmpty()|| textArea.getText().toLowerCase().length()>195){
						textArea.requestFocus();
						textArea.selectAll();
						textArea.setText(null);
						} 
						 if (e==KeyEvent.VK_ENTER || textArea.getText().toLowerCase().length()==195){
							 buttonGrabar.doClick();
						}
				}
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char e=evet.getKeyChar();
			if (evet.getSource() == textNom){ 
				if(!Character.isLetter(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textDir){ 
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textDni){
				if(!Character.isDigit(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textRuc){
				if(!Character.isDigit(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textTel){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textFecNacio){
				if((e<'0' || e>'9') && (e<'/' || e>'/'))evet.consume();
			}
			if (evet.getSource() == textEda){
				if((e<'0' || e>'9'))evet.consume();
			}
			if (evet.getSource() == textFormatSueldo){
				if ((e<'0'||e>'9')&&(e<'.'||e>'.'))evet.consume();
			}
			if (evet.getSource() == textArea){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			

		}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource()==(dateChooser)){
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			FechaAlta= form.format(dateChooser.getDate());
		}
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		//char e=evet.getKeyChar();
		
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
	public void mousePressed(MouseEvent press) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseReleased(MouseEvent Mouse_evt) {
		// TODO Auto-generated method stub
			try {
				if (Mouse_evt.getClickCount() == 1) {
	
				}
				if (Mouse_evt.getClickCount() == 2) {
					
				}
			} catch (Exception e) {}
	}
}
