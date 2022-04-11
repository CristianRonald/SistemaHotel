package modelo.Presentacion;

import java.awt.Color;
import java.awt.Toolkit;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import modelo.Datos.ConexionDB;


public class VentanaTarifaTipo implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	public JDialog 	frame;
	private JPanel  panelDto = new JPanel();JPanel  panelLst = new JPanel();


	private JLabel				lbl1;
	private JButton  			buttonNuevo,buttonCancelar,buttonGrabar,buttonEditar,buttonEliminar,buttonSalir;
	private JTextField textNom;
	private JScrollPane scrollTable;
	private JTable tableList;
	private DefaultTableModel model;
	
	private String consultar="";
	private String TIPO_TARIFA="";
	private int MOD=0;
	
	public VentanaTarifaTipo() {
		frameTarifaTipo();
		crearPanel();
		crearButtons();
		crearTable();
		crearTextFields();
		crearLabels();
		activarButton(true);
		
		LimpiarTable();
		llenarTable("");
		frame.setModal(true);
	}
	public void frameTarifaTipo() {
		frame = new JDialog();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaTarifaTipo.class.getResource("/modelo/Images/Dollar_Green.png")));
		frame.setResizable(false);
		frame.setTitle("Mantenimiento: Tipo tarifas                     ");
		frame.setBounds(100, 100, 330, 301);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
	}
	public void crearPanel() {
		panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDto.setBounds(10, 11, 306, 76);
		frame.getContentPane().add(panelDto);
		panelDto.setLayout(null);
		
		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 86, 306, 177);
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
	}
	public void crearLabels(){
		lbl1= new JLabel("Tipo:");
		lbl1.setBounds(10, 48, 37, 14);
		panelDto.add(lbl1);
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl1.setFont(Menu.fontLabel);
	}
	public void crearTextFields() {
		textNom = new JTextField();
		textNom.setColumns(10);
		textNom.setFont(Menu.fontText);
		textNom.setForeground(Menu.textColorForegroundInactivo);
		textNom.setHorizontalAlignment(SwingConstants.LEFT);
		textNom.addActionListener(this);
		textNom.addFocusListener(this);
		textNom.addKeyListener(this);
		textNom.setBounds(57, 43, 226, 22);
		panelDto.add(textNom);
	}
	public void crearButtons() {
		buttonNuevo= new JButton("");
		buttonNuevo.setBounds(57, 15, 36, 23);
		panelDto.add(buttonNuevo);
		buttonNuevo.setToolTipText("Nuevo ítem");
		buttonNuevo.addActionListener(this);
		buttonNuevo.setIcon(new ImageIcon(VentanaTarifaTipo.class.getResource("/modelo/Images/nuevo.png")));
		
		buttonCancelar= new JButton("");
		buttonCancelar.setBounds(95, 15, 36, 23);
		panelDto.add(buttonCancelar);
		buttonCancelar.setToolTipText("Deshacer ítem");
		buttonCancelar.addActionListener(this);
		buttonCancelar.setIcon(new ImageIcon(VentanaTarifaTipo.class.getResource("/modelo/Images/undo.png")));
		
		buttonGrabar= new JButton("");
		buttonGrabar.setBounds(133, 15, 36, 23);
		panelDto.add(buttonGrabar);
		buttonGrabar.setToolTipText("Grabar ítem");
		buttonGrabar.addActionListener(this);
		buttonGrabar.setIcon(new ImageIcon(VentanaTarifaTipo.class.getResource("/modelo/Images/save.png")));
		
		buttonEditar= new JButton("");
		buttonEditar.setBounds(171, 15, 36, 23);
		panelDto.add(buttonEditar);
		buttonEditar.setToolTipText("Modificar ítem");
		buttonEditar.addActionListener(this);
		buttonEditar.setIcon(new ImageIcon(VentanaTarifaTipo.class.getResource("/modelo/Images/edit.png")));
		
		buttonEliminar= new JButton("");
		buttonEliminar.setBounds(209, 15, 36, 23);
		panelDto.add(buttonEliminar);
		buttonEliminar.setToolTipText("Eliminar ítem");
		buttonEliminar.addActionListener(this);
		buttonEliminar.setIcon(new ImageIcon(VentanaTarifaTipo.class.getResource("/modelo/Images/delete.png")));
		
		buttonSalir= new JButton("");
		buttonSalir.setBounds(247, 15, 36, 23);
		panelDto.add(buttonSalir);
		buttonSalir.setToolTipText("Salir");
		buttonSalir.addActionListener(this);
		buttonSalir.setIcon(new ImageIcon(VentanaTarifaTipo.class.getResource("/modelo/Images/Exit.png")));
	}

	public void crearTable() {
		tableList = new JTable(); 
		tableList.addMouseListener(this);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        
		scrollTable = new JScrollPane();
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 20, 280, 146);
        panelLst.add(scrollTable);
        tableList.setGridColor(new Color(238, 232, 170));
	}
	public void limpiarTexts() {
		textNom.setText(null);
		textNom.setBackground(Menu.textColorBackgroundInactivo);	
		textNom.setForeground(Menu.textColorForegroundInactivo);
		
        panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		consultar="SELECT * FROM TIPO_TARIFA order by TipoTar asc";
        llenarTable("");
        TIPO_TARIFA="";
	}
	public void activarTexts(boolean b) {
		textNom.setEnabled(b);
	}
	public void activarButton(boolean c) {
		 if (c == true){
			buttonNuevo.setEnabled(true); // NUEVO
			buttonCancelar.setEnabled(false);// CALCELAR
			buttonGrabar.setEnabled(false);// GRABAR
			buttonEditar.setEnabled(true);	// EDITAR
			buttonEliminar.setEnabled(true);	// ELIMINAR
			buttonSalir.setEnabled(true);	// SALIR
		 }
		 if (c == false){
			buttonNuevo.setEnabled(false); // NUEVO
			buttonCancelar.setEnabled(true);// CALCELAR
			buttonGrabar.setEnabled(true);// GRABAR
			buttonEditar.setEnabled(false);	// EDITAR
			buttonEliminar.setEnabled(false);	// ELIMINAR
			buttonSalir.setEnabled(false);	// SALIR
		 }
	}
	public void focusGained(FocusEvent ev) {
		if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundActivo);}
		if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundActivo);}
	}
	public void focusLost(FocusEvent ev) {
		if (ev.getSource() == textNom){textNom.setBackground(Menu.textColorBackgroundInactivo);}
		if (ev.getSource() == textNom){textNom.setForeground(Menu.textColorForegroundInactivo);} 
	}
	protected void llenarTable(String Tab) {
		conexion = new ConexionDB();
		LimpiarTable();
        try {
        	
     	   int totalitems=0;
     	   model= new DefaultTableModel();
		   model.addColumn("Item");
		   model.addColumn("Tipo");

 		   String []datos= new String[2];
 		   tableList.setModel(model);
 		  
		   consultar="SELECT * FROM TIPO_TARIFA where TipoTar like'" + Tab + "%' order by TipoTar asc";
 		   Statement st = conexion.gConnection().createStatement();
 		   ResultSet rs=st.executeQuery(consultar);
 		  
    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(5);
    	   tableList.getColumnModel().getColumn(1).setPreferredWidth(200);

    	   while(rs.next()) {
               totalitems=totalitems+1;
		    	datos[0]=Integer.toString(totalitems)+" ";
		    	datos[1]=" "+rs.getString("TipoTar");
	            model.addRow(datos);
	            tableList.setModel(model);

	        }
           DefaultTableCellRenderer modeloRight = new DefaultTableCellRenderer();
           modeloRight.setHorizontalAlignment(SwingConstants.RIGHT);
           tableList.getColumnModel().getColumn(0).setCellRenderer(modeloRight);
           
           rs.close();
           	
           	panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	
	//METODO GRABAR =================
	public void insertar() {

		if (textNom.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textNom.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from TIPO_TARIFA where TipoTar ='"+ textNom.getText() +"'");
			if (resultSet.next()== true) {
				JOptionPane.showMessageDialog(null, "El ítem ya fue registrado",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
				textNom.requestFocus(true);
				resultSet.close();
				return;
			}else{
				String sql ="INSERT INTO TIPO_TARIFA (TipoTar) VALUES (?)";
				PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
				ps.setString(1, textNom.getText());
				ps.execute();
				ps.close();
				JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				limpiarTexts();
				activarButton(true);
				buttonNuevo.requestFocus(true);
				}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al grabar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	//METODO ACTUALIZA =================
	public void actualizar() {

		if (textNom.getText().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Falta llenar campos obligatorios...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			textNom.requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
         String sql="UPDATE TIPO_TARIFA SET TipoTar = ?"
                 + "WHERE TipoTar = '"+TIPO_TARIFA +"'"; 
		PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
		ps.setString(1, textNom.getText());
		ps.executeUpdate();
		ps.close();
		JOptionPane.showMessageDialog(null, "Datos actualizados con éxito",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		limpiarTexts();
		activarButton(true);buttonNuevo.requestFocus(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	//METODO ELIMINAR =================
	public void delete() {
		//if (ID_TAR==""){
		if (tableList.getSelectedRow() == -1 || TIPO_TARIFA=="") {
			JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			tableList.requestFocus();
			return;
		}
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select* from TARIFAS where TipoTar ='"+TIPO_TARIFA+"'");
			if(resultSet.next()==true) {
				JOptionPane.showMessageDialog(null, "No se permite eliminar el item...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				statement.close();
				limpiarTexts();
				return;
		}} catch (Exception e) { e.printStackTrace(); }
		
		int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem " + Menu.separador + TIPO_TARIFA  +" ?", Menu.SOFTLE_HOTEL,		
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (respuesta == JOptionPane.YES_OPTION) {
			conexion = new ConexionDB();
			try {
				String query="Delete from TIPO_TARIFA where TipoTar ='"+TIPO_TARIFA+"'";
				Statement st = conexion.gConnection().createStatement();
				st.execute(query);
				st.close();
				JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
				limpiarTexts();
				buttonEliminar.setSelected(true);
				
			} catch (Exception e) { e.printStackTrace(); }
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
		//char e=evet.getKeyChar();
	}
	@Override
	public void keyReleased(KeyEvent evet) {
		// TODO Auto-generated method stub
		char e=evet.getKeyChar();
		if (evet.getSource().equals(textNom)) {
			int pos = textNom.getCaretPosition();textNom.setText(textNom.getText().toUpperCase());textNom.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
			llenarTable(textNom.getText());
			if (textNom.getText().toLowerCase().isEmpty()|| textNom.getText().toLowerCase().length()>=38){
				textNom.requestFocus();
				textNom.selectAll();
				textNom.setText(null);
				} 
				 if (e==KeyEvent.VK_ENTER){
					 buttonGrabar.doClick();
				}
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void actionPerformed(ActionEvent action) {
		// TODO Auto-generated method stub
		if (action.getSource().equals(buttonNuevo)) {
			  MOD=0;
			  limpiarTexts();
			  activarButton(false);
			  activarTexts(true);
			  textNom.requestFocus();
		}
		if (action.getSource().equals(buttonCancelar)) {
			  limpiarTexts();
			  activarButton(true);
			  buttonNuevo.requestFocus(true);
			  
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
		

	}
	void modificar() {
		try {
			TIPO_TARIFA="";
			TIPO_TARIFA=(String) tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();	
			textNom.setText((String)tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim());
			activarButton(false);
		} catch (Exception e) {}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			if (e.getSource().equals(tableList)) {
				TIPO_TARIFA="";
				TIPO_TARIFA=(String) tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();	
				textNom.setText((String)tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim());
				activarButton(true);
				MOD=1;
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
		if (Mouse_evt.getSource().equals(tableList)) {
			if (Mouse_evt.getClickCount() == 2) {
				modificar();
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent evet) {
		// TODO Auto-generated method stub
	}

}

