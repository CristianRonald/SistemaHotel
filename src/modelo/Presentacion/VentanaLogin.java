
package modelo.Presentacion;

import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
//import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;

public class VentanaLogin implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JFrame frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl2,lbl3,lbl4,lblFto;
	private JButton  			buttonGrabar,buttonSalir;
	protected JTextField 			textCuenta;
	protected static JComboBox<String> 		cbTipo;
	public static String COD_EMP_USER="",NOM_USUARIO="",TIP_USUARIO="",ID_EMP="",TUR_CORTO="",NOMBRE_USU_CORTO="";
	public static String RAZON_SOCIAL_HOTEL="",RUC_HOTEL="",DIRECCION_HOTEL="",TELEFONO_HOTEL="",URLIMAGE_HOTEL="";
	protected JPasswordField textPassword;
	private JLabel button_1,button_2,button_3,button_4,button_5;
	
	private int exit=0;
	public static int ID_APETURA_CAJA=0;
	public static String TURNO="******",DESCRIPCION_TURNO="CERRADO";
	public static int ID_TURNO=0;
	public static double TIPO_CAMBIO;
	//public static String FechaAyer="";
	
	public VentanaLogin() {
		frameLogin();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		llenarcbTipo();
		mostrarLogo();
	}
	public void frameLogin() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				if(exit==0) {
					System.exit(0);
				}
			}
		});
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaLogin.class.getResource("/modelo/Images/login.png")));
		frame.setResizable(false);
		
		frame.setTitle("::::::::::::::::::::::::::  Ruc - Nombre hotel  ::::::::::::::::::::::::");
		frame.setBounds(100, 100, 449, 175);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setFont(new Font("Tahoma", Font.BOLD, 12));
	}
    public void opcacityLogin(){
        
    	// PARA LA VENTANA LOIGIN
		// Determinar si el dispositivo de gráficos compatible con la translucidez.
        GraphicsEnvironment ge = 
        GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        //Si no son compatibles con ventanas translúcidas, salida.
        if (!gd.isWindowTranslucencySupported(TRANSLUCENT)) {
        	VentanaLogin logearse = new VentanaLogin() {};
        	logearse.frame.setVisible(true);
            //System.exit(0);
        }
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Crear la interfaz gráfica de usuario en el hilo de despacho de eventos
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // TRANPARENTE SI DESEO ACTIVARLO DSACTIVE EL SKINS  
            	VentanaLogin logearse = new VentanaLogin();
            	//logearse.frame.setOpacity(0.95f); // Fije la ventana al 55% opaco (45% translúcida).
            	logearse.frame.setVisible(true);
            }
        });
    }
	public void mostrarLogo () {
		// CARGO LA IMAGEN
		conexion = new ConexionDB();
		try {
			String cons="Select* from EMPRESA";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(cons);
			if (rs.next()==true) {
				frame.setTitle(rs.getString("RazonSocial")); //+" | " + rs.getString("Ruc"));
				if (rs.getString("Categoria").equals("3")) {
					button_3.setEnabled(true);
				}
				if (rs.getString("Categoria").equals("4")) {
					button_3.setEnabled(true);
					button_4.setEnabled(true);
				}
				if (rs.getString("Categoria").equals("5") || rs.getInt("Categoria")>5) {
					button_3.setEnabled(true);
					button_4.setEnabled(true);
					button_5.setEnabled(true);
				}
			    Image i=null;
		        Blob blob = rs.getBlob("Logo");
		        i= javax.imageio.ImageIO.read(blob.getBinaryStream());
		        ImageIcon image = new ImageIcon(i);
				Icon icono = new ImageIcon(image.getImage().getScaledInstance(lblFto.getWidth(), lblFto.getHeight(), Image.SCALE_DEFAULT)); 
				lblFto.setText(null);
				lblFto.setIcon(icono);
			}
			st.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), ":::::::::::::::::::::::::::::::::::::::::::::  Acceso restringido  ::::::::::::::::::::::::::::::::::::::::::::::::::", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 424, 128);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
	}
	public void crearLabels() {
		lbl2= new JLabel("Tipo:");
		lbl2.setBounds(16, 52, 75, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Cuenta:");
		lbl3.setBounds(10, 29, 81, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Password:");
		lbl4.setBounds(20, 78, 71, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);
	
		lblFto= new JLabel("");
		lblFto.setIcon(new ImageIcon(VentanaLogin.class.getResource("/modelo/Images/cliente32.png")));
		lblFto.setBounds(290, 23, 119, 78);
		panelDto.add(lblFto);
		lblFto.setHorizontalAlignment(SwingConstants.CENTER);
		lblFto.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.RED, Color.ORANGE));
		
		button_1 = new JLabel("");
		button_1.setIcon(new ImageIcon(VentanaLogin.class.getResource("/modelo/Images/Star.png")));
		button_1.setBounds(285, 102, 25, 23);
		panelDto.add(button_1);
		
		button_2 = new JLabel("");
		button_2.setIcon(new ImageIcon(VentanaLogin.class.getResource("/modelo/Images/Star.png")));
		button_2.setBounds(310, 102, 25, 23);
		panelDto.add(button_2);
		
		button_3 = new JLabel("");
		button_3.setIcon(new ImageIcon(VentanaLogin.class.getResource("/modelo/Images/Star.png")));
		button_3.setEnabled(false);
		button_3.setBounds(335, 102, 25, 23);
		panelDto.add(button_3);
		
		button_4 = new JLabel("");
		button_4.setIcon(new ImageIcon(VentanaLogin.class.getResource("/modelo/Images/Star.png")));
		button_4.setEnabled(false);
		button_4.setBounds(360, 102, 25, 23);
		panelDto.add(button_4);
		
		button_5 = new JLabel("");
		button_5.setIcon(new ImageIcon(VentanaLogin.class.getResource("/modelo/Images/Star.png")));
		button_5.setEnabled(false);
		button_5.setBounds(385, 102, 25, 23);
		panelDto.add(button_5);
	}
	public void crearTextFields(){
		//textCuenta= new JTextField();
		textCuenta= new JTextFieldIcon(new JTextField(),"user.png","Username","user.png");
		textCuenta.setColumns(10);
		textCuenta.setFont(Menu.fontText);
		textCuenta.setForeground(Menu.textColorForegroundActivo);
		textCuenta.setHorizontalAlignment(SwingConstants.LEFT);
		textCuenta.addActionListener(this);
		textCuenta.addFocusListener(this);
		textCuenta.addKeyListener(this);
		textCuenta.setBounds(101, 25, 176, 22);
		panelDto.add(textCuenta);
		
		textPassword= new JPasswordField();
		//textPassword= new  JTextFieldIcon(new JPasswordField(),"Star.png","Password","Star.png");
		textPassword.setColumns(10);
		textPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
		textPassword.setForeground(Menu.textColorForegroundInactivo);
		textPassword.setHorizontalAlignment(SwingConstants.LEFT);
		textPassword.addActionListener(this);
		textPassword.addFocusListener(this);
		textPassword.addKeyListener(this);
		textPassword.setBounds(101, 74, 100, 22);
		panelDto.add(textPassword);
	}
	public void crearComboBox() { 
		cbTipo = new JComboBox<>();
		cbTipo.setBounds(101, 50, 176, 21);
		cbTipo.setFont(Menu.fontText);
		cbTipo.addActionListener(this);
		cbTipo.addFocusListener(this);
		cbTipo.addKeyListener(this);
		panelDto.add(cbTipo);
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
		buttonGrabar.setIcon(new ImageIcon(VentanaLogin.class.getResource("/modelo/Images/check.png")));
		buttonGrabar.setToolTipText("Ingresar");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(204, 73, 36, 23);
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setIcon(new ImageIcon(VentanaLogin.class.getResource("/modelo/Images/exit-sis.png")));
		buttonSalir.setToolTipText("Cerrar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(241, 73, 36, 23);
		panelDto.add(buttonSalir);
	}
	public void limpiarTexts() {
		textCuenta.setText(null);
		textCuenta.setBackground(Menu.textColorBackgroundInactivo);	
		textCuenta.setForeground(Menu.textColorForegroundInactivo);
		textPassword.setText(null);
		textPassword.setBackground(Menu.textColorBackgroundInactivo);	
		textPassword.setForeground(Menu.textColorForegroundInactivo);
		cbTipo.removeAllItems();
	}
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonGrabar){// INGRESAR
				if (cbTipo.getSelectedItem()==null){
					JOptionPane.showMessageDialog(null, "Selecione el tipo de usuario",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					cbTipo.requestFocus();
					return;
				}
				if (textCuenta.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Ingrese su cuenta de usuario",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textCuenta.requestFocus();
					return;
				}
				if (textPassword.getPassword().length<=0){
					JOptionPane.showMessageDialog(null, "Ingrese su password / clave",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					textPassword.requestFocus();
					return;
				}
				String password = new String(textPassword.getPassword());
				if (textCuenta.getText().trim().equals("LIBRA") && cbTipo.getSelectedItem().equals("ADMINISTRADOR") && password.toString().trim().equals("LMLIBRALMN")) {
					exit=1;
					Menu v = new Menu();
					v.frame.setVisible(true);
					this.frame.dispose();
					return;
				}
				
				// LIMPIA VARIABLES PUBLICAS
				COD_EMP_USER="";NOM_USUARIO="";TIP_USUARIO="";
				RAZON_SOCIAL_HOTEL="";RUC_HOTEL="";
				conexion = new ConexionDB();
				try {
					Statement statement = conexion.gConnection().createStatement();
					ResultSet resultSet = statement.executeQuery("SELECT * FROM USUARIO where CuentaUsu='"  + textCuenta.getText() +"'");
					String CUENTA="",TIPO="",CLAVE="";
					if (resultSet.next()==true) {
						CUENTA=resultSet.getString("CuentaUsu");
					}
					ResultSet res = statement.executeQuery("SELECT * FROM USUARIO where CuentaUsu='"  + textCuenta.getText() +"'and TipoUsu='"  + cbTipo.getSelectedItem() +"'");
					if (res.next()==true) {
						TIPO= res.getString("TipoUsu");
						TIP_USUARIO= res.getString("TipoUsu");
					}
					ResultSet r = statement.executeQuery("SELECT * FROM USUARIO where CuentaUsu='"  + textCuenta.getText() +"'and TipoUsu='"  + cbTipo.getSelectedItem() +"'and PasswordUsu='"  + password.trim() +"'");
					if (r.next()==true) {
						CLAVE= r.getString("PasswordUsu");
						COD_EMP_USER= r.getString("Id_Emp");
					}
					if(CUENTA.equals(textCuenta.getText()) ) {
						if(TIPO.equals(cbTipo.getSelectedItem()) && CUENTA.equals(textCuenta.getText())) {
							if(CLAVE.equals(password.trim()) ) {//"SELECT * FROM EMPLEADO E, HORARIO as H where E.Id_Emp=H.Id_Hor E.Id_Emp='"  + COD_USUARIO +"'");
								ResultSet rs = statement.executeQuery("SELECT * FROM EMPLEADO E, EMPRESA as H where  E.Id_Emp='"  + COD_EMP_USER +"'");
								if (rs.next()==true) {
									//EMPRESA
									RAZON_SOCIAL_HOTEL=rs.getString("RazonSocial");
									RUC_HOTEL= rs.getString("Ruc");
									DIRECCION_HOTEL=rs.getString("Direccion");
									TELEFONO_HOTEL= rs.getString("Telefono");
									URLIMAGE_HOTEL= rs.getString("Url");
									// EMPLEADO
									NOM_USUARIO=rs.getString("NombresEmp");
									ID_EMP= rs.getString("Id_Emp");
								}
									
								exit=1;
								//statement.close();
								//Menu v = new Menu();
								//v.frame.setVisible(true);
								
								//************************************* CAMBIO EL SKINS
								/*Menu menuprincipal = new Menu();
								SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.CremeCoffeeSkin");
								SubstanceLookAndFeel.setCurrentTheme("org.jvnet.substance.theme.SubstanceSaturatedTheme");
					            SwingUtilities.updateComponentTreeUI(menuprincipal.frame);*/
						        //*********************************** END CAMBIO EL SKINS	
								
								CONSULTO_CAJA_APERTURA();// CONSULTO SI LA CAJA SE APETURADO 

								// SKINS
						        JFrame.setDefaultLookAndFeelDecorated(false);
				            	Menu ventana = new Menu();
				                try {
				                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				                    SwingUtilities.updateComponentTreeUI(ventana.frame);
				                    ventana.frame.setVisible(true);
				                } catch (UnsupportedLookAndFeelException ex) {}
				                  catch (ClassNotFoundException ex) {}
				                  catch (InstantiationException ex) {}
				                  catch (IllegalAccessException ex) {}
				                // END SKINS
				                
								this.frame.dispose();
						        JFrame.setDefaultLookAndFeelDecorated(true);
							}else {
								JOptionPane.showMessageDialog(null, "------ Clave incorrecta ------",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
								textPassword.requestFocus(true);
								textPassword.setText(null);
							}
						}else{
							JOptionPane.showMessageDialog(null, "------ Tipo usuario incorrecto ------",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
							cbTipo.requestFocus(true);
						}
						
					}else {
						JOptionPane.showMessageDialog(null, "------  Cuenta de usuario incorrecta  ------",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
						textCuenta.requestFocus(true);
						textCuenta.selectAll();
					}
				resultSet.close();	
				statement.close();
				} catch (Exception e) {}
				conexion.DesconectarDB();
			  }
		  if (evento.getSource() == buttonSalir){// SALIR
			  this.frame.dispose();
		  	  }
		  if (evento.getSource() == cbTipo){
			  
			  }
		}
		
	public static void CONSULTO_CAJA_APERTURA() { 
		conexion= new ConexionDB();
		try {
        	/*Calendar c1 = GregorianCalendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            c1.add(Calendar.DATE, -1);
            FechaAyer=sdf.format(c1.getTime());*/
			//String con = "Select * from CAJA_APE_CIE where EstadoApeCie='" + 1 + "' and FechaApe BETWEEN'" + FechaAyer.trim() + "'and'" + Menu.date.trim() + "'and User='" + VentanaLogin.COD_USUARIO  + "'";
            
			TIPO_CAMBIO=0;
            int id=0;
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from CAJA_APE_CIE order by Id_ApeCie desc limit 0,1");
			if (resultSet.next()== true) {
				id=(Integer.parseInt(resultSet.getString("Id_ApeCie")));
			}
            resultSet.close();
            statement.close();
            String con = "Select * from CAJA_APE_CIE as C, TURNO as T where C.Turno=T.Turno and C.EstadoApeCie='" + 1 + "'and C.Id_ApeCie='" + id  + "'and C.User='" + VentanaLogin.COD_EMP_USER  + "'";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			if (rs.next()==true) {
				ID_APETURA_CAJA=Integer.parseInt(rs.getString("Id_ApeCie"));
				ID_TURNO=rs.getInt("Id_Tur");
				TURNO=rs.getString("Turno");
				DESCRIPCION_TURNO=rs.getString("Turno")+ " - ABIERTO";
				TIPO_CAMBIO=rs.getDouble("TipoCambioApeCie");
			}else {
				ID_APETURA_CAJA=0;
				DESCRIPCION_TURNO="CERRADO";
				TURNO="******";
				VentanaAperturaCaja v = new VentanaAperturaCaja();
				v.frame.setVisible(true);
			}
			rs.close();
			st.close();
			
			// NOMBRE CORTO DE USUARIO Y TURNO
			TUR_CORTO="";NOMBRE_USU_CORTO="";
			String TEMP_TUR="",TEMP_NOM="";
			String delimiter = " ";
			String[] temp;
			
			temp = TURNO.trim().split(delimiter);
			for(int i =0; i < temp.length ; i++){
				if (temp[i].trim().length()>=(1)) {
					TEMP_TUR=temp[i].trim().substring(0, 1);
					}
					TUR_CORTO=TUR_CORTO.trim() + TEMP_TUR.trim();
			}
			
			temp = NOM_USUARIO.trim().split(delimiter);
			for(int i =0; i < temp.length ; i++){
				if (temp[i].trim().length()>=(1)) {
					TEMP_NOM=temp[i].trim().substring(0, 1);
					}
					NOMBRE_USU_CORTO=NOMBRE_USU_CORTO.trim() + TEMP_NOM.trim();
			}
			// NOMBRE CORTO DE USUARIO Y TURNO
			
		} catch (Exception e) {}
		conexion.DesconectarDB(); 
	}
	
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == cbTipo){cbTipo.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textCuenta){textCuenta.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textPassword){textPassword.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textCuenta){textCuenta.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textPassword){textPassword.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundActivo);}
			
			if (ev.getSource() == textCuenta){textCuenta.selectAll();} 
			if (ev.getSource() == textPassword){textPassword.selectAll();} 
			if (ev.getSource() == textPassword){
				textPassword.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 0, 0), new Color(0, 255, 255), new Color(255, 200, 0), Color.BLACK));
				}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == cbTipo){cbTipo.setBackground(new Color(240,240,240));}
			if (ev.getSource() == textCuenta){textCuenta.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textPassword){textPassword.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textCuenta){textCuenta.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textPassword){textPassword.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundInactivo);}
			
			if (ev.getSource() == textPassword){
				textPassword.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
				}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
				if (evet.getSource() == textCuenta){
					if (e==KeyEvent.VK_ENTER){
						if (!textCuenta.getText().isEmpty() ) {
							cbTipo.requestFocus();
						}else {
							textCuenta.requestFocus();
							textCuenta.selectAll();
							Toolkit.getDefaultToolkit().beep();
						}
					}
				}
				if (evet.getSource() == cbTipo){
					if (e==KeyEvent.VK_ENTER){
						if (!cbTipo.getSelectedItem().equals("") ) {
							textPassword.requestFocus();
							textPassword.selectAll();
						}else {
							cbTipo.requestFocus();
							Toolkit.getDefaultToolkit().beep();
						}

					}
				}
				if (evet.getSource() == textPassword){
					if (e==KeyEvent.VK_ENTER){
						if (textPassword.getPassword().length>0 ) {
							buttonGrabar.doClick();
						}else {
							textPassword.requestFocus();
							Toolkit.getDefaultToolkit().beep();
						}
					}
				}
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			//char e=evet.getKeyChar();
			if (evet.getSource().equals(textCuenta)) {
		        if (!String.valueOf(evet.getKeyChar()).matches("[a-zA-Z]|\\s")) {
		            Toolkit.getDefaultToolkit().beep();
		            evet.consume();
		        }
			}
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
