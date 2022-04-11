
package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
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
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;
import javax.swing.border.LineBorder;

public class VentanaInventariosAgregar implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JInternalFrame frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl7,lbl8,lbl9,lbl10,lbl11,lbl12,lblFto;
	private JButton  			buttonGrabar,buttonSalir,buttonBus,buttonImg;
	protected static JDateChooser dateChooserAlta;
	protected static String dateAlta;
	protected JTextField 			textCod,textDescripcion,textMarca;
	protected static JComboBox<String> 		cbTipo,cbFamilia,cbEstado;
	protected static JFormattedTextField 	textFormatPrecio,textFormatCosto,textFormatStock,textFormatStockMinimo;
	
	private JScrollPane scrollArea;
	protected static JTextArea textArea = new JTextArea();
	
	public File fichero;
	private String almacenaFoto;
	private AbrirFoto archivo;
	
	private int Id_Cate;
	public static int CONTAR_VENTANA_AGREGAR_INVENTARIOS=0;
	public static int MOD;
	String cadenaDescripcion="";
	
	NumberFormat formatDsct;

	// constructor
	public VentanaInventariosAgregar() {
		frameInventariosAgregar();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		llenarcombox();
		limpiarTexts();
		llenarNuevo();
		llenarcbCategoria();
		dateChooserAlta.setDate(Menu.fecha);
		if (MOD==1) {
			llenarModificar();
		}
		CONTAR_VENTANA_AGREGAR_INVENTARIOS ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.

		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dateAlta = form.format(dateChooserAlta.getDate());
		
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    Menu.Desktop.add(frame);
	}
	public void frameInventariosAgregar() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaInventariosAgregar.class.getResource("/modelo/Images/folder-document.png")));
		frame.setTitle("Agregar artículos");
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			//@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				CONTAR_VENTANA_AGREGAR_INVENTARIOS=0;
			}
		});
		frame.setClosable(true);
		frame.setBounds(100, 100, 594, 339);
		frame.getContentPane().setLayout(null);
	}
	
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 558, 284);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		dateChooserAlta = new JDateChooser();
		dateChooserAlta.setDateFormatString("dd-MMM-yyyy");
		dateChooserAlta.setBorder(new LineBorder(new Color(51, 153, 255)));
		dateChooserAlta.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)dateChooserAlta.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)dateChooserAlta.getDateEditor()).setEditable(false);
		dateChooserAlta.setToolTipText("Fecha de alta");
		dateChooserAlta.setBounds(230, 58, 95, 20);
		dateChooserAlta.addPropertyChangeListener(this);
		panelDto.add(dateChooserAlta);
	}
	public void crearLabels() {
		lbl1= new JLabel("Código:");
		lbl1.setBounds(32, 35, 61, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Categoria:");
		lbl2.setBounds(20, 86, 73, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Descripción:");
		lbl3.setBounds(12, 110, 81, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Marca:");
		lbl4.setBounds(339, 14, 57, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);
		lbl4.setVisible(false);
		
		lbl5= new JLabel("Costo:");
		lbl5.setBounds(103, 131, 57, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);

		lbl7= new JLabel("Tipo inventario:");
		lbl7.setBounds(10, 62, 83, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Precio:");
		lbl8.setBounds(177, 131, 43, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		lbl9 = new JLabel("Stock M:");
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl9.setBounds(230, 131, 49, 14);
		panelDto.add(lbl9);
		
		lbl10 = new JLabel("Stock:");
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl10.setBounds(282, 131, 46, 14);
		panelDto.add(lbl10);
		
		lbl11 = new JLabel("Observación:");
		lbl11.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl11.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl11.setBounds(10, 173, 83, 14);
		panelDto.add(lbl11);
		
		lbl12 = new JLabel("Estado:");
		lbl12.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl12.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl12.setBounds(343, 131, 40, 14);
		panelDto.add(lbl12);
		
		lblFto= new JLabel("Sin image");
		lblFto.setBounds(393, 28, 144, 115);
		panelDto.add(lblFto);
		lblFto.setHorizontalAlignment(SwingConstants.CENTER);
		lblFto.setBorder(BorderFactory.createEtchedBorder());
	
	}
	public void crearTextFields(){
		
		textCod = new JTextField();
		textCod.setEditable(false);
		textCod.setColumns(10);
		textCod.setFont(Menu.fontText);
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.setHorizontalAlignment(SwingConstants.CENTER);
		textCod.addActionListener(this);
		textCod.addFocusListener(this);
		textCod.addKeyListener(this);
		textCod.setBounds(103, 31, 77, 22);
		panelDto.add(textCod);
		
		textDescripcion= new JTextField();
		textDescripcion.setColumns(10);
		textDescripcion.setFont(Menu.fontText);
		textDescripcion.setForeground(Menu.textColorForegroundInactivo);
		textDescripcion.setHorizontalAlignment(SwingConstants.LEFT);
		textDescripcion.addActionListener(this);
		textDescripcion.addFocusListener(this);
		textDescripcion.addKeyListener(this);
		textDescripcion.setBounds(103, 107, 280, 22);
		panelDto.add(textDescripcion);
		
		textMarca= new JTextField();
		textMarca.setColumns(10);
		textMarca.setFont(Menu.fontText);
		textMarca.setForeground(Menu.textColorForegroundInactivo);
		textMarca.setHorizontalAlignment(SwingConstants.LEFT);
		textMarca.addActionListener(this);
		textMarca.addFocusListener(this);
		textMarca.addKeyListener(this);
		textMarca.addPropertyChangeListener(this);
		textMarca.setBounds(406, 11, 131, 22);
		panelDto.add(textMarca);
		textMarca.setVisible(false);

		scrollArea= new JScrollPane();
		scrollArea.setBounds(103, 170, 434, 72);
		panelDto.add(scrollArea);
		textArea = new JTextArea();
		scrollArea.setViewportView(textArea);
		textArea.setFont(Menu.fontText);
		textArea.setForeground(Menu.textColorForegroundInactivo);
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);

		textFormatPrecio = new JFormattedTextField();
		textFormatPrecio.setBackground(new Color(255, 255, 255));
		textFormatPrecio.setText("0.00");
		textFormatPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatPrecio.setBounds(163, 144, 57, 21);
		textFormatPrecio.addFocusListener(this);
		textFormatPrecio.addKeyListener(this);
		panelDto.add(textFormatPrecio);
		
		textFormatCosto = new JFormattedTextField();
		textFormatCosto.setBackground(new Color(255, 255, 255));
		textFormatCosto.setText("0.00");
		textFormatCosto.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatCosto.setBounds(103, 144, 57, 21);
		textFormatCosto.addFocusListener(this);
		textFormatCosto.addKeyListener(this);
		panelDto.add(textFormatCosto);
		
		textFormatStock = new JFormattedTextField();
		textFormatStock.setText("0");
		textFormatStock.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatStock.setBounds(282, 144, 50, 21);
		textFormatStock.addActionListener(this);
		textFormatStock.addPropertyChangeListener(this);
		textFormatStock.addKeyListener(this);
		textFormatStock.addFocusListener(this);
		panelDto.add(textFormatStock);
		
		textFormatStockMinimo = new JFormattedTextField();
		textFormatStockMinimo.setText("0");
		textFormatStockMinimo.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatStockMinimo.setBounds(222, 144, 57, 21);
		textFormatStockMinimo.addActionListener(this);
		textFormatStockMinimo.addPropertyChangeListener(this);
		textFormatStockMinimo.addKeyListener(this);
		textFormatStockMinimo.addFocusListener(this);
		panelDto.add(textFormatStockMinimo);

	}
	
	public void crearComboBox() { 
		cbTipo = new JComboBox<>();
		cbTipo.setBounds(103, 82, 187, 21);
		cbTipo.setFont(Menu.fontText);
		cbTipo.addActionListener(this);
		cbTipo.addFocusListener(this);
		cbTipo.addKeyListener(this);
		panelDto.add(cbTipo);
		
		cbFamilia = new JComboBox<>();
		cbFamilia.setBounds(103, 57, 122, 21);
		cbFamilia.setFont(Menu.fontText);
		cbFamilia.addActionListener(this);
		cbFamilia.addFocusListener(this);
		cbFamilia.addKeyListener(this);
		panelDto.add(cbFamilia);
		
		cbEstado = new JComboBox<>();
		cbEstado.setBounds(333, 144, 50, 21);
		cbEstado.setFont(Menu.fontText);
		cbEstado.addActionListener(this);
		cbEstado.addFocusListener(this);
		cbEstado.addKeyListener(this);
		panelDto.add(cbEstado);
	}
	public void llenarNuevo() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_Inv from INVENTARIOS order by Id_Inv desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_Inv"))+1);
				textCod.setText(Menu.formatid_7.format(id));
			}else {
				textCod.setText(Menu.formatid_7.format(0000001));
			}
			statement.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al load" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void llenarModificar() { 
		conexion= new ConexionDB();
		try {
			String con = "Select * from INVENTARIOS,CATEGORIA where INVENTARIOS.Id_Cat = CATEGORIA.Id_Cat and INVENTARIOS.Id_Inv ='" + Integer.parseInt(VentanaInventarios.id) + "'";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			if (rs.next()==true) {
		        almacenaFoto="";
		        lblFto.setText("Sin image");
		        lblFto.setIcon(null);
		        
				dateAlta=(rs.getString("FechaActualizarInv").toString());
				Date date = Menu.formatoFecha.parse(dateAlta);
				dateChooserAlta.setDate(date);
				dateChooserAlta.setEnabled(false);
				
		        cbFamilia.setSelectedItem(rs.getString("FamiliaCat"));
				cbTipo.setSelectedItem(rs.getString("DescripcionCat"));
				textDescripcion.setText(rs.getString("DescripcionInv"));
				textMarca.setText(rs.getString("MarcaInv"));
				textFormatCosto.setText(rs.getString("CostoInv"));
				textFormatPrecio.setText(rs.getString("PrecioInv"));
				textFormatStock.setText(rs.getString("StockInv"));
				textFormatStockMinimo.setText(rs.getString("StockMinimoInv"));
				textArea.setText(rs.getString("ObservacionInv"));
				cbEstado.setSelectedItem(rs.getString("EstadoInv"));
				almacenaFoto=rs.getString("URLInv");
				// CARGO LA IMAGEN
			    Image i=null;
		        Blob blob = rs.getBlob("ImageInv");
		        i= javax.imageio.ImageIO.read(blob.getBinaryStream());
		        ImageIcon image = new ImageIcon(i);
				Icon icono = new ImageIcon(image.getImage().getScaledInstance(lblFto.getWidth(), lblFto.getHeight(), Image.SCALE_DEFAULT)); 
				lblFto.setText(null);
				lblFto.setIcon(icono);
			}
			st.close();
		} catch (Exception e) {}
	}
	public static void llenarcombox() { 
		cbEstado.removeAllItems();
		cbEstado.addItem("A");
		cbEstado.addItem("X");
		cbEstado.setSelectedIndex(0);
		
		cbFamilia.removeAllItems();
		cbFamilia.addItem("INV_HOTEL");
		cbFamilia.addItem("VITRINA");
		cbFamilia.setSelectedIndex(-1);
	}
	public static void llenarcbCategoria() { 
		cbTipo.removeAllItems();
		try {
			String con = "Select * from CATEGORIA where FamiliaCat='" +cbFamilia.getSelectedItem() + "'order by  DescripcionCat asc";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			while (rs.next()==true) {
				cbTipo.addItem(rs.getString("DescripcionCat"));
			}
			cbTipo.setSelectedIndex(-1);
			st.close();
		} catch (Exception e) {}
	}
	public void mostrarcbTipo() { 
		try {
			Id_Cate=0;
			String con = "Select * from CATEGORIA where DescripcionCat ='" + cbTipo.getSelectedItem().toString().trim() + "'";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			if (rs.next()==true) {
				Id_Cate=rs.getInt("Id_Cat");
			}
			st.close();
		} catch (Exception e) {}
	}
	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(464, 250, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaInventariosAgregar.class.getResource("/modelo/Images/save.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Cerrar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(501, 250, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaInventariosAgregar.class.getResource("/modelo/Images/Exit.png")));
		panelDto.add(buttonSalir);
		
		buttonBus= new JButton("");
		buttonBus.setBounds(292, 81, 36, 22);
		buttonBus.setToolTipText("Buscar");
		buttonBus.addActionListener(this);
		buttonBus.setIcon(new ImageIcon(VentanaInventariosAgregar.class.getResource("/modelo/Images/search.png")));
		panelDto.add(buttonBus);

		buttonImg= new JButton("");
		buttonImg.setToolTipText("Agregar imagen");
		buttonImg.addActionListener(this);
		buttonImg.setBounds(393, 140, 144, 23);
		buttonImg.setIcon(new ImageIcon(VentanaInventariosAgregar.class.getResource("/modelo/Images/blue.png")));
		panelDto.add(buttonImg);
	}
	
	public void limpiarTexts() {
		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.requestFocus(true);
		
		textFormatPrecio.setText(null);
		textFormatPrecio.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatPrecio.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatCosto.setText(null);
		textFormatCosto.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatCosto.setForeground(Menu.textColorForegroundInactivo);
		
		textDescripcion.setText(null);
		textDescripcion.setBackground(Menu.textColorBackgroundInactivo);	
		textDescripcion.setForeground(Menu.textColorForegroundInactivo);
		
		textMarca.setText(null);
		textMarca.setBackground(Menu.textColorBackgroundInactivo);	
		textMarca.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatStock.setText(null);
		textFormatStock.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatStock.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatStockMinimo.setText(null);
		textFormatStockMinimo.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatStockMinimo.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
		
		cbTipo.removeAllItems();

        almacenaFoto="";
        lblFto.setText("Sin image");
        lblFto.setIcon(null);
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textDescripcion.setEnabled(b);
		textMarca.setEnabled(b);
		textFormatPrecio.setEnabled(b);
		textFormatCosto.setEnabled(b);
		textFormatStock.setEnabled(b);
		textFormatStockMinimo.setEnabled(b);
		textArea.setEnabled(b);
		cbTipo.setEnabled(b);
	}
	public void activarButton(boolean c) {
		 if (c == true){
			buttonGrabar.setEnabled(false);// GRABAR
			buttonSalir.setEnabled(true);	// SALIR

			buttonBus.setEnabled(true);// BUSCAR
			
			panelDto.setVisible(true);// PANEL DATOS
		 }
		 if (c == false){
			buttonGrabar.setEnabled(true);// GRABAR
			buttonSalir.setEnabled(false);	// SALIR

			//buttonDsct.setEnabled(true);// ALTA DE DSCT A CLIENTES
			buttonBus.setEnabled(false);// BUSCAR
			
			panelDto.setVisible(true);// PANEL DATOS
		 }
	}
	
	
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonGrabar){// GRABAR
			  insertarUpdate();
			  }
		  if (evento.getSource() == buttonSalir){// SALIR
			  this.frame.dispose();
		  	  }
		  if (evento.getSource() == buttonBus){	// BUSCAR
			  VentanaCategoria v = new VentanaCategoria();
	      	  if (VentanaCategoria.CONTAR_VENTANA_CATEGORIA==1) {
	      			v.frame.toFront();
	      			v.frame.show();
	      			}
			  if (VentanaCategoria.CONTAR_VENTANA_CATEGORIA>1) {
			    	v.frame.toFront();
			    	}
			   VentanaCategoria.textDescripcion.requestFocus(true);
			   VentanaCategoria.cbFamilia.removeAllItems();
			    String familia = (String)cbFamilia.getSelectedItem();
			    VentanaCategoria.cbFamilia.addItem(familia);
			    VentanaCategoria.cbFamilia.setSelectedItem(cbFamilia.getSelectedItem());
			    
			  }
		  	
		  if (evento.getSource() == cbFamilia){
			  llenarcbCategoria();
			  }
		  if (evento.getSource() == cbTipo){
			  mostrarcbTipo();
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
	
		public void insertarUpdate() {
			if (textCod.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCod.requestFocus();
				return;
			}
			if (dateChooserAlta.getDate()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				dateChooserAlta.requestFocus();
				return;
			}
			if (cbFamilia.getSelectedItem()==null || cbFamilia.getSelectedItem()==""){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbFamilia.requestFocus();
				return;
			}
			if (cbTipo.getSelectedItem()==null || cbTipo.getSelectedItem()==""){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbTipo.requestFocus();
				return;
			}
			if (textDescripcion.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDescripcion.requestFocus();
				return;
			}
			/*if (textMarca.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textMarca.requestFocus();
				return;
			}*/
			if (textFormatCosto.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textFormatCosto.requestFocus();
				return;
			}
			if (textFormatPrecio.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textFormatPrecio.requestFocus();
				return;
			}
			if (textFormatStock.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textFormatStock.requestFocus();
				return;
			}
			if (textFormatStockMinimo.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textFormatStockMinimo.requestFocus();
				return;
			}
			if (cbEstado.getSelectedItem()==null || cbEstado.getSelectedItem()==""){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbEstado.requestFocus();
				return;
			}
			conexion = new ConexionDB();
			if (MOD==0) {// REGISTRAR
				try {
					FileInputStream  archivofoto;
					String sql ="INSERT INTO  INVENTARIOS (Id_Inv,Id_Cat,DescripcionInv,MarcaInv,CostoInv,PrecioInv,StockInv,StockMinimoInv,FechaAltaInv,FechaActualizarInv,ObservacionInv,EstadoInv,ImageInv,UrlInv) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText().toString().trim()));
					ps.setString(2, Integer.toString(Id_Cate).toString());
					//ps.setString(3,(String)cbFamilia.getSelectedItem());
					ps.setString(3,textDescripcion.getText().trim());
					ps.setString(4,textMarca.getText().trim());
					ps.setString(5, textFormatCosto.getText().trim());;
					ps.setString(6, textFormatPrecio.getText().trim());
					ps.setString(7, textFormatStock.getText().trim());
					ps.setString(8, textFormatStockMinimo.getText().trim());
					ps.setNString(9, dateAlta.trim());
					ps.setNString(10, dateAlta.trim());
					ps.setString(11,textArea.getText().trim());
					ps.setString(12,(String)cbEstado.getSelectedItem());
					
					ps.setString(14, almacenaFoto);
					// VERIFICA SI SE GUARDARA UNA IMAGEN
					if ((almacenaFoto==null)|| (almacenaFoto=="")){
						almacenaFoto="sin image";
						ps.setString(13,almacenaFoto);
					}else {
						archivofoto = new FileInputStream(almacenaFoto);
						ps.setBinaryStream(13,archivofoto);
					}
					
					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					limpiarTexts();
					llenarNuevo();
					llenarcbCategoria();
					//activarButton(true);
					//frame.dispose();
					cbTipo.requestFocus();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			if (MOD==1) { // MODIFICAR 
				try {
					FileInputStream  archivofoto;
			         String sql="UPDATE INVENTARIOS SET Id_Inv = ?,"
			                 + "Id_Cat =?,"
			                 + "DescripcionInv = ?,"
			                 + "MarcaInv = ?,"
			                 + "CostoInv = ?,"
			                 + "PrecioInv =?,"
			                 + "StockInv = ?,"
			                 + "StockMinimoInv=?,"
			                 + "FechaActualizarInv =?,"
			                 + "ObservacionInv =?,"
			                 + "EstadoInv =?,"
			                 + "ImageInv =?,"
			                 + "UrlInv =?"
			                 + "WHERE Id_Inv = '"+ Integer.parseInt(textCod.getText()) + "'"; 
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText().toString().trim()));
					ps.setString(2, Integer.toString(Id_Cate).toString());
					//ps.setString(3,(String)cbFamilia.getSelectedItem());
					ps.setString(3,textDescripcion.getText().trim());
					ps.setString(4,textMarca.getText().trim());
					ps.setString(5, textFormatCosto.getText().trim());;
					ps.setString(6, textFormatPrecio.getText().trim());
					ps.setString(7, textFormatStock.getText().trim());
					ps.setString(8, textFormatStockMinimo.getText().trim());
					ps.setNString(9, dateAlta.trim() + " " + Menu.HORA_24.trim().substring(0, 8));
					ps.setString(10,textArea.getText().trim());
					ps.setString(11,(String)cbEstado.getSelectedItem());
					// DOY FORMATO A LA FECHA
					//SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
					//dateActualizar = form.format(dateChooserAlta.getDate());
					//ps.setNString(5, (dateActualizar + " " + Menu.HORA).trim());
					
					ps.setString(13, almacenaFoto);
					
					// VERIFICA SI SE GUARDARA UNA IMAGEN
					if ((almacenaFoto==null)|| (almacenaFoto=="")){
						almacenaFoto="sin image";
						ps.setString(12,almacenaFoto);
					}else {
						archivofoto = new FileInputStream(almacenaFoto);
						ps.setBinaryStream(12,archivofoto);
					}
					ps.executeUpdate();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					limpiarTexts();
					activarButton(true);
					frame.dispose();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			
		}
		
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == cbTipo){cbTipo.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbEstado){cbEstado.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textDescripcion){textDescripcion.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textMarca){textMarca.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textFormatPrecio){textFormatPrecio.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFormatCosto){textFormatCosto.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFormatStock){textFormatStock.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFormatStockMinimo){textFormatStockMinimo.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textFormatPrecio){textFormatPrecio.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textFormatCosto){textFormatCosto.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textDescripcion){textDescripcion.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textMarca){textMarca.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbEstado){cbEstado.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textFormatStock){textFormatStock.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textFormatStockMinimo){textFormatStockMinimo.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == cbTipo){cbTipo.setBackground(new Color(240,240,240));}
			if (ev.getSource() == cbEstado){cbEstado.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textDescripcion){textDescripcion.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textMarca){textMarca.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textFormatPrecio){textFormatPrecio.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textFormatCosto){textFormatCosto.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textFormatStock){textFormatStock.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textFormatStockMinimo){textFormatStockMinimo.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textFormatPrecio){textFormatPrecio.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textFormatCosto){textFormatCosto.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textDescripcion){textDescripcion.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textMarca){textMarca.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbEstado){cbEstado.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textFormatStock){textFormatStock.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textFormatStockMinimo){textFormatStockMinimo.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			if (evet.getSource() == cbFamilia){
				if (e==KeyEvent.VK_ENTER){
					if (cbFamilia.getSelectedIndex()!=-1){
						cbTipo.requestFocus();
					}else{
						cbFamilia.requestFocus();
					}
				}	
			}
				if (evet.getSource() == cbTipo){
					if (e==KeyEvent.VK_ENTER){
						if (cbTipo.getSelectedIndex()!=-1){
							textDescripcion.requestFocus();
						}else{
							cbTipo.requestFocus();
						}
					}	
				}
				if (evet.getSource() == textDescripcion){
					int pos = textDescripcion.getCaretPosition();textDescripcion.setText(textDescripcion.getText().toUpperCase());textDescripcion.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					cadenaDescripcion=textDescripcion.getText() ;
					if (textDescripcion.getText().toLowerCase().isEmpty()|| textDescripcion.getText().toLowerCase().length()>85){
						textDescripcion.requestFocus();
						textDescripcion.selectAll();
						textDescripcion.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textDescripcion.getText().toLowerCase().length()==85){
							textFormatCosto.requestFocus();
							textFormatCosto.selectAll();	
						}
				} 
				/*if (evet.getSource() == textMarca){
					int pos = textMarca.getCaretPosition();textMarca.setText(textMarca.getText().toUpperCase());textMarca.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textMarca.getText().toLowerCase().isEmpty()|| textMarca.getText().toLowerCase().length()>55){
						textMarca.requestFocus();
						textMarca.selectAll();
						textMarca.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textMarca.getText().toLowerCase().length()==55){
							textFormatCosto.requestFocus();
							textFormatCosto.selectAll();	
						}
				} */
				if (evet.getSource() == textFormatCosto){
					int pos = textFormatCosto.getCaretPosition();textFormatCosto.setText(textFormatCosto.getText().toUpperCase());textFormatCosto.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textFormatCosto.getText().toLowerCase().isEmpty()|| textFormatCosto.getText().toLowerCase().length()>8){
						textFormatCosto.requestFocus();
						textFormatCosto.selectAll();
						textFormatCosto.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textFormatCosto.getText().toLowerCase().length()==8){
							textFormatPrecio.requestFocus();
							textFormatPrecio.selectAll();	
						}
				} 
				if (evet.getSource() == textFormatPrecio){
					int pos = textFormatPrecio.getCaretPosition();textFormatPrecio.setText(textFormatPrecio.getText().toUpperCase());textFormatPrecio.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textFormatPrecio.getText().toLowerCase().isEmpty()|| textFormatPrecio.getText().toLowerCase().length()>8){
						textFormatPrecio.requestFocus();
						textFormatPrecio.selectAll();
						textFormatPrecio.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textFormatPrecio.getText().toLowerCase().length()==8){
							textFormatStockMinimo.requestFocus();
							textFormatStockMinimo.selectAll();
						}
				} 
				if (evet.getSource() == textFormatStockMinimo){
					int pos = textFormatStockMinimo.getCaretPosition();textFormatStockMinimo.setText(textFormatStockMinimo.getText().toUpperCase());textFormatStockMinimo.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textFormatStockMinimo.getText().toLowerCase().isEmpty()|| textFormatStockMinimo.getText().toLowerCase().length()>5){
						textFormatStockMinimo.requestFocus();
						textFormatStockMinimo.selectAll();
						textFormatStockMinimo.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textFormatStockMinimo.getText().toLowerCase().length()==5){
							textFormatStock.requestFocus();
							textFormatStock.selectAll();
						}
				} 
				if (evet.getSource() == textFormatStock){
					int pos = textFormatStock.getCaretPosition();textFormatStock.setText(textFormatStock.getText().toUpperCase());textFormatStock.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textFormatStock.getText().toLowerCase().isEmpty()|| textFormatStock.getText().toLowerCase().length()>5){
						textFormatStock.requestFocus();
						textFormatStock.selectAll();
						textFormatStock.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textFormatStock.getText().toLowerCase().length()==5){
							buttonGrabar.doClick();
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
				if (evet.getSource() == cbEstado){
					if (e==KeyEvent.VK_ENTER){
						if (cbEstado.getSelectedIndex()!=-1){
							buttonGrabar.doClick();
						}else{
							cbEstado.requestFocus();
						}
					}	
				}
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char car=evet.getKeyChar();
			if (evet.getSource() == textDescripcion){
				if((car<'a' || car>'z') && (car<'A' || car>'Z')&&(car<' '||car>'.')&&(car<'0'||car>'9')) evet.consume();
			}
			if (evet.getSource() == textMarca){
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textFormatCosto){
		       //if(textFormatCosto.getText().length()>=8)evet.consume();
		       if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
			}
			if (evet.getSource() == textFormatPrecio){ 
				if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
			}
			if (evet.getSource() == textFormatStock){
				if(!Character.isDigit(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textFormatStockMinimo){
				if(!Character.isDigit(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
		}
		
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource()==(dateChooserAlta)){
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			dateAlta= form.format(dateChooserAlta.getDate());
		}
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		
	}
	
	}
