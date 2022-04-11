
package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;

public class VentanaGastosAgregar implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JInternalFrame frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl7,lbl8,lbl9;
	protected JLabel			lblAcuenta;
	private JButton  			buttonGrabar,buttonSalir;
	protected static JDateChooser dateChooserEmision;
	protected static String dateEmision;
	protected static JTextField 			textCod;
	protected static JComboBox<String> 		cbDocumento,cbDoTipoGasto;
	protected static JFormattedTextField 	textNumero,textMonto;
	
	private JScrollPane scrollArea;
	protected static JTextArea textArea = new JTextArea();
	
	public static int MOD;
	
	NumberFormat formatDsct;
	// constructor
	public VentanaGastosAgregar() {
		frameGastosAgregar();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		
		llenarNuevo();
		llenarcbDoc();
		
		dateChooserEmision.setDate(Menu.fecha);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dateEmision = form.format(dateChooserEmision.getDate());
		
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    Menu.Desktop.add(frame);
	}
	public void frameGastosAgregar() {
		frame = new JInternalFrame();
		frame.setTitle("Agregar gastos de la empresa");
		frame.setFrameIcon(new ImageIcon(VentanaGastosAgregar.class.getResource("/modelo/Images/cajasalida.png")));
		
		frame.setClosable(true);
		frame.setBounds(100, 100, 580, 269);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 544, 213);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);

		dateChooserEmision = new JDateChooser();
		dateChooserEmision.setDateFormatString("dd-MMM-yyyy");
		dateChooserEmision.setBorder(new LineBorder(new Color(0, 191, 255), 1, true));
		dateChooserEmision.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)dateChooserEmision.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)dateChooserEmision.getDateEditor()).setEditable(false);
		dateChooserEmision.setBounds(101, 46, 95, 23);
		dateChooserEmision.addPropertyChangeListener(this);
		panelDto.add(dateChooserEmision);
	}
	public void llenarNuevo() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_Gst from GASTOS order by Id_Gst desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_Gst"))+1);
				textCod.setText(Menu.formatid_9.format(id));
			}else {
				textCod.setText(Menu.formatid_9.format(0000001));
			}
			statement.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al load" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void llenarParaModificar() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select* from GASTOS where Id_Gst ='" + VentanaGastos.id + "'");
			if (resultSet.next()== true) {
				textCod.setText(Menu.formatid_9.format(resultSet.getInt("Id_Gst")));
				cbDoTipoGasto.setSelectedItem((String)resultSet.getString("TipoGst"));
				textArea.setText(resultSet.getString("ConceptoGst"));
				cbDocumento.setSelectedItem((String)resultSet.getString("DocumentoGst"));
				textNumero.setText(resultSet.getString("NumeroGst"));
				textMonto.setText(Menu.formateadorCurrency.format(resultSet.getFloat("MontoGst")));
				
				lbl3.setText(resultSet.getString("HoraGst").toString());
				dateEmision=(resultSet.getString("FechaGst").toString());
				Date date = Menu.formatoFecha.parse(dateEmision);
				dateChooserEmision.setDate(date);
			}
			statement.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al load" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void crearLabels() {
		lbl1= new JLabel("Código");
		lbl1.setBounds(10, 24, 83, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Documento:");
		lbl2.setBounds(10, 184, 83, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("");
		lbl3.setBounds(202, 53, 65, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Número:");
		lbl4.setBounds(309, 168, 57, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);

		lbl7= new JLabel("Fecha de emisión:");
		lbl7.setBounds(10, 51, 88, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Descripción:");
		lbl8.setBounds(20, 101, 72, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		lbl9= new JLabel("Tipo gasto.");
		lbl9.setBounds(18, 73, 75, 14);
		panelDto.add(lbl9);
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(Menu.fontLabel);
		
		JLabel lblTot = new JLabel("Monto:");
		lblTot.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTot.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTot.setBounds(377, 168, 57, 14);
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
		textCod.setBounds(101, 21, 95, 22);
		panelDto.add(textCod);
		
		scrollArea= new JScrollPane();
		scrollArea.setBounds(102, 98, 416, 65);
		panelDto.add(scrollArea);
		textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		scrollArea.setViewportView(textArea);
		textArea.setFont(Menu.fontText);
		textArea.setForeground(Menu.textColorForegroundInactivo);
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);
		
		textNumero = new JFormattedTextField();
		textNumero.setBackground(new Color(255, 255, 255));
		textNumero.setText("0");
		textNumero.setHorizontalAlignment(SwingConstants.RIGHT);
		textNumero.setBounds(247, 181, 119, 21);
		textNumero.addPropertyChangeListener(this);
		textNumero.addFocusListener(this);
		textNumero.addActionListener(this);
		textNumero.addKeyListener(this);
		panelDto.add(textNumero);
		
		textMonto = new JFormattedTextField();
		textMonto.setText("0.00");
		textMonto.setHorizontalAlignment(SwingConstants.RIGHT);
		textMonto.setBounds(367, 181, 67, 21);
		textMonto.addActionListener(this);
		textMonto.addPropertyChangeListener(this);
		textMonto.addKeyListener(this);
		textMonto.addFocusListener(this);
		panelDto.add(textMonto);

	}
	
	public void crearComboBox() {
		cbDoTipoGasto = new JComboBox<>();
		cbDoTipoGasto.setEditable(true);
		cbDoTipoGasto.setBounds(101, 73, 333, 21);
		cbDoTipoGasto.setFont(Menu.fontText);
		cbDoTipoGasto.addActionListener(this);
		cbDoTipoGasto.addFocusListener(this);
		cbDoTipoGasto.addKeyListener(this);
        panelDto.add(cbDoTipoGasto);
        
		cbDocumento = new JComboBox<>();
		cbDocumento.setBounds(101, 181, 141, 21);
        cbDocumento.setFont(Menu.fontText);
        cbDocumento.addActionListener(this);
        cbDocumento.addFocusListener(this);
        cbDocumento.addKeyListener(this);
        panelDto.add(cbDocumento);
	}
	public static void llenarcbDoc() { 
		cbDocumento.removeAllItems();textNumero.setText("0");
		textMonto.setText("0.00");textArea.setText("");
		try {
			String con = "Select * from DOCUMENTO";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			while (rs.next()==true) {
				cbDocumento.addItem(rs.getString("TipoDoc"));
			}
			st.close();
			cbDocumento.addItem("GUIA REMISION");
			cbDocumento.addItem("RECIBO");
			cbDocumento.addItem("OTRO");
			cbDocumento.setSelectedIndex(-1);
		} catch (Exception e) {}
		
		
		// LLENAR TIPO GASTOS
		cbDoTipoGasto.removeAllItems();
		String [] lista2 = {"PAGOS A PERSONAL","GASTOS DIVERSOS"};
	    Arrays.sort (lista2);
		for (String llenar:lista2) {
			cbDoTipoGasto.addItem(llenar);
		}
		cbDoTipoGasto.setSelectedItem("GASTOS DIVERSOS");
	}
	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(444, 179, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaGastosAgregar.class.getResource("/modelo/Images/check.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Regresar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(482, 179, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaGastosAgregar.class.getResource("/modelo/Images/undo.png")));
		panelDto.add(buttonSalir);
	}
	public void limpiarTexts() {
		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.requestFocus(true);
		
		textNumero.setText(null);
		textNumero.setBackground(Menu.textColorBackgroundInactivo);	
		textNumero.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
		
		textMonto.setText(null);
		textMonto.setBackground(Menu.textColorBackgroundInactivo);	
		textMonto.setForeground(Menu.textColorForegroundInactivo);
		
		cbDocumento.setSelectedIndex(-1);
				
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textNumero.setEnabled(b);
		textArea.setEnabled(b);
		cbDocumento.setEnabled(b);
	}
	public void activarButton(boolean c) {
		 if (c == true){
			buttonGrabar.setEnabled(false);// GRABAR
			buttonSalir.setEnabled(true);	// SALIR

			panelDto.setVisible(true);// PANEL DATOS
		 }
		 if (c == false){
			buttonGrabar.setEnabled(true);// GRABAR
			buttonSalir.setEnabled(false);	// SALIR

			//buttonDsct.setEnabled(true);// ALTA DE DSCT A CLIENTES
			panelDto.setVisible(true);// PANEL DATOS
		 }
	}
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonGrabar){// GRABAR
			  insertarUpdate();
			  }
		  if (evento.getSource() == buttonSalir){// SALIR
			  frame.dispose();
			  }

		  if (evento.getSource().equals(cbDocumento)) {
			  	}
		  
		  if (evento.getSource().equals(cbDoTipoGasto)) {

		  	  }
		}

	public void insertarUpdate() {
		if (VentanaLogin.ID_APETURA_CAJA==0){
			JOptionPane.showMessageDialog(null, "Primero debe aperturar su TURNO...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (VentanaLogin.COD_EMP_USER.trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "No se encontro ningun usuario...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
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
		if (cbDoTipoGasto.getSelectedItem()==null||cbDoTipoGasto.getSelectedItem().toString().trim().equals("")){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbDoTipoGasto.requestFocus();
			return;
		}
		if (textArea.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textArea.requestFocus();
			return;
		}
		if (cbDocumento.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbDocumento.requestFocus();
			return;
		}
		if (textNumero.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textNumero.requestFocus();
			return;
		}
		if (textMonto.getText().trim().isEmpty()||Float.parseFloat(textMonto.getText().trim().replaceAll(",", ""))==0){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textMonto.requestFocus();
			textMonto.selectAll();
			return;
		}
		conexion = new ConexionDB();
		if (MOD==0) {// REGISTRAR
			try {
				String sql ="INSERT INTO  GASTOS (Id_Gst,TipoGst,ConceptoGst,DocumentoGst,NumeroGst,FechaGst,HoraGst,MontoGst) VALUES (?,?,?,?,?,?,?,?)";
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setString(1, textCod.getText().trim());
				ps.setString(2,cbDoTipoGasto.getSelectedItem().toString());
				ps.setString(3, textArea.getText().trim());
				ps.setString(4,cbDocumento.getSelectedItem().toString());
				ps.setString(5, textNumero.getText().trim());
				ps.setNString(6, dateEmision.trim());
				ps.setNString(7, Menu.HORA.trim());//12 HORAS HILO
				ps.setString(8, textMonto.getText().trim().replaceAll(",", ""));
				ps.execute();
				ps.close();
				JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				limpiarTexts();
				//activarButton(true);
				llenarNuevo();
				textArea.requestFocus();
				//frame.dispose();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
		}
		if (MOD==1) { // MODIFICAR 
			try {
		        String sql="UPDATE GASTOS SET TipoGst =?,"
		                 + "ConceptoGst =?,"
		                 + "DocumentoGst =?,"
		                 + "NumeroGst =?,"
		                 + "FechaGst =?,"
		                 + "HoraGst =?,"
		                 + "MontoGst =?"
		                 + "WHERE Id_Gst ='"+Integer.parseInt(textCod.getText())+"'"; 
		         
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setString(1, cbDoTipoGasto.getSelectedItem().toString());
				ps.setString(2, textArea.getText().trim());
				ps.setString(3,cbDocumento.getSelectedItem().toString());
				ps.setString(4, textNumero.getText().trim());
				ps.setNString(6, Menu.HORA.trim());//12 HORAS HILO
				ps.setString(7, textMonto.getText().trim().replaceAll(",", ""));
				// DOY FORMATO A LA FECHA
				ps.setNString(5, dateEmision.trim());
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
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textNumero){textNumero.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textMonto){textMonto.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbDocumento){cbDocumento.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textNumero){textNumero.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == cbDocumento){cbDocumento.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textMonto){textMonto.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
			
			if (ev.getSource() == textNumero){textNumero.selectAll();} 
			if (ev.getSource() == textMonto){textMonto.selectAll();} 
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textNumero){textNumero.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textMonto){textMonto.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == cbDocumento){cbDocumento.setBackground(new Color(240,240,240));}
			
			if (ev.getSource() == textNumero){textNumero.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == cbDocumento){cbDocumento.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textMonto){textMonto.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			if (evet.getSource().equals(cbDoTipoGasto)){
				if (cbDoTipoGasto.getSelectedItem()==""){
					cbDoTipoGasto.requestFocus();
					} 
					else if (e==KeyEvent.VK_ENTER){// || textFormatAmortizo.getText().toLowerCase().length()==5){
						textArea.requestFocus();
						textArea.selectAll();
					}
			}	
				if (evet.getSource().equals(textArea)){
					int pos = textArea.getCaretPosition();textArea.setText(textArea.getText().toUpperCase());textArea.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textArea.getText().toLowerCase().isEmpty()|| textArea.getText().toLowerCase().length()>295){
						textArea.requestFocus();
						textArea.selectAll();
						textArea.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textArea.getText().toLowerCase().length()==295){
							cbDocumento.requestFocus();
						}
				}	
				if (evet.getSource().equals(cbDocumento)){
					if (cbDocumento.getSelectedItem()==""){
						cbDocumento.requestFocus();
						} 
						else if (e==KeyEvent.VK_ENTER){// || textFormatAmortizo.getText().toLowerCase().length()==5){
							textNumero.requestFocus();
							textNumero.selectAll();
						}
				}	
				if (evet.getSource().equals(textNumero)){
					if (textNumero.getText().toLowerCase().isEmpty()|| textNumero.getText().toLowerCase().length()>19){
						textNumero.requestFocus();
						textNumero.selectAll();
						textNumero.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textNumero.getText().toLowerCase().length()==19){
							textMonto.requestFocus();
							textMonto.selectAll();
						}
				}
				if (evet.getSource().equals(textMonto)){
					if (textMonto.getText().toLowerCase().isEmpty()|| textMonto.getText().toLowerCase().length()>=8){
						textMonto.requestFocus();
						textMonto.selectAll();
						textMonto.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER){// || textFormatAmortizo.getText().toLowerCase().length()==5){
							//internalFrameDialog.setVisible(false);
							buttonGrabar.doClick();
						}
					}
				}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char e=evet.getKeyChar();
			if (evet.getSource().equals(textNumero)) {
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource().equals(textMonto)) {
				if ((e<'0'||e>'9')&&(e<'.'||e>'.'))evet.consume();
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
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		
	}
}
	
