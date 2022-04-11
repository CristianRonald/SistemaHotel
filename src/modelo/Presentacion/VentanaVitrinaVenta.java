
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
public class VentanaVitrinaVenta implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	
	private static ConexionDB conexion;

	public JInternalFrame frame;
	public JPanel 				panelDto,panelPie,panelLst;

	public static JTextField 	textCId,textCNom,textCDni;
	public static JTextField			textNumero,textPrecio,textCantidad,textCod,textDescripcion;
	
	public static String        Direccion,Ruc;
	private JLabel 				lbl2,lbl3,lbl6,lbl7;
	private JLabel 				lbl11,lbl13,lbl15,lbl16,lbl17,lbl18,lblE,lblSer,lblNum;
	public  JButton				buttonNuevo,buttonGrabar,buttonPrint,buttonAnular,buttonSalir,buttonCliente,buttonAgregar,buttonQuitar,buttonArticulo;
	private JComboBox<String> 	cbSerie;
	public  JComboBox<String>   cbDocumento,cbArtServ;
	private JFormattedTextField textFormatImporte,textFormatDsct,textFormatTotal;
	
	private JDateChooser chooserIngreso;
	private JRadioButton rdbtnEfectivo,rdbtnCredito;
	public Integer totalitems=0;
	public static int CONTAR_VENTANA_VENTA=0;
	public int ID;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	private String FechaEntrada="";
	public static int MOD;
	private JSpinner  spinnerE;
	SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss a");
	
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	private  String []datos= new String[6];
	   
	float IMPORTE=0,DSCT=0,TOTAL=0,UTLT=0;
	protected static float COSTO=0;
	private String ID_DOC="";
	private int ID_SERIE=0;
	protected int ID_TRA=0;
	
	public static String id="",descripcion="";
	public VentanaVitrinaVenta() {
		super();
		MOD=0;
		frameVenta();
		crearPanel();
		crearTable();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		crearOtros();
		llenarTableConfiguration();
		horaSpinner();
		chooserIngreso.setDate(Menu.fecha);
		llenarComboDocumento();
		llenarCbArtSer();
		
		// ACTUALIZO FECHA ACTUAL DE ENTRADA
		//SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
		//FechaEntrada = form.format(chooserIngreso.getDate());
		//textFIngreso.setText(FechaEntrada);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		FechaEntrada = form.format(chooserIngreso.getDate());
		
		
		CONTAR_VENTANA_VENTA ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
		
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    Menu.Desktop.add(frame);
	}
	public void frameVenta() {
		frame = new JInternalFrame();
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				
			}
			//@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				CONTAR_VENTANA_VENTA=0;
			}
		});
		frame.setTitle("VITRINA: venta directa");
		frame.setFrameIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/venta16.png")));
		frame.setClosable(true);
		frame.setBounds(100, 100, 771, 426);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto = new JPanel();
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), ":::: | Facturación | ::::" , TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 128)));
		panelDto.setBounds(10, 11, 732, 109);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		panelPie= new JPanel();
		panelPie.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "¿?", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelPie.setBounds(10, 322, 732, 54);
		frame.getContentPane().add(panelPie);
		panelPie.setLayout(null);
		
		panelLst= new JPanel();
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "¿?", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 118, 732, 205);
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
	}
	public void crearTable(){
		tableList = new JTable();
		tableList.setForeground(new Color(0, 139, 139));
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tableList.setBounds(10, 303, 665, 229);
		tableList.addMouseListener(this);
		
		// PERMITE BLOQUEAR CELDAS
		/*tableList=new JTable(model){
		 @Override
		     public boolean isCellEditable (int fila, int columna) {
		         if (columna==0||columna==1){
		             return true;
		 		}else{
		 			return false;
		 		}
		     }
		 };*/
		 // END PERMITE BLOQUEAR CELDAS
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 17, 703, 177);
        panelLst.add(scrollTable);
    	tableList.setShowVerticalLines(true);
    	tableList.setFillsViewportHeight(true);
    	tableList.setGridColor(new Color(255, 160, 122));
    	tableList.setBackground(new Color(255, 255, 255));
	}
	void horaSpinner() {
		 SpinnerDateModel model = new SpinnerDateModel();
		//model.setCalendarField(Calendar.MINUTE);
		spinnerE= new JSpinner();
		spinnerE.setModel(model);
		spinnerE.setEditor(new JSpinner.DateEditor(spinnerE, "hh:mm:ss a"));
		spinnerE.setBounds(620, 0, 91, 20);
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
		spinnerE.setVisible(false);
	}
	
	public void crearOtros() {
		JSeparator separator = new JSeparator();
		separator.setBounds(83, 60, 628, 7);
		panelDto.add(separator);
		
		chooserIngreso= new JDateChooser();
		chooserIngreso.setDateFormatString("dd-MMM-yyyy");
		chooserIngreso.setBorder(new LineBorder(new Color(255, 255, 0), 1, true));
		chooserIngreso.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)chooserIngreso.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)chooserIngreso.getDateEditor()).setEditable(false);
		//chooserIngreso.getCalendarButton().addActionListener(this);
		chooserIngreso.addPropertyChangeListener(this);
		chooserIngreso.setBounds(252, 23, 94, 20);

		//chooserChooser.setIcon(new ImageIcon(VentanaAlquilarReservar.class.getResource("/modelo/Images/date.png")));
		panelPie.add(chooserIngreso);
		
		rdbtnEfectivo = new JRadioButton("Contado");
		rdbtnEfectivo.setSelected(true);
		buttonGroup.add(rdbtnEfectivo);
		rdbtnEfectivo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnEfectivo.setBounds(588, 7, 66, 16);
		rdbtnEfectivo.addActionListener(this);
		panelPie.add(rdbtnEfectivo);
		
		rdbtnCredito = new JRadioButton("Por Pagar");
		buttonGroup.add(rdbtnCredito);
		rdbtnCredito.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnCredito.setBounds(654, 7, 72, 16);
		rdbtnCredito.addActionListener(this);
		panelPie.add(rdbtnCredito);
		
		rdbtnEfectivo.setVisible(false);
		rdbtnCredito.setVisible(false);
	}
	public void crearLabels() {
		
		lblE= new JLabel("Tipo inventario:");
		lblE.setBounds(86, 64, 82, 14);
		panelDto.add(lblE);
		lblE.setHorizontalAlignment(SwingConstants.RIGHT);
		lblE.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Cliente:");
		lbl2.setBounds(10, 30, 68, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Documento:");
		lbl3.setBounds(26, 8, 78, 14);
		panelPie.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lblSer= new JLabel("Serie:");
		lblSer.setBounds(107, 8, 56, 14);
		panelPie.add(lblSer);
		lblSer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSer.setFont(Menu.fontLabel);

		lblNum= new JLabel("Número:");
		lblNum.setBounds(178, 8, 70, 14);
		panelPie.add(lblNum);
		lblNum.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNum.setFont(Menu.fontLabel);
		
		lbl6= new JLabel("Fecha emisión:");
		lbl6.setBounds(248, 8, 98, 14);
		panelPie.add(lbl6);
		lbl6.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl6.setFont(Menu.fontLabel);
		
		lbl7= new JLabel("Cantidad:");
		lbl7.setBounds(576, 64, 59, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl15 = new JLabel("Precio S/.:");
		lbl15.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl15.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl15.setBounds(514, 64, 60, 14);
		panelDto.add(lbl15);
		
		lbl16 = new JLabel("Descripción:");
		lbl16.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl16.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl16.setBounds(362, 64, 108, 14);
		panelDto.add(lbl16);
		
		lbl17 = new JLabel("Detalle:");
		lbl17.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl17.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl17.setBounds(10, 82, 68, 14);
		panelDto.add(lbl17);
		
		
		lbl11= new JLabel("Importe:");
		lbl11.setBounds(350, 9, 66, 14);
		panelPie.add(lbl11);
		lbl11.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl11.setFont(Menu.fontLabel);
		
		lbl18= new JLabel("Dscto:");
		lbl18.setBounds(423, 9, 44, 14);
		panelPie.add(lbl18);
		lbl18.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl18.setFont(Menu.fontLabel);
		
		lbl13= new JLabel("Total:");
		lbl13.setBounds(489, 9, 50, 14);
		panelPie.add(lbl13);
		lbl13.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl13.setFont(Menu.fontLabel);
	}
	public void crearTextFields(){
		textCId = new JTextField("");
		textCId.setEditable(false);
		textCId.setColumns(10);
		textCId.setForeground(new Color(0, 0, 128));
		textCId.setBackground(SystemColor.menu);
		textCId.setBounds(121, 30, 60, 19);
		panelDto.add(textCId);
		textCId.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(205, 92, 92)));
		
		textCNom = new JTextField("");
		textCNom.setEditable(false);
		textCNom.setColumns(10);
		textCNom.setForeground(new Color(0, 0, 128));
		textCNom.setBackground(SystemColor.menu);
		textCNom.setBounds(186, 30, 438, 19);
		textCNom.addActionListener(this);
		panelDto.add(textCNom);
		textCNom.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(205, 92, 92)));
		
		textCDni = new JTextField("");
		textCDni.setHorizontalAlignment(SwingConstants.CENTER);
		textCDni.setColumns(10);
		textCDni.setForeground(new Color(0, 0, 128));
		textCDni.setEditable(false);
		textCDni.setBackground(SystemColor.menu);
		textCDni.setBounds(631, 30, 80, 19);
		panelDto.add(textCDni);
		textCDni.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(205, 92, 92)));
		
		textPrecio = new JTextField("0.00");
		textPrecio.setHorizontalAlignment(SwingConstants.CENTER);
		textPrecio.setToolTipText("Precio");
		textPrecio.setForeground(new Color(0, 102, 102));
		textPrecio.setColumns(10);
		textPrecio.setBackground(new Color(255, 255, 255));
		textPrecio.setBounds(506, 79, 68, 21);
		textPrecio.addActionListener(this);
		textPrecio.addKeyListener(this);
		panelDto.add(textPrecio);
		
		textCantidad = new JTextField();
		textCantidad.setHorizontalAlignment(SwingConstants.CENTER);
		textCantidad.setToolTipText("Cantidad");
		textCantidad.setForeground(new Color(0, 102, 102));
		textCantidad.setColumns(10);
		textCantidad.setBackground(new Color(255, 255, 255));
		textCantidad.setBounds(576, 79, 60, 21);
		textCantidad.addActionListener(this);
		textCantidad.addKeyListener(this);
		panelDto.add(textCantidad);

		textDescripcion = new JTextField("");
		textDescripcion.setEditable(false);
		textDescripcion.setColumns(10);
		textDescripcion.setBackground(new Color(240, 240, 240));
		textDescripcion.setBounds(173, 79, 297, 21);
		textDescripcion.addActionListener(this);
		textDescripcion.setForeground(new Color(0, 102, 102));
		panelDto.add(textDescripcion);
		//textObservacion.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		textNumero = new JTextField("");
		textNumero.setFont(new Font("Tahoma", Font.BOLD, 11));
		textNumero.setEditable(false);
		textNumero.setBounds(167, 23, 81, 21);
		textNumero.setHorizontalAlignment(SwingConstants.CENTER);
		textNumero.setToolTipText("Numero documento");
		textNumero.setForeground(new Color(70, 130, 180));
		textNumero.setColumns(10);
		panelPie.add(textNumero);
		
		textCod = new JTextField("");
		textCod.setHorizontalAlignment(SwingConstants.CENTER);
		textCod.setToolTipText("Id");
		textCod.setForeground(new Color(0, 102, 102));
		textCod.setEditable(false);
		textCod.setColumns(10);
		textCod.setBackground(Color.WHITE);
		textCod.setBounds(163, 79, 60, 21);
		panelDto.add(textCod);
		textCod.setVisible(false);

		textFormatImporte = new JFormattedTextField();
		textFormatImporte.setFont(new Font("Tahoma", Font.BOLD, 12));
		textFormatImporte.setForeground(new Color(70, 130, 180));
		textFormatImporte.setEditable(false);
		textFormatImporte.setBackground(new Color(255, 255, 255));
		textFormatImporte.setText("0.00");
		textFormatImporte.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatImporte.setBounds(350, 23, 66, 21);
		panelPie.add(textFormatImporte);
		
		textFormatDsct = new JFormattedTextField();
		textFormatDsct.setFont(new Font("Tahoma", Font.BOLD, 12));
		textFormatDsct.setForeground(new Color(70, 130, 180));
		textFormatDsct.setText("0.00");
		textFormatDsct.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatDsct.setBounds(420, 23, 50, 21);
		textFormatDsct.addActionListener(this);
		textFormatDsct.addPropertyChangeListener(this);
		textFormatDsct.addKeyListener(this);
		panelPie.add(textFormatDsct);
		
		textFormatTotal = new JFormattedTextField();
		textFormatTotal.setFont(new Font("Tahoma", Font.BOLD, 12));
		textFormatTotal.setForeground(new Color(70, 130, 180));
		textFormatTotal.setBackground(new Color(255, 255, 255));
		textFormatTotal.setEditable(false);
		textFormatTotal.setText("0.00");
		textFormatTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatTotal.setBounds(473, 23, 66, 21);
		panelPie.add(textFormatTotal);
	}
	public void crearComboBox() { 
		cbArtServ = new JComboBox<>();
		cbArtServ.setForeground(new Color(0, 102, 102));
		cbArtServ.setBounds(87, 79, 82, 21);
		cbArtServ.setFont(Menu.fontText);
		cbArtServ.removeAllItems();
		cbArtServ.addActionListener(this);
		cbArtServ.addPropertyChangeListener(this);
		cbArtServ.addFocusListener(this);
		cbArtServ.addKeyListener(this);
		panelDto.add(cbArtServ);
		
		cbDocumento = new JComboBox<>();
		cbDocumento.setForeground(new Color(70, 130, 180));
		cbDocumento.addActionListener(this);
		cbDocumento.addPropertyChangeListener(this);
		cbDocumento.addFocusListener(this);
		cbDocumento.addKeyListener(this);
		cbDocumento.setBounds(10, 23, 94, 21);
		cbDocumento.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelPie.add(cbDocumento);
		
		cbSerie = new JComboBox<>();
		cbSerie.setForeground(new Color(70, 130, 180));
		cbSerie.setBounds(107, 23, 56, 21);
		cbSerie.setFont(new Font("Tahoma", Font.BOLD, 11));
		cbSerie.removeAllItems();
		cbSerie.addActionListener(this);
		cbSerie.addPropertyChangeListener(this);
		cbSerie.addFocusListener(this);
		cbSerie.addKeyListener(this);
		panelPie.add(cbSerie);
	}
	public void crearButtons() {
		buttonCliente= new JButton("");
		buttonCliente.setToolTipText("Alta de clientes");
		buttonCliente.addActionListener(this);
		buttonCliente.setBounds(83, 26, 31, 23);
		buttonCliente.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/add_friend.png")));
		panelDto.add(buttonCliente);
		
		buttonNuevo= new JButton("");
		buttonNuevo.setToolTipText("Nuevo");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setBounds(540, 22, 36, 23);
		buttonNuevo.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/nuevo.png")));
		panelPie.add(buttonNuevo);
		
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Generar...");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(612, 22, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/ok.png")));
		panelPie.add(buttonGrabar);
		
		buttonPrint= new JButton("");
		buttonPrint.setEnabled(false);
		buttonPrint.setToolTipText("Print..");
		buttonPrint.addActionListener(this);
		buttonPrint.setBounds(576, 22, 36, 23);
		buttonPrint.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/print.png")));
		panelPie.add(buttonPrint);
		
		buttonAnular= new JButton("");
		buttonAnular.setEnabled(false);
		buttonAnular.setToolTipText("Anular..");
		buttonAnular.addActionListener(this);
		buttonAnular.setBounds(649, 22, 36, 23);
		buttonAnular.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/future-projects.png")));
		panelPie.add(buttonAnular);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(685, 22, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/Exit.png")));
		panelPie.add(buttonSalir);
		
		buttonArticulo= new JButton("");
		buttonArticulo.setToolTipText("Buscar");
		buttonArticulo.addActionListener(this);
		buttonArticulo.setBounds(472, 78, 31, 23);
		buttonArticulo.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/search.png")));
		panelDto.add(buttonArticulo);
		
		buttonAgregar= new JButton("");
		buttonAgregar.setToolTipText("Agregar ítem");
		buttonAgregar.addActionListener(this);
		buttonAgregar.setBounds(646, 77, 31, 23);
		buttonAgregar.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/plus.png")));
		panelDto.add(buttonAgregar);

		buttonQuitar= new JButton("");
		buttonQuitar.setToolTipText("Quitar ítem");
		buttonQuitar.addActionListener(this);
		buttonQuitar.setBounds(680, 77, 31, 23);
		buttonQuitar.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/mant-quitar.png")));
		panelDto.add(buttonQuitar);
	}
	public void llenarTableConfiguration(){
		
		   model= new DefaultTableModel();
		   model.addColumn("ID");
		   model.addColumn("Descripción");
		   model.addColumn("Precio S/.");
		   model.addColumn("Cantidad");
	       model.addColumn("Importe S/.");
	       model.addColumn("Utl. S/.");
	       
		   tableList.setModel(model);
		   
		   tableList.getColumnModel().getColumn(0).setPreferredWidth(40);
		   tableList.getColumnModel().getColumn(1).setPreferredWidth(360);
		   tableList.getColumnModel().getColumn(2).setPreferredWidth(25);
		   tableList.getColumnModel().getColumn(3).setPreferredWidth(10);
		   tableList.getColumnModel().getColumn(4).setPreferredWidth(25);
		   tableList.getColumnModel().getColumn(5).setPreferredWidth(25);
           tableList.getColumnModel().getColumn(5).setMinWidth(0);
           tableList.getColumnModel().getColumn(5).setMaxWidth(0);
           
           DefaultTableCellRenderer modelRight = new DefaultTableCellRenderer();
           modelRight.setHorizontalAlignment(SwingConstants.RIGHT);
           DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
           modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);
           
           tableList.getColumnModel().getColumn(2).setCellRenderer(modelRight);
           tableList.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);
           tableList.getColumnModel().getColumn(4).setCellRenderer(modelRight);
           tableList.getColumnModel().getColumn(5).setCellRenderer(modelRight);
           
	}
	public void llenarModificarVenta (int COD) {
		conexion = new ConexionDB();
        try {
           float IMPORTES=0,DESCT=0,TOTAL=0;
     	   totalitems=0;
     	   String consultar="Select * from DOCUMENTO as D, TRANSACCION as T,DETALLE_TRANSACCION as DT,CLIENTES as C where T.Id_DocTra=D.Id_Doc and T.Id_CliPro=C.Id_Cli and DT.Id_Tra = T.Id_Tra and T.Id_Tra='" + COD + "'";

 		   String []datos= new String[6];
 		   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(consultar);
 		   tableList.setModel(model);
 		   
           while(rs.next()) {
        	ID_TRA=rs.getInt("Id_Tra");   
            datos[0]=" "+Menu.formatid_7.format(rs.getInt("Id_ArtSer"));
            datos[1]=" "+rs.getString("DescripcionDetTra");
            datos[2]=" "+Menu.formateadorCurrency.format(rs.getFloat("PrecioDetTra"));
            datos[3]=" "+rs.getString("CantidadDetTra");
            datos[4]=" "+Menu.formateadorCurrency.format(rs.getFloat("ImporteDetTra"));
            datos[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("UtlDetTra"));
            
            totalitems=totalitems+1;
            model.addRow(datos);
            tableList.setModel(model);

            IMPORTES= rs.getFloat("SubTotalTra");
			DESCT= rs.getFloat("DsctTra");
			TOTAL= rs.getFloat("Totaltra");
			UTLT= rs.getFloat("UtlTra");
			
			textCId.setText(rs.getString("Id_CliPro"));
			textCNom.setText(rs.getString("NombreCli"));
			textCDni.setText(rs.getString("DniCli"));
			Direccion= rs.getString("DireccionCli");
			Ruc=rs.getString("RucCli");
			
			cbDocumento.setSelectedItem(rs.getString("TipoDoc"));
			cbSerie.setSelectedItem(Menu.formatid_4.format(Integer.parseInt(rs.getString("SerieTra"))));
			textNumero.setText(rs.getString("NumeroTra"));
			
			buttonGrabar.setIcon(new ImageIcon(VentanaVitrinaVenta.class.getResource("/modelo/Images/convert.png")));
			//buttonGrabar.setEnabled(false);
	        }
           	st.close();
           	rs.close();
            textFormatImporte.setText(Menu.formateadorCurrency.format(IMPORTES));
            textFormatDsct.setText(Menu.formateadorCurrency.format(DESCT));
            textFormatTotal.setText(Menu.formateadorCurrency.format(TOTAL));
           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));

           	Enable(false);
           	buttonPrint.setEnabled(true);cbDocumento.setEnabled(true);cbSerie.setEnabled(true);
           	
           	textPrecio.setBackground(new Color(240, 240, 240));
           	textCantidad.setBackground(new Color(240, 240, 240));
           	tableList.setBackground(new Color(240, 240, 240));
           	
			MOD=1;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
        conexion.DesconectarDB();
	}

	public void llenarCbArtSer() {
		cbArtServ.removeAllItems();
		cbArtServ.addItem("ARTICULO");
		cbArtServ.addItem("SERVICIO");
		cbArtServ.setSelectedIndex(0);
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
			statement.close();
			resultSet.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	
	public void llenarComboSeries() {
		textNumero.setText("");
		cbSerie.removeAllItems();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from SERIES where Id_Doc='" + ID_DOC + "'");
				for (; resultSet.next();) {
					String [] lista = {resultSet.getString("Serie")};
				    Arrays.sort (lista);
					for (String llenar:lista) {
						cbSerie.addItem(Menu.formatid_4.format(Integer.parseInt(llenar)));
					}
				}
			statement.close();
		} catch (Exception e) {}
	}
	void nuevo(){
		conexion = new ConexionDB();
		try {
			// CONSULTO EL CODIGO DE LA TABLE TRANSACCIONAL ::::::::::::::::::::::::::::::::
			Statement s = conexion.gConnection().createStatement();
			ResultSet rs = s.executeQuery("Select Id_Tra from TRANSACCION order by Id_Tra desc limit 0,1");
			if (rs.next()==true) {
				ID_TRA=Integer.parseInt(rs.getString("Id_Tra"))+1;
			}else{
				ID_TRA=1;
			}
			s.close();
			rs.close();
			// CONSULTO EL CODIGO DE LA TABLE TRANSACCIONAL :::::::::::::::::::::::::::::::::
			Enable(true);
		conexion.DesconectarDB();	
		} catch (Exception e) {}
	}
	void Enable(boolean v){
		buttonCliente.setEnabled(v);
		buttonNuevo.setEnabled(v);
		buttonGrabar.setEnabled(v);
		cbDocumento.setEnabled(v);cbSerie.setEnabled(v);
		buttonPrint.setEnabled(v);
		
		buttonAgregar.setEnabled(v);
		buttonQuitar.setEnabled(v);
		buttonArticulo.setEnabled(v);
       	tableList.setEnabled(v);
       	//buttonCliente.setEnabled(false);
       	cbArtServ.setEnabled(v);
       	chooserIngreso.setEnabled(v);
       	textFormatImporte.setEnabled(v);
       	textFormatDsct.setEnabled(v);
       	textFormatTotal.setEnabled(v);
	}
	//METODO GRABAR MODIFICAR
	public void insertarUpdate() {
		if (VentanaLogin.ID_APETURA_CAJA==0){
			JOptionPane.showMessageDialog(null, "Primero debe aperturar su TURNO...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (tableList.getRowCount()==0){
			JOptionPane.showMessageDialog(null, "La lista se encuentra vacía...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "Seleccione la serie",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbSerie.requestFocus();
			return;
		}
		if (chooserIngreso.getDate()==null){
			JOptionPane.showMessageDialog(null, "Falta ingresar la fecha de ingreso...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			chooserIngreso.requestFocus();
			return;
		}

		if (rdbtnEfectivo.isSelected()==false && rdbtnCredito.isSelected()==false){
			JOptionPane.showMessageDialog(null, "Porfavor seleccione modo de pago...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (rdbtnEfectivo.isSelected()==true){
		}

		conexion = new ConexionDB();
		String ID_TEM="",descrip="",precio="",cantidad="",total="";
		int ID_DET_TRA=0;
		float Utl_Uni=0;
		if (MOD==0) {// REGISTRAR
				try {
					
					// CONSULTO EL NUMERO DL DOCUMENTO ::::::::::::::::::::::::::::::::
					int NUMERO=0;
					Statement stat = conexion.gConnection().createStatement();
					ResultSet rss = stat.executeQuery("Select * from SERIES where Id_S='" + ID_SERIE + "'order by NumeroSerie desc limit 0,1");
					if (rss.next()== true) {
						NUMERO=(Integer.parseInt(rss.getString("NumeroSerie")));
						NUMERO=NUMERO+1;
					}
					stat.close();
					rss.close();
					// GRABO NUMERO
					PreparedStatement p = conexion.gConnection().prepareStatement("UPDATE SERIES SET Id_S = ?,"
							 + "NumeroSerie =?"
			                 + "WHERE Id_S = '"+ID_SERIE+"'" );
					p.setString(1, Integer.toString(ID_SERIE));
					p.setString(2, Integer.toString(NUMERO));
					p.executeUpdate();
					p.close();
					// END CONSULTO EL NUMERO DL DOCUMENTO ::::::::::::::::::::::::::::::::
			
					// CONSULTO EL CODIGO DE LA TABLE TRANSACCIONAL ::::::::::::::::::::::::::::::::
					Statement s = conexion.gConnection().createStatement();
					ResultSet rs = s.executeQuery("Select Id_Tra from TRANSACCION order by Id_Tra desc limit 0,1");
					if (rs.next()==true) {
						ID_TRA=Integer.parseInt(rs.getString("Id_Tra"))+1;
					}else{
						ID_TRA=1;
					}
					s.close();
					rs.close();
					// CONSULTO EL CODIGO DE LA TABLE TRANSACCIONAL :::::::::::::::::::::::::::::::::
					PreparedStatement ps = conexion.gConnection().prepareStatement("INSERT INTO TRANSACCION(Id_Tra,Id_CliPro,Id_DocTra,SerieTra,NumeroTra,TipoTra,FechaTra,HoraTra,SubTotalTra,DsctTra,IgvTra,TotalTra,TipoOperacionTra,UtlTra,EstadoTra,IdTurnoT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					ps.setInt(1, ID_TRA);
					ps.setString(2, textCId.getText());
					ps.setString(3, ID_DOC);//(String)cbDocumento.getSelectedItem());
					ps.setString(4, (String)cbSerie.getSelectedItem());
					ps.setString(5, textNumero.getText());
			      	if (rdbtnEfectivo.isSelected()){
			      		ps.setString(6, "CONTADO");
			      		}
			      	if (rdbtnCredito.isSelected()){
			      		ps.setString(6, "CREDITO");
			      		}
					ps.setString(7, FechaEntrada.trim());
					ps.setString(8, Menu.HORA.trim());// HORA NORMAL 12 HILO 
					
					ps.setString(9, textFormatImporte.getText().trim().replaceAll(",", ""));
					ps.setString(10, textFormatDsct.getText().trim().replaceAll(",", ""));
					ps.setString(11,   Menu.formateadorCurrency.format(Float.parseFloat(textFormatTotal.getText().trim().replaceAll(",", ""))-Float.parseFloat(textFormatTotal.getText().trim().replaceAll(",", ""))/Menu.IGV));
					ps.setString(12, textFormatTotal.getText().trim().replaceAll(",", ""));
					ps.setString(13, "VENTA");
					ps.setFloat(14, (UTLT - Float.parseFloat(textFormatDsct.getText().trim().replaceAll(",", ""))));
					ps.setString(15, "1");
					ps.setInt(16, VentanaLogin.ID_APETURA_CAJA);
					ps.execute();
					ps.close();
					
					Enable(false);
		           	buttonPrint.setEnabled(true);buttonNuevo.setEnabled(true);buttonAnular.setEnabled(false);  
					// GRABO EL DETALLE DEL TRANSACCION :::::::::::::::::::::::::::::::::::::::::::::
				    for(int i=0;i<tableList.getRowCount();i++)
				    {
						// CONSULTO EL CODIGO DE LA TABLE TRANSACCIONAL ::::::::::::::::::::::::::::::::
						Statement ss = conexion.gConnection().createStatement();
						ResultSet rr = ss.executeQuery("Select Id_DetTra from DETALLE_TRANSACCION order by Id_DetTra desc limit 0,1");
						if (rr.next()==true) {
							ID_DET_TRA=Integer.parseInt(rr.getString("Id_DetTra"))+1;
						}else{
							ID_DET_TRA=1;
						}
						s.close();
						rr.close();
						// CONSULTO EL CODIGO DE LA TABLE TRANSACCIONAL :::::::::::::::::::::::::::::::::
						
				    	ID_TEM= tableList.getValueAt(i, 0).toString().trim();
				    	descrip=  tableList.getValueAt(i, 1).toString().trim();
				    	precio= tableList.getValueAt(i, 2).toString().trim().replaceAll(",", "");
				    	cantidad=  tableList.getValueAt(i, 3).toString().trim();
				    	total=  tableList.getValueAt(i, 4).toString().trim().replaceAll(",", "");
				    	Utl_Uni= Float.parseFloat(tableList.getValueAt(i, 5).toString().trim().replaceAll(",", ""));
				    	
						PreparedStatement pp = conexion.gConnection().prepareStatement("INSERT INTO DETALLE_TRANSACCION(Id_DetTra,Id_Tra,Id_ArtSer,DescripcionDetTra,PrecioDetTra,CantidadDetTra,ImporteDetTra,UtlDetTra) VALUES (?,?,?,?,?,?,?,?)");
						pp.setInt(1, ID_DET_TRA);
						pp.setInt(2, ID_TRA);
						pp.setString(3, ID_TEM);
				      	pp.setString(4, descrip);
						pp.setString(5, precio);
						pp.setString(6, cantidad);
						pp.setString(7, total);
						pp.setFloat(8, Utl_Uni);
						pp.execute();
						pp.close();
						
						// DESCUENTO EL STOCK DEL ARTICULO
						Statement st = conexion.gConnection().createStatement();
						ResultSet rt = st.executeQuery("Select * from INVENTARIOS where Id_Inv= '"+ ID_TEM +"'and DescripcionInv= '"+ descrip.trim() +"'"); 
						int CAT=0;
						if (rt.next()==true) {
							CAT=(rt.getInt("StockInv"));
					         String sq="UPDATE INVENTARIOS SET StockInv = ?"
					                 + "WHERE Id_Inv = '"+ID_TEM+"'and DescripcionInv= '"+ descrip.trim() +"'"; 
							PreparedStatement pst = conexion.gConnection().prepareStatement(sq);
							pst.setDouble(1, CAT - Double.parseDouble(cantidad));
							pst.executeUpdate();
							pst.close();
							//JOptionPane.showMessageDialog(null, "El stock del artículo " + VentanaConsumo.detalle.trim()  + " fue actualizado",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						}
						st.close();
						rt.close();
						// END DESCUENTO EL STOCK DEL ARTICULO
				    };
					JOptionPane.showMessageDialog(null, "La operación se efectuó correctamente" + Menu.separador
					,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
		}
		if (MOD==1) { // MODIFICAR :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			int resp =0;
			if (cbDocumento.getSelectedItem().equals("TICKET")||cbDocumento.getSelectedItem().equals("RECIBO")){
				resp = JOptionPane.showConfirmDialog (null, "¿ Desea ANULAR el documento anterior \ny generar el " + cbDocumento.getSelectedItem() +"  "+cbSerie.getSelectedItem()+"-"+textNumero.getText()+" ?", Menu.SOFTLE_HOTEL,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			}else if (cbDocumento.getSelectedItem().equals("BOLETA")||cbDocumento.getSelectedItem().equals("FACTURA")){
				resp = JOptionPane.showConfirmDialog (null, "¿ Desea ANULAR el documento anterior \ny generar la " + cbDocumento.getSelectedItem() +"  "+cbSerie.getSelectedItem()+"-"+textNumero.getText()+" ?", Menu.SOFTLE_HOTEL,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			}else if (cbDocumento.getSelectedItem().equals("NINGUNO")){
				resp = JOptionPane.showConfirmDialog (null, "¿ Desea ANULAR el documento anterior \ny no generar NINGUN DOCUMENTO ?\n== registro interno #" +cbSerie.getSelectedItem()+"-"+textNumero.getText()+" ==", Menu.SOFTLE_HOTEL,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			}				
			if (resp == JOptionPane.YES_OPTION) {
				try {
					// GRABO NUMERO
					PreparedStatement p = conexion.gConnection().prepareStatement("UPDATE SERIES SET Id_S = ?,"
							 + "NumeroSerie =?"
			                 + "WHERE Id_S = '"+ID_SERIE+"'" );
					p.setString(1, Integer.toString(ID_SERIE));
					p.setString(2, textNumero.getText());
					p.executeUpdate();
					p.close();
					// END GRABO NUMERO
					
					//===============================ANULO DOCUMENTO
			         String sq="UPDATE TRANSACCION SET Id_Tra = ?,"
			        		 + "EstadoTra =?"
			                 + "WHERE Id_Tra = '"+RptFacturacion.COD_A+"'and TipoOperacionTra ='"+"VENTA"+"'"; 
					PreparedStatement pst = conexion.gConnection().prepareStatement(sq);
					pst.setString(1, Integer.toString(RptFacturacion.COD_A));
					pst.setString(2, "0");
					pst.executeUpdate();
					pst.close();
					//===============================END ANULO DOCMENTO
					
					//========================================================== DUPLICO EL REGISTRO DE VENTA
					Statement s = conexion.gConnection().createStatement();
					ResultSet rs = s.executeQuery("Select Id_Tra from TRANSACCION order by Id_Tra desc limit 0,1");
					if (rs.next()==true) {
						ID_TRA=Integer.parseInt(rs.getString("Id_Tra"))+1;
					}else{
						ID_TRA=1;
					}
					s.close();
					rs.close();
					
					PreparedStatement ps = conexion.gConnection().prepareStatement("INSERT INTO TRANSACCION(Id_Tra,Id_CliPro,Id_DocTra,SerieTra,NumeroTra,TipoTra,FechaTra,HoraTra,SubTotalTra,DsctTra,IgvTra,TotalTra,TipoOperacionTra,UtlTra,EstadoTra,IdTurnoT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					ps.setInt(1, ID_TRA);
					ps.setString(2, textCId.getText());
					ps.setString(3, ID_DOC);//(String)cbDocumento.getSelectedItem());
					ps.setString(4, (String)cbSerie.getSelectedItem());
					ps.setString(5, textNumero.getText());
			      	if (rdbtnEfectivo.isSelected()){
			      		ps.setString(6, "CONTADO");
			      		}
			      	if (rdbtnCredito.isSelected()){
			      		ps.setString(6, "CREDITO");
			      		}
					ps.setString(7, FechaEntrada.trim());
					ps.setString(8, Menu.HORA.trim());// HORA NORMAL 12 HILO 
					
					ps.setString(9, textFormatImporte.getText().trim().replaceAll(",", ""));
					ps.setString(10, textFormatDsct.getText().trim().replaceAll(",", ""));
					ps.setString(11,  Menu.formateadorCurrency.format(Float.parseFloat(textFormatTotal.getText().trim().replaceAll(",", ""))-Float.parseFloat(textFormatTotal.getText().trim().replaceAll(",", ""))/Menu.IGV));
					ps.setString(12, textFormatTotal.getText().trim().replaceAll(",", ""));
					ps.setString(13, "VENTA");
					ps.setFloat(14, UTLT);
					ps.setString(15, "1");
					ps.setInt(16, VentanaLogin.ID_APETURA_CAJA);
					ps.execute();
					ps.close();
					//========================================================== END DUPLICO EL REGISTRO DE VENTA
					
					// GRABO EL DETALLE DEL TRANSACCION :::::::::::::::::::::::::::::::::::::::::::::
				    for(int i=0;i<tableList.getRowCount();i++)
				    {
						// CONSULTO EL CODIGO DE LA TABLE TRANSACCIONAL ::::::::::::::::::::::::::::::::
						Statement ss = conexion.gConnection().createStatement();
						ResultSet rr = ss.executeQuery("Select Id_DetTra from DETALLE_TRANSACCION order by Id_DetTra desc limit 0,1");
						if (rr.next()==true) {
							ID_DET_TRA=Integer.parseInt(rr.getString("Id_DetTra"))+1;
						}else{
							ID_DET_TRA=1;
						}
						s.close();
						rr.close();
						// CONSULTO EL CODIGO DE LA TABLE TRANSACCIONAL :::::::::::::::::::::::::::::::::
						
				    	ID_TEM= tableList.getValueAt(i, 0).toString().trim();
				    	descrip=  tableList.getValueAt(i, 1).toString().trim();
				    	precio= tableList.getValueAt(i, 2).toString().trim().replaceAll(",", "");
				    	cantidad=  tableList.getValueAt(i, 3).toString().trim();
				    	total=  tableList.getValueAt(i, 4).toString().trim().replaceAll(",", "");
				    	Utl_Uni= Float.parseFloat(tableList.getValueAt(i, 5).toString().trim().replaceAll(",", ""));
				    	
						PreparedStatement pp = conexion.gConnection().prepareStatement("INSERT INTO DETALLE_TRANSACCION(Id_DetTra,Id_Tra,Id_ArtSer,DescripcionDetTra,PrecioDetTra,CantidadDetTra,ImporteDetTra,UtlDetTra) VALUES (?,?,?,?,?,?,?,?)");
						pp.setInt(1, ID_DET_TRA);
						pp.setInt(2, ID_TRA);
						pp.setString(3, ID_TEM);
				      	pp.setString(4, descrip);
						pp.setString(5, precio);
						pp.setString(6, cantidad);
						pp.setString(7, total);
						pp.setFloat(8, Utl_Uni);
						pp.execute();
						pp.close();
				    };
				    Enable(false);
					buttonPrint.setEnabled(true);buttonAnular.setEnabled(false);  
				    
					if (cbDocumento.getSelectedItem().equals("TICKET")||cbDocumento.getSelectedItem().equals("RECIBO")||cbDocumento.getSelectedItem().equals("BOLETA")||cbDocumento.getSelectedItem().equals("FACTURA")){
						JOptionPane.showMessageDialog(null, "El cambio de documento se efectuo correctamente" + Menu.separador + 
								"::::::::::::::::::::::: "  +cbDocumento.getSelectedItem() +"  "+cbSerie.getSelectedItem()+"-"+textNumero.getText()+  " :::::::::::::::::::::::"
								,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						
					}else if (cbDocumento.getSelectedItem().equals("NINGUNO")){
						JOptionPane.showMessageDialog(null, "El cambio de documento se efectuo correctamente" + Menu.separador + 
								":::::::::::::: "  +" Registro interno #"+cbSerie.getSelectedItem()+"-"+textNumero.getText()+  " :::::::::::::::"
								,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}	
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
			}else if (resp == JOptionPane.NO_OPTION) {}
		}
		conexion.DesconectarDB();
	}
	void anularVenta(){
		conexion = new ConexionDB();
		try {
			int resp = JOptionPane.showConfirmDialog (null, "¿Desea anular la venta? \n::::::::::: " + Menu.formatid_9.format(RptFacturacion.COD_A) + " :::::::::::" , Menu.SOFTLE_HOTEL,
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resp == JOptionPane.YES_OPTION) {
				String ID_TEM="",cantidad="";
				// CONSULTO A LA TABLE :::::::::::::::::::::::::::::::::::::::::::::
			    for(int i=0;i<tableList.getRowCount();i++)
			    {
			    	ID_TEM= tableList.getValueAt(i, 0).toString().trim();
			    	cantidad=  tableList.getValueAt(i, 3).toString().trim();
	
					// AUMENTO EL STOCK DEL ARTICULO
					Statement st = conexion.gConnection().createStatement();
					ResultSet rt = st.executeQuery("Select * from INVENTARIOS where Id_Inv= '"+ ID_TEM +"'"); 
					int CAT=0;
					if (rt.next()==true) {
						CAT=(rt.getInt("StockInv"));
				         String sql="UPDATE INVENTARIOS SET StockInv = ?"
				                 + "WHERE Id_Inv = '"+ID_TEM+"'"; 
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setDouble(1, CAT + Double.parseDouble(cantidad));
						ps.executeUpdate();
						ps.close();
					}
					st.close();
					// END AUMENTO EL STOCK DEL ARTICULO
			    };
			    // END CONSULTO A LA TABLE :::::::::::::::::::::::::::::::::::::::::::::
			    String sq="UPDATE TRANSACCION SET EstadoTra = ?"
		                 + "WHERE Id_Tra = '"+RptFacturacion.COD_A+"'"; 
				PreparedStatement pst = conexion.gConnection().prepareStatement(sq);
				pst.setString(1, "0");
				pst.executeUpdate();
				pst.close();
				buttonGrabar.setEnabled(false);
				JOptionPane.showMessageDialog(null, "El documento fue ANULADO",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
			}else if (resp == JOptionPane.NO_OPTION) {}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al anular" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		conexion.DesconectarDB();
	}
	public void mostrarDscto() {
		try {
			if (textFormatDsct.getText().isEmpty()) {
				textFormatDsct.setText("0.00");
				textFormatDsct.selectAll();
				textFormatDsct.requestFocus();
			}
			float importe=(Float.parseFloat(textFormatImporte.getText().replaceAll(",", "")));
			float amortizo=(Float.parseFloat(textFormatDsct.getText().replaceAll(",", "")));
			float total=(importe-amortizo);
			textFormatTotal.setText(Menu.formateadorCurrency.format(total));
			textFormatImporte.setText(Menu.formateadorCurrency.format(Float.parseFloat(textFormatImporte.getText())));
			textFormatTotal.setText(Menu.formateadorCurrency.format(Float.parseFloat(textFormatTotal.getText())));
			
		} catch (Exception e) {}
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	void limpiarText() {
		textCod.setText(null);
		textDescripcion.setText(null);
		textPrecio.setText(null);
		textCantidad.setText(null);
	}
	//	METODO ELIMINAR
	public void delete(int caso) {
		try{
		    switch(caso){
		      case 1:
		    	 tableList.removeColumn(tableList.getColumnModel().getColumn(tableList.getSelectedColumn()));//Elimna la columna selecionada por el usuario
		        break;
		      case 2:
		        model.removeRow(tableList.getSelectedRow());//Elimna la fila selecionada por el usuario
		        break;
		    }
		 }catch(ArrayIndexOutOfBoundsException aIE){}
	}
	
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == buttonNuevo){// BUSCAR ARTICULO O SERVICIO
			limpiarText();
			nuevo();
			buttonGrabar.setEnabled(true);buttonPrint.setEnabled(false);
			cbDocumento.setSelectedIndex(-1);cbSerie.setSelectedIndex(-1);textNumero.setText(null);
			textFormatImporte.setText("0.00");textFormatDsct.setText("0.00");textFormatTotal.setText("0.00");
			textCId.setText(null);textCNom.setText(null);textCDni.setText(null);
			LimpiarTable();
		}
		if (evento.getSource() == cbArtServ){// BUSCAR ARTICULO O SERVICIO
			limpiarText();
		}
		if (evento.getSource() == buttonCliente){// BUSCAR CLIENTE
			VentanaCliente ventana =new VentanaCliente();
			VentanaCliente.FILTRO_CLI="VV";
      		ventana.frame.setVisible(true);
            ventana.llenarTable("Select * from Clientes order by NombreCli asc");
            ventana.textBus.requestFocus(true);
		}
		if (evento.getSource() == buttonArticulo){// BUSCAR ARTICULO
			if (cbArtServ.getSelectedItem()=="ARTICULO") {
				 VentanaInventarios a = new VentanaInventarios();
				 VentanaInventarios.frame.show();
				 VentanaInventarios.FILTRO_INV = 2;
				 VentanaInventarios.frame.setTitle("Search productos:");
				 a.textBus.setBounds(333, 21, 350, 22);
				 a.buttonBus.setBounds(690, 20, 36, 23);
				 a.textBus.requestFocus(true);
				 a.buttonNuevo.setVisible(false);
				 a.buttonEditar.setVisible(false);
				 a.buttonEliminar.setVisible(false);
				 
				 a.cbFamilia.removeAllItems();
	      		 a.cbFamilia.addItem("VITRINA");
			}
			if (cbArtServ.getSelectedItem()=="SERVICIO") {
				 VentanaServicio v = new VentanaServicio();
				 v.frame.show();
				 VentanaServicio.FILTRO_SER = 2;
				 v.frame.setTitle("Search servicios:");
				 v.textBus.requestFocus(true);
				 v.buttonNuevo.setEnabled(false);
				 v.buttonEditar.setEnabled(false);
				 v.buttonEliminar.setEnabled(false);
			}
		}	 
		
		if (evento.getSource()==buttonAgregar) {
				if (textCod.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "No se filtro ningún ítem",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (textPrecio.getText().equals("")||textPrecio.getText().equals("0")) {
					JOptionPane.showMessageDialog(null, "Ingrese el precio de venta...",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textPrecio.requestFocus();
					textPrecio.selectAll();
					return;
				}
				if (textCantidad.getText().isEmpty()||Integer.parseInt(textCantidad.getText())==0) {
					JOptionPane.showMessageDialog(null, "Ingrese la cantidad...",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textCantidad.requestFocus();
					textCantidad.selectAll();
					return;
				}
				try{
					Statement s = conexion.gConnection().createStatement();
					ResultSet r = s.executeQuery("Select * from INVENTARIOS where DescripcionInv= '"+ textDescripcion.getText().trim() +"'"); 
					int CAT=0;
					if (r.next()==true) {
						CAT=(r.getInt("StockInv"));
						if (MOD==1) {CAT= CAT + VentanaConsumo.cantidad;}
						if (Integer.parseInt(textCantidad.getText()) > CAT){
							JOptionPane.showMessageDialog(null, "La cantidad ingresada supera "+ Menu.separador +"las " +  CAT  + " unidades existentes en stock " ,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
							textCantidad.requestFocus();
							textCantidad.selectAll();
							return;
						}
						s.close();
						r.close();
					}	
				} catch (Exception e) {}
				if (!textCod.getText().equals("")) {
					 for(int i=0;i<tableList.getRowCount();i++)
					    {
						 String cod =(String)tableList.getValueAt(i, 0);
						 String des =(String)tableList.getValueAt(i, 1);
					    if (textCod.getText().equals(cod.trim())&&textDescripcion.getText().equals(des.trim())){
							JOptionPane.showMessageDialog(null, "El ítem ya fue ingresado a la lista",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
							return;
					    }
					 };
				}
				try {
		            datos[0]=" "+textCod.getText();
		            datos[1]=" "+textDescripcion.getText();
		            datos[2]=" "+Menu.formateadorCurrency.format(Float.parseFloat(textPrecio.getText().trim().replaceAll(",", "")));
		            datos[3]=" "+textCantidad.getText();
		            datos[4]=" "+Menu.formateadorCurrency.format(Float.parseFloat(textPrecio.getText().trim().replaceAll(",", "")) * Float.parseFloat(textCantidad.getText()));
		            if (cbArtServ.getSelectedItem().equals("ARTICULO")) {
		            	datos[5]=" "+Menu.formateadorCurrency.format((Float.parseFloat(textPrecio.getText().trim().replaceAll(",", "")) - COSTO) * (Float.parseFloat(textCantidad.getText())));
			            UTLT = UTLT + (Float.parseFloat(textPrecio.getText().trim().replaceAll(",", "")) - COSTO) * (Float.parseFloat(textCantidad.getText()));
		            }
		            if (cbArtServ.getSelectedItem().equals("SERVICIO")) {
		            	datos[5]=" "+Menu.formateadorCurrency.format(Float.parseFloat(textPrecio.getText().trim().replaceAll(",", "")) * Float.parseFloat(textCantidad.getText()));
			            UTLT = UTLT + Float.parseFloat(textPrecio.getText().trim().replaceAll(",", "")) * Float.parseFloat(textCantidad.getText());
		            }
		            totalitems=totalitems+1;
		            model.addRow(datos);
		            tableList.setModel(model);
		            
		            IMPORTE = IMPORTE + (Float.parseFloat(textPrecio.getText().trim().replaceAll(",", ""))*Float.parseFloat(textCantidad.getText()));
		            DSCT =  (Float.parseFloat(textFormatDsct.getText()));
		            TOTAL = (IMPORTE - DSCT);
	
		            textFormatImporte.setText(Float.toString(IMPORTE));
		            limpiarText();
		            mostrarDscto();
		            panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
				} catch (Exception e) {}
		}
		if (evento.getSource()==buttonQuitar) { // QUITAR ITEM DE TABLE
			try {
				if (id==null || id.equals("")){
					return;
				}
				int resp = JOptionPane.showConfirmDialog (null, "¿Desea quitar el ítem?" + Menu.separador + id + " " +  descripcion, Menu.SOFTLE_HOTEL,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resp == JOptionPane.YES_OPTION) {
							delete(2);
				}else if (resp == JOptionPane.NO_OPTION) {}
			    totalitems=0;
				 IMPORTE=0;
			    for(int i=0;i<tableList.getRowCount();i++)
			    {
			        totalitems=totalitems+1;
			    	IMPORTE=IMPORTE +Float.parseFloat((String) tableList.getValueAt(i, 4).toString().replaceAll(",", ""));
			    };
			    textFormatImporte.setText(Float.toString(IMPORTE));
			    mostrarDscto();
			
			    panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
			} catch (Exception e) {}    
		}
		if (evento.getSource().equals(cbDocumento)) {
			ID_DOC="";
			conexion = new ConexionDB();
			try {
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * from DOCUMENTO where TipoDoc='" + cbDocumento.getSelectedItem() + "'");
				if ( resultSet.next()==true) {
					ID_DOC =resultSet.getString("Id_Doc");
				}
				statement.close();
			} catch (Exception e) {}
			llenarComboSeries();
		}
		if (evento.getSource().equals(cbSerie)) {
			try {
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * from SERIES where Id_Doc='" + ID_DOC + "' and serie='" + cbSerie.getSelectedItem() + "'");
				if ( resultSet.next()==true) {
					buttonGrabar.setEnabled(true);buttonCliente.setEnabled(true);
					buttonPrint.setEnabled(false);
					textNumero.setText(Menu.formatid_9.format(Integer.parseInt(resultSet.getString("NumeroSerie"))+1));
					ID_SERIE=Integer.parseInt(resultSet.getString("Id_S"));
				}
				statement.close();
			} catch (Exception e) {}
		}
      	if (evento.getSource().equals(textFormatDsct)){
      		mostrarDscto();
      	}
      	if (evento.getSource().equals(rdbtnEfectivo)){
      		//textFormatAmortizo.setEnabled(false);
      	}
      	if (evento.getSource().equals(rdbtnCredito)){
      		//textFormatAmortizo.setEnabled(true);
      	}
      	if (evento.getSource()==buttonGrabar) {
      		insertarUpdate();
      	}
		if (evento.getSource().equals(buttonPrint)) {
			JasperViewer visor;
			conexion = new ConexionDB();
			try {
				if (cbDocumento.getSelectedItem().equals("NINGUNO")){
					JOptionPane.showMessageDialog(null, "No seleccionó ningún documento para imprimir\nRegistro interno #"+cbSerie.getSelectedItem()+"-"+textNumero.getText(),Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					return;
				}
				// ACTUALIZO FECHA ACTUAL DE ENTRADA
				SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
				String FechaEntrada = form.format(Menu.fecha);

				//CREAR EL MAPA DE PARAMETROS
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("PrtId_User",new Integer (VentanaLogin.COD_EMP_USER));
				parameters.put("PrtNombreUser",new String (VentanaLogin.NOM_USUARIO));
				parameters.put("PrtFecha",new String (FechaEntrada  +"   " + Menu.HORA.trim()));//12 HORAS HILO
				parameters.put("PrtCliente",new String (textCNom.getText()));
				parameters.put("PrtDireccion",new String (Direccion));
				parameters.put("PrtDni",new String (textCDni.getText()));
				
				parameters.put("PrtTurno",new String (VentanaLogin.TURNO));
				
				//String DOC=cbDocumento.getSelectedItem().toString().substring(0, 1) +"/";
				//parameters.put("PrtIdDoc",new String (DOC));
				
				parameters.put("PrtIdA",new Integer (ID_TRA));
				parameters.put("PrtOperacion",new String ("VENTA"));
				parameters.put("PrtEstado",new Integer (1));
				
				JasperPrint jp = new JasperPrint(); 
				if (cbDocumento.getSelectedItem().equals("TICKET")) {
					jp = JasperFillManager.fillReport(Menu.URL+"Ticket_Vitrina.jasper",parameters,conexion.gConnection());
				}
				if (cbDocumento.getSelectedItem().equals("BOLETA")) {
					JOptionPane.showMessageDialog(null, "Falta pre-diseñar la BOLETA",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					return;
				}	
				if (cbDocumento.getSelectedItem().equals("FACTURA")) {
					JOptionPane.showMessageDialog(null, "Falta pre-diseñar la FACTURA",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					return;
				}	
				
				visor = new JasperViewer(jp,false) ;
				visor.setTitle("Documento de venta");
				visor.setVisible(true) ; 
				visor.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
				
			    //EXPORTAR EL INFORME A PDF
			    //JasperExportManager.exportReportToPdfFile(jp, Menu.URL+"Ticket_Vitrina.pdf");
			      
				}
				catch (Throwable e) {
				e.printStackTrace();
				}
			conexion.DesconectarDB();
		}
      	if (evento.getSource()==buttonAnular) {
			anularVenta(); 
      	}
      	if (evento.getSource()==buttonSalir) {
      		frame.dispose();
      	}
      	
	}
	       
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==(chooserIngreso)){
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			FechaEntrada = form.format(chooserIngreso.getDate());
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
		if (evet.getSource() == cbDocumento){
			if (e==KeyEvent.VK_ENTER){
				if (cbDocumento.getSelectedIndex()!=-1){
					cbSerie.requestFocus();
				}else{
					cbDocumento.requestFocus();
				}
			}
		} 
		if (evet.getSource() == cbSerie){
			if (e==KeyEvent.VK_ENTER){
				if (cbSerie.getSelectedIndex()!=-1){
					textPrecio.requestFocus();
					textPrecio.selectAll();
				}else{
					cbSerie.requestFocus();
				}
			}
		}
		if (evet.getSource().equals(textCantidad)){
			if (textCantidad.getText().toLowerCase().isEmpty()|| textCantidad.getText().toLowerCase().length()>=6){
				textCantidad.requestFocus();
				textCantidad.selectAll();
				textCantidad.setText(null);
				} 
				else if (e==KeyEvent.VK_ENTER){
					buttonAgregar.doClick();
				}
		}
		if (evet.getSource().equals(textPrecio)){
			if (textPrecio.getText().toLowerCase().isEmpty()|| textPrecio.getText().toLowerCase().length()>7){
				textPrecio.requestFocus();
				textPrecio.selectAll();
				textPrecio.setText(null);
				} 
				else if (e==KeyEvent.VK_ENTER || textPrecio.getText().toLowerCase().length()==7){
					textCantidad.requestFocus();
					textCantidad.selectAll();
				}
		}
		if (evet.getSource().equals(textFormatDsct)){
			if (textFormatDsct.getText().toLowerCase().isEmpty()|| textFormatDsct.getText().toLowerCase().length()>7){
				textFormatDsct.requestFocus();
				textFormatDsct.selectAll();
				textFormatDsct.setText(null);
				} 
				else if (e==KeyEvent.VK_ENTER || textFormatDsct.getText().toLowerCase().length()==7){
				}
		}
		
      	if (evet.getSource().equals(textFormatDsct)){
      		mostrarDscto();
      	}
      	if (evet.getSource().equals(textPrecio) || evet.getSource().equals(textCantidad)){
      		if (textCantidad.getText().length()>0) {
	      		//mostrarImporte();
	      		mostrarDscto();
      		}
      	}
	}
	@Override
	public void keyTyped(KeyEvent evet) {
		// PRECIONA EL TECLADO Y ME DA EL EVENTO
		char car=evet.getKeyChar();

		if (evet.getSource() == textDescripcion){
			if((car<'a' || car>'z') && (car<'A' || car>'Z')&&(car<' '||car>'.')&&(car<'0'||car>'9')) evet.consume();
		}
		if (evet.getSource() == textPrecio){ 
			if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
		}
		if (evet.getSource() == textCantidad){ 
			if ((car<'0'||car>'9'))evet.consume();
		}
		if (evet.getSource() == textFormatDsct){ 
			if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
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
		try {
			id="";descripcion="";
			if (Mouse_evt.getSource().equals(tableList)) {
				id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
				if (id==null || id.equals("")){
					tableList.requestFocus();
					return;
				}
				buttonQuitar.setEnabled(true);
				id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
				descripcion=(String) tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
				if (Mouse_evt.getClickCount() == 2) {
					buttonQuitar.setEnabled(false);
				}
			}
		} catch (Exception e) {
			//JOptionPane.showMessageDialog(null, "Error metodo click " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
