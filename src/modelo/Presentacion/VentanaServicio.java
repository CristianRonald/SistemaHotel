package modelo.Presentacion;

import java.awt.Color;
import java.awt.SystemColor;
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

public class VentanaServicio implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public JInternalFrame frame;
		private JPanel  panelBtn,panelLst;
		public  JLabel		lbl9;
		public JTextField 			textBus;
		protected JButton  			buttonNuevo,buttonEditar,buttonEliminar,buttonSalir,buttonBus;
		JComboBox<String> cbBus;
	    
		private JScrollPane scrollTable;
		private JTable tableList;
		private DefaultTableModel model;
		
		public Integer totalitems;
		public static int CONTAR_VENTANA_SERVICIOS=0;
		public static int FILTRO_SER;
		
		public static String id="",detalle="";
		
		private float  precio;
		private String  descripcion,observacion;
		private String consultar="";
		
		// constructor
		public VentanaServicio() {
			super();
			
			frameServicios();
			crearPanel();
			crearButtons();
			crearTable();
			crearComboBox();
			crearTextFields();
			crearLabels();
			panelLst.setVisible(true); // PANEL LISTA
			llenarcbBuscar();
			
			//llenarTable();
			llenarServicios();
			
			CONTAR_VENTANA_SERVICIOS ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
			
			Menu.Desktop.add(frame);
	        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
	        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
	    	frame.setLocation(x, y);
		}
		public void frameServicios() {
			frame = new JInternalFrame();
			frame.addInternalFrameListener(new InternalFrameAdapter() {
				@Override
				public void internalFrameActivated(InternalFrameEvent e) {
					llenarServicios();
				}
				//@Override
				public void internalFrameClosed(InternalFrameEvent arg0) {
					CONTAR_VENTANA_SERVICIOS=0;
				}
			});
			frame.setFrameIcon(new ImageIcon(VentanaServicio.class.getResource("/modelo/Images/servicio.png")));
			frame.setTitle("Servicios");
			frame.setClosable(true);
			frame.setBounds(100, 100, 722, 342);
			frame.getContentPane().setLayout(null);
		}
		public void crearPanel() {
			panelBtn = new JPanel();
			panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelBtn.setBounds(10, 247, 695, 49);
			frame.getContentPane().add(panelBtn);
			panelBtn.setLayout(null);
			
			panelLst = new JPanel();
			panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLst.setBounds(10, 11, 695, 236); //10, 333, 659, 268
			frame.getContentPane().add(panelLst);
			panelLst.setLayout(null);
			
		}
		
		void LimpiarTable(){
			try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
		}
		public void llenarServicios () {
			id="";detalle="";
			conexion = new ConexionDB();
			//LimpiarTable();
	        try {
	            // COLOREA TODO EL JTABLE
	          // tableList.setDefaultRenderer (Object.class, new MiRender());
	     	   totalitems=0;
	     	   if (textBus.getText().isEmpty()) {
	     		  consultar="Select * from SERVICIOS";
	     	   }else{
					if(cbBus.getSelectedItem()=="CODIGO") {
						consultar="Select * from SERVICIOS where Id_Ser ='"+textBus.getText()+"'";
					}
					if(cbBus.getSelectedItem()=="DESCRIPCION") {
						consultar="Select * from SERVICIOS where DescripcionSer like'"+textBus.getText()+"%'";
					}
					if(cbBus.getSelectedItem()=="PRECIO") {
						consultar="Select * from SERVICIOS where PrecioSer like'"+textBus.getText()+"%'";
					}
					if(cbBus.getSelectedItem()=="OBSERVACION") {
						consultar="Select * from SERVICIOS where ObservacionSer like'"+textBus.getText()+"%'";
					}
	     	   }
	     	   
	     	   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
	 		   
			   model= new DefaultTableModel();
			   model.addColumn("Código");
		       model.addColumn("Descripción");
			   model.addColumn("Observación");
			   model.addColumn("Precio S/.");
			   model.addColumn("F. Actualizo");
			       
	 		   String []datos= new String[5];
	 		   tableList.setModel(model);
	           while(rs.next()) {
	            datos[0]=" "+Menu.formatid_7.format(rs.getInt("Id_Ser"));
	            datos[1]=" "+rs.getString("DescripcionSer");
	            datos[2]=" "+rs.getString("ObservacionSer");
	            datos[3]=" "+Menu.formateadorCurrency.format(rs.getFloat("PrecioSer"));
	            datos[4]=" "+Menu.formatoFechaString.format(rs.getDate("FechaActualizoSer"))+" "+rs.getString("FechaActualizoSer").trim().substring(11, 22);
	            totalitems=totalitems+1;
	            model.addRow(datos);
	            tableList.setModel(model);
		        }
	           st.close();
	           
	           // MODELO TABLE
	    	   int CONT=11;
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
	           
	           DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
	           modelocentrar.setHorizontalAlignment(SwingConstants.CENTER);
	           
	           tableList.getColumnModel().getColumn(3).setCellRenderer(modeloRight);
	           tableList.getColumnModel().getColumn(4).setCellRenderer(modeloRight);
		           
	     	   tableList.getColumnModel().getColumn(0).setPreferredWidth(50);
	     	   tableList.getColumnModel().getColumn(1).setPreferredWidth(190);
	     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(260);
	     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(50);
	     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(70);

	           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
			} catch (SQLException e) {
				//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
		}
		public void crearLabels() {
			lbl9= new JLabel("Filtrar por:");
			lbl9.setBounds(53, 8, 137, 14);
			panelBtn.add(lbl9);
			lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl9.setFont(Menu.fontLabel);
		}
		public void crearTextFields(){
			textBus = new JTextField();
			textBus.setColumns(10);
			textBus.setFont(Menu.fontText);
			textBus.setForeground(Menu.textColorForegroundInactivo);
			textBus.setHorizontalAlignment(SwingConstants.LEFT);
			textBus.addActionListener(this);
			textBus.addFocusListener(this);
			textBus.addKeyListener(this);
			textBus.addPropertyChangeListener(this);
			textBus.setBounds(194, 21, 292, 22);
			panelBtn.add(textBus);
			textBus.addFocusListener(this);
		}
		public void crearComboBox() {
			cbBus = new JComboBox<>();
	        cbBus.setBounds(10, 21, 180, 21);
	        cbBus.setFont(Menu.fontText);
	        cbBus.removeAllItems();
	        cbBus.addFocusListener(this);
	        cbBus.addKeyListener(this);
	        panelBtn.add(cbBus);

		}
		public void llenarcbBuscar() {
	        cbBus.removeAllItems();
			String [] lista1 = {"CODIGO","DESCRIPCION","PRECIO","OBSERVACION"};
			for (String llenar:lista1) {
				cbBus.addItem(llenar);
			}
			cbBus.setSelectedIndex(1);
		}
		public void crearButtons() {
			buttonNuevo= new JButton("");
			buttonNuevo.setToolTipText("Agregar ítem");
			buttonNuevo.addActionListener(this);
			buttonNuevo.setBounds(535, 20, 36, 23);
			buttonNuevo.setIcon(new ImageIcon(VentanaServicio.class.getResource("/modelo/Images/nuevo.png")));
			panelBtn.add(buttonNuevo);

			buttonEditar= new JButton("");
			buttonEditar.setToolTipText("Modificar ítem");
			buttonEditar.addActionListener(this);
			buttonEditar.setBounds(573, 20, 36, 23);
			buttonEditar.setIcon(new ImageIcon(VentanaServicio.class.getResource("/modelo/Images/edit.png")));
			panelBtn.add(buttonEditar);
			
			buttonEliminar= new JButton("");
			buttonEliminar.setToolTipText("Eliminar ítem");
			buttonEliminar.addActionListener(this);
			buttonEliminar.setBounds(611, 20, 36, 23);
			buttonEliminar.setIcon(new ImageIcon(VentanaServicio.class.getResource("/modelo/Images/delete.png")));
			panelBtn.add(buttonEliminar);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Salir");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(649, 20, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaServicio.class.getResource("/modelo/Images/Exit.png")));
			panelBtn.add(buttonSalir);
			
			buttonBus= new JButton("");
			buttonBus.setBounds(489, 20, 36, 23);
			buttonBus.setToolTipText("Aceptar");
			buttonBus.addActionListener(this);
			buttonBus.setIcon(new ImageIcon(VentanaServicio.class.getResource("/modelo/Images/ok.png")));
			panelBtn.add(buttonBus);
		}
		public void crearTable(){
			tableList = new JTable(); 
			tableList.setBackground(new Color(245, 255, 250));
			tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			tableList.setBounds(10, 303, 665, 229);
			tableList.addMouseListener(this);
			tableList.addKeyListener(this);
			
			scrollTable = new JScrollPane();
			scrollTable.setViewportView(tableList);
			scrollTable.setBounds(10, 25, 675, 200);
	        panelLst.add(scrollTable);
	        
	    	/*tableList.setShowHorizontalLines(false);
	    	tableList.setShowVerticalLines(true);
	    	tableList.setFillsViewportHeight(true);
	    	tableList.setGridColor(new Color(238, 232, 170));*/
	    	
	    	tableList.setGridColor(new Color(238, 232, 170));
		}
		
		public void limpiarTexts() {
			textBus.setText(null);
			textBus.setBackground(Menu.textColorBackgroundInactivo);	
			textBus.setForeground(Menu.textColorForegroundInactivo);
			
			cbBus.removeAllItems();
		}
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonNuevo){// NUEVO
					VentanaServiciosAgregar.MOD=0;// NO PERMITE MODIFICAR
					VentanaServiciosAgregar ven= new VentanaServiciosAgregar();
				    ven.frame.toFront();
				    ven.frame.setVisible(true);
				    VentanaServiciosAgregar.textDescripcion.requestFocus(true);
				  }
			  if (evento.getSource() == buttonEditar){// EDITAR
				  if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				  }
				  if (id.length() > 0){
					  modificarServicio();
				  	}
				  }	
			  if (evento.getSource() == buttonEliminar){// ELIMINAR
				  delete();
				  }	
			  if (evento.getSource() == buttonSalir){// SALIR
				  frame.dispose();
				  }
			  if (evento.getSource() == buttonBus){	// BUSCAR
					if (FILTRO_SER == 1){
						tableList.selectAll();
						descripcion=(String)tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
						VentanaConsumoAgregar.cbTipo.setSelectedItem(descripcion);
						this.frame.dispose();
						VentanaConsumoAgregar.textFormatCantidad.requestFocus(true);
						VentanaConsumoAgregar.textFormatCantidad.selectAll();
					}
					if (FILTRO_SER==2) {
						tableList.selectAll();
						VentanaVitrinaVenta.textCod.setText(tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim());
						VentanaVitrinaVenta.textDescripcion.setText((String)tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim());
						VentanaVitrinaVenta.textPrecio.setText(tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim());
						frame.dispose();
						VentanaVitrinaVenta.textCantidad.requestFocus(true);
						VentanaVitrinaVenta.textCantidad.selectAll();
					}
				  }
			 
			}
			// 	METODO MODIFICAR
			void modificarServicio() {
				VentanaServiciosAgregar.MOD=1;// PERMITE MODIFICAR
				VentanaServiciosAgregar ven= new VentanaServiciosAgregar();

				ven.frame.toFront();
				ven.frame.setVisible(true);
				VentanaServiciosAgregar.textCod.setText(id);
				VentanaServiciosAgregar.textDescripcion.setText(descripcion);
				VentanaServiciosAgregar.textFormatPrecio.setText(Float.toString(precio));
				VentanaServiciosAgregar.textArea.setText(observacion);
				VentanaServiciosAgregar.textDescripcion.requestFocus(true);
				VentanaServiciosAgregar.textDescripcion.selectAll();
				ven.panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modificar Datos", TitledBorder.LEADING, TitledBorder.TOP, null,(SystemColor.RED)));
			}
			//	METODO ELIMINAR 
			public void delete() {
				if (id==null || id == ""){
					JOptionPane.showMessageDialog(null, "Primero debe seleccionar un ítem de la lista...!",Menu.SOFTLE_HOTEL,JOptionPane.WARNING_MESSAGE);
					tableList.requestFocus();
					return;
				}
				int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el servicio?" + Menu.separador + detalle, Menu.SOFTLE_HOTEL,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (respuesta == JOptionPane.YES_OPTION) {
					try {
						conexion = new ConexionDB();
						Statement statement =  conexion.gConnection().createStatement();
						String query ="Delete from SERVICIOS where Id_Ser ='" + id  + "'";
						statement.execute(query);
						JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						statement.close();
						llenarServicios();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}else if (respuesta == JOptionPane.NO_OPTION) {}
			}
			public void filtrar(){
				if (FILTRO_SER == 1){
					tableList.selectAll();
					descripcion=(String)tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
					VentanaConsumoAgregar.cbTipo.setSelectedItem(descripcion);
					this.frame.dispose();
					VentanaConsumoAgregar.textFormatCantidad.requestFocus(true);
					VentanaConsumoAgregar.textFormatCantidad.selectAll();
				}
				if (FILTRO_SER==2) {
					tableList.selectAll();
					VentanaVitrinaVenta.textCod.setText(tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim());
					VentanaVitrinaVenta.textDescripcion.setText((String)tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim());
					VentanaVitrinaVenta.textPrecio.setText(tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim());
					frame.dispose();
					VentanaVitrinaVenta.textCantidad.requestFocus(true);
					VentanaVitrinaVenta.textCantidad.selectAll();
				}
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
						llenarServicios();
						int pos = textBus.getCaretPosition();textBus.setText(textBus.getText().toUpperCase());textBus.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
						if (e==KeyEvent.VK_ENTER || textBus.getText().toLowerCase().length()==30){
								filtrar();

						}
						
		
					}
			}

			public void keyTyped(KeyEvent evet) {
				// PRECIONA EL TECLADO Y ME DA EL EVENTO
				char car=evet.getKeyChar();
				if (evet.getSource() == textBus){
					if((car<'a' || car>'z') && (car<'A' || car>'Z')&&(car<' '||car>'.')&&(car<'0'||car>'9')) evet.consume();
				}
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
						if (FILTRO_SER==1) {
							descripcion=(String)tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
							VentanaConsumoAgregar.cbTipo.setSelectedItem(descripcion);
							this.frame.dispose();
							VentanaConsumoAgregar.textFormatCantidad.requestFocus(true);
							VentanaConsumoAgregar.textFormatCantidad.selectAll();
						}
						if (FILTRO_SER==2) {
							VentanaVitrinaVenta.textCod.setText(id);
							VentanaVitrinaVenta.textDescripcion.setText(descripcion);
							VentanaVitrinaVenta.textPrecio.setText(Float.toString(precio));
							frame.dispose();
							VentanaVitrinaVenta.textCantidad.requestFocus(true);
							VentanaVitrinaVenta.textCantidad.selectAll();
						}
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
		public void mousePressed(MouseEvent evt) {

		}
		@Override
		public void mouseReleased(MouseEvent Mouse_evt) {
			// TODO Auto-generated method stub
			id="";detalle="";
			descripcion="";precio=0;observacion="";
			if (Mouse_evt.getSource().equals(tableList)) {
				try {
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
					if (id==null || id == ""){
						tableList.requestFocus();
						return;
					}
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
					detalle=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim() +" "+ (String) tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
					descripcion=(String)tableList.getValueAt(tableList.getSelectedRow(),1).toString().trim();
					precio=Float.parseFloat(tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim().replaceAll(",", ""));
					observacion=tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {
						if (FILTRO_SER==1||FILTRO_SER==2) {
							if (FILTRO_SER==1) {
								VentanaConsumoAgregar.llenarcbTipo();
								VentanaConsumoAgregar.cbTipo.setSelectedItem(descripcion);
								frame.dispose();
								VentanaConsumoAgregar.textFormatCantidad.requestFocus(true);
								VentanaConsumoAgregar.textFormatCantidad.selectAll();
							}
							if (FILTRO_SER==2) {
								VentanaVitrinaVenta.textCod.setText(id);
								VentanaVitrinaVenta.textDescripcion.setText(descripcion);
								VentanaVitrinaVenta.textPrecio.setText(Float.toString(precio));
								frame.dispose();
								VentanaVitrinaVenta.textCantidad.requestFocus(true);
								VentanaVitrinaVenta.textCantidad.selectAll();
							}
						}else{
							modificarServicio();
						}
					}
					
					/*if (Mouse_evt.getClickCount() == 2) {
						if (FILTRO_SER==1) {
							filtrar();
						}else{
							modificarServicio();
						}
					}*/
				} catch (Exception e) {}
			}
		}
	


}
