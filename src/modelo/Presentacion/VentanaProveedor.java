package modelo.Presentacion;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import modelo.Datos.ConexionDB;
import javax.swing.JSeparator;


public class VentanaProveedor implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public  JInternalFrame frame;
	private JPanel  			panelDto,panelBtn,panelLst;
	private JLabel				lbl1,lbl2,lbl3,lbl4,lbl5,lbl6,lbl7,lbl8,lbl9,
								lbl10;
	private JTextField 			textCod,textNom,textContacto,textRuc,textDir,textTel,textBus,textHotmail,textWeb;
	private JButton  			buttonNuevo,buttonCancelar,buttonGrabar,buttonEditar,buttonEliminar,buttonSalir,
			 					buttonPrimero,buttonUltimo,buttonSiguiente,buttonAnterior,
			 					buttonOki,buttonImp,buttonReg;
	
	protected JButton buttonBus;
	JComboBox<String> cbBus;
    
	private JScrollPane scrollArea,scrollTable;
	private JTextArea textArea = new JTextArea();
	private JTable tableList;
	private DefaultTableModel model;
	
	public Integer totalitems;
	
	public static String FILTRO_PRV="";
	
	static int openFrameCountProveedor = 0;

	private String consultar="";
	
	protected static int id=0;
	protected static String descripcion="";
	protected static int MOD=0;
	
	public VentanaProveedor() {
		 //AUMENTADOS EL CONTEO DE LAS VENTANAS.
		openFrameCountProveedor ++;
		
		frameProveedor();
		crearPanel();
		crearButtons();
		crearTable();
		crearComboBox();
		crearTextFields();
		crearLabels();
		activarButton(true);
		textCod.setEditable(false);
		llenarcbBuscar();
      	llenarTable("Select * from Proveedor order by RazonSocialPrv asc");
      	
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
    	frame.setLocation(x, y);
    	Menu.Desktop.add(frame);
	}
	public void frameProveedor() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/menu-proveedor.png")));
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				openFrameCountProveedor=0;
			}
		});
		frame.setTitle("Alta de proveedores");
		frame.setClosable(true);
		frame.setBounds(100, 100, 596, 378);
		frame.getContentPane().setLayout(null);
	}

	public void crearPanel() {
		panelBtn = new JPanel();
		panelBtn.setBorder(new TitledBorder(null, "Controles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBtn.setBounds(10, 11, 562, 49);
		frame.getContentPane().add(panelBtn);
		panelBtn.setLayout(null);
		
		panelLst = new JPanel();
		panelLst.setBounds(10, 61, 562, 276);
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
		
		panelDto = new JPanel();
		panelDto.setBounds(10, 61, 562, 276);
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
	}
	
	public void crearLabels() {
		lbl1= new JLabel("Código:");
		lbl1.setBounds(26, 35, 63, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Razón social:");
		lbl2.setBounds(10, 63, 79, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Contacto:");
		lbl3.setBounds(26, 137, 63, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Ruc:");
		lbl4.setBounds(45, 112, 44, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);

		lbl5= new JLabel("Dirección:");
		lbl5.setBounds(10, 87, 79, 14);
		panelDto.add(lbl5);
		lbl5.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl5.setFont(Menu.fontLabel);
		
		lbl6= new JLabel("Hotmail:");
		lbl6.setBounds(10, 163, 79, 14);
		panelDto.add(lbl6);
		lbl6.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl6.setFont(Menu.fontLabel);

		lbl7= new JLabel("Teléfono:");
		lbl7.setBounds(224, 112, 56, 14);
		panelDto.add(lbl7);
		lbl7.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl7.setFont(Menu.fontLabel);
		
		lbl8= new JLabel("Observaciones:");
		lbl8.setBounds(10, 202, 79, 14);
		panelDto.add(lbl8);
		lbl8.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl8.setFont(Menu.fontLabel);
		
		lbl9= new JLabel("Elija el tipo de búsqueda:");
		lbl9.setBounds(50, 229, 137, 14);
		panelLst.add(lbl9);
		lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl9.setFont(Menu.fontLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 225, 537, 4);
		panelLst.add(separator);
		
		lbl10= new JLabel("Web:");
		lbl10.setBounds(329, 163, 36, 14);
		panelDto.add(lbl10);
		lbl10.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl10.setFont(Menu.fontLabel);
		panelDto.setVisible(false);// PANEL DATOS
		panelLst.setVisible(true); // PANEL LISTA
	}
	public void crearTextFields(){
		textCod = new JTextField();
		textCod.setColumns(10);
		textCod.setFont(Menu.fontText);
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.setHorizontalAlignment(SwingConstants.CENTER);
		textCod.addActionListener(this);
		textCod.addFocusListener(this);
		textCod.addKeyListener(this);
		textCod.setBounds(99, 32, 106, 22);
		panelDto.add(textCod);
		
		textNom = new JTextField();
		textNom.setColumns(10);
		textNom.setFont(Menu.fontText);
		textNom.setForeground(Menu.textColorForegroundInactivo);
		textNom.setHorizontalAlignment(SwingConstants.LEFT);
		textNom.addActionListener(this);
		textNom.addFocusListener(this);
		textNom.addKeyListener(this);
		textNom.setBounds(99, 57, 431, 22);
		panelDto.add(textNom);
		
		textContacto = new JTextField();
		textContacto.setColumns(10);
		textContacto.setFont(Menu.fontText);
		textContacto.setForeground(Menu.textColorForegroundInactivo);
		textContacto.setHorizontalAlignment(SwingConstants.LEFT);
		textContacto.addActionListener(this);
		textContacto.addFocusListener(this);
		textContacto.addKeyListener(this);
		textContacto.setBounds(99, 133, 431, 22);
		
		//textDni.setText(Format instanceof = "00000");

		
		panelDto.add(textContacto);
		
		textRuc = new JTextField();
		textRuc.setColumns(10);
		textRuc.setFont(Menu.fontText);
		textRuc.setForeground(Menu.textColorForegroundInactivo);
		textRuc.setHorizontalAlignment(SwingConstants.CENTER);
		textRuc.addActionListener(this);
		textRuc.addFocusListener(this);
		textRuc.addKeyListener(this);
		textRuc.setBounds(99, 108, 128, 22);
		panelDto.add(textRuc);
		
		textDir = new JTextField();
		textDir.setColumns(10);
		textDir.setFont(Menu.fontText);
		textDir.setForeground(Menu.textColorForegroundInactivo);
		textDir.setHorizontalAlignment(SwingConstants.LEFT);
		textDir.addActionListener(this);
		textDir.addFocusListener(this);
		textDir.addKeyListener(this);
		textDir.setBounds(99, 82, 431, 22);
		panelDto.add(textDir);

		
		textTel = new JTextField();
		textTel.setColumns(10);
		textTel.setFont(Menu.fontText);
		textTel.setForeground(Menu.textColorForegroundInactivo);
		textTel.setHorizontalAlignment(SwingConstants.CENTER);
		textTel.addActionListener(this);
		textTel.addFocusListener(this);
		textTel.addKeyListener(this);
		textTel.setBounds(286, 108, 244, 22);
		panelDto.add(textTel);
		
		scrollArea= new JScrollPane();
		scrollArea.setBounds(99, 186, 431, 69);
		panelDto.add(scrollArea);
		textArea = new JTextArea();
		scrollArea.setViewportView(textArea);
		textArea.setFont(Menu.fontText);
		textArea.setForeground(Menu.textColorForegroundInactivo);
		textArea.setWrapStyleWord(true);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);

		textBus = new JTextField();
		textBus.setColumns(10);
		textBus.setFont(Menu.fontText);
		textBus.setForeground(Menu.textColorForegroundInactivo);
		textBus.setHorizontalAlignment(SwingConstants.LEFT);
		textBus.addActionListener(this);
		textBus.addFocusListener(this);
		textBus.addKeyListener(this);
		textBus.addPropertyChangeListener(this);
		textBus.setBounds(191, 243, 231, 22);
		panelLst.add(textBus);
		
		
		/// EMPRESA
		textHotmail = new JTextField();
		textHotmail.setColumns(10);
		textHotmail.setFont(Menu.fontText);
		textHotmail.setForeground(Menu.textColorForegroundInactivo);
		textHotmail.setHorizontalAlignment(SwingConstants.LEFT);
		textHotmail.addActionListener(this);
		textHotmail.addFocusListener(this);
		textHotmail.addKeyListener(this);
		textHotmail.setBounds(99, 159, 232, 22);
		panelDto.add(textHotmail);

		textWeb = new JTextField();
		textWeb.setColumns(10);
		textWeb.setFont(Menu.fontText);
		textWeb.setForeground(Menu.textColorForegroundInactivo);
		textWeb.setHorizontalAlignment(SwingConstants.LEFT);
		textWeb.addActionListener(this);
		textWeb.addFocusListener(this);
		textWeb.addKeyListener(this);
		textWeb.setBounds(377, 159, 153, 22);
		panelDto.add(textWeb);

	}
	
	public void crearComboBox() {
		
		cbBus = new JComboBox<>();
        cbBus.setBounds(10, 243, 176, 21);
        cbBus.setFont(Menu.fontText);
        cbBus.removeAllItems();
        cbBus.addFocusListener(this);
        cbBus.addKeyListener(this);
        panelLst.add(cbBus);
	}
	
	public void llenarcbBuscar() {
        cbBus.removeAllItems();
		String [] lista1 = {"CODIGO","NOMBRE / RAZON SOCIAL","RUC","CONTACTO"};
		for (String llenar:lista1) {
			cbBus.addItem(llenar);
		}
		cbBus.setSelectedIndex(1);
	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setToolTipText("Nuevo ítem");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setBounds(323, 20, 36, 23);
		buttonNuevo.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/nuevo.png")));
		panelBtn.add(buttonNuevo);
		
		buttonCancelar= new JButton("");
		buttonCancelar.setToolTipText("Deshacer ítem");
		buttonCancelar.addActionListener(this);
		buttonCancelar.setBounds(361, 20, 36, 23);
		buttonCancelar.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/undo.png")));
		panelBtn.add(buttonCancelar);
		
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(399, 20, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/save.png")));
		panelBtn.add(buttonGrabar);

		buttonEditar= new JButton("");
		buttonEditar.setToolTipText("Modificar ítem");
		buttonEditar.addActionListener(this);
		buttonEditar.setBounds(437, 20, 36, 23);
		buttonEditar.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/edit.png")));
		panelBtn.add(buttonEditar);
		
		buttonEliminar= new JButton("");
		buttonEliminar.setToolTipText("Eliminar ítem");
		buttonEliminar.addActionListener(this);
		buttonEliminar.setBounds(475, 20, 36, 23);
		buttonEliminar.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/delete.png")));
		panelBtn.add(buttonEliminar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(513, 20, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/Exit.png")));
		panelBtn.add(buttonSalir);
		
		buttonPrimero= new JButton("");
		buttonPrimero.setToolTipText("Primer ítem");
		buttonPrimero.addActionListener(this);
		buttonPrimero.setBounds(18, 20, 36, 23);
		buttonPrimero.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/hide-left.png")));
		panelBtn.add(buttonPrimero);

		buttonUltimo= new JButton("");
		buttonUltimo.setToolTipText("Anterior ítem");
		buttonUltimo.addActionListener(this);
		buttonUltimo.setBounds(56, 20, 36, 23);
		buttonUltimo.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/navigate-left.png")));
		panelBtn.add(buttonUltimo);

		buttonSiguiente= new JButton("");
		buttonSiguiente.setToolTipText("Siguiente ítem");
		buttonSiguiente.addActionListener(this);
		buttonSiguiente.setBounds(94, 20, 36, 23);
		buttonSiguiente.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/navigate-right.png")));
		panelBtn.add(buttonSiguiente);

		buttonAnterior= new JButton("");
		buttonAnterior.setToolTipText("Ultimo ítem");
		buttonAnterior.addActionListener(this);
		buttonAnterior.setBounds(132, 20, 36, 23);
		buttonAnterior.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/hide-right.png")));
		panelBtn.add(buttonAnterior);
		
		buttonBus= new JButton("");
		buttonBus.setBounds(206, 32, 36, 22);
		buttonBus.setToolTipText("Buscar");
		buttonBus.addActionListener(this);
		buttonBus.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/search.png")));
		panelDto.add(buttonBus);
		
		// BUTTON DE PANEL LST
		buttonOki= new JButton("");
		buttonOki.setToolTipText("Filtrar");
		buttonOki.addActionListener(this);
		buttonOki.setBounds(428, 244, 36, 21);
		buttonOki.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/ok.png")));
		panelLst.add(buttonOki);
		
		buttonImp= new JButton("");
		buttonImp.setToolTipText("Ver Lista");
		buttonImp.addActionListener(this);
		buttonImp.setBounds(474, 244, 36, 21);
		buttonImp.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/old-versions.png")));
		panelLst.add(buttonImp);
		
		buttonReg= new JButton("");
		buttonReg.setToolTipText("Regresar al registro");
		buttonReg.addActionListener(this);
		buttonReg.setBounds(511, 244, 36, 21);
		buttonReg.setIcon(new ImageIcon(VentanaProveedor.class.getResource("/modelo/Images/mant-cancelar.png")));
		panelLst.add(buttonReg);

		
	}
	public void crearTable(){
		tableList = new JTable(); 
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tableList.setBounds(10, 303, 665, 229);
		tableList.addMouseListener(this);
		tableList.addKeyListener(this);
		
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 17, 537, 201);
        panelLst.add(scrollTable);
        
    	tableList.setFillsViewportHeight(true);
    	tableList.setGridColor(new Color(153,204,255));
	}
	
	public void limpiarTexts() {

		textCod.setText(null);
		textCod.setBackground(Menu.textColorBackgroundInactivo);	
		textCod.setForeground(Menu.textColorForegroundInactivo);
		textCod.requestFocus(true);
		
		textNom.setText(null);
		textNom.setBackground(Menu.textColorBackgroundInactivo);	
		textNom.setForeground(Menu.textColorForegroundInactivo);

		textContacto.setText(null);
		textContacto.setBackground(Menu.textColorBackgroundInactivo);	
		textContacto.setForeground(Menu.textColorForegroundInactivo);
		
		textRuc.setText(null);
		textRuc.setBackground(Menu.textColorBackgroundInactivo);	
		textRuc.setForeground(Menu.textColorForegroundInactivo);
		
		textDir.setText(null);
		textDir.setBackground(Menu.textColorBackgroundInactivo);	
		textDir.setForeground(Menu.textColorForegroundInactivo);
		
		textTel.setText(null);
		textTel.setBackground(Menu.textColorBackgroundInactivo);	
		textTel.setForeground(Menu.textColorForegroundInactivo);
		
		textArea.setText(null);
		textArea.setBackground(Menu.textColorBackgroundInactivo);	
		textArea.setForeground(Menu.textColorForegroundInactivo);
		
		textBus.setText(null);
		textBus.setBackground(Menu.textColorBackgroundInactivo);	
		textBus.setForeground(Menu.textColorForegroundInactivo);
		
		cbBus.removeAllItems();
		
        
        textRuc.setText(null);
        textRuc.setBackground(Menu.textColorBackgroundInactivo);	
		textRuc.setForeground(Menu.textColorForegroundInactivo);
		
		textHotmail.setText(null);
		textHotmail.setBackground(Menu.textColorBackgroundInactivo);	
		textHotmail.setForeground(Menu.textColorForegroundInactivo);
		
		textWeb.setText(null);
		textWeb.setBackground(Menu.textColorBackgroundInactivo);	
		textWeb.setForeground(Menu.textColorForegroundInactivo);
		
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	}
	
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textNom.setEnabled(b);
		textContacto.setEnabled(b);
		textRuc.setEnabled(b);
		textDir.setEnabled(b);
		textTel.setEnabled(b);
		textArea.setEnabled(b);
		textBus.setEnabled(b);
		cbBus.setEnabled(b);
		
		textHotmail.setEnabled(b);
		textWeb.setEnabled(b);
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

			buttonBus.setEnabled(true);// BUSCAR
			
			panelDto.setVisible(true);// PANEL DATOS
			panelLst.setVisible(false);// PANEL LISTA
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

			buttonBus.setEnabled(false);// BUSCAR
			
			panelDto.setVisible(true);// PANEL DATOS
			panelLst.setVisible(false);// PANEL LISTA
		 }
	}

	
	protected void llenarTable(String Consultar) {
		conexion = new ConexionDB();
        try {
     	   totalitems=0;
 			
		   model= new DefaultTableModel();
		   model.addColumn("Código");
		   model.addColumn("Razón Social");
		   model.addColumn("Ruc");
		   model.addColumn("Contacto");
           
 		   String []datos= new String[4];
 		   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(Consultar);
 		   tableList.setModel(model);
 		   
    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(30);
    	   tableList.getColumnModel().getColumn(1).setPreferredWidth(280);
    	   tableList.getColumnModel().getColumn(2).setPreferredWidth(60);
    	   tableList.getColumnModel().getColumn(3).setPreferredWidth(50);

           while(rs.next()) {
            datos[0]=" "+Menu.formatid_7.format(rs.getInt("Id_Prv"));
            datos[1]=" "+rs.getString("RazonSocialPrv");
            datos[2]=" "+rs.getString("RucPrv");
            datos[3]=" "+rs.getString("ContactoPrv");
            totalitems=totalitems+1;
            model.addRow(datos);
            tableList.setModel(model);
	        }
           	st.close();
           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
        textBus.requestFocus();
	}

	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}

	//METODO BUSCAR =================
	public void buscar() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(consultar);
			if (resultSet.next()== true) {
				MOD=1;
				
				int id=(Integer.parseInt(resultSet.getString("Id_Prv")));
				textCod.setText(Menu.formatid_7.format(id));
				textNom.setText(resultSet.getString("RazonSocialPrv"));
				textDir.setText(resultSet.getString("DireccionPrv"));
				textRuc.setText(resultSet.getString("RucPrv"));
				textTel.setText(resultSet.getString("TelefonoPrv"));
				textContacto.setText(resultSet.getString("ContactoPrv"));
				textHotmail.setText(resultSet.getString("HotmailPrv"));
				textWeb.setText(resultSet.getString("WebPrv"));
				textArea.setText(resultSet.getString("ObservacionPrv"));
				
				panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos: Fecha de Alta " + resultSet.getString("FechaAltaCli") + "  :::::::  Fecha q actualizo " + resultSet.getString("FechaActualizaCli"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
				
				}else{
					limpiarTexts();
					llenarcbBuscar();
					textBus.requestFocus();
					textBus.selectAll();
				}
			statement.close();
			} catch (Exception e) {}
		}
	
		//METODO NUEVO =================
		public void nuevo() {
			MOD=0;
			conexion = new ConexionDB();
			try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_Prv from Proveedor order by Id_Prv desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_Prv"))+1);
					textCod.setText(Menu.formatid_7.format(id));
				}else{
					textCod.setText("0000001");
				}	
				textBus.requestFocus();
				textBus.selectAll();
			statement.close();
			} catch (Exception e) {}
		}
	
		//METODO GRABAR =================
		public void insertar() {
			if (textCod.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCod.requestFocus();
				return;
			}
			if (textNom.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textNom.requestFocus();
				return;
			}
			if (textContacto.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textContacto.requestFocus();
				return;
			}
			if (textDir.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDir.requestFocus();
				return;
			}
			conexion = new ConexionDB();
			try {
				String buscoNom= textNom.getText();
				Statement statement = conexion.gConnection().createStatement();
				ResultSet resultSet = statement.executeQuery("Select * from Proveedor where RazonSocialPrv ='"+buscoNom+"'");
				if (resultSet.next()== true) {
					JOptionPane.showMessageDialog(null, "Proveedor ya fue registrado",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
					textNom.requestFocus(true);
					textNom.selectAll();
					statement.close();
				}else{
					try {
	                nuevo();
					String sql ="INSERT INTO  Proveedor (Id_Prv,RazonSocialPrv,DireccionPrv,RucPrv,TelefonoPrv,ContactoPrv,HotmailPrv,WebPrv,ObservacionPrv) VALUES (?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText()));
					ps.setNString(2,textNom.getText().trim());
					ps.setNString(3,textDir.getText());
					ps.setString(4, textRuc.getText().trim());
					ps.setString(5, textTel.getText().trim());;
					ps.setString(6, textContacto.getText().trim());
					ps.setString(7, textHotmail.getText().trim());
					ps.setString(8, textWeb.getText().trim());
					ps.setString(9, textArea.getText().trim());
					ps.execute();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			  		filtrar();
					activarButton(true);
			  		buttonNuevo.doClick();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
			}
		}
		//METODO ACTUALIZA =================
		public void actualizar() {
			if (textCod.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCod.requestFocus();
				return;
			}
			if (textNom.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textNom.requestFocus();
				return;
			}
			if (textContacto.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textContacto.requestFocus();
				return;
			}
			if (textDir.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDir.requestFocus();
				return;
			}
			conexion = new ConexionDB();
			try {
	         String sql="UPDATE PROVEEDOR SET Id_Prv = ?,"
	                 + "RazonSocialPrv = ?,"
	                 + "DireccionPrv = ?,"
	                 + "RucPrv = ?,"
	                 + "TelefonoPrv =?,"
	                 + "ContactoPrv = ?,"
	                 + "HotmailPrv=?,"
	                 + "WebPrv =?,"
	                 + "ObservacionPrv=?"
	                 + "WHERE Id_Prv = '"+textCod.getText()+"'"; 
	         
			PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(textCod.getText()));
			ps.setNString(2,textNom.getText().trim());
			ps.setNString(3,textDir.getText());
			ps.setString(4, textRuc.getText().trim());
			ps.setString(5, textTel.getText().trim());;
			ps.setString(6, textContacto.getText().trim());
			ps.setString(7, textHotmail.getText().trim());
			ps.setString(8, textWeb.getText().trim());
			ps.setString(9, textArea.getText().trim());
			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
	  		filtrar();
			activarButton(true);
			limpiarTexts();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		//METODO ELIMINAR =================
		public void delete() {
			if (id==0){
				JOptionPane.showMessageDialog(null, "Primero debe filtrar el ítem...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				tableList.requestFocus();
				return;
			}
			if (id==1){
				JOptionPane.showMessageDialog(null, "No esta permitido eliminar el ítem...!"+ Menu.separador + Menu.formatid_7.format(id) +" "+ descripcion,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				tableList.requestFocus();
				return;
			}
			int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem?"+ Menu.separador + Menu.formatid_7.format(id) +" "+ descripcion, Menu.SOFTLE_HOTEL,		
			JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (respuesta == JOptionPane.YES_OPTION) {
				conexion = new ConexionDB();
				try {
					Statement statement = conexion.gConnection().createStatement();
					String query="Delete from PROVEEDOR where Id_Prv ="+ id;
					statement.execute(query);
					JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					statement.close();

					buttonEliminar.setSelected(true);
					llenarTable("Select * from PROVEEDOR order by RazonSocialPrv asc");
				} catch (Exception e) { e.printStackTrace(); }
			}else if (respuesta == JOptionPane.NO_OPTION) {
			}
		}
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textContacto){textContacto.setBackground(Menu.textColorBackgroundActivo);} 
			if (ev.getSource() == textRuc){textRuc.setBackground(Menu.textColorBackgroundActivo);} 
			if (ev.getSource() == textDir){textDir.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textTel){textTel.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundActivo);}
			
			if (ev.getSource() == textHotmail){textHotmail.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textWeb){textWeb.setBackground(Menu.textColorBackgroundActivo);}
			//FORE
			if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textContacto){textContacto.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textRuc){textRuc.setForeground(Menu.textColorForegroundActivo);} 
			if (ev.getSource() == textDir){textDir.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textTel){textTel.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundActivo);}
			
			if (ev.getSource() == textHotmail){textHotmail.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textWeb){textWeb.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textContacto){textContacto.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textRuc){textRuc.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textDir){textDir.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textTel){textTel.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textArea){textArea.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundInactivo);}
			
			if (ev.getSource() == textHotmail){textHotmail.setBackground(Menu.textColorBackgroundInactivo);} 
			if (ev.getSource() == textWeb){textWeb.setBackground(Menu.textColorBackgroundInactivo);}
			//FORE
			if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textContacto){textContacto.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textRuc){textRuc.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textDir){textDir.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textTel){textTel.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textArea){textArea.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundInactivo);}
			
			if (ev.getSource() == textHotmail){textHotmail.setForeground(Menu.textColorForegroundInactivo);} 
			if (ev.getSource() == textWeb){textWeb.setForeground(Menu.textColorForegroundInactivo);} 
		}
		
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonNuevo){// NUEVO
				  limpiarTexts();
				  activarButton(false);
				  activarTexts(true);
				  
				  nuevo();
				  textCod.setEditable(false);
				  textNom.requestFocus(true);
				  }
			  if (evento.getSource() == buttonCancelar){// CANCELAR
				  limpiarTexts();
				  activarButton(true);
				  buttonNuevo.requestFocus(true);
				  }
			  if (evento.getSource() == buttonGrabar){// GRABAR
				  if (MOD==0){
					  insertar();
				  }else{
					  actualizar();
				  	}
			  		/*filtrar();
					limpiarTexts();
					activarButton(true);
					buttonNuevo.requestFocus(true);
					limpiarTexts();
					activarButton(true);buttonNuevo.requestFocus(true);*/
				  }
			  if (evento.getSource() == buttonEditar){// EDITAR
				  activarButton(false);
				  //if (id>0) {
					  //consultar="Select* from PROVEEDOR where Id_Prv='" + id + "'";
				  //}
				  //buscar();
				  }	
			  if (evento.getSource() == buttonEliminar){// ELIMINAR
				  delete();
				  }	
			  if (evento.getSource() == buttonSalir){// SALIR
				  frame.dispose();
				  }
			  if (evento.getSource() == buttonBus){	// BUSCAR
				  panelLst.setVisible(true);
				  llenarcbBuscar();
				  llenarTable("Select* from PROVEEDOR order by RazonSocialPrv asc");
				  panelDto.setVisible(false);// PANEL DATOS
				  textBus.requestFocus();textBus.selectAll();
				  }
			  if (evento.getSource() == buttonOki){	// OK
					if (id>0) {
						consultar="Select* from PROVEEDOR where Id_Prv='" + id + "'";
					}
				  	buscar();
				  	filtrar();
					panelLst.setVisible(false);
					panelDto.setVisible(true);
				  }
			  if (evento.getSource() == buttonReg){	// REGRESAR
					panelLst.setVisible(false);
					panelDto.setVisible(true);
				  }
			  if (evento.getSource() == buttonImp){	// IMPRIMIR
				  filtrar();
				  
				  /*lAlquilarReservar Al = new lAlquilarReservar();
				  Al.setCodCliente(this.textCod.getText());
				  Al.setApeCliente(this.textNom.getText());
				  Al.setDirCliente(this.textDir.getText());*/
				  
					
					
				  if(this.tableList.getSelectedRow() != -1)
					  
				  {


				  }
			        //this("Seleccione una fila de la tabla");
			       
			   
				  }
			}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
				if (evet.getSource() == textNom){
					int pos = textNom.getCaretPosition();textNom.setText(textNom.getText().toUpperCase());textNom.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textNom.getText().toLowerCase().isEmpty()|| textNom.getText().toLowerCase().length()>95){
						textNom.requestFocus();
						textNom.selectAll();
						textNom.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textNom.getText().toLowerCase().length()==95){
							textDir.requestFocus();
							textDir.selectAll();	
						}
				} 
				if (evet.getSource() == textDir){
					int pos = textDir.getCaretPosition();textDir.setText(textDir.getText().toUpperCase());textDir.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textDir.getText().toLowerCase().isEmpty()|| textDir.getText().toLowerCase().length()>195){
						textDir.requestFocus();
						textDir.selectAll();
						textDir.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textDir.getText().toLowerCase().length()==195){
							textRuc.requestFocus();
							textRuc.selectAll();	
						}
				}
				if (evet.getSource() == textRuc){
					if (textRuc.getText().toLowerCase().isEmpty()|| textRuc.getText().toLowerCase().length()>11){
						textRuc.requestFocus();
						textRuc.selectAll();
						textRuc.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textRuc.getText().toLowerCase().length()==11){
							textTel.requestFocus();
							textTel.selectAll();	
						}
				}
				if (evet.getSource() == textTel){
					int pos = textTel.getCaretPosition();textTel.setText(textTel.getText().toUpperCase());textTel.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textTel.getText().length()>75){
						textTel.requestFocus();
						textTel.selectAll();
						textTel.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textTel.getText().toLowerCase().length()==75){
							textContacto.requestFocus();
							textContacto.selectAll();	
						}
				}
				if (evet.getSource() == textContacto){
					int pos = textContacto.getCaretPosition();textContacto.setText(textContacto.getText().toUpperCase());textContacto.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textContacto.getText().toLowerCase().isEmpty()|| textContacto.getText().toLowerCase().length()>95){
						textContacto.requestFocus();
						textContacto.selectAll();
						textContacto.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textContacto.getText().toLowerCase().length()==95){
							textHotmail.requestFocus();
							textHotmail.selectAll();	
						}
				}
				if (evet.getSource() == textHotmail){
					int pos = textHotmail.getCaretPosition();textHotmail.setText(textHotmail.getText().toUpperCase());textHotmail.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textHotmail.getText().length()>85){
						textHotmail.requestFocus();
						textHotmail.selectAll();
						textHotmail.setText(null);
						} 
						 if (e==KeyEvent.VK_ENTER || textHotmail.getText().toLowerCase().length()==85){
							textWeb.requestFocus();
							textWeb.selectAll();
						}
				}
				if (evet.getSource() == textWeb){
					int pos = textWeb.getCaretPosition();textWeb.setText(textWeb.getText().toUpperCase());textWeb.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textWeb.getText().length()>85){
						textWeb.requestFocus();
						textWeb.selectAll();
						textWeb.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textWeb.getText().toLowerCase().length()==85){
							buttonGrabar.doClick();
						}
				}
				if (evet.getSource() == textArea){
					int pos = textArea.getCaretPosition();textArea.setText(textArea.getText().toUpperCase());textArea.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					if (textArea.getText().length()>495){
						textArea.requestFocus();
						textArea.selectAll();
						textArea.setText(null);
						} 
						else if (e==KeyEvent.VK_ENTER || textArea.getText().toLowerCase().length()==495){
							buttonGrabar.doClick();
						}
				}
				if (evet.getSource() == textBus){
					int pos = textBus.getCaretPosition();textBus.setText(textBus.getText().toUpperCase());textBus.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
					consultar="";
					if(cbBus.getSelectedItem()=="CODIGO") {
						consultar="SELECT * FROM PROVEEDOR where Id_Prv like'"+textBus.getText()+"%' order by Id_Prv asc";
					}
					if(cbBus.getSelectedItem()=="NOMBRE / RAZON SOCIAL") {
						consultar="SELECT * FROM PROVEEDOR where RazonSocialPrv like'"+textBus.getText()+"%' order by RazonSocialPrv asc";
					}
					if(cbBus.getSelectedItem()=="RUC") {
						consultar="SELECT * FROM PROVEEDOR where RucPrv  like'"+textBus.getText()+"%' order by RucPrv  asc";
					}
					if(cbBus.getSelectedItem()=="CONTACTO") {
						consultar="SELECT * FROM PROVEEDOR where ContactoPrv like'"+textBus.getText()+"%' order by ContactoPrv asc";
					}
					
					llenarTable(consultar);
					if (e==KeyEvent.VK_ENTER || textBus.getText().toLowerCase().length()==30){
						buscar();
						filtrar();
						panelLst.setVisible(false);
						panelDto.setVisible(true);
					}
					if (e==KeyEvent.VK_DOWN){
						System.out.println("HOLA ESTE ES LA TECLAD DDJDJJDJDJJ");
						tableList.selectAll();
						tableList.requestFocus();
					}
				}
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			char e=evet.getKeyChar();
			if (evet.getSource() == textNom){ 
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textDir){ 
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textRuc){
				if ((e<'0'||e>'9'))evet.consume();
			}
			if (evet.getSource() == textTel){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textContacto){
				if(!Character.isLetter(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textHotmail){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textWeb){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
			if (evet.getSource() == textArea){
				if(!Character.isDefined(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
			}
		}
	
	public void filtrar(){
		if (FILTRO_PRV == "VA"){
				VentanaAlquiler.textCId.setText(this.textCod.getText());
				VentanaAlquiler.textCNom.setText(this.textNom.getText());
				VentanaAlquiler.Direccion= this.textDir.getText();
			  
			  //String [] lista = {textFormatDsctSol.getText(),textFormatDsctPor.getText() };
			  //for (String string : lista) {
				  //VentanaAlquilarReservar.cbDsct.addItem(string);
			  //}
			  
			  this.frame.dispose();
			}
		if (FILTRO_PRV == "VGD"){
			  VentanaGenerarDocumento.textCId.setText(this.textCod.getText());
			  VentanaGenerarDocumento.textCNom.setText(this.textNom.getText());
			  VentanaGenerarDocumento.textCRuc.setText(this.textRuc.getText());
			  VentanaGenerarDocumento.textCDir.setText(this.textDir.getText());
			  this.frame.dispose();
			}
		if (FILTRO_PRV == "VC"){
			  VentanaVitrinaCompra.textCId.setText(this.textCod.getText());
			  VentanaVitrinaCompra.textCNom.setText(this.textNom.getText());
			  VentanaVitrinaCompra.textCDni.setText(this.textRuc.getText());
			  this.frame.dispose();
			}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {

	}
	
	@Override
	public void keyPressed(KeyEvent evet) {
		// TODO Auto-generated method stub
		char e=evet.getKeyChar();
		if (evet.getSource().equals(tableList)) {
			id =Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim());
			if (id==0){
				tableList.requestFocus();
				return;
			}
			if (e==KeyEvent.VK_ENTER ){
				consultar="";
				activarButton(false);
				consultar="SELECT * FROM PROVEEDOR where Id_Prv ='"+ id +"' order by Id_Prv asc";
				buscar();
				filtrar();
			}

		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent press) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseReleased(MouseEvent Mouse_evt) {
		// TODO Auto-generated method stub
		if (Mouse_evt.getSource().equals(tableList)) {
			try {
				id=0;descripcion="";
				if (Mouse_evt.getClickCount() == 1 || Mouse_evt.getClickCount() == 2) {
					id = Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim());
					descripcion = (String)(tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim());
					if (id==0){
						tableList.requestFocus();
						return;
					}
					if (Mouse_evt.getClickCount() == 2) {
						buttonOki.doClick();
					}
				}

			} catch (Exception e) {}
		}
	}
}
