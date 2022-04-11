package modelo.Presentacion;

import java.awt.Color;
import java.awt.Image;
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
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import modelo.Datos.ConexionDB;


public class VentanaInventarioHabitacion implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public JInternalFrame 	frame;
	private JPanel  panelDto = new JPanel();JPanel  panelBtn = new JPanel();JPanel  panelLst = new JPanel();
	private JFormattedTextField textCantidad;


	private JLabel				lbl1,lbl2,lbl3,lbl4,lblFto;
	private JButton  			buttonNuevo,buttonCancelar,buttonGrabar,buttonEditar,buttonEliminar,buttonSalir,
								buttonPrimero,buttonUltimo,buttonSiguiente,buttonAnterior;
	
	private JComboBox<String> cbTipo = new JComboBox<String>();private JComboBox<String> cbNumero = new JComboBox<String>();private JComboBox<String> cbArticulo = new JComboBox<String>();
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	
	private String consultar="";
	private String ID_HAB="",ID_ART="", tipo="",articulo="";
	private int MOD=0;
	private int cantidad=0; 
	public VentanaInventarioHabitacion() {
		frameInventarioHabitacion();
		crearPanel();
		
		crearButtons();
		crearTable();
		crearComboBox();
		crearTextFields();
		crearLabels();
		llenarcbTipo();
		llenarcbNumero();
		llenarcbArticulo();
		activarButton(true);
		Menu.Desktop.add(frame);
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
    	frame.setLocation(x, y);
	}
	public void frameInventarioHabitacion() {
		frame = new JInternalFrame();
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
			}
			@Override
			public void internalFrameClosing(InternalFrameEvent arg0) {
			}
		});
		frame.setFrameIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/book_picture.png")));
		frame.setTitle("Implementar habitación");
		frame.setClosable(true);
		frame.setBounds(100, 100, 591, 491);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDto.setBounds(10, 65, 555, 107);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
	
		panelBtn.setBorder(new TitledBorder(null, "Controles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBtn.setBounds(10, 11, 555, 49);
		frame.getContentPane().add(panelBtn);
		panelBtn.setLayout(null);
		
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 172, 555, 278);
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
	}
	public void crearLabels(){
		lbl1= new JLabel("Tipo Habitación:");
		lbl1.setBounds(10, 28, 86, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Nro:");
		lbl2.setBounds(317, 11, 60, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);

		lbl3= new JLabel("Artículo:");
		lbl3.setBounds(31, 52, 67, 14);
		panelDto.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
		
		lbl4= new JLabel("Cantidad:");
		lbl4.setBounds(34, 77, 59, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl4.setFont(Menu.fontLabel);
		
		lblFto= new JLabel("Sin image");
		lblFto.setBounds(405, 16, 140, 79);
		panelDto.add(lblFto);
		lblFto.setHorizontalAlignment(SwingConstants.CENTER);
		lblFto.setBorder(BorderFactory.createEtchedBorder());
	}
	public void crearTextFields() {
		textCantidad = new JFormattedTextField();
		textCantidad.setColumns(10);
		textCantidad.setFont(Menu.fontText);
		textCantidad.setForeground(Menu.textColorForegroundInactivo);
		textCantidad.setHorizontalAlignment(SwingConstants.CENTER);
		textCantidad.addActionListener(this);
		textCantidad.addFocusListener(this);
		textCantidad.addKeyListener(this);
		textCantidad.setBounds(103, 73, 67, 22);
		textCantidad.setForeground(new Color(30, 144, 255));
		panelDto.add(textCantidad);

	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setToolTipText("Nuevo ítem");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setBounds(318, 20, 36, 23);
		buttonNuevo.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/nuevo.png")));
		panelBtn.add(buttonNuevo);
		
		buttonCancelar= new JButton("");
		buttonCancelar.setToolTipText("Deshacer ítem");
		buttonCancelar.addActionListener(this);
		buttonCancelar.setBounds(356, 20, 36, 23);
		buttonCancelar.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/undo.png")));
		panelBtn.add(buttonCancelar);
		
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(394, 20, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/save.png")));
		panelBtn.add(buttonGrabar);

		buttonEditar= new JButton("");
		buttonEditar.setToolTipText("Modificar ítem");
		buttonEditar.addActionListener(this);
		buttonEditar.setBounds(432, 20, 36, 23);
		buttonEditar.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/edit.png")));
		panelBtn.add(buttonEditar);
		
		buttonEliminar= new JButton("");
		buttonEliminar.setToolTipText("Eliminar ítem");
		buttonEliminar.addActionListener(this);
		buttonEliminar.setBounds(470, 20, 36, 23);
		buttonEliminar.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/delete.png")));
		panelBtn.add(buttonEliminar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(508, 20, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/Exit.png")));
		panelBtn.add(buttonSalir);
		
		buttonPrimero= new JButton("");
		buttonPrimero.setToolTipText("Primer ítem");
		buttonPrimero.addActionListener(this);
		buttonPrimero.setBounds(18, 20, 36, 23);
		buttonPrimero.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/hide-left.png")));
		panelBtn.add(buttonPrimero);

		buttonUltimo= new JButton("");
		buttonUltimo.setToolTipText("Anterior ítem");
		buttonUltimo.addActionListener(this);
		buttonUltimo.setBounds(56, 20, 36, 23);
		buttonUltimo.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/navigate-left.png")));
		panelBtn.add(buttonUltimo);

		buttonSiguiente= new JButton("");
		buttonSiguiente.setToolTipText("Siguiente ítem");
		buttonSiguiente.addActionListener(this);
		buttonSiguiente.setBounds(94, 20, 36, 23);
		buttonSiguiente.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/navigate-right.png")));
		panelBtn.add(buttonSiguiente);

		buttonAnterior= new JButton("");
		buttonAnterior.setToolTipText("Ultimo ítem");
		buttonAnterior.addActionListener(this);
		buttonAnterior.setBounds(132, 20, 36, 23);
		buttonAnterior.setIcon(new ImageIcon(VentanaInventarioHabitacion.class.getResource("/modelo/Images/hide-right.png")));
		panelBtn.add(buttonAnterior);
	
	}
	public void crearComboBox() {
		cbTipo = new JComboBox<>();
		cbTipo.setBounds(103, 25, 184, 21);
		cbTipo.setFont(Menu.fontText);
		cbTipo.addActionListener(this);
		cbTipo.addFocusListener(this);
		cbTipo.addKeyListener(this);
		cbTipo.addMouseListener(this);
		cbTipo.addPropertyChangeListener(this);
		panelDto.add(cbTipo);
		
		cbNumero = new JComboBox<>();
		cbNumero.setBounds(291, 25, 86, 21);
		cbNumero.setFont(Menu.fontText);
		cbNumero.addActionListener(this);
		cbNumero.addFocusListener(this);
		cbNumero.addKeyListener(this);
		cbNumero.addPropertyChangeListener(this);
		panelDto.add(cbNumero);
		
		cbArticulo = new JComboBox<>();
		cbArticulo.setBounds(103, 49, 274, 21);
		cbArticulo.setFont(Menu.fontText);
		cbArticulo.addActionListener(this);
		cbArticulo.addFocusListener(this);
		cbArticulo.addKeyListener(this);
		cbArticulo.addPropertyChangeListener(this);
		panelDto.add(cbArticulo);

	}
	public void limpiarTexts() {
		textCantidad.setText(null);
		textCantidad.setBackground(Menu.textColorBackgroundInactivo);	
		textCantidad.setForeground(Menu.textColorForegroundInactivo);
		
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
        llenarTable();
        ID_ART="";
	}
	public void activarTexts(boolean b) {
		textCantidad.setEnabled(b);
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
			
			//cbTipo.setEnabled(false);
			//cbNumero.setEnabled(false);
			//cbTarifa.setEnabled(false);
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
		 
			//cbTipo.setEnabled(true);
			//cbNumero.setEnabled(true);
			//cbTarifa.setEnabled(true);
		 }
	}
	public void focusGained(FocusEvent ev) {
		if (ev.getSource() == textCantidad){textCantidad.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == cbTipo){cbTipo.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == cbNumero){cbNumero.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == cbArticulo){cbArticulo.setBackground(Menu.textColorBackgroundActivo);}
		
		//FORE
		if (ev.getSource() == textCantidad){textCantidad.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == cbNumero){cbNumero.setForeground(Menu.textColorForegroundActivo);}
		if (ev.getSource() == cbArticulo){cbArticulo.setForeground(Menu.textColorForegroundActivo);}
	}
	public void focusLost(FocusEvent ev) {
		if (ev.getSource() == textCantidad){textCantidad.setBackground(Menu.textColorBackgroundInactivo);}
		if (ev.getSource() == cbTipo){cbTipo.setBackground(new Color(240,240,240));}
		if (ev.getSource() == cbNumero){cbNumero.setBackground(new Color(240,240,240));}
		if (ev.getSource() == cbArticulo){cbArticulo.setBackground(new Color(240,240,240));}
		
		//FORE
		if (ev.getSource() == textCantidad){textCantidad.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == cbTipo){cbTipo.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == cbNumero){cbNumero.setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == cbArticulo){cbArticulo.setForeground(Menu.textColorForegroundInactivo);}
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
			cbTipo.setSelectedIndex(-1);
			resultSet.close();
			statement.close();
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
	public void llenarcbArticulo(){
		cbArticulo.removeAllItems();
		cbArticulo.addItem("%TODOS");
		String consul="";
		conexion = new ConexionDB();
		//if (cbArticulo.getSelectedItem()=="%TODOS") {
			consul ="Select* from INVENTARIOS,CATEGORIA where INVENTARIOS.Id_Cat=CATEGORIA.Id_Cat and FamiliaCat ='" + "INV_HOTEL" + "'order by FamiliaCat asc";//}
		//else {
			//consul ="Select* from INVENTARIOS as I,CATEGORIA as C  where I.Id_Cat=C.Id_Cat and DescripcionCat ='" + "INV.HOTEL" + "'and DescripcionInv='" + cbArticulo.getSelectedItem()  + "'order by C.DescripcionInv asc";}
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet rs = statement.executeQuery(consul);
			while (rs.next()==true) {
				cbArticulo.addItem(rs.getString("DescripcionInv"));
			}
			cbArticulo.setSelectedIndex(-1);
			rs.close();
			statement.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		conexion.DesconectarDB();
	}
	public void mostrarcbArticulo(){
		conexion = new ConexionDB();
		ID_ART="";
		String consul ="Select* from INVENTARIOS as I where I.DescripcionInv ='" + cbArticulo.getSelectedItem() + "'order by I.DescripcionInv asc";//}
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet rs = statement.executeQuery(consul);
	        lblFto.setText("Sin image");
	        lblFto.setIcon(null);
			while (rs.next()==true) {
				ID_ART=(rs.getString("Id_Inv"));
				
				// CARGO LA IMAGEN
			    Image i=null;
		        Blob blob = rs.getBlob("ImageInv");
		        i= javax.imageio.ImageIO.read(blob.getBinaryStream());
		        ImageIcon image = new ImageIcon(i);
				Icon icono = new ImageIcon(image.getImage().getScaledInstance(lblFto.getWidth(), lblFto.getHeight(), Image.SCALE_DEFAULT)); 
				lblFto.setText(null);
				lblFto.setIcon(icono);
			}
			rs.close();
			statement.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	public void crearTable() {
		tableList = new JTable();
		tableList.addMouseListener(this);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 20, 535, 247);
        panelLst.add(scrollTable);
        
    	tableList.setGridColor(new Color(238, 232, 170));
	}
	protected void llenarTable() {
		conexion = new ConexionDB();
		LimpiarTable();
        try {

     	   int totalitems=0;
     	   model= new DefaultTableModel();
		   model.addColumn("Nro");
		   model.addColumn("Tipo habitación");
		   model.addColumn("ID");
		   model.addColumn("Artículo");
		   model.addColumn("Valor S/.");
	       model.addColumn("Cant.");
	       
 		   String []datos= new String[6];
 		   tableList.setModel(model);
 		   
 		   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(consultar);
 		   
    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(20);
    	   tableList.getColumnModel().getColumn(1).setPreferredWidth(130);
    	   tableList.getColumnModel().getColumn(2).setPreferredWidth(18);
    	   tableList.getColumnModel().getColumn(3).setPreferredWidth(220);
    	   tableList.getColumnModel().getColumn(4).setPreferredWidth(45);
    	   tableList.getColumnModel().getColumn(5).setPreferredWidth(20);

    	   while(rs.next()) {
		    	datos[0]=" "+rs.getString("NumeroHab");
		    	datos[1]=" "+rs.getString("Tipo_Hab");
		    	datos[2]=Menu.formatid_4.format(rs.getInt("Id_Inv"));
	            datos[3]=" "+rs.getString("DescripcionInv");
	            datos[4]=Menu.formateadorCurrency.format(rs.getFloat("CostoInv"));
	            datos[5]=rs.getString("CantidadIH");
	            totalitems=totalitems+1;
	            model.addRow(datos);
	            tableList.setModel(model);
	            
	        }
    	   // MODELO TABLE
    	   int CONT=14;
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
    	   
           DefaultTableCellRenderer modeloRight = new DefaultTableCellRenderer();
           modeloRight.setHorizontalAlignment(SwingConstants.RIGHT);
           tableList.getColumnModel().getColumn(2).setCellRenderer(modeloRight);
           tableList.getColumnModel().getColumn(4).setCellRenderer(modeloRight);
           tableList.getColumnModel().getColumn(5).setCellRenderer(modeloRight);
           
           rs.close();
           
           //ColorearFilas colorear = new ColorearFilas();
           //tableList.setDefaultRenderer (Object.class, colorear);
           
           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
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
		if (cbNumero.getSelectedItem()==null || cbNumero.getSelectedItem()==""){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el número",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbNumero.requestFocus();
			return;
		}
		if (cbArticulo.getSelectedItem()==null || cbArticulo.getSelectedItem()=="%TODOS"){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el artículo",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbArticulo.requestFocus();
			return;
		}
		if (textCantidad.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textCantidad.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from HABITACION as H,INVENTARIO_HABITACION as IH, INVENTARIOS as I where IH.NumeroHab=H.NumeroHab and IH.Id_Inv=I.Id_Inv and IH.NumeroHab='"+cbNumero.getSelectedItem()+"'and I.DescripcionInv='"+cbArticulo.getSelectedItem()+"'");
			if (resultSet.next()== true) {
				JOptionPane.showMessageDialog(null, "El ítem ya fue registrado",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
				cbNumero.requestFocus(true);
				resultSet.close();
				return;
			}else{
				try {
				
					// ********************************************************************************GRABA TODOS
					if (cbTipo.getSelectedItem()!="%TODOS" && cbNumero.getSelectedItem()=="%TODOS" && cbArticulo.getSelectedItem()!="%TODOS"){
						Statement st = conexion.gConnection().createStatement();
						ResultSet rs = st.executeQuery("Select* from HABITACION where Tipo_Hab='"+cbTipo.getSelectedItem()+"'"); 
	
						String sql ="INSERT INTO INVENTARIO_HABITACION (NumeroHab,Id_Inv,CantidadIH) VALUES (?,?,?)";
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						String CONT="",C="";//int A=0;
						while (rs.next()==true) {
							// CONSULTO SI ESTA REGITRADO
							Statement s = conexion.gConnection().createStatement();
							ResultSet r = s.executeQuery("Select * from HABITACION as H,INVENTARIO_HABITACION as IH, INVENTARIOS as I where IH.NumeroHab=H.NumeroHab and IH.Id_Inv=I.Id_Inv and IH.NumeroHab='"+rs.getString("NumeroHab")+"'and I.DescripcionInv='"+cbArticulo.getSelectedItem()+"'");
							
							if (r.next()==true) {// REGISTRADO
								C="1";
								cbNumero.requestFocus(true);
							}else{ // FALTA REGISTRAR
								CONT=CONT +"-"+ (rs.getString("NumeroHab"));
								C="0";
								// DESCUENTO EL STOCK DEL ARTICULO
								Statement stt = conexion.gConnection().createStatement();
								ResultSet rss = stt.executeQuery("Select * from INVENTARIOS where Id_Inv = '"+ ID_ART +"'"); 
								int CAT=0;
								if (rss.next()==true) {
									CAT=(rss.getInt("StockInv"));
									
									//A=A + Integer.parseInt(textCantidad.getText());
									if (Integer.parseInt(textCantidad.getText()) > CAT){
										JOptionPane.showMessageDialog(null, "La cantidad ingresada supera "+ Menu.separador +"a las " +  CAT  + " unidades existentes en stock " ,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
										textCantidad.requestFocus();
										return;
									}
							         String sq="UPDATE INVENTARIOS SET Id_Inv = ?,"
							                 + "StockInv =?"
							                 + "WHERE Id_Inv = '"+ ID_ART +"'"; 
									PreparedStatement pst = conexion.gConnection().prepareStatement(sq);
									pst.setString(1, ID_ART);
									pst.setDouble(2, + CAT - Double.parseDouble(textCantidad.getText()));
									pst.executeUpdate();
									pst.close();
								}
								stt.close();
								// DESCUENTO EL STOCK DEL ARTICULO
								
								ps.setString(1, rs.getString("NumeroHab"));
								ps.setString(2, (ID_ART.toString()));
								ps.setDouble(3, Double.parseDouble(textCantidad.getText()));
								ps.execute();
							}
							s.close();
						}
						if (C=="1") {
							JOptionPane.showMessageDialog(null, "Las habitaciones "+ CONT + Menu.separador +"ya fue agregado el artículo "+ cbArticulo.getSelectedItem(),Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
							cbNumero.requestFocus(true);
							}
						if (C=="0") {
							JOptionPane.showMessageDialog(null, "Las habitaciones "+ CONT + Menu.separador +"Se les agrego el artículo "+ ID_ART +" "+cbArticulo.getSelectedItem()+ Menu.separador + "El stock del artículo " +  " fue actualizado",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
							limpiarTexts();
							activarButton(true);
							buttonNuevo.requestFocus(true);
							}
						ps.close();
						st.close();
					// ********************************************************************************* FIN GRABA TODOS
					 } else{
		                //nuevo();
						 
						// DESCUENTO EL STOCK DEL ARTICULO
						Statement s = conexion.gConnection().createStatement();
						ResultSet r = s.executeQuery("Select * from INVENTARIOS where Id_Inv = '"+ ID_ART +"'"); 
						int CAT=0;
						if (r.next()==true) {
							CAT=(r.getInt("StockInv"));
							if (Integer.parseInt(textCantidad.getText()) > CAT){
								JOptionPane.showMessageDialog(null, "La cantidad ingresada supera "+ Menu.separador +"a las " +  CAT  + " unidades existentes en stock " ,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
								textCantidad.requestFocus();
								return;
							}
					         String sq="UPDATE INVENTARIOS SET Id_Inv = ?,"
					                 + "StockInv =?"
					                 + "WHERE Id_Inv = '"+ ID_ART +"'"; 
							PreparedStatement pst = conexion.gConnection().prepareStatement(sq);
							pst.setString(1, ID_ART);
							pst.setDouble(2, + CAT - Double.parseDouble(textCantidad.getText()));
							pst.executeUpdate();
							pst.close();
							//JOptionPane.showMessageDialog(null, "El stock del artículo " + ID_ART +" "+cbArticulo.getSelectedItem() + " fue actualizado",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						}
						s.close();
						// DESCUENTO EL STOCK DEL ARTICULO
						String sql ="INSERT INTO INVENTARIO_HABITACION (NumeroHab,Id_Inv,CantidadIH) VALUES (?,?,?)";
						PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
						ps.setString(1, (String)cbNumero.getSelectedItem());
						ps.setString(2, ID_ART.toString());
						ps.setDouble(3, Double.parseDouble(textCantidad.getText()));
						ps.execute();
						ps.close();
						JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente" + Menu.separador + "el stock del artículo " + ID_ART +" "+cbArticulo.getSelectedItem() + " fue actualizado",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						limpiarTexts();
						activarButton(true);
						buttonNuevo.requestFocus(true);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				}
			}
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
			JOptionPane.showMessageDialog(null, "Falta seleccionar su número",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbNumero.requestFocus();
			return;
		}
		if (cbArticulo.getSelectedItem()==null || cbArticulo.getSelectedItem()=="%TODOS"){
			JOptionPane.showMessageDialog(null, "Falta seleccionar el artículo",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			cbArticulo.requestFocus();
			return;
		}
		if (textCantidad.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textCantidad.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
         String sql="UPDATE INVENTARIO_HABITACION SET NumeroHab = ?,"
                 + "Id_Inv = ?,"
                 + "CantidadIH =?"
                 + "WHERE NumeroHab = '"+ID_HAB +"'"; 
		PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
		ps.setString(1, (String)cbNumero.getSelectedItem());
		ps.setString(2, (ID_ART));
		ps.setDouble(3, Double.parseDouble(textCantidad.getText()));
		ps.executeUpdate();
		ps.close();
		
		// DESCUENTO EL STOCK DEL ARTICULO
		Statement s = conexion.gConnection().createStatement();
		ResultSet r = s.executeQuery("Select * from INVENTARIOS where Id_Inv = '"+ ID_ART +"'"); 
		int CAT=0;
		if (r.next()==true) {
			CAT=(r.getInt("StockInv"));
			if (MOD==1) {CAT= CAT + cantidad;}
			if (Integer.parseInt(textCantidad.getText()) > CAT){
				JOptionPane.showMessageDialog(null, "La cantidad ingresada supera "+ Menu.separador +"a las " +  CAT  + " unidades existentes en stock " ,Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCantidad.requestFocus();
				return;
			}
	         String sq="UPDATE INVENTARIOS SET StockInv = ?"
	                 + "WHERE Id_Inv = '"+ ID_ART +"'"; 
			PreparedStatement pst = conexion.gConnection().prepareStatement(sq);
			pst.setDouble(1, CAT - Double.parseDouble(textCantidad.getText()));
			pst.executeUpdate();
			pst.close();
			//JOptionPane.showMessageDialog(null, "El stock del artículo " + ID_ART +" "+cbArticulo.getSelectedItem() + " fue actualizado",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		r.close();
		s.close();
		// DESCUENTO EL STOCK DEL ARTICULO
		
		JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		limpiarTexts();
		activarButton(true);buttonNuevo.requestFocus(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		conexion.DesconectarDB();
	}
	
	//METODO ELIMINAR =================
	public void delete() {
		//if (ID_TAR==""){
		if (tableList.getSelectedRow() == -1 || ID_ART=="") {
			JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			tableList.requestFocus();
			return;
		}
		int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem " + Menu.separador + ID_HAB + " " + tipo +" :::: " + ID_ART + " "+  articulo  +" ?", Menu.SOFTLE_HOTEL,		
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (respuesta == JOptionPane.YES_OPTION) {
			conexion = new ConexionDB();
			try {
				// DESCUENTO EL STOCK DE ALMACEN
				Statement s = conexion.gConnection().createStatement();
				ResultSet r = s.executeQuery("Select * from INVENTARIOS where Id_Inv = '"+ ID_ART +"'"); 
				int CAT=0;
				if (r.next()==true) {
					CAT=(r.getInt("StockInv"));
			         String sq="UPDATE INVENTARIOS SET Id_Inv = ?,"
			                 + "StockInv =?"
			                 + "WHERE Id_Inv = '"+ ID_ART +"'"; 
					PreparedStatement pst = conexion.gConnection().prepareStatement(sq);
					pst.setString(1, ID_ART);
					pst.setDouble(2, + CAT + Double.parseDouble(textCantidad.getText()));
					pst.executeUpdate();
					pst.close();
				}
				r.close();
				s.close();
				// DESCUENTO EL STOCK DE ALMACEN
				Statement statement = conexion.gConnection().createStatement();
				String query="Delete from INVENTARIO_HABITACION where NumeroHab ='"+ID_HAB+"'and Id_Inv ='"+ID_ART+"'";
				statement.execute(query);
				JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!"+ Menu.separador + "El stock del artículo "  + ID_ART +" "+cbArticulo.getSelectedItem() + " fue actualizado" ,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				statement.close();
				limpiarTexts();

				buttonEliminar.setSelected(true);
				
			} catch (Exception e) { e.printStackTrace(); }
			conexion.DesconectarDB();
		}else if (respuesta == JOptionPane.NO_OPTION) {
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
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
					//llenarcbNumero();
					//llenarTable();
				}else{
					cbTipo.requestFocus();
				}
			}
		}
		if (evet.getSource() == cbNumero){
			if (e==KeyEvent.VK_ENTER){
				if (cbNumero.getSelectedIndex()!=-1){
					cbArticulo.requestFocus();
				}else{
					cbNumero.requestFocus();
				}
			}
		}
		if (evet.getSource() == cbArticulo){
			if (e==KeyEvent.VK_ENTER){
				if (cbArticulo.getSelectedIndex()!=-1){
					textCantidad.requestFocus();
					textCantidad.selectAll();
				}else{
					cbArticulo.requestFocus();
				}
			}
		}
		if (evet.getSource() ==textCantidad){
			if (textCantidad.getText().toLowerCase().isEmpty()|| textCantidad.getText().toLowerCase().length()>4){
				textCantidad.requestFocus();
				textCantidad.selectAll();
				textCantidad.setText(null);
			} else if (e==KeyEvent.VK_ENTER || textCantidad.getText().toLowerCase().length()==5){
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
		
		if (evet.getSource().equals(textCantidad)) {
			if ((car<'0'||car>'9')&&(car<'.'||car>'.'))evet.consume();
		}
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		// TODO Auto-generated method stub
		if (action.getSource().equals(buttonNuevo)) {
			  MOD=0;
			  limpiarTexts();
			  //llenarcbTipo();
			  //llenarcbNumero();
			  //llenarTarifa();
			  activarButton(false);
			  activarTexts(true);
			  
			  cbArticulo.setSelectedIndex(-1);
			  cbTipo.requestFocus();
		}
		if (action.getSource().equals(buttonCancelar)) {
			  limpiarTexts();
			  cbArticulo.setSelectedIndex(-1);
			  cbNumero.setSelectedIndex(-1);
			  cbTipo.setSelectedIndex(-1);
			  activarButton(true);
			  buttonNuevo.requestFocus(true);
			  //llenarcbTipo();
			  
		}
		if (action.getSource().equals(buttonGrabar)) {
			  if (MOD==0){
				  insertar();
			  }else{
				 actualizar();
			  	}
		}
		if (action.getSource().equals(buttonEditar)) {
			MOD=1;
			modificar();
		}
		if (action.getSource().equals(buttonEliminar)) {
			delete();
		}
		if (action.getSource().equals(buttonSalir)) {
			frame.dispose();
		}
		

		if (action.getSource().equals(cbTipo)) {
			llenarcbNumero();
			//llenarcbArticulo();
			if(cbTipo.getSelectedItem()=="%TODOS") {
				consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv order by I.DescripcionInv asc";
			}
			else if (cbTipo.getSelectedItem()!="%TODOS") {
				consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and H.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'order by I.DescripcionInv asc";
				}
			//System.out.println("111111111111111111111111111111111111111111111111111");
			llenarTable();
		}
		if (action.getSource().equals(cbNumero)) {
			if	(cbNumero.getSelectedItem()!=null){
				if	(cbTipo.getSelectedItem()=="%TODOS"){
					if	(cbNumero.getSelectedItem()=="%TODOS" || cbNumero.getSelectedItem()=="") {
						consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv order by I.DescripcionInv asc";
					}
					else if (cbNumero.getSelectedItem()!="%TODOS" || cbNumero.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and IH.NumeroHab ='"+cbNumero.getSelectedItem()+"'order by I.DescripcionInv asc";
						}
				}
				if	(cbTipo.getSelectedItem()!="%TODOS"){ 
					if (cbNumero.getSelectedItem()=="%TODOS" || cbNumero.getSelectedItem()=="") {
						consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and H.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'order by I.DescripcionInv asc";
					}
					else if (cbNumero.getSelectedItem()!="%TODOS" || cbNumero.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and H.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'and IH.NumeroHab ='"+cbNumero.getSelectedItem()+"'order by I.DescripcionInv asc";
						}
				}
				//System.out.println("2222222222222222222222222222222222222222222222222222222");
				llenarTable();
			}
		}
		if (action.getSource().equals(cbArticulo)) { 
			mostrarcbArticulo();
			 if (cbTipo.getSelectedItem()=="%TODOS" && cbNumero.getSelectedItem()=="%TODOS"){
				if	(cbArticulo.getSelectedItem()=="%TODOS" || cbArticulo.getSelectedItem()=="") {
					consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv order by I.DescripcionInv asc";
				}
				else if	(cbArticulo.getSelectedItem()!="%TODOS" || cbArticulo.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and I.DescripcionInv ='"+cbArticulo.getSelectedItem()+"'order by I.DescripcionInv asc";
					}
			 } 
			 if (cbTipo.getSelectedItem()!="%TODOS" && cbNumero.getSelectedItem()!="%TODOS"){ 
				if	(cbArticulo.getSelectedItem()=="%TODOS" || cbArticulo.getSelectedItem()=="") {
					consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and H.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'and IH.NumeroHab ='"+cbNumero.getSelectedItem()+"'order by I.DescripcionInv asc";
					}
					else if	(cbArticulo.getSelectedItem()!="%TODOS" || cbArticulo.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and H.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'and IH.NumeroHab ='"+cbNumero.getSelectedItem()+"'and I.DescripcionInv ='"+cbArticulo.getSelectedItem()+"'order by I.DescripcionInv asc";
					}
			 }
			 if (cbTipo.getSelectedItem()=="%TODOS" && cbNumero.getSelectedItem()!="%TODOS"){ 
				if	(cbArticulo.getSelectedItem()=="%TODOS" || cbArticulo.getSelectedItem()=="") {
					consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and IH.NumeroHab ='"+cbNumero.getSelectedItem()+"'order by I.DescripcionInv asc";
					}
					else if	(cbArticulo.getSelectedItem()!="%TODOS" || cbArticulo.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and IH.NumeroHab ='"+cbNumero.getSelectedItem()+"'and I.DescripcionInv ='"+cbArticulo.getSelectedItem()+"'order by I.DescripcionInv asc";
					}
			 }
			 if (cbTipo.getSelectedItem()!="%TODOS" && cbNumero.getSelectedItem()=="%TODOS"){
				if	(cbArticulo.getSelectedItem()=="%TODOS" || cbArticulo.getSelectedItem()=="") {
					consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and H.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'order by I.DescripcionInv asc";
					}
					else if	(cbArticulo.getSelectedItem()!="%TODOS" || cbArticulo.getSelectedItem()!="") {
						consultar="SELECT * FROM HABITACION as H,INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.NumeroHab = H.NumeroHab and IH.Id_Inv = I.Id_Inv and H.Tipo_Hab ='"+cbTipo.getSelectedItem()+"'and I.DescripcionInv ='"+cbArticulo.getSelectedItem()+"'order by I.DescripcionInv asc";
					}
			 }
			 llenarTable();
		}
	}

	void modificar() {
		try {
			ID_HAB="";ID_ART="";tipo="";articulo="";
			ID_HAB=(String) tableList.getValueAt(tableList.getSelectedRow(),0);	
			ID_ART=(String) tableList.getValueAt(tableList.getSelectedRow(),2);	
			tipo=(String) tableList.getValueAt(tableList.getSelectedRow(),1);
			articulo=(String) tableList.getValueAt(tableList.getSelectedRow(),3);
			textCantidad.setText((String)tableList.getValueAt(tableList.getSelectedRow(),5));
			cantidad=(Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),5).toString()));
			
			cbTipo.setSelectedItem(tipo);
			cbNumero.setSelectedItem(ID_HAB);
			cbArticulo.setSelectedItem(articulo);
			activarButton(false);
		} catch (Exception e) {}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			if (e.getSource().equals(tableList)) {

			}
		} catch (Exception x) {}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent Mouse_evt) {
		try {
			if (Mouse_evt.getSource().equals(tableList)) {
				ID_HAB="";ID_ART="";tipo="";articulo="";
				ID_HAB=(String) tableList.getValueAt(tableList.getSelectedRow(),0);	
				ID_ART=(String) tableList.getValueAt(tableList.getSelectedRow(),2);	
				tipo=(String) tableList.getValueAt(tableList.getSelectedRow(),1);
				articulo=(String) tableList.getValueAt(tableList.getSelectedRow(),3);
				textCantidad.setText((String)tableList.getValueAt(tableList.getSelectedRow(),5));
				cantidad=(Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),5).toString()));
				activarButton(true);
				if (Mouse_evt.getClickCount() == 2) {
					MOD=1;
					modificar();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
}

