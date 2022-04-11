
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
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;

public class VentanaUsuario implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JInternalFrame frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5;
	private JButton  			buttonGrabar,buttonSalir,buttonBus;
	protected static JDateChooser dateChooserActualizar;
	protected static String dateAlta,dateActualizar;
	protected JTextField 			textCod,textNom,textCuenta;
	protected JPasswordField 		textPassword,textConfir;
	protected static JComboBox<String> 		cbTipo;
	protected static JTextArea textArea = new JTextArea();
	
	public static int CONTAR_USUARIO=0;
	public static int MOD;
	
	// constructor
	public VentanaUsuario() {
		frameUsuario();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		
		llenarcbTipo();
		llenarModificar();
		
		CONTAR_USUARIO ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
		
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    Menu.Desktop.add(frame);
	}
	public void frameUsuario() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaUsuario.class.getResource("/modelo/Images/user.png")));
		frame.setTitle("Cuenta de usuario");
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			//@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				CONTAR_USUARIO=0;
			}

		});
		frame.setClosable(true);
		frame.setBounds(100, 100, 453, 231);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 425, 175);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
	}
	public void crearLabels() {
		lbl1= new JLabel("Empleado:");
		lbl1.setBounds(32, 35, 63, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Tipo:");
		lbl2.setBounds(18, 62, 75, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Cuenta:");
		lbl3.setBounds(12, 86, 81, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Password:");
		lbl4.setBounds(22, 111, 71, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);
		
		lbl5= new JLabel("Confirmar:");
		lbl5.setBounds(16, 133, 77, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);
		
	}
	public void crearTextFields(){
		
		textCod = new JTextField();
		textCod.setEditable(false);
		textCod.setColumns(10);
		textCod.setFont(Menu.fontText);
		textCod.setForeground(Menu.textColorForegroundActivo);
		textCod.setHorizontalAlignment(SwingConstants.CENTER);
		textCod.addActionListener(this);
		textCod.addFocusListener(this);
		textCod.addKeyListener(this);
		textCod.setBounds(103, 35, 63, 22);
		panelDto.add(textCod);
		textCod.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		
		textNom = new JTextField();
		textNom.setEditable(false);
		textNom.setColumns(10);
		textNom.setFont(Menu.fontText);
		textNom.setForeground(Menu.textColorForegroundActivo);
		textNom.setHorizontalAlignment(SwingConstants.LEFT);
		textNom.addActionListener(this);
		textNom.addFocusListener(this);
		textNom.addKeyListener(this);
		textNom.setBounds(169, 35, 233, 22);
		panelDto.add(textNom);
		textNom.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		
		textCuenta= new JTextFieldIcon(new JTextField(),"user.png","Cuenta usuario","user.png");
		textCuenta.setColumns(10);
		textCuenta.setFont(new Font("Tahoma", Font.BOLD, 12));
		textCuenta.setForeground(Menu.textColorForegroundActivo);
		textCuenta.setHorizontalAlignment(SwingConstants.LEFT);
		textCuenta.addActionListener(this);
		textCuenta.addFocusListener(this);
		textCuenta.addKeyListener(this);
		textCuenta.setBounds(103, 82, 149, 22);
		panelDto.add(textCuenta);
		
		textPassword= new JPasswordField();
		textPassword.setColumns(10);
		textPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		textPassword.setForeground(Menu.textColorForegroundInactivo);
		textPassword.setHorizontalAlignment(SwingConstants.LEFT);
		textPassword.addActionListener(this);
		textPassword.addFocusListener(this);
		textPassword.addKeyListener(this);
		textPassword.setBounds(103, 107, 100, 22);
		panelDto.add(textPassword);
		
		textConfir= new JPasswordField();
		textConfir.setColumns(10);
		textConfir.setFont(new Font("Tahoma", Font.BOLD, 12));
		textConfir.setForeground(Menu.textColorForegroundInactivo);
		textConfir.setHorizontalAlignment(SwingConstants.LEFT);
		textConfir.addActionListener(this);
		textConfir.addFocusListener(this);
		textConfir.addKeyListener(this);
		textConfir.setBounds(103, 132, 100, 22);
		panelDto.add(textConfir);

	}
	
	public void crearComboBox() { 
		cbTipo = new JComboBox<>();
		cbTipo.setBounds(103, 60, 187, 21);
		cbTipo.setFont(Menu.fontText);
		cbTipo.addActionListener(this);
		cbTipo.addFocusListener(this);
		cbTipo.addKeyListener(this);
		panelDto.add(cbTipo);
	}
	
	public void llenarModificar() { 
		conexion= new ConexionDB();
		try {
			String con = "Select * from USUARIO where Id_Emp ='" + Integer.parseInt(VentanaEmpleado.id) + "'";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Nuevo: ítem " , TitledBorder.LEADING, TitledBorder.TOP, null,( new Color(106, 90, 205))));
			MOD=0;
			if (rs.next()==true) {
				cbTipo.setSelectedItem(rs.getString("TipoUsu"));
				textCuenta.setText(rs.getString("CuentaUsu"));
				textPassword.setText(rs.getString("PasswordUsu"));
				textConfir.setText(rs.getString("PasswordConfirmarUsu"));
				MOD=1;
				panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modificar: ítem " , TitledBorder.LEADING, TitledBorder.TOP, null,( new Color(106, 90, 205))));
			}
			st.close();
		} catch (Exception e) {}
	}
	public static void llenarcbTipo() { 
		cbTipo.removeAllItems();
		String [] lista1 = {"ADMINISTRADOR","RECEPCIONISTA","CAJERO","INVITADO"};
		Arrays.sort (lista1);
		for (String llenar:lista1) {
			cbTipo.addItem(llenar);
		}
	}

	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(213, 132, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaUsuario.class.getResource("/modelo/Images/save.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Cerrar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(250, 132, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaUsuario.class.getResource("/modelo/Images/Exit.png")));
		panelDto.add(buttonSalir);
		
		buttonBus= new JButton("");
		buttonBus.setBounds(292, 59, 36, 22);
		buttonBus.setToolTipText("Buscar");
		buttonBus.addActionListener(this);
		buttonBus.setIcon(new ImageIcon(VentanaUsuario.class.getResource("/modelo/Images/search.png")));
		panelDto.add(buttonBus);
		buttonBus.setVisible(false);
	}
	
	public void limpiarTexts() {
		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.requestFocus(true);
		
		textNom.setText(null);
		textNom.setBackground(Menu.textColorBackgroundInactivo);	
		textNom.setForeground(Menu.textColorForegroundInactivo);
		
		textCuenta.setText(null);
		textCuenta.setBackground(Menu.textColorBackgroundInactivo);	
		textCuenta.setForeground(Menu.textColorForegroundInactivo);
		
		textPassword.setText(null);
		textPassword.setBackground(Menu.textColorBackgroundInactivo);	
		textPassword.setForeground(Menu.textColorForegroundInactivo);
		
		textConfir.setText(null);
		textConfir.setBackground(Menu.textColorBackgroundInactivo);	
		textConfir.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
		
		cbTipo.removeAllItems();

        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textCuenta.setEnabled(b);
		textPassword.setEnabled(b);
		textNom.setEnabled(b);
		textConfir.setEnabled(b);
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

	  		  }
		  if (evento.getSource() == cbTipo){
			  
			  }
		}
	
		public void insertarUpdate() {
			if (textCod.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCod.requestFocus();
				this.frame.dispose();
				return;
			}
			if (cbTipo.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbTipo.requestFocus();
				return;
			}
			if (textCuenta.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCuenta.requestFocus();
				return;
			}
			if (textPassword.getPassword().length<=0){
				JOptionPane.showMessageDialog(null, "Ingrese su password / clave",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textPassword.requestFocus();
				return;
			}
			String password = new String(textPassword.getPassword());
			
			if (textConfir.getPassword().length<=0){
				JOptionPane.showMessageDialog(null, "Confirme el password...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textConfir.requestFocus();
				return;
			}
			String confirme = new String(textConfir.getPassword());
			
			if (!password.trim().equals(confirme.trim())){
				JOptionPane.showMessageDialog(null, "Claves incorrectas...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textConfir.requestFocus();
				return;
			}
			conexion = new ConexionDB();
			if (MOD==0) {// REGISTRAR
				try {
					String sql ="INSERT INTO  USUARIO (Id_Emp,TipoUsu,CuentaUsu,PasswordUsu,PasswordConfirmarUsu) VALUES (?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText().toString().trim()));
					ps.setString(2, (cbTipo.getSelectedItem()).toString());
					ps.setString(3, (textCuenta.getText()).toString());
					ps.setNString(4, password.trim());
					ps.setNString(5,confirme.trim());

					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					limpiarTexts();
					llenarcbTipo();
					//activarButton(true);
					//frame.dispose();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			if (MOD==1) { // MODIFICAR 
				try {
			         String sql="UPDATE USUARIO SET Id_Emp = ?,"
			        		 + "TipoUsu =?,"
			                 + "CuentaUsu =?,"
			                 + "PasswordUsu = ?,"
			                 + "PasswordConfirmarUsu = ?"
			                 + "WHERE Id_Emp = '"+ textCod.getText().trim() + "'"; 
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText().toString().trim()));
					ps.setString(2, (cbTipo.getSelectedItem()).toString());
					ps.setString(3, (textCuenta.getText()).toString());
					ps.setNString(4, password.trim());
					ps.setNString(5,confirme.trim());

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
			if (ev.getSource() == textCuenta){textCuenta.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textPassword){textPassword.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textConfir){textConfir.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textCuenta){textCuenta.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textPassword){textPassword.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textConfir){textConfir.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == cbTipo){cbTipo.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textCuenta){textCuenta.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textPassword){textPassword.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textConfir){textConfir.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textCuenta){textCuenta.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textPassword){textPassword.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textConfir){textConfir.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			if (evet.getSource() == cbTipo){
				if (e==KeyEvent.VK_ENTER){
					if (cbTipo.getSelectedIndex()!=-1){
						textCuenta.requestFocus();
						textCuenta.selectAll();
					}else{
						cbTipo.requestFocus();
					}
				}	
			}
			if (evet.getSource() == textCuenta){
				if (textCuenta.getText().toLowerCase().isEmpty()|| textCuenta.getText().toLowerCase().length()>8){
					textCuenta.requestFocus();
					textCuenta.selectAll();
					textCuenta.setText(null);
					} 
					 if (e==KeyEvent.VK_ENTER){
						textPassword.requestFocus();
						textPassword.selectAll();
					}
			}
			if (evet.getSource() == textPassword){
				if (textPassword.getPassword().equals("")||textPassword.getPassword().length>8){
					textPassword.requestFocus();
					textPassword.selectAll();
					textPassword.setText(null);
					} 
					 if (e==KeyEvent.VK_ENTER){
						textConfir.requestFocus();
						textConfir.selectAll();
					}
			}
			if (evet.getSource() == textConfir){
				if (textConfir.getPassword().equals("")||textConfir.getPassword().length>8){
					textConfir.requestFocus();
					textConfir.selectAll();
					textConfir.setText(null);
					} 
					 if (e==KeyEvent.VK_ENTER){
						 buttonGrabar.doClick();
					}
			}
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
		}
		
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		//Object source = e.getSource();
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		
	}
	}
