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
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.toedter.calendar.JDateChooser;

import modelo.Datos.ConexionDB;

public class VentanaServiciosAgregar implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	private static ConexionDB conexion;
	public JInternalFrame frame;
	public JPanel  panelDto = new JPanel();
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl7;
	private JButton  			buttonGrabar,buttonSalir;
	protected static JDateChooser dateChooserEmision;
	protected static String dateEmision;
	protected static JTextField 			textCod,textDescripcion;
	protected static JFormattedTextField 	textFormatPrecio;
	
	private JScrollPane scrollArea;
	protected static JTextArea textArea;
	
	static int CONTAR_VENTANA_AGREGAR_SERVICIOS=0;
	public static int MOD;
	
	NumberFormat formatPrecio;

	// constructor
	public VentanaServiciosAgregar() {
		frameServiciosAgregar();
		crearPanel();
		crearButtons();
		crearTextFields();
		crearLabels();
		
		llenarNuevo();
		
		CONTAR_VENTANA_AGREGAR_SERVICIOS ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
		
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    Menu.Desktop.add(frame);
	}
	public void frameServiciosAgregar() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaServiciosAgregar.class.getResource("/modelo/Images/servicios.png")));
		frame.setTitle("Agregar servicios");
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				CONTAR_VENTANA_AGREGAR_SERVICIOS=0;
			}
		});
		frame.setClosable(true);
		frame.setBounds(100, 100, 570, 226);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 537, 171);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		dateChooserEmision = new JDateChooser();
		dateChooserEmision.setBounds(181, 25, 95, 22);
		panelDto.add(dateChooserEmision);
		dateChooserEmision.setDate(Menu.fecha);
		dateChooserEmision.setVisible(false);
	}
	
	public void llenarNuevo() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_Ser from SERVICIOS order by Id_Ser desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_Ser"))+1);
				textCod.setText(Menu.formatid_7.format(id));
			}else {
				textCod.setText(Menu.formatid_7.format(0000001));
			}
			textDescripcion.requestFocus();
			statement.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al load" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void crearLabels() {
		lbl1= new JLabel("Código");
		lbl1.setBounds(21, 28, 75, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Descripción:");
		lbl2.setBounds(12, 56, 84, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Precio S/:");
		lbl3.setBounds(447, 37, 66, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Observación:");
		lbl4.setBounds(10, 84, 84, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);

		lbl5= new JLabel("===>");
		lbl5.setBounds(366, 144, 57, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);

		lbl7= new JLabel("Fecha actualizo:");
		lbl7.setBounds(170, 9, 101, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		lbl7.setVisible(false);
	
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
		textCod.setBounds(104, 25, 75, 22);
		panelDto.add(textCod);
		
		textDescripcion= new JTextField();
		textDescripcion.setColumns(10);
		textDescripcion.setFont(Menu.fontText);
		textDescripcion.setForeground(Menu.textColorForegroundInactivo);
		textDescripcion.setHorizontalAlignment(SwingConstants.LEFT);
		textDescripcion.addActionListener(this);
		textDescripcion.addFocusListener(this);
		textDescripcion.addKeyListener(this);
		textDescripcion.setBounds(104, 52, 341, 22);
		panelDto.add(textDescripcion);
			
		scrollArea= new JScrollPane();
		scrollArea.setBounds(104, 77, 409, 56);
		panelDto.add(scrollArea);
		
		textArea = new JTextArea();
		scrollArea.setViewportView(textArea);
		textArea.setFont(Menu.fontText);
		textArea.setForeground(Menu.textColorForegroundInactivo);
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);

		textFormatPrecio = new JFormattedTextField();
		textFormatPrecio.setBackground(new Color(255, 255, 255));
		textFormatPrecio.setText("0.00");
		textFormatPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
		textFormatPrecio.setBounds(447, 52, 66, 22);
		textFormatPrecio.addFocusListener(this);
		textFormatPrecio.addKeyListener(this);
		panelDto.add(textFormatPrecio);

	}
	public void crearButtons() {
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(439, 137, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaServiciosAgregar.class.getResource("/modelo/Images/save.png")));
		panelDto.add(buttonGrabar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Regresar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(477, 137, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaServiciosAgregar.class.getResource("/modelo/Images/mant-cancelar.png")));
		panelDto.add(buttonSalir);
	}
	
	public void limpiarTexts() {
		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		
		textDescripcion.setText(null);
		textDescripcion.setBackground(Menu.textColorBackgroundInactivo);	
		textDescripcion.setForeground(Menu.textColorForegroundInactivo);
		
		textFormatPrecio.setText(null);
		textFormatPrecio.setBackground(Menu.textColorBackgroundInactivo);	
		textFormatPrecio.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
        
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textFormatPrecio.setEnabled(b);
		textDescripcion.setEnabled(b);
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
			  frame.dispose();
			  }
		}
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
			if (textDescripcion.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDescripcion.requestFocus();
				return;
			}
			if (textFormatPrecio.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textFormatPrecio.requestFocus();
				return;
			}

			conexion = new ConexionDB();
			if (MOD==0) {// REGISTRAR
				try {
					String sql ="INSERT INTO  SERVICIOS (Id_Ser,FechaActualizoSer,DescripcionSer,PrecioSer,ObservacionSer) VALUES (?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText()));
					ps.setNString(2, (Menu.date.trim() + " " + Menu.HORA).trim());
					ps.setString(3,textDescripcion.getText().trim());
					ps.setString(4, textFormatPrecio.getText().trim());
					ps.setString(5, textArea.getText().trim());
					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					limpiarTexts();
					llenarNuevo();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			if (MOD==1) { // MODIFICAR 
				try {
			         String sql="UPDATE SERVICIOS SET Id_Ser = ?,"
			                 + "FechaActualizoSer = ?,"
			                 + "DescripcionSer = ?,"
			                 + "PrecioSer = ?,"
			                 + "ObservacionSer = ?"

			                 + "WHERE Id_Ser = '"+VentanaServicio.id.trim()+"'"; 
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(VentanaServicio.id));
					ps.setNString(2, (Menu.date.trim() + " " + Menu.HORA).trim());
					ps.setString(3,textDescripcion.getText().trim());
					ps.setString(4, textFormatPrecio.getText().trim());
					ps.setString(5, textArea.getText().trim());
					
					// DOY FORMATO A LA FECHA
					//SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
					//dateEmision = form.format(dateChooserEmision.getDate());
					//ps.setNString(2, (dateEmision + " " + Menu.HORA).trim());
					
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
			if (ev.getSource() == textDescripcion){textDescripcion.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textFormatPrecio){textFormatPrecio.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textDescripcion){textDescripcion.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textFormatPrecio){textFormatPrecio.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textDescripcion){textDescripcion.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textFormatPrecio){textFormatPrecio.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textFormatPrecio){textFormatPrecio.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textDescripcion){textDescripcion.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			if (evet.getSource() == textDescripcion){
				int pos = textDescripcion.getCaretPosition();textDescripcion.setText(textDescripcion.getText().toUpperCase());textDescripcion.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				if (textDescripcion.getText().toLowerCase().isEmpty()|| textDescripcion.getText().toLowerCase().length()>75){
					textDescripcion.requestFocus();
					textDescripcion.selectAll();
					textDescripcion.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textDescripcion.getText().toLowerCase().length()==75){
						textFormatPrecio.requestFocus();
						textFormatPrecio.selectAll();	
					}
			} 
			if (evet.getSource() == textFormatPrecio){
				if (textFormatPrecio.getText().toLowerCase().isEmpty()|| textFormatPrecio.getText().toLowerCase().length()>8){
					textFormatPrecio.requestFocus();
					textFormatPrecio.selectAll();
					textFormatPrecio.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER){
						buttonGrabar.doClick();	
					}
			} 
			if (evet.getSource() == textArea){
				int pos = textArea.getCaretPosition();textArea.setText(textArea.getText().toUpperCase());textArea.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				if (textArea.getText().toLowerCase().isEmpty()|| textArea.getText().toLowerCase().length()>195){
					textArea.requestFocus();
					textArea.selectAll();
					textArea.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textFormatPrecio.getText().toLowerCase().length()==195){
						//buttonGrabar.doClick();
					}
			}
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char car=evet.getKeyChar();
			if (evet.getSource() == textDescripcion){
				if((car<'a' || car>'z') && (car<'A' || car>'Z')&&(car<' '||car>'.')&&(car<'0'||car>'9')) evet.consume();
			}
			if (evet.getSource() == textFormatPrecio){ 
				if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
			}
			if (evet.getSource() == textArea){
				if(!Character.isDefined(car)&&car!=KeyEvent.VK_SPACE&&car!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
		}
		
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		formatPrecio =NumberFormat.getNumberInstance();
		formatPrecio.setMaximumFractionDigits(3);
		//Object source = e.getSource();
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		
	}
	}
