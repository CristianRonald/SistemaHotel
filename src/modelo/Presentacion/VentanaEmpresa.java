package modelo.Presentacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
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
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;

public class VentanaEmpresa implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JDialog frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl7,lbl8,lbl9,lbl10,lblFto;
	private JButton  			buttonGrabar,buttonSalir,buttonImg,buttonBus;
	protected static JDateChooser dateChooserAlta,dateChooserActualizar;
	protected static String dateAlta,dateActualizar;
	protected JTextField 			textRuc,textRazonSocial,textDireccion,textEmail,textTelefono,textWeb,textPropietario;
	
	private JScrollPane scrollArea;
	protected static JTextArea textArea = new JTextArea();
	private JSpinner spinner;
	
	public File fichero;
	private String almacenaFoto;
	private AbrirFoto archivo;
	String cadenaDescripcion="";
	
	public static int MOD;
	
	NumberFormat formatDsct;

	// constructor
	public VentanaEmpresa() {
		frameEmpresa();
		crearPanel();
		crearButtons();
		crearTextFields();
		crearLabels();
	
		
			llenarModificar();
		
		
	}
	public void frameEmpresa() {
		frame = new JDialog();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaEmpresa.class.getResource("/modelo/Images/empresa.png")));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frame.setTitle("Datos de la empresa                                                                        ");

		frame.setBounds(100, 100, 511, 330);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(5, 5, 487, 284);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		dateChooserAlta = new JDateChooser();
		dateChooserAlta.setBounds(105, 202, 95, 20);
		panelDto.add(dateChooserAlta);
		dateChooserAlta.setDate(Menu.fecha);
	}
	public void crearLabels() {
		lbl1= new JLabel("Ruc:");
		lbl1.setBounds(32, 28, 63, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Direcci\u00F3n:");
		lbl2.setBounds(20, 79, 75, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Tel\u00E9fono:");
		lbl3.setBounds(12, 107, 81, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("E-mail:");
		lbl4.setBounds(36, 134, 57, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);

		lbl5= new JLabel("Web:");
		lbl5.setBounds(36, 159, 57, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);

		lbl7= new JLabel("Raz\u00F3n social:");
		lbl7.setBounds(10, 55, 87, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Propietario:");
		lbl8.setBounds(20, 181, 73, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		lbl9 = new JLabel("Giro de negocio:");
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl9.setBounds(12, 236, 81, 14);
		panelDto.add(lbl9);
		
		lbl10 = new JLabel("Fecha formo:");
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lbl10.setBounds(20, 206, 75, 14);
		panelDto.add(lbl10);
		
		lblFto= new JLabel("Image");
		lblFto.setBounds(321, 100, 144, 101);
		panelDto.add(lblFto);
		lblFto.setHorizontalAlignment(SwingConstants.CENTER);
		lblFto.setBorder(BorderFactory.createEtchedBorder());
		
		spinner = new JSpinner();
		spinner.setToolTipText("Categoria del hotel \r\nelija # de estrellas");
		spinner.setModel(new SpinnerNumberModel(new Integer(2), null, null, new Integer(1)));
		spinner.setBounds(225, 25, 36, 20);
		panelDto.add(spinner);
	}
	public void crearTextFields(){
		
		textRuc = new JTextField();
		textRuc.setBackground(SystemColor.activeCaption);
		textRuc.setColumns(10);
		textRuc.setFont(new Font("Tahoma", Font.BOLD, 12));
		textRuc.setForeground(Color.YELLOW);
		textRuc.setHorizontalAlignment(SwingConstants.CENTER);
		textRuc.addActionListener(this);
		textRuc.addFocusListener(this);
		textRuc.addKeyListener(this);
		textRuc.setBounds(105, 24, 115, 22);
		panelDto.add(textRuc);
		
		textRazonSocial= new JTextField();
		textRazonSocial.setColumns(10);
		textRazonSocial.setFont(Menu.fontText);
		textRazonSocial.setForeground(Menu.textColorForegroundInactivo);
		textRazonSocial.setHorizontalAlignment(SwingConstants.LEFT);
		textRazonSocial.addActionListener(this);
		textRazonSocial.addFocusListener(this);
		textRazonSocial.addKeyListener(this);
		textRazonSocial.setBounds(105, 51, 360, 22);
		panelDto.add(textRazonSocial);
		
		textDireccion= new JTextField();
		textDireccion.setColumns(10);
		textDireccion.setFont(Menu.fontText);
		textDireccion.setForeground(Menu.textColorForegroundInactivo);
		textDireccion.setHorizontalAlignment(SwingConstants.LEFT);
		textDireccion.addActionListener(this);
		textDireccion.addFocusListener(this);
		textDireccion.addKeyListener(this);
		textDireccion.setBounds(105, 76, 360, 22);
		panelDto.add(textDireccion);

		scrollArea= new JScrollPane();
		scrollArea.setBounds(105, 226, 360, 51);
		panelDto.add(scrollArea);
		textArea = new JTextArea();
		scrollArea.setViewportView(textArea);
		textArea.setFont(Menu.fontText);
		textArea.setForeground(Menu.textColorForegroundInactivo);
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);

		textEmail = new JTextField();
		textEmail.setBackground(new Color(255, 255, 255));
		textEmail.setText("");
		textEmail.setHorizontalAlignment(SwingConstants.LEFT);
		textEmail.setBounds(105, 126, 210, 21);
		textEmail.addActionListener(this);
		textEmail.addFocusListener(this);
		textEmail.addKeyListener(this);
		panelDto.add(textEmail);
		
		textTelefono = new JTextField();
		textTelefono.setBackground(new Color(255, 255, 255));
		textTelefono.setText("");
		textTelefono.setHorizontalAlignment(SwingConstants.LEFT);
		textTelefono.setBounds(105, 102, 210, 21);
		textTelefono.addActionListener(this);
		textTelefono.addFocusListener(this);
		textTelefono.addKeyListener(this);
		panelDto.add(textTelefono);
		
		textWeb = new JTextField();
		textWeb.setText("");
		textWeb.setHorizontalAlignment(SwingConstants.LEFT);
		textWeb.setBounds(105, 151, 210, 21);
		textWeb.addActionListener(this);
		textWeb.addPropertyChangeListener(this);
		textWeb.addKeyListener(this);
		textWeb.addFocusListener(this);
		panelDto.add(textWeb);
		
		textPropietario = new JTextField();
		textPropietario.setText("");
		textPropietario.setHorizontalAlignment(SwingConstants.LEFT);
		textPropietario.setBounds(105, 176, 210, 21);
		textPropietario.addActionListener(this);
		textPropietario.addPropertyChangeListener(this);
		textPropietario.addKeyListener(this);
		textPropietario.addFocusListener(this);
		panelDto.add(textPropietario);

	}
	public void llenarModificar() { 
		conexion= new ConexionDB();
		MOD=0;
		try {
			String con = "Select * from EMPRESA";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			
	        almacenaFoto="";
	        lblFto.setText("Sin image");
	        lblFto.setIcon(null);
			if (rs.next()==true) {
				MOD=1;
		        
		        textRuc.setText((String)(rs.getString("Ruc")));
				textRazonSocial.setText(rs.getString("RazonSocial"));
				textDireccion.setText(rs.getString("Direccion"));
				textTelefono.setText(rs.getString("Telefono"));
				textEmail.setText(rs.getString("Email"));
				textWeb.setText(rs.getString("Web"));
				textPropietario.setText(rs.getString("Propietario"));
				textArea.setText(rs.getString("GiroNegocio"));
				//.setText(rs.getString("FechaCreo"));
				spinner.setValue(Integer.parseInt(rs.getString("Categoria")));
				almacenaFoto=rs.getString("URL");
				
				// CARGO LA IMAGEN
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
	}
	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(242, 199, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaEmpresa.class.getResource("/modelo/Images/save.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Cerrar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(279, 199, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaEmpresa.class.getResource("/modelo/Images/Exit.png")));
		panelDto.add(buttonSalir);

		buttonImg= new JButton("Adjuntar logo");
		buttonImg.setHorizontalAlignment(SwingConstants.LEFT);
		buttonImg.setToolTipText("Agregar imagen");
		buttonImg.addActionListener(this);
		buttonImg.setBounds(321, 199, 144, 23);
		buttonImg.setIcon(new ImageIcon(VentanaEmpresa.class.getResource("/modelo/Images/blue.png")));
		panelDto.add(buttonImg);
		
		buttonBus= new JButton("");
		buttonBus.setBounds(203, 200, 36, 22);
		buttonBus.setToolTipText("Ver saldos");
		buttonBus.addActionListener(this);
		buttonBus.setIcon(new ImageIcon(VentanaEmpresa.class.getResource("/modelo/Images/search.png")));
		panelDto.add(buttonBus);
	}
	
	public void limpiarTexts() {
		textRuc.setText(null);
		textRuc.setBackground(Menu.textColorBackgroundInactivo);	
		textRuc.setForeground(Menu.textColorForegroundInactivo);
		textRuc.requestFocus(true);
		
		textEmail.setText(null);
		textEmail.setBackground(Menu.textColorBackgroundInactivo);	
		textEmail.setForeground(Menu.textColorForegroundInactivo);
		
		textTelefono.setText(null);
		textTelefono.setBackground(Menu.textColorBackgroundInactivo);	
		textTelefono.setForeground(Menu.textColorForegroundInactivo);
		
		textRazonSocial.setText(null);
		textRazonSocial.setBackground(Menu.textColorBackgroundInactivo);	
		textRazonSocial.setForeground(Menu.textColorForegroundInactivo);
		
		textDireccion.setText(null);
		textDireccion.setBackground(Menu.textColorBackgroundInactivo);	
		textDireccion.setForeground(Menu.textColorForegroundInactivo);
		
		textWeb.setText(null);
		textWeb.setBackground(Menu.textColorBackgroundInactivo);	
		textWeb.setForeground(Menu.textColorForegroundInactivo);
		
		textPropietario.setText(null);
		textPropietario.setBackground(Menu.textColorBackgroundInactivo);	
		textPropietario.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
		
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	
	public void activarTexts(boolean b) {
		textRuc.setEnabled(b);
		textRazonSocial.setEnabled(b);
		textDireccion.setEnabled(b);
		textEmail.setEnabled(b);
		textTelefono.setEnabled(b);
		textWeb.setEnabled(b);
		textPropietario.setEnabled(b);
		textArea.setEnabled(b);
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
			if (textRuc.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textRuc.requestFocus();
				return;
			}
			if (textRazonSocial.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textRazonSocial.requestFocus();
				return;
			}
			if (textDireccion.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDireccion.requestFocus();
				return;
			}
			if (textTelefono.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textTelefono.requestFocus();
				return;
			}
			if (textEmail.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textEmail.requestFocus();
				return;
			}
			if (textWeb.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textWeb.requestFocus();
				return;
			}
			if (textPropietario.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textPropietario.requestFocus();
				return;
			}
			if (dateChooserAlta.getDate()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				dateChooserAlta.requestFocus();
				return;
			}
			
			conexion = new ConexionDB();
			if (MOD==0) {// REGISTRAR
				try {
					FileInputStream  archivofoto;
					String sql ="INSERT INTO  EMPRESA (Ruc,RazonSocial,Direccion,Telefono,Email,Web,Propietario,GiroNegocio,FechaCreo,Categoria,Estado,Logo,Url) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setString(1, textRuc.getText());
					ps.setString(2,textRazonSocial.getText().trim());
					ps.setString(3,textDireccion.getText().trim());
					ps.setString(4, textTelefono.getText().trim());;
					ps.setString(5, textEmail.getText().trim());
					ps.setString(6, textWeb.getText().trim());
					ps.setString(7, textPropietario.getText().trim());
					
					ps.setNString(8, textArea.getText().trim());
					ps.setNString(9, (Menu.date.trim()));// FALTA LA FECHA DEL CHOOSER
					ps.setNString(10,spinner.getValue().toString());
					ps.setString(11,"1");
					
					ps.setString(13, almacenaFoto);
					// VERIFICA SI SE GUARDARA UNA IMAGEN
					if ((almacenaFoto==null)|| (almacenaFoto=="")){
						almacenaFoto="sin image";
						ps.setString(12,almacenaFoto);
					}else {
						archivofoto = new FileInputStream(almacenaFoto);
						ps.setBinaryStream(12,archivofoto);
					}
					
					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			if (MOD==1) { // MODIFICAR 
				try {
					FileInputStream  archivofoto;
			         String sql="UPDATE EMPRESA SET Ruc = ?,"
			                 + "RazonSocial =?,"
			                 + "Direccion = ?,"
			                 + "Telefono = ?,"
			                 + "Email = ?,"
			                 + "Web =?,"
			                 + "Propietario = ?,"
			                 + "GiroNegocio=?,"
			                 + "FechaCreo =?,"
			                 + "Categoria =?,"
			                 + "Estado =?,"
			                 + "Logo =?,"
			                 + "Url=?";
			                 //+ "WHERE Ruc = '"+ textCod.getText() + "'"; 
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setString(1, textRuc.getText());
					ps.setString(2,textRazonSocial.getText().trim());
					ps.setString(3,textDireccion.getText().trim());
					ps.setString(4, textTelefono.getText().trim());;
					ps.setString(5, textEmail.getText().trim());
					ps.setString(6, textWeb.getText().trim());
					ps.setString(7, textPropietario.getText().trim());
					
					ps.setNString(8, textArea.getText().trim());
					ps.setNString(9, (Menu.date.trim()));// FALTA LA FECHA DEL CHOOSER
					ps.setNString(10,spinner.getValue().toString());
					ps.setString(11,"1");
					
					ps.setString(13, almacenaFoto);
					// VERIFICA SI SE GUARDARA UNA IMAGEN
					if ((almacenaFoto==null)|| (almacenaFoto=="")){
						almacenaFoto="sin image";
						ps.setString(12,almacenaFoto);
					}else {
						archivofoto = new FileInputStream(almacenaFoto);
						ps.setBinaryStream(12,archivofoto);
					}
					
					
					// DOY FORMATO A LA FECHA
					//SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
					//dateActualizar = form.format(dateChooserAlta.getDate());
					//ps.setNString(5, (dateActualizar + " " + Menu.HORA).trim());
					
					/*ps.setString(13, almacenaFoto);
					
					// VERIFICA SI SE GUARDARA UNA IMAGEN
					if ((almacenaFoto==null)|| (almacenaFoto=="")){
						almacenaFoto="sin image";
						ps.setString(12,almacenaFoto);
					}else {
						archivofoto = new FileInputStream(almacenaFoto);
						ps.setBinaryStream(12,archivofoto);
					}*/
					
					ps.executeUpdate();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			limpiarTexts();
			activarButton(true);
			frame.dispose();
		}
		
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == textRazonSocial){textRazonSocial.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textDireccion){textDireccion.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textEmail){textEmail.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textTelefono){textTelefono.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textWeb){textWeb.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textPropietario){textPropietario.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textEmail){textEmail.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textTelefono){textTelefono.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textRazonSocial){textRazonSocial.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textDireccion){textDireccion.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textWeb){textWeb.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textPropietario){textPropietario.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
			
			if (ev.getSource() == textRuc){textRuc.selectAll();}
			if (ev.getSource() == textRazonSocial){textRazonSocial.selectAll();}
			if (ev.getSource() == textDireccion){textDireccion.selectAll();}
			if (ev.getSource() == textTelefono){textTelefono.selectAll();}
			if (ev.getSource() == textEmail){textEmail.selectAll();}
			if (ev.getSource() == textWeb){textWeb.selectAll();}
			if (ev.getSource() == textPropietario){textPropietario.selectAll();}
			if (ev.getSource() == textArea){textArea.selectAll();}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textRazonSocial){textRazonSocial.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textDireccion){textDireccion.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textEmail){textEmail.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textTelefono){textTelefono.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textWeb){textWeb.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textPropietario){textPropietario.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textEmail){textEmail.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textTelefono){textTelefono.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textRazonSocial){textRazonSocial.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textDireccion){textDireccion.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textWeb){textWeb.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textPropietario){textPropietario.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			if (evet.getSource() == textRuc){
				if (textRuc.getText().toLowerCase().isEmpty()|| textRuc.getText().toLowerCase().length()>11){
					textRuc.requestFocus();
					textRuc.selectAll();
					textRuc.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textRuc.getText().toLowerCase().length()==11){
						textRazonSocial.requestFocus();
						textRazonSocial.selectAll();	
					}
			} 
			if (evet.getSource() == textRazonSocial){
				int pos = textRazonSocial.getCaretPosition();textRazonSocial.setText(textRazonSocial.getText().toUpperCase());textRazonSocial.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				cadenaDescripcion=textRazonSocial.getText() ;
				if (textRazonSocial.getText().toLowerCase().isEmpty()|| textRazonSocial.getText().toLowerCase().length()>98){
					textRazonSocial.requestFocus();
					textRazonSocial.selectAll();
					textRazonSocial.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textRazonSocial.getText().toLowerCase().length()==98){
						textDireccion.requestFocus();
						textDireccion.selectAll();	
					}
			} 
			if (evet.getSource() == textDireccion){
				int pos = textDireccion.getCaretPosition();textDireccion.setText(textDireccion.getText().toUpperCase());textDireccion.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				cadenaDescripcion=textDireccion.getText() ;
				if (textDireccion.getText().toLowerCase().isEmpty()|| textDireccion.getText().toLowerCase().length()>198){
					textDireccion.requestFocus();
					textDireccion.selectAll();
					textDireccion.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textDireccion.getText().toLowerCase().length()==198){
						textTelefono.requestFocus();
						textTelefono.selectAll();	
					}
			} 
			if (evet.getSource() == textTelefono){
				int pos = textTelefono.getCaretPosition();textTelefono.setText(textTelefono.getText().toUpperCase());textTelefono.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				cadenaDescripcion=textTelefono.getText() ;
				if (textTelefono.getText().toLowerCase().isEmpty()|| textTelefono.getText().toLowerCase().length()>29){
					textTelefono.requestFocus();
					textTelefono.selectAll();
					textTelefono.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textTelefono.getText().toLowerCase().length()==29){
						textEmail.requestFocus();
						textEmail.selectAll();	
					}
			} 
			if (evet.getSource() == textEmail){
				int pos = textEmail.getCaretPosition();textEmail.setText(textEmail.getText().toUpperCase());textEmail.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				cadenaDescripcion=textEmail.getText() ;
				if (textEmail.getText().toLowerCase().isEmpty()|| textEmail.getText().toLowerCase().length()>98){
					textEmail.requestFocus();
					textEmail.selectAll();
					textEmail.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textEmail.getText().toLowerCase().length()==98){
						textWeb.requestFocus();
						textWeb.selectAll();	
					}
			} 
			if (evet.getSource() == textWeb){
				int pos = textWeb.getCaretPosition();textWeb.setText(textWeb.getText().toUpperCase());textWeb.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				cadenaDescripcion=textWeb.getText() ;
				if (textWeb.getText().toLowerCase().isEmpty()|| textWeb.getText().toLowerCase().length()>98){
					textWeb.requestFocus();
					textWeb.selectAll();
					textWeb.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textWeb.getText().toLowerCase().length()==98){
						textPropietario.requestFocus();
						textPropietario.selectAll();	
					}
			} 
			if (evet.getSource() == textPropietario){
				int pos = textPropietario.getCaretPosition();textPropietario.setText(textPropietario.getText().toUpperCase());textPropietario.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				cadenaDescripcion=textPropietario.getText() ;
				if (textPropietario.getText().toLowerCase().isEmpty()|| textPropietario.getText().toLowerCase().length()>88){
					textPropietario.requestFocus();
					textPropietario.selectAll();
					textPropietario.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textPropietario.getText().toLowerCase().length()==88){
						textArea.requestFocus();
						textArea.selectAll();	
					}
			} 
			
			if (evet.getSource() == textArea){
				int pos = textArea.getCaretPosition();textArea.setText(textArea.getText().toUpperCase());textArea.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				cadenaDescripcion=textArea.getText() ;
				if (textArea.getText().toLowerCase().isEmpty()|| textArea.getText().toLowerCase().length()>78){
					textArea.requestFocus();
					textArea.selectAll();
					textArea.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textArea.getText().toLowerCase().length()==78){
						buttonGrabar.requestFocus();
					}
			} 

		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char car=evet.getKeyChar();
			if (evet.getSource() == textRuc){
				//if(!Character.isDigit(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
				if((car<'0'||car>'9')) evet.consume();
			}
			if (evet.getSource() == textRazonSocial){
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textDireccion){
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textTelefono){
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textEmail){ 
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textWeb){
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textPropietario){
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textArea){
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
		}
		
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		formatDsct =NumberFormat.getNumberInstance();
		formatDsct.setMaximumFractionDigits(3);
		//Object source = e.getSource();
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		
	}
	}
