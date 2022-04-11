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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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


public class VentanaTipoHabitacion implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
	private static ConexionDB conexion;
	JInternalFrame frame;
	JPanel  panelBtn = new JPanel();JPanel  panelDatos = new JPanel();JPanel panelLst = new JPanel();
	JTextField 			[] textField = new JTextField[2];
	JLabel				[] label =  new JLabel[1];
	JButton     		[] button = new JButton[13];

	JScrollPane scrollTable;
    JTable tableList;
	DefaultTableModel model;
	
	int i;
	
	String tipo="";
	
	
	// constructor
	public VentanaTipoHabitacion() {
		WindowTipoHabitacion();
		crearPanel();
		crearLabels();
		crearTextFields();
		crearButtons();
		activarButton(true);
		crearTable();
		llenarTable();
		centrar();
		Menu.Desktop.add(frame);
	}
	
	void centrar (){
        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
        frame.setLocation (x, y);
	    
	}
	public void WindowTipoHabitacion() {
		frame = new JInternalFrame();
		frame.setFrameIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/homeAzul.png")));
		frame.addInternalFrameListener(new InternalFrameAdapter() {
			//@Override
			public void internalFrameClosed(InternalFrameEvent arg0) {
				  VentanaHabitacionAgregar.llenarTipoHabitacion();
			}
		});
		frame.setClosable(true);
		frame.setTitle("Tipo de Habitación");
		frame.setBounds(100, 100, 497, 175);
		frame.getContentPane().setLayout(null);
	}
	public void crearPanel() {
		panelBtn.setBorder(new TitledBorder(null, "Controles", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBtn.setBounds(10, 11, 463, 52);
		frame.getContentPane().add(panelBtn);
		panelBtn.setLayout(null);
		
		panelDatos.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDatos.setBounds(10, 63, 463, 73);
		frame.getContentPane().add(panelDatos);
		panelDatos.setLayout(null);
		panelDatos.setVisible(true);

		panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelLst.setBounds(10, 11, 463, 190);
		frame.getContentPane().add(panelLst);
		panelLst.setLayout(null);
		panelLst.setVisible(true);
	}
	public void crearLabels() {
		int l=label.length;
		for(i=0; i<l; i++) {
			label[i]= new JLabel();
			label[i].setHorizontalAlignment(SwingConstants.RIGHT);
			panelDatos.add(label[i]);
			if (i==0){
				label[i].setBounds(10, 29, 76, 14);
				label[i].setText("Descripción:");
			}
		}
	}
	public void crearTextFields(){
		int t=textField.length;
		for( i=0; i<t; i++){
			if (i==0){
				textField[i] = new JTextField();
				textField[i].setBounds(96, 26, 319, 22);
				panelDatos.add(textField[i]);
			}
			if (i==1){
				textField[i] = new JTextFieldIcon(new JTextField(),"Search16.png","Search","Search16.png");
				textField[i].setBounds(10, 15, 370, 22);
				panelLst.add(textField[i]);
			}
			
			textField[i].setFont(Menu.fontText);
			textField[i].setColumns(10);
			textField[i].addActionListener(this);
			textField[i].addFocusListener(this);
			textField[i].addKeyListener(this);
		}
	}
	public void crearButtons() {
		int b=button.length;
		for(i=0;i<b; i++) {
			button[i]= new JButton();
			panelBtn.add(button[i]);
			button[i].addActionListener(this);
			switch (i) {
			case 0:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/nuevo.png")));
				button[i].setToolTipText("Nuevo ítem");
				button[i].setText("");
				button[i].setBounds(225, 20, 36, 23);
				break;
			case 1:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/undo.png")));
				button[i].setToolTipText("Deshacer ítem");
				button[i].setText("");
				button[i].setBounds(263, 20, 36, 23);
				break;
			case 2:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/save.png")));
				button[i].setToolTipText("Grabar ítem");
				button[i].setText("");
				button[i].setBounds(301, 20, 36, 23);
				break;
			case 3:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/edit.png")));
				button[i].setToolTipText("Modificar ítem");
				button[i].setText("");
				button[i].setBounds(339, 20, 36, 23);
				break;
			case 4:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/delete.png")));
				button[i].setToolTipText("Eliminar ítem");
				button[i].setText("");
				button[i].setBounds(377, 20, 36, 23);
				break;
			case 5:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/Exit.png")));
				button[i].setToolTipText("Salir");
				button[i].setText("");
				button[i].setBounds(415, 20, 36, 23);
				break;
				
			case 6:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/hide-left.png")));
				button[i].setToolTipText("Primer ítem");
				button[i].setText("");
				button[i].setBounds(10, 20, 36, 23);
				break;
			case 7:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/navigate-left.png")));
				button[i].setToolTipText("Anterior ítem");
				button[i].setText("");
				button[i].setBounds(48, 20, 36, 23);
				break;
			case 8:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/navigate-right.png")));
				button[i].setToolTipText("Siguiente ítem");
				button[i].setText("");
				button[i].setBounds(86, 20, 36, 23);
				break;
			case 9:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/hide-right.png")));
				button[i].setToolTipText("Ultimo ítem");
				button[i].setText("");
				button[i].setBounds(124, 20, 36, 23);
				break;
			case 10:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/search.png")));
				button[i].setToolTipText("Buscar");
				button[i].setText("");
				button[i].setBounds(415, 25, 36, 24);
				panelDatos.add(button[i]);
				break;
			case 11:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/ok.png")));
				button[i].setToolTipText("Ok");
				button[i].setText("");
				button[i].setBounds(380, 14, 36, 24);
				panelLst.add(button[i]);
				break;
			case 12:
				button[i].setIcon(new ImageIcon(VentanaTipoHabitacion.class.getResource("/modelo/Images/mant-cancelar.png")));
				button[i].setToolTipText("Regresar");
				button[i].setText("");
				button[i].setBounds(415, 14, 36, 24);
				panelLst.add(button[i]);
				break;
			default:
				break;
			}
		}
	}
	
	public void crearTable(){
		tableList = new JTable(); 
		tableList.addMouseListener(this);
		tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		scrollTable = new JScrollPane();
		
		scrollTable.setViewportView(tableList);
		scrollTable.setBounds(10, 40, 442, 140);
        panelLst.add(scrollTable);
        tableList.setGridColor(new Color(238, 232, 170));
	}
	
	public void limpiarTexts() {
		int t=textField.length;
		for( int i=0; i<t; i++){
			textField[i].setText(null);
			textField[i].setBackground(new Color(255, 255,255));	
		}
	}
	
	public void activarTexts(boolean b) {
		int t=textField.length;
		for(int i=0;i<t;i++) {
			textField[i].setEnabled(b);
		}
	}
	
	public void activarButton(boolean c) {
		 if (c == true){
			button[0].setEnabled(true); // NUEVO
			button[1].setEnabled(false);// CALCELAR
			button[2].setEnabled(false); // GRABAR
			button[3].setEnabled(true);// EDITAR
			button[4].setEnabled(true);// ELIMINAR
			button[5].setEnabled(true);// SALIR
			
			button[6].setEnabled(true);
			button[7].setEnabled(true);
			button[8].setEnabled(true);
			button[9].setEnabled(true);
			button[10].setEnabled(true);
			panelLst.setVisible(false);
			panelDatos.setVisible(true);
			panelBtn.setVisible(true);
		 }
		 if (c == false){
			button[0].setEnabled(false); // NUEVO
			button[1].setEnabled(true);// CALCELAR
			button[2].setEnabled(true); // GRABAR
			button[3].setEnabled(false);// EDITAR
			button[4].setEnabled(false);// ELIMINAR
			button[5].setEnabled(true);// SALIR
			
			button[6].setEnabled(false);
			button[7].setEnabled(false);
			button[8].setEnabled(false);
			button[9].setEnabled(false);
			button[10].setEnabled(false);
			panelLst.setVisible(false);
			panelDatos.setVisible(true);
			panelBtn.setVisible(true);
		 }
	}
	protected void llenarTable() {
		int totalitems=0;
		conexion = new ConexionDB();
        try {
    		model= new DefaultTableModel();
    		model.addColumn("Tipo Habitación");
			Statement statement = conexion.gConnection().createStatement();
     	    String consultar="";
     	   if (textField[1].getText().equals("")) {
     		   consultar="Select * from TIPO_HABITACION order by Tipo_Hab asc";
     	   }else{
     		   consultar="Select * from TIPO_HABITACION where Tipo_Hab like'"+textField[1].getText()+"%'order by Tipo_Hab asc";
     	   }
		   String []datos= new String[1];
           ResultSet rs=statement.executeQuery(consultar);
           
           tableList.setModel(model);
    	   tableList.getColumnModel().getColumn(0).setPreferredWidth(40);
           while(rs.next())
            {
	            datos[0]=rs.getString("Tipo_Hab");
	            totalitems=totalitems+1;
	            model.addRow(datos);
	                
	            tableList.setModel(model);
            }
           rs.close();
           statement.close();
           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
		} catch (SQLException e) {
			//JOptionPane.showMessageDialog(null, "Error al llenar la table" + e);
		}
        conexion.DesconectarDB();
	}
	
	void LimpiarTable(){
		try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
	}
	//METODO BUSCAR =================
	public void buscar() {
		if (textField[1].getText().toLowerCase().trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Busca mediante el Tipo de habitación...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
			limpiarTexts();
			textField[1].requestFocus();
			return;
		}
		conexion = new ConexionDB();
		try {
		
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from TIPO_HABITACION where Tipo_Hab like'"+textField[1].getText()+"%'order by Tipo_Hab asc");
			if (resultSet.next()== true) {
				textField[0].setText(resultSet.getString("Tipo_Hab"));
				panelBtn.setVisible(true);
				panelLst.setVisible(false);
				panelDatos.setVisible(true);
				textField[0].selectAll();
				textField[0].requestFocus();
			}else{
				limpiarTexts();
				textField[1].requestFocus();
				textField[1].selectAll();
			}
			frame.setBounds(100, 100, 497, 175);
			centrar();
			resultSet.close();
			statement.close();
		} catch (Exception e) {}
		conexion.DesconectarDB();
	}
	public void actionPerformed(ActionEvent evento) {
	  if (evento.getSource() == button[0]){// NUEVO
		  limpiarTexts();
		  activarButton(false);
		  textField[0].requestFocus(true);
		  }
	  if (evento.getSource() == button[1]){// CANCELAR
		  limpiarTexts();
		  activarButton(true);
		  button[0].requestFocus(true);
		  }
	  if (evento.getSource() == button[2]){// GRABAR
		  if (textField[1].getText().length()==0) {
			  insertar();
			  LimpiarTable();
		  }else {
			  actualizar();
			  LimpiarTable();
		  	}
		  }
	  if (evento.getSource() == button[3]){// EDITAR
		  activarButton(false);
		  textField[0].requestFocus(true);
		  
		  }
	  if (evento.getSource() == button[4]){// ELIMINAR
		  delete();
		  }
	  if (evento.getSource() == button[5]){// SALIR
		  frame.dispose();
		  VentanaHabitacionAgregar.llenarTipoHabitacion();
		  }
	  if (evento.getSource() == button[10]){// BUSCAR
		  	panelBtn.setVisible(false);
		  	panelDatos.setVisible(false);
		  	panelLst.setVisible(true);
		  	
		  	textField[1].requestFocus();
		  	textField[1].selectAll();
		  	
		  	frame.setBounds(100, 100, 497, 240);
		  	centrar(); 
		  	
			llenarTable();
		  }
	  if (evento.getSource() == button[11]){// OK
		  buscar();
		  }
	  if (evento.getSource() == button[12]){// REGRESAR
		  	panelBtn.setVisible(true);
			panelLst.setVisible(false);
			panelDatos.setVisible(true);
			frame.setBounds(100, 100, 497, 175);
			centrar();
		  }

	}
	
	public void focusGained(FocusEvent ev) {
		if (ev.getSource() == textField[0]){textField[0].setBackground(Menu.textColorBackgroundActivo);textField[0].setForeground(Menu.textColorForegroundActivo);} 
		if (ev.getSource() == textField[1]){textField[1].setBackground(Menu.textColorBackgroundActivo);textField[1].setForeground(Menu.textColorForegroundActivo);} 
	}
	public void focusLost(FocusEvent ev) {
		if (ev.getSource() == textField[0]){textField[0].setBackground(Menu.textColorBackgroundInactivo);textField[0].setForeground(Menu.textColorForegroundInactivo);}
		if (ev.getSource() == textField[1]){textField[1].setBackground(Menu.textColorBackgroundInactivo);textField[1].setForeground(Menu.textColorForegroundInactivo);}
	}
	@Override
	public void keyReleased(KeyEvent evet) {
		// TODO ESTE METODO ES PARA MAYUSCULAS
		for (int i = 0; i < textField.length; i++) {
			int pos = textField[i].getCaretPosition();textField[i].setText(textField[i].getText().toUpperCase());textField[i].setCaretPosition(pos);
		}
		char e=evet.getKeyChar();
		if (evet.getSource() == textField[0]){
			if (textField[0].getText().toLowerCase().isEmpty()|| textField[0].getText().toLowerCase().length()>30){
				textField[0].requestFocus();
				textField[0].selectAll();
				textField[0].setText(null);
			}else if (e==KeyEvent.VK_ENTER || textField[0].getText().toLowerCase().length()==30){
					button[2].doClick();
				}
		} 
		
		if (evet.getSource() == textField[1]){
			llenarTable();
			if (e==KeyEvent.VK_ENTER){
				buscar();
			}
		}
		
	}
	
	@Override
	public void keyPressed (KeyEvent evet) {

	}
	public void keyTyped(KeyEvent evet) {
		// PRECIONA EL TECLADO Y ME DA EL EVENTO
		char e=evet.getKeyChar();
		if (evet.getSource() == textField[0]){ // NOMBRE
			if(!Character.isLetter(e)&&e!=KeyEvent.VK_SPACE&&e!=KeyEvent.VK_BACK_SPACE){frame.getToolkit().beep(); evet.consume();}
		}
			
	}	
	
	//METODO ELIMINAR =================
	public void delete() {
		int [] text= {0}; 
		for (int j :text) {
			if (textField[j].getText().toLowerCase().isEmpty()){
				JOptionPane.showMessageDialog(null, "Debe seleccionar un ítem...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textField[j].requestFocus();
				return;
			}
		}
		conexion = new ConexionDB();
		try {
			String query ="Select * from HABITACION as H,TIPO_HABITACION as TH where H.tipo_hab=TH.tipo_hab and H.tipo_hab='" + textField[0].getText()  + "'";
			Statement statement =  conexion.gConnection().createStatement();
			ResultSet rs=statement.executeQuery(query);
			if (rs.next()==true) {
				JOptionPane.showMessageDialog(null, "No se permite eliminar el ítem " + textField[0].getText() +" "+ Menu.separador + "porque ha sido asignado a una o más habitaciones ...!" ,Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
				rs.close();
				statement.close();
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem?", Menu.SOFTLE_HOTEL,		
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (respuesta == JOptionPane.YES_OPTION) {
						try {
							Statement statement = conexion.gConnection().createStatement();
							String query="Delete from TIPO_HABITACION where Tipo_Hab ='"+textField[0].getText()+"'";
							statement.execute(query);
							JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
							statement.close();
							limpiarTexts();

							button[3].setSelected(true);
							//LimpiarTable();
						} catch (Exception e) { e.printStackTrace(); }
					}else if (respuesta == JOptionPane.NO_OPTION) {
					}
		} catch (Exception e) {
		}
		conexion.DesconectarDB();
	}
	
	
	//METODO GRABAR =================
	public void insertar() {
		conexion = new ConexionDB();
		Connection con = conexion.gConnection();
		Statement st;
		int [] text= {0};
		for (int j :text) {
			if (textField[j].getText().trim().toLowerCase().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar el siguiente campo...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textField[j].requestFocus();
				return;
			}
		}
		try {
			
			Statement statement = conexion.gConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from TIPO_HABITACION where Tipo_hab='"+textField[0].getText()+"'");
			if (resultSet.next()== true) {
				JOptionPane.showMessageDialog(null, "Este ítem ya fue registrado",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
				textField[0].requestFocus(true);
				textField[0].selectAll();
				resultSet.close();
				statement.close();
			}else{
				String sql = "INSERT INTO Tipo_Habitacion (Tipo_Hab)"+" VALUES ('"+textField[0].getText()+"')";
		    	st=con.createStatement();
		    	st.executeUpdate(sql);
		    	JOptionPane.showMessageDialog(null, "Datos grabados satisfactoriamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		    	con.close();
		    	st.close();
		    	limpiarTexts();
				activarButton(true);
				button[0].requestFocus(true);
			}
			resultSet.close();
			statement.close();
		} catch (SQLException ex) {
		    ex.printStackTrace();
		}
		conexion.DesconectarDB();
	}
	//METODO ASCTUALIZAR =================
	public void actualizar() {
		int [] text= {0};
		for (int j :text) {
			if (textField[j].getText().trim().toLowerCase().isEmpty()){
				JOptionPane.showMessageDialog(null, "Falta llenar el siguiente campo...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
				textField[j].requestFocus();
				return;
			}
		}
		conexion = new ConexionDB();
		String sql = "UPDATE TIPO_HABITACION SET Tipo_Hab=?"+" WHERE Tipo_Hab like '"+textField[1].getText().trim()+"%'";
		try {
			PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
			ps.setString(1, textField[0].getText().trim());
			ps.executeUpdate();
			ps.close();
	    	JOptionPane.showMessageDialog(null, "Datos actualizados correctamente",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
	    	limpiarTexts();
			activarButton(true);
			button[0].requestFocus(true);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar" + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
		}
		conexion.DesconectarDB();
	}
	
	
	public void mouseReleased(MouseEvent Mouse_evt) {
		// TODO Auto-generated method stub
		tipo="";
		if (Mouse_evt.getSource().equals(tableList)) {
			try {
				tipo=(String)tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
				if (tipo==null || tipo == ""){
					tableList.requestFocus();
					return;
				}
				if (Mouse_evt.getClickCount() == 1) {
					textField[1].setText(tipo);
				}
				
				if (Mouse_evt.getClickCount() == 2) {
					textField[1].setText(tipo);
					button[11].doClick();
				}
			} catch (Exception e) {}
		}
	}	@Override
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
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
