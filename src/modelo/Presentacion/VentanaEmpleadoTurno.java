package modelo.Presentacion;

import java.awt.Color;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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

public class VentanaEmpleadoTurno implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JInternalFrame frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl2,lbl3;
	private JButton  			buttonGrabar,buttonSalir,buttonBus;
	protected static JDateChooser dateChooserActualizar;
	protected static String dateAlta,dateActualizar;
	protected JTextField 			textCod,textNom;
	protected static JComboBox<String> 		cbTurno,cbHorario;
	protected static JTextArea textArea = new JTextArea();
	
	
	public static int CONTAR_USUARIO=0;
	public static int MOD;
	public int ID_HORARIO=0;
	
	
	NumberFormat formatDsct;

	// constructor
	public VentanaEmpleadoTurno() {
		frameEmpleadoTurno();
		crearPanel();
		crearButtons();
		crearComboBox();
		crearTextFields();
		crearLabels();
		
		llenarcbTurno();
		llenarModificar();
		
		CONTAR_USUARIO ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
		
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    Menu.Desktop.add(frame);
	}
	public void frameEmpleadoTurno() {
		frame = new JInternalFrame();
		frame.setTitle("Agregar turno a empleado");
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			//@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				CONTAR_USUARIO=0;
			}

		});
		frame.setClosable(true);
		frame.setBounds(100, 100, 455, 169);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 425, 119);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
	}
	public void crearLabels() {
		lbl1= new JLabel("Empleado:");
		lbl1.setBounds(30, 25, 63, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Turno:");
		lbl2.setBounds(16, 52, 75, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Horario:");
		lbl3.setBounds(10, 76, 81, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
	
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
		textCod.setBounds(101, 25, 63, 22);
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
		textNom.setBounds(167, 25, 233, 22);
		panelDto.add(textNom);
		textNom.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));

	}
	
	public void crearComboBox() { 
		cbTurno = new JComboBox<>();
		cbTurno.setBounds(101, 50, 187, 21);
		cbTurno.setFont(Menu.fontText);
		cbTurno.addActionListener(this);
		cbTurno.addFocusListener(this);
		cbTurno.addKeyListener(this);
		panelDto.add(cbTurno);
		
		cbHorario = new JComboBox<>();
		cbHorario.setBounds(101, 73, 225, 21);
		cbHorario.setFont(Menu.fontText);
		cbHorario.addActionListener(this);
		cbHorario.addFocusListener(this);
		cbHorario.addKeyListener(this);
		panelDto.add(cbHorario);
	}
	
	public void llenarModificar() { 
		conexion= new ConexionDB();
		try {
			String con = "Select * from HORARIO where Id_Hor ='" + Integer.parseInt(VentanaEmpleado.id) + "'";
			Statement st = conexion.gConnection().createStatement();
			ResultSet rs = st.executeQuery(con);
			panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Nuevo: ítem " , TitledBorder.LEADING, TitledBorder.TOP, null,( new Color(106, 90, 205))));
			MOD=0;
			if (rs.next()==true) {
				cbTurno.setSelectedItem(rs.getString("Turno"));
				MOD=1;
				panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modificar: ítem " , TitledBorder.LEADING, TitledBorder.TOP, null,( new Color(106, 90, 205))));
			}
			st.close();
		} catch (Exception e) {}
	}
	public void llenarcbTurno() {
		conexion = new ConexionDB();
		cbTurno.removeAllItems();
		String consul="";
		consul="Select* from TURNO order by turno asc";
		try {
			Statement statement =conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(consul);
			while (resultSet.next()==true) {
				cbTurno.addItem(resultSet.getString("Turno"));
			}
			resultSet.close();
			cbTurno.setSelectedIndex(-1);
		} catch (Exception e) {}
	}
	public void llenarcbHorario() {
		conexion = new ConexionDB();
		cbHorario.removeAllItems();
		String consul="";
		consul="Select* from HORARIO as H,TURNO as T where H.Turno=T.Turno and H.Turno='" + cbTurno.getSelectedItem() + "'order by H.turno asc";
		try {
			Statement statement =conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(consul);
			while (resultSet.next()==true) {
				cbHorario.addItem(resultSet.getString("HoraEntradaHor")); //+"  "+ resultSet.getString("HoraSalidaHor"));
			}
			resultSet.close();
			cbHorario.setSelectedIndex(-1);
		} catch (Exception e) {}
	}
	public void MostrarHorario() {
		conexion = new ConexionDB();
		String consul="";
		//String Horario = (String)cbHorario.getSelectedItem();
		consul="Select* from HORARIO where Turno='" + cbTurno.getSelectedItem() + "'and HoraEntradaHor ='" + cbHorario.getSelectedItem() +"'"; //  + '  ' + HoraSalidaHor ='" + Horario + "'";
		try {
			Statement statement =conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(consul);
			if (resultSet.next()==true) {
				ID_HORARIO= Integer.parseInt(resultSet.getString("Id_Hor"));
				System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee " + ID_HORARIO );
			}
			resultSet.close();
		} catch (Exception e) {}
	}
	
	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(327, 73, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaEmpleadoTurno.class.getResource("/modelo/Images/save.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Cerrar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(364, 73, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaEmpleadoTurno.class.getResource("/modelo/Images/exit.png")));
		panelDto.add(buttonSalir);
		
		buttonBus= new JButton("");
		buttonBus.setBounds(290, 49, 36, 22);
		buttonBus.setToolTipText("Buscar turnos");
		buttonBus.addActionListener(this);
		buttonBus.setIcon(new ImageIcon(VentanaEmpleadoTurno.class.getResource("/modelo/Images/search.png")));
		panelDto.add(buttonBus);
	}
	
	public void limpiarTexts() {
		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.requestFocus(true);
		
		textNom.setText(null);
		textNom.setBackground(Menu.textColorBackgroundInactivo);	
		textNom.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
		
		cbTurno.removeAllItems();
		cbHorario.removeAllItems();
		
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textNom.setEnabled(b);
		textArea.setEnabled(b);
		cbTurno.setEnabled(b);
		cbHorario.setEnabled(b);
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
		  if (evento.getSource() == cbTurno){
			  llenarcbHorario();
			  }
		  if (evento.getSource() == cbHorario){
			  MostrarHorario();
			  }
		}
	
		public void insertarUpdate() {
			if (textCod.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Debe selecionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCod.requestFocus();
				this.frame.dispose();
				return;
			}
			if (cbTurno.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbTurno.requestFocus();
				return;
			}
			if (cbHorario.getSelectedItem()==null){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbHorario.requestFocus();
				return;
			}

			
			/*if (MOD==0) {// REGISTRAR
				try {
					System.out.println(Integer.parseInt(textCod.getText().toString().trim()));
					String sql ="INSERT INTO  USUARIO (Id_Emp,TipoUsu,CuentaUsu,PasswordUsu,PasswordConfirmarUsu) VALUES (?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText().toString().trim()));
					ps.setString(2, (cbTurno.getSelectedItem()).toString());

					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					limpiarTexts();
					//activarButton(true);
					//frame.dispose();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}*/
			
			conexion = new ConexionDB();
			if (MOD==1) { // MODIFICAR 
				try {
					int COD= Integer.parseInt(textCod.getText());
					System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb "+ COD +" HORARIO   "+ ID_HORARIO);
					
			        String sql="UPDATE EMPLEADO SET Id_Emp = ?,"
			        		 + "Id_Hor =?"
			                 + "WHERE Id_Emp = '"+ COD + "'"; 
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, COD);
					ps.setInt(2, ID_HORARIO);

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
			if (ev.getSource() == cbTurno){cbTurno.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbHorario){cbHorario.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == cbTurno){cbTurno.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == cbHorario){cbHorario.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == cbTurno){cbTurno.setBackground(new Color(240,240,240));}
			if (ev.getSource() == cbHorario){cbHorario.setBackground(new Color(240,240,240));}
			
			if (ev.getSource() == cbTurno){cbTurno.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == cbHorario){cbHorario.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
				if (evet.getSource() == cbTurno){
					if (e==KeyEvent.VK_ENTER){
						//textFecha.requestFocus();
						//textFecha.selectAll();
					}
				}
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
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
