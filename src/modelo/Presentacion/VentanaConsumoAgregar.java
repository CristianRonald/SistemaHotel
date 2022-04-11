
package modelo.Presentacion;

import java.awt.Color;
import java.awt.Component;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;

public class VentanaConsumoAgregar implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JDialog frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl7,lbl8;
	protected JLabel			lblAcuenta;
	private JButton  			buttonGrabar,buttonSalir,buttonBus;
	protected static JDateChooser dateChooserEmision;
	protected static String dateEmision;
	protected static JTextField 			textCod;
	protected static JComboBox<String> 		cbTipo,cbCategoria;
	protected static JFormattedTextField 	textFormatPrecio,textFormatCantidad,textFormatImporte,textFormatDsct,textFormatTot;
	
	private JScrollPane scrollArea;
	protected static JTextArea textArea = new JTextArea();
	
	public static int MOD;
	private JSpinner  spinnerE;
	
	private float UTLT=0;
	protected static float COSTO=0;	
	private int COD_HOSPEDAJE=0;
	private int COD_DETALLE=0;
	public VentanaConsumoAgregar(int cOD_HOSPEDAJE,int cOD_DETALLE) {
		super();
		COD_HOSPEDAJE =cOD_HOSPEDAJE;
		COD_DETALLE =cOD_DETALLE;
		frameConsumoAgregar();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		horaSpinner();
		
		llenarNuevo();
		llenarcbTipo();
		mostrarcbTipoConsumo();
		
		dateChooserEmision.setDate(Menu.fecha);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dateEmision = form.format(dateChooserEmision.getDate());
		
	}
	public void frameConsumoAgregar() {
		frame = new JDialog();
		frame.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				if(MOD==0) {
					//cbTipo.requestFocusInWindow();
				}
				if(MOD==1) {
					textFormatCantidad.requestFocus(true);
					textFormatCantidad.selectAll();
				}
			}
			public void windowLostFocus(WindowEvent e) {
			}
		});
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaConsumoAgregar.class.getResource("/modelo/Images/consumo.png")));
		frame.setTitle("Agregar consumo"); 
		frame.setBounds(100, 100, 588, 237);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setModal(true);
	}
	public void llenarParaModificar() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet =statement.executeQuery("Select * from DETALLE_A_CONSUMO where Id_C='" + Integer.parseInt(textCod.getText().trim()) + "'");
			if (resultSet.next()==true){ 
				cbCategoria.setSelectedItem(resultSet.getString("TipoConsumoC"));
				dateEmision=(resultSet.getString("FechaC").toString());
				Date date = Menu.formatoFecha.parse(dateEmision);
				dateChooserEmision.setDate(date);
				cbTipo.setSelectedItem((String)resultSet.getString("DescripcionC"));
				textFormatPrecio.setText((String)Menu.formateadorCurrency.format(resultSet.getFloat("PrecioC")));
				textFormatCantidad.setText(resultSet.getString("CantidadC"));
				textFormatImporte.setText((String)Menu.formateadorCurrency.format(resultSet.getFloat("ImporteC")));
				textFormatDsct.setText((String)Menu.formateadorCurrency.format(resultSet.getFloat("DsctC")));
				textFormatTot.setText((String)Menu.formateadorCurrency.format(resultSet.getFloat("TotalC")));
				cbCategoria.setEnabled(false);
				cbTipo.setEnabled(false);
				textFormatCantidad.requestFocus(true);
				textFormatCantidad.selectAll();
				panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modificar Datos ", TitledBorder.LEADING, TitledBorder.TOP, null,(SystemColor.RED)));
				//spinnerE.setValue(formatTime.parseObject(resultSet.getString("HoraIngresoH"))); // e.g. input 16:45
			}else{
				
			}
				
			resultSet.close();
			statement.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 557, 182);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);

		dateChooserEmision = new JDateChooser();
		dateChooserEmision.setDateFormatString("dd-MMM-yyyy");
		dateChooserEmision.setBorder(new LineBorder(new Color(0, 206, 209), 1, true));
		dateChooserEmision.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)dateChooserEmision.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)dateChooserEmision.getDateEditor()).setEditable(false);
		dateChooserEmision.setBounds(124, 49, 95, 20);
		panelDto.add(dateChooserEmision);
		dateChooserEmision.addPropertyChangeListener(this);
		dateChooserEmision.setDate(Menu.fecha);
	}
	void horaSpinner() {
		 SpinnerDateModel model = new SpinnerDateModel();
		spinnerE= new JSpinner();
		spinnerE.setModel(model);
		spinnerE.setEditor(new JSpinner.DateEditor(spinnerE, "hh:mm:ss a"));
		spinnerE.setBounds(225, 49, 91, 20);
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
	}	
	public void llenarNuevo() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_C from DETALLE_A_CONSUMO order by Id_C desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_C"))+1);
				textCod.setText(Menu.formatid_7.format(id));
			}else {
				textCod.setText(Menu.formatid_7.format(0000001));
			}
			resultSet.close();statement.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al load" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		conexion.DesconectarDB();
	}
	public void crearLabels() {
		lbl1= new JLabel("Nro. de comanda");
		lbl1.setBounds(21, 25, 95, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Vitrina / Servicio:");
		lbl2.setBounds(10, 76, 106, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Precio S/:");
		lbl3.setBounds(124, 135, 65, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Cantidad:");
		lbl4.setBounds(192, 135, 57, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);

		lbl5= new JLabel("Importe S/:");
		lbl5.setBounds(252, 135, 67, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);

		lbl7= new JLabel("Fecha de emisión:");
		lbl7.setBounds(20, 52, 98, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Observación:");
		lbl8.setBounds(20, 104, 94, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		
		JLabel lblDscto = new JLabel("Dscto S/:");
		lblDscto.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDscto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblDscto.setBounds(322, 135, 57, 14);
		panelDto.add(lblDscto);
		
		JLabel lblTot = new JLabel("Total S/:");
		lblTot.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTot.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTot.setBounds(384, 135, 65, 14);
		panelDto.add(lblTot);
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
		textCod.setBounds(124, 22, 75, 22);
		panelDto.add(textCod);
		
		scrollArea= new JScrollPane();
		scrollArea.setBounds(124, 98, 409, 35);
		panelDto.add(scrollArea);
		textArea = new JTextArea();
		textArea.setBackground(new Color(240, 240, 240));
		textArea.setEditable(false);
		scrollArea.setViewportView(textArea);
		textArea.setFont(Menu.fontText);
		textArea.setForeground(Menu.textColorForegroundInactivo);
		textArea.setWrapStyleWord(true);
		//textArea.addFocusListener(this);
		textArea.addKeyListener(this);

		textFormatPrecio = new JFormattedTextField();
		textFormatPrecio.setEditable(false);
		textFormatPrecio.setText("0.00");
		textFormatPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatPrecio.setBounds(124, 148, 65, 21);
		textFormatPrecio.addPropertyChangeListener(this);
		textFormatPrecio.addFocusListener(this);
		textFormatPrecio.addActionListener(this);
		textFormatPrecio.addKeyListener(this);
		panelDto.add(textFormatPrecio);
		
		textFormatCantidad = new JFormattedTextField();
		textFormatCantidad.setBackground(new Color(255, 255, 255));
		textFormatCantidad.setText("0");
		textFormatCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatCantidad.setBounds(192, 148, 57, 21);
		textFormatCantidad.addPropertyChangeListener(this);
		textFormatCantidad.addFocusListener(this);
		textFormatCantidad.addActionListener(this);
		textFormatCantidad.addKeyListener(this);
		panelDto.add(textFormatCantidad);
		
		textFormatImporte = new JFormattedTextField();
		textFormatImporte.setEditable(false);
		textFormatImporte.setText("0.00");
		textFormatImporte.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatImporte.setBounds(252, 148, 67, 21);
		textFormatImporte.addFocusListener(this);
		panelDto.add(textFormatImporte);
		
		textFormatDsct = new JFormattedTextField();
		textFormatDsct.setText("0.00");
		textFormatDsct.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatDsct.setBounds(322, 148, 57, 21);
		textFormatDsct.addActionListener(this);
		textFormatDsct.addPropertyChangeListener(this);
		textFormatDsct.addKeyListener(this);
		textFormatDsct.addFocusListener(this);
		panelDto.add(textFormatDsct);
		
		textFormatTot = new JFormattedTextField();
		textFormatTot.setEditable(false);
		textFormatTot.setText("0.00");
		textFormatTot.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatTot.setBounds(382, 148, 67, 21);
		textFormatTot.addActionListener(this);
		textFormatTot.addPropertyChangeListener(this);
		textFormatTot.addKeyListener(this);
		textFormatTot.addFocusListener(this);
		panelDto.add(textFormatTot);
	}
	public void crearComboBox() { 
		cbTipo = new JComboBox<>();
		cbTipo.setBounds(225, 74, 270, 21);
		cbTipo.setFont(Menu.fontText);
		cbTipo.addActionListener(this);
		cbTipo.addFocusListener(this);
		cbTipo.addKeyListener(this);
		panelDto.add(cbTipo);
        
		cbCategoria = new JComboBox<>();
		cbCategoria.setBounds(124, 74, 95, 21);
        cbCategoria.setFont(Menu.fontText);
        cbCategoria.addActionListener(this);
        cbCategoria.addFocusListener(this);
        cbCategoria.addKeyListener(this);
        panelDto.add(cbCategoria);
	}
	public static void llenarcbTipo() { 
		conexion = new ConexionDB();
		cbTipo.removeAllItems();
		textFormatPrecio.setText("0.00");textFormatCantidad.setText("0");textFormatImporte.setText("0.00");textFormatDsct.setText("0.00");
		textFormatTot.setText("0.00");textArea.setText("");
		try {
			if (cbCategoria.getSelectedItem().equals("%TODOS")||cbCategoria.getSelectedItem().equals("SERVICIO")) {
				String con = "Select * from SERVICIOS";
				Statement st = conexion.gConnection().createStatement();
				ResultSet rs = st.executeQuery(con);
				while (rs.next()==true) {
					cbTipo.addItem(rs.getString("DescripcionSer"));
				}
				cbTipo.setSelectedIndex(-1);
				rs.close();
				st.close();
			}
			if (cbCategoria.getSelectedItem().equals("%TODOS")|| cbCategoria.getSelectedItem().equals("VITRINA")) {
				String c = "Select * from INVENTARIOS AS I,CATEGORIA AS C where I.Id_Cat=C.Id_Cat and  FamiliaCat ='" + "VITRINA" + "'";
				Statement s = conexion.gConnection().createStatement();
				ResultSet r = s.executeQuery(c);
				while (r.next()==true) {
					cbTipo.addItem(r.getString("DescripcionInv").trim()); 
				}
				cbTipo.setSelectedIndex(-1);
				r.close();
				s.close();
			}
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	public void mostrarcbTipo() { 
		conexion = new ConexionDB();
		textFormatPrecio.setText("0.00");textFormatCantidad.setText("0");textFormatImporte.setText("0.00");textFormatDsct.setText("0.00");
		textFormatTot.setText("0.00");textArea.setText("");
		try {
			// SERVICIOS
			String con = "Select * from SERVICIOS where DescripcionSer ='" + cbTipo.getSelectedItem().toString().trim() + "'";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			if (rs.next()==true) {
				textFormatPrecio.setText(Menu.formateadorCurrency.format(rs.getFloat("PrecioSer")));
				textArea.setText(rs.getString("ObservacionSer"));
				  st.close();
				  return;
			}
			rs.close();st.close();
			
			// VITRINA
			String c = "Select * from INVENTARIOS where DescripcionInv ='" + cbTipo.getSelectedItem().toString().trim() + "'";
			Statement s = conexion.gConnection().createStatement();
			ResultSet r = s.executeQuery(c);
			if (r.next()==true) {
				textFormatPrecio.setText(Menu.formateadorCurrency.format(r.getFloat("PrecioInv")));
				textArea.setText(r.getString("ObservacionInv"));
				COSTO=r.getFloat("CostoInv");
			}
			r.close();s.close();
	
		} catch (Exception e) {}
	}
	public void mostrarcbTipoConsumo() { 
		String [] lista1 = {"VITRINA","SERVICIO"};
		for (String llenar:lista1) {
			cbCategoria.addItem(llenar);
		}
	}
	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(459, 146, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaConsumoAgregar.class.getResource("/modelo/Images/check.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Regresar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(497, 146, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaConsumoAgregar.class.getResource("/modelo/Images/undo.png")));
		panelDto.add(buttonSalir);
		
		buttonBus= new JButton("");
		buttonBus.setEnabled(false);
		buttonBus.setBounds(497, 73, 36, 22);
		buttonBus.setToolTipText("Buscar");
		buttonBus.addActionListener(this);
		buttonBus.setIcon(new ImageIcon(VentanaConsumoAgregar.class.getResource("/modelo/Images/search.png")));
		panelDto.add(buttonBus);
		buttonBus.setVisible(false);
	}
	public void limpiarTexts() {
		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.requestFocus(true);
		
		textFormatPrecio.setText(null);
		textFormatPrecio.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatPrecio.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatCantidad.setText(null);
		textFormatCantidad.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatCantidad.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatImporte.setText(null);
		textFormatImporte.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatImporte.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatDsct.setText(null);
		textFormatDsct.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatDsct.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
		
		cbTipo.setSelectedIndex(-1);
				
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textFormatPrecio.setEnabled(b);
		textFormatCantidad.setEnabled(b);
		textFormatImporte.setEnabled(b);
		textFormatDsct.setEnabled(b);
		textArea.setEnabled(b);
		cbTipo.setEnabled(b);
	}
	public void activarButton(boolean c) {
		 if (c == true){
			buttonGrabar.setEnabled(false);
			buttonSalir.setEnabled(true);
			buttonBus.setEnabled(true);
			panelDto.setVisible(true);
		 }
		 if (c == false){
			buttonGrabar.setEnabled(true);
			buttonSalir.setEnabled(false);
			buttonBus.setEnabled(false);
			panelDto.setVisible(true);
		 }
	}
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonGrabar){// GRABAR
			  insertarUpdate();
		  }	  
		  if (evento.getSource() == buttonSalir){// SALIR
			  frame.dispose();
			  }
		  if (evento.getSource() == buttonBus){	// BUSCAR
			  	  if (cbCategoria.getSelectedItem().equals("VITRINA")) {
					 VentanaInventarios a = new VentanaInventarios();
					 VentanaInventarios.frame.show();
					 VentanaInventarios.FILTRO_INV = 1;
					 VentanaInventarios.frame.setTitle("Search productos:");
					 a.textBus.requestFocus(true);
					 a.buttonNuevo.setEnabled(false);
					 a.buttonEditar.setEnabled(false);
					 a.buttonEliminar.setEnabled(false);
					 
					 a.cbFamilia.removeAllItems();
		      		 a.cbFamilia.addItem("VITRINA");
					}
			  	if (cbCategoria.getSelectedItem().equals("SERVICIO")) {
					 VentanaServicio v = new VentanaServicio();
					 v.frame.show();
					 VentanaServicio.FILTRO_SER = 1;
					 v.frame.setTitle("Search servicios:");
					 v.textBus.requestFocus(true);
					 v.buttonNuevo.setEnabled(false);
					 v.buttonEditar.setEnabled(false);
					 v.buttonEliminar.setEnabled(false);
					}
			 }
		  if (evento.getSource().equals(cbTipo)) {
			  mostrarcbTipo();
		  	  }
		  if (evento.getSource().equals(cbCategoria)) {
				llenarcbTipo();
				buttonBus.setEnabled(true);
				if (cbCategoria.getSelectedItem().equals("%TODOS")) {
				  buttonBus.setEnabled(false);
			  	}
		  	  }
	}
	void sumar() {
		float PRE=0,IMP=0,DSCT=0,TOT=0;
		int CANT=0;
		try {
			if (textFormatPrecio.getText().isEmpty()) {
				textFormatPrecio.setText("0.00");
				textFormatPrecio.selectAll();
			}
			if (textFormatCantidad.getText().isEmpty()) {
				textFormatCantidad.setText("0");
				textFormatCantidad.selectAll();
			}
			if (textFormatImporte.getText().isEmpty()) {
				textFormatImporte.setText("0.00");
				textFormatImporte.selectAll();
			}
			if (textFormatDsct.getText().isEmpty()) {
				textFormatDsct.setText("0.00");
				textFormatDsct.selectAll();
			}
			if (textFormatTot.getText().isEmpty()) {
				textFormatTot.setText("0.00");
				textFormatTot.selectAll();
			}

			PRE=Float.parseFloat(textFormatPrecio.getText().replaceAll(",", ""));
			CANT=Integer.parseInt(textFormatCantidad.getText());
			DSCT=Float.parseFloat(textFormatDsct.getText().replaceAll(",", ""));
			if (PRE>0) {
				IMP= (PRE * CANT);
				TOT= (IMP - DSCT);
				textFormatImporte.setText(Menu.formateadorCurrency.format(IMP));
				textFormatTot.setText(Menu.formateadorCurrency.format(TOT));
			}
	
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error de usuario" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textFormatDsct.selectAll();
			textFormatDsct.requestFocus();
		}
	}
	// DESCUENTO EL STOCK DEL ARTICULO
	public static void actualizarStock(){
		conexion = new ConexionDB();
		try{
			Statement s = conexion.gConnection().createStatement();
			ResultSet r = s.executeQuery("Select * from INVENTARIOS where DescripcionInv= '"+ cbTipo.getSelectedItem() +"'"); 
			int CAT=0;
			if (r.next()==true) {
				CAT=(r.getInt("StockInv"));
				if (MOD==1) {CAT= CAT + VentanaConsumo.cantidad;}
			         String sq="UPDATE INVENTARIOS SET StockInv = ?"
			                 + "WHERE DescripcionInv = '"+ cbTipo.getSelectedItem() +"'"; 
					PreparedStatement pst = conexion.gConnection().prepareStatement(sq);
					if (MOD==0) {
						pst.setDouble(1, CAT - Double.parseDouble(textFormatCantidad.getText()));
					}
					if (MOD==1) {
						pst.setDouble(1, CAT - Double.parseDouble(textFormatCantidad.getText()));
					}
					pst.executeUpdate();
					pst.close();
					//JOptionPane.showMessageDialog(null, "El stock del artículo " + VentanaConsumo.detalle.trim()  + " fue actualizado",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
			r.close();s.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error actualizar stock" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	// DESCUENTO EL STOCK DEL ARTICULO
	
	public void insertarUpdate() {
		if (textCod.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textCod.requestFocus();
			return;
		}
		if (dateChooserEmision.getDate()==null){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			dateChooserEmision.requestFocus();
			return;
		}
		if (cbTipo.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbTipo.requestFocus();
			return;
		}
		if (textFormatPrecio.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textFormatPrecio.requestFocus();
			return;
		}
		if (textFormatCantidad.getText().trim().isEmpty()||Integer.parseInt(textFormatCantidad.getText())==0){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textFormatCantidad.requestFocus();
			textFormatCantidad.selectAll();
			return;
		}
		if (textFormatImporte.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textFormatImporte.requestFocus();
			return;
		}
		if (textFormatDsct.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textFormatDsct.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try{
			Statement s = conexion.gConnection().createStatement();
			ResultSet r = s.executeQuery("Select * from INVENTARIOS where DescripcionInv= '"+ cbTipo.getSelectedItem() +"'"); 
			int CAT=0;
			if (r.next()==true) {
				CAT=(r.getInt("StockInv"));
				if (MOD==1) {CAT= CAT + VentanaConsumo.cantidad;}
				if (Integer.parseInt(textFormatCantidad.getText()) > CAT){
					JOptionPane.showMessageDialog(null, "La cantidad ingresada supera "+ Menu.separador +"a las " +  CAT  + " unidades existentes en stock " ,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textFormatCantidad.requestFocus();
					return;
				}
			}
			r.close();s.close();
		} catch (Exception e) {}
		
    	UTLT=0;
        if (cbCategoria.getSelectedItem().equals("ARTICULO")||cbCategoria.getSelectedItem().equals("VITRINA")) {
            UTLT =  (Float.parseFloat(textFormatPrecio.getText().trim().replaceAll(",", "")) - COSTO) * (Float.parseFloat(textFormatCantidad.getText()));
        }
        if (cbCategoria.getSelectedItem().equals("SERVICIO")) {
            UTLT =  Float.parseFloat(textFormatPrecio.getText().trim().replaceAll(",", "")) * Float.parseFloat(textFormatCantidad.getText());
        }
        
		if (MOD==0) {// REGISTRAR
			try {
				actualizarStock();
				String sql ="INSERT INTO  DETALLE_A_CONSUMO (Id_C,Id_D,DescripcionC,FechaC,HoraC,PrecioC,CantidadC,ImporteC,DsctC,TotalC,UtlC,TipoConsumoC,IdTurnoC) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setString(1, textCod.getText().trim());
				ps.setString(2, Integer.toString(this.COD_DETALLE));
				ps.setString(3, cbTipo.getSelectedItem().toString());
				ps.setNString(4, (dateEmision.trim()));
				ps.setNString(5, Menu.HORA.trim());//12 HORAS HILO
				ps.setFloat(6, Float.parseFloat(textFormatPrecio.getText().replaceAll(",", "")));
				ps.setString(7, textFormatCantidad.getText().trim());;
				ps.setFloat(8, Float.parseFloat(textFormatImporte.getText().replaceAll(",", "")));
				ps.setFloat(9, Float.parseFloat(textFormatDsct.getText().replaceAll(",", "")));
				ps.setFloat(10, Float.parseFloat(textFormatTot.getText().replaceAll(",", "")));
				ps.setFloat(11, (UTLT - Float.parseFloat(textFormatDsct.getText().trim().replaceAll(",", ""))));
				ps.setString(12, cbCategoria.getSelectedItem().toString());
				ps.setInt(13, VentanaLogin.ID_APETURA_CAJA);  // ID TURNO
				ps.execute();
				ps.close();
				JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				insertCuentaHuesped();//CUENTA HUESPED CONSUMO
				
				limpiarTexts();
				llenarNuevo();
				cbTipo.requestFocus();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
		}
		if (MOD==1) { // MODIFICAR 
			try {
				actualizarStock();
		         String sql="UPDATE DETALLE_A_CONSUMO SET Id_C = ?,"
		                 + "Id_D =?,"
		                 + "DescripcionC = ?,"
		                 + "FechaC = ?,"
		                 + "HoraC = ?,"
		                 + "PrecioC =?,"
		                 + "CantidadC = ?,"
		                 + "ImporteC=?,"
		                 + "DsctC =?,"
		                 + "TotalC =?,"
		                 + "UtlC =?,"
		                 + "TipoConsumoC =?"
		                 + "WHERE Id_C = '"+Integer.parseInt(textCod.getText())+"'"; 
				
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setString(1, textCod.getText().trim());
				ps.setString(2, Integer.toString(this.COD_DETALLE));
				ps.setString(3, cbTipo.getSelectedItem().toString());
				ps.setNString(4, (dateEmision.trim()));
				ps.setNString(5, Menu.HORA.trim());//12 HORAS HILO
				ps.setFloat(6, Float.parseFloat(textFormatPrecio.getText().replaceAll(",", "")));
				ps.setString(7, textFormatCantidad.getText().trim());;
				ps.setFloat(8, Float.parseFloat(textFormatImporte.getText().replaceAll(",", "")));
				ps.setFloat(9, Float.parseFloat(textFormatDsct.getText().replaceAll(",", "")));
				ps.setFloat(10, Float.parseFloat(textFormatTot.getText().replaceAll(",", "")));
				ps.setFloat(11, (UTLT - Float.parseFloat(textFormatDsct.getText().trim().replaceAll(",", ""))));
				ps.setString(12, cbCategoria.getSelectedItem().toString());
				ps.executeUpdate();
				ps.close();
				JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				insertCuentaHuesped();//CUENTA HUESPED CONSUMO
				
				limpiarTexts();
				activarButton(true);
				frame.dispose();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
		}
		
	}
	protected void insertCuentaHuesped() {
		int COD_CUENTA_HUESPED=0;
		conexion = new ConexionDB();
		try {
	     	//***********************************************************************CONSULTO CONSUMOS
 	 		String consultarConsumo="Select * from ALQUILER as A, DETALLE_A_HABITACION as DH, DETALLE_A_CONSUMO as C where C.Id_D=DH.Id_D and DH.Id_A=A.Id_A and A.Id_A  ='" + this.COD_HOSPEDAJE  +"'";
 	 		Statement stc= conexion.gConnection().createStatement();
 	 		ResultSet rsc=stc.executeQuery(consultarConsumo);
 	 		float MONTO_CONSUMO=0;
 	 		while(rsc.next()) {
 	 			MONTO_CONSUMO=MONTO_CONSUMO + rsc.getFloat("ImporteC");
 	 			MONTO_CONSUMO=MONTO_CONSUMO-rsc.getFloat("DsctC");
 	 		}
 	 		rsc.close();stc.close(); 
 	 		//***********************************************************************CONSULTO CONSUMOS
			
			Statement stcon = conexion.gConnection().createStatement();
			ResultSet resultS =  stcon.executeQuery("Select * from CUENTA_HUESPED where Id_A ='" + this.COD_HOSPEDAJE+ "'and DescripcionCta ='" +"CONSUMO"+ "'");
			JSpinner.DateEditor modele = new JSpinner.DateEditor(spinnerE, "hh:mm:ss a");
			if (resultS.next()==true) { //ACTUALIZO
				try {
					
					//*********************************************************************** FILTRO LOS IMPORTES
					Statement stPago = conexion.gConnection().createStatement();
					ResultSet resultPago =  stPago.executeQuery("Select * from CUENTA_HUESPED_PAGOS where Id_Cta ='" + resultS.getString("Id_Cta") + "'");
					float IMPORTES=0;
					while (resultPago.next()==true) {
						IMPORTES=IMPORTES+resultPago.getFloat("AcuentaDetCta");
					}
					resultPago.close();stPago.close();
					//*********************************************************************** FILTRO LOS IMPORTES
					
					COD_CUENTA_HUESPED= Integer.parseInt(resultS.getString("Id_Cta"));
					String sql="UPDATE CUENTA_HUESPED SET Id_A =?,"
			                 + "FechaCta =?,"
			                 + "HoraCta =?,"
			                 + "DescripcionCta =?,"
			                 + "MontoCta =?,"
			                 + "SaldoCta =?,"
			                 + "EstadoCta = ?"
			                 + "WHERE Id_A='"+this.COD_HOSPEDAJE+"'and DescripcionCta ='" +"CONSUMO"+ "'"; 
			         
							PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
							ps.setInt(1, this.COD_HOSPEDAJE);
							ps.setString(2,  (dateEmision.trim()));
							ps.setString(3, modele.getFormat().format(spinnerE.getValue()));
							ps.setString(4, "CONSUMO");
							ps.setFloat(5, MONTO_CONSUMO);
							ps.setFloat(6, MONTO_CONSUMO-IMPORTES);
							ps.setString(7, "A");
							ps.executeUpdate();
							ps.close();
							
							// *************** SI EL SALDO ES CERO GRABO EL ESTADO EN X (CANCELADO)
							Statement con = conexion.gConnection().createStatement();
							ResultSet res =  con.executeQuery("Select * from CUENTA_HUESPED where Id_A ='" +this.COD_HOSPEDAJE+ "'and DescripcionCta ='" +"CONSUMO"+ "'and SaldoCta ='" + 0 + "'");
							if (res.next()==true) { 
								String s="UPDATE CUENTA_HUESPED SET EstadoCta =?"
				                 + "WHERE Id_Cta='"+res.getString("Id_Cta")+"'"; 
				         
								PreparedStatement pss = conexion.gConnection().prepareStatement(s);
								pss.setString(1, "X");
								pss.executeUpdate();
								pss.close();
							}
							res.close();con.close();
							// *************** SI EL SALDO ES CERO GRABO EL ESTADO EN X (CANCELADO)
							//JOptionPane.showMessageDialog(null, "Cuenta de huesped actualizada  " + Menu.formatid_9.format(COD_CUENTA_HUESPED) + Menu.separador  ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Error al actualizar cuenta huesped" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						}
				resultS.close();stcon.close();
			}else{ // REGISTRO POR 1 VES EL CONSUMO 
				try {
						Statement statement = conexion.gConnection().createStatement();
						ResultSet resultSet = statement.executeQuery("Select Id_Cta from CUENTA_HUESPED order by Id_Cta desc limit 0,1");
						if (resultSet.next()== true) {
							COD_CUENTA_HUESPED=(Integer.parseInt(resultSet.getString("Id_Cta"))+1);
						}else {
							COD_CUENTA_HUESPED=1;
						}
						resultSet.close();statement.close();
	
						PreparedStatement ps = conexion.gConnection().prepareStatement("INSERT INTO CUENTA_HUESPED(Id_Cta,Id_A,FechaCta,HoraCta,DescripcionCta,MontoCta,SaldoCta,EstadoCta,IdTurnoCta) VALUES (?,?,?,?,?,?,?,?,?)");
						ps.setInt(1, COD_CUENTA_HUESPED);
						ps.setInt(2, this.COD_HOSPEDAJE);
						ps.setString(3, (dateEmision.trim()));
						ps.setString(4, modele.getFormat().format(spinnerE.getValue()));
						ps.setString(5, "CONSUMO");
						ps.setFloat(6, MONTO_CONSUMO);
						ps.setFloat(7, MONTO_CONSUMO);
						ps.setString(8, "A");
						ps.setInt(9,VentanaLogin.ID_APETURA_CAJA);
						ps.execute();
						ps.close();
						JOptionPane.showMessageDialog(null, "El consumo se ha ingresado a su cuenta #: " + Menu.formatid_9.format(this.COD_HOSPEDAJE) + Menu.separador  ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al crear cuenta huesped" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch (Exception e) {}
	}
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == cbTipo){cbTipo.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFormatCantidad){textFormatCantidad.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFormatDsct){textFormatDsct.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textFormatCantidad){textFormatCantidad.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textFormatDsct){textFormatDsct.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == cbTipo){cbTipo.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textFormatCantidad){textFormatCantidad.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textFormatDsct){textFormatDsct.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textFormatCantidad){textFormatCantidad.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textFormatDsct){textFormatDsct.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
		}
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
				if (evet.getSource() == cbTipo){
					if (e==KeyEvent.VK_ENTER){
						if (cbTipo.getSelectedIndex()!=-1){
							textFormatCantidad.requestFocus();
							textFormatCantidad.selectAll();	
						}else{
							cbTipo.requestFocus();
						}
					}
				} 
				if (evet.getSource().equals(textFormatPrecio)){
					if (textFormatPrecio.getText().toLowerCase().isEmpty()|| textFormatPrecio.getText().toLowerCase().length()>7){
						textFormatPrecio.requestFocus();
						textFormatPrecio.selectAll();
						textFormatPrecio.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textFormatPrecio.getText().toLowerCase().length()==7){
							textFormatCantidad.requestFocus();
							textFormatCantidad.selectAll();	
						}
				}
				if (evet.getSource().equals(textFormatCantidad)){
					sumar();
					if (textFormatCantidad.getText().toLowerCase().isEmpty()|| textFormatCantidad.getText().toLowerCase().length()>4){
						textFormatCantidad.requestFocus();
						textFormatCantidad.selectAll();
						textFormatCantidad.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textFormatCantidad.getText().toLowerCase().length()==4){
							textFormatDsct.requestFocus();
							textFormatDsct.selectAll();	
						}
				}
				if (evet.getSource().equals(textFormatDsct)){
					sumar();
					if (textFormatDsct.getText().toLowerCase().isEmpty()|| textFormatDsct.getText().toLowerCase().length()>5){
						textFormatDsct.requestFocus();
						textFormatDsct.selectAll();
						textFormatDsct.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER){
							buttonGrabar.doClick();
						}
				}
		}
		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char e=evet.getKeyChar();
			
			if (evet.getSource().equals(textFormatPrecio)|| evet.getSource().equals(textFormatDsct)) {
				if ((e<'0'||e>'9')&&(e<'.'||e>'.'))evet.consume();
			}
			if (evet.getSource().equals(textFormatCantidad)) {
				if ((e<'0'||e>'9'))evet.consume();
			}
			if (evet.getSource() == textArea){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
		}
	@Override
	public void propertyChange(PropertyChangeEvent e) {
      	if (e.getSource()==(dateChooserEmision)){
    		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
    		dateEmision = form.format(dateChooserEmision.getDate());
		}
		//Object source = e.getSource();
	}
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		
	}
	}
