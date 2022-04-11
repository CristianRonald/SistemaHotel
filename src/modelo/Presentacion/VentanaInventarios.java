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
import modelo.Otros.JTextFieldIcon;

public class VentanaInventarios implements ActionListener,FocusListener,KeyListener,PropertyChangeListener,MouseListener {
		private static ConexionDB conexion;
		public static JInternalFrame frame;
		private JPanel  panelBtn,panelLst;
		public  JLabel		lbl1,lbl9;
		public JTextField 			textBus;
		protected JButton  			buttonNuevo,buttonEditar,buttonEliminar,buttonSalir,buttonBus;
		JComboBox<String> cbBus,cbFamilia,cbEstado;
	    
		private JScrollPane scrollTable;
		private JTable tableList;
		private DefaultTableModel model;
		
		public Integer totalitems;
		public static int CONTAR_VENTANA_INVENTARIOS=0;
		public static String NOMBRE_ACTIVO;
		public static int FILTRO_INV;
		
		public static String id="",descripcion="",categoria="",id_Cate;
		
		private String consultar="";
		
		// constructor
		public VentanaInventarios() {
			super();
			
			frameInventarios();
			crearPanel();
			crearButtons();
			crearTable();
			crearComboBox();
			crearTextFields();
			crearLabels();
			panelLst.setVisible(true); // PANEL LISTA
			llenarcbEstado();
			llenarcbBuscar();
			llenarcbFamilia();
			
			llenarInventarios();
			
			CONTAR_VENTANA_INVENTARIOS ++;//AUMENTAMOS EL CONTEO DE LAS VENTANAS.
			
			Menu.Desktop.add(frame);
	        int x = (Menu.Desktop.getWidth () / 2) - frame.getWidth () / 2;
	        int y = (Menu.Desktop.getHeight () / 2) - frame.getHeight () / 2;
	    	frame.setLocation(x, y);
		}
		public void frameInventarios() {
			frame = new JInternalFrame();
			frame.setFrameIcon(new ImageIcon(VentanaInventarios.class.getResource("/modelo/Images/folder-search.png")));
			frame.setTitle("Inventarios");
			frame.addInternalFrameListener(new InternalFrameAdapter() {
				@Override
				public void internalFrameActivated(InternalFrameEvent e) {
					llenarInventarios();
				}
				//@Override
				public void internalFrameClosed(InternalFrameEvent arg0) {
					CONTAR_VENTANA_INVENTARIOS=0;
				}
			});
			frame.setClosable(true);
			frame.setBounds(100, 100, 811, 389);
			frame.getContentPane().setLayout(null);
		}
		public void crearPanel() {
			panelBtn = new JPanel();
			panelBtn.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "| Search |", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelBtn.setBounds(10, 295, 775, 49);
			frame.getContentPane().add(panelBtn);
			panelBtn.setLayout(null);
			
			panelLst = new JPanel();
			panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelLst.setBounds(10, 11, 775, 283); //10, 333, 659, 268
			frame.getContentPane().add(panelLst);
			panelLst.setLayout(null);
			
		}
		void LimpiarTable(){
			try {do {model.removeRow(0);} while (tableList.getRowCount() !=0);}catch (Exception e) {}
		}
		public void llenarInventarios () {
			id="";descripcion="";
			int CANT=0;
			conexion = new ConexionDB();
			//LimpiarTable();
	        try {
	            // COLOREA TODO EL JTABLE
	          // tableList.setDefaultRenderer (Object.class, new MiRender());
	     	   totalitems=0;
	     	  if (cbFamilia.getSelectedItem()=="%TODOS") {
		     	   if (textBus.getText().isEmpty()) {
		     		  consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'";
		     	   }else{
						if(cbBus.getSelectedItem()=="CODIGO") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and INVENTARIOS.Id_Inv ='" +textBus.getText()+"'";
						}
						if(cbBus.getSelectedItem()=="CATEGORIA") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and CATEGORIA.DescripcionCat like'" +textBus.getText()+"%'";
						}
						if(cbBus.getSelectedItem()=="DESCRIPCION") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and INVENTARIOS.DescripcionInv like'" +textBus.getText()+"%'";
						}
						if(cbBus.getSelectedItem()=="MARCA") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and INVENTARIOS.MarcaInv like'" +textBus.getText()+"%'";
						}
						if(cbBus.getSelectedItem()=="COSTO") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and INVENTARIOS.CostoInv like'" +textBus.getText()+"%'";
						}
						if(cbBus.getSelectedItem()=="PRECIO") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and INVENTARIOS.PrecioInv like'" +textBus.getText()+"%'";
						}
						if(cbBus.getSelectedItem()=="ESTADO") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and INVENTARIOS.EstadoInv like'" +textBus.getText()+"%'";
						}
		     	   }
	     	  }
	     	  if (cbFamilia.getSelectedItem()!="%TODOS") {
		     	   if (textBus.getText().isEmpty()) {
		     		  consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and CATEGORIA.FamiliaCat ='" + cbFamilia.getSelectedItem() +"'";
		     	   }else{
						if(cbBus.getSelectedItem()=="CODIGO") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.Id_Inv ='" +textBus.getText()+"'and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and CATEGORIA.FamiliaCat ='" + cbFamilia.getSelectedItem() +"'";
						}
						if(cbBus.getSelectedItem()=="CATEGORIA") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and CATEGORIA.DescripcionCat like'" +textBus.getText()+"%'and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and CATEGORIA.FamiliaCat ='" + cbFamilia.getSelectedItem() +"'";
						}
						if(cbBus.getSelectedItem()=="DESCRIPCION") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.DescripcionInv like'" +textBus.getText()+"%'and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and CATEGORIA.FamiliaCat ='" + cbFamilia.getSelectedItem() +"'";
						}
						if(cbBus.getSelectedItem()=="MARCA") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.MarcaInv like'" +textBus.getText()+"%'and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and CATEGORIA.FamiliaCat ='" + cbFamilia.getSelectedItem() +"'";
						}
						if(cbBus.getSelectedItem()=="COSTO") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.CostoInv like'" +textBus.getText()+"%'and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and CATEGORIA.FamiliaCat ='" + cbFamilia.getSelectedItem() +"'";
						}
						if(cbBus.getSelectedItem()=="PRECIO") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.PrecioInv like'" +textBus.getText()+"%'and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and CATEGORIA.FamiliaCat ='" + cbFamilia.getSelectedItem() +"'";
						}
						if(cbBus.getSelectedItem()=="ESTADO") {
							consultar="Select * from INVENTARIOS,CATEGORIA where CATEGORIA.Id_Cat=INVENTARIOS.Id_Cat and INVENTARIOS.EstadoInv like'" +textBus.getText()+"%'and INVENTARIOS.EstadoInv ='" + cbEstado.getSelectedItem() +"'and CATEGORIA.FamiliaCat ='" + cbFamilia.getSelectedItem() +"'";
						}
		     	   }
	     	  }
	     	   Statement st = conexion.gConnection().createStatement();
	 		   ResultSet rs=st.executeQuery(consultar);
	 		   
			   model= new DefaultTableModel();
			   model.addColumn("Código");
			   model.addColumn("");//Tipo
		       model.addColumn("Categoria");
			   model.addColumn("Descripción");
			   model.addColumn("Costo. S/.");
			   model.addColumn("Precio S/.");
		       model.addColumn("S-M");
		       model.addColumn("Stock");
		       model.addColumn("F.Actualizo");
		       model.addColumn("Estado");
		       
		 		   String []datos= new String[10];
		 		   tableList.setModel(model);
		     	  
		           while(rs.next()) {
		            datos[0]=" "+Menu.formatid_7.format(rs.getInt("Id_Inv"));
		            datos[1]=" "+rs.getString("FamiliaCat");
		            datos[2]=" "+rs.getString("DescripcionCat");
		            datos[3]=" "+rs.getString("DescripcionInv");// +" " +rs.getString("MarcaInv");
		            datos[4]=" "+Menu.formateadorCurrency.format(rs.getFloat("CostoInv"));
		            datos[5]=" "+Menu.formateadorCurrency.format(rs.getFloat("PrecioInv"));
		            datos[6]=" "+rs.getString("StockMinimoInv");
		            datos[7]=" "+rs.getString("StockInv");
		            datos[8]=" "+Menu.formatoFechaString.format(rs.getDate("FechaActualizarInv"))+"  "+ Menu.formatoTimeString.format(rs.getTime("FechaActualizarInv"));
		            datos[9]=" "+rs.getString("EstadoInv");
		            
		            totalitems=totalitems+1;
		            model.addRow(datos);
		            tableList.setModel(model);
		            
		            CANT= CANT + rs.getInt("StockInv");
			        }
		           st.close();
		           
		           // LLENO EL ESPACIO
		    	   if (totalitems>0) {
			           String []d= new String[10];
			 		   tableList.setModel(model);
			   
			            d[3]="=====================================";
			            d[4]="======";
			            d[5]="====";
			            d[7]="======";
			            model.addRow(d);
			            tableList.setModel(model);
			            
			           // LLENO RESULTADOS
			           String []dato= new String[9];
			 		   tableList.setModel(model);
			   
			            dato[3]=" TOTAL STOCK ===> ";
			            dato[4]=" ";
			            dato[7]=" " + 	CANT;
			            model.addRow(dato);
			            tableList.setModel(model);
		 		   }
		           // MODELO TABLE
		    	   int CONT=12;
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
					   CONT=CONT+2;
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
	           
	           tableList.getColumnModel().getColumn(4).setCellRenderer(modeloRight);
	           tableList.getColumnModel().getColumn(5).setCellRenderer(modeloRight);
	           tableList.getColumnModel().getColumn(6).setCellRenderer(modeloRight);
	           tableList.getColumnModel().getColumn(7).setCellRenderer(modeloRight);
	           //tableList.getColumnModel().getColumn(8).setCellRenderer(modelocentrar);
		           
	           tableList.getColumnModel().getColumn(1).setMinWidth(0);
	           tableList.getColumnModel().getColumn(1).setMaxWidth(0);
	           
	     	   tableList.getColumnModel().getColumn(0).setPreferredWidth(60);
	     	   //tableList.getColumnModel().getColumn(1).setPreferredWidth(0);
	     	   tableList.getColumnModel().getColumn(2).setPreferredWidth(150);
	     	   tableList.getColumnModel().getColumn(3).setPreferredWidth(280);
	     	   tableList.getColumnModel().getColumn(4).setPreferredWidth(70);
	     	   tableList.getColumnModel().getColumn(5).setPreferredWidth(70);
	     	   tableList.getColumnModel().getColumn(6).setPreferredWidth(30);
	     	   tableList.getColumnModel().getColumn(7).setPreferredWidth(50);
	     	   tableList.getColumnModel().getColumn(8).setPreferredWidth(90);
	     	   tableList.getColumnModel().getColumn(9).setPreferredWidth(5);
	           panelLst.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Lista: total ítems "+totalitems));
			} catch (SQLException e) {
				//JOptionPane.showMessageDialog(null, "Error al llenar la table " + Menu.separador + e,Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
			}
		}
		public void crearLabels() {
			lbl9= new JLabel("Filtrar por:");
			lbl9.setBounds(233, 7, 96, 14);
			panelBtn.add(lbl9);
			lbl9.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl9.setFont(Menu.fontLabel);
			
			lbl1= new JLabel("Tipo inventario:");
			lbl1.setBounds(78, 7, 82, 14);
			panelBtn.add(lbl1);
			lbl1.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl1.setFont(Menu.fontLabel);
		}
		public void crearTextFields(){
			textBus =  new  JTextFieldIcon(new JTextField(),"searchBlue.png","Search","searchBlue.png");
			textBus.setColumns(10);
			textBus.setFont(Menu.fontText);
			textBus.setForeground(Menu.textColorForegroundInactivo);
			textBus.setHorizontalAlignment(SwingConstants.LEFT);
			textBus.addActionListener(this);
			textBus.addFocusListener(this);
			textBus.addKeyListener(this);
			textBus.addPropertyChangeListener(this);
			textBus.setBounds(333, 21, 236, 22);
			panelBtn.add(textBus);
			textBus.addFocusListener(this);
		}
		public void crearComboBox() {
			cbEstado= new JComboBox<>();
			cbEstado.setBounds(10, 21, 51, 21);
			cbEstado.setFont(Menu.fontText);
			cbEstado.removeAllItems();
			cbEstado.addActionListener(this);
			cbEstado.addFocusListener(this);
			cbEstado.addKeyListener(this);
	        panelBtn.add(cbEstado);
	        
			cbFamilia= new JComboBox<>();
			cbFamilia.setBounds(64, 21, 96, 21);
			cbFamilia.setFont(Menu.fontText);
			cbFamilia.removeAllItems();
			cbFamilia.addActionListener(this);
			cbFamilia.addFocusListener(this);
			cbFamilia.addKeyListener(this);
	        panelBtn.add(cbFamilia);
		        
			cbBus = new JComboBox<>();
	        cbBus.setBounds(163, 21, 166, 21);
	        cbBus.setFont(Menu.fontText);
	        cbBus.removeAllItems();
	        cbBus.addFocusListener(this);
	        cbBus.addKeyListener(this);
	        panelBtn.add(cbBus);
	        
		}
		public void llenarcbEstado() {
	        cbEstado.removeAllItems();
			String [] lista1 = {"A","X"};
			for (String llenar:lista1) {
				cbEstado.addItem(llenar);
			}
			cbEstado.setSelectedIndex(0);
		}
		public void llenarcbBuscar() {
	        cbBus.removeAllItems();
			String [] lista1 = {"CODIGO","CATEGORIA","DESCRIPCION","MARCA","COSTO","PRECIO"};
			for (String llenar:lista1) {
				cbBus.addItem(llenar);
			}
			cbBus.setSelectedIndex(2);
		}
		public void llenarcbFamilia() {
			/*conexion = new ConexionDB();
			cbFamilia.removeAllItems();
			try {
				cbFamilia.addItem("%TODOS");
				String con = "Select * from CATEGORIA";
				Statement st = conexion.gConnection().createStatement();
				ResultSet rs = st.executeQuery(con);
				while (rs.next()==true) {
					cbFamilia.addItem(rs.getString("DescripcionCat"));
				}
				cbFamilia.setSelectedIndex(2);
				st.close();
			} catch (Exception e) {}*/
			cbFamilia.removeAllItems();
			cbFamilia.addItem("%TODOS");
			cbFamilia.addItem("INV_HOTEL");
			cbFamilia.addItem("VITRINA");
			cbFamilia.setSelectedIndex(2);
		}
		public void crearButtons() {
			buttonNuevo= new JButton("");
			buttonNuevo.setToolTipText("Agregar ítem");
			buttonNuevo.addActionListener(this);
			buttonNuevo.setBounds(615, 20, 36, 23);
			buttonNuevo.setIcon(new ImageIcon(VentanaInventarios.class.getResource("/modelo/Images/nuevo.png")));
			panelBtn.add(buttonNuevo);

			buttonEditar= new JButton("");
			buttonEditar.setToolTipText("Modificar ítem");
			buttonEditar.addActionListener(this);
			buttonEditar.setBounds(653, 20, 36, 23);
			buttonEditar.setIcon(new ImageIcon(VentanaInventarios.class.getResource("/modelo/Images/edit.png")));
			panelBtn.add(buttonEditar);
			
			buttonEliminar= new JButton("");
			buttonEliminar.setToolTipText("Eliminar ítem");
			buttonEliminar.addActionListener(this);
			buttonEliminar.setBounds(691, 20, 36, 23);
			buttonEliminar.setIcon(new ImageIcon(VentanaInventarios.class.getResource("/modelo/Images/delete.png")));
			panelBtn.add(buttonEliminar);
			
			buttonSalir= new JButton("");
			buttonSalir.setToolTipText("Salir");
			buttonSalir.addActionListener(this);
			buttonSalir.setBounds(729, 20, 36, 23);
			buttonSalir.setIcon(new ImageIcon(VentanaInventarios.class.getResource("/modelo/Images/Exit.png")));
			panelBtn.add(buttonSalir);
			
			buttonBus= new JButton("");
			buttonBus.setBounds(569, 20, 36, 23);
			buttonBus.setToolTipText("Aceptar");
			buttonBus.addActionListener(this);
			buttonBus.setIcon(new ImageIcon(VentanaInventarios.class.getResource("/modelo/Images/ok.png")));
			panelBtn.add(buttonBus);
		}
		public void crearTable(){
			tableList = new JTable(); 
			tableList.setBackground(new Color(248, 248, 255));
			tableList.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			tableList.setBounds(10, 303, 665, 229);
			tableList.addMouseListener(this);
			tableList.addKeyListener(this);
			
			scrollTable = new JScrollPane();
			scrollTable.setViewportView(tableList);
			scrollTable.setBounds(10, 25, 755, 247);
	        panelLst.add(scrollTable);

	        tableList.setGridColor(new Color(238, 232, 170));
		}
		
		public void limpiarTexts() {
			textBus.setText(null);
			textBus.setBackground(Menu.textColorBackgroundInactivo);	
			textBus.setForeground(Menu.textColorForegroundInactivo);
			
			cbBus.setSelectedIndex(2);
		}
		public void actionPerformed(ActionEvent evento) {
			  if (evento.getSource() == buttonNuevo){// NUEVO
				  VentanaInventariosAgregar.MOD=0;// PERMITE REGISTRAR 
				  VentanaInventariosAgregar ven= new VentanaInventariosAgregar();
				    ven.frame.toFront();
				    ven.frame.setVisible(true);
				    VentanaInventariosAgregar.cbTipo.requestFocus(true);
				    
				    VentanaInventariosAgregar.cbFamilia.removeAllItems();
				    String familia = (String)cbFamilia.getSelectedItem();
				    VentanaInventariosAgregar.cbFamilia.addItem(familia);
				    VentanaInventariosAgregar.cbFamilia.setSelectedItem(cbFamilia.getSelectedItem());
				    ven.panelDto.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datos " , TitledBorder.LEADING, TitledBorder.TOP, null,( new Color(106, 90, 205))));

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
				  try {
					  id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
					  //tableList.selectAll();
					  filtrar();
				  	} catch (Exception e) {}
				  }
			  if (evento.getSource() == cbEstado){
				  	llenarInventarios();
				  }
			  if (evento.getSource() == cbFamilia){
				  	limpiarTexts();
				  	llenarInventarios();
				  }
			}
			void modificarArticulo() {
				VentanaInventariosAgregar.MOD=1;// PERMITE MODIFICAR
				VentanaInventariosAgregar ven= new VentanaInventariosAgregar();
				ven.frame.toFront();
				ven.frame.setVisible(true);
				ven.textCod.setText(id);
				
			    VentanaInventariosAgregar.cbFamilia.removeAllItems();
			    String familia = (String)cbFamilia.getSelectedItem();
			    VentanaInventariosAgregar.cbFamilia.addItem(familia);
			    VentanaInventariosAgregar.cbTipo.setSelectedItem(categoria);
			    
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
				try {
					conexion = new ConexionDB();
					String query ="Select * from INVENTARIO_HABITACION as IH,INVENTARIOS as I where IH.Id_Inv=I.Id_Inv and IH.Id_Inv='" + Integer.parseInt(id)  + "'";
					Statement statement =  conexion.gConnection().createStatement();
					ResultSet rs=statement.executeQuery(query);
					if (rs.next()==true) {
						JOptionPane.showMessageDialog(null, "No se permite eliminar el ítem " + id +" "+ descripcion   +" "+ Menu.separador + "porque ha sido asignado a una o más habitaciones ...!" ,Menu.SOFTLE_HOTEL,JOptionPane.ERROR_MESSAGE);
						rs.close();statement.close();
						return;
					}
					rs.close();statement.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				int respuesta = JOptionPane.showConfirmDialog (null, "¿Desea eliminar el artículo?" + Menu.separador + id + " " +  descripcion, Menu.SOFTLE_HOTEL,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (respuesta == JOptionPane.YES_OPTION) {
					try {
						conexion = new ConexionDB();
						Statement statement =  conexion.gConnection().createStatement();
						String query ="Delete from INVENTARIOS where Id_Inv='" + id  + "'";
						statement.execute(query);
						JOptionPane.showMessageDialog(null, "El ítem fue eliminado de forma correcta...!",Menu.SOFTLE_HOTEL,JOptionPane.INFORMATION_MESSAGE);
						statement.close();
						llenarInventarios();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}else if (respuesta == JOptionPane.NO_OPTION) {}
			}
			public void filtrar(){
				try {
					if (FILTRO_INV==1) { //VENTANA AGREGAR CONSUMOS
						VentanaConsumoAgregar.llenarcbTipo();
						VentanaConsumoAgregar.cbTipo.setSelectedItem(descripcion);
						VentanaConsumoAgregar.COSTO = (Float.parseFloat(tableList.getValueAt(tableList.getSelectedRow(),4).toString()));
						frame.dispose();
						VentanaConsumoAgregar.textFormatCantidad.requestFocus(true);
						VentanaConsumoAgregar.textFormatCantidad.selectAll();
					}
					if (FILTRO_INV == 2){	//VENTANA VITRINA VENTAS
						categoria=(String)tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim();
						descripcion=(String)tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim();
						//VentanaVitrinaVenta v = new VentanaVitrinaVenta();
						VentanaVitrinaVenta.textCod.setText(tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim());
						VentanaVitrinaVenta.textDescripcion.setText(descripcion);
						VentanaVitrinaVenta.textPrecio.setText(tableList.getValueAt(tableList.getSelectedRow(),5).toString().trim());
						VentanaVitrinaVenta.COSTO = (Float.parseFloat(tableList.getValueAt(tableList.getSelectedRow(),4).toString()));
						frame.dispose();
						VentanaVitrinaVenta.textCantidad.requestFocus(true);
						VentanaVitrinaVenta.textCantidad.selectAll();
					}
					if (FILTRO_INV == 3){	//VENTANA VITRINA COMPRAS
						categoria=(String)tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim();
						descripcion=(String)tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim();
						//VentanaVitrinaCompra v = new VentanaVitrinaVenta();
						VentanaVitrinaCompra.textCod.setText(tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim());
						VentanaVitrinaCompra.textDescripcion.setText(descripcion);
						VentanaVitrinaCompra.textPrecioC.setText(tableList.getValueAt(tableList.getSelectedRow(),4).toString().trim());
						VentanaVitrinaCompra.textPrecioV.setText(tableList.getValueAt(tableList.getSelectedRow(),5).toString().trim());
						frame.dispose();
						VentanaVitrinaCompra.textCantidad.requestFocus(true);
						VentanaVitrinaCompra.textCantidad.selectAll();
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
						llenarInventarios();
						int pos = textBus.getCaretPosition();textBus.setText(textBus.getText().toUpperCase());textBus.setCaretPosition(pos);// LINEA DE CODIGO PARA MAYUSCULAS
						if (e==KeyEvent.VK_ENTER || textBus.getText().toLowerCase().length()==30){
							tableList.selectAll();
							filtrar();
						}
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
						id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
						descripcion=(String)tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim();
						filtrar();
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
			id="";descripcion="";categoria="";
			try {
				if (Mouse_evt.getSource().equals(tableList)) {
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0);
					if (id==null || id == ""){
						tableList.requestFocus();
						return;
					}
					id=(String) tableList.getValueAt(tableList.getSelectedRow(),0).toString().trim();
					categoria=(String)tableList.getValueAt(tableList.getSelectedRow(),2).toString().trim();
					descripcion=(String) tableList.getValueAt(tableList.getSelectedRow(),3).toString().trim();
					if (Mouse_evt.getClickCount() == 2) {	
						if (FILTRO_INV==1 || FILTRO_INV==2 || FILTRO_INV==3) {
							filtrar();
						}else{
							modificarArticulo();
						}
					}
				}
			} catch (Exception e) {}
		}
}
