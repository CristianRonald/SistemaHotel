
package modelo.Presentacion;

import java.awt.Color;
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
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import modelo.Clases.AbstractJasperReports;
import modelo.Datos.ConexionDB;

public class VentanaCierreCaja implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JDialog frame;
	private JPanel  panelDto,panelDto2;
	private JLabel				lbl3,lbl4,lbl6,lbl7,lbl8,lbl10,lbl11,lblHraApe,lbl13,lblmons,lblmond,lblfecha,lblTurno,lblOperador;
	private JButton  			buttonGrabar,buttonSalir,buttonPrint;
	private JDateChooser dateChooserSalida;
	private String FechaSalida="";
	private JTextField 			textCod;
	protected  JFormattedTextField 	textMontoDol,textMontoSol;
	
	public static int MOD;
	private Date dateApetura;
	// constructor
	public VentanaCierreCaja() {
		frameCierreCaja();
		crearPanel();
		crearButtons();
		crearLabels();
		crearTextFields();
		
		dateChooserSalida.setDate(Menu.fecha);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		FechaSalida = form.format(dateChooserSalida.getDate());
		
		llenarModificar();
		
		//KeyStroke controlA = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK);
		
	    //MyAction action = new MyAction();
	    //buttonSalir.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
	    //KeyStroke.getKeyStroke("F2"), action.getValue(Action.NAME));
	    
	}
	@SuppressWarnings("serial")
	class MyAction extends AbstractAction {
		  public MyAction() {
		    super("my action");
		  }
		  public void actionPerformed(ActionEvent e) {
		    System.out.println("hi");
		  }
		}
	public void frameCierreCaja() {
		frame = new JDialog();
		frame.setTitle("Cierre de turno");//Cierre de caja por turno
		frame.setBounds(100, 100, 330, 301);
		frame.getContentPane().setLayout(null);
		frame.setModal(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaCierreCaja.class.getResource("/modelo/Images/clock_stop16.png")));
	}
	public void crearPanel() {
		panelDto = new JPanel();
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos de apertura", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 302, 109);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);

		panelDto2 = new JPanel();
		panelDto2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos de cierre", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(210, 105, 30)));
		panelDto2.setBounds(10, 124, 302, 140);
		frame.getContentPane().add(panelDto2);
		panelDto2.setLayout(null);
	}
	public void crearLabels() {
		lbl3= new JLabel("Monto Apertura S/.:");
		lbl3.setBounds(14, 66, 97, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Monto Apertura  $.:");
		lbl4.setBounds(14, 86, 97, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);

		lblHraApe= new JLabel("00:00:00:PM");
		lblHraApe.setForeground(new Color(0, 0, 128));
		lblHraApe.setBounds(194, 26, 83, 14);
		panelDto.add(lblHraApe);
		lblHraApe.setHorizontalAlignment(SwingConstants.LEFT);
		lblHraApe.setFont(Menu.fontLabel);
		
		lbl7= new JLabel("Fecha de apertura:");
		lbl7.setBounds(10, 26, 101, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
	
		lbl6= new JLabel("Turno:");
		lbl6.setBounds(14, 46, 97, 14);
		panelDto.add(lbl6);
		lbl6.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl6.setFont(Menu.fontLabel);
		
		lblTurno= new JLabel("::::::::");
		lblTurno.setBounds(115, 46, 74, 14);
		lblTurno.setForeground(new Color(0, 0, 128));
		panelDto.add(lblTurno);
		lblTurno.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTurno.setFont(Menu.fontLabel);
		
		lblmons= new JLabel("::::::::");
		lblmons.setForeground(new Color(0, 0, 128));
		lblmons.setBounds(115, 66, 74, 14);
		panelDto.add(lblmons);
		lblmons.setHorizontalAlignment(SwingConstants.RIGHT);
		lblmons.setFont(Menu.fontLabel);
		
		lblmond= new JLabel("::::::::");
		lblmond.setForeground(new Color(0, 0, 139));
		lblmond.setBounds(115, 86, 74, 14);
		panelDto.add(lblmond);
		lblmond.setHorizontalAlignment(SwingConstants.RIGHT);
		lblmond.setFont(Menu.fontLabel);
		
		lblfecha= new JLabel("::::::::");
		lblfecha.setForeground(new Color(0, 0, 128));
		lblfecha.setBounds(115, 26, 74, 14);
		panelDto.add(lblfecha);
		lblfecha.setHorizontalAlignment(SwingConstants.RIGHT);
		lblfecha.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Turno Nro:");
		lbl8.setBounds(32, 35, 63, 14);
		panelDto2.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		lbl10= new JLabel("Monto S/.:");
		lbl10.setBounds(12, 91, 81, 14);
		panelDto2.add(lbl10);
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(Menu.fontLabel);
		
		lbl11= new JLabel("Monto $.:");
		lbl11.setBounds(36, 113, 57, 14);
		panelDto2.add(lbl11);
		lbl11.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl11.setFont(Menu.fontLabel);

		lbl13= new JLabel("Fecha de cierre:");
		lbl13.setBounds(8, 62, 87, 14);
		panelDto2.add(lbl13);
		lbl13.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl13.setFont(Menu.fontLabel);
		
		lblOperador= new JLabel("::::::::::::::::::::");
		lblOperador.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblOperador.setForeground(new Color(0, 0, 128));
		lblOperador.setBounds(194, 46, 100, 14);
		panelDto.add(lblOperador);
		lblOperador.setHorizontalAlignment(SwingConstants.LEFT);
		lbl4.setFont(Menu.fontLabel);
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
		textCod.setBounds(105, 31, 75, 22);
		panelDto2.add(textCod);

		textMontoDol = new JFormattedTextField();
		textMontoDol.setForeground(Color.BLUE);
		textMontoDol.setFont(new Font("Tahoma", Font.BOLD, 12));
		textMontoDol.setBackground(new Color(255, 255, 255));
		textMontoDol.setText("0.00");
		textMontoDol.setHorizontalAlignment(SwingConstants.RIGHT);
		textMontoDol.setBounds(103, 110, 93, 22);
		textMontoDol.addFocusListener(this);
		textMontoDol.addKeyListener(this);
		panelDto2.add(textMontoDol);
		
		textMontoSol = new JFormattedTextField();
		textMontoSol.setForeground(Color.BLUE);
		textMontoSol.setFont(new Font("Tahoma", Font.BOLD, 12));
		textMontoSol.setBackground(new Color(255, 255, 255));
		textMontoSol.setText("0.00");
		textMontoSol.setHorizontalAlignment(SwingConstants.RIGHT);
		textMontoSol.setBounds(103, 84, 93, 22);
		textMontoSol.addFocusListener(this);
		textMontoSol.addKeyListener(this);
		panelDto2.add(textMontoSol);
		
		dateChooserSalida = new JDateChooser();
		dateChooserSalida.setDateFormatString("dd-MMM-yyyy");
		dateChooserSalida.setBorder(new EtchedBorder(EtchedBorder.RAISED, new Color(139, 69, 19), new Color(255, 228, 181)));
		dateChooserSalida.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)dateChooserSalida.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)dateChooserSalida.getDateEditor()).setEditable(false);
		dateChooserSalida.setBounds(105, 58, 93, 23);
		dateChooserSalida.addPropertyChangeListener(this);
		panelDto2.add(dateChooserSalida);
	}
	public void llenarModificar() { 
		conexion= new ConexionDB();
		try {
			
			int ESTADO=0;int id=0;
			//String FECHA_APE="",FECHA_CIE="";
			Statement statement = conexion.gConnection().createStatement();
			//ResultSet resultSet = statement.executeQuery("Select * from CAJA_APE_CIE order by Id_ApeCie desc limit 0,1");
			ResultSet resultSet = statement.executeQuery("Select * from CAJA_APE_CIE where Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'order by Id_ApeCie desc limit 0,1");
			if (resultSet.next()== true) {
				id=(Integer.parseInt(resultSet.getString("Id_ApeCie")));
				ESTADO=Integer.parseInt(resultSet.getString("EstadoApeCie"));
				//FECHA_APE=resultSet.getString("FechaApe");
				//FECHA_CIE=resultSet.getString("FechaCie");
				textCod.setText(Menu.formatid_9.format(id));
				
				lblfecha.setText(Menu.formatoDiaMesAño.format(resultSet.getDate("FechaApe")));
				dateApetura =resultSet.getDate("FechaApe");
				lblTurno.setText(resultSet.getString("turno"));
				lblmons.setText(Menu.formateadorCurrency.format(resultSet.getFloat("MontoSolApe")));
				lblmond.setText(Menu.formateadorCurrency.format(resultSet.getFloat("MontoDolApe")));
				lblHraApe.setText(resultSet.getString("HoraApe"));
				
		    	//CONSULTO TURNO Y USER
				Statement ss = conexion.gConnection().createStatement();
				ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + resultSet.getInt("Id_ApeCie")+"'");
				if (rr.next()== true) {
					String USU="";
					String TEMP_NOM="";
					String delimiter = " ";
					String[] temp;
					temp =  rr.getString("NombresEmp").trim().split(delimiter);
					for(int i =0; i < temp.length ; i++){
						if (temp[i].trim().length()>=(1)) {
							TEMP_NOM=temp[i].trim().substring(0, 1);
							}
							USU=USU.trim() + TEMP_NOM.trim();
					}
					lblOperador.setText("/ OPERADOR: "+ USU);
				}
				rr.close();
				ss.close();
				//END CONSULTO TURNO Y USER
			}

			float APERTURA=0,APER_DOLLAR=0,CAJA=0,COBRO=0,VENTA=0;
			String consultaAperura="",consultaPagos="",consultaVenta="",consultarCaja="";
			if (ESTADO==1) {
				MOD=0;
				/*if (FECHA_APE.trim().equals(FECHA_CIE.trim())||FECHA_APE.trim().equals(Menu.date.trim())) {
					 consultaAperura="Select* from CAJA_APE_CIE where FechaApe ='" + Menu.date + "'and User='" + VentanaLogin.COD_USUARIO  + "'";
					 consultarCaja="Select * from CAJA where FechaCaj ='" + Menu.date.trim() + "'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'";
					 consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta ='" + Menu.date.trim() + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'";
					 consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra ='" + Menu.date.trim() + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
				}
				if (!FECHA_APE.trim().equals(Menu.date.trim())) {
					 consultaAperura="Select* from CAJA_APE_CIE where FechaApe BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and User='" + VentanaLogin.COD_USUARIO  + "'";
					 consultarCaja="Select * from CAJA where FechaCaj BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'";
					 consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and D.FechaDetCta BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'";
					 consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.FechaTra BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date.trim() + "'and IdTurno='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
				}*/
				 consultaAperura="Select* from CAJA_APE_CIE where EstadoApeCie='" + 1 +  "'and Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'";
				 consultarCaja="Select * from CAJA where Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'";
				 consultaPagos="Select * from CUENTA_HUESPED_PAGOS as D, CUENTA_HUESPED as CC where D.Id_Cta=CC.Id_Cta and IdTurnoP='" + VentanaLogin.ID_APETURA_CAJA + "'";
				 consultaVenta="Select * from TRANSACCION as T, CLIENTES as C where T.Id_CliPro=C.Id_Cli and T.TipoOperacionTra='" + "VENTA" + "'and T.EstadoTra='" + 1 + "'and IdTurnoT='" + VentanaLogin.ID_APETURA_CAJA + "'"; 
				 
				Statement s = conexion.gConnection().createStatement();
				ResultSet r = s.executeQuery(consultaAperura);
				while (r.next()==true) {
					APERTURA= APERTURA +r.getFloat("MontoSolApe");
					APER_DOLLAR=r.getFloat("MontoDolApe");
				}

				ResultSet rp = s.executeQuery(consultaPagos);
				while (rp.next()==true) {
					COBRO= COBRO +rp.getFloat("AcuentaDetCta");
				}
				
				ResultSet rv = s.executeQuery(consultaVenta);
				while (rv.next()==true) {
					VENTA= VENTA +rv.getFloat("TotalTra");
				}
	
				ResultSet rc = s.executeQuery(consultarCaja);
				while (rc.next()==true) {
					if (rc.getString("TipoMov").equals("INGRESO")) {
						CAJA=CAJA + rc.getFloat("MontoCaj");
						}
		            if (rc.getString("TipoMov").equals("EGRESO")) {
		            	CAJA= CAJA - rc.getFloat("MontoCaj");
		            	}
				}
				s.close();
				textMontoSol.setText(Menu.formateadorCurrency.format(APERTURA + COBRO +CAJA + VENTA));
				textMontoDol.setText(Menu.formateadorCurrency.format(APER_DOLLAR));
			}
			if (ESTADO==0) {
				textMontoSol.setText(Menu.formateadorCurrency.format(resultSet.getFloat("MontoSolCie")));
				textMontoDol.setText(Menu.formateadorCurrency.format(resultSet.getFloat("MontoDolCie")));
				MOD=1;
				panelDto2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Turno cerrado", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.RED));
			}
			statement.close();
		} catch (Exception e) {}
	}

	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setMnemonic('O');
		buttonGrabar.setToolTipText("Cerrar turno");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(201, 110, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaCierreCaja.class.getResource("/modelo/Images/cajacerrar.png")));
		panelDto2.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(238, 110, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaCierreCaja.class.getResource("/modelo/Images/Exit.png")));
		panelDto2.add(buttonSalir);
		
		buttonPrint= new JButton("");
		buttonPrint.setToolTipText("Vista previa");
		buttonPrint.addActionListener(this);
		buttonPrint.setBounds(201, 85, 36, 23);
		buttonPrint.setIcon(new ImageIcon(VentanaCierreCaja.class.getResource("/modelo/Images/print.png")));
		panelDto2.add(buttonPrint);
	}
	
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonGrabar){// GRABAR
			  insertarUpdate();
			  }
		  if (evento.getSource() == buttonSalir){// SALIR
			  this.frame.dispose();
		  	  }
		  if (evento.getSource() == buttonPrint){// PRINT
			  Imprimir();
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
			if (dateChooserSalida.getDate()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				dateChooserSalida.requestFocus();
				return;
			}
			if (dateChooserSalida.getDate()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				dateChooserSalida.requestFocus();
				return;
			}
			if (textMontoSol.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textMontoSol.requestFocus();
				return;
			}
			if (textMontoDol.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textMontoDol.requestFocus();
				return;
			}

			conexion = new ConexionDB();
			if (MOD==0) {// REGISTRAR
				int respuesta = JOptionPane.showConfirmDialog (null, "�Desea cerrar turno? \n::::::::::"+VentanaLogin.TURNO+ "::::::::::", Menu.SOFTLE_HOTEL,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (respuesta == JOptionPane.YES_OPTION) {
					try {
				        String sql="UPDATE CAJA_APE_CIE SET User = ?,"
				                 + "Turno = ?,"
				                 + "FechaCie = ?,"
				                 + "HoraCie = ?,"
				                 + "MontoSolCie = ?,"
				                 + "MontoDolCie =?,"
				                 + "EstadoApeCie =?"
				                 + "WHERE Id_ApeCie = '"+ textCod.getText().trim() + "'"; 
						
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setString(1, (String)(VentanaLogin.COD_EMP_USER));
						ps.setString(2, lblTurno.getText());
						ps.setString(3, FechaSalida.trim());
						ps.setNString(4, Menu.HORA.trim());
						ps.setFloat(5, Float.parseFloat(textMontoSol.getText().replaceAll(",", "")));
						ps.setFloat(6, Float.parseFloat(textMontoDol.getText().replaceAll(",", "")));
						ps.setString(7,"0");
						ps.executeUpdate();
						ps.close();
						JOptionPane.showMessageDialog(null, "Turno fue cerrado correctamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						frame.dispose();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						}
				}else if (respuesta == JOptionPane.NO_OPTION) {}
			}
			if (MOD==1) { // MODIFICAR 
				try {
					int respuesta = JOptionPane.showConfirmDialog (null, "�Desea modificar el turno cerrado? \n::::::::::::::::::::"+VentanaLogin.TURNO+"::::::::::::::::::::", Menu.SOFTLE_HOTEL,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (respuesta == JOptionPane.YES_OPTION) {
			        String sql="UPDATE CAJA_APE_CIE SET User = ?,"
			                 + "Turno = ?,"
			                 + "FechaCie = ?,"
			                 + "HoraCie = ?,"
			                 + "MontoSolCie = ?,"
			                 + "MontoDolCie =?,"
			                 + "EstadoApeCie =?"
			                 + "WHERE Id_ApeCie = '"+ textCod.getText().trim() + "'"; 
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setString(1, (String)(VentanaLogin.COD_EMP_USER));
					ps.setString(2, lblTurno.getText());
					ps.setString(3, FechaSalida.trim());
					ps.setNString(4, Menu.HORA.trim());
					ps.setFloat(5, Float.parseFloat(textMontoSol.getText().replaceAll(",", "")));
					ps.setFloat(6, Float.parseFloat(textMontoDol.getText().replaceAll(",", "")));
					ps.setString(7,"0");
					ps.executeUpdate();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos de cierre fueron actualizados con �xito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					frame.dispose();
					}else if (respuesta == JOptionPane.NO_OPTION) {}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		}
		
		void Imprimir() {
			try {
				try {
					Map<String,Object> parameters = new HashMap<String,Object>();
					parameters.put("Prt_ID",new Integer(VentanaLogin.ID_APETURA_CAJA));
					parameters.put("PrtFecha",new String(Menu.formatoDiaMesAño.format(dateChooserSalida.getDate()))+Menu.HORA);
					parameters.put("PrtTurno",new String (VentanaLogin.TUR_CORTO + " / "+ VentanaLogin.NOMBRE_USU_CORTO));
					//parameters.put("PrtEstado",new String("ALOJAMIENTO"));
					
					parameters.put("PrtFechaApertura",new String(Menu.formatoFechaString.format(dateApetura))+"  "+lblHraApe.getText());
					parameters.put("PrtFechaCierre",new String(Menu.formatoFechaString.format(dateChooserSalida.getDate()))+"  "+Menu.HORA);
					
					
					int totalItem=0;
					int TItem= 0;
					//conexion = new ConexionDB();
					String consult="",estado="";
						String [] lista = {"DISPONIBLE","OCUPADO","ALQUILADO","RESERVADO","LIMPIEZA","MANTENIMIENTO"};
						for(String llenar:lista) {
							estado=llenar;
							consult="Select * from HABITACION where EstadoHab='" + estado + "'";
							Statement statement = conexion.gConnection().createStatement();
							ResultSet rs = statement.executeQuery(consult);
							totalItem=0;
							if (estado.trim()=="DISPONIBLE") {
								parameters.put("PrtD",new Integer(totalItem));
							}
							else if (estado.trim()=="ALQUILADO") {
								parameters.put("PrtO",new Integer(totalItem));
							}
							else if (estado.trim()=="RESERVADO") {
								parameters.put("PrtR",new Integer(totalItem));
							}
							else if (estado.trim()=="LIMPIEZA") {
								parameters.put("PrtL",new Integer(totalItem));
							}
							else if (estado.trim()=="MANTENIMIENTO") {
								parameters.put("PrtM",new Integer(totalItem));
							}
							while (rs.next()==true){
								totalItem=totalItem+1;
								if (estado.trim()=="DISPONIBLE") {
									parameters.put("PrtD",new Integer(totalItem));
								}
								else if (estado.trim()=="ALQUILADO") {
									parameters.put("PrtO",new Integer(totalItem));
								}
								else if (estado.trim()=="RESERVADO") {
									parameters.put("PrtR",new Integer(totalItem));
								}
								else if (estado.trim()=="LIMPIEZA") {
									parameters.put("PrtL",new Integer(totalItem));
								}
								else if (estado.trim()=="MANTENIMIENTO") {
									parameters.put("PrtM",new Integer(totalItem));
								}
								TItem= TItem+ 1;
							}
							//lbli8.setText("TOTAL HABITACIONES: "+ TItem+"");
							rs.close();
							statement.close();
						}
					
						
						Statement statement = conexion.gConnection().createStatement();
						// CONSULTO PAGOS ALOJAMIENTOS
						float hos=0;
						//consult="Select * from CUENTA_HUESPED_PAGOS where IdTurnoP='" + VentanaLogin.ID_APETURA_CAJA + "'and EstadoA<>'" + 0 + "'";
						consult=("SELECT * FROM CUENTA_HUESPED_PAGOS INNER JOIN CUENTA_HUESPED ON (CUENTA_HUESPED_PAGOS.Id_Cta = CUENTA_HUESPED.Id_Cta) AND (CUENTA_HUESPED_PAGOS.IdTurnoP)='" + VentanaLogin.ID_APETURA_CAJA +"'AND (CUENTA_HUESPED.DescripcionCta)='" + "ALOJAMIENTO" +"'");
						ResultSet rs = statement.executeQuery(consult);
						while (rs.next()==true){
							hos=hos+rs.getFloat("AcuentaDetCta");
						}
						rs.close();
						parameters.put("PrtHos",new Float(hos));
						
						// CONSULTO PAGOS CONSUMOS
						float ser=0;
						consult=("SELECT * FROM CUENTA_HUESPED_PAGOS INNER JOIN CUENTA_HUESPED ON (CUENTA_HUESPED_PAGOS.Id_Cta = CUENTA_HUESPED.Id_Cta) AND (CUENTA_HUESPED_PAGOS.IdTurnoP)='" + VentanaLogin.ID_APETURA_CAJA +"'AND (CUENTA_HUESPED.DescripcionCta)='" + "CONSUMO" +"'");
						ResultSet rs2 = statement.executeQuery(consult);
						while (rs2.next()==true){
							ser=ser+rs2.getFloat("AcuentaDetCta");
						}
						rs2.close();
						parameters.put("PrtSer",new Float(ser));
					
						/*float vit=0;
						consult="Select * from DETALLE_A_CONSUMO where IdTurnoC='" + VentanaLogin.ID_APETURA_CAJA + "'and TipoConsumoC='" + "VITRINA" + "'";
						ResultSet rs3 = statement.executeQuery(consult);
						while (rs3.next()==true){
							vit=vit+rs3.getFloat("TotalC");
						}
						rs3.close();*/
						
						// VENTA DIRECTA VITRINA
						float ven=0;
						consult="Select * from TRANSACCION where IdTurnoT='" + VentanaLogin.ID_APETURA_CAJA + "'and TipoOperacionTra='" + "VENTA" + "'and EstadoTra='" + 1 + "'";
						ResultSet rs33 = statement.executeQuery(consult);
						while (rs33.next()==true){
							ven=ven+rs33.getFloat("TotalTra");
						}
						rs33.close();
						parameters.put("PrtVit",new Float(ven));
						
						// CONSULTO EGRESOS DE CAJA
						float gst=0;
						consult="Select * from CAJA where Id_ApeCie='" + VentanaLogin.ID_APETURA_CAJA + "'and TipoMov='" + "EGRESO" + "'and EstadoCaj='" + 1 + "'";
						ResultSet rs4 = statement.executeQuery(consult);
						while (rs4.next()==true){
							gst=gst+rs4.getFloat("MontoCaj");
						}
						rs4.close();
						parameters.put("PrtGst",new Float(gst));
						statement.close();
						
						parameters.put("PrtApe1",new Float(Float.parseFloat(lblmons.getText().toString().replaceAll(",", ""))));
						parameters.put("PrtApe2",new Float(Float.parseFloat(lblmond.getText().toString().replaceAll(",", ""))));
						parameters.put("PrtApe3",new Float(Float.parseFloat(lblmons.getText().toString().replaceAll(",", ""))));//TOTAL APETURA
						
						parameters.put("PrtMonS",new Float(Float.parseFloat(textMontoSol.getText().replaceAll(",", ""))));
						parameters.put("PrtMonD",new Float(Float.parseFloat(textMontoDol.getText().replaceAll(",", ""))));
						parameters.put("PrtMonTot",new Float(Float.parseFloat(textMontoSol.getText().replaceAll(",", ""))));//TOTAL CIERRE
					AbstractJasperReports.createReport( conexion.gConnection(), Menu.URL+"RCierreTurno.jasper",parameters );
					AbstractJasperReports.showViewerModal("Lista por cierre de turno",530,600,true,"/modelo/Images/cajacerrar.png");
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
			    //EXPORTAR EL INFORME A PDF
			    //JasperExportManager.exportReportToPdfFile(jasperPrint,Menu.URL + "CGastos.pdf");
			} catch (Exception e) {}
		}
		
		void centrar (){
	        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
	        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
	        frame.setLocation (x, y);
		    
		}
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == textMontoDol){textMontoDol.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textMontoSol){textMontoSol.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textMontoDol){textMontoDol.setForeground(new Color(0, 51, 153));} 
			if (ev.getSource() == textMontoSol){textMontoSol.setForeground(new Color(0, 51, 153));} 
			
			if (ev.getSource() == textMontoDol){textMontoDol.setFont(new Font("Tahoma", Font.BOLD, 14));} 
			if (ev.getSource() == textMontoSol){textMontoSol.setFont(new Font("Tahoma", Font.BOLD, 14));} 
			
			if (ev.getSource() == textMontoDol){textMontoDol.selectAll();} 
			if (ev.getSource() == textMontoSol){textMontoSol.selectAll();} 
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textMontoDol){textMontoDol.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textMontoSol){textMontoSol.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textMontoDol){textMontoDol.setForeground(new Color(0, 51, 153));} 
			if (ev.getSource() == textMontoSol){textMontoSol.setForeground(new Color(0, 51, 153));} 

			if (ev.getSource() == textMontoDol){textMontoDol.setFont(new Font("Tahoma", Font.BOLD, 12));} 
			if (ev.getSource() == textMontoSol){textMontoSol.setFont(new Font("Tahoma", Font.BOLD, 12));} 
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			if (evet.getSource() == textMontoSol){
				if (textMontoSol.getText().toLowerCase().isEmpty()|| textMontoSol.getText().toLowerCase().length()>=9 ){
					textMontoSol.requestFocus();
					textMontoSol.selectAll();
					textMontoSol.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER){
						textMontoDol.requestFocus();
						textMontoDol.selectAll();	
					}
			} 
			if (evet.getSource() == textMontoDol){
				if (textMontoDol.getText().toLowerCase().isEmpty()|| textMontoDol.getText().toLowerCase().length()>=9){
					textMontoDol.requestFocus();
					textMontoDol.selectAll();
					textMontoDol.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER){
						buttonGrabar.doClick();
					}
			}
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char car=evet.getKeyChar();
			if (evet.getSource() == textMontoDol){ 
				if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
			}
			if (evet.getSource() == textMontoSol){ 
				if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
			}
		}
		
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource()==(dateChooserSalida)){
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			FechaSalida = form.format(dateChooserSalida.getDate());
		}
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		
	}
	}
