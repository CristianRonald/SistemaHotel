
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

public class VentanaPlan implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public static JInternalFrame frame;
		private JPanel  panelBtn,panelLst;
		public  JLabel		lbl9;
		public JTextField 			textBus;
		private JButton  			buttonNuevo,buttonEditar,buttonEliminar,buttonSalir,buttonBus;
	    
		private JScrollPane scrollTable;
		private JTextArea textArea = new JTextArea();
		private JTable tableList;
		private DefaultTableModel model;
		
		public int totalitems;
		
		public static String id="",Inicial,detalle="";
		
		private String consultar="";
		
		JMenuItem m1,m2,m3,m4;
		// constructor
		public VentanaPlan() {
			super();
			
			framePlan();
			crearPanel();
			crearButtons();
			crearTable();
			crearComboBox();
			crearTextFields();
			crearLabels();
			crearPopupMenu();
			panelLst.setVisible(true); // PANEL LISTA
			llenarTable();
			Menu.Desktop.add(frame);
	        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
	        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
	    	frame.setLocation(x, y);
		}
		public void framePlan() {
			frame = new JInternalFrame();
			frame.addInternalFrameListener(new InternalFrameAdapter() {
				@Override
				public void internalFrameActivated(InternalFrameEvent e) {
					llenarTable();
				}
				//@Override
				public void internalFrameClosed(InternalFrameEvent arg0) {
				}
			});
			frame.setFrameIcon(new ImageIcon(VentanaPlan.class.getResource("/modelo/Images/Add.png")));
			frame.setTitle("Gestión de planes para Alojamiento");
			frame.setClosable(true);
			frame.setBounds(100, 100, 729, 383);
			frame.getContentPane().setLayout(null);
		}
		public void crearPanel() {
			panelBtn = new JPanel();
			panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "|  Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelBtn.setBounds(10, 287, 695, 49);
			frame.getContentPane().add(panelBtn);
			panelBtn.setLayout(null);
			
			panelLst = new JPanel();
			panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLst.setBounds(10, 11, 695, 276); //10, 333, 659, 268
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
	     		   	consultar="Select * from PLANES";
	     	   }else{
					consultar="Select * from PLANES where DescripcionPla ='"+textBus.getText()+"'";

	     	   }
	     	   
	 		   
			   model= new DefaultTableModel();
			   model.addColumn("Código");
		       model.addColumn("Iniciales");
			   model.addColumn("Descripción");
			   model.addColumn("Costo");
			   model.addColumn("Detalle");
		       
		       Object []datos= new Object[5];
		       tableList.setModel(model);
		       
	     	   // Instanciamos el TableRowSorter y lo añadimos al JTable
     		   TableRowSorter<TableModel> elQueOrdena = new TableRowSorter<TableModel>(model);
     		   tableList.setRowSorter(elQueOrdena);
	     	   // end sorterd
     		   
     		   
	     	   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
	           while(rs.next()) {
		            datos[0]=" "+rs.getInt("Id_Pla");
		            datos[1]=" "+rs.getString("InicialesPla");
		            datos[2]=" "+rs.getString("DescripcionPla").trim();
		            datos[3]=Menu.formateadorCurrency.format(rs.getDouble("CostoPla"))+" ";
		            datos[4]=" "+rs.getString("ObservacionPla");
		            totalitems=totalitems+1;
		            model.addRow(datos);
		            tableList.setModel(model);
		        }
	           	rs.close();
	           	st.close();
	           // MODELO TABLE
	    	   int CONT=15;
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
	    	   
	    	   
	     	   tableList.getColumnModel().getColumn(0).setPreferredWidth(10);
	     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(80);
	     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(220);
	     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(70);
	     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(300);

	     	   tableList.setDefaultRenderer(Object.class, new TableCellRendererColor("PLAN"));//ESTABLESCO COLOR CELDAS
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
			scrollTable.setBounds(10, 17, 675, 246);
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
			m1.setEnabled(false);
			m2.setEnabled(false);
			m3.setEnabled(false);
			m4.setEnabled(false);
		}
		public void crearLabels() {
			
			lbl9= new JLabel("Filtrar por:");
			lbl9.setBounds(10, 26, 73, 14);
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
			textBus.setBounds(93, 21, 385, 22);
			panelBtn.add(textBus);
			textBus.addFocusListener(this);
		}
		public void crearComboBox() {
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
			
		}
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonNuevo){// NUEVO
				  VentanaPlanAgregar.MOD=0;// PERMITE REGISTRAR 
				  VentanaPlanAgregar ven= new VentanaPlanAgregar();
				    ven.frame.toFront();
				    ven.frame.setVisible(true);
				    ven.textInicial.requestFocus();
				    ven.panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos " , TitledBorder.LEADING, TitledBorder.TOP, null,( new Color(106, 90, 205))));
				  }
			  if (evento.getSource() == buttonEditar){// EDITAR
				  if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				  }
				  if (id.length() > 0){
					  modificarPlan();
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
				  textBus.requestFocus();textBus.selectAll();
				  }
			 
			}
			// 	METODO MODIFICAR
			void modificarPlan() {
				VentanaPlanAgregar.MOD=1;// PERMITE MODIFICAR
				VentanaPlanAgregar ven= new VentanaPlanAgregar();
				ven.frame.toFront();
				ven.frame.setVisible(true);
				ven.textCod.setText(id);
				
				ven.textDescripcion.requestFocus(true);
				ven.textDescripcion.selectAll();
    			ven.panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modificar Datos " + id , TitledBorder.LEADING, TitledBorder.TOP, null,(SystemColor.RED)));
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
				ResultSet rss = stt.executeQuery("Select* from DETALLE_A_HABITACION  where PlanH='" + Inicial.trim() + "'");
				if (rss.next()==true) {
					JOptionPane.showMessageDialog(null, "No se permite eliminar el plan \n" + Inicial.trim()+" "+detalle.trim() + " porque existen movimientos de este plan...!",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Statement st = conexion.gConnection().createStatement();
				ResultSet rs = st.executeQuery("Select* from PLANES as P, DETALLE_A_HABITACION as DH where DH.PlanH=P.Id_Pla and P.InicialesPla='" + Inicial.trim()+ "'");
				if (rs.next()==false) {
						// ELIMINARA EMPLEADO
						int resp = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el ítem? " + Menu.separador +  Inicial.trim()+" "+detalle.trim(), Menu.SOFTLE_HOTEL,
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (resp == JOptionPane.YES_OPTION) {
							try {
								conexion = new ConexionDB();
								Statement statement =  conexion.gConnection().createStatement();
								String query ="Delete from PLANES where Id_Pla='" + id  + "'";
								statement.execute(query);
								JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
								statement.close();
								llenarTable();
							} catch (Exception e) {}
						}else if (resp == JOptionPane.NO_OPTION) {}
						return;
				}else{
					JOptionPane.showMessageDialog(null, "No se permite eliminar el plan \n" + Inicial.trim()+" "+detalle.trim() + " porque se ha efectuado movimientos...!",Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
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
			id="";Inicial="";detalle="";
			if (Mouse_evt.getSource().equals(tableList)) {
				try {
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
					
					m1.setEnabled(true);
					m2.setEnabled(true);
					m3.setEnabled(true);
					m4.setEnabled(true);
					if (id==null || id.trim() == ""){
						m1.setEnabled(false);
						m2.setEnabled(false);
						m3.setEnabled(false);
						m4.setEnabled(false);
						
						tableList.requestFocus();
						return;
					}
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
					Inicial=(String) tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
					detalle=(String) tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {
						modificarPlan();
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
