
package modelo.Presentacion;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;

public class VentanaAperturaCaja implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JDialog frame;
	private JPanel  panelDto;
	private JLabel				lbl1,lbl3,lbl4,lbl7,lbl8,lbl9,lbl10,lblOperador;
	private JButton  			buttonGrabar,buttonSalir;
	private JDateChooser dateChooserAlta;
	private String FechaEntrada="";
	private JTextField 			textCod;
	private JFormattedTextField 	textMontoDol,textMontoSol,textTipoCambio;
	protected static JComboBox<String>  cbTurno;
	private int MOD;
	
	private JSpinner  spinnerE;
	SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss a");
	
	final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //pura hora con segundos
	
	// constructor
	public VentanaAperturaCaja() {
		frameAperturaCaja();
		crearPanel();
		crearButtons();
		crearLabels();
		crearTextFields();
		// ACTUALIZO FECHA ACTUAL DE ENTRADA
		dateChooserAlta.setDate(Menu.fecha);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		FechaEntrada = form.format(dateChooserAlta.getDate());
		
		llenarcbTurno();
		llenarModificar();

	}
	public void frameAperturaCaja() {
		frame = new JDialog();
		frame.setTitle("Abrir turno");//Apertura de caja por turno
		frame.setBounds(100, 100, 331, 247);
		frame.getContentPane().setLayout(null);
		frame.setModal(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaAperturaCaja.class.getResource("/modelo/Images/clock_play16.png")));
	}
	public void crearPanel() {
		panelDto = new JPanel();
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u00BF?", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 303, 196);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);

		SpinnerDateModel model = new SpinnerDateModel();
		//model.setCalendarField(Calendar.MINUTE);
		spinnerE= new JSpinner();
		spinnerE.setModel(model);
		spinnerE.setEditor(new JSpinner.DateEditor(spinnerE, "hh:mm:ss a"));
		spinnerE.setBounds(197, 59, 85, 20);
		spinnerE.addFocusListener(this);
		spinnerE.addKeyListener(this);
		spinnerE.addPropertyChangeListener(this);
		((JSpinner.DefaultEditor)spinnerE.getEditor()).getTextField().setEditable(false);
		// COLOR
		Component c = spinnerE.getEditor().getComponent(0);
		c.setBackground(new Color(250, 250, 220));
		c.setForeground(Color.black);
		// END COLOR
		panelDto.add(spinnerE);
	}
    // ************************************ PARA LA HORA
	 Thread clock = new Thread(){
			public void run(){
				try {
					for(;;){
					DecimalFormat format = new DecimalFormat("00");
					Calendar cr = new GregorianCalendar();
					int second = cr.get(Calendar.SECOND);
					int minute = cr.get(Calendar.MINUTE);
					//int hour = cr.get(Calendar.HOUR);
					String ampm = cr.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM";
					
					// 24 HORAS
					int hour = cr.get(Calendar.HOUR_OF_DAY);
					//lbl5.setText(" "+format.format(hour) +":"+ format.format(minute) +":"+ format.format(second) +" "+ ampm); 
					
					// 12 HORAS
		            if ( hour >= 13  ) {
		                hour = cr.get(Calendar.HOUR);
		            }
		            String tim=" "+format.format(hour) +":"+ format.format(minute) +":"+ format.format(second) +" "+ ampm;
		            Time time = new Time(formatTime.parse(tim).getTime());
		            spinnerE.setValue(time);
					sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		// ************************************ END PARA LA HORA
		

		
	public void crearLabels() {
		lbl1= new JLabel("Turno Nro:");
		lbl1.setBounds(10, 35, 85, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Monto S/.:");
		lbl3.setBounds(10, 114, 85, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Monto $:");
		lbl4.setBounds(32, 139, 63, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);

		lbl7= new JLabel("Fecha Apertura:");
		lbl7.setBounds(8, 62, 87, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
	
		lbl8= new JLabel("Turno:");
		lbl8.setBounds(10, 88, 85, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		lbl9= new JLabel("");
		lbl9.setBounds(189, 114, 81, 14);
		panelDto.add(lbl9);
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(Menu.fontLabel);

		lbl10= new JLabel("Tipo Cambio:");
		lbl10.setBounds(10, 164, 85, 14);
		panelDto.add(lbl10);
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(Menu.fontLabel);
		
		lblOperador= new JLabel("**************");
		lblOperador.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblOperador.setForeground(new Color(0, 0, 128));
		lblOperador.setBounds(178, 34, 104, 14);
		panelDto.add(lblOperador);
		lblOperador.setHorizontalAlignment(SwingConstants.RIGHT);
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
		textCod.setBounds(101, 31, 71, 22);
		panelDto.add(textCod);

		textMontoDol = new JFormattedTextField();
		textMontoDol.setForeground(new Color(0, 51, 153));
		textMontoDol.setFont(new Font("Tahoma", Font.BOLD, 12));
		textMontoDol.setBackground(new Color(255, 255, 255));
		textMontoDol.setText("0.00");
		textMontoDol.setHorizontalAlignment(SwingConstants.RIGHT);
		textMontoDol.setBounds(101, 135, 87, 22);
		textMontoDol.addFocusListener(this);
		textMontoDol.addKeyListener(this);
		panelDto.add(textMontoDol);
		
		textTipoCambio = new JFormattedTextField();
		textTipoCambio.setForeground(new Color(0, 51, 153));
		textTipoCambio.setFont(new Font("Tahoma", Font.BOLD, 12));
		textTipoCambio.setBackground(new Color(255, 255, 255));
		textTipoCambio.setText("0.00");
		textTipoCambio.setHorizontalAlignment(SwingConstants.RIGHT);
		textTipoCambio.setBounds(101, 160, 87, 22);
		textTipoCambio.addFocusListener(this);
		textTipoCambio.addKeyListener(this);
		panelDto.add(textTipoCambio);
		
		textMontoSol = new JFormattedTextField();
		textMontoSol.setForeground(new Color(0, 51, 153));
		textMontoSol.setFont(new Font("Tahoma", Font.BOLD, 12));
		textMontoSol.setBackground(new Color(255, 255, 255));
		textMontoSol.setText("0.00");
		textMontoSol.setHorizontalAlignment(SwingConstants.RIGHT);
		textMontoSol.setBounds(101, 110, 87, 22);
		textMontoSol.addFocusListener(this);
		textMontoSol.addKeyListener(this);
		panelDto.add(textMontoSol);
		
		cbTurno = new JComboBox<>();
		cbTurno.setBounds(101, 85, 169, 21);
		cbTurno.setFont(Menu.fontText);
		cbTurno.addActionListener(this);
		cbTurno.addFocusListener(this);
		cbTurno.addKeyListener(this);
		panelDto.add(cbTurno);
		
		dateChooserAlta = new JDateChooser();
		dateChooserAlta.setDateFormatString("dd-MMM-yyyy");
		dateChooserAlta.setBorder(new EtchedBorder(EtchedBorder.RAISED, new Color(70, 130, 180), new Color(0, 255, 255)));
		dateChooserAlta.setFont(new Font("SansSerif", Font.BOLD, 11));
		((JTextField)dateChooserAlta.getDateEditor().getUiComponent()).setBackground(new Color(245, 255, 250));
		
		((JTextField)dateChooserAlta.getDateEditor()).setEditable(false);
		
		dateChooserAlta.setBounds(101, 58, 93, 23);
		dateChooserAlta.addPropertyChangeListener(this);
		panelDto.add(dateChooserAlta);
	}
	public void llenarcbTurno() { 
		conexion = new ConexionDB();
		cbTurno.removeAllItems();
		try {
			String con = "Select * from TURNO";// where turno='" +cbTurno.getSelectedItem() + "'order by  turno asc";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			while (rs.next()==true) {
				cbTurno.addItem(rs.getString("turno"));
			}
			cbTurno.setSelectedIndex(-1);
			rs.close();
			st.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	public void llenarModificar() { 
		conexion= new ConexionDB();
		try {
			int ESTADO=0;
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select EstadoApeCie,Id_ApeCie from CAJA_APE_CIE order by Id_ApeCie desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_ApeCie"))+1);
				ESTADO=Integer.parseInt(resultSet.getString("EstadoApeCie"));
				textCod.setText(Menu.formatid_9.format(id));
			}else {
				textCod.setText("000000001");
			}
			resultSet.close();
			statement.close();

			if (ESTADO==1) {
				//String con = "Select * from CAJA_APE_CIE where EstadoApeCie ='" + 1 + "'and FechaApe BETWEEN'" + VentanaLogin.FechaAyer.trim() + "'and'" + Menu.date + "'and User='" + VentanaLogin.COD_USUARIO  + "'order by Id_ApeCie desc limit 0,1";
				String con = "Select * from CAJA_APE_CIE where EstadoApeCie ='" + 1 + "'and User='" + VentanaLogin.COD_EMP_USER.trim()  + "'order by Id_ApeCie desc limit 0,1";
				Statement st = conexion.gConnection().createStatement();
				ResultSet rs = st.executeQuery(con);
				if (rs.next()==true) {
					//spinnerE.setEnabled(false);
					MOD=1;
					FechaEntrada=(rs.getString("FechaApe").toString());
					Date date = Menu.formatoFecha.parse(FechaEntrada);
					dateChooserAlta.setDate(date);
					
		            Time time = new Time(formatTime.parse(rs.getString("HoraApe")).getTime());
		            spinnerE.setValue(time);
		            
					int d=(Integer.parseInt(rs.getString("Id_ApeCie")));
					textCod.setText(Menu.formatid_9.format(d));
					cbTurno.setSelectedItem(rs.getString("turno"));
					textMontoSol.setText(Menu.formateadorCurrency.format(rs.getFloat("MontoSolApe")));
					textMontoDol.setText(Menu.formateadorCurrency.format(rs.getFloat("MontoDolApe")));
					textTipoCambio.setText(Double.toString(rs.getDouble("TipoCambioApeCie")));
					
					panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Turno abierto", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
				

			    	//CONSULTO TURNO Y USER
					Statement ss = conexion.gConnection().createStatement();
					ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.Id_ApeCie)='" + rs.getInt("Id_ApeCie")+"'");
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
						lblOperador.setText("OPERADOR: "+ USU);
					}
					rr.close();
					ss.close();
					//END CONSULTO TURNO Y USER
				
				
				}else {
			    	//CONSULTO TURNO Y USER
					Statement ss = conexion.gConnection().createStatement();
					ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.User)='" + VentanaLogin.COD_EMP_USER+"'");
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
						lblOperador.setText("OPERADOR: "+ USU);
					}
					rr.close();
					ss.close();
					//END CONSULTO TURNO Y USER
					
					MOD=0;
					panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Turno cerrado", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(165, 42, 42)));
					//clock.start();
				}
				rs.close();
				st.close();
			}else{
				 DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
				 DecimalFormat Currency;
				 simbolo.setDecimalSeparator('.');
				 simbolo.setGroupingSeparator(',');
				 Currency = new DecimalFormat("###,##0.00",simbolo);
			    
				String cons = "Select * from CAJA_APE_CIE where EstadoApeCie ='" + 0 + "'order by Id_ApeCie desc limit 0,1";
				Statement st = conexion.gConnection().createStatement();
				ResultSet rs = st.executeQuery(cons);
				if (rs.next()==true) {
					textMontoSol.setText(Currency.format(rs.getFloat("MontoSolCie")));
					textMontoDol.setText(Currency.format(rs.getFloat("MontoDolCie")));
					textTipoCambio.setText(Double.toString(rs.getDouble("TipoCambioApeCie")));
					
			    	//CONSULTO TURNO Y USER
					Statement ss = conexion.gConnection().createStatement();
					ResultSet rr = ss.executeQuery("SELECT * FROM EMPLEADO INNER JOIN CAJA_APE_CIE ON (EMPLEADO.Id_Emp = CAJA_APE_CIE.User) AND (CAJA_APE_CIE.User)='" + VentanaLogin.COD_EMP_USER+"'");
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
						lblOperador.setText("OPERADOR: "+ USU);
					}
					rr.close();
					ss.close();
					//END CONSULTO TURNO Y USER
				}	
				rs.close();
				st.close();
				lbl9.setText("Saldo anterior");
				
				MOD=0;
				panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Turno cerrado", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(165, 42, 42)));
			}
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}

	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Abrir turno");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(195, 160, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaAperturaCaja.class.getResource("/modelo/Images/cajaabrir.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(232, 160, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaAperturaCaja.class.getResource("/modelo/Images/Exit.png")));
		panelDto.add(buttonSalir);
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
			if (VentanaLogin.COD_EMP_USER.trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "No se encontro ningun usuario...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (cbTurno.getSelectedItem()==null || cbTurno.getSelectedItem()==""){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbTurno.requestFocus();
				return;
			}
			if (dateChooserAlta.getDate()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				dateChooserAlta.requestFocus();
				return;
			}
			if (dateChooserAlta.getDate()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				dateChooserAlta.requestFocus();
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
			if (textTipoCambio.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textTipoCambio.requestFocus();
				return;
			}
			conexion = new ConexionDB();
			if (MOD==0) {// REGISTRAR
				try {
					String sql ="INSERT INTO  CAJA_APE_CIE (Id_ApeCie,User,Turno,FechaApe,HoraApe,MontoSolApe,MontoDolApe,FechaCie,HoraCie,MontoSolCie,MontoDolCie,TipoCambioApeCie,EstadoApeCie) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText().toString().trim()));
					ps.setString(2, (String)(VentanaLogin.COD_EMP_USER));
					ps.setString(3, (String)cbTurno.getSelectedItem());
					ps.setString(4, FechaEntrada.trim());
					
					JSpinner.DateEditor modele = new JSpinner.DateEditor(spinnerE, "hh:mm:ss a");
					ps.setNString(5, modele.getFormat().format(spinnerE.getValue())); 
					
					ps.setFloat(6, Float.parseFloat(textMontoSol.getText().replaceAll(",", "")));
					ps.setFloat(7, Float.parseFloat(textMontoDol.getText().replaceAll(",", "")));
					
					ps.setString(8, "0000-00-00");
					ps.setString(9, "");
					ps.setFloat(10, 0);
					ps.setFloat(11, 0);
					ps.setDouble(12, Double.parseDouble(textTipoCambio.getText().replaceAll(",", "")));
					ps.setString(13,"1");
					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Turno fue aperturado correctamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					frame.dispose();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			if (MOD==1) { // MODIFICAR 
				try {
			         String sql="UPDATE CAJA_APE_CIE SET Id_ApeCie = ?,"
			                 + "User =?,"
			                 + "Turno = ?,"
			                 + "FechaApe = ?,"
			                 + "HoraApe = ?,"
			                 + "MontoSolApe = ?,"
			                 + "MontoDolApe =?,"
			                 + "TipoCambioApeCie =?,"
			                 + "EstadoApeCie =?"
			                 + "WHERE Id_ApeCie = '"+ textCod.getText().trim() + "'"; 
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText().toString().trim()));
					ps.setString(2, (String)(VentanaLogin.COD_EMP_USER));
					ps.setString(3, (String)cbTurno.getSelectedItem());
					ps.setString(4, FechaEntrada.trim());

					JSpinner.DateEditor modele = new JSpinner.DateEditor(spinnerE, "hh:mm:ss a");
					ps.setNString(5, modele.getFormat().format(spinnerE.getValue())); 
					
					ps.setFloat(6, Float.parseFloat(textMontoSol.getText().replaceAll(",", "")));
					ps.setFloat(7, Float.parseFloat(textMontoDol.getText().replaceAll(",", "")));
					ps.setDouble(8, Double.parseDouble(textTipoCambio.getText().replaceAll(",", "")));
					ps.setString(9,"1");

					ps.executeUpdate();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos de apertura fueron actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					frame.dispose();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			VentanaLogin.CONSULTO_CAJA_APERTURA();// CONSULTO SI LA CAJA SE APETURADO 
		}
		
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == textMontoDol){textMontoDol.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textMontoSol){textMontoSol.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbTurno){cbTurno.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textMontoDol){textMontoDol.setForeground(new Color(0, 51, 153));} 
			if (ev.getSource() == textMontoSol){textMontoSol.setForeground(new Color(0, 51, 153));} 
			
			if (ev.getSource() == textMontoDol){textMontoDol.setFont(new Font("Tahoma", Font.BOLD, 14));} 
			if (ev.getSource() == textMontoSol){textMontoSol.setFont(new Font("Tahoma", Font.BOLD, 14));} 
			
			if (ev.getSource() == textMontoDol){textMontoDol.selectAll();} 
			if (ev.getSource() == textMontoSol){textMontoSol.selectAll();}
			if (ev.getSource() == textTipoCambio){textTipoCambio.selectAll();} 
			
			if (ev.getSource() == cbTurno){cbTurno.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textMontoDol){textMontoDol.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textMontoSol){textMontoSol.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == cbTurno){cbTurno.setBackground(new Color(240,240,240));}
			
			if (ev.getSource() == textMontoDol){textMontoDol.setForeground(new Color(0, 51, 153));} 
			if (ev.getSource() == textMontoSol){textMontoSol.setForeground(new Color(0, 51, 153));} 

			if (ev.getSource() == textMontoDol){textMontoDol.setFont(new Font("Tahoma", Font.BOLD, 12));} 
			if (ev.getSource() == textMontoSol){textMontoSol.setFont(new Font("Tahoma", Font.BOLD, 12));} 
			
			
			if (ev.getSource() == cbTurno){cbTurno.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			if (evet.getSource() == cbTurno){
				if (e==KeyEvent.VK_ENTER){
					if (cbTurno.getSelectedIndex()!=-1){
						textMontoSol.requestFocus();
						textMontoSol.selectAll();
					}else{
						cbTurno.requestFocus();
					}
				}	
			}
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
						textTipoCambio.requestFocus();
						textTipoCambio.selectAll();
					}
			}
			if (evet.getSource() == textTipoCambio){
				if (textTipoCambio.getText().toLowerCase().isEmpty()|| textTipoCambio.getText().toLowerCase().length()>=9){
					textTipoCambio.requestFocus();
					textTipoCambio.selectAll();
					textTipoCambio.setText(null);
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
			if (evet.getSource() == textTipoCambio){ 
				if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
			}
		}
		
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource()==(dateChooserAlta)){
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
			FechaEntrada = form.format(dateChooserAlta.getDate());
		}
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		
	}
	}
