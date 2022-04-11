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
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;

public class VentanaCategoria implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public JInternalFrame frame;
	public JPanel  panelDto,panelLst,panePie;
	private JLabel				lbl1,lbl2,lbl3,lbl4;
	private JButton  			buttonNuevo,buttonGrabar,buttonSalir,buttonEliminar;
	protected static String dateEmision;
	protected static JTextField 			textCod,textDescripcion,textBuscar;
	protected static JComboBox<String> 		cbFamilia;
	
	static int CONTAR_VENTANA_CATEGORIA=0;
	public static int MOD;
	
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	
	NumberFormat formatPrecio;
	private String consultar="";
	private int id=0;private String descripcion="",familia="";
	
	// constructor
	public VentanaCategoria() {
		frameCategoria();
		crearPanel();
		crearButtons();
		crearTextFields();
		crearLabels();
		crearTable();
		llenarcbFamilia();
		llenarNuevo();
		llenarTable("");
		CONTAR_VENTANA_CATEGORIA ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
		
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    Menu.Desktop.add(frame);
	}
	public void frameCategoria() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaCategoria.class.getResource("/modelo/Images/Category.png")));
		frame.setTitle("Agregar categorias");
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				CONTAR_VENTANA_CATEGORIA=0;
				try {
					VentanaInventariosAgregar.cbTipo.removeAllItems();
					VentanaInventariosAgregar.llenarcbCategoria();
				} catch (Exception e) {}
			}
		});
		frame.setClosable(true);
		frame.setBounds(100, 100, 570, 369);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelDto= new JPanel();
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		panelDto.setBounds(10, 11, 537, 112);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		panelLst= new JPanel();
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 124, 537, 166);
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
		
		panePie= new JPanel();
		panePie.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panePie.setBounds(10, 289, 537, 39);
		frame.getContentPane().add(panePie);
		panePie.setLayout(null);
	}
	
	public void llenarNuevo() {
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select Id_Cat from CATEGORIA order by Id_Cat desc limit 0,1");
			if (resultSet.next()== true) {
				int id=(Integer.parseInt(resultSet.getString("Id_Cat"))+1);
				textCod.setText(Menu.formatid_4.format(id));
			}else {
				textCod.setText(Menu.formatid_4.format(0001));
			}
			MOD=0;//PERMITE GRABAR NUEVO
			statement.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al load" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void crearLabels() {
		lbl1= new JLabel("Código:");
		lbl1.setBounds(21, 28, 75, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
		
		lbl2= new JLabel("Tipo inventario:");
		lbl2.setBounds(12, 54, 84, 14);
		panelDto.add(lbl2);
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl2.setFont(Menu.fontLabel);
		
		lbl3= new JLabel("Filtrar:");
		lbl3.setBounds(262, 17, 48, 14);
		panePie.add(lbl3);
		lbl3.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl3.setFont(Menu.fontLabel);
	
		lbl4= new JLabel("Descripción:");
		lbl4.setBounds(12, 80, 84, 14);
		panelDto.add(lbl4);
		lbl4.setHorizontalAlignment(SwingConstants.RIGHT);
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
		textCod.setBounds(104, 25, 75, 22);
		panelDto.add(textCod);
		
		cbFamilia = new JComboBox<>();
		cbFamilia.setBounds(104, 51, 151, 21);
		cbFamilia.setFont(Menu.fontText);
		cbFamilia.addActionListener(this);
		cbFamilia.addFocusListener(this);
		cbFamilia.addKeyListener(this);
		panelDto.add(cbFamilia);
		
		textDescripcion= new JTextField();
		textDescripcion.setColumns(10);
		textDescripcion.setFont(Menu.fontText);
		textDescripcion.setForeground(Menu.textColorForegroundInactivo);
		textDescripcion.setHorizontalAlignment(SwingConstants.LEFT);
		textDescripcion.addActionListener(this);
		textDescripcion.addFocusListener(this);
		textDescripcion.addKeyListener(this);
		textDescripcion.setBounds(104, 76, 266, 22);
		panelDto.add(textDescripcion);

		textBuscar= new  JTextFieldIcon(new JTextField(),"searchBlue.png","Search","searchBlue.png");
		textBuscar.setColumns(10);
		textBuscar.setFont(Menu.fontText);
		textBuscar.setForeground(Menu.textColorForegroundInactivo);
		textBuscar.setHorizontalAlignment(SwingConstants.LEFT);
		textBuscar.addActionListener(this);
		textBuscar.addFocusListener(this);
		textBuscar.addKeyListener(this);
		textBuscar.setBounds(314, 11, 213, 22);
		panePie.add(textBuscar);
	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setToolTipText("Nuevo ítem");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setBounds(376, 76, 36, 23);
		buttonNuevo.setIcon(new ImageIcon(VentanaCategoria.class.getResource("/modelo/Images/nuevo.png")));
		panelDto.add(buttonNuevo);
		
		buttonGrabar= new JButton("");
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setBounds(415, 76, 36, 23);
		buttonGrabar.setIcon(new ImageIcon(VentanaCategoria.class.getResource("/modelo/Images/save.png")));
		panelDto.add(buttonGrabar);
		
		buttonEliminar= new JButton("");
		buttonEliminar.setToolTipText("Eliminar ítem");
		buttonEliminar.addActionListener(this);
		buttonEliminar.setBounds(453, 76, 36, 23);
		buttonEliminar.setIcon(new ImageIcon(VentanaCategoria.class.getResource("/modelo/Images/delete.png")));
		panelDto.add(buttonEliminar);
		
		buttonSalir= new JButton("");
		buttonSalir.setToolTipText("Regresar");
		buttonSalir.addActionListener(this);
		buttonSalir.setBounds(491, 76, 36, 23);
		buttonSalir.setIcon(new ImageIcon(VentanaCategoria.class.getResource("/modelo/Images/Exit.png")));
		panelDto.add(buttonSalir);
	}
	public void crearTable() {
		tableList = new JTable();
		tableList.addMouseListener(this);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tableList.setBounds(10, 20, 527, 146);
		
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 20, 517, 135);
        panelLst.add(scrollTable);
        
        tableList.setGridColor(new Color(238, 232, 170));
	}
	
	public static void llenarcbFamilia() { 
		cbFamilia.removeAllItems();
		cbFamilia.addItem("INV_HOTEL");
		cbFamilia.addItem("VITRINA");
		cbFamilia.setSelectedIndex(-1);
	}
	
	protected void llenarTable(String buscar) {
		conexion = new ConexionDB();
		LimpiarTable();
        try {
	     	if (buscar.equals("")) {
	     		consultar="Select * from CATEGORIA where FamiliaCat ='" +cbFamilia.getSelectedItem()+"'";
	     	 }else{
				consultar="Select * from CATEGORIA where FamiliaCat ='" +cbFamilia.getSelectedItem()+"' and DescripcionCat like '" + buscar +"%'";
	     	 }
     	   int totalitems=0;
     	   model= new DefaultTableModel();
		   model.addColumn("Código");
		   model.addColumn("Tipo inventario");
		   model.addColumn("Descripción");
		   
 		   String []datos= new String[3];
 		   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(consultar);
 		   tableList.setModel(model);
 		   
    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(30);
    	   tableList.getColumnModel().getColumn(1).setPreferredWidth(80);
    	   tableList.getColumnModel().getColumn(2).setPreferredWidth(360);
    	   while(rs.next()) {
		    	datos[0]=Menu.formatid_4.format(rs.getInt("Id_Cat"));
		    	datos[1]=rs.getString("FamiliaCat");
		    	datos[2]=rs.getString("DescripcionCat");
		    	
	            totalitems=totalitems+1;
	            model.addRow(datos);
	            tableList.setModel(model);
	        }
           rs.close();
           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	
	public void limpiarTexts() {
		textCod.setText(null);

		textDescripcion.setText(null);
		textDescripcion.setBackground(Menu.textColorBackgroundInactivo);	
		textDescripcion.setForeground(Menu.textColorForegroundInactivo);
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(106, 90, 205)));
		textDescripcion.requestFocus();
	}
	
	public void activarTexts(boolean b) {
		textCod.setEnabled(b);
		textDescripcion.setEnabled(b);
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
	
	//	METODO ELIMINAR
	public void delete() {
		if (id==0){
			JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			tableList.requestFocus();
			return;
		}
		try {
			conexion = new ConexionDB();
			String query ="Select * from CATEGORIA as C,INVENTARIOS as I where I.Id_Cat=C.Id_Cat and C.Id_Cat='" + id  + "'";
			Statement statement =  conexion.gConnection().createStatement();
			ResultSet rs=statement.executeQuery(query);
			if (rs.next()==true) {
				JOptionPane.showMessageDialog(null, "No se permite eliminar el ítem " + id +" "+ descripcion   +" "+ Menu.separador + "porque existen artículos registrados con esta ...!" ,Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
				statement.close();
				return;
			}	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar la categoria?" + Menu.separador + id + " " +  descripcion, Menu.SOFTLE_HOTEL,
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (respuesta == JOptionPane.YES_OPTION) {
			try {
				conexion = new ConexionDB();
				String query ="Delete from CATEGORIA where Id_Cat='" + id  + "'";
				Statement statement =  conexion.gConnection().createStatement();
				statement.execute(query);
				JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				statement.close();
				llenarTable("");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else if (respuesta == JOptionPane.NO_OPTION) {}
	}
	public void actionPerformed(ActionEvent evento) {
		  if (evento.getSource() == buttonNuevo){// NUEVO
				limpiarTexts();
				textBuscar.setText(null);
				llenarTable("");
				llenarNuevo();
			  }
		  if (evento.getSource() == buttonGrabar){// GRABAR
			  insertarUpdate();
			  }
		  if (evento.getSource() == buttonSalir){// SALIR
			  frame.dispose();
			  }
		  if (evento.getSource() == buttonEliminar){// ELIMINAR
			  delete();
			  }
		  if (evento.getSource() == cbFamilia){	// CBFAMILIA
			  llenarTable("");
			  }
		}
		public void insertarUpdate() {
			if (textCod.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textCod.requestFocus();
				return;
			}
			if (cbFamilia.getSelectedItem()==null || cbFamilia.getSelectedItem()==""){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				cbFamilia.requestFocus();
				return;
			}
			if (textDescripcion.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textDescripcion.requestFocus();
				return;
			}

			conexion = new ConexionDB();
			if (MOD==0) {// REGISTRAR
				try {
					String sql ="INSERT INTO  CATEGORIA (Id_Cat,FamiliaCat,DescripcionCat) VALUES (?,?,?)";
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText()));
					ps.setString(2,(String)cbFamilia.getSelectedItem());
					ps.setString(3,textDescripcion.getText().trim());
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
			         String sql="UPDATE CATEGORIA SET Id_Cat = ?,"
			                 + "FamiliaCat = ?,"
			                 + "DescripcionCat = ?"
			                 + "WHERE Id_Cat = '"+Integer.parseInt(textCod.getText())+"'"; 
					
					PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(textCod.getText()));
					ps.setString(2,(String)cbFamilia.getSelectedItem());
					ps.setString(3,textDescripcion.getText().trim());
					
					ps.executeUpdate();
					ps.close();
					JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					limpiarTexts();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
					}
			}
			llenarTable("");
		}
		
		public void focusGained(FocusEvent ev) {
			if (ev.getSource() == cbFamilia){cbFamilia.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == cbFamilia){cbFamilia.setForeground(Menu.textColorForegroundActivo);}
			if (ev.getSource() == textDescripcion){textDescripcion.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textDescripcion){textDescripcion.setForeground(Menu.textColorForegroundActivo);}
			
			if (ev.getSource() == textBuscar){textBuscar.setBackground(Menu.textColorBackgroundActivo);}
			if (ev.getSource() == textBuscar){textBuscar.setForeground(Menu.textColorForegroundActivo);}
		}
		public void focusLost(FocusEvent ev) {
			if (ev.getSource() == cbFamilia){cbFamilia.setBackground(new Color(240,240,240));}
			if (ev.getSource() == cbFamilia){cbFamilia.setForeground(Menu.textColorForegroundInactivo);}
			if (ev.getSource() == textDescripcion){textDescripcion.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textDescripcion){textDescripcion.setForeground(Menu.textColorForegroundInactivo);}

			if (ev.getSource() == textBuscar){textBuscar.setBackground(Menu.textColorBackgroundInactivo);}
			if (ev.getSource() == textBuscar){textBuscar.setForeground(Menu.textColorForegroundInactivo);}
		}
		
		public void keyReleased(KeyEvent evet) {
			char e=evet.getKeyChar();
			if (evet.getSource() == textBuscar){
				int pos = textBuscar.getCaretPosition();textBuscar.setText(textBuscar.getText().toUpperCase());textBuscar.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				llenarTable(textBuscar.getText());
			}
			if (evet.getSource() == cbFamilia){
				if (e==KeyEvent.VK_ENTER){
					if (cbFamilia.getSelectedIndex()!=-1){
						textDescripcion.requestFocus();
					}else{
						cbFamilia.requestFocus();
					}
				}	
			}
			if (evet.getSource() == textDescripcion){
				int pos = textDescripcion.getCaretPosition();textDescripcion.setText(textDescripcion.getText().toUpperCase());textDescripcion.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
				if (textDescripcion.getText().toLowerCase().isEmpty()|| textDescripcion.getText().toLowerCase().length()>75){
					textDescripcion.requestFocus();
					textDescripcion.selectAll();
					textDescripcion.setText(null);
					} 
					else if (e==KeyEvent.VK_ENTER || textDescripcion.getText().toLowerCase().length()==75){
						buttonGrabar.doClick();
					}
			}	
		}

		public void keyTyped(KeyEvent evet) {
			// PRECIONA EL TECLADO Y ME DA EL EVENTO
			//char e=evet.getKeyChar();
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
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent Mouse_evt) {
		// TODO Auto-generated method stub
		id=0;descripcion="";
		try {
			if (Mouse_evt.getSource().equals(tableList)) {
				id=Integer.parseInt(tableList.getValueAt(tableList.getSelectedRow(),0).toString());
				if (id==0){
					tableList.requestFocus();
					return;
				}
				familia=(String) tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
				descripcion=(String) tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim();
				if (Mouse_evt.getClickCount() == 2) {
					textCod.setText(Menu.formatid_4.format(id));
					cbFamilia.setSelectedItem(familia);
					textDescripcion.setText(descripcion);
					panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos - Modificando", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(165, 42, 42)));
					MOD=1; // PERMITE MODIFICAR
					//textBuscar.setText(descripcion);
					llenarTable(descripcion);
				}
			}
		} catch (Exception e) {}
	}
	}
