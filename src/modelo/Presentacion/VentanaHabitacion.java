
package modelo.Presentacion;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import modelo.Clases.TableCellRendererColor;
import modelo.Datos.ConexionDB;
import modelo.Otros.JTextFieldIcon;

public class VentanaHabitacion implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public static JInternalFrame frame;
		private JPanel  panelBtn,panelLst;
		public  JLabel		lbl9;
		public JTextField 			textBus;
		private JButton  			buttonNuevo,buttonEditar,buttonEliminar,buttonSalir,buttonBus;
		JComboBox<String> cbBus;
	    
		private JScrollPane scrollTable;
		private JTextArea textArea = new JTextArea();
		private JTable tableList;
		private DefaultTableModel model;
		
		public int totalitems;
		public static int CONTAR_VENTANA_HABITACION=0;
		
		public static String id="",detalle="";
		
		private String consultar="";
		
		JMenuItem m1,m2,m3,m4,m5;
		// constructor
		public VentanaHabitacion() {
			super();
			
			frameHabitacion();
			crearPanel();
			crearButtons();
			crearTable();
			crearComboBox();
			crearTextFields();
			crearLabels();
			crearPopupMenu();
			panelLst.setVisible(true); // PANEL LISTA
			llenarcbBuscar();
			
			//llenarTable();
			llenarTable();
			
			CONTAR_VENTANA_HABITACION ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
			
			Menu.Desktop.add(frame);
	        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
	        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
	    	frame.setLocation(x, y);
		}
		public void frameHabitacion() {
			frame = new JInternalFrame();
			frame.addInternalFrameListener(new InternalFrameAdapter() {
				@Override
				public void internalFrameActivated(InternalFrameEvent e) {
					llenarTable();
				}
				//@Override
				public void internalFrameClosed(InternalFrameEvent arg0) {
					CONTAR_VENTANA_HABITACION=0;
				}
			});
			frame.setFrameIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/Hab.png")));
			frame.setTitle("Gestión de Habitaciones");
			frame.setClosable(true);
			frame.setBounds(100, 100, 729, 498);
			frame.getContentPane().setLayout(null);
		}
		public void crearPanel() {
			panelBtn = new JPanel();
			panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "|  Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelBtn.setBounds(10, 414, 695, 49);
			frame.getContentPane().add(panelBtn);
			panelBtn.setLayout(null);
			
			panelLst = new JPanel();
			panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLst.setBounds(10, 11, 695, 404); //10, 333, 659, 268
			frame.getContentPane().add(panelLst);
			panelLst.setLayout(null);
			
		}
		void LimpiarTable(){
			try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
		}
		public void llenarTable () {
			id="";detalle="";
			conexion = new ConexionDB();
			//LimpiarTable();
	        try {
	     	   totalitems=0;
	     	   if (textBus.getText().isEmpty()) {
	     		  consultar="Select * from HABITACION";
	     	   }else{
					if(cbBus.getSelectedItem()=="NRO #") {
						consultar="Select * from HABITACION where NumeroHab ='"+textBus.getText()+"'";
					}
					if(cbBus.getSelectedItem()=="TIPO HABITACION") {
						consultar="Select * from HABITACION where Tipo_Hab like'"+textBus.getText()+"%'";
					}
					if(cbBus.getSelectedItem()=="ESTADO") {
						consultar="Select * from HABITACION where EstadoHab like'"+textBus.getText()+"%'";
					}
					if(cbBus.getSelectedItem()=="PISO") {
						consultar="Select * from HABITACION where PisoHab ='"+textBus.getText()+"'";
					}
					if(cbBus.getSelectedItem()=="DETALLE") {
						consultar="Select * from HABITACION where DescripcionHab like '"+textBus.getText()+"%'";
					}
					if(cbBus.getSelectedItem()=="PRECIO") {
						 consultar="Select * from HABITACION,TARIFAS where TARIFAS.NumeroHab= HABITACION.NumeroHab and PrecioTar ='"+textBus.getText()+"'";
					}
	     	   }
	     	   
	 		   
			   model= new DefaultTableModel();
			   model.addColumn("Nro #");
		       model.addColumn("Tipo Habitación");
			   model.addColumn("Detalle");
			   model.addColumn("Estado");
			   model.addColumn("Piso");
		       model.addColumn("Fecha");
		       
		       Object []datos= new Object[6];
		       tableList.setModel(model);
		       
	     	   // Instanciamos el TableRowSorter y lo añadimos al JTable
     		   TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(model);
     		   tableList.setRowSorter(elQueOrdena);
	     	   // end sorterd
     		   
	     	   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
	           while(rs.next()) {
		            datos[0]=" "+rs.getInt("NumeroHab");
		            datos[1]=" "+rs.getString("Tipo_Hab");
		            if(rs.getString("caracteristicasHab").trim().equals("")) {
		            	datos[2]=" "+rs.getString("DescripcionHab").trim();
		            }else{
		            	datos[2]=" "+rs.getString("caracteristicasHab").trim() +" :  "+rs.getString("DescripcionHab").trim();
		            }	
		            datos[3]=" "+rs.getString("EstadoHab");
		            datos[4]="    "+rs.getString("PisoHab");
		            datos[5]=" "+rs.getString("FechaAltaHab");
		            totalitems=totalitems+1;
		            model.addRow(datos);
		            tableList.setModel(model);
		        }
	           	rs.close();
	           	st.close();
	           // MODELO TABLE
	    	   int CONT=22;
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
	    	   
	           tableList.getColumnModel().getColumn(5).setMinWidth(0);
	           tableList.getColumnModel().getColumn(5).setMaxWidth(0);
	    	   
	     	   tableList.getColumnModel().getColumn(0).setPreferredWidth(10);
	     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(120);
	     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(320);
	     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(70);
	     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(04);
	     	   //tableList.getColumnModel().getColumn(5).setPreferredWidth(50);

	     	   tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("HABITACION"));//ESTABLESCO COLOR CELDAS
	     	   //tableList.changeSelection(0,0,false,true);//SELECCIONA EN LA PRIMERA FILA
	     	   tableList.getSelectionModel().setSelectionInterval (0, 0); //SELECCIONA EN LA PRIMERA FILA Y INTERVALOS O GRUPOS

	           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
		}
		public void crearTable(){
			tableList = new JTable(); 
			tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			tableList.setBounds(10, 303, 665, 229);
			tableList.addMouseListener(this);
			
			scrollTable = new JScrollPane();
			scrollTable.setViewportView(tableList);
			scrollTable.setBounds(10, 17, 675, 376);
	        panelLst.add(scrollTable);
	        
	    	//tableList.setFillsViewportHeight(true);
			//tableList.setShowHorizontalLines(false);
	        //tableList.setGridColor(new Color(238, 232, 170));
	        tableList.setGridColor(new Color(238, 232, 170));
	        
		    JTableHeader anHeader = tableList.getTableHeader();
		    anHeader.setForeground(new Color(204, 102, 102));
		    anHeader.setBackground(Color.white);
		}
		public void crearPopupMenu(){
			JPopupMenu popupMenu = new JPopupMenu();
			addPopup(tableList, popupMenu);
			
			m1 = new JMenuItem("Seleccionar ítem...");
			m1.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/check.png")));
			//m1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));// TECLAS RAPIDAS
			popupMenu.add(m1);
			m1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

				}
			});
			
			JSeparator separator = new JSeparator();
			popupMenu.add(separator);
			
			m4 = new JMenuItem("Nuevo ítem");
			m4.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/nuevo.png")));
			//m2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
			popupMenu.add(m4);
			m4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					buttonNuevo.doClick();
				}
			});
			
			 m2 = new JMenuItem("Modificar el ítem");
			 m2.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/edit.png")));
			//mModi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
			popupMenu.add(m2);
			 m2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					buttonEditar.doClick();
				}
			});
			

			m3 = new JMenuItem("Eliminar el ítem");
			m3.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/delete.png")));
			//mEli.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
			popupMenu.add(m3);
			m3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					buttonEliminar.doClick();
				}
			});
			JSeparator separator1 = new JSeparator();
			popupMenu.add(separator1);
			
			m5 = new JMenuItem("Vista previa de tarifas");
			m5.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/Dollar_Green.png")));
			//m3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
			popupMenu.add(m5);
			m5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					VistaTarifa v = new VistaTarifa();
					v.llenarTable(id,"");
					VistaTarifa.textNom.setText(id);
					VistaTarifa.textNom.selectAll();
					v.frame.setVisible(true);

				}
			});
			m1.setEnabled(false);
			m2.setEnabled(false);
			m3.setEnabled(false);
			m4.setEnabled(false);
			m5.setEnabled(false);
		}
		public void crearLabels() {
			
			lbl9= new JLabel("Filtrar por:");
			lbl9.setBounds(53, 8, 120, 14);
			panelBtn.add(lbl9);
			lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl9.setFont(Menu.fontLabel);
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
			textBus.setBounds(178, 21, 300, 22);
			panelBtn.add(textBus);
			textBus.addFocusListener(this);
		}
		public void crearComboBox() {
			cbBus = new JComboBox<>();
	        cbBus.setBounds(10, 21, 164, 21);
	        cbBus.setFont(Menu.fontText);
	        cbBus.addActionListener(this);
	        cbBus.addFocusListener(this);
	        cbBus.addKeyListener(this);
	        panelBtn.add(cbBus);
		}
		public void llenarcbBuscar() {
	        cbBus.removeAllItems();
			String [] lista1 = {"NRO #","TIPO HABITACION","ESTADO","PISO","PRECIO","DETALLE"};
			for (String llenar:lista1) {
				cbBus.addItem(llenar);
			}
			cbBus.setSelectedIndex(0);
		}
		public void crearButtons() {
			buttonNuevo= new JButton("");
			buttonNuevo.setToolTipText("Agregar ítem");
			buttonNuevo.addActionListener(this);
			buttonNuevo.setBounds(535, 20, 36, 23);
			buttonNuevo.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/nuevo.png")));
			panelBtn.add(buttonNuevo);

			buttonEditar= new JButton("");
			buttonEditar.setToolTipText("Modificar ítem");
			buttonEditar.addActionListener(this);
			buttonEditar.setBounds(573, 20, 36, 23);
			buttonEditar.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/edit.png")));
			panelBtn.add(buttonEditar);
			
			buttonEliminar= new JButton("");
			buttonEliminar.setToolTipText("Eliminar ítem");
			buttonEliminar.addActionListener(this);
			buttonEliminar.setBounds(611, 20, 36, 23);
			buttonEliminar.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/delete.png")));
			panelBtn.add(buttonEliminar);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Salir");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(649, 20, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/Exit.png")));
			panelBtn.add(buttonSalir);
			
			buttonBus= new JButton("");
			buttonBus.setBounds(479, 20, 36, 23);
			buttonBus.setToolTipText("Buscar");
			buttonBus.addActionListener(this);
			buttonBus.setIcon(new ImageIcon(VentanaHabitacion.class.getResource("/modelo/Images/ok.png")));
			panelBtn.add(buttonBus);
		}
		public void limpiarTexts() {
			textArea.setText(null);
			textArea.setBackground(Menu.textColorBackgroundInactivo);	
			textArea.setForeground(Menu.textColorForegroundInactivo);
			
			textBus.setText(null);
			textBus.setBackground(Menu.textColorBackgroundInactivo);	
			textBus.setForeground(Menu.textColorForegroundInactivo);
			
			cbBus.removeAllItems();
		}
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonNuevo){// NUEVO
				  	nuevaHabitacion();
				  }
			  if (evento.getSource() == buttonEditar){// EDITAR
				  if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				  }
				  if (id.length() > 0){
					  modificarHabitacion();
				  	}
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
				  textBus.requestFocus();textBus.selectAll();
				  }
			 
			}
			// 	METODO NUEVO
			void nuevaHabitacion() {
				//VentanaHabitacionAgregar.MOD=1;// PERMITE MODIFICAR
				VentanaHabitacionAgregar ven= new VentanaHabitacionAgregar();
				ven.frame.toFront();
				ven.frame.setVisible(true);
				ven.llenarcbComboBox();
				ven.activarButton(false);
				ven.textFieldBuscar.setEnabled(false);
				ven.textField1.requestFocus(true);
				ven.chckbxDesc.setEnabled(false);
			}
			// 	METODO MODIFICAR
			void modificarHabitacion() {
				//VentanaHabitacionAgregar.MOD=1;// PERMITE MODIFICAR
				VentanaHabitacionAgregar ven= new VentanaHabitacionAgregar();
				ven.frame.toFront();
				ven.frame.setVisible(true);
				ven.textFieldBuscar.setText(id);
				ven.buscar();
				ven.activarButton(false);
				ven.textField1.setEnabled(false);
				VentanaHabitacionAgregar.cbTipoHabitacion.requestFocus(true);
				ven.panelDatos.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modificar Datos ", TitledBorder.LEADING, TitledBorder.TOP, null,(SystemColor.RED)));
			  	
			}
			//	METODO ELIMINAR
			public void delete() {
				if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				}
			try{	
				
				Statement stt = conexion.gConnection().createStatement();
				ResultSet rss = stt.executeQuery("Select* from INVENTARIO_HABITACION  where NumeroHab='" + id + "'");
				if (rss.next()==true) {
					JOptionPane.showMessageDialog(null, "No se permite eliminar la habitación \n" + detalle + " porque se ha inventariado...!",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
					return;
					}
				
				Statement st = conexion.gConnection().createStatement();
				ResultSet rs = st.executeQuery("Select* from HABITACION as H, DETALLE_A_HABITACION as DH where DH.NumeroH=H.NumeroHab and H.NumeroHab='" + id + "'");
				if (rs.next()==false) {
						// ELIMINARA EMPLEADO
						int resp = JOptionPane.showConfirmDialog (null, "¿Desea eliminar la habitación? " + Menu.separador +  detalle, Menu.SOFTLE_HOTEL,
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (resp == JOptionPane.YES_OPTION) {
							try {
								conexion = new ConexionDB();
								Statement statement =  conexion.gConnection().createStatement();
								String query ="Delete from HABITACION where NumeroHab='" + id  + "'";
								statement.execute(query);
								JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
								statement.close();
								llenarTable();
							} catch (Exception e) {}
						}else if (resp == JOptionPane.NO_OPTION) {}
						return;
				}else{
					JOptionPane.showMessageDialog(null, "No se permite eliminar la habitación \n" + detalle + " porque se ha efectuado movimientos...!",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {}
				
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
								//buscar();

						}
						
		
					}
			}

			public void keyTyped(KeyEvent evet) {
				// PRECIONA EL TECLADO Y ME DA EL EVENTO

			}
			
		@Override
		public void propertyChange(PropertyChangeEvent e) {
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
			id="";detalle="";
			if (Mouse_evt.getSource().equals(tableList)) {
				try {
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
					
					m1.setEnabled(true);
					m2.setEnabled(true);
					m3.setEnabled(true);
					m4.setEnabled(true);
					m5.setEnabled(true);
					if (id==null || id.trim() == ""){
						m1.setEnabled(false);
						m2.setEnabled(false);
						m3.setEnabled(false);
						m4.setEnabled(false);
						m5.setEnabled(false);
						
						tableList.requestFocus();
						return;
					}
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
					detalle=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim() +" "+ (String) tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {
						modificarHabitacion();
					}
					
				} catch (Exception e) {}
			}
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
