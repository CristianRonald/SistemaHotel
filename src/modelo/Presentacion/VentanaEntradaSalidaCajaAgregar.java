
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
import java.util.Date;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;

public class VentanaEntradaSalidaCajaAgregar implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JInternalFrame frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl7,lbl8,lbl9;
	protected JLabel			lblAcuenta;
	private JButton  			buttonGrabar,buttonSalir;
	protected static JDateChooser dateChooserEmision;
	protected static String dateEmision;
	protected static JTextField 			textCod;
	protected static JComboBox<String> 		cbDocumento;
	protected static JFormattedTextField 	textNumero,textMonto;
	
	private JScrollPane scrollArea;
	protected static JTextArea textArea = new JTextArea();
	protected static JRadioButton rdbtnEntrada,rdbtnSalida;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public static int MOD;
	
	NumberFormat formatDsct;
	// constructor
	public VentanaEntradaSalidaCajaAgregar() {
		frameEntradaSalidaCajaAgregar();
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
	public void frameEntradaSalidaCajaAgregar() {
		frame = new JInternalFrame();
		frame.setTitle("Agregar movimientos de caja");
		frame.setFrameIcon(new ImageIcon(VentanaEntradaSalidaCajaAgregar.class.getResource("/modelo/Images/cajamov.png")));
		
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
		dateChooserEmision.setBounds(101, 48, 95, 23);
		dateChooserEmision.addPropertyChangeListener(this);
		panelDto.add(dateChooserEmision);
	}
	public void llenarNuevo() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_Caj from CAJA order by Id_Caj desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_Caj"))+1);
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
			ResultSet resultSet = statement.executeQuery("Select* from CAJA where Id_Caj ='" + VentanaCajaBalance.id + "'");
			if (resultSet.next()== true) {
				textCod.setText(Menu.formatid_9.format(resultSet.getInt("Id_Caj")));
				textArea.setText(resultSet.getString("descripcionCaj"));
				cbDocumento.setSelectedItem((String)resultSet.getString("DocumentoCaj"));
				textNumero.setText((resultSet.getString("NumeroDocCaj")));
				textMonto.setText(Menu.formateadorCurrency.format(resultSet.getFloat("MontoCaj")));
				lbl3.setText(resultSet.getString("HoraCaj").toString());
				dateEmision=(resultSet.getString("FechaCaj").toString());
				Date date = Menu.formatoFecha.parse(dateEmision);
				dateChooserEmision.setDate(date);
				if (resultSet.getString("TipoMov").equals("INGRESO")){
					rdbtnEntrada.setSelected(true);
				}
				if (resultSet.getString("TipoMov").equals("EGRESO")){
					rdbtnSalida.setSelected(true);
				}
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
		lbl3.setBounds(202, 54, 65, 14);
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
		
		lbl9= new JLabel("Tipo mov.");
		lbl9.setBounds(18, 73, 75, 14);
		panelDto.add(lbl9);
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(Menu.fontLabel);
		
		JLabel lblTot = new JLabel("Monto:");
		lblTot.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTot.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTot.setBounds(377, 168, 57, 14);
		panelDto.add(lblTot);
		
		rdbtnEntrada = new JRadioButton("INGRESO");
		rdbtnEntrada.setSelected(true);
		buttonGroup.add(rdbtnEntrada);
		rdbtnEntrada.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnEntrada.setBounds(102, 73, 75, 20);
		rdbtnEntrada.addActionListener(this);
		panelDto.add(rdbtnEntrada);
		
		rdbtnSalida = new JRadioButton("EGRESO");
		buttonGroup.add(rdbtnSalida);
		rdbtnSalida.setFont(new Font("Tahoma", Font.PLAIN, 11));
		rdbtnSalida.setBounds(173, 73, 75, 20);
		rdbtnSalida.addActionListener(this);
		panelDto.add(rdbtnSalida);
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
	}
	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(444, 179, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaEntradaSalidaCajaAgregar.class.getResource("/modelo/Images/check.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Regresar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(482, 179, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaEntradaSalidaCajaAgregar.class.getResource("/modelo/Images/undo.png")));
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
			  if (rdbtnSalida.isSelected()==true) {
				  rdbtnSalida.doClick();
			  	}
			  }
		  if (evento.getSource() == buttonSalir){// SALIR
			  frame.dispose();
			  }

		  if (evento.getSource().equals(cbDocumento)) {
			  	}
		  
		  if (evento.getSource().equals(rdbtnEntrada)) {

		  	  }
		  if (evento.getSource().equals(rdbtnSalida)) {
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
		if (rdbtnEntrada.isSelected()==false&&rdbtnSalida.isSelected()==false){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el tipo de movimiento...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		conexion = new ConexionDB();
		if (MOD==0) {// REGISTRAR
			try {
				String sql ="INSERT INTO  CAJA (Id_Caj,Id_ApeCie,TipoMov,MovCaj,DescripcionCaj,DocumentoCaj,NumeroDocCaj,FechaCaj,HoraCaj,MontoCaj,EstadoCaj) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setString(1, textCod.getText().trim());
				ps.setInt(2, VentanaLogin.ID_APETURA_CAJA);
				if (rdbtnEntrada.isSelected()==true){
					ps.setString(3, rdbtnEntrada.getText().trim());
				}
				if (rdbtnSalida.isSelected()==true){
					ps.setString(3, rdbtnSalida.getText().trim());
				}
				ps.setString(4, "CAJA-CHICA");
				ps.setString(5, textArea.getText().trim());
				ps.setString(6,cbDocumento.getSelectedItem().toString());
				ps.setString(7, textNumero.getText().trim());
				ps.setNString(8, dateEmision.trim());
				ps.setNString(9, Menu.HORA.trim());//12 HORAS HILO
				ps.setString(10, textMonto.getText().trim().replaceAll(",", ""));
				ps.setInt(11, 1);
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
		        String sql="UPDATE caja SET Id_Caj =?,"
		                 + "TipoMov =?,"
		                 + "DescripcionCaj =?,"
		                 + "DocumentoCaj =?,"
		                 + "NumeroDocCaj =?,"
		                 + "FechaCaj =?,"
		                 + "HoraCaj =?,"
		                 + "MontoCaj =?,"
		                 + "EstadoCaj =?"
		                 + "WHERE Id_Caj ='"+Integer.parseInt(textCod.getText())+"'"; 
		         
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(textCod.getText()));
				if (rdbtnEntrada.isSelected()==true){
					ps.setString(2, rdbtnEntrada.getText().trim());
				}
				if (rdbtnSalida.isSelected()==true){
					ps.setString(2, rdbtnSalida.getText().trim());
				}
				ps.setString(3, textArea.getText().trim());
				ps.setString(4,cbDocumento.getSelectedItem().toString());
				ps.setString(5, textNumero.getText().trim());
				ps.setNString(7, Menu.HORA.trim());//12 HORAS HILO
				ps.setString(8, textMonto.getText().trim().replaceAll(",", ""));
				ps.setString(9, "1");
				// DOY FORMATO A LA FECHA
				ps.setNString(6, dateEmision.trim());
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
					if (textNumero.getText().toLowerCase().isEmpty()|| textNumero.getText().toLowerCase().length()>18){
						textNumero.requestFocus();
						textNumero.selectAll();
						textNumero.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textNumero.getText().toLowerCase().length()==18){
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
				if (e<'0'||e>'9')evet.consume();
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
	