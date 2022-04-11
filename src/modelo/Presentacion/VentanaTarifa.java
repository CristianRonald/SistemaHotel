	
	package modelo.Presentacion;
	
	import java.awt.Color;
	import java.awt.Component;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.FocusEvent;
	import java.awt.event.FocusListener;
	import java.awt.event.KeyEvent;
	import java.awt.event.KeyListener;
	import java.awt.event.MouseAdapter;
	import java.awt.event.MouseEvent;
	import java.awt.event.MouseListener;
	import java.beans.PropertyChangeEvent;
	import java.beans.PropertyChangeListener;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	
	import javax.swing.ImageIcon;
	import javax.swing.JButton;
	import javax.swing.JComboBox;
	import javax.swing.JFormattedTextField;
	import javax.swing.JInternalFrame;
	import javax.swing.JLabel;
	import javax.swing.JMenuItem;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JPopupMenu;
	import javax.swing.JScrollPane;
	import javax.swing.JSeparator;
	import javax.swing.JTable;
	import javax.swing.SwingConstants;
	import javax.swing.UIManager;
	import javax.swing.border.TitledBorder;
	import javax.swing.table.DefaultTableModel;
	
	import modelo.Clases.TableCellRendererColor;
	import modelo.Datos.ConexionDB;
	import javax.swing.JCheckBox;
	
	public class VentanaTarifa implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public JInternalFrame 	frame;
		private JPanel  panelDto = new JPanel();JPanel  panelBtn = new JPanel();JPanel  panelLst = new JPanel();
		private JFormattedTextField textPrecio,textPrecio2;
	
	
		private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl6,lblms;
		private JButton  			buttonNuevo,buttonCancelar,buttonGrabar,buttonEditar,buttonEliminar,buttonSalir,
									buttonPrimero,buttonUltimo,buttonSiguiente,buttonAnterior;
		
		public JComboBox<String> cbTipo = new JComboBox<String>();private JComboBox<String> cbNumero = new JComboBox<String>();private JComboBox<String> cbTarifa = new JComboBox<String>();
		private JScrollPane scrollTable;
		private JTable tableList;
		private DefaultTableModel model;
		
		JCheckBox chckbxRegGrupal;
		
		private String consultar="";
	private String ID_TAR="";
	private int MOD=0,DEL=0;
	
	public VentanaTarifa() {
		frameTarifa();
		crearPanel();
		crearButtons();
		crearTable();
		crearComboBox();
		crearTextFields();
		crearLabels();
		llenarcbTipo();
		//llenarcbNumero();
		llenarTarifa();
		activarButton(true);
		
		Menu.Desktop.add(frame);
	    int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
	    int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
		frame.setLocation(x, y);
		MOD=1;
		DEL=1;
	}
	
	public void frameTarifa() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/menu-tarifa.png")));
		frame.setTitle("Gestión de tarifas...");
		frame.setClosable(true);
		frame.setBounds(100, 100, 591, 523);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDto.setBounds(10, 65, 555, 60);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
	
		panelBtn.setBorder(new TitledBorder(null, "Controles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBtn.setBounds(10, 11, 555, 52);
		frame.getContentPane().add(panelBtn);
		panelBtn.setLayout(null);
		
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 124, 555, 358);
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
	}
	public void crearLabels(){
		lbl1= new JLabel("Tipo Habitación:");
		lbl1.setBounds(75, 15, 101, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Nro:");
		lbl2.setBounds(218, 15, 39, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
	
		lbl3= new JLabel("Tipo Tarifa:");
		lbl3.setBounds(300, 15, 101, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Precio S/.:");
		lbl4.setBounds(415, 15, 59, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);
	
		lbl5= new JLabel("Precio 2 S/.:");
		lbl5.setBounds(484, 15, 61, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);
		
		lbl6= new JLabel("Nacionalidad:");
		lbl6.setBounds(20, 140, 106, 14);
		panelDto.add(lbl6);
		lbl6.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl6.setFont(Menu.fontLabel);
		
		lblms= new JLabel("<html>Ud. puede ingresar y actualizar las TARIFAS de manera INDIVIDUAL y GRUPAL<br>para grupal seleccione la casilla </html>");
		lblms.setForeground(new Color(139, 0, 0));
		lblms.setBounds(95, 8, 218, 40);
		panelBtn.add(lblms);
		lblms.setHorizontalAlignment(SwingConstants.LEFT);
		lblms.setFont(Menu.fontLabel);
	}
	public void crearTextFields() {
		textPrecio = new JFormattedTextField();
		textPrecio.setColumns(10);
		textPrecio.setFont(Menu.fontText);
		textPrecio.setHorizontalAlignment(SwingConstants.CENTER);
		textPrecio.addActionListener(this);
		textPrecio.addFocusListener(this);
		textPrecio.addKeyListener(this);
		textPrecio.setBounds(407, 29, 67, 22);
		textPrecio.setForeground(new Color(30, 144, 255));
		panelDto.add(textPrecio);
		
		textPrecio2 = new JFormattedTextField();
		textPrecio2.setColumns(10);
		textPrecio2.setFont(Menu.fontText);
		textPrecio2.setHorizontalAlignment(SwingConstants.CENTER);
		textPrecio2.addActionListener(this);
		textPrecio2.addFocusListener(this);
		textPrecio2.addKeyListener(this);
		textPrecio2.setBounds(478, 29, 67, 22);
		textPrecio2.setForeground(new Color(30, 144, 255));
		panelDto.add(textPrecio2);
	
	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setToolTipText("Nuevo ítem");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setBounds(318, 20, 36, 23);
		buttonNuevo.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/nuevo.png")));
		panelBtn.add(buttonNuevo);
		
		buttonCancelar= new JButton("");
		buttonCancelar.setToolTipText("Deshacer ítem");
		buttonCancelar.addActionListener(this);
		buttonCancelar.setBounds(356, 20, 36, 23);
		buttonCancelar.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/undo.png")));
		panelBtn.add(buttonCancelar);
		
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(394, 20, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/save.png")));
		panelBtn.add(buttonGrabar);
	
		buttonEditar= new JButton("");
		buttonEditar.setToolTipText("Modificar ítem");
		buttonEditar.addActionListener(this);
		buttonEditar.setBounds(432, 20, 36, 23);
		buttonEditar.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/edit.png")));
		panelBtn.add(buttonEditar);
		
		buttonEliminar= new JButton("");
		buttonEliminar.setToolTipText("Eliminar ítem");
		buttonEliminar.addActionListener(this);
		buttonEliminar.setBounds(470, 20, 36, 23);
		buttonEliminar.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/delete.png")));
		panelBtn.add(buttonEliminar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(508, 20, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/Exit.png")));
		panelBtn.add(buttonSalir);
		
		buttonPrimero= new JButton("");
		buttonPrimero.setToolTipText("Primer ítem");
		buttonPrimero.addActionListener(this);
		buttonPrimero.setBounds(405, -14, 36, 23);
		buttonPrimero.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/hide-left.png")));
		panelBtn.add(buttonPrimero);
		buttonPrimero.setVisible(false);
		
		buttonUltimo= new JButton("");
		buttonUltimo.setToolTipText("Anterior ítem");
		buttonUltimo.addActionListener(this);
		buttonUltimo.setBounds(443, -14, 36, 23);
		buttonUltimo.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/navigate-left.png")));
		panelBtn.add(buttonUltimo);
		buttonUltimo.setVisible(false);
		
		buttonSiguiente= new JButton("");
		buttonSiguiente.setToolTipText("Siguiente ítem");
		buttonSiguiente.addActionListener(this);
		buttonSiguiente.setBounds(481, -14, 36, 23);
		buttonSiguiente.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/navigate-right.png")));
		panelBtn.add(buttonSiguiente);
		buttonSiguiente.setVisible(false);
		
		buttonAnterior= new JButton("");
		buttonAnterior.setToolTipText("Ultimo ítem");
		buttonAnterior.addActionListener(this);
		buttonAnterior.setBounds(519, -14, 36, 23);
		buttonAnterior.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/hide-right.png")));
		panelBtn.add(buttonAnterior);
		buttonAnterior.setVisible(false);
		
		chckbxRegGrupal = new JCheckBox("Ope.Grupal");
		chckbxRegGrupal.setBounds(6, 20, 83, 23);
		chckbxRegGrupal.addActionListener(this);
		panelBtn.add(chckbxRegGrupal);
	
	}
	public void crearComboBox() {
		cbTipo = new JComboBox<>();
		cbTipo.setBounds(10, 29, 166, 21);
		cbTipo.setFont(Menu.fontText);
		cbTipo.addActionListener(this);
		cbTipo.addFocusListener(this);
		cbTipo.addKeyListener(this);
		cbTipo.addMouseListener(this);
		cbTipo.addPropertyChangeListener(this);
		panelDto.add(cbTipo);
		
		cbNumero = new JComboBox<>();
		cbNumero.setBounds(178, 29, 79, 21);
		cbNumero.setFont(Menu.fontText);
		cbNumero.addActionListener(this);
		cbNumero.addFocusListener(this);
		cbNumero.addKeyListener(this);
		cbNumero.addMouseListener(this);
		cbNumero.addPropertyChangeListener(this);
		panelDto.add(cbNumero);
		
		cbTarifa = new JComboBox<>();
		cbTarifa.setBounds(259, 29, 142, 21);
		cbTarifa.setFont(Menu.fontText);
		cbTarifa.addActionListener(this);
		cbTarifa.addFocusListener(this);
		cbTarifa.addKeyListener(this);
		cbTarifa.addPropertyChangeListener(this);
		panelDto.add(cbTarifa);
	}
	public void crearTable() {
		tableList = new JTable(); 
		tableList.addMouseListener(this);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	    
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 20, 535, 327);
	    panelLst.add(scrollTable);
	
	    tableList.setShowHorizontalLines(false);
		tableList.setShowVerticalLines(true);
		tableList.setFillsViewportHeight(true);
		tableList.setGridColor(new Color(153,204,255));
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(tableList, popupMenu);
		
		JMenuItem m1 = new JMenuItem("Seleccionar ítem...");
		m1.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/check.png")));
		//m1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));// TECLAS RAPIDAS
		popupMenu.add(m1);
		m1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
			}
		});
		
		JSeparator separator = new JSeparator();
		popupMenu.add(separator);
		
		
		JMenuItem mModi = new JMenuItem("Modificar el ítem");
		mModi.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/edit.png")));
		//mModi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		popupMenu.add(mModi);
		mModi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actualizoUnaTarifa();
			}
		});
		
		JMenuItem mEli = new JMenuItem("Eliminar el ítem");
		mEli.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/delete.png")));
		//mEli.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		popupMenu.add(mEli);
		mEli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DEL=2;// ELIMINO UNA
				delete();
			}
		});
		JSeparator separator1 = new JSeparator();
		popupMenu.add(separator1);
		
		JMenuItem m2 = new JMenuItem("Modificar todo los ítems");
		m2.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/edit.png")));
		//m2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		popupMenu.add(m2);
		m2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cbTipo.getSelectedItem()==null || cbTipo.getSelectedItem()=="%TODOS"){
					JOptionPane.showMessageDialog(null, "Falta selecionar el tipo de habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					cbTipo.requestFocus();
					return;
				}
				if (cbNumero.getSelectedItem()==null || cbNumero.getSelectedItem()!="%TODOS"){
					JOptionPane.showMessageDialog(null, "Selecionar todas las habitaciones",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					cbNumero.requestFocus();
					return;
				}
				if (cbTarifa.getSelectedItem()==null || cbTarifa.getSelectedItem()=="%TODOS"){
					JOptionPane.showMessageDialog(null, "Falta seleccionar su tarifa",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					cbTarifa.requestFocus();
					return;
				}
				tableList.selectAll();
				activarButton(false);
				MOD=1;
				textPrecio.requestFocus();
			}
		});
		
		JMenuItem m3 = new JMenuItem("Eliminar todo los ítems");
		m3.setIcon(new ImageIcon(VentanaTarifa.class.getResource("/modelo/Images/delete.png")));
		//m3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		popupMenu.add(m3);
		m3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cbTipo.getSelectedItem()==null || cbTipo.getSelectedItem()=="%TODOS"){
					JOptionPane.showMessageDialog(null, "Falta selecionar el tipo de habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					cbTipo.requestFocus();
					return;
				}
				if (cbNumero.getSelectedItem()==null || cbNumero.getSelectedItem()!="%TODOS"){
					JOptionPane.showMessageDialog(null, "Selecionar todas las habitaciones",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					cbNumero.requestFocus();
					return;
				}
				if (cbTarifa.getSelectedItem()==null || cbTarifa.getSelectedItem()=="%TODOS"){
					JOptionPane.showMessageDialog(null, "Falta seleccionar su tarifa",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					cbTarifa.requestFocus();
					return;
				}
				tableList.selectAll();
				DEL=0;// ELIMINO VARIAS
				delete();
			}
		});
	
	}
	public void limpiarTexts() {
		textPrecio.setText(null);
		textPrecio.setBackground(Menu.textColorBackgroundInactivo);	
		textPrecio.setForeground(Menu.textColorForegroundInactivo);
		
		textPrecio2.setText(null);
		textPrecio2.setBackground(Menu.textColorBackgroundInactivo);	
		textPrecio2.setForeground(Menu.textColorForegroundInactivo);
	    panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	    
	    llenarTable();
	    ID_TAR="";
	}
	public void activarTexts(boolean b) {
		textPrecio.setEnabled(b);
		textPrecio2.setEnabled(b);
	}
	public void activarButton(boolean c) {
		 if (c == true){
			buttonNuevo.setEnabled(true); // NUEVO
			buttonCancelar.setEnabled(false);// CALCELAR
			buttonGrabar.setEnabled(false);// GRABAR
			buttonEditar.setEnabled(true);	// EDITAR
			buttonEliminar.setEnabled(true);	// ELIMINAR
			buttonSalir.setEnabled(true);	// SALIR
			
			buttonPrimero.setEnabled(true);	// PRIMERO
			buttonAnterior.setEnabled(true); // ANTERIOR
			buttonSiguiente.setEnabled(true);	// SIGUIENTE
			buttonUltimo.setEnabled(true);	// ULTIMO
	
		 }
		 if (c == false){
			buttonNuevo.setEnabled(false); // NUEVO
			buttonCancelar.setEnabled(true);// CALCELAR
			buttonGrabar.setEnabled(true);// GRABAR
			buttonEditar.setEnabled(false);	// EDITAR
			buttonEliminar.setEnabled(false);	// ELIMINAR
			buttonSalir.setEnabled(false);	// SALIR
			
			buttonPrimero.setEnabled(false);	// PRIMERO
			buttonAnterior.setEnabled(false); // ANTERIOR
			buttonSiguiente.setEnabled(false);	// SIGUIENTE
			buttonUltimo.setEnabled(false);	// ULTIMO
		 
		 }
	}
	public void focusGained(FocusEvent ev) {
		if (ev.getSource() == textPrecio){textPrecio.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == textPrecio2){textPrecio2.setBackground(Menu.textColorBackgroundActivo);} 
		if (ev.getSource() == cbTipo){cbTipo.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == cbNumero){cbNumero.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == cbTarifa){cbTarifa.setBackground(Menu.textColorBackgroundActivo);}
		
		//FORE
		if (ev.getSource() == textPrecio){textPrecio.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == textPrecio2){textPrecio2.setForeground(Menu.textColorForegroundActivo);} 
		if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == cbNumero){cbNumero.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == cbTarifa){cbTarifa.setForeground(Menu.textColorForegroundActivo);}
	}
	public void focusLost(FocusEvent ev) {
		if (ev.getSource() == textPrecio){textPrecio.setBackground(Menu.textColorBackgroundInactivo);}
		if (ev.getSource() == textPrecio2){textPrecio2.setBackground(Menu.textColorBackgroundInactivo);} 
		if (ev.getSource() == cbTipo){cbTipo.setBackground(new Color(240,240,240));}
		if (ev.getSource() == cbNumero){cbNumero.setBackground(new Color(240,240,240));}
		if (ev.getSource() == cbTarifa){cbTarifa.setBackground(new Color(240,240,240));}
		//FORE
		if (ev.getSource() == textPrecio){textPrecio.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == textPrecio2){textPrecio2.setForeground(Menu.textColorForegroundInactivo);} 
		if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == cbNumero){cbNumero.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == cbTarifa){cbTarifa.setForeground(Menu.textColorForegroundInactivo);}
	}
	public void llenarcbTipo() {
		String consul="";
		consul="Select* from TIPO_HABITACION order by Tipo_Hab asc";
		cbTipo.removeAllItems();
		cbTipo.addItem("%TODOS");
		conexion = new ConexionDB();
		try {
			Statement statement =conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(consul);
			while (resultSet.next()==true) {
				cbTipo.addItem(resultSet.getString("Tipo_Hab"));
			}
			cbTipo.setSelectedIndex(0);
			statement.close();
			resultSet.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		conexion.DesconectarDB();
	}
	
	public void llenarcbNumero() {
		cbNumero.removeAllItems();
		cbNumero.addItem("%TODOS");
		String consul="";
		conexion = new ConexionDB();
		if (cbTipo.getSelectedItem()=="%TODOS") {
			consul ="Select* from HABITACION  order by NumeroHab asc";}
		else {
			consul ="Select* from HABITACION where Tipo_Hab ='" + cbTipo.getSelectedItem() + "' order by NumeroHab asc";}
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet rs = statement.executeQuery(consul);
			while (rs.next()==true) {
				cbNumero.addItem(rs.getString("NumeroHab"));
			}
			rs.close();
			statement.close();
			cbNumero.setSelectedIndex(-1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		cbNumero.setSelectedIndex(-1);
		conexion.DesconectarDB();
	}
	public void llenarTarifa(){
		cbTarifa.removeAllItems();
		cbTarifa.addItem("%TODOS");
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select* from TIPO_TARIFA");
			while(resultSet.next()==true) {
				cbTarifa.addItem(resultSet.getString("Tipotar"));
			}
			cbTarifa.setSelectedIndex(-1);
			statement.close();
			resultSet.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	protected void llenarTable() {
		conexion = new ConexionDB();
		LimpiarTable();
	    try {
	 	   int totalitems=0;
	 	   model= new DefaultTableModel();
		   model.addColumn("Código");
		   model.addColumn("Tipo habitación");
		   model.addColumn("Nro");
		   model.addColumn("Tipo de tarifa");
		   model.addColumn("Precio S/.");
	       model.addColumn("Precio2 S/.");
	       model.addColumn("Fecha");
	       
		   String []datos= new String[7];
		   Statement st = conexion.gConnection().createStatement();
		   ResultSet rs=st.executeQuery(consultar);
		   tableList.setModel(model);
		   
		   tableList.getColumnModel().getColumn(0).setPreferredWidth(70);
		   tableList.getColumnModel().getColumn(1).setPreferredWidth(120);
		   tableList.getColumnModel().getColumn(2).setPreferredWidth(35);
		   tableList.getColumnModel().getColumn(3).setPreferredWidth(140);
		   tableList.getColumnModel().getColumn(4).setPreferredWidth(65);
		   tableList.getColumnModel().getColumn(5).setPreferredWidth(65);
	
		   while(rs.next()) {
		    	datos[0]=Menu.formatid_9.format(rs.getInt("Id_Tar"));
		    	datos[1]=rs.getString("Tipo_Hab");
		    	datos[2]=rs.getString("NumeroHab");
	            datos[3]=rs.getString("TipoTar");
	            datos[4]=Menu.formateadorCurrency.format(rs.getFloat("PrecioTar"))+" ";
	            datos[5]=Menu.formateadorCurrency.format(rs.getFloat("Precio2Tar"))+" ";
	            datos[6]=Menu.formatoFechaString.format(rs.getDate("FechaActualizarTar"));
	            totalitems=totalitems+1;
	            model.addRow(datos);
	            tableList.setModel(model);
	        }
		   st.close();
		   rs.close();
		   // MODELO TABLE
		   int CONT=19;
		   if (totalitems>0) {
			   int c=0;
	            c=CONT-totalitems;
	            if ( CONT>totalitems) {
				    String []registros= new String[c];
				    for (int n=0; n<c;n++) {
					    model.addRow(registros);
					    tableList.setModel(model);
				    }
	            } 
		   }else{
			    String []registros= new String[CONT];
			    for (int n=0; n<CONT;n++) {
				    model.addRow(registros);
				    tableList.setModel(model);
			    }
		   }
		   // FIN MODELOTABLE
	       tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("TARIFAS"));//ESTABLESCO COLOR CELDAS
	       	
	       	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		conexion.DesconectarDB();
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	
	//METODO GRABAR =================
	public void insertar() {
		if (cbTipo.getSelectedItem()==null || cbTipo.getSelectedItem()=="%TODOS"){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el tipo de habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbTipo.requestFocus();
			return;
		}
		if (cbNumero.getSelectedItem()==null || cbNumero.getSelectedItem()=="%TODOS"){
			//JOptionPane.showMessageDialog(null, "Falta seleccionar el número",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			//cbNumero.requestFocus();
			//return;
		}
		if (cbNumero.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Falta seleccionar la habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbNumero.requestFocus();
			return;
		}
		if (cbTarifa.getSelectedItem()==null || cbTarifa.getSelectedItem()=="%TODOS"){
			JOptionPane.showMessageDialog(null, "Falta seleccionar su tarifa",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbTarifa.requestFocus();
			return;
		}
		if (textPrecio.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textPrecio.requestFocus();
			return;
		}
		if (textPrecio2.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textPrecio2.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from TARIFAS where NumeroHab='"+cbNumero.getSelectedItem()+"'and TipoTar='"+cbTarifa.getSelectedItem()+"'");
			if (resultSet.next()== true) {
				JOptionPane.showMessageDialog(null, "El ítem ya fue registrado",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
				cbTipo.requestFocus(true);
			}else{
				try {
				
					// ********************************************************************************GRABA TODOS
					if (cbTipo.getSelectedItem()!="%TODOS" && cbNumero.getSelectedItem()=="%TODOS" && cbTarifa.getSelectedItem()!="%TODOS"){
						Statement st = conexion.gConnection().createStatement();
						ResultSet rs = st.executeQuery("Select* from HABITACION where Tipo_Hab='"+cbTipo.getSelectedItem()+"'"); 
						String sql ="INSERT INTO TARIFAS (Id_Tar,NumeroHab,TipoTar,PrecioTar,Precio2Tar,fechaActualizarTar) VALUES (?,?,?,?,?,?)";
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						
						String MsjHab="";
						while (rs.next()==true) {
							// CONSULTO SI ESTA REGITRADO
							Statement s = conexion.gConnection().createStatement();
							ResultSet r = s.executeQuery("Select * from TARIFAS where NumeroHab='"+rs.getString("NumeroHab")+"'and TipoTar='"+cbTarifa.getSelectedItem()+"'");
							
							if (r.next()==true) {
								//JOptionPane.showMessageDialog(null, "El ítem "+rs.getString("NumeroHab")+" "+cbTarifa.getSelectedItem()+" ya fue registrado ",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
								cbTarifa.requestFocus(true);
							}else{
								// CONSULTO EL CODIGO DEL ALQUILER ::::::::::::::::::::::::::::::::
								int ID=0;
								Statement state = conexion.gConnection().createStatement();
								ResultSet resul = state.executeQuery("Select Id_Tar from TARIFAS order by Id_Tar desc limit 0,1");
								if (resul.next()== true) {
									int id=(Integer.parseInt(resul.getString("Id_Tar"))+1);
									ID=id;
								}else{
									ID=1;
								}
								resul.close();
								state.close();
								// CONSULTO EL CODIGO DEL ALQUILER :::::::::::::::::::::::::::::::::
								
								MsjHab=MsjHab + " #"+ rs.getString("NumeroHab");
								
								ps.setInt(1, ID);
								ps.setString(2, rs.getString("NumeroHab"));
								ps.setString(3, (String)cbTarifa.getSelectedItem());
								ps.setDouble(4, Double.parseDouble(textPrecio.getText()));
								ps.setDouble(5, Double.parseDouble(textPrecio2.getText()));
								ps.setString(6, (Menu.date).trim());
								ps.execute();
							}
							r.close();
							s.close();
						}
						if (MsjHab!="") {
							JOptionPane.showMessageDialog(null, "Datos grabados con éxito" +Menu.separador + "LA TARIFA "+cbTarifa.getSelectedItem() +" ha sido asignada a las sgt. habitaciones "+Menu.separador +MsjHab ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						}
						if (MsjHab=="") {
							JOptionPane.showMessageDialog(null, "Habitaciones ya han sido registradas con" +Menu.separador + "LA TARIFA "+cbTarifa.getSelectedItem() ,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
						}
						ps.close();
						rs.close();
						limpiarTexts();
						activarButton(true);
						buttonNuevo.requestFocus(true);
	
					// ********************************************************************************* FIN GRABA TODOS
					 } else{
						// CONSULTO EL CODIGO DEL ALQUILER ::::::::::::::::::::::::::::::::
						int ID=0;
						Statement state = conexion.gConnection().createStatement();
						ResultSet resul = state.executeQuery("Select Id_Tar from TARIFAS order by Id_Tar desc limit 0,1");
						if (resul.next()== true) {
							int id=(Integer.parseInt(resul.getString("Id_Tar"))+1);
							ID=id;
						}else{
							ID=1;
						}
						resul.close();
						state.close();
						// CONSULTO EL CODIGO DEL ALQUILER :::::::::::::::::::::::::::::::::uevo();
						String sql ="INSERT INTO TARIFAS (Id_Tar,NumeroHab,TipoTar,PrecioTar,Precio2Tar,fechaActualizarTar) VALUES (?,?,?,?,?,?)";
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setInt(1, ID);
						ps.setString(2, (String)cbNumero.getSelectedItem());
						ps.setString(3, (String)cbTarifa.getSelectedItem());
						ps.setDouble(4, Double.parseDouble(textPrecio.getText()));
						ps.setDouble(5, Double.parseDouble(textPrecio2.getText()));
						ps.setString(6, (Menu.date).trim());
						ps.execute();
						ps.close();
						JOptionPane.showMessageDialog(null, "Datos grabados con éxito" +Menu.separador + "LA TARIFA "+cbTarifa.getSelectedItem() +" ha sido asignada a la habitación "+Menu.separador +"#"+ cbNumero.getSelectedItem() ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						limpiarTexts();
						activarButton(true);
						buttonNuevo.requestFocus(true);
					}
					buttonNuevo.doClick();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
		conexion.DesconectarDB();
	}
	//METODO ACTUALIZA =================
	public void actualizar() {
		if (cbTipo.getSelectedItem()==null || cbTipo.getSelectedItem()=="%TODOS"){
			JOptionPane.showMessageDialog(null, "Falta seleccionar su tipo de habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbTipo.requestFocus();
			return;
		}
		if (cbNumero.getSelectedItem()==null || cbNumero.getSelectedItem()=="%TODOS"){
			//JOptionPane.showMessageDialog(null, "Falta seleccionar su número",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			//cbNumero.requestFocus();
			//return;
		}
		if (cbNumero.getSelectedItem()==null){
			JOptionPane.showMessageDialog(null, "Falta seleccionar la habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbNumero.requestFocus();
			return;
		}
		if (cbTarifa.getSelectedItem()==null || cbTarifa.getSelectedItem()=="%TODOS"){
			JOptionPane.showMessageDialog(null, "Falta seleccionar su tarifa",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbTarifa.requestFocus();
			return;
		}
		if (textPrecio.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textPrecio.requestFocus();
			return;
		}
		if (textPrecio2.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textPrecio2.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
			// ********************************************************************************  ACTUALIZA UNA
			if (MOD==1) {
		         String sql="UPDATE TARIFAS SET NumeroHab = ?,"
		                 + "TipoTar = ?,"
		                 + "PrecioTar = ?,"
		                 + "Precio2Tar = ?,"
		                 + "fechaActualizarTar =?"
		                 + "WHERE Id_Tar = '"+ID_TAR+"'"; 
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setString(1, (String)cbNumero.getSelectedItem());
				ps.setString(2, (String)cbTarifa.getSelectedItem());
				ps.setDouble(3, Double.parseDouble(textPrecio.getText()));
				ps.setDouble(4, Double.parseDouble(textPrecio2.getText()));
				ps.setString(5, (Menu.date).trim());
				ps.executeUpdate();
				ps.close();
				JOptionPane.showMessageDialog(null, "Datos actualizados con éxito" + Menu.separador  +"LA TARIFA "+cbTarifa.getSelectedItem() + " ha sido actualizada, habitación afectada "+Menu.separador +"#"+ cbNumero.getSelectedItem(),Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				limpiarTexts();
				activarButton(true);buttonNuevo.requestFocus(true);
			}
			// ********************************************************************************* FIN ACTUALIZA UNA
			
			// ******************************************************************************** ACTUALIZA TODOS
			if (MOD==2) {
					if (cbTipo.getSelectedItem()!="%TODOS" && cbNumero.getSelectedItem()=="%TODOS" && cbTarifa.getSelectedItem()!="%TODOS"){
					Statement st = conexion.gConnection().createStatement();
					ResultSet rs = st.executeQuery("Select* from HABITACION as H,TARIFAS as T where T.NumeroHab=H.NumeroHab and H.Tipo_Hab='"+cbTipo.getSelectedItem()+"'and T.TipoTar='"+cbTarifa.getSelectedItem()+"'"); 
					String MsjHab="";
					while (rs.next()==true) {
						MsjHab=MsjHab + " #"+ rs.getString("NumeroHab");
						String sql="UPDATE TARIFAS SET TipoTar = ?,"
				                 + "PrecioTar = ?,"
				                 + "Precio2Tar = ?,"
				                 + "fechaActualizarTar =?"
				                 + "WHERE Id_Tar = '"+rs.getString("Id_Tar")+"'";
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setString(1, (String)cbTarifa.getSelectedItem());
						ps.setDouble(2, Double.parseDouble(textPrecio.getText()));
						ps.setDouble(3, Double.parseDouble(textPrecio2.getText()));
						ps.setString(4, (Menu.date).trim());
						
						ps.executeUpdate();
						ps.close();
					}
					rs.close();
					st.close();
					JOptionPane.showMessageDialog(null, "Datos actualizados con éxito" + Menu.separador  +"LA TARIFA "+cbTarifa.getSelectedItem() + " ha sido actualizada, habitaciones afectadas "+Menu.separador +MsjHab,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					limpiarTexts();
					activarButton(true);
					buttonNuevo.requestFocus(true);
				}
			}
			// ********************************************************************************* FIN ACTUALIZA TODOS
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		conexion.DesconectarDB();
	}
	
	//METODO ELIMINAR =================
	public void delete() {
		conexion = new ConexionDB();
		activarButton(true);
		if (tableList.getSelectedRows().equals("")) {
			JOptionPane.showMessageDialog(null, "La lista se encuentra vacía",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		String Msj="";
		try {
			Statement s = conexion.gConnection().createStatement();
			ResultSet r = s.executeQuery("Select* from HABITACION as H,TARIFAS as T where T.NumeroHab=H.NumeroHab and H.Tipo_Hab='"+cbTipo.getSelectedItem()+"'and T.TipoTar='"+cbTarifa.getSelectedItem()+"'"); 
			while (r.next()==true) {
				Msj=Msj + " #"+ r.getString("NumeroHab");
			}
		} catch (Exception e) { e.printStackTrace(); }

		int respuesta=0;
		if (DEL==2) {
			if (cbTipo.getSelectedItem()==null || cbTipo.getSelectedItem()=="%TODOS"){
				JOptionPane.showMessageDialog(null, "Elija el grupo o tipo de habitación",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbTipo.requestFocus();
				return;
			}
			if (cbNumero.getSelectedItem()==null || cbNumero.getSelectedItem()!="%TODOS"){
				JOptionPane.showMessageDialog(null, "Elija la opción %TODOS",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbNumero.requestFocus();
				return;
			}
			if (cbTarifa.getSelectedItem()==null || cbTarifa.getSelectedItem()=="%TODOS"){
				JOptionPane.showMessageDialog(null, "Elija la tarifa que desea eliminar",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbTarifa.requestFocus();
				return;
			}
			if (Msj.equals(null)||Msj.equals("")) {
				JOptionPane.showMessageDialog(null, "La lista se encuentra vacía",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar los ítems "+ Menu.separador + Msj + " ?", Menu.SOFTLE_HOTEL,	
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		} else{
			if (tableList.getSelectedRow() == -1 || ID_TAR=="") {
				JOptionPane.showMessageDialog(null, "Seleccionar un ítem de la lista",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				return;
			}
			 respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem "+ Menu.separador + ID_TAR + " ?", Menu.SOFTLE_HOTEL,	
			 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}
		if (respuesta == JOptionPane.YES_OPTION) {
			try {
				if (DEL==1) {
					// ******************************************************************************** ELIMINAR UNA
					Statement statement = conexion.gConnection().createStatement();
					String query="Delete from TARIFAS where Id_Tar ='"+ID_TAR+"'";
					statement.execute(query);
					JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					statement.close();
					limpiarTexts();
	
					buttonEliminar.setSelected(true);
					// ********************************************************************************* FIN ELIMINAR UNA
				}
				if (DEL==2) {
					// ******************************************************************************** ELIMINAR TODOS
					if (cbTipo.getSelectedItem()!="%TODOS" && cbNumero.getSelectedItem()=="%TODOS" && cbTarifa.getSelectedItem()!="%TODOS"){
						Statement st = conexion.gConnection().createStatement();
						ResultSet rs = st.executeQuery("Select* from HABITACION as H,TARIFAS as T where T.NumeroHab=H.NumeroHab and H.Tipo_Hab='"+cbTipo.getSelectedItem()+"'and T.TipoTar='"+cbTarifa.getSelectedItem()+"'"); 
						String MsjHab="";
						while (rs.next()==true) {
							MsjHab=MsjHab + " #"+ rs.getString("NumeroHab");
							Statement ps = conexion.gConnection().createStatement();
							String query="Delete from TARIFAS where Id_Tar ='"+rs.getString("Id_Tar")+"'";
							ps.execute(query);
							ps.close();
						}
						JOptionPane.showMessageDialog(null, "Datos eliminados con éxito,  tarifas eliminadas "+Menu.separador +MsjHab +" TARIFA "+cbTarifa.getSelectedItem(),Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						limpiarTexts();
						//activarButton(true);
						buttonEliminar.setSelected(true);
						st.close();
					// ********************************************************************************* FIN ELIMINAR TODOS
					}
				}
			} catch (Exception e) { e.printStackTrace(); }
		}else if (respuesta == JOptionPane.NO_OPTION) {
		}
		conexion.DesconectarDB();	
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evet) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		char e=evet.getKeyChar();
		if (evet.getSource() == cbTipo){
			if (e==KeyEvent.VK_ENTER){
				if (cbTipo.getSelectedIndex()!=-1){
					cbNumero.requestFocus();
					llenarcbNumero();
					cbTarifa.setSelectedItem("%TODOS");
					llenarTable();
				}else{
					cbTipo.requestFocus();
				}
			}
		}
		if (evet.getSource() == cbNumero){
			if (e==KeyEvent.VK_ENTER){
				if (cbNumero.getSelectedIndex()!=-1){
					cbTarifa.requestFocus();
				}else{
					cbNumero.requestFocus();
				}
			}
		} 
		if (evet.getSource() == cbTarifa){
			if (e==KeyEvent.VK_ENTER){
				if (cbTarifa.getSelectedIndex()!=-1){
					textPrecio.requestFocus();
				}else{
					cbTarifa.requestFocus();
				}
			}
		} 
		
		
		if (evet.getSource() ==textPrecio){
			if (textPrecio.getText().toLowerCase().isEmpty()|| textPrecio.getText().toLowerCase().length()>7){
				textPrecio.requestFocus();
				textPrecio.selectAll();
				textPrecio.setText(null);
			} else if (e==KeyEvent.VK_ENTER || textPrecio.getText().toLowerCase().length()==7){
				textPrecio2.requestFocus();
				textPrecio2.selectAll();
				}
		} 
		if (evet.getSource() ==textPrecio2){
			if (textPrecio2.getText().toLowerCase().isEmpty()|| textPrecio2.getText().toLowerCase().length()>7){
				textPrecio2.requestFocus();
				textPrecio2.selectAll();
				textPrecio2.setText(null);
			} else if (e==KeyEvent.VK_ENTER || textPrecio2.getText().toLowerCase().length()==7){
				buttonGrabar.requestFocus();
				buttonGrabar.doClick();
				}
		} 
	}
	
	@Override
	public void keyReleased(KeyEvent evet) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void keyTyped(KeyEvent evet) {
		// TODO Auto-generated method stub
		char car=evet.getKeyChar();
		
		if (evet.getSource().equals(textPrecio)) {
			//if (textPrecio.getText().length() >= 8 ) {evet.consume();}
			if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
		}
		if (evet.getSource().equals(textPrecio2)) {
			//if (textDsct.getText().length() >= 4 ) {evet.consume();}
			if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent action) {
		// TODO Auto-generated method stub
		if (action.getSource().equals(buttonNuevo)) {
			  limpiarTexts();
			  activarButton(false);
			  activarTexts(true);
			  MOD=0;
			  //DEL=0;
			  cbTipo.requestFocus();
			  chckbxRegGrupal.setEnabled(false);
		}
		if (action.getSource().equals(buttonCancelar)) {
			  limpiarTexts();
			  activarButton(true);
			  buttonNuevo.requestFocus(true);
			  MOD=0;
			  //DEL=0;
			  chckbxRegGrupal.setEnabled(true);
		}
		if (action.getSource().equals(buttonGrabar)) {
			  if (MOD==0){	  
				  insertar();
			  }else{
				 actualizar();
			  	}
		}
		if (action.getSource().equals(buttonEditar)) {
			actualizoUnaTarifa();
		}
		if (action.getSource().equals(buttonEliminar)) {
			delete();
		}
		if (action.getSource().equals(buttonSalir)) {
			frame.dispose();
		}
		if (action.getSource().equals(chckbxRegGrupal)) {
			if (chckbxRegGrupal.isSelected()) {
				MOD=2;// ACTUALIZO VARIAS
				DEL=2;// ELIMINO VARIAS
			}else{
				MOD=1;// ACTUALIZO UNA
				DEL=1;// ELIMINO UNA
			}
		}
	
		if (action.getSource().equals(cbTipo)) {
			llenarcbNumero();
			//cbTarifa.setSelectedItem("%TODOS");
			if(cbTipo.getSelectedItem()=="%TODOS") {
				consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab order by TipoTar asc";
			}
			else if (cbTipo.getSelectedItem()!="%TODOS") {
				consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and Tipo_Hab ='"+cbTipo.getSelectedItem()+"'order by TipoTar asc";
				}
			llenarTable();
		}
		if (action.getSource().equals(cbNumero)) {
			if	(cbNumero.getSelectedItem()!=null){
				if	(cbTipo.getSelectedItem()=="%TODOS"){
					if	(cbNumero.getSelectedItem()=="%TODOS" || cbNumero.getSelectedItem()=="") {
						consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab order by TipoTar asc";
					}
					else if (cbNumero.getSelectedItem()!="%TODOS" || cbNumero.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and HABITACION.NumeroHab ='"+cbNumero.getSelectedItem()+"'order by TipoTar asc";
						}
				}
				if	(cbTipo.getSelectedItem()!="%TODOS"){ 
					if (cbNumero.getSelectedItem()=="%TODOS" || cbNumero.getSelectedItem()=="") {
						consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and Tipo_Hab ='"+cbTipo.getSelectedItem()+"'order by TipoTar asc";
					}
					else if (cbNumero.getSelectedItem()!="%TODOS" || cbNumero.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and Tipo_Hab ='"+cbTipo.getSelectedItem()+"'and HABITACION.NumeroHab ='"+cbNumero.getSelectedItem()+"'order by TipoTar asc";
						}
				}
				llenarTable();
			}
		}
		if (action.getSource().equals(cbTarifa)) { 
			 if (cbTipo.getSelectedItem()=="%TODOS" && cbNumero.getSelectedItem()=="%TODOS"){
				if	(cbTarifa.getSelectedItem()=="%TODOS" || cbTarifa.getSelectedItem()=="") {
					consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab order by TipoTar asc";
				}
				else if	(cbTarifa.getSelectedItem()!="%TODOS" || cbTarifa.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and TARIFAS.TipoTar ='"+cbTarifa.getSelectedItem()+"'order by TipoTar asc";
					}
			 } 
			 if (cbTipo.getSelectedItem()!="%TODOS" && cbNumero.getSelectedItem()!="%TODOS"){ 
				if	(cbTarifa.getSelectedItem()=="%TODOS" || cbTarifa.getSelectedItem()=="") {
					consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and HABITACION.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'and HABITACION.NumeroHab ='"+cbNumero.getSelectedItem()+"'order by TipoTar asc";
					}
					else if	(cbTarifa.getSelectedItem()!="%TODOS" || cbTarifa.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and HABITACION.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'and HABITACION.NumeroHab ='"+cbNumero.getSelectedItem()+"'and TARIFAS.TipoTar ='"+cbTarifa.getSelectedItem()+"'order by TipoTar asc";
					}
			 }
			 if (cbTipo.getSelectedItem()=="%TODOS" && cbNumero.getSelectedItem()!="%TODOS"){ 
				if	(cbTarifa.getSelectedItem()=="%TODOS" || cbTarifa.getSelectedItem()=="") {
					consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and HABITACION.NumeroHab ='"+cbNumero.getSelectedItem()+"'order by TipoTar asc";
					}
					else if	(cbTarifa.getSelectedItem()!="%TODOS" || cbTarifa.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and HABITACION.NumeroHab ='"+cbNumero.getSelectedItem()+"'and TARIFAS.TipoTar ='"+cbTarifa.getSelectedItem()+"'order by TipoTar asc";
					}
			 }
			 if (cbTipo.getSelectedItem()!="%TODOS" && cbNumero.getSelectedItem()=="%TODOS"){
				if	(cbTarifa.getSelectedItem()=="%TODOS" || cbTarifa.getSelectedItem()=="") {
					consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and HABITACION.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'order by TipoTar asc";
					}
					else if	(cbTarifa.getSelectedItem()!="%TODOS" || cbTarifa.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION,TARIFAS where TARIFAS.NumeroHab = HABITACION.NumeroHab and HABITACION.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'and TARIFAS.TipoTar ='"+cbTarifa.getSelectedItem()+"'order by TipoTar asc";
					}
			 }
			 llenarTable();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(tableList)) {
			try {
				ID_TAR=(String) tableList.getValueAt(tableList.getSelectedRow(),0);	
			} catch (Exception e2) {}	
		}
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	
	}
	
	void actualizoUnaTarifa(){
		String tipo,tarifa,numero;
		if (tableList.getSelectedRows().equals("")) {
			JOptionPane.showMessageDialog(null, "La lista se encuentra vacía",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(MOD==1){
			if (ID_TAR=="") {
				JOptionPane.showMessageDialog(null, "Seleccione un ítem de la lista",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				ID_TAR=(String) tableList.getValueAt(tableList.getSelectedRow(),0);	
				tipo=(String) tableList.getValueAt(tableList.getSelectedRow(),1);	
				numero=(String) tableList.getValueAt(tableList.getSelectedRow(),2);	
				tarifa=(String) tableList.getValueAt(tableList.getSelectedRow(),3);
				
				textPrecio.setText((String)tableList.getValueAt(tableList.getSelectedRow(),4).toString().trim().replaceAll(",", ""));
				textPrecio2.setText((String)tableList.getValueAt(tableList.getSelectedRow(),5).toString().trim().replaceAll(",", ""));
		
				cbTipo.setSelectedItem(tipo);
				cbNumero.setSelectedItem(numero);
				cbTarifa.setSelectedItem(tarifa);
				activarButton(false);
				MOD=1;
				textPrecio.requestFocus();textPrecio.selectAll();
				chckbxRegGrupal.setSelected(false);
				chckbxRegGrupal.setEnabled(false);
			} catch (Exception e) {} 
		}
		if(MOD==2){
			if (cbNumero.getSelectedItem()!="%TODOS") {
				JOptionPane.showMessageDialog(null, "Seleccione de 2 a mas habitaciones\npara actualizar de forma grupal",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbNumero.requestFocus();
				return;
			}
			activarButton(false);
			MOD=2;
			textPrecio.requestFocus();textPrecio.selectAll();
			chckbxRegGrupal.setSelected(true);
			chckbxRegGrupal.setEnabled(true);
		}	
	}
	@Override
	public void mousePressed(MouseEvent Mouse_evt) {
		// TODO Auto-generated method stub
		if (Mouse_evt.getSource().equals(tableList)) {
			if (Mouse_evt.getClickCount() == 1) {
				try {ID_TAR=(String) tableList.getValueAt(tableList.getSelectedRow(),0);} catch (Exception e) {} 
			}
			if (Mouse_evt.getClickCount() == 2) {
				buttonEditar.doClick();
			}
		}
		
	}
	
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	
		}
		private static void addPopup(Component component, final JPopupMenu popup) {
			component.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (e.isPopupTrigger()) {
						showMenu(e);
					}
				}
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger()) {
						showMenu(e);
					}
				}
				private void showMenu(MouseEvent e) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			});
		}
	}
