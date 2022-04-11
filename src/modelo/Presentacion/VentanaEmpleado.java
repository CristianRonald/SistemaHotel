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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;

public class VentanaEmpleado implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public static JInternalFrame frame;
		private JPanel  			panelBtn,panelLst;
		public  JLabel				lbl1,lbl2;
		public JTextField 			textBus;
		protected JButton  			buttonNuevo,buttonEditar,buttonEliminar,buttonSalir,buttonBus,buttonUsu;
		JComboBox<String> cbBus,cbEstado;
	    
		private JScrollPane scrollTable;
		private JTable tableList;
		private DefaultTableModel model;
		
		public Integer totalitems;
		public static int CONTAR_VENTANA_PERSONAL=0;
		
		public static String id="",descripcion="";
		private String consultar="";
		// constructor
		public VentanaEmpleado() {
			super();
			
			frameEmpleado();
			crearPanel();
			crearButtons();
			crearTable();
			crearComboBox();
			crearTextFields();
			crearLabels();
			panelLst.setVisible(true); // PANEL LISTA
			llenarcbBuscar();
			llenarcbEstado();
			
			llenarTable();
			
			CONTAR_VENTANA_PERSONAL ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
			
			Menu.Desktop.add(frame);
	        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
	        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
	    	frame.setLocation(x, y);
		}
		public void frameEmpleado() {
			frame = new JInternalFrame();
			frame.setFrameIcon(new ImageIcon(VentanaEmpleado.class.getResource("/modelo/Images/personal.png")));
			frame.setTitle("Padrón: personal");
			frame.addInternalFrameListener(new InternalFrameAdapter() {
				@Override
				public void internalFrameActivated(InternalFrameEvent e) {
					llenarTable();
				}
				//@Override
				public void internalFrameClosed(InternalFrameEvent arg0) {
					CONTAR_VENTANA_PERSONAL=0;
				}
			});
			frame.setClosable(true);
			frame.setBounds(100, 100, 721, 345);
			frame.getContentPane().setLayout(null);
		}
		public void crearPanel() {
			panelBtn = new JPanel();
			panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelBtn.setBounds(10, 250, 695, 49);
			frame.getContentPane().add(panelBtn);
			panelBtn.setLayout(null);
			
			panelLst = new JPanel();
			panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLst.setBounds(10, 11, 695, 238); //10, 333, 659, 268
			frame.getContentPane().add(panelLst);
			panelLst.setLayout(null);
			
		}
		void LimpiarTable(){
			try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
		}
		public void llenarTable () {
			id="";descripcion="";
			float TOT=0;
			conexion = new ConexionDB();
	        try {
	     	   totalitems=0;
	     	   
	   		if (VentanaLogin.TIP_USUARIO.equals("ADMINISTRADOR")||VentanaLogin.TIP_USUARIO.equals("PROGRAMADOR")) {
		     	   if (textBus.getText().isEmpty()) {
			     		  consultar="Select * from EMPLEADO where ActividadEmp ='" + cbEstado.getSelectedItem() +"'";
			     	   }else{
							if(cbBus.getSelectedItem()=="CODIGO") {
								consultar="Select * from EMPLEADO where Id_Emp ='" +textBus.getText()+"'and ActividadEmp ='" + cbEstado.getSelectedItem() +"'";
							}
							if(cbBus.getSelectedItem()=="NOMBRE") {
								consultar="Select * from EMPLEADO where NombresEmp like'" +textBus.getText()+"%'and ActividadEmp ='" + cbEstado.getSelectedItem() +"'";
							}
							if(cbBus.getSelectedItem()=="DNI") {
								consultar="Select * from EMPLEADO where DniEmp like'" +textBus.getText()+"%'and ActividadEmp ='" + cbEstado.getSelectedItem() +"'";
							}
							if(cbBus.getSelectedItem()=="CARGO") 
								consultar="Select * from EMPLEADO where CargoEmp like'" +textBus.getText()+"%'and ActividadEmp ='" + cbEstado.getSelectedItem() +"'";
							}
			}else{
		     	   if (textBus.getText().isEmpty()) {
			     		  consultar="Select * from EMPLEADO where ActividadEmp ='" + cbEstado.getSelectedItem() +"'and Id_Emp ='" + VentanaLogin.ID_EMP.trim() +"'";
			     	   }else{
							if(cbBus.getSelectedItem()=="CODIGO") {
								consultar="Select * from EMPLEADO where Id_Emp ='" +textBus.getText()+"'and ActividadEmp ='" + cbEstado.getSelectedItem() +"'and Id_Emp ='" + VentanaLogin.ID_EMP.trim() +"'";
							}
							if(cbBus.getSelectedItem()=="NOMBRE") {
								consultar="Select * from EMPLEADO where NombresEmp like'" +textBus.getText()+"%'and ActividadEmp ='" + cbEstado.getSelectedItem() +"'and Id_Emp ='" + VentanaLogin.ID_EMP.trim() +"'";
							}
							if(cbBus.getSelectedItem()=="DNI") {
								consultar="Select * from EMPLEADO where DniEmp like'" +textBus.getText()+"%'and ActividadEmp ='" + cbEstado.getSelectedItem() +"'and Id_Emp ='" + VentanaLogin.ID_EMP.trim() +"'";
							}
							if(cbBus.getSelectedItem()=="CARGO") 
								consultar="Select * from EMPLEADO where CargoEmp like'" +textBus.getText()+"%'and ActividadEmp ='" + cbEstado.getSelectedItem() +"'and Id_Emp ='" + VentanaLogin.ID_EMP.trim() +"'";
							}
			}
	   		
		     	   Statement st = conexion.gConnection().createStatement();
		 		   ResultSet rs=st.executeQuery(consultar);
		 		   
				   model= new DefaultTableModel();
				   model.addColumn("Código");
			       model.addColumn("Nombres");
				   model.addColumn("Dni");
				   model.addColumn("Teléfono");
			       model.addColumn("Cargo");
			       model.addColumn("Sueldo S/.");
		       
		 		   String []datos= new String[6];
		 		   tableList.setModel(model);
		     	  
		           while(rs.next()) {
		            datos[0]=" "+Menu.formatid_7.format(rs.getInt("Id_Emp"));
		            datos[1]=" "+rs.getString("NombresEmp");
		            datos[2]=" "+rs.getString("DniEmp");
		            datos[3]=" "+rs.getString("TelefonoEmp");
		            datos[4]=" "+rs.getString("CargoEmp");
		            datos[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("SueldoEmp"));
		            
		            totalitems=totalitems+1;
		            model.addRow(datos);
		            tableList.setModel(model);
		            
		            TOT= TOT + rs.getInt("SueldoEmp");
			        }
		           rs.close();
		           st.close();
		           
		           // LLENO EL ESPACIO
		    	   if (totalitems>0) {
			           String []d= new String[6];
			 		   tableList.setModel(model);
			   
			            d[3]="================ ";
			            d[4]=" ========";
			            d[5]="======";
			            model.addRow(d);
			            tableList.setModel(model);
			            
			           // LLENO RESULTADOS
			           String []dato= new String[6];
			 		   tableList.setModel(model);
			   
			            dato[3]=" TOTAL SUELDOS ===> ";
			            dato[5]=" " + 	Menu.formateadorCurrency.format(TOT);
			            model.addRow(dato);
			            tableList.setModel(model);
		 		   }
		    	   
		    	   // MODELO TABLE
		     	   int CONT=11;
		     	   if (totalitems>0) {
		     		   int l=0;
		 	            l=CONT-totalitems;
		 	            if ( CONT>totalitems) {
		 				    String []registros= new String[l];
		 				    for (int n=2; n<l;n++) {
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
	           
	           DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
	           modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);
	           
	           tableList.getColumnModel().getColumn(3).setCellRenderer(modeloRight);
	           tableList.getColumnModel().getColumn(5).setCellRenderer(modeloRight);
		           
	     	   tableList.getColumnModel().getColumn(0).setPreferredWidth(50);
	     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(250);//NOMBRE
	     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(50); //DNI
	     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(130);//TELEFONO
	     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(70); //CARGO
	     	   tableList.getColumnModel().getColumn(5).setPreferredWidth(50); //SUELDO
	     	   
	           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
			} catch (SQLException e) {
				//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
		}
		public void crearLabels() {
			lbl1= new JLabel("Estado:");
			lbl1.setBounds(10, 7, 46, 14);
			panelBtn.add(lbl1);
			lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl1.setFont(Menu.fontLabel);
			
			lbl2= new JLabel("Filtrar por:");
			lbl2.setBounds(144, 7, 78, 14);
			panelBtn.add(lbl2);
			lbl2.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl2.setFont(Menu.fontLabel);
		}
		public void crearTextFields(){
			textBus = new  JTextFieldIcon(new JTextField(),"Search16.png","Search","Search16.png");
			textBus.setColumns(10);
			textBus.setFont(Menu.fontText);
			textBus.setForeground(Menu.textColorForegroundInactivo);
			textBus.setHorizontalAlignment(SwingConstants.LEFT);
			textBus.addActionListener(this);
			textBus.addFocusListener(this);
			textBus.addKeyListener(this);
			textBus.addPropertyChangeListener(this);
			textBus.setBounds(230, 21, 257, 22);
			panelBtn.add(textBus);
			textBus.addFocusListener(this);
		}
		public void crearComboBox() {
			cbBus = new JComboBox<>();
	        cbBus.setBounds(62, 21, 162, 21);
	        cbBus.setFont(Menu.fontText);
	        cbBus.removeAllItems();
	        cbBus.addFocusListener(this);
	        cbBus.addKeyListener(this);
	        panelBtn.add(cbBus);
	        
			cbEstado= new JComboBox<>();
			cbEstado.setBounds(10, 21, 46, 21);
			cbEstado.setFont(Menu.fontText);
			cbEstado.removeAllItems();
			cbEstado.addActionListener(this);
			cbEstado.addFocusListener(this);
			cbEstado.addKeyListener(this);
	        panelBtn.add(cbEstado);
		}
		public void llenarcbBuscar() {
	        cbBus.removeAllItems();
			String [] lista1 = {"CODIGO","NOMBRE","DNI","CARGO"};
			for (String llenar:lista1) {
				cbBus.addItem(llenar);
			}
			cbBus.setSelectedIndex(2);
		}
		public void llenarcbEstado() {
			cbEstado.removeAllItems();
			String [] lista1 = {"A","X"};
			for (String llenar:lista1) {
				cbEstado.addItem(llenar);
			}
			cbEstado.setSelectedIndex(0);
		}
		public void crearButtons() {
			buttonNuevo= new JButton("");
			buttonNuevo.setToolTipText("Agregar ítem");
			buttonNuevo.addActionListener(this);
			buttonNuevo.setBounds(534, 20, 36, 23);
			buttonNuevo.setIcon(new ImageIcon(VentanaEmpleado.class.getResource("/modelo/Images/nuevo.png")));
			panelBtn.add(buttonNuevo);

			buttonEditar= new JButton("");
			buttonEditar.setToolTipText("Modificar ítem");
			buttonEditar.addActionListener(this);
			buttonEditar.setBounds(572, 20, 36, 23);
			buttonEditar.setIcon(new ImageIcon(VentanaEmpleado.class.getResource("/modelo/Images/edit.png")));
			panelBtn.add(buttonEditar);
			
			buttonEliminar= new JButton("");
			buttonEliminar.setToolTipText("Eliminar ítem");
			buttonEliminar.addActionListener(this);
			buttonEliminar.setBounds(610, 20, 36, 23);
			buttonEliminar.setIcon(new ImageIcon(VentanaEmpleado.class.getResource("/modelo/Images/delete.png")));
			panelBtn.add(buttonEliminar);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Salir");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(649, 20, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaEmpleado.class.getResource("/modelo/Images/Exit.png")));
			panelBtn.add(buttonSalir);
			
			buttonBus= new JButton("");
			buttonBus.setBounds(494, 20, 36, 23);
			buttonBus.setToolTipText("Aceptar");
			buttonBus.addActionListener(this);
			buttonBus.setIcon(new ImageIcon(VentanaEmpleado.class.getResource("/modelo/Images/ok.png")));
			panelBtn.add(buttonBus);
			
			
			buttonUsu= new JButton("");
			buttonUsu.setToolTipText("Cuenta usuario");
			buttonUsu.addActionListener(this);
			buttonUsu.setBounds(610, 20, 36, 23);
			buttonUsu.setIcon(new ImageIcon(VentanaEmpleado.class.getResource("/modelo/Images/menu-control.png")));
			panelBtn.add(buttonUsu);
			buttonUsu.setVisible(false);
		}
		public void crearTable(){
			tableList = new JTable(); 
			tableList.setShowHorizontalLines(false);
			tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			tableList.setBounds(10, 303, 665, 229);
			tableList.addMouseListener(this);
			tableList.addKeyListener(this);
			
			scrollTable = new JScrollPane();
			scrollTable.setViewportView(tableList);
			scrollTable.setBounds(10, 25, 675, 202);
	        panelLst.add(scrollTable);
	        
	    	//tableList.setShowHorizontalLines(false);
	    	//tableList.setShowVerticalLines(true);
	    	//tableList.setFillsViewportHeight(true);
	    	tableList.setGridColor(new Color(238, 232, 170));
		}
		
		public void limpiarTexts() {
			textBus.setText(null);
			textBus.setBackground(Menu.textColorBackgroundInactivo);	
			textBus.setForeground(Menu.textColorForegroundInactivo);
			
			cbBus.setSelectedIndex(1);
			llenarTable();
		}
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonNuevo){// NUEVO
				  	VentanaEmpleadoAgregar.MOD=0;// PERMITE REGISTRAR 
				  	VentanaEmpleadoAgregar ven= new VentanaEmpleadoAgregar();
				    ven.frame.setVisible(true);
				    ven.textNom.requestFocus(true);

				  }
			  if (evento.getSource() == buttonEditar){// EDITAR
				  if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				  }
				  if (id.length() > 0){
					  modificarArticulo();
				  	}
				  }	
			  if (evento.getSource() == buttonEliminar){// ELIMINAR
				  delete();
				  }	
			  if (evento.getSource() == buttonSalir){// SALIR
				  frame.dispose();
				  }
			  if (evento.getSource() == buttonBus){	// BUSCAR
				  
				  }
			  if (evento.getSource() == buttonUsu){	// USUARIO
				  if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				  }
				  VentanaUsuario v = new  VentanaUsuario();
				  v.frame.show();
				  v.textCod.setText(id);
				  v.textNom.setText(descripcion);
				  }
			  if (evento.getSource() == cbEstado){
				  	limpiarTexts();
				  }
			}
			void modificarArticulo() {
				VentanaEmpleadoAgregar.MOD=1;// PERMITE MODIFICAR
				VentanaEmpleadoAgregar ven= new VentanaEmpleadoAgregar();
				ven.frame.toFront();
				ven.frame.setVisible(true);
				ven.textCod.setText(id);
				ven.textNom.requestFocus(true);
				ven.textNom.selectAll();
			}
			//	METODO DAR BAJA EMPLEADO
			public void delete() {
				if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				}
				
				conexion = new ConexionDB();
				try {
					Statement st = conexion.gConnection().createStatement();
					ResultSet rs = st.executeQuery("Select* from EMPLEADO as E, USUARIO as U where E.Id_Emp=U.Id_Emp and E.Id_Emp='" + id + "'");
					if (rs.next()==false) {
							// ELIMINARA EMPLEADO
							int resp = JOptionPane.showConfirmDialog (null, "¿Desea eliminar al empleado?" + Menu.separador + id + " " +  descripcion, Menu.SOFTLE_HOTEL,
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if (resp == JOptionPane.YES_OPTION) {
								try {
									Statement statement =  conexion.gConnection().createStatement();
									String query ="Delete from EMPLEADO where Id_Emp='" + id  + "'";
									statement.execute(query);
									JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
									statement.close();
									llenarTable();
								} catch (Exception e) {}
							}else if (resp == JOptionPane.NO_OPTION) {}
					}else{
							// DAR DE BAJA EMPLEADO
							int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea dar de baja al empleado?" + Menu.separador + id + " " +  descripcion, Menu.SOFTLE_HOTEL,
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (respuesta == JOptionPane.YES_OPTION) {
								try {
									String sql ="UPDATE EMPLEADO SET Id_Emp= ?,"
											+ "ActividadEmp=?,"
											+ "FechaBajaEmp=?"
											+ "WHERE Id_Emp = '"+id+"'"; 
									PreparedStatement ps = conexion.gConnection().prepareStatement(sql);
									ps.setInt(1, Integer.parseInt(id));
									ps.setString(2, "X");
									ps.setString(3, (Menu.date +" "+ Menu.HORA_24.substring(0,8)).trim());
									ps.executeUpdate();
									ps.close();
									JOptionPane.showMessageDialog(null, "El ítem fue dado de baja de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
									llenarTable();
								} catch (Exception e) {}
							}else if (respuesta == JOptionPane.NO_OPTION) {}
						}
						rs.close();
						st.close();
					} catch (Exception e) {}
					conexion.DesconectarDB();
			}
			public void focusGained(FocusEvent ev) {
				if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundActivo);}
				if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundActivo);}
			}
			public void focusLost(FocusEvent ev) {
				if (ev.getSource() == textBus){textBus.setBackground(Menu.textColorBackgroundInactivo);}
				if (ev.getSource() == textBus){textBus.setForeground(Menu.textColorForegroundInactivo);}
			}
			
			public void keyReleased(KeyEvent evet) {
				char e=evet.getKeyChar();
					if (evet.getSource() == textBus){
						llenarTable();
						int pos = textBus.getCaretPosition();textBus.setText(textBus.getText().toUpperCase());textBus.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
						if (e==KeyEvent.VK_ENTER || textBus.getText().toLowerCase().length()==30){
							}
						}
				  if (evet.getSource() == cbEstado){
					  	limpiarTexts();
					  }
			}

			public void keyTyped(KeyEvent evet) {
				// PRECIONA EL TECLADO Y ME DA EL EVENTO
				//char e=evet.getKeyChar();
			}
			
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			//Object source = e.getSource();
		}
		
		@Override
		public void keyPressed(KeyEvent evet) {
			// TODO Auto-generated method stub
			char e=evet.getKeyChar();
			if (evet.getSource().equals(tableList)) {
				if (e==KeyEvent.VK_ENTER ){
					try {
						
					} catch (Exception e2) {}
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
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseReleased(MouseEvent Mouse_evt) {
			// TODO Auto-generated method stub
			id="";descripcion="";
			try {
				if (Mouse_evt.getSource().equals(tableList)) {
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
					if (id==null || id == ""){
						tableList.requestFocus();
						return;
					}
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
					descripcion=(String) tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {
						if (buttonUsu.isVisible()==true){
							buttonUsu.doClick();	
						}else{
							modificarArticulo();
						}
					}
				}
			} catch (Exception e) {}
		}

}
