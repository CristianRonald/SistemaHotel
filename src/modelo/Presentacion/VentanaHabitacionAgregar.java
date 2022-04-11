
package modelo.Presentacion;


import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Datos.ConexionDB;

public class VentanaHabitacionAgregar implements ActionListener,FocusListener,KeyListener,PropertyChangeListener {
	JInternalFrame frame;
	JPanel  panelDatos = new JPanel();
	JFormattedTextField textFieldFormatt;
	JTextField 			textField1,textFieldBuscar;
	JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl6,lbl7,lbl8,lblC1,lblC2,lblC3,lblC4,lblC5;
	JButton 			buttonBuscar,buttonFoto,buttonGrabar,buttonSalir;
	static JComboBox<String> cbTipoHabitacion = new JComboBox<String>();
	JComboBox<String> cbEstado = new JComboBox<String>();
	JComboBox<String> cbPiso = new JComboBox<String>();
	
    JScrollPane scrollArea,scrollTable;
	JTextArea textArea = new JTextArea();
	JCheckBox chckbxDesc,chck1,chck2,chck3,chck4,chck5;
	
	NumberFormat formatId;
	

    JTable tableCliente;
	DefaultTableModel model;
    
	private static ConexionDB conexion;
	public File fichero;
	private String almacenaFoto;
	private AbrirFoto archivo;
	int N_ORDEN=0;
	// constructor
	public VentanaHabitacionAgregar() {
		frameHabitacionAgregar();
		crearPanel();
		crearLabels();
		crearTextFields();
		crearComboBox();
		crearButtons();
		formatUp();
		activarButton(true);
		llenarTipoHabitacion();
		
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    Menu.Desktop.add(frame);
	    
        almacenaFoto="";
        lbl7.setText("Sin image");
        lbl7.setIcon(null);
	}
	public void frameHabitacionAgregar() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaHabitacionAgregar.class.getResource("/modelo/Images/menu-home.png")));
		frame.setTitle("Alta de Habitaciones");
		frame.setClosable(true);
		frame.setBounds(100, 100, 698, 324);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDatos.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDatos.setBounds(10, 10, 665, 273);
		frame.getContentPane().add(panelDatos);
		panelDatos.setLayout(null);
		panelDatos.setVisible(true);

		chckbxDesc = new JCheckBox("Cambiar Nro");
		chckbxDesc.setBounds(245, 38, 96, 15);
		chckbxDesc.addActionListener(this);
		panelDatos.add(chckbxDesc);
		
		
		chck1 = new JCheckBox("");
		chck1.setBackground(new Color(144, 238, 144));
		chck1.setHorizontalAlignment(SwingConstants.LEFT);
		chck1.setBounds(138, 164, 23, 17);
		chck1.addActionListener(this);
		panelDatos.add(chck1);
		
		chck2 = new JCheckBox("");
		chck2.setBackground(new Color(144, 238, 144));
		chck2.setBounds(138, 182, 23, 17);
		chck2.addActionListener(this);
		panelDatos.add(chck2);
		
		chck3 = new JCheckBox("");
		chck3.setBackground(new Color(144, 238, 144));
		chck3.setBounds(138, 200, 23, 17);
		chck3.addActionListener(this);
		panelDatos.add(chck3);
		
		chck4 = new JCheckBox("");
		chck4.setBackground(new Color(144, 238, 144));
		chck4.setHorizontalAlignment(SwingConstants.LEFT);
		chck4.setBounds(138, 218, 23, 17);
		chck4.addActionListener(this);
		panelDatos.add(chck4);
		
		chck5 = new JCheckBox("");
		chck5.setBackground(new Color(144, 238, 144));
		chck5.setHorizontalAlignment(SwingConstants.LEFT);
		chck5.setBounds(138, 236, 23, 17);
		chck5.addActionListener(this);
		panelDatos.add(chck5);
	}
	public void crearLabels() {
		lbl1= new JLabel();
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		panelDatos.add(lbl1);
		lbl1.setText("Nro:");
		lbl1.setBounds(20, 38, 106, 14);
		lbl1.setForeground(Menu.lblColorForegroundActivo);
		
		lbl2= new JLabel();
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		panelDatos.add(lbl2);
		lbl2.setText("Tipo Habitación:");
		lbl2.setBounds(20, 64, 108, 14);

		lbl3= new JLabel();
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		panelDatos.add(lbl3);
		lbl3.setText("Piso:");
		lbl3.setBounds(60, 89, 66, 14);
		
		lbl4= new JLabel();
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);
		panelDatos.add(lbl4);
		lbl4.setText("Estado:");
		lbl4.setBounds(60, 114, 66, 14);

		lbl5= new JLabel();
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);
		panelDatos.add(lbl5);
		lbl5.setText("Fecha Alta:");
		lbl5.setBounds(30, 139, 96, 14);

		lbl6= new JLabel();
		lbl6.setForeground(Color.DARK_GRAY);
		lbl6.setHorizontalAlignment(SwingConstants.LEFT);
		lbl6.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelDatos.add(lbl6);
		lbl6.setText("DETALLE:");
		lbl6.setBounds(217, 139, 148, 19);

		lbl8= new JLabel();
		lbl8.setForeground(Color.GRAY);
		lbl8.setHorizontalAlignment(SwingConstants.LEFT);
		lbl8.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panelDatos.add(lbl8);
		lbl8.setText("<html>Configuración actual 154 Hab.<br>Configurable hasta 1000 Hab.</html>");
		lbl8.setBounds(317, 92, 148, 36);

		lbl7= new JLabel();
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		panelDatos.add(lbl7);
		lbl7.setText("Sin image");
		lbl7.setHorizontalAlignment(SwingConstants.CENTER);
		lbl7.setBounds(475, 16, 168, 122);
		lbl7.setBorder(new LineBorder(new Color(211, 211, 211), 1, true));
		
		
		
		lblC1= new JLabel();
		lblC1.setForeground(new Color(0, 0, 139));
		lblC1.setOpaque(true);
		lblC1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblC1.setFont(Menu.fontLabel);
		panelDatos.add(lblC1);
		lblC1.setText("TV:");
		lblC1.setBounds(12, 164, 125, 17);
		lblC1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 0), new Color(0, 255, 255)));
		
		lblC2= new JLabel();
		lblC2.setForeground(new Color(0, 0, 139));
		lblC2.setOpaque(true);
		lblC2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblC2.setFont(Menu.fontLabel);
		panelDatos.add(lblC2);
		lblC2.setText("BAÑO PRIVADO:");
		lblC2.setBounds(12, 182, 125, 17);
		lblC2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 0), new Color(0, 255, 255)));
		
		lblC3= new JLabel();
		lblC3.setForeground(new Color(0, 0, 139));
		lblC3.setOpaque(true);
		lblC3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblC3.setFont(Menu.fontLabel);
		panelDatos.add(lblC3);
		lblC3.setText("AGUA CALIENTE:");
		lblC3.setBounds(12, 200, 126, 17);
		lblC3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 0), new Color(0, 255, 255)));
		
		lblC4= new JLabel();
		lblC4.setForeground(new Color(0, 0, 139));
		lblC4.setOpaque(true);
		lblC4.setHorizontalAlignment(SwingConstants.RIGHT);
		lblC4.setFont(Menu.fontLabel);
		panelDatos.add(lblC4);
		lblC4.setText("VENTILADOR:");
		lblC4.setBounds(12, 218, 125, 17);
		lblC4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 0), new Color(0, 255, 255)));
		
		lblC5= new JLabel();
		lblC5.setForeground(new Color(0, 0, 139));
		lblC5.setOpaque(true);
		lblC5.setHorizontalAlignment(SwingConstants.RIGHT);
		lblC5.setFont(Menu.fontLabel);
		panelDatos.add(lblC5);
		lblC5.setText("AIRE ACONDICIONADO:");
		lblC5.setBounds(12, 236, 125, 17);
		lblC5.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 0), new Color(0, 255, 255)));
	}
	public void crearTextFields(){
		
		textField1 = new JTextField();
		textField1.setColumns(10);
		textField1.setFont(Menu.fontText);
		textField1.setForeground(Menu.textColorForegroundInactivo);
		textField1.setHorizontalAlignment(SwingConstants.CENTER);
		panelDatos.add(textField1);
		textField1.addActionListener(this);
		textField1.addFocusListener(this);
		textField1.addKeyListener(this);
		textField1.setBounds(136, 35, 106, 22);
		textField1.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		textFieldBuscar = new JTextField();
		textFieldBuscar.setColumns(10);
		textFieldBuscar.setFont(Menu.fontText);
		textFieldBuscar.setForeground(Menu.textColorForegroundInactivo);
		textFieldBuscar.setHorizontalAlignment(SwingConstants.CENTER);
		panelDatos.add(textFieldBuscar);
		textFieldBuscar.addActionListener(this);
		textFieldBuscar.addFocusListener(this);
		textFieldBuscar.addKeyListener(this);
		textFieldBuscar.setBounds(10, 34, 66, 22);
		textFieldBuscar.setVisible(false);
		
		scrollArea= new JScrollPane();
		scrollArea.setBounds(162, 164, 481, 89);
		panelDatos.add(scrollArea);
		textArea = new JTextArea();
		textArea.setToolTipText("Descripci\u00F3n");
		scrollArea.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);
		
		textFieldFormatt = new JFormattedTextField(formatId);
		textFieldFormatt.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldFormatt.setBounds(136, 136, 76, 22);
		textFieldFormatt.setText("");
		textFieldFormatt.setEditable(false);
		panelDatos.add(textFieldFormatt);
		textFieldFormatt.addPropertyChangeListener("values",this);
		textFieldFormatt.setValue(Menu.date);
		textFieldFormatt.addActionListener(this);
		textFieldFormatt.addFocusListener(this);
		textFieldFormatt.addKeyListener(this);

	}
	
	public void crearComboBox() { 
		// TIPO HABITACION 
		cbTipoHabitacion.setBounds(136, 61, 201, 21);
		cbTipoHabitacion.setBackground(new Color(240,240,240));
		cbTipoHabitacion.setFont(Menu.fontText);
		cbTipoHabitacion.addFocusListener(this);
		cbTipoHabitacion.addKeyListener(this);
		cbTipoHabitacion.addActionListener(this);
		panelDatos.add(cbTipoHabitacion);
		
		// PISO
		cbPiso.setBounds(136, 86, 86, 21);
		cbPiso.setBackground(new Color(240,240,240));
		cbPiso.setFont(Menu.fontText);
		cbPiso.addFocusListener(this);
		cbPiso.addKeyListener(this);
		panelDatos.add(cbPiso);
		
		// ESTADO
		cbEstado.setBounds(136, 111, 139, 21);
		cbEstado.setBackground(new Color(240,240,240));
		cbEstado.setFont(Menu.fontText);
		cbEstado.addFocusListener(this);
		cbEstado.addKeyListener(this);
		panelDatos.add(cbEstado);
	}
	
	public static void llenarTipoHabitacion() {
		@SuppressWarnings("unused")
		int l=0;
		// LLENAR TIPO HABITACION
		cbTipoHabitacion.removeAllItems();
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from tipo_habitacion");
				for ( l=0; resultSet.next(); l++) {
					String [] lista = {resultSet.getString("Tipo_Hab")};
				    Arrays.sort (lista);
					for (String llenar:lista) {
						cbTipoHabitacion.addItem(llenar);
					}
				}
			statement.close();
		} catch (Exception e) {}
	}
	public void llenarcbComboBox(){
		// LLENAR PISO
		cbPiso.removeAllItems();
		String [] lista1 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
		for (String llenar:lista1) {
			cbPiso.addItem(llenar);
		}
		cbPiso.setSelectedItem("1");
		
		// LLENAR ESTADO
		cbEstado.removeAllItems();
		String [] lista2 = {"DISPONIBLE","ALQUILADO","RESERVADO","LIMPIEZA","MANTENIMIENTO"};
	    Arrays.sort (lista2);
		for (String llenar:lista2) {
			cbEstado.addItem(llenar);
		}
		cbEstado.setSelectedItem("DISPONIBLE");
	}

	public void crearButtons() {
		buttonGrabar= new JButton();
		buttonGrabar.setIcon(new ImageIcon(VentanaHabitacionAgregar.class.getResource("/modelo/Images/save.png")));
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.setText("");
		buttonGrabar.setBounds(390, 138, 36, 23);
		panelDatos.add(buttonGrabar);
		buttonGrabar.addActionListener(this);
	
		buttonSalir= new JButton();
		buttonSalir.setIcon(new ImageIcon(VentanaHabitacionAgregar.class.getResource("/modelo/Images/Exit.png")));
		buttonSalir.setToolTipText("Salir");
		buttonSalir.setText("");
		buttonSalir.setBounds(430, 138, 36, 23);
		panelDatos.add(buttonSalir);
		buttonSalir.addActionListener(this);
	
		buttonBuscar= new JButton();
		buttonBuscar.setIcon(new ImageIcon(VentanaHabitacionAgregar.class.getResource("/modelo/Images/homeAzul.png")));
		buttonBuscar.setToolTipText("Alta tipo de habitación");
		buttonBuscar.setText("");
		buttonBuscar.setBounds(339, 60, 36, 23);
		panelDatos.add(buttonBuscar);
		buttonBuscar.addActionListener(this);
	
		buttonFoto= new JButton();
		buttonFoto.setIcon(new ImageIcon(VentanaHabitacionAgregar.class.getResource("/modelo/Images/blue.png")));
		buttonFoto.setToolTipText("Agregar imagen");
		buttonFoto.setText("Agregar imagen...");
		buttonFoto.setBounds(474, 138, 169, 23);
		panelDatos.add(buttonFoto);
		buttonFoto.addActionListener(this);
	}
	protected void llenarTable() {
		conexion = new ConexionDB();
        try {
    		model= new DefaultTableModel();
    		model.addColumn("Nro");
    		model.addColumn("Piso");
    		model.addColumn("Tipo Habitación");
    		model.addColumn("Estado");
    		model.addColumn("Fecha");
    		model.addColumn("Descripción");
    		model.addColumn("Image");
    		model.addColumn("Url Image");
    		
		   String consultar="SELECT * FROM Habitacion where NumeroHab="+ textFieldBuscar.getText();
		   String []datos= new String[4];
		   Statement st = conexion.gConnection().createStatement();
           ResultSet rs=st.executeQuery(consultar);
           while(rs.next())
            {
        	   //cambia de color segun estado
        	   /*if(rs.getString("ESTADO").equals("ACTIVO")){
        		   		tableCliente.setBackground(Color.blue);
        		   } else tableCliente.setBackground(Color.red);{
        		}*/  
               
            datos[0]=rs.getString("NumeroHab");
            datos[1]=rs.getString("PisoHab");
            datos[2]=rs.getString("Tipo_Hab");
            datos[3]=rs.getString("EstadoHab");
            datos[3]=rs.getString("FechaAltaHab");
            datos[3]=rs.getString("DescripcionHab");
            datos[3]=rs.getString("ImageHab");
            datos[3]=rs.getString("URLHab");
            
            model.addRow(datos);
            tableCliente.setModel(model);
            }
            	
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al llenar la table" + e);
		}
	}

	public void limpiarTexts() {
		textField1.setText(null);
		textField1.setBackground(Menu.textColorBackgroundInactivo);	
		
		textField1.setText(null);
		textField1.setBackground(Menu.textColorBackgroundInactivo);	
		
		textFieldBuscar.setText(null);
		textFieldBuscar.setBackground(Menu.textColorBackgroundInactivo);	
		
		textFieldFormatt.setText(null);
		textArea.setText(null);
		cbTipoHabitacion.setForeground(Menu.textColorForegroundInactivo);
		cbPiso.setForeground(Menu.textColorForegroundInactivo);
		cbEstado.setForeground(Menu.textColorForegroundInactivo);
		textField1.requestFocus(true);
		
        almacenaFoto="";
        lbl7.setText("Sin image");
        lbl7.setIcon(null);
	}
	
	public void activarTexts(boolean b) {
		textField1.setEnabled(b);
		textField1.setEnabled(b);
		textFieldBuscar.setEnabled(b);
		textFieldFormatt.setEnabled(b);
		textArea.setEnabled(b);
	}
	public void activarButton(boolean c) {
		 if (c == true){
			buttonGrabar.setEnabled(false);// GRABAR
			buttonSalir.setEnabled(true);	// SALIR
			

			buttonBuscar.setEnabled(true);// ALTA TIPO HABITACION
			buttonFoto.setEnabled(false);// AGREGAR FOTO
			
			textFieldBuscar.setEnabled(true);// TEXT BUSCAR
		 }
		 if (c == false){
			buttonGrabar.setEnabled(true); // GRABAR
			buttonSalir.setEnabled(true);	// SALIR

			
			//button[2].setEnabled(false);// ALTA TIPO HABITACION
			buttonFoto.setEnabled(true);// AGREGAR FOTO
			
			textFieldBuscar.setEnabled(false);// TEXT BUSCAR
		 }
	}
	
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonGrabar){// GRABAR
			  if (textFieldBuscar.getText().toLowerCase().trim().isEmpty()){
				  insertar();
			  }else{
				  actualizar();
			  	}
			  }
		  if (evento.getSource() == buttonSalir){// SALIR
			  VentanaTipoHabitacion t = new VentanaTipoHabitacion();
			  t.frame.setVisible(false);
			  t.frame.dispose();
			  frame.dispose();
			  t.frame.dispose();
			  }

		  if (evento.getSource() == buttonBuscar){// BUSCAR TIPO HABITACION
			  VentanaTipoHabitacion tipo = new VentanaTipoHabitacion();
				// LLAMO - CENTRO LA VENTANA TIPO HABITACION
				tipo.frame.toFront();
				tipo.frame.setVisible(true);
			  }	
		  if (evento.getSource() == buttonFoto){// AGREGO FOTO
			  int resultado;
				archivo = new AbrirFoto();
				archivo.frame.toFront();
				/*FileNameExtensionFilter filter = new FileNameExtensionFilter("Formatos de Archivos JPEG(*.JPG;*.JPEG)", "jpg","jpeg");
				AbrirFoto.fileChooser.setFileFilter(filter);
				File ruta = new File("D:/LIBRA/DOCUMENTOS/Documentos L.M/Otros");
				AbrirFoto.fileChooser.setCurrentDirectory(ruta);*/

				resultado= AbrirFoto.fileChooser.showOpenDialog(null);
				if (JFileChooser.APPROVE_OPTION == resultado){
					fichero= AbrirFoto.fileChooser.getSelectedFile();
					try {
						almacenaFoto="";
						ImageIcon icon = new ImageIcon(fichero.toString());
						Icon icono = new ImageIcon(icon.getImage().getScaledInstance(lbl7.getWidth(), lbl7.getHeight(), Image.SCALE_DEFAULT));        
			            //textArea.setText(String.valueOf(fichero));//Lo imprimimos en una caja de texto para ver su ruta			            
			            almacenaFoto=(String.valueOf(fichero));
			            lbl7.setText(null);
			            lbl7.setIcon(icono);
							
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al abrir la imagen" + e);
					}
					
				}
			  
			 }
	      	if (evento.getSource().equals(chckbxDesc)){
	      		if (chckbxDesc.isSelected()==true) {
	      			textField1.setEnabled(true);
	      			textField1.requestFocus();
	      			textField1.selectAll();
	      		}
	      		if (chckbxDesc.isSelected()==false) {
	      			textField1.setEnabled(false);
	      			cbTipoHabitacion.requestFocus();
	      		}
	      		
	      	}
		}

	//METODO NUEVO =================
	public void nuevo() {
		  limpiarTexts();
		  activarButton(false);
		  activarTexts(true);
		  textFieldFormatt.setText(Menu.date);
		  textFieldBuscar.setEnabled(false);
		  textField1.requestFocus(true);
		  
		  //cbTipoHabitacion.setSelectedIndex(-1);
		  //cbPiso.setSelectedIndex(-1);
		  
		  almacenaFoto="";
		  lbl7.setText("Sin image");
		  lbl7.setIcon(null);
	}
	
	//METODO BUSCAR =================
	public void buscar() {
		if (textFieldBuscar.getText().toLowerCase().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Busca mediante el Nro de habitación...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			limpiarTexts();
			textFieldBuscar.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
			 llenarcbComboBox();
			int buscoNro= Integer.parseInt(textFieldBuscar.getText());
		
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from habitacion where NumeroHab="+buscoNro);
			if (resultSet.next()== true) {
		        almacenaFoto="";
		        lbl7.setText("Sin image");
		        lbl7.setIcon(null);
		        
		        textField1.setText(resultSet.getString("NumeroHab"));
				cbPiso.setSelectedItem(Integer.toString(resultSet.getInt("PisoHab")));
				cbTipoHabitacion.setSelectedItem(resultSet.getString("Tipo_Hab"));
				cbEstado.setSelectedItem(resultSet.getString("EstadoHab"));
				textFieldFormatt.setText(Menu.formatoFechaString.format(resultSet.getDate("FechaAltaHab")));
				textArea.setText(resultSet.getString("DescripcionHab").trim());
				almacenaFoto=resultSet.getString("URLHab");
				
				String caracteristica=resultSet.getString("CaracteristicasHab").trim();
				String delimiter = "-";
				String[] temp;
				temp = caracteristica.split(delimiter);
				for(int i =0; i < temp.length ; i++){
					
					if (lblC1.getText().trim().equals(temp[i].trim()+":")) {
						chck1.setSelected(true);
					}
					if (lblC2.getText().trim().equals(temp[i].trim()+":")) {
						chck2.setSelected(true);
					}
					if (lblC3.getText().trim().equals(temp[i].trim()+":")) {
						chck3.setSelected(true);
					}
					if (lblC4.getText().trim().equals(temp[i].trim()+":")) {
						chck4.setSelected(true);
					}
					if (lblC5.getText().trim().equals(temp[i].trim()+":")) {
						chck5.setSelected(true);
					}
					//System.out.println(temp[i].trim());
				}
				
				// CARGO LA IMAGEN
			    Image i=null;
		        Blob blob = resultSet.getBlob("ImageHab");
		        i= javax.imageio.ImageIO.read(blob.getBinaryStream());
		        ImageIcon image = new ImageIcon(i);
				Icon icono = new ImageIcon(image.getImage().getScaledInstance(lbl7.getWidth(), lbl7.getHeight(), Image.SCALE_DEFAULT)); 
				lbl7.setText(null);
				lbl7.setIcon(icono);
			}else{
				limpiarTexts();
				textFieldBuscar.requestFocus();
				textFieldBuscar.selectAll();
			}
			statement.close();
		} catch (Exception e) {}
	}
	
	//METODO GRABAR =================
	public void insertar() {
		ordenar_Hab();
		if (textField1.getText().toLowerCase().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textField1.requestFocus();
			return;
		}
		if (textField1.getText().toLowerCase().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textField1.requestFocus();
			return;
		}
		if (cbTipoHabitacion.getSelectedItem()==null || cbTipoHabitacion.getSelectedItem()==""){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el tipo de habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbTipoHabitacion.requestFocus();
			return;
		}
		if (cbPiso.getSelectedItem()==null ||  cbPiso.getSelectedItem()==""){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el nivel o piso de la habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbPiso.requestFocus();
			return;
		}
		if (cbEstado.getSelectedItem()==null ||  cbEstado.getSelectedItem()==""){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el estado de habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbEstado.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
			int buscoNro= Integer.parseInt(textField1.getText());
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from Habitacion where NumeroHab="+buscoNro);
			if (resultSet.next()== true) {
				JOptionPane.showMessageDialog(null, "Habitación ya fue registrada",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
				textField1.requestFocus(true);
				textField1.selectAll();
			}else{
				try {
                FileInputStream  archivofoto;
		      
				String sql ="INSERT INTO  Habitacion (NumeroHab,PisoHab,Tipo_Hab,EstadoHab,FechaAltaHab,DescripcionHab,ImageHab,URLHab,OrdenarHab,CaracteristicasHab) VALUES (?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(textField1.getText()));
				ps.setString(2, (String)cbPiso.getSelectedItem());
				ps.setString(3,(String)cbTipoHabitacion.getSelectedItem());
				ps.setString(4,(String)cbEstado.getSelectedItem());
				ps.setNString(5, (Menu.date).trim());
				ps.setString(6, textArea.getText().trim());
				
				ps.setString(8, almacenaFoto);
				// VERIFICA SI SE GUARDARA UNA IMAGEN
				if ((almacenaFoto==null)|| (almacenaFoto=="")){
					almacenaFoto="sin image";
					ps.setString(7,almacenaFoto);
				}else {
					archivofoto = new FileInputStream(almacenaFoto);
					ps.setBinaryStream(7,archivofoto);
				}
				ps.setInt(9, N_ORDEN);// ORDENAR
				
				String caracteristica="";
				for (int x=1;x<5;x++) {
					if (chck1.isSelected()) {
						caracteristica=lblC1.getText().trim().replaceAll(":", "");
					}
					if (chck2.isSelected()) {
						caracteristica=caracteristica+" - "+lblC2.getText().trim().replaceAll(":", "");
					}
					if (chck3.isSelected()) {
						caracteristica=caracteristica+" - "+lblC3.getText().trim().replaceAll(":", "");
					}
					if (chck4.isSelected()) {
						caracteristica=caracteristica+" - "+lblC4.getText().trim().replaceAll(":", "");
					}
					if (chck5.isSelected()) {
						caracteristica=caracteristica+" - "+lblC5.getText().trim().replaceAll(":", "");
					}
				}	
				ps.setString(10, caracteristica.trim());
				ps.execute();
				ps.close();
				JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				nuevo();
				
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
			}
			statement.close();
		} catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
	}

	//METODO ACTUALIZA =================
	public void actualizar() {
		if (textField1.getText().toLowerCase().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textField1.requestFocus();
			return;
		}
		if (textField1.getText().toLowerCase().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textField1.requestFocus();
			return;
		}
		if (cbTipoHabitacion.getSelectedItem()==null || cbTipoHabitacion.getSelectedItem()== ""){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el tipo de habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbTipoHabitacion.requestFocus();
			return;
		}
		if (cbPiso.getSelectedItem()==null || cbPiso.getSelectedItem()==""){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el nivel o piso de la habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbPiso.requestFocus();
			return;
		}
		if (cbEstado.getSelectedItem()==null || cbEstado.getSelectedItem()==""){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el estado de habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbEstado.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
			if (chckbxDesc.isSelected()==true) {
				int buscoNro= Integer.parseInt(textField1.getText());
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * from Habitacion where NumeroHab="+buscoNro);
				if (resultSet.next()== true) {
					JOptionPane.showMessageDialog(null, "Este Nro se encuentra registrado \nagrege otro Nro# para la habitación",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
					textField1.requestFocus(true);
					textField1.selectAll();
					return;
				}
				statement.close();
			}	
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error de numero habitación (duplication)" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		
		
		try {
        FileInputStream  archivofoto;
         String sql="UPDATE Habitacion SET NumeroHab = ?,"
                 + "PisoHab = ?,"
                 + "Tipo_Hab = ?,"
                 + "EstadoHab = ?,"
                 + "FechaAltaHab=?,"
                 + "DescripcionHab = ?,"
                 + "ImageHab=?,"
                 + "URLHab=?,"
                 + "CaracteristicasHab=?"
                 + "WHERE NumeroHab = '"+textFieldBuscar.getText()+"'"; 
         
		PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
		ps.setInt(1, Integer.parseInt(textField1.getText()));
		ps.setString(2, (String)cbPiso.getSelectedItem());
		ps.setString(3,(String)cbTipoHabitacion.getSelectedItem());
		ps.setString(4,(String)cbEstado.getSelectedItem());
		ps.setString(5, Menu.date.trim());
		ps.setString(6, textArea.getText().trim());
		ps.setString(8, almacenaFoto);
		// VERIFICA SI SE ACTUALIZA UNA IMAGEN
		if ((almacenaFoto==null)||(almacenaFoto=="")){
			almacenaFoto="sin imagen";
			ps.setString(7,almacenaFoto);
		}else {
			archivofoto = new FileInputStream(almacenaFoto);
			ps.setBinaryStream(7,archivofoto);
		}
		
		String caracteristica="";
		for (int x=1;x<5;x++) {
			if (chck1.isSelected()) {
				caracteristica=lblC1.getText().trim().replaceAll(":", "");
			}
			if (chck2.isSelected()) {
				caracteristica=caracteristica+" - "+lblC2.getText().trim().replaceAll(":", "");
			}
			if (chck3.isSelected()) {
				caracteristica=caracteristica+" - "+lblC3.getText().trim().replaceAll(":", "");
			}
			if (chck4.isSelected()) {
				caracteristica=caracteristica+" - "+lblC4.getText().trim().replaceAll(":", "");
			}
			if (chck5.isSelected()) {
				caracteristica=caracteristica+" - "+lblC5.getText().trim().replaceAll(":", "");
			}
		}	
		ps.setString(9, caracteristica.trim());
		ps.executeUpdate();
		ps.close();
		JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		limpiarTexts();
		frame.dispose();
		activarButton(true);buttonGrabar.requestFocus(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}

	}
	public void focusGained(FocusEvent ev) {
		if (ev.getSource() == textField1){textField1.setBackground(Menu.textColorBackgroundActivo);} 
		if (ev.getSource() == textFieldBuscar){textFieldBuscar.setBackground(Menu.textColorBackgroundActivo);} 
		if (ev.getSource() == cbTipoHabitacion){cbTipoHabitacion.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == cbPiso){cbPiso.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == cbEstado){cbEstado.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
		//FORE
		if (ev.getSource() == textField1){textField1.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == textField1){textField1.setForeground(Menu.textColorForegroundActivo);} 
		if (ev.getSource() == textFieldBuscar){textFieldBuscar.setForeground(Menu.textColorForegroundActivo);} 
		if (ev.getSource() == cbTipoHabitacion){cbTipoHabitacion.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == cbPiso){cbPiso.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == cbEstado){cbEstado.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
	}
	public void focusLost(FocusEvent ev) {
		if (ev.getSource() == textField1){textField1.setBackground(Menu.textColorBackgroundInactivo);} 
		if (ev.getSource() == textFieldBuscar){textFieldBuscar.setBackground(Menu.textColorBackgroundInactivo);} 
		if (ev.getSource() == cbTipoHabitacion){cbTipoHabitacion.setBackground(new Color(240,240,240));}
		if (ev.getSource() == cbPiso){cbPiso.setBackground(new Color(240,240,240));}
		if (ev.getSource() == cbEstado){cbEstado.setBackground(new Color(240,240,240));}
		if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
		//FORE
		if (ev.getSource() == textField1){textField1.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == textField1){textField1.setForeground(Menu.textColorForegroundInactivo);} 
		if (ev.getSource() == textFieldBuscar){textFieldBuscar.setForeground(Menu.textColorForegroundInactivo);} 
		if (ev.getSource() == cbTipoHabitacion){cbTipoHabitacion.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == cbPiso ){cbPiso.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == cbEstado){cbEstado.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
	}
	@Override
	public void keyReleased(KeyEvent evet) {
		// TODO ESTE METODO ES PARA MAYUSCULAS
		
		
		char e=evet.getKeyChar();
		// TIPO HABITACION
		if (evet.getSource() == cbTipoHabitacion){
			if (e==KeyEvent.VK_ENTER){	
				cbPiso.requestFocus();
			}
		}
		// PISO HABITACION  
		if (evet.getSource() == cbPiso){
			if (e==KeyEvent.VK_ENTER){	
				cbEstado.requestFocus();
			}
		}
		// ESTADO
		if (evet.getSource() == cbEstado){
			if (e==KeyEvent.VK_ENTER){	
				textArea.requestFocus();
				textArea.selectAll();
			}
		}
		// FECHA 
		if (evet.getSource() == textFieldFormatt){						 
			if (e==KeyEvent.VK_ENTER || textFieldFormatt.getText().toLowerCase().length()==12){
				buttonBuscar.requestFocus();
				}else if (textFieldFormatt.getText().toLowerCase().length()>12) {	
					buttonGrabar.doClick();
				}
		}
		// DESCRIPCION 
		if (evet.getSource() ==textArea){
			int pos = textArea.getCaretPosition();textArea.setText(textArea.getText().toUpperCase());textArea.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
			if (textArea.getText().toLowerCase().length()>290){
				textArea.requestFocus();
				textArea.selectAll();
				textArea.setText(null);
				}
				else if (e==KeyEvent.VK_ENTER || textArea.getText().toLowerCase().length()==290){
					//button[0].doClick();
				}
		} 
		// BUSCAR X NUMERO
		if (evet.getSource() == textFieldBuscar){						 
			if (e==KeyEvent.VK_ENTER || textFieldBuscar.getText().toLowerCase().length()==6){
				buscar();
				}else if (textFieldBuscar.getText().toLowerCase().length()>6) {	
					buttonBuscar.requestFocus();
				}
		}
	}
	
	@Override
	public void keyPressed (KeyEvent evet) {
		
	}
	public void keyTyped(KeyEvent evet) {
		// PRECIONA EL TECLADO Y ME DA EL EVENTO
		char e=evet.getKeyChar();
		if (evet.getSource() == textField1){ // PISO
			if(!Character.isDigit(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
		}
		if (evet.getSource() == textFieldBuscar){ // BUSCAR POR NRO
			if(!Character.isDigit(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
		}
	}
	
	
	void ordenar_Hab() {
		// CONSULTO EL ORDEN DE LA HABITACION  ::::::::::::::::::::::::::::::::
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select OrdenarHab from HABITACION order by OrdenarHab desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("OrdenarHab"))+1);
				N_ORDEN=id;
			}
			statement.close();
		} catch (Exception e) {}
		// CONSULTO EL ORDEN DE LA HABITACION :::::::::::::::::::::::::::::::::
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void formatUp () {
		formatId =NumberFormat.getNumberInstance();
		formatId.setMaximumFractionDigits(3);
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
		//for(int i=0;i<textFieldFormatt.length;i++) {
			
		//	if(i==0) {
				formatId =NumberFormat.getNumberInstance();
				formatId.setMaximumFractionDigits(3);
			//}else if(i==0) {
				
			//}
	//	}
		
	
				Object source = e.getSource();
				if (source==textFieldFormatt.getText()) {
					//fecha = ((Number) textFieldFormatt[0].getValue()).doubleValue();
				}
						
	}
	
	

	
	
	

}
