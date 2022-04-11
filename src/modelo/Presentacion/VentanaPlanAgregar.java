
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
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;

public class VentanaPlanAgregar implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JInternalFrame frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl3,lbl5,lbl7,lbl10,lbl11,lbl12;
	private JButton  			buttonGrabar,buttonSalir;
	protected static JDateChooser dateChooserAlta;
	protected static String dateAlta;
	protected JTextField 			textCod,textInicial,textDescripcion;
	protected static JComboBox<String> 		cbEstado;
	protected static JFormattedTextField 	textCosto;
	
	private JScrollPane scrollArea;
	protected static JTextArea textArea = new JTextArea();
	
	
	public static int MOD;
	String cadenaDescripcion="";
	
	NumberFormat formatDsct;

	// constructor
	public VentanaPlanAgregar() {
		framePlanAgregar();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		llenarcombox();
		limpiarTexts();
		llenarNuevo();
		dateChooserAlta.setDate(Menu.fecha);
		if (MOD==1) {
			llenarModificar();
		}

		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dateAlta = form.format(dateChooserAlta.getDate());
		
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    Menu.Desktop.add(frame);
	}
	public void framePlanAgregar() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaPlanAgregar.class.getResource("/modelo/Images/Add.png")));
		frame.setTitle("Agregar planes de Alojamiento");
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			//@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
			}
		});
		frame.setClosable(true);
		frame.setBounds(100, 100, 501, 295);
		frame.getContentPane().setLayout(null);
	}
	
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 468, 244);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		dateChooserAlta = new JDateChooser();
		dateChooserAlta.setDateFormatString("dd-MMM-yyyy");
		dateChooserAlta.setBorder(new LineBorder(new Color(51, 153, 255)));
		dateChooserAlta.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)dateChooserAlta.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)dateChooserAlta.getDateEditor()).setEditable(false);
		dateChooserAlta.setToolTipText("Fecha de alta");
		dateChooserAlta.setBounds(190, 95, 95, 20);
		dateChooserAlta.addPropertyChangeListener(this);
		panelDto.add(dateChooserAlta);
		dateChooserAlta.setVisible(false);
		
	}
	public void crearLabels() {
		lbl1= new JLabel("Código:");
		lbl1.setBounds(16, 25, 61, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Descripción:");
		lbl3.setBounds(16, 73, 61, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl5= new JLabel("Costo:");
		lbl5.setBounds(20, 94, 57, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);

		lbl7= new JLabel("Iniciales:");
		lbl7.setBounds(16, 49, 61, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl10 = new JLabel("Fecha:");
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl10.setBounds(137, 101, 46, 14);
		panelDto.add(lbl10);
		lbl10.setVisible(false);
		
		lbl11 = new JLabel("Observación:");
		lbl11.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl11.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl11.setBounds(0, 142, 77, 14);
		panelDto.add(lbl11);
		
		lbl12 = new JLabel("Estado:");
		lbl12.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl12.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl12.setBounds(26, 121, 51, 14);
		panelDto.add(lbl12);
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
		textCod.setBounds(87, 21, 77, 22);
		panelDto.add(textCod);
		
		textInicial= new JTextField();
		textInicial.setColumns(10);
		textInicial.setFont(Menu.fontText);
		textInicial.setForeground(Menu.textColorForegroundInactivo);
		textInicial.setHorizontalAlignment(SwingConstants.LEFT);
		textInicial.addActionListener(this);
		textInicial.addFocusListener(this);
		textInicial.addKeyListener(this);
		textInicial.setBounds(87, 45, 132, 22);
		panelDto.add(textInicial);
		
		textDescripcion= new JTextField();
		textDescripcion.setColumns(10);
		textDescripcion.setFont(Menu.fontText);
		textDescripcion.setForeground(Menu.textColorForegroundInactivo);
		textDescripcion.setHorizontalAlignment(SwingConstants.LEFT);
		textDescripcion.addActionListener(this);
		textDescripcion.addFocusListener(this);
		textDescripcion.addKeyListener(this);
		textDescripcion.addPropertyChangeListener(this);
		textDescripcion.setBounds(87, 69, 326, 22);
		panelDto.add(textDescripcion);

		scrollArea= new JScrollPane();
		scrollArea.setBounds(87, 142, 364, 87);
		panelDto.add(scrollArea);
		textArea = new JTextArea();
		scrollArea.setViewportView(textArea);
		textArea.setFont(Menu.fontText);
		textArea.setForeground(Menu.textColorForegroundInactivo);
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);
		
		textCosto = new JFormattedTextField();
		textCosto.setBackground(new Color(255, 255, 255));
		textCosto.setText("0.00");
		textCosto.setHorizontalAlignment(SwingConstants.LEFT);
		textCosto.setBounds(87, 94, 57, 21);
		textCosto.addFocusListener(this);
		textCosto.addKeyListener(this);
		panelDto.add(textCosto);

	}
	
	public void crearComboBox() {
		
		cbEstado = new JComboBox<>();
		cbEstado.setBounds(87, 118, 57, 21);
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
			ResultSet resultSet = statement.executeQuery("Select Id_Pla from PLANES order by Id_Pla desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_Pla"))+1);
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
			String con = "Select * from PLANES where Id_Pla ='" + Integer.parseInt(VentanaPlan.id) + "'";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			if (rs.next()==true) {
		        
				//dateAlta=(rs.getString("FechaActualizarInv").toString());
				Date date = Menu.formatoFecha.parse(dateAlta);
				dateChooserAlta.setDate(date);
				dateChooserAlta.setEnabled(false);
				
				textInicial.setText(rs.getString("InicialesPla"));
				textDescripcion.setText(rs.getString("DescripcionPla"));
				textCosto.setText(rs.getString("CostoPla"));
				textArea.setText(rs.getString("ObservacionPla"));
				cbEstado.setSelectedItem(rs.getString("EstadoPla"));
			}
			st.close();
		} catch (Exception e) {}
	}
	public static void llenarcombox() { 
		cbEstado.removeAllItems();
		cbEstado.addItem("A");
		cbEstado.addItem("X");
		cbEstado.setSelectedIndex(0);
	}
	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(378, 116, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaInventariosAgregar.class.getResource("/modelo/Images/save.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Cerrar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(415, 116, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaInventariosAgregar.class.getResource("/modelo/Images/Exit.png")));
		panelDto.add(buttonSalir);
	}
	
	public void limpiarTexts() {
		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.requestFocus(true);
		
		textCosto.setText(null);
		textCosto.setBackground(Menu.textColorBackgroundInactivo);	
		textCosto.setForeground(Menu.textColorForegroundInactivo);
		
		textInicial.setText(null);
		textInicial.setBackground(Menu.textColorBackgroundInactivo);	
		textInicial.setForeground(Menu.textColorForegroundInactivo);
		
		textDescripcion.setText(null);
		textDescripcion.setBackground(Menu.textColorBackgroundInactivo);	
		textDescripcion.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);

        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textInicial.setEnabled(b);
		textDescripcion.setEnabled(b);
		textCosto.setEnabled(b);
		textArea.setEnabled(b);
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
			if (textInicial.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textInicial.requestFocus();
				return;
			}
			if (textDescripcion.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDescripcion.requestFocus();
				return;
			}
			if (textCosto.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCosto.requestFocus();
				return;
			}
			if (textArea.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textArea.requestFocus();
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
					String sql ="INSERT INTO  PLANES (Id_Pla,InicialesPla,DescripcionPla,CostoPla,ObservacionPla,EstadoPla) VALUES (?,?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText().toString().trim()));
					ps.setString(2,textInicial.getText().trim());
					ps.setString(3,textDescripcion.getText().trim());
					ps.setString(4, textCosto.getText().trim());;
					ps.setString(5,textArea.getText().trim());
					ps.setString(6,(String)cbEstado.getSelectedItem());
					
					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					limpiarTexts();
					llenarNuevo();
					textInicial.requestFocus();
					//activarButton(true);
					//frame.dispose();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			if (MOD==1) { // MODIFICAR 
				try {
			         String sql="UPDATE PLANES SET InicialesPla = ?,"
			                 + "DescripcionPla =?,"
			                 + "CostoPla = ?,"
			                 + "ObservacionPla = ?,"
			                 + "EstadoPla = ?"
			                
			                 + "WHERE Id_Pla = '"+ Integer.parseInt(textCod.getText()) + "'"; 
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setString(1,textInicial.getText().trim());
					ps.setString(2,textDescripcion.getText().trim());
					ps.setString(3, textCosto.getText().trim());;
					ps.setString(4,textArea.getText().trim());
					ps.setString(5,(String)cbEstado.getSelectedItem());
					
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
			if (ev.getSource() == cbEstado){cbEstado.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textInicial){textInicial.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textDescripcion){textDescripcion.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textCosto){textCosto.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textCosto){textCosto.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textInicial){textInicial.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textDescripcion){textDescripcion.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbEstado){cbEstado.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == cbEstado){cbEstado.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textInicial){textInicial.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textDescripcion){textDescripcion.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textCosto){textCosto.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textCosto){textCosto.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textInicial){textInicial.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textDescripcion){textDescripcion.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbEstado){cbEstado.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
				if (evet.getSource() == textInicial){
					int pos = textInicial.getCaretPosition();textInicial.setText(textInicial.getText().toUpperCase());textInicial.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					cadenaDescripcion=textInicial.getText() ;
					if (textInicial.getText().toLowerCase().isEmpty()|| textInicial.getText().toLowerCase().length()>9){
						textInicial.requestFocus();
						textInicial.selectAll();
						textInicial.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textInicial.getText().toLowerCase().length()==9){
							textDescripcion.requestFocus();
							textDescripcion.selectAll();	
						}
				} 
				if (evet.getSource() == textDescripcion){
					int pos = textDescripcion.getCaretPosition();textDescripcion.setText(textDescripcion.getText().toUpperCase());textDescripcion.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textDescripcion.getText().toLowerCase().isEmpty()|| textDescripcion.getText().toLowerCase().length()>98){
						textDescripcion.requestFocus();
						textDescripcion.selectAll();
						textDescripcion.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textDescripcion.getText().toLowerCase().length()==98){
							textCosto.requestFocus();
							textCosto.selectAll();	
						}
				}
				if (evet.getSource() == textCosto){
					int pos = textCosto.getCaretPosition();textCosto.setText(textCosto.getText().toUpperCase());textCosto.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textCosto.getText().toLowerCase().isEmpty()|| textCosto.getText().toLowerCase().length()>8){
						textCosto.requestFocus();
						textCosto.selectAll();
						textCosto.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textCosto.getText().toLowerCase().length()==8){
							cbEstado.requestFocus();
						}
				}
				if (evet.getSource() == textArea){
					int pos = textArea.getCaretPosition();textArea.setText(textArea.getText().toUpperCase());textArea.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textArea.getText().toLowerCase().isEmpty()|| textArea.getText().toLowerCase().length()>245){
						textArea.requestFocus();
						textArea.selectAll();
						textArea.setText(null);
						}
						if (e==KeyEvent.VK_ENTER || textArea.getText().toLowerCase().length()==245){
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
			if (evet.getSource() == textInicial){
				if((car<'a' || car>'z') && (car<'A' || car>'Z')&&(car<' '||car>'.')&&(car<'0'||car>'9')) evet.consume();
			}
			if (evet.getSource() == textDescripcion){
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textCosto){
		       //if(textFormatCosto.getText().length()>=8)evet.consume();
		       if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
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
