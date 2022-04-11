package modelo.Presentacion;

import java.awt.Color;
import java.awt.Image;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Clases.ListCliente;
import modelo.Clases.TableCellRendererColor;
import modelo.Datos.ConexionDB;
import modelo.Negocio.DatasourceCliente;
import modelo.Otros.JTextFieldIcon;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;

public class VentanaCliente implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public  JDialog frame;
	private JPanel  			panelDto,panelBtn,panelLst,panelHtl,panelContenedor1;
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl6,lbl7,lbl8,lbl9,lblFto,
								lbl10,lbl11,lbl12,lbl13,lbl14,lbl15,lblMs;
	protected JTextField 			textCod,textNom,textDni,textRuc,textDir,textTel,textBus,textRazEmp,textDirEmp,textRucEmp;
	private JButton  			buttonNuevo,buttonCancelar,buttonGrabar,buttonEditar,buttonEliminar,buttonSalir,
			 					buttonPrimero,buttonUltimo,buttonSiguiente,buttonAnterior,
			 					buttonImg,buttonBus,
			 					buttonOki,buttonImp,buttonReg,buttonEnviar;
	private JFormattedTextField textFormatDsct;
	JComboBox<String> cbNacionalidad,cbTipoCliente,cbTipoDocumento,cbBus,cbTipoDsct;
    
	private JScrollPane scrollArea,scrollTable;
	private JTextArea textArea = new JTextArea();
	private JTable tableList;
	private DefaultTableModel model;
	
	public File fichero;
	private String almacenaFoto;
	private AbrirFoto archivo;
	
	public Integer totalitems;
	int i;
	double codigo=0;
	
	DecimalFormat formatId = new DecimalFormat("0000000");
	NumberFormat formatDsct;
	private JTabbedPane tabbedPane;
	public static String FILTRO_CLI="";

	private String consultar="";
	
	protected static int V_ID_CLIENTE=0;
	protected static String V_DETALLE_CLIENTE="";
	protected static int V_MOD_CLIENTE=0;
	
	public VentanaCliente() {
		frameCliente();
		crearTabbed();
		crearPanel();
		crearButtons();
		crearTable();
		crearComboBox();
		crearTextFields();
		crearLabels();
		activarButton(true);
		textCod.setEditable(false);
		llenarcbBuscar();
		llenarTabbed();
		panelContenedor1.add(panelDto).setVisible(false);
		panelContenedor1.add(panelLst).setVisible(true);
      	llenarTable("Select * from Clientes order by Id_Cli desc");

	}
	public void frameCliente() {
		frame = new JDialog();
		frame.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent arg0) {
				textBus.requestFocusInWindow();
			}
			public void windowLostFocus(WindowEvent arg0) {
			}
		});
		frame.setResizable(false);
		frame.setTitle("Carpeta de mantenimiento para huesped / cliente");
		frame.setBounds(100, 100, 672, 387);
		frame.getContentPane().setLayout(null);
		frame.setModal(true);
		frame.setLocationRelativeTo(null);
	}
	public void crearTabbed() {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(5, 55, 659, 299);
	}
	public void llenarTabbed(){
		tabbedPane.addTab("Datos generales", null, panelContenedor1, null);
		tabbedPane.addTab("Facturar a...", panelHtl);
		frame.getContentPane().add(tabbedPane);
	}
	public void crearPanel() {
		panelContenedor1 = new JPanel();
		panelContenedor1.setBorder(null);
		panelContenedor1.setBounds(668, 65, 659, 268); //10, 333, 659, 268
		tabbedPane.add(panelContenedor1);
		
		panelBtn = new JPanel();
		panelBtn.setBorder(new TitledBorder(null, "Controles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBtn.setBounds(5, 5, 659, 49);
		frame.getContentPane().add(panelBtn);
		panelBtn.setLayout(null);
		panelContenedor1.setLayout(null);
		
		panelDto = new JPanel();
		panelDto.setBounds(0, 0, 654, 268);
		panelDto.setBorder(new TitledBorder(null, "!", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelContenedor1.add(panelDto);
		panelDto.setLayout(null);
		
		panelLst = new JPanel();
		panelLst.setBounds(0, 0, 654, 268);
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelContenedor1.add(panelLst);
		panelLst.setLayout(null);
		
		panelHtl = new JPanel();
		panelHtl.setBorder(null);
		panelHtl.setBounds(10, 335, 480, 101);//170, 103, 267, 158
		//frame.getContentPane().add(panelHtl);
		tabbedPane.add(panelHtl);
		panelHtl.setLayout(null);
	}
	
	public void crearLabels() {
		lbl1= new JLabel("Código:");
		lbl1.setBounds(20, 38, 106, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Nombres:");
		lbl2.setBounds(10, 66, 116, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Tipo documento:");
		lbl3.setBounds(20, 91, 106, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Ruc:");
		lbl4.setBounds(335, 91, 45, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);

		lbl5= new JLabel("Dirección:");
		lbl5.setBounds(10, 116, 116, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);
		
		lbl6= new JLabel("Nacionalidad:");
		lbl6.setBounds(20, 140, 106, 14);
		panelDto.add(lbl6);
		lbl6.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl6.setFont(Menu.fontLabel);

		lbl7= new JLabel("Teléfono:");
		lbl7.setBounds(262, 140, 77, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Descripción:");
		lbl8.setBounds(20, 180, 106, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		lblFto= new JLabel("Sin image");
		lblFto.setBounds(481, 16, 162, 122);
		panelDto.add(lblFto);
		lblFto.setHorizontalAlignment(SwingConstants.CENTER);
		lblFto.setBorder(new LineBorder(new Color(211, 211, 211)));
		
		lbl9= new JLabel("Elija el tipo de búsqueda:");
		lbl9.setBounds(91, 17, 137, 14);
		panelLst.add(lbl9);
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(Menu.fontLabel);
		
		lbl10= new JLabel("Tip Cliente:");
		lbl10.setBounds(473, 175, 56, 14);
		panelDto.add(lbl10);
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(Menu.fontLabel);
		
		lbl11= new JLabel("Tip Dsct:");
		lbl11.setBounds(473, 200, 56, 14);
		panelDto.add(lbl11);
		lbl11.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl11.setFont(Menu.fontLabel);
		
		lbl12= new JLabel("Dsct :");
		lbl12.setBounds(473, 225, 56, 14);
		panelDto.add(lbl12);
		lbl12.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl12.setFont(Menu.fontLabel);
		
		lbl13= new JLabel("Razón social:");
		lbl13.setBounds(101, 90, 71, 14);
		panelHtl.add(lbl13);
		lbl13.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl13.setFont(Menu.fontLabel);
		
		lbl14= new JLabel("Dirección:");
		lbl14.setBounds(101, 113, 71, 14);
		panelHtl.add(lbl14);
		lbl14.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl14.setFont(Menu.fontLabel);
		
		lbl15= new JLabel("Ruc:");
		lbl15.setBounds(101, 138, 71, 14);
		panelHtl.add(lbl15);
		lbl15.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl15.setFont(Menu.fontLabel);
		
		lblMs= new JLabel("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		lblMs.setForeground(new Color(0, 128, 128));
		lblMs.setBounds(181, 62, 335, 21);
		panelHtl.add(lblMs);
		lblMs.setHorizontalAlignment(SwingConstants.CENTER);
		lblMs.setFont(Menu.fontLabel);
		lblMs.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panelDto.setVisible(false);// PANEL DATOS
		panelLst.setVisible(true); // PANEL LISTA
	}
	public void crearTextFields(){
		textCod = new JTextField();
		textCod.setColumns(10);
		textCod.setFont(Menu.fontText);
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.setHorizontalAlignment(SwingConstants.CENTER);
		textCod.addActionListener(this);
		textCod.addFocusListener(this);
		textCod.addKeyListener(this);
		textCod.setBounds(136, 35, 106, 22);
		panelDto.add(textCod);
		
		textNom = new JTextField();
		textNom.setColumns(10);
		textNom.setFont(Menu.fontText);
		textNom.setForeground(Menu.textColorForegroundInactivo);
		textNom.setHorizontalAlignment(SwingConstants.LEFT);
		textNom.addActionListener(this);
		textNom.addFocusListener(this);
		textNom.addKeyListener(this);
		textNom.setBounds(136, 60, 335, 22);
		panelDto.add(textNom);
		
		textDni = new JTextField();
		textDni.setColumns(10);
		textDni.setFont(Menu.fontText);
		textDni.setForeground(Menu.textColorForegroundInactivo);
		textDni.setHorizontalAlignment(SwingConstants.CENTER);
		textDni.addActionListener(this);
		textDni.addFocusListener(this);
		textDni.addKeyListener(this);
		textDni.setBounds(246, 85, 106, 22);
		
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
		textRuc.setBounds(385, 85, 86, 22);
		panelDto.add(textRuc);
		
		textDir = new JTextField();
		textDir.setColumns(10);
		textDir.setFont(Menu.fontText);
		textDir.setForeground(Menu.textColorForegroundInactivo);
		textDir.setHorizontalAlignment(SwingConstants.LEFT);
		textDir.addActionListener(this);
		textDir.addFocusListener(this);
		textDir.addKeyListener(this);
		textDir.setBounds(136, 111, 335, 22);
		panelDto.add(textDir);

		
		textTel = new JTextField();
		textTel.setColumns(10);
		textTel.setFont(Menu.fontText);
		textTel.setForeground(Menu.textColorForegroundInactivo);
		textTel.setHorizontalAlignment(SwingConstants.CENTER);
		textTel.addActionListener(this);
		textTel.addFocusListener(this);
		textTel.addKeyListener(this);
		textTel.setBounds(344, 136, 127, 22);
		panelDto.add(textTel);
		
		scrollArea= new JScrollPane();
		scrollArea.setBounds(136, 164, 335, 79);
		panelDto.add(scrollArea);
		textArea = new JTextArea();
		scrollArea.setViewportView(textArea);
		textArea.setFont(Menu.fontText);
		textArea.setForeground(Menu.textColorForegroundInactivo);
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);

		textBus = new  JTextFieldIcon(new JTextField(),"searchBlue.png","Search","searchBlue.png");
		textBus.setColumns(10);
		textBus.setFont(Menu.fontText);
		textBus.setForeground(Menu.textColorForegroundInactivo);
		textBus.setHorizontalAlignment(SwingConstants.LEFT);
		textBus.addActionListener(this);
		textBus.addFocusListener(this);
		textBus.addKeyListener(this);
		textBus.addPropertyChangeListener(this);
		textBus.setBounds(230, 31, 282, 21);
		panelLst.add(textBus);
		
		textFormatDsct = new JFormattedTextField(formatDsct);
		textFormatDsct.setText("0.00");
		textFormatDsct.setColumns(10);
		textFormatDsct.setFont(Menu.fontText);
		textFormatDsct.setForeground(Menu.textColorForegroundInactivo);
		textFormatDsct.setHorizontalAlignment(SwingConstants.LEFT);
		textFormatDsct.addActionListener(this);
		textFormatDsct.addFocusListener(this);
		textFormatDsct.addKeyListener(this);
		textFormatDsct.addPropertyChangeListener("values",this);
		textFormatDsct.setBounds(531, 221, 45, 22);
		panelDto.add(textFormatDsct);
		
		
		/// EMPRESA
		textRazEmp = new JTextField();
		textRazEmp.setColumns(10);
		textRazEmp.setFont(Menu.fontText);
		textRazEmp.setForeground(Menu.textColorForegroundInactivo);
		textRazEmp.setHorizontalAlignment(SwingConstants.LEFT);
		textRazEmp.addActionListener(this);
		textRazEmp.addFocusListener(this);
		textRazEmp.addKeyListener(this);
		textRazEmp.setBounds(181, 86, 335, 22);
		panelHtl.add(textRazEmp);

		textDirEmp = new JTextField();
		textDirEmp.setColumns(10);
		textDirEmp.setFont(Menu.fontText);
		textDirEmp.setForeground(Menu.textColorForegroundInactivo);
		textDirEmp.setHorizontalAlignment(SwingConstants.LEFT);
		textDirEmp.addActionListener(this);
		textDirEmp.addFocusListener(this);
		textDirEmp.addKeyListener(this);
		textDirEmp.setBounds(181, 109, 335, 22);
		panelHtl.add(textDirEmp);
		
		textRucEmp = new JTextField();
		textRucEmp.setColumns(10);
		textRucEmp.setFont(Menu.fontText);
		textRucEmp.setForeground(Menu.textColorForegroundInactivo);
		textRucEmp.setHorizontalAlignment(SwingConstants.LEFT);
		textRucEmp.addActionListener(this);
		textRucEmp.addFocusListener(this);
		textRucEmp.addKeyListener(this);
		textRucEmp.setBounds(181, 134, 141, 22);
		panelHtl.add(textRucEmp);

	}
	
	public void crearComboBox() { 
		cbTipoDocumento = new JComboBox<>();
		cbTipoDocumento.setBounds(136, 85, 106, 21);
		cbTipoDocumento.setFont(Menu.fontText);
		cbTipoDocumento.removeAllItems();
		cbTipoDocumento.addFocusListener(this);
		cbTipoDocumento.addKeyListener(this);
		panelDto.add(cbTipoDocumento);
		
		cbNacionalidad = new JComboBox<>();
		cbNacionalidad.setBounds(136, 136, 148, 21);
		cbNacionalidad.setFont(Menu.fontText);
		cbNacionalidad.removeAllItems();
		cbNacionalidad.addFocusListener(this);
		cbNacionalidad.addKeyListener(this);
		panelDto.add(cbNacionalidad);
		
		cbBus = new JComboBox<>();
        cbBus.setBounds(10, 31, 218, 21);
        cbBus.setFont(Menu.fontText);
        cbBus.removeAllItems();
        cbBus.addFocusListener(this);
        cbBus.addKeyListener(this);
        panelLst.add(cbBus);
        
        cbTipoCliente = new JComboBox<>();
        cbTipoCliente.setBounds(531, 172, 111, 21);
        cbTipoCliente.setFont(Menu.fontText);
        cbTipoCliente.removeAllItems();
        cbTipoCliente.addActionListener(this);
        cbTipoCliente.addFocusListener(this);
        cbTipoCliente.addKeyListener(this);
        panelDto.add(cbTipoCliente);
        
        cbTipoDsct = new JComboBox<>();
        cbTipoDsct.setBounds(531, 197, 61, 21);
        cbTipoDsct.setFont(Menu.fontText);
        cbTipoDsct.removeAllItems();
        cbTipoDsct.addFocusListener(this);
        cbTipoDsct.addKeyListener(this);
        panelDto.add(cbTipoDsct);
	}
	public void llenarcbNacionalidad() { 
		cbNacionalidad.removeAllItems();
		String [] lista = 	{
						  	"AFGANISTÁN","ALBANIA","ALEMANIA","ANDORRA","ANGOLA","ANTIGUA Y BARBUDA","ARABIA SAUDITA","ARGELIA","ARGENTINA","ARMENIA",
						  	"AUSTRALIA","AUSTRIA","AZERBAIYÁN","BAHAMAS","BARBADOS","BARÉIN","BÉLGICA","BELICE","BENÍN","BIELORRUSIA",
						  	"BIRMANIA","BOLIVIA","BOSNIA Y HERZEGOVINA","BOTSUANA","BRASIL","BRUNÉI","BULGARIA","BURKINA FASO","BURUNDI",
						  	"BUTÁN","CABO VERDE","CAMBOYA","CAMERÚN","CANADÁ","CATAR","CHAD","CHILE","CHINA","CHIPRE",
						  	"CIUDAD DEL VATICANO","COLOMBIA","COMORAS","COREA DEL NORTE","COREA DEL SUR","COSTA DE MARFIL","COSTA RICA","CROACIA","CUBA","DINAMARCA",
						  	"DOMINICA","ECUADOR","EGIPTO","EL SALVADOR","EMIRATOS ÁRABES UNIDOS","ERITREA","ESLOVAQUIA","ESLOVENIA","ESPAÑA","ESTADOS UNIDOS",
						  	"ESTONIA","ETIOPÍA","FILIPINAS","FINLANDIA","FIYI","FRANCIA","GABÓN","GAMBIA","GEORGIA","GHANA",
						  	"GRANADA","GRECIA","GUATEMALA","GUYANA","GUINEA","GUINEA ECUATORIAL","GUINEA-BISÁU","HAITÍ","HONDURAS","HUNGRÍA",
						  	"INDIA","INDONESIA","IRAK","IRÁN","IRLANDA","ISLANDIA","ISLAS MARSHALL","ISLAS SALOMÓN","ISRAEL","ITALIA",
						  	"JAMAICA","JAPÓN","JORDANIA","KAZAJISTÁN","KENIA","KIRGUISTÁN","KIRIBATI","KUWAIT","LAOS","LESOTO",
						  	"LÍBANO","LIBERIA","LIBIA","LIECHTENSTEIN","LITUANIA","LUXEMBURGO","MADAGASCAR","MALASIA","MALAUI","MALDIVAS",
						  	"MALÍ","MARRUECOS","MAURICIO","MAURITANIA","MÉXICO","MICRONESIA","MOLDAVIA","MÓNACO","MONGOLIA","MONTENEGRO",
						  	"MOZAMBIQUE","NAMIBIA","NAURU","NEPAL","NICARAGUA","NÍGER","NIGERIA","NORUEGA","NUEVA ZELANDA","OMÁN",
						  	"PAÍSES BAJOS","PAKISTÁN","PALAOS","PANAMÁ","PAPÚA NUEVA GUINEA","PARAGUAY","PERÚ","POLONIA","PORTUGAL","REINO UNIDO",
						  	"REPÚBLICA CENTROAFRICANA","REPÚBLICA CHECA","REPÚBLICA DE MACEDONIA","REPÚBLICA DEL CONGO","REPÚBLICA DEMOCRÁTICA DEL CONGO","REPÚBLICA DOMINICANA","REPÚBLICA SUDAFRICANA","RUANDA","RUMANÍA","RUSIA",
						  	"SAMOA","SAN CRISTÓBAL Y NIEVES","SAN MARINO","SAN VICENTE Y LAS GRANADINAS","SANTA LUCÍA","SANTO TOMÉ Y PRÍNCIPE","SENEGAL","SERBIA","SEYCHELLES","SIERRA LEONA",
						  	"SINGAPUR","SIRIA","SOMALIA","SRI LANKA","SUAZILANDIA","SUDÁN","SUDÁN DEL SUR","SUECIA","SUIZA","SURINAM",
						  	"TAILANDIA","TANZANIA","TAYIKISTÁN","TIMOR ORIENTAL","TOGO","TONGA","TRINIDAD Y TOBAGO","TÚNEZ","TURKMENISTÁN","TURQUÍA",
						  	"TUVALU","UCRANIA","UGANDA","URUGUAY","UZBEKISTÁN","VANUATU","VENEZUELA","VIETNAM","YEMEN","YIBUTI",
						  	"ZAMBIA","ZIMBABUE"};
	    Arrays.sort (lista);
		for (String llenar:lista) {
			cbNacionalidad.addItem(llenar);
		}
		cbNacionalidad.setSelectedItem("PERÚ");
		
		// LLENO TIPO DE CLIENTE
		cbTipoCliente.removeAllItems();
		String [] lista1 = 	{"OCASIONAL","HABITUAL"};
	    Arrays.sort (lista1);
		for (String llenar:lista1) {
			cbTipoCliente.addItem(llenar);
		}
		cbTipoCliente.setSelectedIndex(1);
		
		
		// LLENO TIPO DE DSCT
		cbTipoDsct.removeAllItems();
		String [] lista2 = 	{"S/.","%"};
	    Arrays.sort (lista2);
		for (String llenar:lista2) {
			cbTipoDsct.addItem(llenar);
		}
		cbTipoDsct.setSelectedIndex(1);
		
		// LLENO EL TIPO DOCUMENTO
		cbTipoDocumento.removeAllItems();
		String [] lista3 = 	{"DNI","PASAPORTE"};
	    Arrays.sort (lista3);
		for (String llenar:lista3) {
			cbTipoDocumento.addItem(llenar);
		}
		cbTipoDocumento.setSelectedIndex(0);
	}
	
	public void llenarcbBuscar() {
        cbBus.removeAllItems();
		String [] lista1 = {"CODIGO","NOMBRE / RAZON SOCIAL","DNI","RUC","NACIONALIDAD"};
		for (String llenar:lista1) {
			cbBus.addItem(llenar);
		}
		cbBus.setSelectedIndex(1);
	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setToolTipText("Nuevo ítem");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setBounds(424, 20, 36, 23);
		buttonNuevo.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/nuevo.png")));
		panelBtn.add(buttonNuevo);
		
		buttonCancelar= new JButton("");
		buttonCancelar.setToolTipText("Deshacer ítem");
		buttonCancelar.addActionListener(this);
		buttonCancelar.setBounds(462, 20, 36, 23);
		buttonCancelar.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/undo.png")));
		panelBtn.add(buttonCancelar);
		
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(500, 20, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/save.png")));
		panelBtn.add(buttonGrabar);

		buttonEditar= new JButton("");
		buttonEditar.setToolTipText("Modificar ítem");
		buttonEditar.addActionListener(this);
		buttonEditar.setBounds(538, 20, 36, 23);
		buttonEditar.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/edit.png")));
		panelBtn.add(buttonEditar);
		
		buttonEliminar= new JButton("");
		buttonEliminar.setToolTipText("Eliminar ítem");
		buttonEliminar.addActionListener(this);
		buttonEliminar.setBounds(576, 20, 36, 23);
		buttonEliminar.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/delete.png")));
		panelBtn.add(buttonEliminar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(614, 20, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/Exit.png")));
		panelBtn.add(buttonSalir);
		
		buttonPrimero= new JButton("");
		buttonPrimero.setToolTipText("Primer ítem");
		buttonPrimero.addActionListener(this);
		buttonPrimero.setBounds(18, 20, 36, 23);
		buttonPrimero.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/hide-left.png")));
		panelBtn.add(buttonPrimero);

		buttonUltimo= new JButton("");
		buttonUltimo.setToolTipText("Anterior ítem");
		buttonUltimo.addActionListener(this);
		buttonUltimo.setBounds(56, 20, 36, 23);
		buttonUltimo.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/navigate-left.png")));
		panelBtn.add(buttonUltimo);

		buttonSiguiente= new JButton("");
		buttonSiguiente.setToolTipText("Siguiente ítem");
		buttonSiguiente.addActionListener(this);
		buttonSiguiente.setBounds(94, 20, 36, 23);
		buttonSiguiente.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/navigate-right.png")));
		panelBtn.add(buttonSiguiente);

		buttonAnterior= new JButton("");
		buttonAnterior.setToolTipText("Ultimo ítem");
		buttonAnterior.addActionListener(this);
		buttonAnterior.setBounds(132, 20, 36, 23);
		buttonAnterior.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/hide-right.png")));
		panelBtn.add(buttonAnterior);
		
		buttonImg= new JButton("Agregar imagen..");
		buttonImg.setToolTipText("Agregar imagen");
		buttonImg.addActionListener(this);
		buttonImg.setBounds(481, 138, 163, 23);
		buttonImg.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/blue.png")));
		panelDto.add(buttonImg);
		
		buttonBus= new JButton("");
		buttonBus.setBounds(243, 34, 36, 24);
		buttonBus.setToolTipText("Buscar");
		buttonBus.addActionListener(this);
		buttonBus.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/searchNormal.png")));
		panelDto.add(buttonBus);
		
		// BUTTON DE PANEL LST
		buttonOki= new JButton("");
		buttonOki.setToolTipText("Filtrar");
		buttonOki.addActionListener(this);
		buttonOki.setBounds(515, 31, 36, 22);
		buttonOki.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/ok.png")));
		panelLst.add(buttonOki);
		
		buttonImp= new JButton("");
		buttonImp.setToolTipText("Ver Lista");
		buttonImp.addActionListener(this);
		buttonImp.setBounds(561, 31, 36, 22);
		buttonImp.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/old-versions.png")));
		panelLst.add(buttonImp);
		
		buttonReg= new JButton("");
		buttonReg.setToolTipText("Regresar al registro");
		buttonReg.addActionListener(this);
		buttonReg.setBounds(598, 31, 36, 22);
		buttonReg.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/mant-cancelar.png")));
		panelLst.add(buttonReg);
		
		buttonEnviar= new JButton("Enviar datos");
		buttonEnviar.setToolTipText("");
		buttonEnviar.addActionListener(this);
		buttonEnviar.setBounds(325, 135, 191, 21);
		buttonEnviar.setIcon(new ImageIcon(VentanaCliente.class.getResource("/modelo/Images/go-into.png")));
		panelHtl.add(buttonEnviar);
		
	}
	public void crearTable(){
		tableList = new JTable(); 
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tableList.setBounds(10, 303, 665, 229);
		tableList.addMouseListener(this);
		tableList.addKeyListener(this);
		
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 56, 627, 204);
        panelLst.add(scrollTable);
        
    	tableList.setShowHorizontalLines(false);
    	tableList.setShowVerticalLines(true);
    	tableList.setFillsViewportHeight(true);
    	tableList.setGridColor(new Color(153,204,255));
	}
	
	public void limpiarTexts() {

		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.requestFocus(true);
		
		textNom.setText(null);
		textNom.setBackground(Menu.textColorBackgroundInactivo);	
		textNom.setForeground(Menu.textColorForegroundInactivo);

		textDni.setText(null);
		textDni.setBackground(Menu.textColorBackgroundInactivo);	
		textDni.setForeground(Menu.textColorForegroundInactivo);
		
		textRuc.setText(null);
		textRuc.setBackground(Menu.textColorBackgroundInactivo);	
		textRuc.setForeground(Menu.textColorForegroundInactivo);
		
		textDir.setText(null);
		textDir.setBackground(Menu.textColorBackgroundInactivo);	
		textDir.setForeground(Menu.textColorForegroundInactivo);
		
		textTel.setText(null);
		textTel.setBackground(Menu.textColorBackgroundInactivo);	
		textTel.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
		
		textBus.setText(null);
		textBus.setBackground(Menu.textColorBackgroundInactivo);	
		textBus.setForeground(Menu.textColorForegroundInactivo);
		
		cbNacionalidad.setSelectedIndex(-1);
		cbTipoDocumento.setSelectedIndex(-1);
		cbBus.setSelectedIndex(-1);
		cbTipoCliente.setSelectedIndex(-1);
		
        almacenaFoto="";
        lblFto.setText("Sin image");
        lblFto.setIcon(null);
        
        textRuc.setText(null);
        textRuc.setBackground(Menu.textColorBackgroundInactivo);	
		textRuc.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatDsct.setText("0.00");
		textFormatDsct.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatDsct.setForeground(Menu.textColorForegroundInactivo);
		
		textRazEmp.setText(null);
		textRazEmp.setBackground(Menu.textColorBackgroundInactivo);	
		textRazEmp.setForeground(Menu.textColorForegroundInactivo);
		
		textDirEmp.setText(null);
		textDirEmp.setBackground(Menu.textColorBackgroundInactivo);	
		textDirEmp.setForeground(Menu.textColorForegroundInactivo);
		
		textRucEmp.setText(null);
		textRucEmp.setBackground(Menu.textColorBackgroundInactivo);	
		textRucEmp.setForeground(Menu.textColorForegroundInactivo);
		
		lblMs.setText("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textNom.setEnabled(b);
		textDni.setEnabled(b);
		textRuc.setEnabled(b);
		textDir.setEnabled(b);
		textTel.setEnabled(b);
		textArea.setEnabled(b);
		textBus.setEnabled(b);
		cbNacionalidad.setEnabled(b);
		cbTipoDocumento.setEnabled(b);
		cbBus.setEnabled(b);
		
		cbTipoCliente.setEnabled(b);
		//textFormatDsct.setEnabled(b);
		textRazEmp.setEnabled(b);
		textDirEmp.setEnabled(b);
		textRucEmp.setEnabled(b);
	}
	public void activarButton(boolean c) {
		 if (c == true){
			buttonNuevo.setEnabled(true); // NUEVO
			buttonCancelar.setEnabled(false);// CALCELAR
			buttonGrabar.setEnabled(false);// GRABAR
			buttonEditar.setEnabled(true);	// EDITAR
			buttonEliminar.setEnabled(true);	// ELIMINAR
			buttonSalir.setEnabled(true);	// SALIR
			
			buttonPrimero.setEnabled(true);	// PRIMERO
			buttonAnterior.setEnabled(true); // ANTERIOR
			buttonSiguiente.setEnabled(true);	// SIGUIENTE
			buttonUltimo.setEnabled(true);	// ULTIMO

			//buttonDsct.setEnabled(true);// ALTA DE DSCT A CLIENTES
			buttonImg.setEnabled(false);// AGREGAR FOTO
			buttonBus.setEnabled(true);// BUSCAR
			
			panelDto.setVisible(true);// PANEL DATOS
			panelLst.setVisible(false);// PANEL LISTA
			panelHtl.setVisible(false);// PANEL HABITUAL
		 }
		 if (c == false){
			buttonNuevo.setEnabled(false); // NUEVO
			buttonCancelar.setEnabled(true);// CALCELAR
			buttonGrabar.setEnabled(true);// GRABAR
			buttonEditar.setEnabled(false);	// EDITAR
			buttonEliminar.setEnabled(false);	// ELIMINAR
			buttonSalir.setEnabled(false);	// SALIR
			
			buttonPrimero.setEnabled(false);	// PRIMERO
			buttonAnterior.setEnabled(false); // ANTERIOR
			buttonSiguiente.setEnabled(false);	// SIGUIENTE
			buttonUltimo.setEnabled(false);	// ULTIMO

			//buttonDsct.setEnabled(true);// ALTA DE DSCT A CLIENTES
			buttonImg.setEnabled(true);// AGREGAR FOTO
			buttonBus.setEnabled(false);// BUSCAR
			
			panelDto.setVisible(true);// PANEL DATOS
			panelLst.setVisible(false);// PANEL LISTA
			panelHtl.setVisible(false);// PANEL HABITUAL
		 }
	}
	
	protected void llenarTable(String Consultar) {
		conexion = new ConexionDB();
        try {
     	   totalitems=0;
 			
		   model= new DefaultTableModel();
		   model.addColumn("Código");
		   model.addColumn("Nombre");
		   model.addColumn("# Doc.");
		   model.addColumn("Ruc");
	       model.addColumn("Dirección");
	       model.addColumn("Nacionalidad");
	       model.addColumn("Teléfono");
	       
 		   String []datos= new String[7];
 		   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(Consultar);
 		   tableList.setModel(model);
 		   
 		  try {
 	            DefaultTableModel modelo=(DefaultTableModel) tableList.getModel();
 	            int filas=tableList.getRowCount();
 	            for (int i = 0;filas>i; i++) {
 	                modelo.removeRow(0);
 	            }
 		  }catch (Exception e) {}
 		  
    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(30);
    	   tableList.getColumnModel().getColumn(1).setPreferredWidth(220);
    	   tableList.getColumnModel().getColumn(2).setPreferredWidth(25);
    	   tableList.getColumnModel().getColumn(3).setPreferredWidth(50);
    	   tableList.getColumnModel().getColumn(4).setPreferredWidth(125);

           while(rs.next()) {
            datos[0]=" "+formatId.format(rs.getInt("Id_Cli"));
            datos[1]=" "+rs.getString("NombreCli");
            datos[2]=" "+rs.getString("DniCli");
            datos[3]=" "+rs.getString("RucCli");
            datos[4]=" "+rs.getString("DireccionCli");
            datos[5]=" "+rs.getString("NacionalidadCli");
            datos[6]=" "+rs.getString("TelefonoCli");
            totalitems=totalitems+1;
            model.addRow(datos);
            tableList.setModel(model);
	        }
            rs.close();
           	st.close();
           	
           // MODELO TABLE
    	   /*int CONT=11;
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
		   }*/
    	   // FIN MODELOTABLE
            tableList.getColumnModel().getColumn(5).setMaxWidth(0);
            tableList.getColumnModel().getColumn(5).setMinWidth(0);
            tableList.getColumnModel().getColumn(6).setMaxWidth(0);
            tableList.getColumnModel().getColumn(6).setMinWidth(0);
            
            tableList.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(0);
            tableList.getTableHeader().getColumnModel().getColumn(5).setMinWidth(0);
            tableList.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);
            tableList.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);
            
            tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("HABITACION"));//ESTABLESCO COLOR CELDAS
            tableList.getSelectionModel().setSelectionInterval (0, 0); //SELECCIONA EN LA PRIMERA FILA Y INTERVALOS O GRUPOS
            
           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
        textBus.requestFocus();
	}

	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}

	//METODO BUSCAR =================
	public void buscar() {
		conexion = new ConexionDB();
		try {
			llenarcbNacionalidad();
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(consultar);
			if (resultSet.next()== true) {
				// LIMPIO MI TABLA
				//LimpiarTable();
				
				V_MOD_CLIENTE=1;
				
		        almacenaFoto="";
		        lblFto.setText("Sin image");
		        lblFto.setIcon(null);
				int id=(Integer.parseInt(resultSet.getString("Id_Cli")));
				textCod.setText(formatId.format(id));
				textNom.setText(resultSet.getString("NombreCli"));
				textDni.setText(resultSet.getString("DniCli"));
				textRuc.setText(resultSet.getString("RucCli"));
				textDir.setText(resultSet.getString("DireccionCli"));
				cbNacionalidad.setSelectedItem(resultSet.getString("NacionalidadCli"));
				cbTipoDocumento.setSelectedItem(resultSet.getString("TipoDocumentoCli"));
				textTel.setText(resultSet.getString("TelefonoCli"));
				textArea.setText(resultSet.getString("ObservacionCli"));
				almacenaFoto=resultSet.getString("URLCli");
				
				cbTipoCliente.setSelectedItem(resultSet.getString("TipoCli"));
				cbTipoDsct.setSelectedItem(resultSet.getString("TipoDsctCli"));
				textFormatDsct.setText(resultSet.getString("DsctCli"));
				textRazEmp.setText(resultSet.getString("RazonCliEmp"));
				textDirEmp.setText(resultSet.getString("DireccionCliEmp"));
				textRucEmp.setText(resultSet.getString("RucCliEmp"));
				
				panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos: Fecha de Alta " + resultSet.getString("FechaAltaCli") + "  :::::::  Fecha q actualizo " + resultSet.getString("FechaActualizaCli"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
				//filtrar();
				// CARGO LA IMAGEN
			    Image i=null;
		        Blob blob = resultSet.getBlob("ImageCli");
		        i= javax.imageio.ImageIO.read(blob.getBinaryStream());
		        ImageIcon image = new ImageIcon(i);
				Icon icono = new ImageIcon(image.getImage().getScaledInstance(lblFto.getWidth(), lblFto.getHeight(), Image.SCALE_DEFAULT)); 
				lblFto.setText(null);
				lblFto.setIcon(icono);
				
				lblMs.setText(textCod.getText() + " - " + textNom.getText());
				}else{
					limpiarTexts();
					llenarcbBuscar();
					textBus.requestFocus();
					textBus.selectAll();
				}
			resultSet.close();
			statement.close();
			} catch (Exception e) {}
		}
	
		//METODO NUEVO =================
		public void nuevo() {
			V_MOD_CLIENTE=0;
			conexion = new ConexionDB();
			try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_Cli from Clientes order by Id_Cli desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_Cli"))+1);
				textCod.setText(Menu.formatid_7.format(id));
			}else{
					//limpiarTexts();
					//textBus.requestFocus();
					//textBus.selectAll();
					textCod.setText(Menu.formatid_7.format(0000001));
				}
			resultSet.close();
			statement.close();
			} catch (Exception e) {}
		}
	
		//METODO GRABAR =================
		public void insertar() {
			if (textCod.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCod.requestFocus();
				return;
			}
			if (textNom.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textNom.requestFocus();
				return;
			}
			if (cbTipoDocumento.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null, "Falta seleccionar su tipo de documento",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbTipoDocumento.requestFocus();
				return;
			}
			if (textDni.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDni.requestFocus();
				return;
			}
			if (textDir.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDir.requestFocus();
				return;
			}
			if (cbNacionalidad.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null, "Falta seleccionar su nacionalidad",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbNacionalidad.requestFocus();
				return;
			}
			conexion = new ConexionDB();
			try {
				String buscoNom= textNom.getText();
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * from Clientes where NombreCli='"+buscoNom+"'");
				if (resultSet.next()== true) {
					JOptionPane.showMessageDialog(null, "Cliente ya fue registrado",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
					textNom.requestFocus(true);
					textNom.selectAll();
				}else{
					try {
	                FileInputStream  archivofoto;
	                nuevo();
					String sql ="INSERT INTO  Clientes (Id_Cli,NombreCli,DniCli,RucCli,DireccionCli,NacionalidadCli,TelefonoCli,ObservacionCli,TipoCli,FechaActualizaCli,TipoDsctCli,DsctCli,RazonCliEmp,DireccionCliEmp,RucCliEmp,EstadoCli,ImageCli,UrlCli,FechaAltaCli,TipoDocumentoCli) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText()));
					ps.setNString(2,textNom.getText().trim());
					ps.setNString(3, textDni.getText());
					ps.setString(4, textRuc.getText().trim());
					ps.setString(5, textDir.getText().trim());;
					ps.setString(6, (String)cbNacionalidad.getSelectedItem());
					ps.setString(7, textTel.getText().trim());
					ps.setString(8, textArea.getText().trim());
					
					ps.setString(9, (String)cbTipoCliente.getSelectedItem());
					ps.setString(10, (Menu.date +" "+ Menu.HORA).trim());
					ps.setString(11,  (String)cbTipoDsct.getSelectedItem());
					ps.setFloat(12,  Float.parseFloat(textFormatDsct.getText()));
					ps.setString(13, textRazEmp.getText().trim());
					ps.setString(14, textDirEmp.getText().trim());
					ps.setString(15, textRucEmp.getText().trim());
					
					ps.setString(16, "1");
					ps.setString(18, almacenaFoto);
					// VERIFICA SI SE GUARDARA UNA IMAGEN
					if ((almacenaFoto==null)|| (almacenaFoto=="")){
						almacenaFoto="sin image";
						ps.setString(17,almacenaFoto);
					}else {
						archivofoto = new FileInputStream(almacenaFoto);
						ps.setBinaryStream(17,archivofoto);
					}
					ps.setString(19, (Menu.date).trim());
					ps.setString(20, (String)cbTipoDocumento.getSelectedItem());
					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					filtrar();
					if (FILTRO_CLI.equals("VM")||FILTRO_CLI.equals("")){	
						limpiarTexts();
						activarButton(true);
						buttonNuevo.requestFocus(true);
					}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
				}
				resultSet.close();
				statement.close();
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
			}
		}
		//METODO ACTUALIZA =================
		public void actualizar() {
			if (textCod.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCod.requestFocus();
				return;
			}
			if (textNom.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textNom.requestFocus();
				return;
			}
			if (cbTipoDocumento.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null, "Falta seleccionar su tipo de documento",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbTipoDocumento.requestFocus();
				return;
			}
			if (textDni.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDni.requestFocus();
				return;
			}
			if (textDir.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDir.requestFocus();
				return;
			}
			if (cbNacionalidad.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null, "Falta seleccionar su nacionalidad",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbNacionalidad.requestFocus();
				return;
			}
			conexion = new ConexionDB();
			try {
	        FileInputStream  archivofoto;
	         String sql="UPDATE CLIENTES SET Id_Cli = ?,"
	                 + "NombreCli = ?,"
	                 + "DniCli = ?,"
	                 + "RucCli = ?,"
	                 + "DireccionCli =?,"
	                 + "NacionalidadCli = ?,"
	                 + "TelefonoCli=?,"
	                 + "ObservacionCli =?,"
	                 
	                 + "TipoCli = ?,"
	                 + "FechaActualizaCli = ?,"
	                 + "TipoDsctCli =?,"
	                 + "DsctCli = ?,"
	                 + "RazonCliEmp=?,"
	                 + "DireccionCliEmp =?,"
	                 + "RucCliEmp=?,"
	                 
	                 + "EstadoCli =?,"
	                 + "ImageCli=?,"
	                 + "URLCli=?,"
	                 + "TipoDocumentoCli=?"
	                 + "WHERE Id_Cli = '"+textCod.getText()+"'"; 
	         
			PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(textCod.getText()));
			ps.setNString(2,textNom.getText().trim());
			ps.setNString(3, textDni.getText());
			ps.setString(4, textRuc.getText().trim());
			ps.setString(5, textDir.getText().trim());;
			ps.setString(6, (String)cbNacionalidad.getSelectedItem());
			ps.setString(7, textTel.getText().trim());
			ps.setString(8, textArea.getText().trim());
			
			ps.setString(9, (String)cbTipoCliente.getSelectedItem());
			ps.setString(10, (Menu.date +" "+ Menu.HORA).trim());
			ps.setString(11,  (String)cbTipoDsct.getSelectedItem());
			ps.setFloat(12,  Float.parseFloat(textFormatDsct.getText()));
			ps.setString(13, textRazEmp.getText().trim());
			ps.setString(14, textDirEmp.getText().trim());
			ps.setString(15, textRucEmp.getText().trim());
			
			ps.setString(16, "1");
			ps.setString(18, almacenaFoto);
			
			// VERIFICA SI SE GUARDARA UNA IMAGEN
			if ((almacenaFoto==null)|| (almacenaFoto=="")){
				almacenaFoto="sin image";
				ps.setString(17,almacenaFoto);
			}else {
				archivofoto = new FileInputStream(almacenaFoto);
				ps.setBinaryStream(17,archivofoto);
			}
			ps.setString(19, (String)cbTipoDocumento.getSelectedItem());
			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			filtrar();
			if (FILTRO_CLI.equals("VM")||FILTRO_CLI.equals("")){	
				limpiarTexts();
				activarButton(true);
				buttonNuevo.requestFocus(true);
			}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		//METODO ELIMINAR =================
		public void delete() {
			if (V_ID_CLIENTE==0){
				JOptionPane.showMessageDialog(null, "Primero debe buscar y filtrar el ítem...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				tableList.requestFocus();
				return;
			}
			if (V_ID_CLIENTE==1){
				JOptionPane.showMessageDialog(null, "No esta permitido eliminar el ítem...!"+ Menu.separador + Menu.formatid_7.format(V_ID_CLIENTE) +" "+ V_DETALLE_CLIENTE,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				tableList.requestFocus();
				return;
			}
			try {
				conexion = new ConexionDB();
				String query ="Select * from ALQUILER as A,CLIENTES as C where A.Id_Cli=C.Id_Cli and A.Id_Cli='" + V_ID_CLIENTE  + "'";
				Statement statement =  conexion.gConnection().createStatement();
				ResultSet rs=statement.executeQuery(query);
				if (rs.next()==true) {
					JOptionPane.showMessageDialog(null, "No se permite eliminar el ítem " + V_ID_CLIENTE +" "+ V_DETALLE_CLIENTE   +" "+ Menu.separador + "porque se le ha efectuado un alquiler...!" ,Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
					statement.close();
					return;
				}	
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem?"+ Menu.separador + Menu.formatid_7.format(V_ID_CLIENTE) +" "+ V_DETALLE_CLIENTE, Menu.SOFTLE_HOTEL,		
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (respuesta == JOptionPane.YES_OPTION) {
				conexion = new ConexionDB();
				try {
					Statement statement = conexion.gConnection().createStatement();
					String query="Delete from CLIENTES where Id_Cli ="+ V_ID_CLIENTE;
					statement.execute(query);
					JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					statement.close();

					buttonEliminar.setSelected(true);
					llenarTable("Select * from Clientes order by Id_Cli desc");
				} catch (Exception e) { e.printStackTrace(); }
			}else if (respuesta == JOptionPane.NO_OPTION) {
			}
		}
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textDni){textDni.setBackground(Menu.textColorBackgroundActivo);} 
			if (ev.getSource() == textRuc){textRuc.setBackground(Menu.textColorBackgroundActivo);} 
			if (ev.getSource() == textDir){textDir.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbNacionalidad){cbNacionalidad.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbTipoDocumento){cbTipoDocumento.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textTel){textTel.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbTipoCliente){cbTipoCliente.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbTipoDsct){cbTipoDsct.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFormatDsct){textFormatDsct.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textRazEmp){textRazEmp.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textDirEmp){textDirEmp.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textRucEmp){textRucEmp.setBackground(Menu.textColorBackgroundActivo);}
			//FORE
			if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textDni){textDni.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textRuc){textRuc.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textDir){textDir.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbNacionalidad){cbNacionalidad.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbTipoDocumento){cbTipoDocumento.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbTipoDsct){cbTipoDsct.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textTel){textTel.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbTipoCliente){cbTipoCliente.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textFormatDsct){textFormatDsct.setForeground(Menu.textColorForegroundActivo);}
			
			if (ev.getSource() == textRazEmp){textRazEmp.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textDirEmp){textDirEmp.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textRucEmp){textRucEmp.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textDni){textDni.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textRuc){textRuc.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textDir){textDir.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == cbNacionalidad){cbNacionalidad.setBackground(new Color(240,240,240));}
			if (ev.getSource() == cbTipoDocumento){cbTipoDocumento.setBackground(new Color(240,240,240));}
			if (ev.getSource() == cbTipoDsct){cbTipoDsct.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textTel){textTel.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == cbTipoCliente){cbTipoCliente.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textFormatDsct){textFormatDsct.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textRazEmp){textRazEmp.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textDirEmp){textDirEmp.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textRucEmp){textRucEmp.setBackground(Menu.textColorBackgroundInactivo);} 
			//FORE
			if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textDni){textDni.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textRuc){textRuc.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textDir){textDir.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbNacionalidad){cbNacionalidad.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbTipoDocumento){cbTipoDocumento.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbTipoCliente){cbTipoCliente.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textTel){textTel.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbTipoDsct){cbTipoDsct.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textFormatDsct){textFormatDsct.setForeground(Menu.textColorForegroundInactivo);}
			
			if (ev.getSource() == textRazEmp){textRazEmp.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textDirEmp){textDirEmp.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textRucEmp){textRucEmp.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		void imprimir () {
			try {
				
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtSubTitle",new String ("LISTA DE HÚESPEDES - POR FILTRO"));
				parameters.put("PrtNomEmpresa",new String (VentanaLogin.RAZON_SOCIAL_HOTEL));
				parameters.put("PrtRucEmpresa",new String (VentanaLogin.RUC_HOTEL));
				parameters.put("PrtDirEmpresa",new String (VentanaLogin.DIRECCION_HOTEL));
				if (!VentanaLogin.URLIMAGE_HOTEL.equals("")) {
					parameters.put("PrtUbiFoto",new String (VentanaLogin.URLIMAGE_HOTEL));
				//}else{
					//parameters.put("PrtUbiFoto",new String (Menu.URL_IMAGE+"login.png"));
				}
				DatasourceCliente datasource = new DatasourceCliente();
			    for(int i=0;i<tableList.getRowCount();i++)
			    {
			    	ListCliente c= new ListCliente(  tableList.getValueAt(i, 0).toString().trim(),tableList.getValueAt(i, 1).toString().trim(), tableList.getValueAt(i, 2).toString().trim(), tableList.getValueAt(i, 3).toString().trim(), 
			    									 tableList.getValueAt(i, 4).toString().trim(), tableList.getValueAt(i, 6).toString().trim());
			    	datasource.addList(c);
			    }
			    
		        JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(Menu.URL+"CCliente.jasper");  
		        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, datasource);  
		        JDialog visor = new JDialog();
		        visor.setModal(true);
		        JasperViewer jrViewer = new JasperViewer(jasperPrint, true); 
		        visor.setSize(900,600); 
		        visor.setLocationRelativeTo(null); 
				visor.setTitle("Lista de húespedes");

				visor.getContentPane().add(jrViewer.getContentPane()); 
				visor.setVisible(true);
				
		        //JRExporter exporter = new JRPdfExporter();  
		        //exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint); 
		        //exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new java.io.File("reporte2PDF.pdf")); 
		        //exporter.exportReport(); 
			} catch (Exception e) {}
		}
				
		public void actionPerformed(ActionEvent evento) {
			if (evento.getSource()==cbTipoCliente) {
				if (cbTipoCliente.getSelectedItem()=="HABITUAL") {
					cbTipoDsct.setEnabled(true);
					textFormatDsct.setEnabled(true);
				}else{
					cbTipoDsct.setEnabled(false);
					textFormatDsct.setEnabled(false);
					textFormatDsct.setText("0.00");
				}
			}
			if (evento.getSource()==buttonImp) {
				imprimir();
			}
			  if (evento.getSource() == buttonNuevo){// NUEVO
				  limpiarTexts();
				  llenarcbNacionalidad();
				  activarButton(false);
				  activarTexts(true);
				  
				  nuevo();
				  textCod.setEditable(false);
				  textNom.requestFocus(true);
				  }
			  if (evento.getSource() == buttonCancelar){// CANCELAR
				  limpiarTexts();
				  activarButton(true);
				  buttonNuevo.requestFocus(true);
				  }
			  if (evento.getSource() == buttonGrabar){// GRABAR
				  if (V_MOD_CLIENTE==0){
					  insertar();
				  }else{
					  actualizar();
				  	}
				  
			  		//filtrar();
					//limpiarTexts();
					//activarButton(true);
					//buttonNuevo.requestFocus(true);
					//limpiarTexts();
					//activarButton(true);
					//buttonNuevo.requestFocus(true);
				  }
			  if (evento.getSource() == buttonEditar){// EDITAR
				  activarButton(false);
				  if (V_ID_CLIENTE>0) {
					  consultar="Select* from CLIENTES where Id_Cli='" + V_ID_CLIENTE + "'";
				  }
				  buscar();
				  }	
			  if (evento.getSource() == buttonEliminar){// ELIMINAR
				  delete();
				  }	
			  if (evento.getSource() == buttonSalir){// SALIR
				  frame.dispose();
				  }
			  if (evento.getSource() == buttonBus){	// BUSCAR
				  panelLst.setVisible(true);
				  llenarcbBuscar();
				  llenarTable("Select* from CLIENTES order by Id_Cli desc");
				  panelDto.setVisible(false);// PANEL DATOS
				  textBus.requestFocus();textBus.selectAll();
				  }
			  if (evento.getSource() == buttonOki){	// OK
					if (V_ID_CLIENTE>0) {
						consultar="Select* from CLIENTES where Id_Cli='" + V_ID_CLIENTE + "'";
					}
				  	buscar();
				  	filtrar();
					panelLst.setVisible(false);
					panelDto.setVisible(true);
				  }
			  if (evento.getSource() == buttonReg){	// REGRESAR
					panelLst.setVisible(false);
					panelDto.setVisible(true);
				  }
			  
			  if (evento.getSource() == buttonEnviar){	// ENVIAR PARA FACTURAR
				  	filtrar();
					if (FILTRO_CLI == "VGD"){
						  VentanaGenerarDocumento.textCId.setText(this.textCod.getText());
						  VentanaGenerarDocumento.textCNom.setText(this.textRazEmp.getText());
						  VentanaGenerarDocumento.textCRuc.setText(this.textRucEmp.getText());
						  VentanaGenerarDocumento.textCDir.setText(this.textDirEmp.getText());
						  this.frame.dispose();
						}
				  
				  }
			  if (evento.getSource() == buttonImp){	// IMPRIMIR
				  filtrar();
				  
				  /*lAlquilarReservar Al = new lAlquilarReservar();
				  Al.setCodCliente(this.textCod.getText());
				  Al.setApeCliente(this.textNom.getText());
				  Al.setDirCliente(this.textDir.getText());*/
				  
					
					
				  if(this.tableList.getSelectedRow() != -1)
					  
				  {


				  }
			        //this("Seleccione una fila de la tabla");
			       
			   
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
					if (textNom.getText().toLowerCase().isEmpty()|| textNom.getText().toLowerCase().length()>80){
						textNom.requestFocus();
						textNom.selectAll();
						textNom.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textNom.getText().toLowerCase().length()==80){
							cbTipoDocumento.requestFocus();
						}
				} 
				if (evet.getSource() == cbTipoDocumento){
					if (e==KeyEvent.VK_ENTER){
						textDni.requestFocus();
						textDni.selectAll();
					}
				}
				if (evet.getSource() == textDni){
					if (cbTipoDocumento.getSelectedItem().equals("DNI")){
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
					if (!cbTipoDocumento.getSelectedItem().equals("DNI")){
						if (textDni.getText().toLowerCase().isEmpty()|| textDni.getText().toLowerCase().length()>17){
							textDni.requestFocus();
							textDni.selectAll();
							textDni.setText(null);
							} 
							else if (e==KeyEvent.VK_ENTER || textDni.getText().toLowerCase().length()==17){
								textRuc.requestFocus();
								textRuc.selectAll();	
							}
					}
				}
				if (evet.getSource() == textRuc){
					int pos = textRuc.getCaretPosition();textRuc.setText(textRuc.getText().toUpperCase());textRuc.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textRuc.getText().length()>11){
						textRuc.requestFocus();
						textRuc.selectAll();
						textRuc.setText(null);
						} 
						 if (e==KeyEvent.VK_ENTER || textRuc.getText().toLowerCase().length()==11){
							textDir.requestFocus();
							textDir.selectAll();
						}
				}
				if (evet.getSource() == textDir){
					int pos = textDir.getCaretPosition();textDir.setText(textDir.getText().toUpperCase());textDir.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textDir.getText().toLowerCase().isEmpty()|| textDir.getText().toLowerCase().length()>99){
						textDir.requestFocus();
						textDir.selectAll();
						textDir.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textDir.getText().toLowerCase().length()==99){
							cbNacionalidad.requestFocus();
						}
				}
				if (evet.getSource() == cbNacionalidad){
					if (e==KeyEvent.VK_ENTER){textTel.requestFocus();
						textTel.selectAll();
					}
				}
		
				if (evet.getSource() == textTel){
					int pos = textTel.getCaretPosition();textTel.setText(textTel.getText().toUpperCase());textTel.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textTel.getText().toLowerCase().length()>25){
						textTel.requestFocus();
						textTel.selectAll();
						textTel.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textTel.getText().toLowerCase().length()==25){
							buttonGrabar.requestFocus();
							buttonGrabar.doClick();
						}
				}
		
				if (evet.getSource() == textArea){
					int pos = textArea.getCaretPosition();textArea.setText(textArea.getText().toUpperCase());textArea.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textArea.getText().toLowerCase().isEmpty()|| textArea.getText().toLowerCase().length()>200){
						textArea.requestFocus();
						textArea.selectAll();
						textArea.setText(null);
						} 
				}
				if (evet.getSource() == textBus){
					int pos = textBus.getCaretPosition();textBus.setText(textBus.getText().toUpperCase());textBus.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					consultar="";
					if(cbBus.getSelectedItem()=="CODIGO") {
						consultar="SELECT * FROM Clientes where Id_Cli like'"+textBus.getText()+"%' order by Id_Cli desc";
					}
					if(cbBus.getSelectedItem()=="NOMBRE / RAZON SOCIAL") {
						consultar="SELECT * FROM Clientes where NombreCli like'"+textBus.getText()+"%' order by Id_Cli desc";
					}
					if(cbBus.getSelectedItem()=="DNI") {
						consultar="SELECT * FROM Clientes where DniCli like'"+textBus.getText()+"%' order by Id_Cli desc";
					}
					if(cbBus.getSelectedItem()=="RUC") {
						consultar="SELECT * FROM Clientes where RucCli like'"+textBus.getText()+"%' order by Id_Cli desc";
					}
					if(cbBus.getSelectedItem()=="NACIONALIDAD") {
						consultar="SELECT * FROM Clientes where NacionalidadCli like'"+textBus.getText()+"%' order by Id_Cli desc";
					}
					
					llenarTable(consultar);
					if (e==KeyEvent.VK_ENTER || textBus.getText().toLowerCase().length()==30){
						buscar();
						filtrar();
						panelLst.setVisible(false);
						panelDto.setVisible(true);
					}
					if (e==KeyEvent.VK_DOWN){
						System.out.println("HOLA ESTE ES LA TECLAD DDJDJJDJDJJ");
						tableList.selectAll();
						tableList.requestFocus();
					}
				}
				
				if (evet.getSource() == textFormatDsct){
					if (textFormatDsct.getText().toLowerCase().isEmpty() || textFormatDsct.getText().toLowerCase().length()>8){
						textFormatDsct.requestFocus();
						textFormatDsct.selectAll();
						textFormatDsct.setText(null);
					}
					if (e==KeyEvent.VK_ENTER || textFormatDsct.getText().toLowerCase().length()==8){

					}
				}
				
				if (evet.getSource() == textRazEmp){
					int pos = textRazEmp.getCaretPosition();textRazEmp.setText(textRazEmp.getText().toUpperCase());textRazEmp.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textRazEmp.getText().toLowerCase().isEmpty()|| textRazEmp.getText().toLowerCase().length()>96){
						textRazEmp.requestFocus();
						textRazEmp.selectAll();
						textRazEmp.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textRazEmp.getText().toLowerCase().length()==96){
							textDirEmp.requestFocus();
							textDirEmp.selectAll();	
						}
				} 
				if (evet.getSource() == textDirEmp){
					int pos = textDirEmp.getCaretPosition();textDirEmp.setText(textDirEmp.getText().toUpperCase());textDirEmp.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textDirEmp.getText().toLowerCase().isEmpty()|| textDirEmp.getText().toLowerCase().length()>190){
						textDirEmp.requestFocus();
						textDirEmp.selectAll();
						textDirEmp.setText(null);
						} 
					else if (e==KeyEvent.VK_ENTER || textDirEmp.getText().toLowerCase().length()==190){
							 textRucEmp.requestFocus();
							 textRucEmp.selectAll();
						}
				}
				if (evet.getSource() == textRucEmp){
					int pos = textRucEmp.getCaretPosition();textRucEmp.setText(textRucEmp.getText().toUpperCase());textRucEmp.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textRucEmp.getText().toLowerCase().isEmpty()|| textRucEmp.getText().toLowerCase().length()>11){
						textRucEmp.requestFocus();
						textRucEmp.selectAll();
						textRucEmp.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER){
							buttonGrabar.doClick();
						}
				}
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char e=evet.getKeyChar();
			if (evet.getSource() == textNom){ 
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textDni){
				if(!Character.isDigit(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textRuc){
				if(!Character.isDigit(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textDir){ 
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textTel){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textArea){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			
			
			if (evet.getSource() == textFormatDsct){
				if ((e<'0'||e>'9')&&(e<'.'||e>'.'))evet.consume();
			}
			
			
			if (evet.getSource() == textRazEmp){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textDirEmp){ 
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textRucEmp){
				if(!Character.isDigit(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
		}
	
	public void filtrar(){
		if (FILTRO_CLI == "VA"){
			VentanaAlquiler.textCId.setText(this.textCod.getText());
				VentanaAlquiler.textCNom.setText(this.textNom.getText());
				VentanaAlquiler.textDscCli.setText((String)this.cbTipoDsct.getSelectedItem() +" " + this.textFormatDsct.getText());
				VentanaAlquiler.Direccion= this.textDir.getText();
				VentanaAlquiler.TIP_DSCT_CLI= (String)this.cbTipoDsct.getSelectedItem();
				VentanaAlquiler.DSCT_CLI=(Float.parseFloat(this.textFormatDsct.getText()));
				
			  
			  //String [] lista = {textFormatDsctSol.getText(),textFormatDsctPor.getText() };
			  //for (String string : lista) {
				  //VentanaAlquilarReservar.cbDsct.addItem(string);
			  //}

			  this.frame.dispose();
			}
		if (FILTRO_CLI == "VR"){ // VENTANA RESERVA
				VentanaAReservar.textCId.setText(this.textCod.getText());
				VentanaAReservar.textCNom.setText(this.textNom.getText());
				VentanaAReservar.textDscCli.setText((String)this.cbTipoDsct.getSelectedItem() +" " + this.textFormatDsct.getText());
				VentanaAReservar.Direccion= this.textDir.getText();
				VentanaAReservar.TIP_DSCT_CLI= (String)this.cbTipoDsct.getSelectedItem();
				VentanaAReservar.DSCT_CLI=(Float.parseFloat(this.textFormatDsct.getText()));
				  this.frame.dispose();
			}
		if (FILTRO_CLI == "VGD"){	//VENTANA FACTURACION CHEKH-OUT 
			  	VentanaGenerarDocumento.textCId.setText(this.textCod.getText());
			  	VentanaGenerarDocumento.textCNom.setText(this.textNom.getText());
			  	VentanaGenerarDocumento.textCRuc.setText(this.textRuc.getText());
			  	VentanaGenerarDocumento.textCDir.setText(this.textDir.getText());
			  	this.frame.dispose();
			}
		if (FILTRO_CLI == "VV"){  	//VENTANA VITRINA VENTAS
				VentanaVitrinaVenta.textCId.setText(this.textCod.getText());
				VentanaVitrinaVenta.textCNom.setText(this.textNom.getText());
				VentanaVitrinaVenta.Ruc=(this.textRuc.getText());
				VentanaVitrinaVenta.Direccion=(this.textDir.getText());
				VentanaVitrinaVenta.textCDni.setText(this.textDni.getText());
				this.frame.dispose();
			}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		formatDsct =NumberFormat.getNumberInstance();
		formatDsct.setMaximumFractionDigits(3);
		Object source = e.getSource();
		if (source==textFormatDsct.getText()) {
			//fecha = ((Number) textFieldFormatt[0].getValue()).doubleValue();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		char e=evet.getKeyChar();
		try {
	
			if (evet.getSource().equals(tableList)) {
				V_ID_CLIENTE =Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim());
				if (V_ID_CLIENTE==0){
					tableList.requestFocus();
					return;
				}
				if (e==KeyEvent.VK_ENTER ){
					consultar="";
					activarButton(false);
					consultar="SELECT * FROM Clientes where Id_Cli ='"+ V_ID_CLIENTE +"' order by Id_Cli asc";
					buscar();
					filtrar();
				}
			}
			
		} catch (Exception e2) {
			// TODO: handle exception
		}
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
		if (Mouse_evt.getSource().equals(tableList)) {
			try {
				V_ID_CLIENTE=0;V_DETALLE_CLIENTE="";
				if (Mouse_evt.getClickCount() == 1 || Mouse_evt.getClickCount() == 2) {
					V_ID_CLIENTE = Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim());
					V_DETALLE_CLIENTE = (String)(tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim());
					if (V_ID_CLIENTE==0){
						tableList.requestFocus();
						return;
					}
				}
				if (Mouse_evt.getClickCount() == 1) {
					if (V_ID_CLIENTE>0) {
						consultar="Select* from CLIENTES where Id_Cli='" + V_ID_CLIENTE + "'";
					}
				  	buscar();
				}  	
				if (Mouse_evt.getClickCount() == 2) {
					buttonOki.doClick();
				}
			} catch (Exception e) {}
		}
	}
}
